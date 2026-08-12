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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.data.Collecte
import com.attiekeco.data.QualiteJus
import com.attiekeco.data.TourProduction
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoriqueScreen(
    productriceId: Long,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val bidons by viewModel.bidonsForProductrice(productriceId).collectAsState(initial = emptyList())
    val collectes by viewModel.collectes.collectAsState(initial = emptyList())

    val collectesProductrice = collectes.filter { c ->
        bidons.any { b -> b.id == c.bidonId && b.productriceId == productriceId }
    }

    var filtreQualite by rememberSaveable { mutableStateOf<String?>(null) }

    val collectesFiltrees = if (filtreQualite != null) {
        collectesProductrice.filter {
            it.qualite.name == filtreQualite
        }
    } else {
        collectesProductrice
    }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val totalMontant = collectesProductrice.sumOf { it.montantPaye }
    val totalLitres = collectesProductrice.sumOf { it.litresReels }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Historique des collectes") },
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
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(12.dp)
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
                            contentDescription = null,
                            tint = Green40
                        )
                        Text(
                            text = "${formatDec(totalLitres)} L",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total collecté",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.AttachMoney,
                            contentDescription = null,
                            tint = Green40
                        )
                        Text(
                            text = "${formatDec(totalMontant)} FCFA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total reçu",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Text(
                text = "Filtrer par qualité",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filtreQualite == null,
                    onClick = { filtreQualite = null },
                    label = { Text("Tout") }
                )
                QualiteJus.entries.forEach { qualite ->
                    FilterChip(
                        selected = filtreQualite == qualite.name,
                        onClick = {
                            filtreQualite = if (filtreQualite == qualite.name) null else qualite.name
                        },
                        label = {
                            Text(
                                when (qualite) {
                                    QualiteJus.PREMIUM -> "Premium"
                                    QualiteJus.STANDARD -> "Standard"
                                    QualiteJus.BASSE -> "Basse"
                                }
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange40.copy(alpha = 0.15f),
                            selectedLabelColor = Orange40
                        )
                    )
                }
            }

            if (collectesFiltrees.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aucune collecte",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Vos collectes apparaîtront ici une fois validées.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(collectesFiltrees, key = { it.id }) { collecte ->
                        CollecteHistoriqueCard(collecte = collecte, dateFormatter = dateFormatter)
                    }
                }
            }
        }
    }
}

@Composable
private fun CollecteHistoriqueCard(
    collecte: Collecte,
    dateFormatter: SimpleDateFormat
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
                    text = qualiteLabel,
                    fontWeight = FontWeight.Bold,
                    color = Orange40
                )
                Text(
                    text = tourLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            Text(
                text = "${formatDec(collecte.litresReels)} L collectés",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Agent : ${collecte.nomAgent}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${formatDec(collecte.montantPaye)} FCFA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Green40,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = dateFormatter.format(collecte.dateCollecte),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun formatDec(value: Double): String =
    String.format(Locale.getDefault(), "%.0f", value)
