package com.example.bersihkan.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostalCode (
    @PrimaryKey
    val postalCodeIdx: Int,
    val postalCode: Int,
)