/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

/**
 *
 * @author ahirusan
 */
public class EtatPlanCliquable extends EtatDefaut{
    
    /**
     * Constructeur de EtatPlanCliquable
     */
    public EtatPlanCliquable() {
    }
    
    /**
     * 
     */
    public void intersectionPlusProche(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude) {
        modele.outils.Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        fenetre.getVueGraphique().ajouterMarker(pointClique.getLatitude(), pointClique.getLongitude());
        Controleur.etatCourant = Controleur.ETAT_INTERSECTION_SELECTIONNEE;
    }
    
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estTourneesCalculees("SUCCESS");
        fenetre.estAjoutLivraisonFini();
    }
    
}
