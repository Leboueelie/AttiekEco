package com.attiekeco.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.attiekeco.data.QualiteJus
import com.attiekeco.data.StatutBidon
import com.attiekeco.data.TourProduction
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40
import com.attiekeco.ui.theme.StatutCollecte
import com.attiekeco.ui.theme.StatutEnAttente
import com.attiekeco.ui.theme.StatutSignale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductriceDashboardScreen(
    productriceId: Long,
    onHistoriqueClick: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: AttiekEcoViewModel = viewModel()
) {
    val allProductrices by viewModel.productrices.collectAsState(initial = emptyList())
    val productrice = allProductrices.find { it.id == productriceId }
    val bidons by viewModel.bidonsForProductrice(productriceId).collectAsState(initial = emptyList())
    val collectes by viewModel.collectes.collectAsState(initial = emptyList())

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedQualite by rememberSaveable { mutableStateOf(QualiteJus.STANDARD.name) }
    var selectedTour by rememberSaveable { mutableStateOf(TourProduction.PREMIER.name) }
    var litresText by rememberSaveable { mutableStateOf("") }
    var errorText by rememberSaveable { mutableStateOf("") }

    val bidonDisponible = bidons.firstOrNull { it.statut == StatutBidon.EN_ATTENTE }

    val qualiteLabels = mapOf(
        QualiteJus.PREMIUM.name to "Premium",
        QualiteJus.STANDARD.name to "Standard",
        QualiteJus.BASSE.name to "Basse"
    )
    val tourLabels = mapOf(
        TourProduction.PREMIER.name to "1er tour",
        TourProduction.DEUXIEME.name to "2e tour"
    )

    val montantTotal = collectes
        .filter { c ->
            bidons.any { b -> b.id == c.bidonId && b.productriceId == productriceId }
        }
        .sumOf { it.montantPaye }

    val nbEnAttente = bidons.count { it.statut == StatutBidon.EN_ATTENTE }
    val nbSignales = bidons.count { it.statut == StatutBidon.SIGNE_PLN }
    val nbCollectes = bidons.count { it.statut == StatutBidon.COLLECTE }

    if (showDialog && bidonDisponible != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                errorText = ""
                litresText = ""
            },
            title = { Text(text = "Déclarer un bidon de liquide") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                    ) {
                        Text(
                            text = "$nbEnAttente bidon(s) disponible(s)",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Orange40
                        )
                    }

                    Text(
                        text = "Qualité du jus",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        QualiteJus.entries.forEach { qualite ->
                            FilterChip(
                                selected = selectedQualite == qualite.name,
                                onClick = { selectedQualite = qualite.name },
                                label = { Text(qualiteLabels[qualite.name] ?: qualite.name) },
                                leadingIcon = if (selectedQualite == qualite.name) {
                                    { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Orange40.copy(alpha = 0.15f),
                                    selectedLabelColor = Orange40
                                )
                            )
                        }
                    }

                    Text(
                        text = "Tour de production",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        TourProduction.entries.forEach { tour ->
                            FilterChip(
                                selected = selectedTour == tour.name,
                                onClick = { selectedTour = tour.name },
                                label = { Text(tourLabels[tour.name] ?: tour.name) },
                                leadingIcon = if (selectedTour == tour.name) {
                                    { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Orange40.copy(alpha = 0.15f),
                                    selectedLabelColor = Orange40
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = litresText,
                        onValueChange = {
                            litresText = it.filter { c -> c.isDigit() || c == '.' }
                            errorText = ""
                        },
                        label = { Text("Volume du liquide réel (L)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorText.isNotBlank(),
                        supportingText = if (errorText.isNotBlank()) {
                            { Text(errorText, color = MaterialTheme.colorScheme.error) }
                        } else null
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val litres = litresText.toDoubleOrNull()
                        if (litres == null || litres <= 0) {
                            errorText = "Veuillez entrer un volume valide."
                            return@TextButton
                        }
                        viewModel.signalerBidonPlein(
                            bidonId = bidonDisponible.id,
                            qualite = QualiteJus.valueOf(selectedQualite),
                            tour = TourProduction.valueOf(selectedTour),
                            litresReels = litres
                        )
                        showDialog = false
                        errorText = ""
                        litresText = ""
                    }
                ) {
                    Text("Envoyer", color = Orange40)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        errorText = ""
                        litresText = ""
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Mon espace") },
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
        if (productrice == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Productrice introuvable.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Orange40),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = productrice.nom,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = productrice.localisation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    if (productrice.cooperative != null) {
                        Text(
                            text = "Coopérative : ${productrice.cooperative}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "Statut des bidons",
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
                StatutChip(
                    label = "En attente",
                    count = nbEnAttente,
                    color = StatutEnAttente,
                    modifier = Modifier.weight(1f)
                )
                StatutChip(
                    label = "Signalé",
                    count = nbSignales,
                    color = StatutSignale,
                    modifier = Modifier.weight(1f)
                )
                StatutChip(
                    label = "Collecté",
                    count = nbCollectes,
                    color = StatutCollecte,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Solde cumulé",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Green40, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Money,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${formatDec(montantTotal)} FCFA",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Green40
                        )
                        Text(
                            text = "Total reçu depuis le début",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange40)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Déclarer un bidon de liquide", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.demanderBidon(productriceId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Demander un bidon vide")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onHistoriqueClick(productriceId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Historique des collectes")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatutChip(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = count > 0,
        onClick = { },
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color.copy(alpha = 0.15f),
            selectedLabelColor = color
        ),
        modifier = modifier
    )
}

@Composable
private fun formatDec(value: Double): String =
    String.format(java.util.Locale.getDefault(), "%.0f", value)
