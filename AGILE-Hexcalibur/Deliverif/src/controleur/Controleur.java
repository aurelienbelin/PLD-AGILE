/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

/**
 *
 * @author malbinet
 */

public class Controleur
{
    
    protected static final EtatInit ETAT_INIT = new EtatInit();
    protected static final EtatPlanCharge ETAT_PLAN_CHARGE = new EtatPlanCharge();
    protected static final EtatLivraisonsCharge ETAT_LIVRAISONS_CHARGE = new EtatLivraisonsCharge();
    protected static final EtatCalculTournees ETAT_CALCUL_TOURNEES = new EtatCalculTournees();
    protected static final EtatTourneesCalculees ETAT_TOURNEES_CALCULEES = new EtatTourneesCalculees();
    
    protected static Etat etatCourant;
    private final modele.GestionLivraison gestionLivraison;
    
    public Controleur (modele.GestionLivraison gestionLivraison){
        this.gestionLivraison = gestionLivraison;
        Controleur.etatCourant = ETAT_INIT;
    }
    
    public void boutonChargePlan (String fichier){
        etatCourant.chargePlan(gestionLivraison, fichier);
    }

}


