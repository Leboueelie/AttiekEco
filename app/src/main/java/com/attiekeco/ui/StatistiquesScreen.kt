package com.attiekeco.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.data.QualiteJus
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatistiquesScreen(
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val collectes by viewModel.collectes.collectAsState(initial = emptyList())
    val commandes by viewModel.commandes.collectAsState(initial = emptyList())
    val productrices by viewModel.productrices.collectAsState(initial = emptyList())
    val entreprises by viewModel.entreprises.collectAsState(initial = emptyList())
    val totalMontant by viewModel.totalMontant.collectAsState(initial = 0.0)
    val totalLitres by viewModel.totalLitres.collectAsState(initial = 0.0)

    val nbPremium = collectes.count { it.qualite == QualiteJus.PREMIUM }
    val nbStandard = collectes.count { it.qualite == QualiteJus.STANDARD }
    val nbBasse = collectes.count { it.qualite == QualiteJus.BASSE }
    val litresPremium = collectes.filter { it.qualite == QualiteJus.PREMIUM }.sumOf { it.litresReels }
    val litresStandard = collectes.filter { it.qualite == QualiteJus.STANDARD }.sumOf { it.litresReels }
    val litresBasse = collectes.filter { it.qualite == QualiteJus.BASSE }.sumOf { it.litresReels }
    val montantPremium = collectes.filter { it.qualite == QualiteJus.PREMIUM }.sumOf { it.montantPaye }
    val montantStandard = collectes.filter { it.qualite == QualiteJus.STANDARD }.sumOf { it.montantPaye }
    val montantBasse = collectes.filter { it.qualite == QualiteJus.BASSE }.sumOf { it.montantPaye }

    val commandesValidees = commandes.count { it.statut == com.attiekeco.data.StatutCommande.VALIDEE }
    val commandesEnCours = commandes.count { it.statut == com.attiekeco.data.StatutCommande.EN_COURS }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Statistiques") },
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
                text = "Vue d'ensemble",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Orange40
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    icon = Icons.Filled.People,
                    label = "Utilisateurs",
                    value = "${productrices.size + entreprises.size}",
                    color = Orange40,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Filled.Receipt,
                    label = "Commandes",
                    value = "${commandes.size}",
                    color = Color(0xFF7E57C2),
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Impact total",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Green40
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${String.format("%.0f", totalLitres)} L",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Green40
                            )
                            Text(
                                text = "Liquide collecté",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${String.format("%.0f", totalMontant)} F",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Green40
                            )
                            Text(
                                text = "Revenus générés",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            Text(
                text = "Ventilation par qualité",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Orange40
            )

            QualiteRow("Premium", nbPremium, litresPremium, montantPremium, Color(0xFFFFB300))
            QualiteRow("Standard", nbStandard, litresStandard, montantStandard, Color(0xFF42A5F5))
            QualiteRow("Basse", nbBasse, litresBasse, montantBasse, Color(0xFF78909C))

            Text(
                text = "Commandes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Orange40
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$commandesValidees",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Green40
                        )
                        Text(
                            text = "Validées",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$commandesEnCours",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFA726)
                        )
                        Text(
                            text = "En cours",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun QualiteRow(
    qualite: String,
    nbCollectes: Int,
    litres: Double,
    montant: Double,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(color, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = qualite,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = "$nbCollectes collectes",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format("%.0f", litres)} L",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.0f", montant)} FCFA",
                    style = MaterialTheme.typography.bodySmall,
                    color = Green40
                )
            }
        }
    }
}
