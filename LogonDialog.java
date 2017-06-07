package application;

import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * 
 * @author Dominik Orli�ski
 * @version 1.0
 * 
 */

public class LogonDialog {
	boolean password;
	boolean user;
	boolean enviroment;
	boolean correctPassword;
	Optional<Pair<Environment, String>> returnPair;
	ChoiceBox<Object> choiceBox = new ChoiceBox<Object>();
	ComboBox<String> comboBox = new ComboBox<String>();
	PasswordField passwordField = new PasswordField();
	ButtonType loginButton = new ButtonType("Logon", ButtonData.OK_DONE);
	ButtonType cancelButton = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
	Dialog<Pair<Environment, String>> primaryStage = new Dialog<Pair<Environment, String>>();
	GridPane gridPane = new GridPane();
	ArrayList<Pair<String, String>> pairList = new ArrayList<Pair<String, String>>();
	ObservableList<String> developUserList = FXCollections.observableArrayList("Jagoda.pierworodna@wp.pl",
			"J�zef.spadkobierca@gmail.com");
	ObservableList<String> testUserList = FXCollections.observableArrayList("Jagoda.pierworodna@wp.pl",
			"J�zef.spadkobierca@gmail.com", "Julia.delegacja@onet.pl", "Tadeusz.niejadek@wp.pl",
			"Franciszek.mlody@gmai.com", "�ucja.kopia@o2.pl");
	ObservableList<String> productionUserList = FXCollections.observableArrayList("Julia.delegacja@onet.pl",
			"�ucja.kopia@o2.pl");

	/**
	 * 
	 * Okno logowania jest oparte o zarz�dce 'gridPane'. 
	 * Przyciski i listy wyboru opar�em na klasach z wymaga� projektowych.
	 * Przekonwertowa�em warto�� zwracan� przez okno logowania.
	 * Mo�liwo�� minimalizacji okna oraz zmiany jego rozmiaru zosta�a zablokowana.
	 * Wi�ciej komentarzy przy metodzie 'show'.
	 * 
	 */
	
	public LogonDialog() {
		try {
			makePasswordList();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			gridPane.setPadding(new Insets(10, 10, 10, 10));
			Text textEnvironment = new Text(10, 50, "�rodowisko : ");
			Text textUser = new Text(10, 50, "U�ytkownik : ");
			choiceBox.setItems(FXCollections.observableArrayList("Produkcyjne", "Testowe", "Deweloperskie"));
			comboBox.setItems(FXCollections.observableArrayList());
			comboBox.setPromptText("Tw�j adres email");
			comboBox.setEditable(true);
			comboBox.setPrefSize(200, choiceBox.getHeight());
			passwordField.setPromptText("Twoje has�o");
			
			GridPane buttonGridPane = new GridPane();
			buttonGridPane.setHgap(10);
			buttonGridPane.setVgap(10);
			buttonGridPane.setAlignment(Pos.CENTER_RIGHT);
			Text textPassword = new Text(10, 50, "Has�o : ");
			gridPane.add(textEnvironment, 0, 0);
			gridPane.add(choiceBox, 1, 0, 2, 1);
			gridPane.add(textUser, 0, 1);
			gridPane.add(comboBox, 1, 1, 2, 1);
			gridPane.add(textPassword, 0, 2);
			gridPane.add(passwordField, 1, 2, 2, 1);

			primaryStage.initStyle(StageStyle.UTILITY);
			primaryStage.setResizable(false);
			primaryStage.setTitle(" Logowanie");
			
			comboBox.valueProperty().addListener((observable, oldVal, newVal) -> comboBox_Changed(newVal));
			choiceBox.valueProperty().addListener((observable, oldVal, newVal) -> choiceBox_Changed(newVal));
			passwordField.textProperty().addListener((observable, oldVal, newVal) -> passwordField_Changed(newVal));
			
			primaryStage.getDialogPane().setContent(gridPane);
			primaryStage.getDialogPane().getButtonTypes().addAll(loginButton, cancelButton);
			Node login = primaryStage.getDialogPane().lookupButton(loginButton);
			login.setDisable(true);
			correctPassword = true;
			primaryStage.setResultConverter(dialogButton -> {
				if (dialogButton == loginButton) {
					if (checkPassword() == true) {
						return new Pair<Environment, String>(Environment.getResult(choiceBox.getValue().toString()),
								comboBox.getValue().toString());
					} else {
						correctPassword = false;
						return null;
					}
				}
				return null;
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Publiczna metoda 'show' pozwala na wy�wietlenie okna logowania. 
	   Okno mo�na zamkn�� poprzez : 
	   		1) klikni�cie przysisku 'x' w prawym g�rnym rogu
			2) klikni�cie klawisza ESC na klawiaturze 
			3) klikni�cie przysisku 'Anuluj' 
	 		4) poprawne wpisanie has�a i klikni�cie przycisku 'Logon'
	 * B��dnie wpisane has�o zostanie zasygnalizowane po klikni�ciu przysisku 'Logon' poprzez znaznaczenie czerwon� obwolut� pola 'passwordField' 
	 * W przypadku 1,2,3 metoda 'show' nie zwraca nic (w znaczeniu'returnPair.isPresent() == false'), oraz zwraca par� warto�ci <Enviroment, String> w przypadku 4
	 * Mo�liwo�� minimalizacji okna oraz zmiany jego rozmiaru zosta�a zablokowana
	 * 
	 */
	
