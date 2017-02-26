package controller;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
 * This class is the controller for the replay of the game, it provides methods 
 * for read the log
 * and update the UI
 * @author Nicola Gemo
 *
 */
public class ReplayBoardController implements Initializable {

	@FXML
	private TextArea chatField;
	@FXML
	private GridPane gameGrid;
	
	/**
	 * The board object
	 */
	private Board gameBoard;
	
	/**
	 * The log object
	 */
	private Log log;
	
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
		//Initialization of the log
		log=new Log();
		try (ObjectInputStream reader = new ObjectInputStream (
				new FileInputStream ("log.dat" ))) {
				log=(Log)reader.readObject();
		} catch ( IOException | ClassNotFoundException ex) {
			Service.messageWait("Log not found");
		}
				
		
		//Loading the texture
		b = new Image(getClass().getResourceAsStream("/b.png"));
		w = new Image(getClass().getResourceAsStream("/w.png"));
		
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
		
		
		//Creating the new board
		gameBoard=new Board();
		
		//Updating the interface
		updateView(gameBoard.getBoard());
		
	}
	

	
	
	/**
	 * Update the UI
	 * @param state The two dimension array that rapresent the board
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
	 * Method executed when next button is pressed, it
	 * shows the next move on the log
	 * @param event 
	 * @throws IOException 
	 */
	public void next(ActionEvent event) throws IOException{
		
		//Temp message
		Message msg=new Message(MessageType.MSG, "End of replay");
		//Get the message
		try{
			msg=log.pop();
		}catch(Exception e){
			Service.messageWait("End of the replay");
			Service.loadHome(this);
			
		}
		
		//Check the message type
		if(msg.getType()==MessageType.MOVE){
			
			//Sending the move to the board
			int x1=((int) msg.getMessage().charAt(0))-48;
			int y1=((int) msg.getMessage().charAt(1))-48;
			int x2=((int) msg.getMessage().charAt(2))-48;
			int y2=((int) msg.getMessage().charAt(3))-48;
			
			//Checking if there is a winner
			State win =gameBoard.move(x1, y1, x2, y2);
			//Updating the view
			updateView(gameBoard.getBoard());
			//If win = E there is no winner
			if(win!=State.E)
				Win(win);
			
		}else if (msg.getType()==MessageType.MSG){//chat
			
			//Updating the chat
			chatField.setText(chatField.getText()+msg.getMessage()+"\n");
			
		}else{//Error					
			
			//Closing the game and the connection
			Service.message("Connection lost");
			
			//Loading the home UI
			try {
				Service.loadHome(this);
			} catch (IOException e) {e.printStackTrace();}
		}
		
	}

	
	
	/**
	 * The win message is display and the game close
	 * @param w The winner
	 */
	public void Win(State w){
		
		//Checking who wins
		if(w==gameBoard.getMyColor()){
			//I win
			Service.messageWait("Game Over, you win the match!");
			try {
				Service.loadHome(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			//Other payer wins
			Service.messageWait("Game Over, the other player wins the match!");
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