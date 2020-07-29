import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  javax.swing.*;
import  java.awt.image.*;
import  javax.imageio.*;
import  javax.swing.filechooser.FileFilter;

public class ImageEdit {
    // Режим рисования 
    int drawType  = 0;

    //Текущие координаты нажатия
    int xPad;
    int yPad;

    //Временные координаты, для отрисовки линии, элипса, прямоугольника (отрисовка только в паре c xPad и yPad)
    int xf;
    int yf;

    //Было ли нажатие
    boolean pressed = false;

    // текущий цвет
    Color currentColor;

    //Главное окно (все окно)
    MyFrame mainFrame;

    //Полотно
    MyPanel mainCanvas;

    //Кнопка для выбора цвета (открывает Color Picker)
    JButton colorButton;

    // поверхность рисования
    BufferedImage image;

    //Загружаем ли мы картинку
    boolean loading = false;

    //Имя файла, которое сохраняем
    String fileName;

    public ImageEdit() {
        //Название окна, размер, что делать при нажатии на крестик
        mainFrame = new MyFrame("Графический редактор");
        mainFrame.setSize(1000,1000);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Устанавливаю текущий цвет черным (теперь пока я не выбрал цвет - все убдет рисоваться черным)
        currentColor = Color.black;

        //Верхнее меню с кнопок "Файл"
        JMenuBar menuBar = new  JMenuBar();
        mainFrame.setJMenuBar(menuBar);
        menuBar.setBounds(0,0, mainFrame.getWidth(),60);
        JMenu fileMenu = new  JMenu("Файл");
        menuBar.add(fileMenu);

        //Добавляю кнопку "Сохранить как..." и назначаю на нее слушателя (то-есть что будет происходить при нажатии)
        Action saveAsAction = new  AbstractAction("Сохранить как...") {
            public void actionPerformed(ActionEvent event) {
                try {
                    //Окно для выбора положения файла
                    JFileChooser jf = new JFileChooser();
                    // Создаем фильтры для файлов
                    TextFileFilter pngFilter = new  TextFileFilter(".png");
                    TextFileFilter jpgFilter = new  TextFileFilter(".jpg");
                    // Добавляем фильтры
                    jf.addChoosableFileFilter(pngFilter);
                    jf.addChoosableFileFilter(jpgFilter);

                    int result = jf.showSaveDialog(null);

                    if(result == JFileChooser.APPROVE_OPTION) {
                        fileName = jf.getSelectedFile().getAbsolutePath();
                    }
                    // Смотрим какой фильтр выбран
                    if(jf.getFileFilter() == pngFilter) {
                        ImageIO.write(image, "png", new  File(fileName+".png"));
                    }
                    else {
                        ImageIO.write(image, "jpeg", new  File(fileName+".jpg"));
                    }
                } catch(IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Ошибка ввода-вывода");
                }
            }
        };
        //Добавляю эту кнопку к верхнему меню
        JMenuItem saveAsMenu = new JMenuItem(saveAsAction);
        fileMenu.add(saveAsMenu);

        mainCanvas = new MyPanel();
        mainCanvas.setBounds(110,0, mainFrame.getWidth() - 110, mainFrame.getHeight());
        mainCanvas.setBackground(Color.WHITE);
        mainCanvas.setOpaque(true);
        mainFrame.add(mainCanvas);

        //Левое меню
        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.VERTICAL);

