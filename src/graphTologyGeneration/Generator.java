package graphTologyGeneration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.EvolvingGraphGenerator;
import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import graph.AgentGraph;
import graph.UIAgent;
import graph.MyEdge;

public class Generator
{
	private static Random seed = new Random();
	
	public static AgentGraph getKleinbergGraph (int nbRows, int nbCols, int clusteringExp) {
		AgentGraph result;
		
		GraphGenerator<UIAgent, MyEdge> generator = new KleinbergSmallWorldGenerator<UIAgent, MyEdge>(AgentGraph.agentGraphFactory,
				AgentGraph.vertexFactory, AgentGraph.edgeFactory, nbRows, nbCols, clusteringExp, true);
		
		
		result = (AgentGraph)generator.create();
		// IMPORTANT: reset all agent ids
		int agentId = 0;
		for (UIAgent ag : result.getVertices()) {
			ag.setId(agentId++);
		}
		
		return result;
	}
	
	public static AgentGraph getBarabasiAlbertGraph (int nbInitNodes, int nbFinalNodes, int nbEdgesToAttach) {
		AgentGraph generatedGraph, reversedGraph;
		Set<UIAgent> initAgentSet = new HashSet<UIAgent>();
		for (int i = 0; i < nbInitNodes; i++) {
			initAgentSet.add(new UIAgent(i));
		}
		
		EvolvingGraphGenerator<UIAgent, MyEdge> generator = new BarabasiAlbertGenerator<UIAgent, MyEdge>(AgentGraph.agentGraphFactory,
				AgentGraph.vertexFactory, AgentGraph.edgeFactory, nbInitNodes, nbEdgesToAttach, initAgentSet);
		
		generator.evolveGraph(nbFinalNodes - nbInitNodes);
		
		generatedGraph = (AgentGraph)generator.create();
		// IMPORTANT: reset all agent ids
		int agentId = 0;
		for (UIAgent ag : generatedGraph.getVertices()) {
			ag.setId(agentId++);
		}
		
		reversedGraph = new AgentGraph();
		for (UIAgent ag : generatedGraph.getVertices()) {
			reversedGraph.addVertex(ag);
		}
		// get the symmetric graph
		MyEdge edge;
		for (UIAgent ag1 : reversedGraph.getVertices()) {
			for (UIAgent ag2 : reversedGraph.getVertices()) {
				edge = generatedGraph.findEdge(ag1, ag2);
				if (edge != null) {
					reversedGraph.addEdge(new MyEdge(), ag2, ag1);
				}
			}
		}
		
		return reversedGraph;
	}
	
	public static AgentGraph getSymmetricBarabasiAlbertScaleFree (int n) {
		AgentGraph g, g2;
		Set<UIAgent> set = new HashSet<UIAgent>();
		for (int i = 0; i < 2; i++) {
			set.add(new UIAgent(i));
		}
		
		//Factory<Graph<AgentNode, BasicEdge>> facAgentGraph = (Factory<Graph<AgentNode, BasicEdge>>)AgentGraph.agentGraphFactory;
		
		//System.out.println("ici1");
		
		int init, deg;
		if (n < 10) {
			init = 1;
			deg = 1;
		}
		else if (n < 100) {
			init = 2;
			deg = 2;
		}
		else {
			init = 3;
			deg = 2;
		}
		EvolvingGraphGenerator<UIAgent, MyEdge> generator = new BarabasiAlbertGenerator<UIAgent, MyEdge>(AgentGraph.agentGraphFactory,
				AgentGraph.vertexFactory, AgentGraph.edgeFactory, init, deg, set);
		
		//System.out.println("ici2");
		
		generator.evolveGraph(n-1-(n/10));
		
		//System.out.println("ici3");
		g = (AgentGraph)generator.create();
		//System.out.println("ici4");
		
		g2 = new AgentGraph();
		for (UIAgent ag : g.getVertices()) {
			g2.addVertex(ag);
		}
		// get the symmetric graph
		MyEdge edge;
		for (UIAgent ag1 : g2.getVertices()) {
			for (UIAgent ag2 : g2.getVertices()) {
				edge = g.findEdge(ag1, ag2);
				if (edge != null) {
					//g.removeEdge(edge);
					g2.addEdge(new MyEdge(), ag1, ag2);
					g2.addEdge(new MyEdge(), ag2, ag1);
				}
			}
		}
		
		return g2;
	}
	
