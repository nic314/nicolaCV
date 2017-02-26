package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import model.Service;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	/**
	 * Initialize the home screen of the application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Service.stage=primaryStage;
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("home.fxml"));
			Scene scene = new Scene(root,300,250);
			scene.getStylesheets().add(getClass().getResource("home.css").toExternalForm());
			Service.stage.setScene(scene);
			Service.stage.setResizable(false);
			Service.stage.setTitle("Draughts");
			Service.stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Enter point of the application 
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
	

