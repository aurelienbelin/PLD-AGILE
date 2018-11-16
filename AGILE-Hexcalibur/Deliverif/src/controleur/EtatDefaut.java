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
import modele.PointPassage;
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
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) throws SAXException, IOException, Exception{
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
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) throws SAXException, IOException, Exception{
    }
    
    /**
     * @param gestionLivraison
     * @param nbLivreurs
     * @param fenetre
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void calculerTournees(modele.GestionLivraison gestionLivraison, int nbLivreurs, deliverif.Deliverif fenetre){
    }
    
    //Test
    @Override
    public void trouverLocalisation(modele.GestionLivraison gestionLivraison, deliverif.DescriptifLivraison point, deliverif.Deliverif fenetre){
        modele.PointPassage intersection = gestionLivraison.identifierPointPassage(point.getPoint());
        fenetre.getVueGraphique().identifierPtPassage(!point.estLocalise(), intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        fenetre.getVueTextuelle().majVueTextuelle(point);
    }
    
    @Override
    public void ajouterLivraison(deliverif.Deliverif fenetre) {}
    
    @Override
    public void validerSuppression(GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, ListeCommandes listeCde) {}
    
    @Override
    public void clicGauche(GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        int[] positionDansTournee = gestionLivraison.ouEstLePoint(pointClique);
        fenetre.estSelectionne(positionDansTournee[0], positionDansTournee[1]);
    }
    
    @Override
    public void annuler(deliverif.Deliverif fenetre, ListeCommandes listeCdes){}
    
    @Override
    public void validerSelection(deliverif.Deliverif fenetre){}
    
    @Override
    public void retourSelection(deliverif.Deliverif fenetre, ListeCommandes listeCdes){}
    
    @Override
    public void clicPlus(GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde) {}
    
    public void validerAjout(GestionLivraison gestionLivraison, Deliverif fenetre, float duree, ListeCommandes listeCde){}
    
    @Override
    public void supprimerLivraison (Deliverif fenetre) {}

    @Override
    public void arreterCalcul(modele.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre) {}

    
    @Override
    public void selectionnerPoint(modele.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre, double latitude, double longitude){}
    
    @Override
    public void reorgTournees(deliverif.Deliverif fenetre){}
    
    @Override
    public void zoomPlus(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);      
    }
    @Override
    public void zoomMoins(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
    }
    
    @Override

    public void clicFleche(GestionLivraison gestionLivraison, Deliverif fenetre, boolean haut, int indexLivraison, int indexTournee, ListeCommandes commandes){}
    
    @Override
    public void clicDroit(DescriptifLivraison livraisonCliquee){}
    
    @Override
    public void changerLivraisonDeTournee(GestionLivraison gestionLivraison, Deliverif fenetre, int indexTourneeChoisi, ListeCommandes commandes){}
    
    @Override
    public void validerReorganisation(Deliverif fenetre){}

    public void undo(ListeCommandes listeCde){
        listeCde.undo();//Par défaut
    }
    
    @Override
    public void redo(ListeCommandes listeCde){
        listeCde.redo();//Par défaut
    }
}
