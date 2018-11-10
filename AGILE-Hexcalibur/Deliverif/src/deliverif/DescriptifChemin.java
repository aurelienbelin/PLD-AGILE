/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package deliverif;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * Composant décrivant le point de départ d'un chemin, ainsi que le chemin associé pour aller au prochain point de passage de la tournée considérée.
 * @author Aurelien Belin
 */
public class DescriptifChemin extends VBox{
    
    private EcouteurBoutons ecouteurBoutons;
    
    private int largeur;
    private int hauteur;
    
    private String identifiantPoint;
    
    //Composants
    private HBox entete;
    private Label horaire;
    private Label duree;
    private Label livraison;
    private Button obtenirDetails;
    private VBox details;
    private boolean developpe;
    
    private boolean localise;
    
    /**
     * 
     * @param largeur
     * @param numTournee
     * @param numLivraison
     * @param horaire
     * @param duree
     * @param nomLivraison
     * @param description
     * @param ecouteurBoutons 
     */
    public DescriptifChemin(int largeur, int numTournee, int numLivraison, String horaire, String duree, String nomLivraison, List<String> description, EcouteurBoutons ecouteurBoutons){
        super();
        
        //System.out.println(largeur);
        
        this.identifiantPoint = numTournee+"_"+numLivraison;
        
        this.ecouteurBoutons = ecouteurBoutons;
        
        this.largeur = largeur;
        
        this.setSpacing(0);
        this.setMaxWidth(largeur);
        this.setStyle("-fx-border-color:black; -fx-border-width:2px;");
        
        this.creerEnteteDescriptifTournee(horaire, duree, nomLivraison, (description!=null && description.size()!=0));
        
        this.creerDetails(description);
        
        this.developpe = false;
        
        this.localise = false;
        
        this.getChildren().addAll(this.entete);
    }
    
    public DescriptifChemin(int largeur, int numLivraison, String duree, String nomLivraison, EcouteurBoutons ecouteurBoutons){
        super();
        
        this.identifiantPoint = "-1_"+numLivraison;
        
        this.ecouteurBoutons = ecouteurBoutons;
        
        this.largeur = largeur;
        
        this.setSpacing(0);
        this.setMaxWidth(largeur);
        this.setStyle("-fx-border-color:black; -fx-border-width:2px;");
        
        this.creerEnteteDescriptifDL(duree, nomLivraison);
        
        this.developpe = false;
        
        this.localise = false;
        
        this.getChildren().addAll(this.entete);        
    }
    
    /**
     * Crée l'entête du composant dans le cas où il correspond à une entête d'un descriptif d'une livraison de la demande de livraison
     * @param duree
     * @param nom 
     */
    private void creerEnteteDescriptifDL(String duree, String nom){
        this.entete = new HBox();
        this.entete.setSpacing(0);
        this.entete.setStyle("-fx-background-color:white;");
        
        this.duree = new Label(duree+" min");
        this.duree.setAlignment(Pos.CENTER);
        this.duree.setPrefSize(largeur/4.0,50);
        this.duree.setStyle("-fx-background-color:linear-gradient(#87CEFA, #6495ED); -fx-font-style:italic; -fx-font-weight:bold;");
        
        this.livraison = new Label(nom);
        this.livraison.setAlignment(Pos.CENTER);
        this.livraison.setWrapText(true);
        this.livraison.setTextAlignment(TextAlignment.CENTER);
        this.livraison.setPrefHeight(50);
        this.livraison.setPrefWidth(4*this.largeur/5.0);
        this.livraison.setStyle("-fx-background-color: white; ");
        this.livraison.setOnMouseClicked(e->ecouteurBoutons.localiserPointVueGraphique(this));
        
        this.entete.getChildren().addAll(this.duree,this.livraison);
    }
    
