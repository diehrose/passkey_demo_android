package com.pinpin.passkey_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinpin.passkey_demo.data.model.Credential

@Composable
fun PasskeyManagementScreen(
    viewModel: PasskeyViewModel
) {
    var username by remember {
        mutableStateOf("test@example.com")
    }

    var deleteTarget by remember {
        mutableStateOf<Credential?>(null)
    }

    val credentials by viewModel.credentials.collectAsState()

    val isLoading by
    viewModel.isCredentialLoading.collectAsState()

    val message by
    viewModel.credentialMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Passkey Management"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text("Username")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                viewModel.loadCredentials(username)
            },
            enabled = !isLoading &&
                    username.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Passkeys")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Passkeys (${credentials.size})"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        message?.let {
            Text(
                text = it
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        if (isLoading) {
            CircularProgressIndicator()

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        if (!isLoading && credentials.isEmpty()) {
            Text(
                text = "目前沒有 Passkey"
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = credentials,
                key = {
                    it.id
                }
            ) { credential ->

                CredentialItem(
                    credential = credential,
                    onDelete = {
                        deleteTarget = credential
                    }
                )
            }
        }
    }

    // Delete confirmation
    deleteTarget?.let { credential ->

        AlertDialog(
            onDismissRequest = {
                deleteTarget = null
            },
            title = {
                Text("Delete Passkey?")
            },
            text = {
                Text(
                    "確定要刪除這個 Passkey 嗎？"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        viewModel.deleteCredential(
                            username = username,
                            credentialId =
                                credential.credential_id
                        )

                        deleteTarget = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteTarget = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CredentialItem(
    credential: Credential,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Credential #${credential.id}"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Credential ID"
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = shortenCredentialId(
                    credential.credential_id
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Counter: ${credential.counter}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.End
            ) {
                TextButton(
                    onClick = onDelete
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

private fun shortenCredentialId(
    credentialId: String
): String {
    if (credentialId.length <= 20) {
        return credentialId
    }

    return "${credentialId.take(8)}..." +
            credentialId.takeLast(8)
}