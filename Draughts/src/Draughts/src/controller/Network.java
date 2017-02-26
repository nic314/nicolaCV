package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.application.Platform;
import model.Message;
import model.MessageType;
import model.Service;

/**
 * This class provides method for create use and close the network
 * @author Nicola Gemo
 *
 */
public class Network{
	
	/**
	 * The consumer used for consume the incoming messages
	 */
	private Consumer<Serializable> consumer;
	
	/**
	 * The IP to use
	 */
	String ip="";
	
	/**
	 * The Port to use
	 */
	int port;
	
	/**
	 * The object that provide the connection
	 */
	private Connection connection;
	
	/**
	 * Constructor for the client
	 * @param ip
	 * @param port
	 */
	public Network(String ip, int port){
		this.port=port;
		this.ip=ip;
	}
	
	/**
	 * Constructor for the host
	 * @param port
	 */
	public Network(int port){
		this.ip="";
		this.port=port;
	}
	
	/**
	 * Set the consumer
	 * @param consumer
	 */
	public void setConsumer(Consumer<Serializable> consumer){
		this.consumer=consumer;
	}
	
	/**
	 * Reset the consumer
	 */
	public void resetConsumer(){
		//Reset the consumer
		Service.network.setConsumer(data -> {
			Platform.runLater(()->{
				
			});
		});
		
	}
	
	/**
	 * Starts the connection thread
	 */
	public void openConnection(){
		
		//Initialize the object for the connection
		connection=new Connection();
		
		//The thread will close if the application is closed
		connection.setDaemon(true);
		
		//Open the connection
		connection.start();
		
	}
	
	/**
	 * Send data using the connection
	 * @param data The Data to send
	 * @return Return -1 in case of error,1 otherwise
	 */
	public int send(Serializable data){
		
		try {
			//sending the Object in the network
			connection.out.writeObject(data);
			return 1;
		} catch (IOException e) {
			//Operation failed
			return -1;
		}
		
	}
	
	/**
	 * Close the connection
	 * @return Return -1 in case of error,1 otherwise
	 */
	public int closeConnection(){
		
		//close the connection
		try {
			connection.socket.close();
			
			return 1;
		} catch (Exception e) {
			try {
				connection.server.close();
			} catch (Exception e1) {
				return -1;
			}
			return -1;
		}
		
	}
	
	/**
	 * 
	 * @author Nicola Gemo
	 *
	 */
	public class Connection extends Thread {
		
		Socket socket;
		ServerSocket server;
		private ObjectOutputStream out;
		
		/**
		 * The Entry point of the thread, 
		 * the connection is opened and the incoming data 
		 * sent to the consumer
		 */
		@Override
		public void run() {
			try {
				if(ip.equals("")){
					//Server
					server=new ServerSocket(port);
					socket=server.accept();
				}else{
					//Client
					socket=new Socket(ip, port);
				}
				
				//Setting the output
				out=new ObjectOutputStream(socket.getOutputStream());
				//Setting up the input
				ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
				//No delay
				socket.setTcpNoDelay(true);
				
				//Send the successful string
				send(new Message(MessageType.CONNECTED,""));
				
				//Waiting for messages
				while(true){
					Serializable data=(Serializable) in.readObject();
					consumer.accept(data);
				}
				
			} catch (Exception e) {
				
				//Error message sent to the consumer
				Serializable data=new Message(MessageType.ERROR,e.toString());
				consumer.accept(data);
				
			}
			
		}
	
	}
}
