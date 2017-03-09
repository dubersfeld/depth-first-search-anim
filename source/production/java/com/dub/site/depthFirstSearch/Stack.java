package com.dub.site.depthFirstSearch;

import java.util.ArrayList;
import java.util.List;


public class Stack<T> {
	
	/** A generic stack implementation */
	private List<T> list;
	
	public Stack() {
		list = new ArrayList<T>();
	}
	
	public T pop() {
		if (!list.isEmpty()) {
			T top = list.get(list.size()-1);
		
			list.remove(list.size()-1);
		
			return top;
		} else {
			return null;
		}
	}
	
	public void push(T obj) {
		list.add(obj);
	}
	
	public T top() {
		if (!list.isEmpty()) {
			return list.get(list.size()-1);
		} else {
			return null;
		}
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}

}
