/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.tsp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @version 1.0 23/10/2018
 * @author Louis
 */
public class TSPSimple extends TemplateTSP{
    private int nbLivreur;

    /**
     *
     * @param nbLivreur
     */
    public TSPSimple(int nbLivreur){
        this.nbLivreur=nbLivreur;
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout) {
        //Si on a parcouru total_sommet/livreur, alors on renvoie un itérateur
        //renvoyant à l'entrepot fictif, sinon on laisse un itérateur vers
        //les sommets non vus.
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
            return new IteratorSeq(aVoir, sommetCrt);
        } else {
            //Il n'est pas encore temps d'aller voir un entrepot virtuel
            return new IteratorSeq(nonVus, sommetCrt);
        }
    }


    @Override
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int nombreEntrepot) {
        
        return 0;
    }
}
