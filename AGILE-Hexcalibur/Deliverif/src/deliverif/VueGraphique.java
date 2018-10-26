/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif;

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
public class VueGraphique extends HBox implements Observer {
    
    private GestionLivraison gestionLivraison;
    private float echelle;
    private float origineLatitude;
    private float origineLongitude;
    
    //Etats
    boolean init;
    boolean planCharge;
    boolean dlCharge;
    boolean calculRealise;
    
    //Composants
    
    public VueGraphique(GestionLivraison gl){
        super();
        
        this.gestionLivraison = gl;
        gestionLivraison.addObserver(this);
        
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
        float maxLatitude = 90;
        float minLatitude = -90;
        float maxLongitude = 180;
        float minLongitude = -180;
        
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
        
        float echelleLatitude = (abs(maxLatitude-minLatitude)) / 1; //hauteur fenetre
        float echelleLongitude = (abs(maxLongitude-minLongitude)) / 1; //longueur fenetre
        
        origineLatitude = minLatitude;
        origineLongitude = minLongitude;
        
        echelle = Float.max(echelleLatitude, echelleLongitude);
    }
    
    public void dessinerPlan(){
        List <modele.outils.Troncon> troncons = gestionLivraison.getPlan().getTroncons();
        
        for(modele.outils.Troncon troncon : troncons){
            float absDebutTroncon = (troncon.getDebut().getLongitude() - origineLongitude) / echelle; 
            float ordDebutTroncon = (troncon.getDebut().getLatitude() - origineLongitude) / echelle; 
            float absFinTroncon = (troncon.getFin().getLongitude() - origineLongitude) / echelle; 
            float ordFinTroncon = (troncon.getFin().getLatitude() - origineLongitude) / echelle;
            
            //Dessin des traits
            Line route = new Line(0, 0, 400, 0);
        }
    }
    
    public void dessinerPtLivraison(){
        List <modele.outils.PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(modele.outils.PointPassage livraison : livraisons){
            float abscissePtLivraison = (livraison.getPosition().getLongitude() - origineLongitude) / echelle;
            float ordonneePtLivraison = (livraison.getPosition().getLatitude() - origineLatitude) / echelle;
            
            //Dessin marqueur
        }
    }
    
}
