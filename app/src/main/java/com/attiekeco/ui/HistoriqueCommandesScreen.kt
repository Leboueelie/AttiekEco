package com.attiekeco.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.data.Collecte
import com.attiekeco.data.Commande
import com.attiekeco.data.StatutCommande
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40
import com.attiekeco.ui.theme.StatutEnAttente
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoriqueCommandesScreen(
    entrepriseId: Long,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val commandes by viewModel.commandesForEntreprise(entrepriseId).collectAsState(initial = emptyList())
    val collectes by viewModel.collectes.collectAsState(initial = emptyList())

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Historique des commandes") },
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
        if (commandes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aucune commande",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Vos commandes apparaîtront ici.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(commandes, key = { it.id }) { commande ->
                    val collecte = collectes.find { it.id == commande.collecteId }
                    CommandeCard(commande = commande, collecte = collecte, dateFormatter = dateFormatter)
                }
            }
        }
    }
}

@Composable
private fun CommandeCard(
    commande: Commande,
    collecte: Collecte?,
    dateFormatter: SimpleDateFormat
) {
    val qualiteLabel = collecte?.qualite?.let { q ->
        when (q) {
            com.attiekeco.data.QualiteJus.PREMIUM -> "Premium"
            com.attiekeco.data.QualiteJus.STANDARD -> "Standard"
            com.attiekeco.data.QualiteJus.BASSE -> "Basse"
        }
    } ?: "N/A"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${commande.id}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (commande.statut == StatutCommande.VALIDEE) {
                            Icons.Filled.CheckCircle
                        } else {
                            Icons.Filled.HourglassEmpty
                        },
                        contentDescription = null,
                        tint = if (commande.statut == StatutCommande.VALIDEE) Green40 else StatutEnAttente,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = when (commande.statut) {
                            StatutCommande.EN_COURS -> "En cours"
                            StatutCommande.VALIDEE -> "Validée"
                            StatutCommande.ANNULEE -> "Annulée"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = if (commande.statut == StatutCommande.VALIDEE) Green40 else StatutEnAttente
                    )
                }
            }

            if (collecte != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "$qualiteLabel — ${String.format("%.1f", collecte.litresReels)} L",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Agent : ${collecte.nomAgent}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${String.format("%.0f", commande.montant)} FCFA",
                            fontWeight = FontWeight.Bold,
                            color = Green40
                        )
                        Text(
                            text = dateFormatter.format(commande.dateCommande),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
