package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UtilisateurDao {

    @Query("SELECT * FROM utilisateurs WHERE role = :role")
    fun getByRole(role: Role): Flow<List<Utilisateur>>

    @Query("SELECT * FROM utilisateurs WHERE role = :role")
    suspend fun getByRoleOnce(role: Role): List<Utilisateur>

    @Query("SELECT * FROM utilisateurs")
    suspend fun getAllOnce(): List<Utilisateur>

    @Query("SELECT profileId FROM utilisateurs WHERE id = :id LIMIT 1")
    suspend fun getProfileIdById(id: Long): Long?

    @Query("SELECT * FROM utilisateurs WHERE id = :id")
    suspend fun getById(id: Long): Utilisateur?

    @Query("SELECT * FROM utilisateurs WHERE profileId = :profileId AND role = :role LIMIT 1")
    suspend fun getByProfileIdAndRole(profileId: Long, role: Role): Utilisateur?

    @Query("SELECT COUNT(*) FROM utilisateurs")
    suspend fun count(): Int

    @Insert
    suspend fun insert(utilisateur: Utilisateur): Long

    @Update
    suspend fun update(utilisateur: Utilisateur)

    @Query("DELETE FROM utilisateurs WHERE profileId = :profileId AND role = :role")
    suspend fun deleteByProfileIdAndRole(profileId: Long, role: Role)

    @Query("DELETE FROM utilisateurs WHERE profileId = :profileId")
    suspend fun deleteByProfileId(profileId: Long)
}
