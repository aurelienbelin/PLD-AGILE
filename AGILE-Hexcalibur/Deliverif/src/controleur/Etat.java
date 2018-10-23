package controleur;

/** Par ses actions, l'utilisateur fait passer l'application d'une situation 
 * à une autre. Suivant la situation, la réponses aux demandes de l'utilisateur 
 * ne sera pas la même. Cette interface représente le comportement d'une 
 * situation face aux actions de l'utilisateur.
 *
 * @author Hex'Calibur
 */

interface Etat
{
     /** 
      *  @param gestionLivraison
      *  @param fichier
      *  @see modele.GestionLivraison
     */
    public void chargePlan (modele.GestionLivraison gestionLivraison, String fichier);
    
    /** 
      *  @param gestionLivraison
      *  @param fichier
      *  @see modele.GestionLivraison
     */
    public void chargeLivraisons (modele.GestionLivraison gestionLivraison, String fichier);
    
    /** 
      *  @param gestionLivraison
      *  @param nbLivreurs
      *  @see modele.GestionLivraison
     */
    public void calculerTournees(modele.GestionLivraison gestionLivraison, int nbLivreurs);
}
