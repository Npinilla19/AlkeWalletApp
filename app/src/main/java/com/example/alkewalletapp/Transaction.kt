package com.example.alkewalletapp

/**
 * Clase de datos para representar una transacción.
 */
data class Transaction(
    val name: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean // true para ingreso (+), false para envío (-)
)