	// THIS METHOD DOES NOT WORK, MUST BE REIMPLEMENTED
	public static AgentGraph getDAGGraph (int n, double k) {
		MyEdge edge;
		AgentGraph g = new AgentGraph();
		for (int i = 0 ; i < n ; ++i) {
			g.addVertex(new UIAgent(i));
		}
		for(int i = 0 ; i < n ; i++) {
			UIAgent alpha = g.getAgent(i);
			UIAgent beta;

			// agent i, lier avec tous les autres agents avec une probabilite de k 
			for(int j = 0 ; j < n ; ++j) {
				if (i != j) {
					beta = g.getAgent(j);
					if (seed.nextDouble() < k) {
						// before adding the edge, check if this does not make a cycle
						// otherwise, do not add the edge
						edge = new MyEdge();
						g.addEdge(edge, alpha, beta);
						// TODO must implement hasCycle (always returns false now)
						//if (g.hasCycle()) {
						//	g.removeEdge(edge);
						//}
					}
				}
			}
		}
		return g;
	}
	
	
	public static AgentGraph getCycleGraph(int n) {
		// k = n-1 -> Clique
		// k = 1   -> Circuit
		// Plutot 2 en fait
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i) {
			g.addVertex(new UIAgent(i));
		}
		for (int i = 0; i < n - 1; i++) {
			g.addEdge(new MyEdge(), g.getAgent(i), g.getAgent(i + 1));
		}
		if (n > 1) {
			g.addEdge(new MyEdge(), g.getAgent(n - 1), g.getAgent(0));
		}
		
