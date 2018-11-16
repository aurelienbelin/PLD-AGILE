/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur.commandes;

import modele.GestionLivraison;
import modele.PointPassage;

/**
 *
 * @author lohl
 */
public class CdeDeplacerLivraison extends Commande{
    
    private PointPassage livraisonDeplacee;
    private int numeroTournee;
    private int ancienIndice;
    private int nouvelIndice;
    
    public CdeDeplacerLivraison(GestionLivraison gestion, PointPassage p, int numeroTournee, int nouvelIndice){
        super(gestion);
        this.numeroTournee=numeroTournee;
        this.nouvelIndice=nouvelIndice;
        this.ancienIndice=this.gestion.positionPointDansTournee(this.numeroTournee, p);
        this.livraisonDeplacee=p;
    }
    
    @Override
    public void doCde(){
        if(this.etatCommande==EtatCommande.EXECUTEE){
            return;
        }
        this.gestion.supprimerLivraison(this.livraisonDeplacee);
        this.gestion.ajouterLivraison(this.livraisonDeplacee, this.numeroTournee, this.nouvelIndice-1); 
        
        this.etatCommande=EtatCommande.EXECUTEE;
    }
    
    @Override
    public void undoCde(){
        if(this.etatCommande==EtatCommande.ANNULEE){
            return;
        }
        this.gestion.supprimerLivraison(this.livraisonDeplacee);
        this.gestion.ajouterLivraison(this.livraisonDeplacee, this.numeroTournee, this.ancienIndice+(this.ancienIndice>this.nouvelIndice ? -1 : 0));
        this.etatCommande=EtatCommande.ANNULEE;
    }
    
}
