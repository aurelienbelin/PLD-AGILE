/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Calendar heureDepart;

    /**
     * Crée une nouvelle Tournee.
     * @param trajet - L'ensemble de chemins ordonné à parcourir pour le livreur
     */
    public Tournee(List<Chemin> trajet, Calendar heureDepart) {
        this.trajet = trajet;
        this.heureDepart=heureDepart;
    }

    /**
     * @return - La suite ordonné des trajets
     */
    public List<Chemin> getTrajet() {
        return trajet;
    }
    
    /**
     * @return - La durée totale que le livreur mettra entre son départ de l'entrepot
     * et son retour à l'entrepot.
     */
    protected float getTempsTournee(){
        float duree=0f;
        for(Chemin chemin : trajet){
            duree+=chemin.getDuree();
        }
        return duree;
    }
    
    /**
     * @return - La longueur de la tournée, c-à-d de l'entrepot à l'entrepot.
     */
    public float getLongueur(){
        float longueur=0f;
        for(Chemin chemin : trajet){
            longueur+=chemin.getLongueur();
        }
        return longueur;
    }
    
    /**
     * 
     * @return - Le point de départ de la tournée 
     */
    protected PointPassage getDepart(){
        if(this.trajet!=null){
            return this.trajet.get(0).getDebut();
        }
        return null;
    }
    
    /**
     * 
     * @return - Le point d'arrivée de la tournée 
     */
    protected PointPassage getArrivee(){
        if (this.trajet!=null){
            return this.trajet.get(this.trajet.size()-1).getFin();
        }
        return null;
    }
    
    /**
     * 
     * @param i
     * @return - Le point de passage du trajet i 
     */
    protected PointPassage getPointPassage(int i){
        if (this.trajet!=null){
            return this.trajet.get(i).getDebut();
        }
        return null;
    }
    
    /**
     * 
     * @return - La description de la tournée 
     */
    public Iterator<String> getDescription(){
        List<String> sousDescription = new ArrayList<String>();
        for(Chemin c : this.trajet){
            sousDescription.addAll(c.getDescription(this.heureDepart));
        }
        sousDescription.add("Fin de la tournée");
        this.heureDepart.add(Calendar.SECOND, -(int)this.getTempsTournee());//retablir l'objet partagé heureDepart
        return sousDescription.iterator();
    }
    
    //Test
    public Iterator<List<String>> getDescription_Bis(){
        List<List<String>> sousDescription = new ArrayList<>();
        List<String> s = new ArrayList<>();
        
        s.add(new SimpleDateFormat("HH:mm").format(heureDepart.getTime()));
        s.add(""+0);
        s.add("Entrepôt");
        
        sousDescription.add(s);
        
        for(Chemin c : this.trajet){
            sousDescription.add(c.getDescription_Bis(this.heureDepart));
        }
        
        this.heureDepart.add(Calendar.SECOND, -(int)this.getTempsTournee());//retablir l'objet partagé heureDepart
        
        return sousDescription.iterator();
    }

    
    
}
