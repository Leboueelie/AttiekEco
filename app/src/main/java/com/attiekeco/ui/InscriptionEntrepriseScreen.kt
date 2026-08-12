package com.attiekeco.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.attiekeco.data.TypeEntreprise
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscriptionEntrepriseScreen(
    onInscriptionComplete: (utilisateurId: Long) -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var nomEntreprise by rememberSaveable { mutableStateOf("") }
    var contactDigits by rememberSaveable { mutableStateOf("") }
    var matriculeAnnee by rememberSaveable { mutableStateOf("") }
    var matriculeNumero by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf(TypeEntreprise.LONO.name) }
    var zoneCollecte by rememberSaveable { mutableStateOf("") }
    var typeExpanded by rememberSaveable { mutableStateOf(false) }

    val contact = "+225 $contactDigits"
    val contactValide = Regex("""^\d{2}\s?\d{2}\s?\d{2}\s?\d{2}\s?\d{2}$""").matches(contactDigits)
    val matriculeComplet = "CI-$matriculeAnnee-$matriculeNumero"
    val matriculeValide = Regex("""^\d{4}$""").matches(matriculeAnnee) &&
            Regex("""^\d{3}$""").matches(matriculeNumero)
    val isValid = nomEntreprise.isNotBlank() && contactValide &&
            matriculeValide && zoneCollecte.isNotBlank()

    val typeLabels = mapOf(
        TypeEntreprise.LONO.name to "LONO",
        TypeEntreprise.COOPERATIVE.name to "Coopérative",
        TypeEntreprise.ONG.name to "ONG",
        TypeEntreprise.AUTRE.name to "Autre"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Inscription Entreprise") },
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
                text = "Informations de l'entreprise",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Orange40
            )

            OutlinedTextField(
                value = nomEntreprise,
                onValueChange = { nomEntreprise = it },
                label = { Text("Nom de l'entreprise *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contactDigits,
                onValueChange = { contactDigits = it },
                label = { Text("Contact *") },
                placeholder = { Text("XX XX XX XX XX") },
                prefix = { Text("+225 ") },
                singleLine = true,
                isError = contactDigits.isNotEmpty() && !contactValide,
                supportingText = {
                    if (contactDigits.isNotEmpty() && !contactValide) {
                        Text("10 chiffres requis")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CI-",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
                OutlinedTextField(
                    value = matriculeAnnee,
                    onValueChange = { if (it.length <= 4) matriculeAnnee = it.filter { c -> c.isDigit() } },
                    label = { Text("Année *") },
                    placeholder = { Text("2024") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = matriculeAnnee.isNotEmpty() && matriculeAnnee.length != 4,
                    supportingText = {
                        if (matriculeAnnee.isNotEmpty() && matriculeAnnee.length != 4) {
                            Text("4 chiffres")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
                OutlinedTextField(
                    value = matriculeNumero,
                    onValueChange = { if (it.length <= 3) matriculeNumero = it.filter { c -> c.isDigit() } },
                    label = { Text("Numéro *") },
                    placeholder = { Text("001") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = matriculeNumero.isNotEmpty() && matriculeNumero.length != 3,
                    supportingText = {
                        if (matriculeNumero.isNotEmpty() && matriculeNumero.length != 3) {
                            Text("3 chiffres")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            if (matriculeValide) {
                Text(
                    text = "Matricule : $matriculeComplet",
                    style = MaterialTheme.typography.bodySmall,
                    color = Green40,
                    fontWeight = FontWeight.Bold
                )
            }

            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it }
            ) {
                OutlinedTextField(
                    value = typeLabels[type] ?: type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type d'entreprise *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    typeLabels.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                type = value
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = zoneCollecte,
                onValueChange = { zoneCollecte = it },
                label = { Text("Zone de collecte souhaitée *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.inscrireEntreprise(
                        nomEntreprise = nomEntreprise,
                        contact = contact,
                        matricule = matriculeComplet,
                        type = TypeEntreprise.valueOf(type),
                        zoneCollecte = zoneCollecte
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
