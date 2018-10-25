/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;


/**
 * Un Troncon représente une rue sur notre plan. Il est caractérisé
 * par ses extrémités, sa longueur.
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class Troncon {
    /** L'idXML sert à maintenir une cohérence entre le troncon dans le fichier
     * xml et l'instance de l'objet.*/
    private int idXML;
    
    private String nom;
    private Intersection debut, fin;
    private float longueur;
    
    /**
     * Créer un nouveau troncon
     * @param idXML - l'identifiant du troncon dans le fichier xml.
     * @param nom - Le nom associé à ce troncon.
     * @param debut - L'intersection à la première extrémité du troncon.
     * @param fin - L'intersection à la seconde extrémité du troncon.
     * @param longueur - La longueur (en m) du troncon.
     */
    public Troncon(int idXML, String nom, Intersection debut, Intersection fin,
            float longueur){
        this.idXML=idXML;
        this.nom=nom;
        this.debut=debut;
        debut.addTroncon(this);
        this.fin=fin;
        this.longueur=longueur;
    }
    
    /**
     * Ce setter se charge d'affecter à l'intersection un nouveau troncon
     * partant.
     * @param debut - La nouvelle intersection au debut de ce troncon
     */
    public void setDebut(Intersection debut){
        if (debut!=null){
            this.debut.removeTroncon(this);
            this.debut=debut;
            this.debut.addTroncon(this);
        }
    }
    
    /**
     * @return L'intersection à la première extrémité de ce troncon
     */
    public Intersection getDebut(){
        return this.debut;
    }
    
    /**
     * @param fin - La nouvelle intersection à la fin de ce troncon
     */
    public void setFin(Intersection fin){
        if(fin!=null){
            this.fin=fin;
        }
    }
    
    /**
     * @return L'intersection à la seconde extrémité de ce troncon
     */
    public Intersection getFin(){
        return this.fin;
    }
    
    public float getLongueur(){ return this.longueur; }
    
    public String toString(){
        return this.nom;
    }
}
