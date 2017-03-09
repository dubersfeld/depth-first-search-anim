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
	private ClassifiedJSONEdge[][] edges;
	
	private int N;
	
	private Integer index = 0;// main search loop current index
	private int lastFound = 0;
	
	int time = 0;
	
	
	public DFSGraph(int N) {
		super();
		System.out.println("Constructor begin");
		edges = new ClassifiedJSONEdge[N][N]; 
		stack = new Stack<>();
		this.N = N;
		for (int i1  = 0; i1 < N; i1++) {
			for (int i2 = 0; i2 < N; i2++) {
				edges[i1][i2] = null;
			}
		}
	}
	
	// deep copy
	public DFSGraph(DFSGraph source) {
		this.N = source.N;
		this.stack = new Stack<>();
		this.edges = new ClassifiedJSONEdge[source.N][source.N]; 
			
		int k = 0;
		for (Vertex vertex : source.getVertices()) {
			DFSVertex dfsVertex = (DFSVertex)vertex;
			DFSVertex v = new DFSVertex(dfsVertex);// deep copy
			this.getVertices().add(v);
			k++;
		}

		if (source.edges[0][0] != null) {
			this.edges[0][0] 
					= new ClassifiedJSONEdge(source.edges[0][0]);
		}
		
		for (int i1 = 0; i1 < source.N; i1++) {
			for (int i2 = 0; i2 < source.N; i2++) {	
				if (source.edges[i1][i2] != null) {
				
					this.edges[i1][i2] 
							= new ClassifiedJSONEdge(source.edges[i1][i2]);
				}
			}// for
		}// for
		
	}
	
	
	public ClassifiedJSONEdge[][] getEdges() {
		return edges;
	}

	public void setEdges(ClassifiedJSONEdge[][] edges) {
		this.edges = edges;
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

	/*
	public DFSGraph clone() {// deep copy
		
		DFSGraph graph = new DFSGraph(this.vertices.size());
		
		for (Vertex vertex : this.getVertices()) {
			DFSVertex dfsVertex = (DFSVertex)vertex;
			DFSVertex v = new DFSVertex(dfsVertex);
			graph.getVertices().add(v);
		}
		
		for (int i1 = 0; i1 < this.getVertices().size(); i1++) {
			for (int i2 = 0; i2 < this.getVertices().size(); i2++) {
				if (this.edges[i1][i2] != null) {
					graph.edges[i1][i2] = new ClassifiedJSONEdge(
															this.edges[i1][i2]);
				}
			}
		}
		
		return graph;
	}
	*/
	
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
			if (u.getParent() != null) {
				edges[u.getParent()][index].setType(ClassifiedJSONEdge.Type.TREE);
				
			}
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
	    // need to clone the array edges too
	    
	    snapshot = new DFSGraph(this);
	    	    
		result.setGraph(snapshot);
		
		return result;
	        
	}// searchStep
			
	
	public void displayEdges() {
		for (int i1 = 0; i1 < this.N; i1++) {
			for (int i2 = 0; i2 < this.N; i2++) {
				if (edges[i1][i2] != null) {
					System.out.println(edges[i1][i2]);
				}
			}
		}
	}
	
	
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
		
			if (edges[from][to].getType() == null) {
			
				if (v.getColor().equals(DFSVertex.Color.GREEN)) {
				
					edges[from][to]
							.setType(ClassifiedJSONEdge.Type.BACKWARD);
				} else if (v.getColor().equals(DFSVertex.Color.BLUE)){
					if (((DFSVertex)this.vertices.get(from)).getD() < ((DFSVertex)this.vertices.get(nind)).getD()) {
						edges[from][to]
								.setType(ClassifiedJSONEdge.Type.FORWARD);
					} else {
						edges[from][to]
							.setType(ClassifiedJSONEdge.Type.CROSS);
					}
				}
			}
			
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
