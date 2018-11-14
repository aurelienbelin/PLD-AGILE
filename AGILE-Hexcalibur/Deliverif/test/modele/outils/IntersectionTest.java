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
     * Test of addTroncon method, of class Intersection.
     */
    @Test
    public void testAddTroncon() {
        System.out.println("--methode addTroncon");
        Intersection inter = new Intersection(0, 1.0f, 1.0f);
        Intersection inter2 = new Intersection(0, 1.0f, 1.0f);
        Troncon t = new Troncon("rue quelconque", inter, inter2, 1.0f);
        
        assertEquals(1, inter.getTroncons().size());
        assertEquals(0, inter2.getTroncons().size());
        assertSame(t, inter.getTroncon(0));
        assertNull(inter2.getTroncon(0));
        
        inter.addTroncon(t); // Aucun troncons ne devrait être en double
        assertEquals(1, inter.getTroncons().size());
        
        Intersection inter3 = new Intersection(0,1.0f, 1.0f);
        Troncon t2 = new Troncon("une autre rue", inter3, inter3, 1.0f);
        assertEquals(1, inter3.getTroncons().size());
        assertSame(t2.getDebut(), t2.getFin());
    }

    /**
     * Test of removeTroncon method, of class Intersection.
     */
    @Test
    public void testRemoveTroncon() {
        System.out.println("--methode removeTroncon");
        
        Intersection inter = new Intersection(0, 1.0f, 1.0f);
        Intersection inter2 = new Intersection(0, 1.0f, 1.0f);
        Troncon t = new Troncon("rue quelconque", inter, inter2, 1.0f);
        
        inter.removeTroncon(t);
        assertEquals(0, inter.getTroncons().size());
        assertNull(inter.getTroncon(0));
    }
}
