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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import modele.outils.DemandeLivraison;
import modele.outils.GestionLivraison;
import modele.outils.Tournee;

/**
 *
 * @author Aurelien Belin
 */
public class VueTextuelle extends VBox implements Observer {
    
    private GestionLivraison gestionLivraison;
    private ArrayList<String> descriptions;
    private ObservableList<String> contenu;
    
    //Composants
    private ComboBox<String> choixTournee;
    private Label descriptionTournee;
    
    /**
     *
     * @param gl
     */
    public VueTextuelle(GestionLivraison gl){
        super();
        
        this.gestionLivraison = gl;
        this.gestionLivraison.addObserver(this);
        this.descriptions = new ArrayList<>();
        this.contenu = FXCollections.observableArrayList();
        
        this.setSpacing(10);
        this.setPrefSize(285,420);
        this.setMinHeight(420);
        
        this.choixTournee = new ComboBox();
        this.choixTournee.setPrefWidth(375);
        
        //Test
        /*contenu = FXCollections.observableArrayList("Option 1","Option 2","Option 3");
        choixTournee.setItems(contenu);
        descriptions.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. In et nisi maximus, laoreet dolor eget, lacinia dolor. In quis felis laoreet, luctus risus vel, tempus tellus. Morbi tempor, felis feugiat molestie ullamcorper, dolor erat sollicitudin metus, quis porttitor metus sapien gravida lacus. Vivamus eget rhoncus nisl. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tincidunt volutpat enim eget blandit. Morbi dolor urna, lacinia eget nibh non, egestas varius tortor. Aenean mattis volutpat neque ac consequat. Curabitur scelerisque erat est, at molestie magna auctor at. Sed eget massa et tellus posuere vulputate. Ut neque lectus, facilisis maximus massa vel, sollicitudin dignissim ex. Nullam vel odio est.\n" +
"\n" +
"Fusce ut justo cursus, maximus tortor ut, consequat massa. Ut efficitur semper nisl a eleifend. Nulla tincidunt dui metus, sed mollis orci consectetur ac. Suspendisse hendrerit leo turpis, et ultrices tellus fermentum vitae. Nunc at est eros. Suspendisse facilisis porttitor lobortis. Aliquam urna quam, rhoncus id gravida ac, luctus eget magna. Praesent scelerisque suscipit dui sit amet blandit. Phasellus nulla justo, aliquet ac euismod vitae, pretium id mi. Ut ex quam, commodo sit amet nisl eget, consequat aliquam metus.");
        descriptions.add("In malesuada ut justo nec suscipit. Curabitur ut ex eget ipsum pharetra luctus. Donec et posuere orci. Suspendisse vitae mollis enim. Nunc accumsan felis mattis mi pharetra mollis. Phasellus malesuada laoreet neque. Maecenas sit amet mi at velit condimentum tempus. Morbi molestie tellus a quam vulputate gravida. Suspendisse sit amet mauris non leo porttitor sagittis. Mauris venenatis lacus nec elementum aliquet. In ornare diam vitae ipsum interdum, vel tristique neque malesuada. Ut nec feugiat mi.");
        descriptions.add("Coucou !");*/
        
        ScrollPane panel = new ScrollPane();
        panel.setPrefSize(285,375);
        
        this.descriptionTournee = new Label();
        this.descriptionTournee.setMaxWidth(285);
        this.descriptionTournee.setWrapText(true);
        
        panel.setContent(this.descriptionTournee);
        
        this.getChildren().addAll(choixTournee,panel);
        
    }

    /**
     *
     * @param ec
     */
    public void ajouterEcouteur(EcouteurBoutons ec){
        choixTournee.setOnAction(e->{
            try {
                ec.changerTourneeAffichee((ActionEvent) e);
            } catch (InterruptedException ex) {
                Logger.getLogger(VueTextuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     *
     */
    public int changerDescriptionAffichee(){
        String s = choixTournee.getSelectionModel().getSelectedItem();

        if(s!=null || s.equals("")){
            for(int i=0;i<contenu.size();i++){
                if(contenu.get(i).equals(s)){
                    this.descriptionTournee.setText("");
                    this.descriptionTournee.setText(this.descriptions.get(i));
                    return i;
                }
            }
        }
        
        return -1;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        //choixTournee.getItems().clear();
        contenu.clear();
        descriptions.clear();
        this.descriptionTournee.setText("");
        
        if(this.gestionLivraison.getDemande()!=null){
            DemandeLivraison demande = this.gestionLivraison.getDemande();
            
            String des;
            des=new String("");
            Iterator<String> it = demande.getDescription();
            contenu.add("Demande de livraison");
            while(it.hasNext()){
                String s = it.next();
                des+="\n\t"+s;
            }

            descriptions.add(des);
            
            choixTournee.setItems(contenu);
        }
        
        if(this.gestionLivraison.getTournees()!=null){
            Tournee[] tournees = this.gestionLivraison.getTournees();
            
            String des;
            if(tournees.length!=0){
                int i = 1;
                for(Tournee t : tournees){
                    des="";
                    Iterator<String> it = t.getDescription();
                    contenu.add("Tournée "+i);
                    while(it.hasNext()){
                        String s = it.next();
                        des+="\n\t"+s;
                    }

                    descriptions.add(des);
                    i++;
                }
            }
            
            choixTournee.setItems(contenu);
        }
        
    }
}
