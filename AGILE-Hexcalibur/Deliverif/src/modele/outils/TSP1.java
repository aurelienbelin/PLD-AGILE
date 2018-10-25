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

public class TSP1 extends TemplateTSP {

    private int nbLivreur;
    //Liste contenant les sommets que l'on visite apres l'entrepot
    //Sert a casser les symetries.
    protected List<Integer> ordreDepart;//TODO : mettre en place
    
    public TSP1(int nbLivreur){
        this.nbLivreur=nbLivreur;
        this.ordreDepart = new ArrayList<Integer>();
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
            return new IteratorMin(nonVus, sommetCrt, cout);
        }
    }


    @Override
    protected int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        /**
         * Dans un premier temps, est-ce qu'on verifie la condition suivante :
         * premier sommet de la tournee i > premier sommet des tournees i-1
         * Si ce n'est pas le cas :
         * on renvoie une valeur trop elevee (coutMeilleureSolution)
         * sinon :
         * On réalise dans un dfs pour obtenir une approximation
         * du parcours de noeud.
         * Nous avons la garantie que cette solution sera nécessairement inférieure
         * à la solution optimale puisqu'elle ne prend pas en compte les allers-retours
         * vers les entrepots fictifs.
         */
        int premierSommet=0;
        boolean apresZero=false;
        for (Integer sommet : vus){
            if (apresZero){
                apresZero=false;
                if (sommet<=premierSommet){
                    return this.getCoutMeilleureSolution();
                } else {
                    premierSommet=sommet;
                }
            }
            if (sommet==0){
                apresZero=true;
            }
        }
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
