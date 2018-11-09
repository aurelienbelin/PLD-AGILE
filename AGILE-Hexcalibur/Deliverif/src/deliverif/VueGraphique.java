/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package deliverif;

import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

import javafx.scene.input.MouseButton;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import modele.outils.Chemin;
import modele.outils.GestionLivraison;
import modele.outils.Tournee;
import modele.outils.Troncon;

/**
 * Classe implémentant le composant de Vue Graphique de l'IHM du projet ainsi que son comportement.
 * La Vue Graphique représente la description graphique des tournées de livraison à effectuer par les livreurs, ainsi que du plande la ville.
 * @author Aurelien Belin
 * @see StackPane
 * @see Deliverif
 * @see Observer
 */
public class VueGraphique extends StackPane implements Observer {
    
    private final GestionLivraison gestionLivraison;
    private double echelleLat;
    private double echelleLong;
    private double origineLatitude;
    private double origineLongitude;
    
    //Composants
    private final Color[] couleurs = {Color.BLUEVIOLET, Color.BROWN, Color.CHARTREUSE,Color.CORAL,Color.CRIMSON,Color.DARKBLUE, Color.DARKGREEN, Color.DEEPPINK, Color.GOLD, Color.LIGHTSALMON};
    private Canvas plan;
    private Canvas dl;
    private ArrayList<Canvas> tournees;
    private Canvas marker;
    private Image imageMarker;
    private Deliverif fenetre;
    

    /**
     * Constructeur de VueGraphique.
     * @param gl - point d'entrée du modèle observé
     * @param f - IHM principale dans laquelle est affichée la Vue Graphique
     * @see GestionLivraison
     * @see Deliverif
     */

    public VueGraphique(GestionLivraison gl, Deliverif f){
        super();
        this.fenetre=f;
        this.setPrefSize(640,640-95);
        
        this.gestionLivraison = gl;
        gestionLivraison.addObserver(this);
        
        plan = new Canvas(640,640-95);
        dl = new Canvas(640,640-95);
        tournees = new ArrayList<>();
        
        this.getChildren().addAll(plan,dl);
        
        imageMarker = new Image("/deliverif/Marker_1.png",true);
        this.marker = new Canvas(640,640-95);
   
    }
    
    /**
     * Met à jour la VueGraphique en fonction des données du modèle et de ses mises à jour.
     * @param o - Objet à observer, ici une instance de GestionLivraison
     * @param arg - contenu de la mise à jour 
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof modele.outils.PlanVille){
            calculEchelle(gestionLivraison.getPlan().getIntersections());
            dessinerPlan();
        } else if (arg instanceof modele.outils.DemandeLivraison){
            dessinerPtLivraison();
        } else if (arg instanceof modele.outils.Tournee[]){
            dessinerTournees();
        }
    }
    
    /**
     * Calcule l'échelle d'affichage du plan de la ville à afficher.
     * @param intersections - liste des intersections contenues dans le plan à afficher.
     */
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
    
