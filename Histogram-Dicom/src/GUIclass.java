import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import ij.ImagePlus;
import ij.plugin.DICOM;

public class GUIclass extends GLCanvas implements MouseListener {
    public static int x11, y11, x22, y22;
    int imageNumber1=1;
    int imageNumber2=100;
    int imageNumber3=100;
    int coordinate_x = 0;
    int coordinate_y = 0;
    int first_x = 0;
    int first_y = 0;
    int second_x = 0;
    int second_y = 0;
    static GLCanvas canvas2 = new GLCanvas();
    static GLCanvas canvas3 = new GLCanvas();
    static JTextArea text = new JTextArea("Для перехода в режим рисования\n инструмента нажмите F1,\n для возвращения в режим просмотра\n нажмите ESCAPE.");
    static JTextArea a = new JTextArea(1,1);
    static JTextArea b = new JTextArea(1,1);
    static JTextArea c = new JTextArea(1,1);
    static GLCanvas canvas1 = new GLCanvas();
    static final JTextArea tx1 = new JTextArea(1,1);
    static final JTextArea tx2 = new JTextArea(1,1);
    static final JTextArea tx3 = new JTextArea(1,1);
    static final JTextArea tx4 = new JTextArea(1,1);
    static final JTextArea tx5 = new JTextArea(1,1);
    //lines l = new lines();
    int x1 = 0;
    int x2 = 0;
    int y1 =0;
    int y2 =0;
    boolean flag = false, fl = false;
    static lines lines_axial[] = new lines[20]; // массив объектов, аксиальная проекция
    static lines lines_sagital_coronal[][] = new lines[256][2]; // массив объектов, сагитальная, корональная проекции


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static DICOM image = new DICOM();

