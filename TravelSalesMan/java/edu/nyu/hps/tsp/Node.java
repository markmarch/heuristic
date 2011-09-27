package edu.nyu.hps.tsp;

import java.util.*;

public class Node {
	private int ID;
	private int degree;
	private Node parent;
	private int size;
//	private List<Edge> edges;
	private Queue<Integer> neighbour;
	
	public Node(int ID) {
		this.ID = ID;
		this.degree = 0;
		parent = this;
		size = 1;
//		edges = new ArrayList<Edge>();
		neighbour = new LinkedList<Integer>();
	}
	
	public boolean hasNeighbour() {
		return !neighbour.isEmpty();
	}
	
	public int pollNeighbour() {
		return neighbour.poll();
	}
	
	public void removeNeighbour(Integer ID) {
		neighbour.remove(ID);
	}
	
	public void setSize(int s) {
		this.size = s;
	}
	
	public int size() {
		return size;
	}
	
	public void setPar(Node n) {
		this.parent = n;
	}
	
	public Node par() {
		return parent;
	}
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}
	
	public void addEdge(Edge e) {
//		this.edges.add(e);
		if (e.Vex1() == this.ID) {
			neighbour.add(e.Vex2());
		}
		else {
			neighbour.add(e.Vex1());
		}
		this.degree++;
	}
	
	@Override
	public String toString() {
		return String.valueOf(ID);
	}
}
