/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele;

import modele.tsp.TSPMinCFC;
import modele.tsp.TemplateTSP;
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
 * @author Hex'calibur
 */
public class GestionLivraison extends Observable{
    
    private PlanVille plan;
    private Tournee[] tournees;
    private DemandeLivraison demande;
    private TemplateTSP tsp;
    private Thread threadTSP;
    
    /**
     * Connstruit le plan de la ville à l'aide d'un fichier de description
     * xml.
     * @param fichier - L'url du fichier xml décrivant le plan d'une ville.
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    public void chargerPlan(String fichier) throws SAXException, IOException, 
            Exception{
        modele.flux.LecteurXML Lecteur = new modele.flux.LecteurXML();
        this.plan = Lecteur.creerPlanVille(fichier);
        setChanged();
        this.notifyObservers(plan);
    }
    
    /**
     * Construit la demande de livraison à l'aide d'un fichier de description
     * xml.
     * @param fichier - l'url du fichier xml contenant une demande de livraison.
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    public void chargerDemandeLivraison(String fichier) throws SAXException, 
            IOException, Exception{
        modele.flux.LecteurXML Lecteur = new modele.flux.LecteurXML();
        this.demande = Lecteur.creerDemandeLivraison(fichier, this.plan);
        setChanged();
        this.notifyObservers(demande);
    }
    
    /**
     * Calcule le TSP (et donc tournées) pour les livreurs en un temps donné.
     * @param nbLivreur - Le nombre de liveur (i.e. le nombre de tournée) à 
     * prendre en compte.
     * @param tempsLimite - Le temps maximal (en s) alloué au calcul de tournée.
     * @throws Exception 
     */
    public void calculerTournees(int nbLivreur, int tempsLimite) 
            throws Exception{
        //Eviter les problèmes de paramètres
        if(this.plan==null || this.demande==null || (this.calculTSPEnCours())){
            return;
        }
        if (nbLivreur<1 || tempsLimite<=0){
            return;
        }
        //Prendre tous les points de passages, entrepot compris
        //L'entrepot est à l'indice 0 pour faciliter le fonctionnement du calcul.
        List<PointPassage> listePoints = new ArrayList<>();
        listePoints.add(this.demande.getEntrepot());
        listePoints.addAll(this.demande.getLivraisons());
        //Obtenir l'ensemble des chemins d'un point i à un point j
        List<Chemin> graphe = this.plan.dijkstraTousPoints(listePoints);
        
        //Construire le cout des chemins
        int[][] cout = new int[listePoints.size()][listePoints.size()];
        for(Chemin c : graphe){
            int i = listePoints.indexOf(c.getDebut());
            int j = listePoints.indexOf(c.getFin());
            cout[i][j]=(int)c.calculerDureeChemin();
        }
        //Préparer le TSP
        tsp = new TSPMinCFC(nbLivreur);
        ObservateurTSP observateur = new ObservateurTSP(graphe, listePoints,
                nbLivreur);
        tsp.addObserver(observateur);
        //Lancer le calcul dans un processus parallèle.
        threadTSP = new Thread(new Runnable(){
            @Override
            public void run(){
                tsp.chercheSolution(tempsLimite, listePoints.size(), nbLivreur,
                        cout);
                if (tsp.getCoutMeilleureSolution()!=Integer.MAX_VALUE){
                    genererTournee(graphe, listePoints, nbLivreur);
                }
            }
        });
        //L'arrêt de l'application doit couper net ce calcul.
        threadTSP.setDaemon(true);
        //C'est parti
        threadTSP.start();
    }
    
