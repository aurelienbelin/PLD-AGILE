/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author lohl
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
        int n=50;//50 noeuds dans le graphe
        int[][] cout = new int[1][];
        cout[0] = new int[n];
        for (int i=0; i<n; i++){
            cout[0][i]=i*i;
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
        IteratorMin it = new IteratorMin(nonVus, 0, cout);
        int precedent = it.next();
        while(it.hasNext()){
            int courant = it.next();
            assertTrue(cout[0][precedent]<cout[0][courant]);
            precedent=courant;
        }
    }
    
}
