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

/**En réaction aux actions de l'utilisateur, l'IHM envoie des notifications au 
 * contrôleur qui suivant l'état courant de l'application réalisera les actions 
 * appropriées.  
 * Utilisation du pattern State
 *
 * @author Hex'Calibur
 */

public class Controleur {
    /**Etat dans lequel se trouve l'application au lancement de l'application. 
     * @see EtatInit
     */
    protected static final EtatInit ETAT_INIT = new EtatInit();

    /**Etat dans lequel se trouve l'application lorsqu'un plan a été chargé.
     * @see EtatPlanCharge
     */
    protected static final EtatPlanCharge ETAT_PLAN_CHARGE = new EtatPlanCharge();

    /**Etat dans lequel se trouve l'application lorsqu'un fichier contenant les 
     * demandes de livraisons a été chargé.
     * @see EtatLivraisonsChargees
     */
    protected static final EtatLivraisonsChargees ETAT_LIVRAISONS_CHARGEES = new EtatLivraisonsChargees();

    /**Etat dans lequel se trouve l'application lorsqu'un calcul de tournées a
     * été lancé et n'est pas encore terminé.
     * @see EtatCalculTournees
     */
    protected static final EtatCalculTournees ETAT_CALCUL_TOURNEES = new EtatCalculTournees();

    /**Etat dans lequel se trouve l'application à la fin d'un calcul de 
     * tournées.
     * @see EtatTourneesCalculees
     */
    protected static final EtatTourneesCalculees ETAT_TOURNEES_CALCULEES = new EtatTourneesCalculees();
    
    /**Etat dans lequel se trouve l'application quand l'ajout d'une livraison a 
     * été demandé. 
     * Appartient au processus d'ajout d'une livraison, point d'entrée.
     * @see EtatIntersectionCliqable
     */
    protected static final EtatIntersectionCliquable ETAT_PLAN_CLIQUABLE = new EtatIntersectionCliquable();
    
    /**Etat dans lequel se trouve l'application quand un emplacement a été 
     * sélectionné pour la nouvelle livraison mais non encore validé.
     * Appartient au processus d'ajout d'une livraison, étape de sélection de 
     * l'emplacement dela nouvelle livraison.
     * @see EtatIntersectionSelectionnee
     */
    protected static final EtatIntersectionSelectionnee ETAT_INTERSECTION_SELECTIONNEE = new EtatIntersectionSelectionnee();
    
    /**Etat dans lequel se trouve l'application quand l'emplacement de la 
     * nouvelle livraison a été validé.
     * Appartient au processus d'ajout d'une livraison, point d'entrée de 
     * l'attribution de la livraison à un livreur.
     * @see EtatIntersectionValidee
     */
    protected static final EtatIntersectionValidee ETAT_INTERSECTION_VALIDEE = new EtatIntersectionValidee();
    
    /**Etat dans lequel se trouve l'application quand un livreur a été choisi
     * pour la nouvelle livraison mais non validé.
     * Appartient au processus, étape d'attribution à un livreur et validation.
     * @see EtatAjoutLivraison
     */
    protected static final EtatAjoutLivraison ETAT_AJOUT_LIVRAISON = new EtatAjoutLivraison();
    
    /**Etat dans lequel se trouve l'application quand la suppression d'une
     * livraison a été demandée.
     * Processus de suppression d'une livraison, point d'entrée.
     * @see EtatSupprimer
     */
    protected static final EtatSupprimerLivraison ETAT_SUPPRIMER_LIVRAISON = new EtatSupprimerLivraison();
    
    /**Etat dans lequel se trouve l'application quand une livraison a été 
     * sélectionnée pour la suppression.
     * Processus de suppression d'une livraison, sélection et validation.
     * @see EtatLivraisonSelectionnee
     */
    protected static final EtatLivraisonSelectionnee ETAT_LIVRAISON_SELECTIONNEE = new EtatLivraisonSelectionnee();
    

    /**Etat dans lequel se trouve l'application quand la réorganisation des 
     * tournées a été demandée.
     * @see EtatReorgTourneesDemandee
     */
    protected static final EtatReorgTourneesDemandee ETAT_REORG_TOURNEES_DEMANDE = new EtatReorgTourneesDemandee();
    
    /**etatCourant représente l'état dans lequel se trouve l'application.
     * Il prend successivement comme valeur les états décrits ci-dessus.
     */
    private static Etat etatCourant;
    
    /**La classe GestionLivraison est le point d'entrée du modèle.
     * C'est avec cette classe que le controleur communique pour intéragir 
     * avec les classes du modèle.
     */
    private final GestionLivraison gestionLivraison;
    
    /**La classe Deliverif est le point d'entrée de la vue.
     * C'est avec cette classe que le controleur communique pour intéragir avec 
     * les classes de la vue.
     */
    private final Deliverif fenetre;
    
    /**
     * Stocke l'ensemble des commandes réalisées au cours de l'application.
     */
    private ListeCommandes listeCde;
    
    
    
    /**Crée le conroleur dans son état inital.
     * @param gestionLivraison
     * @param fenetre
     */
    public Controleur (GestionLivraison gestionLivraison, Deliverif fenetre){
        this.gestionLivraison = gestionLivraison;
        Controleur.etatCourant = ETAT_INIT;
        this.fenetre = fenetre;
        this.listeCde = new ListeCommandes();
    }
    
