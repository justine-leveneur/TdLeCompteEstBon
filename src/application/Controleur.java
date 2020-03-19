package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Controleur{

	@FXML
	private Button jouer;

	@FXML
	private AnchorPane demarrerPartie;

	@FXML
	private Text dateNow;

	private SimpleDateFormat formatDateNow = new SimpleDateFormat("HH:mm:ss");

	@FXML
	private void Jouer(ActionEvent jouer) {
		demarrerPartie.setVisible(true);
	}

	@FXML
	private void initialize(){
		dateNow.setText(formatDateNow.format(new Date()));
		defiler();
	}

	private void defiler() {
		Timer timer = new Timer();
		Calendar calendarToDateNow = Calendar.getInstance();
	    calendarToDateNow.setTime(new Date());

	    TimerTask defilerTime = new TimerTask() {
			public void run() {
				    calendarToDateNow.add(Calendar.SECOND, 1);
					dateNow.setText(formatDateNow.format(calendarToDateNow.getTime()));
			}
		};
		timer.schedule(defilerTime, 1000, 1000);
	}
}