    /**
     * Crée l'entête du composant dans le cas où il correspond à une entête d'un descriptif de chemin pour une tournée
     * @param horaire
     * @param duree
     * @param nomLivraison 
     */
    private void creerEnteteDescriptifTournee(String horaire, String duree, String nomLivraison, boolean details){
        this.entete = new HBox();
        this.entete.setSpacing(0);
        this.entete.setStyle("-fx-background-color:white;");
        
        VBox temps = new VBox();
        temps.setSpacing(0);
        temps.setPrefSize(largeur/4.0,50);
        temps.setStyle("-fx-border-color:white;");
        
        this.horaire = new Label(horaire);
        this.horaire.setAlignment(Pos.CENTER);
        this.horaire.setMinWidth(largeur/4.0);
        this.horaire.setPrefHeight(30);
        this.horaire.setStyle("-fx-background-color: linear-gradient(#E4EAA2, #9CD672); -fx-font-size:15px; -fx-font-weight:bold;");
        
        this.duree = new Label(duree+" min");
        this.duree.setAlignment(Pos.CENTER_RIGHT);
        this.duree.setMinWidth(largeur/4.0);
        this.duree.setStyle("-fx-background-color:linear-gradient(#E4EAA2, #9CD672); -fx-font-style:italic;");
        
        temps.getChildren().addAll(this.horaire, this.duree);
        
        this.livraison = new Label(nomLivraison);
        this.livraison.setAlignment(Pos.CENTER);
        this.livraison.setWrapText(true);
        this.livraison.setTextAlignment(TextAlignment.CENTER);
        this.livraison.setPrefHeight(50);
        this.livraison.setPrefWidth(4*this.largeur/5.0);
        this.livraison.setStyle("-fx-background-color: white; ");
        this.livraison.setOnMouseClicked(e->ecouteurBoutons.localiserPointVueGraphique(this));
        
        this.entete.getChildren().addAll(temps,this.livraison);
        
        if(details){
            this.obtenirDetails = new Button("+");
            this.obtenirDetails.setOnAction(e->ecouteurBoutons.obtenirDetailsVueTextuelle(this));
            this.obtenirDetails.setPrefHeight(50);
            this.obtenirDetails.setMinWidth(largeur/10.0);
            this.obtenirDetails.setStyle("-fx-background-color:#c3c4c4," +
                "linear-gradient(#d6d6d6 50%, white 100%)," +
                "radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);");
            this.entete.getChildren().add(this.obtenirDetails);
        }
    }
    
    /**
     * Crée les détails associés
     * @param description - liste des descriptions des actions à suivre pour parcourir le chemin 
     */
    private void creerDetails(List<String> description){
        this.details = new VBox();
        this.details.setSpacing(0);
        this.details.setMaxWidth(this.largeur);

        if(description!=null){
            for(String s : description){
                Label l = new Label("\t"+s);
                l.setPrefWidth(largeur);
                l.setStyle("-fx-border-color:grey;");

                details.getChildren().add(l);
            }
        }
    }
    
    /**
     * Develloppe les détails du composant
     */
    public void developperDetails(){
        if(this.developpe){
            this.obtenirDetails.setText("+");
            this.getChildren().remove(1);
        }else{
            this.obtenirDetails.setText("-");
            this.getChildren().add(details);
        }
        
        this.developpe = !this.developpe;
    }
    
    /**
     * Renvoie vrai si le point de passage associé est localisé par un marqueur sur la Vue Graphique, false sinon
     * @return 
     */
    public boolean estLocalise(){
        return this.localise;
    }

    /**
     * 
     * @param b - valeur à donner à la localisation du composant.
     */
    public void setLocalise(boolean b) {
        this.localise = b;
        
        if(b){
            this.setStyle("-fx-border-color:red;-fx-border-width:4px;");
        }else{
            this.setStyle("-fx-border-color:black;-fx-border-width:2px;");
        }
    }
    
    /**
     * Renvoie l'identifiant du point de passage associé au composant.
     * @return 
     */
    public String getPoint(){
        return this.identifiantPoint;
    }
}
