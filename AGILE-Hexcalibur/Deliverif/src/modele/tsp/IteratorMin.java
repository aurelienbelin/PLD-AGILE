/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 * Un IteratorMin représente un itérateur permettant d'itérer sur l'ensemble des sommets non vus.
 * 
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class IteratorMin implements Iterator<Integer>{
    
    private Integer[] candidats;
    private int nbCandidats;

    /**
    * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
    * @param nonVus - L'ensemble des sommets non vus.
    * @param cout - Le tableau des cout t.q. cout[i]= le cout pour aller du
    * sommet i.
    */
    public IteratorMin(Collection<Integer> nonVus, int[] cout){
        this.candidats = new Integer[nonVus.size()];
        nbCandidats = 0;
        for (Integer s : nonVus){
            candidats[nbCandidats++] = s;
        }
        this.quicksort(0, this.candidats.length-1, cout);
    }
    
    /**
    * Réalise un tri rapide.
    * @param debut -  L'indice du debut du tableau a trier
    * @param fin - L'indice de fin du tableau a trier
    * @param cout - Le tableau des cout 
    */
    private void quicksort(int debut, int fin, int[] cout){
        if (debut<fin){
            int pivot = (fin-debut)/2+debut;
            pivot=this.arrange(debut, fin, pivot, cout);
            quicksort(debut, pivot-1, cout);
            quicksort(pivot+1, fin, cout);
        }
    }
    
    /**
    * Arrange les permutations de valeur par comparaison à un pivot.
    * @param debut - L'indice de debut du tableau a trier
    * @param fin - L'indice de fin du tableau a trier
    * @param pivot - L'indice du pivot
    * @param cout - Le tableau des cout
    */
    private int arrange(int debut, int fin, int pivot, int[] cout){
        int j=debut;
        this.permute(pivot, fin);
        for(int i=debut; i<fin; i++){
            if (cout[this.candidats[i]]>=cout[this.candidats[fin]]){
                permute(i,j);
                j++;
            }
        }
        this.permute(j,fin);
        return j;
    }
    
    /**
     * Permute l'emplacement de deux valeurs dans le tableau candidats.
     * candidat[i]<=>candidat[j]
     * @param i - L'indice du premier élément à permuter
     * @param j - L'indice du second élément à permuter
     */
    private void permute(int i, int j){
        int dummy = this.candidats[i];
        this.candidats[i]=this.candidats[j];
        this.candidats[j]=dummy;
    }

    /**
     * @return - True si le nombre de candidats est positif 
     */
    @Override
    public boolean hasNext() {
        return nbCandidats > 0;
    }

    /**
     * @return - Le candidat suivant 
     */
    @Override
    public Integer next() {
        return candidats[--nbCandidats];
    }

    /**
     * Non implémentée
     */
    @Override
    public void remove() {}
}
