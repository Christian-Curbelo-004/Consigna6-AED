package ucu.edu.aed.tda.clasificacion;
import ucu.edu.aed.tda.clasificacion.datos.*;
import java.util.Locale;
import java.util.concurrent.*;
/** Serie geométrica, mediana de tres, verificación de cada corrida con Medidor. */
public final class Medicion {
    private Medicion() {}
    static void serie(String alg,Perfil p,int max,boolean preliminar) {
        TClasificadorImpl c=Main.clasificador(alg,false);
        int[] cal=GeneradorPadrones.generar(Perfil.ALEATORIO,2000,Main.SEMILLA);
        for(int i=0;i<4;i++) Main.ordenar(alg,c,cal.clone());
        double prev=-1,estimado=0; int anterior=0;
        String corte=null;
        for(int n=Math.min(10000,max);;n=Math.min(max,n*2)) {
            boolean lento=alg.equals("seleccion") || (alg.equals("insercion") || alg.equals("burbuja")) && p!=Perfil.ASCENDENTE || alg.equals("quick-ingenuo") && p!=Perfil.ALEATORIO && p!=Perfil.POCOS_DISTINTOS || alg.equals("quick-mediana3") && p==Perfil.TUBO_DE_ORGANO;
            if(prev>=0) { double ratio=(double)n/anterior;
                estimado=prev*(lento?ratio*ratio:ratio*Math.log(n)/Math.log(anterior));
                if(preliminar && lento && estimado>1500 && corte==null) corte="PREVENTIVO";
            }
            if(corte!=null) fila(alg,p,n,corte.equals("PREVENTIVO")?corte:"NO_MEDIDO_TRAS_"+corte,Double.NaN,estimado,null);
            else {
                try {
                    int[] a=GeneradorPadrones.generar(p,n,Main.SEMILLA);
                    long[] t=new long[3];
                    long warm=medirUna(alg,c,a);
                    if(warm>60_000_000_000L) { corte="CORTE_60S"; fila(alg,p,n,corte,warm/1e6,estimado,null); }
                    else {
                        for(int r=0;r<3;r++) { t[r]=medirUna(alg,c,a);
                            if(t[r]>60_000_000_000L) { corte="CORTE_60S"; fila(alg,p,n,corte,t[r]/1e6,estimado,null); break; }
                        }
                        if(corte==null) { prev=Medidor.mediana(t)/1e6; anterior=n; fila(alg,p,n,"OK",prev,Double.NaN,new long[]{warm,t[0],t[1],t[2]}); }
                    }
                } catch(TiempoAgotado e) { corte="CORTE_60S"; fila(alg,p,n,corte,Double.NaN,estimado,null); }
                catch(StackOverflowError e) { corte="STACK_OVERFLOW"; fila(alg,p,n,corte,Double.NaN,estimado,null); }
            }
            if(n==max) break;
        }
    }
    /** Se conserva Medidor: el vigilante externo solo limita el tiempo de pared.
     *  El hilo es daemon; si se agota el límite, no se ejecutan más algoritmos en este proceso.
     */
    static long medirUna(String alg,TClasificadorImpl c,int[] a) {
        FutureTask<Long> tarea=new FutureTask<>(()->Medidor.medirNanos(b->Main.ordenar(alg,c,b),a,1,0)[0]);
        Thread hilo=new Thread(tarea,"medicion"); hilo.setDaemon(true); hilo.start();
        try { return tarea.get(60,TimeUnit.SECONDS); }
        catch(TimeoutException e) { tarea.cancel(true); throw new TiempoAgotado(); }
        catch(InterruptedException e) { Thread.currentThread().interrupt(); throw new IllegalStateException(e); }
        catch(ExecutionException e) {
            if(e.getCause() instanceof Error err) throw err;
            if(e.getCause() instanceof RuntimeException err) throw err;
            throw new IllegalStateException(e.getCause());
        }
    }
    static final class TiempoAgotado extends RuntimeException {}
    private static void fila(String alg,Perfil p,int n,String estado,double ms,double est,long[] t) {
        System.out.printf(Locale.ROOT,"%s,%s,%d,%s,%s,%s,%s,%s,%s,%s%n",alg,p,n,estado,num(ms),num(est),t==null?"":num(t[0]/1e6),t==null?"":num(t[1]/1e6),t==null?"":num(t[2]/1e6),t==null?"":num(t[3]/1e6));
        System.out.flush();
    }
    private static String num(double x) { return Double.isNaN(x)?"":String.format(Locale.ROOT,"%.6f",x); }
}
