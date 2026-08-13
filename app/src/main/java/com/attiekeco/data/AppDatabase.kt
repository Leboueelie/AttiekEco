package com.attiekeco.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Productrice::class,
        Bidon::class,
        Collecte::class,
        Entreprise::class,
        Utilisateur::class,
        GrilleTarifaire::class,
        Commande::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productriceDao(): ProductriceDao
    abstract fun bidonDao(): BidonDao
    abstract fun collecteDao(): CollecteDao
    abstract fun entrepriseDao(): EntrepriseDao
    abstract fun utilisateurDao(): UtilisateurDao
    abstract fun grilleTarifaireDao(): GrilleTarifaireDao
    abstract fun commandeDao(): CommandeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "attiekeko-db"
            )
                .fallbackToDestructiveMigration(true)
                .build()
    }
}
