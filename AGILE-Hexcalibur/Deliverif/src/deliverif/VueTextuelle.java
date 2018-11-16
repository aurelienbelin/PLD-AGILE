/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import modele.DemandeLivraison;
import modele.GestionLivraison;
import modele.Tournee;

/**
 * Classe implémentant le composant de Vue Textuelle de l'IHM du projet ainsi que son comportement.
 * La Vue Textuelle représente la description textuelle des tournées de livraison à effectuer par les livreurs.
 * @author Hex'Calibur
 * @see VBox
 * @see Observer
 * @see Deliverif
 */
public class VueTextuelle extends VBox implements Observer {
    
    private GestionLivraison gestionLivraison;
    private final String[] NOMS_COULEURS = {"violet","marron","vert fluo","corail","rouge","bleu","vert foncé", "rose","or","beige"};
    private final String CHANGER_LIVRAISON_DE_TOURNEE = "Vers livreur ";
    
    /**
     * @deprecated
     */
    private ArrayList<String> descriptions;
    
    private ObservableList<String> contenu;
    private Ecouteur ecouteurBoutons;
    
    //Composants
    private ComboBox<String> choixTournee;
    private Label descriptionTournee;
    private ArrayList<VBox> tournees;
    private VBox demandeLivraisons;
    private ScrollPane panel;
    private DescriptifLivraison descriptifSelectionne;
    
