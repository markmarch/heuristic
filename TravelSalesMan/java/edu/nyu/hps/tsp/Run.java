package edu.nyu.hps.tsp;

import java.io.IOException;
import java.util.*;

import static edu.nyu.hps.tsp.Util.*;

public class Run {
	public String run(String[] args) {
		String fileName = "";
		int cityNumber = 0;
		if (args.length == 1) {
			fileName = args[0];
			try {
				cityNumber = countLine(fileName) + 1;
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		
		if (args.length == 2) {
			fileName = args[0];
			cityNumber = Integer.parseInt(args[1]);
		}
	    
	    Graph g = loadFile(fileName, cityNumber);
	    g.generateMST();
	    ArrayList<Edge> match = perfectMatching(g.findOddDegreeNodes());
	    /////////////////////////////////////////////////
//	    System.out.print("match number: " + match.size());
	    
	    g.union(match);
	    ArrayList<Integer> TSPtour = formTour(g.eulerCircuit());
	    
	    ////////////////////////////////////////////////
	    ArrayList<Integer> odd = g.findOddDegreeNodes();
	    for (Iterator<Integer> it = odd.iterator(); it.hasNext(); ) {
	    	System.out.print(it.next() + " THIS IS WRONG!!!!!!!!!!!!!!!!!!!!!!!");
	    	System.out.println();
	    }
	    StringBuffer sb = new StringBuffer();
	    for (Iterator<Integer> it = TSPtour.iterator(); it.hasNext(); ) {
	    	sb.append(it.next() + ",");
	    }
	    return sb.toString();
//	    System.out.print("\nlength: " + TSPtour.size());
//	    g.improveEuler(TSPtour);
	}
}
