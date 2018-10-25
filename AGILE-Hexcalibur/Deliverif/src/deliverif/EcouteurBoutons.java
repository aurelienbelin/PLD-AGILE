/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif;

import controleur.Controleur;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

/**
 *
 * @author Aurelien Belin
 */
public class EcouteurBoutons{
    
    private Deliverif fenetrePrincipale;
    private Controleur controleur;
    
    public EcouteurBoutons(Deliverif f, Controleur c){
        this.fenetrePrincipale = f;
        this.controleur = c;
    }
    
    public void chargerPlanAction(ActionEvent e) throws InterruptedException{
        System.out.println("Choisir un plan à charger"); //DEBUG
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir le plan à charger");
        fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XML File", "*.xml")
        );
        File f = Deliverif.openFileChooser(fileChooser);
        if (f != null) {
            System.out.println(controleur); //DEBUG
            //On appelle la méthode du controleur devant charger le plan
            controleur.boutonChargePlan(f.getAbsolutePath());
            //System.out.println(f); //DEBUG
        }else
            fenetrePrincipale.avertir("Fichier non trouvé");
    }
    
    public void chargerDemandeLivraisonAction(ActionEvent e) throws InterruptedException{
        System.out.println("Choisir une demade de livraison à charger"); //DEBUG
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir la demande de livraison à charger");
        fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XML File", "*.xml")
        );
        File f = Deliverif.openFileChooser(fileChooser);
        if (f != null) {
            //On appelle la méthode du controleur devant charger la demande de livraison
            //System.out.println(f); //DEBUG
            controleur.boutonChargeLivraisons(f.getAbsolutePath());
        }else
            fenetrePrincipale.avertir("Fichier non trouvé");
    }
    
    public void calculerTourneesAction(ActionEvent e) throws InterruptedException{
        //System.out.println(fenetrePrincipale.getNbLivreurs());
        controleur.boutonCalculerTournees(fenetrePrincipale.getNbLivreurs());
    }

    public void changerTourneeAffichee(ActionEvent e) throws InterruptedException{
        this.fenetrePrincipale.getVueTextuelle().changerDescriptionAffichee();
        //this.fenetrePrincipale.avertir("Description modifiée");
    }

}
