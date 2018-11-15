/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
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
    private String nom;
    private Intersection debut, fin;
    private float longueur;
    
    /**
     * Créer un nouveau troncon
     * @param nom - Le nom associé à ce troncon.
     * @param debut - L'intersection à la première extrémité du troncon.
     * @param fin - L'intersection à la seconde extrémité du troncon.
     * @param longueur - La longueur (en m) du troncon.
     */
    public Troncon(String nom, Intersection debut, Intersection fin,
            float longueur){
        if (nom!=null){
            this.nom=nom;
        } else {
            this.nom="";
        }
        this.longueur=(longueur>0 ? longueur : 0);
        if (fin!=null){
            this.fin=fin;
        } else {
            this.fin=new Intersection(0,0,0);
        }
        if (debut!=null){
            this.debut=debut;
            this.debut.addTroncon(this);
        } else {
            this.debut = new Intersection(0,0,0);
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
     * @return L'intersection à la seconde extrémité de ce troncon
     */
    public Intersection getFin(){
        return this.fin;
    }
    
    /**
     * @return La longueur du troncon (en m)
     */
    public float getLongueur(){ return this.longueur; }

    /**
     * @return Le nom du troncon (ex : "Rue Rivoli")
     */
    public String getNom() {
        return nom;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof Troncon){
            Troncon t = (Troncon)o;
            return this.nom.equals(t.getNom()) && this.debut.equals(t.getDebut()) &&
                    this.fin.equals(t.getFin());
        }
        return false;
    }
}
