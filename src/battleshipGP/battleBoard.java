package battleshipGP;

import java.util.Random;



public class battleBoard {
	
	public enum Square {
		HIT, MISS, EMPTY, SHIP
	}

	
	public Square[][] board;
	private int numMoves = 0;
	public int size;
	
	public battleBoard(int size) {
		this.size = size;
		board = new Square[size][size];
		for(int i = 0; i < size;i++ ){
			for(int j = 0;j < size;j++) {
				board[i][j] = Square.EMPTY;
			}
		}
	}
	
	public void setShip(int length) {
		Random rand = new Random();
		boolean invalid = true;
		int dir = 0;
		int xloc = 0;
		int yloc = 0;
		while(invalid){
			dir = rand.nextInt(2);
			xloc = rand.nextInt(board.length - length);
			yloc = rand.nextInt(board.length - length);
			invalid = false;
			for(int i = 0; i < length; i++){
				if(dir == 0){
					if(board[xloc][yloc+i] != Square.EMPTY) {invalid = true;}
				}
				else if(dir == 1){
					if(board[xloc+i][yloc] != Square.EMPTY) {invalid = true;}
				}
			}
		}
		for(int i = 0; i < length; i++){
			if(dir == 0){
				board[xloc][yloc+i] = Square.SHIP;
			}
			else if(dir == 1){
				board[xloc+i][yloc] = Square.SHIP;
			}
		}
	}
	
	public int randomEmpty(){
		Random rand = new Random();
		int locationx = rand.nextInt(size);
		int locationy = rand.nextInt(size);
		boolean isEmpty = false;
		while(!isEmpty){
			if(board[locationx][locationy]!=Square.MISS && board[locationx][locationy]!=Square.HIT ){isEmpty = true;}
			else{
				locationx = rand.nextInt(size);
				locationy = rand.nextInt(size);
			}
		}
		return locationx*10+locationy;
	}
	
	public int randomHot(){
		Random rand = new Random();
		int locationx = rand.nextInt(size);
		int locationy = rand.nextInt(size);
		boolean isEmpty = false;
		while(!isEmpty){
			if(board[locationx][locationy]==Square.EMPTY){isEmpty = true;}
			else{
				locationx = rand.nextInt(size);
				locationy = rand.nextInt(size);
			}
		}
		return fire(locationx,locationy);
	}
	
	public void initializeBoard(){
		this.setShip(5);
		this.setShip(4);
		this.setShip(3);
		this.setShip(3);
		this.setShip(2);
	}
	
	public void printBoard(){
		System.out.println(" ======================================= ");
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board.length;j++){
				if (board[i][j]==Square.EMPTY){
					System.out.print(" - ");		
				}if (board[i][j]==Square.SHIP){
					System.out.print(" X ");		
				}if (board[i][j]==Square.MISS){
					System.out.print(" O ");		
				}if (board[i][j]==Square.HIT){
					System.out.print(" Z ");		
				}				
			}	
			System.out.print("\n");
		}
	}
	
	public int calcFitness(){
		return numMoves;
	}
	
	public int fire(int xloc, int yloc){
		numMoves++;
		if(board[xloc][yloc]==Square.EMPTY){
			board[xloc][yloc]=Square.MISS;
			return 1;
		}else if(board[xloc][yloc]==Square.SHIP){
			board[xloc][yloc]=Square.HIT;
			return 2;
		}
		return 0;
	}
	
	public boolean gameDone(){
		boolean done = true;
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board.length;j++){
				if (board[i][j]==Square.SHIP){
					done = false;
					return done;
				}				
			}
		}
		return done;
	}
	
	public static void main(String [] args){
		int sumFits = 0;
		for (int j=0;j<1000;j++){
			battleBoard board = new battleBoard(10);
			board.initializeBoard();
			while(!board.gameDone()){
				Random rand = new Random();
				boolean invalid = true;
				int xloc = 0;
				int yloc = 0;
				while(invalid){
					xloc = rand.nextInt(board.board.length);
					yloc = rand.nextInt(board.board.length);
					invalid = false;
					if( (board.board[xloc][yloc] != Square.EMPTY) && ( board.board[xloc][yloc] != Square.SHIP) ) {invalid = true;}
				}
				board.fire(xloc,yloc);
			}
			board.printBoard();
			System.out.println("FITNESS: "+board.calcFitness());
			sumFits += board.calcFitness();
		}
		System.out.println("AVERAGE FITNESS: "+sumFits/1000);
		
	}
}
