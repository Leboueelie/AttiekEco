package com.attiekeco.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.ui.theme.AttiekEcoTheme
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40
import com.attiekeco.ui.theme.StatutSignale
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(
    viewModel: AttiekEcoViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val bidons by viewModel.bidonsSignales.collectAsState(initial = emptyList())
    val totalLitres by viewModel.totalLitres.collectAsState(initial = 0.0)
    val totalMontant by viewModel.totalMontant.collectAsState(initial = 0.0)
    val allProductrices by viewModel.productrices.collectAsState(initial = emptyList())
    val names = allProductrices.associate { it.id to it.nom }

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var bidonACollecterId by rememberSaveable { mutableStateOf(0L) }
    var nomAgentText by rememberSaveable { mutableStateOf("") }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Agent de collecte") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Orange40),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.LocalDrink,
                                contentDescription = "Volume collecté",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "${formatDec(totalLitres)} L",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Collecté",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Montant payé",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "${formatDec(totalMontant)} F",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Payé",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Bidons signalés (${bidons.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (bidons.isEmpty()) {
                item {
                    Text(
                        text = "Aucun bidon signalé pour le moment.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(bidons, key = { it.id }) { bidon ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Productrice",
                                        tint = StatutSignale,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Column(modifier = Modifier.padding(start = 4.dp)) {
                                        Text(
                                            text = names[bidon.productriceId] ?: "Inconnue",
                                            fontWeight = FontWeight.Bold
                                        )
                                        val qualiteLabel = when (bidon.qualite) {
                                            com.attiekeco.data.QualiteJus.PREMIUM -> "Premium"
                                            com.attiekeco.data.QualiteJus.STANDARD -> "Standard"
                                            com.attiekeco.data.QualiteJus.BASSE -> "Basse"
                                            else -> "Qualité inconnue"
                                        }
                                        val tourLabel = when (bidon.tour) {
                                            com.attiekeco.data.TourProduction.PREMIER -> "1er tour"
                                            com.attiekeco.data.TourProduction.DEUXIEME -> "2e tour"
                                            else -> "Tour inconnu"
                                        }
                                        Text(
                                            text = "$qualiteLabel — $tourLabel — ${formatDec(bidon.litresReels ?: 0.0)} L",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Déclaré le ${dateFormatter.format(bidon.dateDeclaration ?: Date())}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Button(
                                onClick = {
                                    bidonACollecterId = bidon.id
                                    nomAgentText = ""
                                    showDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Green40)
                            ) {
                                Text(text = "Confirmer la collecte", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog && bidonACollecterId > 0) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmer la collecte") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nomAgentText,
                        onValueChange = { nomAgentText = it },
                        label = { Text(text = "Nom de l'agent") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nomAgentText.isNotBlank()) {
                            viewModel.confirmerCollecte(
                                bidonId = bidonACollecterId,
                                nomAgent = nomAgentText
                            )
                        }
                        showDialog = false
                        nomAgentText = ""
                    }
                ) {
                    Text(text = "Confirmer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Annuler")
                }
            }
        )
    }
}

@Composable
private fun formatDec(value: Double): String =
    String.format(Locale.getDefault(), "%.2f", value)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AgentScreenPreview() {
    AttiekEcoTheme {
        AgentScreen(onBack = {})
    }
}
