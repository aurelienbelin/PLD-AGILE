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
import modele.PointPassage;
import modele.Tournee;

/**
 * Cette commande permet de supprimer une livraison au sein d'une tournée.
 * En soi, elle est l'exact inverse de l'ajout d'une livraison.
 * @author Hex'calibur
 */
public class CdeSuppressionLivraison extends Commande{
    
    //La livraison à supprimer
    private PointPassage livraison;
    //Le numéro de la tournée dans laquelle se situe cette livraison
    private int numeroTournee;
    //L'indice auquel se trouve la livraison dans la tournée.
    private int positionTournee;
    
    /**
     * Construit une nouvelle commande de suppression de livraison.
     * @param gestion - La GestionLivraison
     * @param livraison - Le point de passage que l'on compte supprimer.
     */
    public CdeSuppressionLivraison(GestionLivraison gestion, PointPassage livraison){
        super(gestion);
        //Il faut déterminer le numéro et la position de tournée avant de supprimer.
        this.livraison=livraison;
        this.numeroTournee=-1;
        for(int i=0; i<this.gestion.getTournees().length; i++){
            int position=this.gestion.positionPointDansTournee(i,livraison);
            if (position!=-1){
                this.numeroTournee=i;
                this.positionTournee=position;
            }
        }
    }
    
    @Override
    public void doCde(){
        this.gestion.supprimerLivraison(livraison);
    }
    
    @Override
    public void undoCde(){
        //positionTournee-1 car il faut indiquer l'indice apres lequel la livraison
        //va s'insérer.
        this.gestion.ajouterLivraison(livraison, numeroTournee, positionTournee-1);
    }
    
}
