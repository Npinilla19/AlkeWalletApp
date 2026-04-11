package com.example.alkewalletapp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Objeto Singleton para gestionar el saldo y las transacciones de la billetera.
 */
object WalletManager {
    private var balanceCLP: Double = 120000.0
    private const val EXCHANGE_RATE = 950.0
    private var isDollarView: Boolean = false

    // Lista de transacciones
    private val transactions = mutableListOf<Transaction>()

    init {
        // Transacciones iniciales de ejemplo
        transactions.add(Transaction("Juanito", "Oct 14, 10:24 AM", 15000.0, false))
        transactions.add(Transaction("Martita", "Oct 12, 02:13 PM", 20500.0, true))
    }

    fun getBalance(): Double {
        return if (isDollarView) {
            balanceCLP / EXCHANGE_RATE
        } else {
            balanceCLP
        }
    }

    fun isDollar(): Boolean = isDollarView

    fun toggleCurrency() {
        isDollarView = !isDollarView
    }

    fun getTransactions(): List<Transaction> = transactions

    fun deposit(amount: Double) {
        if (amount > 0) {
            balanceCLP += amount
            addTransaction("Ingreso de Dinero", amount, true)
        }
    }

    fun withdraw(amount: Double): Boolean {
        return if (amount > 0 && amount <= balanceCLP) {
            balanceCLP -= amount
            addTransaction("Envío de Dinero", amount, false)
            true
        } else {
            false
        }
    }

    private fun addTransaction(name: String, amount: Double, isIncome: Boolean) {
        val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
        val currentDate = sdf.format(Date())
        transactions.add(0, Transaction(name, currentDate, amount, isIncome))
    }
}