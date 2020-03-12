package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class Controller {
	
	@FXML
	private Button jouer;
	
	@FXML
	private AnchorPane demarrerPartie;
	
	@FXML
	private void Jouer(ActionEvent jouer) {
		demarrerPartie.setVisible(true);
	}
	
}
