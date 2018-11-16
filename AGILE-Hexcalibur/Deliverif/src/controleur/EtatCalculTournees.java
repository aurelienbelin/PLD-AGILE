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

/** Etat dans lequel se trouve l'application pendant le calcul des tournées
 *  Permet l'affichage des solutions au fur et à mesure du calcul et l'arrêt du 
 *  calcul si la solution convient.
 *  Cet etat est codé différemment, car il observe un thread se déroulant en parallèle
 *  et attend sa fin d'exécution.
 * @author Hex'Calibur
 */

public class EtatCalculTournees extends EtatDefaut{
        
    /**
     * Constructeur EtatCalculTournees
     */
    public EtatCalculTournees(){
        
    }
     
    /**Arrête le calcul s'il est en cours
     * @param controleur
     * @param gestionLivraison
     * @param fenetre 
     */
    @Override
    public void boutonArreterCalcul(Controleur controleur, modele.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre){
        if(gestionLivraison.calculTSPEnCours()){
            gestionLivraison.arreterCalculTournee();
        }
        if (gestionLivraison.aSolution()){
            controleur.setEtatCourant(Controleur.ETAT_TOURNEES_CALCULEES);
            fenetre.estTourneesCalculees("SUCCES");
        } else {
            controleur.setEtatCourant(Controleur.ETAT_LIVRAISONS_CHARGEES);
        }
        fenetre.activerBoutonArreterCalcul(true);
    }
    
    /**Pas d'undo dans cet état
     * @param listeCde 
     */
    @Override
    public void undo(ListeCommandes listeCde){
    }
    
    /**Pas de redo dans cet état
     * @param listeCde 
     */
    @Override
    public void redo(ListeCommandes listeCde){
    }

}
