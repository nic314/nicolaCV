package model;

import java.io.IOException;

import controller.Network;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This class provide function that can be used from all classes
 * @author Nicola Gemo
 *
 */
public final class Service {
	
	/**
	 * Used for change the UI
	 */
	public static Stage stage;
	
	/**
	 * Used for manage the network
	 */
	public static Network network;
	
	/**
	 * Store the nickname of the player
	 */
	public static String playerName;
	
	/**
	 * Value equal to true is the user choose server, false otherwise
	 */
	public static Boolean isServer;
	
	/**
	 * Load the Home UI
	 * @param obj Used for get the resource
	 * @throws IOException
	 */
	public static void loadHome(Object obj) throws IOException{
		BorderPane waitServer = (BorderPane)FXMLLoader.load(obj.getClass().getResource("/view/home.fxml"));
		Scene scene = new Scene(waitServer,310,260);
		scene.getStylesheets().add(obj.getClass().getResource("/view/home.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Load the Wait UI
	 * @param obj Used for get the resource
	 * @throws IOException
	 */
	public static void loadWait(Object obj) throws IOException{
		BorderPane waitServer = (BorderPane)FXMLLoader.load(obj.getClass().getResource("/view/wait.fxml"));
		Scene scene = new Scene(waitServer,310,190);
		scene.getStylesheets().add(obj.getClass().getResource("/view/wait.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Load the Board UI
	 * @param obj Used for get the resource
	 * @throws IOException
	 */
	public static void loadBoard(Object obj) throws IOException{
		BorderPane waitServer = (BorderPane)FXMLLoader.load(obj.getClass().getResource("/view/board.fxml"));
		Scene scene = new Scene(waitServer,750,520);
		scene.getStylesheets().add(obj.getClass().getResource("/view/board.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Load the ReplayBoard UI
	 * @param obj Used for get the resource
	 * @throws IOException
	 */
	public static void loadReplayBoard(Object obj) throws IOException{
		BorderPane waitServer = (BorderPane)FXMLLoader.load(obj.getClass().getResource("/view/replayboard.fxml"));
		Scene scene = new Scene(waitServer,750,520);
		scene.getStylesheets().add(obj.getClass().getResource("/view/replayboard.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Display a message to the user
	 * @param msg The String printed on screen
	 */
	public static void message(String msg){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.show();
	}
	
	/**
	 * Display a message to the user and wait
	 * @param msg The String printed on screen
	 */
	public static void messageWait(String msg){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}


