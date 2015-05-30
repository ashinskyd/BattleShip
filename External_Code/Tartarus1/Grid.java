package Tartarus1;
import java.util.*;
import java.io.*;

public class Grid {

    //functions and terminals
    public final static int EMPTY = 0;
    public final static int MISS = 1; 
    public final static int HIT = 2; 
    public final static int RE = 3; 
    public final static int INC = 4;  
    public final static int DEC = 5;  
    public final static int ADD = 6;  
    public final static int SUB = 7;  
    public final static int MAX = 8;  
    public final static int MIN = 9;  
    public final static int RH = 10;  
    public final static int HH = 11;  
    public final static int HV = 12;  


    // grid private vars
    private char[][] grid;
    private int xdim, ydim;
    private Random rgen;
    private int numBoxes = 6;
    private int dozerX, dozerY;
    public int dozerFacing;
    char[] dirs = new char[] {'e','n','w','s'};

    // create a Grid of characters with the given dimensions and number of boxes
    public Grid(int xdim, int ydim, int numBoxes) {
        this.xdim = xdim;
        this.ydim = ydim;
        this.numBoxes = numBoxes;
        grid = new char[xdim][ydim];
        // initially fill the grid with spaces
        for (int i=0; i<xdim; i++)
            for (int j=0; j<ydim; j++)
                grid[i][j] = ' ';
        rgen = new Random();
        initGrid();
    }

    // place boxes and bulldozer on the grid, if try to place box in space that would complete 
    // 2x2 grid, just skip that one and place 1 less box overall.
    private void initGrid() {

        // place boxes, if try to place box in space that would complete 2x2 grid, just skip that one
        // and place 1 less box overall.
        int toPlace = numBoxes;
        int remLocs = (xdim-2)*(ydim-2);
        int x=1, y=1;
        while (toPlace > 0) {
            // the probability that this square should get a block is the number of blocks that 
            // remain to be placed divided by the number of squares left to be considered
            double p = (double)toPlace/remLocs;
            if (rgen.nextDouble() < p) {
                if (grid[x-1][y]!='b' || grid[x][y-1]!='b' || grid[x-1][y-1]!='b')
                    grid[x][y] = 'b';
                toPlace--;
            }
            remLocs--;
            // at end of each row, move to beginning of next row
            if (++x == xdim-1) {
                x=1;
                y++;
            }
        }
        // place dozer
        x = rgen.nextInt(xdim-2) + 1;
        y = rgen.nextInt(ydim-2) + 1;

        // if randomly chosen square has a box, just search 2x2 space by
        // it because know can't have 2x2 squares all with boxes
        if (grid[x][y] == 'b') {
            if (grid[x+1][y] != 'b') x++;
            else if (grid[x][y+1] != 'b') y++;
            else {
                x++; 
                y++;
            }
        }
        grid[x][y] = 'D';
        dozerX = x;
        dozerY = y;

        // set dozer to face random direction
        dozerFacing = rgen.nextInt(4);
    }

    // turn dozer left
    public void left() {
        dozerFacing = (dozerFacing + 1) % 4;
    }

    // turn dozer right
    public void right() {
        if (--dozerFacing < 0) dozerFacing = 3;
    }
    
    // if dozer is facing wall or 2 consecutive squares with blocks, nothing happens
    // otherwise move dozer foward 1 and if block in front move it too
    public void forward() {
        // get coordinates of space in front of dozer and space 2 in front
        int frontX = dozerX, frontY = dozerY;
        int forw2X = dozerX, forw2Y = dozerY;
        if (dozerFacing==0) {
            frontX++;
            forw2X += 2;
        }
        else if (dozerFacing==1) {
            frontY--;
            forw2Y -= 2;
        }
        else if (dozerFacing==2) {
            frontX--;
            forw2X -= 2;
        }
        else {
            frontY++;
            forw2Y += 2;
        }
        // if facing wall
        if (frontX<0 || frontX >= xdim || frontY<0 || frontY>=ydim) return;

        // if facing block 
        if (grid[frontX][frontY] == 'b') {
            // if has wall or another block behind it
            if (forw2X<0 || forw2X>=xdim || forw2Y<0 || forw2Y>=ydim ||
                grid[forw2X][forw2Y]=='b') return;
            // if clear behind block, move block
            grid[forw2X][forw2Y] = 'b'; 
        }
        // if here were either facing block with nothing behind it or empty space so move dozer
        grid[frontX][frontY] = 'D';
        grid[dozerX][dozerY] = ' ';
        dozerX = frontX;
        dozerY = frontY;
    }

