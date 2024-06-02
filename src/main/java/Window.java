import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Window extends JFrame implements ActionListener {
        private JPanel mainPanel;
        private JMenuBar menuBar;
        private JMenu viewMenu;
        private Maze maze = null;

        public Window()
        {
                setTitle("Astrolabe");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(600, 600);
                getContentPane().setBackground(Color.BLACK);

                mainPanel = new JPanel(new BorderLayout());
                initMenuBar();

                JLabel label = new JLabel("Labirynt nie załadowany");
                label.setFont(new Font("Arial", Font.BOLD, 16));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(label, BorderLayout.CENTER);

                add(mainPanel);
                setVisible(true);
        }

        private void initMenuBar()
        {
                menuBar = new JMenuBar();
                JMenu mazeMenu = new JMenu("Labirynt");
                JMenuItem openFile = new JMenuItem("Otwórz");
                JMenuItem saveFile = new JMenuItem("Zapisz");
                JMenuItem closeFile = new JMenuItem("Zamknij");
                openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
                saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
                closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
                openFile.addActionListener(this);
                saveFile.addActionListener(this);
                closeFile.addActionListener(this);
                mazeMenu.add(openFile);
                mazeMenu.add(saveFile);
                mazeMenu.add(closeFile);
                menuBar.add(mazeMenu);

                viewMenu = new JMenu("Widok");
                JMenuItem resetView = new JMenuItem("Reset");
                JMenuItem smallView = new JMenuItem("Oddal");
                JMenuItem bigView = new JMenuItem("Przybliż");
                resetView.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
                smallView.setAccelerator(KeyStroke.getKeyStroke('o'));
                bigView.setAccelerator(KeyStroke.getKeyStroke('p'));
                resetView.addActionListener(this);
                smallView.addActionListener(this);
                bigView.addActionListener(this);
                viewMenu.add(resetView);
                viewMenu.add(smallView);
                viewMenu.add(bigView);
                viewMenu.setEnabled(false);
                menuBar.add(viewMenu);

                this.setJMenuBar(menuBar);
        }

        @Override
        public void actionPerformed(ActionEvent event)
        {
                switch (event.getActionCommand()) {
                case "Otwórz":
                        mainPanel.removeAll();
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
                        int result = fileChooser.showOpenDialog(null);

                        File selectedFile;
                        if (result == JFileChooser.APPROVE_OPTION) {
                                selectedFile = fileChooser.getSelectedFile();
                        } else {
                                return;
                        }

                        maze = new Maze();
                        maze.loadMazeFromFile(selectedFile.getAbsolutePath());
                        mainPanel.add(maze, BorderLayout.CENTER);
                        mainPanel.add(maze.initBottom(), BorderLayout.SOUTH);
                        addMouseMotionListener(maze);
                        addMouseListener(maze);
                        addMouseWheelListener(maze);

                        viewMenu.setEnabled(true);
                        mainPanel.revalidate();
                        break;
                case "Zapisz":
                        if (maze == null) {
                                return;
                        }

                        BufferedImage image = new BufferedImage(maze.viewport_width, maze.viewport_height,
                            BufferedImage.TYPE_INT_ARGB);
                        File outputFile = new File("maze.png");
                        maze.paint(image.getGraphics());
                        try {
                                ImageIO.write(image, "PNG", outputFile);
                        } catch (Exception e) {
                                System.out.println("Failed to write the image");
                        }
                        break;
                case "Zamknij":
                        maze = null;
                        viewMenu.setEnabled(false);
                        mainPanel.removeAll();

                        JLabel label = new JLabel("Maze file not loaded");
                        label.setFont(new Font("Arial", Font.BOLD, 16));
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        mainPanel.add(label, BorderLayout.CENTER);

                        mainPanel.revalidate();
                        break;
                case "Reset":
                        maze.reset();
                        break;
                case "Oddal":
                        maze.makeSmaller();
                        break;
                case "Przybliż":
                        maze.makeBigger();
                        break;
                default:
                        break;
                }
        }
}
