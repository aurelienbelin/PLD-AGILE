/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import modele.outils.GestionLivraison;
import modele.outils.Intersection;

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
    public void clicGauche(GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double  latitude, double longitude) {
        Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        intersectionSelectionnee = pointClique;
        fenetre.changerIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
    }
    
    /**
     *
     * @param fenetre
     */
    @Override
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estAjoutLivraisonFini();
    }
    
    @Override
    public void validerSelection(deliverif.Deliverif fenetre){
        Controleur.ETAT_INTERSECTION_VALIDEE.actionEntree(intersectionSelectionnee);
        Controleur.etatCourant = Controleur.ETAT_INTERSECTION_VALIDEE;
        fenetre.estIntersectionValidee();
    }
    
    @Override
    public void zoomPlus(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        fenetre.getVueGraphique().dessinerMarker();
    }
    
    @Override
    public void zoomMoins(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        fenetre.getVueGraphique().dessinerMarker();
    }
}
