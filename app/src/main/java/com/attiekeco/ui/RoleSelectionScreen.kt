package com.attiekeco.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.attiekeco.ui.theme.AttiekEcoTheme
import com.attiekeco.ui.theme.Green40
import com.attiekeco.ui.theme.Orange40

@Composable
fun RoleSelectionScreen(
    onProductriceClick: () -> Unit = {},
    onEntrepriseClick: () -> Unit = {},
    onCreateurClick: () -> Unit = {},
    onProductriceConnexionClick: () -> Unit = {},
    onEntrepriseConnexionClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Orange40, Color.White)
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Green40, MaterialTheme.shapes.extraLarge),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AE",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "AttiekEco",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Collecte du liquide de production\nd'attieké",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        RoleButton(
            icon = Icons.Filled.Agriculture,
            label = "Productrice",
            description = "Déclarer mes bidons de liquide et suivre mes collectes",
            onClick = onProductriceClick
        )

        TextButton(
            onClick = onProductriceConnexionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Déjà inscrit ? Se connecter",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        RoleButton(
            icon = Icons.Filled.Business,
            label = "Entreprise",
            description = "Acheter des liquides disponibles",
            onClick = onEntrepriseClick
        )

        TextButton(
            onClick = onEntrepriseConnexionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Déjà inscrit ? Se connecter",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCreateurClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.AdminPanelSettings,
                contentDescription = "Espace Créateur",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Espace Créateur")
        }
    }
}

@Composable
private fun RoleButton(
    icon: ImageVector,
    label: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Orange40
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RoleSelectionScreenPreview() {
    AttiekEcoTheme {
        RoleSelectionScreen()
    }
}
