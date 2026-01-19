package com.iregados.deckarr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iregados.deckarr.core.theme.DeckarrTheme
import com.iregados.deckarr.feature.navigation.main.MainNavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        enableEdgeToEdge()

        setContent {
            DeckarrTheme {
                MainNavigationRoot(
                    hideSplashScreen = {
                        keepSplashScreen = false
                    }
                )
            }
        }
    }
}