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
import java.util.List;

/**
 *
 * @author lohl
 */
public class Chemin {
    private List<Troncon> troncons;
    private PointPassage debut;
    private PointPassage fin;
    
    public Chemin(List<Troncon> troncons, PointPassage debut, PointPassage fin) {
        this.troncons = troncons;
        this.debut=debut;
        this.fin=fin;
    }
    
    public Chemin(PointPassage debut, PointPassage fin){
        this.debut=debut;
        this.fin=fin;
        this.troncons = new ArrayList<Troncon>();
    }
    
    /**
     * ATTENTION : supprime la fin actuelle du chemin
     * @param troncon - Le troncon à ajouter à la fin du chemin
     */
    public void addTroncon(Troncon troncon){
        if (troncon==null){
            return;
        } else if (troncons.size()!=0 && this.troncons.get(this.troncons.size()-1).getFin()!=troncon.getDebut()){
            return;
        } else if (troncons.size()==0 && this.debut.getPosition()!=troncon.getDebut()){
            return;
        }
        this.fin=null;
        this.troncons.add(troncon);
        
    }
    
    public PointPassage getDebut(){
        return this.debut;
    }
    
    public void setFin(PointPassage fin){
        if (troncons.size()!=0){
            if (this.troncons.get(this.troncons.size()-1).getFin()==fin.getPosition()){
                this.fin=fin;
            }
        }
        
    }
    
    public PointPassage getFin(){
        return this.fin;
    }
    
    public float getLongueur(){
        float longueur=0.0f;
        for(Troncon t : this.troncons){
            longueur+=t.getLongueur();
        }
        return longueur;
    }
    
    public float getDuree(){
        float longueur = this.getLongueur();
        float resultat = longueur/3.6f;
        if (this.fin!=null){
            resultat+=this.fin.getDuree();
        }
        return resultat;
    }
    
    public List<String> getDescription(){
        List<String> etapes = new ArrayList<String>();
        etapes.add("Depart : "+this.debut.getPosition());
        for(Troncon c : this.troncons){
            etapes.add("Traverser : "+c);
        }
        etapes.add("Arriver à : "+this.fin.getPosition());
        
        return etapes;
    }
}
