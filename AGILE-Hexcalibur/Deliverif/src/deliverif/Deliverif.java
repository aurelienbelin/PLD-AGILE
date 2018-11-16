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
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.GestionLivraison;

/**
 * Classe principale/point d'entrée de l'application. Il s'agit de la fenetre principale de l'application.
 * @author Romain
 * @see Application
 */
public class Deliverif extends Application implements Observer{
    
    private static Stage stage;
    private static Scene scene;
   
    //Labels
    public final static String CHARGER_PLAN = "Charger un plan";
    public final static String CHARGER_DL = "Charger une demande de livraisons";
    public final static String AJOUTER_LIVRAISON = "Ajouter une livraison";
    public final static String SUPPRIMER_LIVRAISON = "Supprimer une livraison";
    public final static String REORGANISER_TOURNEE = "Réorganiser tournée";
    public final static String CALCULER_TOURNEES = "Calculer les tournées";
    public final static String VALIDER_MODIF = "Valider modifications";
    public final static String ZOOM_AVANT = "+";
    public final static String ARRETER_CALCUL_TOURNEES = "Stop";
    public final static String TITRE = "Deliver'IF";
    public final static String VALIDER_SELECTION = "Valider la sélection";
    public final static String RETOUR_MENU = "Retour au menu";
    public final static String RETOUR_SELECTION = "Retour à la sélection";
    public final static String AJOUTER = "Ajouter";
    public final static String LIVREURS = "Livreurs : ";
    public final static String DUREE = "Durée : ";
    public final static String OK = "Ok";
    public final static String SUCCES = "SUCCES";
    public final static String CALCUL_TERMINE = "";
    public final static String SYSTEM = "System";
    public final static String MESSAGE = "Message";
    public final static String SUPPRESSION_LIVRAISON = "Supprimer la livraison";
    public final static String PLAN_NON_CHARGE = "Le plan n'a pas pu être chargé.";
    public final static String FONT_INFORMATION = "Arial";
    public final static String STYLE_INFORMATION = "-fx-font-style:italic; -fx-font-weight:bold; -fx-text-fill:red;";
    public final static String CALCUL_NON_TERMINE = "Le calcul des tournées n'a pas pu se terminer.";
    public final static String DEMANDE_NON_CHARGEE = "La demande de livraison n'a pas pu être chargée.";
    
    //Constantes
    public final static int LARGEUR = 1024;
    public final static int LARGEUR_GRAPHIQUE = 115;
    public final static int HAUTEUR = 640;
    public final static int PADDING = 15;
    public final static int PADDING_PANEL_DROIT = 25;
    public final static int SPACING_PANEL_DROIT = 25;
    public final static int SPACING = 5;
    public final static int LARGEUR_BOUTON = 100;
    public final static int HAUTEUR_BOUTON = 65;
    
    private GestionLivraison gestionLivraison;
    private Ecouteur ecouteurBoutons;
    private Controleur controleur;
    private VueTextuelle vueTextuelle;
    private VueGraphique vueGraphique;
    
    //Conteneurs
    private BorderPane bord;
    private HBox boutonsActionsPrincipales;
    private VBox boxCalculTournees;
    private VBox boxAjoutLivraison;
    private VBox boxSuppressionLivraison;
    private VBox panelDroit;
    private HBox boutonsAjoutLivraison;
    private HBox boutonsSuppressionLivraison;
    private HBox boutonsReorgLivraison;
    
    //Composants de controle
    private Button boutonChargerPlan;
    private Button boutonChargerDL;
    private Button boutonAjouterLivraison;
    private Button boutonSupprimerLivraison;
    private Button boutonReorganiserTournee;
    private Button boutonCalculerTournees;
    private Button boutonArreterCalcul;
    private Button boutonRetourAuMenu;
    private Button boutonValiderSelection;
    private Button boutonValiderAjout;
    private Button boutonRetourSelection;
    private Button boutonValiderSuppression;
    private Button boutonAnnulerSuppression;
    private Button boutonValiderReorg;
    private Spinner nbLivreurs;
    private Spinner choixDuree;
    private Label descriptionTextuelle;
    private ComboBox choixTournee;
    private Label information;
    
