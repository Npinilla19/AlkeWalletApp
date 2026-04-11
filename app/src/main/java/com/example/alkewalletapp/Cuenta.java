package com.example.alkewalletapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

public class Cuenta extends AppCompatActivity {
    public Button BtnEnviarDinero;
    public Button Btn_ingresarDinero;
    public TextView tvBalanceAmount;
    public ImageView ivProfile;
    public LinearLayout llTransactionsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vincular vistas
        BtnEnviarDinero = findViewById(R.id.BtnEnviarDinero);
        Btn_ingresarDinero = findViewById(R.id.Btn_ingresarDinero);
        tvBalanceAmount = findViewById(R.id.tvBalanceAmount);
        ivProfile = findViewById(R.id.ivProfile);
        llTransactionsContainer = findViewById(R.id.llTransactionsContainer);

        // NAVEGACIÓN AL PERFIL
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Cuenta.this, Perfil.class);
            startActivity(intent);
        });

        updateUI();

        BtnEnviarDinero.setOnClickListener(view -> {
            Intent intent = new Intent(Cuenta.this, EnviarDinero.class);
            startActivity(intent);
        });

        Btn_ingresarDinero.setOnClickListener(view -> {
            Intent intent = new Intent(Cuenta.this, IngresoDinero.class);
            startActivity(intent);
        });
    }

    private void updateUI() {
        updateBalance();
        renderTransactions();
    }

    private void updateBalance() {
        double currentBalance = WalletManager.INSTANCE.getBalance();
        boolean isDollar = WalletManager.INSTANCE.isDollar();
        
        if (isDollar) {
            tvBalanceAmount.setText(String.format(Locale.US, "$%.2f", currentBalance));
        } else {
            tvBalanceAmount.setText(String.format(Locale.getDefault(), "$%,.0f", currentBalance).replace(',', '.'));
        }
    }

    private void renderTransactions() {
        if (llTransactionsContainer == null) return;
        
        llTransactionsContainer.removeAllViews();
        List<Transaction> transactions = WalletManager.INSTANCE.getTransactions();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Transaction transaction : transactions) {
            View itemView = inflater.inflate(R.layout.item_transaction, llTransactionsContainer, false);
            
            TextView tvName = itemView.findViewById(R.id.tvTransName);
            TextView tvDate = itemView.findViewById(R.id.tvTransDate);
            TextView tvAmount = itemView.findViewById(R.id.tvTransAmount);
            ImageView ivIcon = itemView.findViewById(R.id.ivTransIcon);

            tvName.setText(transaction.getName());
            tvDate.setText(transaction.getDate());
            
            String sign = transaction.isIncome() ? "+" : "-";
            tvAmount.setText(String.format(Locale.getDefault(), "%s$%,.0f", sign, transaction.getAmount()).replace(',', '.'));
            
            // Icono según sea ingreso o egreso
            ivIcon.setImageResource(transaction.isIncome() ? R.drawable.martita : R.drawable.juanito);

            llTransactionsContainer.addView(itemView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
