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
import modele.PointPassage;
import org.xml.sax.SAXException;

/**
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
    
    /** 
      *  @param gestionLivraison
      *  @param nbLivreurs
      * @param fenetre
      *  @see modele.GestionLivraison
      *  @see EtatCalculTournees
     */
    @Override
    public void calculerTournees(modele.GestionLivraison gestionLivraison, int nbLivreurs, deliverif.Deliverif fenetre){
        Controleur.etatCourant = Controleur.ETAT_CALCUL_TOURNEES;
        try{
            gestionLivraison.calculerTournees(nbLivreurs, Integer.MAX_VALUE);
            fenetre.activerBoutonArreterCalcul(false);
            fenetre.estTourneesCalculees(SUCCES);
        } catch(Exception e){
            
        }
    }
    
    /**  Cette méthode délègue la chargement des livraisons au modèle
     *   Si le chargement s'est bien passé on passe dans 
     *   l'EtatLivraisonsChargees   
     *  @param gestionLivraison
     *  @param fichier
     * @param fenetre
     *  @see modele.GestionLivraison
     *  @see EtatLivraisonsChargees
     */
    @Override
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) {
        try{
            gestionLivraison.effacerTournees();
            gestionLivraison.chargerDemandeLivraison(fichier);
            Controleur.etatCourant = Controleur.ETAT_LIVRAISONS_CHARGEES;
            fenetre.estDemandeLivraisonChargee(SUCCES);
        } catch (SAXException e){
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        } catch (IOException e) {
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        } catch (Exception e) {
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        }
    }
    
    /**  Cette méthode délègue la chargement du plan au modèle
      *  Si le chargement s'est bien passé on passe dans 
      *  l'EtatPlanCharge 
      *  @param gestionLivraison
      *  @param fichier
     * @param fenetre
      *  @see modele.GestionLivraison
      *  @see EtatPlanCharge
     */
    @Override
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre){
        try{
            gestionLivraison.getDemande().effacerLivraisons();
            gestionLivraison.effacerTournees();
            gestionLivraison.chargerPlan(fichier);
            Controleur.etatCourant = Controleur.ETAT_PLAN_CHARGE;
            fenetre.estPlanCharge(SUCCES);
        } catch (SAXException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (IOException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (Exception e) {
            fenetre.estPlanCharge(e.getMessage());
        }
    }
    
    @Override
    public void ajouterLivraison (Deliverif fenetre) {
        Controleur.etatCourant = Controleur.ETAT_PLAN_CLIQUABLE;
        fenetre.estPlanCliquable();
    }
    
    @Override
    public void supprimerLivraison (Deliverif fenetre) {
        Controleur.etatCourant = Controleur.ETAT_SUPPRIMER_LIVRAISON;
        fenetre.ajouterSuppression();
    }
    
    public void reorgTournees(Deliverif fenetre){
        fenetre.estReorgTourneesDemandee();
        Controleur.etatCourant = Controleur.ETAT_REORG_TOURNEES_DEMANDE;
    }
    
    @Override
    public void zoomPlus(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomPlus(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        fenetre.getVueGraphique().dessinerMarqueur();
    }
    
    @Override
    public void zoomMoins(deliverif.Deliverif fenetre, double lat, double lon){
        fenetre.getVueGraphique().zoomMoins(lat,lon);
        fenetre.getVueGraphique().dessinerPlan();
        fenetre.getVueGraphique().dessinerPtLivraison();
        fenetre.getVueGraphique().dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        fenetre.getVueGraphique().dessinerMarqueur();
    }
}
