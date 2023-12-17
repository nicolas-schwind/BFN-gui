package mouseMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;


// A class to implement the deletion of an edge from within a PopupVertexEdgeMenuMousePlugin.
public class DeleteEdgeMenuItem<E> extends JMenuItem implements ListenerEdgeMenu<E>
{
	private static final long serialVersionUID = 1L;
	private E edge;
    private VisualizationViewer<?,E> vv;
    
    public DeleteEdgeMenuItem()
    {
        super("Delete edge");
        this.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                vv.getPickedEdgeState().pick(edge, false);
                vv.getGraphLayout().getGraph().removeEdge(edge);
                vv.repaint();
            }
        });
    }
    
    // Implements the EdgeMenuListener interface to update
    // the menu item with info on the currently chosen edge.
    public void setEdgeAndView(E edge, VisualizationViewer<?, E> vv)
    {
        this.edge = edge;
        this.vv = vv;
        this.setText("Delete edge");
    }
}
