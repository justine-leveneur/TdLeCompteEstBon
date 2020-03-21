package model;

import java.util.LinkedList;

public class Model {
	
	private LinkedList deroulement = new LinkedList <Etape>();
	private Object gereScores;
	private String pseudo;
	private int nbATrouver;
	private int dureeMax;
	private String modeDeJeu; 
	
	/** Constructeur privé */
    private Model()
    {}
 
    /** Instance unique pré-initialisée */
    private static Model INSTANCE = new Model();
    
    /** Point d'accès pour l'instance unique du singleton */
    public static Model getInstance()
    {   
    	return INSTANCE;
    }

}
