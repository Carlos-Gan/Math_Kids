package com.example.math_kids;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class Sudoku extends AppCompatActivity {

    TextView celdaSeleccionada = null;
    ImageButton btnRegresar, btnLimpiar;

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    GridLayout gridSudoku;
    SudokuBoard board;
    TextView[] celdas = new TextView[81];

    LottieAnimationView animationView;
    TextView celdaAntesSeleccionada = null;

    int dificultad = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sudoku);

        animationView = findViewById(R.id.animationView);
        animationView.setVisibility(View.INVISIBLE);

        gridSudoku = findViewById(R.id.GridLayOutSudoku);

        btnRegresar = findViewById(R.id.btnRegresar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        board = new SudokuBoard();

        board.imprimirTablero();


        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        iniciarTablero();

        btnLimpiar.setOnClickListener(v -> {
            if (celdaSeleccionada != null) {
                celdaSeleccionada.setText("");
            }
        });

        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(Sudoku.this, MainActivity.class);
            startActivity(intent);
        });

        btn1.setOnClickListener(v -> ingresarNumero(1));
        btn2.setOnClickListener(v -> ingresarNumero(2));
        btn3.setOnClickListener(v -> ingresarNumero(3));
        btn4.setOnClickListener(v -> ingresarNumero(4));
        btn5.setOnClickListener(v -> ingresarNumero(5));
        btn6.setOnClickListener(v -> ingresarNumero(6));
        btn7.setOnClickListener(v -> ingresarNumero(7));
        btn8.setOnClickListener(v -> ingresarNumero(8));
        btn9.setOnClickListener(v -> ingresarNumero(9));

        for (int i = 0; i < gridSudoku.getChildCount(); i++) {
            TextView celda = (TextView) gridSudoku.getChildAt(i);
            celda.setOnClickListener(v -> {
                int index = (int) v.getTag();
                seleccionarCeldas(index);
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
    }

    private void seleccionarCeldas(int index) {
        // Verificar que el índice es válido
        if (index < 0 || index >= celdas.length) {
            Log.e("Sudoku", "Índice fuera de rango: " + index);
            return;
        }

        // Restaurar la celda previamente seleccionada
        if (celdaAntesSeleccionada != null) {
            celdaAntesSeleccionada.setBackgroundResource(R.drawable.celda_background);
            celdaAntesSeleccionada.invalidate();
        }

        // Obtener la nueva celda seleccionada
        celdaSeleccionada = celdas[index];

        // Verificar si la celda es editable antes de seleccionarla
        if (celdaSeleccionada != null && celdaSeleccionada.isEnabled()) {
            celdaSeleccionada.setBackgroundResource(R.drawable.sudoku_celda_seleccionada);
            celdaSeleccionada.invalidate();
            celdaAntesSeleccionada = celdaSeleccionada;
        } else {
            Log.d("Sudoku", "La celda seleccionada no es editable o es null.");
        }
    }

    private void iniciarTablero() {
        for (int i = 0; i < gridSudoku.getChildCount(); i++) {
            TextView celda = (TextView) gridSudoku.getChildAt(i);
            if (celda == null) continue;
            celda.setTag(i);
            celdas[i] = celda; // Asegúrate de asignar la celda al array

            int fila = i / 9;
            int col = i % 9;
            int valor = board.getValor(fila, col);

            if (Math.random() < (double) dificultad / 100) {
                celda.setText("");
                celda.setEnabled(true);
                board.tablero[fila][col] = 0;
            } else {
                celda.setText(String.valueOf(valor));
                celda.setEnabled(false);
            }
        }
    }

    private void ingresarNumero(int numero) {
        if (celdaSeleccionada != null) {
            int index = (int) celdaSeleccionada.getTag();
            int fila = index / 9;
            int col = index % 9;

            if (board.tablero[fila][col] == 0) { // Solo permitir cambios en celdas editables
                if (board.puedeColocarse(fila, col, numero)) {
                    celdaSeleccionada.setText(String.valueOf(numero));
                    board.tablero[fila][col] = numero; // Actualizar el tablero interno

                    int contador = contarNumeroEnTablero(numero);
                    if (contador >= 9) {
                        desactivarBoton(numero);
                    }
                    if (tableroCompleto()) {
                        animationView.setVisibility(View.VISIBLE);
                        animationView.playAnimation();

                        mostrarFelicitaciones();
                    }

                } else {
                    Toast.makeText(this, "Número incorrecto", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No puedes cambiar esta celda", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Selecciona una celda primero", Toast.LENGTH_SHORT).show();
        }
    }

    private int contarNumeroEnTablero(int numero) {
        int count = 0;
        for (int i = 0; i < 81; i++) {
            int fila = i / 9;
            int col = i % 9;
            if (board.getValor(fila, col) == numero) {
                count++;
            }
        }
        return count;
    }

    private void desactivarBoton(int numero) {
        Button boton = findViewById(getResources().getIdentifier("btn" + numero, "id", getPackageName()));
        if (boton != null) {
            boton.setEnabled(false);
            boton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean tableroCompleto() {
        // Verificar que todas las celdas están llenas y cumplen las reglas del Sudoku
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                int valor = board.getValor(fila, col);
                if (valor == 0 || !board.puedeColocarse(fila, col, valor)) {
                    return false;
                }
            }
        }

        // Verificar si todos los botones de números están invisibles
        for (int i = 1; i <= 9; i++) {
            Button boton = findViewById(getResources().getIdentifier("btn" + i, "id", getPackageName()));
            if (boton != null && boton.getVisibility() == View.VISIBLE) {
                return false;
            }
        }

        return true; // El tablero está completo y los botones están invisibles
    }

    private void mostrarFelicitaciones() {
        Toast.makeText(this, "¡Felicidades, has completado el Sudoku!", Toast.LENGTH_SHORT).show();
    }
}
