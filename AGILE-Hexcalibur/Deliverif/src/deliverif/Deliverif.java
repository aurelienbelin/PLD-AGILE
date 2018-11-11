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
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.outils.GestionLivraison;
import modele.outils.PointPassage;

/**
 * Classe principale/point d'entrée de l'application. Il s'agit de la fenetre principale de l'application.
 * @author romain
 * @see Application
 */
public class Deliverif extends Application implements Observer{
    
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
    
    public final static String ZOOM_AVANT = "+";
    
    public final static String ARRETER_CALCUL_TOURNEES = "Stop";
    
    //private Controleur controleur;
    private GestionLivraison gestionLivraison;
    private EcouteurBoutons ecouteurBoutons;
    private Controleur controleur;
    
    private VueTextuelle vueTextuelle;
    private VueGraphique vueGraphique;
    
    //Conteneurs
    private BorderPane bord;
    private HBox boutons;
    private VBox boxCalculTournees;
    private VBox boxAjoutLivraison;
    private VBox panelDroit;
    private HBox boutonsAjoutLivraison;
    
    //Composants de controle
    private Button boutonChargerPlan;
    private Button boutonChargerDL;
    private Button boutonAjouterLivraison;
    private Button boutonSupprimerLivraison;
    private Button boutonReorganiserTournee;
    private Button boutonCalculerTournees;
    private Button boutonArreterCalcul;
    private Button boutonAnnuler;
    private Button boutonValiderSelection;
    private Button boutonValiderAjout;
    private Button boutonRetourSelection;
    private Spinner nbLivreurs;
    private Spinner choixDuree;
    private Label descriptionTextuelle;
    private ComboBox choixTournee;
    private Label information;
    
