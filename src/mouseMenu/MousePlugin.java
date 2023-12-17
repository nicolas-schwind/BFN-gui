package mouseMenu;

import java.awt.event.MouseEvent;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.Component;
import java.awt.geom.Point2D;
import javax.swing.JPopupMenu;

@SuppressWarnings("unchecked")

public class MousePlugin<V,E> extends AbstractPopupGraphMousePlugin
{
	private JPopupMenu edgePopup;
	private JPopupMenu vertexPopup;
	private boolean enabled;
	
    public MousePlugin()
    {
        this(MouseEvent.BUTTON3_MASK);
    }
    
    public MousePlugin(int modifiers)
    {
        super(modifiers);
    }
    
    public void setEnabled(boolean enabled)
    {
    	this.enabled = enabled;
    }

	protected void handlePopup(MouseEvent e)
	{
		final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>) e.getSource();
		Point2D p = e.getPoint();
		GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
		if(pickSupport != null && this.enabled)
		{
			final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
			if(v != null)
			{
				updateVertexMenu(v, vv, p);
                vertexPopup.show(vv, e.getX(), e.getY());
			}
			else
			{
				final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
				if(edge != null)
				{
					updateEdgeMenu(edge, vv, p);
					edgePopup.show(vv, e.getX(), e.getY());
				}
			}
		}
	}

	private void updateVertexMenu(V v, VisualizationViewer<V,E> vv, Point2D point)
	{
	    if (vertexPopup == null) return;
	    Component[] menuComps = vertexPopup.getComponents();
	    for (Component comp: menuComps)
	    {
	        if (comp instanceof ListenerVertexMenu)
	        {
	            ((ListenerVertexMenu<V>)comp).setVertexAndView(v, vv);
	        }
	        if (comp instanceof ListenerMenuPoint)
	        {
	            ((ListenerMenuPoint)comp).setPoint(point);
	        }
	    }
	    
	}
	
	private void updateEdgeMenu(E edge, VisualizationViewer<V,E> vv, Point2D point)
	{
	    if (edgePopup == null) return;
	    Component[] menuComps = edgePopup.getComponents();
	    for (Component comp: menuComps)
	    {
	        if (comp instanceof ListenerEdgeMenu)
	        {
	            ((ListenerEdgeMenu<E>)comp).setEdgeAndView(edge, vv);
	        }
	        if (comp instanceof ListenerMenuPoint)
	        {
	            ((ListenerMenuPoint)comp).setPoint(point);
	        }
	    }
	}
	
	public JPopupMenu getEdgePopup()
	{
	    return edgePopup;
	}
	
	public void setEdgePopup(JPopupMenu edgePopup)
	{
	    this.edgePopup = edgePopup;
	}
	
	public JPopupMenu getVertexPopup()
	{
	    return vertexPopup;
	}
	
	public void setVertexPopup(JPopupMenu vertexPopup)
	{
	    this.vertexPopup = vertexPopup;
	}
}
