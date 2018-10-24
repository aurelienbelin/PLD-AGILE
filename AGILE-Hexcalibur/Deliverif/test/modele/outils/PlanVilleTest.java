/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;


import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Troncon ED = new Troncon(7,"ED",E,D,4);
        Troncon AE = new Troncon(8,"AE",A,E,4);
        Troncon DC= new Troncon(9,"DC",D,C,2);
        Troncon CD = new Troncon(10,"CD",C,D,2);
        Troncon DA = new Troncon(11,"DA",D,A,9);
        Troncon DF = new Troncon(12,"DF",D,F,3);
        Troncon FA = new Troncon(19,"FA",F,A,6);
        Troncon BA = new Troncon(13,"BA",B,A,2);
        Troncon CB = new Troncon(14,"CB",C,B,9);
        Troncon CF = new Troncon(15,"CF",C,F,6);
        Troncon FC = new Troncon(16,"FC",F,C,6);
        Troncon FB = new Troncon(17,"FB",F,B,3);
        Troncon BF = new Troncon(18,"BF",B,F,3);
    
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
        C.addTroncon(CD);
        C.addTroncon(CB);
        C.addTroncon(CF);
        B.addTroncon(BA);       
        B.addTroncon(BF);     
        F.addTroncon(FA);
        F.addTroncon(FB);
        F.addTroncon(FC);
        D.addTroncon(DC);
        D.addTroncon(DA);
        D.addTroncon(DF);
        E.addTroncon(ED);
        A.addTroncon(AE);
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
        Intersection C = new Intersection(1,3,0);
        Intersection B = new Intersection(2,3,2);
        Intersection F= new Intersection(3,2,1);
        Intersection D = new Intersection(4,1,0);
        Intersection E = new Intersection(5,0,1);
        Intersection A = new Intersection(6,1,2);
        PlanVille pv = new PlanVille(intersections,troncons);
        System.out.println("-- methode dijkstra");
        Map<Intersection,Pair<Intersection,Float>> result = pv.dijkstra(p);
        for(Intersection key : result.keySet()){
            System.out.println("pt :"+ key.getIdXML() + " pred : "+result.get(key).getKey().getIdXML() + " cout "+result.get(key).getValue());
            
        }
        assertTrue(result.containsKey(B));
        assertEquals((float)8, (float)result.get(B).getValue());
        assertEquals(F, result.get(B).getKey());
        assertEquals((float)5, (float)result.get(F).getValue());
        assertEquals(D, result.get(F).getKey());
        assertEquals((float)2, (float)result.get(D).getValue());
        assertEquals(C, result.get(D).getKey());
        assertEquals((float)10, (float)result.get(A).getValue());
        assertEquals(B, result.get(A).getKey());
        

    }
}
