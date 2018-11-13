/*
 * Projet Deliverif
 *
 * Hexanome nÂ° 41
 *
 * Projet dÃ©veloppÃ© dans le cadre du cours "Conception OrientÃ©e Objet
 * et dÃ©veloppement logiciel AGILE".
 */
package deliverif;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import modele.outils.DemandeLivraison;
import modele.outils.GestionLivraison;
import modele.outils.Tournee;

/**
 * Classe implémentant le composant de Vue Textuelle de l'IHM du projet ainsi que son comportement.
 * La Vue Textuelle représente la description textuelle des tournées de livraison à effectuer par les livreurs.
 * @author Aurelien Belin
 * @see VBox
 * @see Observer
 * @see Deliverif
 */
public class VueTextuelle extends VBox implements Observer {
    
    private GestionLivraison gestionLivraison;
    private final String[] NOMS_COULEURS = {"violet","marron","vert fluo","corail","rouge","bleu","vert foncé", "rose","or","beige"};
    
    /**
     * @deprecated
     */
    private ArrayList<String> descriptions;
    
    private ObservableList<String> contenu;
    private EcouteurBoutons ecouteurBoutons;
    
    //Composants
    private ComboBox<String> choixTournee;
    private Label descriptionTournee;
    private ArrayList<VBox> tournees;
    private VBox demandeLivraisons;
    private ScrollPane panel;
    private DescriptifChemin descriptifSelectionne;
    
