import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class Maze extends JComponent implements MouseInputListener, MouseWheelListener
{
        public class Tile
        {
                public boolean wall;
                public boolean visited;
                Tile()
                {
                        this.wall = false;
                        this.visited = false;
                }
                Tile(boolean wall)
                {
                        this.wall = wall;
                        this.visited = false;
                }
        };

        public int width;
        public int height;
        public int entry;
        public int exit;
        public String file_path;

        public List<List<Tile>> tiles;
        public List<Integer> solution;

        public Maze()
        {
                this.width = 0;
                this.height = 0;
        }
        public Maze(int width, int height)
        {
                this.width = width;
                this.height = height;
                this.tiles = new ArrayList<>();
                for (int i = 0; i < this.height; i++)
                {
                        List<Tile> line = new ArrayList<>();
                        for (int j = 0; j < this.width; j++)
                        {
                                line.add(new Tile(Math.random() < 0.5));
                        }
                        this.tiles.add(line);
                }
        }

        private int square_size = 16;
	private int offset_x = 0;
	private int offset_y = 0;

	private int init_offset_x = 0;
	private int init_offset_y = 0;
	private Integer move_start_x = null;
	private Integer move_start_y = null;

        @Override protected void paintComponent(Graphics g)
        {
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();

		int center_x = width / 2;
		int center_y = height / 2;

		int offset_x = center_x + this.offset_x - square_size * this.width / 2;
		int offset_y = center_y + this.offset_y - square_size * this.height/ 2;

                for (int x = 0; this.width > x; x++)
                {
                        for (int y = 0; this.height > y; y++)
                        {
                                if (this.tiles.get(y).get(x).wall)
                                {
                                        g.setColor(new Color(0, 0, 0));
                                }
                                else
                                {
                                        g.setColor(new Color(255, 255, 255));
                                }
                                g.fillRect(
					offset_x + x * square_size,
					offset_y + y * square_size, 
					square_size, square_size
				);
                        }
                }
                g.setColor(new Color(0, 255, 0));
		g.fillRect(
			offset_x + this.entry % this.width * square_size,
			offset_y + this.entry / this.width * square_size, 
			square_size, square_size
		);
                g.setColor(new Color(255, 0, 0));
		g.fillRect(
			offset_x + this.exit % this.width * square_size,
			offset_y + this.exit / this.width * square_size, 
			square_size, square_size
		);
        }
        @Override public void mouseMoved(MouseEvent event) {}
        @Override public void mouseClicked(MouseEvent event) {}
        @Override public void mouseEntered(MouseEvent event) {}
        @Override public void mouseExited(MouseEvent event) {}

        @Override public void mouseDragged(MouseEvent event)
        {
		if (this.move_start_x == null || this.move_start_y == null) {
			return;
		}
		this.offset_x = this.init_offset_x + event.getX() - this.move_start_x;
		this.offset_y = this.init_offset_y + event.getY() - this.move_start_y;
		this.repaint();
        }
        @Override public void mousePressed(MouseEvent event)
        {
		if (event.getButton() == MouseEvent.BUTTON1) {
			this.init_offset_x = this.offset_x;
			this.init_offset_y = this.offset_y;
			this.move_start_x = event.getX();
			this.move_start_y = event.getY();
		}
        }
        @Override public void mouseReleased(MouseEvent event)
        {
		if (event.getButton() == MouseEvent.BUTTON1) {
			this.move_start_x = null;
			this.move_start_y = null;
		}
        }
        @Override public void mouseWheelMoved(MouseWheelEvent event)
        {
		int diff = Math.max(1, (int) Math.ceil(this.square_size * 0.1));
		this.square_size += -1 * diff * event.getWheelRotation();
		this.square_size = Math.min(150, Math.max(1, this.square_size));
		this.repaint();
        }
}
