import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SwingPaint {

    JButton clearBtn, eraserBtn, lineBtn, colorPicker, circleBtn, rectangleBtn, pencilBtn;
    DrawArea drawArea;

    ActionListener actionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == pencilBtn){
                DrawArea.type = 0;
            }else if (e.getSource() == eraserBtn){
                DrawArea.type = 1;
            }else if (e.getSource() == lineBtn){
                DrawArea.type = 2;
            }else if (e.getSource() == circleBtn){
                DrawArea.type = 3;
            }else if (e.getSource() == rectangleBtn){
                DrawArea.type = 4;
            }else if (e.getSource() == colorPicker){
                Color initialcolor = Color.WHITE;
                // color chooser Dialog Box
                Color color =  JColorChooser.showDialog(drawArea,
                        "Select a color", initialcolor);
                // set Background color of the Conatiner
                drawArea.setColor(color);
            }else if (e.getSource() == clearBtn) {
                drawArea.clear();
            }
        }
    };


    public static void main(String[] args) {
        new SwingPaint().show();
    }

    public void show() {
        // create main frame
        JFrame frame = new JFrame("Swing Paint");
        Container content = frame.getContentPane();
        // set layout on content pane
        content.setLayout(new BorderLayout());
        // create draw area
        DrawArea.type = 0;
        drawArea = new DrawArea();

        // add to content pane
        content.add(drawArea, BorderLayout.CENTER);

        // create controls to apply colors and call clear feature
        JPanel controls = new JPanel();

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(actionListener);
        eraserBtn = new JButton("Ластик");
        eraserBtn.addActionListener(actionListener);
        lineBtn = new JButton("Линия");
        lineBtn.addActionListener(actionListener);
        colorPicker = new JButton("Выбор цвета");
        colorPicker.addActionListener(actionListener);
        pencilBtn = new JButton("Карандаш");
        pencilBtn.addActionListener(actionListener);
        circleBtn = new JButton("Окружность");
        circleBtn.addActionListener(actionListener);
        rectangleBtn = new JButton("Прямоугольник");
        rectangleBtn.addActionListener(actionListener);


        // add to panel
        controls.add(pencilBtn);
        controls.add(eraserBtn);
        controls.add(lineBtn);
        controls.add(circleBtn);
        controls.add(rectangleBtn);
        controls.add(colorPicker);
        controls.add(clearBtn);


        // add to content pane
        content.add(controls, BorderLayout.NORTH);

        frame.setSize(1200, 600);
        // can close frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // show the swing paint result
        frame.setVisible(true);

        // Now you can try our Swing Paint !!! Enjoy <img draggable="false" class="emoji" alt="😀" src="https://s.w.org/images/core/emoji/11/svg/1f600.svg">
    }

}