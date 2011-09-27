package edu.nyu.hps.tsp;

import java.util.*;

public class Graph {
	private Node[] V;
	private ArrayList<Edge> E;
	
	
	public Graph(Node[] V, ArrayList<Edge> E) {
		this.V = V;
		this.E = E;
	}
	
	public void generateMST() {
		Edge e;
		Queue<Edge> Q = new PriorityQueue<Edge>(E);
		
		while (!Q.isEmpty()) {
			e = Q.poll();
			Node vex1 = V[e.Vex1()];
			Node vex2 = V[e.Vex2()];
			while (vex1.par() != vex1) {
				vex1 = vex1.par();
			}
			while (vex2.par() != vex2) {
				vex2 = vex2.par();
			}
			if (vex1 != vex2) {
				if (vex1.size() <= vex2.size()) {
					vex1.setPar(vex2);
					vex2.setSize(vex1.size() + vex2.size());
				}
				else {
					vex2.setPar(vex1);
					vex1.setSize(vex1.size() + vex2.size());
				}
				V[e.Vex1()].addEdge(e);
				V[e.Vex2()].addEdge(e);
			}
		}
	}
	
	public ArrayList<Integer> findOddDegreeNodes() {
		ArrayList<Integer> oddNodes = new ArrayList<Integer>(70);
		for (int i = 1; i < V.length; i++) {
			if (V[i].getDegree() % 2 != 0) {
				oddNodes.add(i);
			}
		}
		//need clone?
		return oddNodes;
	}
	
	public void union(ArrayList<Edge> match) {
		Iterator<Edge> it = match.iterator();
		while (it.hasNext()) {
			Edge e = it.next();
			V[e.Vex1()].addEdge(e);
			V[e.Vex2()].addEdge(e);
		}
	}
	
	public ArrayList<Integer> eulerCircuit() {
		ArrayList<Integer> eulerCircuit = new ArrayList<Integer>(V.length);
		Stack<Node> stack = new Stack<Node>();
		Node currentVex = V[1];
		
		while (currentVex.hasNeighbour() || !stack.isEmpty()) {
			if (!currentVex.hasNeighbour()) {
				eulerCircuit.add(currentVex.getID());
				currentVex = stack.pop();
			}
			else {
				stack.add(currentVex);
				int next = currentVex.pollNeighbour();
				V[next].removeNeighbour(currentVex.getID());
				currentVex = V[next];
			}
		}
		return eulerCircuit;
	}
	
	public ArrayList<Integer> improveEuler(ArrayList<Integer> eulerCircuit) {
		int[] tour = new int[V.length];
		
		ArrayList<Integer> repeatVeisitedNodes = new ArrayList<Integer>();
		for (int i = 1; i< V.length; i++) {
			if (V[i].getDegree() > 2){
				repeatVeisitedNodes.add(V[i].getID());
//				System.out.println(V[i].getDegree());
			}
		}
		
		return repeatVeisitedNodes;
	}
}
