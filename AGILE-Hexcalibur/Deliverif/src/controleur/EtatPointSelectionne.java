/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

/**
 *
 * @author Amine Nahid
 */
public class EtatPointSelectionne extends EtatDefaut{

    public EtatPointSelectionne() {
    }
    
    @Override
    public void supprimerLivraison (deliverif.Deliverif fenetre) {
        Controleur.etatCourant = Controleur.ETAT_TOURNEES_CALCULEES;
    }
}
