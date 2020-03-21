package model;

import java.util.LinkedList;

public class Model {
	
	private LinkedList deroulement = new LinkedList <Etape>();
	private Object gereScores;
	private String pseudo;
	private int nbATrouver;
	private int dureeMax;
	private String modeDeJeu; 
	
	/** Constructeur priv� */
    private Model()
    {}
 
    /** Instance unique pr�-initialis�e */
    private static Model INSTANCE = new Model();
    
    /** Point d'acc�s pour l'instance unique du singleton */
    public static Model getInstance()
    {   
    	return INSTANCE;
    }

}
