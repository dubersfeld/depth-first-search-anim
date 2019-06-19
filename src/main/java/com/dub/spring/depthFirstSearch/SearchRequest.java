package com.dub.spring.depthFirstSearch;

/** used in AJAX request */
public class SearchRequest {
	private Type type;
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}


	enum Type {
		STEP, COLLECTION
	}
}