    /**
     * Génère les tournées en fonction des chemins d'un point de passage à un
     * autre, connaissant le nombre de livreurs (i.e. nombre de tournées).
     * @param graphe - L'ensemble des chemins d'un point de passage à un autre.
     * @param listePoints - L'ensemble des points de passage (dont entrepot)
     * @param nbLivreur - Le nombre de livreur/tournée.
     */
    private synchronized void genererTournee(List<Chemin> graphe, 
            List<PointPassage>  listePoints, int nbLivreur){
        List<Tournee> listeTournee = new ArrayList<>(nbLivreur);
        this.tournees = new Tournee[nbLivreur];
        List<Chemin> trajets = new ArrayList<>();
        
        for(int i=0; i<listePoints.size()+nbLivreur-2;i++){
            //Pour chaque chemin, l'ajouter à la tournée en cours.
            int deb = tsp.getMeilleureSolution(i);
            int fin = tsp.getMeilleureSolution(i+1);
            for(Chemin c : graphe){
                if (c.getDebut()==listePoints.get(deb) 
                        && c.getFin()==listePoints.get(fin)){
                    trajets.add(c);
                    break;
                }
            }
            if (fin==0){
                //On revient vers l'entrepot, on change de tournée !
                listeTournee.add(new Tournee(trajets, 
                        demande.getHeureDepart()));
                trajets = new ArrayList<>();
            }
        }
        //Ne pas oublier de fermer la dernière tournée.
        int fin = tsp.getMeilleureSolution(listePoints.size()+nbLivreur-2);
        for(Chemin c : graphe){
            if (c.getDebut()==listePoints.get(fin) 
                    && c.getFin()==this.demande.getEntrepot()){
                trajets.add(c);
                break;
            }
        }
        
        listeTournee.add(new Tournee(trajets, demande.getHeureDepart()));
        //Rétablir l'attribut this.tournees[]
        listeTournee.toArray(this.tournees);
        if(this.tournees!=null){
            setChanged();
            this.notifyObservers(tournees);
        }
    }
    
