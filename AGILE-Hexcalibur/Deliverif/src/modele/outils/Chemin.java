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
        this.troncons = troncons;
        this.debut=debut;
        this.fin=fin;
    }

    /**
     * Créer un nouveau chemin
     * @param debut - Le point de passage par lequel commence ce chemin
     * @param fin - Le point de passage par lequel se termine ce chemin
     * @see modele.outils.PointPassagen
     */
    public Chemin(PointPassage debut, PointPassage fin){
        this.debut=debut;
        this.fin=fin;
        this.troncons = new ArrayList<Troncon>();
    }

    /**
     * ATTENTION : supprime la fin actuelle du chemin
     * @param troncon - Le troncon à ajouter à la fin du chemin
     */
    protected void addTroncon(Troncon troncon){
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

    /**
     *
     * @return - Le point de passage par lequel débute le chemin
     */
    protected PointPassage getDebut(){
        return this.debut;
    }

    /**
     * Affecte le param fin à l'attribut fin du chemin
     * @param fin
     */
    protected void setFin(PointPassage fin){
        if (troncons.size()!=0){
            if (this.troncons.get(this.troncons.size()-1).getFin()==fin.getPosition()){
                this.fin=fin;
            }
        }
    }

    /**
     *
     * @return - Le point de passage par lequel se termine le chemin
     */
    protected PointPassage getFin(){
        return this.fin;
    }

    /**
     *
     * @return - La longueur du chemin, i.e la somme des longueurs des tronçons composant le chemin
     */
    protected float getLongueur(){
        float longueur=0.0f;
        for(Troncon t : this.troncons){
            longueur+=t.getLongueur();
        }
        return longueur;
    }

    /**
     *
     * @return - La durée du chemin, i.e la somme des durées des tronçons et de la livraison
     */
    protected float getDuree(){
        float longueur = this.getLongueur();
        float resultat = longueur/3.6f;
        if (this.fin!=null){
            resultat+=this.fin.getDuree();
        }
        return resultat;
    }

    /**
     *
     * @return - La description tectuelle du chemin
     */
    protected List<String> getDescription(Calendar heureDepart){
        List<String> etapes = new ArrayList<String>();
        String heure = new SimpleDateFormat("HH:mm").format(heureDepart.getTime());
        etapes.add("("+heure+") Depart"+
                (this.debut.estEntrepot()? " de l'entrepot." : " du point de livraison"));
        String dernierNom="";
        float longueur=0f;
        for(Troncon c : this.troncons){
            longueur+=c.getLongueur();
            if(!c.getNom().equals(dernierNom)){
                etapes.add("Traverser : "+(dernierNom.equals("") ? "Rue anonyme" : dernierNom)+" pendant "+(10*(int)(longueur/10))+" m.");
                etapes.add("Tourner à : "+(c.getNom().equals("") ? "Rue anonyme" : c.getNom()));
                dernierNom=c.getNom();
            }
        }
        etapes.add("Traverser : "+(dernierNom.equals("") ? "Rue anonyme" : dernierNom)+" pendant "+(10*(int)(longueur/10))+" m.");
        heureDepart.add(Calendar.SECOND, (int)this.getDuree());
        
        heure = new SimpleDateFormat("HH:mm").format(heureDepart.getTime());
        etapes.add("Arriver "+(this.fin.estEntrepot()? "à l'entrepot." :" au point de livraison")+"("+
                heure+
                ").");

        etapes.add("("+heure+") Depart"+
                (this.debut.estEntrepot()? " de l'entrepot." : " du point de livraison"));
        
        return etapes;
    }
    
    //Test
    protected List<String> getDescription_Bis(Calendar heureDepart){
        List<String> etapes = new ArrayList<String>();
        
        heureDepart.add(Calendar.SECOND, (int)this.getDuree());
        heureDepart.add(Calendar.SECOND, (int)this.debut.getDuree());
        
        String heure = new SimpleDateFormat("HH:mm").format(heureDepart.getTime());
        
        etapes.add(heure);
        etapes.add(""+(int)(this.fin.getDuree()/60.0));
        etapes.add(this.fin.estEntrepot()?"Entrepot":"Livraison à "+this.fin.getPosition().getTroncon(0).getNom());
        
        String dernierNom="";
        float longueur=0f;
        for(Troncon c : this.troncons){
            longueur+=c.getLongueur();
            if(!c.getNom().equals(dernierNom)){
                etapes.add("Traverser : "+(dernierNom.equals("") ? "Rue anonyme" : dernierNom)+" ("+(10*(int)(longueur/10))+" m)");
                etapes.add("Tourner à : "+(c.getNom().equals("") ? "Rue anonyme" : c.getNom()));
                dernierNom=c.getNom();
            }
        }
        etapes.add("Traverser : "+(dernierNom.equals("") ? "Rue anonyme" : dernierNom)+" ("+(10*(int)(longueur/10))+" m)");
        etapes.add("Arriver à : "+(this.fin.getPosition().getTroncon(0).getNom().equals("") ? "Rue anonyme" : this.fin.getPosition().getTroncon(0).getNom()));
        
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
