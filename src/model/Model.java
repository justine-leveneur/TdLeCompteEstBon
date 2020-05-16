package model;

import java.util.LinkedList;
import java.util.Random;

public class Model {
	private int NB_A_TROUVER_MAX = 999;
	private int NB_A_TROUVER_MIN = 100;
	private int PLAQUE1 = 25;
	private int PLAQUE2 = 50;
	private int PLAQUE3 = 75;
	private int PLAQUE4 = 100;
	
	private Random rand = new Random();  
	private LinkedList<Etape> deroulement;
	private GereScores gereScores;
	private String sPseudo;
	private int iNbATrouver;
	private int iDureeMax;
	private String sModeDeJeu; 	

	/** 
	 * Instance unique non pr�-initialis�e 
	 */
	private static Model INSTANCE = null;

	/** 
	 * represente "l'affichage de depart"
	 * Constructeur priv� 
	 */
	private Model()
	{
		this.sModeDeJeu = "00";
		this.iDureeMax = 60;
		this.sPseudo = null;
		this.iNbATrouver = rand.nextInt(999 - 100 + 1) + 100;
		this.deroulement = new LinkedList <Etape>();
		this.gereScores = GereScores.getInstance();
		this.gereScores.load();
		creerEtape(); 
	}

	/** 
	 * Point d'acc�s pour l'instance unique du singleton 
	 */
	public static Model getInstance()
	{   
		if (INSTANCE == null) INSTANCE = new Model();
		return INSTANCE;
	}
	
	/** 
	 *attendre une action de l'utilisateur
	 */
	public void attendre() {
		this.sModeDeJeu = "01";
	}

	/** 
	 *preparer le jeu: garde le pseudo, met a jour le nombre a trouver, cree la premiere etape
	 * @return les plaques
	 */
	public int[] preparer(String sPseudoJoueur) {
		this.sModeDeJeu = "02";
		this.sPseudo = sPseudoJoueur;	
		setNbATrouver();// modifie le nombre a trouver
		Etape etape1 = creerEtape();
		deroulement.add(etape1);//  ajoute la premiere etape
		return etape1.getTabPlaques(); // retourne les plaques de l'etape
	}
	
	/**
	 * l'utilisateur joue
	 */
	public void jouer() {
		this.sModeDeJeu = "03";
	}
	
	/**
	 * calcule une equation compos� du premier nombre puis d'une operation et enfin d'un deuxieme nombre
	 * @param iIndice1
	 * @param iIndice2
	 * @param sOperation
	 * @return
	 */
	public String calculer(int iIndice1, int iIndice2, String sOperation) {
		Etape derniereEtape = deroulement.get(deroulement.size()-1);
		derniereEtape.calculer(iIndice1, iIndice2, sOperation);// effectue le calcul
		if(derniereEtape.calculIsOk()) return derniereEtape.resultatSousFormeDeString();// retoune le resultat sous forme de chaine de caractere si le calcul est bon
		else return null;
	}
	
	/**
	 * revient en arriere et annule le calcul precedent
	 * @return les plaques modifi�es
	 */
	public int[] supprimerEtape() {
		deroulement.removeLast();// supprime la derniere etape
		Etape derniereEtape = deroulement.get(deroulement.size()-1);
		return derniereEtape.getTabPlaques();// retourne les plaques de la "nouvelle" derniere etape
	}

	/**
	 * genere le score du joueur
	 * @param iTemps
	 */
	public void score(int iTemps) {
		int score;
		if(deroulement.size() == 1) score = iNbATrouver;// si le joueur n'a effectu� aucune operation
		else {
			Etape derniereEtapeValidee = deroulement.get(deroulement.size()-2);
			score = Math.abs(iNbATrouver-derniereEtapeValidee.getResultat());
			gereScores.addScore(sPseudo, score, iDureeMax-iTemps);	// ajoute le score si il fait parti du top 10
		}
	}
	
