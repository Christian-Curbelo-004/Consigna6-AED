package ucu.edu.aed.tda.clasificacion;

/** Seis algoritmos obligatorios. Quicksort recursivo heredado sin modificar. */
public class TClasificadorImpl extends TClasificador {
    public enum Pivote { INGENUO, MEDIANA3 }
    private final Pivote estrategia;
    private final boolean contar;
    private long comparaciones, movimientos;
    public TClasificadorImpl() { this(Pivote.INGENUO, false); }
    public TClasificadorImpl(Pivote estrategia, boolean contar) { this.estrategia=estrategia; this.contar=contar; }
    public long getComparaciones() { return comparaciones; }
    public long getMovimientos() { return movimientos; }
    public void reiniciarContadores() { comparaciones=movimientos=0; }
    /** Comparación de claves, no de índices. */
    protected int comparar(int a,int b) { if(contar) comparaciones++; return Integer.compare(a,b); }
    /** Movimiento = escritura de una clave; un intercambio cuenta dos. */
    void escribir(int[] a,int i,int x) { a[i]=x; if(contar) movimientos++; }
    @Override protected void intercambiar(int[] a,int i,int j) {
        if(i!=j) { int x=a[i]; escribir(a,i,a[j]); escribir(a,j,x); }
    }
    @Override public void insercionDirecta(int[] a) { insercionRango(a,0,a.length-1); }
    void insercionRango(int[] a,int lo,int hi) {
        for(int i=lo+1;i<=hi;i++) { int x=a[i],j=i;
            while(j>lo && comparar(a[j-1],x)>0) { escribir(a,j,a[j-1]); j--; }
            if(j!=i) escribir(a,j,x);
        }
    }
    @Override public void shell(int[] a,Integer[] incrementos) {
        for(int h:incrementos) { if(h<=0) throw new IllegalArgumentException("Incremento no positivo");
            for(int i=h;i<a.length;i++) { int x=a[i],j=i;
                while(j>=h && comparar(a[j-h],x)>0) { escribir(a,j,a[j-h]); j-=h; }
                if(j!=i) escribir(a,j,x);
            }
        }
    }
    @Override public void burbuja(int[] a) {
        for(int fin=a.length-1;fin>0;fin--) { boolean cambio=false;
            for(int i=0;i<fin;i++) if(comparar(a[i],a[i+1])>0) { intercambiar(a,i,i+1); cambio=true; }
            if(!cambio) break;
        }
    }
    @Override public void clasificacionDirecta(int[] a) {
        for(int i=0;i<a.length-1;i++) { int m=i;
            for(int j=i+1;j<a.length;j++) if(comparar(a[j],a[m])<0) m=j;
            intercambiar(a,i,m);
        }
    }
    private int ingenuo(int[] a,int i,int j) {
        int k=i+1; while(k<=j && comparar(a[k],a[i])==0) k++;
        if(k>j) return -1;
        return comparar(a[i],a[k])>0?i:k;
    }
    int mediana(int[] a,int i,int m,int j) {
        if(comparar(a[i],a[m])<0) return comparar(a[m],a[j])<0?m:(comparar(a[i],a[j])<0?j:i);
        return comparar(a[i],a[j])<0?i:(comparar(a[m],a[j])<0?j:m);
    }
    @Override protected int obtenerPivote(int[] a,int i,int j) {
        if(i>=j) return -1;
        int p;
        if(estrategia==Pivote.INGENUO) p=ingenuo(a,i,j);
        else { int m=i+(j-i)/2; p=mediana(a,i,m,j);
            // Una muestra estrictamente menor garantiza que el pivote no es el mínimo.
            if(comparar(a[i],a[p])>=0 && comparar(a[m],a[p])>=0 && comparar(a[j],a[p])>=0) p=ingenuo(a,i,j);
        }
        if(p==j) { intercambiar(a,i,j); p=i; }
        return p;
    }
    /** [i,k) < pivote; [k,j] >= pivote. Ambas partes no vacías. */
    @Override protected int particion(int[] a,int i,int j,int pivote) {
        int l=i,r=j;
        while(l<=r) {
            while(l<=r && comparar(a[l],pivote)<0) l++;
            while(l<=r && comparar(a[r],pivote)>=0) r--;
            if(l<r) { intercambiar(a,l,r); l++; r--; }
        }
        return l;
    }
    @Override public void heapsort(int[] a) { heapRango(a,0,a.length-1); }
    void heapRango(int[] a,int lo,int hi) {
        int n=hi-lo+1;
        for(int i=n/2-1;i>=0;i--) bajar(a,lo,i,n);
        for(int f=n-1;f>0;f--) { intercambiar(a,lo,lo+f); bajar(a,lo,0,f); }
    }
    private void bajar(int[] a,int lo,int i,int n) {
        int x=a[lo+i],original=i;
        while(i<n/2) { int h=2*i+1;
            if(h+1<n && comparar(a[lo+h+1],a[lo+h])>0) h++;
            if(comparar(a[lo+h],x)<=0) break;
            escribir(a,lo+i,a[lo+h]); i=h;
        }
        if(i!=original) escribir(a,lo+i,x);
    }
    @Override protected void desplazaElemento(int[] a,int primero,int ultimo) { bajar(a,0,primero,ultimo+1); }
    // Métodos de distribución fuera de la consigna; no se presentan como implementados.
    private UnsupportedOperationException fuera() { return new UnsupportedOperationException("Fuera del alcance de Consigna 6"); }
    @Override public void cuentaPorDistribucion(int[] a) { throw fuera(); }
    @Override public void binsort(int[] a) { throw fuera(); }
    @Override public void bucketsort(int[] a,int n) { throw fuera(); }
    @Override public void radixsort(int[] a,int base) { throw fuera(); }
    @Override protected void distribuirPorDigito(int[] a,int exp,int base) { throw fuera(); }
}
