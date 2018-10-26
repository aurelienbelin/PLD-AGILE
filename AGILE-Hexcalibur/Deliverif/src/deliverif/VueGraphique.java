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

/**
 *
 * @author Aurelien Belin
 */
public class VueGraphique extends Canvas implements Observer {
    
    private GestionLivraison gestionLivraison;
    private double echelle;
    private double origineLatitude;
    private double origineLongitude;
    
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
        
        double echelleLatitude = (abs(maxLatitude-minLatitude)) / 640; //hauteur fenetre
        double echelleLongitude = (abs(maxLongitude-minLongitude)) / 640; //longueur fenetre
        
        origineLatitude = minLatitude;
        origineLongitude = minLongitude;
        
        echelle = Double.max(echelleLatitude, echelleLongitude);
        System.out.println("Echelle : "+echelle);
    }
    
    public void dessinerPlan(){
        GraphicsContext gc = this.getGraphicsContext2D();
        /*List <modele.outils.Troncon> troncons = gestionLivraison.getPlan().getTroncons();
        System.out.println("Chargement du plan : "+troncons.size()); //DEBUG
                
        for(modele.outils.Troncon troncon : troncons){
            double absDebutTroncon = (troncon.getDebut().getLongitude() - origineLongitude) / echelle; 
            double ordDebutTroncon = (this.getHeight() - troncon.getDebut().getLatitude() - origineLongitude) / echelle; 
            double absFinTroncon = (troncon.getFin().getLongitude() - origineLongitude) / echelle; 
            double ordFinTroncon = (this.getHeight() - troncon.getFin().getLatitude() - origineLatitude) / echelle;
            System.out.println(absDebutTroncon+" : "+troncon.getDebut().getLongitude()+" ; "+ordDebutTroncon+" : "+troncon.getDebut().getLatitude()); //DEBUG
            
            //Dessin des traits
            //Line ligne = new Line(absDebutTroncon, ordDebutTroncon, absFinTroncon, ordFinTroncon);
            //this.troncons.add(ligne);
            gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
        }*/
        //gc.strokeLine(100,100,200,200); //DEBUG=> fontionne, donc c'est bien un problème de conversion
    }
    
    public void dessinerPtLivraison(){
        /*List <modele.outils.PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(modele.outils.PointPassage livraison : livraisons){
            double abscissePtLivraison = (livraison.getPosition().getLongitude() - origineLongitude) / echelle;
            double ordonneePtLivraison = (this.getHeight() - livraison.getPosition().getLatitude() - origineLatitude) / echelle;
            
            //Dessin marqueur
            Circle cercle = new Circle(abscissePtLivraison,ordonneePtLivraison,2);
            this.pointsPassage.add(cercle);
        }*/
    }
    
}
