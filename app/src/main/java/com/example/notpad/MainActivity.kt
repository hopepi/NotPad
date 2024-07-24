package com.example.notpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notpad.view.MainScreen
import com.example.notpad.view.NoteAddScreen
import com.example.notpad.view.NoteDetailsScreen
import com.example.notpad.view.theme.NotPadTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotPadTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main_note_screen"){

                    composable("main_note_screen"){
                        MainScreen(navController = navController)
                    }

                    composable("note_details_screen/{noteId}", arguments = listOf(
                        navArgument("noteId"){
                            type = NavType.IntType
                        }
                    )){
                        backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId")
                        if (noteId != null) {
                            NoteDetailsScreen(navController = navController, id = noteId)
                        }
                    }

                    composable("note_add_screen"){
                        NoteAddScreen(navController = navController)
                    }
                }
            }
        }

    }



}