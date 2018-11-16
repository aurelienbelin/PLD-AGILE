/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele;

import modele.Intersection;
import modele.PointPassage;
import modele.DemandeLivraison;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Louis
 */
public class DemandeLivraisonTest {
    
    private List<PointPassage> livraisons;
    private PointPassage entrepot;
    
    public DemandeLivraisonTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("JUNIT - DEMANDELIVRAISON TEST ------------------------");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("JUNIT - DEMANDELIVRAISON TEST ----- FIN --------------");
    }
    
    @Before
    public void setUp(){
        livraisons = new ArrayList<PointPassage>();
        Intersection interE = new Intersection(0,0,0);
        Intersection interL1 = new Intersection(1,1,1);
        Intersection interL2 = new Intersection(2,2,2);
        entrepot = new PointPassage(true, interE, 0);
        livraisons.add(new PointPassage(false, interL1, 60));
        livraisons.add(new PointPassage(false, interL2, 60));
    }

    /**
     * Test du constructeur
     * - Bon paramètres
     * - Mauvais paramètres
     */
    @Test
    public void testConstructeur(){
        System.out.println("-- Constructeur");
        
        //Bon constructeur
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, Calendar.getInstance());
        assertSame(this.livraisons, dl.getLivraisons());
        assertSame(this.entrepot, dl.getEntrepot());
        assertNotNull(dl.getHeureDepart());
        
        //Mauvaise construction
        dl = new DemandeLivraison(null, null, null);
        assertNotNull(dl.getLivraisons());
        assertEquals(0,dl.getLivraisons().size());
        assertNull(dl.getEntrepot());
        assertNotNull(dl.getHeureDepart());
        
        //L'entrepot n'est pas un entrepot
        dl = new DemandeLivraison(this.livraisons, this.livraisons.get(0), null);
        assertNotSame(this.livraisons.get(0),dl.getEntrepot());
        assertNull(dl.getEntrepot());
    }

    /**
     * Test de setLivraisons
     * - Affectation de liste null
     * - Affectation d'une liste vide
     * - Affectation d'une liste correcte
     */
    @Test
    public void testSetLivraisons() {
        System.out.println("-- setLivraisons");
        
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, null);
        
        //Affectation null
        dl.setLivraisons(null);
        assertSame(this.livraisons, dl.getLivraisons());
        
        //Affectation d'une liste vide
        List<PointPassage> listeVide = new ArrayList<>();
        dl.setLivraisons(listeVide);
        assertSame(this.livraisons, dl.getLivraisons());
        
        //Une liste valide
        Intersection inter = new Intersection(5,5,5);
        PointPassage p = new PointPassage(false, inter, 0);
        listeVide.add(p);
        dl.setLivraisons(listeVide);
        assertSame(listeVide, dl.getLivraisons());
    }

    /**
     * Test de setEntrepot
     * - Affectation de null
     * - Affectation d'un point qui n'est pas un entrepot
     * - Affectation d'un point valide
     */
    @Test
    public void testSetEntrepot() {
        System.out.println("-- setEntrepot");
        
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, Calendar.getInstance());
        
        //Affectation de null
        dl.setEntrepot(null);
        assertSame(this.entrepot, dl.getEntrepot());
        
        //Affectation d'un point qui n'est pas un entrepot
        dl.setEntrepot(this.livraisons.get(0));
        assertSame(this.entrepot, dl.getEntrepot());
        
        //Affectation d'un point valide
        Intersection inter = new Intersection(5,5,5);
        PointPassage p = new PointPassage(true, inter, 0);
        dl.setEntrepot(p);
        assertSame(p, dl.getEntrepot());
    }

    /**
     * Test de setHeureDepart
     * - Affectation de null
     * - Affectation correcte
     */
    @Test
    public void testSetHeureDepart() {
        System.out.println("-- setHeureDepart");
        
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, Calendar.getInstance());
        
        //cas null
        dl.setHeureDepart(null);
        assertNotNull(dl.getHeureDepart());
        
        //cas normal
        dl.setHeureDepart(Calendar.getInstance());
        assertNotNull(dl.getHeureDepart());
    }

    /**
     * Test de ajoutLivraison
     * - Ajout de null
     * - Ajout d'une bonne livraison
     */
    @Test
    public void testAjouterLivraison() {
        System.out.println("-- ajouterLivraison");
        
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, Calendar.getInstance());
        int tailleAvant=this.livraisons.size();
        //cas null
        dl.ajouterLivraison(null);
        assertEquals(tailleAvant, dl.getLivraisons().size());
        
        //cas normal
        dl.ajouterLivraison(entrepot);//On a le droit de se livrer soi-même !
        assertEquals(tailleAvant+1, dl.getLivraisons().size());
    }

    /**
     * Test de annulerLivraison
     * - On annule une livraison null
     * - On annule une livraison qui n'existe pas
     * - On annule une livraison qui existe
     */
    @Test
    public void testAnnulerLivraison() {
        System.out.println("-- annulerLivraison");
        
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, Calendar.getInstance());
        int tailleAvant=this.livraisons.size();
        
        //cas null
        dl.annulerLivraison(null);
        assertEquals(tailleAvant, dl.getLivraisons().size());
        
        //cas inexistant
        dl.annulerLivraison(this.entrepot);
        assertEquals(tailleAvant, dl.getLivraisons().size());
        
        //Cas réel
        dl.annulerLivraison(this.livraisons.get(0));
        assertEquals(tailleAvant-1, dl.getLivraisons().size());
    }

    /**
     * Test de getDescription
     */
    @Test
    public void testGetDescription() {
        System.out.println("-- getDescription");
        
        DemandeLivraison dl = new DemandeLivraison(this.livraisons, this.entrepot, Calendar.getInstance());
        Iterator<List<String>> iterateur = dl.getDescription();
        int qte=0;
        while(iterateur.hasNext()){
            iterateur.next();
            qte++;
        }
        assertEquals(this.livraisons.size(), qte);
    }
    
}
