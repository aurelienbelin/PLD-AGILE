/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** Un chemin représente une liste de tronçons, il est caractérisé par son début
 * et sa fin.
 * @author Hex'calibur
 */
public class Chemin {
    
    //formattage heure
    public final static String FORMAT_HEURE = "HH:mm";
    
    //constantes numériques
    //Conversion par 15 km/h [/ (15/3.6) === * 0.24]
    public final static float QUINZE_KMH_MS = 3.6f/15f; 

    //labels description
    public final static String ENTREPOT = "Entrepot";
    public final static String LIVRAISON_A = "Livraison à ";
    public final static String CONTINUER = "Continuer sur ";
    public final static String TOURNER = "Tourner à ";
    public final static String ARRIVER = "Arriver à ";
    public final static String RUE_SANS_NOM = "Rue sans nom";
    public final static String METRES = " m";
        
    private List<Troncon> troncons;
    private PointPassage debut;
    private PointPassage fin;
    
    /**
     * @return La longueur du chemin, i.e la somme des longueurs des tronçons 
     * composant le chemin
     */
    protected float calculerLongueurChemin(){
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
     * @return La durée du chemin, i.e la somme des durées des tronçons et 
     * de la livraison
     */
    protected float calculerDureeChemin(){
        if (this.debut==null || this.fin==null || this.troncons==null){
            return 0;
        }
        float longueur = this.calculerLongueurChemin();
        float resultat = longueur*QUINZE_KMH_MS;
        if (this.fin!=null){
            resultat+=this.fin.getDuree();
        }
        return resultat;
    }
    
    /**
     * Renvoie une liste de String décrivant textuellement ce chemin troncon par
     * troncon.
     * @param heureDepart -  L'heure permettant de calculer l'horaire de passage
     * à la fin de ce chemin.
     * @return Une liste de String contenant les étapes textuelles de ce chemin.
     */
    protected List<String> getDescriptionChemin(Calendar heureDepart){
        if(heureDepart==null){
            heureDepart=Calendar.getInstance();
        }
        if (this.debut==null || this.fin==null || this.troncons==null){
            return new ArrayList<>();
        }
        List<String> etapes = new ArrayList<>();
        
        heureDepart.add(Calendar.SECOND, (int)this.calculerDureeChemin());
        heureDepart.add(Calendar.SECOND, (int)this.debut.getDuree());
        
        String heure = new SimpleDateFormat(FORMAT_HEURE).format(
                heureDepart.getTime());
        
        etapes.add(heure);
        etapes.add(""+(int)(this.fin.getDuree()/60.0));
        if (this.fin.getPosition().getTroncons().isEmpty()){
           etapes.add(this.fin.estEntrepot()? ENTREPOT : LIVRAISON_A +
                   RUE_SANS_NOM);
        } else {
           etapes.add(this.fin.estEntrepot()? ENTREPOT: LIVRAISON_A+(
                   this.fin.getPosition().getTroncon(0).getNom().equals("") ? 
                           RUE_SANS_NOM : 
                           this.fin.getPosition().getTroncon(0).getNom())); 
        }
        
        String dernierNom="";
        float longueur=0f;
        for(Troncon c : this.troncons){
            longueur+=c.getLongueur();
            if(!c.getNom().equals(dernierNom)){
                etapes.add(CONTINUER+(dernierNom.equals("") ? 
                        RUE_SANS_NOM : 
                        dernierNom)+" ("+(10*(int)(longueur/10))+METRES +")");
                etapes.add(TOURNER+(c.getNom().equals("") ? 
                        RUE_SANS_NOM : 
                        c.getNom()));
                dernierNom=c.getNom();
            }
        }
        etapes.add(CONTINUER+(dernierNom.equals("") ? 
                RUE_SANS_NOM : 
                dernierNom)+" ("+(10*(int)(longueur/10))+METRES +")");
        if(this.fin.getPosition().getTroncons().isEmpty()){
            etapes.add(ARRIVER + RUE_SANS_NOM);
        } else {
            etapes.add(ARRIVER+(
                    this.fin.getPosition().getTroncon(0).getNom().equals("") ? 
                            RUE_SANS_NOM : 
                            this.fin.getPosition().getTroncon(0).getNom()));
        }

        return etapes;
    }
    
    /**
     * @return L'ensemble des troncons (routes/rues) composant ce chemin.
     */
    public List<Troncon> getTroncons(){
        return this.troncons;
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
     * Créer un nouveau chemin
     * @param troncons - La liste de tronçons composant ce chemin
     * @param debut - Le point de passage par lequel commence ce chemin
     * @param fin - Le point de passage par lequel se termine ce chemin
     * @see modele.PointPassage
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
}
