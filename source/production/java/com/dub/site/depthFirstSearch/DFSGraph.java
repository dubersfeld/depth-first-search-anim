package com.dub.site.depthFirstSearch;

import java.io.Serializable;
import java.util.List;

/** DFSGraph has Vertices and Adjacency Lists */
public class DFSGraph extends Graph implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Stack<Integer> stack;
	
	private Integer index = 0;// main search loop current index
	private int lastFound = 0;
	private boolean finished = false;
	
	int time = 0;
	
	
	public DFSGraph(int N) {
		super(N);
		System.out.println("Constructor begin");
		stack = new Stack<>();
		finished = false;
	}
	
	public Stack<Integer> getStack() {
		return stack;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setStack(Stack<Integer> stack) {
		this.stack = stack;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public void searchStep() {
		/** one vertex is visited at each step */
		
		DFSVertex u = (DFSVertex)this.vertices[index];
	 
		// begin with coloring
		if (u.getColor().equals(DFSVertex.Color.BLACK)) {// vertex has just been discovered
			u.setColor(DFSVertex.Color.GREEN);// visited
			time++;
			u.setD(time);
			System.out.println("has parent " + (u.getParent() != null));
		}
			
		List<Integer> conn = u.getAdjacency();// present vertex successors 
		
	    Integer first = null;// first successor index if present
	    boolean finish = false;
	    
	    if (conn.isEmpty() || (first = this.findNotVisitedAndMark(conn, index)) == null) {
	    	finish = true;
	    }
	      
	    if (!finish) {// prepare to descend
	   
	        ((DFSVertex)vertices[first]).setParent(index);// only change here
	        stack.push(index);// push present vertex before descending 	
	        index = first;// save u for the next step
	        
	    } else {// finish present vertex

	    	u.setColor(DFSVertex.Color.BLUE);
	    	time++;
	    	u.setF(time);
	    	
	    	if (!stack.isEmpty()) {
	    		index = stack.pop();
	
	    	} else {
	    		index = this.findNotVisited();// can be null
	    	
	    		if (index == null) {
	    			finished = true;
	    		}	
	    	}     
	    }
		
	}// searchStep
			
	
	// helper methods
		
	public Integer findNotVisited() {
		int nind = 0;
		DFSVertex v = null;
		for (nind = this.lastFound + 1; nind < N; nind++) {
			v = (DFSVertex)vertices[nind];
			if (v.getColor().equals(DFSVertex.Color.BLACK)) {
				break;
			}
		}
	
		if (nind < N) {
			this.lastFound = nind;// save as initial value for next lookup 

			return nind;
		} else {
			return null;
		}
				
	}// findNotVisited
	
		
	public Integer findNotVisitedAndMark(List<Integer> list, int from) {

		// successor look up		
		int nind = 0;
		DFSVertex v = null;
		
		for (nind = 0; nind < list.size(); nind++) {
			int to = list.get(nind);
			v = (DFSVertex)vertices[to];
		
			if (v.getColor().equals(DFSVertex.Color.BLACK)) {
				break;
			}
		}
		if (nind < list.size()) {
			return list.get(nind);
		} else {
			return null;
		}
		
	}// findNotVisited
	
}
