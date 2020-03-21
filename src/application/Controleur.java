package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Model;

public class Controleur{

	@FXML
	private Button jouer, scores;

	@FXML
	private Button ajouter, soustraire, multiplier, diviser;

	@FXML
	private GridPane tabPlaques;

	@FXML
	private Button annuler, valider, proposer, supprimer;

	@FXML
	private Text dateNow, chrono, warningPseudo, nbATrouver;

	@FXML
	private TextField pseudo;

	@FXML
	private Label operation;

	@FXML
	private TextArea content;

	private SimpleDateFormat formatDateNow = new SimpleDateFormat("HH:mm:ss");
	private Model model;
	private Button[] plaque = new Button[6]; 
	private int indice1, indice2;
	private String operateur;

	@FXML
	private void commencer(ActionEvent commencer) {
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
		changeButtonOprationDisabilitie(false);
		for (Button valeur: plaque) valeur.setDisable(false);		
	}

	@FXML
	private void annuler(ActionEvent annuler) {
		content.setText(content.getText() + "\n" + model.calculer(indice1, indice2, operateur));
	}

	private void preparer() {
		int[] plaques = new int[6];
		if(warningPseudo.isVisible()) warningPseudo.setVisible(false);
		pseudo.setDisable(true);
		preparerBoutons();
		plaques = model.preparer(pseudo.getText());
		creationBoutonsPlaques(plaques);		
		nbATrouver.setText(model.getNbATrouver());
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
		Timer timer = new Timer();

		TimerTask chronometre = new TimerTask() {
			int secondsChrono = 180;
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
		timer.schedule(chronometre, 1000, 1000);
	}
}