    GUIclass()
    {
        super();
        for(int i=0;i<20;i++){
            lines_axial[i]=new lines();
        }
        for(int i=0;i<256;i++){
            lines_sagital_coronal[i][0]=new lines();
        }
        for(int i=0;i<256;i++){
            lines_sagital_coronal[i][1]=new lines();
        }
        image.open("images\\brain_001.dcm");
        JFrame frame=new JFrame("Dicom Viewer");
        frame.setSize(780, 648);
        frame.setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel panel_new = new JPanel();
        JPanel panel_new2 = new JPanel();
        panel_new2.setBackground(Color.GRAY);
        panel_new.setSize(image.getWidth()*3, image.getHeight()*2);
        panel_new2.setSize(250, 648);
        panel_new.setLayout(new GridLayout(2,2));
        GridLayout gl= new GridLayout(20,2);
        panel_new2.setLayout(gl);
        final JPanel panel1 = new JPanel();
        final JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        final JPanel panel4 = new JPanel();
        final JButton buttonPanel1 = new JButton("Next");
        final JButton buttonPanel2 = new JButton("Next");
        final JButton buttonPanel3 = new JButton("Next");
        panel1.setLayout(new BorderLayout());
        panel2.setLayout(new BorderLayout());
        panel3.setLayout(new BorderLayout());
        panel4.setLayout(new BorderLayout());
        panel1.setSize(image.getWidth(), image.getHeight() + buttonPanel1.getHeight());



        System.out.println("The hight of button " + buttonPanel1.getHeight());
        panel1.setBounds(0, 0, image.getWidth(), image.getHeight());
        panel2.setSize(image.getWidth(), image.getHeight());
        panel2.setBounds(image.getWidth(), 0, image.getWidth(), image.getHeight());
        panel1.setBackground(Color.RED);
        panel2.setBackground(Color.GREEN);
        panel3.setSize(image.getWidth(), image.getHeight());
        panel1.setBounds(image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight());
        panel4.setSize(image.getWidth(), image.getHeight());
        panel1.setBounds(image.getWidth()*2, image.getHeight(), image.getWidth(), image.getHeight());
        panel3.setBackground(Color.BLUE);
        panel4.setBackground(Color.WHITE);
        panel_new.add(BorderLayout.SOUTH, panel1);
        panel_new.add(BorderLayout.SOUTH, panel2);
        panel_new.add(BorderLayout.NORTH, panel3);
        panel_new.add(BorderLayout.NORTH, panel4);
        JButton button_calc_hist = new JButton("Calculate Histogram");
        JButton button_del_hist = new JButton("Delete Histogram");
        JButton button_first_window = new JButton("First window");
        JButton  button_second_window = new JButton("Second window");
        JButton  button_third_window = new JButton("Third  window");
        JButton button_add = new JButton("Add tool");
        JButton button_delete = new JButton("Delete tool");
        button_add.setPreferredSize(new Dimension(135,30));
        button_delete.setPreferredSize(new Dimension(135,30));
		/*panel_new2.add(BorderLayout.CENTER, button_first_window);
		panel_new2.add(BorderLayout.CENTER, button_second_window);
		panel_new2.add(BorderLayout.CENTER, button_third_window);*/
        //panel_new2.add(BorderLayout.CENTER, button_add);
        panel_new2.add(BorderLayout.CENTER, button_add);
        panel_new2.add(BorderLayout.CENTER, button_delete);
        frame.add(BorderLayout.EAST, panel_new2);
        frame.add(BorderLayout.EAST, panel_new);
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        canvas1 = new GLCanvas(capabilities);
        canvas2 = new GLCanvas(capabilities);
        canvas3 = new GLCanvas(capabilities);

        panel1.add(BorderLayout.NORTH, a);
        a.setBackground(Color.BLACK);
        a.setForeground(Color.WHITE);
        panel2.add(BorderLayout.NORTH, b);
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        panel3.add(BorderLayout.NORTH, c);
        c.setBackground(Color.BLACK);
        c.setForeground(Color.WHITE);
        panel_new2.add(button_calc_hist);
        panel_new2.add(button_del_hist);
        ActionListener actionListener = new TestActionListener();
        buttonPanel2.addActionListener(actionListener);
        buttonPanel3.addActionListener(actionListener);
        panel4.add(text);

        button_calc_hist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GetHistogram myPic2 = new GetHistogram(new Dimension(256, 256),"red", Imageformation.axialImage(imageNumber1), Imageformation.axialImage(imageNumber1+1));
                panel4.add(new JScrollPane(myPic2));
            }
        });

        button_del_hist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel4.add(new JScrollPane(null));
                tx1.setText("");
                tx2.setText("");
                tx3.setText("");
                tx4.setText("");
                tx5.setText("");
            }
        });

        tx1.setBackground(Color.WHITE);
        tx2.setBackground(Color.WHITE);
        tx3.setBackground(Color.WHITE);
        tx4.setBackground(Color.WHITE);
        tx5.setBackground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        tx1.setBorder(border);
        tx2.setBorder(border);
        tx3.setBorder(border);
        tx4.setBorder(border);
        tx5.setBorder(border);

        JLabel labelx1 = new JLabel("   Мат. ожидание:");
        JLabel labelx2 = new JLabel("   Энтропия:");
        JLabel labelx3 = new JLabel("   Энергия:");
        JLabel labelx4 = new JLabel("   Коэффициент асимметрии:");
        //JLabel labelx5 = new JLabel("   Средняя абсолютная разница:");
        JLabel labelx5 = new JLabel("   Ковариация:");
        panel_new2.add(labelx1);
        panel_new2.add(tx1);
        panel_new2.add(labelx2);
        panel_new2.add(tx2);
        panel_new2.add(labelx3);
        panel_new2.add(tx3);
        panel_new2.add(labelx4);
        panel_new2.add(tx4);
        panel_new2.add(labelx5);
        panel_new2.add(tx5);



        canvas1.addMouseListener(this);
        canvas1.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                switch (e.getKeyCode() ) {
                    case KeyEvent.VK_F1:
                        //lines.drawRight(coordinate_x, coordinate_y);
                        flag = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        //lines.drawLeft(coordinate_x, coordinate_y);
                        flag = false;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

        });
        canvas2.addMouseListener(this);
        canvas2.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                switch (e.getKeyCode() ) {
                    case KeyEvent.VK_F1:
                        flag = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        flag = false;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

        });
        canvas3.addMouseListener(this);
        canvas3.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                switch (e.getKeyCode() ) {
                    case KeyEvent.VK_F1:
                        flag = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        flag = false;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

        });
        lines l1_window = new lines();
        l1_window.setImageNumber(imageNumber1);
        l1_window.setWindow(1);
        l1_window.run();
        canvas1.addGLEventListener(l1_window);
        canvas1.setSize(new Dimension(image.getWidth(), image.getHeight()));
        panel1.add(BorderLayout.SOUTH, buttonPanel1);
        panel1.add(canvas1);
        buttonPanel1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel1.setForeground(Color.RED);
                buttonPanel2.setForeground(Color.GRAY);
                buttonPanel3.setForeground(Color.GRAY);
                a.setBackground(Color.BLACK);
                a.setForeground(Color.WHITE);
                a.setText(imageNumber1 + "/20");
            	/*GetHistogram myPic2 = new GetHistogram(new Dimension(256, 256),"red", Imageformation.axialImage(imageNumber1));
            	panel4.add(new JScrollPane(myPic2));*/
                imageNumber1++;
                if (imageNumber1>=21)
                {
                    imageNumber1=1;
                }
                //lines l = new lines();
                canvas1.addGLEventListener(lines_axial[imageNumber1]);
                lines_axial[imageNumber1].setAdd(lines.add);
                lines_axial[imageNumber1].setImageNumber(imageNumber1);
                lines_axial[imageNumber1].setWindow(1);
                lines_axial[imageNumber1].run();
                canvas1.display();
            }
        });
        lines l2_window = new lines();
        l2_window.setWindow(2);
        l2_window.run();
        canvas2.addGLEventListener(l2_window);
        canvas2.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        panel2.add(BorderLayout.SOUTH, buttonPanel2);
        panel2.add(canvas2);
        buttonPanel2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel2.setForeground(Color.RED);
                buttonPanel1.setForeground(Color.GRAY);
                buttonPanel3.setForeground(Color.GRAY);
                b.setText(imageNumber2 + "/256");
                imageNumber2++;
                if (imageNumber2>=255)
                {
                    imageNumber2=100;
                }

                //lines l = new lines();
                canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
                lines_sagital_coronal[imageNumber2][0].setAdd(lines.add);
                lines_sagital_coronal[imageNumber2][0].setImageNumber2(imageNumber2);
                lines_sagital_coronal[imageNumber2][0].setWindow(2);
                lines_sagital_coronal[imageNumber2][0].run();
                canvas2.display();
            }
        });

        lines l3 = new lines();
        l3.setWindow(3);
        l3.run();
        canvas3.addGLEventListener(l3);
        canvas3.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        panel3.add(BorderLayout.SOUTH, buttonPanel3);
        panel3.add(canvas3);
        canvas3.display();
        buttonPanel3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel3.setForeground(Color.RED);
                buttonPanel2.setForeground(Color.GRAY);
                buttonPanel1.setForeground(Color.GRAY);
                c.setText(imageNumber3 + "/256");
                imageNumber3++;
                if (imageNumber3>=255)
                {
                    imageNumber3=100;
                }

                //lines l = new lines();
                canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);
                lines_sagital_coronal[imageNumber3][1].setImageNumber3(imageNumber3);
                lines_sagital_coronal[imageNumber3][1].setAdd(lines.add);
                lines_sagital_coronal[imageNumber3][1].setWindow(3);
                lines_sagital_coronal[imageNumber3][1].run();
                canvas3.display();
            }
        });

        //panel4.add(text);
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);
        frame.setVisible(true);
        button_first_window.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel1.setForeground(Color.RED);
                buttonPanel2.setForeground(Color.GRAY);
                buttonPanel3.setForeground(Color.GRAY);
                //lines l = new lines();
                canvas1.addGLEventListener(lines_axial[imageNumber1]);
                lines_axial[imageNumber1].setImageNumber(imageNumber1);
                lines_axial[imageNumber1].setAdd(lines.add);
                lines_axial[imageNumber1].setWindow(1);
                lines_axial[imageNumber1].run();
                canvas1.display();
            }
        });
        button_second_window.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel1.setForeground(Color.GRAY);
                buttonPanel2.setForeground(Color.RED);
                buttonPanel3.setForeground(Color.GRAY);
                //lines l = new lines();
                canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
                lines_sagital_coronal[imageNumber2][0].setImageNumber2(imageNumber2);
                lines_sagital_coronal[imageNumber2][0].setAdd(lines.add);
                lines_sagital_coronal[imageNumber2][0].setWindow(2);
                lines_sagital_coronal[imageNumber2][0].run();
                canvas2.display();
            }
        });
        button_third_window.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel1.setForeground(Color.GRAY);
                buttonPanel2.setForeground(Color.GRAY);
                buttonPanel3.setForeground(Color.RED);
                //lines l = new lines();
                canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);
                lines_sagital_coronal[imageNumber3][1].setImageNumber3(imageNumber3);
                lines_sagital_coronal[imageNumber3][1].setAdd(lines.add);
                lines_sagital_coronal[imageNumber3][1].setWindow(3);
                lines_sagital_coronal[imageNumber3][1].run();
                canvas3.display();
            }
        });

        button_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //lines l = new lines();
                switch(lines.window)
                {
                    case 1:
                        canvas1.addGLEventListener(lines_axial[imageNumber1]);
                        lines_axial[imageNumber1].setWindow(lines.window);
                        lines_axial[imageNumber1].setAdd(1);
                        lines_axial[imageNumber1].setImageNumber(imageNumber1);
                        lines_axial[imageNumber1].run();
                        canvas1.display();
                        //canvas1.addGLEventListener(lines_axial[imageNumber1]);
                        System.out.println("Instrument should be on first window!");
                        break;
                    case 2:
                        canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
                        lines_sagital_coronal[imageNumber2][0].setWindow(lines.window);
                        lines_sagital_coronal[imageNumber2][0].setAdd(1);
                        lines_sagital_coronal[imageNumber2][0].setImageNumber2(imageNumber2);
                        lines_sagital_coronal[imageNumber2][0].run();
                        canvas2.display();
                        //canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
                        System.out.println("Instrument should be on secind window!");
                        break;
                    case 3:
                        canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);
                        lines_sagital_coronal[imageNumber3][1].setWindow(lines.window);
                        lines_sagital_coronal[imageNumber3][1].setAdd(1);
                        lines_sagital_coronal[imageNumber3][1].setImageNumber3(imageNumber3);
                        lines_sagital_coronal[imageNumber3][1].run();
                        canvas3.display();
                        //canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);
                        System.out.println("Instrument should be on third window!");
                        break;
                    case 0:
                        System.out.println("Window is 0");
                }
                //l.setWindow(lines.window);
                //l.setAdd(1);
                //l.run();
            }
        });

        button_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //lines l = new lines();
                switch(lines.window)
                {
                    case 1:
                        canvas1.addGLEventListener(lines_axial[imageNumber1]);
                        lines_axial[imageNumber1].setWindow(lines.window);
                        lines_axial[imageNumber1].setImageNumber(imageNumber1);
                        lines_axial[imageNumber1].setAdd(0);
                        lines_axial[imageNumber1].run();
                        canvas1.display();
                        //canvas1.addGLEventListener(lines_axial[imageNumber1]);
                        System.out.println("Instrument should be on first window!");
                        break;
                    case 2:
                        canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
                        lines_sagital_coronal[imageNumber2][0].setWindow(lines.window);
                        lines_sagital_coronal[imageNumber2][0].setImageNumber2(imageNumber2);
                        lines_sagital_coronal[imageNumber2][0].setAdd(0);
                        lines_sagital_coronal[imageNumber2][0].run();
                        canvas2.display();
                        //canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
                        System.out.println("Instrument should be on second window!");
                        break;
                    case 3:
                        canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);
                        lines_sagital_coronal[imageNumber3][1].setWindow(lines.window);
                        lines_sagital_coronal[imageNumber3][1].setImageNumber3(imageNumber3);
                        lines_sagital_coronal[imageNumber3][1].setAdd(0);
                        lines_sagital_coronal[imageNumber3][1].run();
                        canvas3.display();
                        //canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);
                        System.out.println("Instrument should be on third window!");
                        break;
                    case 0:
                        System.out.println("Window is 0");
                }
                //l.setWindow(lines.window);
                //l.setAdd(lines.add);
                //l.setAdd(0);
                //l.run();
            }

        });


    }

    public class TestActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("The button was pressed!");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if (flag==true){
            //lines l = new lines();
            canvas1.addGLEventListener(lines_axial[imageNumber1]);
            canvas2.addGLEventListener(lines_sagital_coronal[imageNumber2][0]);
            canvas3.addGLEventListener(lines_sagital_coronal[imageNumber3][1]);

            if((e.getModifiers()& e.BUTTON1_MASK)!=0) {
			/*x1 = e.getX() - 128;
			y1 = 128 - e.getY() + 23;*/
                //l.setFirstPoint(x1, y1);
                //l.setWindow(lines.window);
                //lines_axial[imageNumber1].setAdd(lines.add);
                switch(lines.window)
                {
                    case 1:
                        lines_axial[imageNumber1].setWindow(lines.window);
                        x11 = e.getX();
                        y11 = e.getY();
                        System.out.println("Coordinates x11 "+x11 +" y11 "+y11);
                        first_x = x11;
                        first_y = y11;
                        x1 = e.getX() - 128;
                        y1 = 128 - e.getY();
                        lines_axial[imageNumber1].setFirstPoint(x1, y1);
                        lines_axial[imageNumber1].setAdd(lines.add);
                        lines_axial[imageNumber1].setImageNumber(imageNumber1);
                        break;
                    case 2:
                        lines_sagital_coronal[imageNumber2][0].setWindow(lines.window);
                        x11 = e.getX();
                        y11 = e.getY();
                        x1 = e.getX() - 128;
                        y1 = 128 - e.getY();
                        lines_sagital_coronal[imageNumber2][0].setFirstPoint(x1, y1);
                        lines_sagital_coronal[imageNumber2][0].setAdd(lines.add);
                        lines_sagital_coronal[imageNumber2][0].setImageNumber2(imageNumber2);
                        break;
                    case 3:
                        lines_sagital_coronal[imageNumber3][1].setWindow(lines.window);
                        x11 = e.getX();
                        y11 = e.getY();
                        x1 = e.getX() - 128;
                        y1 = 128 - e.getY();
                        lines_sagital_coronal[imageNumber3][1].setFirstPoint(x1, y1);
                        lines_sagital_coronal[imageNumber3][1].setAdd(lines.add);
                        lines_sagital_coronal[imageNumber3][1].setImageNumber3(imageNumber3);
                        break;
                    case 0:
                        System.out.println("Window is 0");
                }

                //text.setText("X1="+x1+"\nY1="+y1 +"\n");
            }

            if((e.getModifiers()& e.BUTTON3_MASK)!=0) {

//			l.setSecondPoint(x2, y2);
                //lines l1 = new lines(x1, x2, y1, y2);
                //l.setWindow(lines.window);
//			l.setAdd(lines.add);
                //l.setAdd(1);

                switch(lines.window)
                {
                    case 1:
                        lines_axial[imageNumber1].setWindow(lines.window);
                        x22 = e.getX();
                        y22 = e.getY();
                        System.out.println("Coordinates x22 "+x22 +" y22 "+y22);
                        second_x = x11;
                        second_y = y11;
                        x2 = e.getX() - 128;
                        y2 = 128 - e.getY();
                        lines_axial[imageNumber1].setSecondPoint(x2, y2);
                        lines_axial[imageNumber1].setAdd(lines.add);
                        lines_axial[imageNumber1].setImageNumber(imageNumber1);
                        canvas1.display();
                        lines_axial[imageNumber1].run();
                        //text.setText("X1="+x1+"\nY1="+y1 +"\n" + "X2="+x2+"\nY2="+y2 +"\n" + lines_axial[imageNumber1].dlina(x1, x2, y1, y2) + " pixels" + "\n" + lines_axial[imageNumber1].dlina(x1, x2, y1, y2) * Imageformation.pixelSpacing + " mm");
                        break;
                    case 2:
                        lines_sagital_coronal[imageNumber2][0].setWindow(lines.window);
                        x22 = e.getX();
                        y22 = e.getY();
                        x2 = e.getX() - 128;
                        y2 = 128 - e.getY();
                        lines_sagital_coronal[imageNumber2][0].setSecondPoint(x2, y2);
                        lines_sagital_coronal[imageNumber2][0].setAdd(lines.add);
                        lines_sagital_coronal[imageNumber2][0].setImageNumber2(imageNumber2);
                        canvas2.display();
                        lines_sagital_coronal[imageNumber2][0].run();
                        //text.setText("X1="+x1+"\nY1="+y1 +"\n" + "X2="+x2+"\nY2="+y2 +"\n" + lines_sagital_coronal[imageNumber2][0].dlina(x1, x2, y1, y2) + " pixels" + "\n" + lines_axial[imageNumber1].dlina(x1, x2, y1, y2) * Imageformation.pixelSpacing + " mm");
                        break;
                    case 3:
                        lines_sagital_coronal[imageNumber3][1].setWindow(lines.window);
                        x22 = e.getX();
                        y22 = e.getY();
                        x2 = e.getX() - 128;
                        y2 = 128 - e.getY();
                        lines_sagital_coronal[imageNumber3][1].setSecondPoint(x2, y2);
                        lines_sagital_coronal[imageNumber3][1].setAdd(lines.add);
                        lines_sagital_coronal[imageNumber3][1].setImageNumber3(imageNumber3);
                        canvas3.display();
                        lines_sagital_coronal[imageNumber3][1].run();
                        //text.setText("X1="+x1+"\nY1="+y1 +"\n" + "X2="+x2+"\nY2="+y2 +"\n" + lines_sagital_coronal[imageNumber3][1].dlina(x1, x2, y1, y2) + " pixels" + "\n" + lines_axial[imageNumber1].dlina(x1, x2, y1, y2) * Imageformation.pixelSpacing + " mm");
                        break;
                    case 0:
                        System.out.println("Window is 0");
                }

	    	/*l.run();
	    	text.setText("X1="+x1+"\nY1="+y1 +"\n" + "X2="+x2+"\nY2="+y2 +"\n" + l.dlina(x1, x2, y1, y2) + " pixels" + "\n");*/
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
//		int x_center = e.getX() - 128;
//		int y_center = 128-e.getY()+23;
//		System.out.println("Pressed");
//		text.setText("X="+(e.getX() - 128 )+"\n Y="+(128-e.getY()+23) +"\n");
//		//new coordiantes
//		lines.draw(x_center, y_center);
//		coordinate_x = x_center;
//		coordinate_y = y_center;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
