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
import java.util.List;
import java.util.Locale;

/** Un chemin représente une liste de tronçons, il est caractérisé par son début et sa fin.
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class Chemin {
    private List<Troncon> troncons;
    private PointPassage debut;
    private PointPassage fin;

    /**
     * Créer un nouveau chemin
     * @param troncons - La liste de tronçons composant ce chemin
     * @param debut - Le point de passage par lequel commence ce chemin
     * @param fin - Le point de passage par lequel se termine ce chemin
     * @see modele.outils.PointPassage
     */
    public Chemin(List<Troncon> troncons, PointPassage debut, PointPassage fin) {
        if (troncons==null){
            this.troncons = new ArrayList<Troncon>();
        } else {
            this.troncons = troncons;
        }
        this.debut=debut;
        this.fin=fin;
    }

    /**
     * @return - Le point de passage par lequel débute le chemin
     */
    protected PointPassage getDebut(){
        return this.debut;
    }

    /**
     *
     * @return - Le point de passage par lequel se termine le chemin
     */
    protected PointPassage getFin(){
        return this.fin;
    }

    /**
     * @return - La longueur du chemin, i.e la somme des longueurs des tronçons composant le chemin
     */
    protected float getLongueur(){
        if (this.debut==null || this.fin==null || this.troncons==null){
            return 0;
        }
        float longueur=0.0f;
        for(Troncon t : this.troncons){
            longueur+=t.getLongueur();
        }
        return longueur;
    }

    /**
     * @return - La durée du chemin, i.e la somme des durées des tronçons et de la livraison
     */
    protected float getDuree(){
        if (this.debut==null || this.fin==null || this.troncons==null){
            return 0;
        }
        float longueur = this.getLongueur();
        float resultat = longueur*3.6f/15f;//Conversion par 15 km/h [/ (15/3.6) === * 0.24]
        if (this.fin!=null){
            resultat+=this.fin.getDuree();
        }
        return resultat;
    }
    
    /**
     * Renvoie une liste de String décrivant textuellement ce chemin troncon par troncon.
     * @param heureDepart -  L'heure permettant de calculer l'horaire de passage à la fin de ce chemin.
     * @return Une liste de String contenant les étapes textuelles de ce chemin.
     */
    protected List<String> getDescription(Calendar heureDepart){
        if(heureDepart==null){
            heureDepart=Calendar.getInstance();
        }
        if (this.debut==null || this.fin==null || this.troncons==null){
            return new ArrayList<String>();
        }
        List<String> etapes = new ArrayList<String>();
        
        heureDepart.add(Calendar.SECOND, (int)this.getDuree());
        heureDepart.add(Calendar.SECOND, (int)this.debut.getDuree());
        
        String heure = new SimpleDateFormat("HH:mm").format(heureDepart.getTime());
        
        etapes.add(heure);
        etapes.add(""+(int)(this.fin.getDuree()/60.0));
        if (this.fin.getPosition().getTroncons().isEmpty()){
           etapes.add(this.fin.estEntrepot()?"Entrepot":"Livraison à Rue sans nom");
        } else {
           etapes.add(this.fin.estEntrepot()?"Entrepot":"Livraison à : "+(this.fin.getPosition().getTroncon(0).getNom().equals("") ? "Rue sans nom" : this.fin.getPosition().getTroncon(0).getNom())); 
        }
        
        String dernierNom="";
        float longueur=0f;
        for(Troncon c : this.troncons){
            longueur+=c.getLongueur();
            if(!c.getNom().equals(dernierNom)){
                etapes.add("Continuer sur : "+(dernierNom.equals("") ? "Rue sans nom" : dernierNom)+" ("+(10*(int)(longueur/10))+" m)");
                etapes.add("Tourner à : "+(c.getNom().equals("") ? "Rue sans nom" : c.getNom()));
                dernierNom=c.getNom();
            }
        }
        etapes.add("Continuer sur : "+(dernierNom.equals("") ? "Rue sans nom" : dernierNom)+" ("+(10*(int)(longueur/10))+" m)");
        if(this.fin.getPosition().getTroncons().isEmpty()){
            etapes.add("Arriver à : Rue sans nom");
        } else {
            etapes.add("Arriver à : "+(this.fin.getPosition().getTroncon(0).getNom().equals("") ? "Rue sans nom" : this.fin.getPosition().getTroncon(0).getNom()));
        }
        //heure = new SimpleDateFormat("HH:mm").format(heureDepart.getTime());
        //etapes.add(heure);

        return etapes;
    }
    
    /**
     * @return L'ensemble des troncons (routes/rues) composant ce chemin.
     */
    public List<Troncon> getTroncons(){
        return this.troncons;
    }
}
