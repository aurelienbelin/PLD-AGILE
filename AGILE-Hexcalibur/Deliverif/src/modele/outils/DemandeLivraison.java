/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.Calendar;
import java.util.List;

/**
 * Une DemandeLivraison liste l'ensemble des points que l'on aimerait livrer,
 * ainsi que l'entrepot par lequel on doit passer.
 * 
 * @version 1.0 23/10/2018
 * @author Louis Ohl
 */
public class DemandeLivraison {
    
    private List<PointPassage> livraisons;
    private PointPassage entrepot;
    private Calendar heureDepart;

    /**
     * Crée une nouvelle demande de livraison.
     * @param livraisons - L'ensemble des points de passages à livrer.
     * @param entrepot - Le point de passage de l'entrepot.
     */
    public DemandeLivraison(List<PointPassage> livraisons, PointPassage entrepot, Calendar heureDepart) {
        this.livraisons = livraisons;
        this.entrepot = entrepot;
        this.heureDepart=heureDepart;
    }

    /**
     * @return L'ensemble des livraisons concernées par cette demande.
     */
    public List<PointPassage> getLivraisons() {
        return livraisons;
    }

    /**
     * @return Le Point de Passage correspondant à l'entrepot.
     */
    public PointPassage getEntrepot() {
        return entrepot;
    }

    /**
     * @param livraisons - Le nouvel ensemble de points à livrer pour cette demande.
     */
    public void setLivraisons(List<PointPassage> livraisons) {
        this.livraisons = livraisons;
    }

    /**
     * @param entrepot - Le nouvel entrepot duquel partiront les livreurs
     * pour cette demande.
     */
    public void setEntrepot(PointPassage entrepot) {
        this.entrepot = entrepot;
    }
    
    /**
     * @param heureDepart - L'heure a laquelle doit débuter l'ensemble
     * des tournees.
     */
    public void setHeureDepart(Calendar heureDepart){
        this.heureDepart=heureDepart;
    }
    
    /**
     * @return L'heure de départ pour l'ensemble des tournées.
     */
    protected Calendar getHeureDepart(){
        return this.heureDepart;
    }
}
