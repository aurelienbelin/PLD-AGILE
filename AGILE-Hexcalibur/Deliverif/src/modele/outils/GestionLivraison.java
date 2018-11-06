/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import modele.flux.LecteurXML;
import org.xml.sax.SAXException;

/** 
 * Point d'entrée du contrôleur. Permet à l'application d'accéder de manière
 * sécurisée aux objets composant le modèle.
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class GestionLivraison extends Observable{
    
    private PlanVille plan;
    private Tournee[] tournees;
    private DemandeLivraison demande;
    private TSP tsp;
    
    /**
     * Créer une nouvelle GestionLivraison
     */
    public GestionLivraison(){
        this.tournees=null;
        this.plan=null;
        this.demande=null;
    }
    
    public int calculerTournees(int nbLivreur){
        if(this.plan==null || this.demande==null){
            return 0;
        }
        List<PointPassage> listePoints = new ArrayList<>();
        listePoints.add(this.demande.getEntrepot());
        listePoints.addAll(this.demande.getLivraisons());
        List<Chemin> graphe = this.plan.dijkstraTousPoints(listePoints);//Nous sommes déjà sûrs que l'entrepot est à l'indice 0
        int[][] cout = new int[listePoints.size()][listePoints.size()];
        for(Chemin c : graphe){
            int i = listePoints.indexOf(c.getDebut());
            int j = listePoints.indexOf(c.getFin());
            cout[i][j]=(int)c.getDuree();
        }
        tsp = new TSPGlouton(nbLivreur);
        tsp.chercheSolution(Integer.MAX_VALUE, listePoints.size(), nbLivreur, cout);
        List<Tournee> listeTournee = new ArrayList<Tournee>(nbLivreur);
        this.tournees = new Tournee[nbLivreur];
        List<Chemin> trajets = new ArrayList<Chemin>();
        for (int i=0; i<listePoints.size()+nbLivreur-1;i++){
            System.out.print(tsp.getMeilleureSolution(i)+" ");
        }
        System.out.println("\n\n");
        for(int i=0; i<listePoints.size()+nbLivreur-2;i++){
            int deb = tsp.getMeilleureSolution(i);
            int fin = tsp.getMeilleureSolution(i+1);
            for(Chemin c : graphe){
                if (c.getDebut()==listePoints.get(deb) && c.getFin()==listePoints.get(fin)){
                    trajets.add(c);
                    break;
                }
            }
            if (fin==0){
                listeTournee.add(new Tournee(trajets, demande.getHeureDepart()));
                trajets = new ArrayList<Chemin>();
            }
        }
        int fin = tsp.getMeilleureSolution(listePoints.size()+nbLivreur-2);
        for(Chemin c : graphe){
            if (c.getDebut()==listePoints.get(fin) && c.getFin()==this.demande.getEntrepot()){
                trajets.add(c);
                break;
            }
        }
        listeTournee.add(new Tournee(trajets, demande.getHeureDepart()));
        listeTournee.toArray(this.tournees);
        
        if(this.tournees==null){
            System.out.println("ALERTE GOGOL");
            return 0;
        } else {
            setChanged();
            this.notifyObservers(tournees);
            return 1;
        }
    }
    
    /**
     * Interrompt le calcul en cours du TSP.
     */
    public void arreterCalculTournee(){
        tsp.arreterCalcul();
    }
    
    /**
     *
     * @param fichier - L'url du fichier xml décrivant le plan d'une ville.
     */
    public void chargerPlan(String fichier) throws SAXException, IOException, Exception{
        modele.flux.LecteurXML Lecteur = new modele.flux.LecteurXML();
        this.plan = Lecteur.creerPlanVille(fichier);
        setChanged();
        this.notifyObservers(plan); //?

    }
    
    /**
     *
     * @param fichier - l'url du fichier xml contenant une demande de livraison.
     */
    public void chargerDemandeLivraison(String fichier) throws SAXException, IOException, Exception{
        modele.flux.LecteurXML Lecteur = new modele.flux.LecteurXML();
        this.demande = Lecteur.creerDemandeLivraison(fichier, this.plan);
        setChanged();
        this.notifyObservers(demande); //?
    }
    
    //Test
    public Intersection identifierPointPassage(String point){
        String[] identifiants = point.split("_");
        
        int numTournee = Integer.parseInt(identifiants[0]);
        int numLivraison = Integer.parseInt(identifiants[1]);
        
        return this.tournees[numTournee-1].getPointPassage(numLivraison-1).getPosition();        
    }
    
    /**
     *
     * @return - Le plan de la Ville
     */
    public PlanVille getPlan() {
        return plan;
    }

    /**
     *
     * @return - Les tournées calculées précédemment
     */
    public Tournee[] getTournees() {
        return tournees;
    }

    /**
     *
     * @return - La demande de livraison
     */
    public DemandeLivraison getDemande() {
        return demande;
    }
}
