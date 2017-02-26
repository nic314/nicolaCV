package controller;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Board;
import model.Log;
import model.Message;
import model.MessageType;
import model.Service;
import model.State;

/**
 * This class is the controller for the game, it provides methods for perform the moves
 * and update the UI
 * @author Nicola Gemo
 *
 */
public class BoardController implements Initializable {

	@FXML
	private TextArea chatField;
	@FXML
	private TextField messField;
	@FXML
	private Button messSend;
	@FXML
	private GridPane gameGrid;
	@FXML
	private Label turnLabel;
	
	/**
	 * The board object
	 */
	private Board gameBoard;
	
	/**
	 * The log object
	 */
	private Log log;
	
	/**
	 * If the application is the host myTurn will be true,the host starts the game first
	 */
	private Boolean myTurn=Service.isServer;
	
	/**
	 * Used for keep in memory the last button pressed and when 
	 * a new buttons is pressed, this variable will be used for
	 * check if the move is valid or not
	 */
	private int oldX=-1;
	
	/**
	 * Used for keep in memory the last button pressed and when 
	 * a new buttons is pressed, this variable will be used for
	 * check if the move is valid or not
	 */
	private int oldY=-1;
	
	/**
	 * Dark token texture
	 */
	Image b;
	
	/**
	 * Light token texture
	 */
	Image w;
	
	/**
	 * Initialize the page and the Consumer of the Network object
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Loading the texture
		b = new Image(getClass().getResourceAsStream("/b.png"));
		w = new Image(getClass().getResourceAsStream("/w.png"));
		
		//Setting the label that show the turn
		if(myTurn)
			turnLabel.setText("Your turn");
		else
			turnLabel.setText("Wait");
		
		//Setting chatField
		chatField.setWrapText(true);
		chatField.setEditable(false);
		
		//Creating the buttons of the 8x8 board with different colors
		for(int i=0;i<8;i++){
			
			int color;
			if(i % 2==0)
				color=0;
			else
				color=1;
			
			for(int j=0;j<8;j++){
				
				//Creating the button with the right propriety
				Button child=new Button();
				child.setUserData(Integer.toString(j)+Integer.toString(i));
				child.setOnAction(this::press);
				if(color==0){
					color=1;
					child.setStyle("-fx-base:#e04827;");
				}else{
					color=0;
					child.setStyle("-fx-base:#DAF7A6;");
				}
		
				//Adding the button to the grid
				gameGrid.add(child, j, i);
			}
		}
		
		//Initialization of the log
		log=new Log();
		
		//Creating the new board
		gameBoard=new Board();
		
		//Updating the interface
		updateView(gameBoard.getBoard());
		
		//Setting up the new consumer
		Service.network.setConsumer(data -> {
			Platform.runLater(()->{
				
				//Get the message
				Message msg=(Message) data;
				
				//Check the message type
				if(msg.getType()==MessageType.MOVE){
					
					//Sending the move to the board
					int x1=((int) msg.getMessage().charAt(0))-48;
					int y1=((int) msg.getMessage().charAt(1))-48;
					int x2=((int) msg.getMessage().charAt(2))-48;
					int y2=((int) msg.getMessage().charAt(3))-48;
					x1=7-x1;
					y1=7-y1;
					x2=7-x2;
					y2=7-y2;
					//Checking if there is a winner
					State win =gameBoard.move(x1, y1, x2, y2);
					
					//Add message to log
					log.addEvent(new Message(MessageType.MOVE, ""+x1+y1+x2+y2));
					//Update the log file
					try (ObjectOutputStream writer=new ObjectOutputStream(new FileOutputStream ("log.dat"))) {
						writer . writeObject(log);
					} catch ( IOException ex) {
						Service.message("Error on the update of log file");
					}
					
					//updating the view
					updateView(gameBoard.getBoard());
					//If win = E there is no winner
					if(win==State.E){
						myTurn=true;
						turnLabel.setText("Your turn");
					}else
						//There is a winner
						Win(win);
					
				}else if (msg.getType()==MessageType.MSG){//chat
					
					//Updating the chat
					chatField.setText(chatField.getText()+msg.getMessage()+"\n");
					
					//Add message to log
					log.addEvent(msg);
					//Update the log file
					try (ObjectOutputStream writer=new ObjectOutputStream(new FileOutputStream ("log.dat"))) {
						writer . writeObject(log);
					} catch ( IOException ex) {
						Service.message("Error on the update of log file");
					}
					
				}else{//Error					
					
					//Closing the game and the connection
					Service.message("Connection lost, press replay for the match again");
					Service.network.closeConnection();
					
					//Loading the home UI
					try {
						Service.loadHome(this);
					} catch (IOException e) {e.printStackTrace();}
				}
			});
		});
		
	}
	
	/**
	 * Send a message a chat message throw the network
	 */
	public void sendMess(){
		
		//Check if there is a message
		if(messField.getText().equals("")) 
			return;
		
		//prepare the message
		Message msg=new Message(MessageType.MSG, Service.playerName+": "+messField.getText());
		
		//Send the message
		Service.network.send(msg);
		
		//Add message to log
		log.addEvent(msg);
		
		//Update the log file
		try (ObjectOutputStream writer=new ObjectOutputStream(new FileOutputStream ("log.dat"))) {
			writer . writeObject(log);
		} catch ( IOException ex) {
			Service.message("Error on the update of log file");
		}
		
		//Update the fields
		chatField.setText(chatField.getText()+Service.playerName+": "+messField.getText()+"\n");
		messField.setText("");
		
	}
	
