package ucu.edu.aed.tda.clasificacion;
import junit.framework.TestCase;
public class ClasificacionTest extends TestCase {
    public void testAlgoritmos() { Main.pruebas(); }
    public void testContadores() {
        TClasificadorImpl c=new TClasificadorImpl(TClasificadorImpl.Pivote.INGENUO,true);
        c.burbuja(new int[]{2,1});
        assertEquals(1L,c.getComparaciones()); assertEquals(2L,c.getMovimientos());
        c.reiniciarContadores(); c.clasificacionDirecta(new int[]{3,2,1});
        assertEquals(3L,c.getComparaciones()); assertEquals(2L,c.getMovimientos());
    }
}
