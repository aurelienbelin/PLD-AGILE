/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package controleur.commandes;

/**
 * Décrit l'état dans lequel se situe une commande.
 * Cela a pour but d'éviter deux fois de suite la méthode doCde() ou undoCde() d'une commande.
 * Avec un peu de bon sens, on ne peut pas annuler deux fois consécutive une commande !
 * @author Louis
 */
enum EtatCommande {
    INACTIVE,
    EXECUTEE,
    ANNULEE;
}
