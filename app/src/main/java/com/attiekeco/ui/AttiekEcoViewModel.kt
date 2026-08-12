package com.attiekeco.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.attiekeco.data.AppDatabase
import com.attiekeco.data.AttiekEcoRepository
import com.attiekeco.data.Bidon
import com.attiekeco.data.Collecte
import com.attiekeco.data.Commande
import com.attiekeco.data.Entreprise
import com.attiekeco.data.GrilleTarifaire
import com.attiekeco.data.Productrice
import com.attiekeco.data.QualiteJus
import com.attiekeco.data.StatutCommande
import com.attiekeco.data.TourProduction
import com.attiekeco.data.TypeEntreprise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AttiekEcoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AttiekEcoRepository(AppDatabase.getInstance(application))

    val productrices: StateFlow<List<Productrice>> =
        repository.productrices.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val entreprises: StateFlow<List<Entreprise>> =
        repository.entreprises.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bidonsSignales: StateFlow<List<Bidon>> =
        repository.bidonsSignales.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalMontant: StateFlow<Double> =
        repository.totalMontant.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val totalLitres: StateFlow<Double> =
        repository.totalLitres.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val collectes: StateFlow<List<Collecte>> =
        repository.collectes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val collectesDisponibles: StateFlow<List<Collecte>> =
        repository.collectesDisponibles.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val grilleTarifaire: StateFlow<List<GrilleTarifaire>> =
        repository.grilleTarifaire.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val commandes: StateFlow<List<Commande>> =
        repository.commandes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun bidonsForProductrice(productriceId: Long): Flow<List<Bidon>> =
        repository.bidonsForProductrice(productriceId)

    fun commandesForEntreprise(entrepriseId: Long): Flow<List<Commande>> =
        repository.commandesForEntreprise(entrepriseId)

    fun collecteForBidon(bidonId: Long): Collecte? =
        collectes.value.find { it.bidonId == bidonId }

    fun inscrireProductrice(
        nom: String,
        telephone: String,
        localisation: String,
        commune: String?,
        cooperative: String?,
        onResult: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val id = repository.inscrireProductrice(nom, telephone, localisation, commune, cooperative)
            onResult(id)
        }
    }

    fun inscrireEntreprise(
        nomEntreprise: String,
        contact: String,
        matricule: String,
        type: TypeEntreprise,
        zoneCollecte: String,
        onResult: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val id = repository.inscrireEntreprise(nomEntreprise, contact, matricule, type, zoneCollecte)
            onResult(id)
        }
    }

    fun verifierSms(utilisateurId: Long, code: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val succes = repository.verifierUtilisateur(utilisateurId, code)
            onResult(succes)
        }
    }

    fun signalerBidonPlein(
        bidonId: Long,
        qualite: QualiteJus,
        tour: TourProduction,
        litresReels: Double
    ) {
        viewModelScope.launch {
            repository.signalerBidonPlein(bidonId, qualite, tour, litresReels)
        }
    }

    fun confirmerCollecte(bidonId: Long, nomAgent: String) {
        viewModelScope.launch {
            repository.confirmerCollecte(bidonId, nomAgent)
        }
    }

    fun passerCommande(entrepriseId: Long, collecteId: Long, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val succes = repository.passerCommande(entrepriseId, collecteId)
            onResult(succes)
        }
    }

    fun modifierTarif(qualite: QualiteJus, nouveauPrix: Double) {
        viewModelScope.launch {
            repository.modifierTarif(qualite, nouveauPrix)
        }
    }

    fun getProfileId(utilisateurId: Long, onResult: (Long?) -> Unit) {
        viewModelScope.launch {
            val profileId = repository.getProfileId(utilisateurId)
            onResult(profileId)
        }
    }

    fun connecterProductrice(telephone: String, onResult: (Long?) -> Unit) {
        viewModelScope.launch {
            val utilisateur = repository.connecterProductrice(telephone)
            if (utilisateur != null && utilisateur.isVerified) {
                onResult(utilisateur.profileId)
            } else {
                onResult(null)
            }
        }
    }

    fun connecterEntreprise(nom: String, matricule: String, onResult: (Long?) -> Unit) {
        viewModelScope.launch {
            val utilisateur = repository.connecterEntreprise(nom, matricule)
            if (utilisateur != null && utilisateur.isVerified) {
                onResult(utilisateur.profileId)
            } else {
                onResult(null)
            }
        }
    }

    fun demanderBidon(productriceId: Long) {
        viewModelScope.launch {
            repository.demanderBidon(productriceId)
        }
    }

    fun validerCommande(commandeId: Long) {
        viewModelScope.launch {
            repository.validerCommande(commandeId)
        }
    }

    fun annulerCommande(commandeId: Long) {
        viewModelScope.launch {
            repository.annulerCommande(commandeId)
        }
    }

    fun supprimerProductrice(productriceId: Long) {
        viewModelScope.launch {
            repository.supprimerProductrice(productriceId)
        }
    }

    fun supprimerEntreprise(entrepriseId: Long) {
        viewModelScope.launch {
            repository.supprimerEntreprise(entrepriseId)
        }
    }

    init {
        viewModelScope.launch {
            repository.seedDemoData()
        }
    }
}
