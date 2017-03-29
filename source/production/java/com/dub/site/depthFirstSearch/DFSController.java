package com.dub.site.depthFirstSearch;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dub.config.annotation.WebController;

@WebController
public class DFSController {
	@Inject 
	private GraphServices graphServices;
	
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
			graph.getVertices()[i1] = v;
		}
		
		for (int i1 = 0; i1 < jsonEdges.size(); i1++) {
			JSONEdge edge = jsonEdges.get(i1);
			int from = edge.getFrom();
			int to = edge.getTo();
			DFSVertex v1 = (DFSVertex)graph.getVertices()[from];
			
			v1.getAdjacency().add(to);
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
				
		System.out.println("search sator");
		
		// snapshots for display
		List<JSONSnapshot> snapshots = new ArrayList<>();
				
		while (!graph.isFinished()) {
			System.out.println("search while begin");
			graph.searchStep();
			JSONSnapshot snapshot = graphServices.graphToJSON(graph);
			System.out.println("search while ");
			snapshots.add(snapshot);
			
		}// while
				
		dfsResponse.setSnapshots(snapshots);
		dfsResponse.setStatus(DFSResponse.Status.OK);
		
		return dfsResponse;
	}// searchGraph
	
}
