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

/** Etat dans lequel se trouve l'application une fois le fichier de livraisons 
 *  chargé.
 * @author Hex'Calibur
 */

public class EtatLivraisonsChargees extends EtatDefaut{

    private final String SUCCES = "SUCCES";
    
    /**
     * Constructeur EtatLivraisonsChargees
     */
    public EtatLivraisonsChargees() {
    }
    
    /**Demande au modèle de lancer le calcul
     * @param controleur
     * @param gestionLivraison
     * @param nbLivreurs
     * @param fenetre
     * @see GestionLivraison
     * @see EtatCalculTournees
     */
    @Override
    public void boutonCalculerTournees(Controleur controleur, GestionLivraison gestionLivraison, int nbLivreurs, Deliverif fenetre){
        controleur.setEtatCourant(Controleur.ETAT_CALCUL_TOURNEES);
        try{
            gestionLivraison.calculerTournees(nbLivreurs, Integer.MAX_VALUE);
            fenetre.activerBoutonArreterCalcul(false);
        } catch(Exception e){
            e.printStackTrace();
            controleur.setEtatCourant(Controleur.ETAT_LIVRAISONS_CHARGEES);
        }
    }
    
    /**Cette méthode délègue la chargement des livraisons au modèle
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
            gestionLivraison.chargerDemandeLivraison(fichier);
            fenetre.estDemandeLivraisonChargee(SUCCES);
        } catch (SAXException e){
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        } catch (IOException e) {
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        } catch (Exception e) {
            fenetre.estDemandeLivraisonChargee(e.getMessage());
        }
    }
    
    /**Cette méthode délègue la chargement du plan au modèle
      *Si le chargement s'est bien passé on passe dans l'EtatPlanCharge 
     * @param controleur
      *@param gestionLivraison
      *@param fichier
      *@param fenetre
      *@see GestionLivraison
      *@see EtatPlanCharge
     */
    @Override
    public void boutonChargePlan (Controleur controleur, GestionLivraison gestionLivraison, String fichier, Deliverif fenetre){
        try{
            gestionLivraison.getDemande().effacerLivraisons();
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
}