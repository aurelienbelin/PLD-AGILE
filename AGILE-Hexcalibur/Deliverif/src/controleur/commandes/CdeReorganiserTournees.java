/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur.commandes;

import java.util.ArrayList;
import java.util.List;
import modele.outils.GestionLivraison;

/**
 * Ceci est une sur-commande de réorganisation. Elle permet
 * de réorganiser toutes les tournées plutôt que d'en réorganiser une seule.
 * @author Louis Ohl
 */
public class CdeReorganiserTournees extends Commande{
    
    private List<CdeChangerOrdreTournee> changement;
    
    public CdeReorganiserTournees(GestionLivraison gestion, List<List<Integer>> nouveauxOrdres){
        super(gestion);
        this.changement = new ArrayList<CdeChangerOrdreTournee>();
        for(int i=0; i<nouveauxOrdres.size(); i++){
            this.changement.add(new CdeChangerOrdreTournee(gestion, i, nouveauxOrdres.get(i)));
        }
    }
    
    @Override
    public void doCde(){
        if (this.etatCommande==EtatCommande.EXECUTEE){
            return;
        }
        for(Commande cde : this.changement){
            cde.doCde();
        }
        this.etatCommande=EtatCommande.EXECUTEE;
    }
    
    @Override
    public void undoCde(){
        if(this.etatCommande==EtatCommande.ANNULEE){
            return;
        }
        for(Commande cde : this.changement){
            cde.undoCde();
        }
        this.etatCommande=EtatCommande.ANNULEE;
        
    }
    
}
