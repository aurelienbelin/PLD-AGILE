/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import controleur.commandes.ListeCommandes;
import modele.GestionLivraison;
import modele.Intersection;

/**
 *
 * @author Hex'calibur
 */
public class EtatIntersectionSelectionnee extends EtatDefaut{
    
    private Intersection intersectionSelectionnee;

    public EtatIntersectionSelectionnee() {
    }
    
    public void actionEntree(Intersection intersectionCliquee){
        intersectionSelectionnee = intersectionCliquee;
    }
    
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude) {
        Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        intersectionSelectionnee = pointClique;
        fenetre.estIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
    }
    
    /**
     *
     * @param fenetre
     */
    @Override
    public void boutonAnnuler(Controleur controleur, deliverif.Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(false, -1,-1);
    }
    
    @Override
    public void validerSelection(Controleur controleur, deliverif.Deliverif fenetre){
        Controleur.ETAT_INTERSECTION_VALIDEE.actionEntree(intersectionSelectionnee);
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_VALIDEE);
        fenetre.estIntersectionValidee();
    }
}
