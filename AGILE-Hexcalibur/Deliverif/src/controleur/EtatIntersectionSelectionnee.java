/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

/**
 *
 * @author ahirusan
 */
public class EtatIntersectionSelectionnee extends EtatDefaut{

    public EtatIntersectionSelectionnee() {
    }
    
    public void intersectionPlusProche(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double  latitude, double longitude) {
        modele.outils.Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        fenetre.getVueGraphique().effacerMarker();
        fenetre.getVueGraphique().ajouterMarker(pointClique.getLatitude(), pointClique.getLongitude());
    }
    
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estTourneesCalculees("SUCCESS");
        fenetre.getVueGraphique().effacerMarker();
        fenetre.estAjoutLivraisonFini();
    }
}
