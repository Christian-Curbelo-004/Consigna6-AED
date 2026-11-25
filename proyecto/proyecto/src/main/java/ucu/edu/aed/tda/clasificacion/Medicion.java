package ucu.edu.aed.tda.clasificacion;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorPadrones;
import ucu.edu.aed.tda.clasificacion.datos.Perfil;

/**
 * Consigna 6 · Punto 7: medir algoritmos x perfiles x tamaños, con regla de corte.
 *
 * Usa las clases de la cátedra: GeneradorPadrones, Perfil.
 * Algoritmos incluidos (cambien/ajusten por los seis que les pidieron):
 *   insercion, seleccion, shellsort, mergesort, heapsort,
 *   quicksort (pivote ingenuo) y quicksort (mediana de tres).
 *
 * Regla de corte: si el tiempo medido en el tamaño anterior, extrapolado
 * cuadráticamente al tamaño siguiente, supera LIMITE_MS, no se mide ese
 * tamaño ni los siguientes (se registra CORTE).
 *
 * Compilar: javac *.java      Ejecutar: java -Xmx2g Medicion
 */

// utiliza esos algoritmos para medir su rendimiento
public class Medicion {

    static final long LIMITE_MS = 30_000;
    static final int[] TAMANIOS = {1_000, 10_000, 100_000, 1_000_000, 5_000_000, 10_000_000};
    static final long SEMILLA = 2026;

    interface Ordenador { String nombre(); void ordenar(int[] a); }

    static Ordenador alg(String nombre, java.util.function.Consumer<int[]> f) {
        return new Ordenador() {
            public String nombre() { return nombre; }
            public void ordenar(int[] a) { f.accept(a); }
        };
    }

    public static void main(String[] args) throws Exception {
        List<Ordenador> algoritmos = List.of(
            alg("insercion", Medicion::insercion),
            alg("seleccion", Medicion::seleccion),
            alg("shellsort", Medicion::shell),
            alg("mergesort", Medicion::merge),
            alg("heapsort", Medicion::heap),
            alg("quick-ingenuo", a -> quick(a, true)),
            alg("quick-mediana3", a -> quick(a, false))
        );

        // Calentamiento de la JVM (JIT) para no medir código sin compilar
        int[] calentar = GeneradorPadrones.generar(Perfil.ALEATORIO, 50_000, 1);
        for (Ordenador o : algoritmos) for (int i = 0; i < 3; i++) o.ordenar(calentar.clone());

        try (PrintWriter csv = new PrintWriter("resultados.csv")) {
            csv.println("algoritmo,perfil,n,ms");
            System.out.printf("%-15s %-18s %12s %12s%n", "algoritmo", "perfil", "n", "ms");

            for (Ordenador o : algoritmos) {
                for (Perfil p : Perfil.values()) {
                    boolean cortado = false;
                    long prevMs = -1; int prevN = 0;
                    for (int n : TAMANIOS) {
                        String res;
                        if (cortado || (prevMs >= 0
                                && prevMs * Math.pow((double) n / prevN, 2) > LIMITE_MS)) {
                            cortado = true;
                            res = "CORTE";
                        } else {
                            int[] original = GeneradorPadrones.generar(p, n, SEMILLA);
                            int reps = n <= 1_000_000 ? 3 : 1;
                            long[] tiempos = new long[reps];
                            for (int r = 0; r < reps; r++) {
                                int[] copia = original.clone();     // la copia no se mide
                                long t0 = System.nanoTime();
                                o.ordenar(copia);
                                tiempos[r] = (System.nanoTime() - t0) / 1_000_000;
                                if (!estaOrdenado(copia))
                                    throw new IllegalStateException(o.nombre() + " no ordenó " + p);
                            }
                            Arrays.sort(tiempos);
                            long ms = tiempos[reps / 2];            // mediana
                            prevMs = ms; prevN = n;
                            res = Long.toString(ms);
                        }
                        System.out.printf("%-15s %-18s %12d %12s%n", o.nombre(), p, n, res);
                        csv.printf("%s,%s,%d,%s%n", o.nombre(), p, n, res);
                        csv.flush();
                    }
                }
            }
        }
    }

    static boolean estaOrdenado(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
        return true;
    }

