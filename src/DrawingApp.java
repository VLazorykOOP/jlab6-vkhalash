import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawingApp extends JFrame {
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;
    private final ArrayList<Line> lines = new ArrayList<>();

    public DrawingApp() {
        setTitle("Drawing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu colorMenu = new JMenu("Color");
        JMenuItem chooseColorItem = new JMenuItem("Choose Color");
        chooseColorItem.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(this, "Choose Line Color", currentColor);
            if (selectedColor != null) {
                currentColor = selectedColor;
            }
        });
        colorMenu.add(chooseColorItem);
        menuBar.add(colorMenu);

        JMenu thicknessMenu = new JMenu("Thickness");
        JMenuItem thinItem = new JMenuItem("Thin (2px)");
        JMenuItem mediumItem = new JMenuItem("Medium (5px)");
        JMenuItem thickItem = new JMenuItem("Thick (10px)");

        thinItem.addActionListener(e -> currentThickness = 2);
        mediumItem.addActionListener(e -> currentThickness = 5);
        thickItem.addActionListener(e -> currentThickness = 10);

        thicknessMenu.add(thinItem);
        thicknessMenu.add(mediumItem);
        thicknessMenu.add(thickItem);
        menuBar.add(thicknessMenu);

        setJMenuBar(menuBar);
    }

    private class DrawingPanel extends JPanel {
        private Point startPoint;

        public DrawingPanel() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    lines.add(new Line(startPoint, e.getPoint(), currentColor, currentThickness));
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    lines.add(new Line(startPoint, e.getPoint(), currentColor, currentThickness));
                    startPoint = e.getPoint();
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (Line line : lines) {
                g2d.setColor(line.getColor());
                g2d.setStroke(new BasicStroke(line.getThickness()));
                g2d.drawLine(line.getStartPoint().x, line.getStartPoint().y, line.getEndPoint().x, line.getEndPoint().y);
            }
        }
    }

    private static class Line {
        private final Point startPoint;
        private final Point endPoint;
        private final Color color;
        private final int thickness;

        public Line(Point startPoint, Point endPoint, Color color, int thickness) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.color = color;
            this.thickness = thickness;
        }

        public Point getStartPoint() {
            return startPoint;
        }

        public Point getEndPoint() {
            return endPoint;
        }

        public Color getColor() {
            return color;
        }

        public int getThickness() {
            return thickness;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingApp app = new DrawingApp();
            app.setVisible(true);
        });
    }
}
