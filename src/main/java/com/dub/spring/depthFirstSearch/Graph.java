package com.dub.spring.depthFirstSearch;

import java.io.Serializable;

/** Graph has Vertices and Adjacency Lists */
public class Graph implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Vertex[] vertices;
	protected int N;
	
	public Graph(Graph source) {
		this.N = source.N;
		this.vertices = new Vertex[source.N];
		for (int i = 0; i < source.N; i++) {
			this.vertices[i] = new Vertex(source.vertices[i]);
		}
	}
	
	public Graph(int N) {
		vertices = new Vertex[N];
	}
	
	
	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}

	public void display() {// used for debugging only
		for (Vertex v : vertices) {
			System.out.println(v);
		}
		
		System.out.println();
	}
	
	public void display2() {// used for debugging only
		for (int i1 = 0; i1 < N; i1++) {// for each vertex
			System.out.print(vertices[i1].getName() + " -> ");
			for (int i2 = 0; i2 < vertices[i1].getAdjacency().size(); i2++) {
				System.out.print(this.vertices[i1].getName() + " ");
			}
			System.out.println();
		}
		System.out.println();	
	
	}	

}
