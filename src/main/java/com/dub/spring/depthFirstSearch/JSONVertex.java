package com.dub.spring.depthFirstSearch;


import com.dub.spring.depthFirstSearch.DFSVertex.Color;


/** POJO represents vertex for AJAX initialization request only */
public class JSONVertex {
	
	/**
	 * 
	 */
	private String name;
	private Color color;
	private Integer parent;
	private int d, f;
	
	public JSONVertex() {
	}
	
	public JSONVertex(DFSVertex v) {
		this.name = v.getName();
		this.parent = v.getParent(); 
		this.color = v.getColor();
		this.d = v.getD();
		this.f = v.getF();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
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
	
	
	
}
