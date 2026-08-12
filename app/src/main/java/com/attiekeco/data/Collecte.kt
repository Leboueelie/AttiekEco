package com.attiekeco.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "collectes",
    foreignKeys = [
        ForeignKey(
            entity = Bidon::class,
            parentColumns = ["id"],
            childColumns = ["bidonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bidonId"), Index("dateCollecte")]
)
data class Collecte(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bidonId: Long,
    val nomAgent: String,
    val qualite: QualiteJus,
    val tour: TourProduction,
    val litresReels: Double,
    val montantPaye: Double,
    val dateCollecte: Date = Date(),
    val disponible: Boolean = true
)
