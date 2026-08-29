package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainHalalScreen
import com.example.ui.theme.HalalKontrolTheme
import com.example.ui.viewmodel.HalalScannerViewModel
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : ComponentActivity() {

    private val viewModel: HalalScannerViewModel by viewModels()

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
            HalalKontrolTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainHalalScreen(viewModel = viewModel)
                }
            }
        }
        checkForImmediateUpdate()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startImmediateUpdate(info)
            }
        }
    }

    private fun checkForImmediateUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startImmediateUpdate(info)
            }
        }
    }

    private fun startImmediateUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            updateResultLauncher,
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
        )
    }
}
