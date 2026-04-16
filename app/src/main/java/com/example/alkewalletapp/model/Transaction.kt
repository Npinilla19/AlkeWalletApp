package com.example.alkewalletapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)