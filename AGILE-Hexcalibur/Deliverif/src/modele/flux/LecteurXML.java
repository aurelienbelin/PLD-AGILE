/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.flux;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlanVille;
import modele.PointPassage;
import modele.Troncon;
import org.xml.sax.SAXException;
/**
 * Classe chargé de la lecture des fichier XML et de charger ainsi les 
 * plans de ville et les demandes de livraisons
 * @author Hex'calibur
 */
public class LecteurXML {
    
    //Messages pour exceptions
    public final static String FORMAT_XML_ERREUR = "Le fichier en entrée n'est"
            + " pas au format xml, ou est corrompu";
    public final static String PLAN_VILLE_ERREUR = "Le fichier chargé ne "
            + "correspond pas à un plan de ville";
    public final static String DEMANDE_LIVRAISON_ERREUR = "Le fichier en entrée"
            + " ne correspond pas à une demande de livraison";
    public final static String PLAN_NON_CHARGE = "Le plan n'est pas chargé,"
            + " ou le fichier est corrompu";
    public final static String ENTREPOT_NON_DEFINI = "L'entrepôt n'est pas "
            + "défini";
    public final static String PAS_DE_LIVRAISON = "Pas de livraison dans cette"
            + " demande";
    
    //Balises dans l'XML
    public final static String RESEAU = "reseau";
    public final static String NOEUD = "noeud";
    public final static String ID = "id";
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";
    public final static String TRONCON = "troncon";
    public final static String NOM_RUE = "nomRue";
    public final static String ORIGINE = "origine";
    public final static String DESTINATION = "destination";
    public final static String LONGUEUR = "longueur";
    public final static String DEMANDE_LIVRAISON = "demandeDeLivraisons";
    public final static String LIVRAISON = "livraison";
    public final static String ENTREPOT = "entrepot";
    public final static String ADRESSE = "adresse";
    public final static String HEURE_DEPART = "heureDepart";
    public final static String DUREE = "duree";
    
    public final static String FORMAT_HEURE = "HH:mm:ss";
    
