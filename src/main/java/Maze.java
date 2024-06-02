import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class Maze extends JComponent implements MouseInputListener, MouseWheelListener, ActionListener {
        public int width;
        public int height;
        public int viewport_width = 0;
        public int viewport_height = 0;
        public Integer entry;
        public Integer exit;
        public String file_path;

        public List<List<Tile>> tiles;
        public List<Integer> solution = null;

        private JPanel bottomPanel;
        private JButton pathButton;
        private JButton moveEntry;
        private JButton moveExit;

        private boolean moving = false;
        private int movedType = 0;
        private int square_size = 16;
        private int offset_x = 0;
        private int offset_y = 0;

        private int init_offset_x = 0;
        private int init_offset_y = 0;
        private Integer move_start_x = null;
        private Integer move_start_y = null;
        private Integer highlighted = null;

        public class Tile {
                public boolean wall;
                public boolean visited;
                public int direciton = 0;

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
                for (int i = 0; i < this.height; i++) {
                        List<Tile> line = new ArrayList<>();
                        for (int j = 0; j < this.width; j++) {
                                line.add(new Tile(false));
                        }
                        this.tiles.add(line);
                }
        }

        public JPanel initBottom()
        {
                bottomPanel = new JPanel();
                pathButton = new JButton("Rozwiąż");
                moveEntry = new JButton("Zmiana wejścia");
                moveExit = new JButton("Zmiana wyjścia");
                pathButton.addActionListener(this);
                moveEntry.addActionListener(this);
                moveExit.addActionListener(this);
                bottomPanel.add(pathButton);
                bottomPanel.add(moveEntry);
                bottomPanel.add(moveExit);
                return bottomPanel;
        }

        public void reset()
        {
                square_size = 16;
                offset_x = 0;
                offset_y = 0;
                this.repaint();
        }

        public void makeSmaller()
        {
                int diff = Math.max(1, (int)Math.ceil(this.square_size * 0.1));
                this.square_size -= diff;
                this.square_size = Math.min(150, Math.max(1, this.square_size));
                this.repaint();
        }

        public void makeBigger()
        {
                int diff = Math.max(1, (int)Math.ceil(this.square_size * 0.1));
                this.square_size += diff;
                this.square_size = Math.min(150, Math.max(1, this.square_size));
                this.repaint();
        }

        private void appendCond(ArrayList<Integer> list, Integer index, Integer direction, Integer dx, Integer dy)
        {
                int x = index % width + dx;
                int y = index / width + dy;

                if (0 > x || 0 > y || x >= width || y >= height) {
                        return;
                }
                Tile tile = tiles.get(y).get(x);
                if (tile.visited || tile.wall) {
                        return;
                }
                tile.direciton = direction;
                tile.visited = true;
                list.add(y * width + x);
        }

        public void solve()
        {
                ArrayList<Integer> qNow = new ArrayList<>();
                ArrayList<Integer> qNext = new ArrayList<>();

                qNow.add(exit);

                while (qNow.size() > 0) {
                        for (Integer index : qNow) {
                                appendCond(qNext, index, 1, 0, 1);
                                appendCond(qNext, index, 2, -1, 0);
                                appendCond(qNext, index, 3, 0, -1);
                                appendCond(qNext, index, 4, 1, 0);
                        }

                        qNow = qNext;
                        qNext = new ArrayList<>();
                }

                solution = new ArrayList<>();
                int current = entry;
                while (current != exit) {
                        solution.add(current);
                        Tile tile = tiles.get(current / width).get(current % width);

                        switch (tile.direciton) {
                        case 1:
                                current -= width;
                                break;
                        case 2:
                                current += 1;
                                break;
                        case 3:
                                current += width;
                                break;
                        case 4:
                                current -= 1;
                                break;
                        default:
                                System.out.println("uff");
                                break;
                        }
                }
                this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g)
        {
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();
                viewport_width = getWidth();
                viewport_height = getHeight();

                int center_x = width / 2;
                int center_y = height / 2;

                int offset_x = center_x + this.offset_x - square_size * this.width / 2;
                int offset_y = center_y + this.offset_y - square_size * this.height / 2;

                for (int x = 0; this.width > x; x++) {
                        for (int y = 0; this.height > y; y++) {
                                if (this.tiles.get(y).get(x).wall) {
                                        g.setColor(new Color(0, 0, 0));
                                } else {
                                        g.setColor(new Color(255, 255, 255));
                                }
                                g.fillRect(
                                    offset_x + x * square_size,
                                    offset_y + y * square_size,
                                    square_size, square_size);
                        }
                }
                if (solution != null) {
                        g.setColor(new Color(100, 100, 255));
                        for (Integer index : solution) {
                                g.fillRect(
                                    offset_x + index % this.width * square_size,
                                    offset_y + index / this.width * square_size,
                                    square_size, square_size);
                        }
                }

                g.setColor(new Color(0, 255, 0));
                g.fillRect(
                    offset_x + this.entry % this.width * square_size,
                    offset_y + this.entry / this.width * square_size,
                    square_size, square_size);
                g.setColor(new Color(255, 0, 0));
                g.fillRect(
                    offset_x + this.exit % this.width * square_size,
                    offset_y + this.exit / this.width * square_size,
                    square_size, square_size);
                if (highlighted != null) {
                        g.setColor(new Color(255, 255, 0));
                        g.fillRect(
                            offset_x + highlighted % this.width * square_size,
                            offset_y + highlighted / this.width * square_size,
                            square_size, square_size);
                }
        }

        @Override
        public void mouseMoved(MouseEvent event)
        {
                if (!moving) {
                        return;
                }
                int center_x = getWidth() / 2;
                int center_y = getHeight() / 2;

                int offset_x = center_x + this.offset_x - square_size * this.width / 2;
                int offset_y = center_y + this.offset_y - square_size * this.height / 2;

                int x = event.getX() - offset_x;
                int y = event.getY() - offset_y - 50;

                if (0 > x || 0 > y || width * square_size <= x || height * square_size <= y) {
                        highlighted = null;
                        this.repaint();
                        return;
                }

                highlighted = y / square_size * width + x / square_size;
                this.repaint();
        }

        @Override
        public void mouseClicked(MouseEvent event)
        {
                if (highlighted == null) {
                        return;
                }
                boolean isWall = tiles.get(highlighted / width).get(highlighted % width).wall;
                if (highlighted != null && moving && !isWall) {
                        switch (movedType) {
                        case 1:
                                entry = highlighted;
                                break;

                        case 2:
                                exit = highlighted;
                                break;
                        default:
                                break;
                        }
                        moveEntry.setEnabled(true);
                        moveExit.setEnabled(true);
                        moving = false;
                        movedType = 0;
                        highlighted = null;
                        solution = null;
                        for (List<Tile> line : tiles) {
                                for (Tile tile : line) {
                                        tile.direciton = 0;
                                        tile.visited = false;
                                }
                        }
                }
                this.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent event)
        {
        }

        @Override
        public void mouseExited(MouseEvent event)
        {
        }

        @Override
        public void mouseDragged(MouseEvent event)
        {
                if (this.move_start_x == null || this.move_start_y == null) {
                        return;
                }
                this.offset_x = this.init_offset_x + event.getX() - this.move_start_x;
                this.offset_y = this.init_offset_y + event.getY() - this.move_start_y;
                this.repaint();
        }

        @Override
        public void mousePressed(MouseEvent event)
        {
                if (event.getButton() == MouseEvent.BUTTON1) {
                        this.init_offset_x = this.offset_x;
                        this.init_offset_y = this.offset_y;
                        this.move_start_x = event.getX();
                        this.move_start_y = event.getY();
                }
        }

        @Override
        public void mouseReleased(MouseEvent event)
        {
                if (event.getButton() == MouseEvent.BUTTON1) {
                        this.move_start_x = null;
                        this.move_start_y = null;
                }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event)
        {
                int diff = Math.max(1, (int)Math.ceil(this.square_size * 0.1));
                this.square_size += -1 * diff * event.getWheelRotation();
                this.square_size = Math.min(150, Math.max(1, this.square_size));
                this.repaint();
        }

        public void loadMazeFromFile(String filePath)
        {
                tiles = new ArrayList<List<Tile>>();
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        int y = 0;
                        while ((line = br.readLine()) != null) {
                                if (line.trim().isEmpty())
                                        continue;
                                if (this.width == 0)
                                        this.width = line.length();
                                List<Tile> row = new ArrayList<>();
                                for (int x = 0; x < line.length(); x++) {
                                        char c = line.charAt(x);
                                        Tile tile = new Tile(c == 'X');
                                        if (c == 'P')
                                                this.entry = y * this.width + x;
                                        if (c == 'K')
                                                this.exit = y * this.width + x;
                                        row.add(tile);
                                }
                                this.tiles.add(row);
                                y++;
                        }
                        this.height = y;
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        @Override
        public void actionPerformed(ActionEvent event)
        {
                switch (event.getActionCommand()) {
                case "Rozwiąż":
                        solve();
                        break;
                case "Zmiana wejścia":
                        moveEntry.setEnabled(false);
                        moveExit.setEnabled(false);
                        moving = true;
                        movedType = 1;
                        break;
                case "Zmiana wyjścia":
                        moveEntry.setEnabled(false);
                        moveExit.setEnabled(false);
                        moving = true;
                        movedType = 2;
                        break;
                default:
                        break;
                }
        }
}
