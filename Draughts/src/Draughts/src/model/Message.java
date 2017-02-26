package model;

import java.io.Serializable;

/**
 * This class Represent the object used for communications
 * @author Nicola Gemo
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Store the type of the message
	 */
	private MessageType type;
	
	/**
	 * Store the message
	 */
	private String message;
	
	/**
	 * The constructor
	 * @param type Represent the type of the message
	 * @param msg The information
	 */
	public Message(MessageType type,String msg){
		this.type=type;
		this.message=msg;
	}
	
	public MessageType getType(){
		return type;
	}
	
	public String getMessage(){
		return message;
	}
	
	
}
