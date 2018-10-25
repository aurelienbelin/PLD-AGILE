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

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public List<Troncon> getTroncons() {
        return troncons;
    }

    public void setTroncons(List<Troncon> troncons) {
        this.troncons = troncons;
    }
    
    public Map<Intersection, Pair<Intersection, Float>> dijkstra(PointPassage p){
        
        List<Intersection> nonVus = new ArrayList();
        
        List<List<Troncon>> resultat = new ArrayList<List<Troncon>>();
        ListIterator<Troncon> troncIt;
        int sommetVus=0;
        Intersection origine = p.getPosition();
        Map<Intersection, Pair<Intersection,Float>> tab = new HashMap<Intersection, Pair<Intersection,Float>>();
        //retrait de l'origine de la liste des sommets non vus
        for(Intersection sommetVisite : this.intersections){
            if(sommetVisite.getLongitude()==origine.getLongitude()&&sommetVisite.getLatitude()==origine.getLatitude()){
                //permutation
                origine = sommetVisite;
                break;
            }
        }
        //ajout de l'origine avec un cout nul dans le tableau
        tab.put(origine, new Pair(origine,(float)0));
        nonVus.add(origine);
        
        ListIterator<Intersection> interIt = nonVus.listIterator();
        while(interIt.hasNext()){
            //parcours des voisins du point actuellement visité
            troncIt = origine.getTroncons().listIterator();
            while(troncIt.hasNext()){
                Troncon arc=troncIt.next();
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
                    nonVus.add(arc.getFin());
                }
            }
            //retrait de la liste des nonVus
            nonVus.remove(origine);
            sommetVus++;
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
            }
            // raz iterateur : 
            interIt = nonVus.listIterator();
        }
        return tab;
    }
    
    public List<Chemin> dijkstraToutPoints(List<PointPassage> listePoints){
        List<Chemin> graph = new ArrayList<Chemin>();
        for(PointPassage depart : listePoints){
            Map<Intersection, Pair<Intersection,Float>> tab = dijkstra(depart);
            //identifier lesquels sont des points passages
            for(Intersection i : tab.keySet()){
                for(PointPassage arrivee : listePoints){ 
                    if(arrivee!=depart){
                        //pour chaque
                        if(i.getLatitude()==arrivee.getPosition().getLatitude()&&i.getLongitude()==arrivee.getPosition().getLongitude()){
                        //recréer la liste de chemin
                            List<Troncon> trChemin = new ArrayList<Troncon>();
                            //retrouver point précédent
                            Intersection ptInter1 = tab.get(i).getKey();
                            Intersection ptInter2=i;
                            while(ptInter2!=depart.getPosition()){
                                //retrouver troncon entre ces 2 points
                                List<Troncon> trPossibles = ptInter1.getTroncons();
                                ListIterator<Troncon> it = trPossibles.listIterator();
                                while(it.hasNext()){
                                    Troncon tr = it.next();
                                    if(tr.getFin()==ptInter2){
                                        //l'ajouter en tête de liste
                                        trChemin.add(0,tr);
                                        break;
                                    }
                                }
                                //remonter d'un troncon
                                ptInter2=ptInter1;
                                ptInter1=tab.get(ptInter2).getKey();
                            }
                        //ajouter a la liste des chemins entre pts de passages
                        graph.add(new Chemin(trChemin,depart,arrivee)); 
                        }
                    }
                }
            }
        }
        return graph;
    }
}
