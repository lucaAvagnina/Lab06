package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import java.time.Month;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;
	
	Model model = new Model();

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		
		txtResult.clear();
		
		//Si poteva anche mettere Month mese
		//Month mese = boxMese.getValue();
		int mese = boxMese.getValue().getValue();
		
		String percorsoOttimo = model.trovaSequenza(mese);
		
		txtResult.setText(percorsoOttimo);
		
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		txtResult.clear();
		
		int mese = boxMese.getValue().getValue();
		
		String umiditaMedia = model.getUmiditaMedia(mese);
		
		txtResult.setText(umiditaMedia);
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		
		//Inserisco nella choiceBox le opzioni di scelta, in questo caso sono i mesi
		for(int i=1; i<13; i++)
			boxMese.getItems().add(Month.of(i));
	}
	
	public void setModel(Model model) {
		
		this.model = model;
	}
	
}
