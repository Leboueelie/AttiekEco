package com.attiekeco.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.attiekeco.ui.theme.AttiekGreenLight
import com.attiekeco.ui.theme.Orange40

@Composable
fun ConnexionScreen(
    role: String,
    onConnexionReussie: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val isEntreprise = role == "ENTREPRISE"

    var telephone by remember { mutableStateOf("") }
    var nomEntreprise by remember { mutableStateOf("") }
    var matricule by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val telephoneComplet = "+225 $telephone"
    val telephoneValide = telephone.isBlank() || Regex("""^\d{2}\s?\d{2}\s?\d{2}\s?\d{2}\s?\d{2}$""").matches(telephone)

    val roleLabel = if (isEntreprise) "Entreprise" else "Productrice"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Orange40
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Connexion $roleLabel",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Orange40
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isEntreprise) "Entrez le nom et le matricule de l'entreprise" else "Entrez votre numéro de téléphone",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isEntreprise) {
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Orange40,
                focusedBorderColor = Orange40,
                unfocusedBorderColor = Color.Gray
            )
            OutlinedTextField(
                value = nomEntreprise,
                onValueChange = {
                    nomEntreprise = it
                    errorMessage = null
                },
                label = { Text("Nom de l'entreprise") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = matricule,
                onValueChange = {
                    matricule = it
                    errorMessage = null
                },
                label = { Text("Matricule") },
                placeholder = { Text("Année-Numéro") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
        } else {
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Orange40,
                focusedBorderColor = Orange40,
                unfocusedBorderColor = Color.Gray
            )
            OutlinedTextField(
                value = telephone,
                onValueChange = {
                    telephone = it
                    errorMessage = null
                },
                label = { Text("Numéro de téléphone") },
                placeholder = { Text("XX XX XX XX") },
                prefix = { Text("+225 ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = telephone.isNotBlank() && !telephoneValide,
                    supportingText = {
                        if (telephone.isNotEmpty() && !telephoneValide) {
                            Text("10 chiffres requis")
                        }
                    },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isEntreprise) {
                    if (nomEntreprise.isBlank() || matricule.isBlank()) {
                        errorMessage = "Veuillez remplir tous les champs"
                        return@Button
                    }
                    viewModel.connecterEntreprise(nomEntreprise, matricule) { entrepriseId ->
                        if (entrepriseId != null) {
                            onConnexionReussie(entrepriseId)
                        } else {
                            errorMessage = "Aucun compte entreprise trouvé"
                        }
                    }
                } else {
                    if (telephone.isBlank()) {
                        errorMessage = "Veuillez entrer votre numéro de téléphone"
                        return@Button
                    }
                    if (!telephoneValide) {
                        errorMessage = "10 chiffres requis"
                        return@Button
                    }
                    viewModel.connecterProductrice(telephoneComplet) { productriceId ->
                        if (productriceId != null) {
                            onConnexionReussie(productriceId)
                        } else {
                            errorMessage = "Aucun compte productrice trouvé avec ce numéro"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Orange40)
        ) {
            Text(text = "Se connecter", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) {
            Text(text = "Retour", color = Orange40)
        }
    }
}
