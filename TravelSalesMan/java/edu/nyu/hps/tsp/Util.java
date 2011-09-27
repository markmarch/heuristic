package edu.nyu.hps.tsp;

import java.io.*;
import java.util.*;

public class Util {
	private static double[][] weightMatrix;
	
	private static int square(int n) {
		return n*n;
	}
	
	public static int countLine(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        while ((readChars = is.read(c)) != -1) {
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n')
	                    ++count;
	            }
	        }
	        return count;
	    } finally {
	        is.close();
	    }
	}
	
	public static Graph loadFile(String fileName, int cityNumber)  {
		
		Coord[] Coords = new Coord[cityNumber + 1];
		Coords[0] = null;
		Node[] V = new Node[cityNumber + 1];
		V[0] = null;
		
//		ArrayList<Coord> Coords = new ArrayList<Coord>();
		
		
		try {
			File f = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			int index = 1;
			while((line = br.readLine()) != null) {
				String[] args = line.split("\\s+");
				int ID = Integer.parseInt(args[0]);
				Coord coord = new Coord(args);
				Coords[index] = coord;
				Node node = new Node(ID);
				V[index] = node;
				index++;
			}
			br.close();
		}catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			System.out.println("Error: file cant open");
		}
		return computeWeight(Coords, V);
	}
	
	private static Graph computeWeight(Coord[] coords,Node[] V) {
		ArrayList<Edge> E = new ArrayList<Edge>(10000);
		
		weightMatrix = new double[coords.length][coords.length];
		weightMatrix[0][0] = 0;
		
		for (int i = 1; i < coords.length; i++) {
			for (int j = 1; j < coords.length; j++) {
				if (i == j) {
					weightMatrix[i][j] = 0;
				}
				else if (i < j) {
					double weight = computeWeight(coords[i], coords[j]);
					weightMatrix[i][j] = weight;
					Edge e = new Edge(i, j, weight);
					E.add(e);
				}
				else if (i > j) {
					weightMatrix[i][j] = weightMatrix[j][i];
				}
			}
		}
		return new Graph(V, E);
	}
	
	public static double Weight(int n1, int n2) {
		return weightMatrix[n1][n2];
	}
	
	public static double computeWeight(Coord m, Coord n) {
		return Math.sqrt(square(m.X() - n.X()) + square(m.Y() - n.Y()) +
			square(m.Z() - n.Z()));
	}
	
	public static ArrayList<Edge> perfectMatching(ArrayList<Integer> oddNodes) {
		Integer[] nodes = oddNodes.toArray(new Integer[oddNodes.size()]);
		Queue<Edge> maxHeap = new PriorityQueue<Edge>(square(nodes.length)
				, Collections.reverseOrder());
		Map<Integer, PriorityQueue<Edge>> adjacentEdge = new HashMap<Integer, PriorityQueue<Edge>>();
		ArrayList<Integer> matchedNodes = new ArrayList<Integer>(nodes.length);
		ArrayList<Edge> match = new ArrayList<Edge>(nodes.length/2);
		
		for (int i = 0; i < nodes.length; i++) {
			PriorityQueue<Edge> Q = new PriorityQueue<Edge>(nodes.length/2 + 1);
			adjacentEdge.put(nodes[i], Q);
		}
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = i + 1; j < nodes.length; j++) {
				Edge e = new Edge(nodes[i], nodes[j], Weight(nodes[i], nodes[j]));
				Edge edge_for_i = new Edge(nodes[j], Weight(nodes[i], nodes[j]));
				Edge edge_for_j = new Edge(nodes[i], Weight(nodes[i], nodes[j]));
				maxHeap.add(e);
				adjacentEdge.get(nodes[i]).add(edge_for_i);
				adjacentEdge.get(nodes[j]).add(edge_for_j);
			}
		}
		
		while (matchedNodes.size() < nodes.length) {
			Edge longEdge = maxHeap.poll();
			int Vex1 = longEdge.Vex1();
			int Vex2 = longEdge.Vex2();
			if (!matchedNodes.contains(Vex1) &&
					!matchedNodes.contains(Vex2)) {
				matchedNodes.add(Vex1);
				matchedNodes.add(Vex2);
				
				Edge e1 = adjacentEdge.get(Vex1).poll();
				while (matchedNodes.contains(e1.Vex2())) {
					e1 = adjacentEdge.get(Vex1).poll();
				}
				Edge e2 = adjacentEdge.get(Vex2).poll();
				while (matchedNodes.contains(e2.Vex2())) {
					e2 = adjacentEdge.get(Vex1).poll();
				}
				
				e1.setVex1(Vex1);
				e2.setVex1(Vex2);
				match.add(e1);
				match.add(e2);
				
				matchedNodes.add(e1.Vex2());
				matchedNodes.add(e2.Vex2());
			}
		}
		return match;
	}
	
	public static ArrayList<Integer> formTour(ArrayList<Integer> eulerCycle) {
		ArrayList<Integer> visitedCities = new ArrayList<Integer>(20);
		
		Iterator<Integer> it = eulerCycle.iterator();
		while (it.hasNext()) {
			int city = it.next();
			if (visitedCities.contains(city)) {
				it.remove();
			}
			else {
				visitedCities.add(city);
			}
		}
		eulerCycle.add(1);
		return eulerCycle;
	}
}
