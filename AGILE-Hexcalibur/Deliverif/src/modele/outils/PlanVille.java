/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author lohl
 * @author rfournier
 */



public class PlanVille {
    
    private List<Intersection> intersections;
    private List<Troncon> troncons;
    
    public void dijkstra(PointPassage p){
        
        List<Intersection> nonVus = new ArrayList<Intersection>(intersections);
        
        List<List<Troncon> resultat = new ArrayList<List<Troncon>>();
        ListIterator<Intersection> interIt = nonVus.listIterator();
        ListIterator<Troncon> TroncIt;
        
        Intersection origine = p.getPosition();
        List<List<Pair<Intersection,float>>> tab = new ArrayList<List<Pair<Intersection,float>>>();
        nonVus.remove(origine);
        while(interIt.hasNext()){
            List<Pair<Intersection,float>> = new ArrayList<Pair<Intersection,float>>();
            troncIt = origine.getTroncons().listIterator();
            while(troncIt.hasNext()){
                Intersection inter=troncIt.next();
                if(nonVus.contains(inter)){
                    
                }
            }
        }
    }
    
    private Intersection getFin(List<Troncon> list){
        return (list.get(list.size() -1).getFin());
    }
    private
}
