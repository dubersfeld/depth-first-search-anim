package com.dub.site.depthFirstSearch;

//import java.io.Serializable;


/** Vertex has an adjacency list of vertices */
public class DFSVertex extends Vertex{

	/**
	 * 
	 */
	// all additional fields    
	private Integer parent = null;
	private Color color = Color.BLACK;
	private int d = 0;
	private int f = 0;
	
	public DFSVertex() {
		super();
		
	}
	
	public DFSVertex(DFSVertex source) {
		super(source);
		this.parent = source.parent;
		this.color = source.color;
		this.d = source.d;
		this.f = source.f;
	}
		
	
	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}
	
	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}


	public String toString() {
		return this.name + " " + this.parent 
				+ " " + this.color + " " + this.d + "/" + this.f;
	}

	public static enum Color {
		BLACK, GREEN, BLUE
	}

}
