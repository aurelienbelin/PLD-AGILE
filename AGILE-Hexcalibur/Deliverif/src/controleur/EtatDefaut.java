package controleur;

/** Classe mère de tous les états.
 * Améliore la visibilité du code, dans les classes filles on implémente 
 * seulement les méthodes ayant un traitement particulier. 
 *
 * @author Hex'calibur
 */

public class EtatDefaut implements Etat
{

    /**
     * Constructeur EtatDefaut
     */
    public EtatDefaut (){}
    
    /**
     * @param gestionLivraison
     * @param fichier
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier){
    }
    
    /**
     * @param gestionLivraison
     * @param fichier
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier){
    }
    
    /**
     * @param gestionLivraison
     * @param nbLivreurs
     * @see modele.GestionLivraison
     * @version 1
     */
    @Override
    public void calculerTournees(modele.GestionLivraison gestionLivraison, int nbLivreurs){
    }
}
