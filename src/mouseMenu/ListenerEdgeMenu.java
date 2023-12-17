package mouseMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * An interface for menu items that are interested in knowning the currently selected edge and
 * its visualization component context. Used with PopupVertexEdgeMenuMousePlugin.
 */

//Used to set the edge and visulization component.
public interface ListenerEdgeMenu<E>
{
    void setEdgeAndView(E e, VisualizationViewer<?,E> vv); 
}
