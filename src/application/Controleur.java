package application;

import java.awt.Desktop;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Model;

public class Controleur{

	@FXML
	private Button jouer, scores;

	@FXML
	private Button ajouter, soustraire, multiplier, diviser;

	@FXML
	private GridPane tabPlaques, tabScoreAfterTop3;

	@FXML
	private Button annuler, valider, proposer, supprimer;

	@FXML
	private Text dateNow, chrono, warningPseudo, nbATrouver;

	@FXML
	private TextField pseudo;

	@FXML
	private Label operation, score1, score2, score3;

	@FXML
	private TextArea content;
	
	@FXML
	private Pane ongletScores;
	
	@FXML
	private SplitPane ongletJeu;

	private int WARNING_CHRONO = 60;
	private SimpleDateFormat formatDateNow = new SimpleDateFormat("HH:mm:ss");
	private Model model;
	private Button[] plaque = new Button[6]; 
	private Text[] all10Scores = new Text[10]; 
	private int indice1, indice2;
	private String operateur;
	private int secondsChrono;
	private Timer timerChrono;


	/**
	 * methode lancee lorsque l'utilisateur appuie sur le bouton "jouer"
	 * @param commencer
	 */
	@FXML
	private void commencer(ActionEvent commencer) {
		ongletScores.setVisible(false);// l'onglet des scores n'est pas visible
		ongletJeu.setVisible(true);// l'onglet du jeu est donc affiché
		if(pseudo.getText() == null || pseudo.getText().length()<3 || pseudo.getText().length()>8) {
			warningPseudo.setVisible(true); // si le pseudo n'est pas valide affciher une erreur
		}
		else {

			indice1=indice2=-1;// initalisation des indices a -1
			operation.setVisible(true);// les bouttons operations sont visibles
			operation.setText("");// l'espace qui affiche les operations est vide
			tabPlaques.getChildren().removeAll(plaque);// enleve les anciennes plaques
			preparer();
		}
		model.jouer();// lancer le jeu
	}
	

	/**
	 * est executee lorsque le joueur appuie sur un bouton operation
	 * @param choixOperation
	 */
	@FXML
	private void choixOperation (ActionEvent choixOperation) {
		Button boutonOperation = (Button) choixOperation.getSource();// recupere le bouton sur lequel le jouer a cliqué
		operateur = boutonOperation.getText(); // recupere le type d'operation : +,-,x,/
		operation.setText(operation.getText() + boutonOperation.getText());
		changeButtonOprationDisabilitie(true); // les boutons operation ne peuvent plus etre selectionnés
		for (Button valeur: plaque) valeur.setDisable(false);// les boutons des nombres peuvent etre selectionnés
		plaque[indice1].setDisable(true);// mais le bouton du premier nombre selectionné n'est pas disponible car une plaque ne sert qu'une seule fois
	}

	/**
	 * les boutons operation seront utilisabe ou non en fonction du parametre etat
	 * @param state
	 */
	private void changeButtonOprationDisabilitie(boolean state) {
		ajouter.setDisable(state);
		soustraire.setDisable(state);
		multiplier.setDisable(state);
		diviser.setDisable(state);
	}

