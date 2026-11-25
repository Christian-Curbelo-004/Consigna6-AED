#!/usr/bin/env python3
"""Resume un CSV de benchmark.py en Markdown sin dependencias externas."""
import argparse,csv,collections
p=argparse.ArgumentParser();p.add_argument('csv');a=p.parse_args()
with open(a.csv,encoding='utf-8',newline='') as f: rows=list(csv.DictReader(f))
alg=list(dict.fromkeys(r['algoritmo'] for r in rows)); per=['ALEATORIO','ASCENDENTE','DESCENDENTE','TUBO_DE_ORGANO','POCOS_DISTINTOS','ORDENADO_CON_COLA']; nmax=max(int(r['n']) for r in rows)
print('# Resumen del benchmark\n');print('Estados:',dict(collections.Counter(r['estado'] for r in rows)));print('Tamaño máximo:',nmax)
print('\n| Algoritmo | '+' | '.join(per)+' |\n|---|'+'---:|'*len(per))
for x in alg:
    cells=[];vals=[]
    for y in per:
        q=[r for r in rows if r['algoritmo']==x and r['perfil']==y and r['estado']=='OK']; vals.append(max((float(r['ms']) for r in q),default=-1));cells.append((vals[-1],any(int(r['n'])==nmax for r in q)))
    m=max(vals);print('| '+x+' | '+' | '.join(('**' if v==m and v>=0 else '')+(f'{v:.3f}' if v>=0 else 's/d')+('' if full else ' †')+('**' if v==m and v>=0 else '') for v,full in cells)+' |')
print('\n† Serie incompleta al tamaño máximo. Valores: mayor mediana medida (ms), no estimaciones.')
print('\n## Peor caso medido por algoritmo\n')
for x in alg:
    q=[r for r in rows if r['algoritmo']==x and r['estado']=='OK'];full=all(any(r['perfil']==y and int(r['n'])==nmax for r in q) for y in per)
    if q:
        r=max(q,key=lambda r:float(r['ms']));print(f'- {x}: {float(r["ms"]):.3f} ms; {r["perfil"]}; n={r["n"]}; cobertura completa={full}.')
print('\n## Estimaciones al tamaño máximo\n')
for r in rows:
    if int(r['n'])==nmax and r['estado']!='OK':print(f'- {r["algoritmo"]}/{r["perfil"]}: {r["estado"]}; estimación ms={r["estimado_ms"] or "s/d"}.')
