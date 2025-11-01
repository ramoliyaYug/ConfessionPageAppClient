package com.example.confessionpageapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.confessionpageapplication.ui.theme.ConfessionpageapplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConfessionpageapplicationTheme {
                var currentScreen by remember { mutableStateOf("list") }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Confession Page",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        currentScreen = if (currentScreen == "list") "post" else "list"
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (currentScreen == "list") Icons.Default.Add else Icons.Default.List,
                                        contentDescription = if (currentScreen == "list") "Post Confession" else "View Confessions"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        if (currentScreen == "list") {
                            ConfessionScreen()
                        } else {
                            PostConfessionScreen()
                        }
                    }
                }
            }
        }
    }
}