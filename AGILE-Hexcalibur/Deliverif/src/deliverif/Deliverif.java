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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import modele.outils.GestionLivraison;

/**
 * Classe principale/point d'entrée de l'application. Il s'agit de la fenetre principale de l'application.
 * @author romain
 * @see Application
 */
public class Deliverif extends Application {
    
    private static Stage stage;
    private static Scene scene;
    
    //Commande

    /**
     *
     */
    public final static String CHARGER_PLAN = "Charger un plan";

    /**
     *
     */
    public final static String CHARGER_DL = "Charger une demande de livraisons";

    /**
     *
     */
    public final static String AJOUTER_LIVRAISON = "Ajouter une livraison";

    /**
     *
     */
    public final static String SUPPRIMER_LIVRAISON = "Supprimer une livraison";

    /**
     *
     */
    public final static String REORGANISER_TOURNEE = "Réorganiser tournée";

    /**
     *
     */
    public final static String CALCULER_TOURNEES = "Calculer les tournées";
    
    //private Controleur controleur;
    private GestionLivraison gestionLivraison;
    private EcouteurBoutons ecouteurBoutons;
    private Controleur controleur;
    
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
    private Label information;
    
    @Override
    public void init() throws Exception{
        super.init();
        gestionLivraison = new GestionLivraison();
        controleur = new Controleur(gestionLivraison,this);
        ecouteurBoutons = new EcouteurBoutons(this, controleur);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Group root = new Group();
        scene = new Scene(root,1024,640);
        
        bord = new BorderPane();
        
        boutons = new HBox();
        boutons.setPadding(new Insets(15, 15, 15, 15));
        boutons.setSpacing(5);
        
        creerBoutonsChargement();
        
        boutons.getChildren().addAll(boutonChargerPlan, boutonChargerDL, boutonAjouterLivraison,boutonSupprimerLivraison, boutonReorganiserTournee);
        
        Separator sv = new Separator();
        sv.setOrientation(Orientation.VERTICAL);
        sv.setPrefHeight(scene.getHeight());
        sv.setLayoutX(640);
        
        creerPanelDroit();   
        //Il faudra ajouter la vue graphique
        vueGraphique = new VueGraphique(this.gestionLivraison, this);
        vueGraphique.setLayoutX(0);
        vueGraphique.setLayoutY(115);
        
        bord.setTop(boutons);
        
        root.getChildren().add(bord);
        root.getChildren().add(sv);
        root.getChildren().add(panelDroit);
        root.getChildren().add(vueGraphique);
        
        stage.setTitle("Deliver'IF");
        stage.setResizable(false);
        stage.setScene(scene);
        this.stage = stage;
        stage.show();
    }
    
