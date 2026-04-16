package com.example.alkewalletapp.data.repository

import android.content.Context
import com.example.alkewalletapp.data.api.RetrofitClient
import com.example.alkewalletapp.data.db.AppDatabase
import com.example.alkewalletapp.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio real que une la API y la base de datos local (Room).
 * Es la "única fuente de verdad" para el resto de la aplicación.
 */
object WalletRepository {
    private var balanceCLP: Double = 120000.0
    private const val EXCHANGE_RATE = 950.0
    private var isDollarView: Boolean = false
    
    private var database: AppDatabase? = null
    private val api = RetrofitClient.getApi()

    fun init(context: Context) {
        if (database == null) {
            database = AppDatabase.getDatabase(context)
        }
    }

    // --- LÓGICA DE SALDO ---

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

    // --- LÓGICA DE TRANSACCIONES (API + ROOM) ---

    /**
     * Obtiene las transacciones guardadas localmente en Room.
     */
    fun getLocalTransactions(): List<Transaction> {
        return database?.transactionDao()?.getAllTransactions() ?: emptyList()
    }

    /**
     * Sincroniza las transacciones desde la API y las guarda en Room.
     */
    fun fetchAndSaveTransactions(onResult: (List<Transaction>?) -> Unit) {
        api.getTransactions().enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(call: Call<List<Transaction>>, response: Response<List<Transaction>>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiTransactions = response.body()!!
                    
                    // Guardar en base de datos local (Room)
                    database?.transactionDao()?.insertAll(apiTransactions)
                    
                    onResult(apiTransactions)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    /**
     * Crea una nueva transacción (Ingreso o Envío).
     * En un entorno real, esto también enviaría un POST a la API.
     */
    fun deposit(amount: Double) {
        if (amount > 0) {
            balanceCLP += amount
            addTransactionLocally("Ingreso de Dinero", amount, true)
        }
    }

    fun withdraw(amount: Double): Boolean {
        return if (amount > 0 && amount <= balanceCLP) {
            balanceCLP -= amount
            addTransactionLocally("Envío de Dinero", amount, false)
            true
        } else {
            false
        }
    }

    private fun addTransactionLocally(name: String, amount: Double, isIncome: Boolean) {
        val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val newTransaction = Transaction(name = name, date = currentDate, amount = amount, isIncome = isIncome)
        
        // Guardar en Room inmediatamente
        database?.transactionDao()?.insertTransaction(newTransaction)
    }
}