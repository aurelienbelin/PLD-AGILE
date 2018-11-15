/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lohl
 */
public class CheminTest {
    
    public CheminTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - CHEMIN TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - CHEMIN TEST ----- FIN --------------");
    }

    /**
     * Test of addTroncon method, of class Chemin.
     */
    @Test
    public void testAddTroncon() {
        System.out.println("-- methode addTroncon");
        Intersection inter1 = new Intersection(0, 1.0f, 1.0f);
        Intersection inter2 = new Intersection(0, 1.0f, 1.0f);
        PointPassage p1 = new PointPassage(true, inter1, 2f);
        PointPassage p2 = new PointPassage(false, inter2, 0.6f);
        Troncon t = new Troncon("rue", inter1, inter2, 1.0f);
        Chemin instance = new Chemin(p1,p2);
        assertSame(instance.getDebut(),p1);
        assertSame(instance.getDebut().getPosition(), inter1);
        assertSame(instance.getFin(), p2);
        assertSame(instance.getFin().getPosition(), inter2);
        
        instance.addTroncon(t);
        assertNull(instance.getFin());
        float expected = 1f/3.6f;
        assertEquals(1.0f, instance.getLongueur(), 0.02f);
        assertEquals(expected, instance.getDuree(), 0.02f);
        
        instance.setFin(p2);
        assertSame(instance.getFin(), p2);
        assertEquals(1.0f, instance.getLongueur(), 0.02f);
        expected = 1f/3.6f+0.6f;
        assertEquals(expected, instance.getDuree(), 0.02f);
    }
    
}
