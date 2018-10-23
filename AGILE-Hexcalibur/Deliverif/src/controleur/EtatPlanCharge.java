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

public class EtatPlanCharge extends EtatDefaut{
    
    @Override
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier){
        int cre = gestionLivraison.chargerDemandeLivraison(fichier);
        if(cre==0){
            Controleur.etatCourant = Controleur.ETAT_LIVRAISONS_CHARGE;
        }
    }
}
