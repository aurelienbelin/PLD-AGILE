/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import java.io.IOException;
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
      *  @see modele.GestionLivraison
     */
    public void chargePlan (modele.outils.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) throws SAXException, IOException, Exception;
    
    /** 
      *  @param gestionLivraison
      *  @param fichier
      *  @see modele.GestionLivraison
     */
    public void chargeLivraisons (modele.outils.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) throws SAXException, IOException, Exception;
    
    /** 
      *  @param gestionLivraison
      *  @param nbLivreurs
      *  @see modele.GestionLivraison
     */
    public void calculerTournees(modele.outils.GestionLivraison gestionLivraison, int nbLivreurs, deliverif.Deliverif fenetre);
    
    //Test
    public void trouverLocalisation(modele.outils.GestionLivraison gestionLivraison, deliverif.DescriptifChemin point, deliverif.Deliverif fenetre);
    
    public void ajouterLivraison(deliverif.Deliverif fenetre);
    
    public void supprimerLivraison(deliverif.Deliverif fenetre);
    
    public void intersectionPlusProche(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude);
    
    public void annuler(deliverif.Deliverif fenetre);
    
    public void validerSelection(deliverif.Deliverif fenetre);
    
    public void retourSelection(deliverif.Deliverif fenetre);

    public void arreterCalcul(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre);

    public void selectionnerPoint(deliverif.Deliverif fenetre);

    public void zoomPlus(deliverif.Deliverif fenetre);
    
    public void zoomPlus(deliverif.Deliverif fenetre, double lat, double lon);
    public void zoomMoins(deliverif.Deliverif fenetre, double lat, double lon);


}
