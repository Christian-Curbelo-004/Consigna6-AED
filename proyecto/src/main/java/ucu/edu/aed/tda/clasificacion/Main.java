package ucu.edu.aed.tda.clasificacion;
import ucu.edu.aed.tda.clasificacion.datos.*;
import java.util.*;
import java.nio.file.*;
public final class Main {
    static final long SEMILLA=2026;
    static final String[] ALGORITMOS={"insercion","shellsort","burbuja","quick-ingenuo","quick-mediana3","seleccion","heapsort","mergesort","introsort"};
    static TClasificadorImpl clasificador(String alg,boolean contar) {
        return new TClasificadorImpl(alg.equals("quick-mediana3")?TClasificadorImpl.Pivote.MEDIANA3:TClasificadorImpl.Pivote.INGENUO,contar);
    }
    static void ordenar(String alg,TClasificadorImpl c,int[] a) {
        switch(alg) {
            case "insercion" -> c.insercionDirecta(a);
            case "shellsort" -> c.shell(a,TipoClasificador.incrementosPorDefecto(a.length));
            case "burbuja" -> c.burbuja(a);
            case "quick-ingenuo","quick-mediana3" -> c.quicksort(a);
            case "seleccion" -> c.clasificacionDirecta(a);
            case "heapsort" -> c.heapsort(a);
            case "mergesort" -> Mergesort.ordenar(a,c);
            case "introsort" -> Introsort.ordenar(a,c);
            default -> throw new IllegalArgumentException(alg);
        }
    }
    public static void main(String[] args) throws Exception {
        if(args.length==0 || args[0].equals("demo")) { demo(); return; }
        switch(args[0]) {
            case "test" -> pruebas();
            case "adversario" -> Adversario.main(new String[0]);
            case "serie" -> Medicion.serie(args[1],Perfil.valueOf(args[2]),Integer.parseInt(args[3]),Boolean.parseBoolean(args[4]));
            case "medir" -> {
                try { medir(args[1],Perfil.valueOf(args[2]),Integer.parseInt(args[3]),args.length>4?Path.of(args[4]):null); }
                catch(Medicion.TiempoAgotado e) { System.out.println("CORTE_60S"); }
                catch(StackOverflowError e) { System.out.println("STACK_OVERFLOW"); }
            }
            case "operaciones" -> operaciones(args.length>1?Integer.parseInt(args[1]):10000);
            case "verificar-datos" -> verificarDatos(Path.of(args[1]));
            default -> throw new IllegalArgumentException("demo | test | adversario | serie algoritmo PERFIL max preliminar | medir algoritmo PERFIL n [archivo] | operaciones [n] | verificar-datos carpeta");
        }
    }
    private static void medir(String alg,Perfil p,int n,Path archivo) {
        int[] original=archivo==null?GeneradorPadrones.generar(p,n,SEMILLA):ArchivoDatos.leerEnteros(archivo);
        if(original.length!=n) throw new IllegalArgumentException("Tamaño incorrecto");
        TClasificadorImpl c=clasificador(alg,false);
        int[] cal=GeneradorPadrones.generar(Perfil.ALEATORIO,2000,SEMILLA);
        for(int i=0;i<3;i++) ordenar(alg,c,cal.clone());
        // Cada corrida verificada por Medidor; si supera el límite no se inicia otra.
        long[] t=new long[3];
        long warm=Medicion.medirUna(alg,c,original);
        if(warm>60_000_000_000L) { System.out.println("CORTE,"+warm/1e6); return; }
        for(int r=0;r<3;r++) {
            t[r]=Medicion.medirUna(alg,c,original);
            if(t[r]>60_000_000_000L) { System.out.println("CORTE,"+t[r]/1e6); return; }
        }
        System.out.printf(Locale.ROOT,"OK,%.6f,%.6f,%.6f,%.6f,%.6f%n",Medidor.mediana(t)/1e6,warm/1e6,t[0]/1e6,t[1]/1e6,t[2]/1e6);
    }
    private static void demo() {
        int[] a=GeneradorPadrones.generar(Perfil.POCOS_DISTINTOS,10000,SEMILLA);
        for(String alg:ALGORITMOS) {
            TClasificadorImpl c=clasificador(alg,false);
            double ms=Medidor.medianaMs(b->ordenar(alg,c,b),a,3,1);
            System.out.printf(Locale.ROOT,"%-16s %.3f ms%n",alg,ms);
        }
    }
    private static void operaciones(int n) {
        System.out.println("algoritmo,perfil,n,comparaciones,movimientos");
        for(Perfil p:Perfil.values()) { int[] a=GeneradorPadrones.generar(p,n,SEMILLA);
            for(String alg:ALGORITMOS) { TClasificadorImpl c=clasificador(alg,true); int[] copia=a.clone();
                ordenar(alg,c,copia);
                if(!Medidor.mismoContenidoOrdenado(a,copia)) throw new AssertionError(alg);
                System.out.printf("%s,%s,%d,%d,%d%n",alg,p,n,c.getComparaciones(),c.getMovimientos());
            }
        }
    }
    private static void verificarDatos(Path dir) throws Exception {
        for(Perfil p:Perfil.values()) { Path archivo=dir.resolve("padrones-"+p.name().toLowerCase(Locale.ROOT).replace('_','-')+".txt");
            if(!Files.exists(archivo)) { System.out.println(p+": no suministrado; se genera"); continue; }
            int[] a=ArchivoDatos.leerEnteros(archivo);
            if(!Arrays.equals(a,GeneradorPadrones.generar(p,a.length,SEMILLA))) throw new AssertionError(p+": datos distintos");
            System.out.println(p+": coincide, n="+a.length);
        }
    }
    static void pruebas() {
        Random rnd=new Random(2026); int total=0;
        List<int[]> casos=new ArrayList<>(List.of(new int[]{},new int[]{1},new int[]{2,1},new int[]{1,1,1,2},new int[]{2,1,1,1},new int[]{Integer.MAX_VALUE,0,Integer.MIN_VALUE},new int[]{7,7,7}));
        for(int n=0;n<=128;n++) for(int k=0;k<6;k++) { int[] a=new int[n]; for(int i=0;i<n;i++) a[i]=k%2==0?rnd.nextInt(9)-4:rnd.nextInt(); casos.add(a); }
        for(Perfil p:Perfil.values()) for(int n:new int[]{17,100,1000}) casos.add(GeneradorPadrones.generar(p,n,SEMILLA));
        casos.add(Adversario.entradaAsesina(1000));
        casos.add(Adversario.entradaIngenua(1000));
        for(int[] a:casos) for(String alg:ALGORITMOS) { int[] copia=a.clone(); ordenar(alg,clasificador(alg,true),copia);
            if(!Medidor.mismoContenidoOrdenado(a,copia)) throw new AssertionError(alg+" "+Arrays.toString(a)); total++; }
        System.out.println("Pruebas correctas: "+total);
    }
}
