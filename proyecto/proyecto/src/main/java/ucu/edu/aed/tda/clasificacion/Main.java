package ucu.edu.aed.tda.clasificacion;
import java.util.Arrays;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorPadrones;
import ucu.edu.aed.tda.clasificacion.datos.Perfil;


public class Main {

    public static void main(String[] args) {

        int[] vectorAleatorio = GeneradorPadrones.generar(Perfil.ALEATORIO, 100, 2026);
        int[] vectorAscendente = GeneradorPadrones.generar(Perfil.ASCENDENTE, 100, 2026);
        int[] vectorDescendente = GeneradorPadrones.generar(Perfil.DESCENDENTE, 100, 2026);

        TClasificador clasificador = new Clasificador();

        
        System.out.println("PRUEBA DE CLASIFICADORES: ");
        

        probarClasificador("ALEATORIO", vectorAleatorio, clasificador);
        probarClasificador("ASCENDENTE", vectorAscendente, clasificador);
        probarClasificador("DESCENDENTE", vectorDescendente, clasificador);
    }

    private static void probarClasificador(
            String nombre, int[] datos, TClasificador clasificador) {

        System.out.println("\n PERFIL: " + nombre + " ");

        System.out.println("Antes:    " + Arrays.toString(datos));

        clasificador.insercionDirecta(datos);

        System.out.println("Después:  " + Arrays.toString(datos));
        System.out.println("Estado:   " + (estaOrdenado(datos) ? "CORRECTO" : "INCORRECTO"));
    }

    private static boolean estaOrdenado(int[] datos) {
        for (int i = 1; i < datos.length; i++) {
            if (datos[i - 1] > datos[i]) {
                return false;
            }
        }
        return true;
    }
}
