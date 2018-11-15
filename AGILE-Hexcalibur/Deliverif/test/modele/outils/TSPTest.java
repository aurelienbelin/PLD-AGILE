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
public class TSPTest {
    
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
    
    
    public TSPTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - TSP TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - TSP TEST ----- FIN --------------");
    }
    
    /**
     * Test de la validité du branch and bound d'un tsp personnalisé
     * par rapport à une référence.
     */
    @Test(timeout=10*60*1000)
    public void testChercheSolution(){
        System.out.println("-- chercheSolution");
        TSP reference;
        TSP algoTest;
        for(int nbLivreur=1; nbLivreur<5; nbLivreur++){//Pas plus que 18! possiblité
            System.out.println("\nnombre livreur : "+nbLivreur);
            reference = new TSPSimple(nbLivreur);
            algoTest = new TSPMinCFC(nbLivreur);
            reference.chercheSolution(Integer.MAX_VALUE, cout.length, nbLivreur, cout);
            algoTest.chercheSolution(Integer.MAX_VALUE, cout.length, nbLivreur, cout);
            assertEquals(reference.getCoutMeilleureSolution(), algoTest.getCoutMeilleureSolution());
            
        }
    }
    
}
