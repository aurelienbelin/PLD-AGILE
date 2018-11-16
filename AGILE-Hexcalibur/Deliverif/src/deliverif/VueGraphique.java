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
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.CubicCurve;
import javafx.util.Pair;
import modele.Chemin;
import modele.DemandeLivraison;
import modele.GestionLivraison;
import modele.PlanVille;
import modele.PointPassage;
import modele.Tournee;
import modele.Troncon;

/**
 * Classe implémentant le composant de Vue Graphique de l'IHM du projet ainsi que son comportement.
 * La Vue Graphique représente la description graphique des tournées de livraison à effectuer par les livreurs, ainsi que du plande la ville.
 * @author Aurelien Belin
 * @see StackPane
 * @see Deliverif
 * @see Observer
 */
public class VueGraphique extends StackPane implements Observer {

    //Labels
    private final String NOM_MARQUEUR = "/deliverif/Marqueur.png";
    private final String NOM_MARQUEUR_MODIF = "/deliverif/Marqueur_Modif.png";
    
    //Constantes
    private final double coefMultiplicateurZoom = 0.2;
    private final double coefDiviseurZoom = 1.2;
    
    private final GestionLivraison gestionLivraison;
    private double echelleLat;
    private double echelleLong;
    private double origineLatitude;
    private double origineLongitude;
    private double origineLatitudeMin;
    private double origineLongitudeMin;
    private double echelleLatitudeMin;
    private double echelleLongitudeMin;

    private Deliverif fenetre;
    
    
    //Couleurs
    private final String[] NOMS_COULEURS = {"violet","marron","vert fluo","corail","rouge","bleu","vert foncé", "rose","or","beige"};
    private final Color[] couleursDbt = {Color.BLUEVIOLET, Color.BROWN, Color.CHARTREUSE,Color.CORAL,Color.CRIMSON,Color.DARKBLUE, Color.DARKGREEN, Color.DEEPPINK, Color.GOLD, Color.LIGHTSALMON};
    private final Color[] couleursCible = {new Color(0.85,0.723,0.96,1), new Color(0.88,0.72,0.72,1), new Color(0.83,1.0,2.0/3.0,1), new Color(1.0,0.83,0.77,1), new Color(0.955,0.69,0.745,1), new Color(2.0/3.0,2.0/3.0,0.848,1), new Color(2.0/3.0, 0.797, 2.0/3.0,1), new Color(1.0,0.693,0.859,1), new Color(1.0,0.95,2.0/3.0,1), new Color(1.0,0.876,0.83,1.0)};
    
    //Composants
    private Canvas plan;
    private Canvas dl;
    private ArrayList<Canvas> tournees;
    private Canvas marqueurSelectionLivraison;
    private Canvas marqueurModif;
    private Image imageMarqueurSelection;
    private Image imageMarqueurModif;
    private Pair<Double, Double> positionMarqueur;
    private Pair<Double, Double> positionMarqueurAModifier;
    
    
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
        
