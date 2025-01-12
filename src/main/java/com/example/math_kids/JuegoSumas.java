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

public class JuegoSumas extends AppCompatActivity {

    Button btnRes1P1suma, btnRes2P1suma, btnRes1P2suma, btnRes2P2suma;

    ProgressBar progressBarSuma;

    TextView ResPersona1, ResPersona2, resCorrectasP1suma, resCorrectasP2suma;

    private MediaPlayer
            correcto,
            victoria;

    int CorrectasP1suma = 0;
    int CorrectasP2suma = 0;

    Dialog dialogS;

    int maxRango, sumaCorrectaP1, sumaCorrectaP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego_sumas);

        btnRes1P1suma = findViewById(R.id.btnRes1P1suma);
        btnRes2P1suma = findViewById(R.id.btnRes2P1suma);
        btnRes1P2suma = findViewById(R.id.btnRes1P2suma);
        btnRes2P2suma = findViewById(R.id.btnRes2P2suma);

        progressBarSuma = findViewById(R.id.progressBarSuma);

        ResPersona1 = findViewById(R.id.resPersona1suma);
        ResPersona2 = findViewById(R.id.resPersona2suma);

        resCorrectasP1suma = findViewById(R.id.resCorrectasP1suma);
        resCorrectasP2suma = findViewById(R.id.resCorrectasP2suma);

        correcto = MediaPlayer.create(this, R.raw.correcto);


        progressBarSuma.setMax(10);
        progressBarSuma.setMin(-10);
        progressBarSuma.setProgress(0);

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

        GenerarSumas();

        View.OnClickListener btnListenerP1 = v -> {
            Button btn = (Button) v;
            int respuesta = Integer.parseInt(btn.getText().toString());

            if (respuesta == sumaCorrectaP1) {
                CorrectasP1suma++;
                resCorrectasP1suma.setText(String.valueOf(CorrectasP1suma));

                progressBarSuma.incrementProgressBy(1);  // Jugador 1 acierta, mueve la barra a la derecha
                if (progressBarSuma.getProgress() >= progressBarSuma.getMax()) {
                    determinarGanador("Jugador 1");
                }
                correcto.start(); // Sonido de respuesta correcta
            } else {
                CorrectasP2suma++;
                resCorrectasP2suma.setText(String.valueOf(CorrectasP2suma));
            }

            GenerarSumas();
        };

        View.OnClickListener btnListenerP2 = v -> {
            Button btn = (Button) v;
            int respuesta = Integer.parseInt(btn.getText().toString());

            if (respuesta == sumaCorrectaP2) {
                CorrectasP2suma++;
                resCorrectasP2suma.setText(String.valueOf(CorrectasP2suma));

                progressBarSuma.incrementProgressBy(-1);  // Jugador 2 acierta, mueve la barra a la izquierda
                if (progressBarSuma.getProgress() <= progressBarSuma.getMin()) {
                    determinarGanador("Jugador 2");
                }
                correcto.start(); // Sonido de respuesta correcta
            } else {
                CorrectasP1suma++;
                resCorrectasP1suma.setText(String.valueOf(CorrectasP1suma));
            }

            GenerarSumas();
        };

        btnRes1P1suma.setOnClickListener(btnListenerP1);
        btnRes2P1suma.setOnClickListener(btnListenerP1);
        btnRes1P2suma.setOnClickListener(btnListenerP2);
        btnRes2P2suma.setOnClickListener(btnListenerP2);


        dialogS = new Dialog(JuegoSumas.this);
        dialogS.setContentView(R.layout.reintentar);
        dialogS.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogS.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogS.setCancelable(false);

        Button btnDialogJugar = dialogS.findViewById(R.id.btnJugar);
        Button btnDialogSalir = dialogS.findViewById(R.id.btnSalir);

        btnDialogJugar.setOnClickListener(v -> {
            reiniciarJuego();
            dialogS.dismiss();
        });



        btnDialogSalir.setOnClickListener(v -> {
            dialogS.dismiss();
            Intent intent = new Intent(JuegoSumas.this, MainActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void GenerarSumas() {
        // Jugador 1
        int num1P1 = generarNumeroAleatorio();
        int num2P1 = generarNumeroAleatorio();

        if (num1P1 < num2P1) {
            int temp = num1P1;
            num1P1 = num2P1;
            num2P1 = temp;
        }
        sumaCorrectaP1 = num1P1 + num2P1;

        btnRes1P1suma.setText(String.valueOf(num1P1));
        btnRes2P1suma.setText(String.valueOf(num2P1));

        ResPersona1.setText(num1P1 + " + " + num2P1 + "=  ?");

        int opIncorrecta1P1 = sumaCorrectaP1 + generarNumeroAleatorio() - 2;

        List<Integer> op1 = Arrays.asList(sumaCorrectaP1, opIncorrecta1P1);
        Collections.shuffle(op1);

        btnRes1P1suma.setText(String.valueOf(op1.get(0)));
        btnRes2P1suma.setText(String.valueOf(op1.get(1)));

        // Jugador 2
        int num1P2 = generarNumeroAleatorio();
        int num2P2 = generarNumeroAleatorio();

        if (num1P2 < num2P2) {
            int temp = num1P2;
            num1P2 = num2P2;
            num2P2 = temp;
        }
        sumaCorrectaP2 = num1P2 + num2P2;

        ResPersona2.setText(num1P2 + " + " + num2P2);
        int opIncorrecta1P2 = sumaCorrectaP1 + generarNumeroAleatorio() - 2;

        List<Integer> op = Arrays.asList(sumaCorrectaP2, opIncorrecta1P2);
        Collections.shuffle(op);

        btnRes1P2suma.setText(String.valueOf(op.get(0)));
        btnRes2P2suma.setText(String.valueOf(op.get(1)));
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
        dialogS.show();

        btnRes1P1suma.setEnabled(false);
        btnRes2P1suma.setEnabled(false);
        btnRes1P2suma.setEnabled(false);
        btnRes2P2suma.setEnabled(false);
    }

    private void reiniciarJuego() {
        CorrectasP1suma = 0;
        CorrectasP2suma = 0;
        resCorrectasP1suma.setText(String.valueOf(CorrectasP1suma));
        resCorrectasP2suma.setText(String.valueOf(CorrectasP2suma));
        progressBarSuma.setProgress(0);

        btnRes1P1suma.setEnabled(true);
        btnRes2P1suma.setEnabled(true);
        btnRes1P2suma.setEnabled(true);
        btnRes2P2suma.setEnabled(true);

        GenerarSumas();
    }
}