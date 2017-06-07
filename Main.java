package application;

import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

	/**
	 * 
	 * Przeci��ona metoda 'start' testuje klas� 'LogonDialog'.
	 * Wywo�uje ona jednokrotnie okno logowania za pomoc� metody 'show' z klasy 'LogonDialog' i je�li u�ytkownik zalogowa� si� poprawnie wypisuje uzyskan� par� warto�ci.
	 * Je�li u�ytkownik zamknie okienko nie loguj�c si� na konsoli pojawi si� odpowiedni komentarz.
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
