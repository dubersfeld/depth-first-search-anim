package com.dub.spring.depthFirstSearch;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** Vertex has an adjacency list of vertices */
public class Vertex {

	/**
	 * 
	 */
	protected String name = "";    
	
	@JsonIgnore
	protected List<Integer> adjacency;// all adjacent vertices indices, unused in AJAX response
	
	public Vertex() {
		adjacency = new ArrayList<Integer>();
	}
	
	public Vertex(Vertex source) {
		this.name = source.name;
		this.adjacency = new ArrayList<Integer>();
		for (int i = 0; i < source.adjacency.size(); i++) {
			this.adjacency.add(source.adjacency.get(i));
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getAdjacency() {
		return adjacency;
	}

	public void setAdjacency(List<Integer> adjacency) {
		this.adjacency = adjacency;
	}

}
