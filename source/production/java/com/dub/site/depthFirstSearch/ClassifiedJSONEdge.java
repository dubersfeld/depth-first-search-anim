package com.dub.site.depthFirstSearch;

/** POJO represents an edge for AJAX initialization request */
public class ClassifiedJSONEdge extends JSONEdge {
	
	private Type type;// edge classifier

	public ClassifiedJSONEdge(JSONEdge source) {
		super(source);
		this.type = null;
	}
	
	public ClassifiedJSONEdge(ClassifiedJSONEdge source) {
		super(source);
		if (source != null) { 
			this.type = source.type;
		}
	}
	
	public String toString() {// for debug only
		return super.toString() + " " + this.type;
	}
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}


	public static enum Type {
		TREE, FORWARD, BACKWARD, CROSS, CROSS_OR_FORWARD;
	}

}
