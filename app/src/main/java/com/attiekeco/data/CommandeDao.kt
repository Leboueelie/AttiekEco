package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandeDao {

    @Query("SELECT * FROM commandes ORDER BY dateCommande DESC")
    fun getAll(): Flow<List<Commande>>

    @Query("SELECT * FROM commandes WHERE entrepriseId = :entrepriseId ORDER BY dateCommande DESC")
    fun getByEntreprise(entrepriseId: Long): Flow<List<Commande>>

    @Query("SELECT * FROM commandes WHERE id = :id")
    suspend fun getById(id: Long): Commande?

    @Insert
    suspend fun insert(commande: Commande): Long

    @Query("UPDATE commandes SET statut = :statut WHERE id = :commandeId")
    suspend fun updateStatut(commandeId: Long, statut: StatutCommande)
}
