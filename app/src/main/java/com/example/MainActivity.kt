package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainHalalScreen
import com.example.ui.theme.HalalKontrolTheme
import com.example.ui.viewmodel.HalalScannerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HalalScannerViewModel by viewModels()

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
    }
}
