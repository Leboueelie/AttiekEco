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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.data.TourProduction
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrilleTarifaireScreen(
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val grille by viewModel.grilleTarifaire.collectAsState(initial = emptyList())

    val prixModifies = remember { mutableStateMapOf<String, String>() }

    val tourLabels = mapOf(
        TourProduction.PREMIER.name to "1er tour",
        TourProduction.DEUXIEME.name to "2e tour"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Grille tarifaire") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Prix par tour (FCFA/L)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Orange40
            )

            Text(
                text = "Modifiez les tarifs appliqués lors de la collecte selon le tour de production.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            TourProduction.entries.forEach { tour ->
                val currentPrix = grille.find { it.tour == tour }?.prixParLitre
                val editedValue = prixModifies[tour.name]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (editedValue != null) Orange40.copy(alpha = 0.05f) else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tourLabels[tour.name] ?: tour.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tarif actuel : ${currentPrix?.let { String.format("%.0f", it) } ?: "—"} FCFA/L",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = editedValue ?: "",
                            onValueChange = { newValue ->
                                val filtered = newValue.filter { c -> c.isDigit() || c == '.' }
                                if (filtered.isEmpty()) {
                                    prixModifies.remove(tour.name)
                                } else {
                                    prixModifies[tour.name] = filtered
                                }
                            },
                            label = { Text("Nouveau prix") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(140.dp),
                            suffix = { Text("F") }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    prixModifies.forEach { (tourName, prixText) ->
                        val prix = prixText.toDoubleOrNull()
                        if (prix != null && prix > 0) {
                            viewModel.modifierTarif(TourProduction.valueOf(tourName), prix)
                        }
                    }
                    prixModifies.clear()
                },
                enabled = prixModifies.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green40)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Enregistrer les modifications", color = Color.White)
            }
        }
    }
}
