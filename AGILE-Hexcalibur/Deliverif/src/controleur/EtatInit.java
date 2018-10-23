package controleur;

/** Etat dans lequel l'application se trouve à son ouverture
 *  Possibilité de charger un plan
 *
 * @author Hex'Calibur
 */

public class EtatInit extends EtatDefaut{
    
    /**
     * Constructeur EtatInit
     */
    public EtatInit (){}
    
    /**  Cette méthode délègue la chargement du plan au modèle
      *  Si le chargement s'est bien passé on passe dans 
      *  l'EtatPlanCharge 
      *  @param gestionLivraison
      *  @param fichier
      *  @see modele.GestionLivraison
      *  @see EtatPlanCharge
     */
    @Override
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier){
        int cre = gestionLivraison.chargerVille(fichier);
        if(cre==0){
            Controleur.etatCourant = Controleur.ETAT_PLAN_CHARGE;
        }
    }
}
