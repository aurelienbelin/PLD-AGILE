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

/**Etat dans lequel se trouve l'application lorsqu'une intersection a été 
 * sélectionnée et validée pour l'ajout de livraison. 
 * Point d'entrée de la phase d'attribution de la livraison à un livreur.
 *
 * @author Hex'calibur
 */
public class EtatIntersectionValidee extends EtatDefaut{
    
    private Intersection intersectionValidee;

    /**
     * Constructeur de EtatIntersectionValidee
     */
    public EtatIntersectionValidee() {
    }
    
    /**Methode appelee avant d'entrer dans l'etat this : garde l'intersection 
     * qui a été validée en mémoire
     * @param intersectionSelectionnee 
     */
    public void actionEntree(Intersection intersectionSelectionnee) {
        intersectionValidee = intersectionSelectionnee;
    }
    
    /**Retour au menu
     * @param fenetre 
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estAjoutLivraisonFini(true, -1,-1);
    }
    
    /**Retour à la sélection de l'intersection
     * @param fenetre 
     */
    @Override
    public void boutonRetourSelection(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        controleur.setEtatCourant(Controleur.ETAT_INTERSECTION_SELECTIONNEE);
        fenetre.estRetourSelection();
    }
    
    /**La nouevelle livraison est temporairement ajoutée. Tant que la validation 
     * n'aura pas été faite, l'ajout n'est pas définitif. Attention, les indices 
     * donnés à l'état suivant correspondent à la place de la livraison 
     * précédant le bouton + cliqué en comptant les boutons + entre les livraison
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param indexPlus Indice de la place dans la tournée choisie 
     * @param indexTournee Indice de la tournée choisie
     * @param duree
     * @param listeCde 
     */
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
