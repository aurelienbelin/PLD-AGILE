/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javafx.util.Pair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 *
 * @author romain
 */
public class PlanVilleTest {
    
    private List<Intersection> intersections;
    private List<Troncon> troncons;
    Intersection dep= new Intersection(1,3,0);
    PointPassage p = new PointPassage(false,dep,2);
    Intersection C = new Intersection(1,3,0);
        Intersection B = new Intersection(2,3,2);
        Intersection F= new Intersection(3,2,1);
        Intersection D = new Intersection(4,1,0);
        Intersection E = new Intersection(5,0,1);
        Intersection A = new Intersection(6,1,2);
        Troncon ED = new Troncon("ED",E,D,4);
        Troncon AE = new Troncon("AE",A,E,4);
        Troncon DC= new Troncon("DC",D,C,2);
        Troncon CD = new Troncon("CD",C,D,2);
        Troncon DA = new Troncon("DA",D,A,9);
        Troncon DF = new Troncon("DF",D,F,3);
        Troncon FA = new Troncon("FA",F,A,6);
        Troncon BA = new Troncon("BA",B,A,2);
        Troncon CB = new Troncon("CB",C,B,9);
        Troncon CF = new Troncon("CF",C,F,6);
        Troncon FC = new Troncon("FC",F,C,6);
        Troncon FB = new Troncon("FB",F,B,3);
        Troncon BF = new Troncon("BF",B,F,3);
    
    public PlanVilleTest(){
        
        troncons = new ArrayList<Troncon>();
        troncons.add(ED);
        troncons.add(AE);
        troncons.add(DC);
        troncons.add(CD);
        troncons.add(DA);
        troncons.add(DF);
        troncons.add(FA);
        troncons.add(BA);
        troncons.add(CB);
        troncons.add(CF);
        troncons.add(FC);
        troncons.add(FB);
        troncons.add(BF);
        intersections = new ArrayList<Intersection>();
        intersections.add(A);
        intersections.add(B);
        intersections.add(C);
        intersections.add(D);
        intersections.add(E);
        intersections.add(F);
    }
    
     @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - PlanVille TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - PlanVille TEST ----- FIN --------------");
    }
    
     /**
     * Test of dijkstra method, of class PlanVille.
     */
    @Test
    public void testDijkstra() {
        PlanVille pv = new PlanVille(intersections,troncons);
        System.out.println("-- dijkstra");
        Map<Intersection,Pair<Intersection,Float>> result = pv.dijkstra(p);
        assertEquals(8.0, result.get(B).getValue(),0.0);
        assertEquals(F, result.get(B).getKey());
        assertEquals(5.0, result.get(F).getValue(), 0.0);
        assertEquals(D, result.get(F).getKey());
        assertEquals(2.0, result.get(D).getValue(),0.0);
        assertEquals(C, result.get(D).getKey());
        assertEquals(10.0, result.get(A).getValue(),0.0);
        assertEquals(B, result.get(A).getKey());
    }
    
    /**
     * Test de dijkstraTousPoints
     * - Pas de points
     * - Certains points sont absent du plan
     * - Certains points sont null
     * - Tout va bien
     */
    @Test
    public void testDijkstraTousPoints(){
        System.out.println("-- dijkstraTousPoints");
        PlanVille pv = new PlanVille(intersections, troncons);
        PointPassage p2 = new PointPassage(false, A, 10);
        //Pas de points
        List<Chemin> resultat = pv.dijkstraTousPoints(null);
        assertNotNull(resultat);
        assertEquals(0, resultat.size());
        
        Intersection exterieur = new Intersection(10,10,10);
        PointPassage pDehors = new PointPassage(false, exterieur, 10);
        //Certains points sont en-dehors
        List<PointPassage> liste = new ArrayList<PointPassage>();
        liste.add(pDehors);
        liste.add(p);
        liste.add(p2);
        resultat = pv.dijkstraTousPoints(liste);
        assertNotNull(resultat);
        assertEquals(2, resultat.size());
        assertSame(p, resultat.get(0).getDebut());
        
        //Une liste avec des null
        liste = new ArrayList<PointPassage>();
        liste.add(null);
        liste.add(p);
        liste.add(p2);
        resultat = pv.dijkstraTousPoints(liste);
        assertNotNull(resultat);
        assertEquals(2,resultat.size());
        assertSame(p, resultat.get(0).getDebut());
        
        //Une liste avec un seul point !
        liste = new ArrayList<PointPassage>();
        liste.add(p);
        resultat=pv.dijkstraTousPoints(liste);
        assertNotNull(resultat);
        assertEquals(0,resultat.size());
        
        //Une liste correcte
        liste = new ArrayList<PointPassage>();
        liste.add(p);
        liste.add(p2);
        resultat = pv.dijkstraTousPoints(liste);
        assertNotNull(resultat);
        assertEquals(2, resultat.size());
        assertSame(p, resultat.get(0).getDebut());
        assertSame(p2, resultat.get(0).getFin());
        assertSame(p2, resultat.get(1).getDebut());
        assertSame(p, resultat.get(1).getFin());
        
        
    }
    
    /**
     * Test de reconstruireChemin
     * -Cas normal
     * - Avec un point inatteignable
     * - Avec un depart inatteignable
     * - Sans structure de precedence
     */
    @Test
    public void testReconstruireChemin(){
        System.out.println("-- reconstruireChemin");
        
        PlanVille pv = new PlanVille(intersections, troncons);
        
        
    }
}
