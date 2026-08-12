package com.attiekeco.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grille_tarifaire")
data class GrilleTarifaire(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val qualite: QualiteJus,
    val prixParLitre: Double
)
