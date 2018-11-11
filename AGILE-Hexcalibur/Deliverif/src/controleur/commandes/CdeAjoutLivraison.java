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
import modele.outils.Intersection;
import modele.outils.PointPassage;

/**
 * Commande permettant d'ajouter une livraison à une tournée dans la GestionLivraison.
 * @author Louis
 */
public class CdeAjoutLivraison extends Commande{
    
    private int numeroTournee;
    private int positionTournee;
    private PointPassage livraison;
    
    /**
     * Crée une nouvelle commande d'ajout de livraison dans la GestionLivraison.
     * @param gestion - La GestionLivraison
     * @param intersection - L'intersection où l'on doit réaliser une livraison
     * @param numeroTournee - Le numéro de la tournée à laquelle ce point de passage doit être ajouté
     * @param positionTournee - L'indice auquel doit être ajouté ce point de passage. /!\  : positionTournee!= indiceEntrepot (0 et size()-1)
     * @param duree - La durée que le livreur devra passer sur ce point de livraison.
     */
    public CdeAjoutLivraison(GestionLivraison gestion, Intersection intersection,
            int numeroTournee, int positionTournee, float duree){
        super(gestion);
        this.livraison = new PointPassage(false, intersection, duree);
        this.numeroTournee=numeroTournee;
        this.positionTournee=positionTournee;
    }
    
    @Override
    public void doCde(){
        if(this.etatCommande==EtatCommande.EXECUTEE){
            return;
        }
        gestion.ajouterLivraison(this.livraison.getPosition(), this.livraison.getDuree(), numeroTournee, positionTournee);
        this.etatCommande=EtatCommande.EXECUTEE;
    }
    
    @Override
    public void undoCde(){
        if(this.etatCommande==EtatCommande.ANNULEE){
            return;
        }
        gestion.supprimerLivraison(livraison);
        this.etatCommande=EtatCommande.ANNULEE;
    }
    
}
