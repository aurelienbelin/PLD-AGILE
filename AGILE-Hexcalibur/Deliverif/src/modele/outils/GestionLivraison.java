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
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
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
    private TemplateTSP tsp;
    private Thread threadTSP;
    
    
    /**
     * Créer une nouvelle GestionLivraison
     */
    public GestionLivraison(){
        this.tournees=null;
        this.plan=null;
        this.demande=null;
    }
    
    public void calculerTournees(int nbLivreur) throws Exception{
        if(this.plan==null || this.demande==null || (this.tsp!=null && this.tsp.calculEnCours())){
            return;
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
        tsp = new TSPMinCFC(nbLivreur);
        ObservateurTSP observateur = new ObservateurTSP(graphe, listePoints, nbLivreur);
        tsp.addObserver(observateur);
        threadTSP = new Thread(new Runnable(){
            @Override
            public void run(){
                tsp.chercheSolution(Integer.MAX_VALUE, listePoints.size(), nbLivreur, cout);
                genererTournee(graphe, listePoints, nbLivreur);
            }
        });
        threadTSP.start();
    }
    
    private synchronized void genererTournee(List<Chemin> graphe, List<PointPassage>  listePoints, int nbLivreur){
        List<Tournee> listeTournee = new ArrayList<Tournee>(nbLivreur);
        this.tournees = new Tournee[nbLivreur];
        List<Chemin> trajets = new ArrayList<Chemin>();
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
        if(this.tournees!=null){
            setChanged();
            this.notifyObservers(tournees);
        } else {
            System.out.println("C'est la merde, yo !");
        }
    }
    
    /**
     * Permet d'ajouter une livraison à la fin d'une tournée donnée.
     * @param intersection - L'intersection sur laquelle a été aimanté la livraison à ajouter
     * @param dureePassage - La durée que l'utilisateur devra passer sur le point de livraison
     * @param numeroTournee - La tournée à laquelle cette livraison doit être ajoutée.
     */
    public void ajouterLivraison(Intersection intersection, float dureePassage, int numeroTournee){
        //FIXME : something for the undo/redo stuff.
        PointPassage livraison = new PointPassage(false, intersection, dureePassage);
        this.demande.ajouterLivraison(livraison);
        PointPassage derniereLivraison = this.tournees[numeroTournee].getPointPassage(
            this.tournees[numeroTournee].getTrajet().size()-1);
        Map depuisLivraison = this.plan.dijkstra(livraison);
        Map depuisPrecedent = this.plan.dijkstra(derniereLivraison);
        Chemin finVersLivraison = this.plan.reconstruireChemin(derniereLivraison, livraison, depuisPrecedent);
        Chemin livraisonVersEntrepot = this.plan.reconstruireChemin(livraison, this.demande.getEntrepot(), depuisLivraison);
        //Modifications sur la référence, rien d'autre à faire.
        List<Chemin> ancienneTournee = this.tournees[numeroTournee].getTrajet();
        ancienneTournee.remove(ancienneTournee.size()-1);
        ancienneTournee.add(finVersLivraison);
        ancienneTournee.add(livraisonVersEntrepot);
        setChanged();
        notifyObservers(this.tournees);
    }
    
    /**
     * Permet de supprimer une livraison connue
     * @param livraison - La livraison à supprimer
     */
    public void supprimerLivraison(PointPassage livraison){
        //FIXME : à changer pour l'undo/redo
        if (livraison.estEntrepot()){
            return;//Non monsieur, on ne supprime pas l'entrepôt
        }
        //Récupérer la tournée à laquelle appartient ce point, ainsi que sa place dans la tournée.
        int numero=0;
        for(numero=0; numero<this.tournees.length && !this.tournees[numero].contientPointPassage(livraison); numero++);
        int place;
        for(place=0; place<this.tournees[numero].getTrajet().size() &&
                    this.tournees[numero].getPointPassage(place)!=livraison;place++);
        //Remplacement de : precedent => suppression => suivant, par precedent => suivant
        Map res = this.plan.dijkstra(this.tournees[numero].getPointPassage(place-1));
        PointPassage suivant=null;
        if (place==this.tournees[numero].getTrajet().size()-1){
            suivant=this.demande.getEntrepot();
        } else {
            suivant=this.tournees[numero].getPointPassage(place+1);
        }
        Chemin nouveauChemin = this.plan.reconstruireChemin(this.tournees[numero].getPointPassage(place-1), suivant, res);
        List<Chemin> ancienneTournee = this.tournees[numero].getTrajet();
        List<Chemin> nouvelleTournee = new ArrayList<Chemin>();
        for(Chemin chemin : ancienneTournee){
            if (chemin.getDebut()!=livraison && chemin.getFin()!=livraison){
                //Ignorer les deux chemins : precedent => suppression => suivant
                nouvelleTournee.add(chemin);
            }
            if (chemin.getDebut()==livraison){
                //Remplacer par le raccourci : precedent => suivant
                nouvelleTournee.add(nouveauChemin);
            }
        }
        this.tournees[numero] = new Tournee(nouvelleTournee, this.demande.getHeureDepart());
        
        setChanged();
        notifyObservers(this.tournees);
        
    }
    
    /**
     * Interrompt le calcul (éventuellement) en cours du TSP.
     */
    public void arreterCalculTournee(){
        if (tsp!=null && tsp.calculEnCours()){
            tsp.arreterCalcul();
        }
        if (threadTSP.isAlive()){
            threadTSP.interrupt();
        }
    }
    
    /**
     * @return true Si un calcul de TSP est déjà en cours, false sinon.
     */
    public boolean calculTSPEnCours(){
        if (tsp!=null){
            return tsp.calculEnCours();
        } 
        return false;
    }
    
    /**
     * Methode de test
     */
    public boolean threadAlive(){
        if (threadTSP!=null){
            if (threadTSP.getState().equals(State.TERMINATED)){
                return false;
            }
        }
        return true;
    }
    /**
     * Connstruit le plan de la ville à l'aide d'un fichier de description
     * xml.
     * @param fichier - L'url du fichier xml décrivant le plan d'une ville.
     */
    public void chargerPlan(String fichier) throws SAXException, IOException, Exception{
        modele.flux.LecteurXML Lecteur = new modele.flux.LecteurXML();
        this.plan = Lecteur.creerPlanVille(fichier);
        setChanged();
        this.notifyObservers(plan); //?

    }
    
    /**
     * Construit la demande de livraison à l'aide d'un fichier de description
     * xml.
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
    
    private class ObservateurTSP implements Observer{
        
        private List<Chemin> graphe;
        private List<PointPassage> listePoints;
        private int nbLivreur;
        private long dernierAffichage;
        
        public ObservateurTSP(List<Chemin> graphe, List<PointPassage> listePoints, int nbLivreur){
            this.graphe=graphe;
            this.listePoints=listePoints;
            this.nbLivreur=nbLivreur;
            dernierAffichage=0;
        }
        
        @Override
        public void update(Observable o, Object arg){
            genererTournee(graphe, listePoints, nbLivreur);
        }
    }
}
