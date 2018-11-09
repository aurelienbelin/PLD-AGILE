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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
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
        contenu.clear();
        tournees.clear();
        this.descriptionTournee.setText("");
        
        /*if(this.gestionLivraison.getDemande()!=null && !this.gestionLivraison.calculTSPEnCours()){
            DemandeLivraison demande = this.gestionLivraison.getDemande();
            
            String des;
            des=new String("");
            Iterator<String> it = demande.getDescription();
            contenu.add("Demande de livraison");
            while(it.hasNext()){
                String s = it.next();
                des+="\n\t"+s;
            }

            VBox box = new VBox();
            box.setPrefWidth(this.panel.getViewportBounds().getWidth());
            
            Label l = new Label();
            l.setWrapText(true);
            l.setText(des);
            
            box.getChildren().add(l);
            
            this.tournees.add(box);
        }*/
        
        //A charger dès que la DL a été chargée ??
        if(this.gestionLivraison.getDemande()!=null && !this.gestionLivraison.calculTSPEnCours()){
            DemandeLivraison demande = this.gestionLivraison.getDemande();
            
            VBox box = new VBox();
            box.setPrefWidth(this.panel.getViewportBounds().getWidth());
            contenu.add("Demande de livraison");
            
            Iterator<String> it = demande.getDescription();
            int j = 1;
            
            while(it.hasNext()){
                String s = it.next();
                DescriptifChemin dc = new DescriptifChemin((int)(this.panel.getViewportBounds().getWidth()),0,j,"", "", s, null,this.ecouteurBoutons);
                box.getChildren().add(dc);
                j++;
            }
            
            this.tournees.add(box);
        }
        
        if(this.gestionLivraison.getTournees()!=null && !this.gestionLivraison.calculTSPEnCours()){
            Tournee[] listeTournees = this.gestionLivraison.getTournees();
            String nom ="";
            
            if(listeTournees.length!=0){
                int i = 1;
                
                for(Tournee t : listeTournees){
                    
                    int j = 1;
                    VBox vbox = new VBox();
                    vbox.setMinWidth(this.panel.getViewportBounds().getWidth());
                    contenu.add("Livreur "+i);
                    Iterator<List<String>> it = t.getDescription_Bis();
                    
                    while(it.hasNext()){
                        List<String> s = it.next();
                        
                        /*if(s.get(2).contains("Livraison")){
                            j++;
                        }*/
                        
                        DescriptifChemin dc = new DescriptifChemin((int)(this.panel.getViewportBounds().getWidth()),i,j,s.get(0), s.get(1), s.get(2), s.size()>3?s.subList(4,s.size()):null,this.ecouteurBoutons);
                        vbox.getChildren().add(dc);
                        j++;
                    }
                    i++;
                    
                    this.tournees.add(vbox);
                }
            }
            
            choixTournee.setItems(contenu);
        }
        
        for(VBox tournee : this.tournees){
            for(Node n : tournee.getChildren()){
                n.setStyle("-fx-border-color:black; -fx-border-width:2px;");
            }
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

                if(contenu.get(i).equals(s)){
                    this.panel.setContent(this.tournees.get(i));
                    return i;
                }
            }
        }else
            this.descriptionTournee.setText("");
        
        return -1;
    }
    
    /**
     * Met à jour le DescriptifChemin sélectionné ou désélectionné dans la Vue Textuelle.
     * @param dc - DescriptifChemin à mettre à jour
     * @see DescriptifChemin
     */
    public void majVueTextuelle(DescriptifChemin nouveauDescriptif){
        if(this.descriptifSelectionne!=null){
            this.descriptifSelectionne.setLocalise(false);
        }
        
        this.descriptifSelectionne = nouveauDescriptif;
        this.descriptifSelectionne.setLocalise(true);
    }
}
