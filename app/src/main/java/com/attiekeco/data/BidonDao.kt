package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BidonDao {

    @Query("SELECT * FROM bidons ORDER BY id DESC")
    fun getAll(): Flow<List<Bidon>>

    @Query("SELECT * FROM bidons WHERE productriceId = :productriceId ORDER BY id DESC")
    fun getByProductrice(productriceId: Long): Flow<List<Bidon>>

    @Query("SELECT * FROM bidons WHERE statut = 'SIGNE_PLN' ORDER BY dateDeclaration ASC")
    fun getSignales(): Flow<List<Bidon>>

    @Query("SELECT * FROM bidons WHERE id = :id")
    suspend fun getById(id: Long): Bidon?

    @Insert
    suspend fun insert(bidon: Bidon): Long

    @Insert
    suspend fun insertAll(vararg bidons: Bidon)

    @Update
    suspend fun update(bidon: Bidon)

    @Query("SELECT COUNT(*) FROM bidons WHERE productriceId = :productriceId AND statut = 'EN_ATTENTE'")
    suspend fun countEnAttente(productriceId: Long): Int

    @Query("DELETE FROM bidons WHERE productriceId = :productriceId")
    suspend fun deleteByProductriceId(productriceId: Long)
}