    @Override
    public void init() throws Exception{
        super.init();
        gestionLivraison = new GestionLivraison();
        controleur = new Controleur(gestionLivraison,this);
        vueGraphique = new VueGraphique(this.gestionLivraison, this);
        ecouteurBoutons = new EcouteurBoutons(this, controleur, vueGraphique);
        gestionLivraison.addObserver(this);
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
        
        boutonsAjoutLivraison = new HBox();
        boutonsAjoutLivraison.setPadding(new Insets(15, 15, 15, 15));
        boutonsAjoutLivraison.setSpacing(5);
        
        boutonsAjoutLivraison.getChildren().addAll(boutonAnnuler, boutonValiderSelection, boutonRetourSelection);
        
        Separator sv = new Separator();
        sv.setOrientation(Orientation.VERTICAL);
        sv.setPrefHeight(scene.getHeight());
        sv.setLayoutX(640);
        
        creerPanelDroit();   
        //Il faudra ajouter la vue graphique
        vueGraphique.setLayoutX(0);
        vueGraphique.setLayoutY(115);
        
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
        
        creerBoxAjoutLivraison();
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
        boutonAjouterLivraison.setOnAction(e -> {
            try {
                ecouteurBoutons.ajouterLivraison(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
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
        
        boutonAnnuler = new Button("Retour au menu");
        boutonAnnuler.setPrefSize(100,65);
        boutonAnnuler.setWrapText(true);
        boutonAnnuler.setDisable(true);
        boutonAnnuler.setTextAlignment(TextAlignment.CENTER);
        boutonAnnuler.setOnAction(e -> {
            try {
                ecouteurBoutons.boutonAnnuler(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        boutonRetourSelection = new Button("Retour à la sélection");
        boutonRetourSelection.setPrefSize(100,65);
        boutonRetourSelection.setWrapText(true);
        boutonRetourSelection.setDisable(false);
        boutonRetourSelection.setTextAlignment(TextAlignment.CENTER);
        boutonRetourSelection.setTranslateX(295);
        boutonRetourSelection.setVisible(false);
        boutonRetourSelection.setOnAction(e -> {
            try {
                ecouteurBoutons.boutonRetour(e);
            } catch (Exception ex) {
                Logger.getLogger(Deliverif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
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
        
        boxCalculTournees = new VBox();
        boxCalculTournees.setSpacing(15);
        
        HBox boxLivreurs = new HBox();
        boxLivreurs.setSpacing(25);
        
        Label livreurs = new Label("Livreurs : ");
        livreurs.setFont(new Font("System",20));
        
        nbLivreurs = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        nbLivreurs.setValueFactory(valueFactory);
        nbLivreurs.setPrefSize(60,25);
        
        
        
        boxLivreurs.getChildren().addAll(livreurs, nbLivreurs);
        
        
        
        HBox boxBoutons = new HBox();
        boxBoutons.setSpacing(15);
        
        boutonCalculerTournees = new Button(CALCULER_TOURNEES);
        boutonCalculerTournees.setPrefSize(1024-640-90-50,50);
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
        this.information.setStyle("-fx-font-style:italic; -fx-font-weight:bold; -fx-text-fill:red;");
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
        
        Label duree = new Label("Durée : ");
        duree.setFont(new Font("System",20));
        
        choixDuree = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        choixDuree.setValueFactory(valueFactory);
        choixDuree.setPrefSize(60,25);
        
        boxDuree.getChildren().addAll(duree, choixDuree);
        
        boutonValiderAjout = new Button("Ajouter la livraison");
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
            if (arg instanceof modele.outils.Tournee[]){
                if (!((GestionLivraison)o).calculTSPEnCours()){
                    System.out.println("Le calcul est enfin fini !");
                    /*On appelle la methode bouton stop, cela marchera puisque
                    le calcul est fini !*/
                    this.controleur.boutonArretCalcul();
                }
            }
        }
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

    /**n
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
        informationEnCours("");
        if(("SUCCESS").equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(true);
            boutonAjouterLivraison.setDisable(true);
            boutonSupprimerLivraison.setDisable(true);
            boutonReorganiserTournee.setDisable(true);
            vueTextuelle.effacer();
            //avertir("Le plan de la ville a bien été chargé");
        }else if(cre!=null){
            avertir(cre);
        }else{
            avertir("Le plan n'a pas pu être chargé");
        }
    }
    
    public void estPointPassageSelectionne(double latitude, double longitude) {
        getVueGraphique().effacerMarker();
        getVueGraphique().ajouterMarker(latitude, longitude);
    }
    /**
     * Passe l'IHM dans l'état suivant une fois la demande de livraison chargée.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estDemandeLivraisonChargee(String cre){
        this.informationEnCours("");
        if(("SUCCESS").equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(false);
            boutonAjouterLivraison.setDisable(true);
            boutonSupprimerLivraison.setDisable(true);
            boutonReorganiserTournee.setDisable(true);
            //avertir("La demande de livraison a bien été chargée");
        }else if(cre!=null){
            avertir(cre);
        }else{
            avertir("La demande de livraison n'a pas pu être chargée");
        }
    }
    
    /**
     * Passe l'IHM dans l'état suivant une fois les tournées calculées.
     * @param cre - compte rendu d'execution des opérations sur le modèle
     */
    public void estTourneesCalculees(String cre){
        informationEnCours("");
        if(("SUCCESS").equals(cre)){
            boutonChargerPlan.setDisable(false);
            boutonChargerDL.setDisable(false);
            boutonCalculerTournees.setDisable(false);
            boutonAjouterLivraison.setDisable(false);
            boutonSupprimerLivraison.setDisable(false);
            boutonReorganiserTournee.setDisable(true);
        }else{
            avertir("Le calcul des tournées n'a pas pu se terminer");
        }
       
    }
    
    public void estPlanCliquable(){
        bord.setTop(boutonsAjoutLivraison);
        panelDroit.getChildren().remove(boxCalculTournees);
        panelDroit.getChildren().add(0, boxAjoutLivraison);
        boxAjoutLivraison.setDisable(true);
        
        boutonValiderSelection.setVisible(true);
        boutonRetourSelection.setVisible(false);
        
        boutonAnnuler.setDisable(false);
        boutonValiderSelection.setDisable(true);
    }
    
    public void estIntersectionSelectionnee(double latitude,double longitude){
        boutonValiderSelection.setDisable(false);
        vueGraphique.ajouterMarkerAjout(latitude, longitude);
    }
    
    public void changerIntersectionSelectionnee(double latitude,double longitude){
        vueGraphique.effacerMarkerAjout();
        vueGraphique.ajouterMarkerAjout(latitude, longitude);
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
        boutonValiderAjout.setDisable(false);
        vueTextuelle.entourerPlusClique(indexPlus, indexTournee);
    }
    
    public void changePlusClique(int indexPlusPreced,int indexTourneePreced, int indexPlus, int indexTournee){
        vueTextuelle.changerPlusEntoure(indexPlusPreced, indexTourneePreced, indexPlus, indexTournee);
    }
    
    public void estAjoutLivraisonFini(){
        bord.setTop(boutons);
        
        panelDroit.getChildren().remove(boxAjoutLivraison);
        panelDroit.getChildren().add(0, boxCalculTournees);
        
        vueTextuelle.supprimerBoutonAjout();
        vueGraphique.effacerMarkerAjout();
        
        
        estTourneesCalculees("SUCCESS");
    }
    
    public void activerBoutonArreterCalcul(boolean activation){
        this.boutonArreterCalcul.setDisable(activation);
        this.boutonCalculerTournees.setDisable(!activation);
        //this.boutonChargerPlan.setDisable(!activation);
        //this.boutonChargerDL.setDisable(!activation);
    }
}
