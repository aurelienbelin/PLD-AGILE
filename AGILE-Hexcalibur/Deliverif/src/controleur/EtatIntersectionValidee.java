/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import deliverif.Deliverif;
import modele.outils.GestionLivraison;
import modele.outils.Intersection;

/**
 *
 * @author Hex'calibur
 */
public class EtatIntersectionValidee extends EtatDefaut{
    
    private Intersection intersectionValidee;

    public EtatIntersectionValidee() {
    }
    
    public void actionEntree(Intersection intersectionSelectionnee) {
        intersectionValidee = intersectionSelectionnee;
    }
    
    @Override
    public void annuler(Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estAjoutLivraisonFini();
    }
    
    @Override
    public void retourSelection(Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_INTERSECTION_SELECTIONNEE;
        fenetre.estRetourSelection();
    }
    
    @Override
    public void clicPlus(Deliverif fenetre, int indexPlus, int indexTournee) {
        Controleur.ETAT_AJOUT_LIVRAISON.actionEntree(intersectionValidee, indexPlus, indexTournee);
        Controleur.etatCourant = Controleur.ETAT_AJOUT_LIVRAISON;
        fenetre.estPlusClique(indexPlus, indexTournee);
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
