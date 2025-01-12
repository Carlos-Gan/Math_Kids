package com.example.math_kids;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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

public class Practica_Restas extends AppCompatActivity {

    Button btnRes1;
    Button btnRes2;
    Button btnRes3;
    TextView textNum1, textNum2, txtRes;
    TextView txtEstrellas;

    int maxRango, restaCorrecta;


    private MediaPlayer
            correcta,
            drama;

    int estrellas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practica_restas);

        btnRes1 = findViewById(R.id.btnRes1res);
        btnRes2 = findViewById(R.id.btnRes2res);
        btnRes3 = findViewById(R.id.btnRes3res);

        textNum1 = findViewById(R.id.textNum1Resta);
        textNum2 = findViewById(R.id.textNum2Resta);
        txtRes = findViewById(R.id.txtResResta);

        txtEstrellas = findViewById(R.id.txtEstrellas);

        correcta = MediaPlayer.create(this, R.raw.correcta);
        drama = MediaPlayer.create(this, R.raw.drama);

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        int savedNivel = sharedPreferences.getInt("savedNivel", 1);

        estrellas = sharedPreferences.getInt("estrellas resta", 0);
        txtEstrellas.setText(String.valueOf(estrellas));

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

        System.out.println(savedNivel);

        generarNuevaResta();

        View.OnClickListener btnListener = v -> {
            Button btn = (Button) v;
            int respuesta = Integer.parseInt(btn.getText().toString());

            if (respuesta == restaCorrecta) {
                correcta.start();
                MotionToast.Companion.createColorToast(this, "Correcto", "Respuesta correcta", MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null);
                estrellas++;
            } else {
                drama.start();
                MotionToast.Companion.createColorToast(this, "Error", "Respuesta equivocada", MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null);
                estrellas--;

            }
            darEstrellas();

            new Handler().postDelayed(this::generarNuevaResta, 2500);
        };

        btnRes1.setOnClickListener(btnListener);
        btnRes2.setOnClickListener(btnListener);
        btnRes3.setOnClickListener(btnListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void generarNuevaResta() {
        Random rand = new Random();

        int num1 = rand.nextInt(maxRango) + 1;
        int num2 = rand.nextInt(maxRango) + 1;

        if (num1 < num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }
        restaCorrecta = num1 - num2;

        textNum1.setText(String.valueOf(num1));
        textNum2.setText(String.valueOf(num2));

        int opIncorrecta1 = restaCorrecta + rand.nextInt(5) - 2;
        int opIncorrecta2 = restaCorrecta + rand.nextInt(5) + 2;

        if (opIncorrecta1 == restaCorrecta || opIncorrecta1 < 0) opIncorrecta1 += 1;
        if (opIncorrecta2 == restaCorrecta || opIncorrecta2 < 0 || opIncorrecta1 == opIncorrecta2)
            opIncorrecta2 += 1;


        List<Integer> opciones = Arrays.asList(restaCorrecta, opIncorrecta1, opIncorrecta2);
        Collections.shuffle(opciones);


        btnRes1.setText(String.valueOf(opciones.get(0)));
        btnRes2.setText(String.valueOf(opciones.get(1)));
        btnRes3.setText(String.valueOf(opciones.get(2)));

    }

    private void darEstrellas(){
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("estrellas resta", estrellas);
        editor.apply();
        txtEstrellas.setText(String.valueOf(estrellas));
    }
}