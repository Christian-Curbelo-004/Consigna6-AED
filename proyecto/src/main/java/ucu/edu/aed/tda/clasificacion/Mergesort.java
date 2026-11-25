package ucu.edu.aed.tda.clasificacion;
/** Comparación adicional de la hipótesis original. */
public final class Mergesort {
    private Mergesort() {}
    public static void ordenar(int[] a,TClasificadorImpl c) { merge(a,new int[a.length],0,a.length,c); }
    private static void merge(int[] a,int[] aux,int l,int r,TClasificadorImpl c) {
        if(r-l<2) return;
        int m=l+(r-l)/2; merge(a,aux,l,m,c); merge(a,aux,m,r,c);
        for(int k=l;k<r;k++) c.escribir(aux,k,a[k]);
        int i=l,j=m;
        for(int k=l;k<r;k++) {
            if(i==m || (j<r && c.comparar(aux[j],aux[i])<0)) c.escribir(a,k,aux[j++]);
            else c.escribir(a,k,aux[i++]);
        }
    }
}
