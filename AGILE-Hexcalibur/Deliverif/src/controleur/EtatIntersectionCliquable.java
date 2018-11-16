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
import modele.GestionLivraison;
import modele.Intersection;

/**Etat dans lequel se trouve l'application après que l'utilisateur ait demandé
 * l'ajout d'une livraison.
 *
 * @author Hex'calibur
 */
public class EtatIntersectionCliquable extends EtatDefaut{
    
    /**
     * Constructeur de EtatPlanCliquable
     */
    public EtatIntersectionCliquable() {
    }
    
    /**Permet de sélectionner une intersection en vue d'ajouter une livraison.
     * L'intersection est identifiée à partir de ses coordonnées par le modèle.
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude
     */
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        fenetre.estIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
        Controleur.ETAT_INTERSECTION_SELECTIONNEE.actionEntree(pointClique);
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_SELECTIONNEE);
    }
    
    /**Retour au menu
     * @param fenetre 
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(false, -1, -1);
    } 
}
