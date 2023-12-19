package com.example.bersihkan.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recommendation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postalCode: Int,
    val r1: Int,
    val r2: Int,
    val r3: Int,
    val r4: Int,
    val r5: Int,
    val r6: Int,
    val r7: Int,
    val r8: Int,
    val r9: Int,
    val r10: Int,
)
