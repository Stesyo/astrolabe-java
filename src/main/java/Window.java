import java.awt.*;
import javax.swing.*;

public class Window extends JFrame
{
        private JPanel mainPanel;

        public Window()
        {
                this.setTitle("Astrolabe");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setSize(600, 600);
                this.getContentPane().setBackground(Color.BLACK);

                mainPanel = new JPanel(new BorderLayout());
                Maze maze = new Maze(100, 100);
                maze.entry = 0;
                maze.exit = 99;
                mainPanel.add(maze, BorderLayout.CENTER);

                this.addMouseMotionListener(maze);
                this.addMouseListener(maze);
                this.addMouseWheelListener(maze);

                this.add(mainPanel);
                this.setVisible(true);
        }
}
