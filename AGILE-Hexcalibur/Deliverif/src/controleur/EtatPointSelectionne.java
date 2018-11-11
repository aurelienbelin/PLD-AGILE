/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import modele.outils.GestionLivraison;
import modele.outils.PointPassage;

/**
 *
 * @author Amine Nahid
 */
public class EtatPointSelectionne extends EtatDefaut{
    
    private PointPassage livraisonASupprimer;

    public EtatPointSelectionne() {
    }
    public void actionEntree(PointPassage intersectionASupprimer, int indexPlus, int indexTournee){
        this.livraisonASupprimer = intersectionASupprimer;
    }
    
    @Override
    public void selectionnerPoint (modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude) {
        modele.outils.Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        fenetre.getVueGraphique().effacerMarker();
        fenetre.getVueGraphique().ajouterMarker(pointClique.getLatitude(), pointClique.getLongitude());
        fenetre.estIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
        Controleur.etatCourant = Controleur.ETAT_POINT_SELECTIONNE;
    }
    
    @Override
    public void supprimerLivraison (GestionLivraison gestionLivraison, deliverif.Deliverif fenetre) {
        gestionLivraison.supprimerLivraison(livraisonASupprimer);
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
    }
    
    @Override
    public void ajouterLivraison (deliverif.Deliverif fenetre) {
        Controleur.etatCourant = Controleur.ETAT_PLAN_CLIQUABLE;
        fenetre.estPlanCliquable();
    }
}
