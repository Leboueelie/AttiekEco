package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GrilleTarifaireDao {

    @Query("SELECT * FROM grille_tarifaire ORDER BY qualite ASC")
    fun getAll(): Flow<List<GrilleTarifaire>>

    @Query("SELECT * FROM grille_tarifaire WHERE qualite = :qualite LIMIT 1")
    suspend fun getByQualite(qualite: QualiteJus): GrilleTarifaire?

    @Query("SELECT COUNT(*) FROM grille_tarifaire")
    suspend fun count(): Int

    @Insert
    suspend fun insert(grille: GrilleTarifaire): Long

    @Insert
    suspend fun insertAll(grilles: List<GrilleTarifaire>)

    @Update
    suspend fun update(grille: GrilleTarifaire)
}
