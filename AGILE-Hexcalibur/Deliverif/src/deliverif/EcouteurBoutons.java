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
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

/**
 * Ecouteur des évènements émis par l'IHM de l'application.
 * @author Aurelien Belin
 * @see Deliverif
 * @see Controleur
 */
public class EcouteurBoutons{
    
    private final Deliverif fenetrePrincipale;
    private final Controleur controleur;
    private File file;
    
    /**
     * Constructeur d'EcouteurBoutons
     * @param f - fenetre principale de l'application
     * @param c - Controleur de l'application
     * @see Deliverif
     * @see Controleur
     */
    public EcouteurBoutons(Deliverif f, Controleur c){
        this.fenetrePrincipale = f;
        this.controleur = c;
    }
    
    /**
     * Appelle le chargement du plan dans le controleur.
     * @param e - ActionEvent déclenché
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception 
     */
    public void chargerPlanAction(ActionEvent e) throws InterruptedException, IOException, Exception{
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
     * Appelle le chargement de la demande de livraison dans le controleur.
     * @param e
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception 
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
     * Appelle le calcule des tournées dans le controleur.
     * @param e
     * @throws InterruptedException
     */
    public void calculerTourneesAction(ActionEvent e) throws InterruptedException{
        //System.out.println(fenetrePrincipale.getNbLivreurs()); //DEBUG
        //this.fenetrePrincipale.informationEnCours("Calcul des tournées...");
        this.fenetrePrincipale.getVueGraphique().creerCalques(fenetrePrincipale.getNbLivreurs());
        controleur.boutonCalculerTournees(fenetrePrincipale.getNbLivreurs());
    }

    /**
     * Modifie les tournées affichées dans les Vues de l'IHM.
     * @param e
     * @throws InterruptedException
     */
    public void changerTourneeAffichee(ActionEvent e) throws InterruptedException{
        //int i = this.fenetrePrincipale.getVueTextuelle().changerDescriptionAffichee();
        
        int i = this.fenetrePrincipale.getVueTextuelle().changerDescription_Bis();
        
        //System.out.println("Tournee n°"+i); //DEBUG
        
        if(i!=-1)
            this.fenetrePrincipale.getVueGraphique().changerTourneeAffichee(i);
    }

    public double[] recupererCoordonneesSouris(MouseEvent e) throws InterruptedException{
        double[] point = new double[2];
        point[0] = e.getX();;
        point[1] = e.getY();
        return point;
    }
    
    /**
     * Envoie une requête au controleur pour connaitre la localisation du point de livraison associé au composant DescriptifChemin.
     * @param dc 
     */
    public void localiserPointVueGraphique(DescriptifChemin dc){
        controleur.afficherMarqueur(dc);
    }
    
    /**
     * Devellope les détails du DescriptifChemin passé en paramètre.
     * @param dc 
     */
    public void obtenirDetailsVueTextuelle(DescriptifChemin dc){
        dc.developperDetails();
    }
    
    /**
     * Crée un objet donnat accès au Gestionnaire de fichier pour choisir un fichier à charger.
     * @param docACharger
     * @return URL du fichier choisi
     * @throws InterruptedException 
     */
    private String choisirFichier(String docACharger) throws InterruptedException
    {
        FileChooser fileChooser = new FileChooser();
        //Ouvre fileChooser au dernier dossier
        if(file != null){
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        }
        fileChooser.setTitle(docACharger);
        fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XML File", "*.xml")
        );
        file = Deliverif.openFileChooser(fileChooser);
        if (file != null) {
            //On appelle la méthode du controleur devant charger la demande de livraison
            return file.getAbsolutePath();
        }else{
            return null;
        }
    }
}
