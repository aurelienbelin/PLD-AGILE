/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import controleur.commandes.ListeCommandes;
import java.io.IOException;
import org.xml.sax.SAXException;

/**En réaction aux actions de l'utilisateur, l'IHM envoie des notifications au 
 * contrôleur qui utilise les méthodes ci-dessous pour faire passer 
 * l'application dans l'état approprié. 
 * Utilisation du pattern State
 *
 * @author Hex'Calibur
 */

public class Controleur {
    /** @see Etat
     */
    protected static final EtatInit ETAT_INIT = new EtatInit();

    /**
     *
     */
    protected static final EtatPlanCharge ETAT_PLAN_CHARGE = new EtatPlanCharge();

    /**
     *
     */
    protected static final EtatLivraisonsChargees ETAT_LIVRAISONS_CHARGEES = new EtatLivraisonsChargees();

    /**
     *
     */
    protected static final EtatCalculTournees ETAT_CALCUL_TOURNEES = new EtatCalculTournees();

    /**
     *
     */
    protected static final EtatTourneesCalculees ETAT_TOURNEES_CALCULEES = new EtatTourneesCalculees();
    
    /**
     * 
     */
    protected static final EtatPlanCliquable ETAT_PLAN_CLIQUABLE = new EtatPlanCliquable();
    
    /**
     * 
     */
    protected static final EtatIntersectionSelectionnee ETAT_INTERSECTION_SELECTIONNEE = new EtatIntersectionSelectionnee();
    
    /**
     * 
     */
    protected static final EtatIntersectionValidee ETAT_INTERSECTION_VALIDEE = new EtatIntersectionValidee();
    
    /**
     * 
     */
    protected static final EtatAjoutLivraison ETAT_AJOUT_LIVRAISON = new EtatAjoutLivraison();
    protected static final EtatPointSelectionne ETAT_POINT_SELECTIONNE = new EtatPointSelectionne();
    
    /** etatCourant prendra successivement les états définis ci-dessus comme 
     * valeurs
     */
    protected static Etat etatCourant;
    
    /** La classe GestionLivraison est le point d'entrée du modèle.
     *  C'est avec cette classe que le controleur communique pour intéragir 
     * avec les classes du modèle.
     */
    private final modele.outils.GestionLivraison gestionLivraison;
    
    private final deliverif.Deliverif fenetre;
    
    /**
     * Stocke l'ensemble des commandes réalisées au cours de l'application.
     */
    private ListeCommandes listeCde;
    
    
    
    /** * @param gestionLivraison
     * @param fenetre
     * @see modele.GestionLivraison
     * @version 1
     */
    public Controleur (modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre){
        this.gestionLivraison = gestionLivraison;
        Controleur.etatCourant = ETAT_INIT;
        this.fenetre = fenetre;
        this.listeCde = new ListeCommandes();
    }
    
    /** * @param fichier
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @see Etat
     * @version 1
     */
    public void boutonChargePlan (String fichier) throws SAXException, IOException, Exception{
        etatCourant.chargePlan(gestionLivraison, fichier, fenetre);
    }
    
    /** * @param fichier
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @see Etat
     * @version 1
     */
    public void boutonChargeLivraisons (String fichier) throws SAXException, IOException, Exception{
        etatCourant.chargeLivraisons(gestionLivraison, fichier, fenetre);
    }
    
    /**@param nbLivreurs
     * @see Etat
     * @version 1
     */
    public void boutonCalculerTournees (int nbLivreurs){
        etatCourant.calculerTournees(gestionLivraison, nbLivreurs, fenetre);
    }
    
    //Test
    public void afficherMarqueur(deliverif.DescriptifChemin point) {
        etatCourant.trouverLocalisation(this.gestionLivraison, point, this.fenetre);
    }
    
    /**@see Etat
     * @version 2.1
     */
    public void boutonAjouterLivraison() {
        etatCourant.ajouterLivraison(this.fenetre);
    }
    
    public void boutonSupprimerLivraison() {
        etatCourant.supprimerLivraison(this.gestionLivraison, this.fenetre);
    }
    
    /**@param latitude
     * @param longitude
     * @param
     * @see Etat
     * @version 2.1
     */
    public void clicGauche(double latitude, double longitude) {
        etatCourant.clicGauche(this.gestionLivraison, this.fenetre, latitude, longitude);
    }
    
    public void boutonAnnuler() {
        etatCourant.annuler(this.fenetre);
    }
    
    public void boutonValiderSelection() {
        etatCourant.validerSelection(this.fenetre);
    }
    
    public void boutonRetour(){
        etatCourant.retourSelection(this.fenetre);
    }

    public void boutonArretCalcul(){
        etatCourant.arreterCalcul(this.gestionLivraison, this.fenetre);
    }
    
    /** @see Etat
     * @version 1
     */
    public void scrollZoomPlus(double lat, double lon){
        etatCourant.zoomPlus(this.fenetre, lat, lon);
    }
    
    public void scrollZoomMoins(double lat, double lon){
        etatCourant.zoomMoins(this.fenetre, lat, lon);
    }
    
    public void clicPlus(int indexPlus, int indexTournee){
        etatCourant.clicPlus(this.fenetre, indexPlus, indexTournee);
    }
    
    public void boutonValiderAjout(float duree){
        etatCourant.validerAjout(gestionLivraison, fenetre, duree, this.listeCde);
    }
}


