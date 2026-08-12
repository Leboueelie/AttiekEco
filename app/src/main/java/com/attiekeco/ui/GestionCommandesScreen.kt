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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun GestionCommandesScreen(
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val commandes by viewModel.commandes.collectAsState(initial = emptyList())
    val collectes by viewModel.collectes.collectAsState(initial = emptyList())
    val entreprises by viewModel.entreprises.collectAsState(initial = emptyList())

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val commandesEnCours = commandes.filter { it.statut == StatutCommande.EN_COURS }
    val commandesTraitees = commandes.filter { it.statut != StatutCommande.EN_COURS }

    var commandeAValider by remember { mutableStateOf<Commande?>(null) }
    var commandeAAnnuler by remember { mutableStateOf<Commande?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Gestion des commandes") },
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Les commandes des entreprises apparaîtront ici.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
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
                if (commandesEnCours.isNotEmpty()) {
                    item {
                        Text(
                            text = "En attente (${commandesEnCours.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StatutEnAttente,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(commandesEnCours, key = { it.id }) { commande ->
                        val collecte = collectes.find { it.id == commande.collecteId }
                        val entreprise = entreprises.find { it.id == commande.entrepriseId }
                        CommandeAdminCard(
                            commande = commande,
                            collecte = collecte,
                            entrepriseNom = entreprise?.nomEntreprise ?: "Inconnu",
                            dateFormatter = dateFormatter,
                            onValider = { commandeAValider = commande },
                            onAnnuler = { commandeAAnnuler = commande }
                        )
                    }
                }

                if (commandesTraitees.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Déjà traitées (${commandesTraitees.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(commandesTraitees, key = { it.id }) { commande ->
                        val collecte = collectes.find { it.id == commande.collecteId }
                        val entreprise = entreprises.find { it.id == commande.entrepriseId }
                        CommandeTraiteeCard(
                            commande = commande,
                            collecte = collecte,
                            entrepriseNom = entreprise?.nomEntreprise ?: "Inconnu",
                            dateFormatter = dateFormatter
                        )
                    }
                }
            }
        }
    }

    commandeAValider?.let { commande ->
        val collecte = collectes.find { it.id == commande.collecteId }
        val entreprise = entreprises.find { it.id == commande.entrepriseId }
        AlertDialog(
            onDismissRequest = { commandeAValider = null },
            title = { Text("Valider la commande #${commande.id} ?") },
            text = {
                Text("Confirmer la validation de la commande de ${entreprise?.nomEntreprise ?: "Inconnu"} pour ${String.format("%.0f", commande.montant)} FCFA ?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.validerCommande(commande.id)
                        commandeAValider = null
                    }
                ) {
                    Text("Valider", color = Green40)
                }
            },
            dismissButton = {
                TextButton(onClick = { commandeAValider = null }) {
                    Text("Annuler")
                }
            }
        )
    }

    commandeAAnnuler?.let { commande ->
        val collecte = collectes.find { it.id == commande.collecteId }
        val entreprise = entreprises.find { it.id == commande.entrepriseId }
        AlertDialog(
            onDismissRequest = { commandeAAnnuler = null },
            title = { Text("Annuler la commande #${commande.id} ?") },
            text = {
                Text("Annuler la commande de ${entreprise?.nomEntreprise ?: "Inconnu"} ? La collecte redeviendra disponible.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.annulerCommande(commande.id)
                        commandeAAnnuler = null
                    }
                ) {
                    Text("Annuler la commande", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { commandeAAnnuler = null }) {
                    Text("Retour")
                }
            }
        )
    }
}

@Composable
private fun CommandeAdminCard(
    commande: Commande,
    collecte: Collecte?,
    entrepriseNom: String,
    dateFormatter: SimpleDateFormat,
    onValider: () -> Unit,
    onAnnuler: () -> Unit
) {
    val qualiteLabel = collecte?.qualite?.let { q ->
        when (q) {
            com.attiekeco.data.QualiteJus.PREMIUM -> "Premium"
            com.attiekeco.data.QualiteJus.STANDARD -> "Standard"
            com.attiekeco.data.QualiteJus.BASSE -> "Basse"
        }
    } ?: "N/A"

    val tourLabel = collecte?.tour?.let { t ->
        when (t) {
            com.attiekeco.data.TourProduction.PREMIER -> "1er tour"
            com.attiekeco.data.TourProduction.DEUXIEME -> "2e tour"
        }
    } ?: "N/A"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = StatutEnAttente.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Commande #${commande.id}",
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.HourglassEmpty,
                        contentDescription = null,
                        tint = StatutEnAttente,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "En cours",
                        style = MaterialTheme.typography.labelMedium,
                        color = StatutEnAttente
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = entrepriseNom,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            if (collecte != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "$qualiteLabel — $tourLabel",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocalDrink,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${String.format("%.1f", collecte.litresReels)} L",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onValider,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Green40)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Valider", color = Color.White)
                }
                OutlinedButton(
                    onClick = onAnnuler,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Annuler", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun CommandeTraiteeCard(
    commande: Commande,
    collecte: Collecte?,
    entrepriseNom: String,
    dateFormatter: SimpleDateFormat
) {
    val qualiteLabel = collecte?.qualite?.let { q ->
        when (q) {
            com.attiekeco.data.QualiteJus.PREMIUM -> "Premium"
            com.attiekeco.data.QualiteJus.STANDARD -> "Standard"
            com.attiekeco.data.QualiteJus.BASSE -> "Basse"
        }
    } ?: "N/A"

    val isValidee = commande.statut == StatutCommande.VALIDEE
    val statusColor = if (isValidee) Green40 else MaterialTheme.colorScheme.error
    val statusText = when (commande.statut) {
        StatutCommande.VALIDEE -> "Validée"
        StatutCommande.ANNULEE -> "Annulée"
        StatutCommande.EN_COURS -> "En cours"
    }

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
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = if (isValidee) Icons.Filled.CheckCircle else Icons.Filled.Close,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.padding(6.dp).size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "#${commande.id} — $entrepriseNom",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor
                    )
                }
                Text(
                    text = "$qualiteLabel — ${String.format("%.0f", commande.montant)} FCFA",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
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
