/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.outils;

import java.util.Iterator;
import modele.flux.LecteurXML;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lohl
 */
public class GestionLivraisonTest {
    
    private GestionLivraison gestion;
    
    public GestionLivraisonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - GESTIONLIVRAISON TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - GESTIONLIVRAISON TEST -------FIN--------------");
    }
    
    @Before
    public void setUp() {
        gestion = new GestionLivraison();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculerTournee method, of class GestionLivraison.
     */
    @Test
    public void testCalculerTournee() {
        String urlFichierPlan = "test/modele/flux/grandPlan.xml";
        String urlFichierDemande = "test/modele/flux/dl-grand-15.xml";
        int nbLivreur=4;
        LecteurXML lecteur = new LecteurXML();
        assertTrue(gestion.chargerVille(urlFichierPlan));
        //assertEquals(308, gestion.getPlan().getIntersections().size());
        //assertEquals(616, gestion.getPlan().getTroncons().size());
        for (Troncon t : gestion.getPlan().getTroncons()){
            assertTrue(gestion.getPlan().getIntersections().contains(t.getDebut()));
            assertTrue(gestion.getPlan().getIntersections().contains(t.getFin()));
        }
        assertTrue(gestion.chargerDemandeLivraison(urlFichierDemande));
        assertEquals(15, gestion.getDemande().getLivraisons().size());
        assertEquals(1,gestion.calculerTournee(nbLivreur));
        assertNotNull(gestion.getTournees());
        assertEquals(nbLivreur,gestion.getTournees().length);
        for(Tournee t : gestion.getTournees()){
            Iterator<String> it = t.getDescription();
            while(it.hasNext()){
                System.out.println(it.next());
            }
        }
    }
    
}
