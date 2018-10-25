/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.flux;

import modele.outils.DemandeLivraison;
import modele.outils.PlanVille;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author Amine Nahid
 */
public class LecteurXMLTest {
    
    public LecteurXMLTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of chargerXML method, of class LecteurXML.
     */
    @Test
    public void testChargerXML() throws Exception {
        System.out.println("chargerXML");
        String urlFichierXML = "test/modele/flux/petitPlan.xml";
        LecteurXML instance = new LecteurXML();
        String expResult = "reseau";
        String result = instance.chargerXML(urlFichierXML).getDocumentElement().getNodeName();
        assertEquals(expResult, result);
    }

    /**
     * Test of creerPlanVille method, of class LecteurXML.
     */
    @Test
    public void testCreerPlanVille() {
        System.out.println("creerPlanVille");
        String urlFichierXML = "";
        LecteurXML instance = new LecteurXML();
        PlanVille expResult = null;
        PlanVille result = instance.creerPlanVille(urlFichierXML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of creerDemandeLivraison method, of class LecteurXML.
     */
    @Test
    public void testCreerDemandeLivraison() {
        System.out.println("creerDemandeLivraison");
        String urlFichierXML = "";
        PlanVille planVille = null;
        LecteurXML instance = new LecteurXML();
        DemandeLivraison expResult = null;
        DemandeLivraison result = instance.creerDemandeLivraison(urlFichierXML, planVille);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of creerPlanVille method, of class LecteurXML.
     */
    /*@Test
    public void testCreerPlanVille() {
        System.out.println("creerPlanVille");
        String urlFichierXML = "./petitPlan.xml";
        LecteurXML instance = new LecteurXML();
        PlanVille expResult = null;
        PlanVille result = instance.creerPlanVille(urlFichierXML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
