package com.example.bersihkan

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.bersihkan.ui.BersihKanApp
import com.example.bersihkan.ui.BaseViewModel
import com.example.bersihkan.ui.MainViewModel
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        val apiKey = BuildConfig.MAPS_TOKEN
        Places.initialize(applicationContext, apiKey)

        setContent {
            BersihKanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BersihKanApp()
                }
            }
        }
    }
}