/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import javafx.util.Pair;

/**
 *
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 * @author Romain Fournier
 */
public class PlanVille {
    
    private List<Intersection> intersections;
    private List<Troncon> troncons;
    
    /**
     * Crée un nouveau plan de ville à partir d'un ensemble d'intersections reliées
     * par un ensemble de troncons.
     * @param i - Une liste d'intersections composant la ville.
     * @param t - Une liste de troncons reliant les intersections de la ville.
     */
    public PlanVille(List<Intersection> i, List<Troncon> t){
        this.intersections =i;
        this.troncons=t;
    }

    /**
     * @return La liste des intersections de la ville.
     */
    public List<Intersection> getIntersections() {
        return intersections;
    }

    /**
     * @return La liste des troncons reliant les intersections de la ville.
     */
    public List<Troncon> getTroncons() {
        return troncons;
    }
    
    /**
     * Réalise l'algorithme de Dijkstra au départ d'un point donné.
     * @param p - Le PointPassage duquel l'algorithme doit partir.
     * @return Une HashMap réalisation le hashage suivant : Intersection i1 => (i2,v)
     * où i2 est l'Intersection précédente et v un float représent le cout pour aller de
     * p à i1.
     */
    protected Map<Intersection, Pair<Intersection, Float>> dijkstra(PointPassage p){
        
        
        List<List<Troncon>> resultat = new ArrayList<List<Troncon>>();
        ListIterator<Troncon> troncIt;
        int sommetVus=0;
        Intersection origine = p.getPosition();
        Map<Intersection, Pair<Intersection,Float>> tab = new HashMap<Intersection, Pair<Intersection,Float>>();
        PriorityQueue<Intersection> nonVus = new PriorityQueue<Intersection>(this.intersections.size(),
        new Comparator<Intersection>(){
            @Override
            public int compare(Intersection i1, Intersection i2){
                if (tab.get(i1).getValue()<tab.get(i2).getValue()){
                    return -1;
                } else if(tab.get(i1).getValue()>tab.get(i2).getValue()){
                    return 1;
                }
                return 0;
            }
        });
        //Identifier l'intersection de depart
        for(Intersection sommetVisite : this.intersections){
            if(sommetVisite.equals(origine)){
                //permutation
                origine = sommetVisite;
                break;
            }
        }
        //ajout de l'origine avec un cout nul dans le tableau
        tab.put(origine, new Pair(origine,(float)0));
        nonVus.add(origine);
        
        while(!nonVus.isEmpty()){
            origine=nonVus.poll();
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
        }
        return tab;
    }
    
    /**
     * Réalise l'algorithme de Dijkstra pour chaque points de passage, ce afin
     * de construire un graphe fortement connexe.
     * @param listePoints - L'ensemble des points de passage (livraison & entrepot)
     * par où on doit passer.
     * @return Une List de Chemin. Il est garanti que la liste sera de taille n² avec
     * n le nombre de point de passage. Dans cette liste, il existe toujours un unique
     * chemin menant de n_i à n_j, ce chemin étant le plus court possible dans la ville.
     */
    protected List<Chemin> dijkstraTousPoints(List<PointPassage> listePoints){
        List<Chemin> graph = new ArrayList<Chemin>();
        for(PointPassage depart : listePoints){
            Map<Intersection, Pair<Intersection,Float>> tab = dijkstra(depart);
            //identifier lesquels sont des points passages
            for(Intersection i : tab.keySet()){
                for(PointPassage arrivee : listePoints){ 
                    if(arrivee!=depart){
                        //pour chaque
                        if(i.equals(arrivee.getPosition())){
                            //recréer la liste de chemin
                            Chemin chemin = reconstruireChemin(depart, arrivee, tab);
                            //ajouter a la liste des chemins entre pts de passages
                            graph.add(chemin); 
                        }
                    }
                }
            }
        }
        return graph;
    }
    
    /**
     * Reconstruit le chemin menant d'un point de départ à un point d'arrivée à l'aide de la structure de précédence générée
     * par dijkstra.
     * @param depart - Le Point de passage duquel le chemin doit partir.
     * @param arrivee - Le point de passage où l'on souhaite arriver.
     * @param tab - Une Map liant à une intersection son cout et sa précédence. Il s'agit de la structure obtenue après algorithme de Dijkstra.
     * @return Le Chemin menant du départ à l'arrivée.
     */
    protected Chemin reconstruireChemin(PointPassage depart, PointPassage arrivee, Map<Intersection, Pair<Intersection, Float>> tab){
        List<Troncon> trChemin = new ArrayList<Troncon>();
        //retrouver point précédent
        Intersection ptInter1 = tab.get(arrivee.getPosition()).getKey();
        Intersection ptInter2=arrivee.getPosition();
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
        return new Chemin(trChemin, depart, arrivee);
    }
}
