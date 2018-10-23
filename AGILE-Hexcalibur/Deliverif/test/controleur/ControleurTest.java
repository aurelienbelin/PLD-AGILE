/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

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
    public void testBoutonChargePlan() {
        System.out.println("boutonChargePlan");
        String fichier = "";
        modele.GestionLivraison gestion = new modele.GestionLivraison();
        Controleur instance = new Controleur(gestion);
        instance.boutonChargePlan(fichier);
        assertEquals(Controleur.ETAT_INIT, Controleur.etatCourant);
    }

    
    
}
