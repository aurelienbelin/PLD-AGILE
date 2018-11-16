/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import controleur.commandes.ListeCommandes;
import deliverif.*;
import java.io.IOException;
import modele.GestionLivraison;
import org.xml.sax.SAXException;

/** Par ses actions, l'utilisateur fait passer l'application d'un état 
 * à un autre. Suivant l'état, la réponses aux demandes de l'utilisateur 
 * ne sera pas la même. Cette interface représente le comportement d'un état
 * face aux actions de l'utilisateur.
 *
 * @author Hex'Calibur
 */

interface Etat
{
    /** Methode appelee par controleur apres un clic sur le bouton de
      *  chargement d'un plan
      *  @param controleur
      *  @param gestionLivraison
      *  @param fichier
      *  @see GestionLivraison
     */
    public void boutonChargePlan (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) throws SAXException, IOException, Exception;
    
    /**  Methode appelee par controleur apres un clic sur le bouton de
      *  chargement d'une demande de livraison
      *  @param controleur
      *  @param gestionLivraison
      *  @param fichier
      *  @see GestionLivraison
     */
    public void boutonChargeLivraisons (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) throws SAXException, IOException, Exception;
    
    /**  Methode appelee par controleur apres un clic sur le bouton de
      *  calcul des tournées
      *  @param controleur
      *  @param gestionLivraison
      *  @param nbLivreurs
      *  @see GestionLivraison
     */
    public void boutonCalculerTournees(Controleur controleur, GestionLivraison gestionLivraison, int nbLivreurs, Deliverif fenetre);
    
    /**Methode appelee par controleur apres un clic sur le bouton "Stop"
     * @param controleur
     * @param gestionLivraison
     * @param fenetre 
     */
    public void boutonArreterCalcul(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre);
    
    /**Methode appelee par controleur apres un clic sur le bouton 
     * "Ajouter Livraison".
     * @param controleur
     * @param fenetre 
     */
    public void boutonAjouterLivraison(Controleur controleur, Deliverif fenetre);
    
    /**Methode appelee par controleur apres un clic sur le bouton 
     * "Supprimer Livraison".
     * @param controleur
     * @param fenetre 
     */
    public void boutonSupprimerLivraison (Controleur controleur, Deliverif fenetre);

    /**Methode appelee par controleur apres un clic sur le bouton 
     * "Réorganiser tournées"
     * @param controleur
     * @param fenetre 
     */
    public void boutonReorgTournees(Controleur controleur, Deliverif fenetre);
    
    /**Methode appelee par controleur apres un clic sur le bouton "Retour au
     * menu"
     * @param controleur
     * @param fenetre 
     */
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes);
    
    /**Methode appelee par controleur apres un clic sur le bouton "Retour
     * sélection"
     * @param controleur
     * @param fenetre 
     */
    public void boutonRetourSelection(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes);
    
    /**Methode appelee par controleur apres un bouton valider suppression
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param listeCde 
     */
    public void validerSuppression(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, ListeCommandes listeCde);
    
    /**Methode appelee par controleur apres un bouton valider selection
     * @param controleur
     * @param fenetre 
     */
    public void validerSelection(Controleur controleur, Deliverif fenetre);
    
    /**Methode appelee par controleur apres un bouton valider ajout
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param duree
     * @param listeCde 
     */
    public void validerAjout(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, float duree, ListeCommandes listeCde);

    /**Methode appelee par controleur apres un bouton valider reorganisation
     * @param fenetre
     * @param controleur 
     */
    public void validerReorganisation(Deliverif fenetre, Controleur controleur);
    
    /**Methode appelee par controleur apres un clic sur un des boutons "+"
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param indexPlus
     * @param indexTournee
     * @param duree
     * @param listeCde 
     */
    public void clicPlus(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde);
    
    /**Methode appelee par controleur apres un clic sur un des boutons fléchés
     * @param gestionLivraison
     * @param fenetre
     * @param haut
     * @param indexLivraison
     * @param indexTournee
     * @param commandes 
     */
    public void clicFleche(GestionLivraison gestionLivraison, Deliverif fenetre, boolean haut, int indexLivraison, int indexTournee, ListeCommandes commandes);
    
    /**Methode appelee par controleur apres un clic sur l'une des options du
     * menu contextuel
     * @param gestionLivraison
     * @param fenetre
     * @param indexTourneeChoisi
     * @param commandes 
     */
    public void selectionMenuLivreurs(GestionLivraison gestionLivraison, Deliverif fenetre, int indexTourneeChoisi, ListeCommandes commandes);
    
    /**Methode appelee par controleur apres un clic droit sur la vue textuelle 
     * @param livraisonCliquee 
     */
    public void clicDroit(DescriptifLivraison livraisonCliquee);
    
    /**Methode appelee par controleur apres un clic gauche
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude 
     */
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude);
    
     /**Methode appelee par controleur apres un clic sur un livraison de la vue
     * textuelle.
     * @param controleur
     * @param gestionLivraison
     * @param point
     * @param fenetre 
     */
    public void clicDescriptionLivraison(Controleur controleur, GestionLivraison gestionLivraison, DescriptifLivraison point, Deliverif fenetre);
    
    /**Methode appelee par cotnroleur après une demande de zoom avant
     * @param fenetre
     * @param lat
     * @param lon 
     */
    public void zoomPlus(Deliverif fenetre, double lat, double lon);
    
    /**Methode appelee par controleur apres une demande de zoom arrière
     * @param fenetre
     * @param lat
     * @param lon 
     */
    public void zoomMoins(Deliverif fenetre, double lat, double lon);

    /**Methode appelee par controleur après un appui sur la touche Z
     * @param listeCdes 
     */
    public void undo(ListeCommandes listeCdes);
    
    /**Methode appelee par controleur après un appui sur la touche Y
     * @param listeCdes 
     */
    public void redo(ListeCommandes listeCdes);
}
