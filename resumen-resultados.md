# Resumen del benchmark

Estados: {'OK': 497, 'CORTE_60S': 18, 'NO_MEDIDO_TRAS_CORTE_60S': 79}
Tamaño máximo: 10000000

| Algoritmo | ALEATORIO | ASCENDENTE | DESCENDENTE | TUBO_DE_ORGANO | POCOS_DISTINTOS | ORDENADO_CON_COLA |
|---|---:|---:|---:|---:|---:|---:|
| insercion | 32138.364 † | 13.057 | **47242.371 †** | 23173.882 † | 29383.714 † | 33625.906 † |
| shellsort | **4753.141** | 233.184 | 362.507 | 662.083 | 1771.038 | 2690.881 |
| burbuja | 14855.596 † | 6.016 | 21000.551 † | 16032.252 † | 14579.046 † | **46931.747 †** |
| quick-ingenuo | 2115.428 | 11850.690 † | **51738.531 †** | 2690.309 | 257.794 | 21720.734 |
| quick-mediana3 | 2260.441 | 350.612 | 622.356 | 3376.966 | 373.945 | **3503.482** |
| seleccion | 21231.394 † | 21152.873 † | 30590.427 † | **59368.936 †** | 10476.860 † | 34328.762 † |
| heapsort | **3401.451** | 1105.901 | 1095.915 | 1323.178 | 1135.619 | 1117.139 |
| mergesort | **1937.679** | 477.459 | 507.062 | 467.687 | 1026.816 | 833.269 |
| introsort | 2385.199 | 295.011 | 307.147 | **2458.214** | 840.077 | 1871.486 |

† Serie incompleta al tamaño máximo. Valores: mayor mediana medida (ms), no estimaciones.

## Peor caso medido por algoritmo

- insercion: 47242.371 ms; DESCENDENTE; n=320000; cobertura completa=False.
- shellsort: 4753.141 ms; ALEATORIO; n=10000000; cobertura completa=True.
- burbuja: 46931.747 ms; ORDENADO_CON_COLA; n=320000; cobertura completa=False.
- quick-ingenuo: 51738.531 ms; DESCENDENTE; n=320000; cobertura completa=False.
- quick-mediana3: 3503.482 ms; ORDENADO_CON_COLA; n=10000000; cobertura completa=True.
- seleccion: 59368.936 ms; TUBO_DE_ORGANO; n=320000; cobertura completa=False.
- heapsort: 3401.451 ms; ALEATORIO; n=10000000; cobertura completa=True.
- mergesort: 1937.679 ms; ALEATORIO; n=10000000; cobertura completa=True.
- introsort: 2458.214 ms; TUBO_DE_ORGANO; n=10000000; cobertura completa=True.

## Estimaciones al tamaño máximo

- insercion/ALEATORIO: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=31385121.191406.
- insercion/DESCENDENTE: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=46135127.734375.
- insercion/TUBO_DE_ORGANO: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=22630744.335938.
- insercion/POCOS_DISTINTOS: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=28695033.691406.
- insercion/ORDENADO_CON_COLA: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=513090.600586.
- burbuja/ALEATORIO: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=232118689.062500.
- burbuja/DESCENDENTE: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=82033403.906250.
- burbuja/TUBO_DE_ORGANO: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=62625985.546875.
- burbuja/POCOS_DISTINTOS: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=227797592.187500.
- burbuja/ORDENADO_CON_COLA: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=45831784.375000.
- quick-ingenuo/ASCENDENTE: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=46291755.859375.
- quick-ingenuo/DESCENDENTE: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=50525909.375000.
- seleccion/ALEATORIO: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=82935134.375000.
- seleccion/ASCENDENTE: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=82628408.593750.
- seleccion/DESCENDENTE: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=29873464.160156.
- seleccion/TUBO_DE_ORGANO: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=57977476.757813.
- seleccion/POCOS_DISTINTOS: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=40925235.937500.
- seleccion/ORDENADO_CON_COLA: NO_MEDIDO_TRAS_CORTE_60S; estimación ms=33524182.128906.
