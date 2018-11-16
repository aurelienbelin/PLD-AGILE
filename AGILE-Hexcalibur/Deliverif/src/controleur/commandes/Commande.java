/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur.commandes;

import modele.GestionLivraison;

/**
 * Définit le comportement par défaut d'une Commande.
 * Une commande doit être capable d'exécuter une action et son inverse.
 * @author Hex'calibur
 */
public abstract class Commande {
    
    //Le point d'entrée du modèle.
    protected GestionLivraison gestion;
    
    /**
    * Construit une nouvelle Commande
    * @param gestion - La GestionLivraison sur laquelle la commande travaille.
    */
    public Commande(GestionLivraison gestion){
        this.gestion=gestion;
    }
    
    /**
     * Exécution de la commande.
     */
    protected abstract void doCde();
    
    /**
     * Annulation de la commande.
     */
    protected abstract void undoCde();
    
}
