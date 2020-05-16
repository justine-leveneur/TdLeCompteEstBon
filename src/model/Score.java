package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Score implements Comparable <Score> , Serializable{
	
	private String sPseudo;
	private int iValeur; 
	private int iTemps; // temps en secondes
	private String sDate; 
	
	/**
	 * constructeur de la classe Score
	 * @param sPseudo
	 * @param iValeur
	 * @param iTemps
	 */
	public Score(String sPseudo, int iValeur, int iTemps){
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy"); // format de date
		 
		this.sPseudo = sPseudo;
		this.iValeur = iValeur;
		this.iTemps = iTemps;
		this.sDate = formatDate.format(new Date()); // initialiser la date a la date d'ajourd'hui
	}
	
	/**
	 * methode compareTo qui permet de trier une liste contenant des scores
	 */
	@Override
	public int compareTo(Score scoreJ2) {
		
		if(this.iValeur < scoreJ2.iValeur) return -1; // compare en fonction de la valeur
		else if(this.iValeur != scoreJ2.iValeur) return 1;
		else {
			if(this.iTemps < scoreJ2.iTemps) return -1; // compare en fonction du temps
			else if(this.iTemps != scoreJ2.iTemps) return 1;
			else return 0;
		}
	}
	
	/**
	 * @return le temps sous forme "minutes: secondes"
	 */
	public String convertToTime() {
		return (this.iTemps/60 + ":" + this.iTemps%60);
	}
	
	/**
	 * @return le pseudo
	 */
	public String getPseudo() {
		return sPseudo;
	}

	/**
	 * modifie le pseudo
	 * @param sPseudo
	 */
	public void setPseudo(String sPseudo) {
		this.sPseudo = sPseudo;
	}

	/**
	 * @return la valeur
	 */
	public int getValeur() {
		return iValeur;
	}

	/**
	 * modifie la valeur
	 * @param iValeur
	 */
	public void setValeur(int iValeur) {
		this.iValeur = iValeur;
	}

	/**
	 * @return le temps
	 */
	public int getTemps() {
		return iTemps;
	}

	/**
	 * modifie le temps
	 * @param temps
	 */
	public void setTemps(int temps) {
		this.iTemps = temps;
	}

	/**
	 * @return la date
	 */
	public String getDate() {
		return sDate;
	}
}
