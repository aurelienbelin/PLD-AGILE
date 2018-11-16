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
import deliverif.DescriptifLivraison;
import modele.GestionLivraison;
import modele.PointPassage;

/**Etat dans lequel se trouve l'application après que l'utilisateur ait demandé
 * la réorganisation des tournées
 * 
 * @author Hex'Calibur
 */

public class EtatReorgTourneesDemandee extends EtatDefaut{
    
    /**Tournée de la livraison sélectionnée pour un changement de tournées
     */
    protected int tourneeLivraisonAChanger;
    /**Place dans la tournée de la livraison sélectionnée pour un changement de tournées
     */
    protected int indiceLivraisonAChanger;

    /**
     * Contructeur de EtatReorgTourneesDemandee
     */
    public EtatReorgTourneesDemandee() {
    }
    
    /**Permet d'avancer ou de retarder une livraison en échangeant deux 
     * livraisons au sein d'une même tournée
     * @param gestionLivraison
     * @param fenetre
     * @param haut Vrai si la livraison est avancée
     * @param indexLivraison Place dans la tournée de la livraison à avancer ou retarder
     * @param indexTournee Tournée de la livraison à avancer ou retarder
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
    
    /**Retient la tournée et la place d'une livraison dans une tournée en vue
     * d'un changement de tournée
     * @param livraisonCliquee 
     */
    @Override
    public void clicDroit(DescriptifLivraison livraisonCliquee){
        String[] identifiants = livraisonCliquee.getPoint().split("_");
        tourneeLivraisonAChanger = Integer.parseInt(identifiants[0])-1; //DESCRIPTIF
        indiceLivraisonAChanger = Integer.parseInt(identifiants[1])-1; //DESCRIPTIF
        
    }
    
    /**Change de tournée la livraison retenue dans cet état
     * @param gestionLivraison
     * @param fenetre
     * @param indiceTourneeChoisi
     * @param commandes 
     */
    @Override
    public void selectionMenuLivreurs(GestionLivraison gestionLivraison, Deliverif fenetre, int indiceTourneeChoisi, ListeCommandes commandes){
        int nouvelIndice = gestionLivraison.getTournees()[indiceTourneeChoisi].getTrajet().size()-1;
        commandes.ajouterCde(new CdeChangerLivraisonTournee(gestionLivraison, tourneeLivraisonAChanger, indiceTourneeChoisi, indiceLivraisonAChanger, nouvelIndice));
        fenetre.changerVueTextuelle(indiceTourneeChoisi);
    }
    
    /**Validation des modifications et retour au menu
     * @param controleur
     * @param fenetre
     */
    @Override
    public void validerReorganisation(Deliverif fenetre, Controleur controleur){
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
        fenetre.estReorgFinie();
    }
    
    /**Sélectionne une livraison sur le plan et l'identifie sur la vue graphique et textuelle
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude 
     */
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
        PointPassage pointClique = gestionLivraison.pointPassagePlusProche(latitude, longitude);
        fenetre.estPointPassageSelectionne(pointClique.getPosition().getLatitude(), pointClique.getPosition().getLongitude());
        int[] positionDansTournee = gestionLivraison.ouEstLePoint(pointClique);
        fenetre.estSelectionne(positionDansTournee[0], positionDansTournee[1]);
    }
    
    /**Retour au menu avec annulation des modifications
     * @param fenetre
     */
    @Override
    public void boutonAnnuler(Controleur controleur, Deliverif fenetre, ListeCommandes listeCdes){
        fenetre.estReorgFinie();
        controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
    }
    
    /**
     * 
     * @param fenetre
     * @param lat
     * @param lon 
     */
    @Override
    public void zoomPlus(Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees();
        fenetre.getVueGraphique().dessinerMarqueur();
    }
    
    /**
     * 
     * @param fenetre
     * @param lat
     * @param lon 
     */
    @Override
    public void zoomMoins(Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees();
        fenetre.getVueGraphique().dessinerMarqueur();
    }
}
