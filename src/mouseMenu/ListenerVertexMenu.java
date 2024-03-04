package mouseMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * Used to indicate that this class wishes to be told of a selected vertex
 * along with its visualization component context. Note that the VisualizationViewer
 * has full access to the graph and layout.
 */

public interface ListenerVertexMenu<V>
{
    void setVertexAndView(V v, VisualizationViewer<V,?> vv);    
}
