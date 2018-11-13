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
import modele.outils.PointPassage;
import modele.outils.Tournee;

/**
 * Cette commande permet de supprimer une livraison au sein d'une tournée.
 * En soi, elle est l'exact inverse de l'ajout d'une livraison.
 * @author Louis
 */
public class CdeSuppressionLivraison extends Commande{
    
    private PointPassage livraison;
    private int numeroTournee;
    private int positionTournee;
    
    /**
     * Construit une nouvelle commande de suppression de livraison.
     * @param gestion - La GestionLivraison
     * @param livraison - Le point de passage que l'on compte supprimer.
     */
    public CdeSuppressionLivraison(GestionLivraison gestion, PointPassage livraison){
        super(gestion);
        //Il faut déterminer le numéro et la position de tournée.
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
        if(this.etatCommande==EtatCommande.EXECUTEE){
            return;
        }
        this.gestion.supprimerLivraison(livraison);
        this.etatCommande=EtatCommande.EXECUTEE;
    }
    
    @Override
    public void undoCde(){
        if(this.etatCommande==EtatCommande.ANNULEE){
            return;
        }
        this.gestion.ajouterLivraison(livraison.getPosition(), livraison.getDuree(), numeroTournee, positionTournee-1);
        this.etatCommande=EtatCommande.ANNULEE;
    }
    
}
