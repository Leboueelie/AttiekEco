package com.attiekeco.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utilisateurs")
data class Utilisateur(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: Role,
    val profileId: Long,
    val isVerified: Boolean = false
)
