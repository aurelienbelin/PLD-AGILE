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
import modele.PointPassage;
import modele.Intersection;
import modele.Tournee;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Hex'calibur
 */
public class TourneeTest {
    
    private List<Chemin> chemins;
    
    public TourneeTest() {
        Intersection inter1 = new Intersection(1,1,1);
        Intersection inter2 = new Intersection(2,2,2);
        Intersection inter3 = new Intersection(3,3,3);
        PointPassage p1 = new PointPassage(true, inter1, 0);
        PointPassage p2 = new PointPassage(false, inter2, 60);
        PointPassage p3 = new PointPassage(false, inter3, 60);
        Troncon t12 = new Troncon("Rue 1->2", inter1,inter2, 250);
        Troncon t23 = new Troncon("Avenue 2->3", inter2, inter3, 500);
        List<Troncon> tChemin1 = new ArrayList<>();
        tChemin1.add(t12);
        Chemin chemin1 = new Chemin(tChemin1, p1,p2);
        List<Troncon> tChemin2 = new ArrayList<>();
        tChemin2.add(t23);
        Chemin chemin2 = new Chemin(tChemin2, p2,p3);
        chemins = new ArrayList<>();
        chemins.add(chemin1);
        chemins.add(chemin2);
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - TOURNEE TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - TOURNEE TEST ----- FIN --------------");
    }

    /**
     * Test du constructeur
     * - Bon paramètres
     * - Mauvais paramètres
     */
    @Test
    public void testConstructeur(){
        System.out.println("-- Constructeur");
        
        //Bonne construction
        Tournee t = new Tournee(this.chemins, Calendar.getInstance());
        assertSame(this.chemins, t.getTrajet());
        assertEquals(2, t.nombrePoints());
        
        //Mauvaise construction
        t = new Tournee(null,null);
        assertNotNull(t.getTrajet());
        assertEquals(0, t.nombrePoints());
    }

    /**
     * Test de getTempsTournee
     * - Avec une tournée bien construite
     * - Avec une tournée mal construite
     */
    @Test
    public void testGetTempsTournee() {
        System.out.println("-- getTempsTournee");
        
        //Tournée bien construite
        Tournee t = new Tournee(this.chemins, Calendar.getInstance());
        assertEquals(300, t.getTempsTournee(), 0.1f);
        
        //Tournée mal construite
        t = new Tournee(null, Calendar.getInstance());
        assertEquals(0, t.getTempsTournee(), 0.1f);
    }

    /**
     * Test de getLongueur
     * - Avec une tournée bien construite
     * - Avec une tournée mal construite
     */
    @Test
    public void testGetLongueur() {
        System.out.println("-- getLongueur");
        
        //Tournée bien construite
        Tournee t = new Tournee(this.chemins, Calendar.getInstance());
        assertEquals(750, t.getLongueur(), 0.1f);
        
        //Tournée mal construite
        t = new Tournee(null, Calendar.getInstance());
        assertEquals(0, t.getLongueur(), 0.1f);
    }

    /**
     * Test de getPointPassage
     * - Avec un indice valide
     * - Avec des indices out of bounds
     * - Avec une tournée mal construite
     */
    @Test
    public void testGetPointPassage() {
        System.out.println("-- getPointPassage");
        PointPassage p1 = this.chemins.get(0).getDebut();
        PointPassage p2 = this.chemins.get(1).getDebut();
        PointPassage p3 = this.chemins.get(1).getFin();
        //Indice valide
        Tournee t = new Tournee(this.chemins, null);
        assertSame(p1, t.getPointPassage(0));
        assertSame(p2, t.getPointPassage(1));
        assertSame(p3, t.getPointPassage(2));
        //Indice out of bounds
        assertNull(t.getPointPassage(-1));
        assertNull(t.getPointPassage(3));
        
        //Tournée mal construite
        t =new Tournee(null, null);
        assertNull(t.getPointPassage(0));
    }

    /**
     * Test de contientPointPassage
     * - Avec un point présent
     * - Avec un point absent
     * - Avec un null
     * - Avec une tournée mal construite
     */
    @Test
    public void testContientPointPassage() {
        System.out.println("-- contientPointPassage");
        
        PointPassage p2 = this.chemins.get(0).getFin();
        Tournee t = new Tournee(this.chemins, Calendar.getInstance());
        
        //Point présent
        assertTrue(t.contientPointPassage(p2));
        
        //Point absent
        PointPassage fauxPoint = new PointPassage(false, null, -3);
        assertFalse(t.contientPointPassage(fauxPoint));
        
        //Point null
        assertFalse(t.contientPointPassage(null));
        
        //Tournée mal construite
        t = new Tournee(null, null);
        assertFalse(t.contientPointPassage(p2));
    }

    /**
     * Test de getDescription
     * - Avec une tournée bien construite
     * - Avec une tournée mal construite
     */
    @Test
    public void testGetDescription() {
        System.out.println("-- getDescription");
        
        //Tournée bien construite
        Tournee t = new Tournee(this.chemins, Calendar.getInstance());
        Iterator<List<String>> iterateur = t.getDescription();
        int qte=0;
        while(iterateur.hasNext()){
            qte++;
            iterateur.next();
        }
        assertEquals(3,qte);
        
        //Tournée mal construite
        // -> pas d'heure précisée
        t = new Tournee(this.chemins, null);
        iterateur = t.getDescription();
        qte=0;
        while(iterateur.hasNext()){
            qte++;
            iterateur.next();
        }
        assertEquals(3,qte);
        // -> pas de chemins
        t = new Tournee(null, null);
        iterateur = t.getDescription();
        assertNotNull(iterateur);
        assertFalse(iterateur.hasNext());
        
    }
    
}
