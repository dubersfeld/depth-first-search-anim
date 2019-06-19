package com.dub.spring.depthFirstSearch;

/** POJO represents an edge for AJAX initialization request */
public class JSONEdge {
	
	protected int from;// origin
	protected int to;// end

	public JSONEdge() {
	}
	
	public JSONEdge(JSONEdge source) {
		if (source != null) {
			this.from = source.from;
			this.to = source.to;
		}
	}
	
	public JSONEdge(int from, int to) {
		this.to = to;
		this.from = from;
	}
	
	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
	
	public String toString() {// for debug only
		return from + ", " + to;
	}
	
}
