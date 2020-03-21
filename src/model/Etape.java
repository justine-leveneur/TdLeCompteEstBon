package model;

public class Etape {
	
	private int[] tabPlaques = new int [6];
	private boolean calculOk;
	private int indice1, indice2;
	private String operation;
	private int resultat;
	
	public Etape(int[] plaques) {
		this.tabPlaques = plaques;
	}
	
	public void calculer(int indicePlaque1, int indicePlaque2, String operation) {
		if(indicePlaque1 == indicePlaque2);// TODO
		else {
			this.indice1 = indicePlaque1;
			this.indice2 = indicePlaque2;
			this.operation = operation;
			calculerLeResultat(indicePlaque1, indicePlaque2, operation);
		}		
	}
	
	private void calculerLeResultat(int indicePlaque1, int indicePlaque2, String operation) {
		if(operation.equals("+")) resultat = tabPlaques[indicePlaque1]+tabPlaques[indicePlaque2];
		else if(operation.equals("-")) resultat = tabPlaques[indicePlaque1]-tabPlaques[indicePlaque2];
		else if(operation.equals("x")) resultat = tabPlaques[indicePlaque1]*tabPlaques[indicePlaque2];
		
		else if(tabPlaques[indicePlaque1]%tabPlaques[indicePlaque2] == 0) resultat = tabPlaques[indicePlaque1]/tabPlaques[indicePlaque2];
		else if(tabPlaques[indicePlaque1]%tabPlaques[indicePlaque2] != 0) calculOk = false;
	}
	
	public String resultatSousFormeDeString() {
		return tabPlaques[indice1] + operation + tabPlaques[indice2] + "=" + resultat;
	}

	public int[] getTabPlaques() {
		return tabPlaques;
	}
	
	public int getResultat() {
		return resultat;
	}
}
