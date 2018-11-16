/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele;

import modele.Troncon;
import modele.Chemin;
import modele.Intersection;
import modele.PointPassage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lohl
 */
public class CheminTest {
    
    private PointPassage p1;
    private PointPassage p2;
    private List<Troncon> trajet;
    
    public CheminTest() {
        Intersection inter1 = new Intersection(0,1,1);
        Intersection inter2 = new Intersection(1,2,2);
        Intersection inter3 = new Intersection(3,3,3);
        p1 = new PointPassage(false, inter1, 60);
        p2 = new PointPassage(false, inter3, 2*60);//2 min sur place
        Troncon t1 = new Troncon("rue a", inter1, inter2, 250);
        Troncon t2 = new Troncon("rue b", inter2,inter3, 250);
        //250m = distance en m parcourue en 1min à 15 km/h
        this.trajet = new ArrayList<Troncon>();
        this.trajet.add(t1);
        this.trajet.add(t2);
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
     * Test du constructeur
     * - Paramètres ok
     * - Paramètres mauvais
     */
    @Test
    public void testConstructeur(){
        System.out.println("-- Constructeur");
        
        //Construction normale
        Chemin chemin = new Chemin(this.trajet, this.p1, this.p2);
        assertSame(this.trajet, chemin.getTroncons());
        assertSame(this.p1, chemin.getDebut());
        assertSame(this.p2, chemin.getFin());
        
        //Construction mauvaise
        chemin = new Chemin(null, null, null);
        assertNotNull(chemin.getTroncons());
    }
    
    /**
     * test de getDuree
     * - Chemin normal
     * - Chemin sans debut ni fin ni troncons
     */
    @Test
    public void testGetDuree(){
        System.out.println("-- getDuree");
        
        Chemin chemin = new Chemin(this.trajet, this.p1, this.p2);
        assertEquals(240, chemin.getDuree(),0.1f);
        
        chemin = new Chemin(null,null,null);
        assertEquals(0, chemin.getDuree(),0.1f);
    }
    
    /**
     * Test de getDescription
     * - Chemin normal
     * - Chemin sans debut ni fin ni troncons
     */
    @Test
    public void testGetDescription(){
        System.out.println("-- getDescription");
        
        Chemin chemin = new Chemin(this.trajet, this.p1, this.p2);
        assertEquals(9, chemin.getDescription(Calendar.getInstance()).size());
        
        assertEquals(9, chemin.getDescription(null).size());
        
        chemin = new Chemin(null,null,null);
        assertEquals(0, chemin.getDescription(Calendar.getInstance()).size());
    }
    
    /**
     * Test de getLongueur
     * - Chemin normal
     * - Chemin sans troncon
     * - Chemin sans debut
     * - Chemin sans fin
     */
    @Test
    public void testGetLongueur(){
        System.out.println("-- getLongueur");
        
        //Normal
        Chemin chemin = new Chemin(this.trajet, this.p1, this.p2);
        assertEquals(500, chemin.getLongueur(),0.1f);
        
        //Pas de troncon
        chemin = new Chemin(null,this.p1, this.p2);
        assertEquals(0, chemin.getLongueur(),0.1f);
        
        //Pas de debut
        chemin = new Chemin(this.trajet, null, this.p2);
        assertEquals(0, chemin.getLongueur(), 0.1f);
        
        //Pas de fin
        chemin = new Chemin(this.trajet, this.p1, null);
        assertEquals(0, chemin.getLongueur(),0.1f);
        
    }
    
}
