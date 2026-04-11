package com.example.alkewalletapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Perfil extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnLogout;
    private SwitchCompat switchCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        switchCurrency = findViewById(R.id.switchCurrency);

        // Estado inicial del Switch según el WalletManager
        switchCurrency.setChecked(WalletManager.INSTANCE.isDollar());

        // Manejo del Switch para cambiar la divisa
        switchCurrency.setOnCheckedChangeListener((buttonView, isChecked) -> {
            WalletManager.INSTANCE.toggleCurrency();
            String msg = isChecked ? "Saldo ahora en Dólares" : "Saldo ahora en Pesos Chilenos";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}