package Tartarus1;
// Tartarus implementation
// Copyright (c) 2013, Sherri Goings
//
// This program is free software; you can redistribute it and/or 
// modify it under the terms of version 2 of the GNU General Public 
// License as published by the Free Software Foundation.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

import java.io.*;
import java.util.Properties;

import battleshipGP.battleBoard;
import gpjpp.*;

//extension of GPVariables for Tartarus-specific stuff

public class TartVariables extends GPVariables {

    //number of cells in trail grid
    public int size = 10;

    //number of random Tartarus grids to test each genome on
    public int NumTestGrids = 10;

    public battleBoard board;

    //public null constructor required for stream loading
    public TartVariables() { /*gets default values*/ }

    //ID routine required for streams
    public byte isA() { return GPObject.USERVARIABLESID; }

    public void createGrid() {
        board = new battleBoard(size);
        board.initializeBoard();
    }

    //get values from properties
    public void load(Properties props) {
        if (props == null)
            return;
        super.load(props);
        NumTestGrids = getInt(props, "NumTestGrids", NumTestGrids);
    }

    //get values from a stream
    protected void load(DataInputStream is)
        throws ClassNotFoundException, IOException,
            InstantiationException, IllegalAccessException {

        super.load(is);
        NumTestGrids = is.readInt();
    }

    //save values to a stream
    protected void save(DataOutputStream os) throws IOException {

        super.save(os);
        os.writeInt(NumTestGrids);
    }

    //write values to a text file
    public void printOn(PrintStream os, GPVariables cfg) {

        super.printOn(os, cfg);
        os.println("NumTestGrids              = "+NumTestGrids);
    }
}
