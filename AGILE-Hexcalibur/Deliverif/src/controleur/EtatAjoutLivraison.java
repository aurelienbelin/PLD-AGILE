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


/** Etat dans lequel se trouve l'application apres que l'utilisateur ait choisi 
 * une tournée et un emplacement dans la tournée pour la nouvelle livraison
 * pour la première fois
 *
 * @author Hex'Calibur
 */

public class EtatAjoutLivraison extends EtatDefaut{

    private Intersection intersectionValidee;
    private int indexPlus;
    private int indexTournee;


    public EtatAjoutLivraison() {
    }
    
    /**Methode appelee avant d'entrer dans l'etat this : garde l'intersection 
     * qui avait été sélectionnée et la tournée et l'endroit dans la tournée où
     * a été attribuée la livraison en mémoire
     * @param intersectionValidee Emplacement de la nouvelle livraison sur le plan
     * @param indexPlus Indice de la place dans la tournée choisie, en comptant les boutons + entre les livraisons
     * @param indexTournee Indice de la tournée choisie
     */
    public void actionEntree(Intersection intersectionValidee, int indexPlus, int indexTournee){
        this.intersectionValidee = intersectionValidee;
        this.indexPlus = indexPlus;
        this.indexTournee = indexTournee;
    }
    
    /**La nouvelle livraison est ajoutée temporairement. Comme il s'agit dans 
     * cet état d'une resélection, il faut supprimer la livraison qui avait été 
     * ajoutée lors du clic précédent sur un "+"
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param indexPlus Indice de la place dans la tournée choisie, en comptant les boutons + entre les livraisons et la livraison ajoutée lors du clicPlus précédent
     * @param indexTournee Indice de la tournée choisie
     * @param duree
     * @param listeCde 
     */
    @Override
    public void clicPlus(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde) {
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
    
    /**Retour dans l'état TourneesCalculees et gestion de l'affichage
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param duree
     * @param listeCde 
     */
    @Override
    public void validerAjout(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, float duree, ListeCommandes listeCde){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(true, indexTournee, indexPlus);
    }
    
    /**Retour dans l'état IntersectionSelectionnee et gestion de l'affichage.
     * Attention, on annule l'ajout qui avait été fait lors du clic sur le +
     * @param controleur
     * @param fenetre 
     */
    @Override
    public void boutonRetourSelection(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_SELECTIONNEE);
        fenetre.estRetourSelection();
        listeCdes.undo();
        fenetre.getVueTextuelle().majVueTextuelle(null);
    }
    
    /**Retour au menu et annulation de l'ajout de la livraison
     * @param controleur
     * @param fenetre
     * @param listeCdes 
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(true, indexTournee, indexPlus);
        listeCdes.undo();
    }
    
    /**Pas de undo dans cet état
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
