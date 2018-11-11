/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur.commandes;

import modele.outils.GestionLivraison;

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
    public abstract void doCde();
    
    /**
     * Annulation de la commande.
     */
    public abstract void undoCde();
    
}
