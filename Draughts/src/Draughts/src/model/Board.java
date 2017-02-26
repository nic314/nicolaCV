package model;

import java.util.Random;


/**
 * This Class represent the memory of the board, providing method for change it.
 * @author Nicola Gemo
 *
 */
public class Board {
	
	/**
	 * the board is represented throw an array
	 */
	private State[][] board=new State[10][10];
	/**
	 * store the color of the player
	 */
	State myColor;//my color
	/**
	 * store the color of the enemy player
	 */
	State opColor;//enemy color
	
	/**
	 * The constructor, initialize the board and put the token in the right place
	 */
	public Board(){
		
		//initialize an empty board
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++)
				board[i][j]=State.E;
		
		//random color
		Random rand = new Random();
		int  n = rand.nextInt(2);
		if(n==0){
			myColor=State.B;
			opColor=State.W;
		}
		else{
			myColor=State.W;
			opColor=State.B;
		}
			
		//set up starting position
		board[0][0]=opColor;
		board[2][0]=opColor;
		board[4][0]=opColor;
		board[6][0]=opColor;
		board[1][1]=opColor;
		board[3][1]=opColor;
		board[5][1]=opColor;
		board[7][1]=opColor;
		board[0][2]=opColor;
		board[2][2]=opColor;
		board[4][2]=opColor;
		board[6][2]=opColor;
		board[1][7]=myColor;
		board[3][7]=myColor;
		board[5][7]=myColor;
		board[7][7]=myColor;
		board[0][6]=myColor;
		board[2][6]=myColor;
		board[4][6]=myColor;
		board[6][6]=myColor;
		board[1][5]=myColor;
		board[3][5]=myColor;
		board[5][5]=myColor;
		board[7][5]=myColor;
				
	}
	
	/**
	 * return the board array
	 * @return The board array
	 */
	public State[][] getBoard(){
		return board;
	}
	
	/**
	 * Execute the move changing the board, in the end check for winners
	 * @param x1 the x coordinate of the starting point
	 * @param y1 the y coordinate of the starting point
	 * @param x2 the x coordinate of the final point
	 * @param y2 the y coordinate of the final point
	 * @return State.E if nobody wins, or State.B, State.W is there is a winner
	 */
	public State move(int x1, int y1, int x2 , int y2){
		
		//I'm performing the move
		if(board[x1][y1]==myColor){
			
			//Moving in an empty place
			if((x2==x1-1 || x2==x1+1) && (y2==y1-1 || y2==y1+1)){
				board[x2][y2]=board[x1][y1];
				board[x1][y1]=State.E;
			}
			
			//Moving and eating an enemy token
			if((x2==x1-2 || x2==x1+2) && (y2==y1-2 || y2==y1+2) && (board[x1+((x2-x1)/2)][y1+((y2-y1)/2)]==opColor || board[x1+((x2-x1)/2)][y1+1]==opColor)){
				board[x1+((x2-x1)/2)][y1+((y2-y1)/2)]=State.E;
				board[x2][y2]=board[x1][y1];
				board[x1][y1]=State.E;
			}
		}	
		
		//The other player is performing the move
		if(board[x1][y1]==opColor){
			
			//Moving in an empty place
			if((x2==x1-1 || x2==x1+1) && (y2==y1-1 || y2==y1+1)){
				board[x2][y2]=board[x1][y1];
				board[x1][y1]=State.E;
			}
			
			//Moving and eating an enemy token
			if((x2==x1-2 || x2==x1+2) && (y2==y1-2 || y2==y1+2)  && board[x1+((x2-x1)/2)][y1+((y2-y1)/2)]==myColor){
				board[x1+((x2-x1)/2)][y1+((y2-y1)/2)]=State.E;
				board[x2][y2]=board[x1][y1];
				board[x1][y1]=State.E;
			}
		}
		
		//Checking for winners and returning the result
		return isWin();
	}
	
	/**
	 * Check if the move is valid
	 * @param x1 the x coordinate of the starting point
	 * @param y1 the y coordinate of the starting point
	 * @param x2 the x coordinate of the final point
	 * @param y2 the y coordinate of the final point
	 * @return True if the move is valid, false otherwise
	 */
	public boolean isValidMove(int x1, int y1, int x2 , int y2){
		
		//Checking the coordinates
		if(x1<0 || x1>7 || y1<0 || y1>7 || x2<0 || x2>7 || y2<0 || y2>7)
			return false;
		
		//Checking the move 
		if(board[x1][y1]==myColor && board[x2][y2]==State.E){
			
			//simple move
			if((x2==x1-1 || x2==x1+1) && (y2==y1-1 || y2==y1+1))
				return true;
			
			//catch
			if((x2==x1-2 || x2==x1+2) && (y2==y1-2 || y2==y1+2) && board[x1+((x2-x1)/2)][y1+((y2-y1)/2)]==opColor)
				return true;
		}
		return false;
	}
	
	/**
	 * Check if somebody wins
	 * @return State.E if nobody wins, or State.B, State.W is there is a winner 
	 */
	private State isWin(){
		int countB=0;
		int countW=0;
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				if(board[j][i]==State.B)
					countB+=1;
				if(board[j][i]==State.W)
					countW+=1;
			}
		
		//If there aren't black tokens the white player wins
		if(countB==0)
			return State.W;
		
		//If there aren't white tokens the black player wins
		if(countW==0)
			return State.B;
		
		//no winners
		return State.E;
				
	}
	/**
	 * Return the color of the player
	 * @return Return the color of the player
	 */
	public State getMyColor(){
		return myColor;
	}
}
