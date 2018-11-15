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
import static org.junit.Assert.assertArrayEquals;
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
        try{
            gestion.chargerPlan("test/outils/flux/petitPlan.xml");
            gestion.chargerDemandeLivraison("test/outils/flux/dl-petit-3.xml");
        } catch(Exception e){
            System.out.println("ERREUR pré-test");
            fail();
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test de calculerTournee
     * - Sans plan chargé
     * - Sans demande de livraison chargée
     * - Avec un nombre de livreur < 1
     * - Avec un temps limite négatif
     * - Régulier
     */
    @Test
    public void testCalculerTournee(){
        System.out.println("-- calculerTournees");
        
        gestion = new GestionLivraison();
        
        //Rien de chargé
        try{
            gestion.calculerTournees(1, Integer.MAX_VALUE);
            assertFalse(gestion.calculTSPEnCours());
        } catch(Exception e){}
        
        //Seul le plan est chargé
        try{
            gestion.chargerPlan("test/modele/flux/petitPlan.xml");
        } catch(Exception e) {fail();}
        try{
            gestion.calculerTournees(1, Integer.MAX_VALUE);
            assertFalse(gestion.calculTSPEnCours());
        } catch(Exception e){}
        
        //Nombre de livreur improbable
        try{
            gestion.chargerDemandeLivraison("test/modele/flux/dl-petit-3.xml");
        } catch(Exception e) {fail();}
        try{
            gestion.calculerTournees(-1, Integer.MAX_VALUE);
            assertFalse(gestion.calculTSPEnCours());
        } catch(Exception e){}
        
        //Temps impossible
        try{
            gestion.calculerTournees(1, 0);
            assertFalse(gestion.calculTSPEnCours());
        } catch(Exception e){}
        
        //Regulier
        try{
            gestion.calculerTournees(1, Integer.MAX_VALUE);
            while (gestion.threadAlive()){
                Thread.sleep(100);
            }
        } catch(Exception e){}
        assertEquals(1,gestion.getTournees().length);
        assertNotNull(gestion.getTournees()[0]);
        assertFalse(gestion.threadAlive());
        assertFalse(gestion.calculTSPEnCours());
        
        assertEquals(gestion.getDemande().getLivraisons().size(),
                gestion.getTournees()[0].nombrePoints()-1);
    }
    
    /**
     * Test de calculerTournee, verifier la cohérence des tournées.
     */
    @Test(timeout = 30*60*1000)
    public void testCalculerTournee2() {
        System.out.println("-- calculerTournees");
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
    
    /**
     * Test de ajoutTournee
     * - Ajouter le point lorsque les tournées n'ont pas été calculées ?
     * - Ajouter un point de passage null ?
     * - Ajouter le point dans une tournée inexistante ?
     * - Ajouter le point hors de la tournée ?
     * - Ajout normal
     */
    @Test
    public void testAjoutTournee(){
        System.out.println("-- ajouterTournee");
        
        Intersection inter = gestion.getPlan().getIntersections().get(0);
        PointPassage aAjouter = new PointPassage(false, inter, 60);
        int nombreLivraison = gestion.getDemande().getLivraisons().size();
        int nombreTournee=1;
        
        
        //Ajout sans calcul de tournées
        gestion.ajouterLivraison(aAjouter, 0, 0);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons().size());
        
        
        try{
            gestion.calculerTournees(nombreTournee, Integer.MAX_VALUE);
            while(gestion.threadAlive()){
                Thread.sleep(100);
            }
        } catch(Exception e){
            System.out.println("erreur calcul");
            //Ignorer, elle est du au notify qui n'envoie rien.
        }
        
        float duree = gestion.getTournees()[0].getTempsTournee();
                
        //Ajout d'un null
        gestion.ajouterLivraison(null, 0, 0);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(), 0.1f);
        
        //Ajout dans tournée out of bounds
        gestion.ajouterLivraison(aAjouter, -1, 0);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(), 0.1f);
        gestion.ajouterLivraison(aAjouter,3,0);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(), 0.1f);
        
        //Idem avec la position dans la tournée
        gestion.ajouterLivraison(aAjouter, 0, -1);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(), 0.1f);
        gestion.ajouterLivraison(aAjouter, 0, nombreLivraison+1);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(), 0.1f);
        
        //Ajout normal
        gestion.ajouterLivraison(aAjouter, 0,0);
        assertSame(aAjouter, gestion.getTournees()[0].getPointPassage(0));
        assertTrue(duree<gestion.getTournees()[0].getTempsTournee());
    }
    
    /**
     * Test de supprimerLivraison
     * -suppression sans calcul de tournée
     * -suppression d'une livraison null
     * -suppression d'une livraison inexistante
     * -suppression d'une livraison existante
     */
    @Test
    public void supprimerLivraison(){
        System.out.println("-- supprimerLivraison");
        
        Intersection inter = new Intersection(0,0,0);
        PointPassage fauxPoint = new PointPassage(false, inter, 60);
        PointPassage existant = this.gestion.getDemande().getLivraisons().get(0);
        int nombreLivraison = this.gestion.getDemande().getLivraisons().size();
        int nombreTournee=2;
        
        //Sans pré-calcul
        gestion.supprimerLivraison(existant);
        assertEquals(nombreLivraison, this.gestion.getDemande().getLivraisons().size());
        
        //calcul
        try{
            gestion.calculerTournees(nombreTournee, Integer.MAX_VALUE);
            while(gestion.threadAlive()){
                Thread.sleep(100);
            }
        } catch(Exception e) {fail(); }
        //S'assurer qu'il est corretement fini
        assertFalse(gestion.calculTSPEnCours());
        assertFalse(gestion.threadAlive());
        assertEquals(nombreTournee, gestion.getTournees().length);
        
        int tournee = gestion.ouEstLePoint(existant)[0];
        int place = gestion.ouEstLePoint(existant)[1];
        
        //Suppression de null
        this.gestion.supprimerLivraison(null);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        assertSame(existant, gestion.getTournees()[tournee].getPointPassage(place));
        
        //Suppression d'un faux
        this.gestion.supprimerLivraison(fauxPoint);
        assertEquals(nombreLivraison, gestion.getDemande().getLivraisons());
        
        //Suppression d'un existant
        this.gestion.supprimerLivraison(existant);
        assertEquals(nombreLivraison-1, gestion.getDemande().getLivraisons());
        assertArrayEquals(new int[] {-1,-1}, gestion.ouEstLePoint(existant));
        
    }
    
    /**
     * Test intervertirPoint
     * - Avec des indices out of bounds
     * - Cas régulier
     */
    public void testIntervertirPoint(){
        System.out.println("-- intervertirPoint");
        
        //calcul
        try{
            gestion.calculerTournees(1, Integer.MAX_VALUE);
            while(gestion.threadAlive()){
                Thread.sleep(100);
            }
        } catch(Exception e) {fail(); }
        //S'assurer qu'il est corretement fini
        assertFalse(gestion.calculTSPEnCours());
        assertFalse(gestion.threadAlive());
        assertEquals(1, gestion.getTournees().length);
        
        float duree = gestion.getTournees()[0].getTempsTournee();
        PointPassage p1 = gestion.getTournees()[0].getPointPassage(1);
        PointPassage p2 = gestion.getTournees()[0].getPointPassage(2);
        
        //Out of bounds
        gestion.intervertirPoint(-1, 0, 0, 0);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(1,0,0,0);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(0,-1,0,0);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(0,1,0,0);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(0, 0, -1, 0);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(0,0,gestion.getDemande().getLivraisons().size()+1,0);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(0,0,0,-1);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        gestion.intervertirPoint(0, 0, 0, gestion.getDemande().getLivraisons().size()+1);
        assertEquals(duree, gestion.getTournees()[0].getTempsTournee(),0.1f);
        
        //Cas régulier
        gestion.intervertirPoint(0, 0, 1, 2);
        assertSame(p2, gestion.getTournees()[0].getPointPassage(1));
        assertSame(p1, gestion.getTournees()[0].getPointPassage(2));
        assertTrue(duree < gestion.getTournees()[0].getTempsTournee());
    }
}
