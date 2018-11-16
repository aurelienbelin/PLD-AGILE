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

/**
 *
 * @author Amine Nahid
 */
public class EtatLivraisonSelectionnee extends EtatDefaut {
    
    private PointPassage livraisonASupprimer;

    public EtatLivraisonSelectionnee() {
    }
    
    public void actionEntree(PointPassage intersectionASupprimer){
        this.livraisonASupprimer = intersectionASupprimer;
    }
    
    /**
     *
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude
     */
    @Override
    public void clicGauche (Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        this.livraisonASupprimer = pointClique;
        fenetre.estPointPassageASupprimerSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
    }
    
    @Override
    public void boutonAnnuler(Controleur controleur, deliverif.Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estSuppressionFinie();
    }
    
    @Override
    public void validerSuppression (Controleur controleur, GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, ListeCommandes listeCde) {
        listeCde.ajouterCde(new CdeSuppressionLivraison(gestionLivraison, livraisonASupprimer));
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estSuppressionFinie();
    }
    
    @Override
    public void clicDescriptionLivraison(Controleur controleur, GestionLivraison gestionLivraison, DescriptifLivraison point, Deliverif fenetre) {
        modele.PointPassage intersection = gestionLivraison.identifierPointPassage(point.getPoint());
        fenetre.getVueGraphique().identifierPtPassageAModifier(!point.estLocalise(), intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        fenetre.getVueTextuelle().majVueTextuelle(point);
        fenetre.estPointPassageASupprimerSelectionne(intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        this.livraisonASupprimer = intersection;
    }
    
    @Override
    public void undo(ListeCommandes listeCde){
        //L'utilisateur ne peut pas faire de undo à ce moment-là.
    }
    
    @Override
    public void redo(ListeCommandes listeCde){
        //L'utilisateur ne peut pas faire de redo à ce moment-là.
    }
}
