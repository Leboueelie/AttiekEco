package com.attiekeco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.attiekeco.ui.AdminDashboardScreen
import com.attiekeco.ui.AgentScreen
import com.attiekeco.ui.CollecteDetailScreen
import com.attiekeco.ui.CommandeScreen
import com.attiekeco.ui.ConnexionScreen
import com.attiekeco.ui.EcranBienvenue
import com.attiekeco.ui.EcranCodeCreateur
import com.attiekeco.ui.EcranVerificationSms
import com.attiekeco.ui.EntrepriseDashboardScreen
import com.attiekeco.ui.GestionComptesScreen
import com.attiekeco.ui.GestionCommandesScreen
import com.attiekeco.ui.GrilleTarifaireScreen
import com.attiekeco.ui.HistoriqueScreen
import com.attiekeco.ui.HistoriqueCommandesScreen
import com.attiekeco.ui.InscriptionEntrepriseScreen
import com.attiekeco.ui.InscriptionProductriceScreen
import com.attiekeco.ui.ProductriceDashboardScreen
import com.attiekeco.ui.RoleSelectionScreen
import com.attiekeco.ui.StatistiquesScreen
import com.attiekeco.ui.theme.AttiekEcoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttiekEcoTheme {
                AttiekEcoApp()
            }
        }
    }
}