    // ---------- O(n^2) ----------
    public static void insercion(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int x = a[i], j = i - 1;
            while (j >= 0 && a[j] > x) { a[j + 1] = a[j]; j--; }
            a[j + 1] = x;
        }
    }

    public static void seleccion(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int m = i;
            for (int j = i + 1; j < a.length; j++) if (a[j] < a[m]) m = j;
            int t = a[i]; a[i] = a[m]; a[m] = t;
        }
    }

    // ---------- Shellsort (huecos de Knuth) ----------
    public static void shell(int[] a) {
        int h = 1;
        while (h < a.length / 3) h = 3 * h + 1;
        for (; h >= 1; h /= 3)
            for (int i = h; i < a.length; i++) {
                int x = a[i], j = i;
                while (j >= h && a[j - h] > x) { a[j] = a[j - h]; j -= h; }
                a[j] = x;
            }
    }

    // ---------- Mergesort ----------
    public static void merge(int[] a) {
        int[] aux = new int[a.length];
        mergeRec(a, aux, 0, a.length - 1);
    }

    public static void mergeRec(int[] a, int[] aux, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        mergeRec(a, aux, lo, mid);
        mergeRec(a, aux, mid + 1, hi);
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (aux[j] < aux[i]) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    // ---------- Heapsort ----------
    public static void heap(int[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) hundir(a, i, n);
        for (int fin = n - 1; fin > 0; fin--) {
            int t = a[0]; a[0] = a[fin]; a[fin] = t;
            hundir(a, 0, fin);
        }
    }

    static void hundir(int[] a, int i, int n) {
        int x = a[i];
        while (true) {
            int h = 2 * i + 1;
            if (h >= n) break;
            if (h + 1 < n && a[h + 1] > a[h]) h++;
            if (a[h] <= x) break;
            a[i] = a[h];
            i = h;
        }
        a[i] = x;
    }

    // ---------- Quicksort con dos estrategias de pivote ----------
    // Se recursa sobre la parte chica y se itera sobre la grande: la pila
    // nunca pasa de log2(n) y el pivote ingenuo no desborda la pila con
    // entradas ordenadas (solo se vuelve lento, que es lo que queremos medir).
    static void quick(int[] a, boolean ingenuo) {
        quickRec(a, 0, a.length - 1, ingenuo);
    }

    public static void quickRec(int[] a, int lo, int hi, boolean ingenuo) {
        while (lo < hi) {
            int corte; // la partición deja [lo, corte] y [corte+1, hi]
            if (ingenuo) {
                // pivote = el mayor de los dos primeros elementos distintos
                int k = lo + 1;
                while (k <= hi && a[k] == a[lo]) k++;
                if (k > hi) return; // todos iguales: ya está ordenado
                int pivote = Math.max(a[lo], a[k]);
                int i = lo, j = hi;
                while (i <= j) {
                    while (i <= j && a[i] < pivote) i++;
                    while (i <= j && a[j] >= pivote) j--;
                    if (i < j) { int t = a[i]; a[i] = a[j]; a[j] = t; i++; j--; }
                }
                corte = i - 1; // izquierda < pivote <= derecha, ambas no vacías
            } else {
                // mediana de tres (primero, medio, último), partición de Hoare
                int mid = (lo + hi) >>> 1;
                int m = mediana(a, lo, mid, hi);
                int t = a[lo]; a[lo] = a[m]; a[m] = t;
                int pivote = a[lo];
                int i = lo - 1, j = hi + 1;
                while (true) {
                    do i++; while (a[i] < pivote);
                    do j--; while (a[j] > pivote);
                    if (i >= j) break;
                    int s = a[i]; a[i] = a[j]; a[j] = s;
                }
                corte = j;
            }
            if (corte - lo < hi - corte) { quickRec(a, lo, corte, ingenuo); lo = corte + 1; }
            else { quickRec(a, corte + 1, hi, ingenuo); hi = corte; }
        }
    }

    static int mediana(int[] a, int i, int j, int k) {
        if (a[i] < a[j]) return a[j] < a[k] ? j : (a[i] < a[k] ? k : i);
        else             return a[i] < a[k] ? i : (a[j] < a[k] ? k : j);
    }
}
