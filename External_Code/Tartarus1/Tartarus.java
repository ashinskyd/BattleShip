package Tartarus1;
// gpjpp example program
// Copyright (c) 1997, Kim Kokkonen
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
//
// Send comments, suggestions, problems to kimk@turbopower.com

import java.io.*;
import java.util.Properties;
import gpjpp.*;

//Lawnmower test
class Tartarus extends GPRun {

    //must override GPRun.createVariables to return lawn-specific variables
    protected GPVariables createVariables() { 
        return new TartVariables(); 
    }

    //must override GPRun.createNodeSet to return 
    //  initialized set of functions & terminals
    protected GPAdfNodeSet createNodeSet(GPVariables cfg) {
        GPAdfNodeSet adfNs = new GPAdfNodeSet(1);
        GPNodeSet ns0 = new GPNodeSet(11);
        

        //MAIN TREE
        ns0.putNode(new GPNode(Grid.RAND, "ran"));
        ns0.putNode(new GPNode(Grid.INC, "inc", 1));
        ns0.putNode(new GPNode(Grid.INC10, "inc10", 1));
        ns0.putNode(new GPNode(Grid.DEC, "dec", 1));
        ns0.putNode(new GPNode(Grid.DEC10, "dec10", 1));
        ns0.putNode(new GPNode(Grid.ADD, "add", 2));
        ns0.putNode(new GPNode(Grid.SUB, "sub", 2));
        ns0.putNode(new GPNode(Grid.MAX, "max", 2));
        ns0.putNode(new GPNode(Grid.MIN, "min", 2));
        ns0.putNode(new GPNode(Grid.RE, "re"));        
        ns0.putNode(new GPNode(Grid.RH, "rh"));        
        
        adfNs.put(0, ns0);
        return adfNs;
    }

    //must override GPRun.createPopulation to return 
    //  lawn-specific population
    protected GPPopulation createPopulation(GPVariables cfg, 
        GPAdfNodeSet adfNs) {
        return new TartPopulation(cfg, adfNs);
    }

    //construct this test case
    Tartarus(String baseName) { super(baseName, true); }

    //main application function
    public static void main(String[] args) {

        //compute base file name from command line parameter
        String baseName;
        if (args.length == 1)
            baseName = args[0];
        else
            baseName = "tartarus";

        //construct the test case
        Tartarus test = new Tartarus(baseName);

        //run the test
        test.run();

        //make sure all threads are killed
        System.exit(0);
    }
}
