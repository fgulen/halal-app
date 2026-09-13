package com.example.ui.camera

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.data.analyzer.HalalAnalyzer
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.Locale

/**
 * Launches the system camera app to take a single full-resolution photo (not the continuous
 * CameraX barcode-scan preview used elsewhere), then runs it through on-device ML Kit text
 * recognition. [onTextRecognized] always fires on a successful capture - with an empty string
 * if OCR found nothing, letting the caller fall back to manual entry. [onCancelled] fires only
 * if the user backs out of the camera without taking a photo.
 *
 * Returns a launch function; call it from a button's onClick.
 */
@Composable
fun rememberPhotoIngredientsCaptureLauncher(
    onTextRecognized: (String) -> Unit,
    onCancelled: () -> Unit
): () -> Unit {
    val context = LocalContext.current
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val uri = pendingUri
        pendingUri = null
        if (!success || uri == null) {
            onCancelled()
        } else {
            recognizeIngredientsText(context, uri, onTextRecognized)
        }
    }

    return {
        val uri = createPhotoCaptureUri(context)
        pendingUri = uri
        launcher.launch(uri)
    }
}

private fun createPhotoCaptureUri(context: Context): Uri {
    val dir = File(context.cacheDir, "photo_analysis").apply { mkdirs() }
    val file = File(dir, "ingredients_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

private fun recognizeIngredientsText(
    context: Context,
    uri: Uri,
    onTextRecognized: (String) -> Unit
) {
    try {
        val image = InputImage.fromFilePath(context, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText -> onTextRecognized(extractIngredientsSection(visionText)) }
            .addOnFailureListener { onTextRecognized("") }
    } catch (e: Exception) {
        onTextRecognized("")
    }
}

// Phrases that mark the end of the ingredients section on real packaging (storage/shelf-life
// instructions, manufacturer address, customer service line) - once one of these shows up,
// every following block is footer/legal boilerplate, never more ingredients.
private val SECTION_END_MARKERS = listOf(
    "aufbewahren", "haltbar", "verbraucherservice", "consumer service", "referenzmenge",
    "hergestellt", "produced by", "best before", "keep refrigerated", "storage instructions",
    "raf ömrü", "üretim yeri", "saklama koşulları", "tüketim tarihi"
)

/**
 * True for a block that can't be a continuation of the ingredients list: a nutrition-table cell
 * (number-only, or naming a nutrient), or known storage/address/legal footer text. Digit-vs-letter
 * ratio (not a marker word list) is what actually catches nutrition-table cells: OCR'd rows like
 * "6", "20.5g", "142 kcal" carry almost no letters, whereas a real ingredient fragment - even a
 * percentage-heavy one like "49,5 % (Zucker, Palmöl" - is still letter-dominant.
 */
private fun isSectionBoundary(text: String): Boolean {
    val lower = text.lowercase(Locale.ROOT)
    if (SECTION_END_MARKERS.any { lower.contains(it) }) return true
    if (HalalAnalyzer.NUTRITION_TABLE_MARKERS.any { lower.contains(it) }) return true
    val letters = text.count { it.isLetter() }
    val digits = text.count { it.isDigit() }
    return digits > 0 && letters < 3
}

/**
 * ML Kit groups recognized text into blocks that roughly follow the package's visual layout
 * (one block per printed paragraph/box) - unlike the flattened [Text.getText] string, which
 * concatenates everything on the label (nutrition table, barcode caption, ingredients, storage
 * instructions, manufacturer address) into one blob with no section boundaries. Find the block
 * that starts the ingredients section by its "Zutaten/Ingredients/İçindekiler" header and collect
 * forward from there, stopping at the first block that looks like a different section (see
 * [isSectionBoundary]) rather than blindly appending every remaining block on the label - a
 * label's nutrition table and footer almost always follow the ingredients, never interleave with
 * them, so a single stop is enough. Falls back to the full flattened text when no ingredients
 * header block is found at all, so the nutrition-table safety net in HalalAnalyzer and the manual
 * review/edit step still catch it.
 */
private fun extractIngredientsSection(visionText: Text): String {
    val blocks = visionText.textBlocks
    val startIndex = blocks.indexOfFirst { block ->
        val lower = block.text.lowercase(Locale.ROOT)
        HalalAnalyzer.INGREDIENT_SECTION_MARKERS.any { lower.contains(it) }
    }
    if (startIndex == -1) return visionText.text

    val collected = StringBuilder()
    for (i in startIndex until blocks.size) {
        val blockText = blocks[i].text
        if (i != startIndex && isSectionBoundary(blockText)) break
        if (collected.isNotEmpty()) collected.append("\n")
        collected.append(blockText)
    }
    return collected.toString()
}
