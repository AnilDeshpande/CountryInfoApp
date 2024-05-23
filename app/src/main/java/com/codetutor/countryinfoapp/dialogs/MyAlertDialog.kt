package com.codetutor.countryinfoapp.dialogs

import android.os.Bundle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.codetutor.countryinfoapp.data.Country

@Composable
fun MyAlertDialog(showDialog: MutableState<Boolean>, title: String, message: String, positiveAction: () -> Unit) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button
                showDialog.value = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Handle confirm button click
                        positiveAction()
                        showDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Handle dismiss button click
                        showDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}