        //Кнопка для выбора "Карандаш"
        JButton penButton = new  JButton("Карандаш");
        penButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent event) {
                drawType = 0;
            }
        });
        toolbar.add(penButton);

        //Кнопка для выбора "Ластик"
        JButton eraserButton = new JButton("Ластик");
        eraserButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent event) {
                drawType = 1;
            }
        });
        toolbar.add(eraserButton);

        //Кнопка для выбора "Линия"
        JButton lineButton = new  JButton("Линия");
        lineButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent event) {
                drawType = 2;
            }
        });
        toolbar.add(lineButton);

        //Кнопка для выбора "Элипс"
        JButton ellipseButton = new  JButton("Эллипс");
        ellipseButton.addActionListener(new  ActionListener(){
            public void actionPerformed(ActionEvent event) {
                drawType = 3;
            }
        });
        toolbar.add(ellipseButton);

        //Кнопка для выбора "Прямоугольник"
        JButton rectangleButton = new  JButton("Прямоугольник");
        rectangleButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent event) {
                drawType = 4;
            }
        });
        toolbar.add(rectangleButton);

        //Кнопка для выбора цвета
        colorButton = new JButton();
        colorButton.setBackground(currentColor);
        colorButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Вызваю класс с ColorPicker
                Color color =  JColorChooser.showDialog(mainFrame,
                        "Выберите цвет", currentColor);
                //Инициализирую цвет из Color Picker
                colorButton.setBackground(color);
                currentColor = color;
            }
        });
        toolbar.add(colorButton);

        //Размеры левого меню
        toolbar.setBounds(0, 0, 200, 300);
        //Добавляю к главному окну
        mainFrame.add(toolbar);

        //Слушатель во время движения
        mainCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e){
                if (pressed) {
                    Graphics g = image.getGraphics();
                    Graphics2D g2 = (Graphics2D)g;
                    //Установка цвета
                    g2.setColor(currentColor);
                    switch (drawType) {
                        //Карандаш
                        case 0:
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        //Ластик
                        case 1:
                            //Размер ластика
                            g2.setStroke(new BasicStroke(20.0f));
                            g2.setColor(Color.WHITE);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                    }
                    xPad = e.getX();
                    yPad = e.getY();
                }
                mainCanvas.repaint();
            }
        });
        //Просто слушатель
        mainCanvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Graphics g = image.getGraphics();
                Graphics2D g2 = (Graphics2D)g;
                //Установка цвета
                g2.setColor(currentColor);
                switch (drawType) {
                    //Карандаш
                    case 0:
                        g2.drawLine(xPad, yPad, xPad+1, yPad+1);
                        break;
                    //Ластик
                    case 1:
                        //Размер ластика
                        g2.setStroke(new BasicStroke(20.0f));
                        g2.setColor(Color.WHITE);
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                }
                xPad = e.getX();
                yPad = e.getY();

                pressed = true;
                mainCanvas.repaint();
            }
            public void mousePressed(MouseEvent e) {
                xPad = e.getX();
                yPad = e.getY();
                xf = e.getX();
                yf = e.getY();
                pressed = true;
            }
            public void mouseReleased(MouseEvent e) {
                Graphics g = image.getGraphics();
                Graphics2D g2 = (Graphics2D)g;
                //Установка цвета
                g2.setColor(currentColor);
                //Общие рассчеты для овала и прямоугольника
                int  x1 = xf, x2 = xPad, y1 = yf, y2 = yPad;
                if(xf > xPad) {
                    x2 = xf; x1 = xPad;
                }
                if(yf > yPad) {
                    y2 = yf; y1 = yPad;
                }
                switch(drawType) {
                    //Линия
                    case 2:
                        g.drawLine(xf, yf, e.getX(), e.getY());
                        break;
                    //Эллипс
                    case 3:
                        g.drawOval(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                    //Прямоугольник
                    case 4:
                        g.drawRect(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                }
                xf = 0; yf = 0;
                pressed = false;
                mainCanvas.repaint();
            }
        });
        mainFrame.addComponentListener(new ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                //Если делаем загрузку, то изменение размеров формы
                //Отрабатываем в коде загрузки
                if(!loading) {
                    mainCanvas.setSize(mainFrame.getWidth() - 40, mainFrame.getHeight() - 80);
                    BufferedImage tempImage = new  BufferedImage(mainCanvas.getWidth(), mainCanvas.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D d2 = tempImage.createGraphics();
                    d2.setColor(Color.white);
                    d2.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
                    tempImage.setData(image.getRaster());
                    image = tempImage;
                    mainCanvas.repaint();
                }
                loading=false;
            }
        });
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new  Runnable() {
            public void run() {
                new ImageEdit();
            }
        });
    }

    class MyFrame extends JFrame {
        public void paint(Graphics g) {
            super.paint(g);
        }
        public MyFrame(String title) {
            super(title);
        }
    }

    class MyPanel extends JPanel {
        public MyPanel(){}

        public void paintComponent (Graphics g) {
            if(image == null) {
                image = new  BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = image.createGraphics();
                d2.setColor(Color.white);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(image, 0, 0,this);
        }
    }

    // Фильтр картинок
    class TextFileFilter extends FileFilter {
        private String ext;

        public TextFileFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(java.io.File file) {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }

        public String getDescription() {
            return "*" + ext;
        }
    }
}