    /**
    * Change l'etat courant du controleur
    * @param etat le nouvel etat courant
    */
    protected void setEtatCourant(Etat etat){
            etatCourant = etat;
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le 
     * bouton "Charger Plan". De manière générale, il sera alors demandé au 
     * modèle de charger un fichier plan.
     * @param fichier
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public void boutonChargePlan (String fichier) throws SAXException, IOException, Exception{
        etatCourant.boutonChargePlan(this, gestionLivraison, fichier, fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le 
     * bouton "Charger Livraison". De manière générale, il sera alors demandé au 
     * modèle de charger un fichier de demande de livraisons.
     * @param fichier
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public void boutonChargeLivraisons (String fichier) throws SAXException, IOException, Exception{
        etatCourant.boutonChargeLivraisons(this, gestionLivraison, fichier, fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le 
     * bouton "Calculer tournées". De manière générale, il sera alors demandé au 
     * modèle de lancer le calcul des tournées.
     * @param nbLivreurs
     */
    public void boutonCalculerTournees (int nbLivreurs){
        etatCourant.boutonCalculerTournees(this, gestionLivraison, nbLivreurs, fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le 
     * bouton "Stop". De manière générale, il sera alors demandé au 
     * modèle d'arrêter la calcul des tournées.
     */
    public void boutonArretCalcul(){
        etatCourant.boutonArreterCalcul(this, this.gestionLivraison, this.fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton d'ajout de livraison. De manière générale, il sera alors demandé 
     * à la vue de s'adapter au changement d'état de l'application.
     */
    public void boutonAjouterLivraison() {
        etatCourant.boutonAjouterLivraison(this, this.fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de suppression de livraison. De manière générale, il sera alors 
     * demandé à la vue de s'adapter au changement d'état de l'application.
     */
    public void boutonSupprimerLivraison(){
        etatCourant.boutonSupprimerLivraison(this, fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de réorganisation de livraison. De manière générale, il sera alors
     * demandé à la vue de s'adapter au changement d'état de l'application.
     */
    public void boutonReorgTournees(){
        etatCourant.boutonReorgTournees(this, fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de retour au menu. De manière générale, toute action en cours sera
     * alors annulée et la vue s'adaptera au changement d'état.
     */
    public void boutonAnnuler() {
        etatCourant.boutonAnnuler(this, this.fenetre, this.listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de retour à la sélection. Utilisée lorsque le processus (comme
     * celui de l'ajout) demande la sélection d'une intersection sur le plan.
     */
    public void boutonRetour(){
        etatCourant.boutonRetourSelection(this, this.fenetre, this.listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de validation de la suppression. 
     */
    public void boutonValiderSupprimerLivraison() {
        etatCourant.validerSuppression(this, this.gestionLivraison, this.fenetre, this.listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de validation de la sélection. 
     */
    public void boutonValiderSelection() {
        etatCourant.validerSelection(this, this.fenetre);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de validation d'ajout. 
     */
    public void boutonValiderAjout(float duree){
        etatCourant.validerAjout(this, gestionLivraison, fenetre, duree, this.listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur le
     * bouton de validation de réorganisation. 
     */
    public void validerReorganisation(){
        etatCourant.validerReorganisation(fenetre, null);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur un des 
     * boutons "+" du processus d'ajout. De manière générale, il sera alors
     * demandé au modèle d'ajouter la nouvelle livraison à cette emplacement 
     * dans la tournée.
     * @param indexPlus
     * @param indexTournee
     */
    public void clicPlus(int indexPlus, int indexTournee){
        etatCourant.clicPlus(this, this.gestionLivraison, this.fenetre, indexPlus, indexTournee, fenetre.getDuree(), this.listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur un des
     * boutons fléchés du processus de réorganisation des tournées. De manière 
     * générale, il sera alors demandé au modèle de modifier les tournées.
     * @param haut
     * @param indiceLivraison
     * @param indiceTournee
     */
    public void clicFleche(boolean haut, int indiceLivraison, int indiceTournee){
        etatCourant.clicFleche(gestionLivraison, fenetre, haut, indiceLivraison, indiceTournee, this.listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère la sélection d'un
     * livreur dans un menu répertoriant tous les livreurs.
     * @param indiceTourneeChoisi
     */
    public void selectionMenuChangerTournee(int indiceTourneeChoisi){
        etatCourant.selectionMenuLivreurs(gestionLivraison, fenetre, indiceTourneeChoisi, listeCde);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics droits.
     * @param livraisonCliquee
     */
    public void clicDroit(DescriptifLivraison livraisonCliquee){
        etatCourant.clicDroit(livraisonCliquee);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics gauches.
     * @param latitude
     * @param longitude
     */
    public void clicGauche(double latitude, double longitude) {
        etatCourant.clicGauche(this, this.gestionLivraison, this.fenetre, latitude, longitude);
    }
    
    /**Redirige vers la méthode de l'état courant qui gère les clics sur la vue 
     * textuelle des livraisons. De manière générale, il sera alors demandé au 
     * modèle de trouver le point de passage correspondant et à la vue de 
     * l'afficher sur le plan.
     * @param point
     */
    public void afficherMarqueur(DescriptifLivraison point) {
        etatCourant.clicDescriptionLivraison(this, this.gestionLivraison, point, this.fenetre);
    }
    
    /**Redirige vers la méthode e l'état courant qui gère les zooms avant.
     * @param lat
     * @param lon
     */
    public void scrollZoomPlus(double lat, double lon){
        etatCourant.zoomPlus(this.fenetre, lat, lon);
    }
    
    /**Redirige vers la méthode e l'état courant qui gère les zooms arrière.
     * @param lat
     * @param lon
     */
    public void scrollZoomMoins(double lat, double lon){
        etatCourant.zoomMoins(this.fenetre, lat, lon);
    }
    
    /**Annule une action précedement exécutée.
     */
    public void undo(){
        this.etatCourant.undo(this.listeCde);
    }
    
    /**Refait une action précedemment annulée.
     */
    public void redo(){
        this.etatCourant.redo(this.listeCde);
    }
}


