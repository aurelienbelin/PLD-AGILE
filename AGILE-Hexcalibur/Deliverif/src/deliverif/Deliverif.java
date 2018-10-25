/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import modele.outils.GestionLivraison;

/**
 *
 * @author romain
 */
public class Deliverif extends Application {
    
    private static Stage stage;
    private static Scene scene;
    
    //Commande
    public final static String CHARGER_PLAN = "Charger un plan";
    public final static String CHARGER_DL = "Charger une demande de livraison";
    public final static String AJOUTER_LIVRAISON = "Ajouter une livraison";
    public final static String SUPPRIMER_LIVRAISON = "Supprimer une livraison";
    public final static String REORGANISER_TOURNEE = "Réorganiser tournée";
    public final static String CALCULER_TOURNEES = "Calculer les tournées";
    
    //private Controleur controleur;
    private GestionLivraison gestionLivraison;
    private EcouteurBoutons ecouteurBoutons;
    
    private VueTextuelle vueTextuelle;
    private VueGraphique vueGraphique;
    
    //Conteneurs
    private BorderPane bord;
    private HBox boutons;
    private VBox panelDroit;
    
    //Composants de controle
    private Button boutonChargerPlan;
    private Button boutonChargerDL;
    private Button boutonAjouterLivraison;
    private Button boutonSupprimerLivraison;
    private Button boutonReorganiserTournee;
    private Button boutonCalculerTournees;
    private Spinner nbLivreurs;
    private Label descriptionTextuelle;
    private ComboBox choixTournee;
    
    @Override
    public void init() throws Exception{
        super.init();
        gestionLivraison = new GestionLivraison();
        ecouteurBoutons = new EcouteurBoutons(this);
        
        //new Controleur()s
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Group root = new Group();
        scene = new Scene(root,1024,640);
        
        bord = new BorderPane();
        
        boutons = new HBox();
        boutons.setPadding(new Insets(25, 25, 25, 25));
        boutons.setSpacing(5);
        
        creerBoutonsChargement();
        
        boutons.getChildren().addAll(boutonChargerPlan, boutonChargerDL, boutonAjouterLivraison,boutonSupprimerLivraison, boutonReorganiserTournee);
        
        Separator sv = new Separator();
        sv.setOrientation(Orientation.VERTICAL);
        sv.setPrefHeight(scene.getHeight());
        sv.setLayoutX(640);
        
        creerPanelDroit();   
        //Il faudra ajouter la vue graphique
        
        bord.setTop(boutons);
        
        root.getChildren().add(bord);
        root.getChildren().add(sv);
        root.getChildren().add(panelDroit);
        
        stage.setTitle("Deliver'IF");
        stage.setResizable(false);
        stage.setScene(scene);
        this.stage = stage;
        stage.show();
    }
    