    /**
     * Création des boutons de la fenetre
     */
    private void creerBoutonsChargement(){
        //A factoriser
        boutonChargerPlan = new Button(CHARGER_PLAN);
        boutonChargerPlan.setPrefSize(100,65);
        boutonChargerPlan.setWrapText(true);
        boutonChargerPlan.setTextAlignment(TextAlignment.CENTER);
        boutonChargerPlan.setOnAction(e->{
            try {
                ecouteurBoutons.chargerPlanAction(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            } 
        });
        
        boutonChargerDL = new Button(CHARGER_DL);
        boutonChargerDL.setPrefSize(100,65);
        boutonChargerDL.setWrapText(true);
        boutonChargerDL.setTextAlignment(TextAlignment.CENTER);
        boutonChargerDL.setDisable(true);
        boutonChargerDL.setOnAction(e -> {
            try {
                ecouteurBoutons.chargerDemandeLivraisonAction(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        boutonAjouterLivraison = new Button(AJOUTER_LIVRAISON);
        boutonAjouterLivraison.setPrefSize(100,65);
        boutonAjouterLivraison.setWrapText(true);
        boutonAjouterLivraison.setDisable(true);
        boutonAjouterLivraison.setTextAlignment(TextAlignment.CENTER);
        
        boutonSupprimerLivraison = new Button(SUPPRIMER_LIVRAISON);
        boutonSupprimerLivraison.setPrefSize(100,65);
        boutonSupprimerLivraison.setWrapText(true);
        boutonSupprimerLivraison.setDisable(true);
        boutonSupprimerLivraison.setTextAlignment(TextAlignment.CENTER);
        
        boutonReorganiserTournee = new Button(REORGANISER_TOURNEE);
        boutonReorganiserTournee.setPrefSize(100,65);
        boutonReorganiserTournee.setWrapText(true);
        boutonReorganiserTournee.setDisable(true);
        boutonReorganiserTournee.setTextAlignment(TextAlignment.CENTER);
    }
    
    /**
     * Création du panel droit de la fenetre principal.
     */
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
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        nbLivreurs.setValueFactory(valueFactory);
        nbLivreurs.setPrefSize(60,25);
        
        boxLivreurs.getChildren().addAll(livreurs, nbLivreurs);
        
        boutonCalculerTournees = new Button(CALCULER_TOURNEES);
        boutonCalculerTournees.setPrefSize(300,50);
        boutonCalculerTournees.setMinHeight(50);
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
        sh.setMaxWidth(1024-640-50);
        sh.setLayoutX(640);
        
        vueTextuelle = new VueTextuelle(gestionLivraison, ecouteurBoutons);
        vueTextuelle.setMaxWidth(1024-640-50);
        
        this.information = new Label();
        this.information.setPrefSize(1024-640,30);
        this.information.setMinWidth(1024-640-25);
        this.information.setMaxHeight(30);
        this.information.setAlignment(Pos.CENTER_RIGHT);
        this.information.setFont(new Font("Arial", 10));
        this.information.setText("Test");
        //this.information.setStyle("-fx-background-color:red;");
        
        panelDroit.getChildren().addAll(boxLivreurs, boutonCalculerTournees, sh, vueTextuelle, information);
    }
    
    /**
     * Affiche en bas à droite l'action en cours dans l'application.
     * @param message - message à afficher (état courant de l'application)
     */
    protected void informationEnCours(String message){
        this.information.setText(message);
    }
    
    /**
     * Renvoie le nombre de livreurs.
     * @return le nombre de livreurs
     */
    public int getNbLivreurs(){
        return (Integer)this.nbLivreurs.getValue();
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
    public static File openFileChooser(FileChooser fileChooser) throws InterruptedException {
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
    
    /**
     * Créer un Pop-Up pour avertir l'utilisateur.
     * @param message - message à afficher dans le pop-up
     */
    public void avertir(String message){
        this.createMessagePopup(message);
    }
    
    /**
     * Construit une fenetre de pop-up.
     * @param message - message à afficher dans le pop-up
     */
    private void createMessagePopup(String message) {
        //System.out.println("Xx : "+this.boutons.getWidth()+" ; Yy : "+this.boutons.getHeight()); //DEBUG
        
        Label mess = new Label(message);
        mess.setPadding(new Insets(15));
        mess.setWrapText(true);
        mess.setTextAlignment(TextAlignment.CENTER);
        mess.setAlignment(Pos.CENTER);
        Button boutonRetour = new Button("Ok");

        VBox vbox = new VBox(mess, boutonRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
                        + "-fx-border-width : 3;");
        
        Scene secondeScene = new Scene(vbox, 230, 130);
 
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
        popUp.setY(stage.getY() + stage.getHeight()/2 - 65);

        popUp.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Passe l'IHM dans l'état suivant une fois le plan chargé.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estPlanCharge(String cre) {
        if(("SUCCESS").equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(true);
            boutonAjouterLivraison.setDisable(true);
            boutonSupprimerLivraison.setDisable(true);
            boutonReorganiserTournee.setDisable(true);
            //avertir("Le plan de la ville a bien été chargé");
        }else if(cre!=null){
            this.informationEnCours("");
            avertir(cre);
        }else{
            this.informationEnCours("");
            avertir("Le plan n'a pas pu être chargé");
        }
    }
    
    /**
     * Passe l'IHM dans l'état suivant une fois la demande de livraison chargée.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estDemandeLivraisonChargee(String cre){
        if(("SUCCESS").equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(false);
            boutonAjouterLivraison.setDisable(true);
            boutonSupprimerLivraison.setDisable(true);
            boutonReorganiserTournee.setDisable(true);
            //avertir("La demande de livraison a bien été chargée");
        }else if(cre!=null){
            this.informationEnCours("");
            avertir(cre);
        }else{
            this.informationEnCours("");
            avertir("La demande de livraison n'a pas pu être chargée");
        }
    }
    
    /**
     * Passe l'IHM dans l'état suivant une fois les tournées calculées.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estTourneesCalculees(String cre){
        if(("SUCCESS").equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(false);
            boutonAjouterLivraison.setDisable(false);
            boutonSupprimerLivraison.setDisable(false);
            boutonReorganiserTournee.setDisable(true);
            //this.vueGraphique.dessinerTournees();
            //avertir("Calcul des tournées terminé");
        }else{
            this.informationEnCours("");
            avertir("Le calcul des tournées n'a pas pu se terminer");
    
        }
    }
}
