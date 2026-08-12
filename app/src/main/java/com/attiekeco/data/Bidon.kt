package com.attiekeco.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

enum class StatutBidon {
    EN_ATTENTE,
    SIGNE_PLN,
    COLLECTE
}

@Entity(
    tableName = "bidons",
    foreignKeys = [
        ForeignKey(
            entity = Productrice::class,
            parentColumns = ["id"],
            childColumns = ["productriceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productriceId"), Index("statut")]
)
data class Bidon(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productriceId: Long,
    val statut: StatutBidon = StatutBidon.EN_ATTENTE,
    val qualite: QualiteJus? = null,
    val tour: TourProduction? = null,
    val litresReels: Double? = null,
    val dateDeclaration: Date? = null
)