	/**
	 * executee lorsque le jouer clique sur le bouton valider
	 * @param valider
	 */
	@FXML
	private void valider(ActionEvent valider) {
		if (model.calculer(indice1, indice2, operateur) != null){ //si le resultat est bon
			content.setText(content.getText() + "\n" + model.calculer(indice1, indice2, operateur));// ajoute l'equation dans le champ "content" qui s'affiche instantannement
		operation.setText("");// vide le champ "operation" qui s'affiche instantannement 
		changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);// les boutons nombre peuvent etre selectionnés
		for (Button oldValues: plaque) {
			tabPlaques.getChildren().remove(oldValues);
		} // supprime les plaques
		creationBoutonsPlaques(model.creerNouvellesPlaques(indice1, indice2));// cree les nouvelles plaques
		indice1 = -1; // reinitalise les indices
		indice2 = -1;
		}	
	}

	/**
	 * annule les boutons selectionnés par le joueur
	 * @param annuler
	 */
	@FXML
	private void annuler(ActionEvent annuler) {
		changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);
		operation.setText("");// vide le champ "operation" qui s'affiche instantannement 
		indice1 = -1; // reinitalise les indices
		indice2 = -1;
	}
	
	/**
	 * supprime la precedente equations realisee par le joueur lorsqu'il appuie sur le bouton "supprimer"
	 * @param supprimer
	 */
	@FXML
	private void supprimer(ActionEvent supprimer) {
		if(!content.getText().isEmpty()) {// si le champ est vide rend les boutons operation utilisables
			changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);//rend les boutons des nombres utilisables
		for (Button oldValues: plaque) {
			tabPlaques.getChildren().remove(oldValues);
		}// supprime les plaques
		content.setText(content.getText().substring(0, content.getText().lastIndexOf("\n")));// supprime la derniere equation du champ "content"
		operation.setText("");// vide le champ "operation" qui s'affiche instantannement 
		creationBoutonsPlaques(model.supprimerEtape());// recree les plaques de l'etape precedente
		indice1 = -1; // reinitalise les indices
		indice2 = -1;
		}	
	}
	
	/**
	 * propose la valeur lorsque le joueur appuie sur le bouton "proposer"
	 * @param proposerValeur
	 */
	@FXML
	private void proposerValeur(ActionEvent proposerValeur) {
		finDuJeuEtRecuperationDuScore();
	}

	/**
	 * la partie est fini et le score est sauvegardé en fonction de sa position dans le classement
	 * (s'il fait parti du top10)
	 */
	private void finDuJeuEtRecuperationDuScore() {
		timerChrono.cancel();// arret du chronometre
		model.score(secondsChrono);// transmet le temps au model
		
		jouer.setDisable(false); // les boutons "jouer" et "scores" sont utilisables
		scores.setDisable(false);

		tabPlaques.setVisible(false); //les boutons des plaques ne sont plus visibles
		
		changeButtonOprationDisabilitie(true); // les boutons operation ne sont plus utilisables

		annuler.setDisable(true); // tous les autres boutons de la partie ne sont plus utilisables
		valider.setDisable(true);
		proposer.setDisable(true);
		supprimer.setDisable(true);
		
		JOptionPane.showMessageDialog(null, "ton score est de " 
		+ model.getScore(), "Bravo " 
		+ model.getPseudo() ,JOptionPane.INFORMATION_MESSAGE); // affiche le score realisé dans une pop up
		
		operation.setVisible(false); // le champ "operation" nest plus visible
		content.setText("");// le champ "content" est vidé
		
		secondsChrono = model.getDureeMax();
		chrono.setText(chronoFormate()); // reinitialisation du chrono
	}
	
	/**
	 * executee lorsque l'utilisateur appuie sur le bouton "score"
	 * @param scores
	 */
	@FXML
	private void scores(ActionEvent scores) {
		ouvrirFichierHtml(); // ouvre le fichier scores.html sur internet
		
		ongletScores.setVisible(true); //l'onglet des scores est visible
		ongletJeu.setVisible(false); //et l'onglet du jeu n'est donc plus visible
		String[] tabScores = model.getScores(); //recupere les scores gerés par le model
		if(tabScores[0] != null) score1.setText(tabScores[0].replace("seconds", "").replace(" point(s)", "").replace(" by ", "\n").replace(" in ", ", "));
		if(tabScores[1] != null) score2.setText(tabScores[1].replace("seconds", "").replace("point(s)", "").replace(" by ", "\n").replace(" in ", ", "));
		if(tabScores[2] != null) score3.setText(tabScores[2].replace("seconds", "").replace("point(s)", "").replace(" by ", "\n").replace(" in ", ", "));
		//affiche le top 3 correctement
		for(int i = 3; i < tabScores.length; i++) {
			if(tabScores[i] != null) 
			{
				all10Scores[i] = new Text(tabScores[i]);
				all10Scores[i].toFront();
				tabScoreAfterTop3.add(all10Scores[i], 1, i-3);
			}
			else tabScoreAfterTop3.add(new Separator(), 1, i-3);//affiche les autres scores avec des separateurs entre eux 
		}
	}


	/**
	 * genere plus affiche les fichier scores.html
	 */
	private void ouvrirFichierHtml() {
		model.exportTheScores();// cree le dossier composé des scores
		
		File scoresHtml = new File("score/scores.html");
		  try {
			Desktop.getDesktop().browse(scoresHtml.toURI());// ouvre le fichier scores.html
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	/**
	 * prepare le jeu c'est a dire adapte la visibilite des boutons, demarre le chrono et affiche les plaques ainsi que le chiffre a trouver
	 */
	private void preparer() {
		tabPlaques.setVisible(true); //rend les plaques visibles
		int[] plaques = new int[6];
		if(warningPseudo.isVisible()) warningPseudo.setVisible(false);// rend le warning du pseudo non visible
		pseudo.setDisable(true); //on ne peut plus modifier le pseudo et le chemps est unitilisable
		preparerBoutons();
		plaques = model.preparer(pseudo.getText()); //transmet le pseudo au model
		creationBoutonsPlaques(plaques); //cree les palques
		nbATrouver.setText(model.getNbATrouver()); //modifie le nombre a trouver
		secondsChrono = model.getDureeMax(); //reinitialise le chrono
		defilerChrono(); 
	}

	/**
	 * cree les boutons (plaques) au fur et a mesure et ajout du clique pour chacun d'entre eux
	 */
	private void creationBoutonsPlaques(int[] plaques) {
		for(int i = 0; i < plaques.length; i++) {
			plaque[i] = new Button("" + plaques[i]);
			plaque[i].setId(""+i); // ajout d'un id aux boutons
			plaque[i].setPrefSize(60, 40); //ajuste leur taille
			plaque[i].setOnAction(new EventHandler<ActionEvent>() { //ajout du clique
				@Override public void handle(ActionEvent choixValeur) {
					Button boutonValeur = (Button) choixValeur.getSource();
					addIndices(boutonValeur); 
				}

				/**
				 * si c'est la premiere valeur a etre selecitonnee alors desactive les boutons plaques et active les boutons operation
				 * sinon desactive tous les boutons pour imposer a l'utilisateur de choisir une valeur + un operateur + une valeur 
				 * et d'annuler ou de valider le calcul
				 */
				private void addIndices(Button boutonValeur) {
					if(indice1 == -1) // premiere valeur selectionnee
					{
						indice1 = Integer.parseInt(boutonValeur.getId());
						operation.setText(operation.getText() + boutonValeur.getText());
						for (Button valeur: plaque) valeur.setDisable(true); //desactive les boutons plaques 
						changeButtonOprationDisabilitie(false); //active les boutons operation
					}
					else if(indice2 == -1 && indice1 != -1) //deuxieme valeur selectionnee
					{
						indice2 = Integer.parseInt(boutonValeur.getId());
						operation.setText(operation.getText() + boutonValeur.getText());
						for (Button valeur: plaque) valeur.setDisable(true); //desactive tous les boutons
						changeButtonOprationDisabilitie(true); //desactive tous les boutons
					}
				}
			});
			tabPlaques.add(plaque[i], i, 0); //insere le bouton dans un case du gridpane
		}
	}

	/**
	 * prepare les boutons afin de pouvoir commenecer le jeu
	 */
	private void preparerBoutons() {
		jouer.setDisable(true);//desactive les boutons du menu
		scores.setDisable(true); //desactive les boutons du menu

		//active les autres
		ajouter.setDisable(false); 
		soustraire.setDisable(false);
		multiplier.setDisable(false);
		diviser.setDisable(false);
		annuler.setDisable(false);
		valider.setDisable(false);
		proposer.setDisable(false);
		supprimer.setDisable(false);
	}

	/**
	 * initialise le jeu 
	 */
	@FXML
	private void initialize(){
		ongletScores.setVisible(false); // le jeu s'ouvre sur la page du jeu
		ongletJeu.setVisible(true);
		indice1 = -1; //aucune plaque selectionnee
		indice2 = -1;//aucune plaque selectionnee
		model = Model.getInstance();
		chrono.setText("0" + (int)(model.getDureeMax()/60) + ":0" + model.getDureeMax()%60); //affiche le chrono
		nbATrouver.setText(model.getNbATrouver()); //affiche une valeur aleatoire du nombre a trouver
		dateNow.setText(formatDateNow.format(new Date()));
		defilerTemps();//demarre le temps
		model.attendre();		
	}

	/**
	 * defile le temps (heure, minute et seconde a partir de la date) en temps reel
	 */
	private void defilerTemps() {
		Calendar calendarToDateNow = Calendar.getInstance();
		calendarToDateNow.setTime(new Date());//recupere la date actuelle
		Timer timer = new Timer();

		TimerTask defilerTime = new TimerTask() {// defilement synchronne
			public void run() {
				calendarToDateNow.add(Calendar.SECOND, 1);//ajoute une seconde
				dateNow.setText(formatDateNow.format(calendarToDateNow.getTime()));
			}
		};
		timer.schedule(defilerTime, 1000, 1000);//defilement toutes les secondes
	}

	/**
	 * defile le chronometre
	 */
	private void defilerChrono() {
		timerChrono = new Timer();	
		TimerTask chronometre = new TimerTask() {

			public void run() { //lance le chrono en thread
				secondsChrono -= 1; //diminue le chronometre de 1 seconde
				chrono.setText(chronoFormate()); //formate les chronometre
				if(secondsChrono == 0) { //lorsqu'il atteint 0, arret du chrono et de la partie
					timerChrono.cancel();
					finDuJeuEtRecuperationDuScore();
				}
			}
		};
		timerChrono.schedule(chronometre, 1000, 1000);// realise le run toute les secondes
	}

	public String chronoFormate() {
		if(secondsChrono<WARNING_CHRONO+1)chrono.setFill(Color.RED); //lorsque le chronometre arrive a la duree voulu il s'affiche en rouge
		if(secondsChrono%60<10) {
			return "0" + (int)(secondsChrono/60) 
					+ ":0" + 
					secondsChrono%60;
		}else {
			return "0" + (int)(secondsChrono/60) 
					+ ":" + 
					secondsChrono%60;
		}
		//affiche le chrono en mm:ss
	}
}
