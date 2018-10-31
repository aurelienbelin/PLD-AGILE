/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package deliverif;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import modele.outils.Chemin;
import modele.outils.GestionLivraison;
import modele.outils.Tournee;
import modele.outils.Troncon;

/**
 *
 * @author Aurelien Belin
 */
public class VueGraphique extends StackPane implements Observer {
    
    private final GestionLivraison gestionLivraison;
    private double echelleLat;
    private double echelleLong;
    private double origineLatitude;
    private double origineLongitude;
    
    //Etats
    private boolean init;
    private boolean planCharge;
    private boolean dlCharge;
    private boolean calculRealise;
    
    //Composants
    private Canvas plan;
    private Canvas dl;
    private ArrayList<Canvas> tournees;
    
    //Test
    private Button bouton;
    
    public VueGraphique(GestionLivraison gl){
        super();
        
        this.setPrefSize(640,640-95);
        
        this.gestionLivraison = gl;
        gestionLivraison.addObserver(this);
        
        plan = new Canvas(640,640-95);
        dl = new Canvas(640,640-95);
        tournees = new ArrayList<>();
        
        this.getChildren().addAll(plan,dl);
        
        /*//Test
        bouton = new Button("Test");
        bouton.setPrefSize(50,50);
        bouton.setLayoutX(0);
        bouton.setLayoutY(0);
        thi.getChildren().add(bouton)*/
        
        init = true;     
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        if(init==true) {
            calculEchelle(gestionLivraison.getPlan().getIntersections());
            dessinerPlan();
            planCharge = true;
            init =false;
        }else if(planCharge==true) {
            dessinerPtLivraison();
            dlCharge = true;
            planCharge = false;
        }else if(dlCharge==true){
            dessinerTournees();
        }
    }
    
    public void calculEchelle (List <modele.outils.Intersection> intersections) {
        float maxLatitude = -90;
        float minLatitude = 90;
        float maxLongitude = -180;
        float minLongitude = 180;
        
        for(modele.outils.Intersection i: intersections){
            if(i.getLatitude() > maxLatitude){
                maxLatitude = i.getLatitude();
            }else if(i.getLatitude() < minLatitude){
                minLatitude = i.getLatitude();
            }
            if(i.getLongitude() > maxLongitude){
                maxLongitude = i.getLongitude();
            }else if(i.getLongitude() < minLongitude){
                minLongitude = i.getLongitude();
            }
        }
        
        echelleLat = (640-95)/(maxLatitude-minLatitude);
        echelleLong = (640)/(maxLongitude-minLongitude); //longueur fenetre
        
        origineLatitude = minLatitude;
        origineLongitude = minLongitude;
    }
    
    public void dessinerPlan(){
        GraphicsContext gc = this.plan.getGraphicsContext2D();
        gc.setStroke(Color.SLATEGREY);
        List <modele.outils.Troncon> troncons = gestionLivraison.getPlan().getTroncons();
                
        for(modele.outils.Troncon troncon : troncons){
            int absDebutTroncon =(int) ((troncon.getDebut().getLongitude() - origineLongitude) * echelleLong); 
            int ordDebutTroncon =(int) (this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat); 
            int absFinTroncon = (int)((troncon.getFin().getLongitude() - origineLongitude) * echelleLong); 
            int ordFinTroncon = (int)(this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat);
            
            //Dessin des traits
            gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
        }
    }
    
    public void dessinerPtLivraison(){
        GraphicsContext gc = this.dl.getGraphicsContext2D();
        List <modele.outils.PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(modele.outils.PointPassage livraison : livraisons){
            int abscissePtLivraison = (int)((livraison.getPosition().getLongitude() - origineLongitude) * echelleLong);
            int ordonneePtLivraison = (int)(this.getHeight() - ( livraison.getPosition().getLatitude() - origineLatitude) * echelleLat);
            
            //Dessin marqueur
            gc.setFill(Color.BLUE);
            gc.fillOval(abscissePtLivraison-4, ordonneePtLivraison-4, 8, 8);
   
        }
        
        int abscissePtLivraison =(int) ((gestionLivraison.getDemande().getEntrepot().getPosition().getLongitude() - origineLongitude) * echelleLong);
        int ordonneePtLivraison =(int) (this.getHeight() - ( gestionLivraison.getDemande().getEntrepot().getPosition().getLatitude() - origineLatitude) * echelleLat);
        gc.setFill(Color.RED);
        
        gc.fillOval(abscissePtLivraison-4, ordonneePtLivraison-4, 8, 8);
        
    }
    
    //Début : 30min + 14h15-
    public void dessinerTournees(){
        this.tournees.clear();
        
        for(Node n : this.getChildren()){
            if(!n.equals(this.bouton) && !n.equals(this.dl) && !n.equals(this.plan))
                this.getChildren().remove(n);
        }
        
        Tournee[] listeTournees = this.gestionLivraison.getTournees();
        
        Canvas canvasTemp;
        
        for(Tournee tournee : listeTournees){  
            //On créée un nouveau Canvas par tournée
            canvasTemp = new Canvas(this.getWidth(),this.getHeight());
            GraphicsContext gc = canvasTemp.getGraphicsContext2D();
            
            List<Chemin> chemins = tournee.getTrajet();
            
            //Changer de couleur
            int couleur = (int)(Math.random()*0xFFFFFF);
            String couleur_hex = Integer.toHexString(couleur);
            gc.setLineWidth(2);
            gc.setStroke(Color.web("#"+couleur_hex.substring(2,couleur_hex.length())));
            
            for(Chemin chemin : chemins){
                List<Troncon> troncons = chemin.getTroncons();
                
                for(Troncon troncon : troncons){
                    int absDebutTroncon =(int) ((troncon.getDebut().getLongitude() - origineLongitude) * echelleLong); 
                    int ordDebutTroncon =(int) (this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat); 
                    int absFinTroncon = (int)((troncon.getFin().getLongitude() - origineLongitude) * echelleLong); 
                    int ordFinTroncon = (int)(this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat);
                    
                    gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
                }
            }
            
            this.tournees.add(canvasTemp);
        }
        
        this.getChildren().addAll(this.tournees);
        this.getChildren().get(0).toBack();
        this.getChildren().get(1).toFront();
        
    }
    
    //Test
    private void activerBouton(){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Test");
                tournees.get(1).getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
            }
        });
    }
    
}
