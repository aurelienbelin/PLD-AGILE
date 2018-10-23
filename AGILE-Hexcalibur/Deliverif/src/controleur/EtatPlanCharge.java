package controleur;

/** Etat dans lequel se trouve l'application après le chargement du plan
 *  La seule action possible depuis cet état est le chargement d'une demande de 
 *  livraisons ou le chargement d'un autre plan
 * @author Hex'Calibur
 */

public class EtatPlanCharge extends EtatDefaut{

    /**
     * Constructeur EtatPlanCharge
     */
    public EtatPlanCharge() {
    }
    
    /**  Cette méthode délègue la chargement des livraisons au modèle
     *   Si le chargement s'est bien passé on passe dans 
     *   l'EtatLivraisonsChargees   
     *  @param gestionLivraison
     *  @param fichier
     *  @see modele.GestionLivraison
     *  @see EtatLivraisonsChargees
     */
    @Override
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier){
        int cre = gestionLivraison.chargerDemandeLivraison(fichier);
        if(cre==0){
            Controleur.etatCourant = Controleur.ETAT_LIVRAISONS_CHARGEES;
        }
    }
}
