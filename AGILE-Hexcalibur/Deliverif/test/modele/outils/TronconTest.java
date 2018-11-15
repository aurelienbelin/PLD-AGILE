/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     * Test du constructeur
     * - Cas normal
     * - Paramètres null
     */
    @Test
    public void testConstructeur(){
        System.out.println("-- constructeur");
        //Cas normal
        Intersection inter = new Intersection(0,0,0);
        Intersection inter2 = new Intersection(1,1,1);
        String nom = "bonjour";
        float longueur=10f;
        Troncon t = new Troncon(nom, inter, inter2, longueur);
        assertEquals(nom, t.getNom());
        assertSame(inter,t.getDebut());
        assertSame(inter2,t.getFin());
        assertEquals(longueur,t.getLongueur(),0.01f);
        
        //Cas avec null
        t = new Troncon(null, null, null, -3);
        assertNotNull(t.getNom());
        assertNotNull(t.getDebut());
        assertNotNull(t.getFin());
        assertTrue(t.getLongueur()==0);
    }   
    
}
