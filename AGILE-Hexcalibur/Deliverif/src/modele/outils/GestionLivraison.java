/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.Observable;

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

    public PlanVille getPlan() {
        return plan;
    }

    public Tournee[] getTournees() {
        return tournees;
    }

    public DemandeLivraison getDemande() {
        return demande;
    }
    
    public int chargerVille(String fichier){
        return 0;
    }
    
    public int chargerDemandeLivraison(String fichier) {
        return 0;
    }
    
    public int calculerTournees(int nbLivreurs) {
        return 0;
    }

}
