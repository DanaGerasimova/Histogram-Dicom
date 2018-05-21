import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import ij.plugin.DICOM;
import ij.util.DicomTools;

public class lines
        extends JFrame implements GLEventListener
{
    static Texture currentTexture = null;
    int number=0;
    int imageNumberCanvas1=1;
    int imageNumberCanvas2=50;
    int imageNumberCanvas3=1;
    private static final long serialVersionUID = 1L;
    static int x1=0;
    static int w=0, h=0;
    static int changed_x1 =0;
    static int y1 = 0;
    static int x2 = 0;
    static int changed_x2 = 0;
    static int y2 = 0;
    static int new_x1= -40;
    static int new_y1 = -40;
    static int new_x2 = 40;
    static int new_y2 = 40;
    static GL2 gl = null;
    static int counter1 = 0;
    static int counter2 = 0;
    static int first_corner = 0;
    static int second_corner = 0;
    int []first_corner_array={2, 1};
    static int count_x1 =0;
    static int count_x2 = 0;
    public static int window = 0;
    static int add = 1;
    public static int mm = 0;
    public static int[] hist = new int[256];
    public static DICOM image = new DICOM();
    BufferedImage result = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);

    public lines(){

    }

    public lines(int x_1, int x_2, int y_1, int y_2){
        x1 = x_1;
        x2 = x_2;
        y1 = y_1;
        y2 = y_2;
    }

    public void setWindow(int canvasWindow)
    {
        window = canvasWindow;
        System.out.println("the window is " + window);
    }

    public void setAdd(int addInstrument)
    {
        add = addInstrument;
    }

    public void setFirstPoint(int x, int y)
    {
        x1 = x;
        y1 = y;
    }
    public void setSecondPoint(int x, int y)
    {
        x2 = x;
        y2 = y;
    }

	/*public int dlina(int x1, int x2, int y1, int y2){
		return mm = (int)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}*/

    public double mo(int x1, int x2, int y1, int y2){
        w = Math.abs(GUIclass.x22 - GUIclass.x11);
        h = Math.abs(GUIclass.y22 - GUIclass.y11);

        int[][] ima = new int[h][w];
        int sum = 0;
        double mat_oz = 0;


        return mat_oz = sum / (w * h);
    }

    public void run()
    {
        //lines l = new lines();
        System.out.println("The number of window is " + window);
    }


    public void setImageNumber(int imageN)
    {
        imageNumberCanvas1=imageN;
        System.out.println("the number is " + imageNumberCanvas1);
    }

    public void setImageNumber2(int imageN)
    {
        imageNumberCanvas2=imageN;
        System.out.println("the number of second window image is " + imageNumberCanvas2);
    }

    public void setImageNumber3(int imageN)
    {
        imageNumberCanvas3=imageN;
        System.out.println("the number sagital " + imageNumberCanvas3);
    }



    public static void drawLineWithCoherence4(GL2 gl, int x1, int x2, int y1, int y2)
    {
        gl.glLineWidth(3f);
        gl.glBegin(GL2.GL_LINES);
        gl.glLineWidth(10f);
        gl.glVertex2i(x1, y1);
        gl.glVertex2i(x2, y1);
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glLineWidth(10f);
        gl.glVertex2i(x2, y1);
        gl.glVertex2i(x2, y2);
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glLineWidth(10f);
        gl.glVertex2i(x2, y2);
        gl.glVertex2i(x1, y2);
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glLineWidth(10f);
        gl.glVertex2i(x1, y2);
        gl.glVertex2i(x1, y1);
        gl.glEnd();
        gl.glFlush();

    }

    @Override
    public void display(GLAutoDrawable drawable)
    {
        gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        gl.glOrtho(-128.0, 128.0, -128.0, 128.0, -128.0, 128.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glEnable(GL2.GL_TEXTURE_2D);
        currentTexture.bind(gl);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glTexCoord2i(0, 1);
        gl.glVertex2i(-128, 128);
        gl.glTexCoord2i(0, 0);
        gl.glVertex2i(-128, -128);
        gl.glTexCoord2i(1, 0);
        gl.glVertex2i(128, -128);
        gl.glTexCoord2i(1, 1);
        gl.glVertex2i(128, 128);
        gl.glEnd();
        gl.glFlush();

        if (add == 1){
            drawLineWithCoherence4(gl, x1, x2, y1, y2);
        }
        else {}
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        // TODO Auto-generated method stub

        gl = glAutoDrawable.getGL().getGL2();
        //gl2.glEnable(GL.GL_TEXTURE_2D);
        //gl2.glEnable(GL.GL_DEPTH_TEST);

        switch(window)
        {
            case 1:
                //currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.generateImagesFor3D().get(0), false);
                currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.axialImage(imageNumberCanvas1), false);
                //Imageformation.sagitalImage();
                System.out.println("Texture xz");
                System.out.println("The next image is " + imageNumberCanvas1);
                break;
            case 2:
                //currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.generateImagesFor3D().get(1), false);
                currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.coronarImage(imageNumberCanvas2), false);
                System.out.println("Texture yz");
                //System.out.println("The next image is " + imageNumberCanvas2);
                break;
            case 3:
                currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.sagitalImage(imageNumberCanvas3), false);
                //currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.generateImagesFor3D().get(2), false);
                System.out.println("Texture xy");
                break;
            case 0:
                // currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.axialImage(imageNumberCanvas1), false);
                System.out.println("Zero window");

        }

        //currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), Imageformation.axialImage(imageNumberCanvas1), false);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int arg1, int arg2, int arg3, int arg4) {
        // TODO Auto-generated method stub
        gl = glAutoDrawable.getGL().getGL2();
        gl.glEnable(GL2.GL_LINES);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2i(0, 256);
        gl.glVertex2i(0, -256);
        gl.glVertex2i(256, 0);
        gl.glVertex2i(-256, 0);
        gl.glLineWidth(50);
        gl.glColor3f(1,1,1);
        gl.glEnd();
    }


    public static void draw(int x, int y)
    {
        counter1 = 0;
        counter2 = 0;
        x1 = changed_x1 + x;
        y1 = y1 + y;
        x2 = changed_x2 + x;
        y2 = y2 + y;
        new_x1 = new_x1 + x;
        new_x2 = new_x2 + x;
        new_y1 = new_y1 + y;
        new_y2 = new_y2 + y;
        lines l = new lines();
        //GUIclass.canvas1.addGLEventListener(l);
        //countCorner(x1, y1, new_x1, new_y1, x, y);
        switch(window)
        {
            case 1:
                GUIclass.canvas1.display();
                break;
            case 2:
                GUIclass.canvas2.display();
                break;
            case 3:
                GUIclass.canvas3.display();
                break;
            case 0:
                System.out.println("Window is 0");
        }
        x1=-40;
        y1 = 40;
        x2 = 40;
        y2 = -40;
        new_x1= -40;
        new_y1 = -40;
        new_x2 = 40;
        new_y2 = 40;
    }

    public static void drawRight(int x, int y){

        count_x1 = 0;
        counter1 ++;
        x1 = x1 + x +1;
        changed_x1 = x1;
        y1 = y1 + y;
        x2 = x2 + x -1 ;
        changed_x2 = x2;
        y2 = y2 + y;
        new_x1 = new_x1 + x;
        new_x2 = new_x2 + x;
        new_y1 = new_y1 + y;
        new_y2 = new_y2 + y;

        lines l = new lines();
        //GUIclass.canvas1.addGLEventListener(l);
        countCorner(x1 - x, y1 - y, new_x2 - x, new_y2 - y, 0, 0);
        //countCorner(x1, y1, new_x2, new_y2, x, y);
        switch(window)
        {
            case 1:
                GUIclass.canvas1.display();
                break;

            case 2:
                GUIclass.canvas2.display();
                break;
            case 3:
                GUIclass.canvas3.display();
                break;
            case 0:
                System.out.println("Window is 0");
        }

        x1=-40 + counter1;
        y1 = 40;
        x2 = 40 - counter1;
        y2 = -40;
        count_x1 = x1;

        new_x1= -40;
        new_y1 = -40;
        new_x2 = 40;
        new_y2 = 40;

    }

    public static void drawLeft(int x,int y)
    {
        count_x2 = 0;
        counter2 ++;
        x1 = x1 + x -1;
        changed_x1 = x1;
        y1 = y1 + y;
        x2 = x2 + x +1 ;
        changed_x2 = x2;
        y2 = y2 + y;
        new_x1 = new_x1 + x;
        new_x2 = new_x2 + x;
        new_y1 = new_y1 + y;
        new_y2 = new_y2 + y;

        lines l = new lines();
        //GUIclass.canvas1.addGLEventListener(l);
        countCorner(x1 - x, y1 - y, new_x2 - x, new_y2 - y, 0, 0);
        //countCorner(x1, y1, new_x2, new_y2, x, y);
        switch(window)
        {
            case 1:
                GUIclass.canvas1.display();
                break;
            case 2:
                GUIclass.canvas2.display();
                break;
            case 3:
                GUIclass.canvas3.display();
                break;
            case 0:
                System.out.println("Window is 0");
        }

        x1=-40 - counter2;
        y1 = 40;
        x2 = 40 + counter2;
        y2 = -40;
        count_x2 = x2;

        new_x1= -40;
        new_y1 = -40;
        new_x2 = 40;
        new_y2 = 40;

    }

    public static void countCorner(int x1, int y1, int x2, int y2, int x_center,int y_center)
    {
        GUIclass.text.setText(null);
        int corner1 = 0;
        int corner2 = 0;
        double cos = 0;
        cos = (float) ((x1*x2 + y1*y2)/(Math.sqrt(x1*x1 + y1*y1)*(Math.sqrt(x2*x2 + y2*y2))));
        corner1 = (int) (Math.acos(cos)*180/Math.PI);
        corner2 = 180-corner1;
        GUIclass.text.append("The first corner is " + corner1+"\n");
        System.out.println("The first corner is " + corner1+"\n");
        GUIclass.text.append("The second corner is "+ corner2+"\n");
        System.out.println("The second corner is " + corner2+"\n");
        first_corner = corner1;
        second_corner = corner2;
    }

}




