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

/**
 *
 * @author lohl
 * @author rfournier
 */



public class PlanVille {
    
    private List<Intersection> intersections;
    private List<Troncon> troncons;
    
    public void dijkstra(PointPassage p){
        
        List<Intersection> nonVus = new ArrayList(intersections);
        
        List<List<Troncon>> resultat = new ArrayList();
        ListIterator<Intersection> interIt = nonVus.listIterator();
        ListIterator<Troncon> troncIt;
        
        Intersection origine = p.getPosition();
        Map<Intersection, Pair<Intersection,Float>> tab = new HashMap();
        Intersection sommetVisite;
        //retrait de l'origine de la liste des sommets non vus
        while(interIt.hasNext()){
            sommetVisite=interIt.next();
            if(sommetVisite.getLongitude()==origine.getLongitude()&&sommetVisite.getLatitude()==origine.getLatitude()){
                //permutation
                origine = sommetVisite;
                //retrait de la liste des nonVus
                nonVus.remove(sommetVisite);
                break;
            }
        }
        //raz de l'itérateur
        
        interIt = nonVus.listIterator();
        while(interIt.hasNext()){
            //parcours des voisins du point actuellement visité
            troncIt = origine.getTroncons().listIterator();
            while(troncIt.hasNext()){
                Troncon arc=troncIt.next();
                if(nonVus.contains(arc.getFin())){
                    //s'il y a deja une valeur dans le tableau de précédence :
                    if(tab.get(arc.getFin())!=null){
                        //on fait regarde s'il est plus avanageux de changer de point
                        if(tab.get(arc.getFin()).getValue()>(arc.getLongueur()+tab.get(origine).getValue())){
                            Pair<Intersection,Float> predecesseur = new Pair(origine,arc.getLongueur()+tab.get(origine).getValue());
                            tab.put(arc.getFin(), predecesseur);
                        }
                    }else{
                         Pair<Intersection,Float> predecesseur = new Pair(origine,arc.getLongueur());
                            tab.put(arc.getFin(), predecesseur);
                    }
                }
            }
        }
    }
}
