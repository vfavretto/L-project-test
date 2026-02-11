package com.itinerary.feature.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTripDialog(
    onConfirm: (String, String?) -> Unit,
    onDismiss: () -> Unit
) {
    var tripName by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Viagem") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = tripName,
                    onValueChange = { tripName = it },
                    label = { Text("Nome da viagem") },
                    placeholder = { Text("Ex: Europa 2024") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL da imagem (opcional)") },
                    placeholder = { Text("https://...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (tripName.isNotBlank()) {
                        onConfirm(
                            tripName.trim(),
                            imageUrl.trim().ifBlank { null }
                        )
                    }
                },
                enabled = tripName.isNotBlank()
            ) {
                Text("Criar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
