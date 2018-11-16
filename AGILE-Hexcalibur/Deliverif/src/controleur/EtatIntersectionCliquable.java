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

/**
 *
 * @author Hex'calibur
 */
public class EtatIntersectionCliquable extends EtatDefaut{
    
    /**
     * Constructeur de EtatPlanCliquable
     */
    public EtatIntersectionCliquable() {
    }
    
    /**
     * 
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude
     */
    @Override
    public void clicGauche(Controleur controleur, modele.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude) {
        modele.Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        fenetre.estIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
        Controleur.ETAT_INTERSECTION_SELECTIONNEE.actionEntree(pointClique);
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_SELECTIONNEE);
    }
    
    @Override
    public void boutonAnnuler(Controleur controleur, deliverif.Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(false, -1, -1);
    } 
}
