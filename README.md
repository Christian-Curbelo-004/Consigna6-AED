# Consigna 6 – Algoritmos y Estructuras de Datos

Repositorio correspondiente a la **Consigna 6 de Algoritmos y Estructuras de Datos (AED)**.

El objetivo del proyecto es implementar, analizar y comparar distintos algoritmos de ordenamiento mediante pruebas experimentales sobre diferentes perfiles de datos y tamaños de entrada.

## Objetivos

- Implementar diferentes algoritmos de clasificación.
- Evaluar su comportamiento con distintos tipos de entrada.
- Comparar sus tiempos de ejecución.
- Analizar el impacto de las estrategias de selección de pivote en Quicksort.
- Estudiar el rendimiento de los algoritmos mediante mediciones experimentales.

## Tecnologías utilizadas

- **Lenguaje:** Java
- **Gestión y compilación:** Maven
- **Entorno de desarrollo:** Visual Studio Code
- **Control de versiones:** Git y GitHub

## Estructura del proyecto

```text
Consigna6-AED/
│
├── README.md
├── resultados.csv
├── La_idea_central_del_trabajo.pdf
│
└── proyecto/
    └── proyecto/
        ├── pom.xml
        └── src/
            └── main/
                └── java/
                    └── ucu/
                        └── edu/
                            └── aed/
                                └── tda/
                                    └── clasificacion/
                                        ├── Main.java
                                        ├── TClasificador.java
                                        ├── Clasificador.java
                                        ├── TipoClasificador.java
                                        ├── Medicion.java
                                        ├── MedicionIntrosort.java
                                        ├── Introsort.java
                                        ├── Adversario.java
                                        ├── GeneradorDatosGenericos.java
                                        │
                                        └── datos/
                                            ├── GeneradorPadrones.java
                                            └── Perfil.java
```

## Algoritmos implementados

La clase `Clasificador` extiende `TClasificador` e implementa los métodos de clasificación definidos en la clase abstracta.

Entre los algoritmos contemplados se encuentran:

- Inserción directa.
- Shellsort.
- Burbuja.
- Quicksort.
- Selección directa.
- Heapsort.
- Cuenta por distribución.
- Binsort.
- Bucketsort.
- Radixsort.

También se incluye una implementación de **Introsort**, que combina Quicksort, Heapsort e inserción directa para controlar el peor caso de rendimiento.

## Perfiles de entrada

Para evaluar los algoritmos se generan datos con diferentes características:

| Perfil | Descripción |
|---|---|
| ALEATORIO | Datos en orden aleatorio. |
| ASCENDENTE | Datos ordenados de menor a mayor. |
| DESCENDENTE | Datos ordenados de mayor a menor. |
| TUBO_DE_ORGANO | Datos que primero crecen y luego decrecen. |
| POCOS_DISTINTOS | Datos con pocos valores diferentes. |
| ORDENADO_CON_COLA | Datos ordenados con una pequeña parte desordenada al final. |

Los datos se generan mediante `GeneradorPadrones`, utilizando semillas para permitir la reproducibilidad de las pruebas.

## Mediciones experimentales

La clase `Medicion` se encarga de ejecutar las pruebas de rendimiento de los algoritmos.

El proceso contempla:

- Diferentes tamaños de entrada.
- Distintos perfiles de datos.
- Calentamiento de la JVM.
- Repeticiones de las mediciones.
- Cálculo de la mediana de los tiempos obtenidos.
- Registro de resultados en formato CSV.
- Aplicación de una regla de corte para evitar mediciones excesivamente costosas.

Los resultados experimentales se almacenan en `resultados.csv`.

## Compilación y ejecución

El proyecto utiliza Maven.

### 1. Ubicarse en la carpeta del proyecto Maven

```bash
cd proyecto/proyecto
```

### 2. Compilar

```bash
mvn clean compile
```

### 3. Ejecutar las pruebas iniciales

```bash
Me ubico en las carpetas: Consigna6-AED\proyecto\proyecto> y ejecuto:
java -cp target/classes ucu.edu.aed.tda.clasificacion.Main
```

### 4. Ejecutar las mediciones

```bash
Me ubico en las carpetas: Consigna6-AED\proyecto\proyecto> y ejecuto:
java -Xmx2g -cp target/classes ucu.edu.aed.tda.clasificacion.Medicion
```

> La memoria máxima de la JVM puede ajustarse según los recursos disponibles en el equipo.

## Resultados

Los resultados de las pruebas se registran en archivos CSV para facilitar su análisis y comparación.

El análisis busca identificar las diferencias de rendimiento entre los algoritmos, considerando tanto la complejidad teórica como el comportamiento observado experimentalmente.


