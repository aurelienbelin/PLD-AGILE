/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package deliverif;

import controleur.Controleur;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

/**
 *
 * @author Aurelien Belin
 */
public class EcouteurBoutons{
    
    private final Deliverif fenetrePrincipale;
    private final Controleur controleur;
    
    /**
     *
     * @param f
     * @param c
     */
    public EcouteurBoutons(Deliverif f, Controleur c){
        this.fenetrePrincipale = f;
        this.controleur = c;
    }
    
    /**
     *
     * @param e
     * @throws InterruptedException
     * @throws java.io.IOException
     */

    public void chargerPlanAction(ActionEvent e) throws InterruptedException, IOException, Exception{
        //System.out.println("Choisir un plan à charger"); //DEBUG
        this.fenetrePrincipale.informationEnCours("Chargement du plan...");
        String nomFichier = choisirFichier("Choisir le plan à charger");
        if(nomFichier != null)
        {
            controleur.boutonChargePlan(nomFichier);
        }else{
            this.fenetrePrincipale.informationEnCours("");
        }
    }
    
    /**
     *
     * @param e
     * @throws InterruptedException
     * @throws java.io.IOException
     */

    public void chargerDemandeLivraisonAction(ActionEvent e) throws InterruptedException, IOException, Exception{
        //System.out.println("Choisir une demande de livraison à charger"); //DEBUG
        this.fenetrePrincipale.informationEnCours("Chargement de la demande de livraison...");
        String nomFichier = choisirFichier("Choisir la demande de livraison à charger");
        if(nomFichier != null)
        {
            controleur.boutonChargeLivraisons(nomFichier);
        }else{
            this.fenetrePrincipale.informationEnCours("");
        }
    }

    /**
     *
     * @param e
     * @throws InterruptedException
     */
    public void calculerTourneesAction(ActionEvent e) throws InterruptedException{
        //System.out.println(fenetrePrincipale.getNbLivreurs()); //DEBUG
        //this.fenetrePrincipale.informationEnCours("Calcul des tournées...");
        controleur.boutonCalculerTournees(fenetrePrincipale.getNbLivreurs());
    }

    /**
     *
     * @param e
     * @throws InterruptedException
     */
    public void changerTourneeAffichee(ActionEvent e) throws InterruptedException{
        this.fenetrePrincipale.getVueTextuelle().changerDescriptionAffichee();
        //this.fenetrePrincipale.avertir("Description modifiée");
    }
    
    /**
     * 
     * @param docACharger
     * @return
     * @throws InterruptedException 
     */
    private String choisirFichier(String docACharger) throws InterruptedException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(docACharger);
        fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XML File", "*.xml")
        );
        File f = Deliverif.openFileChooser(fileChooser);
        if (f != null) {
            //On appelle la méthode du controleur devant charger la demande de livraison
            return f.getAbsolutePath();
        }else{
            return null;
        }
    }
}
