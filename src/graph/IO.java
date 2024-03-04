package graph;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import parameters.Constants;
import parameters.Global;

public class IO {
	private static class IDTransformer implements Transformer<UIAgent,String> {
		public String transform(UIAgent v) {
			return Integer.toString(v.getId());
		}
	}
	
	private static class XTransformer
	implements Transformer<UIAgent,String> {
		private Layout<UIAgent,MyEdge> layout;
		
		public XTransformer(Layout<UIAgent,MyEdge> layout) {
			this.layout = layout;
		}
		
		public String transform(UIAgent v) {
			return Double.toString(layout.transform(v).getX());
		}
	}
	
	private static class YTransformer
	implements Transformer<UIAgent,String> {
		private Layout<UIAgent,MyEdge> layout;
		
		public YTransformer(Layout<UIAgent,MyEdge> layout) {
			this.layout = layout;
		}
		
		public String transform(UIAgent v) {
			return Double.toString(layout.transform(v).getY());
		}
	}
	
	private static class BTransformer
	implements Transformer<UIAgent,String> {
		public String transform(UIAgent v) {
			return v.getOCF().toStringForXMLSave();
		}
	}
	
	private static class RPTransformer implements Transformer<Hypergraph<UIAgent,MyEdge>,String> {
		public String transform(Hypergraph<UIAgent, MyEdge> g) {
			return ((AgentGraph) g).getGlobalRevisionPolicy().toString();
		}
	}
	
	// this class actually does not consider the graph, but the global parameter Global.nbInterpretations
	private static class nbIntTransformer implements Transformer<Hypergraph<UIAgent,MyEdge>,String> {
		public String transform(Hypergraph<UIAgent, MyEdge> g) {
			return String.valueOf(Global.nbInterpretations);
		}
	}
	
	private static class GraphTransformer implements Transformer<GraphMetadata,AgentGraph> {
		public AgentGraph transform(GraphMetadata metadata) {
			Global.nbInterpretations = Integer.parseInt(metadata.getProperty("nb_inter"));
			AgentGraph result = new AgentGraph(); 
			result.setGlobalRevisionPolicyFromName(metadata.getProperty("rp"));
			
			return result;
		}
	}

	private static class VertexTransformer
	implements Transformer<NodeMetadata,UIAgent> {
		public UIAgent transform(NodeMetadata metadata) {
			UIAgent v = AgentGraph.vertexFactory.create();
			v.setId(Integer.parseInt(metadata.getProperty("id_nb")));
			v.x = Double.parseDouble(metadata.getProperty("x"));
			v.y = Double.parseDouble(metadata.getProperty("y"));
			v.getOCF().setOCFFromStringInXMLFile(metadata.getProperty("b"));
			
			return v;
		}
	}
	
	private static class EdgeTransformer
	implements Transformer<EdgeMetadata,MyEdge> {
		public MyEdge transform(EdgeMetadata metadata) {
			MyEdge e = AgentGraph.edgeFactory.create();
			return e;
		}
	}
	
	private static class HyperEdgeTransformer
	implements Transformer<HyperEdgeMetadata,MyEdge> {
		public MyEdge transform(HyperEdgeMetadata metadata) {
			MyEdge e = AgentGraph.edgeFactory.create();
			return e;
		}
	}
	
	private static class PointTransformer
	implements Transformer<UIAgent,Point2D> {
		public Point2D transform(UIAgent v) {
			Point2D p = new Point2D.Double(v.x, v.y);
			return p;
		}
	}
	
	public static void write(final Layout<UIAgent,MyEdge> layout, final String filename) {
		try {
			GraphMLWriter<UIAgent,MyEdge> w = new GraphMLWriter<UIAgent,MyEdge>();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			w.addVertexData("id_nb", "id number", "0", new IDTransformer());
			w.addVertexData("x", "x_position", "0", new XTransformer(layout));
			w.addVertexData("y", "y_position", "0", new YTransformer(layout));
			w.addVertexData("b", "belief (represented as an OCF)", "0", new BTransformer());
			w.addGraphData("nb_inter", "default number of interpretations (same for all agents)", String.valueOf(Constants.DEFAULT_NB_INTER), new nbIntTransformer());
			w.addGraphData("rp", "revision policy (same for all agents)", Global.defaultRevisionPolicy.toString(), new RPTransformer());
			w.save(layout.getGraph(), out);
			
			for (UIAgent ag : Global.graphLayout.getGraph().getVertices()) {
				if (ag.getId() == 0) {
					System.out.println("(saving) position of agent " + ag.getId() + ": (" + Global.graphLayout.getX(ag) + ", " + Global.graphLayout.getY(ag) + ")");
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static StaticLayout<UIAgent,MyEdge> read(final InputStream stream) {
		GraphMLReader2<AgentGraph,UIAgent,MyEdge> graphReader;
		BufferedReader r;
		
		r = new BufferedReader(new InputStreamReader(stream));
		graphReader = new GraphMLReader2<AgentGraph,UIAgent,MyEdge>
						(r, 
						new GraphTransformer(), 
						new VertexTransformer(), 
						new EdgeTransformer(), 
						new HyperEdgeTransformer());
		
		try {
			AgentGraph g = graphReader.readGraph();
			return new StaticLayout<UIAgent, MyEdge>(g, new PointTransformer());
			
		}
		catch(GraphIOException ex) {
			System.out.println("Failed attempt to read a graph.");
			return null;
		}
	}
	
	public static StaticLayout<UIAgent,MyEdge> read(final String filename) {
		GraphMLReader2<AgentGraph,UIAgent,MyEdge> graphReader;
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(filename));
			graphReader = new GraphMLReader2<AgentGraph,UIAgent,MyEdge>
						(r, 
						new GraphTransformer(),
						new VertexTransformer(),
						new EdgeTransformer(),
						new HyperEdgeTransformer());
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found.");
			return null;
		}
		try {
			AgentGraph g = graphReader.readGraph();
			return new StaticLayout<UIAgent, MyEdge>(g, new PointTransformer());
			
		}
		catch(GraphIOException ex) {
			System.out.println("Failed attempt to read a graph.");
			return null;
		}
	}
}
