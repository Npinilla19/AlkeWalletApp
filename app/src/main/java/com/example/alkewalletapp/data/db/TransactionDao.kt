package com.example.alkewalletapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alkewalletapp.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(transactions: List<Transaction>)

    @Query("DELETE FROM transactions")
    fun deleteAll()
}