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
import deliverif.Deliverif;
import deliverif.DescriptifLivraison;
import modele.GestionLivraison;
import modele.Intersection;
import modele.PointPassage;

/**Etat dans lequel se trouve l'application après qu'une suppression de livraison
 * est été demandée.
 * 
 * @author Amine Nahid
 */
public class EtatSupprimerLivraison extends EtatDefaut{
    
    /**
     * Contructeur EtatSupprimerLivraison
     */
    public EtatSupprimerLivraison() {
    }
    
    /**Sélectionne une livraison sur la vue graphique en vue d'une suppression
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude
     */
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageASupprimerSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        Controleur.ETAT_LIVRAISON_SELECTIONNEE.actionEntree(pointClique);
        controleur.setEtatCourant(Controleur.ETAT_LIVRAISON_SELECTIONNEE);
    }
    
    /**Retour au menu
     * @param controleur
     * @param fenetre 
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estSuppressionFinie();
    }

    /**Selectionne une livraison dans la vue textuelle en vue d'une suppression
     * @param controleur
     * @param gestionLivraison
     * @param point
     * @param fenetre 
     */
    @Override
    public void clicDescriptionLivraison(Controleur controleur, GestionLivraison gestionLivraison, DescriptifLivraison point, Deliverif fenetre) {
        PointPassage intersection = gestionLivraison.identifierPointPassage(point.getPoint());
        fenetre.getVueGraphique().identifierPtPassageAModifier(!point.estLocalise(), intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        fenetre.getVueTextuelle().majVueTextuelle(point);
        fenetre.estPointPassageASupprimerSelectionne(intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        Controleur.ETAT_LIVRAISON_SELECTIONNEE.actionEntree(intersection);
        controleur.setEtatCourant(Controleur.ETAT_LIVRAISON_SELECTIONNEE);
    }
}
