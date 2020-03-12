package modele;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GereScores {
	private List <Score> tableJeu;
	public static int TOP = 10;
	public static String PATH = "./score.ser";
	public static String HTMLFILE = "./scores.html";

	/**
	 * gere les scores
	 */
	public void gere() {		
		load();
		save();
		export();
	}

	/**
	 * ajout du score d'un joueur
	 * @param pseudo
	 * @param valeur
	 * @param temps
	 */
	public void addScore(String pseudo, int valeur, int temps) {
		tableJeu.add(new Score(pseudo, valeur, temps));
		sort();
		while(tableJeu.size() > TOP) {
			tableJeu.remove(TOP);
		}
	}

	/**
	 * trie de la liste de score
	 */
	public void sort() {
		Collections.sort(tableJeu);
	}

	/**
	 * charge le contenu du fichier et ajoute les scores
	 */
	public void load() {
		File file = new File(PATH);
		tableJeu = new LinkedList <Score>();

		if(file.exists()) {
			ObjectInputStream oos = null;
			try {
				FileInputStream fichier = new FileInputStream(PATH);
				oos = new ObjectInputStream(fichier);
				boolean reachEnd = false;
				while(!reachEnd) {
					try {
						Score sc = (Score) oos.readObject();
						if(sc != null) {
							tableJeu.add(sc);
						}
					}catch(EOFException e) {
						reachEnd = true;
					}
				}

			} catch (final java.io.IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (oos != null) {
						oos.close();
					}
				} catch (final IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		sort();
	}

	/**
	 * met a jour le fichier avec le tableau de scores
	 */
	public void save() {
		ObjectOutputStream oos = null;

		try {
			final FileOutputStream fichier = new FileOutputStream(PATH);
			oos = new ObjectOutputStream(fichier);
			for(Score score : tableJeu) {
				oos.writeObject(score);
			}

			oos.flush();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * cree le fichier html affichant les differents scores
	 */
	public void export() {
		try {
			BufferedWriter fichier = new BufferedWriter(new FileWriter(HTMLFILE));

			fichier.write("<HTML>\r\n" + 
					"			<head>\r\n" + 
					"				<title>S C O R E S</title>\r\n" + 
					"			</head>\r\n" + 
					"			<body>\r\n" + 
					"				<h1>S C O R E S</h1>\n");
			for(Score score : tableJeu) {
				fichier.write("<p><b style=\"color:#B41414\">"+score.getPseudo()
				+ "="+score.getValeur()
				+"</b> en "+score.convertToTime()
				+"<small><small>(le "+score.getDate()
				+")</small></small></p>\n");
			}

			fichier.write("</body>\r\n" + 
					"</HTML>");

			fichier.close();
		}catch (Exception e) {
			System.err.println(e);
		} 
	}
}
