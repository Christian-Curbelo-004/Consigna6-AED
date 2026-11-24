package ucu.edu.aed.tda.clasificacion;

public final class Adversario {

    interface Cmp { int cmp(int x, int y); }

    // ---------------- réplica del quicksort (ordena índices) ----------------
    private static void quick(int[] idx, int i, int j, Cmp c, boolean med3) {
        if (i <= j) {
            int ip = pivote(idx, i, j, c, med3);
            if (ip >= i && ip < j) {
                int pid = idx[ip];
                int k = particion(idx, i, j, pid, c);
                quick(idx, i, k - 1, c, med3);
                quick(idx, k, j, c, med3);
            }
        }
    }

    private static int particion(int[] idx, int i, int j, int pid, Cmp c) {
        int a = i, b = j;
        while (true) {
            while (a <= j && c.cmp(idx[a], pid) < 0) a++;
            while (b >= i && c.cmp(idx[b], pid) >= 0) b--;
            if (a >= b) break;
            int t = idx[a]; idx[a] = idx[b]; idx[b] = t;
        }
        return a;
    }

    private static int ingenuo(int[] idx, int i, int j, Cmp c) {
        int k = i + 1;
        while (k <= j && c.cmp(idx[k], idx[i]) == 0) k++;
        if (k > j) return -1;
        return c.cmp(idx[i], idx[k]) > 0 ? i : k;
    }

    private static int pivote(int[] idx, int i, int j, Cmp c, boolean med3) {
        if (!med3) return ingenuo(idx, i, j, c);
        if (j - i < 1) return -1;
        int mid = i + (j - i) / 2;
        int x = idx[i], y = idx[mid], z = idx[j];
        int med, min;
        if (c.cmp(x, y) < 0) {
            min = x;
            med = c.cmp(y, z) < 0 ? y : (c.cmp(x, z) < 0 ? z : x);
        } else {
            min = y;
            med = c.cmp(x, z) < 0 ? x : (c.cmp(y, z) < 0 ? z : y);
        }
        if (c.cmp(z, min) < 0) min = z;
        if (c.cmp(med, min) == 0) return ingenuo(idx, i, j, c);   // mediana = mínimo
        int m = (med == y) ? mid : (med == x ? i : j);
        if (m == j) { idx[j] = idx[mid]; idx[mid] = z; m = mid; }  // evitar índice j
        return m;
    }

    // ---------------- contar comparaciones sobre datos reales ----------------
    private static long comparaciones;

    public static long contar(int[] datos, boolean med3) {
        int n = datos.length;
        int[] idx = new int[n];
        for (int q = 0; q < n; q++) idx[q] = q;
        comparaciones = 0;
        quick(idx, 0, n - 1,
              (x, y) -> { comparaciones++; return Integer.compare(datos[x], datos[y]); },
              med3);
        return comparaciones;
    }

    // ---------------- adversario de McIlroy ----------------
    private static int[] val;
    private static int gas, nsolid, candidato;

    private static int cmpAdversario(int x, int y) {
        if (val[x] == gas && val[y] == gas) {
            if (x == candidato) val[x] = nsolid++; else val[y] = nsolid++;
        }
        if (val[x] == gas) candidato = x;
        else if (val[y] == gas) candidato = y;
        return Integer.compare(val[x], val[y]);
    }

    /** Entrada de tamaño n que vuelve cuadrático al quicksort replicado. */
    public static int[] entradaAsesina(int n, boolean med3) {
        val = new int[n]; gas = n - 1; nsolid = 0; candidato = 0;
        int[] idx = new int[n];
        for (int q = 0; q < n; q++) { idx[q] = q; val[q] = gas; }
        quick(idx, 0, n - 1, Adversario::cmpAdversario, med3);
        return val.clone();
    }

    public static void main(String[] args) {
        System.out.println("== Pivote ingenuo, lista ya ordenada ==");
        for (int n : new int[]{1000, 2000, 4000, 8000}) {
            int[] a = new int[n];
            for (int q = 0; q < n; q++) a[q] = q;
            System.out.println(n + " -> " + contar(a, false)
                    + " comparaciones (n^2/2 = " + (long) n * n / 2 + ")");
        }
        System.out.println("== Mediana de tres: aleatoria vs asesina ==");
        java.util.Random r = new java.util.Random(1);
        for (int n : new int[]{1000, 2000, 4000, 8000}) {
            int[] alea = new int[n];
            for (int q = 0; q < n; q++) alea[q] = r.nextInt();
            System.out.println(n + " -> aleatoria " + contar(alea, true)
                    + " | asesina " + contar(entradaAsesina(n, true), true));
        }
    }
}