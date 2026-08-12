package com.attiekeco.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProductriceListScreen(
    viewModel: AttiekEcoViewModel = viewModel(),
    onProductriceClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val productrices by viewModel.productrices.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Productrices") },
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
        if (productrices.isEmpty()) {
            Text(
                text = "Aucune productrice disponible.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = padding
            ) {
                items(productrices, key = { it.id }) { productrice ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductriceClick(productrice.id) }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = productrice.nom,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = productrice.localisation,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = productrice.telephone,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
