package com.itinerary.feature.map.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itinerary.core.common.Constants
import com.itinerary.core.designsystem.theme.RatingGold

@Composable
fun AddMarkerDialog(
    onConfirm: (String, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ranking by remember { mutableIntStateOf(Constants.DEFAULT_RANKING) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Ponto de Interesse") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do local") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(
                        text = "Nível de interesse",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { index ->
                            IconButton(
                                onClick = { ranking = index }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Ranking $index",
                                    modifier = Modifier.size(32.dp),
                                    tint = if (index <= ranking) {
                                        RatingGold
                                    } else {
                                        MaterialTheme.colorScheme.outline
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    if (name.isNotBlank()) {
                        onConfirm(name, ranking)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
