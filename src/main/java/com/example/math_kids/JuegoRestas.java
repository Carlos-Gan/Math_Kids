package com.example.math_kids;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class JuegoRestas extends AppCompatActivity {

    Button btnRes1P1, btnRes2P1, btnRes1P2, btnRes2P2;

    ProgressBar progressBar;

    TextView ResPersona1, ResPersona2, resCorrectasP1, resCorrectasP2;

    private MediaPlayer
    correcto,
    victoria;

    int CorrectasP1=0;
    int CorrectasP2=0;

    int maxRango, restaCorrectaP1, restaCorrectaP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego_restas);

        btnRes1P1 = findViewById(R.id.btnRes1P1);
        btnRes2P1 = findViewById(R.id.btnRes2P1);
        btnRes1P2 = findViewById(R.id.btnRes1P2);
        btnRes2P2 = findViewById(R.id.btnRes2P2);

        progressBar = findViewById(R.id.progressBar);

        ResPersona1 = findViewById(R.id.resPersona1);
        ResPersona2 = findViewById(R.id.resPersona2);

        resCorrectasP1 = findViewById(R.id.resCorrectasP1);
        resCorrectasP2 = findViewById(R.id.resCorrectasP2);

        correcto = MediaPlayer.create(this, R.raw.correcto);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(10);
        progressBar.setMin(-10);
        progressBar.setProgress(0);

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        int savedNivel = sharedPreferences.getInt("savedNivel", 1);

        switch (savedNivel) {
            case 1:
                maxRango = 5;
                break;
            case 2:
                maxRango = 10;
                break;
            case 3:
                maxRango = 20;
                break;
            case 4:
                maxRango = 50;
                break;
        }

        GenerarRestas();

        View.OnClickListener btnListenerP1 = v -> {
            Button btn = (Button) v;
            int respuesta = Integer.parseInt(btn.getText().toString());

            if (respuesta == restaCorrectaP1) {
                CorrectasP1++;
                resCorrectasP1.setText(String.valueOf(CorrectasP1));

                progressBar.incrementProgressBy(1);  // Jugador 1 acierta, mueve la barra a la derecha
                if (progressBar.getProgress() >= progressBar.getMax()) {
                    determinarGanador("Jugador 1");
                }
                correcto.start(); // Sonido de respuesta correcta
            } else {
                CorrectasP2++;
                resCorrectasP2.setText(String.valueOf(CorrectasP2));
            }

            GenerarRestas();
        };

        View.OnClickListener btnListenerP2 = v -> {
            Button btn = (Button) v;
            int respuesta = Integer.parseInt(btn.getText().toString());

            if (respuesta == restaCorrectaP2) {
                CorrectasP2++;
                resCorrectasP2.setText(String.valueOf(CorrectasP2));

                progressBar.incrementProgressBy(-1);  // Jugador 2 acierta, mueve la barra a la izquierda
                if (progressBar.getProgress() <= progressBar.getMin()) {
                    determinarGanador("Jugador 2");
                }
                correcto.start(); // Sonido de respuesta correcta
            } else {
                CorrectasP1++;
                resCorrectasP1.setText(String.valueOf(CorrectasP1));
            }

            GenerarRestas();
        };

        btnRes1P1.setOnClickListener(btnListenerP1);
        btnRes2P1.setOnClickListener(btnListenerP1);
        btnRes1P2.setOnClickListener(btnListenerP2);
        btnRes2P2.setOnClickListener(btnListenerP2);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void GenerarRestas() {
        // Jugador 1
        int num1P1 = generarNumeroAleatorio();
        int num2P1 = generarNumeroAleatorio();

        if (num1P1 < num2P1) {
            int temp = num1P1;
            num1P1 = num2P1;
            num2P1 = temp;
        }
        restaCorrectaP1 = num1P1 - num2P1;

        btnRes1P1.setText(String.valueOf(num1P1));
        btnRes2P1.setText(String.valueOf(num2P1));

        ResPersona1.setText(num1P1 + " - " + num2P1 + "=  ?");

        int opIncorrecta1P1 = restaCorrectaP1 + generarNumeroAleatorio() - 2;

        List<Integer> op1 = Arrays.asList(restaCorrectaP1, opIncorrecta1P1);
        Collections.shuffle(op1);

        btnRes1P1.setText(String.valueOf(op1.get(0)));
        btnRes2P1.setText(String.valueOf(op1.get(1)));

        // Jugador 2
        int num1P2 = generarNumeroAleatorio();
        int num2P2 = generarNumeroAleatorio();

        if (num1P2 < num2P2) {
            int temp = num1P2;
            num1P2 = num2P2;
            num2P2 = temp;
        }
        restaCorrectaP2 = num1P2 - num2P2;

        ResPersona2.setText(num1P2 + " - " + num2P2);
        int opIncorrecta1P2 = restaCorrectaP1 + generarNumeroAleatorio() - 2;

        List<Integer> op = Arrays.asList(restaCorrectaP2, opIncorrecta1P2);
        Collections.shuffle(op);

        btnRes1P2.setText(String.valueOf(op.get(0)));
        btnRes2P2.setText(String.valueOf(op.get(1)));
    }

    private int generarNumeroAleatorio() {
        Random rand = new Random();
        return rand.nextInt(maxRango) + 1;
    }
    private void determinarGanador(String ganador) {
        MotionToast.Companion.createColorToast(this, "Ganador", ganador,
                MotionToastStyle.SUCCESS, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null);
        victoria = MediaPlayer.create(this, R.raw.victoria);
        victoria.start();
        btnRes1P1.setEnabled(false);
        btnRes2P1.setEnabled(false);
        btnRes1P2.setEnabled(false);
        btnRes2P2.setEnabled(false);


        Dialog dialog = new Dialog(JuegoRestas.this);
        dialog.setContentView(R.layout.reintentar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        Button btnDialogJugar = dialog.findViewById(R.id.btnJugar);
        Button btnDialogSalir = dialog.findViewById(R.id.btnSalir);

        dialog.show();

        btnDialogJugar.setOnClickListener(v -> {
            reiniciarJuego();
            dialog.dismiss();
        });

        btnDialogSalir.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(JuegoRestas.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void reiniciarJuego() {
        CorrectasP1 = 0;
        CorrectasP2 = 0;
        resCorrectasP1.setText(String.valueOf(CorrectasP1));
        resCorrectasP2.setText(String.valueOf(CorrectasP2));
        progressBar.setProgress(0);

        btnRes1P1.setEnabled(true);
        btnRes2P1.setEnabled(true);
        btnRes1P2.setEnabled(true);
        btnRes2P2.setEnabled(true);

        GenerarRestas();
    }


}