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
import modele.DemandeLivraison;
import modele.PlanVille;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Amine Nahid
 */
public class LecteurXMLTest {
    
    private final String url = "test/modele/flux/";
    
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
        PlanVille pv = instance.creerPlanVille(urlFichierXML);
        String result1 = pv.getTroncons().get(0).getNom();
        assertEquals("", result1);
        long result2 = instance.creerPlanVille(urlFichierXML).getIntersections().get(0).getIdXML();
        assertEquals(2129259178, result2);
    }
    
    /**
     * Test of creerDemandeLivraison method, of class LecteurXML.
     */
    @Test
    public void testCreerDemandeLivraison(){
        System.out.println("-- creerDemandeLivraison");
        String urlFichierPlan = "test/modele/flux/moyenPlan.xml";
        String urlFichierXML = "test/modele/flux/dl-moyen-12.xml";
        LecteurXML instance = new LecteurXML();
        try{
            PlanVille planVille = instance.creerPlanVille(urlFichierPlan);
            String result = instance.creerDemandeLivraison(urlFichierXML, planVille).getEntrepot().getPosition().getTroncon(0).getNom();
            String result2 = instance.creerDemandeLivraison(urlFichierXML, planVille).getLivraisons().get(0).getPosition().getTroncon(0).getNom();
            assertEquals("Rue Pierre Verger", result);
            assertEquals("Cours Tolstoï", result2);
        } catch(Exception e){
            fail(e.getStackTrace().toString());
        }
    }
    
    /**
     * Test d'un plan cassé :
     * - Un fichier n'est pas syntaxiquement correct
     * - Il manque un attribut dans un noeud
     * - Il manque un attribut dans un troncon
     * - Un troncon réfère un noeud inexistant
     * - Un troncon possède une longueur négative (ce qui démolit dijkstra)
     * - On passe une demande de livraison à la place d'un plan
     * - On donne quelque chose qui n'a rien à voir
     */
    @Test
    public void testPlanCasse(){
        System.out.println("-- test plan cassé");
        String[] fichiers = {url+"planCasse0.xml", url+"planCasse1.xml", url+"planCasse2.xml",
        url+"planCasse3.xml", url+"planCasse4.xml", url+"dl-petit3.xml", "src/modele/outils/TemplateTSP.java"};
        LecteurXML lecteur = new LecteurXML();
        for(String fichier : fichiers){
            
            try{
                PlanVille ville = lecteur.creerPlanVille(fichier);
                if (!fichier.equals(fichiers[4])){
                    fail("Le plan cassé "+fichier+" a tout de même passé.");
                }
            } catch(Exception e) {
                if(fichier.equals(fichiers[4])){
                    fail("La longueur négative génère une exception");
                }
            }
        }
    }
    
    /**
     * Test d'une demande de livraison cassée
     * - La dl est chargée avant un plan
     * - Une dl n'est pas au bon format (caractère manquant)
     * - Une dl n'a pas d'entrepot
     * - Une dl n'a pas de livraison
     * - Une livraison réfère un noeud inexistant
     * - Une livraison avec un attribut manquant
     * - Un fichier qui n'a vraiment rien à voir
     */
    @Test
    public void testDemandeLivraisonCassee(){
        System.out.println("-- test dl cassé");
        LecteurXML lecteur = new LecteurXML();
        try{
            DemandeLivraison dl = lecteur.creerDemandeLivraison(url+"dl-petit3.xml", null);
            fail("La création d'une DL a eu lieu malgré l'absence de plan !");
        } catch(Exception e) {}
        String[] fichiers = {url+"dlCasse0.xml", url+"dlCasse1.xml", url+"dlCasse2.xml",
            url+"dlCasse3.xml", url+"dlCasse4.xml", "src/modele/outils/TemplateTSP.java"};
        //Nous avons maintenant besoin d'une ville.
        PlanVille ville=null;
        try{
            ville=lecteur.creerPlanVille(url+"petitPlan.xml");
        } catch(Exception e){
            fail("Le plan n'a pas été chargé : "+e.getMessage());
        }
        for(String fichier : fichiers){
            try{
                DemandeLivraison dl = lecteur.creerDemandeLivraison(fichier, ville);
                fail("Le fichier "+fichier+" a tout de même été lu");
            } catch(Exception e) {}
        }
    }
}
