package edu.nyu.hps.tsp;

public class Coord {
	private int ID;
	private int x;
	private int y;
	private int z;
	
	public Coord(String[] args) {
		this.ID = Integer.parseInt(args[0]);
		this.x = Integer.parseInt(args[1]);
		this.y = Integer.parseInt(args[2]);
		this.z = Integer.parseInt(args[3]);
	}
	
	public int getID() {
		return ID;
	}
	
	public int X() {
		return x;
	}
	
	public int Y() {
		return y;
	}
	
	public int Z() {
		return z;
	}
}
