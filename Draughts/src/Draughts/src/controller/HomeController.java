package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Service;
import java.net.*;

/**
 * This class is the controller of the Home UI
 * @author Nicola Gemo
 *
 */
public class HomeController implements Initializable {

	@FXML
	TextField ipField;
	@FXML
	TextField portField;
	@FXML
	TextField nameField;
	@FXML
	BorderPane homePane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	/**
	 * Checks the field and start the connection, loading the wait page
	 * @throws IOException
	 */
	public void connect() throws IOException{
		//Set the information
		Service.isServer=false;
		
		//check name
		if(isNameValid(nameField.getText())){
			Service.playerName=nameField.getText();
		}else{
			Service.message("The name can't be empty!");
			return;
		}
		
		//check port 
		if(!isPortValid(portField.getText())){
			portField.setText("");
			Service.message("The PORT is not valid!");
			return;
		}
		
		//check ip
		if(!isIpValid(ipField.getText())){
			ipField.setText("");
			Service.message("The IP is not valid!");
			return;
		}
		
		//all field valid i can proceed
		Service.network =new Network(ipField.getText(),Integer.parseInt(portField.getText()));
		
		//load the wait UI
		Service.loadWait(this);
	}
	
	/**
	 * Checks the field and start the connection, loading the wait page
	 * @throws IOException
	 */
	public void host() throws IOException {
		//Set the information
		Service.isServer=true;
		
		//check name
		if(isNameValid(nameField.getText())){
			Service.playerName=nameField.getText();
		}else{
			Service.message("The name can't be empty!");
			return;
		}
		
		//check port 
		if(!isPortValid(portField.getText())){
			portField.setText("");
			Service.message("The PORT is not valid!");
			return;
		}
		
		//all field valid i can proceed
		Service.network=new Network(Integer.parseInt(portField.getText()));
		
		//load the wait UI
		Service.loadWait(this);
	}

	/**
	 * This function checks if a string is a valid ip
	 * @param ip The string to check
	 * @return Return value is true if the parameter "ip" is a valid IP
	 */
	private boolean isIpValid(String ip){
		
		try{
			InetAddress.getByName(ipField.getText());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * This function checks if a string is a valid port
	 * @param port The string to check
	 * @return Return value is true if the parameter "port" is a valid port
	*/
	private boolean isPortValid(String port){
		
		int intPort;
		try{
			intPort=Integer.parseInt(port);
			
		}catch(Exception e){
			return false; 
		}
		if(intPort>=1 && intPort<=65535){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * This function checks if a string is a valid Nickname
	 * @param name The string to check
	 * @return Return value is true if the parameter "name" is a valid Nickname
	 */
	private boolean isNameValid(String name){
		if(name.equals("" )){
			return false;
		}
		else{
			return true;
		}
	}
	
	
	/**
	 * It launch the repay mode, reading the file log.dat
	 * @throws IOException
	 */
	public void replay() throws IOException{
		
		try (ObjectInputStream reader = new ObjectInputStream (
				new FileInputStream ("log.dat" ))) {
			Service.loadReplayBoard(this);
		} catch ( IOException ex) {
			Service.messageWait("Log file not found");
		}
		
	}
	
	public void rules(ActionEvent event){
		String msg="They token can be moved in oblique direction,forward and backward. \n\n"
				+ "The token can eat only one enemy token at time \n\n"
				+ "The player that lose all the tokens lose the match \n\n\n"
				+ "This application is a project for the 2016 subject Application development "
				+ "of the Elte university of Budapest \n\n"
				+ "Author: Nicola Gemo \n";
		Service.message(msg);
		
	}

	
}
