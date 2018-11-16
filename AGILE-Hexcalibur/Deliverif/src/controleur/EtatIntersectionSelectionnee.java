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

/**Etat dans lequel se trouve l'application lorsqu'une intersection a été 
 * sélectionnée pour l'ajout d'une livraison mais pas encore validée.
 *
 * @author Hex'calibur
 */
public class EtatIntersectionSelectionnee extends EtatDefaut{
    
    private Intersection intersectionSelectionnee;

    /**
     * Constructeur de EtatIntersectionSelectionnee
     */
    public EtatIntersectionSelectionnee() {
    }
    
    /**Methode appelee avant d'entrer dans l'etat this : garde l'intersection 
     * qui avait été sélectionnée en mémoire
     * @param intersectionCliquee 
     */
    public void actionEntree(Intersection intersectionCliquee){
        intersectionSelectionnee = intersectionCliquee;
    }
    
    /**Change l'intersection sélectionnée en vue d'un ajout de livraison
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude Latitude de la nouvelle sélection
     * @param longitude Longitude de la nouvelle sélection
     */
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        intersectionSelectionnee = pointClique;
        fenetre.estIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
    }
    
    /**Retour au menu
     * @param controleur
     * @param fenetre
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(false, -1,-1);
    }
    
    /**Fait passer dans l'état IntersectionValidee
     * @param controleur
     * @param fenetre
     */
    @Override
    public void validerSelection(Controleur controleur, Deliverif fenetre){
        Controleur.ETAT_INTERSECTION_VALIDEE.actionEntree(intersectionSelectionnee);
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_VALIDEE);
        fenetre.estIntersectionValidee();
    }
}
