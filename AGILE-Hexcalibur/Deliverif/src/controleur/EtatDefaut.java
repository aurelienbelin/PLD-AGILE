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

/** Classe mère de tous les états.
 * Améliore la visibilité du code, dans les classes filles on implémente 
 * seulement les méthodes ayant un traitement particulier. 
 *
 * @author Hex'calibur
 */

public class EtatDefaut implements Etat
{

    /**
     * Constructeur EtatDefaut
     */
    public EtatDefaut (){}
    
    /**
     * @param gestionLivraison
     * @param fichier
     * @param fenetre
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void chargePlan (modele.outils.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) throws SAXException, IOException, Exception{
    }
    
    /**
     * @param gestionLivraison
     * @param fichier
     * @param fenetre
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void chargeLivraisons (modele.outils.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) throws SAXException, IOException, Exception{
    }
    
    /**
     * @param gestionLivraison
     * @param nbLivreurs
     * @param fenetre
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void calculerTournees(modele.outils.GestionLivraison gestionLivraison, int nbLivreurs, deliverif.Deliverif fenetre){
    }
    
    //Test
    @Override
    public void trouverLocalisation(modele.outils.GestionLivraison gestionLivraison, deliverif.DescriptifChemin point, deliverif.Deliverif fenetre){
        modele.outils.Intersection intersection = gestionLivraison.identifierPointPassage(point.getPoint());
        
        fenetre.getVueGraphique().identifierPtPassage(!point.estLocalise(), intersection.getLatitude(), intersection.getLongitude());
        fenetre.getVueTextuelle().majVueTextuelle(point);
    }
    
    @Override
    public void ajouterLivraison(deliverif.Deliverif fenetre) {}
    
    @Override
    public void supprimerLivraison(deliverif.Deliverif fenetre) {}
    
    @Override
    public void intersectionPlusProche(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude) {}
    
    @Override
    public void annuler(deliverif.Deliverif fenetre){}
    
    @Override
    public void validerSelection(deliverif.Deliverif fenetre){}
    
    @Override
    public void retourSelection(deliverif.Deliverif fenetre){}
   
    @Override
    public void arreterCalcul(modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre) {}

    
    @Override
    public void selectionnerPoint(deliverif.Deliverif fenetre){}

}
