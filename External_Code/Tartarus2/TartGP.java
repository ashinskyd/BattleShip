package Tartarus2;
// Tartarus Implementation
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
import gpjpp.*;

//extend GP for lawn mowing
//class must be public for stream loading; otherwise non-public ok

public class TartGP extends GP {

    //public null constructor required during stream loading only
    public TartGP() {}

    //this constructor called when new GPs are created
    TartGP(int genes) { super(genes); }

    //this constructor called when GPs are cloned during reproduction
    TartGP(TartGP gpo) { super(gpo); }

    //called when GPs are cloned during reproduction
    protected Object clone() { return new TartGP(this); }

    //ID routine required for streams
    public byte isA() { return GPObject.USERGPID; }

    //must override GP.createGene to create LawnGene instances
    public GPGene createGene(GPNode gpo) { return new TartGene(gpo); }

    //must override GP.evaluate to return standard fitness
    public double evaluate(GPVariables cfg) {

        TartVariables tcfg = (TartVariables)cfg;

        double totFit = 0;
        // test GP on N random boards
        for (int k=0; k<tcfg.NumBoards; k++) {
            //create new random grid
            tcfg.createGrid();
            while (!tcfg.grid.gameDone()) {
                ((TartGene)get(0)).evaluate(tcfg, this);
            }
            totFit += tcfg.grid.calcFitness();
        }
        totFit = totFit/tcfg.NumBoards;
        if (cfg.ComplexityAffectsFitness)
            //add length into fitness to promote small trees
            totFit += length()/1000.0;

        //return standard fitness
        return totFit;
    }

    //optionally override GP.printOn to print lawn-specific data
    public void printOn(PrintStream os, GPVariables cfg) {
        super.printOn(os, cfg);
    }

    /*public void printTree(PrintStream os, GPVariables cfg) {
        //super.printTree(os, cfg);
        
        // write grid at each step for this genome
        TartVariables tcfg = (TartVariables)cfg;
        TartGene gene = (TartGene)get(0);

        double totFit = 0;
        // run this genome on some number of test grids, printing the resulting grid at each step
        for (int i=0; i<tcfg.NumBoards; i++) {
            //create new random grid
            tcfg.createGrid();
            os.println("\n---------------------------------");
            os.println("DOZER BEHAVIOR ON TEST GRID "+i);
            os.println("---------------------------------");
            while (steps<tcfg.NumSteps) {
                ((TartGene)get(0)).evaluate(tcfg, null, os);
                steps += tcfg.dozerGrid.getSteps();
            }
            int curGridFit = tcfg.dozerGrid.calcFitness();
            totFit += curGridFit;
            os.println("GRID FITNESS = "+curGridFit);
        }
        totFit = totFit/tcfg.NumTestGrids;
        if (cfg.ComplexityAffectsFitness)
            //add length into fitness to promote small trees
            totFit += length()/1000.0;
        os.println("FINAL FITNESS = "+totFit);
    }*/
    
}