    /**
     * Interrompt le calcul (éventuellement) en cours du TSP.
     */
    public void arreterCalculTournee(){
        if (tsp!=null && tsp.calculEnCours()){
            tsp.arreterCalcul();
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
     * @return True si le tsp a actuellement trouvé une solution. False sinon.
     */
    public boolean aSolution(){
        if (tsp==null){
            return false;
        }
        return tsp.getCoutMeilleureSolution()!=Integer.MAX_VALUE;
    }
    
    /**
     * @return False si le thread du tsp est dans l'état terminé. True sinon.
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
     * Permet d'ajouter une livraison à la fin d'une tournée donnée.
     * @param livraison
     * @param numeroTournee - La tournée à laquelle cette livraison doit être
     * ajoutée.
     * @param pointPrecedent - L'indice du point de passage après lequel le 
     * novueau point est inséré
     */
    public void ajouterLivraison(PointPassage livraison, int numeroTournee, 
            int pointPrecedent){
        //Vérifier que les paramètres sont bon et qu'on a bien des tournées existantes.
        if (!this.aSolution()){
            return;
        }
        if(livraison==null || 
                numeroTournee<0 ||
                numeroTournee>=this.tournees.length ||
                pointPrecedent<0 || 
                pointPrecedent>=this.tournees[numeroTournee].nombrePoints()){
            return;
        }
        if (pointPrecedent>=this.tournees[numeroTournee].nombrePoints()){
            return;
        }
        //Ajout dans la demande de livraison
        this.demande.ajouterLivraison(livraison);
        //Retracer le chemin allant vers le nouveau point et celui du nouveau
        //point vers le suivant
        PointPassage derniereLivraison = 
                this.tournees[numeroTournee].getPointPassage(pointPrecedent);
        Map depuisLivraison = this.plan.dijkstra(livraison);
        Map depuisPrecedent = this.plan.dijkstra(derniereLivraison);
        Chemin finVersLivraison = this.plan.reconstruireChemin(
                derniereLivraison, livraison, depuisPrecedent);
        
        Chemin livraisonVersSuivant = this.plan.reconstruireChemin(livraison, 
                this.tournees[numeroTournee].getTrajet().get(pointPrecedent).
                        getFin(),
                depuisLivraison);
        //Regénérer la tournée avec une nouvelle liste de chemins.
        List<Chemin> ancienneTournee = this.tournees[numeroTournee].getTrajet();
        List<Chemin> nouvelleTournee = new ArrayList<>();
        for(int i=0; i<ancienneTournee.size(); i++){
            if (i==pointPrecedent){
                nouvelleTournee.add(finVersLivraison);
                nouvelleTournee.add(livraisonVersSuivant);
            } else {
                nouvelleTournee.add(ancienneTournee.get(i));
            }
        }
        this.tournees[numeroTournee]=new Tournee(nouvelleTournee, 
                this.demande.getHeureDepart());
        setChanged();
        notifyObservers(this.tournees);
    }
    
    /**
     * Suppression d'une livraison
     * @param livraison - La livraison à supprimer
     */
    @SuppressWarnings("empty-statement")
    public void supprimerLivraison(PointPassage livraison){
        if(livraison==null){
            return;
        }
        if(!this.aSolution()){
            return;
        }
        if (livraison.estEntrepot()){
            return;//Non monsieur, on ne supprime pas l'entrepôt
        }
        //Eliminer la livraison de la demande
        this.demande.annulerLivraison(livraison);
        //Récupérer la tournée à laquelle appartient ce point, ainsi que sa 
        //place dans la tournée.
        int numero;
        for(numero=0; 
                numero<this.tournees.length &&
                !this.tournees[numero].contientPointPassage(livraison);
                numero++);
        if (numero>=this.tournees.length){
            return;
        }
        int place;
        for(place=0; 
                place<this.tournees[numero].nombrePoints() && 
                this.tournees[numero].getPointPassage(place)!=livraison; 
                place++);
        //Remplacement de : precedent => suppression => suivant, 
        //par precedent => suivant
        Map res = this.plan.dijkstra(
                this.tournees[numero].getPointPassage(place-1));
        PointPassage suivant;
        if (place==this.tournees[numero].nombrePoints()-1){
            suivant=this.demande.getEntrepot();
        } else {
            suivant=this.tournees[numero].getPointPassage(place+1);
        }
        Chemin nouveauChemin = this.plan.reconstruireChemin(
                this.tournees[numero].getPointPassage(place-1), suivant, res);
        //Reconstruire la suite des chemins
        List<Chemin> ancienneTournee = this.tournees[numero].getTrajet();
        List<Chemin> nouvelleTournee = new ArrayList<>();
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
        //Nouvelles tournées
        this.tournees[numero] = new Tournee(nouvelleTournee, 
                this.demande.getHeureDepart());
        
        setChanged();
        notifyObservers(this.tournees);
    }
    
    /**
     * Change un point de passage d'une tournée 1 vers une tournée 2.
     * @param tournee1 - L'indice de la tournée originelle.
     * @param tournee2 - L'indice de la tournée dans laquelle intégrer le point 
     * de passage.
     * @param indice1 - L'indice du point de passage dans la tournée originelle.
     * @param indice2 - L'indice où ajouter le point de passage dans la nouvelle
     * tournée.
     */
    public void intervertirPoint(int tournee1, int tournee2, int indice1, 
            int indice2){
        if (this.tournees==null || 
                tournee1<0 || 
                tournee2 <0 || 
                tournee1>=this.tournees.length || 
                tournee2>=this.tournees.length){
            return; //sinon on aurait un IndexOutOfBoundsException
        }
        if (indice1<0 || indice2<0 || 
                indice1>this.tournees[tournee1].nombrePoints()-1 || 
                indice2> this.tournees[tournee2].nombrePoints()-1){
            return; //idem
        }
        PointPassage livraison = 
                this.tournees[tournee1].getPointPassage(indice1);

        this.supprimerLivraison(livraison);
        this.ajouterLivraison(livraison, tournee2, indice2);
        setChanged();
        notifyObservers(this.tournees);
    }
    
    /**
     * Renvoie l'indice d'un point de passage au sein d'une tournée.
     * @param numeroTournee - Le numéro de la tournée (indice) dans laquelle on 
     * souhaite trouver l'indice du point de passage.
     * @param p - Le point de passage à chercher dans la tournée.
     * @return L'indice du point de passage p dans la tournée. -1 si le point de 
     * passage ne s'y trouve pas.
     */
    public int positionPointDansTournee(int numeroTournee, PointPassage p){
        if (numeroTournee>=this.tournees.length || numeroTournee<0){
            return -1;
        }
        for(int i=0; i<this.tournees[numeroTournee].nombrePoints(); i++){
            if (this.tournees[numeroTournee].getTrajet().get(i).getDebut()==p){
                return i;
            }
        }
        return-1;
    }
    
    /**
     * 
     * @param p
     * @return {numeroTournee, indicePointDansTournee}, 
     * si le point est absent : {nbreTournee+1, -1}
     */
    public int[] ouEstLePoint(PointPassage p){
        int numTournee = -1;
        int numDansTournee = -1;
        while(numDansTournee==-1 && numTournee<this.tournees.length){
            numTournee++;
            numDansTournee = positionPointDansTournee(numTournee, p);
        }
        int[] result = {numTournee, numDansTournee};
        return result;
    }
    
    /**
     * 
     * @param latitude
     * @param longitude
     * @return 
     */
    public Intersection intersectionPlusProche(double latitude, 
            double longitude) {
        Intersection resultat = this.plan.getIntersections().get(0);
        double minDistance = Math.sqrt(Math.pow(
                (resultat.getLatitude()-latitude), 2) + 
                Math.pow((resultat.getLongitude()-longitude),2));
        
        for(Intersection i: this.plan.getIntersections()){
            double distance = Math.sqrt(Math.pow(
                    (i.getLatitude()-latitude), 2) + 
                    Math.pow((i.getLongitude()-longitude),2));
            if(distance <minDistance) {
                resultat = i;
                minDistance = distance;
            }
        }
        
        return resultat;
    }
    
    /**
     * 
     * @param latitude
     * @param longitude
     * @return 
     */
    public PointPassage pointPassagePlusProche(double latitude, 
            double longitude) {
        
        PointPassage resultat = this.demande.getLivraisons().get(0);
        double minDistance = Math.sqrt(Math.pow(
                (resultat.getPosition().getLatitude()-latitude), 2) + 
                Math.pow((resultat.getPosition().getLongitude()-longitude),2));
        
        for(PointPassage p: this.demande.getLivraisons()){
            double distance = Math.sqrt(Math.pow(
                    (p.getPosition().getLatitude()-latitude), 2) + 
                    Math.pow((p.getPosition().getLongitude()-longitude),2));
            if(distance <minDistance) {
                resultat = p;
                minDistance = distance;
            }
        }
        
        return resultat;
    }
    
    /**
     * 
     * @param point
     * @return 
     */
    public PointPassage identifierPointPassage(String point){
        String[] identifiants = point.split("_");
        
        int numTournee = Integer.parseInt(identifiants[0]);
        int numLivraison = Integer.parseInt(identifiants[1]);
        
        if(numTournee != -1)
            return this.tournees[numTournee-1].getPointPassage(numLivraison-1);
        else
            return this.demande.getLivraisons().get(numLivraison-1);
    }

    /**
     * vide le contenu de Tournees
     */
    public void effacerTournees(){
        for(Tournee tournee : tournees){
            tournee.effacerTournee();
        }
    }
    
    /**
     *
     * @return Les tournées calculées précédemment
     */
    public Tournee[] getTournees() {
        return tournees;
    }
    
    /**
     *
     * @return Le plan de la Ville
     */
    public PlanVille getPlan() {
        return plan;
    }

    /**
     *
     * @return La demande de livraison
     */
    public DemandeLivraison getDemande() {
        return demande;
    }
    
    /**
     * Constructeur de la classe
     */
    public GestionLivraison(){
        this.tournees=null;
        this.plan=null;
        this.demande=null;
    }
    
    /**
     * Cette classe interne permet d'observer notre thread déroulant le 
     * branchandbound du tsp. Elle s'intègre dans le mécanisme d'affichage 
     * dit "temps réel" du tsp.
     */
    private class ObservateurTSP implements Observer{
        
        private List<Chemin> graphe;
        private List<PointPassage> listePoints;
        private int nbLivreur;
        private long dernierAffichage;
        
        /**
         * Construit un nouvel observateur de tsp.
         * @param graphe - L'ensemble des chemins (arcs d'un graphe CFC) d'un 
         * point de passage à un autre.
         * @param listePoints - L'ensemble des points de passages.
         * @param nbLivreur - Le nombre de livreur, important pour la 
         * construction des tournées.
         */
        public ObservateurTSP(List<Chemin> graphe, 
                List<PointPassage> listePoints, int nbLivreur){
            this.graphe=graphe;
            this.listePoints=listePoints;
            this.nbLivreur=nbLivreur;
            dernierAffichage=0;
        }
        
        /**
         * 
         * @param o
         * @param arg 
         */
        @Override
        public void update(Observable o, Object arg){
            //Le TSP a trouvé une nouvelle solution, donc on génère les nbLvreur 
            //tournées,
            //Elles seront ensuite affichées dans la vue grâce au premier pattern 
            //observer.
            genererTournee(graphe, listePoints, nbLivreur);
        }
    }
}
