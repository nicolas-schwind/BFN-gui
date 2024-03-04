package mouseMenu;

import java.awt.geom.Point2D;

/**
 * Used to set the point at which the mouse was clicked for those menu items
 * interested in this information.  Useful, for example, if you want to bring up
 * a dialog box right at the point the mouse was clicked.
 * The PopupVertexEdgeMenuMousePlugin checks to see if a menu component implements
 * this interface and if so calls it to set the point.
 */

// Sets the point of the mouse click.
public interface ListenerMenuPoint
{
	void setPoint(Point2D point);   
}
