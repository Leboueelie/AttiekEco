package com.attiekeco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductriceDao {

    @Query("SELECT * FROM productrices ORDER BY nom ASC")
    fun getAll(): Flow<List<Productrice>>

    @Query("SELECT * FROM productrices WHERE id = :id")
    suspend fun getById(id: Long): Productrice?

    @Query("SELECT COUNT(*) FROM productrices")
    suspend fun count(): Int

    @Query("SELECT * FROM productrices WHERE REPLACE(REPLACE(telephone, ' ', ''), '+225', '') = REPLACE(REPLACE(:telephone, ' ', ''), '+225', '') LIMIT 1")
    suspend fun getByTelephone(telephone: String): Productrice?

    @Insert
    suspend fun insert(productrice: Productrice): Long

    @Insert
    suspend fun insertAll(vararg productrices: Productrice)

    @Update
    suspend fun update(productrice: Productrice)

    @Query("DELETE FROM productrices WHERE id = :id")
    suspend fun deleteById(id: Long)
}
