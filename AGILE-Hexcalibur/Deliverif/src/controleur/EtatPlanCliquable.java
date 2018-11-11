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
 * @author Hex'calibur
 */
public class EtatPlanCliquable extends EtatDefaut{
    
    /**
     * Constructeur de EtatPlanCliquable
     */
    public EtatPlanCliquable() {
    }
    
    /**
     * 
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude
     */
    @Override
    public void intersectionPlusProche(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude) {
        modele.outils.Intersection pointClique = gestionLivraison.intersectionPlusProche(latitude, longitude);
        fenetre.estIntersectionSelectionnee(pointClique.getLatitude(), pointClique.getLongitude());
        Controleur.ETAT_INTERSECTION_SELECTIONNEE.actionEntree(pointClique);
        Controleur.etatCourant = Controleur.ETAT_INTERSECTION_SELECTIONNEE;
    }
    
    @Override
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estAjoutLivraisonFini();
    }
    
    @Override
    public void zoomPlus(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees();
        fenetre.getVueGraphique().dessinerMarker();
    }
    @Override
    public void zoomMoins(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees();
        fenetre.getVueGraphique().dessinerMarker();
    }
    
}
