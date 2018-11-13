/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lohl
 */
public class TSP1Test {
    
    private List<List<Chemin>> adjacence;
    int[][] cout = {{0,29,20,21,16,31,100,12,4,31,18},
                    {29,0,15,29,28,40,72,21,29,41,12},
                    {20,15,0,15,14,24,81,9,23,27,13},
                    {21,29,15,0,4,12,92,12,25,13,25},
                    {16,28,14,4,0,16,94,9,20,16,22},
                    {31,40,24,12,16,0,95,24,36,3,37},
                    {100,72,81,92,94,95,0,90,101,99,84},
                    {12,21,9,12,9,24,90,0,15,25,13},
                    {4,29,23,25,20,36,101,15,0,35,38},
                    {31,41,27,13,16,3,99,25,35,0,38},
                    {18,12,13,25,22,37,84,13,18,38,0}};
    
    
    public TSP1Test() {
        adjacence = new ArrayList<List<Chemin>>();
        //https://stackoverflow.com/questions/11007355/data-for-simple-tsp
        List<Intersection> intersections = new ArrayList<Intersection>();
        List<PointPassage> passages = new ArrayList<PointPassage>();
        for(int i=0; i<cout.length; i++){
            Intersection inter = new Intersection(i,i,i);
            intersections.add(inter);
            passages.add(new PointPassage((i==0 ? true : false), inter, 1f));
        }
        for(int i=0; i<cout.length; i++){
            List<Chemin> noeud1 = new ArrayList<Chemin>();
            for(int j=0; j<cout[i].length; j++){
                if (cout[i][j]!=0){
                    Troncon t = new Troncon(i+"->"+j, intersections.get(i),
                            intersections.get(j), cout[i][j]);
                    List<Troncon> parcours = new ArrayList<Troncon>();
                    parcours.add(t);
                    Chemin c = new Chemin(parcours, passages.get(i), passages.get(j));
                    noeud1.add(c);
                }
            }
            adjacence.add(noeud1);
        }
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - TSP1 TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - TSP1 TEST ----- FIN --------------");
    }
    

    /**
     * Test of iterator method, of class TSP1.
     */
    /*@Test
    public void testIterator() {
        System.out.println("-- methode iterator");
        ArrayList<Integer> nonVus = new ArrayList<Integer>();
        int nombreVu=3;
        for(int i=nombreVu+1; i<cout.length; i++){//supposition d'avoir vu 3 noeuds
            nonVus.add(i);
        }
        for(int nbLivreur=1; nbLivreur<cout.length/2;nbLivreur++){
            TSPGlouton tsp = new TSPGlouton(nbLivreur);
            Iterator<Integer> it = tsp.iterator(nombreVu, nonVus, cout);
            int premierElt = it.next();
            System.out.println(premierElt);
            int quantiteSommet = (cout.length-1)/nbLivreur;
            if ((cout.length-1-nonVus.size())%quantiteSommet==0){
                assertEquals(0,premierElt);
                assertFalse(it.hasNext());
            } else {
                int meilleur=nombreVu+1;
                for(int i=nombreVu+1; i<cout.length; i++){
                    if (cout[nombreVu][i]<cout[nombreVu][meilleur]){
                        meilleur=i;
                    }
                }
                assertEquals(meilleur, premierElt);
                assertTrue(it.hasNext());
            }
            
        }
    }*/

    /**
     * Test of bound method, of class TSP1.
     */
    /*@Test
    public void testBound() {
        System.out.println("--methode bound");
        TSPGlouton tsp = new TSPGlouton(2);
        ArrayList<Integer> nonVus = new ArrayList<Integer>();
        int depart=0;
        for(int i=depart+1; i<cout.length; i++){
            nonVus.add(i);
        }
        ArrayList<Integer> vus= new ArrayList<>();
        vus.add(depart);
        int resultat=tsp.bound(vus, depart, nonVus, cout);
        assertEquals(199,resultat);
    }*/
    
    @Test
    public void testTSP(){
        System.out.println("--methode chercheSolution");
        TemplateTSP tsp = new TSPMinCFC(2);
        int[] resultat = {0,8,4,3,5,9,0,10,1,6,2,7};
        tsp.chercheSolution(Integer.MAX_VALUE, cout.length,2, cout);
        assertFalse(tsp.getTempsLimiteAtteint());
        assertEquals(278, tsp.getCoutMeilleureSolution());
        for(int i=0; i<resultat.length; i++){
            System.out.print(tsp.getMeilleureSolution(i)+" ");
        }
        System.out.println();
        for(int i=0; i<resultat.length; i++){
            assertEquals(resultat[i], (int)tsp.getMeilleureSolution(i));
        }
    }
    
    @Test
    public void testTSP2(){
        System.out.println("-- Test du tsp");
        
        for(int nbLivreur=1; nbLivreur<cout.length; nbLivreur++){
            System.out.println("\n livreurs : "+nbLivreur);
            TemplateTSP tsp = new TSPSimple(nbLivreur);
            tsp.chercheSolution(5000, cout.length, nbLivreur, cout);
            assertFalse(tsp.getTempsLimiteAtteint());
            for(int i=0; i<cout.length+nbLivreur-1;i++){
                assertNotNull(tsp.getMeilleureSolution(i));
                System.out.print(tsp.getMeilleureSolution(i)+" ");
                if (i==cout.length+nbLivreur-2){
                    assertNotEquals(0, (int)tsp.getMeilleureSolution(i));
                }
            }
        }
        System.out.println();
    }
    
    @Test
    public void testConvenable(){
        System.out.println("-- methode solutionConvenable");
        int[] ordre= {0,8,0,7,4,0,10,1,0,2,0,3,0,9,0,5,6,0};
        TemplateTSP tsp = new TSPMinCFC(9);
        tsp.chercheSolution(1000, cout.length, 9, cout);
        ArrayList<Integer> vus = new ArrayList<Integer>();
        ArrayList<Integer> nonVus = new ArrayList<Integer>();
        for(int o : ordre){
            nonVus.add(o);
        }
        for(int i=0; i<ordre.length; i++){
            vus.add(ordre[i]);
            nonVus.remove((Integer)ordre[i]);
            if(i>2){
                assertFalse(tsp.solutionConvenable(vus, nonVus, cout));
            } else {
                assertTrue(tsp.solutionConvenable(vus, nonVus, cout));
            }
        }
    }
    
}
