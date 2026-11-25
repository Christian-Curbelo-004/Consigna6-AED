#!/usr/bin/env python3
"""Compila y ejecuta todas las series. Python 3 + JDK 17 o posterior, sin dependencias externas."""
import argparse, concurrent.futures, csv, pathlib, subprocess, sys, os
P=pathlib.Path(__file__).resolve().parent
ALG=['insercion','shellsort','burbuja','quick-ingenuo','quick-mediana3','seleccion','heapsort','mergesort','introsort']
PER=['ALEATORIO','ASCENDENTE','DESCENDENTE','TUBO_DE_ORGANO','POCOS_DISTINTOS','ORDENADO_CON_COLA']
HEAD=['algoritmo','perfil','n','estado','ms','estimado_ms','warm_ms','rep1_ms','rep2_ms','rep3_ms']
def main():
    a=argparse.ArgumentParser(); a.add_argument('--max',type=int,default=10_000_000); a.add_argument('--preliminar',action='store_true'); a.add_argument('--workers',type=int,default=1); a.add_argument('--heap',default='4g'); a.add_argument('--salida',type=pathlib.Path,default=P/'resultados.csv'); a.add_argument('--algoritmos',nargs='+',choices=ALG,default=ALG); opts=a.parse_args()
    if not 1<=opts.max<=10_000_000 or opts.workers<1: a.error('max debe estar entre 1 y 10000000, workers >=1')
    classes=P/'target'/'bench'; classes.mkdir(parents=True,exist_ok=True)
    subprocess.run(['javac','--release','17','-encoding','UTF-8','-d',str(classes)]+[str(p) for p in (P/'src/main/java').rglob('*.java')],check=True)
    java=['java','-Xmx'+opts.heap,'-Xss512m','-XX:ActiveProcessorCount=1','-cp',str(classes),'ucu.edu.aed.tda.clasificacion.Main']
    def pair(pair):
        alg,per=pair; cmd=java+['serie',alg,per,str(opts.max),str(opts.preliminar).lower()]
        proc=subprocess.Popen(cmd,stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True)
        # Recibir líneas a medida que las series avanzan, incluso si se interrumpe el benchmark.
        for line in proc.stdout:
            rows=list(csv.reader([line.strip()]));
            if len(rows[0])!=len(HEAD): raise RuntimeError(line)
            with lock:
                writer.writerow(rows[0]); f.flush()
        err=proc.stderr.read(); rc=proc.wait()
        if rc: raise RuntimeError(f'{alg}/{per}: {rc}: {err}')
        print(f'{alg}/{per} listo',file=sys.stderr,flush=True)
    import threading
    lock=threading.Lock(); opts.salida.parent.mkdir(parents=True,exist_ok=True)
    with opts.salida.open('w',newline='',encoding='utf-8') as f:
        writer=csv.writer(f); writer.writerow(HEAD); f.flush()
        with concurrent.futures.ThreadPoolExecutor(max_workers=opts.workers) as ex:
            list(ex.map(pair,[(x,y) for x in opts.algoritmos for y in PER]))
if __name__=='__main__': main()
