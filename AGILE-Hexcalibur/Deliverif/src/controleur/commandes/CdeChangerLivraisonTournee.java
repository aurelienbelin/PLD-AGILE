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
 * Cette commande permet de passer une livraison d'une tournée vers une autre.
 * L'insertion dans la nouvelle tournée se fait à un indice précisé.
 * @author Hex'calibur
 */
public class CdeChangerLivraisonTournee extends Commande{
    
    private int numeroTournee1;
    private int numeroTournee2;
    private int positionPoint1;
    private int positionPoint2;
    
    /**
     * Construit une nouvelle commande de transfert de PointPassage d'une tournée à une autre.
     * @param gestion - La GestionLivraison sur laquelle opérerer le changement.
     * @param n1 - Le numéro de la tournée dans laquelle prendre un point de passage
     * @param n2 - Le numéro de la tournée dans laquelle insérer le point de passage.
     * @param p1 - L'indice du point de passage dans la tournée originelle.
     * @param p2 - L'indice où insérer le point de passage dans la nouvelle tournée.
     */
    public CdeChangerLivraisonTournee(GestionLivraison gestion, int n1, int n2, int p1, int p2){
        super(gestion);
        this.numeroTournee1=n1;
        this.numeroTournee2=n2;
        this.positionPoint1=p1;
        this.positionPoint2=p2;
    }
    
    @Override
    protected void doCde(){
        this.gestion.intervertirPoint(this.numeroTournee1, this.numeroTournee2, this.positionPoint1, this.positionPoint2);
    }
    
    @Override
    protected void undoCde(){
        this.gestion.intervertirPoint(this.numeroTournee2, this.numeroTournee1, this.positionPoint2+1, this.positionPoint1-1);
    }
    
}
