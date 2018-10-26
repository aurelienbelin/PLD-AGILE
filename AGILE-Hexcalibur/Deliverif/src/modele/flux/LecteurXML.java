/*
 * Projet Deliverif
 *
 * Hexanome n° 41
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.flux;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
import modele.outils.DemandeLivraison;
import modele.outils.Intersection;
import modele.outils.PlanVille;
import modele.outils.PointPassage;
import modele.outils.Troncon;
import org.xml.sax.SAXException;
/**
 * Classe chargé de la lecture des fichier XML et de charger ainsi les plans de ville et les demandes de livraisons
 * @author Amine Nahid
 * @version 1.0 23.10.18
 */
public class LecteurXML {
    
    /**
     * 
     * @param urlFichierXML chemin du XML à charger
     * @return Document XML parsé et normalisé
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws Exception 
     */
    public Document chargerXML (String urlFichierXML) throws ParserConfigurationException, SAXException, IOException, Exception{
        try {
            //chemin du fichier XML en entrée
            File fichier = new File(urlFichierXML);
            //type du fichier
            String mimeType = Files.probeContentType(fichier.toPath());
            if (fichier.canRead() && (mimeType.equals("application/xml") || mimeType.equals("text/xml"))) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document documentXML = dBuilder.parse(fichier);
                //Normalisation du fichier, remplace les espaces de tous types par un espace simple
                documentXML.getDocumentElement().normalize();
                return documentXML;
            }
            else{
                throw new IOException("Le fichier en entrée n'est pas au format xml");
            }
            //return null;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Création du plan de ville à partir d'un document XML chargé et parsé
     * @param urlFichierXML 
     * @return plan de la ville chargée
     */
    public PlanVille creerPlanVille (String urlFichierXML) throws SAXException, IOException, Exception{
        Document documentXML;
        try {
            documentXML = this.chargerXML(urlFichierXML);
            if (documentXML != null && documentXML.getDocumentElement().getNodeName().equals("reseau")){
            
            //Intégration des intersections au planVille
            NodeList listeNoeudsXML = documentXML.getElementsByTagName("noeud");
            List<Intersection> listeIntersections = new ArrayList<>(); 
            for (int temp = 0; temp < listeNoeudsXML.getLength(); temp++) {
                Node noeud = listeNoeudsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   //création d'un object intersection
                   long idXML = Long.parseLong(eNoeud.getAttribute("id"));
                   float latitude = Float.parseFloat(eNoeud.getAttribute("latitude"));
                   float longitude = Float.parseFloat(eNoeud.getAttribute("longitude"));
                   Intersection intersection = new Intersection(idXML, latitude, longitude);
                   
                   //ajout du tronçon à la liste des intersections du plan de la ville
                   listeIntersections.add(intersection);
                }
            }
            
            //Intégration des tronçons au plan ville
            NodeList listeTronconsXML = documentXML.getElementsByTagName("troncon");
            List<Troncon> listeTroncons = new ArrayList<>();
            for (int temp = 0; temp < listeTronconsXML.getLength(); temp++) {
                Node noeud = listeTronconsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   
                   //création d'un object troncon
                   String nomRue = eNoeud.getAttribute("nomRue");
                   long origine = Long.parseLong(eNoeud.getAttribute("origine"));
                   Intersection debut =listeIntersections.stream()
                           .filter(a -> Objects.equals(a.getIdXML(), origine))
                           .collect(Collectors.toList()).get(0);
                   long destination = Long.parseLong(eNoeud.getAttribute("destination"));
                   Intersection fin =listeIntersections.stream()
                           .filter(a -> Objects.equals(a.getIdXML(), destination))
                           .collect(Collectors.toList()).get(0);
                   float longueur = Float.parseFloat(eNoeud.getAttribute("longueur"));
                   Troncon troncon = new Troncon(nomRue, debut, fin, longueur);
                   
                   //ajout du tronçon à la liste des tronçons du plan de la ville
                   listeTroncons.add(troncon);
                   //ajout du tronçon dans la liste des tronçons, attibut de l'intersection d'origine
                   listeIntersections.stream()
                           .filter(a -> Objects.equals(a.getIdXML(), origine))
                           .collect(Collectors.toList()).get(0).addTroncon(troncon);
                }
            }
            PlanVille planVille = new PlanVille(listeIntersections, listeTroncons);
            return planVille;

        }
        } catch (SAXException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return null;
    }
    
    /**
     * 
     * @param urlFichierXML
     * @return la demande de livraison
     */
    public DemandeLivraison creerDemandeLivraison (String urlFichierXML, PlanVille planVille) throws SAXException, IOException, Exception{
        Document documentXML;
        try {
            documentXML = this.chargerXML(urlFichierXML);
            if(planVille == null) throw new Exception ("le plan de la ville n'est pas chargé !");
            if (documentXML != null && documentXML.getDocumentElement().getNodeName().equals("demandeDeLivraisons")){
            DemandeLivraison demande = new DemandeLivraison(null, null);
            
            //Intégration de l'entrepot à l'instance demande de lobjet DemandeLivraison
            NodeList noeudEntrepotXML = documentXML.getElementsByTagName("entrepot");
            Node noeudEntrepot = noeudEntrepotXML.item(0);
            if (noeudEntrepot.getNodeType() == Node.ELEMENT_NODE) {
                Element eNoeud = (Element) noeudEntrepot;
                //TODO : corriger création de l'entrepôt
                Intersection position = planVille.getIntersections().stream()
                        .filter(a -> Objects.equals(a.getIdXML(), Long.parseLong(eNoeud.getAttribute("adresse"))))
                        .collect(Collectors.toList()).get(0);
                PointPassage entrepot = new PointPassage(true, position, 0);
                demande.setEntrepot(entrepot);
            } else throw new Exception("L'entrepot n'est pas défini !");
            
            //Intégration des livraisons à l'instance demande de lobjet DemandeLivraison
            NodeList listeNoeudsXML = documentXML.getElementsByTagName("livraison");
            List<PointPassage> listeLivraisons = new ArrayList<>();
            for (int temp = 0; temp < listeNoeudsXML.getLength(); temp++) {
                Node noeud = listeNoeudsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   //création d'un object intersection
                   Intersection position = planVille.getIntersections().stream()
                        .filter(a -> Objects.equals(a.getIdXML(), Long.parseLong(eNoeud.getAttribute("adresse"))))
                        .collect(Collectors.toList()).get(0);
                   int duree = Integer.parseInt(eNoeud.getAttribute("duree"));
                   PointPassage livraison = new PointPassage(false, position, duree);
                   
                   //ajout du tronçon à la liste des intersections du plan de la ville
                   listeLivraisons.add(livraison);
                }
            }
            
            demande.setLivraisons(listeLivraisons);
            return demande;

        }
        } catch (SAXException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return null;
    }
    
    public LecteurXML(){
    }
}
