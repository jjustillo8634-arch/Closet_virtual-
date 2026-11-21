package com.example.closet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.closet.ui.AgregarPrendaScreen
import com.example.closet.ui.ClosetScreen
import com.example.closet.ui.ClosetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                val vm: ClosetViewModel = viewModel()

                NavHost(navController = nav, startDestination = "closet") {
                    composable("closet") {
                        ClosetScreen(vm, onAgregar = { nav.navigate("agregar") })
                    }
                    composable("agregar") {
                        AgregarPrendaScreen(vm, onTerminar = { nav.popBackStack() })
                    }
                }
            }
        }
    }
}
