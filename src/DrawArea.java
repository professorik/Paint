import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

/**
 * Component for drawing !
 *
 * @author sylsau
 *
 */
public class DrawArea extends JComponent {

    // Image in which we're going to draw
    private Image image;
    // Graphics2D object ==> used to draw on
    private Graphics2D g2;
    // Mouse coordinates
    private int currentX, currentY, oldX, oldY;

    //0 - карандаш , 1 - ластик, 2 - линия , 3 - окружность, 4 - прямоугольник
    public static int type;

    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // save coord x,y when mouse is pressed
                oldX = e.getX();
                oldY = e.getY();
                switch (type){
                    case 0:

                        break;
                    case 1:
                        if (g2 != null) {
                            Color temp =  g2.getColor();
                            g2.setPaint(Color.white);
                            // draw white on entire draw area to clear
                            g2.fillRect(oldX, oldY, 50, 50);
                            repaint();
                            // store current coords x,y as olds x,y
                            oldX = currentX;
                            oldY = currentY;
                            g2.setPaint(temp);
                        }
                        break;
                    case 2:
                         if (g2 != null && !(oldX == currentX && oldY == currentY)) {
                            // draw line if g2 context not null
                            g2.drawLine(oldX, oldY, currentX, currentY);
                            // refresh draw area to repaint
                            repaint();
                            // store current coords x,y as olds x,y
                            oldX = currentX;
                            oldY = currentY;
                         }
                        break;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                // coord x,y when drag mouse
                currentX = e.getX();
                currentY = e.getY();
                Color temp =  g2.getColor();
                switch (type){
                    case 0:
                        if (g2 != null) {
                            // draw line if g2 context not null
                            g2.drawLine(oldX, oldY, currentX, currentY);
                            // refresh draw area to repaint
                            repaint();
                            // store current coords x,y as olds x,y
                            oldX = currentX;
                            oldY = currentY;
                        }
                        break;
                    case 1:
                        if (g2 != null) {
                            g2.setPaint(Color.white);
                            // draw white on entire draw area to clear
                            g2.fillRect(oldX, oldY, 50, 50);
                            repaint();
                            // store current coords x,y as olds x,y
                            oldX = currentX;
                            oldY = currentY;
                            g2.setPaint(temp);
                        }
                        break;
                    case 2:

                        break;

                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            // image to draw null ==> we create
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            // enable antialiasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // clear draw area
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    // now we create exposed methods
    public void clear() {
        g2.setPaint(Color.white);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    public void setColor(Color color){
        g2.setPaint(color);
    }
}