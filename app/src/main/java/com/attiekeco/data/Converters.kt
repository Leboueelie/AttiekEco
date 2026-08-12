package com.attiekeco.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromStatutBidon(statut: StatutBidon): String = statut.name

    @TypeConverter
    fun toStatutBidon(value: String): StatutBidon = runCatching {
        StatutBidon.valueOf(value)
    }.getOrDefault(StatutBidon.EN_ATTENTE)

    @TypeConverter
    fun fromRole(role: Role): String = role.name

    @TypeConverter
    fun toRole(value: String): Role = runCatching {
        Role.valueOf(value)
    }.getOrDefault(Role.PRODUCTRICE)

    @TypeConverter
    fun fromTypeEntreprise(type: TypeEntreprise): String = type.name

    @TypeConverter
    fun toTypeEntreprise(value: String): TypeEntreprise = runCatching {
        TypeEntreprise.valueOf(value)
    }.getOrDefault(TypeEntreprise.AUTRE)

    @TypeConverter
    fun fromQualiteJus(qualite: QualiteJus): String = qualite.name

    @TypeConverter
    fun toQualiteJus(value: String): QualiteJus = runCatching {
        QualiteJus.valueOf(value)
    }.getOrDefault(QualiteJus.STANDARD)

    @TypeConverter
    fun fromTourProduction(tour: TourProduction): String = tour.name

    @TypeConverter
    fun toTourProduction(value: String): TourProduction = runCatching {
        TourProduction.valueOf(value)
    }.getOrDefault(TourProduction.PREMIER)

    @TypeConverter
    fun fromStatutCommande(statut: StatutCommande): String = statut.name

    @TypeConverter
    fun toStatutCommande(value: String): StatutCommande = runCatching {
        StatutCommande.valueOf(value)
    }.getOrDefault(StatutCommande.EN_COURS)

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }
}
