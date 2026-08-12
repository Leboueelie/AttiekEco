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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrepriseDashboardScreen(
    entrepriseId: Long,
    onVoirCollecte: (Long) -> Unit,
    onCommander: () -> Unit,
    onHistoriqueCommandes: () -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val collectesDispo by viewModel.collectesDisponibles.collectAsState(initial = emptyList())

    var filtreQualite by rememberSaveable { mutableStateOf<String?>(null) }
    var filtreTour by rememberSaveable { mutableStateOf<String?>(null) }

    val collectesFiltrees = collectesDispo.filter { c ->
        (filtreQualite == null || c.qualite.name == filtreQualite) &&
                (filtreTour == null || c.tour.name == filtreTour)
    }

    val totalLitres = collectesFiltrees.sumOf { it.litresReels }
    val totalMontant = collectesFiltrees.sumOf { it.montantPaye }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Espace Entreprise") },
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
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
                            contentDescription = null,
                            tint = Orange40
                        )
                        Text(
                            text = "${collectesDispo.size}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Collectes dispo.",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${String.format("%.0f", totalLitres)} L",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Green40
                        )
                        Text(
                            text = "Volume dispo.",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Text(
                text = "Filtrer",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = filtreQualite == null,
                    onClick = { filtreQualite = null },
                    label = { Text("Tout") }
                )
                QualiteJus.entries.forEach { q ->
                    FilterChip(
                        selected = filtreQualite == q.name,
                        onClick = { filtreQualite = if (filtreQualite == q.name) null else q.name },
                        label = {
                            Text(when (q) {
                                QualiteJus.PREMIUM -> "Premium"
                                QualiteJus.STANDARD -> "Standard"
                                QualiteJus.BASSE -> "Basse"
                            })
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange40.copy(alpha = 0.15f),
                            selectedLabelColor = Orange40
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = filtreTour == null,
                    onClick = { filtreTour = null },
                    label = { Text("Tout tour") }
                )
                TourProduction.entries.forEach { t ->
                    FilterChip(
                        selected = filtreTour == t.name,
                        onClick = { filtreTour = if (filtreTour == t.name) null else t.name },
                        label = {
                            Text(when (t) {
                                TourProduction.PREMIER -> "1er tour"
                                TourProduction.DEUXIEME -> "2e tour"
                            })
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Green40.copy(alpha = 0.15f),
                            selectedLabelColor = Green40
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCommander,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange40)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Commander (${collectesFiltrees.size})", color = Color.White)
                }
                OutlinedButton(
                    onClick = onHistoriqueCommandes,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Commandes")
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
                    Icon(
                        imageVector = Icons.Filled.Inventory,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Aucune collecte disponible",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(collectesFiltrees, key = { it.id }) { collecte ->
                        CollecteDispoCard(
                            collecte = collecte,
                            onClick = { onVoirCollecte(collecte.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CollecteDispoCard(
    collecte: Collecte,
    onClick: () -> Unit
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
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$qualiteLabel — $tourLabel",
                    fontWeight = FontWeight.Bold,
                    color = Orange40
                )
                Text(
                    text = "${String.format("%.1f", collecte.litresReels)} L — ${String.format("%.0f", collecte.montantPaye)} FCFA",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = "Agent : ${collecte.nomAgent}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Icon(
                imageVector = Icons.Filled.LocalDrink,
                contentDescription = null,
                tint = Green40
            )
        }
    }
}
