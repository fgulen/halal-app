package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.theme.AppRadius
import com.example.ui.theme.AppTextSize
import com.example.ui.theme.HaramRed
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmSurface
import com.example.ui.theme.PopupBorder

// Shared confirmation for deleting the whole local scan history. Deleting is irreversible, so every
// entry point must go through this dialog.
@Composable
fun ClearHistoryDialog(
    language: AppLanguage,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = AppStrings.getClearHistory(language),
                fontWeight = FontWeight.Bold,
                fontSize = AppTextSize.Title,
                color = NaturalTextDark
            )
        },
        text = {
            Text(
                text = when (language) {
                    AppLanguage.EN -> "Are you sure you want to clear your local scan history? This action cannot be undone."
                    AppLanguage.DE -> "Möchten Sie Ihren gesamten Scan-Verlauf wirklich löschen?"
                    AppLanguage.FR -> "Êtes-vous sûr de vouloir effacer l'historique des scans?"
                    AppLanguage.TR -> "Tüm tarama geçmişinizi silmek istediğinizden emin misiniz? Bu işlem geri alınamaz."
                    AppLanguage.AR -> "هل أنت متأكد من رغبتك في مسح سجل الفحوصات كاملاً؟"
                },
                fontSize = AppTextSize.Body,
                color = NaturalTextMuted,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = HaramRed),
                shape = CircleShape
            ) {
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "Yes, Clear"
                        AppLanguage.DE -> "Ja, Löschen"
                        AppLanguage.FR -> "Oui, Effacer"
                        AppLanguage.TR -> "Evet, Temizle"
                        AppLanguage.AR -> "نعم، احذف"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "Cancel"
                        AppLanguage.DE -> "Abbrechen"
                        AppLanguage.FR -> "Annuler"
                        AppLanguage.TR -> "İptal"
                        AppLanguage.AR -> "إلغاء"
                    },
                    color = NaturalTextMuted,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        containerColor = NaturalWarmSurface,
        shape = RoundedCornerShape(AppRadius.Large),
        modifier = Modifier.border(1.dp, PopupBorder, RoundedCornerShape(AppRadius.Large))
    )
}
