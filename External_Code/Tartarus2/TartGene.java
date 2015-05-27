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
//

import java.awt.Point;
import java.io.*;
import gpjpp.*;
import java.util.Random;

//extend GPGene to evaluate Tartarus

public class TartGene extends GPGene {
    //public null constructor required during stream loading only
    public TartGene() {}

    //this constructor called when new genes are created
    TartGene(GPNode gpo) { super(gpo); }

    //this constructor called when genes are cloned during reproduction
    TartGene(TartGene gpo) { super(gpo); }

    //called when genes are cloned during reproduction
    protected Object clone() { return new TartGene(this); }

    //ID routine required for streams
    public byte isA() { return GPObject.USERGENEID; }

    //must override GPGene.createChild to create TartGene instances
    public GPGene createChild(GPNode gpo) { return new TartGene(gpo); }

    //called by TartGP.evaluate() for main branch of each GP
    int evaluate(TartVariables cfg, TartGP gp) {
        return evaluate(cfg, gp, null);
    }

    //called by TartGP.evaluate() for main branch of each GP
    int evaluate(TartVariables cfg, TartGP gp, PrintStream os) {
        int val = node.value();
        if (val==0) {
            cfg.grid.randomEmpty();
            return 0;
        }
        
        if (val <=10) {
            int result = -1; 
            if (val==Grid.UR)
                result = cfg.dozerGrid.sensor(1, -1);
            else if (val==Grid.MR)
                result = cfg.dozerGrid.sensor(0, -1);
            else if (val==Grid.LR)
                result = cfg.dozerGrid.sensor(-1, -1);
            else if (val==Grid.UM)
                result = cfg.dozerGrid.sensor(1, 0);
            else if (val==Grid.LM)
                result = cfg.dozerGrid.sensor(-1, 0);
            else if (val==Grid.UL)
                result = cfg.dozerGrid.sensor(1, 1);
            else if (val==Grid.ML)
                result = cfg.dozerGrid.sensor(0, 1);
            else if (val==Grid.LL)
                result = cfg.dozerGrid.sensor(-1, 1);

            ( (TartGene)get(result) ).evaluate(cfg, gp, os);
            return 1;
        }
        
        if (val==Grid.PRG2) {
            ( (TartGene)get(0) ).evaluate(cfg, gp, os);
            ( (TartGene)get(1) ).evaluate(cfg, gp, os);
        }
        
        if (val==Grid.PRG3) {
            ( (TartGene)get(0) ).evaluate(cfg, gp, os);
            ( (TartGene)get(1) ).evaluate(cfg, gp, os);
            ( (TartGene)get(2) ).evaluate(cfg, gp, os);
        }
        
        if (val==Grid.RC) {
            Random rand = new Random();
            int child = rand.nextInt(1);
            
            ( (TartGene)get(child) ).evaluate(cfg, gp, os);
        }
        
        if (val==Grid.GETHIT) {
        	
        	
            Random rand = new Random();
            int child = rand.nextInt(1);
            
            ( (TartGene)get(child) ).evaluate(cfg, gp, os);
        }
        
        return 2;
    }
}


