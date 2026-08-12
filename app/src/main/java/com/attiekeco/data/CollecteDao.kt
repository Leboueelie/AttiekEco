package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CollecteDao {

    @Query("SELECT * FROM collectes ORDER BY dateCollecte DESC")
    fun getAll(): Flow<List<Collecte>>

    @Query("SELECT * FROM collectes WHERE bidonId = :bidonId")
    suspend fun getByBidon(bidonId: Long): Collecte?

    @Query("SELECT * FROM collectes WHERE id = :id")
    suspend fun getById(id: Long): Collecte?

    @Query("SELECT * FROM collectes WHERE disponible = 1 ORDER BY dateCollecte DESC")
    fun getDisponibles(): Flow<List<Collecte>>

    @Query("SELECT * FROM collectes WHERE disponible = 1 AND qualite = :qualite ORDER BY dateCollecte DESC")
    fun getDisponiblesByQualite(qualite: QualiteJus): Flow<List<Collecte>>

    @Query("SELECT * FROM collectes WHERE disponible = 1 AND tour = :tour ORDER BY dateCollecte DESC")
    fun getDisponiblesByTour(tour: TourProduction): Flow<List<Collecte>>

    @Query("SELECT * FROM collectes WHERE disponible = 1 AND qualite = :qualite AND tour = :tour ORDER BY dateCollecte DESC")
    fun getDisponiblesByQualiteAndTour(qualite: QualiteJus, tour: TourProduction): Flow<List<Collecte>>

    @Query("SELECT * FROM collectes WHERE dateCollecte BETWEEN :debut AND :fin ORDER BY dateCollecte DESC")
    fun getByPeriode(debut: Long, fin: Long): Flow<List<Collecte>>

    @Insert
    suspend fun insert(collecte: Collecte): Long

    @Query("UPDATE collectes SET disponible = 0 WHERE id = :collecteId")
    suspend fun marquerVendu(collecteId: Long)

    @Query("UPDATE collectes SET disponible = 1 WHERE id = :collecteId")
    suspend fun rendreDisponible(collecteId: Long)

    @Query("SELECT SUM(montantPaye) FROM collectes")
    fun getTotalMontant(): Flow<Double?>

    @Query("SELECT SUM(litresReels) FROM collectes")
    fun getTotalLitres(): Flow<Double?>
}