    @Override
    public void init() throws Exception{
        super.init();
        gestionLivraison = new GestionLivraison();
        gestionLivraison.addObserver(this);
        controleur = new Controleur(gestionLivraison,this);
        vueGraphique = new VueGraphique(this.gestionLivraison, this);
        ecouteurBoutons = new Ecouteur(this, controleur, vueGraphique);
        gestionLivraison.addObserver(this);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
                
        Group root = new Group();
        scene = new Scene(root,LARGEUR,HAUTEUR);
        
        bord = new BorderPane();

        creerBoutonsActionsPrincipales();
        creerBoutonsActionsModifications();
        
        boutonsActionsPrincipales = templateBoiteBouton();
        boutonsActionsPrincipales.getChildren().addAll(boutonChargerPlan, boutonChargerDL, boutonAjouterLivraison,boutonSupprimerLivraison, boutonReorganiserTournee);        
        
        boutonsAjoutLivraison = templateBoiteBouton();
        
        boutonsSuppressionLivraison = templateBoiteBouton();
        boutonsSuppressionLivraison.getChildren().addAll(boutonAnnulerSuppression, boutonValiderSuppression);
        
        boutonsReorgLivraison = templateBoiteBouton();
        
        Separator sv = new Separator();
        sv.setOrientation(Orientation.VERTICAL);
        sv.setPrefHeight(scene.getHeight());
        sv.setLayoutX(HAUTEUR);
        
        creerPanelDroit();   
        
        vueGraphique.setLayoutX(0);
        vueGraphique.setLayoutY(LARGEUR_GRAPHIQUE);
        
        vueGraphique.setOnMouseClicked(m->{
            try {
                if(m.getButton().equals(MouseButton.PRIMARY))
                {
                    ecouteurBoutons.recupererCoordonneesSouris((MouseEvent) m);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(VueTextuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        vueGraphique.setOnScroll(m->{
            try{
                double delta = m.getDeltaY();
                if (delta >0){
                    ecouteurBoutons.scrollZoomPlus((ScrollEvent) m);
                }else if(delta<0){
                    ecouteurBoutons.scrollZoomMoins((ScrollEvent) m);
                }
            }catch (Exception ex) {
                Logger.getLogger(VueTextuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent key){
                ecouteurBoutons.actionClavier(key);
            }
        });
        
        bord.setTop(boutonsActionsPrincipales);
        
        root.getChildren().add(bord);
        root.getChildren().add(sv);
        root.getChildren().add(panelDroit);
        root.getChildren().add(vueGraphique);
        
        stage.setTitle(TITRE);
        stage.setResizable(false);
        stage.setScene(scene);
        this.stage = stage;
        stage.show();
        
        creerBoxAjoutLivraison();
    }
    
    private HBox templateBoiteBouton(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
        hbox.setSpacing(SPACING);
        return hbox;
    }
    
    private Button templateBoutonAction(String titre, Boolean isDisable){
        Button bouton = new Button(titre);
        bouton.setPrefSize(LARGEUR_BOUTON,HAUTEUR_BOUTON);
        bouton.setWrapText(true);
        bouton.setTextAlignment(TextAlignment.CENTER);
        bouton.setDisable(isDisable);
        return bouton;
    }
    /**
     * Création des boutons de la fenetre
     */
    private void creerBoutonsActionsPrincipales(){
        
        boutonChargerPlan = templateBoutonAction(CHARGER_PLAN, false);
        boutonChargerPlan.setOnAction(e->{
            try {
                ecouteurBoutons.chargerPlan(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            } 
        });
        
        boutonChargerDL = templateBoutonAction(CHARGER_DL, true);
        boutonChargerDL.setOnAction(e -> {
            try {
                ecouteurBoutons.chargerDemandeLivraison(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        boutonAjouterLivraison = templateBoutonAction(AJOUTER_LIVRAISON, true);
        boutonAjouterLivraison.setOnAction(e -> {
            try {
                ecouteurBoutons.ajouterLivraison(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        

        boutonSupprimerLivraison = templateBoutonAction(SUPPRIMER_LIVRAISON, true);
        boutonSupprimerLivraison.setOnAction(e -> {
            ecouteurBoutons.boutonSupprimer(e);
        });
        
        boutonReorganiserTournee = templateBoutonAction(REORGANISER_TOURNEE, true);
        
    }
    
    private void creerBoutonsActionsModifications(){
        boutonValiderSelection = templateBoutonAction(VALIDER_SELECTION, true);
        boutonSupprimerLivraison = new Button(SUPPRIMER_LIVRAISON);
        boutonSupprimerLivraison.setPrefSize(100,65);
        boutonSupprimerLivraison.setWrapText(true);
        boutonSupprimerLivraison.setDisable(true);
        boutonSupprimerLivraison.setTextAlignment(TextAlignment.CENTER);
        boutonSupprimerLivraison.setOnAction(e -> ecouteurBoutons.boutonSupprimer(e));
        
        boutonReorganiserTournee = new Button(REORGANISER_TOURNEE);
        boutonReorganiserTournee.setPrefSize(100,65);
        boutonReorganiserTournee.setWrapText(true);
        boutonReorganiserTournee.setDisable(true);
        boutonReorganiserTournee.setTextAlignment(TextAlignment.CENTER);
        boutonReorganiserTournee.setOnAction(e -> ecouteurBoutons.boutonReorgLivraisons(e));

        boutonValiderSelection = new Button("Valider la sélection");
        boutonValiderSelection.setPrefSize(100,65);
        boutonValiderSelection.setWrapText(true);
        boutonValiderSelection.setDisable(true);
        boutonValiderSelection.setTextAlignment(TextAlignment.CENTER);
        boutonValiderSelection.setTranslateX(400);
        boutonValiderSelection.setOnAction(e -> {
            try {
                ecouteurBoutons.boutonValiderSelection(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        

        boutonRetourAuMenu = templateBoutonAction(RETOUR_MENU, true);
        boutonRetourAuMenu.setOnAction(e -> {
            try {
                ecouteurBoutons.boutonRetourAuMenu(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        boutonRetourSelection = templateBoutonAction(RETOUR_SELECTION, false);
        boutonRetourSelection.setTranslateX(295);
        boutonRetourSelection.setVisible(false);
        boutonRetourSelection.setOnAction(e -> {
            try {
                ecouteurBoutons.boutonRetour(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        //A supprimer
        boutonValiderSuppression = new Button(SUPPRIMER_LIVRAISON);
        boutonValiderSuppression.setPrefSize(300,50);
        boutonValiderSuppression.setMinHeight(50);
        boutonValiderSuppression.setWrapText(true);
        boutonValiderSuppression.setDisable(true);
        boutonValiderSuppression.setTextAlignment(TextAlignment.CENTER);
        boutonValiderSuppression.setOnAction(e -> ecouteurBoutons.boutonSupprimerLivraison(e));
        
        boutonAnnulerSuppression = new Button(RETOUR_MENU);
        boutonAnnulerSuppression.setPrefSize(300,50);
        boutonAnnulerSuppression.setMinHeight(50);
        boutonAnnulerSuppression.setWrapText(true);
        boutonAnnulerSuppression.setDisable(true);
        boutonAnnulerSuppression.setTextAlignment(TextAlignment.CENTER);

        boutonAnnulerSuppression.setOnAction(e -> ecouteurBoutons.boutonRetourAuMenu(e)); 
      
        boutonValiderReorg = new Button(VALIDER_MODIF);
        boutonValiderReorg.setPrefSize(110,65);
        boutonValiderReorg.setMinHeight(50);
        boutonValiderReorg.setTranslateX(400);
        boutonValiderReorg.setWrapText(true);
        boutonValiderReorg.setDisable(false);
        boutonValiderReorg.setTextAlignment(TextAlignment.CENTER);
        boutonValiderReorg.setOnAction(e -> ecouteurBoutons.boutonValiderReorg(e));

    }
    
    /**
     * Création du panel droit de la fenetre principal.
     */
    private void creerPanelDroit() {
        panelDroit = new VBox();
        panelDroit.setPrefSize(LARGEUR-HAUTEUR,HAUTEUR);
        panelDroit.setLayoutX(HAUTEUR);
        panelDroit.setPadding(new Insets(PADDING_PANEL_DROIT, PADDING_PANEL_DROIT, PADDING_PANEL_DROIT, PADDING_PANEL_DROIT));
        panelDroit.setSpacing(SPACING_PANEL_DROIT);
        
        boxCalculTournees = new VBox();
        boxCalculTournees.setSpacing(15);
        
        HBox boxLivreurs = new HBox();
        boxLivreurs.setSpacing(25);
        
        Label livreurs = new Label(LIVREURS);
        livreurs.setFont(new Font(SYSTEM,20));
        
        nbLivreurs = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        nbLivreurs.setValueFactory(valueFactory);
        nbLivreurs.setPrefSize(60,25);
        boxLivreurs.getChildren().addAll(livreurs, nbLivreurs);
        HBox boxBoutons = new HBox();
        boxBoutons.setSpacing(15);
        
        boutonCalculerTournees = new Button(CALCULER_TOURNEES);
        boutonCalculerTournees.setPrefSize(LARGEUR-HAUTEUR-90-50,50);
        boutonCalculerTournees.setMinHeight(50);
        boutonCalculerTournees.setWrapText(true);
        boutonCalculerTournees.setDisable(true);
        boutonCalculerTournees.setTextAlignment(TextAlignment.CENTER);
        boutonCalculerTournees.setOnAction(e -> {
            try {
                ecouteurBoutons.calculerTournees(e);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        boutonArreterCalcul = new Button(ARRETER_CALCUL_TOURNEES);
        boutonArreterCalcul.setPrefSize(75,50);
        boutonArreterCalcul.setMinHeight(50);
        boutonArreterCalcul.setWrapText(true);
        boutonArreterCalcul.setDisable(true);
        boutonArreterCalcul.setTextAlignment(TextAlignment.CENTER);
        boutonArreterCalcul.setOnAction(e -> ecouteurBoutons.arreterCalculTournees());
        
        boxBoutons.getChildren().addAll(boutonCalculerTournees, boutonArreterCalcul);
        
        boxCalculTournees.getChildren().addAll(boxLivreurs, boxBoutons);
        
        Separator sh = new Separator();
        sh.setOrientation(Orientation.HORIZONTAL);
        sh.setPrefWidth(panelDroit.getWidth());
        sh.setMaxWidth(LARGEUR-HAUTEUR-50);
        sh.setLayoutX(HAUTEUR);
        
        vueTextuelle = new VueTextuelle(gestionLivraison, ecouteurBoutons);
        vueTextuelle.setMaxWidth(LARGEUR-HAUTEUR-50);
        
        this.information = new Label();
        this.information.setPrefSize(LARGEUR-HAUTEUR,30);
        this.information.setMinWidth(LARGEUR-HAUTEUR-25);
        this.information.setMaxHeight(30);
        this.information.setAlignment(Pos.CENTER_RIGHT);
        this.information.setFont(new Font(FONT_INFORMATION, 10));
        this.information.setStyle(STYLE_INFORMATION);
        //this.information.setText("Test");
        //this.information.setStyle("-fx-background-color:red;");
        
        //panelDroit.getChildren().addAll(boxLivreurs, boxBoutons, sh, vueTextuelle, information);

        panelDroit.getChildren().addAll(boxCalculTournees, sh, vueTextuelle, information);
    }
    
    protected void creerBoxAjoutLivraison(){
        boxAjoutLivraison = new VBox();
        boxAjoutLivraison.setSpacing(15);
        
        HBox boxDuree = new HBox();
        boxDuree.setSpacing(25);
        
        Label duree = new Label(DUREE);
        duree.setFont(new Font(SYSTEM,20));
        
        choixDuree = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        choixDuree.setValueFactory(valueFactory);
        choixDuree.setPrefSize(60,25);
        
        boxDuree.getChildren().addAll(duree, choixDuree);
        
        boutonValiderAjout = new Button(AJOUTER);
        boutonValiderAjout.setPrefSize(300,50);
        boutonValiderAjout.setMinHeight(50);
        boutonValiderAjout.setWrapText(true);
        boutonValiderAjout.setDisable(true);
        boutonValiderAjout.setTextAlignment(TextAlignment.CENTER);
        boutonValiderAjout.setOnAction(e -> ecouteurBoutons.boutonValiderAjout(e));
        
        boxAjoutLivraison.getChildren().addAll(boxDuree, boutonValiderAjout);
    }
    
    @Override
    public void update(Observable o, Object arg){
        if (o instanceof GestionLivraison){
            if (arg instanceof modele.Tournee[]){
                if (!((GestionLivraison)o).calculTSPEnCours()){
                    
                    this.informationEnCours(CALCUL_TERMINE);
                    /*On appelle la methode bouton stop, cela marchera puisque
                    le calcul est fini !*/
                    try{
                        this.controleur.boutonArretCalcul();
                    } catch(IllegalStateException ise){
                        //On s'en fiche que ça ne soit pas sur le thread fx.
                        //Ça marche quand même !
                    }
                }
            }
        }
    }
    
    /**
     * Affiche en bas à droite l'action en cours dans l'application.
     * @param message - message à afficher (état courant de l'application)
     */
    protected void informationEnCours(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                information.setText(message);
            }
        });
    }
    
    /**
     * Renvoie le nombre de livreurs.
     * @return le nombre de livreurs
     */
    public int getNbLivreurs(){
        return (Integer)this.nbLivreurs.getValue();
    }
    
    /**
     * Renvoie la durée de la livraison à ajouter.
     * @return le nombre de livreurs
     */
    public int getDuree(){
        return (int)this.choixDuree.getValue();
    }
    
    /**
     * Renvoie la VueTextuelle associée à cette fenetre principale.
     * @return la Vuetextuelle
     */
    public VueTextuelle getVueTextuelle(){
        return this.vueTextuelle;
    }
    
    /**
     * Renvoie la VueGraphiqueassociée à cette fenetre principale.
     * @return la VueGraphique
     */
    public VueGraphique getVueGraphique(){
        return this.vueGraphique;
    }
    
    /**
     * Ouvre la fenetre du gestionnaire de fichier pour choix de fichier à ouvrir.
     * @param fileChooser - l'objet gestionnaire de fichier à ouvrir
     * @return le fichier à ouvrir
     * @throws InterruptedException
     */
    public static File ouvrirFileChooser(FileChooser fileChooser) throws InterruptedException {
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
    
    /**
     * Créer un Pop-Up pour avertir l'utilisateur.
     * @param message - message à afficher dans le pop-up
     */
    public void avertir(String message){
        this.creerMessagePopup(message);
    }
    
    /**
     * Construit une fenetre de pop-up.
     * @param message - message à afficher dans le pop-up
     */
    private void creerMessagePopup(String message) {
        
        Label mess = new Label(message);
        mess.setPadding(new Insets(15));
        mess.setWrapText(true);
        mess.setTextAlignment(TextAlignment.CENTER);
        mess.setAlignment(Pos.CENTER);
        Button boutonRetour = new Button(OK);

        VBox vbox = new VBox(mess, boutonRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
                        + "-fx-border-width : 3;");
        
        Scene secondeScene = new Scene(vbox, 230, 175);
 
        // New window (Stage)
        Stage popUp = new Stage();
        popUp.setTitle(MESSAGE);
        popUp.setScene(secondeScene);
        
        boutonRetour.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                        popUp.close();
                }
        });

        // Specifies the modality for new window.
        popUp.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        popUp.initOwner(this.stage);

        // Set position of second window, related to primary window.
        popUp.setX(stage.getX() + stage.getWidth()/2 - 115);
        popUp.setY(stage.getY() + stage.getHeight()/2 - 65);

        popUp.show();
    }

    /**n
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void estPointPassageSelectionne(double latitude, double longitude) {
        getVueGraphique().effacerMarqueur();
        getVueGraphique().ajouterMarqueur(latitude, longitude);
    }
    
    public void estPointPassageASupprimerSelectionne(double latitude, double longitude){
        vueGraphique.effacerMarqueurModif();
        vueGraphique.ajouterMarqueurModif(latitude, longitude);
    }
    
    public void estSelectionne(int tournee, int position){
        DescriptifLivraison dc = getVueTextuelle().getDescriptifChemin(tournee, position);
        getVueTextuelle().majVueTextuelle(dc);
        getVueTextuelle().changerDescription_Ter(tournee);
    }
    
    /**
     * Passe l'IHM dans l'état suivant une fois le plan chargé.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estPlanCharge(String cre) {
        informationEnCours("");
        if((SUCCES).equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(true);
            boutonAjouterLivraison.setDisable(true);
            boutonSupprimerLivraison.setDisable(true);
            boutonReorganiserTournee.setDisable(true);
            vueTextuelle.effacer();
        }else if(cre!=null){
            avertir(cre);
        }else{
            avertir(PLAN_NON_CHARGE);
        }
    }
    
    /**
     * Passe l'IHM dans l'état suivant une fois la demande de livraison chargée.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estDemandeLivraisonChargee(String cre){
        this.informationEnCours("");
        if((SUCCES).equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(false);
            boutonAjouterLivraison.setDisable(true);
            boutonSupprimerLivraison.setDisable(true);
            boutonReorganiserTournee.setDisable(true);
        }else if(cre!=null){
            avertir(cre);
        }else{
            avertir(DEMANDE_NON_CHARGEE);
        }
    }
    
    /**
     * Passe l'IHM dans l'état suivant une fois les tournées calculées.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estTourneesCalculees(String cre){
        informationEnCours("");
        if((SUCCES).equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(false);
            boutonAjouterLivraison.setDisable(false);
            boutonSupprimerLivraison.setDisable(false);
            boutonReorganiserTournee.setDisable(false);
        }else{
            avertir(CALCUL_NON_TERMINE);
        }
    }
    
    public void estPlanCliquable(){
        this.vueTextuelle.majVueTextuelle(null);
        this.vueGraphique.effacerMarqueur();
        boutonsAjoutLivraison.getChildren().clear();
        boutonsAjoutLivraison.getChildren().addAll(boutonRetourAuMenu, boutonValiderSelection, boutonRetourSelection);
        bord.setTop(boutonsAjoutLivraison);
        panelDroit.getChildren().remove(boxCalculTournees);
        panelDroit.getChildren().add(0, boxAjoutLivraison);
        boxAjoutLivraison.setDisable(true);
        
        boutonValiderSelection.setVisible(true);
        boutonRetourSelection.setVisible(false);
        
        boutonRetourAuMenu.setDisable(false);
        boutonValiderSelection.setDisable(true);
    }
    
    public void estIntersectionSelectionnee(double latitude,double longitude){
        boutonValiderSelection.setDisable(false);
        vueGraphique.effacerMarqueurModif();
        vueGraphique.ajouterMarqueurModif(latitude, longitude);
    }
    
    public void estIntersectionValidee(){
        boxAjoutLivraison.setDisable(false);
        boutonValiderSelection.setVisible(false);
        boutonRetourSelection.setVisible(true);
        this.getVueTextuelle().ajouterBoutonAjout();
    }
    
    public void estRetourSelection(){
        boutonValiderSelection.setVisible(true);
        boutonRetourSelection.setVisible(false);
        boxAjoutLivraison.setDisable(true);
        vueTextuelle.supprimerBoutonAjout();   
        boutonValiderSelection.setDisable(false);
    }
    
    public void estPlusClique(int indexPlus, int indexTournee){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boutonValiderAjout.setDisable(false);
                //vueTextuelle.entourerPlusClique(indexPlus, indexTournee);
                vueTextuelle.ajouterBoutonAjout();
                vueTextuelle.desactiverPlus(indexPlus, indexTournee);
                vueTextuelle.changerDescription_Ter(indexTournee);
            }
        });
    }
    
    public void changePlusClique(int indexPlusPreced,int indexTourneePreced, int indexPlus, int indexTournee){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //vueTextuelle.entourerPlusClique(indexPlus-2, indexTournee);
                vueTextuelle.ajouterBoutonAjout();
                vueTextuelle.desactiverPlus(indexPlus, indexTournee);
                vueTextuelle.changerDescription_Ter(indexTournee);
                //vueTextuelle.changerPlusEntoure(indexPlusPreced, indexTourneePreced, indexPlus, indexTournee);
            }
        });
    }
    
    public void estAjoutLivraisonFini(boolean supprimerBoutons, int indexTournee, int indexPlus){
        bord.setTop(boutonsActionsPrincipales);
        
        panelDroit.getChildren().remove(boxAjoutLivraison);
        panelDroit.getChildren().add(0, boxCalculTournees);
        
        if(supprimerBoutons){
            vueTextuelle.supprimerBoutonAjout();
        }
        
        vueGraphique.effacerMarqueurModif();
        
        /*if(indexTournee!=-1)
            vueTextuelle.changerPlusEntoure(indexPlus, indexTournee);*/
        
        estTourneesCalculees(SUCCES);
    }
    
    public void activerBoutonArreterCalcul(boolean activation){
        this.boutonArreterCalcul.setDisable(activation);
        this.boutonCalculerTournees.setDisable(!activation);
    }

    public void estSuppressionFinie(){
        bord.setTop(boutonsActionsPrincipales);
        
        boxCalculTournees.setDisable(false);
        
        vueGraphique.effacerMarqueurModif();
        
        estTourneesCalculees(SUCCES);
    }
      
    public void estReorgTourneesDemandee(){
        boutonsReorgLivraison.getChildren().clear();
        boutonsReorgLivraison.getChildren().addAll(boutonRetourAuMenu, boutonValiderReorg);
        bord.setTop(boutonsReorgLivraison);
        //panelDroit.getChildren().remove(boxCalculTournees);
        boxCalculTournees.setDisable(true);
        vueTextuelle.ajouterMenuChangerTournee();
        vueTextuelle.ajouterBoutonsReorg();
        vueTextuelle.changerDescription_Ter(0);
    }
    
    public void changerVueTextuelle(int indexTournee){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                vueTextuelle.ajouterBoutonsReorg();
                vueTextuelle.ajouterMenuChangerTournee();
                vueTextuelle.changerDescription_Ter(indexTournee);
            }
        });
    }
    
    public void estReorgFinie(){
        bord.setTop(boutonsActionsPrincipales);
        boxCalculTournees.setDisable(false);
        vueTextuelle.remettreBoutonsDetails();
        
    }

    public void ajouterSuppression(){
        this.vueTextuelle.majVueTextuelle(null);
        this.vueGraphique.effacerMarqueur();
        
        bord.setTop(boutonsSuppressionLivraison);
        boutonValiderSuppression.setDisable(false);
        boutonAnnulerSuppression.setDisable(false);
        
        boxCalculTournees.setDisable(true);
    }
}