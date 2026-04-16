package com.example.alkewalletapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletapp.data.repository.WalletRepository
import com.example.alkewalletapp.model.Transaction

class WalletViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    private val _isDollar = MutableLiveData<Boolean>()
    val isDollar: LiveData<Boolean> get() = _isDollar

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        loadLocalData()
    }

    fun loadLocalData() {
        _transactions.value = WalletRepository.getLocalTransactions()
        _balance.value = WalletRepository.getBalance()
        _isDollar.value = WalletRepository.isDollar()
    }

    fun fetchTransactionsFromApi() {
        _isLoading.value = true
        WalletRepository.fetchAndSaveTransactions { apiTransactions ->
            _isLoading.value = false
            if (apiTransactions != null) {
                _transactions.value = apiTransactions
            }
        }
    }

    fun deposit(amount: Double) {
        WalletRepository.deposit(amount)
        loadLocalData()
    }

    fun withdraw(amount: Double): Boolean {
        val success = WalletRepository.withdraw(amount)
        if (success) {
            loadLocalData()
        }
        return success
    }

    fun toggleCurrency() {
        WalletRepository.toggleCurrency()
        loadLocalData()
    }
}