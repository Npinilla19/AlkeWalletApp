package com.example.alkewalletapp

import android.app.Application
import com.example.alkewalletapp.data.repository.WalletRepository

class AlkeWalletApp : Application() {
    override fun onCreate() {
        super.onCreate()
        WalletRepository.init(this)
    }
}