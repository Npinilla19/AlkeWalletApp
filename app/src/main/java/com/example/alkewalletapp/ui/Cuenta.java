package com.example.alkewalletapp.ui;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.alkewalletapp.R;
import com.example.alkewalletapp.model.Transaction;
import com.example.alkewalletapp.ui.viewmodel.WalletViewModel;

import java.util.List;
import java.util.Locale;

public class Cuenta extends AppCompatActivity {
    public Button BtnEnviarDinero;
    public Button Btn_ingresarDinero;
    public TextView tvBalanceAmount;
    public ImageView ivProfile;
    public LinearLayout llTransactionsContainer;
    private WalletViewModel viewModel;


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


        viewModel = new ViewModelProvider(this).get(WalletViewModel.class);

        BtnEnviarDinero = findViewById(R.id.BtnEnviarDinero);
        Btn_ingresarDinero = findViewById(R.id.Btn_ingresarDinero);
        tvBalanceAmount = findViewById(R.id.tvBalanceAmount);
        ivProfile = findViewById(R.id.ivProfile);
        llTransactionsContainer = findViewById(R.id.llTransactionsContainer);


        setupObservers();


        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Cuenta.this, Perfil.class);
            startActivity(intent);
        });

        BtnEnviarDinero.setOnClickListener(view -> {
            Intent intent = new Intent(Cuenta.this, EnviarDinero.class);
            startActivity(intent);
        });

        Btn_ingresarDinero.setOnClickListener(view -> {
            Intent intent = new Intent(Cuenta.this, IngresoDinero.class);
            startActivity(intent);
        });


        viewModel.fetchTransactionsFromApi();
    }

    private void setupObservers() {

        viewModel.getTransactions().observe(this, this::renderTransactions);


        viewModel.getBalance().observe(this, balance -> {
            updateBalanceUI(balance, viewModel.isDollar().getValue());
        });
        
        viewModel.isDollar().observe(this, isDollar -> {
            updateBalanceUI(viewModel.getBalance().getValue(), isDollar);
        });
    }

    private void updateBalanceUI(Double balance, Boolean isDollar) {
        if (balance == null || isDollar == null) return;
        
        if (isDollar) {
            tvBalanceAmount.setText(String.format(Locale.US, "$%.2f", balance));
        } else {
            tvBalanceAmount.setText(String.format(Locale.getDefault(), "$%,.0f", balance).replace(',', '.'));
        }
    }

    private void renderTransactions(List<Transaction> transactions) {
        if (llTransactionsContainer == null || transactions == null) return;
        
        llTransactionsContainer.removeAllViews();
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
            

            ivIcon.setImageResource(transaction.isIncome() ? R.drawable.martita : R.drawable.juanito);

            llTransactionsContainer.addView(itemView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadLocalData();
    }
}
