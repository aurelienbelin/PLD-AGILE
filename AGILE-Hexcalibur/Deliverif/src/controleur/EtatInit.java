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
import modele.outils.GestionLivraison;
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
      *  @param gestionLivraison
      *  @param fichier
      *  @param fenetre
      *  @see modele.GestionLivraison
      *  @see EtatPlanCharge
     */
    @Override
    public void chargePlan (modele.outils.GestionLivraison gestionLivraison, String fichier, deliverif.Deliverif fenetre) {
        try{
            gestionLivraison.chargerPlan(fichier);
            Controleur.etatCourant = Controleur.ETAT_PLAN_CHARGE;
            fenetre.estPlanCharge("SUCCES");
        } catch (SAXException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (IOException e) {
            fenetre.estPlanCharge(e.getMessage());
            
        } catch (Exception e) {
            fenetre.estPlanCharge(e.getMessage());
            
        }
    }

    @Override
    public void clicGauche(GestionLivraison gestionLivraison, Deliverif fenetre, double latitude, double longitude) {
    }
    
    @Override
    public void zoomPlus(Deliverif fenetre, double lat, double lon){}
    
    @Override
    public void zoomMoins(Deliverif fenetre, double lat, double lon){}
}
