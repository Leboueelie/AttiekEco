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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.ShowChart
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onGrilleTarifaire: () -> Unit,
    onGestionComptes: () -> Unit,
    onStatistiques: () -> Unit,
    onCollectesAgent: () -> Unit,
    onCommandes: () -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val productrices by viewModel.productrices.collectAsState(initial = emptyList())
    val entreprises by viewModel.entreprises.collectAsState(initial = emptyList())
    val collectes by viewModel.collectes.collectAsState(initial = emptyList())
    val commandes by viewModel.commandes.collectAsState(initial = emptyList())
    val commandesEnCours = commandes.count { it.statut == com.attiekeco.data.StatutCommande.EN_COURS }
    val totalMontant by viewModel.totalMontant.collectAsState(initial = 0.0)
    val totalLitres by viewModel.totalLitres.collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Espace Créateur") },
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
        ) {
            Text(
                text = "Vue d'ensemble",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(
                    icon = Icons.Filled.Group,
                    label = "Productrices",
                    value = "${productrices.size}",
                    color = Orange40,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    icon = Icons.Filled.Group,
                    label = "Entreprises",
                    value = "${entreprises.size}",
                    color = Green40,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(
                    icon = Icons.Filled.LocalDrink,
                    label = "Collectes",
                    value = "${collectes.size}",
                    color = Color(0xFF42A5F5),
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    icon = Icons.Filled.Receipt,
                    label = "Commandes",
                    value = "${commandes.size}",
                    color = Color(0xFF7E57C2),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(
                    icon = Icons.Filled.LocalDrink,
                    label = "Volume total",
                    value = "${String.format("%.0f", totalLitres)} L",
                    color = Color(0xFF26A69A),
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    icon = Icons.Filled.AttachMoney,
                    label = "Revenu total",
                    value = "${String.format("%.0f", totalMontant)} F",
                    color = Color(0xFF66BB6A),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            AdminActionCard(
                icon = Icons.Filled.Settings,
                title = "Grille tarifaire",
                subtitle = "Modifier les prix par qualité",
                onClick = onGrilleTarifaire
            )
            AdminActionCard(
                icon = Icons.Filled.Group,
                title = "Gestion des comptes",
                subtitle = "Productrices et entreprises",
                onClick = onGestionComptes
            )
            AdminActionCard(
                icon = Icons.AutoMirrored.Filled.ShowChart,
                title = "Statistiques",
                subtitle = "Volumes, revenus, impact",
                onClick = onStatistiques
            )
            AdminActionCard(
                icon = Icons.Filled.LocalDrink,
                title = "Collectes agent",
                subtitle = "Valider les collectes en attente",
                onClick = onCollectesAgent
            )
            AdminActionCard(
                icon = Icons.Filled.Receipt,
                title = "Commandes",
                subtitle = if (commandesEnCours > 0) "$commandesEnCours en attente" else "Valider ou annuler les commandes",
                onClick = onCommandes
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AdminStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
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
private fun AdminActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Orange40,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
