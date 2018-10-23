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

interface Etat
{
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier);
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier);
}
