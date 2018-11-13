/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import deliverif.Deliverif;
import modele.outils.GestionLivraison;
import modele.outils.Intersection;
import modele.outils.PointPassage;

/**
 *
 * @author Amine Nahid
 */
public class EtatSupprimerLivraison extends EtatDefaut{
    

    public EtatSupprimerLivraison() {
    }
    
    /**
     *
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude
     */
    @Override
    public void clicGauche(GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        Controleur.ETAT_LIVRAISON_SELECTIONNEE.actionEntree(pointClique);
        Controleur.etatCourant = Controleur.ETAT_LIVRAISON_SELECTIONNEE;
    }
    
    @Override
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
    }
    
    @Override
    public void ajouterLivraison (deliverif.Deliverif fenetre) {
        Controleur.etatCourant = Controleur.ETAT_PLAN_CLIQUABLE;
        fenetre.estPlanCliquable();
    }
}
