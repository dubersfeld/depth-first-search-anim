package com.dub.site.depthFirstSearch;

import java.util.ArrayList;
import java.util.List;

// POJO
/** We need to pass a both a list of vertices and a list of edges */
public class GraphInitRequest {
	
	private List<JSONEdge> jsonEdges;
	private List<JSONVertex> jsonVertices;
	
	public GraphInitRequest() {
		jsonEdges = new ArrayList<JSONEdge>();
	}

	public List<JSONEdge> getJsonEdges() {
		return jsonEdges;
	}

	public void setJsonEdges(List<JSONEdge> jsonEdges) {
		this.jsonEdges = jsonEdges;
	}

	public List<JSONVertex> getJsonVertices() {
		return jsonVertices;
	}

	public void setJsonVertices(List<JSONVertex> jsonVertices) {
		this.jsonVertices = jsonVertices;
	}

}
