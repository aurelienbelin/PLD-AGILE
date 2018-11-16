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
 * Cette commande permet de déplacer une livraison au sein d'une tournée.
 * Elle décale la livraison d'une position vers une autre.
 * @author Hex'calibur
 */
public class CdeDeplacerLivraison extends Commande{
    
    private PointPassage livraisonDeplacee;
    //La tournée dans laquelle se déplace la livraison
    private int numeroTournee;
    //L'indice avant permutation
    private int ancienIndice;
    //L'indice auquel la livraison devra se trouver
    private int nouvelIndice;
    
    /**
     * Construit une nouvelle commande de déplacement de livraison.
     * @param gestion - La gestionLivraison, point d'entrée du modèle.
     * @param p - Le Point de Passage que l'on cherche à déplacer.
     * @param numeroTournee - Le numéro de la tournée dans laquelle se trouve le point de passage.
     * @param nouvelIndice - L'indice auquel devra se trouver le point de passage.
     */
    public CdeDeplacerLivraison(GestionLivraison gestion, PointPassage p, int numeroTournee, int nouvelIndice){
        super(gestion);
        this.numeroTournee=numeroTournee;
        this.nouvelIndice=nouvelIndice;
        this.ancienIndice=this.gestion.positionPointDansTournee(this.numeroTournee, p);
        this.livraisonDeplacee=p;
    }
    
    @Override
    public void doCde(){
        //Combinaison de supprimer et ajouter.
        this.gestion.supprimerLivraison(this.livraisonDeplacee);
        //nouvelIndice-1 car l'ajout demande l'indice avant celui où l'on insère.
        this.gestion.ajouterLivraison(this.livraisonDeplacee, this.numeroTournee, this.nouvelIndice-1); 
        
    }
    
    @Override
    public void undoCde(){
        this.gestion.supprimerLivraison(this.livraisonDeplacee);
        this.gestion.ajouterLivraison(this.livraisonDeplacee, this.numeroTournee, this.ancienIndice+(this.ancienIndice>this.nouvelIndice ? -1 : 0));
        //On teste si l'ancien indice se situe avant ou non le nouvel indice afin d'éviter des problèmes
        //indexoutofboundsexception.
    }
    
}
