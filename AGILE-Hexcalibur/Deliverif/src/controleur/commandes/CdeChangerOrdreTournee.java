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
 * Cette commande permet de changer l'ordre de visite des points de passage
 * au sein d'une même tournée.
 * @author Louis
 */
public class CdeChangerOrdreTournee extends Commande{
    
    private int numeroTournee;
    private List<Integer> nouvelOrdre;
    private List<Integer> ancienOrdre;
    
    /**
     * Construit une nouvelle Commande pour changer l'ordre des livraisons au sein d'une tournée.
     * @param gestion
     * @param numeroTournee - Le numéro de la tournée sur laquelle le changement a lieu.
     * @param nouvelOrdre - Une liste d'entier décrivant le nouvel ordre tel que nouvelOrdre[i]=j où j est l'indice
     * du jème point de passage. Ex : [ancien ordre] a b c d e ; nouvel ordre = {2 4 3 5 1} => b d c e a
     * L'indice 0 doit impérativement exclus, on ne réordonne pas l'entrepot. De même pour le dernier entrepot.
     */
    public CdeChangerOrdreTournee(GestionLivraison gestion, int numeroTournee, List<Integer> nouvelOrdre){
        super(gestion);
        this.numeroTournee=numeroTournee;
        this.nouvelOrdre=nouvelOrdre;
        this.ancienOrdre=new ArrayList<Integer>();
        for(int i=1; i<nouvelOrdre.size(); i++){
            ancienOrdre.add(nouvelOrdre.indexOf(i));
        }
    }
    
    @Override
    protected void doCde(){
        if(this.etatCommande==EtatCommande.EXECUTEE){
            return;
        }
        this.gestion.changerOrdreTournee(this.numeroTournee, this.nouvelOrdre);
        this.etatCommande=EtatCommande.EXECUTEE;
    }
    
    @Override
    protected void undoCde(){
        if(this.etatCommande==EtatCommande.ANNULEE){
            return;
        }
        this.gestion.changerOrdreTournee(this.numeroTournee, this.ancienOrdre);
        this.etatCommande=EtatCommande.ANNULEE;
    }
}
