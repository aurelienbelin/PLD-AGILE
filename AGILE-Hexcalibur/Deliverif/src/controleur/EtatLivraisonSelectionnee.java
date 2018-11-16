/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import controleur.commandes.CdeSuppressionLivraison;
import controleur.commandes.ListeCommandes;
import deliverif.Deliverif;
import deliverif.DescriptifLivraison;
import modele.GestionLivraison;
import modele.PointPassage;

/**Etat dans lequel se trouve l'application lorsqu'une livraison a été 
 * sélectionnée pour la suppression d'une livraison.
 *
 * @author Amine Nahid
 */
public class EtatLivraisonSelectionnee extends EtatDefaut {
    
    private PointPassage livraisonASupprimer;

    /**
     * Contructeur de EtatLivraisonSelectionnee
     */
    public EtatLivraisonSelectionnee() {
    }
    
    /**Methode appelee avant d'entrer dans l'etat this : garde la livraison 
     * qui a été sélectionnée en mémoire
     * @param intersectionASupprimer 
     */
    public void actionEntree(PointPassage intersectionASupprimer){
        this.livraisonASupprimer = intersectionASupprimer;
    }
    
    /**Cherche le point de passage le plus proche des coordonnées données en 
     * paramètre et l'identifie dans la vue graphique et la vue textuelle.
     * Ce point de passage devient la nouvelle livraison à supprimer
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude Latitude de la nouvelle sélection
     * @param longitude  Longitude de la nouvelle sélection
     */
    @Override
    public void clicGauche (Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        this.livraisonASupprimer = pointClique;
        fenetre.estPointPassageASupprimerSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
    }
    
    /**Retour au menu
     * @param fenetre 
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estSuppressionFinie();
    }
    
    /**Réalise la suppression de la livraison sélectionnée
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param listeCde 
     */
    @Override
    public void validerSuppression (Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, ListeCommandes listeCde) {
        listeCde.ajouterCde(new CdeSuppressionLivraison(gestionLivraison, livraisonASupprimer));
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estSuppressionFinie();
    }
    
    /**Sélectionne une livraison depuis la vue textuelle.
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
        this.livraisonASupprimer = intersection;
    }
    
    /**Pas d'undo dans cet état
     * @param listeCde 
     */
    @Override
    public void undo(ListeCommandes listeCde){
    }
    
    /**Pas de redo dans cet état
     * @param listeCde 
     */
    @Override
    public void redo(ListeCommandes listeCde){
    }
}
