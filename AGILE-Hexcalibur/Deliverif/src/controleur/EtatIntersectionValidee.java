/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import controleur.commandes.CdeAjoutLivraison;
import controleur.commandes.Commande;
import controleur.commandes.ListeCommandes;
import deliverif.Deliverif;
import modele.GestionLivraison;
import modele.Intersection;

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
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(true, -1,-1);
    }
    
    @Override
    public void boutonRetourSelection(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_SELECTIONNEE);
        fenetre.estRetourSelection();
    }
    
    @Override
    public void clicPlus(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde) {
        Controleur.ETAT_AJOUT_LIVRAISON.actionEntree(intersectionValidee, indexPlus, indexTournee);
        int indexLivraisonPreced = indexPlus/2;
        duree = duree * 60;
        Commande cde = new CdeAjoutLivraison(gestionLivraison, intersectionValidee, indexTournee, indexLivraisonPreced, duree);
        listeCde.ajouterCde(cde);
        fenetre.estPlusClique(indexPlus, indexTournee);
        controleur.setEtatCourant(Controleur.ETAT_AJOUT_LIVRAISON);
    }
}
