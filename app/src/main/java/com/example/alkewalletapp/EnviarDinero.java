package com.example.alkewalletapp;

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

public class EnviarDinero extends AppCompatActivity {
    private Button BtnEnviarD;
    private ImageView BtnBack;
    private EditText etCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enviar_dinero);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vincular vistas
        BtnEnviarD = findViewById(R.id.BtnEnviarD);
        BtnBack = findViewById(R.id.btnBack);
        etCantidad = findViewById(R.id.etCantidad);

        // Volver a la pantalla anterior
        BtnBack.setOnClickListener(view -> {
            Intent intent = new Intent(EnviarDinero.this, Cuenta.class);
            startActivity(intent);
            finish();
        });

        // Lógica de Retiro/Envío de Dinero (Administración de fondos - Kotlin Integration)
        BtnEnviarD.setOnClickListener(view -> {
            String montoStr = etCantidad.getText().toString();
            
            if (!montoStr.isEmpty()) {
                try {
                    double monto = Double.parseDouble(montoStr);
                    
                    // LLAMADA A LA LÓGICA DE NEGOCIO EN KOTLIN (Withdraw)
                    boolean exito = WalletManager.INSTANCE.withdraw(monto);
                    
                    if (exito) {
                        Toast.makeText(EnviarDinero.this, "¡Dinero enviado con éxito!", Toast.LENGTH_SHORT).show();
                        
                        // Volver al Inicio para ver el saldo actualizado
                        Intent intent = new Intent(EnviarDinero.this, Cuenta.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Saldo insuficiente o monto inválido", Toast.LENGTH_SHORT).show();
                    }
                    
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa un monto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}