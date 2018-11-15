/*
 * Projet Deliverif
 *
 * Hexanome n° 4102
 *
 * Projet développé dans le cadre du cours "Conception Orientée Objet
 * et développement logiciel AGILE".
 */
package modele.outils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 * @version 1.0 23/10/2018
 * @author Christine Solnon, modifié par Louis Ohl.
 */
public abstract class TemplateTSP extends Observable implements TSP{
	/*L'ajout du extends Observable permet de rendre compte en temps réel
        des nouvelles modifications du meilleurChemin !
        */
	private Integer[] meilleureSolution;
	private int coutMeilleureSolution = 0;
	private Boolean tempsLimiteAtteint;
        private Boolean arretCalcul;
        
        private Boolean calculEnCours;
        
        protected int quantiteTournee;//compte le nombre de sommet visite au cours d'une tournee
        private int nombreEntrepot;
	
	public Boolean getTempsLimiteAtteint(){
            return tempsLimiteAtteint;
	}
	
	public void chercheSolution(int tpsLimite, int nbSommets, int nbLivreur, int[][] cout){
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Integer.MAX_VALUE;
		meilleureSolution = new Integer[nbSommets+nbLivreur-1];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i=1; i<nbSommets; i++) nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
                this.quantiteTournee=0;
                this.arretCalcul=false;
                this.nombreEntrepot=1;
                this.calculEnCours=true;
		branchAndBound(0, nonVus, vus, 0, cout, System.currentTimeMillis(), tpsLimite);
                this.calculEnCours=false;
	}
	
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}
	
	public int getCoutMeilleureSolution(){
		return coutMeilleureSolution;
	}
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param sommetCourant
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
         * @param nombreEntrepot : le nombre d'entrepots (virtuels) vus jusqu'à présent
	 * @return une borne inferieure du cout des permutations commencant par sommetCourant, 
	 * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
	 */
	protected abstract int bound(ArrayList<Integer> vus, Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int nombreEntrepot);
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCrt
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout);
	
	/**
	 * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
	 * @param sommetCrt le dernier sommet visite
	 * @param nonVus la liste des sommets qui n'ont pas encore ete visites
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param coutVus la somme des couts des arcs du chemin passant par tous les sommets de vus + la somme des duree des sommets de vus
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param tpsDebut : moment ou la resolution a commence
	 * @param tpsLimite : limite de temps pour la resolution
	 */	
        protected void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, int[][] cout, long tpsDebut, int tpsLimite){
            if (System.currentTimeMillis() - tpsDebut > tpsLimite || arretCalcul){
                    tempsLimiteAtteint = true;
                    return;
            }
	    if (nonVus.isEmpty() && vus.size()==meilleureSolution.length){ // tous les sommets ont ete visites
	    	coutVus += cout[sommetCrt][0];
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
                    vus.toArray(meilleureSolution);
                    coutMeilleureSolution = coutVus;
                    synchronized(this){
                        setChanged();
                        notifyObservers(); // Coucou, màj vue  de ce qui observe ce calcul en cours.
                    }
                }
                
	    }
            if (!solutionConvenable(vus, nonVus, cout)){
                return;
            }
            else if (coutVus + bound(vus, sommetCrt, nonVus, cout, nombreEntrepot) < coutMeilleureSolution){
                //Eviter les erreurs d'overflow
                //quand coutMeilleureSolution vaut encore Integer.MAX_VALUE au début
                //car bound peut renvoyer coutMeilleureSolution.
                Iterator<Integer> it = iterator(sommetCrt, nonVus, cout);
                while (it.hasNext()){
                    Integer prochainSommet = it.next();
                    vus.add(prochainSommet);
                    nonVus.remove(prochainSommet);
                    if (prochainSommet!=0){
                        this.quantiteTournee++;
                    } else {
                        this.quantiteTournee=0;//reset, nouvelle tournee !
                        this.nombreEntrepot++;
                    }
                    branchAndBound(prochainSommet, nonVus, vus, coutVus + cout[sommetCrt][prochainSommet], cout, tpsDebut, tpsLimite);
                    if (prochainSommet!=0){
                        vus.remove(prochainSommet);
                        nonVus.add(prochainSommet);
                        this.quantiteTournee--;
                    } else {
                        //Il faut retrouver le nombre de noeud depuis le dernier
                        //entrepot virtuel
                        vus.remove(vus.size()-1);
                        this.quantiteTournee=0;
                        this.nombreEntrepot--;
                        for(int i=vus.size()-1; vus.get(i)!=0;i--){
                            this.quantiteTournee++;
                        }
                    }
                }
	    }
	}
        
        /**
         * Analyse si la solution partielle est encore valide, i.e. si les symétries
         * sont cassées et si chaque tournée laisse encore assez de sommet à voir
         * pour les livreurs restant.
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param nonVus la liste des sommets qui n'ont pas encore ete visites
         * @return true si la solution est considérée comme convenable. false sinon (i.e. arrêter le branch and bound, remonter
         * à l'étape précédente.
         */
        protected boolean solutionConvenable(ArrayList<Integer> vus, ArrayList<Integer> nonVus, int[][] cout){
            /**
            * Dans un premier temps, est-ce qu'on verifie la condition suivante :
            * premier sommet de la tournee i > premier sommet des tournees i-1
            * Si ce n'est pas le cas :
            * On vérifie qu'il reste assez de sommet nonVus pour les livreurs
            * sans tournee. 
            */
            int premierSommet=0;
            boolean apresZero=false;
            for (Integer sommet : vus){
                if (apresZero){
                    apresZero=false;
                    if (sommet<=premierSommet){
                        return false;
                    } else {
                        premierSommet=sommet;
                    }
                }
                if (sommet==0){
                    apresZero=true;
                }
            }
            int nbLivreur = meilleureSolution.length-cout.length+1;
            int quantiteSommet = (cout.length-1)/nbLivreur;
            if (quantiteSommet*(nbLivreur-nombreEntrepot)>nonVus.size() && !nonVus.isEmpty()){
                //Il ne reste pas assez de sommet pour que cela soit réparti équitablement
                //entre les livreurs restant et le livreur courant
                return false;
            }
            if (nonVus.size()<quantiteSommet && nombreEntrepot!=nbLivreur){
                //Stop, l'un des pénultièmes est en train de tout rafler
                //car les autres ont été trop gourmands !
                //Le dernier n'aura rien !
                return false;
            }
            return true;
        }
        
        public void arreterCalcul(){
            this.arretCalcul=true;
            this.calculEnCours=false;
        }
        
        public boolean calculEnCours(){
            return this.calculEnCours;
        }
         
}

