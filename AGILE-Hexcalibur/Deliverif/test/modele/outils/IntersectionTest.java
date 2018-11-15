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
public class IntersectionTest {
    
    public IntersectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - INTERSECTION TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - INTERSECTION TEST ----- FIN --------------");
    }

    /**
     * Test du constructeur
     * -Cas normal
     */
    @Test
    public void testConstructeur(){
        System.out.println("-- Constructeur");
        //Cas normal
        int idXML=0;
        float latitude=0.1f;
        float longitude=0.1f;
        Intersection inter = new Intersection(idXML, latitude, longitude);
        assertEquals(idXML, inter.getIdXML());
        assertEquals(latitude, inter.getLatitude(),0.001f);
        assertEquals(longitude, inter.getLongitude(),.001f);
        assertNotNull(inter.getTroncons());
        assertEquals(0,inter.getTroncons().size());
    }
    
    /**
     * Test de addTRoncon
     * -Ajout normal
     * -Ajout d'un null
     * -Ajout d'un troncon déjà présent
     */
    @Test
    public void testAddTroncon(){
        System.out.println("-- addTroncon");
        //Cas normal
        Intersection inter = new Intersection(0, 1.0f, 1.0f);
        Intersection inter2 = new Intersection(0, 1.0f, 1.0f);
        Troncon t = new Troncon("rue quelconque", inter, inter2, 1.0f); // L'appel de addTroncon est réalisé dans ce constructeur
        
        assertEquals(1, inter.getTroncons().size());
        assertEquals(0, inter2.getTroncons().size());
        assertSame(t, inter.getTroncon(0));
        
        //Cas des doublons
        inter.addTroncon(t);
        assertEquals(1, inter.getTroncons().size());
        Troncon t2 = new Troncon("rue quelconque", inter, inter2, 1.0f);
        assertEquals(1,inter.getTroncons().size());
        
        //Ajout d'un null
        inter.addTroncon(null);
        assertEquals(1, inter.getTroncons().size());
    }
    
    /**
     * test equals
     * -cas vrai
     * -cas faux
     */
    @Test
    public void testEquals(){
        System.out.println("-- equals");
        
        Intersection inter = new Intersection(0,0,0);
        Intersection inter2 = new Intersection(1,1,1);
        Intersection inter3 = new Intersection(0,0,0);
        
        assertTrue(inter.equals(inter3));
        assertTrue(inter3.equals(inter));
        assertFalse(inter.equals(inter2));
        assertFalse(inter2.equals(inter));
        assertFalse(inter.equals("banane"));
    }
    
    /**
     * Test de getTroncon
     * -indice normal
     * - indice out of bounds
     */
    @Test
    public void testGetTroncon(){
        System.out.println("-- getTroncon");
        
        Intersection inter = new Intersection(0,0,0);
        Intersection inter2 = new Intersection(1,1,1);
        Troncon t = new Troncon("Rue chocolat", inter, inter2, 100);
        
        assertSame(t, inter.getTroncon(0));
        assertNull(inter.getTroncon(-1));
        assertNull(inter.getTroncon(1));
    }
}
