package com.attiekeco.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.attiekeco.data.QualiteJus
import com.attiekeco.data.TourProduction
import com.attiekeco.ui.theme.AttiekGreen
import com.attiekeco.ui.theme.AttiekGreenLight
import com.attiekeco.ui.theme.AttiekOrange
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
    val context = LocalContext.current

    val totalLitres = collectesDispo.sumOf { it.litresReels }
    val totalMontant = collectesDispo.sumOf { it.montantPaye }

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
                colors = CardDefaults.cardColors(containerColor = AttiekGreenLight),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${collectesDispo.size}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Filled.LocalDrink,
                                contentDescription = null,
                                tint = Orange40,
                                modifier = Modifier.size(24.dp)
                            )
                        }
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

            // Bouton Collecte disponible — bleu ciel
            Button(
                onClick = onCommander,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AttiekGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Collecte disponible (${collectesDispo.size})", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bouton Suivre ma commande — style Jumia
            OutlinedButton(
                onClick = onHistoriqueCommandes,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.TrackChanges,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Suivre ma commande")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bouton Contacter le service client
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+2250150448961"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Forum,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Contacter le service client")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (collectesDispo.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalDrink,
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
                    items(collectesDispo, key = { it.id }) { collecte ->
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
