package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import model.Message;
import model.MessageType;
import model.Service;

/**
 * This class is the controller of wait UI
 * @author Nicola Gemo
 *
 */
public class WaitController implements Initializable {
	/**
	 * Initialize the UI and the Consumer of the Network object
	 * and wait for the receive the connect state from the other application
	 * in case of error cancelConnection() is called
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//setting the consumer
		Service.network.setConsumer(data -> {
			Platform.runLater(()->{
				
				//Get the message
				Message msg=(Message) data;
				
				//Checking if the first message is "connected"
				if(msg.getType()==MessageType.CONNECTED){
					
					//loading the board UI
					try {
						Service.loadBoard(this);
					} catch (IOException e) {e.printStackTrace();}
				}else{
					
					//The Connection is not working
					try {
						Service.message("Connection error");
						//closing the connection
						cancelConnection();
					} catch (IOException e) {e.printStackTrace();}
				}
			});
		});
		
		//opening the connection
		Service.network.openConnection();
	}
	
	/**
	 * Close the connection, reset the consumer and return to the home screen
	 * @throws IOException
	 */
	public void cancelConnection() throws IOException{
		//reset the consumer
		Service.network.resetConsumer();
		
		//close the connection
		Service.network.closeConnection();
		
		//load the home UI
		Service.loadHome(this);
	}
}
