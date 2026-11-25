package ucu.edu.aed.tda.clasificacion;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorPadrones;
import ucu.edu.aed.tda.clasificacion.datos.Perfil;

/**
 * Introsort (Musser, 1997): quicksort con mediana de tres que, si la recursión
 * supera 2*log2(n) niveles, cambia a heapsort en ese tramo. Los tramos chicos
 * se terminan con inserción. Peor caso garantizado: O(n log n).
 */
public final class Introsort {

    private static final int UMBRAL = 16;

    private Introsort() { }

    public static void ordenar(int[] a) {
        if (a.length < 2) return;
        int limite = 2 * (31 - Integer.numberOfLeadingZeros(a.length));
        intro(a, 0, a.length - 1, limite);
    }

    private static void intro(int[] a, int lo, int hi, int limite) {
        while (hi - lo > UMBRAL) {
            if (limite == 0) {                 // recursión demasiado profunda
                heapsort(a, lo, hi);
                return;
            }
            limite--;
            int p = mediana(a[lo], a[lo + (hi - lo) / 2], a[hi]);
            int i = lo, j = hi;
            while (i <= j) {
                while (a[i] < p) i++;
                while (a[j] > p) j--;
                if (i <= j) { intercambiar(a, i, j); i++; j--; }
            }
            // recursión sobre el lado chico, iteración sobre el grande
            if (j - lo < hi - i) { intro(a, lo, j, limite); lo = i; }
            else                 { intro(a, i, hi, limite); hi = j; }
        }
        insercion(a, lo, hi);
    }

    private static int mediana(int x, int y, int z) {
        return Math.max(Math.min(x, y), Math.min(Math.max(x, y), z));
    }

    private static void insercion(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i], j = i - 1;
            while (j >= lo && a[j] > x) { a[j + 1] = a[j]; j--; }
            a[j + 1] = x;
        }
    }

    // heapsort sobre el tramo [lo, hi]
    private static void heapsort(int[] a, int lo, int hi) {
        int n = hi - lo + 1;
        for (int i = n / 2 - 1; i >= 0; i--) bajar(a, lo, i, n);   // Floyd, O(n)
        for (int fin = n - 1; fin > 0; fin--) {
            intercambiar(a, lo, lo + fin);
            bajar(a, lo, 0, fin);
        }
    }

    private static void bajar(int[] a, int lo, int i, int n) {
        while (2 * i + 1 < n) {
            int h = 2 * i + 1;
            if (h + 1 < n && a[lo + h + 1] > a[lo + h]) h++;
            if (a[lo + i] >= a[lo + h]) break;
            intercambiar(a, lo + i, lo + h);
            i = h;
        }
    }

    private static void intercambiar(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

        public static void main(String[] args) {
        int n = 1_000_000;                          // subilo a 10_000_000 cuando ande
        for (Perfil p : Perfil.values()) {
            int[] datos = GeneradorPadrones.generar(p, n, 2026);
            long t0 = System.nanoTime();
            ordenar(datos);
            double seg = (System.nanoTime() - t0) / 1e9;
            System.out.println(p + ": " + seg + " s, ordenado = " + estaOrdenado(datos));
        }
    }

    private static boolean estaOrdenado(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) return false;
        }
        return true;
    }
}