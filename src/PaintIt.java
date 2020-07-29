import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import java.awt.geom.Line2D;


public class PaintIt extends JFrame implements ChangeListener,ActionListener{



    public BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
    //JPanel canvas = new JPanel();


    JPanel buttonPanel = new JPanel();
    JColorChooser colorPanel = new JColorChooser();
    Point lastPos = null;
    Point startPos = null;
    Point finishPos = null;
    Graphics g;
    JButton drawButton = new JButton("Free");
    JButton lineButton = new JButton("Line");
    JButton clearButton = new JButton("Clear");
    public ColorSelectionModel Model;
    JPanel canvas = new JPanel();
    int changer = 1;

    public PaintIt () {


        setLocation(100,100);
        setSize(900,700);
        setTitle("Photoshop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setBackground(Color.WHITE);

        clearButton.addActionListener(this);
        clearButton.setActionCommand("clear");

        drawButton.addActionListener(this);
        drawButton.setActionCommand("draw");

        lineButton.addActionListener(this);
        lineButton.setActionCommand("line");

        //add buttons here
        buttonPanel.add(drawButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(clearButton);

        Model = colorPanel.getSelectionModel();
        Model.addChangeListener (this);

        //set the look
        getContentPane().add(canvas, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(colorPanel, BorderLayout.SOUTH);
        setVisible(true);

        g = canvas.getGraphics();
        g.setColor(Color.BLACK);

        canvas.addMouseMotionListener(new MouseMotionListener () {
            public void mouseDragged (MouseEvent m) {
                Point p = m.getPoint() ;
                if (changer==1){
                    g.drawLine(lastPos.x, lastPos.y, p.x, p.y) ;
                }
                lastPos = p ;

            }
            public void mouseMoved (MouseEvent m) {}
        });

        canvas.addMouseListener(new MouseListener () {
            public void mouseClicked(MouseEvent e) {startPos = e.getPoint();}
            public void mousePressed(MouseEvent e) {lastPos = e.getPoint();}
            public void mouseReleased(MouseEvent e) {
                lastPos = null;
                finishPos = e.getPoint();
                startPos = null;}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });


    }



    public void actionPerformed(ActionEvent e) {
        if("clear".equals(e.getActionCommand())) {
            repaint();
        }
        if("draw".equals(e.getActionCommand())) {
            changer = 1;
        }
        if("line".equals(e.getActionCommand())) {
            changer = 2;
        }


    }


    public void captureCanvasImage (){
        Graphics g = image.createGraphics();
        canvas.paint(g);
    }
 
    /*
    @Override
    public void invalidate() {     
    super.invalidate(); 
    
    this.paint(this.getGraphics()); 
    }
*/

    public void stateChanged(ChangeEvent e) {
        Color Choice;
        Choice  = colorPanel.getColor();
        g.setColor(Choice);
    }



    public static void main (String [] args) {
        PaintIt p = new PaintIt();
        p.setVisible(true);
    }
}