    /**
     * Dessine le plan à l'échelle dans la VueGraphique.
     */
    public void dessinerPlan(){        
        GraphicsContext gc = this.plan.getGraphicsContext2D();
        gc.clearRect(0, 0, plan.getWidth(), plan.getHeight());
            
        GraphicsContext gc1 = this.dl.getGraphicsContext2D();
        gc1.clearRect(0, 0, dl.getWidth(), dl.getHeight());
        
        this.tournees.clear();
        
        Iterator<Node> iter = this.getChildren().iterator();
        while(iter.hasNext()) {
            Node n = iter.next();
            if( !n.equals(dl) && !n.equals(plan)){
                iter.remove();
            }
        }
            
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
    
    /**
     * Dessine les points de livraison à desservir sur le plan préalablement dessiné.
     */
    public void dessinerPtLivraison(){
        GraphicsContext gc = this.dl.getGraphicsContext2D();
        gc.clearRect(0, 0, dl.getWidth(), dl.getHeight());
        
        this.tournees.clear();
        
        Iterator<Node> iter = this.getChildren().iterator();
        while(iter.hasNext()) {
            Node n = iter.next();
            if( !n.equals(dl) && !n.equals(plan)){
                iter.remove();
            }
        }
        
        List <modele.outils.PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(modele.outils.PointPassage livraison : livraisons){
            double[] ptLivraison = { 
                                    livraison.getPosition().getLongitude(),
                                    livraison.getPosition().getLatitude()
            };
            ptLivraison = this.mettreCoordonneesALechelle(ptLivraison, false);

            //Dessin marqueur
            gc.setFill(Color.BLUE);
            gc.fillOval(ptLivraison[0]-4, ptLivraison[1]-4, 8, 8);
   
        }
        double[] ptLivraison = { 
                                    gestionLivraison.getDemande().getEntrepot().getPosition().getLongitude(),
                                    gestionLivraison.getDemande().getEntrepot().getPosition().getLatitude()
            };
            ptLivraison = this.mettreCoordonneesALechelle(ptLivraison, false);
        gc.setFill(Color.RED);
        
        gc.fillOval(ptLivraison[0]-4, ptLivraison[1]-4, 8, 8);

        
    }
    
    /**
     * Dessine les tournées à effectuer pour desservir tous les points de livraison préalablement affichée sur le plan.
     */
    public void dessinerTournees(){
        System.out.println("Je commence à dessiner les tournées !");
        
        Tournee[] listeTournees = this.gestionLivraison.getTournees();
        
        //Canvas canvasTemp;
        int nCouleur=0;
        int i=0;
        
        for(Tournee tournee : listeTournees){
            //On créée un nouveau Canvas par tournée
            //canvasTemp = new Canvas(this.getWidth(),this.getHeight());
            //GraphicsContext gc = canvasTemp.getGraphicsContext2D();
            GraphicsContext gc = this.tournees.get(i).getGraphicsContext2D();
            gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            
            List<Chemin> chemins = tournee.getTrajet();
            
            //Changer de couleur
            /*int couleur = (int)(Math.random()*0xFFFFFF);
            String couleur_hex = Integer.toHexString(couleur);
            gc.setLineWidth(3);
            gc.setStroke(Color.web("#"+couleur_hex.substring(2,couleur_hex.length())));*/
            gc.setLineWidth(3);
            gc.setStroke(couleurs[nCouleur]);
            
            for(Chemin chemin : chemins){
                List<Troncon> troncons = chemin.getTroncons();
                
                for(Troncon troncon : troncons){
                    double[] ptDebutTroncon = { 
                                    troncon.getDebut().getLongitude(),
                                    troncon.getDebut().getLatitude()
                    };
                    ptDebutTroncon = this.mettreCoordonneesALechelle(ptDebutTroncon, false);
                    double[] ptFinTroncon = { 
                                    troncon.getFin().getLongitude(),
                                    troncon.getFin().getLatitude()
                    };
                    ptFinTroncon = this.mettreCoordonneesALechelle(ptFinTroncon, false);
                    
                   // int absDebutTroncon =(int) ((troncon.getDebut().getLongitude() - origineLongitude) * echelleLong); 
                   // int ordDebutTroncon =(int) (this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat); 
                   // int absFinTroncon = (int)((troncon.getFin().getLongitude() - origineLongitude) * echelleLong); 
                   // int ordFinTroncon = (int)(this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat);
                    
                    gc.strokeLine(ptDebutTroncon[0],ptDebutTroncon[1],ptFinTroncon[0],ptFinTroncon[1]);
                    //gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
                }
            }
            
            i++;
            nCouleur++;
        }
        

        fenetre.informationEnCours("");
        /*this.getChildren().addAll(this.tournees);
        this.getChildren().get(0).toBack();
        this.getChildren().get(1).toFront();
        this.getChildren().add(this.marker);*/
        
        System.out.println("J'ai fini les tournées !");
    }
    
    /**
     * 
     * @param nb 
     */
    public void creerCalques(int nb){
        this.tournees.clear();
        
        Iterator<Node> iter = this.getChildren().iterator();
        while(iter.hasNext()) {
            Node n = iter.next();
            if( !n.equals(dl) && !n.equals(plan)){
                iter.remove();
            }
        }
        
        for(int i=0;i<nb;i++){
            Canvas canvasTemp = new Canvas(this.getWidth(),this.getHeight());
            this.tournees.add(canvasTemp);
        }
        
        this.getChildren().addAll(this.tournees);
        this.getChildren().get(0).toBack();
        this.getChildren().get(1).toFront();
        this.getChildren().add(this.marker);
    }
    
    /**
     * Change la tournée affichée pour la tournée dont l'indice est passée en paramètre.
     * @param numTournee - indice de la tournée à afficher
     */
    public void changerTourneeAffichee(int numTournee){
        for(int i=0;i<this.tournees.size();i++){
            this.tournees.get(i).setVisible(true);
            if(numTournee!=0 && i!=numTournee-1)
                this.tournees.get(i).setVisible(false);
        }
    }
    
    public double[] mettreCoordonneesALechelle(double[] pointAMAJ, boolean estCoordonneesVueGraphique)
    {
        double[] pointAJour = new double[2];
        if(estCoordonneesVueGraphique){
            pointAJour[0] = pointAMAJ[0] / echelleLong + origineLongitude;
            pointAJour[1] = (pointAMAJ[1] - this.getHeight()) / (-echelleLat) + origineLatitude;
        }
        else
        {
            pointAJour[0] = (pointAMAJ[0] - origineLongitude) * echelleLong;
            pointAJour[1] = this.getHeight() - (pointAMAJ[1] - origineLatitude) * echelleLat;
        }
        return pointAJour;
    }
    
    //Test
    public void effacerMarker() {
        this.marker.getGraphicsContext2D().clearRect(0,0,this.marker.getWidth(), this.marker.getHeight());
    }
    
    //Test
    public void ajouterMarker(double lat, double lon){
        int x = (int)((lon - origineLongitude)*echelleLong);
        int y = (int)(this.getHeight() - (lat - origineLatitude)*echelleLat);

        /*System.out.println("Entrepot : "+gestionLivraison.getDemande().getEntrepot().getPosition().getLongitude()+" ; "+gestionLivraison.getDemande().getEntrepot().getPosition().getLatitude());
        System.out.println("Je suis dans la vue graphique : "+lon+" ; "+lat); //DEBUG
        System.out.println("Je suis dans la vue graphique : "+this.imageMarker.getWidth()+" ; "+this.imageMarker.getHeight()); //DEBUG
        System.out.println("Je suis dans la vue graphique : "+x+" ; "+y); //DEBUG*/

        GraphicsContext gc = this.marker.getGraphicsContext2D();
        gc.drawImage(imageMarker, x - this.imageMarker.getWidth()/2.0, y - this.imageMarker.getHeight());
    }
    
    public void identifierPtPassage(DescriptifChemin dc, double lat, double lon){
        this.effacerMarker();
        
        if(!dc.estLocalise()){
            this.ajouterMarker(lat,lon);
        }
    }
    

    public void zoomPlus(double lat, double lon){
        
        
         
        origineLongitude+=(lon-origineLongitude)*0.2/1.2;
        origineLatitude+= (lat-origineLatitude)*0.2/1.2;
        echelleLong = echelleLong *1.2;
        echelleLat=echelleLat*1.2;
          
                    
    }
    public void zoomMoins(double lat, double lon){
        
        echelleLong = echelleLong /1.2;
        echelleLat=echelleLat/1.2; 
        origineLongitude-=(lon-origineLongitude)*0.2;
        origineLatitude-= (lat-origineLatitude)*0.2;
        
              
    }
}