    /**
     * Charge un fichier XML
     * @param urlFichierXML chemin du XML à charger
     * @return Document XML parsé et normalisé
     * @throws Exception 
     */
    public Document chargerXML (String urlFichierXML) throws Exception{
        try {
            //chemin du fichier XML en entrée
            File fichier = new File(urlFichierXML);
            //type du fichier
            String mimeType = Files.probeContentType(fichier.toPath());
            if (fichier.canRead() && 
                    (mimeType.equals("application/xml") || 
                    mimeType.equals("text/xml"))) {
                DocumentBuilderFactory dbFactory = 
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document documentXML = dBuilder.parse(fichier);
                //Normalisation du fichier, remplace les espaces de tous types 
                //par un espace simple
                documentXML.getDocumentElement().normalize();
                return documentXML;
            } else {
                throw new Exception(FORMAT_XML_ERREUR);
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(
                    Level.SEVERE, null, ex);
            throw new Exception(FORMAT_XML_ERREUR);
        }
    }
    
    /**
     * Crée un plan de ville à partir d'un document XML chargé et parsé
     * @param urlFichierXML 
     * @return plan de la ville chargée
     * @throws java.lang.Exception
     */
    public PlanVille creerPlanVille (String urlFichierXML) throws Exception {
        try {
            Document documentXML = this.chargerXML(urlFichierXML);
            if (documentXML != null && 
                    documentXML.getDocumentElement().getNodeName().equals(RESEAU)){
            
            //Intégration des intersections au planVille
            NodeList listeNoeudsXML = documentXML.getElementsByTagName(NOEUD);
            List<Intersection> listeIntersections = new ArrayList<>(); 
            for (int temp = 0; temp < listeNoeudsXML.getLength(); temp++) {
                Node noeud = listeNoeudsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   //création d'un object intersection
                   long idXML = Long.parseLong(eNoeud.getAttribute(ID));
                   float latitude = Float.parseFloat(
                           eNoeud.getAttribute(LATITUDE));
                   float longitude = Float.parseFloat(
                           eNoeud.getAttribute(LONGITUDE));
                   Intersection intersection = 
                           new Intersection(idXML, latitude, longitude);
                   
                   //ajout du tronçon à la liste des intersections du plan
                   listeIntersections.add(intersection);
                }
            }
            
            //Intégration des tronçons au plan ville
            NodeList listeTronconsXML = documentXML.getElementsByTagName(TRONCON);
            List<Troncon> listeTroncons = new ArrayList<>();
            for (int temp = 0; temp < listeTronconsXML.getLength(); temp++) {
                Node noeud = listeTronconsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   
                   //création d'un object troncon
                   String nomRue = eNoeud.getAttribute(NOM_RUE);
                   long origine = Long.parseLong(eNoeud.getAttribute(ORIGINE));
                   Intersection debut =listeIntersections.stream()
                           .filter(a -> Objects.equals(a.getIdXML(), origine))
                           .collect(Collectors.toList()).get(0);
                   long destination = Long.parseLong(eNoeud.getAttribute(DESTINATION));
                   Intersection fin =listeIntersections.stream()
                           .filter(a -> Objects.equals(a.getIdXML(), destination))
                           .collect(Collectors.toList()).get(0);
                   float longueur = Float.parseFloat(eNoeud.getAttribute(LONGUEUR));
                   if (longueur<0){
                       continue;//Pas de troncon négatif !
                   }
                   Troncon troncon = new Troncon(nomRue, debut, fin, longueur);
                   
                   //ajout du tronçon à la liste des tronçons du plan de la ville
                   listeTroncons.add(troncon);
                   //ajout du tronçon dans la liste des tronçons,
                   //qui est attribut de l'intersection d'origine
                   listeIntersections.stream()
                           .filter(a -> Objects.equals(a.getIdXML(), origine))
                           .collect(Collectors.toList()).get(0).addTroncon(troncon);
                }
            }
            PlanVille planVille = new PlanVille(listeIntersections, listeTroncons);
            return planVille;
            } else {
                throw new Exception(PLAN_VILLE_ERREUR);
            }
        } catch (SAXException | IOException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(PLAN_VILLE_ERREUR);
        } catch (Exception ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(PLAN_VILLE_ERREUR);
        } 
    }
    
    /**
     * Crée une demande de livraison à partir d'un document XML chargé et parsé
     * @param urlFichierXML
     * @param planVille
     * @return la demande de livraison
     * @throws java.lang.Exception
     */
    public DemandeLivraison creerDemandeLivraison (String urlFichierXML, 
            PlanVille planVille) throws Exception{
        try {
            Document documentXML = this.chargerXML(urlFichierXML);
            if(planVille == null) throw new Exception (PLAN_NON_CHARGE);
            if (documentXML != null && documentXML.getDocumentElement().
                    getNodeName().equals(DEMANDE_LIVRAISON)){
            DemandeLivraison demande = new DemandeLivraison(null, null, null);
            
            //Intégration de l'entrepot à l'instance demande de l'objet DemandeLivraison
            NodeList noeudEntrepotXML = documentXML.getElementsByTagName(ENTREPOT);
            Node noeudEntrepot = noeudEntrepotXML.item(0);
            if (noeudEntrepot.getNodeType() == Node.ELEMENT_NODE) {
                Element eNoeud = (Element) noeudEntrepot;
                Intersection position = planVille.getIntersections().stream()
                        .filter(a -> Objects.equals(a.getIdXML(), 
                                Long.parseLong(eNoeud.getAttribute(ADRESSE))))
                        .collect(Collectors.toList()).get(0);
                SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_HEURE);
                Date date = sdf.parse(eNoeud.getAttribute(HEURE_DEPART));
                Calendar temps = Calendar.getInstance();
                temps.setTime(date);
                PointPassage entrepot = new PointPassage(true, position, 0);
                demande.setEntrepot(entrepot);
                demande.setHeureDepart(temps);
            } else throw new Exception(ENTREPOT_NON_DEFINI);
            
            //Intégration des livraisons à l'instance demande de DemandeLivraison
            NodeList listeNoeudsXML = documentXML.getElementsByTagName(LIVRAISON);
            if (listeNoeudsXML.getLength()==0){
                throw new IndexOutOfBoundsException(PAS_DE_LIVRAISON);
            }
            List<PointPassage> listeLivraisons = new ArrayList<>();
            for (int temp = 0; temp < listeNoeudsXML.getLength(); temp++) {
                Node noeud = listeNoeudsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   //création d'un object intersection
                   Intersection position = planVille.getIntersections().stream()
                        .filter(a -> Objects.equals(a.getIdXML(), Long.parseLong(
                                eNoeud.getAttribute(ADRESSE))))
                        .collect(Collectors.toList()).get(0);
                   int duree = Integer.parseInt(eNoeud.getAttribute(DUREE));
                   PointPassage livraison = new PointPassage(false, position, duree);
                   
                   //ajout du tronçon à la liste des intersections du plan de la ville
                   listeLivraisons.add(livraison);
                }
            }
            
            demande.setLivraisons(listeLivraisons);
            return demande;
            } else {
                throw new Exception(DEMANDE_LIVRAISON_ERREUR);
            }
        } catch (SAXException | IOException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(DEMANDE_LIVRAISON_ERREUR);
        } catch (Exception ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(DEMANDE_LIVRAISON_ERREUR);
        }
    }
    
    public LecteurXML(){
    }
}

