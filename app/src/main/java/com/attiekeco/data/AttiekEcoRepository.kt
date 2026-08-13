package com.attiekeco.data

import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttiekEcoRepository(private val db: AppDatabase) {

    private val productriceDao = db.productriceDao()
    private val bidonDao = db.bidonDao()
    private val collecteDao = db.collecteDao()
    private val entrepriseDao = db.entrepriseDao()
    private val utilisateurDao = db.utilisateurDao()
    private val grilleTarifaireDao = db.grilleTarifaireDao()
    private val commandeDao = db.commandeDao()

    val productrices: Flow<List<Productrice>> = productriceDao.getAll()
    val entreprises: Flow<List<Entreprise>> = entrepriseDao.getAll()
    val bidonsSignales: Flow<List<Bidon>> = bidonDao.getSignales()
    val totalMontant: Flow<Double> = collecteDao.getTotalMontant().map { it ?: 0.0 }
    val totalLitres: Flow<Double> = collecteDao.getTotalLitres().map { it ?: 0.0 }
    val collectes: Flow<List<Collecte>> = collecteDao.getAll()
    val collectesDisponibles: Flow<List<Collecte>> = collecteDao.getDisponibles()
    val grilleTarifaire: Flow<List<GrilleTarifaire>> = grilleTarifaireDao.getAll()
    val commandes: Flow<List<Commande>> = commandeDao.getAll()

    fun bidonsForProductrice(productriceId: Long): Flow<List<Bidon>> =
        bidonDao.getByProductrice(productriceId)

    fun commandesForEntreprise(entrepriseId: Long): Flow<List<Commande>> =
        commandeDao.getByEntreprise(entrepriseId)

    suspend fun seedDemoData() {
        if (grilleTarifaireDao.count() == 0) {
            grilleTarifaireDao.insertAll(
                listOf(
                    GrilleTarifaire(tour = TourProduction.PREMIER, prixParLitre = Constants.PRIX_DEFAULT_PREMIER),
                    GrilleTarifaire(tour = TourProduction.DEUXIEME, prixParLitre = Constants.PRIX_DEFAULT_DEUXIEME)
                )
            )
        }
    }

    suspend fun inscrireProductrice(
        nom: String,
        telephone: String,
        localisation: String,
        commune: String?,
        cooperative: String?
    ): Long {
        val productriceId = productriceDao.insert(
            Productrice(
                nom = nom,
                telephone = telephone,
                localisation = localisation,
                commune = commune,
                cooperative = cooperative
            )
        )
        val utilisateurId = utilisateurDao.insert(
            Utilisateur(role = Role.PRODUCTRICE, profileId = productriceId, isVerified = false)
        )
        return utilisateurId
    }

    suspend fun inscrireEntreprise(
        nomEntreprise: String,
        contact: String,
        matricule: String,
        type: TypeEntreprise,
        zoneCollecte: String
    ): Long {
        val entrepriseId = entrepriseDao.insert(
            Entreprise(
                nomEntreprise = nomEntreprise,
                contact = contact,
                matricule = matricule,
                type = type,
                zoneCollecte = zoneCollecte
            )
        )
        val utilisateurId = utilisateurDao.insert(
            Utilisateur(role = Role.ENTREPRISE, profileId = entrepriseId, isVerified = false)
        )
        return utilisateurId
    }

    suspend fun verifierUtilisateur(utilisateurId: Long, code: String): Boolean {
        if (code != Constants.CODE_SMS_SIMULATION) return false
        val utilisateur = utilisateurDao.getById(utilisateurId) ?: return false
        utilisateurDao.update(utilisateur.copy(isVerified = true))
        return true
    }

    suspend fun signalerBidonPlein(
        bidonId: Long,
        qualite: QualiteJus,
        tour: TourProduction,
        litresReels: Double
    ): Boolean {
        val bidon = bidonDao.getById(bidonId) ?: return false
        if (bidon.statut != StatutBidon.EN_ATTENTE) return false

        val misAJour = bidon.copy(
            statut = StatutBidon.SIGNE_PLN,
            qualite = qualite,
            tour = tour,
            litresReels = litresReels,
            dateDeclaration = Date()
        )
        bidonDao.update(misAJour)
        return true
    }

    suspend fun confirmerCollecte(
        bidonId: Long,
        nomAgent: String
    ): Boolean {
        val bidon = bidonDao.getById(bidonId) ?: return false
        if (bidon.statut != StatutBidon.SIGNE_PLN) return false
        val qualite = bidon.qualite ?: QualiteJus.STANDARD
        val tour = bidon.tour ?: TourProduction.PREMIER
        val litresReels = bidon.litresReels ?: return false

        val grille = grilleTarifaireDao.getByTour(tour)
        val prixParLitre = grille?.prixParLitre ?: when (tour) {
            TourProduction.PREMIER -> Constants.PRIX_DEFAULT_PREMIER
            TourProduction.DEUXIEME -> Constants.PRIX_DEFAULT_DEUXIEME
        }
        val montant = litresReels * prixParLitre

        collecteDao.insert(
            Collecte(
                bidonId = bidonId,
                nomAgent = nomAgent,
                qualite = qualite,
                tour = tour,
                litresReels = litresReels,
                montantPaye = montant,
                dateCollecte = Date(),
                disponible = true
            )
        )

        bidonDao.update(bidon.copy(statut = StatutBidon.COLLECTE))
        return true
    }

    suspend fun passerCommande(
        entrepriseId: Long,
        collecteId: Long
    ): Boolean {
        val collecte = collecteDao.getById(collecteId) ?: return false
        if (!collecte.disponible) return false
        val commande = Commande(
            entrepriseId = entrepriseId,
            collecteId = collecteId,
            montant = collecte.montantPaye,
            statut = StatutCommande.EN_COURS
        )
        commandeDao.insert(commande)
        collecteDao.marquerVendu(collecteId)
        return true
    }

    suspend fun validerCommande(commandeId: Long) {
        commandeDao.updateStatut(commandeId, StatutCommande.VALIDEE)
    }

    suspend fun annulerCommande(commandeId: Long) {
        val commande = commandeDao.getById(commandeId) ?: return
        commandeDao.updateStatut(commandeId, StatutCommande.ANNULEE)
        collecteDao.rendreDisponible(commande.collecteId)
    }

    suspend fun modifierTarif(tour: TourProduction, nouveauPrix: Double) {
        val existant = grilleTarifaireDao.getByTour(tour)
        if (existant != null) {
            grilleTarifaireDao.update(existant.copy(prixParLitre = nouveauPrix))
        } else {
            grilleTarifaireDao.insert(GrilleTarifaire(tour = tour, prixParLitre = nouveauPrix))
        }
    }

    suspend fun getUtilisateurs(): List<Utilisateur> {
        return utilisateurDao.getAllOnce()
    }

    suspend fun getProfileId(utilisateurId: Long): Long? {
        return utilisateurDao.getProfileIdById(utilisateurId)
    }

    suspend fun connecterProductrice(telephone: String): Utilisateur? {
        val productrice = productriceDao.getByTelephone(telephone) ?: return null
        return utilisateurDao.getByProfileIdAndRole(productrice.id, Role.PRODUCTRICE)
    }

    suspend fun connecterEntreprise(nom: String, matricule: String): Utilisateur? {
        val entreprise = entrepriseDao.getByNomAndMatricule(nom, matricule) ?: return null
        return utilisateurDao.getByProfileIdAndRole(entreprise.id, Role.ENTREPRISE)
    }

    suspend fun demanderBidon(productriceId: Long) {
        bidonDao.insert(Bidon(productriceId = productriceId, statut = StatutBidon.EN_ATTENTE))
    }

    suspend fun supprimerProductrice(productriceId: Long) {
        bidonDao.deleteByProductriceId(productriceId)
        utilisateurDao.deleteByProfileId(productriceId)
        productriceDao.deleteById(productriceId)
    }

    suspend fun supprimerEntreprise(entrepriseId: Long) {
        utilisateurDao.deleteByProfileId(entrepriseId)
        entrepriseDao.deleteById(entrepriseId)
    }
}
