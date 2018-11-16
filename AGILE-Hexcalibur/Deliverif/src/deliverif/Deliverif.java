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
 * @author Hex'Calibur
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
    public final static int SPACING_BOX_PANEL_DROIT = 15;
    public final static int SPACING = 5;
    public final static int LARGEUR_BOUTON = 100;
    public final static int HAUTEUR_BOUTON = 65;
    public final static int LARGEUR_BOUTON_STOP = 75;
    public final static int HAUTEUR_BOUTON_STOP = 50;
    public final static int SIZE_FONT_LIVREURS = 20;
    public final static int SPINNER_LARGEUR = 60;
    public final static int SPINNER_HAUTEUR = 25;
    
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
    private Button boutonSuppressionRetourAuMenu;
    private Button boutonValiderSelection;
    private Button boutonValiderAjout;
    private Button boutonRetourSelection;
    private Button boutonValiderSuppression;
    private Button boutonValiderReorg;
    private Spinner nbLivreurs;
    private Spinner choixDuree;
    private Label descriptionTextuelle;
    private ComboBox choixTournee;
    private Label information;
    
    /**
     * Initialise les objets utilisés par la classe.
     * @throws Exception 
     */
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
    
    /**
     * Création de l'IHM.
     * @param stage - l'objet dans lequel est défini l'IHM
     * @throws Exception 
     */
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
        boutonsSuppressionLivraison.getChildren().addAll(boutonSuppressionRetourAuMenu, boutonValiderSuppression);
        
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
    
    /**
     * Template utilisé pour les HBox dans lequel sont stockés les boutons correspondant au même contexte d'exécution.
     * @return  
     */
    private HBox templateBoiteBouton(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
        hbox.setSpacing(SPACING);
        return hbox;
    }
    
    /**
     * Template utilisé pour créer les boutons des actions les plus fréquentes.
     * @param titre - texte inscrit dans le bouton
     * @param isDisable - true si le bouton est activé
     * @return 
     */
    private Button templateBoutonAction(String titre, Boolean isDisable){
        Button bouton = templateBouton(titre, isDisable, LARGEUR_BOUTON, HAUTEUR_BOUTON);
        return bouton;
    }
    
    /**
     * Template utilisé pour créer des boutons ayant les mêmes propriétés.
     * @param titre - texte inscrit dans le bouton
     * @param isDisable - true si le bouton est activé
     * @param hauteur - hauteur du bouton
     * @param largeur - largeur du bouton
     * @return 
     */
    private Button templateBouton(String titre, Boolean isDisable, int hauteur, int largeur){
        Button bouton = new Button(titre);
        bouton.setPrefSize(hauteur, largeur);
        bouton.setWrapText(true);
        bouton.setTextAlignment(TextAlignment.CENTER);
        bouton.setDisable(isDisable);
        return bouton;
    }
    
    /**
     * Création des boutons correspondant aux actions principales (chargement et modification).
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
        boutonReorganiserTournee.setOnAction(e -> {
            ecouteurBoutons.boutonReorgLivraisons(e);
        });
    }
    
    /**
     * Création des boutons correspondant aux actions de modifications.
     */
    private void creerBoutonsActionsModifications(){
        boutonValiderSelection = templateBoutonAction(VALIDER_SELECTION, true);
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
        
        boutonSuppressionRetourAuMenu = templateBoutonAction(RETOUR_MENU, true);
        boutonSuppressionRetourAuMenu.setOnAction(e -> {
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
        
        boutonValiderSuppression = templateBoutonAction(VALIDER_SELECTION, true);
        boutonValiderSuppression.setTranslateX(400);
        boutonValiderSuppression.setOnAction(e -> ecouteurBoutons.boutonSupprimerLivraison(e));
      
        boutonValiderReorg = templateBoutonAction(VALIDER_MODIF, false);
        boutonValiderReorg.setTranslateX(400);
        boutonValiderReorg.setOnAction(e -> ecouteurBoutons.boutonValiderReorg(e));
    }
    
    /**
     * Création du panneau droit de la fenetre principale. Utilisé pour les actions concernant le calcul des tournées et la validation de l'ajout.
     */
    private void creerPanelDroit() {
        panelDroit = new VBox();
        panelDroit.setPrefSize(LARGEUR-HAUTEUR,HAUTEUR);
        panelDroit.setLayoutX(HAUTEUR);
        panelDroit.setPadding(new Insets(PADDING_PANEL_DROIT, PADDING_PANEL_DROIT, PADDING_PANEL_DROIT, PADDING_PANEL_DROIT));
        panelDroit.setSpacing(SPACING_PANEL_DROIT);
        
        boxCalculTournees = new VBox();
        boxCalculTournees.setSpacing(SPACING_BOX_PANEL_DROIT);
        
        HBox boxLivreurs = new HBox();
        boxLivreurs.setSpacing(SPACING_PANEL_DROIT);
        
        Label livreurs = new Label(LIVREURS);
        livreurs.setFont(new Font(SYSTEM,SIZE_FONT_LIVREURS));
        
        nbLivreurs = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        nbLivreurs.setValueFactory(valueFactory);
        nbLivreurs.setPrefSize(SPINNER_LARGEUR,SPINNER_HAUTEUR);
        boxLivreurs.getChildren().addAll(livreurs, nbLivreurs);
        HBox boxBoutons = new HBox();
        boxBoutons.setSpacing(SPACING_BOX_PANEL_DROIT);
        
        boutonCalculerTournees = templateBouton(CALCULER_TOURNEES, true, LARGEUR-HAUTEUR-90-50,50);
        boutonCalculerTournees.setOnAction(e -> {
            try {
                ecouteurBoutons.calculerTournees(e);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        boutonArreterCalcul = templateBouton(ARRETER_CALCUL_TOURNEES, true, LARGEUR_BOUTON_STOP, HAUTEUR_BOUTON_STOP);
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
        panelDroit.getChildren().addAll(boxCalculTournees, sh, vueTextuelle, information);
    }
    
    /**
     * Boite utilisé lors de l'ajout.
     */
    protected void creerBoxAjoutLivraison(){
        boxAjoutLivraison = new VBox();
        boxAjoutLivraison.setSpacing(SPACING_BOX_PANEL_DROIT);
        
        HBox boxDuree = new HBox();
        boxDuree.setSpacing(SPACING_PANEL_DROIT);
        
        Label duree = new Label(DUREE);
        duree.setFont(new Font(SYSTEM,20));
        
        choixDuree = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        choixDuree.setValueFactory(valueFactory);
        choixDuree.setPrefSize(SPINNER_LARGEUR,SPINNER_HAUTEUR);
        
        boxDuree.getChildren().addAll(duree, choixDuree);
        
        boutonValiderAjout = templateBouton(AJOUTER, true, 300, 50);
        boutonValiderAjout.setOnAction(e -> ecouteurBoutons.boutonValiderAjout(e));
        
        boxAjoutLivraison.getChildren().addAll(boxDuree, boutonValiderAjout);
    }
    
    /**
     * Méthode appelé par le pattern Observeur pour mettre à jour l'IHM.
     * @param o - observable
     * @param arg - argument
     */
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

    /**
     * Main de l'application
     * @param args - les arguments 
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Gère l'affichage des marqueurs lorsqu'un point de passage est sélectionné.
     * @param latitude - latitude du point de passage
     * @param longitude - longitude du point de passage
     */
    public void estPointPassageSelectionne(double latitude, double longitude) {
        getVueGraphique().effacerMarqueur();
        getVueGraphique().ajouterMarqueur(latitude, longitude);
    }
    
    /**
     * Gère l'affichage lorsqu'un point de passage est sélectionné dans le cas de la suppression.
     * @param latitude - latitude du point de passage
     * @param longitude - longitude du point de passage
     */
    public void estPointPassageASupprimerSelectionne(double latitude, double longitude){
        vueGraphique.effacerMarqueurModif();
        vueGraphique.ajouterMarqueurModif(latitude, longitude);
    }
    
    /**
     * Gère la liaison entre le point de passage sélectionné sur la vue graphique et la vue textuelle.
     * @param tournee - la tournée du point sélectionné
     * @param position - la position du point sélectionné dans la tournée
     */
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
    
    /**
     * Gère l'IHM lorsque l'on entre dans un contexte d'ajout de livraison.
     */
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
    
    /**
     * Gère l'affichage des marqueurs lors d'une action de modification.
     * @param latitude - latitude de l'intersection sélectionnée
     * @param longitude  - longitude de l'intersection sélectionnée
     */
    public void estIntersectionSelectionnee(double latitude,double longitude){
        boutonValiderSelection.setDisable(false);
        vueGraphique.effacerMarqueurModif();
        vueGraphique.ajouterMarqueurModif(latitude, longitude);
    }
    
    /**
     * Gère l'affichage lorsque le point d'ajout de la livraison est choisi.
     */
    public void estIntersectionValidee(){
        boxAjoutLivraison.setDisable(false);
        boutonValiderSelection.setVisible(false);
        boutonRetourSelection.setVisible(true);
        this.getVueTextuelle().ajouterBoutonAjout();
    }
    
    /**
     * Permets de retourner dans le contexte où l'on peut choisir une intersection pour ajouter une livraison.
     */
    public void estRetourSelection(){
        boutonValiderSelection.setVisible(true);
        boutonRetourSelection.setVisible(false);
        boxAjoutLivraison.setDisable(true);
        vueTextuelle.supprimerBoutonAjout();   
        boutonValiderSelection.setDisable(false);
    }
    
    /**
     * Gère l'affichage de la vue textuelle lorsqu'une boîte plus est cliquée
     * @param indexPlus - index de la boîte plus cliquée
     * @param indexTournee  - index de la tournée à laquelle appartient cette boîte plus
     */
    public void estPlusClique(int indexPlus, int indexTournee){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boutonValiderAjout.setDisable(false);
                vueTextuelle.ajouterBoutonAjout();
                vueTextuelle.desactiverPlus(indexPlus, indexTournee);
                vueTextuelle.changerDescription_Ter(indexTournee);
            }
        });
    }
    
    /**
     * Mets à jour la vue textuelle lorsqu'on clique sur une autre boîte plus.
     * @param indexPlusPreced - index boîte plus précedemment sélectionnée
     * @param indexTourneePreced - index tournée à laquelle appartenait cette boîte plus
     * @param indexPlus - index boîte plus cliquée
     * @param indexTournee  - index tournée à laquelle appartient cette boîte plus
     */
    public void changePlusClique(int indexPlusPreced,int indexTourneePreced, int indexPlus, int indexTournee){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vueTextuelle.ajouterBoutonAjout();
                vueTextuelle.desactiverPlus(indexPlus, indexTournee);
                vueTextuelle.changerDescription_Ter(indexTournee);    }
        });
    }
    
    /**
     * Mets à jour l'affichage 
     * @param supprimerBoutons
     * @param indexTournee
     * @param indexPlus 
     */
    public void estAjoutLivraisonFini(boolean supprimerBoutons, int indexTournee, int indexPlus){
        bord.setTop(boutonsActionsPrincipales);
        
        panelDroit.getChildren().remove(boxAjoutLivraison);
        panelDroit.getChildren().add(0, boxCalculTournees);
        
        if(supprimerBoutons){
            vueTextuelle.supprimerBoutonAjout();
        }
        
        vueGraphique.effacerMarqueurModif();
        
        estTourneesCalculees(SUCCES);
    }
    
    /**
     * Bouton permettant de stopper le calcul losrqu'il est trop long
     * @param activation - si true, active l'arrêt du calcul
     */
    public void activerBoutonArreterCalcul(boolean activation){
        this.boutonArreterCalcul.setDisable(activation);
        this.boutonCalculerTournees.setDisable(!activation);
    }

    /**
     * Gère l'affichage des marqueurs losrque la suppression est terminée.
     */
    public void estSuppressionFinie(){
        bord.setTop(boutonsActionsPrincipales);
        
        boxCalculTournees.setDisable(false);
        
        vueGraphique.effacerMarqueurModif();
        
        estTourneesCalculees(SUCCES);
    }
      
    /**
     * Mets à jour la réorganisation des tournées lorsqu'on entre dans le contexte de réorganisation des tournées.
     */
    public void estReorgTourneesDemandee(){
        boutonsReorgLivraison.getChildren().clear();
        boutonsReorgLivraison.getChildren().addAll(boutonRetourAuMenu, boutonValiderReorg);
        bord.setTop(boutonsReorgLivraison);
        boxCalculTournees.setDisable(true);
        vueTextuelle.ajouterMenuChangerTournee();
        vueTextuelle.ajouterBoutonsReorg();
        vueTextuelle.changerDescription_Ter(0);
    }
    
    /**
     * Mets à jour l'affichage de la vue textuelle lorsqu'on change de tournée à visualiser.
     * @param indexTournee 
     */
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
    
    /**
     * Gère l'affichage de l'IHM lorsque la réorganisation est finie
     */
    public void estReorgFinie(){
        bord.setTop(boutonsActionsPrincipales);
        boxCalculTournees.setDisable(false);
        vueTextuelle.remettreBoutonsDetails();
        
    }

    /**
     * Gère l'affichage de l'IHM lorsqu'on entre dans le contexte de suppresion de livraison.
     */
    public void estSupprimerLivraisonDemande(){
        this.vueTextuelle.majVueTextuelle(null);
        this.vueGraphique.effacerMarqueur();
        bord.setTop(boutonsSuppressionLivraison);
        boutonValiderSuppression.setDisable(false);
        boutonSuppressionRetourAuMenu.setDisable(false);
        boxCalculTournees.setDisable(true);
    }
}