	/**
	 * recupere le score pour l'afficher directement
	 * @return le score du joueur
	 */
	public int getScore() {
		if(deroulement.size() == 1) return iNbATrouver; // si le joueur n'a effectu� aucune operation
		else {
			Etape derniereEtapeValidee = deroulement.get(deroulement.size()-2); 
			return Math.abs(iNbATrouver-derniereEtapeValidee.getResultat()); // sinon retourne le resultat de la derniere operation effectuee
		}	
	}
	
	/**
	 * cree les nouvelles plaques suite a l'equation choisie par le joueur
	 * @param iIndice1
	 * @param iIndice2
	 * @return les plaques
	 */
	public int[] creerNouvellesPlaques(int iIndice1, int iIndice2) {
		Etape derniereEtape = deroulement.get(deroulement.size()-1);// creation de la derniere etape en recuperant le dernier indice de la liste d'etape
		int[] plaquesOld = derniereEtape.getTabPlaques();// on garde les anciennes plaques pour pouvoir creer les suivantes
		int[] newPlaques = new int[plaquesOld.length-1];// initialisation du tableau comportant les nouvelles plaques
		int indiceTabNewPlaques = 0;
		for (int i = 0; i < plaquesOld.length; i++) { // remplissage du tableau des nouvelles plaques
			if(i != iIndice1 && i != iIndice2) {
				newPlaques[indiceTabNewPlaques] = plaquesOld[i];
				indiceTabNewPlaques++;
			}
		} 
		newPlaques[indiceTabNewPlaques] = derniereEtape.getResultat();// ajout du resultat de l'operation precedente en tant que nouvelle plaque
		Etape nouvelleEtape = new Etape(newPlaques);
		deroulement.add(nouvelleEtape);// ajout de la nouvelle etape
		return newPlaques;
	}

	/**
	 * @return l'etape
	 */
	private Etape creerEtape() {  	
		int[] plaques = {PLAQUE1,PLAQUE2,PLAQUE3,PLAQUE4,rand.nextInt(10 - 1 + 1) + 1, rand.nextInt(10 - 1 + 1) + 1};// creation des plaques
		return new Etape(plaques);
	}

	/**
	 * @return le nombre a trouver sous forme de chaine de caractere
	 */
	public String getNbATrouver() {
		return "" + iNbATrouver;
	}

	/**
	 * modifie le nombre a trouver par un nombre aleatoire compris entre le min et le max
	 */
	public void setNbATrouver() {
		this.iNbATrouver = rand.nextInt(NB_A_TROUVER_MAX - NB_A_TROUVER_MIN + 1) + NB_A_TROUVER_MIN;
	}

	/**
	 * @return la duree maximum d'une partie
	 */
	public int getDureeMax() {
		return iDureeMax;
	}
	

	/**
	 * @return le mode de jeu
	 */
	public String getModeDeJeu() {
		return sModeDeJeu;
	}

	/**
	 * modifier la duree maximum d'une partie
	 * @param iDureeMax
	 */
	public void setDureeMax(int iDureeMax) {
		this.iDureeMax = iDureeMax;
	}

	/**
	 * @return le pseudo
	 */
	public String getPseudo() {
		return sPseudo;
	}

	/**
	 * @return tous les scores c'est a dire les 10 meilleurs
	 */
	public String[] getScores() {
		String[] scores = new String[GereScores.getTOP()];//garde les 10 premiers scores
		int indice = 0;
		for(Score score: gereScores.getTableJeu()) {
			scores[indice] = score.getValeur() //affiche les scores proprement
					+ " point(s) by "+ score.getPseudo() //affiche les scores proprement
					+ " in " + score.getTemps()/60 + ":" + score.getTemps()%60 + " seconds"; //affiche les scores proprement
			indice ++;
		};
		return scores;
	}

	/**
	 * exporte les scores
	 */
	public void exportTheScores() {
		sModeDeJeu = "05";
		gereScores.export();
		gereScores.save();//exporte les scores dans le dossier score
	}
}
