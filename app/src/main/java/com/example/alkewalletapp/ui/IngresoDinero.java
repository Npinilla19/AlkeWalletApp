package com.example.alkewalletapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.alkewalletapp.R;
import com.example.alkewalletapp.data.repository.WalletRepository;

public class IngresoDinero extends AppCompatActivity {

    private Button BtnIngresoD;
    private ImageView BtnBack;
    private EditText etCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingreso_dinero);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        BtnIngresoD = findViewById(R.id.BtnIngresoD);
        BtnBack = findViewById(R.id.btnBack);
        etCantidad = findViewById(R.id.etCantidad);


        BtnBack.setOnClickListener(view -> {
            Intent intent = new Intent(IngresoDinero.this, Cuenta.class);
            startActivity(intent);
            finish();
        });


        BtnIngresoD.setOnClickListener(view -> {
            String montoStr = etCantidad.getText().toString();
            
            if (!montoStr.isEmpty()) {
                try {
                    double monto = Double.parseDouble(montoStr);
                    

                    WalletRepository.INSTANCE.deposit(monto);
                    
                    Toast.makeText(IngresoDinero.this, "¡Dinero ingresado con éxito!", Toast.LENGTH_SHORT).show();
                    

                    Intent intent = new Intent(IngresoDinero.this, Cuenta.class);
                    startActivity(intent);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa un monto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}