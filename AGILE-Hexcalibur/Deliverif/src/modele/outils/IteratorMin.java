/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author lohl
 */
public class IteratorMin implements Iterator<Integer>{
    
    private Integer[] candidats;
    private int nbCandidats;

    /**
    * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
    * @param nonVus - L'ensemble des sommets non vus.
    * @param sommetCrt - Le sommet sur lequel nous sommes couramment.
    * @param cout - Le tableau des cout t.q. cout[i][j]= le cout pour aller du
    * sommet i au sommet j.
    */
    public IteratorMin(Collection<Integer> nonVus, int sommetCrt, int[][] cout){
        this.candidats = new Integer[nonVus.size()];
        nbCandidats = 0;
        for (Integer s : nonVus){
            candidats[nbCandidats++] = s;
        }
        this.quicksort(0, this.candidats.length-1, cout[sommetCrt]);
    }
    
    private void quicksort(int debut, int fin, int[] cout){
        if (debut<fin){
            int pivot = (fin-debut)/2+debut;
            pivot=this.arrange(debut, fin, pivot, cout);
            quicksort(debut, pivot-1, cout);
            quicksort(pivot+1, fin, cout);
        }
    }
    
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

    @Override
    public boolean hasNext() {
        return nbCandidats > 0;
    }

    @Override
    public Integer next() {
        return candidats[--nbCandidats];
    }

    @Override
    public void remove() {}
}
