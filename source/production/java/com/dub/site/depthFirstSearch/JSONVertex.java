package com.dub.site.depthFirstSearch;

import java.util.List;

/** POJO represents vertex for AJAX initialization request only */
public class JSONVertex {
	

	/**
	 * 
	 */
	private String name;        	
	private List<Integer> edges;// all edges indices from this vertex
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getEdges() {
		return edges;
	}
	public void setEdges(List<Integer> edges) {
		this.edges = edges;
	}

	
}
