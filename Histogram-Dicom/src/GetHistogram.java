import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JPanel;

//import edu.ucla.rip.imageio.DICOMImageReader;

public class GetHistogram extends JPanel {

    Dimension dimPic;
    BufferedImage picture, picture2;
    public static Graphics g;
    String colorReceived;
    int [] samples;
    int [] samples2;

    public GetHistogram(Dimension d, String color, BufferedImage image, BufferedImage image2)
    {
        picture = image;
        picture2 = image2;
        getHistogram();
    }

    public int hist(int w, int h){
        int alpha =  (0xff &(picture.getRGB(w, h)  >> 24));
        int ired =  (0xff &(picture.getRGB(w, h)  >> 16));
        int igreen = (0xff &(picture.getRGB(w, h)  >> 8));
        int iblue =  (0xff & picture.getRGB(w, h));
        int grey = (ired + igreen + iblue) / 3;
        return grey;
    }

    public int hist2(int w, int h){
        int alpha =  (0xff &(picture2.getRGB(w, h)  >> 24));
        int ired =  (0xff &(picture2.getRGB(w, h)  >> 16));
        int igreen = (0xff &(picture2.getRGB(w, h)  >> 8));
        int iblue =  (0xff & picture2.getRGB(w, h));
        int grey = (ired + igreen + iblue) / 3;
        return grey;
    }

    public void getHistogram()
    {
        samples = new int[256];
        samples2 = new int[256];
        int maxNumSamples = 0, maxNumSamples2 = 0;

        for(int i=0; i < 255; i++)
        {
            samples[i] = 0;
            samples2[i] = 0;
        }

        int gray, gray2;
        int wd=Math.abs(GUIclass.y22 - GUIclass.y11);
        int ht=Math.abs(GUIclass.x22 - GUIclass.x11);
        /*
        for(int w=0; w < ( picture.getWidth()); w++ )
        {
            for(int h=0; h < ( picture.getHeight()); h++ )
            {
                gray = hist(w,h);

                   samples[gray]++;
                   if(samples[gray] > maxNumSamples)
                   {
                       maxNumSamples = samples[gray];
                   }
            }
        } */

        for(int w=GUIclass.y11; w < GUIclass.y22; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22; h++ )
            {
                gray = hist(w,h);
                gray2 = hist2(w,h);

                samples[gray]++;
                if(samples[gray] > maxNumSamples)
                {
                    maxNumSamples = samples[gray];
                }

                samples2[gray2]++;
                if(samples2[gray2] > maxNumSamples2)
                {
                    maxNumSamples2 = samples2[gray2];
                }
            }
        }

        //вероятность 1
        double p = 0;
        for(int i=0; i < 255; i++)
        {
            p+=(double)samples[i];
        }
        p/=wd * ht;

        //вероятность 2
        double p2 = 0;
        for(int w=GUIclass.y11; w < GUIclass.y22; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22; h++ )
            {
                gray = hist(w,h);
                gray2 = hist2(w,h);
                if(gray != gray2) p2++;
            }
        }
        p2/=wd * ht * 2;
        System.out.println("" + p2);

        //normalizing

        for(int i=0; i < 255; i++)
        {
            samples[i] = (int)((samples[i]*200)/(maxNumSamples));
        }

        //мат.ожидание 1
        int sum = 0;
        for(int w=GUIclass.y11; w < GUIclass.y22; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22; h++ )
            {
                gray = hist(w,h);
                sum+=gray;
            }
        }
        double mat = (double)sum / (wd * ht);

        //мат.ожидание 2
        int sum2 = 0;
        for(int w=GUIclass.y11; w < GUIclass.y22; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22; h++ )
            {
                gray2 = hist2(w,h);
                sum2+=gray2;
            }
        }
        double mat2 = (double)sum2 / (wd * ht);

        //разница
        double razn = 0;
        double abs;
        for(int w=GUIclass.y11; w < GUIclass.y22-1; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22-1; h++ )
            {
                gray = hist(w,h);
                gray2 = hist2(w,h);
                abs = Math.abs(gray - gray2) * p2;
                razn+=abs;
            }
        }

        //ковариация
        double cov = 0;
        for(int w=GUIclass.y11; w < GUIclass.y22-1; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22-1; h++ )
            {
                gray = hist(w,h);
                gray2 = hist2(w,h);
                cov += (gray - mat) * (gray2 - mat2) * p2;
            }
        }

        //дисперсия
        double disp = 0, d = 0;
        for(int w=GUIclass.y11; w < GUIclass.y22; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22; h++ )
            {
                gray = hist(w,h);
                d = (gray - mat) * (gray - mat) * p;
                disp+=d;
            }
        }

        //асимметрия
        double asim = 0, a = 0;
        for(int w=GUIclass.y11; w < GUIclass.y22; w++ )
        {
            for(int h=GUIclass.x11; h < GUIclass.x22; h++ )
            {
                gray = hist(w,h);
                a = (gray - mat) * (gray - mat) * (gray - mat) * (gray - mat) * (gray - mat) * p;
                asim+=a;
            }
        }
        asim/=(disp * disp * disp);
        double entropy = p2 * Math.log(p2);
        double energy = p2 * p2;

        GUIclass.tx1.setText("" + mat);
        GUIclass.tx2.setText("" + entropy);
        GUIclass.tx3.setText("" + energy);
        GUIclass.tx4.setText("" + asim);
        GUIclass.tx5.setText("" + cov);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.RED);
        for(int i=0; i < 255; i++)
        {
            g.drawLine(255-i, 255,  255-i, 255-samples[i]);
        }
    }
}
