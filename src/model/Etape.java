package model;

public class Etape {
	
	private int[] tabPlaques = new int [6];
	private boolean calculOk;
	private int indice1, indice2;
	private String operation;
	private int resultat;
	
	/**
	 * Constructeur publique 
	 * @param plaques
	 */
	public Etape(int[] plaques) {
		this.tabPlaques = plaques;
	}
	
	/**
	 * calcule une equation
	 * @param indicePlaque1
	 * @param indicePlaque2
	 * @param operation
	 */
	public void calculer(int indicePlaque1, int indicePlaque2, String operation) {
		this.indice1 = indicePlaque1;
		this.indice2 = indicePlaque2;
		this.operation = operation;
		this.calculOk = true;
		if(operation.equals("+")) resultat = tabPlaques[indicePlaque1]+tabPlaques[indicePlaque2]; //si l'operation est +
		else if(operation.equals("-")) resultat = tabPlaques[indicePlaque1]-tabPlaques[indicePlaque2]; //si l'operation est -
		else if(operation.equals("x")) resultat = tabPlaques[indicePlaque1]*tabPlaques[indicePlaque2]; //si l'operation est x
	
		else if(tabPlaques[indicePlaque1]%tabPlaques[indicePlaque2] == 0) resultat = tabPlaques[indicePlaque1]/tabPlaques[indicePlaque2];
		else if(tabPlaques[indicePlaque1]%tabPlaques[indicePlaque2] != 0) this.calculOk = false;
		//si l'operation est /
	}
	
	/**
	 * @return le resultat sous forme de chaine de caractere
	 */
	public String resultatSousFormeDeString() {
		return tabPlaques[indice1] + operation + tabPlaques[indice2] + "=" + resultat;
	}
	
	/**
	 * @return vrai si le calcul est resolu sinon false
	 */
	public boolean calculIsOk() {
		return this.calculOk;
	}

	/**
	 * @return les plaques d'une etape
	 */
	public int[] getTabPlaques() {
		return tabPlaques;
	}
	
	/**
	 * @return retourne le resultat d'une etape
	 */
	public int getResultat() {
		return resultat;
	}
}
