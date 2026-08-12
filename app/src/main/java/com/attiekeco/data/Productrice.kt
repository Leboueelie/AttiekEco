package com.attiekeco.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productrices")
data class Productrice(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nom: String,
    val telephone: String,
    val localisation: String,
    val commune: String? = null,
    val cooperative: String? = null
)
