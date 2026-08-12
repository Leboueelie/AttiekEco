package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntrepriseDao {

    @Query("SELECT * FROM entreprises ORDER BY nomEntreprise ASC")
    fun getAll(): Flow<List<Entreprise>>

    @Query("SELECT * FROM entreprises WHERE id = :id")
    suspend fun getById(id: Long): Entreprise?

    @Query("SELECT COUNT(*) FROM entreprises")
    suspend fun count(): Int

    @Query("SELECT * FROM entreprises WHERE contact = :contact LIMIT 1")
    suspend fun getByContact(contact: String): Entreprise?

    @Query("SELECT * FROM entreprises WHERE nomEntreprise = :nom AND matricule = :matricule LIMIT 1")
    suspend fun getByNomAndMatricule(nom: String, matricule: String): Entreprise?

    @Insert
    suspend fun insert(entreprise: Entreprise): Long

    @Insert
    suspend fun insertAll(entreprises: List<Entreprise>)

    @Query("DELETE FROM entreprises WHERE id = :id")
    suspend fun deleteById(id: Long)
}
