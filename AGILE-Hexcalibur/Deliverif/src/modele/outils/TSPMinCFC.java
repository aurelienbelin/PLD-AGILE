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
import java.util.Iterator;
import java.util.List;

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
        this.nombreFictif=0;
    }
    
    @Override
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        /*
        Pour faire simple, on sait que l'ensemble des nonVus formera toujours
        une CFC.
        Etant donné que nous devons parcourir l'ensemble des sommets nonVus
        une fois chacun, une borne inférieur du chemin optimal est le cout
        du plus petit arc multiplié par la taille de nonVus.
        */
        int minimum=Integer.MAX_VALUE;
        for(Integer depart : nonVus){
            for(Integer arrivee : nonVus){
                if (arrivee!=depart && cout[depart][arrivee]<minimum){
                    minimum=cout[depart][arrivee];
                }
            }
        }
        return minimum*nonVus.size();
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout) {
        //Si on a parcouru total_sommet/livreur, alors on renvoie un itérateur
        //renvoyant à l'entrepot fictif, sinon on laisse un itérateur vers
        //les sommets non vus.
        int quantiteSommet = (cout.length-1)/this.nbLivreur;
        //-1 car on ne compte pas le sommet entrepot
        if ((cout.length-1-nonVus.size())%quantiteSommet==0 && sommetCrt!=0 && this.nombreFictif!=this.nbLivreur-1){
            List<Integer> aVoir = new ArrayList<Integer>();
            aVoir.add(0);
            return new IteratorSeq(aVoir, sommetCrt);
        } else {
            return new IteratorMin(nonVus, sommetCrt, cout);
        }
    }
    
}
