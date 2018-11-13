/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import deliverif.Deliverif;
import modele.outils.GestionLivraison;
import modele.outils.PointPassage;

/**
 *
 * @author Hex'Calibur
 */

public class EtatReorgTourneesDemandee extends EtatDefaut{

    public EtatReorgTourneesDemandee() {
    }
    
    public void clicFleche(GestionLivraison gestionLivraison, Deliverif fenetre, boolean haut, int indexLivraison, int indexTournee){
        if(haut){
            String point = indexLivraison+"_"+indexTournee;
            PointPassage pointPassage = gestionLivraison.identifierPointPassage(point);
            //méthode intervertir livraison
            
            if(indexLivraison==1){
                //fenetre.changerExtremites(haut);
            }
        }else{
            String point = indexLivraison+"_"+indexTournee;
            PointPassage pointPassage = gestionLivraison.identifierPointPassage(point);
            //méthode intervertir livraison
            
            if(indexLivraison == gestionLivraison.getTournees()[indexTournee].getLongueur()-1){
                //fenetre.changerExtremites(!haut);
            }
        }
    }
    
    public void changerLivraisonDeTournee(GestionLivraison gestionLivraison, Deliverif fenetre, int indexLivraison, int indexTournee, int indexTourneeChoisi){
        
    }
    
    public void validerReorganisation(Deliverif fenetre){
        fenetre.estReorgFinie();
    }
    
    public void clicGauche(GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude){
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
    }
    
    public void annuler(Deliverif fenetre){
        fenetre.estReorgFini();
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
    }
    
    @Override
    public void zoomPlus(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees();
        fenetre.getVueGraphique().dessinerMarker();
    }
    
    @Override
    public void zoomMoins(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees();
        fenetre.getVueGraphique().dessinerMarker();
    }
}
