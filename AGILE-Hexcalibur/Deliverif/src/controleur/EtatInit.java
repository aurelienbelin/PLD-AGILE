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

public class EtatInit extends EtatDefaut{
    
    public EtatInit (){}
    
    @Override
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier){
        int cre = gestionLivraison.chargerVille(fichier);
        if(cre==0){
            Controleur.etatCourant = Controleur.ETAT_PLAN_CHARGE;
        }
    }
}
