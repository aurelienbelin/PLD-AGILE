/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Une Intersection représente un carrefour dans notre plan de ville.
 * Elle fait le lien entre plusieurs troncons.
 * @author Hex'calibur
 */
public class Intersection {
    //les troncons partant de l'intersection en question
    private List<Troncon> troncons;
    //coordonnées de l'intersections
    private float latitude;
    private float longitude;
    //id de l'intersection selon le XML
    private long idXML;
    
    /**
     * Ajoute un nouvel element parmi l'ensemble des troncons
     * desservis par cette intersection.
     * @param troncon - Le troncon a ajouter.
     */
    public void addTroncon(Troncon troncon) {
        if (troncon!=null && !this.troncons.contains(troncon)){
            this.troncons.add(troncon);
        }
    }
    
    /**
     * Renvoie le ième troncon partant de cette intersection.
     * @param i - La position du tronçon. 
     * @return - le tronçon situé à la position i dans la liste de tronçon, null
     * si problème.
     */
    public Troncon getTroncon(int i){
        try{
            return this.troncons.get(i);
        } catch(Exception e){
            return null;
        }
    }
    
    /**
     * @return - La liste des tronçons de l'intersection
     */
    public List<Troncon> getTroncons(){
        return troncons;
    }
    
    /**
     * @return - La latitude de l'intersection.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @return - La longitude de l'intersection.
     */
    public float getLongitude() {
        return longitude;
    }
    
    /**
     * @return - L'id de l'intersection dans son fichier xml d'origine.
     */
    public long getIdXML() { 
        return idXML; 
    }
    
    /**
     * Crée une nouvelle intersection.
     * @param idXML -  l'id de l'intersection dans le fichier xml.
     * @param latitude - La latitude de l'intersection.
     * @param longitude  -  La longitude de l'intersection.
     */
    public Intersection(long idXML, float latitude, float longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
        this.idXML=idXML;
        troncons = new ArrayList<>();
    }
    
    @Override
    public String toString(){
        return "("+this.longitude+","+this.latitude+")";
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof Intersection){
            Intersection i =(Intersection)o;
            return i.getLatitude()==this.latitude && 
                    i.getLongitude()==this.longitude;
        }
        return false;
    }
}
