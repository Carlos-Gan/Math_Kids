package com.example.math_kids;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private List<TextView> niveles;
    private TextView nivelSeleccionado;
    ImageButton btnRegresar;

    Switch MusicaSwitch;

    SharedPreferences prefs;

    MediaPlayer kahoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        MusicaSwitch = findViewById(R.id.MusicaSwitch);

        btnRegresar = findViewById(R.id.btnRegresar);

        niveles = Arrays.asList(
                findViewById(R.id.txt1a5),
                findViewById(R.id.txt1a10),
                findViewById(R.id.txt1a20),
                findViewById(R.id.txt1a50)
        );

        prefs= getSharedPreferences("config", MODE_PRIVATE);
        int savedNivel = prefs.getInt("savedNivel", 1);

        if (savedNivel >= 1 && savedNivel <= niveles.size()) {
            nivelSeleccionado = niveles.get(savedNivel - 1);
            setSelectedNivel(nivelSeleccionado);
        }

        for (TextView nivel : niveles) {
            nivel.setOnClickListener(v -> setSelectedNivel(nivel));
        }

        btnRegresar.setOnClickListener(v -> {
            finish();
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        boolean musicaActivada = prefs.getBoolean("musica", true);
        MusicaSwitch.setChecked(musicaActivada);


        MusicaSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
           MusicManager musicManager = MusicManager.getInstance(this);
           if (isChecked) {
               musicManager.startMusic();
           } else {
               musicManager.pauseMusic();
           }
           prefs.edit().putBoolean("musica", isChecked).apply();
        });

    }

    protected void onDestroy() {
        if (kahoot != null) {
            kahoot.release();
            kahoot = null;
        }
        super.onDestroy();
    }

    private void setSelectedNivel(TextView selected) {
        for (TextView txt : niveles) {
            txt.setSelected(false);
            txt.setBackgroundResource(R.drawable.redondo_transparente);
            txt.setTextColor(getResources().getColor(R.color.black));
        }

        // Marca el nivel seleccionado y cambia el color del texto
        selected.setSelected(true);
        selected.setBackgroundResource(R.drawable.selector_nivel);
        selected.setTextColor(getResources().getColor(R.color.white));
        nivelSeleccionado = selected;

        int selectedIndex = niveles.indexOf(selected);
        getSharedPreferences("config", MODE_PRIVATE)
                .edit()
                .putInt("savedNivel", selectedIndex + 1)
                .apply();

        // Log para depurar si es necesario
        System.out.println("Índice seleccionado: " + selectedIndex);
    }


}