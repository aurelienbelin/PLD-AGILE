/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lohl
 */
public class TronconTest {
    
    public TronconTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - TRONCON TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - TRONCON TEST ----- FIN --------------");
    }

    /**
     * Test of setDebut method, of class Troncon.
     */
    @Test
    public void testDebut() {
        System.out.println("--methode setDebut");
        Intersection inter1 = new Intersection(0, 1.0f, 1.0f);
        Intersection inter2 = new Intersection(0, 1.0f, 1.0f);
        Intersection inter3 = new Intersection(0, 1.0f, 1.0f);
        Troncon t = new Troncon("rue", inter1, inter2, 1.0f);
        
        t.setDebut(inter3);
        assertNotSame(t.getDebut(), inter1);
        assertSame(t.getDebut(), inter3);
        assertSame(t.getFin(), inter2);
        
        assertEquals(0, inter1.qteTroncons());
        assertEquals(0, inter2.qteTroncons());
        assertEquals(1, inter3.qteTroncons());
    }

    /**
     * Test of getDebut method, of class Troncon.
     */
    @Test
    public void testFin() {
        System.out.println("--methode setFin");
        Intersection inter1 = new Intersection(0, 1.0f, 1.0f);
        Intersection inter2 = new Intersection(0, 1.0f, 1.0f);
        Intersection inter3 = new Intersection(0, 1.0f, 1.0f);
        Troncon t = new Troncon("rue", inter1, inter2, 1.0f);
        
        t.setFin(inter3);
        assertNotSame(t.getFin(), inter2);
        assertSame(t.getDebut(), inter1);
        assertSame(t.getFin(), inter3);
        
        assertEquals(1, inter1.qteTroncons());
        assertEquals(0, inter2.qteTroncons());
        assertEquals(0, inter3.qteTroncons());
    }
    
}
