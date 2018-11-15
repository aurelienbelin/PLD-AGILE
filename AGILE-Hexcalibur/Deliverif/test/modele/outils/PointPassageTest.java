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
 * @author Louis
 */
public class PointPassageTest {
    
    public PointPassageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - POINTPASSAGE TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - POINTPASSAGE TEST ----- FIN --------------");
    }

    /**
     * Test du constructeur
     */
    @Test
    public void testConstructeur() {
        System.out.println("-- Constructeur");
        
        //Bonne construction
        Intersection inter = new Intersection(2,3,3);
        PointPassage p = new PointPassage(false, inter, 10);
        
        assertFalse(p.estEntrepot());
        assertSame(inter, p.getPosition());
        assertEquals(10f, p.getDuree(),0.01f);
        
        //Mauvaise construction
        p = new PointPassage(true, null, -4);
        assertTrue(p.estEntrepot());
        assertNull(p.getPosition());
        assertTrue(p.getDuree()==0);
    }
    
}
