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
     * @see GestionLivraison
     * @version 1
     */
    @Override
    public void boutonChargePlan (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) throws SAXException, IOException, Exception{
    }
    
    /**
     * @param gestionLivraison
     * @param fichier
     * @param fenetre
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @see GestionLivraison
     * @version 1
     */
    @Override
    public void boutonChargeLivraisons (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) throws SAXException, IOException, Exception{
    }
    
    /**
     * @param gestionLivraison
     * @param nbLivreurs
     * @param fenetre
     * @see GestionLivraison
     * @version 1
     */
    @Override
    public void boutonCalculerTournees(Controleur controleur, GestionLivraison gestionLivraison, int nbLivreurs, Deliverif fenetre){
    }
    
    //Test
    @Override
    public void clicDescriptionLivraison(Controleur controleur, GestionLivraison gestionLivraison, DescriptifLivraison point, Deliverif fenetre){
        PointPassage intersection = gestionLivraison.identifierPointPassage(point.getPoint());
        fenetre.getVueGraphique().identifierPtPassage(!point.estLocalise(), intersection.getPosition().getLatitude(), intersection.getPosition().getLongitude());
        fenetre.getVueTextuelle().majVueTextuelle(point);
    }
    
    @Override
    public void boutonAjouterLivraison(Controleur controleur, Deliverif fenetre) {}
    
    @Override
    public void validerSuppression(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, ListeCommandes listeCde) {}
    
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        int[] positionDansTournee = gestionLivraison.ouEstLePoint(pointClique);
        fenetre.estSelectionne(positionDansTournee[0], positionDansTournee[1]);
    }
    
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){}
    
    @Override
    public void validerSelection(Controleur controleur, Deliverif fenetre){}
    
    @Override
    public void boutonRetourSelection(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){}
    
    @Override
    public void clicPlus(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, int indexPlus, int indexTournee, int duree, ListeCommandes listeCde) {}
    
    public void validerAjout(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, float duree, ListeCommandes listeCde){}
    
    @Override
    public void boutonSupprimerLivraison (Controleur controleur, Deliverif fenetre) {}

    @Override
    public void boutonArreterCalcul(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre) {}

    @Override
    public void boutonReorgTournees(Controleur controleur, Deliverif fenetre){}
    
    @Override
    public void zoomPlus(Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);      
    }
    @Override
    public void zoomMoins(Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
    }
    
    @Override

    public void clicFleche(GestionLivraison gestionLivraison, Deliverif fenetre, boolean haut, int indexLivraison, int indexTournee, ListeCommandes commandes){}
    
    @Override
    public void clicDroit(DescriptifLivraison livraisonCliquee){}
    
    @Override
    public void selectionMenuLivreurs(GestionLivraison gestionLivraison, Deliverif fenetre, int indexTourneeChoisi, ListeCommandes commandes){}
    
    @Override
    public void validerReorganisation(Deliverif fenetre, Controleur controleur){}

    @Override
    public void undo(ListeCommandes listeCde){
        listeCde.undo();//Par défaut
    }
    
    @Override
    public void redo(ListeCommandes listeCde){
        listeCde.redo();//Par défaut
    }
}
