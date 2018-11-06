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
 * Ceci est une première implémentation (partiellement fausse) du TSP.
 * Elle s'appelle Glouton car elle utilise un pseudo-DFS en tant qu'heuristique,
 * c'est-à-dire pas une réelle heuristique. En effet, la fonction bound
 * peut renvoyer une valeur supérieure au chemin optimal.
 * En résumé, la stratégie gloutonne consiste à visiter en priorité les plus
 * proches voisins.
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class TSPGlouton extends TemplateTSP {

    private int nbLivreur;

    
    /**
     * Construit une nouvelle instance pour résoudre un TSP.
     * @param nbLivreur - Le nombre de livreur qui se partageront les points
     * de livraisons.
     */
    public TSPGlouton(int nbLivreur){
        this.nbLivreur=nbLivreur;
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout) {
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


    @Override
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        
        /* On réalise un pseudo-dfs pour obtenir une approximation
         * du parcours de noeud.
         * Nous avons la garantie que cette solution sera nécessairement inférieure
         * à la solution optimale puisqu'elle ne prend pas en compte les allers-retours
         * vers les entrepots fictifs.
         */
        
        
        ArrayList<Integer> aVoir = new ArrayList<Integer>(nonVus);
        int resultat=0;
        while(!aVoir.isEmpty()){
            Integer suivant=aVoir.get(0);
            for(Integer elt : aVoir){

                if (cout[sommetCourant][elt]<cout[sommetCourant][suivant]){
                    suivant=elt;
                }
            }
            resultat+=cout[sommetCourant][suivant];
            aVoir.remove(suivant);
            sommetCourant=suivant;
        }
        return resultat;
    }
}
