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
import deliverif.Deliverif;
import deliverif.DescriptifLivraison;
import java.io.IOException;
import modele.GestionLivraison;
import org.xml.sax.SAXException;

/** Par ses actions, l'utilisateur fait passer l'application d'une situation 
 * à une autre. Suivant la situation, la réponses aux demandes de l'utilisateur 
 * ne sera pas la même. Cette interface représente le comportement d'une 
 * situation face aux actions de l'utilisateur.
 *
 * @author Hex'Calibur
 */

interface Etat
{
     /** 
      *  @param gestionLivraison
      *  @param fichier
      *  @see GestionLivraison
     */
    public void boutonChargePlan (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) throws SAXException, IOException, Exception;
    
    /** 
      *  @param gestionLivraison
      *  @param fichier
      *  @see GestionLivraison
     */
    public void boutonChargeLivraisons (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) throws SAXException, IOException, Exception;
    
    /** 
      *  @param gestionLivraison
      *  @param nbLivreurs
      *  @see GestionLivraison
     */
    public void boutonCalculerTournees(Controleur controleur, GestionLivraison gestionLivraison, int nbLivreurs, Deliverif fenetre);
    
    public void boutonArreterCalcul(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre);
    
    //Test
    public void clicDescriptionLivraison(Controleur controleur, GestionLivraison gestionLivraison, DescriptifLivraison point, Deliverif fenetre);
    
    public void boutonAjouterLivraison(Controleur controleur, Deliverif fenetre);
    
    public void validerSuppression(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, ListeCommandes listeCde);
    
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude);
    
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes);
    
    public void validerSelection(Controleur controleur, Deliverif fenetre);
    
    public void boutonRetourSelection(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes);
    
    public void clicPlus(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde);
    
    public void validerAjout(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, float duree, ListeCommandes listeCde);

    public void boutonSupprimerLivraison (Controleur controleur, Deliverif fenetre);

    
    public void boutonReorgTournees(Controleur controleur, Deliverif fenetre);
    
    public void clicFleche(GestionLivraison gestionLivraison, Deliverif fenetre, boolean haut, int indexLivraison, int indexTournee, ListeCommandes commandes);
    
    public void clicDroit(DescriptifLivraison livraisonCliquee);
    
    public void selectionMenuLivreurs(GestionLivraison gestionLivraison, Deliverif fenetre, int indexTourneeChoisi, ListeCommandes commandes);
    
    public void validerReorganisation(Deliverif fenetre, Controleur controleur);
    
    public void zoomPlus(Deliverif fenetre, double lat, double lon);
    
    public void zoomMoins(Deliverif fenetre, double lat, double lon);

    public void undo(ListeCommandes listeCdes);
    public void redo(ListeCommandes listeCdes);
}
