package model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;


/**
 * This Class store and control the log.
 * @author Nicola Gemo
 *
 */
public class Log implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Deque<Message> stack;
	
	/**
	 * The constructor
	 */
	public Log(){
		stack = new ArrayDeque<Message>();
	}
	
	/**
	 * Insert a message in the log
	 * @param msg
	 */
	public void addEvent(Message msg){
		stack.add(msg);
	}
	
	/**
	 * Return the next message from the log 
	 * @return The next message from the log 
	 */
	public Message pop(){
		return stack.pop();
	}
		

}
