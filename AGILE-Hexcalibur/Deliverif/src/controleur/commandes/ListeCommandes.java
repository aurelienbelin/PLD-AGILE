/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur.commandes;

import java.util.List;
import java.util.ArrayList;

/**
 * Récapitule l'ensemble des commandes exécutées lors de la gestion des livraisons.
 * Merci à C. Solnon pour son exemple dans PlaCo.
 * @author Louis
 */
public class ListeCommandes {
    
    private List<Commande> liste;
    private int indiceCommande;
    
    public ListeCommandes(){
        liste = new ArrayList<Commande>();
        indiceCommande=-1;
    }
    
    /**
     * Ajoute une nouvelle commande à la liste et l'exécute aussitôt.
     * @param cde - La nouvelle commande à ajouter dans la liste.
     */
    public void ajouterCde(Commande cde){
        int indiceEnlevement=indiceCommande+1;
        while (indiceEnlevement<liste.size()){
            liste.remove(indiceEnlevement);
        }
        indiceCommande++;
        liste.add(indiceCommande, cde);
        cde.doCde();
    }
    
    /**
     * Annule la dernière commande réalisée.
     */
    public void undo(){
        if (indiceCommande>=0){
            liste.get(indiceCommande).undoCde();
            indiceCommande--;
        }
    }
    
    /**
     * Rétablit la dernière commande annulée avec undo.
     */
    public void redo(){
        if (indiceCommande<liste.size()-1){
            indiceCommande++;
            liste.get(indiceCommande).doCde();
        }
    }
    
    /**
     * Annule la dernière command et l'élimine de la liste.
     */
    public void annule(){
        if (indiceCommande>=0){
            liste.get(indiceCommande).undoCde();
            liste.remove(indiceCommande);
            indiceCommande--;
        }
    }
    
    /**
     * Remet à zéro l'ensemble des commandes.
     */
    public void reset(){
        indiceCommande=-1;
        liste.clear();
    }
    
    
}
