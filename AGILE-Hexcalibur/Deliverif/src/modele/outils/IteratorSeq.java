/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Un IteratorSeq représente un itérateur permettant d'itérer sur l'ensemble des sommets non vus.
 * 
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class IteratorSeq implements Iterator<Integer> {

	private Integer[] candidats;
	private int nbCandidats;

	/**
	 * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
	 * @param nonVus
	 * @param sommetCrt
	 */
	public IteratorSeq(Collection<Integer> nonVus, int sommetCrt){
		this.candidats = new Integer[nonVus.size()];
		nbCandidats = 0;
		for (Integer s : nonVus){
			candidats[nbCandidats++] = s;
		}
	}
	
        /**
         * 
         * @return - True si le nombre de candidats est positif. 
         */
	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

        /**
         * 
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
