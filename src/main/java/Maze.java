import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Maze extends JComponent
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

        @Override protected void paintComponent(Graphics g)
        {
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();
                System.out.println(width);
                System.out.println(height);

                int size = Math.min(width / this.width, height / this.height);
                System.out.println(size);
                int offset_x = (width - size * this.width) / 2;
                int offset_y = (height - size * this.height) / 2;

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
                                        g.setColor(new Color(255, 128, 128));
                                }
                                g.fillRect(offset_x + x * size, offset_y + y * size, size, size);
                        }
                }
                g.setColor(new Color(0, 255, 0));
                g.fillRect(offset_x + this.entry % this.width * size, offset_y + this.entry / this.width * size, size,
                           size);

                g.setColor(new Color(255, 0, 0));
                g.fillRect(offset_x + this.exit % this.width * size, offset_y + this.exit / this.width * size, size,
                           size);
        }
}