    private void creerBoutonsChargement(){
        //A factoriser
        boutonChargerPlan = new Button(CHARGER_PLAN);
        boutonChargerPlan.setPrefSize(80,65);
        boutonChargerPlan.setWrapText(true);
        boutonChargerPlan.setTextAlignment(TextAlignment.CENTER);
        boutonChargerPlan.setOnAction(e->{
            try {
                ecouteurBoutons.chargerPlanAction(e);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        boutonChargerDL = new Button(CHARGER_DL);
        boutonChargerDL.setPrefSize(80,65);
        boutonChargerDL.setWrapText(true);
        boutonChargerDL.setTextAlignment(TextAlignment.CENTER);
        boutonChargerDL.setOnAction(e -> {
            try {
                ecouteurBoutons.chargerDemandeLivraisonAction(e);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        boutonAjouterLivraison = new Button(AJOUTER_LIVRAISON);
        boutonAjouterLivraison.setPrefSize(80,65);
        boutonAjouterLivraison.setWrapText(true);
        boutonAjouterLivraison.setDisable(true);
        boutonAjouterLivraison.setTextAlignment(TextAlignment.CENTER);
        
        boutonSupprimerLivraison = new Button(SUPPRIMER_LIVRAISON);
        boutonSupprimerLivraison.setPrefSize(80,65);
        boutonSupprimerLivraison.setWrapText(true);
        boutonSupprimerLivraison.setDisable(true);
        boutonSupprimerLivraison.setTextAlignment(TextAlignment.CENTER);
        
        boutonReorganiserTournee = new Button(REORGANISER_TOURNEE);
        boutonReorganiserTournee.setPrefSize(80,65);
        boutonReorganiserTournee.setWrapText(true);
        boutonReorganiserTournee.setDisable(true);
        boutonReorganiserTournee.setTextAlignment(TextAlignment.CENTER);
    }
    
    private void creerPanelDroit() {
        panelDroit = new VBox();
        panelDroit.setPrefSize(1024-640,640);
        panelDroit.setLayoutX(640);
        panelDroit.setPadding(new Insets(25, 25, 25, 25));
        panelDroit.setSpacing(25);
        
        HBox boxLivreurs = new HBox();
        boxLivreurs.setSpacing(25);
        
        Label livreurs = new Label("Livreurs : ");
        livreurs.setFont(new Font("System",20));
        
        nbLivreurs = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        nbLivreurs.setValueFactory(valueFactory);
        nbLivreurs.setPrefSize(60,25);
        
        boxLivreurs.getChildren().addAll(livreurs, nbLivreurs);
        
        boutonCalculerTournees = new Button(CALCULER_TOURNEES);
        boutonCalculerTournees.setPrefSize(300,50);
        boutonCalculerTournees.setWrapText(true);
        boutonCalculerTournees.setDisable(true);
        boutonCalculerTournees.setTextAlignment(TextAlignment.CENTER);
        boutonCalculerTournees.setOnAction(e -> {
            try {
                ecouteurBoutons.calculerTourneesAction(e);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Separator sh = new Separator();
        sh.setOrientation(Orientation.HORIZONTAL);
        sh.setPrefWidth(panelDroit.getWidth());
        sh.setLayoutX(640);
        
        vueTextuelle = new VueTextuelle(gestionLivraison);
        vueTextuelle.ajouterEcouteur(ecouteurBoutons);
        
        panelDroit.getChildren().addAll(boxLivreurs, boutonCalculerTournees, sh, vueTextuelle);
        
    }
    
    public int getNbLivreurs(){
        return (Integer)this.nbLivreurs.getValue();
    }
    
    public VueTextuelle getVueTextuelle(){
        return this.vueTextuelle;
    }
    
    public VueGraphique getVueGraphique(){
        return this.vueGraphique;
    }
    
    public static File openFileChooser(FileChooser fileChooser) throws InterruptedException {
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
    
    public void desactiverBoutonChargerPlan(boolean b){
        if(b)
            this.boutonChargerPlan.setDisable(true);
        else
            this.boutonChargerPlan.setDisable(false);
    }
    
    public void desactiverBoutonChargerDL(boolean b){
        if(b)
            this.boutonChargerDL.setDisable(true);
        else
            this.boutonChargerDL.setDisable(false);
    }
    
    public void desactiverBoutonCalculerTournees(boolean b){
        if(b)
            this.boutonCalculerTournees.setDisable(true);
        else
            this.boutonCalculerTournees.setDisable(false);
    }
    
    public void avertir(String message){
        this.createMessagePopup(message);
    }
    
    private void createMessagePopup(String message) {
        Label mess = new Label(message);
        mess.setPadding(new Insets(15));
        Button boutonRetour = new Button("Retour");

        VBox vbox = new VBox(mess, boutonRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
                        + "-fx-border-width : 3;");
        
        Scene secondeScene = new Scene(vbox, 230, 100);
 
        // New window (Stage)
        Stage popUp = new Stage();
        popUp.setTitle("Message");
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
        popUp.setY(stage.getY() + stage.getHeight()/2 - 50);

        popUp.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