        imageMarqueurSelection = new Image(NOM_MARQUEUR,true);
        imageMarqueurModif = new Image(NOM_MARQUEUR_MODIF, true);
        this.marqueurSelectionLivraison = new Canvas(640,640-95);
        this.marqueurModif = new Canvas(640,640-95);
    }
    
    /**
     * Met à jour la VueGraphique en fonction des données du modèle et de ses mises à jour.
     * @param o - Objet à observer, ici une instance de GestionLivraison
     * @param arg - contenu de la mise à jour 
     */
    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(arg instanceof PlanVille){
                    calculEchelle(gestionLivraison.getPlan().getIntersections());
                    dessinerPlan();
                } else if (arg instanceof DemandeLivraison){
                    dessinerPtLivraison();
                } else if (arg instanceof Tournee[]){
                    dessinerPtLivraison();
                    dessinerTournees();
                }
            }
        });
    }
    
    /**
     * Calcule l'échelle d'affichage du plan de la ville à afficher.
     * @param intersections - liste des intersections contenues dans le plan à afficher.
     */
    public void calculEchelle (List <modele.Intersection> intersections) {
        float maxLatitude = -90;
        float minLatitude = 90;
        float maxLongitude = -180;
        float minLongitude = 180;
        
        for(modele.Intersection i: intersections){
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
        
        echelleLatitudeMin = echelleLat;
        echelleLongitudeMin = echelleLong;
        
        origineLatitude = minLatitude;
        origineLongitude = minLongitude;
        
        origineLatitudeMin = minLatitude;
        origineLongitudeMin = minLongitude;
    }
    
    /**
     * Dessine le plan à l'échelle dans la VueGraphique.
     */
    public void dessinerPlan(){      
        GraphicsContext gc = this.plan.getGraphicsContext2D();
        gc.clearRect(0, 0, plan.getWidth(), plan.getHeight());
            
        GraphicsContext gc1 = this.dl.getGraphicsContext2D();
        gc1.clearRect(0, 0, dl.getWidth(), dl.getHeight());
        
        //this.tournees.clear();
        
        Iterator<Node> iter = this.getChildren().iterator();
        while(iter.hasNext()) {
            Node n = iter.next();
            if( !n.equals(dl) && !n.equals(plan)){
                iter.remove();
            }
        }
            
        gc.setStroke(Color.SLATEGREY);
        List <Troncon> troncons = gestionLivraison.getPlan().getTroncons();
                
        for(Troncon troncon : troncons){
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
        
        effacerDl();
        
        List <PointPassage> livraisons = gestionLivraison.getDemande().getLivraisons();
        
        for(PointPassage livraison : livraisons){
            double[] ptLivraison = { 
                                    livraison.getPosition().getLongitude(),
                                    livraison.getPosition().getLatitude()
            };
            ptLivraison = this.mettreCoordonneesALechelle(ptLivraison, false);

            //Dessin marqueur
            gc.setFill(Color.BLUE);
            gc.fillOval(ptLivraison[0]-4, ptLivraison[1]-4, 8, 8);
        }
    
        if(gestionLivraison.getDemande().getEntrepot() != null)
        {
            double[] ptLivraison = { 
                                    gestionLivraison.getDemande().getEntrepot().getPosition().getLongitude(),
                                    gestionLivraison.getDemande().getEntrepot().getPosition().getLatitude()
            };
            ptLivraison = this.mettreCoordonneesALechelle(ptLivraison, false);
            gc.setFill(Color.RED);
            gc.fillOval(ptLivraison[0]-4, ptLivraison[1]-4, 8, 8);
        }
        
        this.getChildren().removeAll(this.dl,this.marqueurSelectionLivraison, this.marqueurModif);
        this.getChildren().addAll(this.dl,this.marqueurSelectionLivraison, this.marqueurModif);
    }
    
    /**
     * Dessine les tournées à effectuer pour desservir tous les points de livraison préalablement affichée sur le plan.
     */
    public void dessinerTournees(){
        Tournee[] listeTournees = this.gestionLivraison.getTournees();
        int numTournee=0;
        
        Color[] degradeTournee;
        
        if(listeTournees!=null){
            for(Tournee tournee : listeTournees){            
                GraphicsContext gc = this.tournees.get(numTournee).getGraphicsContext2D();
                gc.clearRect(0, 0, this.getWidth(), this.getHeight());

                if(tournee != null){
                    List<Chemin> chemins = tournee.getTrajet();
                    
                    gc.setLineWidth(3);
                    gc.setLineDashes(0,0);
                    
                    degradeTournee = calculDegrade(couleursDbt[numTournee], couleursCible[numTournee], 1.0, chemins.size()+1);
                    
                    int numChemin=0;
                    for(Chemin chemin : chemins){
                        List<Troncon> troncons = chemin.getTroncons();
                        Color[] degradeChemin = calculDegrade(degradeTournee[numChemin], degradeTournee[numChemin+1], 1.0, troncons.size()+1);
                        
                        int numTroncon=0;
                        for(Troncon troncon : troncons){
                           int absDebutTroncon =(int) ((troncon.getDebut().getLongitude() - origineLongitude) * echelleLong); 
                           int ordDebutTroncon =(int) (this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat); 
                           int absFinTroncon = (int)((troncon.getFin().getLongitude() - origineLongitude) * echelleLong); 
                           int ordFinTroncon = (int)(this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat);
                           
                           //Test
                           /*int t;
                           if(absFinTroncon-absDebutTroncon>0){
                               t = 1;
                           }else
                               t = -1;
                           
                           int longueurY = ordFinTroncon-ordDebutTroncon;
                           
                           ordDebutTroncon = (int)(ordDebutTroncon-((double)t*(absFinTroncon-absDebutTroncon))/(ordFinTroncon-ordDebutTroncon));
                           ordFinTroncon = ordDebutTroncon + longueurY;
                           absDebutTroncon += t;
                           absFinTroncon += t;*/
                           
                           //faire un gc.setStroke() du gradient à faire sur la ligne uniquement :) (ce qui implique de recalculer à chaque fois la prochaine couleur).
                           Stop[] stops = new Stop[] { new Stop(0, degradeChemin[numTroncon]), new Stop(1, degradeChemin[numTroncon+1])};
                           gc.setStroke(new LinearGradient(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon, true, CycleMethod.NO_CYCLE, stops));
                           gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
                           numTroncon++;
                        }
                        numChemin++;
                    }
                    
                    this.getChildren().remove(this.tournees.get(numTournee));
                    this.getChildren().add(numTournee+1, this.tournees.get(numTournee));
                }

                numTournee++;
            }
        }
    }
    
    //test
    protected void calculCouleursFin(){
        int i=0;
        
        Color bl = Color.WHITE;
        
        for(Color c : couleursDbt){
            System.out.println("Couleur n° "+i);
            
            double red = c.getRed() + 2*Math.abs(c.getRed() - bl.getRed())/3;
            double green = c.getGreen() + 2*Math.abs(c.getGreen() - bl.getGreen())/3;
            double blue = c.getBlue() + 2*Math.abs(c.getBlue() - bl.getBlue())/3;
            
            System.out.println(red+" ; "+green+" ; "+blue);
            System.out.println();
            i++;
        }
    }
    
    /**
     * Détermine les diffénrentes couleurs intermédiaires pour réaliser un dégradé d'une couleur d'origine à une couleur cible en nbPoints étapes.
     * @param debut - Couleur d'origine
     * @param fin - Couleur cible
     * @param transparence - transparence souhaité pour le dégradé
     * @param nbPoints - nombre de points nécessaires (discrétisation du dégradé)
     * @return Une collection de couleur correspondantes aux couleurs intermédiaires pour réaliser le dégradé
     */
    private Color[] calculDegrade(Color debut, Color fin, double transparence, int nbPoints){        
        Color[] degrade = new Color[nbPoints];
        
        double diffR = Math.abs(debut.getRed()-fin.getRed())/nbPoints;
        double diffG = (Math.abs(debut.getGreen()-fin.getGreen()))/nbPoints;
        double diffB = (Math.abs(debut.getBlue()-fin.getBlue()))/nbPoints;
        
        degrade[0] = debut;
        degrade[nbPoints-1] = fin;
        
        for(int i=1;i<nbPoints-1;i++){
            degrade[i] = new Color(degrade[i-1].getRed()+diffR, degrade[i-1].getGreen()+diffG, degrade[i-1].getBlue()+diffB, transparence);
        }
        
        return degrade;
    }
    
    /**
     * Dessine les tournées à effectuer pour desservir tous les points de livraison préalablement affichée sur le plan.
     * Mets en valeur la tournée selectionnée par rapport aux autres.
     * @param indexTournee - la tournée à mettre en valeur
     */
    public void dessinerTournees(int indexTournee){
        Tournee[] listeTournees = this.gestionLivraison.getTournees();
        int nCouleur=0;
        int numTournee=0;
        
        Color[] degradeTournee;
        
        if(listeTournees!=null){
            for(Tournee tournee : listeTournees){
                GraphicsContext gc = this.tournees.get(numTournee).getGraphicsContext2D();
                gc.clearRect(0, 0, this.getWidth(), this.getHeight());

                if(tournee != null){
                    List<Chemin> chemins = tournee.getTrajet();

                    gc.setLineWidth(3);

                    if(numTournee == -1 || numTournee==(indexTournee-1) || indexTournee==0){
                        degradeTournee = calculDegrade(couleursDbt[numTournee], couleursCible[numTournee], 1.0, chemins.size()+1);
                        gc.setLineDashes(0,0);
                    }else{
                        degradeTournee = calculDegrade(new Color(couleursDbt[numTournee].getRed(), couleursDbt[numTournee].getGreen(),couleursDbt[numTournee].getBlue(),0.4), new Color(couleursCible[numTournee].getRed(),couleursCible[numTournee].getGreen(), couleursCible[numTournee].getBlue(), 0.4), 0.4, chemins.size()+1);
                        gc.setLineDashes(5,5);
                    }
                                        
                    int numChemin=0;
                    for(Chemin chemin : chemins){
                        List<Troncon> troncons = chemin.getTroncons();
                        Color[] degradeChemin = calculDegrade(degradeTournee[numChemin], degradeTournee[numChemin+1], degradeTournee[numChemin].getOpacity(), troncons.size()+1);
                        
                        int numTroncon=0;
                        for(Troncon troncon : troncons){
                           int absDebutTroncon =(int) ((troncon.getDebut().getLongitude() - origineLongitude) * echelleLong); 
                           int ordDebutTroncon =(int) (this.getHeight() - (troncon.getDebut().getLatitude() - origineLatitude) * echelleLat); 
                           int absFinTroncon = (int)((troncon.getFin().getLongitude() - origineLongitude) * echelleLong); 
                           int ordFinTroncon = (int)(this.getHeight()- (troncon.getFin().getLatitude() - origineLatitude) * echelleLat);

                           Stop[] stops = new Stop[] { new Stop(0, degradeChemin[numTroncon]), new Stop(1, degradeChemin[numTroncon+1])};
                           gc.setStroke(new LinearGradient(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon, true, CycleMethod.NO_CYCLE, stops));
                           gc.strokeLine(absDebutTroncon,ordDebutTroncon,absFinTroncon,ordFinTroncon);
                           numTroncon++;
                        }
                        
                        numChemin++;
                    }
                    
                    this.getChildren().remove(this.tournees.get(numTournee));
                    this.getChildren().add(numTournee+1, this.tournees.get(numTournee));
                }
                
                numTournee++;
                nCouleur++;
            }

            if(indexTournee>0){
                //On passe à l'affichage la tournée choisie devant les autres tournées, tout en conservant la demande de livraison et les marqueurs devant dans l'affichage
                this.getChildren().removeAll(this.tournees.get(indexTournee-1), this.dl, this.marqueurSelectionLivraison,this.marqueurModif);
                this.getChildren().addAll(this.tournees.get(indexTournee-1), this.dl, this.marqueurSelectionLivraison, this.marqueurModif);
            }
        }
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
        this.getChildren().add(this.marqueurSelectionLivraison);
        this.getChildren().add(this.marqueurModif);
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
    
    public double[] mettreCoordonneesALechelle(double[] pointAMAJ, boolean estCoordonneesVueGraphique){
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
    
    public void effacerMarqueurModif() {
        this.marqueurModif.getGraphicsContext2D().clearRect(0, 0, this.marqueurModif.getWidth(), this.marqueurModif.getHeight());
        positionMarqueurAModifier = null;
    }
    
    public void ajouterMarqueurModif(double lat, double lon) {
        int x = (int)((lon - origineLongitude)*echelleLong);
        int y = (int)(this.getHeight() - (lat - origineLatitude)*echelleLat);
        
        positionMarqueurAModifier = new Pair(lat,lon);
        
        GraphicsContext gc = this.marqueurModif.getGraphicsContext2D();
        gc.drawImage(imageMarqueurModif, x - this.imageMarqueurModif.getWidth()/2.0, y - this.imageMarqueurModif.getHeight());
    }
    
    //Test
    public void effacerMarqueur() {
        this.positionMarqueur = null;
        this.marqueurSelectionLivraison.getGraphicsContext2D().clearRect(0,0,this.marqueurSelectionLivraison.getWidth(), this.marqueurSelectionLivraison.getHeight());
    }
    
    //Test
    public void ajouterMarqueur(double lat, double lon){
        int x = (int)((lon - origineLongitude)*echelleLong);
        int y = (int)(getHeight() - (lat - origineLatitude)*echelleLat);

        GraphicsContext gc = marqueurSelectionLivraison.getGraphicsContext2D();
        gc.drawImage(imageMarqueurSelection, x - imageMarqueurSelection.getWidth()/2.0, y - imageMarqueurSelection.getHeight());
        
        this.positionMarqueur = new Pair(lat,lon);
        
        this.getChildren().remove(this.marqueurSelectionLivraison);
        this.getChildren().add(this.marqueurSelectionLivraison);
    }
    
    //Test
    public void dessinerMarqueur(){
        this.marqueurSelectionLivraison.getGraphicsContext2D().clearRect(0,0,this.marqueurSelectionLivraison.getWidth(), this.marqueurSelectionLivraison.getHeight());
        
        if(this.positionMarqueur != null)
            ajouterMarqueur(this.positionMarqueur.getKey(),this.positionMarqueur.getValue());
    }
    
    public void identifierPtPassage(boolean aAjouter, double lat, double lon){
        this.effacerMarqueur();
        
        if(aAjouter)
            this.ajouterMarqueur(lat,lon);
    }
    
    public void identifierPtPassageAModifier(boolean aAjouter, double lat, double lon){
        this.effacerMarqueurModif();
        
        if(aAjouter)
            this.ajouterMarqueurModif(lat,lon);
    }

    public void effacerTournees(){
        tournees.clear();
    }
    
    public void effacerDl(){
        Iterator<Node> iter = this.getChildren().iterator();
        while(iter.hasNext()) {
            Node n = iter.next();
            if( !n.equals(dl) && !n.equals(plan)){
                iter.remove();
            }
        }
    }
    
    public void zoomPlus(double lat, double lon){
        origineLongitude+=(lon-origineLongitude)*coefMultiplicateurZoom/coefDiviseurZoom;
        origineLatitude+= (lat-origineLatitude)*coefMultiplicateurZoom/coefDiviseurZoom;
        echelleLong = echelleLong *coefDiviseurZoom;
        echelleLat=echelleLat*coefDiviseurZoom;  
        dessinerPlan();
        dessinerPtLivraison();
        dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        dessinerMarqueur();
        if(positionMarqueurAModifier!=null)
            identifierPtPassageAModifier(true,positionMarqueurAModifier.getKey(),positionMarqueurAModifier.getValue());
    }
    
    public void zoomMoins(double lat, double lon){
        echelleLong = echelleLong /coefDiviseurZoom;
        echelleLat=echelleLat/coefDiviseurZoom;
        
        if(echelleLong<=echelleLongitudeMin && echelleLat<=echelleLatitudeMin){
            echelleLong = echelleLongitudeMin;
            echelleLat = echelleLatitudeMin;
            origineLatitude = origineLatitudeMin;
            origineLongitude = origineLongitudeMin;
        }else{
            origineLongitude-=(lon-origineLongitude)*coefMultiplicateurZoom;
            origineLatitude-= (lat-origineLatitude)*coefMultiplicateurZoom;
        }
        dessinerPlan();
        dessinerPtLivraison();
        dessinerTournees(fenetre.getVueTextuelle().affichageActuel());
        dessinerMarqueur();
        if(positionMarqueurAModifier!=null)
            identifierPtPassageAModifier(true,positionMarqueurAModifier.getKey(),positionMarqueurAModifier.getValue());
    }
}
