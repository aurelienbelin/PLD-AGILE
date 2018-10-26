/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package deliverif;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import modele.outils.GestionLivraison;

import static java.lang.Math.abs;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.layout.HBox;
import modele.outils.GestionLivraison;
import javafx.scene.shape.Line;

/**
 *
 * @author Aurelien Belin
 */
public class VueGraphique extends Canvas implements Observer {
    
    private GestionLivraison gestionLivraison;
    private double echelleLat;
    private double echelleLong;
    private double origineLatitude;
    private double origineLongitude;
    private double origineX;
    private double origineY;
    
    //Etats
    boolean init;
    boolean planCharge;
    boolean dlCharge;
    boolean calculRealise;
    
    //Composants
    private ArrayList<Line> troncons;
    private ArrayList<Circle> pointsPassage;
    
    public VueGraphique(GestionLivraison gl){
        super(640,640);
        
        this.gestionLivraison = gl;
        gestionLivraison.addObserver(this);
        this.troncons = new ArrayList();
        this.pointsPassage = new ArrayList();
        
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
        }
    }
    
    public void calculEchelle (List <modele.outils.Intersection> intersections) {
        //final int R = 6371; //Rayon de la Terre
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
        
        //Conversion de polaire en cartésien + mise à l'échelle
        /*double maxX = R*Math.cos(maxLatitude)*Math.cos(maxLongitude);
        double minX = R*Math.cos(minLatitude)*Math.cos(minLongitude);
        double maxY = R*Math.cos(maxLatitude)*Math.sin(maxLongitude);
        double minY = R*Math.cos(minLatitude)*Math.sin(minLongitude);*/
        
        echelleLat = 640/(maxLatitude-minLatitude);
        echelleLong = 640/(maxLongitude-minLongitude); //longueur fenetre
        
        System.out.println("Echelle : "+echelleLat+" ; "+echelleLong);
        
        /*origineX = R*Math.cos(minLatitude)*Math.cos(minLongitude);
        origineY = R*Math.cos(minLatitude)*Math.sin(minLongitude);*/
        
        origineLatitude = minLatitude;
        origineLongitude = minLongitude;
    }
    
    public void dessinerPlan(){
        //final int R = 6371; //Rayon de la Terre
        GraphicsContext gc = this.getGraphicsContext2D();
        List <modele.outils.Troncon> troncons = gestionLivraison.getPlan().getTroncons();
        //System.out.println("Chargement du plan : "+troncons.size()); //DEBUG
                
        for(modele.outils.Troncon troncon : troncons){
            /*double absDebutTroncon = (R*Math.cos(troncon.getDebut().getLatitude())*Math.cos(troncon.getDebut().getLongitude()) - origineX) * echelleX; 
            double ordDebutTroncon = (R*Math.cos(troncon.getDebut().getLatitude())*Math.sin(troncon.getDebut().getLongitude()) - origineY) * echelleY; 
            double absFinTroncon = (R*Math.cos(troncon.getFin().getLatitude())*Math.cos(troncon.getFin().getLongitude()) - origineX) * echelleX; 
            double ordFinTroncon = (R*Math.cos(troncon.getDebut().getLatitude())*Math.sin(troncon.getDebut().getLongitude()) - origineY) * echelleY;*/
            double absDebutTroncon = (troncon.getDebut().getLongitude() - origineLongitude) * echelleLong; 
            double ordDebutTroncon = this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat; 
            double absFinTroncon = (troncon.getFin().getLongitude() - origineLongitude) * echelleLong; 
            double ordFinTroncon = this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat;
            ///System.out.println(absDebutTroncon+" ; "+ordDebutTroncon); //DEBUG
            
            //Dessin des traits
            gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
        }
    }
    
    public void dessinerPtLivraison(){
        GraphicsContext gc = this.getGraphicsContext2D();
        List <modele.outils.PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(modele.outils.PointPassage livraison : livraisons){
            double abscissePtLivraison = (livraison.getPosition().getLongitude() - origineLongitude) * echelleLong;
            double ordonneePtLivraison = this.getHeight() - ( livraison.getPosition().getLatitude() - origineLatitude) * echelleLat;
            //System.out.println(abscissePtLivraison+" : "+ordonneePtLivraison); //DEBUG
            
            //Dessin marqueur
            /*Circle cercle = new Circle(abscissePtLivraison,ordonneePtLivraison,2);
            this.pointsPassage.add(cercle);*/
            gc.fillOval(abscissePtLivraison-10, ordonneePtLivraison-10, 10, 10);
        }
    }
    
}
