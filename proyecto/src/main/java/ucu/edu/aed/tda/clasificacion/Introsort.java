package ucu.edu.aed.tda.clasificacion;
/** Mediana de tres, partición de Hoare, límite de profundidad y heapsort. */
public final class Introsort {
    private static final int UMBRAL=16;
    private Introsort() {}
    public static void ordenar(int[] a) { ordenar(a,new TClasificadorImpl()); }
    public static void ordenar(int[] a,TClasificadorImpl c) {
        if(a.length>1) intro(a,0,a.length-1,2*(31-Integer.numberOfLeadingZeros(a.length)),c);
    }
    private static void intro(int[] a,int lo,int hi,int limite,TClasificadorImpl c) {
        while(hi-lo+1>UMBRAL) {
            if(limite==0) { c.heapRango(a,lo,hi); return; }
            limite--;
            int p=a[c.mediana(a,lo,lo+(hi-lo)/2,hi)];
            int i=lo,j=hi;
            while(i<=j) {
                while(c.comparar(a[i],p)<0) i++;
                while(c.comparar(a[j],p)>0) j--;
                if(i<=j) { c.intercambiar(a,i,j); i++; j--; }
            }
            if(j-lo<hi-i) { intro(a,lo,j,limite,c); lo=i; }
            else { intro(a,i,hi,limite,c); hi=j; }
        }
        c.insercionRango(a,lo,hi);
    }
}
