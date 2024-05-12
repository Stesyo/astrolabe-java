import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class Window extends JFrame
{
        private JPanel startPanel;

        public Window()
        {
                this.setTitle("Astrolabe");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setSize(600, 600);
                this.getContentPane().setBackground(Color.BLACK);

                startPanel = new JPanel(new GridLayout(1, 1));
                Maze maze = new Maze(10, 10);
                maze.entry = 0;
                maze.exit = 99;
                startPanel.add(maze);

                this.add(startPanel);
                this.setVisible(true);
        }
}
