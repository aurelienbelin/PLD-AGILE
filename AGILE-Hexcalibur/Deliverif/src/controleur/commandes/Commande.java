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
 *
 * @author Louis
 */
public abstract class Commande {
    
    protected GestionLivraison gestion;
    //Décrit l'état dans lequel se trouve la commande.
    protected EtatCommande etatCommande;
    
    /**
    * Construit une nouvelle Commande
    * @param gestion - La GestionLivraison sur laquelle la commande travaille.
    */
    public Commande(GestionLivraison gestion){
        this.gestion=gestion;
        this.etatCommande=EtatCommande.INACTIVE;
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
