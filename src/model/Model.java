package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

public class Model {

	private Random rand = new Random();  
	private LinkedList<Etape> deroulement;
	private GereScores gereScores;
	private String pseudo;
	private int nbATrouver;
	private int dureeMax;
	private String modeDeJeu; 

	/** 
	 * Instance unique non pré-initialisée 
	 */
	private static Model INSTANCE = null;

	/** 
	 * Constructeur privé 
	 */
	private Model()
	{
		this.modeDeJeu = "00";
		this.dureeMax = 180;
		this.pseudo = null;
		this.nbATrouver = rand.nextInt(999 - 100 + 1) + 100;
		this.deroulement = new LinkedList <Etape>();
		this.gereScores = GereScores.getInstance();
		this.gereScores.load();
		creerEtape(); 
	}

	/** 
	 * Point d'accès pour l'instance unique du singleton 
	 */
	public static Model getInstance()
	{   
		if (INSTANCE == null) INSTANCE = new Model();
		return INSTANCE;
	}

	public void attendre() {
		this.modeDeJeu = "01";
	}

	public int[] preparer(String pseudoJoueur) {
		this.modeDeJeu = "02";
		this.pseudo = pseudoJoueur;	
		setNbATrouver();
		Etape etape1 = creerEtape();
		deroulement.add(etape1);
		return etape1.getTabPlaques();
	}
	
	public void jouer() {
		this.modeDeJeu = "03";
	}
	
	public String calculer(int indice1, int indice2, String operation) {
		Etape derniereEtape = deroulement.get(deroulement.size()-1);
		derniereEtape.calculer(indice1, indice2, operation);		
		return derniereEtape.resultatSousFormeDeString();
	}
	
	public int[] supprimerEtape() {
		deroulement.removeLast();
		Etape derniereEtape = deroulement.get(deroulement.size()-1);
		return derniereEtape.getTabPlaques();
	}

	public void score(int temps) {
		Etape derniereEtapeValidee = deroulement.get(deroulement.size()-2);
		int score = nbATrouver-derniereEtapeValidee.getResultat();
		gereScores.addScore(pseudo, score, temps);
	}
	
	public int[] creerNouvellesPlaques(int indice1, int indice2) {
		Etape derniereEtape = deroulement.get(deroulement.size()-1);
		int[] plaquesOld = derniereEtape.getTabPlaques();
		int[] newPlaques = new int[plaquesOld.length-1];
		int indiceTabNewPlaques = 0;
		for (int i = 0; i < plaquesOld.length; i++) {
			if(i != indice1 && i != indice2) {
				newPlaques[indiceTabNewPlaques] = plaquesOld[i];
				indiceTabNewPlaques++;
			}
		} 
		newPlaques[indiceTabNewPlaques] = derniereEtape.getResultat();
		Etape nouvelleEtape = new Etape(newPlaques);
		deroulement.add(nouvelleEtape);
		return newPlaques;
	}

	private Etape creerEtape() {  	
		int[] plaques = {25,50,75,100,rand.nextInt(10 - 1 + 1) + 1, rand.nextInt(10 - 1 + 1) + 1};  	
		return new Etape(plaques);
	}

	public String getNbATrouver() {
		return "" + nbATrouver;
	}

	public void setNbATrouver() {
		this.nbATrouver = rand.nextInt(999 - 100 + 1) + 100;
	}

	public int getDureeMax() {
		return dureeMax;
	}

	public void setDureeMax(int dureeMax) {
		this.dureeMax = dureeMax;
	}

	public String[] getScores() {
		String[] scores = new String[gereScores.getTOP()];
		int indice = 0;
		for(Score score: gereScores.getTableJeu()) {
			scores[indice] = score.getValeur() + " " + score.getPseudo() + " " + score.getTemps()/60 + ":" + score.getTemps()%60;
			indice ++;
		};
		return scores;
	}
}
