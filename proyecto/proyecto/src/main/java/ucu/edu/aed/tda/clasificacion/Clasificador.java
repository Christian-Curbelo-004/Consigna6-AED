
package ucu.edu.aed.tda.clasificacion;

import java.util.ArrayList;
import java.util.List;

// implementa los algoritmos y extiende TClasificador
public class Clasificador extends TClasificador {

    // 1. Inserción directa
    @Override
    void insercionDirecta(int[] datos) {
        for (int i = 1; i < datos.length; i++) {
            int actual = datos[i];
            int j = i - 1;

            while (j >= 0 && datos[j] > actual) {
                datos[j + 1] = datos[j];
                j--;
            }

            datos[j + 1] = actual;
        }
    }

    // 2. Shellsort
    @Override
    void shell(int[] datos, Integer[] incrementos) {
        for (int incremento : incrementos) {
            for (int i = incremento; i < datos.length; i++) {
                int actual = datos[i];
                int j = i;

                while (j >= incremento && datos[j - incremento] > actual) {
                    datos[j] = datos[j - incremento];
                    j -= incremento;
                }

                datos[j] = actual;
            }
        }
    }

    // 3. Burbuja
    @Override
    void burbuja(int[] datos) {
        for (int i = 0; i < datos.length - 1; i++) {
            boolean huboIntercambio = false;

            for (int j = 0; j < datos.length - 1 - i; j++) {
                if (datos[j] > datos[j + 1]) {
                    intercambiar(datos, j, j + 1);
                    huboIntercambio = true;
                }
            }

            if (!huboIntercambio) {
                break;
            }
        }
    }

    // 4. Quicksort: selección del pivote
    @Override
    protected int obtenerPivote(int[] datos, int i, int j) {
        return i;
    }

    // 5. Quicksort: partición
    @Override
    protected int particion(int[] datos, int i, int j, int pivote) {
        while (i <= j) {
            while (i <= j && datos[i] < pivote) {
                i++;
            }

            while (i <= j && datos[j] > pivote) {
                j--;
            }

            if (i <= j) {
                intercambiar(datos, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    // 6. Selección directa
    @Override
    public void clasificacionDirecta(int[] datos) {
        for (int i = 0; i < datos.length - 1; i++) {
            int minimo = i;

            for (int j = i + 1; j < datos.length; j++) {
                if (datos[j] < datos[minimo]) {
                    minimo = j;
                }
            }

            intercambiar(datos, i, minimo);
        }
    }

    // 7. Heapsort
    @Override
    public void heapsort(int[] datos) {
        int n = datos.length;

        // Construcción del heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            desplazaElemento(datos, i, n - 1);
        }

        // Extracción del máximo
        for (int i = n - 1; i > 0; i--) {
            intercambiar(datos, 0, i);
            desplazaElemento(datos, 0, i - 1);
        }
    }

    @Override
    protected void desplazaElemento(int[] datos, int primero, int ultimo) {
        int padre = primero;

        while (2 * padre + 1 <= ultimo) {
            int hijo = 2 * padre + 1;

            if (hijo + 1 <= ultimo && datos[hijo + 1] > datos[hijo]) {
                hijo++;
            }

            if (datos[padre] >= datos[hijo]) {
                break;
            }

            intercambiar(datos, padre, hijo);
            padre = hijo;
        }
    }

    // 8. Cuenta por distribución
    @Override
    public void cuentaPorDistribucion(int[] datos) {
        if (datos.length == 0) {
            return;
        }

        int[] rango = obtenerRango(datos);
        int minimo = rango[0];
        int maximo = rango[1];

        int[] frecuencias = new int[(int) ((long) maximo - minimo + 1)];

        for (int valor : datos) {
            frecuencias[(int) ((long) valor - minimo)]++;
        }

        int indice = 0;

        for (int i = 0; i < frecuencias.length; i++) {
            while (frecuencias[i] > 0) {
                datos[indice++] = i + minimo;
                frecuencias[i]--;
            }
        }
    }

    // 9. Binsort
    @Override
    public void binsort(int[] datos) {
        // Una urna por cada valor posible.
        cuentaPorDistribucion(datos);
    }

    // 10. Bucketsort
    @Override
    public void bucketsort(int[] datos, int cantidadUrnas) {
        if (datos.length == 0) {
            return;
        }

        if (cantidadUrnas <= 0) {
            throw new IllegalArgumentException("La cantidad de urnas debe ser positiva");
        }

        int[] rango = obtenerRango(datos);
        long minimo = rango[0];
        long amplitud = (long) rango[1] - minimo + 1;

        List<List<Integer>> urnas = new ArrayList<>();

        for (int i = 0; i < cantidadUrnas; i++) {
            urnas.add(new ArrayList<>());
        }

        for (int valor : datos) {
            int indice = (int) (((long) valor - minimo) * cantidadUrnas / amplitud);
            urnas.get(indice).add(valor);
        }

        int posicion = 0;

        for (List<Integer> urna : urnas) {
            // Ordenar cada urna mediante inserción
            for (int i = 1; i < urna.size(); i++) {
                int actual = urna.get(i);
                int j = i - 1;

                while (j >= 0 && urna.get(j) > actual) {
                    urna.set(j + 1, urna.get(j));
                    j--;
                }

                urna.set(j + 1, actual);
            }

            for (int valor : urna) {
                datos[posicion++] = valor;
            }
        }
    }

    // 11. Radix sort
    @Override
    public void radixsort(int[] datos, int base) {
        if (base < 2) {
            throw new IllegalArgumentException("La base debe ser mayor o igual a 2");
        }

        if (datos.length == 0) {
            return;
        }

        int maximo = 0;

        for (int valor : datos) {
            if (valor < 0) {
                throw new IllegalArgumentException("Radix sort requiere valores no negativos");
            }

            maximo = Math.max(maximo, valor);
        }

        for (long exponente = 1; maximo / exponente > 0; exponente *= base) {
            distribuirPorDigito(datos, (int) exponente, base);

            if (exponente > Integer.MAX_VALUE / (long) base) {
                break;
            }
        }
    }

    @Override
    protected void distribuirPorDigito(int[] datos, int exponente, int base) {
        int[] frecuencias = new int[base];
        int[] resultado = new int[datos.length];

        for (int valor : datos) {
            int digito = (valor / exponente) % base;
            frecuencias[digito]++;
        }

        for (int i = 1; i < base; i++) {
            frecuencias[i] += frecuencias[i - 1];
        }

        // Recorrer de atrás hacia adelante para mantener estabilidad
        for (int i = datos.length - 1; i >= 0; i--) {
            int digito = (datos[i] / exponente) % base;
            resultado[--frecuencias[digito]] = datos[i];
        }

        System.arraycopy(resultado, 0, datos, 0, datos.length);
    }
}
