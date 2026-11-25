# Consigna 6 — El servicio que no puede fallar

Entrega revisada: Algoritmos y Estructuras de Datos (UT05). El proyecto base de la cátedra se conserva; `TClasificador.java`, `TipoClasificador.java`, `Medidor.java` y los generadores no fueron modificados.

## Archivos

- `proyecto/`: proyecto Java 17 (Maven), con los seis métodos obligatorios, dos pivotes, Mergesort adicional, Introsort, pruebas y Main.
- `Informe_Consigna6.pdf`: informe revisado (7 páginas, conservando el estilo y la redacción original).
- `resultados.csv`: mediciones definitivas del equipo del estudiante (54 series, 11 tamaños, 497 casos válidos, 18 cortes y 79 tamaños omitidos), con tiempos crudos, estado y estimaciones. `resultados-oficiales.csv` conserva el CSV recibido.
- `operaciones.csv`: comparaciones y escrituras reales a n=10.000 por perfil.
- `adversario.csv`: comparaciones en entradas construidas y aleatorias.
- `pruebas.txt`, `verificacion-datos.txt`: salidas reales de las verificaciones.
- `consignas/`: instrucciones generales y particulares.
- `presentacion/`: diapositivas finales en PowerPoint y PDF.

## Ejecutar en IntelliJ

Abrir la carpeta `proyecto` como proyecto Maven, con JDK 17 o superior. Ejecutar `ucu.edu.aed.tda.clasificacion.Main` con VM options `-Xmx4g -Xss512m`. Sin argumentos, muestra una demo rápida con los nueve métodos/variantes.

Argumentos posibles para Main:

| Argumentos | Resultado |
|---|---|
| `demo` | Demostración con todos los algoritmos, tres mediciones |
| `test` | Pruebas de ordenamiento contra Arrays.sort |
| `adversario` | Comparaciones de Quicksort normal/adversario e Introsort |
| `operaciones 10000` | Contadores de comparaciones y movimientos |
| `medir introsort ALEATORIO 10000000` | Calentamiento y tres corridas, mediana en ms |
| `medir introsort ASCENDENTE 10000000 datos/padrones-ascendente.txt` | Medir archivo en vez de generar |
| `serie heapsort ALEATORIO 10000000 false` | Serie geométrica con regla oficial |
| `verificar-datos datos` | Comprueba archivos presentes contra el generador |

Los métodos de distribución (cuenta, binsort, bucketsort, radix) aparecen en la clase base pero no pertenecen a los seis exigidos; sus overrides lanzan `UnsupportedOperationException` deliberadamente. Mergesort es un agregado de comparación, no un reemplazo de Burbuja.

## Benchmark completo (ejecutado con el corte oficial)

**Entorno de las mediciones definitivas (25/11/2026):** Windows, Intel Core i5-12450H (12.ª generación), 20 GB de RAM DDR4 a 3200 MT/s y Java 26.0.2 (2026-07-21).

Desde `proyecto/`, con Python 3 y JDK 17+ en PATH:

```sh
python benchmark.py --salida ../resultados-oficiales.csv
```

El script compila, ejecuta secuencialmente las 54 series y guarda cada fila a medida que termina. Por defecto usa `-Xmx4g -Xss512m`, semilla 2026, tamaños 10.000, 20.000, ..., 5.120.000 y 10.000.000. Una corrida de calentamiento por tamaño y tres cronometradas mediante el `Medidor` original. El vigilante corta a 60 segundos de pared por corrida (incluye preparación/verificación; el tiempo informado por Medidor excluye ambas). Si se agota ese límite, marca `CORTE_60S` y no inicia tamaños mayores (`NO_MEDIDO_TRAS_CORTE_60S`). Si hay `StackOverflowError`, lo registra por separado. Las estimaciones no son mediciones. El archivo definitivo incluido se ejecutó con el corte oficial.

Para una ejecución breve de comprobación:

```sh
python benchmark.py --max 40000 --salida ../resultados-prueba.csv
```

Para probar un subconjunto: `python benchmark.py --algoritmos burbuja seleccion --salida ../resultados-subconjunto.csv`. El modo `--preliminar` evita tamaños lentos cuando la proyección supera 1500 ms; solo sirve para exploración. Se recomienda `--workers 1` para evitar competencia por CPU/caché. Los datos definitivos incluidos corresponden a la ejecución secuencial del estudiante. La propuesta de SLA requiere validación en el entorno real.

## Datos

El benchmark usa `GeneradorPadrones` oficial con semilla 2026. Se comprobó igualdad elemento por elemento para los cuatro TXT recibidos (aleatorio, ascendente, descendente, ordenado con cola). Los perfiles tubo de órgano y pocos distintos se generan con la misma clase. Los TXT grandes no se duplican en este ZIP: se pueden descomprimir los originales en `proyecto/datos/` si se desea probar la lectura de archivos. La generación del resto de tamaños no depende de esos archivos.

## Pruebas

`Main test` fue ejecutado con éxito en Java 21 compilando con `--release 17`. `mvn test` ejecuta además los tests JUnit incluidos si Maven está instalado. No se ejecutó Maven en el entorno de revisión (no estaba disponible).

## Antes de entregar

1. El benchmark oficial y las tablas/gráficos ya están actualizados. Las estimaciones están diferenciadas de las mediciones.
2. Confirmar nombres del equipo y requisitos de presentación de la cátedra (no se inventaron nombres).
3. Preparar una defensa de diez minutos: modelo, hipótesis original, contrato del pivote, adversario, comparación de tiempos, demostración de Main e Introsort, y recomendación condicionada al entorno de producción.

Para reproducir el resumen, `python resumir_resultados.py ../resultados-oficiales.csv > ../resumen-oficial.md` genera automáticamente las tablas de máximos y las estimaciones para facilitar la actualización del informe. `prueba-benchmark.csv` demuestra la ejecución de las 54 combinaciones en modo oficial a n=10.000.
