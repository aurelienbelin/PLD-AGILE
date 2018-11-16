/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import javafx.util.Pair;
import modele.flux.LecteurXML;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

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
    @Test(timeout = 30*60*1000)
    public void testCalculerTournee() {
        System.out.println("-- methode calculerTournee");
        Pair<String, String> paire1 = new Pair<>("petitPlan","petit-3");
        Pair<String,String> paire2 = new Pair<>("petitPlan", "petit-6");
        Pair<String, String> paire3 = new Pair<>("moyenPlan", "moyen-9");
        Pair<String, String> paire4 = new Pair<>("moyenPlan", "moyen-12");
        Pair<String, String> paire5 = new Pair<>("grandPlan", "grand-12");
        Pair<String, String> paire6 = new Pair<>("grandPlan", "grand-15");
        Pair[] tab = {paire1, paire2, paire3, paire4, paire5, paire6};
        for(Pair<String, String> paire : tab){
            String urlFichierPlan = "test/modele/flux/"+paire.getKey()+".xml";
            String urlFichierDemande = "test/modele/flux/dl-"+paire.getValue()+".xml";
            System.out.println("Test : "+paire.getKey()+" / "+paire.getValue());
            gestion = new GestionLivraison();
            LecteurXML lecteur = new LecteurXML();
            
            try{
                gestion.chargerPlan(urlFichierPlan);
            }catch(Exception e){
                fail(e.getStackTrace().toString());
            }
            try{
                gestion.chargerDemandeLivraison(urlFichierDemande);
            }catch(Exception e){
                fail(e.getStackTrace().toString());
            }
            for (Troncon t : gestion.getPlan().getTroncons()){
                assertTrue(gestion.getPlan().getIntersections().contains(t.getDebut()));
                assertTrue(gestion.getPlan().getIntersections().contains(t.getFin()));
            }
            int maxLivreur = gestion.getDemande().getLivraisons().size();
            if (2*maxLivreur>20){
                maxLivreur=20-maxLivreur;
            }
            for(int nbLivreur=1; nbLivreur<maxLivreur; nbLivreur++){
                System.out.println("\t=>"+nbLivreur+" livreurs");
                try{
                    gestion.calculerTournees(nbLivreur, Integer.MAX_VALUE);
                    while(gestion.threadAlive()){
                        try{
                            Thread.sleep(1000);//Attente active, je sais, c'est mal.
                        } catch(InterruptedException e){
                            //nothing
                        }
                    }
                } catch(Exception e){
                    fail(e.getStackTrace().toString());
                }
                assertFalse(gestion.calculTSPEnCours());
                assertNotNull(gestion.getTournees());
                assertEquals(nbLivreur,gestion.getTournees().length);
            }
        }
        
        
    }
    
    @Test(timeout=2000)
    public void testAjoutTournee(){
        System.out.println("-- methode ajouter/supprimer tournees");
        String urlFichierPlan = "test/modele/flux/petitPlan.xml";
        String urlFichierDemande = "test/modele/flux/dl-petit-3.xml";
        gestion = new GestionLivraison();
        LecteurXML lecteur = new LecteurXML();
        try{
            gestion.chargerPlan(urlFichierPlan);
        }catch(Exception e){
            fail(e.getStackTrace().toString());
        }
        try{
            gestion.chargerDemandeLivraison(urlFichierDemande);
        }catch(Exception e){
            fail(e.getStackTrace().toString());
        }

        try{
            gestion.calculerTournees(1,Integer.MAX_VALUE);
            while (gestion.threadAlive()){
                try{
                    Thread.sleep(1000);
                } catch(InterruptedException e) {}//Wow, on s'en moque.
            }
        } catch(Exception e){
            fail(e.getStackTrace().toString());
        }
        System.out.println("-- ajout");
        //Maintenant, faisons un ajout/suppression.
        PointPassage fauxPoint = new PointPassage(true, gestion.getPlan().getIntersections().get(0), 0f);
        float duree=0f;
        float longueur=0f;
        float nouvelleDuree=0f;
        float nouvelleLongueur=0f;
        for(Tournee t : gestion.getTournees()){
            duree+=t.getTempsTournee();
            longueur+=t.getLongueur();
        }
        PointPassage ajout = new PointPassage(false, gestion.getPlan().getIntersections().get(0), 0f);
        gestion.ajouterLivraison(ajout, 0, 0);
        assertEquals(5, gestion.getTournees()[0].getTrajet().size());
        boolean intersectionTrouvee=false;
        PointPassage pointAEnlever=null;

        for(Tournee t : gestion.getTournees()){
            nouvelleDuree+=t.getTempsTournee();
            nouvelleLongueur+=t.getLongueur();
            for (Chemin c : t.getTrajet()){
                if (c.getFin().getPosition()==gestion.getPlan().getIntersections().get(0)){
                    intersectionTrouvee=true;
                    pointAEnlever=c.getFin();
                }
            }
        }
        assertTrue(intersectionTrouvee);
        assertTrue(nouvelleDuree>duree);
        assertTrue(nouvelleLongueur>longueur);
        System.out.println("-- suppression");
        gestion.supprimerLivraison(fauxPoint);
        assertEquals(5, gestion.getTournees()[0].getTrajet().size());
        gestion.supprimerLivraison(pointAEnlever);
        nouvelleDuree=0f;
        nouvelleLongueur=0f;
        intersectionTrouvee=false;
        for(Tournee t : gestion.getTournees()){
            nouvelleDuree+=t.getTempsTournee();
            nouvelleLongueur+=t.getLongueur();
            for (Chemin c : t.getTrajet()){
                if (c.getFin().getPosition()==gestion.getPlan().getIntersections().get(0)){
                    intersectionTrouvee=true;
                }
            }
        }
        assertFalse(intersectionTrouvee);
        assertEquals(duree, nouvelleDuree, 1f);
        assertEquals(longueur, nouvelleLongueur, 1f);
        assertEquals(4, gestion.getTournees()[0].getTrajet().size());
        
    }
}