    // frontOffset is 1 for square in front of dozer, 0 for inline with dozer, and -1 for behind
    // sideOffset is 1 for left of dozer, 0 inline, -1 right
    // y goes from 0 at the top to max val at the bottom of grid/screen
    // returns 0,1, or 2 for empty, box, wall respectively 
    public int sensor(int frontOffset, int sideOffset) {
        // determine the appropriate square to check if is empty, wall, or box
        int checkX=-1, checkY=-1;
        if (dirs[dozerFacing] == 'w') {
            checkX = dozerX - frontOffset;
            checkY = dozerY + sideOffset;
        }
        else if (dirs[dozerFacing] == 'e') {
            checkX = dozerX + frontOffset;
            checkY = dozerY - sideOffset;
        }
        else if (dirs[dozerFacing] == 'n') {
            checkX = dozerX - sideOffset;
            checkY = dozerY - frontOffset;
        }
        else if (dirs[dozerFacing] == 's') {
            checkX = dozerX + sideOffset;
            checkY = dozerY + frontOffset;
        }

        // if box to check is out of bounds, return 2 for wall
        if (checkX < 0 || checkY < 0 || checkX >= xdim || checkY >= ydim) return 2;
        
        // otherwise 0 for empty and 1 for box
        if (grid[checkX][checkY] == 'b') return 1;
        else return 0;
    }

    // determine the fitness of the current state of the grid. fitness is (maxScore+1) - score
    // where score is the number of sides of blocks that are touching a wall
    public int calcFitness() {
        int fit = 0;
        int maxFit = 0;
        if (numBoxes>=4) maxFit = numBoxes+4;
        else maxFit = numBoxes*2;
        
        // increase fitness if find boxes in first or last col
        int i=0;
        for (int j=0; j<ydim; j++) 
            if (grid[i][j] == 'b') fit++;
        i=xdim-1;
        for (int j=0; j<ydim; j++) 
            if (grid[i][j] == 'b') fit++;

        // increase fitness for boxes in first or last row, note that 
        // boxes in the corner will have fitness increased twice which 
        // correctly gives them their bonus of 2 instead of 1.
        i=0;
        for (int j=0; j<xdim; j++) 
            if (grid[j][i] == 'b') fit++;
        i=ydim-1;
        for (int j=0; j<xdim; j++) 
            if (grid[j][i] == 'b') fit++;
        
        return maxFit + 1 - fit;
    }

    public void print() {
        print(System.out);
    }

    // print the current state of the grid, showing blocks and the dozer 
    // pointing in the correct direction
    public void print(PrintStream os) {
        for (int y=0; y<ydim; y++) {
            os.print("|");
            for (int x=0; x<xdim; x++) {
                char out = grid[x][y];
                if (out=='D') {
                    if (dozerFacing==0) out = '>';
                    else if (dozerFacing==1) out = '^';
                    else if (dozerFacing==2) out = '<';
                    else if (dozerFacing==3) out = 'v';
                }
                os.print(out+"|");
            }
            os.println();
        }
        os.println();
    }
    
    /*public static void main(String[] args) {
        int sum = 0;
        for(int j = 0;j<1000;j++) {
            Grid curGrid = new Grid(6,6,5);
            Random rand = new Random();
            for(int i=0;i<80;i++){
                int randInt = rand.nextInt(3);
                if (randInt == 0) curGrid.forward();
                if (randInt == 1) curGrid.left();
                if (randInt == 2) curGrid.right();
            }
            sum += curGrid.calcFitness();
        }
        System.out.println("Avg fitness of random:" + (float)sum/1000.0);
        
    }*/

}