/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import javafx.util.Pair;

/**
 * Ce TSP implémente une heuristique assez simple, ainsi qu'un itérateur
 * identique aux autres versions.
 * @version 1.0 31/10/2018
 * @author Louis Ohl
 */
public class TSPMinCFC extends TemplateTSP{

    int nbLivreur;
    
    /**
     * Construit une nouvelle instance pour résoudre un TSP.
     * @param nbLivreur - Le nombre de livreur qui se partageront les points
     * de livraisons.
     */
    public TSPMinCFC(int nbLivreur){
        this.nbLivreur=nbLivreur;
    }
    
    @Override
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        
        
        /*
        Pour faire simple, on sait que l'ensemble des nonVus formera toujours
        une CFC.
        Etant donné que nous devons parcourir l'ensemble des sommets nonVus
        une fois chacun (n sommets), une borne inférieur du chemin optimal est
        la somme des n-1 plus petits arc restants.
        */
        if (nonVus.size()<=1){
            return 0;
        }
        PriorityQueue<Pair<Integer, Integer>> file = new PriorityQueue<Pair<Integer,Integer>>(nonVus.size()*nonVus.size(),
            new Comparator<Pair<Integer, Integer>>(){
                @Override
                public int compare(Pair<Integer,Integer> p1, Pair<Integer,Integer> p2){
                    if (cout[p1.getKey()][p1.getValue()]<cout[p2.getKey()][p2.getValue()]){
                        return -1;
                    } else if (cout[p1.getKey()][p1.getValue()]>cout[p2.getKey()][p2.getValue()]){
                        return 1;
                    }
                    return 0;
                }
            });
        for(Integer depart : nonVus){
            for(Integer arrivee : nonVus){
                if (arrivee!=depart){
                    file.add(new Pair<Integer,Integer>(depart, arrivee));
                }
            }
        }
        int somme=0;
        for(int i=0; i<nonVus.size()-1; i++){
            Pair<Integer,Integer> p = file.poll();
            somme+=cout[p.getKey()][p.getValue()];
        }
        return somme;
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout) {
        //Si on a parcouru total_sommet/livreur, alors on renvoie un itérateur
        //renvoyant à l'entrepot fictif, sinon on laisse un itérateur vers
        //les sommets non vus.
        //Si on a parcouru total_sommet/livreur, alors on renvoie un itérateur
        //renvoyant à l'entrepot fictif, sinon on laisse un itérateur vers
        //les sommets non vus.
        //-1 car on ne compte pas le sommet entrepot
        
        int quantiteSommet = (cout.length-1)/this.nbLivreur;
        if (this.quantiteTournee==quantiteSommet+1){
            //Il est temps d'aller à l'entrepot virtuel
            List<Integer> aVoir = new ArrayList<Integer>();
            aVoir.add(0);
            return new IteratorSeq(aVoir, sommetCrt); 
        } else if (this.quantiteTournee==quantiteSommet){
            //On a vu le minimum de noeud possible par livreur, peut-être un de plus ?
            List<Integer> aVoir = new ArrayList<Integer>();
            aVoir.add(0);
            aVoir.addAll(nonVus);
            return new IteratorMin(aVoir, sommetCrt, cout);
        } else {
            //Il n'est pas encore temps d'aller voir un entrepot virtuel
            return new IteratorMin(nonVus, sommetCrt, cout);
        }
    }
    
}
