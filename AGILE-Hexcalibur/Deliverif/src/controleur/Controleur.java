package controleur;

/**En réaction aux actions de l'utilisateur, l'IHM envoie des notifications au 
 * contrôleur qui utilise les méthodes ci-dessous pour faire passer 
 * l'application dans l'état approprié. 
 * Utilisation du pattern State
 *
 * @author Hex'Calibur
 */

public class Controleur {
    /** @see Etat
     */
    protected static final EtatInit ETAT_INIT = new EtatInit();
    protected static final EtatPlanCharge ETAT_PLAN_CHARGE = new EtatPlanCharge();
    protected static final EtatLivraisonsChargees ETAT_LIVRAISONS_CHARGEES = new EtatLivraisonsChargees();
    protected static final EtatCalculTournees ETAT_CALCUL_TOURNEES = new EtatCalculTournees();
    protected static final EtatTourneesCalculees ETAT_TOURNEES_CALCULEES = new EtatTourneesCalculees();
    
    /** etatCourant prendra successivement les états définis ci-dessus comme 
     * valeurs
     */
    protected static Etat etatCourant;
    
    /** La classe GestionLivraison est le point d'entrée du modèle.
     *  C'est avec cette classe que le controleur communique pour intéragir 
     * avec les classes du modèle.
     */
    private final modele.outils.GestionLivraison gestionLivraison;
    
    private final deliverif.Deliverif fenetre;
    
    
    
    /**@param gestionLivraison
     * @see modele.GestionLivraison
     * @version 1
     */
    public Controleur (modele.outils.GestionLivraison gestionLivraison, deliverif.Deliverif fenetre){
        this.gestionLivraison = gestionLivraison;
        Controleur.etatCourant = ETAT_INIT;
        this.fenetre = fenetre;
    }
    
    /**@param fichier
     * @see Etat
     * @version 1
     */
    public void boutonChargePlan (String fichier){
        etatCourant.chargePlan(gestionLivraison, fichier, fenetre);
    }
    
    /**@param fichier
     * @see Etat
     * @version 1
     */
    public void boutonChargeLivraisons (String fichier){
        etatCourant.chargeLivraisons(gestionLivraison, fichier, fenetre);
    }
    
    /**@param nbLivreurs
     * @see Etat
     * @version 1
     */
    public void boutonCalculerTournees (int nbLivreurs){
        etatCourant.calculerTournees(gestionLivraison, nbLivreurs, fenetre);
    }

}


