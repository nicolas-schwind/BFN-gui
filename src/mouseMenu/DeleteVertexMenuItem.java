package mouseMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;


// A class to implement the deletion of a vertex from within a PopupVertexEdgeMenuMousePlugin.
public class DeleteVertexMenuItem<V> extends JMenuItem
implements ListenerVertexMenu<V>
{
	private static final long serialVersionUID = 1L;
	private V vertex;
    private VisualizationViewer<V,?> vv;
    
    public DeleteVertexMenuItem()
    {
        super("Delete vertex");
        this.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                vv.getPickedVertexState().pick(vertex, false);
                vv.getGraphLayout().getGraph().removeVertex(vertex);
                vv.repaint();
            }
        });
    }

    public void setVertexAndView(V v, VisualizationViewer<V,?> vv)
    {
        this.vertex = v;
        this.vv = vv;
        this.setText("Delete vertex");
    }
}
