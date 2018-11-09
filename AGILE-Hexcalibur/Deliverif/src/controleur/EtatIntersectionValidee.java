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
public class EtatIntersectionValidee extends EtatDefaut{

    public EtatIntersectionValidee() {
    }
    
    @Override
    public void annuler(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estTourneesCalculees("SUCCESS");
        fenetre.getVueGraphique().effacerMarker();
        fenetre.estAjoutLivraisonFini();
    }
    
    public void retourSelection(deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_INTERSECTION_SELECTIONNEE;
        fenetre.estIntersectionSelectionnee();
    }
    
}
