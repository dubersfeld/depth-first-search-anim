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
	
	private int N;
	
	private Integer index = 0;// main search loop current index
	private int lastFound = 0;
	
	int time = 0;
	
	
	public DFSGraph(int N) {
		super();
		System.out.println("Constructor begin");
		//edges = new ClassifiedJSONEdge[N][N]; 
		stack = new Stack<>();
		this.N = N;
	}
	
	// deep copy
	public DFSGraph(DFSGraph source) {
		this.N = source.N;
		this.stack = new Stack<>();
		 	
		for (Vertex vertex : source.getVertices()) {
			DFSVertex dfsVertex = (DFSVertex)vertex;
			DFSVertex v = new DFSVertex(dfsVertex);// deep copy
			this.getVertices().add(v);
		}
	}
	

	public Stack<Integer> getStack() {
		return stack;
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
	
	// main search method
	public void search(List<StepResult> snapshots) {
		System.out.println("search begin");
		
		index = 0;
		
		boolean fin = false;
		while (!fin) {
		
			StepResult result = searchStep();
			snapshots.add(result);
			if (result.getStatus().equals(StepResult.Status.FINISHED)) {
				fin = true;
			}
			
		}
		
		System.out.println("search completed");
	}// search
	
	
	public StepResult searchStep() {
		/** one vertex is visited at each step */
		DFSGraph snapshot = null;
		
		StepResult result = new StepResult();// empty container
		result.setStatus(StepResult.Status.STEP);// default

		
		DFSVertex u = (DFSVertex)this.vertices.get(index);
	 
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
	   
	        ((DFSVertex)this.vertices.get(first)).setParent(index);// only change here
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
	    			result.setStatus(StepResult.Status.FINISHED);
	    		}	
	    	}     
	    }
		
	    // prepare Ajax response
	    
	    snapshot = new DFSGraph(this);
	    	    
		result.setGraph(snapshot);
		
		return result;
	        
	}// searchStep
			
	
	// helper methods
		
	public Integer findNotVisited() {
		int nind = 0;
		DFSVertex v = null;
		for (nind = this.lastFound + 1; nind < N; nind++) {
			v = (DFSVertex)this.vertices.get(nind);
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
			v = (DFSVertex)this.vertices.get(to);
		
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
