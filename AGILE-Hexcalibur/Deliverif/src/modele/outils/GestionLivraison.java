/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import modele.flux.LecteurXML;

/**
 *
 * @author lohl
 */
public class GestionLivraison extends Observable{
    
    private PlanVille plan;
    private Tournee[] tournees;
    private DemandeLivraison demande;
    
    public GestionLivraison(){
        this.tournees=null;
        this.plan=null;
        this.demande=null;
    }
    
    public boolean chargerVille(String urlFichierXML){
        LecteurXML lecteur = new LecteurXML();
        this.plan=lecteur.creerPlanVille(urlFichierXML);
        return this.plan!=null;
    }
    
    public boolean chargerDemandeLivraison(String urlFichierXML){
        LecteurXML lecteur = new LecteurXML();
        if(this.plan==null){
            return false;
        }
        this.demande=lecteur.creerDemandeLivraison(urlFichierXML, plan);
        return this.demande!=null;
    }
    
    public int calculerTournee(int nbLivreur){
        if(this.plan==null || this.demande==null){
            return 0;
        }
        List<PointPassage> listePoints = new ArrayList<>();
        listePoints.add(this.demande.getEntrepot());
        listePoints.addAll(this.demande.getLivraisons());
        List<Chemin> graphe = this.plan.dijkstraToutPoints(listePoints);//Nous sommes déjà sûrs que l'entrepot est à l'indice 0
        int[][] cout = new int[listePoints.size()][listePoints.size()];
        for(Chemin c : graphe){
            int i = listePoints.indexOf(c.getDebut());
            int j = listePoints.indexOf(c.getFin());
            cout[i][j]=(int)c.getDuree();
        }
        TSP1 tsp = new TSP1(nbLivreur);
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
                listeTournee.add(new Tournee(trajets));
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
        listeTournee.add(new Tournee(trajets));
        listeTournee.toArray(this.tournees);
        
        if(this.tournees==null){
            return 0;
        } else {
            return 1;
        }
    }

    public PlanVille getPlan() {
        return plan;
    }

    public Tournee[] getTournees() {
        return tournees;
    }

    public DemandeLivraison getDemande() {
        return demande;
    }
    
    
}
