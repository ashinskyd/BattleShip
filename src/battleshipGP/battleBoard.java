package battleshipGP;

import java.util.ArrayList;
import java.util.Random;



public class battleBoard {
	
	//Enum for each location in the board
	//Miss=fired but misses
	//empty means no ship and haven't checked yet
	public enum Square {
		HIT, MISS, EMPTY, SHIP
	}

	
	public Square[][] board;
	private int numMoves = 0;
	public int size;
	private ArrayList<Integer> uncheckedHits;
	private ArrayList<ArrayList> uncheckedDirs;
	private boolean anyHits=false;
	private int lastHitIndex;
	
	
	//Constructor sets all the spaces on the given board size to empty
	public battleBoard(int size) {
		uncheckedHits = new ArrayList<Integer>();
		uncheckedDirs = new ArrayList<ArrayList>();
		this.size = size;
		board = new Square[size][size];
		for(int i = 0; i < size;i++ ){
			for(int j = 0;j < size;j++) {
				board[i][j] = Square.EMPTY;
			}
		}
	}
	
	
	//Based on the length of a ship, randomly select a 
	//valid arrangement of squares on the board to place that specific ship, 
	//and set those squares to "Ship"
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
	
	
	//Function searches the board for a random square which has
	//not been fired on yet. Returns that index.
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
	
	//If there was a previous hit, return either (randomly)
	//the square immiedietly to the right or left of the last hit
	//If there was no previous hit or there is not  a valid square, return randomSquare()
	public int lastHotHorizontal(){
		if(!anyHits){
			return  randomSquare();
		}else{
			Random rand = new Random();
			ArrayList<Integer> validDirs = new ArrayList<Integer>();
			boolean foundEmpty = false;
			int left = lastHitIndex-1;
			int right = lastHitIndex+1;
			validDirs.add(left);
			validDirs.add(right);
			while(!foundEmpty){
				if(validDirs.size()==0){
					return randomSquare();
				}else{
					int direction = rand.nextInt(validDirs.size());
					int location = validDirs.get(direction);
					if(location>=0 && location<=99){
						if(board[location/10][location%10]!=Square.HIT &&  board[location/10][location%10]!=Square.MISS){
							return location;
						}else{
							validDirs.remove(direction);
						}
					}else{
						validDirs.remove(direction);
					}
				}
			}
			return  randomSquare();
		}
	}
	
	//If there was a previous hit, return either (randomly)
	//the square immiedietly up or down of the last hit.
	//If there was no previous hit or there is not  a valid square, return randomSquare()
	public int lastHotVertical(){
		if(!anyHits){
			return randomSquare();
		}else{
			Random rand = new Random();
			ArrayList<Integer> validDirs = new ArrayList<Integer>();
			boolean foundEmpty = false;
			int up = lastHitIndex-10;
			int down = lastHitIndex+10;
			validDirs.add(up);
			validDirs.add(down);
			while(!foundEmpty){
				if(validDirs.size()==0){
					return randomSquare();
				}else{
					int direction = rand.nextInt(validDirs.size());
					int location = validDirs.get(direction);
					if(location>=0 && location<=99){
						if(board[location/10][location%10]!=Square.HIT &&  board[location/10][location%10]!=Square.MISS){
							return location;
						}else{
							validDirs.remove(direction);
						}
					}else{
						validDirs.remove(direction);
					}
				}
			}
			return randomSquare();
		}
	}
	
	//Return the last square which was a Hit
	//Otherwise, return a randomsquare
	public int lastHot(){
		if(!anyHits){
			return randomSquare();
		}else{
			return lastHitIndex;
		}
	}
	
	//Return any square adjescent to a randomly selected square which was a hit previously
	//If no hits, return a randomsquare
	public int randomHot(){
		if(!anyHits){
			return randomSquare();
		}
		else{
			Random rand = new Random();
			if(uncheckedHits.size()==0){
				anyHits = false;
				return randomSquare();
			}
			int randhitIndex = rand.nextInt(uncheckedHits.size());
			int randhit = uncheckedHits.get(randhitIndex);
			ArrayList<Integer> validDirs = new ArrayList<Integer>();
			boolean foundEmpty = false;
			int up = randhit -10;
			int down = randhit +10;
			int left = randhit-1;
			int right = randhit+1;
			validDirs.add(up);
			validDirs.add(down);
			validDirs.add(left);
			validDirs.add(right);
			while(!foundEmpty){
				if(validDirs.size()==0){
					uncheckedHits.remove(randhitIndex);
					return randomHot();
				}else{
					int direction = rand.nextInt(validDirs.size());
					int location = validDirs.get(direction);
					if(location>=0 && location<=99){
						if(board[location/10][location%10]!=Square.HIT &&  board[location/10][location%10]!=Square.MISS){
							return location;
						}else{
							validDirs.remove(direction);
						}
					}else{
						validDirs.remove(direction);
					}
				}
			}
			return randomSquare();
		}
	}
	
	//Returns a random square on the board
	public int randomSquare(){
		Random rand = new Random();
		return rand.nextInt(100);
	}
	
	//Sets the board up with the ships present in a standard battleship game
	public void initializeBoard(){
		this.setShip(5);
		this.setShip(4);
		this.setShip(3);
		this.setShip(3);
		this.setShip(2);
	}
	
	//printing function to display boards
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
	
	//Fitness function is just the number of attempts to fire
	//Function returns this fitness
	public int calcFitness(){
		return numMoves;
	}
	
	//increases the fitness by 1 and attempts to fire on a square.
	//If there's a ship, set that square to a hit, otherwise, set to a miss
	public int fire(int xloc, int yloc){
		numMoves++;
		if(board[xloc][yloc]==Square.EMPTY){
			board[xloc][yloc]=Square.MISS;
			return 1;
		}else if(board[xloc][yloc]==Square.SHIP){
			anyHits=true;
			board[xloc][yloc]=Square.HIT;
			lastHitIndex = xloc*10+yloc;
			uncheckedHits.add(xloc*10+yloc);
			ArrayList<Integer> dirs = new ArrayList<Integer>();
			int up = -10;
			dirs.add(up);
			int down = 10;
			dirs.add(down);
			int left = -1;
			dirs.add(left);
			int right = 1;
			dirs.add(right);
			
			uncheckedDirs.add(dirs);
			return 2;
		}
		return 0;
	}
	
	//Just returns true or false if all ships are shrunk.
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
	
	/*public static void main(String [] args){
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
		
	}*/
}
