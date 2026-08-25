package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
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
        enableEdgeToEdge()
        setContent {
            HalalKontrolTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainHalalScreen(viewModel = viewModel)
                }
            }
        }
    }
}
