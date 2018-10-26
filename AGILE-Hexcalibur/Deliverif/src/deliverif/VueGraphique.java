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
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import modele.outils.GestionLivraison;

/**
 *
 * @author Aurelien Belin
 */
public class VueGraphique extends Canvas implements Observer {
    
    private GestionLivraison gestionLivraison;
    private double echelleLat;
    private double echelleLong;
    private double echelle;
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
    HashMap<Long, Pair<Double,Double>> intersections;
    
    public VueGraphique(GestionLivraison gl){
        super(550,640-95);
        
        this.gestionLivraison = gl;
        gestionLivraison.addObserver(this);
        this.intersections = new HashMap<>();
        
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
        
        echelleLat = (640-95)/(maxLatitude-minLatitude);
        echelleLong = (550)/(maxLongitude-minLongitude); //longueur fenetre
        
        echelle = Math.min(echelleLat, echelleLong);
        
        //System.out.println("Echelle : "+echelleLat+" ; "+echelleLong); //DEBUG
        
        origineLatitude = minLatitude;
        origineLongitude = minLongitude;
    }
    
    public void dessinerPlan(){
        GraphicsContext gc = this.getGraphicsContext2D();
        List <modele.outils.Troncon> troncons = gestionLivraison.getPlan().getTroncons();
        //System.out.println("Chargement du plan : "+troncons.size()); //DEBUG
                
        for(modele.outils.Troncon troncon : troncons){
            double absDebutTroncon = (troncon.getDebut().getLongitude() - origineLongitude) * echelleLong; 
            double ordDebutTroncon = this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat; 
            double absFinTroncon = (troncon.getFin().getLongitude() - origineLongitude) * echelleLong; 
            double ordFinTroncon = this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat;
            
            intersections.put(troncon.getDebut().getIdXML(), new Pair<>(absDebutTroncon, ordDebutTroncon));
            intersections.put(troncon.getFin().getIdXML(), new Pair<>(absFinTroncon, ordFinTroncon));
            ///System.out.println(absDebutTroncon+" ; "+ordDebutTroncon); //DEBUG
            
            //Dessin des traits
            gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
            /*gc.fillOval(absDebutTroncon-3,ordDebutTroncon-3,3,3);
            gc.fillOval(absFinTroncon-3,ordFinTroncon-3,3,3);*/
        }
    }
    
    public void dessinerPtLivraison(){
        GraphicsContext gc = this.getGraphicsContext2D();
        List <modele.outils.PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(modele.outils.PointPassage livraison : livraisons){
            /*double abscissePtLivraison = (livraison.getPosition().getLongitude() - origineLongitude) * echelle;
            double ordonneePtLivraison = this.getHeight() - ( livraison.getPosition().getLatitude() - origineLatitude) * echelle;*/
            //System.out.println(abscissePtLivraison+" : "+ordonneePtLivraison); //DEBUG
            double abscissePtLivraison = intersections.get(livraison.getPosition().getIdXML()).getKey();
            double ordonneePtLivraison = intersections.get(livraison.getPosition().getIdXML()).getValue();
            
            //Dessin marqueur
            gc.setFill(Color.BLUE);
            gc.fillOval(abscissePtLivraison-4, ordonneePtLivraison-4, 8, 8);
   
        }
        
        double abscissePtLivraison = intersections.get(gestionLivraison.getDemande().getEntrepot().getPosition().getIdXML()).getKey();
        double ordonneePtLivraison = intersections.get(gestionLivraison.getDemande().getEntrepot().getPosition().getIdXML()).getValue();
        gc.setFill(Color.GREEN);
        
        gc.fillOval(abscissePtLivraison-4, ordonneePtLivraison-4, 8, 8);
        
    }
    
}
