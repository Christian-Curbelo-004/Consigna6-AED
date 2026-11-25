package ucu.edu.aed.tda.clasificacion;
import ucu.edu.aed.tda.clasificacion.datos.*;
import java.util.Arrays;
/** Construcción adaptativa inspirada en McIlroy. Usa el quicksort heredado real. */
public final class Adversario {
    private Adversario() {}
    /** Permutación distinta de los perfiles base: intercambia los dos primeros.
     *  La primera partición deja el mínimo solo y el resto ascendente.
     */
    public static int[] entradaIngenua(int n) {
        int[] a=GeneradorPadrones.generar(Perfil.ASCENDENTE,n,2026);
        if(n>=2) { int x=a[0]; a[0]=a[1]; a[1]=x; }
        return a;
    }
    public static int[] entradaAsesina(int n) {
        int[] valores=new int[n],indices=new int[n];
        Arrays.fill(valores,n); for(int i=0;i<n;i++) indices[i]=i;
        TClasificadorImpl c=new TClasificadorImpl(TClasificadorImpl.Pivote.MEDIANA3,false) {
            int siguiente=0,candidato=0;
            @Override protected int comparar(int x,int y) {
                if(x==y) return 0;
                if(valores[x]==n && valores[y]==n) {
                    if(x==candidato) valores[x]=siguiente++; else valores[y]=siguiente++;
                }
                if(valores[x]==n) candidato=x; else if(valores[y]==n) candidato=y;
                return Integer.compare(valores[x],valores[y]);
            }
        };
        c.quicksort(indices);
        int siguiente=0; for(int v:valores) if(v!=n) siguiente=Math.max(siguiente,v+1);
        for(int i=0;i<n;i++) if(valores[i]==n) valores[i]=siguiente++;
        for(int i=0;i<n;i++) valores[i]+=1_000_000;
        return valores;
    }
    public static void main(String[] args) {
        System.out.println("n,ingenuo_asc,ingenuo_adversario,mediana_aleatorio,mediana_adversario,introsort_adversario");
        for(int n:new int[]{1000,2000,4000,8000,16000}) {
            int[] asc=GeneradorPadrones.generar(Perfil.ASCENDENTE,n,2026),ale=GeneradorPadrones.generar(Perfil.ALEATORIO,n,2026),mal=entradaAsesina(n);
            System.out.printf("%d,%d,%d,%d,%d,%d%n",n,contar(asc,false,false),contar(entradaIngenua(n),false,false),contar(ale,true,false),contar(mal,true,false),contar(mal,true,true));
        }
    }
    private static long contar(int[] original,boolean mediana,boolean intro) {
        int[] a=original.clone();
        TClasificadorImpl c=new TClasificadorImpl(mediana?TClasificadorImpl.Pivote.MEDIANA3:TClasificadorImpl.Pivote.INGENUO,true);
        if(intro) Introsort.ordenar(a,c); else c.quicksort(a);
        if(!Medidor.mismoContenidoOrdenado(original,a)) throw new AssertionError("Error adversario");
        return c.getComparaciones();
    }
}
