package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Score implements Comparable <Score> , Serializable{
	
	private String pseudo;
	private int valeur; 
	private int temps; // temps en secondes
	private String date; 
	
	/**
	 * constructeur de la classe Score
	 * @param pseudo
	 * @param valeur
	 * @param temps
	 */
	Score(String pseudo, int valeur, int temps){
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy"); // format de date
		 
		this.pseudo = pseudo;
		this.valeur = valeur;
		this.temps = temps;
		this.date = formatDate.format(new Date()); // initialiser la date a la date d'ajourd'hui
	}
	
	/**
	 * methode compareTo qui permet de trier une liste contenant des scores
	 */
	@Override
	public int compareTo(Score scoreJ2) {
		
		if(this.valeur < scoreJ2.valeur) return -1; // compare en fonction de la valeur
		else if(this.valeur != scoreJ2.valeur) return 1;
		else {
			if(this.temps < scoreJ2.temps) return -1; // compare en fonction du temps
			else if(this.temps != scoreJ2.temps) return 1;
			else return 0;
		}
	}
	
	/**
	 * @return le temps sous forme "minutes: secondes"
	 */
	public String convertToTime() {
		return (this.temps/60 + ":" + this.temps%60);
	}
	
	
	/**
	 * affiche un score
	 */
	public void affiche() {
		System.out.println(this.pseudo.toUpperCase() + " = " + this.valeur + " en " + convertToTime() + "   (le " + this.date + ")");
	}
	
	/**
	 * @return le pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * modifie le pseudo
	 * @param pseudo
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	/**
	 * @return la valeur
	 */
	public int getValeur() {
		return valeur;
	}

	/**
	 * modifie la valeur
	 * @param valeur
	 */
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	/**
	 * @return le temps
	 */
	public int getTemps() {
		return temps;
	}

	/**
	 * modifie le temps
	 * @param temps
	 */
	public void setTemps(int temps) {
		this.temps = temps;
	}

	/**
	 * @return la date
	 */
	public String getDate() {
		return date;
	}
}