    /**
     * Constructeur de VueTextuelle
     * @param gl - point d'entrée du modèle observé
     * @param ec - Ecouteur sur la fenetre principale
     * @see GestionLivraison
     * @see EcouteurBoutons
     */
    public VueTextuelle(GestionLivraison gl, EcouteurBoutons ec){
        super();
        
        this.ecouteurBoutons = ec;
        this.gestionLivraison = gl;
        this.gestionLivraison.addObserver(this);
        this.descriptions = new ArrayList<>();
        this.contenu = FXCollections.observableArrayList();
        
        this.descriptifSelectionne = null;
        
        this.tournees = new ArrayList<>();
        
        this.setSpacing(10);
        this.setPrefSize(285,420);
        this.setMinHeight(420);
        
        this.choixTournee = new ComboBox();
        this.choixTournee.setPrefWidth(375);
        this.choixTournee.setOnAction(e->{
            try {
                ecouteurBoutons.changerTourneeAffichee((ActionEvent) e);
            } catch (InterruptedException ex) {
                Logger.getLogger(VueTextuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.panel = new ScrollPane();
        panel.setPrefSize(285,375);
        panel.setHbarPolicy(ScrollBarPolicy.NEVER);
        panel.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        this.descriptionTournee = new Label();
        this.descriptionTournee.setMaxWidth(285);
        this.descriptionTournee.setWrapText(true);
        
        this.getChildren().addAll(choixTournee,panel);
        
    }
    
    /**
     * Change la description affichée en fonction de l'option choisie dans le ComboBox (attribut choixTournee).
     * @return l'indice de la description affichée
     * @deprecated
     */
    public int changerDescriptionAffichee(){
        String s = choixTournee.getSelectionModel().getSelectedItem();

        if(s!=null || "".equals(s)){
            for(int i=0;i<contenu.size();i++){
                if(contenu.get(i).equals(s)){
                    this.descriptionTournee.setText("");
                    this.descriptionTournee.setText(this.descriptions.get(i));
                    return i;
                }
            }
        }else
            this.descriptionTournee.setText("");
        
        return -1;
    }
    
    /**
     * Met à jour la VueTextuelle en fonction des données du modèle et de ses mises à jour.
     * @param o - Objet à observer, ici une instance de GestionLivraison
     * @param arg - inutile
     */
    @Override
    public void update(Observable o, Object arg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (arg instanceof modele.outils.DemandeLivraison){
                    contenu.clear();
                    afficherDemandeLivraisons();
                } else if (arg instanceof modele.outils.Tournee[]){
                    String c = contenu.get(0);
                    contenu.clear();
                    contenu.add(c);
                    tournees.clear();

                    afficherTournees();
                }
            }
        });
    }
    
    public void effacer(){
        contenu.clear();
        tournees.clear();
        choixTournee.getItems().clear();
        
        this.panel.setContent(null);
    }
    
    private void afficherDemandeLivraisons(){
        if(this.gestionLivraison.getDemande()!=null && !this.gestionLivraison.calculTSPEnCours()){
            DemandeLivraison demande = this.gestionLivraison.getDemande();
            
            demandeLivraisons = new VBox();
            demandeLivraisons.setPrefWidth(this.panel.getViewportBounds().getWidth());
            contenu.add("Demande de livraison");
            
            Iterator<List<String>> it = demande.getDescription();
            int j = 1;
            
            while(it.hasNext()){
                List<String> s = it.next();
                DescriptifChemin dc = new DescriptifChemin((int)(this.panel.getViewportBounds().getWidth()),j,s.get(0), s.get(1), this.ecouteurBoutons);
                demandeLivraisons.getChildren().add(dc);
                j++;
            }
            
            choixTournee.setItems(contenu);
            choixTournee.getSelectionModel().selectFirst();
            this.panel.setContent(demandeLivraisons);
        }
    }
    
    private void afficherTournees(){
        if(this.gestionLivraison.getTournees()!=null && !this.gestionLivraison.calculTSPEnCours()){
            Tournee[] listeTournees = this.gestionLivraison.getTournees();
            String nom ="";
            
            if(listeTournees.length!=0){
                int i = 1;
                
                for(Tournee t : listeTournees){
                    
                    int j = 1;
                    VBox vbox = new VBox();
                    vbox.setMinWidth(this.panel.getViewportBounds().getWidth());
                    //contenu.add("Livreur "+i);
                    contenu.add("Livreur "+NOMS_COULEURS[i-1]);
                    Iterator<List<String>> it = t.getDescription_Bis();
                    
                    while(it.hasNext()){
                        List<String> s = it.next();
                        
                        DescriptifChemin dc = new DescriptifChemin((int)(this.panel.getViewportBounds().getWidth()),i,j,s.get(0), s.get(1), s.get(2), s.size()>3?s.subList(4,s.size()):null,this.ecouteurBoutons);
                        vbox.getChildren().add(dc);
                        j++;
                    }
                    i++;
                    
                    this.tournees.add(vbox);
                }
            }
            
            choixTournee.setItems(contenu);
            choixTournee.getSelectionModel().selectFirst();
            this.panel.setContent(this.demandeLivraisons);
        }
    }
    
    /**
     * Change la description affichée en fonction de l'option choisie dans le ComboBox (attribut choixTournee).
     * @return l'indice de la description affichée 
     */
    public int changerDescription_Bis(){
        String s = choixTournee.getSelectionModel().getSelectedItem();
        
        if(s!=null || "".equals(s)){
            
            for(int i=0;i<contenu.size();i++){

                if(contenu.get(i).equals(s) && i==0){
                    this.panel.setContent(demandeLivraisons);
                    return i;
                }else if(contenu.get(i).equals(s)){
                    this.panel.setContent(this.tournees.get(i-1));
                    return i;
                }
            }
        }else
            this.panel.setContent(null);
        
        return -1;
    }
    
    /**
     * Met à jour le DescriptifChemin sélectionné ou désélectionné dans la Vue Textuelle.
     * @param nouveauDescriptif - DescriptifChemin à mettre à jour
     * @see DescriptifChemin
     */
    public void majVueTextuelle(DescriptifChemin nouveauDescriptif){
        if(this.descriptifSelectionne!=null){
            this.descriptifSelectionne.setLocalise(false);
        }
        
        if(this.descriptifSelectionne != nouveauDescriptif){
            this.descriptifSelectionne = nouveauDescriptif;
            this.descriptifSelectionne.setLocalise(true);
        }else{
            this.descriptifSelectionne = null;
        }
    }
    
    public int affichageActuel(){
        String selec = this.choixTournee.getSelectionModel().getSelectedItem();
        
        if(selec!=null || "".equals(selec)){
            for(int i=0;i<contenu.size();i++){
                if(contenu.get(i).equals(selec)){
                    return i;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 
     */
    public void ajouterBoutonAjout(){
        for(int parcoursConteneur=0;parcoursConteneur<this.tournees.size();parcoursConteneur++){
            List <Node> tournee = this.tournees.get(parcoursConteneur).getChildren();
            int taille = tournee.size();
            for(int parcoursTournee=1; parcoursTournee<taille; parcoursTournee++){
                Button plus = new Button("+");
                plus.setAlignment(Pos.CENTER);
                plus.setPrefWidth((int)this.panel.getViewportBounds().getWidth());
                 
                this.tournees.get(parcoursConteneur).getChildren().add(2*parcoursTournee-1, plus);
                
                int indexPlus = 2*parcoursTournee-1;
                int indexTournee = parcoursConteneur;
                plus.setOnAction(e -> ecouteurBoutons.clicPlus(e, indexPlus, indexTournee));
            }
        }
        
    }
    
    public void supprimerBoutonAjout(){
        for(int parcoursConteneur=1;parcoursConteneur<this.tournees.size();parcoursConteneur++){
            List <Node> tournee = this.tournees.get(parcoursConteneur).getChildren();
            int taille = tournee.size();
            for(int parcoursTournee=1; parcoursTournee<taille/2+1; parcoursTournee++){
                this.tournees.get(parcoursConteneur).getChildren().remove(parcoursTournee);
            }
        }
    }
    
    public void entourerPlusClique(int indexPlus, int indexTournee){
        this.tournees.get(indexTournee).getChildren().get(indexPlus).setStyle("-fx-border-color:blue;-fx-border-width:4px;");
        this.panel.setContent(this.tournees.get(indexTournee));
    }
    
    public void changerPlusEntoure(int indexPlusPreced, int indexTourneePreced, int indexPlus, int indexTournee){
        this.tournees.get(indexTourneePreced).getChildren().get(indexPlusPreced).setStyle("-fx-border-color:black;-fx-border-width:2px;");
        this.tournees.get(indexTournee).getChildren().get(indexPlus).setStyle("-fx-border-color:blue;-fx-border-width:4px;");
        this.panel.setContent(this.tournees.get(indexTournee));
    }
    
    public void estReorgFinie(){
        VBox tournee = this.tournees.get(0);
        List<DescriptifChemin> desc= (ObservableList) tournee.getChildren();
        for(DescriptifChemin pt : desc){
            pt.disableUpDown();
         }
    }
    public void estReorgTourneesDemandee(){
        VBox tournee = this.tournees.get(0);
        List<DescriptifChemin> desc= (ObservableList) tournee.getChildren();
        for(DescriptifChemin pt : desc){
            pt.enableUpDown();
         }
    }
}
