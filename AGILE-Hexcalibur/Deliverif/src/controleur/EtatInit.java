/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur;

import deliverif.Deliverif;
import java.io.IOException;
import modele.GestionLivraison;
import org.xml.sax.SAXException;

/** Etat dans lequel l'application se trouve à son ouverture
 *  Possibilité de charger un plan
 *
 * @author Hex'Calibur
 */

public class EtatInit extends EtatDefaut{
    
    /**
     * Constructeur EtatInit
     */
    public EtatInit (){}
    
    /**  Cette méthode délègue le chargement du plan au modèle
      *  Si le chargement s'est bien passé on passe dans 
      *  l'EtatPlanCharge
      *  @param controleur
      *  @param gestionLivraison
      *  @param fichier Nom du fichier du plan choisi
      *  @param fenetre
      *  @see modele.GestionLivraison
      *  @see EtatPlanCharge
     */
    @Override
    public void boutonChargePlan (Controleur controleur, modele.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) {
        try{
            gestionLivraison.chargerPlan(fichier);
            controleur.setEtatCourant(Controleur.ETAT_PLAN_CHARGE);
            fenetre.estPlanCharge("SUCCES");
        } catch (SAXException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (IOException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (Exception e) {
            fenetre.estPlanCharge(e.getMessage());
            
        }
    }

    /**Pas de sélection livraison sur le plan dans cet état
     * @param controleur
     * @param gestionLivraison
     * @param fenetre
     * @param latitude
     * @param longitude 
     */
    @Override
    public void clicGauche(Controleur controleur, GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
    }
    
    /**Pas de zoom dans cet état
     * @param fenetre
     * @param lat
     * @param lon 
     */
    @Override
    public void zoomPlus(Deliverif fenetre, double lat, double lon){}
    
    /**Pas de zoom dans cet état
     * @param fenetre
     * @param lat
     * @param lon 
     */
    @Override
    public void zoomMoins(Deliverif fenetre, double lat, double lon){}
}
