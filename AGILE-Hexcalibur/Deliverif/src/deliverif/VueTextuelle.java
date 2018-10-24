/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import modele.outils.GestionLivraison;

/**
 *
 * @author Aurelien Belin
 */
public class VueTextuelle extends VBox implements Observer {
    
    private GestionLivraison gestionLivraison;
    private ArrayList<String> descriptions;    
    
    //Composants
    private ComboBox choixTournee;
    private Label descriptionTournee;
    
    public VueTextuelle(GestionLivraison gl){
        super();
        this.gestionLivraison = gl;
        
        this.setSpacing(10);
        this.setPrefSize(285,420);
        
        this.choixTournee = new ComboBox();
        this.choixTournee.setPrefWidth(375);
        
        ScrollPane panel = new ScrollPane();
        panel.setPrefSize(285,375);
        
        this.descriptionTournee = new Label();
        this.descriptionTournee.setPrefSize(panel.getWidth(),panel.getHeight());
        
        panel.setContent(this.descriptionTournee);
        
        this.getChildren().addAll(choixTournee,panel);
        
        this.descriptions = new ArrayList<>();
        
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
