package com.example.math_kids;

import android.util.Log;

import java.util.Random;

public class SudokuBoard {
    int[][] tablero = new int[9][9];
    int dificultad;

    public SudokuBoard() {
        generarSudoku();
        crearDesafio(dificultad);
    }

    private void generarSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j] = 0;
            }
        }
        if (llenarTableroConBacktracking()) {
            System.out.println("Tablero generado exitosamente.");
        } else {
            System.out.println("No se pudo generar un Sudoku válido.");
        }
    }

    private boolean llenarTableroConBacktracking() {
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                if (tablero[fila][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (puedeColocarse(fila, col, num)) {
                            tablero[fila][col] = num;
                            if (llenarTableroConBacktracking()) {
                                return true;
                            }
                            tablero[fila][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean puedeColocarse(int fila, int col, int num) {
        Log.d("Sudoku", "Intentando colocar " + num + " en (" + fila + ", " + col + ")");
        // Verificar fila
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i] == num) {
                Log.d("Sudoku", "Número " + num + " ya está en fila " + fila + " en la posición " + i);
                return false;
            }
        }
        // Verificar columna
        for (int i = 0; i < 9; i++) {
            if (tablero[i][col] == num) {
                Log.d("Sudoku", "Número " + num + " ya está en columna " + col + " en la posición " + i);
                return false;
            }
        }
        // Verificar subcuadro
        int inicioFila = (fila / 3) * 3;
        int inicioCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[inicioFila + i][inicioCol + j] == num) {
                    Log.d("Sudoku", "Número " + num + " ya está en el subcuadro comenzando en (" + inicioFila + ", " + inicioCol + ")");
                    return false;
                }
            }
        }
        Log.d("Sudoku", "Número " + num + " puede colocarse en (" + fila + ", " + col + ")");
        return true;
    }


    public int getValor(int fila, int col) {
        if (fila < 0 || fila >= 9 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Índices fuera de rango: Fila: " + fila + ", Columna: " + col);
        }
        return tablero[fila][col];
    }


    // Mostrar el tablero para propósitos de depuración
    public void imprimirTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void crearDesafio(int celdasVacias) {
        Random random = new Random();
        while (celdasVacias > 0) {
            int fila = random.nextInt(9);
            int col = random.nextInt(9);
            if (tablero[fila][col] != 0) {
                int respaldo = tablero[fila][col];
                tablero[fila][col] = 0;

                // Verificar unicidad de la solución
                if (!tieneSolucionUnica()) {
                    tablero[fila][col] = respaldo; // Restaurar si no es único
                } else {
                    celdasVacias--;
                }
            }
        }
    }

    private boolean tieneSolucionUnica() {
        int[][] copiaTablero = copiarTablero();
        return contarSoluciones(copiaTablero, 0, 0) == 1;
    }

    private int contarSoluciones(int[][] tablero, int fila, int col) {
        if (fila == 9) return 1;
        if (col == 9) return contarSoluciones(tablero, fila + 1, 0);
        if (tablero[fila][col] != 0) return contarSoluciones(tablero, fila, col + 1);

        int soluciones = 0;
        for (int num = 1; num <= 9; num++) {
            if (puedeColocarse(fila, col, num)) {
                tablero[fila][col] = num;
                soluciones += contarSoluciones(tablero, fila, col + 1);
                if (soluciones > 1) break; // Si hay más de una solución, detener
            }
        }
        tablero[fila][col] = 0; // Restaurar celda
        return soluciones;
    }

    private int[][] copiarTablero() {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, 9);
        }
        return copia;
    }


}
