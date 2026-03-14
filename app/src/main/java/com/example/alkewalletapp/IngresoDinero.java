package com.example.alkewalletapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IngresoDinero extends AppCompatActivity {

    private Button BtnIngresoD;
    private ImageView BtnBack;



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

        BtnBack.setOnClickListener(view -> {
            Intent intent = new Intent(IngresoDinero.this, Cuenta.class);
            startActivity(intent);
        });


        BtnIngresoD.setOnClickListener(view -> {
            Toast.makeText(IngresoDinero.this, "Dinero ingresado con éxito", Toast.LENGTH_SHORT).show();

        });
    }
}