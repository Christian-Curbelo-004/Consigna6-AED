package ucu.edu.aed.tda.clasificacion;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorPadrones;
import ucu.edu.aed.tda.clasificacion.datos.Perfil;

public class MedicionIntrosort {

    // Deben coincidir exactamente con los valores de Medicion.
    private static final int[] TAMANIOS = {
        1_000, 10_000, 100_000, 1_000_000, 5_000_000, 10_000_000
    };

    private static final int SEMILLA = 2026; // Reemplazar por la de Medicion.
    private static final long LIMITE_MS = 30_000;

    public static void main(String[] args) throws Exception {

        // Comprobamos que ya exista el CSV generado por Medicion.
        Path archivo = Path.of("resultados.csv");

        if (!Files.exists(archivo) || Files.size(archivo) == 0) {
            throw new IllegalStateException(
                "Primero debe ejecutarse Medicion para generar resultados.csv"
            );
        }

        // Calentamiento de la JVM.
        int[] calentar = GeneradorPadrones.generar(
            Perfil.ALEATORIO, 50_000, 1
        );

        for (int i = 0; i < 3; i++) {
            Introsort.ordenar(calentar.clone());
        }

        // Abrimos el CSV en modo append para conservar los resultados anteriores.
        try (PrintWriter csv = new PrintWriter(
                new FileWriter("resultados.csv", true))) {

            System.out.printf(
                "%-15s %-18s %12s %12s%n",
                "algoritmo", "perfil", "n", "ms"
            );

            for (Perfil p : Perfil.values()) {

                boolean cortado = false;
                long prevMs = -1;
                int prevN = 0;

                for (int n : TAMANIOS) {

                    String res;

                    if (cortado || (prevMs >= 0
                            && prevMs * Math.pow((double) n / prevN, 2)
                            > LIMITE_MS)) {

                        cortado = true;
                        res = "CORTE";

                    } else {

                        int[] original =
                            GeneradorPadrones.generar(p, n, SEMILLA);

                        int reps = n <= 1_000_000 ? 3 : 1;
                        long[] tiempos = new long[reps];

                        for (int r = 0; r < reps; r++) {

                            int[] copia = original.clone();

                            long t0 = System.nanoTime();
                            Introsort.ordenar(copia);
                            tiempos[r] =
                                (System.nanoTime() - t0) / 1_000_000;

                            if (!estaOrdenado(copia)) {
                                throw new IllegalStateException(
                                    "Introsort no ordenó " + p
                                );
                            }
                        }

                        Arrays.sort(tiempos);

                        long ms = tiempos[reps / 2];
                        prevMs = ms;
                        prevN = n;

                        res = Long.toString(ms);
                    }

                    System.out.printf(
                        "%-15s %-18s %12d %12s%n",
                        "introsort", p, n, res
                    );

                    csv.printf(
                        "introsort,%s,%d,%s%n",
                        p, n, res
                    );

                    csv.flush();
                }
            }
        }
    }

    private static boolean estaOrdenado(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }
}
