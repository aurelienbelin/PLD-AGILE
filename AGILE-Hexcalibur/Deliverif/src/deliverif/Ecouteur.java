/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

/**
 * Ecouteur des évènements émis par l'IHM de l'application.
 * @author Aurelien Belin
 * @see Deliverif
 * @see Controleur
 */
public class Ecouteur{
    
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
    public Ecouteur(Deliverif f, Controleur c, VueGraphique v){
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
    public void chargerPlan(ActionEvent e) throws InterruptedException, IOException, Exception{
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
    public void chargerDemandeLivraison(ActionEvent e) throws InterruptedException, IOException, Exception{
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
    public void calculerTournees(ActionEvent e) throws InterruptedException{
        this.fenetrePrincipale.informationEnCours("Calcul des tournées en cours...");
        this.fenetrePrincipale.getVueGraphique().creerCalques(fenetrePrincipale.getNbLivreurs());
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
     * Envoie une requête au controleur pour connaitre la localisation du point de livraison associé au composant DescriptifLivraison.
     * @param dc 
     */
    public void localiserPointVueGraphique(DescriptifLivraison dc){
        controleur.afficherMarqueur(dc);
    }
    
    /**
     * Devellope les détails du DescriptifLivraison passé en paramètre.
     * @param dc 
     */
    public void obtenirDetailsVueTextuelle(DescriptifLivraison dc){
        dc.developperDetails();
    }
    /**
     * Remonte la livraison dans la liste
     * @param dc 
     */
     public void avancerLivraison(DescriptifLivraison dc){
        String[] identifiants = dc.getPoint().split("_");
        
        int indexTournee = Integer.parseInt(identifiants[0]);
        int indexLivraison = Integer.parseInt(identifiants[1]);
        controleur.clicFleche(true, indexLivraison-1, indexTournee-1); //DESCRIPTIF
    }
     /**
      * Descend la livraison dans la liste
      * @param dc 
      */
    public void reculerLivraison(DescriptifLivraison dc){
        String[] identifiants = dc.getPoint().split("_");
        
        int indexTournee = Integer.parseInt(identifiants[0]);
        int indexLivraison = Integer.parseInt(identifiants[1]);
        controleur.clicFleche(false, indexLivraison-1, indexTournee-1); //DESCRIPTIF
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
        file = Deliverif.ouvrirFileChooser(fileChooser);
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
    
    public void boutonRetourAuMenu(ActionEvent e){
        controleur.boutonAnnuler();
    }
    
    public void boutonValiderSelection(ActionEvent e) {
        controleur.boutonValiderSelection();
    }
    
    public void boutonRetour(ActionEvent e){
        controleur.boutonRetour();
    }
    
    public void clicPlus (ActionEvent e, int indexPlus, int indexTournee){
        controleur.clicPlus(indexPlus, indexTournee);
    }
    
    public void boutonValiderAjout(ActionEvent e){
        float duree = fenetrePrincipale.getDuree();
        controleur.boutonValiderAjout(duree);
    }
    
    public void boutonSupprimerLivraison(ActionEvent e){
        controleur.boutonValiderSupprimerLivraison();
    }
    
    public void boutonSupprimer(ActionEvent e){
        controleur.boutonSupprimerLivraison();
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
    
    public void actionClavier(KeyEvent e){
        if (e.getCode()==KeyCode.Z){
            this.controleur.undo();
        } else if(e.getCode()==KeyCode.Y){
            this.controleur.redo();
        }
    }
    
   public void boutonReorgLivraisons(ActionEvent e){
       controleur.boutonReorgTournees();
   }
   
   public void boutonValiderReorg(ActionEvent e){
       controleur.validerReorganisation();
   }
   
    public void montrerMenuContextuel(ContextMenuEvent event, ContextMenu choixLivreurs, DescriptifLivraison livraisonCliquee){
        controleur.clicDroit(livraisonCliquee);
        
        String[] identifiants = livraisonCliquee.getPoint().split("_");
        int indiceTournee = Integer.parseInt(identifiants[0])-1;
        for(MenuItem livreur: choixLivreurs.getItems()){
            livreur.setVisible(true);
        }
        
        choixLivreurs.getItems().get(indiceTournee).setVisible(false);
        choixLivreurs.show(livraisonCliquee, event.getScreenX(), event.getScreenY());
    }
    
    public void reorgSelectionTournee(ActionEvent e, int indexTourneeChoisi) {
        controleur.selectionMenuChangerTournee(indexTourneeChoisi);
    }

}
