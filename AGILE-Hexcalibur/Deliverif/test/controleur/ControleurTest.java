/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ahirusan
 */
public class ControleurTest {
    
    public ControleurTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of boutonChargePlan method, of class Controleur.
     */
    @Test
    public void testBoutonChargePlan() throws IOException, Exception {
        System.out.println("boutonChargePlan");
        modele.outils.GestionLivraison gestion = new modele.outils.GestionLivraison();
        deliverif.Deliverif fenetre = new deliverif.Deliverif();
        Controleur instance = new Controleur(gestion,fenetre);
        
        String fichier = "fichier existant et bien formé";
        instance.boutonChargePlan(fichier);
        assertEquals(Controleur.ETAT_PLAN_CHARGE, Controleur.etatCourant);
        
        String fichier2 = "fichier non existant ou mal formé";
        instance.boutonChargePlan(fichier2);
        assertEquals(Controleur.ETAT_INIT, Controleur.etatCourant);
    }

    /**
     * Test of boutonChargeLivraisons method, of class Controleur.
     */
    @Test
    public void testBoutonChargeLivraisons() throws IOException, Exception {
        System.out.println("boutonChargeLivraison");
        modele.outils.GestionLivraison gestion = new modele.outils.GestionLivraison();
        
        deliverif.Deliverif fenetre = new deliverif.Deliverif();
        Controleur instance = new Controleur(gestion,fenetre);
        
        String fichierPlan = "fichier existant et bien formé";
        instance.boutonChargePlan(fichierPlan);
        assertEquals(Controleur.ETAT_PLAN_CHARGE, Controleur.etatCourant);
        
        String fichier = "fichier existant et bien formé";
        instance.boutonChargeLivraisons(fichier);
        assertEquals(Controleur.ETAT_LIVRAISONS_CHARGEES, Controleur.etatCourant);
        
        String fichier2 = "fichier non existant ou mal formé";
        instance.boutonChargeLivraisons(fichier2);
        assertEquals(Controleur.ETAT_PLAN_CHARGE, Controleur.etatCourant);
    }

    /**
     * Test of boutonCalculerTournees method, of class Controleur.
     */
    @Test
    public void testBoutonCalculerTournees() throws IOException, Exception {
        System.out.println("boutonCalculerTournees");
        modele.outils.GestionLivraison gestion = new modele.outils.GestionLivraison();
        
        deliverif.Deliverif fenetre = new deliverif.Deliverif();
        Controleur instance = new Controleur(gestion, fenetre);
        
        String fichierPlan = "fichier existant et bien formé";
        instance.boutonChargePlan(fichierPlan);
        assertEquals(Controleur.ETAT_PLAN_CHARGE, Controleur.etatCourant);
        
        String fichierLivraison = "fichier existant et bien formé";
        instance.boutonChargeLivraisons(fichierLivraison);
        assertEquals(Controleur.ETAT_LIVRAISONS_CHARGEES, Controleur.etatCourant);
        
        int nbLivreurs=3;
        instance.boutonCalculerTournees(nbLivreurs);
        assertEquals(Controleur.ETAT_CALCUL_TOURNEES, Controleur.etatCourant);
        
    }

    
    
}
