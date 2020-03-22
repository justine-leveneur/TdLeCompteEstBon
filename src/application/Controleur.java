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

	private SimpleDateFormat formatDateNow = new SimpleDateFormat("HH:mm:ss");
	private Model model;
	private Button[] plaque = new Button[6]; 
	private Text[] all10Scores = new Text[10]; 
	private int indice1, indice2;
	private String operateur;
	private int secondsChrono;
	private Timer timerChrono;


	@FXML
	private void commencer(ActionEvent commencer) {
		ongletScores.setVisible(false);
		ongletJeu.setVisible(true);
		if(pseudo.getText() == null || pseudo.getText().length()<3 || pseudo.getText().length()>8) warningPseudo.setVisible(true);
		else {
			preparer();
		}
		model.jouer();
	}
	

	@FXML
	private void choixOperation (ActionEvent choixOperation) {
		Button boutonOperation = (Button) choixOperation.getSource();
		operateur = boutonOperation.getText();
		operation.setText(operation.getText() + boutonOperation.getText());
		changeButtonOprationDisabilitie(true);
		for (Button valeur: plaque) valeur.setDisable(false);
		plaque[indice1].setDisable(true);
	}

	private void changeButtonOprationDisabilitie(boolean state) {
		ajouter.setDisable(state);
		soustraire.setDisable(state);
		multiplier.setDisable(state);
		diviser.setDisable(state);
	}

	@FXML
	private void valider(ActionEvent valider) {
		content.setText(content.getText() + "\n" + model.calculer(indice1, indice2, operateur));
		operation.setText("");
		changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);
		for (Button oldValues: plaque) {
			tabPlaques.getChildren().remove(oldValues);
		}
		creationBoutonsPlaques(model.creerNouvellesPlaques(indice1, indice2));
		indice1 = -1;
		indice2 = -1;
	}

	@FXML
	private void annuler(ActionEvent annuler) {
		changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);
		operation.setText("");
		indice1 = -1;
		indice2 = -1;
	}
	
	@FXML
	private void supprimer(ActionEvent supprimer) {
		if(!content.getText().isEmpty()) {
			changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);
		for (Button oldValues: plaque) {
			tabPlaques.getChildren().remove(oldValues);
		}
		content.setText(content.getText().substring(0, content.getText().lastIndexOf("\n")));
		operation.setText("");
		creationBoutonsPlaques(model.supprimerEtape());
		indice1 = -1;
		indice2 = -1;
		}	
	}
	
	@FXML
	private void proposerValeur(ActionEvent proposerValeur) {
		timerChrono.cancel();
		model.score(secondsChrono);
				 
		JOptionPane.showMessageDialog(null, "ton score est de " 
		+ model.getScore(), "Bravo " 
		+ model.getPseudo() ,JOptionPane.INFORMATION_MESSAGE); 

		jouer.setDisable(false);
		scores.setDisable(false);

		for (Button oldValues: plaque) {
			tabPlaques.getChildren().remove(oldValues);
		}
		ajouter.setDisable(true);
		soustraire.setDisable(true);
		multiplier.setDisable(true);
		diviser.setDisable(true);

		annuler.setDisable(true);
		valider.setDisable(true);
		proposer.setDisable(true);
		supprimer.setDisable(true);
		
		content.setText(null);
		model.setDureeMax(180);
	}
	
	@FXML
	private void scores(ActionEvent scores) {
		ouvrirFichierHtml();
		
		ongletScores.setVisible(true);
		ongletJeu.setVisible(false);
		String[] tabScores = model.getScores();
		if(tabScores[0] != null) score1.setText(tabScores[0].replace("seconds", "").replace(" point(s)", "").replace(" by ", "\n").replace(" in ", ", "));
		if(tabScores[1] != null) score2.setText(tabScores[1].replace("seconds", "").replace("point(s)", "").replace(" by ", "\n").replace(" in ", ", "));
		if(tabScores[2] != null) score3.setText(tabScores[2].replace("seconds", "").replace("point(s)", "").replace(" by ", "\n").replace(" in ", ", "));
		for(int i = 3; i < tabScores.length; i++) {
			if(tabScores[i] != null) 
			{
				all10Scores[i] = new Text(tabScores[i]);
				all10Scores[i].toFront();
				tabScoreAfterTop3.add(all10Scores[i], 1, i-3);
			}
			else tabScoreAfterTop3.add(new Separator(), 1, i-3);
		}
	}


	/**
	 * genere plus affiche les fichier scores.html
	 */
	private void ouvrirFichierHtml() {
		model.afficherScores();
		
		File scoresHtml = new File("score/scores.html");
		  try {
			Desktop.getDesktop().browse(scoresHtml.toURI());
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	/**
	 * prepare le jeu c'est a dire adapte la visibilite des boutons, demarre le chrono et affiche les plaques ainsi que le chiffre a trouver
	 */
	private void preparer() {
		int[] plaques = new int[6];
		if(warningPseudo.isVisible()) warningPseudo.setVisible(false);
		pseudo.setDisable(true);
		preparerBoutons();
		plaques = model.preparer(pseudo.getText());
		creationBoutonsPlaques(plaques);		
		nbATrouver.setText(model.getNbATrouver());
		secondsChrono = model.getDureeMax();
		defilerChrono();
	}

	private void creationBoutonsPlaques(int[] plaques) {
		for(int i = 0; i < plaques.length; i++) {
			plaque[i] = new Button("" + plaques[i]);
			plaque[i].setId(""+i);
			plaque[i].setPrefSize(60, 40);
			plaque[i].setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent choixValeur) {
					Button boutonValeur = (Button) choixValeur.getSource();
					addIndices(boutonValeur);
				}

				private void addIndices(Button boutonValeur) {
					if(indice1 == -1) 
					{
						indice1 = Integer.parseInt(boutonValeur.getId());
						operation.setText(operation.getText() + boutonValeur.getText());
						for (Button valeur: plaque) valeur.setDisable(true);
						changeButtonOprationDisabilitie(false);
					}
					else if(indice2 == -1 && indice1 != -1) 
					{
						indice2 = Integer.parseInt(boutonValeur.getId());
						operation.setText(operation.getText() + boutonValeur.getText());
						for (Button valeur: plaque) valeur.setDisable(true);
						changeButtonOprationDisabilitie(true);
					}
				}
			});
			tabPlaques.add(plaque[i], i, 0);
		}
	}

	private void preparerBoutons() {
		jouer.setDisable(true);
		scores.setDisable(true);

		ajouter.setDisable(false);
		soustraire.setDisable(false);
		multiplier.setDisable(false);
		diviser.setDisable(false);

		annuler.setDisable(false);
		valider.setDisable(false);
		proposer.setDisable(false);
		supprimer.setDisable(false);
	}

	@FXML
	private void initialize(){
		ongletScores.setVisible(false);
		ongletJeu.setVisible(true);
		indice1 = -1;
		indice2 = -1;
		model = Model.getInstance();
		chrono.setText("03:00");
		nbATrouver.setText(model.getNbATrouver());
		dateNow.setText(formatDateNow.format(new Date()));
		defilerTemps();
		model.attendre();		
	}

	private void defilerTemps() {
		Calendar calendarToDateNow = Calendar.getInstance();
		calendarToDateNow.setTime(new Date());
		Timer timer = new Timer();

		TimerTask defilerTime = new TimerTask() {
			public void run() {
				calendarToDateNow.add(Calendar.SECOND, 1);
				dateNow.setText(formatDateNow.format(calendarToDateNow.getTime()));
			}
		};
		timer.schedule(defilerTime, 1000, 1000);
	}

	private void defilerChrono() {
		timerChrono = new Timer();
		
		TimerTask chronometre = new TimerTask() {
			String chronoFormat = null;

			public void run() {
				secondsChrono -= 1;
				chronoFormate();
				chrono.setText(chronoFormat);
			}

			private void chronoFormate() {
				if(secondsChrono%60<10) {
					chronoFormat = "0" + (int)(secondsChrono/60) 
							+ ":0" + 
							secondsChrono%60;
				}else {
					chronoFormat = "0" + (int)(secondsChrono/60) 
							+ ":" + 
							secondsChrono%60;
				}
				if(secondsChrono<61)chrono.setFill(Color.RED);
			}
		};
		timerChrono.schedule(chronometre, 1000, 1000);
	}
}