    /**
     * Constructeur de VueTextuelle
     * @param gl - point d'entrée du modèle observé
     * @param ec - Ecouteur sur la fenetre principale
     * @see GestionLivraison
     * @see Ecouteur
     */
    public VueTextuelle(GestionLivraison gl, Ecouteur ec){
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
                if (arg instanceof modele.DemandeLivraison){
                    contenu.clear();
                    afficherDemandeLivraisons();
                } else if (arg instanceof modele.Tournee[]){
                    //String c = contenu.get(0);
                    contenu.clear();
                    //contenu.add(c);
                    
                    afficherDemandeLivraisons();
                    
                    tournees.clear();
                    afficherTournees();
                }
            }
        });
    }
    
    /**
     * Efface les éléments présents dans la tournée
     */
    public void effacer(){
        contenu.clear();
        tournees.clear();
        choixTournee.getItems().clear();
        
        this.panel.setContent(null);
    }
    
    /**
     * Affiche la demande de livraison
     */
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
                DescriptifLivraison dc = new DescriptifLivraison((int)(this.panel.getViewportBounds().getWidth()),j,s.get(0), s.get(1), this.ecouteurBoutons);
                demandeLivraisons.getChildren().add(dc);
                j++;
            }
            
            choixTournee.setItems(contenu);
            choixTournee.getSelectionModel().selectFirst();
            this.panel.setContent(demandeLivraisons);
        }
    }
    
    /**
     * Permets de visualiser textuellement les tournées.
     */
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
                    contenu.add("Livreur "+NOMS_COULEURS[i-1]);
                    Iterator<List<String>> it = t.getDescription();
                    
                    while(it.hasNext()){
                        List<String> s = it.next();
                        
                        DescriptifLivraison dc = new DescriptifLivraison((int)(this.panel.getViewportBounds().getWidth()),i,j,s.get(0), s.get(1), s.get(2), s.size()>3?s.subList(4,s.size()):null,this.ecouteurBoutons);
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
     * Permets pour une livraison donnée de récupérer le descriptif du chemin à parcourir pour atteindre le point de livraison.
     * @param tournee
     * @param position
     * @return 
     */
    public DescriptifLivraison getDescriptifChemin(int tournee, int position)
    {
        return (DescriptifLivraison) tournees.get(tournee).getChildren().get(position);
    }
    
    /**
     * Change la description affichée en fonction du paramètre tournée (attribut choixTournee).
     * @param tournee - identifiant de la tournée vers laquelle on se déplace
     */
    public void changerDescription_Ter(int tournee){
        this.panel.setContent(this.tournees.get(tournee));
        choixTournee.setValue(contenu.get(tournee+1));
    }
    
    /**
     * Change la description affichée en fonction de l'option choisie dans le ComboBox (attribut choixTournee).
     * @return - l'indice de la description affichée 
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
     * Met à jour le DescriptifLivraison sélectionné ou désélectionné dans la Vue Textuelle.
     * @param nouveauDescriptif - DescriptifLivraison à mettre à jour
     * @see DescriptifLivraison
     */
    public void majVueTextuelle(DescriptifLivraison nouveauDescriptif){
        if(this.descriptifSelectionne!=null){
            this.descriptifSelectionne.setLocalise(false);
        }
        
        if(nouveauDescriptif!=null && this.descriptifSelectionne != nouveauDescriptif){
            this.descriptifSelectionne = nouveauDescriptif;
            this.descriptifSelectionne.setLocalise(true);
        }else{
            this.descriptifSelectionne = null;
        }
    }
       
    /**
     * Permet de savoir quelle livrison est sélectionnée actuellement.
     * @return - l'indice de la livraison sélectionnée
     */
    public int affichageActuel(){
        String select = this.choixTournee.getSelectionModel().getSelectedItem();
        
        if(select!=null || "".equals(select)){
            for(int i=0;i<contenu.size();i++){
                if(contenu.get(i).equals(select)){
                    return i;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Ajoute les boutons entre les livraisons dans la vue textuelle afin de sélectionner avant quelle livraison on souhaite insérer la livraison à ajouter.
     */
    public void ajouterBoutonAjout(){
        for(int parcoursConteneur=0;parcoursConteneur<tournees.size();parcoursConteneur++){
            List <Node> tournee = tournees.get(parcoursConteneur).getChildren();
            int taille = tournee.size();
            for(int parcoursTournee=1; parcoursTournee<taille; parcoursTournee++){
                Button plus = new Button("+");
                plus.setAlignment(Pos.CENTER);
                plus.setPrefWidth((int)panel.getViewportBounds().getWidth());

                tournees.get(parcoursConteneur).getChildren().add(2*parcoursTournee-1, plus);

                int indexPlus = 2*parcoursTournee-1;
                int indexTournee = parcoursConteneur;
                plus.setOnAction(e -> ecouteurBoutons.clicPlus(e, indexPlus, indexTournee));
            }
        }
    }
    
    /**
     * Enlève les boutons plus permettant de choisir avant quelle livraison on souhaite insérer la nouvelle livraison. 
     */
    public void supprimerBoutonAjout(){
        for(int parcoursConteneur=0;parcoursConteneur<this.tournees.size();parcoursConteneur++){
            List <Node> tournee = this.tournees.get(parcoursConteneur).getChildren();
            int taille = tournee.size();
            for(int parcoursTournee=1; parcoursTournee<taille/2+1; parcoursTournee++){
                this.tournees.get(parcoursConteneur).getChildren().remove(parcoursTournee);
            }
        }
    }
 
    /**
     * Entoure le plus cliqué
     * @param indexPlus - index du plus cliqué
     * @param indexTournee - index de la tournée à laquelle appartient ce plus
     */
    public void entourerPlusClique(int indexPlus, int indexTournee){
        this.tournees.get(indexTournee).getChildren().get(indexPlus).setStyle("-fx-border-color:blue;-fx-border-width:4px;");
        this.panel.setContent(this.tournees.get(indexTournee));
    }
    
    /**
     * Permets de changer le plus entouré
     * @param indexPlusPreced - index du plus cliqué précedemment
     * @param indexTourneePreced - index de la tournée à laquelle appartient ce plus
     */
    public void changerPlusEntoure(int indexPlusPreced, int indexTourneePreced){
        this.tournees.get(indexTourneePreced).getChildren().get(indexPlusPreced).setStyle("-fx-border-color:black;-fx-border-width:2px;");
    }
    
    /**
     * Désactive le bouton plus 
     * @param indexPlus - index du bouton plus à désactiver
     * @param indexTournee  - index de la tournée à laquelle appartient ce plus
     */
    public void desactiverPlus(int indexPlus, int indexTournee){
        this.tournees.get(indexTournee).getChildren().get(indexPlus).setDisable(true);
        this.tournees.get(indexTournee).getChildren().get(indexPlus+2).setDisable(true);
    }
    
    /**
     * Permets d'afficher les détails d'une livraison
     */
    public void remettreBoutonsDetails(){
        List<VBox> tournees = this.tournees;
        for(VBox tournee: tournees){ 
            List<DescriptifLivraison> livraisonsDeTournee= (ObservableList) tournee.getChildren();
            for(DescriptifLivraison livraison : livraisonsDeTournee){
                livraison.desactiverHautBas();
            }
        }
    }
    
    /**
     * Ajoute les boutons permettant de réorganiser une tournée.
     */
    public void ajouterBoutonsReorg(){

        for(VBox tournee: tournees){ 
            List<DescriptifLivraison> livraisonsDeTournee= (ObservableList) tournee.getChildren();
            for(int indiceLivraison=0; indiceLivraison<(livraisonsDeTournee.size()-1);indiceLivraison++){
                DescriptifLivraison livraison = livraisonsDeTournee.get(indiceLivraison);
                livraison.activerHautBas();
            }
            DescriptifLivraison entrepot = livraisonsDeTournee.get(livraisonsDeTournee.size()-1);
            entrepot.enleverDetails();
            
            DescriptifLivraison debutTournee = livraisonsDeTournee.get(1);
            debutTournee.desactiverHaut();
            
            DescriptifLivraison finTournee = livraisonsDeTournee.get(livraisonsDeTournee.size()-2);
            finTournee.desactiverBas();
        }
    }
    
    /**
     * Ajoute le menu contextuel permettant de changer de tournée pendant la réorganisation.
     */
    public void ajouterMenuChangerTournee(){
        
        ContextMenu choixTournee = new ContextMenu();
 
        for(int indiceLivreur=0; indiceLivreur<tournees.size(); indiceLivreur++){
            MenuItem livreur = new MenuItem(CHANGER_LIVRAISON_DE_TOURNEE+NOMS_COULEURS[indiceLivreur]);
            int indiceTournee = indiceLivreur;
            livreur.setOnAction(e -> ecouteurBoutons.reorgSelectionTournee(e, indiceTournee));
            
            choixTournee.getItems().add(livreur);
            
            List<DescriptifLivraison> livraisonsDeLivreur= (ObservableList) tournees.get(indiceLivreur).getChildren();
            for(int indiceLivraison=0; indiceLivraison<(livraisonsDeLivreur.size()-1);indiceLivraison++){
                DescriptifLivraison livraison = livraisonsDeLivreur.get(indiceLivraison);
                livraison.setOnContextMenuRequested(e -> ecouteurBoutons.montrerMenuContextuel(e, choixTournee, livraison));
            }
        }
    }
}
