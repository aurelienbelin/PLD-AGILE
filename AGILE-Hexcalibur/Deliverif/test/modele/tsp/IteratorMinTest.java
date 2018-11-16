/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.tsp;

import modele.tsp.IteratorMin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Hex'calibur
 */
public class IteratorMinTest {
    
    public IteratorMinTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - ITERATORMIN TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - ITERATORMIN TEST ----- FIN --------------");
    }

    /**
     * Test of hasNext method, of class IteratorMin.
     */
    @Test(timeout = 1000)
    public void testConstruction() {
        System.out.println("-- Constructeur");
        int n=50;//50 noeuds dans le graphe
        int[] cout = new int[n];
        for (int i=0; i<n; i++){
            cout[i]=i*i;
        }
        List<Integer> nonVus = new ArrayList<Integer>();
        int m = 40; //40 non vus.
        Random rnd = new Random();
        int ensembleNonVus = rnd.nextInt(2<<m);
        for(int i=m-1; i>0; i--){
            int aAjouter = (ensembleNonVus>>i)&0x1;
            if (aAjouter==1){
                nonVus.add(i);
            }
        }
        IteratorMin it = new IteratorMin(nonVus, cout);
        int precedent = it.next();
        while(it.hasNext()){
            int courant = it.next();
            assertTrue(cout[precedent]<cout[courant]);
            precedent=courant;
        }
    }
    
}
