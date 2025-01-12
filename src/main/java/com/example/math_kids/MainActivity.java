package com.example.math_kids;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageButton btnConfig;

    Button sudoku, btnSuma, btnResta, btnJuegoSumas, btnJuegoRestas;

    Dialog dialog;

    private boolean isMusicPlaying;
    Button btnDialogCancelar,btnDialogAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        isMusicPlaying = getSharedPreferences("config", MODE_PRIVATE).getBoolean("musica", true);
        if(isMusicPlaying){
            MusicManager.getInstance(this).startMusic();
        }
        setContentView(R.layout.activity_main);

        btnConfig = findViewById(R.id.btnConfig);
        btnSuma = findViewById(R.id.btnPracSumas);
        btnResta = findViewById(R.id.btnPracResta);
        btnJuegoRestas = findViewById(R.id.btnResta);
        btnJuegoSumas = findViewById(R.id.btnJuegoSumas);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_sudoku);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        btnDialogAceptar = dialog.findViewById(R.id.btnDialogAceptar);
        btnDialogCancelar = dialog.findViewById(R.id.btnDialogCancelar);

        btnDialogAceptar.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, Sudoku.class);
            startActivity(intent);
        });

        btnDialogCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnConfig.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnSuma.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Practica_Sumas.class);
            startActivity(intent);
                });

        btnResta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Practica_Restas.class);
            startActivity(intent);
        });

        btnJuegoRestas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JuegoRestas.class);
            startActivity(intent);
        });

        btnJuegoSumas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JuegoSumas.class);
            startActivity(intent);
        });

        sudoku = findViewById(R.id.btnSudoku);

        sudoku.setOnClickListener(v -> {
            dialog.show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            MusicManager.getInstance(this).pauseMusic();
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MusicManager.getInstance(this).pauseMusic();
    }

}