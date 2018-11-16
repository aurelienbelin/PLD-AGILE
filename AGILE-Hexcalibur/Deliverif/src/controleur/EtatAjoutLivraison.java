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


/** Etat dans lequel se trouve l'application pendant le calcul des tournées
 *  Permet l'affichage des solutions au fur et à mesure du calcul et l'arrêt du 
 *  calcul si la solution convient.
 *
 * @author Hex'Calibur
 */

public class EtatAjoutLivraison extends EtatDefaut{

    private Intersection intersectionValidee;
    private int indexPlus;
    private int indexTournee;


    public EtatAjoutLivraison() {
    }
    public void actionEntree(Intersection intersectionValidee, int indexPlus, int indexTournee){
        this.intersectionValidee = intersectionValidee;
        this.indexPlus = indexPlus;
        this.indexTournee = indexTournee;
    }
    
    @Override
    public void clicPlus(GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde) {
        listeCde.undo();
        if (this.indexPlus<indexPlus){
            indexPlus-=2;
        }
        this.indexPlus = indexPlus;
        this.indexTournee = indexTournee;
        int indexLivraisonPreced = indexPlus/2;
        duree = duree * 60;
        Commande cde = new CdeAjoutLivraison(gestionLivraison, intersectionValidee, indexTournee, indexLivraisonPreced, duree);
        listeCde.ajouterCde(cde);
        fenetre.changePlusClique(this.indexPlus, this.indexTournee, indexPlus, indexTournee);
    }
    
    @Override
    public void validerAjout(GestionLivraison gestionLivraison, Deliverif fenetre, float duree, ListeCommandes listeCde){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estAjoutLivraisonFini(true, indexTournee, indexPlus);
    }
    
     @Override
    public void retourSelection(Deliverif fenetre, ListeCommandes listeCdes){
        Controleur.etatCourant = Controleur.ETAT_INTERSECTION_SELECTIONNEE;
        fenetre.estRetourSelection();
        listeCdes.undo();
        fenetre.getVueTextuelle().majVueTextuelle(null);
    }
    
    @Override
    public void annuler(Deliverif fenetre, ListeCommandes listeCdes){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estAjoutLivraisonFini(true, indexTournee, indexPlus);
        listeCdes.undo();
    }
    
    @Override
    public void undo(ListeCommandes listeCde){
        //L'utilisateur ne peut pas faire d'undo à ce moment-là.
    }
    
    @Override
    public void redo(ListeCommandes listeCde){
        //L'utilisateur ne peut pas faire de redo à ce moment-là.
    }
}
