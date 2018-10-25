/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.math.BigDecimal;

/**
 * Un Point de Passage est un lieu important ou notre livreur doit
 * passer à un moment donné. Cette notion recouvre à la fois les points
 * de livraison ET les entrepots.
 * 
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class PointPassage {

    private boolean entrepot;
    private Intersection position;
    private float duree;

    /**
     * Crée un nouveau Point de Passage.
     * @param entrepot - Est ce que ce point de passage est un lieu de livraison
     * (false) ou un entrepot (true)?
     * @param position - L'intersection où se situe le point de passage.
     * @param duree - La durée que le livreur devra passer sur place.
     */
    public PointPassage(boolean entrepot, Intersection position, float duree) {
        this.entrepot = entrepot;
        this.position = position;
        this.duree = duree;
    }
    
    /**
     *
     * @return
     */
    public boolean isEntrepot() {
        return entrepot;
    }

    /**
     *
     * @return
     */
    public Intersection getPosition() {
        return position;
    }

    /**
     *
     * @return
     */
    public float getDuree() {
        return duree;
    }
    
    
}
