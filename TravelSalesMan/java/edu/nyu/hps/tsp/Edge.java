package edu.nyu.hps.tsp;

public class Edge implements Comparable{
	private int vex1;
	private int vex2;
	private double weight;
	
	public Edge(int vex1, int vex2, double weight) {
		this.vex1 = vex1;
		this.vex2 = vex2;
		this.weight = weight;
	}
	
	public Edge(int vex2, double weight) {
		this.vex1 = 0;
		this.vex2 = vex2;
		this.weight = weight;
	}
	/**
	 * @return the vex1
	 */
	public int Vex1() {
		return vex1;
	}
	
	/**
	 * @return the vex1
	 */
	public void setVex1(int vex1) {
		this.vex1 = vex1;
	}
	
	/**
	 * @return the vex2
	 */
	public int Vex2() {
		return vex2;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return vex1 + " " + vex2 + " " + weight;
	}
	
	@Override
	public int compareTo(Object o) {
		if (o == this) {
			return 0;
		}
		if (!(o instanceof Edge)) {
			throw new ClassCastException();
		}
		
		Edge e = (Edge)o;
		if (this.weight < e.weight) {
			return -1;
		}
		if (this.weight > e.weight) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
//	@Override
//	public String toString() {
//		return vex1+" " + vex2 + " " + String.valueOf(weight);
//	}
	
}
