/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public class TSPSimpleTest {
    
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
    
    
    public TSPSimpleTest() {
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
                    Troncon t = new Troncon(0, i+"->"+j, intersections.get(i),
                            intersections.get(j), cout[i][j]);
                    Chemin c = new Chemin(passages.get(i), null);
                    c.addTroncon(t);
                    c.setFin(passages.get(j));
                    noeud1.add(c);
                }
            }
            adjacence.add(noeud1);
        }
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - TSPSimple TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - TSPSimple TEST ----- FIN --------------");
    }
    

    /**
     * Test of iterator method, of class TSPSimple.
     */
    @Test
    public void testIterator() {
        System.out.println("-- methode iterator");
        ArrayList<Integer> nonVus = new ArrayList<Integer>();
        int nombreVu=3;
        for(int i=nombreVu+1; i<cout.length; i++){//supposition d'avoir vu 3 noeuds
            nonVus.add(i);
        }
        for(int nbLivreur=1; nbLivreur<adjacence.size()/2;nbLivreur++){
            TSPSimple tsp = new TSPSimple(nbLivreur);
            Iterator<Integer> it = tsp.iterator(nombreVu, nonVus, cout);
            int premierElt = it.next();
            System.out.println("nbLivreur ("+nbLivreur+") size ("+cout.length+") nombreVu ("+ nombreVu+")");
            int quantiteSommet = (cout.length-1)/nbLivreur;
            if ((cout.length-1-nonVus.size())%quantiteSommet==0){
                System.out.println("\tVers l'entrepot");
                assertEquals(0,premierElt);
                assertFalse(it.hasNext());
            } else {
                System.out.println("\tsequentiel");
                assertEquals((int)nonVus.get(nonVus.size()-1), premierElt);
                assertTrue(it.hasNext());
            }
            
        }
    }

    /**
     * Test of bound method, of class TSPSimple.
     */
    @Test
    public void testBound() {
        System.out.println("--methode bound");
        TSPSimple tsp = new TSPSimple(2);
        ArrayList<Integer> nonVus = new ArrayList<Integer>();
        int depart=0;
        for(int i=depart+1; i<cout.length; i++){
            nonVus.add(i);
        }
        int resultat=tsp.bound(new ArrayList<Integer>(),depart, nonVus, cout);
        assertEquals(0,resultat);
    }
    
    @Test
    public void testTSP(){
        System.out.println("--methode chercheSolution");
        TSPSimple tsp = new TSPSimple(2);
        int[] resultat = {0,10,1,6,2,7,0,9,5,3,4,8};
        tsp.chercheSolution(8000, cout.length,2, cout);
        assertFalse(tsp.getTempsLimiteAtteint());
        assertEquals(278, tsp.getCoutMeilleureSolution());
        for(int i=0; i<resultat.length; i++){
            System.out.print(tsp.getMeilleureSolution(i)+" ");
        }
        for(int i=0; i<resultat.length; i++){
            assertEquals(resultat[i], (int)tsp.getMeilleureSolution(i));
        }
    }
    
}