@Composable
fun AttiekEcoApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "role",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("role") {
            RoleSelectionScreen(
                onProductriceClick = { navController.navigate("inscription_productrice") },
                onEntrepriseClick = { navController.navigate("inscription_entreprise") },
                onCreateurClick = { navController.navigate("code_createur") },
                onProductriceConnexionClick = { navController.navigate("connexion_productrice") },
                onEntrepriseConnexionClick = { navController.navigate("connexion_entreprise") }
            )
        }
        composable("connexion_productrice") {
            ConnexionScreen(
                role = "PRODUCTRICE",
                onConnexionReussie = { productriceId ->
                    navController.navigate("productriceDashboard/$productriceId") {
                        popUpTo("role") { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("connexion_entreprise") {
            ConnexionScreen(
                role = "ENTREPRISE",
                onConnexionReussie = { entrepriseId ->
                    navController.navigate("entreprise_dashboard/$entrepriseId") {
                        popUpTo("role") { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("inscription_productrice") {
            InscriptionProductriceScreen(
                onInscriptionComplete = { utilisateurId ->
                    navController.navigate("verification_sms/$utilisateurId?role=PRODUCTRICE") {
                        popUpTo("inscription_productrice") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("inscription_entreprise") {
            InscriptionEntrepriseScreen(
                onInscriptionComplete = { utilisateurId ->
                    navController.navigate("verification_sms/$utilisateurId?role=ENTREPRISE") {
                        popUpTo("inscription_entreprise") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "verification_sms/{utilisateurId}?role={role}",
            arguments = listOf(
                navArgument("utilisateurId") { type = NavType.LongType },
                navArgument("role") { type = NavType.StringType; defaultValue = "PRODUCTRICE" }
            )
        ) { backStackEntry ->
            val utilisateurId = backStackEntry.arguments?.getLong("utilisateurId") ?: 0L
            val role = backStackEntry.arguments?.getString("role") ?: "PRODUCTRICE"
            EcranVerificationSms(
                utilisateurId = utilisateurId,
                onVerificationReussie = {
                    navController.navigate("bienvenue/$role/$utilisateurId") {
                        popUpTo("role") { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "bienvenue/{role}/{utilisateurId}",
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
                navArgument("utilisateurId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "PRODUCTRICE"
            val utilisateurId = backStackEntry.arguments?.getLong("utilisateurId") ?: 0L
            val vm: com.attiekeco.ui.AttiekEcoViewModel = viewModel()
            EcranBienvenue(
                role = role,
                utilisateurId = utilisateurId,
                onContinuerProductrice = { productriceId ->
                    navController.navigate("productriceDashboard/$productriceId") {
                        popUpTo("bienvenue/$role/$utilisateurId") { inclusive = true }
                    }
                },
                onContinuerEntreprise = {
                    vm.getProfileId(utilisateurId) { profileId ->
                        if (profileId != null) {
                            navController.navigate("entreprise_dashboard/$profileId") {
                                popUpTo("bienvenue/$role/$utilisateurId") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        composable("code_createur") {
            EcranCodeCreateur(
                onCodeValide = {
                    navController.navigate("admin_dashboard") {
                        popUpTo("code_createur") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("admin_dashboard") {
            AdminDashboardScreen(
                onGrilleTarifaire = { navController.navigate("grille_tarifaire") },
                onGestionComptes = { navController.navigate("gestion_comptes") },
                onStatistiques = { navController.navigate("statistiques") },
                onCollectesAgent = { navController.navigate("agent") },
                onCommandes = { navController.navigate("gestion_commandes") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("grille_tarifaire") {
            GrilleTarifaireScreen(onBack = { navController.popBackStack() })
        }
        composable("gestion_comptes") {
            GestionComptesScreen(onBack = { navController.popBackStack() })
        }
        composable("gestion_commandes") {
            GestionCommandesScreen(onBack = { navController.popBackStack() })
        }
        composable("statistiques") {
            StatistiquesScreen(onBack = { navController.popBackStack() })
        }
        composable(
            "productriceDashboard/{productriceId}",
            arguments = listOf(navArgument("productriceId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("productriceId") ?: 0L
            ProductriceDashboardScreen(
                productriceId = id,
                onHistoriqueClick = { pid -> navController.navigate("historique/$pid") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "historique/{productriceId}",
            arguments = listOf(navArgument("productriceId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("productriceId") ?: 0L
            HistoriqueScreen(
                productriceId = id,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "entreprise_dashboard/{entrepriseId}",
            arguments = listOf(navArgument("entrepriseId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eid = backStackEntry.arguments?.getLong("entrepriseId") ?: 0L
            EntrepriseDashboardScreen(
                entrepriseId = eid,
                onVoirCollecte = { collecteId -> navController.navigate("collecteDetail/$collecteId/$eid") },
                onCommander = { navController.navigate("commande/$eid") },
                onHistoriqueCommandes = { navController.navigate("historique_commandes/$eid") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "collecteDetail/{collecteId}/{entrepriseId}",
            arguments = listOf(
                navArgument("collecteId") { type = NavType.LongType },
                navArgument("entrepriseId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("collecteId") ?: 0L
            val eid = backStackEntry.arguments?.getLong("entrepriseId") ?: 0L
            CollecteDetailScreen(
                collecteId = id,
                entrepriseId = eid,
                onCommander = { collecteId ->
                    navController.navigate("commande/$eid/$collecteId") {
                        popUpTo("entreprise_dashboard/$eid") { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "commande/{entrepriseId}/{collecteIdPreselected}",
            arguments = listOf(
                navArgument("entrepriseId") { type = NavType.LongType },
                navArgument("collecteIdPreselected") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val eid = backStackEntry.arguments?.getLong("entrepriseId") ?: 0L
            val cid = backStackEntry.arguments?.getLong("collecteIdPreselected") ?: -1L
            CommandeScreen(
                entrepriseId = eid,
                collecteIdPreselected = if (cid > 0) cid else null,
                onCommandeEnvoyee = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "historique_commandes/{entrepriseId}",
            arguments = listOf(navArgument("entrepriseId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eid = backStackEntry.arguments?.getLong("entrepriseId") ?: 0L
            HistoriqueCommandesScreen(
                entrepriseId = eid,
                onBack = { navController.popBackStack() }
            )
        }
        composable("agent") {
            AgentScreen(onBack = { navController.popBackStack() })
        }
    }
}
