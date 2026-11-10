package com.example

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.MainHalalScreen
import com.example.ui.theme.HalalKontrolTheme
import com.example.ui.platform.LocalSharer
import com.example.ui.platform.AndroidSharer
import androidx.compose.runtime.CompositionLocalProvider
import com.example.ui.viewmodel.HalalScannerViewModel
import com.example.ui.viewmodel.HalalScannerViewModelFactory
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory

class MainActivity : ComponentActivity() {

    private val viewModel: HalalScannerViewModel by viewModels { HalalScannerViewModelFactory(application) }

    // Play In-App Update (testing-only scaffolding, safe to remove after closed testing wraps
    // up): testers install from the Play testing-track link and previously only found out about
    // a new build if they manually revisited that link. This forces a blocking IMMEDIATE update
    // prompt on launch/resume instead. No-ops silently on non-Play installs (debug builds, side-
    // loaded APKs), so it's harmless to leave in during dev.
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { /* RESULT_CANCELED etc. just get re-offered on the next onResume check */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // App screens render a fixed light/warm background regardless of system dark mode
        // (most components hardcode NaturalWarmBg-family colors instead of reading
        // MaterialTheme.colorScheme), so the status/nav bar icons must stay dark - the
        // default auto() style follows the system's dark-mode setting instead and can pick
        // light icons that disappear against this app's always-light background.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            CompositionLocalProvider(LocalSharer provides AndroidSharer(this)) {
            HalalKontrolTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainHalalScreen(viewModel = viewModel)
                }
                val reviewRequested by viewModel.reviewRequest.collectAsStateWithLifecycle()
                LaunchedEffect(reviewRequested) {
                    if (reviewRequested) {
                        viewModel.onReviewRequestHandled()
                        launchReviewFlow()
                    }
                }
            }
            }
        }
        checkForImmediateUpdate()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener(this) { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startImmediateUpdate(info)
            }
        }
    }

    private fun checkForImmediateUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener(this) { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startImmediateUpdate(info)
            }
        }
    }

    private fun startImmediateUpdate(appUpdateInfo: AppUpdateInfo) {
        // Listeners are activity-scoped (removed on stop), but onCreate's and onResume's lookups can
        // still both start the flow; the second start throws IllegalStateException (Play vitals,
        // 2.0), and the first flow is already showing, so that one is safe to ignore.
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateResultLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        } catch (e: IllegalStateException) {
            Log.w("MainActivity", "Update flow already started", e)
        }
    }

    // Play In-App Review: Google decides whether the dialog is actually shown (quota), and gives
    // no result either way, so failures are just logged. No-ops on non-Play installs.
    private fun launchReviewFlow() {
        val reviewManager = ReviewManagerFactory.create(this)
        reviewManager.requestReviewFlow().addOnCompleteListener(this) { request ->
            when {
                !request.isSuccessful -> Log.w("MainActivity", "Review flow unavailable", request.exception)
                !lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) ->
                    Log.i("MainActivity", "Review flow skipped: activity not in foreground")
                else -> reviewManager.launchReviewFlow(this, request.result)
            }
        }
    }
}
