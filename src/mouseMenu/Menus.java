package mouseMenu;

import javax.swing.JPopupMenu;

/**
 * Menu constructors
 */

public class Menus {
    public static class EdgeMenu<E> extends JPopupMenu {
		private static final long serialVersionUID = 1L;
        public EdgeMenu () {
            super("Edge menu");
            this.add(new DeleteEdgeMenuItem<E>());
        }
    }
    
    public static class VertexMenu<V> extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		public VertexMenu () {
            super("Vertex menu");
            this.add(new DeleteVertexMenuItem<V>());
        }
    }
}
