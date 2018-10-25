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
 *
 * @author Louis
 */
public class TSPSimple extends TemplateTSP{
    private int nbLivreur;

    public TSPSimple(int nbLivreur){
        this.nbLivreur=nbLivreur;
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout) {
        //Si on a parcouru total_sommet/livreur, alors on renvoie un itérateur
        //renvoyant à l'entrepot fictif, sinon on laisse un itérateur vers
        //les sommets non vus.
        int quantiteSommet = (cout.length-1)/this.nbLivreur;
        //-1 car on ne compte pas le sommet entrepot
        if ((cout.length-1-nonVus.size())%quantiteSommet==0 && sommetCrt!=0){
            List<Integer> aVoir = new ArrayList<Integer>();
            aVoir.add(0);
            return new IteratorSeq(aVoir, sommetCrt);
        } else {
            return new IteratorSeq(nonVus, sommetCrt);
        }
    }


    @Override
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        return 0;
    }
}
