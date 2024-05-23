package com.codetutor.countryinfoapp.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNewAlertDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    message: String,
    currentCapital: String,
    positiveAction: (String) -> Unit
) {
    var newCapital by remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text = title, fontSize = 30.sp)
            },
            text = {
                Column {
                    Text(message,fontSize = 25.sp)
                    Text("Current Country Capital: $currentCapital", fontSize = 20.sp)
                    OutlinedTextField(
                        value = newCapital,
                        onValueChange = { newCapital = it },
                        label = { Text("New Capital") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        positiveAction(newCapital)
                        showDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}