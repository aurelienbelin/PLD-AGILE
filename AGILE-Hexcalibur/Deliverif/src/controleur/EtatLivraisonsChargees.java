package controleur;

/** Etat dans lequel se trouve l'application une fois le fichier de livraisons 
 *  chargé.
 * @author Hex'Calibur
 */

public class EtatLivraisonsChargees extends EtatDefaut{

    /**
     * Constructeur EtatLivraisonsChargees
     */
    public EtatLivraisonsChargees() {
    }
    
    /** 
      *  @param gestionLivraison
      *  @param nbLivreurs
      * @param fenetre
      *  @see modele.GestionLivraison
      *  @see EtatCalculTournees
     */
    @Override
    public void calculerTournees(modele.outils.GestionLivraison gestionLivraison, int nbLivreurs, deliverif.Deliverif fenetre){
        int cre = gestionLivraison.calculerTournees(nbLivreurs);
        if(cre==1){
            Controleur.etatCourant = Controleur.ETAT_CALCUL_TOURNEES;
        }else{
            Controleur.etatCourant = Controleur.ETAT_LIVRAISONS_CHARGEES;
        }
        fenetre.estTourneesCalculees(cre);
    }
}
