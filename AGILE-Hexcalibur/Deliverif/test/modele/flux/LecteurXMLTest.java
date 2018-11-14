/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.flux;

import java.io.IOException;
import modele.outils.DemandeLivraison;
import modele.outils.PlanVille;
import modele.outils.Troncon;
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
    public void testCreerPlanVille() throws IOException, Exception {
        System.out.println("creerPlanVille");
        String urlFichierXML = "test/modele/flux/moyenPlan.xml";
        LecteurXML instance = new LecteurXML();
        String expResult1 = "Rue Danton";
        PlanVille pv = instance.creerPlanVille(urlFichierXML);
        String result1 = pv.getTroncons().get(0).getNom();
        System.out.println(result1);
        assertEquals(expResult1, result1);
        int expResult2 = 25175791;
        long result2 = instance.creerPlanVille(urlFichierXML).getIntersections().get(0).getIdXML();
        System.out.println(result2);
        assertEquals(expResult2, result2);
    }
    
    /**
     * Test of creerDemandeLivraison method, of class LecteurXML.
     */
    @Test
    public void testCreerDemandeLivraison() throws IOException, Exception {
        System.out.println("creerDemandeLivraison");
        String urlFichierPlan = "test/modele/flux/moyenPlan.xml";
        String urlFichierXML = "test/modele/flux/dl-moyen-12.xml";
        LecteurXML instance = new LecteurXML();
        PlanVille planVille = instance.creerPlanVille(urlFichierPlan);
        String expResult = "Rue Danton";
        String result = instance.creerDemandeLivraison(urlFichierXML, planVille).getEntrepot().getPosition().getTroncon(0).getNom();
        System.out.println(result);
        String result2 = instance.creerDemandeLivraison(urlFichierXML, planVille).getLivraisons().get(0).getPosition().getTroncon(0).getNom();
        System.out.println(result2);
        assertEquals(expResult, result);
        assertEquals(expResult, result2);
    }
}
