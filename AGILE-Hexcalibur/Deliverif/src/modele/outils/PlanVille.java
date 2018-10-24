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
    
    public PlanVille(List<Intersection> i, List<Troncon> t){
        this.intersections =i;
        this.troncons=t;
    }
    
    public Map<Intersection, Pair<Intersection, Float>> dijkstra(PointPassage p){
        
        List<Intersection> nonVus = new ArrayList(intersections);
        
        List<List<Troncon>> resultat = new ArrayList<List<Troncon>>();
        ListIterator<Intersection> interIt = nonVus.listIterator();
        ListIterator<Troncon> troncIt;
        
        Intersection origine = p.getPosition();
        Map<Intersection, Pair<Intersection,Float>> tab = new HashMap<Intersection, Pair<Intersection,Float>>();
        Intersection sommetVisite;
        //retrait de l'origine de la liste des sommets non vus
        while(interIt.hasNext()){
            sommetVisite=interIt.next();
            if(sommetVisite.getLongitude()==origine.getLongitude()&&sommetVisite.getLatitude()==origine.getLatitude()){
                //permutation
                origine = sommetVisite;
                break;
            }
        }
        //ajout de l'origine avec un cout nul dans le tableau
        tab.put(origine, new Pair(origine,(float)0));
        
        interIt = nonVus.listIterator();
        while(interIt.hasNext()){
            //parcours des voisins du point actuellement visité
            troncIt = origine.getTroncons().listIterator();
            while(troncIt.hasNext()){
                Troncon arc=troncIt.next();
                if(nonVus.contains(arc.getFin())){
                    //s'il y a deja une valeur dans le tableau de précédence :
                    if(tab.containsKey(arc.getFin())){
                        //on regarde s'il est plus avanageux de changer de point
                        if(tab.get(arc.getFin()).getValue()>(arc.getLongueur()+tab.get(origine).getValue())){
                            Pair<Intersection,Float> predecesseur = new Pair(origine,arc.getLongueur()+tab.get(origine).getValue());
                            tab.put(arc.getFin(), predecesseur);
                        }
                    }else{
                        Pair<Intersection,Float> predecesseur = new Pair(origine,arc.getLongueur()+tab.get(origine).getValue());
                        tab.put(arc.getFin(), predecesseur);
                    }
                }
            }
            //retrait de la liste des nonVus
            nonVus.remove(origine);
            // raz iterateur : 
            interIt = nonVus.listIterator();
            //choix du prochain sommet à visiter : recherche du sommet non visité avec le plus faible cout
            if(interIt.hasNext()){
                origine=interIt.next();
                Intersection candidat;
                while(interIt.hasNext()){
                    
                    candidat=interIt.next();
                    if(!tab.containsKey(origine)){
                        origine=candidat;
                    }else if(tab.containsKey(candidat)){
                        if(tab.get(candidat).getValue()<tab.get(origine).getValue()){
                            origine=candidat;
                        }
                    }
                }
                // raz iterateur : 
                interIt = nonVus.listIterator();
            }
        }
        return tab;
    }
}
