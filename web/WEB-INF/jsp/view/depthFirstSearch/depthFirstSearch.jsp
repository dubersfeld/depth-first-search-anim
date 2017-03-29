<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    												pageEncoding="UTF-8" %>
<!doctype html>

<html lang="en">
<head>
<meta charset="utf-8">
<title>Depth First Search</title>

<link rel="stylesheet"
              href="<c:url value="/resources/stylesheet/bfsDemo.css" />" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>


<script>

"use strict";

/*
Here I draw an oriented graph  
 */

 var Debugger = function() { };// create  object
	Debugger.log = function(message) {
  	try {
    	console.log(message);
  	} catch(exception) {
    	return;
  	}
}


function canvasSupport() {
  	return !!document.createElement('canvas').getContext;
} 

function canvasApp() {

 	function Vertex(name) {
    	this.mName = name;
    	this.mParent = null;
    	this.mD = null;
    	this.mF = null;
    	this.mColor = "black";    
  	}// Vertex

  	// Vertex augmentation
  	function DisplayVertex(name) {
    	Vertex.call(this, name);
  	}// DisplayVertex

  	DisplayVertex.prototype = new Vertex();
  	DisplayVertex.prototype.mRadius = 20;
  	DisplayVertex.prototype.xPos = 0;
  	DisplayVertex.prototype.yPos = 0;
  	DisplayVertex.prototype.yConnU = 0;
  	DisplayVertex.prototype.yConnB = 0;
  	DisplayVertex.prototype.xConnL = 0;
  	DisplayVertex.prototype.xConnR = 0;
  	// 4 connection points, bottom, up, left, right
  	DisplayVertex.prototype.mNx = 0;
  	DisplayVertex.prototype.mNy = 0;
 
  	DisplayVertex.prototype.updateGeometry = function() {  
    	this.yConnU = this.yPos - this.mRadius;
    	this.yConnB = this.yPos + this.mRadius;
    	this.xConnL = this.xPos - this.mRadius;
    	this.xConnR = this.xPos + this.mRadius;
  	};

  	function Graph(N) {// A Graph contains a vector of N vertices
    	this.mV = [];// all vertices
    	this.mAdj = [];// all vertices adjacent to a given vertex
    	this.init = function() {
      	for (var i = 0; i < N; i++) {
        	this.mAdj.push(new Array());
      	}
    	}; 
    	// array of arrays of arrays format [[v,v,v],[]...[]]
    	// v = vertex number 
    	this.init();
  	}// Graph


  	// get canvas context
  	if (!canvasSupport()) {
    	alert("canvas not supported");
    	return;
  	} else {
    	var theCanvas = document.getElementById("canvas");
    	var context = theCanvas.getContext("2d");
  	}// if

  	//var sourcename;
  	
  	var graphReady = false;
  	
  	//var classifiedEdges = [];
  
  	var xMin = 0;
  	var yMin = 0;
  	var xMax = theCanvas.width;
  	var yMax = theCanvas.height; 

  	var xPos = [50, 150, 250, 350, 450, 550, 650];
 
  	var yPos = [100, 200, 300, 400, 500];

  	var names = ["a", "b", "c", "d", "e", "f" ,"g"];
 
  	var delay = 1000;// for animation only

  	var N = 35;// number of vertices

  	var Nedges = 50;// number of edges

  	var graph = new Graph(N);// empty graph
  	var queue = [];// use push and shift for a queue
  	var results = [];// holds the search result collection

  	var animIndex;
  
  	var src = null;// source

  	function setTextStyle() {
    	context.fillStyle    = '#000000';
    	context.font         = '12px _sans';
  	}


  	function fillBackground() {
    	// draw background
    	context.fillStyle = '#ffffff';
    	context.fillRect(xMin, yMin, xMax, yMax);    
  	}

  	function drawVertex(vertex) {
    	context.beginPath();
    	context.strokeStyle = vertex.mColor;
    	context.lineWidth = 2;
    	context.arc(vertex.xPos, vertex.yPos, vertex.mRadius, (Math.PI/180)*0, (Math.PI/180)*360, true); // draw full circle
    	context.stroke();
    	context.closePath();
    	var roff = vertex.mRadius + 2;
    	var timestamp = "";
    	if (vertex.mD != null) {
    	    timestamp += vertex.mD + "/"; 
    	}
    	if (vertex.mF != null && vertex.mF != 0) {
    	    timestamp += vertex.mF; 
    	}
        context.fillText(vertex.mName, vertex.xPos, vertex.yPos);    
        context.fillText(timestamp, vertex.xPos + roff, vertex.yPos - roff);
    	
	}


    function classifyEdge(u, v) {
    	if (u == graph.mV[v.mParent]) {
    		return "T";
    	} else if ((u.mD < v.mD) && (v.mD < v.mF) && (v.mF < u.mF)) {
    		return "F";
    	} else if ((v.mD <= u.mD) && (u.mD < u.mF) & (u.mF <= v.mF)) {
    		return "B";
    	} else {
    		return "C";
    	}	
    }// classifyEdge

    function drawLine(a, b) {// a and b are points 
    	  
    	var xa = a[0];
    	var ya = a[1];
    	var xb = b[0];
    	var yb = b[1];
   
    	// main line
    	context.beginPath();
    	context.moveTo(xa, ya);
    	context.lineTo(xb, yb);
    	context.stroke();
    	context.closePath();

    	// get unity vector from a to b
    	var dx = xa - xb;
    	var dy = ya - yb;

    	var l = Math.sqrt(dx * dx + dy * dy);
    	var u = [dx/l, dy/l];

    	var angle = (Math.PI / 180) * 15;

    	var a1x = Math.cos(angle) * u[0] - Math.sin(angle) * u[1];
    	var a1y = Math.sin(angle) * u[0] + Math.cos(angle) * u[1];
    	var a2x = Math.cos(angle) * u[0] + Math.sin(angle) * u[1];
    	var a2y = -Math.sin(angle) * u[0] + Math.cos(angle) * u[1];
    	var a1 = [xb + a1x*10, yb + a1y*10];
    	var a2 = [xb + a2x*10, yb + a2y*10];

    	context.beginPath();
    	context.moveTo(xb, yb);
    	context.lineTo(a1[0], a1[1]);
    	context.stroke();
    	context.moveTo(xb, yb);
    	context.lineTo(a2[0], a2[1]);
    	context.stroke();
    	context.closePath();   
  	}// drawLine
    
    function drawArc(a, b) {// a and b points 
    	// drawArc from a to b
    	var xa = a[0];
    	var ya = a[1];
    	var xb = b[0];
    	var yb = b[1];

    	// get center coordinates
    	var phi = (Math.PI/180)*30;
   
    	var xm = (xa + xb) / 2; 
    	var ym = (ya + yb) / 2;
    	var xc = xm + (yb - ya)/(2*Math.tan(phi));
    	var yc = ym + (xa - xb)/(2*Math.tan(phi));

    	// get radius
    	var radius = Math.sqrt( (xa - xb)*(xa - xb)+(ya - yb)*(ya - yb) ) / (2*Math.sin(phi));
    	var phia = Math.atan((yc - ya) / (xa - xc));
    	if (xa < xc) {
      		phia += Math.PI;
    	}
    	var phib = phia + 2*phi;
    	//var phib = phia + phi;
    	context.beginPath();
    	context.arc(xc, yc, radius, -phia, -phib, true);  
    	context.stroke();
    	context.closePath();
    	context.textBaseline = "middle";
    	context.textAlign = "center"; 

    	// get tangent vector at B
    	var xt = Math.sin(phib);
    	var yt = Math.cos(phib);
    	var u = [xt, yt];
    	var angle = (Math.PI / 180) * 15;

    	var a1x = Math.cos(angle) * u[0] - Math.sin(angle) * u[1];
    	var a1y = Math.sin(angle) * u[0] + Math.cos(angle) * u[1];
    	var a2x = Math.cos(angle) * u[0] + Math.sin(angle) * u[1];
    	var a2y = -Math.sin(angle) * u[0] + Math.cos(angle) * u[1];
    	var a1 = [xb + a1x*10, yb + a1y*10];
    	var a2 = [xb + a2x*10, yb + a2y*10];

    	context.beginPath();
    	context.moveTo(xb, yb);
    	context.lineTo(a1[0], a1[1]);
    	context.stroke();
    	context.moveTo(xb, yb);
    	context.lineTo(a2[0], a2[1]);
    	context.stroke();
    	context.closePath();   
    	
  	}// drawArc

    
    
    function drawConnect(v1, v2, arc) { 
    	 
    	if (classifyEdge(v1, v2) == "T") {
    		context.strokeStyle = "blue";
    	} else if (classifyEdge(v1, v2) == "F") {
    		context.strokeStyle = "green";
    	} else if (classifyEdge(v1, v2) == "B") {
    		context.strokeStyle = "red";
    	} else {
    		context.strokeStyle = "black";
    	}
    		
        context.lineWidth = 2;
        // discuss according to geometry
        var xa, ya, xb, yb;

        // respect direction: a always v1, b always v2
        if (v1.mNx == v2.mNx) {
          xa = v1.xPos;
          xb = v1.xPos;
          if (v1.mNy < v2.mNy) {    
            ya = v1.yConnB;       
            yb = v2.yConnU;
          } else {
            ya = v1.yConnU;       
            yb = v2.yConnB;
          }
        } else if (v1.mNy == v2.mNy) {
          ya = v1.yPos;
          yb = v1.yPos; 
          if (v1.mNx < v2.mNx) {
            xa = v1.xConnR; 
            xb = v2.xConnL; 
          } else {
            xa = v1.xConnL;         
            xb = v2.xConnR; 
          }         
        } else {
          if (v1.mNx < v2.mNx) {
            xa = v1.xConnR;
            xb = v2.xConnL;
            ya = v1.yPos;
            yb = v2.yPos; 
          } else {
            xa = v1.xConnL;
            xb = v2.xConnR;
            ya = v1.yPos;
            yb = v2.yPos; 
          }      
        }
 
        if (arc) {
      		drawArc([xa, ya], [xb, yb]);
    	} else {
      		drawLine([xa, ya], [xb, yb]);
    	}
      
    }// drawConnect
    
   
 	var vertex;
 
 	function build() {
 
 		console.log("build begin");
 		
    	setTextStyle();

    	context.textBaseline = "middle";
    	context.textAlign = "center";

   		for (var i = 0; i < 5; i++) {
    		for (var j = 0; j < 7; j++) {
        		vertex = new DisplayVertex(names[j] + i);
        		vertex.mNx = j;
        		vertex.mNy = i;
        		vertex.xPos = xPos[j];
        		vertex.yPos = yPos[i];
        		vertex.updateGeometry();        
        		graph.mV.push(vertex);
        		drawVertex(vertex);         
    		}
  		}
  
    	randomize();
    	
    	console.log("build completed");
  
	}// build
	
	function drawEdge(edge) {
		var v1 = graph.mV[edge.from];
		var v2 = graph.mV[edge.to];
		var edgeType = edge.type;
		
		drawConnect(v1, v2, edgeType);
	}
	
	function redraw() {
    	// only use mAdj for drawing connections
    	// clear canvas
    	fillBackground();

    	setTextStyle();

    	context.textBaseline = "middle";
    	context.textAlign = "center";

    	var N = graph.mV.length;

    	// draw all vertices
    	for (var i = 0; i < N; i++) {
      		drawVertex(graph.mV[i]);
    	}
    	
    	var arc = false;
    	// draw all connections
    	for (var i = 0; i < N; i++) {
      		var conn = graph.mAdj[i]; // all vertices connected to vertex i
      		for (var k = 0; k < conn.length; k++) {
      			arc = (graph.mAdj[conn[k]].indexOf(i) >= 0) ? true : false;
        		drawConnect(graph.mV[i], graph.mV[conn[k]], arc);        
      		}
    	}
    	
 	}// redraw
 	
 	
  	function randomize() {

    	/* adding random edges, only Nedges (sparse)
       	allow connection only in cases below:
       mNx1 = mNx2, |mNy1 - mNy2| == 1
       mNy1 = mNy2, |mNx1 - mNx2| == 1
       |mNx1 - mNx2| == 1 and |mNy1 - mNy2| == 1   
    	*/
  
    	var edges = 0;
    	var count = 0;

    	var check = new Array(35);
    	for (var i = 0; i < 35; i++) {
      		check[i] = new Array(35);
    	}

    	for (var i = 0; i < 35; i++) {
      		for (var j = 0; j < 35; j++) {
        		check[i][j] = 0;
      		}
    	}

    	var index1, index2;

    	// reset all vertices
    	for (var i = 0; i < graph.mV.length; i++) {
      		graph.mV[i].mColor = "black";
      		graph.mV[i].mParent = null;
      		graph.mV[i].mD = null;
      		graph.mV[i].mF = null;
    	}

    	// remove all existing edges
    	for (var i = 0; i < graph.mAdj.length; i++) {
      		graph.mAdj[i] = [];
    	}

    	while (edges < Nedges) {
      		// select 2 random indexes
      		index1 = Math.floor(Math.random() * 35);// range
      		index2 = index1;
      		while (index2 == index1) {
        		index2 = Math.floor(Math.random() * 35);// range
      		}
      		var nX1 = graph.mV[index1].mNx;
      		var nY1 = graph.mV[index1].mNy;
      		var nX2 = graph.mV[index2].mNx;
      		var nY2 = graph.mV[index2].mNy;
      		if ((Math.abs(nX1-nX2) <= 1) && (Math.abs(nY1-nY2) <= 1) ) {// allow edge           
        		if (check[index1][index2] == 0) {// oriented graph      			
          			graph.mAdj[index1].push(index2);
          			check[index1][index2] = 1;
          			edges++;      
        		}        
      		}
    	}// while
    	 		

    	// for debugging only 
    	/*
  	  graph.mAdj[0] = [7,1,8];
  	    graph.mAdj[1] = [0,2];
  	    graph.mAdj[2] = [3];
  	    graph.mAdj[3] = [11];
  	    graph.mAdj[4] = [3];
  	    graph.mAdj[5] = [6];
  	    graph.mAdj[6] = [13];
  	    graph.mAdj[7] = [0,1];
  	    graph.mAdj[8] = [2,16];
  	    graph.mAdj[9] = [16,10];
  	    graph.mAdj[10] = [2,4];
  	    graph.mAdj[11] = [];
  	    graph.mAdj[12] = [11,18];
  	    graph.mAdj[13] = [19];
  	    graph.mAdj[14] = [15,21,22];
  	    graph.mAdj[15] = [16,21];
  	    graph.mAdj[16] = [10];
  	    graph.mAdj[17] = [10,11,9,23];
  	    graph.mAdj[18] = [12,26,19];
  	    graph.mAdj[19] = [11,26];
  	    graph.mAdj[20] = [27];
  	    graph.mAdj[21] = [15];
  	    graph.mAdj[22] = [29];
  	    graph.mAdj[23] = [30,29];
  	    graph.mAdj[24] = [30,17];
  	    graph.mAdj[25] = [];
  	    graph.mAdj[26] = [];
  	    graph.mAdj[27] = [34,20];
  	    graph.mAdj[28] = [22,21];
  	    graph.mAdj[29] = [];
  	    graph.mAdj[30] = [24];
  	    graph.mAdj[31] = [];
  	    graph.mAdj[32] = [];
  	    graph.mAdj[33] = [32];
  	    graph.mAdj[34] = [33];
  	    	*/
  
    	redraw();
 
		$('#initColl').find(':submit')[0].disabled = false;
	
		$('#status').text('Ready to search');
 	}// randomize

  	function animSpeedChanged(e) {
	  	console.log("animSpeedChanged");
    	delay = 1e4 / e.target.value;
  	}
   
	function animStep() {
	  	if (animIndex < results.length) {
	  		var stepVertices = results[animIndex]["vertices"];
	  		
	  		for (var i = 0; i < stepVertices.length; i++) {
	     
	      		graph.mV[i].mColor = stepVertices[i].color;// update graph
				graph.mV[i].mD = stepVertices[i].d;
				graph.mV[i].mF = stepVertices[i].f;
				graph.mV[i].mParent = stepVertices[i].parent;
	  		}// for
	  		
	  		redraw();// actual redraw	  		
	  		animIndex++;
	  		setTimeout(function() { animStep(); }, delay);
	  	} else {
	  		// animation completed
	  		stepVertices = [];// clear all
			console.log("animation completed");
			$('#status').text('Animation completed');
	  	}	 
  	}
  
  	function anim() {
  		$('#animation').find(':submit')[0].disabled = true;
  		$('#initColl').find(':submit')[0].disabled = true;
  		$('#searchColl').find(':submit')[0].disabled = true;
  		$('#status').text('Animation started');
	  	animIndex = 0;
	  	animStep();// start actual animation
  	}
 	
	function searchColl() {
		console.log("searchColl begin");
		$('#animation').find(':submit')[0].disabled = true;
		
		
		// now start actual search
		message = {"type": "COLLECTION"};
		
	 	$.ajax({
			type : "POST",
			contentType : "application/json",
			url : '<c:url value="/searchGraph" />',
			data : JSON.stringify(message),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SEARCH SUCCESSFUL");
								
				// this is the actual vertices list			
				results = data["snapshots"];// make it visible
				
				// enable animation but don't start it yet
				$('#animation').find(':submit')[0].disabled = false;
				$('#searchColl').find(':submit')[0].disabled = true;
				$('#status').text('Ready for animation');
			},
			
			error : function(e) {
				console.log("ERROR: ", e);
			},
			done : function(e) {
				console.log("DONE");
			}
		});
		
		
		console.log("searchColl completed");
	}// searchColl
  	
  	function initColl() {
  		console.log("initColl begin");
  		graphReady = false;
		$('#searchColl').find(':submit')[0].disabled = true;
		$('#animation').find(':submit')[0].disabled = true;
		
  		var message;
  	    
    	var edgeArray = [];
    	var vertexArray = [];
    
    	var count = 0;
    	var edges;
    	var vertices;
  		
    	for (var i1 = 0; i1 < 35; i1++) {// for each vertex
  			vertexArray.push({"name":graph.mV[i1].mName});
  			for (var i2 = 0; i2 < graph.mAdj[i1].length; i2++) {// for each adjacent vertex
  				edgeArray.push({"from":i1, "to":graph.mAdj[i1][i2]});
  			}// i2
    	}// i1
  		   
    	edges = {"jsonEdges":edgeArray};
    	vertices = {"jsonVertices":vertexArray};
    	message = {"jsonEdges":edgeArray, "jsonVertices":vertexArray};
    	
    	$.ajax({
			type : "POST",
			contentType : "application/json",
			url : '<c:url value="/initGraph" />',
			data : JSON.stringify(message),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("INITIALIZATION SUCCESSFUL");
				graphReady = true;
				$('#searchColl').find(':submit')[0].disabled = false;
			},
		
			error : function(e) {
				console.log("ERROR: ", e);
			},
			done : function(e) {
				console.log("DONE");
			}
		});
   
		console.log("initColl completed");
  		
	}// searchInit

  
	build(graph, Nedges);
  
	var message;
  
  	var edgeArray = [];
  	var vertexArray = [];
  
  	var count = 0;
  	var edges;
  	var vertices;
		
  	for (var j = 0; j < 35; j++) {// for each vertex
		vertexArray.push({"name":graph.mV[j].mName});
		for (var i = 0; i < graph.mAdj[j].length; i++) {// for each adjacent vertex		
			edgeArray.push({"from":j, "to":graph.mAdj[j][i]});
		}// i
  	}// j

  	 
  	$("#animation").submit(function(event) { anim(); return false; });
  	$("#initColl").submit(function(event) { initColl(graph); return false; });
	$("#searchColl").submit(function(event) { searchColl(graph); return false; });
  	$("#initelem").submit(function(event) { randomize(graph, Nedges); return false; }); 
  	$("#animspeed").change(function(event) { animSpeedChanged(event); return false; });
 
  	$('#initelem').find(':submit')[0].disabled = false;
  	$('#initColl').find(':submit')[0].disabled = false;
  	$('#animation').find(':submit')[0].disabled = true;
	$('#searchColl').find(':submit')[0].disabled = true;
	
}// canvasApp

