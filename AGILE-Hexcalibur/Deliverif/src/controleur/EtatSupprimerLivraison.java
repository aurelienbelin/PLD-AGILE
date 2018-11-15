/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import deliverif.Deliverif;
import deliverif.DescriptifChemin;
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
        fenetre.estPointPassageASupprimerSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        Controleur.ETAT_LIVRAISON_SELECTIONNEE.actionEntree(pointClique);
        Controleur.etatCourant = Controleur.ETAT_LIVRAISON_SELECTIONNEE;
    }
    
    @Override
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estSuppressionFinie();
    }

    @Override
    public void trouverLocalisation(GestionLivraison gestionLivraison, DescriptifChemin point, Deliverif fenetre) {
        modele.outils.PointPassage intersection = gestionLivraison.identifierPointPassage(point.getPoint());
        fenetre.getVueGraphique().identifierPtPassageAModifier(!point.estLocalise(), intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        fenetre.getVueTextuelle().majVueTextuelle(point);
        fenetre.estPointPassageASupprimerSelectionne(intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        Controleur.ETAT_LIVRAISON_SELECTIONNEE.actionEntree(intersection);
        Controleur.etatCourant = Controleur.ETAT_LIVRAISON_SELECTIONNEE;
    }
    
    
}
