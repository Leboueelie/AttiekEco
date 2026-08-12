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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionComptesScreen(
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val productrices by viewModel.productrices.collectAsState(initial = emptyList())
    val entreprises by viewModel.entreprises.collectAsState(initial = emptyList())

    var filtre by remember { mutableStateOf("TOUT") }
    var productriceASupprimer by remember { mutableStateOf<Pair<Long, String>?>(null) }
    var entrepriseASupprimer by remember { mutableStateOf<Pair<Long, String>?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Gestion des comptes") },
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
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filtre == "TOUT",
                    onClick = { filtre = "TOUT" },
                    label = { Text("Tout (${productrices.size + entreprises.size})") }
                )
                FilterChip(
                    selected = filtre == "PRODUCTRICE",
                    onClick = { filtre = "PRODUCTRICE" },
                    label = { Text("Productrices (${productrices.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Orange40.copy(alpha = 0.15f),
                        selectedLabelColor = Orange40
                    )
                )
                FilterChip(
                    selected = filtre == "ENTREPRISE",
                    onClick = { filtre = "ENTREPRISE" },
                    label = { Text("Entreprises (${entreprises.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF42A5F5).copy(alpha = 0.15f),
                        selectedLabelColor = Color(0xFF42A5F5)
                    )
                )
            }

            if (productrices.isEmpty() && entreprises.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aucun compte enregistré",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Les comptes apparaîtront ici\naprès l'inscription des utilisateurs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            } else {
                val filteredProductrices = if (filtre == "ENTREPRISE") emptyList() else productrices
                val filteredEntreprises = if (filtre == "PRODUCTRICE") emptyList() else entreprises

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredProductrices, key = { "p_${it.id}" }) { productrice ->
                        ProductriceAdminCard(
                            productrice = productrice,
                            onDelete = { productriceASupprimer = productrice.id to productrice.nom }
                        )
                    }
                    items(filteredEntreprises, key = { "e_${it.id}" }) { entreprise ->
                        EntrepriseAdminCard(
                            entreprise = entreprise,
                            onDelete = { entrepriseASupprimer = entreprise.id to entreprise.nomEntreprise }
                        )
                    }
                }
            }
        }
    }

    productriceASupprimer?.let { (id, nom) ->
        AlertDialog(
            onDismissRequest = { productriceASupprimer = null },
            title = { Text("Supprimer $nom ?") },
            text = { Text("Cette action supprimera le compte, ses bidons et tous les utilisateurs associés. Cette action est irréversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.supprimerProductrice(id)
                        productriceASupprimer = null
                    }
                ) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { productriceASupprimer = null }) {
                    Text("Annuler")
                }
            }
        )
    }

    entrepriseASupprimer?.let { (id, nom) ->
        AlertDialog(
            onDismissRequest = { entrepriseASupprimer = null },
            title = { Text("Supprimer $nom ?") },
            text = { Text("Cette action supprimera le compte entreprise et l'utilisateur associé. Cette action est irréversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.supprimerEntreprise(id)
                        entrepriseASupprimer = null
                    }
                ) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { entrepriseASupprimer = null }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun ProductriceAdminCard(
    productrice: com.attiekeco.data.Productrice,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Orange40.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Orange40,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = productrice.nom,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = productrice.localisation,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = productrice.telephone,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            if (productrice.cooperative != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Coopérative : ${productrice.cooperative}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (productrice.commune != null) {
                Text(
                    text = "Commune : ${productrice.commune}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun EntrepriseAdminCard(
    entreprise: com.attiekeco.data.Entreprise,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF42A5F5).copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Business,
                        contentDescription = null,
                        tint = Color(0xFF42A5F5),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entreprise.nomEntreprise,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Type : ${entreprise.type.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = entreprise.contact,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Matricule : ${entreprise.matricule}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "Zone : ${entreprise.zoneCollecte}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
