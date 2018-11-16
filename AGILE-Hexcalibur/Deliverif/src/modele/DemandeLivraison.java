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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Une DemandeLivraison liste l'ensemble des points que l'on aimerait livrer,
 * ainsi que l'entrepot par lequel on doit passer.
 * @author Hex'calibur
 */
public class DemandeLivraison {
    
    //labels description
    public final static String LIVRAISON_A = "Livraison à ";
    public final static String RUE_SANS_NOM = "Rue sans nom";
    
    private List<PointPassage> livraisons;
    private PointPassage entrepot;
    private Calendar heureDepart;
    
    
    /**
     * méthode d'ajout d'une nouvelle livraison
     * @param nouvelleLivraison 
     */
    public void ajouterLivraison(PointPassage nouvelleLivraison){
        if(nouvelleLivraison!=null){
            this.livraisons.add(nouvelleLivraison);
        }
    }
    
    /**
     * Annulation ou suppression d'une demande de la livraison
     * @param livraisonAnnulee 
     */
    public void annulerLivraison(PointPassage livraisonAnnulee){
        if(livraisonAnnulee!=null){
            this.livraisons.remove(livraisonAnnulee);
        }
    }
    
    /**
     * Vider le contenu de la demande de livraisons
     */
    public void effacerLivraisons(){
        this.livraisons.clear();
        this.entrepot = null;
    }
    
    /**
     * 
     * @return La description de la livraison
     */
    public Iterator<List<String>> getDescription(){
        List<List<String>> sousDescription = new ArrayList<>();
        for(PointPassage pp : this.livraisons){
            String description;
            if (!pp.getPosition().getTroncons().isEmpty()){
                description = LIVRAISON_A + (
                        pp.getPosition().getTroncon(0).getNom().equals("") ? 
                        RUE_SANS_NOM : 
                        pp.getPosition().getTroncon(0).getNom());
            } else {
                description = LIVRAISON_A + RUE_SANS_NOM;
            }
            ArrayList<String> a = new ArrayList<>();
            a.add(""+(int)(pp.getDuree()/60));
            a.add(description);
            sousDescription.add(a);
        }
        return sousDescription.iterator();
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
     * @return L'heure de départ pour l'ensemble des tournées.
     */
    protected Calendar getHeureDepart(){
        return this.heureDepart;
    }

    /**
     * @param livraisons - Le nouvel ensemble de points à livrer pour cette demande.
     */
    public void setLivraisons(List<PointPassage> livraisons) {
        if(livraisons!=null && livraisons.size()>0){
            this.livraisons = livraisons;
        }
    }

    /**
     * @param entrepot - Le nouvel entrepot duquel partiront les livreurs
     * pour cette demande.
     */
    public void setEntrepot(PointPassage entrepot) {
        if (entrepot!=null && entrepot.estEntrepot()){
            this.entrepot = entrepot;
        }
    }
    
    /**
     * @param heureDepart - L'heure a laquelle doit débuter l'ensemble
     * des tournees.
     */
    public void setHeureDepart(Calendar heureDepart){
        if(heureDepart!=null){
            this.heureDepart=heureDepart;
        }
    }
    
    /**
     * Crée une nouvelle demande de livraison.
     * @param livraisons - L'ensemble des points de passages à livrer.
     * @param entrepot - Le point de passage de l'entrepot.
     * @param heureDepart
     */
    public DemandeLivraison(List<PointPassage> livraisons, 
            PointPassage entrepot, Calendar heureDepart) {
        this.livraisons = livraisons;
        if(this.livraisons==null){
            this.livraisons=new ArrayList<>();
        }
        this.entrepot = entrepot;
        if(this.entrepot!= null && !this.entrepot.estEntrepot()){
            this.entrepot=null;
        }
        this.heureDepart=heureDepart;
        if(this.heureDepart==null){
            this.heureDepart=Calendar.getInstance();
        }
    }
}