		return g;
	}
	
	public static AgentGraph getSymmetricCycleGraph(int n) {
		// k = n-1 -> Clique
		// k = 1   -> Circuit
		// Plutot 2 en fait
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i) {
			g.addVertex(new UIAgent(i));
		}
		for (int i = 0; i < n - 1; i++) {
			g.addEdge(new MyEdge(), g.getAgent(i), g.getAgent(i + 1));
			g.addEdge(new MyEdge(), g.getAgent(i + 1), g.getAgent(i));
		}
		if (n > 1) {
			g.addEdge(new MyEdge(), g.getAgent(n - 1), g.getAgent(0));
			g.addEdge(new MyEdge(), g.getAgent(0), g.getAgent(n - 1));
		}
		
		return g;
	}
	
	public static AgentGraph getLineGraph(int n) {
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i) {
			g.addVertex(new UIAgent(i));
		}
		for (int i = 0; i < n - 1; i++) {
			g.addEdge(new MyEdge(), g.getAgent(i), g.getAgent(i + 1));
		}
		
		return g;
	}
	
	public static AgentGraph getSymmetricLineGraph(int n) {
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i) {
			g.addVertex(new UIAgent(i));
		}
		for (int i = 0; i < n - 1; i++) {
			g.addEdge(new MyEdge(), g.getAgent(i), g.getAgent(i + 1));
			g.addEdge(new MyEdge(), g.getAgent(i + 1), g.getAgent(i));
		}
		
		return g;
	}
	
	
	/*public static AgentGraph getKleinbergSWGraph (int row, int col, double clusteringExponent) {
		AgentGraph g = (AgentGraph)new KleinbergSmallWorldGenerator<AgentNode, BasicEdge>(AgentGraph.agentGraphFactory, AgentGraph.vertexFactory, AgentGraph.edgeFactory,
				row, col, clusteringExponent).create(); 
		
		return g;
	}*/
	
	public static AgentGraph getRegularGraph(int n, int k) {
		AgentGraph g = new AgentGraph();
		
		for(int i = 0 ; i < n ; i++) {
			g.addVertex(new UIAgent(i));
		}
		
		// n * k must be even
		if ((n * k) % 2 != 0) {
			k += 1;
		}
		
		
		
		return g;
		
		// k = n-1 -> Clique
		// k = 1   -> Circuit
		// Plutot 2 en fait
		/*AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i)
		{
			g.addVertex(new AgentNode(i));
		}
		for(int i = 0 ; i < n ; i++)
		{
			AgentNode alpha = g.getAgent(i);
			AgentNode beta;
			AgentNode gamma;
			
			// agent i, � lier avec les k/2 agents suivants (en cyclant) (k pair)
			for(int j = 1 ; j <= k/2 ; j++)
			{
				beta = g.getAgent((i + j)%n);
				g.addEdge(new BasicEdge(), alpha, beta);
			}
			
			// k impair, ajouter l'axe si n pair (sinon impossible)
			if(k%2 == 1 && n%2 == 0 && i < n/2)
			{
				gamma = g.getAgent(i + n/2);
				g.addEdge(new BasicEdge(), alpha, gamma);
			}
		}
		return g;*/
	}
	
	
	/*public static AgentGraph getSmallWorldWatts (int n, boolean parallel) {
        new WattsBetaSmallWorldGenerator();
    }*/
	
	public static AgentGraph getRegularTree(int n, int k)
	{
		AgentGraph g = new AgentGraph();
		// k = 1 -> Ligne
		// k = 2 -> Arbre binaire
		// k = n -> Etoile centr�e sur 0
		for(int i = 0 ; i < n ; ++i)
		{
			g.addVertex(new UIAgent(i));
		}
		for(int i = 1 ; i < n ; ++i)
		{
			g.addEdge(new MyEdge(), g.getAgent(i), g.getAgent((i-1)/k));
		}
		return g;
	}
	
	public static AgentGraph getSmallWorld(int n, int k, double p)
	{
		AgentGraph g = getRegularGraph(n, k);
		for(int i = 0 ; i < g.getVertexCount() ; ++i)
		{
			int j = 0;
			UIAgent node = g.getAgent(i);
			Iterator<UIAgent> vertices = g.getNeighbors(node).iterator();
			while(vertices.hasNext() && ++j < i)
			{
				int s = 0;
				double r = seed.nextDouble();
				if(r <= p)
				{
					UIAgent n1, n2, n3 = null;
					// Determination du noeud fixe
					boolean order = seed.nextBoolean();
					if(order)
					{
						n1 = node;
						n2 = vertices.next();
					}
					else
					{
						n1 = vertices.next();
						n2 = node;
					}
					do
					{
						if(s++ > 100) break; // Safeguard
						n3 = g.getAgent(seed.nextInt(g.getVertexCount()));
					}
					while(g.isNeighbor(n1, n3) || n1 == n3);
					// D�placement de l'arc
					g.removeEdge(g.findEdge(n1, n2));
					g.removeEdge(g.findEdge(n2, n1));
					g.addEdge(new MyEdge(), n1, n3);
				}
			}
		}
		return g;
	}
	
	public static AgentGraph getRandomGraph(int n, double k)
	{
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i)
		{
			g.addVertex(new UIAgent(i));
		}
		for(int i = 0 ; i < n ; i++)
		{
			UIAgent alpha = g.getAgent(i);
			UIAgent beta;
			
			// agent i, lier avec tous les autres agents avec une probabilite de k 
			for(int j = 0 ; j < n ; ++j)
			{
				if (i != j) {
					beta = g.getAgent(j);
					if (seed.nextDouble() < k) {
						g.addEdge(new MyEdge(), alpha, beta);
					}
				}
			}
		}
		
		return g;
	}
	
	public static AgentGraph getSymmetricRandomGraph(int n, double k)
	{
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i)
		{
			g.addVertex(new UIAgent(i));
		}
		for(int i = 0 ; i < n ; i++)
		{
			UIAgent alpha = g.getAgent(i);
			UIAgent beta;
			
			// agent i, lier avec tous les autres agents avec une probabilite de k 
			for(int j = i + 1 ; j < n ; ++j)
			{
				if (i != j) {
					beta = g.getAgent(j);
					if (seed.nextDouble() < k) {
						g.addEdge(new MyEdge(), alpha, beta);
						g.addEdge(new MyEdge(), beta, alpha);
					}
				}
			}
		}
		return g;
	}
	
	// added by Schwind
	// make the symmetric closure of the current graph
	public static AgentGraph getSymmetricClosure (AgentGraph g) {
		AgentGraph result = new AgentGraph();
		for (UIAgent ag : g.getVertices()) {
			result.addVertex(ag);
		}
		// get the symmetric graph
		MyEdge edge1, edge2;
		for (UIAgent ag1 : result.getVertices()) {
			for (UIAgent ag2 : result.getVertices()) {
				if (ag1.getId() < ag2.getId()) {
					edge1 = g.findEdge(ag1, ag2);
					edge2 = g.findEdge(ag2, ag1);
					if (edge1 != null || edge2 != null) {
						//g.removeEdge(edge);
						result.addEdge(new MyEdge(), ag1, ag2);
						result.addEdge(new MyEdge(), ag2, ag1);
					}
				}
			}
		}
		
		return result;
	}
	
	public static AgentGraph getSymmetricClique(int n)
	{
		AgentGraph g = new AgentGraph();
		for(int i = 0 ; i < n ; ++i)
		{
			g.addVertex(new UIAgent(i));
		}
		for(int i = 0 ; i < n ; i++)
		{
			UIAgent alpha = g.getAgent(i);
			UIAgent beta;
			
			// agent i, lier avec tous les autres agents
			for(int j = 0 ; j < n ; ++j)
			{
				if (i != j) {
					beta = g.getAgent(j);
					g.addEdge(new MyEdge(), alpha, beta);
				}
			}
		}
		return g;
	}
}
