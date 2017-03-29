package com.dub.site.depthFirstSearch;

public class JSONAdjacency {

private int[] adjacency;
	
	public JSONAdjacency(int N) {
		this.adjacency = new int[N];
	}
	
	

	public int[] getAdjacency() {
		return adjacency;
	}

	public void setAdjacency(int[] adjacency) {
		this.adjacency = adjacency;
	}
	
	
	// for debugging only
	public void display() {
		for (int i = 0; i < this.adjacency.length; i++) {
			System.out.println(this.adjacency[i]);
		}
	}
	
}
