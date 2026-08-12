package com.attiekeco.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entreprises")
data class Entreprise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nomEntreprise: String,
    val contact: String,
    val matricule: String,
    val type: TypeEntreprise,
    val zoneCollecte: String
)
