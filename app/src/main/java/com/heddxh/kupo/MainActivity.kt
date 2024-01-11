package com.heddxh.kupo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.heddxh.kupo.data.DataDB
import com.heddxh.kupo.data.RealNewsRepository
import com.heddxh.kupo.data.RealQuestsRepository
import com.heddxh.kupo.network.RealNetworkService
import com.heddxh.kupo.ui.home.HomeScreen
import com.heddxh.kupo.ui.home.HomeViewModel
import com.heddxh.kupo.ui.theme.KupoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            KupoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = this@MainActivity
                    HomeScreen(
                        homeViewModel = HomeViewModel(
                            RealNetworkService(),
                            RealQuestsRepository(DataDB.getDatabase(context).questItemDao()),
                            RealNewsRepository(DataDB.getDatabase(context).newsDao())
                        )
                    )
                }
            }
        }
    }
}



