/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
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
        if(this.trajet==null){
            this.trajet= new ArrayList<Chemin>();
        }
        this.heureDepart=heureDepart;
        if(this.heureDepart==null){
            this.heureDepart=Calendar.getInstance();
        }
    }

    /**
     * @return - La suite ordonné des trajets
     */
    public List<Chemin> getTrajet() {
        return trajet;
    }
    
    /**
     * @return Le nombre de points de passage contenu dans cette tournée. Le retour
     * à l'entrepôt n'est pas considéré comme un point de passage.
     * i.e. : entrepot -liv1-liv2-entrepot = 3 points.
     */
    public int nombrePoints(){
        return this.trajet.size();
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
     * Renvoie le ième point de passage de la tournée.
     * @param i - L'indice du point de passage à chercher.
     * @return Le point de passage recherché.
     */
    protected PointPassage getPointPassage(int i){
        if (this.trajet!=null && i<(this.trajet.size()) && i>=0){
            return this.trajet.get(i).getDebut();
        }else if(i==(this.trajet.size()) && this.trajet.size()>0){
            return this.trajet.get(i-1).getFin();
        }
        return null;
    }
    
    /**
     * Vérifie si un point de passage est contenu dans la tournée.
     * @param p - Le point de passage à rechercher.
     * @return true si ce point de passage est contenu dans la tournée, false sinon.
     */
    protected boolean contientPointPassage(PointPassage p){
        for(Chemin c : this.trajet){
            if (c.getDebut()==p){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Renvoie une description de cette tournée, chemin par chemin.
     * @param heureDepart -  L'heure permettant de calculer les horaires de passage
     * dans chaque point de la tournée.
     * @return Un itérateur renvoyant dans l'ordre la description de chaque chemin dans une liste.
     */
    public Iterator<List<String>> getDescription(){
        List<List<String>> sousDescription = new ArrayList<>();
        List<String> s = new ArrayList<>();
        if(this.trajet.size()==0){
            return sousDescription.iterator();
        }
        s.add(new SimpleDateFormat("HH:mm").format(heureDepart.getTime()));
        s.add(""+0);
        s.add("Entrepôt");
        
        sousDescription.add(s);
        Calendar heureCalcul = (Calendar)this.heureDepart.clone();
        for(Chemin c : this.trajet){
            sousDescription.add(c.getDescription(heureCalcul));
        }
        
        return sousDescription.iterator();
    }

    
    
}
