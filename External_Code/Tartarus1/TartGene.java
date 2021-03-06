package Tartarus1;
// Implementation of Tartarus Problem
// Author: Sherri Goings
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
        Random rand = new Random();
        int arg1, arg2, arg3, result;
        switch (node.value()) {
        
       //RandomSquare is a terminal described in battleBoard.java
        case Grid.RAND:
            return cfg.board.randomSquare();
        
         //LastHotVertical is a terminal described in battleBoard.java
        case Grid.HV: 
        	return cfg.board.lastHotVertical();
        
        //horizontalHot is a terminal described in battleBoard.java
        case Grid.HH:
        	return cfg.board.lastHotHorizontal();
       
        //RandomEmpty is a terminal described in battleBoard.java	
        case Grid.RE:
            return cfg.board.randomEmpty();

        //RandomHot is a terminal described in battleBoard.java
        case Grid.RH:
            return cfg.board.randomHot();
          
        //LastHot is a terminal described in battleBoard.java
        case Grid.LH:
        	return cfg.board.lastHot();
            
        case Grid.INC:
            return (( ( (TartGene)get(0) ).evaluate(cfg, gp) + 1) % 100+100)%100;
          
            
            //INC10 is a function which increments the value of its child by 10
            //This is equivelent to choosing a square directly below a chosen square.    
        case Grid.INC10:
            return (( ( (TartGene)get(0) ).evaluate(cfg, gp) + 10) % 100+100)%100;

        case Grid.DEC:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp) - 1;
            result = (result%100 + 100)%100;
            return result;
        
            
        //DEC10 is a function which decrements the value of its child by 10
        //This is equivelent to choosing a square directly above a chosen square.
        case Grid.DEC10:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp) - 10;
            result = (result%100 + 100)%100;
            return result;
            
        case Grid.ADD:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp) + ( (TartGene)get(1) ).evaluate(cfg, gp);
            result = (result%100 + 100)%100;
            return result;

        case Grid.SUB:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp) - ( (TartGene)get(1) ).evaluate(cfg, gp);
            result = (result%100 + 100)%100;
            return result;
        case Grid.MAX:
            arg1 = ( (TartGene)get(0) ).evaluate(cfg, gp);
            arg2 = ( (TartGene)get(1) ).evaluate(cfg, gp);
           
            if (arg1 > arg2) result = (arg1%100 + 100)%100;
            else result = (arg2%100 + 100)%100;
            return result;
            
        case Grid.MIN:
            arg1 = ( (TartGene)get(0) ).evaluate(cfg, gp);
            arg2 = ( (TartGene)get(1) ).evaluate(cfg, gp);
           
            if (arg1 < arg2) result = (arg1%100 + 100)%100;
            else result = (arg2%100 + 100)%100;
            return result;

        default:
            throw new RuntimeException("Undefined function type "+node.value());
        }
    }

}
