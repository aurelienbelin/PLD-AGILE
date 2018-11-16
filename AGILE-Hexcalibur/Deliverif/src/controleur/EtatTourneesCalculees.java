/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import deliverif.Deliverif;
import java.io.IOException;
import modele.GestionLivraison;
import org.xml.sax.SAXException;

/**Etat dans lequel se trouve l'application quand le calcul des tournées est 
 * terminé.
 *
 * @author Hex'Calibur
 */

public class EtatTourneesCalculees extends EtatDefaut{
    
    private final String SUCCES = "SUCCES";
    /**
     * Constructeur EtatLivraisonsChargees
     */
    public EtatTourneesCalculees() {
    }
    
     /**  Cette méthode délègue le calcul des tournées au modèle
      * Pendant le temps de calcul, l'application est dans l'état CalculTournees
      *  @param controleur
      *  @param gestionLivraison
      *  @param nbLivreurs
      *  @param fenetre
      *  @see GestionLivraison
      *  @see EtatCalculTournees
     */
    @Override
    public void boutonCalculerTournees(Controleur controleur, GestionLivraison gestionLivraison, int nbLivreurs, Deliverif fenetre){
        controleur.setEtatCourant(Controleur.ETAT_CALCUL_TOURNEES);
        try{
            gestionLivraison.calculerTournees(nbLivreurs, Integer.MAX_VALUE);
            fenetre.activerBoutonArreterCalcul(false);
            fenetre.estTourneesCalculees(SUCCES);
        } catch(Exception e){
            
        }
    }
    
    /**Cette méthode délègue le chargement des livraisons au modèle
     * Si le chargement s'est bien passé on passe dans l'EtatLivraisonsChargees
     * @param controleur
     * @param gestionLivraison
     * @param fichier
     * @param fenetre
     * @see GestionLivraison
     * @see EtatLivraisonsChargees
     */
    @Override
    public void boutonChargeLivraisons (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre) {
        try{
            gestionLivraison.effacerTournees();
            gestionLivraison.chargerDemandeLivraison(fichier);
            controleur.setEtatCourant(Controleur.ETAT_LIVRAISONS_CHARGEES);
            fenetre.estDemandeLivraisonChargee(SUCCES);
        } catch (SAXException e){
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        } catch (IOException e) {
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        } catch (Exception e) {
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        }
    }
    
    /**Cette méthode délègue le chargement du plan au modèle
      *Si le chargement s'est bien passé on passe dans l'EtatPlanCharge 
      *@param controleur
      *@param gestionLivraison
      *@param fichier
      *@param fenetre
      *@see modele.GestionLivraison
      *@see EtatPlanCharge
     */
    @Override
    public void boutonChargePlan (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre){
        try{
            gestionLivraison.getDemande().effacerLivraisons();
            gestionLivraison.effacerTournees();
            gestionLivraison.chargerPlan(fichier);
            controleur.setEtatCourant(Controleur.ETAT_PLAN_CHARGE);
            fenetre.estPlanCharge(SUCCES);
        } catch (SAXException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (IOException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (Exception e) {
            fenetre.estPlanCharge(e.getMessage());
        }
    }
    
    /**Fait entrer dans le processus d'ajout de livraison
     * L'application passe dans l'état IntersectionCliquable
     * @param controleur
     * @param fenetre 
     */
    @Override
    public void boutonAjouterLivraison (Controleur controleur, Deliverif fenetre) {
        controleur.setEtatCourant(Controleur.ETAT_PLAN_CLIQUABLE);
        fenetre.estPlanCliquable();
    }
    
    /**Fait entrer dans le processus de suppression de livraison
     * L'application passe dans l'état SupprimerLivraison
     * @param controleur
     * @param fenetre 
     */
    @Override
    public void boutonSupprimerLivraison (Controleur controleur, Deliverif fenetre) {
        controleur.setEtatCourant(Controleur.ETAT_SUPPRIMER_LIVRAISON);
        fenetre.ajouterSuppression();
    }
    
    /**Fait entrer dans le processus de réorganisation des tournées
     * L'application passe dans l'état ReorgTourneesDemandee 
     * @param controleur
     * @param fenetre 
     */
    @Override
    public void boutonReorgTournees(Controleur controleur, Deliverif fenetre){
        fenetre.estReorgTourneesDemandee();
        controleur.setEtatCourant(Controleur.ETAT_REORG_TOURNEES_DEMANDE);
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
        fenetre.getVueGraphique().dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
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
        fenetre.getVueGraphique().dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        fenetre.getVueGraphique().dessinerMarqueur();
    }
}