$(document).ready(canvasApp);


</script>
</head>

<body>

<nav>
<br>
<br><br>
</nav>

<header id="intro">
<h1>Depth First Search Demonstration</h1>
<p>I present here a Java based demonstration of the Depth First Search algorithm.<br>
I follow closely the approach of Cormen in his classical textbook.</p>
<h2>Explanations</h2>
<p>The graph edges are randomly initialized. The search always start from the same vertex named a0.
Then a single request is sent to the server that sends back a response that contains the collection of all successive search step results.<br/>
This collection is then used for the display animation.
All newly discovered vertices are colored green. When the search is completed all vertices are blue.<br/>
The discovery time and finishing time d and f are are also displayed as d/f.<br>
An edge that belongs to a tree is colored blue.<br/>
A forward edge is colored green.<br/>
A backward edge is colored red.<br/>
All remaining edges (cross edges) are left black.<br/>
The animation speed can be changed at any time using the range control.<br/>
Notice: for a better display the display colors I use for vertices are not the colors used in the algorithm description.<br/>
</p>

<table>
<thead>
<tr><th>Original color</th><th>My color</th></tr>
</thead>
<tbody>
<tr><td>White</td><td>Black</td></tr>
<tr><td>Gray</td><td>Green</td></tr>
<tr><td>Black</td><td>Blue</td></tr>
</tbody>
</table>  