	/**
	 * Update the UI
	 * @param state The two dimension array that rappresent the board
	 */
	public void updateView(State[][] state){
		
		//Update the UI with 'state'
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				
				if(state[j][i]==State.B){
					//Dark token
					ImageView imageView = new ImageView(b);
					imageView.setFitWidth(65);
					imageView.setFitHeight(65);
					getNode(j,i).setGraphic(imageView);
					
				}else if (state[j][i]==State.W){
					//White roken
					ImageView imageView = new ImageView(w);
					imageView.setFitWidth(65);
					imageView.setFitHeight(65);
					getNode(j,i).setGraphic(imageView);
					
				}else if (state[j][i]==State.E){
					//No token
					getNode(j,i).setGraphic(new ImageView());
				}
			}
		
	}
	
	/**
	 * Return the button at the given coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return Return the button at the given coordinate
	 */
	private Button getNode(int x, int y) {

		//return the button at coordinate (x , y)
	    for (Node node : gameGrid.getChildren()) {
	        if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
	            return (Button) node;
	        }
	    }
	    //No button at the given coordinates
	    return null;

	}
	
	/**
	 * Method executed when a button is pressed, it checks is 
	 * the move is valid and in that case perform and send it
	 * @param event 
	 */
	public void press(ActionEvent event) {
		
		//Check if is my turn
		if(!myTurn){
			//Not my turn, return
			Service.message("Is the turn of the other player");
			return;
		}
		
		//Getting the button
		Button b=(Button) (event.getSource());	

		//Getting the button property and coordinates
		String tmp=(String) (b.getUserData());
		int x=((int) tmp.charAt(0))-48;
		int y=((int) tmp.charAt(1))-48;
		
		//Used for memorize the winner
		State win=State.E;
		
		//Checking if the move is valid
		if(gameBoard.isValidMove(oldX, oldY, x, y)){
			//valid move
			win=gameBoard.move(oldX, oldY, x, y);
			sendMove(oldX, oldY, x, y);
			myTurn=false;
			turnLabel.setText("Wait");
			//Updating the old coordinates
			oldX=-1;
			oldY=-1;
			
		}else{
			//Move not valid
			//Updating the old coordinates
			oldX=x;
			oldY=y;
		}
		
		//Update the interface with the updated board
		updateView(gameBoard.getBoard());
		
		//Check if there is winner
		if(win!=State.E)
			Win(win);
		
	}

	/**
	 * Send a move throw the network
	 * @param x1 the x coordinate of the starting point
	 * @param y1 the y coordinate of the starting point
	 * @param x2 the x coordinate of the final point
	 * @param y2 the y coordinate of the final point
	 */
	public void sendMove(int x1,int y1,int x2, int y2){
		//prepare the message
		Message msg=new Message(MessageType.MOVE, ""+x1+y1+x2+y2);
				
		//Send the message
		Service.network.send(msg);
				
		//Add message to log
		log.addEvent(msg);
		
		//Update the log file
		try (ObjectOutputStream writer=new ObjectOutputStream(new FileOutputStream ("log.dat"))) {
			writer . writeObject(log);
		} catch ( IOException ex) {
			Service.message("Error on the update of log file");
		}
	}
	
	/**
	 * The win message is display and the game close
	 * @param w The winner
	 */
	public void Win(State w){
		
		//Game Over, reset the consumer
		Service.network.resetConsumer();
		
		//Checking who wins
		if(w==gameBoard.getMyColor()){
			//I win
			Service.messageWait("Game Over, you win the match!, press replay for the match again");
			
			try {
				Service.loadHome(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			//Other payer wins
			Service.messageWait("Game Over, the other player wins the match!, press replay for the match again");
			
			try {
				Service.loadHome(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void rules(ActionEvent event){
		String msg="They token can be moved in oblique direction,forward and backward. \n\n"
				+ "The token can eat only one enemy token at time \n\n"
				+ "The player that lose all the tokens lose the match \n";
		Service.message(msg);
		
	}
	public void info(ActionEvent event){
		String msg="This application is a project for the 2016 subject Application development "
				+ "of the Elte university of Budapest \n\n"
				+ "Author: Nicola Gemo \n";
		Service.message(msg);
		
	}
	
	
}
