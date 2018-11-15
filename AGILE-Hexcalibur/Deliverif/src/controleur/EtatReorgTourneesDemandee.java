/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import controleur.commandes.CdeChangerLivraisonTournee;
import controleur.commandes.CdeDeplacerLivraison;
import controleur.commandes.ListeCommandes;
import deliverif.Deliverif;
import deliverif.DescriptifChemin;
import modele.outils.GestionLivraison;
import modele.outils.PointPassage;

/**
 *
 * @author Hex'Calibur
 */

public class EtatReorgTourneesDemandee extends EtatDefaut{
    
    protected int tourneeLivraisonAChanger;
    protected int indiceLivraisonAChanger;

    public EtatReorgTourneesDemandee() {
    }
    
    /**
     *
     * @param gestionLivraison
     * @param fenetre
     * @param haut
     * @param indexLivraison
     * @param indexTournee
     * @param commandes
     */
    @Override
    public void clicFleche(GestionLivraison gestionLivraison, Deliverif fenetre, boolean haut, int indexLivraison, int indexTournee, ListeCommandes commandes){
        String point = (indexTournee+1)+"_"+(indexLivraison+1); //DESCRIPTIF
        PointPassage pointPassage = gestionLivraison.identifierPointPassage(point);
        if(haut){
            commandes.ajouterCde(new CdeDeplacerLivraison(gestionLivraison, pointPassage, indexTournee, indexLivraison-1));
          
        }else{
            commandes.ajouterCde(new CdeDeplacerLivraison(gestionLivraison, pointPassage, indexTournee, indexLivraison+1));
            
        }
        fenetre.changerVueTextuelle(indexTournee);
    }
    
    @Override
    public void clicDroit(DescriptifChemin livraisonCliquee){
        String[] identifiants = livraisonCliquee.getPoint().split("_");
        tourneeLivraisonAChanger = Integer.parseInt(identifiants[0])-1; //DESCRIPTIF
        indiceLivraisonAChanger = Integer.parseInt(identifiants[1])-1; //DESCRIPTIF
        
    }
    
    @Override
    public void changerLivraisonDeTournee(GestionLivraison gestionLivraison, Deliverif fenetre, int indiceTourneeChoisi, ListeCommandes commandes){
        int nouvelIndice = gestionLivraison.getTournees()[indiceTourneeChoisi].getTrajet().size()-1;
        commandes.ajouterCde(new CdeChangerLivraisonTournee(gestionLivraison, tourneeLivraisonAChanger, indiceTourneeChoisi, indiceLivraisonAChanger, nouvelIndice));
        fenetre.changerVueTextuelle(indiceTourneeChoisi);
    }
    
    /**
     *
     * @param fenetre
     */
    @Override
    public void validerReorganisation(Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
        fenetre.estReorgFinie();
    }
    
    @Override
    public void clicGauche(GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        int[] positionDansTournee = gestionLivraison.ouEstLePoint(pointClique);
        fenetre.estSelectionne(positionDansTournee[0], positionDansTournee[1]);
    }
    
    /**
     *
     * @param fenetre
     */
    @Override
    public void annuler(Deliverif fenetre){
        fenetre.estReorgFinie();
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
