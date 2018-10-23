package flux;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.ParserConfigurationException;
import modele.Intersection;
import modele.PlanVille;
import modele.Troncon;
import org.xml.sax.SAXException;
/**
 *
 * @author Amine Nahid
 */
public class LecteurXML {
    
    /**
     * 
     * @param urlFichierXML chemin du XML à charger
     * @return
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
            String mimeType = new MimetypesFileTypeMap().getContentType(fichier);
            if (fichier.canRead() && mimeType.equals("application/xml")) {
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
    public PlanVille creerPlanVille (String urlFichierXML){
        Document documentXML;
        try {
            documentXML = this.chargerXML(urlFichierXML);
            if (documentXML != null && documentXML.getDocumentElement().getNodeName().equals("reseau")){
            PlanVille planVille = new PlanVille();
            
            //Intégration des intersections au planVille
            NodeList listeNoeudsXML = documentXML.getElementsByTagName("noeud");
            List<Intersection> listeIntersections = new ArrayList<>(); 
            for (int temp = 0; temp < listeNoeudsXML.getLength(); temp++) {
                Node noeud = listeNoeudsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   //création d'un object intersection
                   int idXML = Integer.parseInt(eNoeud.getAttribute("id"));
                   float latitude = Float.parseFloat(eNoeud.getAttribute("latitude"));
                   float longitude = Float.parseFloat(eNoeud.getAttribute("longitude"));
                   //TODO: nouvelle intersection, à décommenter avec l'implémentation d'inter)
                   Intersection intersection = new Intersection();//(idXML, latitude, longitude);
                   listeIntersections.add(intersection);
                }
            }
            
            //Intégration des tronçons au plan ville
            NodeList listeTronconsXML = documentXML.getElementsByTagName("noeud");
            List<Troncon> listeTroncons = new ArrayList<>();
            for (int temp = 0; temp < listeTronconsXML.getLength(); temp++) {
                Node noeud = listeTronconsXML.item(temp);
                if (noeud.getNodeType() == Node.ELEMENT_NODE) {
                   Element eNoeud = (Element) noeud;
                   //création d'un object troncon
                   String nomRue = eNoeud.getAttribute("nomRue");
                   int origine = Integer.parseInt(eNoeud.getAttribute("origine"));
                   Intersection debut =listeIntersections.stream()
                           .filter(a -> Objects.equals(a.idXML, origine))
                           .collect(Collectors.toList()).get(0);
                   int destination = Integer.parseInt(eNoeud.getAttribute("destination"));
                   Intersection fin =listeIntersections.stream()
                           .filter(a -> Objects.equals(a.idXML, destination))
                           .collect(Collectors.toList()).get(0);
                   float longueur = Float.parseFloat(eNoeud.getAttribute("longueur"));
                   //TODO: nouveau tronçon, à décommenter avec l'implémentation d'inter)
                   Troncon troncon = new Troncon();//(nom, debut, fin, longueur);
                   listeTroncons.add(troncon);
                   //ajout du tronçon dans la liste des tronçons, attibut de l'intersection d'origine
                   listeIntersections.stream()
                           .filter(a -> Objects.equals(a.idXML, origine))
                           .collect(Collectors.toList()).get(0).addTroncon(troncon);
                }
            }
            //TODO : planVille.setIntersections(listeIntersections);
            //TODO : planVille.setTroncons(listeTroncons);
            return planVille;

        }
        } catch (SAXException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LecteurXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public LecteurXML(){
    }
}
