package com.attiekeco.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "commandes",
    foreignKeys = [
        ForeignKey(
            entity = Entreprise::class,
            parentColumns = ["id"],
            childColumns = ["entrepriseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Collecte::class,
            parentColumns = ["id"],
            childColumns = ["collecteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("entrepriseId"), Index("collecteId")]
)
data class Commande(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entrepriseId: Long,
    val collecteId: Long,
    val dateCommande: Date = Date(),
    val montant: Double,
    val statut: StatutCommande = StatutCommande.EN_COURS
)
