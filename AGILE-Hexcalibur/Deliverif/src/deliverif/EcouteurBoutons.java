/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package deliverif;

import controleur.Controleur;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

/**
 * Ecouteur des évènements émis par l'IHM de l'application.
 * @author Aurelien Belin
 * @see Deliverif
 * @see Controleur
 */
public class EcouteurBoutons{
    
    private final Deliverif fenetrePrincipale;
    private final VueGraphique vueGraphique;
    private final Controleur controleur;
    private File file;
    
    /**
     * Constructeur d'EcouteurBoutons
     * @param f - fenetre principale de l'application
     * @param c - Controleur de l'application
     * @see Deliverif
     * @see Controleur
     */
    public EcouteurBoutons(Deliverif f, Controleur c, VueGraphique v){
        this.fenetrePrincipale = f;
        this.controleur = c;
        this.vueGraphique = v;
    }
    
    /**
     * Appelle le chargement du plan dans le controleur.
     * @param e - ActionEvent déclenché
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception 
     */
    public void chargerPlanAction(ActionEvent e) throws InterruptedException, IOException, Exception{
        this.fenetrePrincipale.informationEnCours("Chargement du plan...");
        String nomFichier = choisirFichier("Choisir le plan à charger");
        if(nomFichier != null)
        {
            controleur.boutonChargePlan(nomFichier);
        }else{
            this.fenetrePrincipale.informationEnCours("");
        }
    }
    
    /**
     * Appelle le chargement de la demande de livraison dans le controleur.
     * @param e
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception 
     */
    public void chargerDemandeLivraisonAction(ActionEvent e) throws InterruptedException, IOException, Exception{
        //System.out.println("Choisir une demande de livraison à charger"); //DEBUG
        this.fenetrePrincipale.informationEnCours("Chargement de la demande de livraison...");
        String nomFichier = choisirFichier("Choisir la demande de livraison à charger");
        if(nomFichier != null)
        {
            controleur.boutonChargeLivraisons(nomFichier);
        }else{
            this.fenetrePrincipale.informationEnCours("");
        }
    }

    /**
     * Appelle le calcul des tournées dans le controleur.
     * @param e
     * @throws InterruptedException
     */
    public void calculerTourneesAction(ActionEvent e) throws InterruptedException{
        this.fenetrePrincipale.getVueGraphique().creerCalques(fenetrePrincipale.getNbLivreurs());
        this.fenetrePrincipale.informationEnCours("Calcul des tournées en cours...");
        controleur.boutonCalculerTournees(fenetrePrincipale.getNbLivreurs());
    }
    
    /**
     * Appelle la méthode du controleur correspondant à l'interruption du calcul des tournées.
     */
    public void arreterCalculTournees(){
        this.fenetrePrincipale.informationEnCours("Arrêt du calcul des tournées.");
        controleur.boutonArretCalcul();
    }

    /**
     * Modifie les tournées affichées dans les Vues de l'IHM.
     * @param e
     * @throws InterruptedException
     */
    public void changerTourneeAffichee(ActionEvent e) throws InterruptedException{
        int i = this.fenetrePrincipale.getVueTextuelle().changerDescription_Bis();
        
        if(i!=-1){
            //this.fenetrePrincipale.getVueGraphique().changerTourneeAffichee(i);
            if(i==0){
                this.fenetrePrincipale.getVueGraphique().dessinerTournees();
            }else{
                this.fenetrePrincipale.getVueGraphique().dessinerTournees(i);
            }
        }
    }
    /**
     * Actualise le plan en fonction du zoom
     * @param e
     * @throws InterruptedException
     */
    
    /**
     * Envoie une requête au controleur pour connaitre la localisation du point de livraison associé au composant DescriptifChemin.
     * @param dc 
     */
    public void localiserPointVueGraphique(DescriptifChemin dc){
        controleur.afficherMarqueur(dc);
    }
    
    /**
     * Devellope les détails du DescriptifChemin passé en paramètre.
     * @param dc 
     */
    public void obtenirDetailsVueTextuelle(DescriptifChemin dc){
        dc.developperDetails();
    }
    
    /**
     * Crée un objet donnat accès au Gestionnaire de fichier pour choisir un fichier à charger.
     * @param docACharger
     * @return URL du fichier choisi
     * @throws InterruptedException 
     */
    private String choisirFichier(String docACharger) throws InterruptedException
    {
        FileChooser fileChooser = new FileChooser();
        //Ouvre fileChooser au dernier dossier
        if(file != null){
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        }
        fileChooser.setTitle(docACharger);
        fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XML File", "*.xml")
        );
        file = Deliverif.openFileChooser(fileChooser);
        if (file != null) {
            //On appelle la méthode du controleur devant charger la demande de livraison
            return file.getAbsolutePath();
        }else{
            return null;
        }
    }
    
    public void ajouterLivraison (ActionEvent e) {
        controleur.boutonAjouterLivraison();
    }
    
    public void supprimerLivraison (ActionEvent e) {
        controleur.boutonSupprimerLivraison();
    }
    
    public void recupererCoordonneesSouris(MouseEvent e) throws InterruptedException{
        double[] point = new double[2];
        point[0] = e.getX();;
        point[1] = e.getY();
        point = vueGraphique.mettreCoordonneesALechelle(point, true);
        controleur.clicGauche(point[1], point[0]);
    }
    
    public void boutonAnnuler(ActionEvent e){
        controleur.boutonAnnuler();
    }
    
    public void boutonValiderSelection(ActionEvent e) {
        controleur.boutonValiderSelection();
    }
    
    public void boutonRetour(ActionEvent e){
        controleur.boutonRetour();
    }
    public void scrollZoomPlus(ScrollEvent e){
        double[] point = new double[2];
        point[0] = e.getX();;
        point[1] = e.getY();
        point = vueGraphique.mettreCoordonneesALechelle(point, true);
        controleur.scrollZoomPlus(point[1], point[0]);
    }
    public void scrollZoomMoins(ScrollEvent e){
        double[] point = new double[2];
        point[0] = e.getX();;
        point[1] = e.getY();
        point = vueGraphique.mettreCoordonneesALechelle(point, true);
        controleur.scrollZoomMoins(point[1], point[0]);
    }

}
