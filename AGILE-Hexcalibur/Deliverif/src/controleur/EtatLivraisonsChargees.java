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
      *  @see modele.GestionLivraison
      *  @see EtatCalculTournees
     */
    @Override
    public void calculerTournees(modele.GestionLivraison gestionLivraison, int nbLivreurs){
        gestionLivraison.calculerTournee(nbLivreurs);
        Controleur.etatCourant = Controleur.ETAT_CALCUL_TOURNEES;
    }
}
