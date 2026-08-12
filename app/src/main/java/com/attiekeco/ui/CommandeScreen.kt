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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.data.Collecte
import com.attiekeco.data.QualiteJus
import com.attiekeco.data.TourProduction
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandeScreen(
    entrepriseId: Long,
    collecteIdPreselected: Long? = null,
    onCommandeEnvoyee: () -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val collectesDispo by viewModel.collectesDisponibles.collectAsState(initial = emptyList())

    val panier = remember { mutableStateListOf<Long>().apply {
        if (collecteIdPreselected != null) add(collecteIdPreselected)
    } }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val collectesPanier = collectesDispo.filter { it.id in panier }
    val totalMontant = collectesPanier.sumOf { it.montantPaye }
    val totalLitres = collectesPanier.sumOf { it.litresReels }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Commander des collectes") },
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Collectes disponibles (${collectesDispo.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            if (collectesDispo.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aucune collecte disponible",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Les collectes validées par les agents apparaîtront ici.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(collectesDispo, key = { it.id }) { collecte ->
                        val isSelected = collecte.id in panier
                        CommandeCollecteItem(
                            collecte = collecte,
                            isSelected = isSelected,
                            onToggle = {
                                if (isSelected) {
                                    panier.remove(collecte.id)
                                } else {
                                    panier.add(collecte.id)
                                }
                            }
                        )
                    }
                }
            }

            if (panier.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Panier (${panier.size} articles)",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${String.format("%.1f", totalLitres)} L",
                                fontWeight = FontWeight.Bold,
                                color = Green40
                            )
                        }
                        Text(
                            text = "Total : ${String.format("%.0f", totalMontant)} FCFA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Green40,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange40)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Valider la commande", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(text = "Confirmer la commande") },
            text = {
                Text(text = "Valider l'achat de ${panier.size} collecte(s) pour ${String.format("%.0f", totalMontant)} FCFA ?")
            },
            confirmButton = {
                TextButton(onClick = {
                    collectesPanier.forEach { collecte ->
                        viewModel.passerCommande(entrepriseId, collecte.id) { }
                    }
                    panier.clear()
                    showConfirmDialog = false
                    onCommandeEnvoyee()
                }) {
                    Text(text = "Confirmer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(text = "Annuler")
                }
            }
        )
    }
}

@Composable
private fun CommandeCollecteItem(
    collecte: Collecte,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val qualiteLabel = when (collecte.qualite) {
        QualiteJus.PREMIUM -> "Premium"
        QualiteJus.STANDARD -> "Standard"
        QualiteJus.BASSE -> "Basse"
    }
    val tourLabel = when (collecte.tour) {
        TourProduction.PREMIER -> "1er tour"
        TourProduction.DEUXIEME -> "2e tour"
    }

    Card(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
        colors = if (isSelected) {
            CardDefaults.cardColors(containerColor = Orange40.copy(alpha = 0.1f))
        } else {
            CardDefaults.cardColors()
        },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.LocalDrink,
                contentDescription = null,
                tint = if (isSelected) Orange40 else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )

            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    text = "$qualiteLabel — $tourLabel",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.1f", collecte.litresReels)} L — ${String.format("%.0f", collecte.montantPaye)} FCFA",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
