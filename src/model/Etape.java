package model;

public class Etape {
	
	private int[] tabPlaques = new int [6];
	private boolean bCalculOk;
	private int iIndice1, iIndice2;
	private String sOperation;
	private int iResultat;
	
	/**
	 * Constructeur publique 
	 * @param plaques
	 */
	public Etape(int[] plaques) {
		this.tabPlaques = plaques;
	}
	
	/**
	 * calcule une equation
	 * @param iIndicePlaque1
	 * @param iIndicePlaque2
	 * @param sOperation
	 */
	public void calculer(int iIndicePlaque1, int iIndicePlaque2, String sOperation) {
		this.iIndice1 = iIndicePlaque1;
		this.iIndice2 = iIndicePlaque2;
		this.sOperation = sOperation;
		this.bCalculOk = true;
		if(sOperation.equals("+")) iResultat = tabPlaques[iIndicePlaque1]+tabPlaques[iIndicePlaque2]; //si l'operation est +
		else if(sOperation.equals("-")) iResultat = tabPlaques[iIndicePlaque1]-tabPlaques[iIndicePlaque2]; //si l'operation est -
		else if(sOperation.equals("x")) iResultat = tabPlaques[iIndicePlaque1]*tabPlaques[iIndicePlaque2]; //si l'operation est x
	
		else if(tabPlaques[iIndicePlaque1]%tabPlaques[iIndicePlaque2] == 0) iResultat = tabPlaques[iIndicePlaque1]/tabPlaques[iIndicePlaque2];
		else if(tabPlaques[iIndicePlaque1]%tabPlaques[iIndicePlaque2] != 0) this.bCalculOk = false;
		//si l'operation est /
	}
	
	/**
	 * @return le resultat sous forme de chaine de caractere
	 */
	public String resultatSousFormeDeString() {
		return tabPlaques[iIndice1] + sOperation + tabPlaques[iIndice2] + "=" + iResultat;
	}
	
	/**
	 * @return vrai si le calcul est resolu sinon false
	 */
	public boolean calculIsOk() {
		return this.bCalculOk;
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
		return iResultat;
	}
}
