package com.dub.site.depthFirstSearch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dub.config.annotation.WebController;

@WebController
public class DFSController {
	
	/** Initialize graph for both automatic and stepwise search */
	@RequestMapping(value="initGraph")
	@ResponseBody
	public DFSResponse initGraph(@RequestBody GraphInitRequest message, 
				HttpServletRequest request) 
	{	
		System.out.println("controller: initGraph begin");
		List<JSONEdge> jsonEdges = message.getJsonEdges();
		List<JSONVertex> jsonVertices = message.getJsonVertices();
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("graph") != null) {
			session.removeAttribute("graph");
		}
	
		DFSGraph graph = new DFSGraph(jsonVertices.size());
			
		for (int i1 = 0; i1 < jsonVertices.size(); i1++) {
			DFSVertex v = new DFSVertex();
			v.setName(jsonVertices.get(i1).getName());
			v.setColor(DFSVertex.Color.BLACK);
			graph.getVertices().add(v);
		}
		
		for (int i1 = 0; i1 < jsonEdges.size(); i1++) {
			JSONEdge edge = jsonEdges.get(i1);
			int from = edge.getFrom();
			int to = edge.getTo();
			DFSVertex v1 = (DFSVertex)graph.getVertices().get(from);
			
			v1.getAdjacency().add(to);
			// helper adjacency matrix
			graph.getEdges()[from][to] = new ClassifiedJSONEdge(edge);
		}
		

		session.setAttribute("graph", graph);
			
		DFSResponse dfsResponse = new DFSResponse();
		dfsResponse.setStatus(DFSResponse.Status.OK);
		
		// here the graph is ready for the search loop
		
		System.out.println("controller: initGraph completed");
			
		return dfsResponse;
	}
	
	
	@RequestMapping(value="searchGraph")
	@ResponseBody
	public DFSResponse searchGraph(@RequestBody SearchRequest message, 
											HttpServletRequest request) 
	{	
		DFSResponse dfsResponse = new DFSResponse();
				
		HttpSession session = request.getSession();
		DFSGraph graph = (DFSGraph)session.getAttribute("graph");
				
		// snapshots for display
		List<StepResult> snapshots = new ArrayList<>();
			
		graph.search(snapshots);// search loop affects a List of container
		
		System.out.println("after search loop: " + snapshots.size());
				
		dfsResponse.setStatus(DFSResponse.Status.OK);
		dfsResponse.setSnapshots(snapshots);
			
		System.out.println("controller searchGraph return");
		return dfsResponse;
	}// searchGraph
	

}