	public Optional<Pair<Environment, String>> show() {
		returnPair = primaryStage.showAndWait();
		while (correctPassword == false) {
			correctPassword = true;
			passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			returnPair = Optional.empty();
			returnPair = primaryStage.showAndWait();
		}
		return returnPair;
	}

	/**
	 * 
	 * Metoda 'checkPassword' s�u�y do sprawdzenia poprawno�ci has�a.
	 * Wyszukuje ona na li�cie 'pairList' podany login i por�wnuje z przypisanym do niego has�em.
	 *  
	 */

	private boolean checkPassword() {
		for (int i = 0; i<7 ; i++) {
			if (pairList.get(i).getKey().equals(comboBox.getValue().toString())) {
				if (passwordField.getText().equals(pairList.get(i).getValue())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * 
	 * Kolejne trzy metody 'passwordField_Changed', 'choiceBox_Changed' i 'comboBox_Changed' odpowiadaj� za rekcj� na cz�� poczyna� u�ytkownika.
	 * Odblokowuj� przysik logon je�li u�ytkownik wybra� �rodowisko, wybra� / wprowadzi� login (minimum 1 znak) oraz wprowadzi� has�o (minimum 1 znak).
	 * Do wyboru s� trzy �rodowiska, a ka�de �rodowisko ma przypisan� do siebie list� login�w.
	 * Has�o przypisane jest do danego loginu, a nie do pary login i �rodowisko.
	 * Lista login�w jest dost�pna dopiero po wybraniu �rodowiska, a w przypadku zmiany wyboru �rodowiska login trzeba wybra� ponownie. 
	 * 
	 */
	
	private Object passwordField_Changed(String newVal) {
		Node login = primaryStage.getDialogPane().lookupButton(loginButton);
		login.setDisable(true);
		password = false;
		if (passwordField.getText().length() > 0) {
			password = true;
		}
		if (user == true && password == true && enviroment == true) {
			login.setDisable(false);
		}
		return null;
	}

	private Object choiceBox_Changed(Object newVal) {
		user = false;
		Node login = primaryStage.getDialogPane().lookupButton(loginButton);
		login.setDisable(true);
		comboBox.setItems(FXCollections.observableArrayList());
		if (newVal == "Deweloperskie") {
			comboBox.setItems(developUserList);
		}
		if (newVal == "Testowe") {
			comboBox.setItems(testUserList);
		}
		if (newVal == "Produkcyjne") {
			comboBox.setItems(productionUserList);
		}
		enviroment = true;
		if (user == true && password == true && enviroment == true) {
			login.setDisable(false);
		}
		return null;
	}

	private Object comboBox_Changed(String newVal) {
		Node login = primaryStage.getDialogPane().lookupButton(loginButton);
		login.setDisable(true);
		user = false;
		if (comboBox.getValue() != null) {
			if (comboBox.getValue().length() > 0) {
				user = true;
			}
		}
		if (user == true && password == true && enviroment == true) {
			login.setDisable(false);
		}
		return null;
	}

	/**
	 * 
	 * Metod� 'makePasswordList' utworzy�em na potrzeb� test�w, �eby wype�ni� list� 'pairList' przyk�adowymi loginami i has�ami. 
	 * 
	 */

	private void makePasswordList() {
		pairList.add(new Pair<String, String>("Jagoda.pierworodna@wp.pl", "admin1"));
		pairList.add(new Pair<String, String>("J�zef.spadkobierca@gmail.com", "admin2"));
		pairList.add(new Pair<String, String>("Julia.delegacja@onet.pl", "julka2308"));
		pairList.add(new Pair<String, String>("Tadeusz.niejadek@wp.pl", "admin4"));
		pairList.add(new Pair<String, String>("Franciszek.mlody@gmai.com", "admin5"));
		pairList.add(new Pair<String, String>("�ucja.kopia@o2.pl", "abc"));
		pairList.add(new Pair<String, String>("admin", "1234"));
		
	}

}
