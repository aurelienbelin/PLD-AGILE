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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import javafx.util.Pair;

/**
 * Ce TSP implémente une heuristique assez simple, ainsi qu'un itérateur
 * identique aux autres versions.
 * @version 1.0 31/10/2018
 * @version 1.1 05/11/2018
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
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int nombreEntrepot) {
        
        
        /*
        Pour faire simple, on sait que l'ensemble des nonVus formera toujours
        une CFC.
        Etant donné que nous devons parcourir l'ensemble des sommets nonVus
        une fois chacun (n sommets), une borne inférieure du chemin optimal est
        la somme des plus petits arc partant de chaque noeuds.
        On ajoute ensuite le nombre de tournée restant * le plus petit aller
        vers l'entrepot.
        */
        if (nonVus.size()<=1){
            return 0;
        }
        
        int sommeMin=0;
        int minimum = Integer.MAX_VALUE;
        for(Integer arrivee : nonVus){
            if (cout[sommetCourant][arrivee]<minimum){
                minimum=cout[sommetCourant][arrivee];
            }
        }
        sommeMin+=minimum;
        int minEntrepot=Integer.MAX_VALUE;
        for(Integer depart : nonVus){
            minimum=Integer.MAX_VALUE;
            for(Integer arrivee : nonVus){
                if (cout[depart][arrivee]<minimum && depart!=arrivee){
                    minimum=cout[depart][arrivee];
                }
            }
            if (cout[depart][0]<minimum){
                minimum=cout[depart][0];
            }
            if (cout[depart][0]<minEntrepot){
                minEntrepot=cout[depart][0];
            }
            sommeMin+=minimum;
        }
        
        return sommeMin+(this.nbLivreur-nombreEntrepot)*minEntrepot;
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
            return new IteratorMin(aVoir, cout[sommetCrt]);
        } else {
            //Il n'est pas encore temps d'aller voir un entrepot virtuel
            return new IteratorMin(nonVus, cout[sommetCrt]);
        }
    }
    
}
