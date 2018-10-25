/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Une tournée correspond à un ordre de chemins bien défini
 * qu'un livreur devra executer.
 * 
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class Tournee {
    
    private List<Chemin> trajet;

    /**
     * Crée une nouvelle Tournee.
     * @param trajet - L'ensemble de chemins ordonné à parcourir pour le livreur
     */
    public Tournee(List<Chemin> trajet) {
        this.trajet = trajet;
    }

    /**
     * @return La suite ordonné des trajets
     */
    public List<Chemin> getTrajet() {
        return trajet;
    }
    
    /**
     * @return La durée totale que le livreur mettra entre son départ de l'entrepot
     * et son retour à l'entrepot.
     */
    public float getTempsTournee(){
        float duree=0f;
        for(Chemin chemin : trajet){
            duree+=chemin.getDuree();
        }
        return duree;
    }
    
    /**
     * @return La longueur de la tournée, c-à-d de l'entrepot à l'entrepot.
     */
    public float getLongueur(){
        float longueur=0f;
        for(Chemin chemin : trajet){
            longueur+=chemin.getLongueur();
        }
        return longueur;
    }
    
    public PointPassage getDepart(){
        if(this.trajet!=null){
            return this.trajet.get(0).getDebut();
        }
        return null;
    }
    
    public PointPassage getArrivee(){
        if (this.trajet!=null){
            return this.trajet.get(this.trajet.size()-1).getFin();
        }
        return null;
    }
    
    public PointPassage getPointPassage(int i){
        if (this.trajet!=null){
            return this.trajet.get(i).getDebut();
        }
        return null;
    }
    
    public Iterator<String> getDescription(){
        List<String> sousDescription = new ArrayList<String>();
        for(Chemin c : this.trajet){
            sousDescription.addAll(c.getDescription());
        }
        
        return sousDescription.iterator();
    }

    
}
