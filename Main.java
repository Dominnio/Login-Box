package application;

import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

	/**
	 * 
	 * Przeci¹¿ona metoda 'start' testuje klasê 'LogonDialog'.
	 * Wywo³uje ona jednokrotnie okno logowania za pomoc¹ metody 'show' z klasy 'LogonDialog' i jeœli u¿ytkownik zalogowa³ siê poprawnie wypisuje uzyskan¹ parê wartoœci.
	 * Jeœli u¿ytkownik zamknie okienko nie loguj¹c siê na konsoli pojawi siê odpowiedni komentarz.
	 * 
	 */
	
	@Override
	public void start(Stage primaryStage) {
		LogonDialog logonBox = new LogonDialog();
		Optional<Pair<Environment, String>> result = logonBox.show();
		if (result.isPresent()) {
			System.out.println(result.get().getKey().toString() + "\n" + result.get().getValue());
		} else {
			System.out.println("Anulowano logowanie");

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
