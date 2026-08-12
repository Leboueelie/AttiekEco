package com.attiekeco.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscriptionProductriceScreen(
    onInscriptionComplete: (utilisateurId: Long) -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var nom by rememberSaveable { mutableStateOf("") }
    var telephone by rememberSaveable { mutableStateOf("") }
    var localisation by rememberSaveable { mutableStateOf("") }
    var commune by rememberSaveable { mutableStateOf("") }
    var cooperative by rememberSaveable { mutableStateOf("") }

    val telephoneComplet = "+225 $telephone"
    val telephoneValide = Regex("""^\d{2}\s?\d{2}\s?\d{2}\s?\d{2}\s?\d{2}$""").matches(telephone)
    val isValid = nom.isNotBlank() && telephoneValide && localisation.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Inscription Productrice") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { padding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informations personnelles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Orange40
            )

            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom complet *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telephone,
                onValueChange = { telephone = it },
                label = { Text("Numéro de téléphone *") },
                placeholder = { Text("XX XX XX XX") },
                prefix = { Text("+225 ") },
                singleLine = true,
                isError = telephone.isNotEmpty() && !telephoneValide,
                supportingText = {
                    if (telephone.isNotEmpty() && !telephoneValide) {
                        Text("10 chiffres requis")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = localisation,
                onValueChange = { localisation = it },
                label = { Text("Localisation de l'unité *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = commune,
                onValueChange = { commune = it },
                label = { Text("Commune") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cooperative,
                onValueChange = { cooperative = it },
                label = { Text("Coopérative (optionnel)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.inscrireProductrice(
                        nom = nom,
                        telephone = telephoneComplet,
                        localisation = localisation,
                        commune = commune.ifBlank { null },
                        cooperative = cooperative.ifBlank { null }
                    ) { utilisateurId ->
                        onInscriptionComplete(utilisateurId)
                    }
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Orange40)
            ) {
                Text(text = "S'inscrire", color = Color.White)
            }
        }
    }
}