</header>

<div id="display">
  <canvas id="canvas" width="700" height="600">
    Your browser does not support HTML 5 Canvas
  </canvas>
<footer>
<p>Dominique Ubersfeld, Cachan, France</p>
</footer> 
 
</div>

<div id="controls">
  <div id="sourceselect">
    <h3>Animation mode</h3>
      <p>Click to start the animation:</p>
      <form name="animation" id="animation">
        <input type="submit" name="source-btn" value="Start">
      </form>
      <form name="initColl" id="initColl">
        Init graph: <!--input type="text" name="sourcename" size="2"-->
        <input type="submit" name="source-btn" value="Init">
      </form>
      <form name="searchColl" id="searchColl">
        Start search: <!--input type="text" name="sourcename" size="2"-->
        <input type="submit" name="source-btn" value="Search">
      </form>
      
      <p id="found"></p>
    </div>
    <div id="randomize">
      <p>Click here to randomize the graph edges</p>
      <form name="initialize" id="initelem">
        <input type="submit" name="randomize-btn" value="Randomize">
      </form>
    </div>

    <div id="animspeed">
      <label for="animSpeed">Animation speed</label>
      <input type="range" id="animSpeed" min="5" max="100" step="5" value="20">
    </div>
    <div id="msg">
    <p id="status"></p>
    </div> 

</div>

</body>

</html>