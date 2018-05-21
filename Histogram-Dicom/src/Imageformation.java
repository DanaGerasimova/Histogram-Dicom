import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ij.util.DicomTools;
import ij.plugin.DICOM;

class Imageformation {
    public static final String SLICE_THICKNESS_TAG = "0018,0050";
    public static final String PIXEL_SPACING_TAG = "0028,0030";
    public static final String PATH = "images\\brain_0XX.dcm";
    public static double pixelSpacing = 0;
    public static DICOM image = new DICOM();
    public static List<Pixel> imageData = new ArrayList<>(image.getWidth()*image.getHeight());
    public static List<Pixel> xy = new ArrayList<>();
    public static BufferedImage result = new BufferedImage(512,512,BufferedImage.TYPE_INT_ARGB);


    public static double[][] generateScaleMatrix(double x, double y, double z)
    {
        double[][] scaleMatrix = new double[4][4];
        scaleMatrix[0][0]=x;
        scaleMatrix[1][1]=y;
        scaleMatrix[2][2]=z;
        scaleMatrix[3][3]=1;
        return scaleMatrix;
    }

    public static double[] generateOneDimensionMatrix(double[][] twoDimsMatrix){
        double[] result = new double[twoDimsMatrix.length*twoDimsMatrix[0].length];
        for(int i=0;i<twoDimsMatrix.length;i++)
            for(int j=0;j<twoDimsMatrix[0].length;j++)
                result[i*twoDimsMatrix[0].length+j] = twoDimsMatrix[j][i];
        return result;
    }

    public static BufferedImage axialImage (int i)
    {
        DICOM image = new DICOM();
        image.open(generateImageName(i));
        return createImageFromPixelsList(createPixelsListFromImage(image), image.getWidth(), image.getHeight());

    }

    public static BufferedImage sagitalImage(int m)
    {
        List<Pixel> zy = new ArrayList<>();
        DICOM image = new DICOM();
        for(int i=1;i<21;i++){
            //DICOM image = new DICOM();
            image.open(generateImageName(i));
            String sliceSpacingTagValue = DicomTools.getTag(image, SLICE_THICKNESS_TAG);
            int sliceSpacing = (int) Double.parseDouble(sliceSpacingTagValue);

            String pixelSpacingTagValue = DicomTools.getTag(image, PIXEL_SPACING_TAG);
            pixelSpacing = Double.valueOf(pixelSpacingTagValue.split("\\\\")[0]);


            int pixelThickness =(int) Math.round(sliceSpacing/pixelSpacing);

            List<Pixel> pixels = createPixelsListFromImage(image);
            List<Pixel> pixelsForZY = new ArrayList<>();
            //the slice from 1 to 255
            int imageCenter = m;
            //int imageCenter = image.getWidth()/2 - 1;
            for(int k=0;k<image.getHeight();k++)
                pixelsForZY.add(pixels.get(k * image.getWidth() + imageCenter));
            //thickness changes for pixelThickness
            for(int k=0;k<pixelThickness;k++){
                zy.addAll(pixelsForZY);
            }
        }
        List<Pixel> fillBlackPixels = new ArrayList<>();

        int blackRowsQuantity = (256 - zy.size()/256)/2;

        if(blackRowsQuantity>0)
            for(int j=0;j<blackRowsQuantity*256;j++)
                fillBlackPixels.add(new Pixel(0));

        zy.addAll(0,fillBlackPixels);
        zy.addAll(fillBlackPixels);

        // pixelsForZY.add(createImageFromPixelsList(zy,256,zy.size()/256));

        return createImageFromPixelsList(zy, image.getWidth(), image.getHeight());

    }

    public static BufferedImage coronarImage(int m){

        DICOM image = new DICOM();
        List<Pixel> xy = new ArrayList<>();

        for(int i=1;i<21;i++){
            //loading image
            //DICOM image = new DICOM();
            image.open(generateImageName(i));

            String sliceSpacingTagValue = DicomTools.getTag(image, SLICE_THICKNESS_TAG);
            int sliceSpacing = (int) Double.parseDouble(sliceSpacingTagValue);

            String pixelSpacingTagValue = DicomTools.getTag(image, PIXEL_SPACING_TAG);
            pixelSpacing = Double.valueOf( pixelSpacingTagValue.split("\\\\")[0]);

            int pixelThickness =(int) Math.round(sliceSpacing/pixelSpacing);

            List<Pixel> pixels = createPixelsListFromImage(image);

            //generating vertical and horizontal lines for XY,ZY images
            int imageCenter = m;
            //int imageCenter = image.getWidth()/2 - 1;

            List<Pixel> pixelsForXY = pixels.subList( imageCenter * image.getWidth(), (imageCenter + 1) * image.getWidth());


//thickness changes for pixelThickness
            for(int k=0;k<pixelThickness;k++){

                xy.addAll(pixelsForXY);
            }
        }

        List<Pixel> fillBlackPixels = new ArrayList<>();

        int blackRowsQuantity = (256 - xy.size()/256)/2;

        if(blackRowsQuantity>0)
            for(int i=0;i<blackRowsQuantity*256;i++)
                fillBlackPixels.add(new Pixel(0));


        xy.addAll(0,fillBlackPixels);
        xy.addAll(fillBlackPixels);

        return createImageFromPixelsList(xy, image.getWidth(), image.getHeight());
    }

    /**
     * @return images for XZ, YZ, XY       left-top-right = z-y-x
     */
    public static List<BufferedImage> generateImagesFor3D(){
        List<BufferedImage> result = new ArrayList<>(3);

        List<Pixel> zy = new ArrayList<>();
        List<Pixel> xy = new ArrayList<>();

        for(int i=1;i<21;i++){
            //loading image
            DICOM image = new DICOM();
            image.open(generateImageName(i));

            String sliceSpacingTagValue = DicomTools.getTag(image, SLICE_THICKNESS_TAG);
            int sliceSpacing = (int) Double.parseDouble(sliceSpacingTagValue);

            String pixelSpacingTagValue = DicomTools.getTag(image, PIXEL_SPACING_TAG);
            double pixelSpacing = Double.valueOf( pixelSpacingTagValue.split("\\\\")[0]);

            int pixelThickness =(int) Math.round(sliceSpacing/pixelSpacing);

            List<Pixel> pixels = createPixelsListFromImage(image);

            //getting XZ image
            if(i==10)
                result.add(image.getBufferedImage());

            //generating vertical and horizontal lines for XY,ZY images
            int imageCenter = image.getWidth()/2 - 1;

            List<Pixel> pixelsForXY = pixels.subList( imageCenter * image.getWidth(), (imageCenter + 1) * image.getWidth());
            List<Pixel> pixelsForZY = new ArrayList<>();

            for(int k=0;k<image.getHeight();k++)
                pixelsForZY.add(pixels.get(k * image.getWidth() + imageCenter));
//thickness changes for pixelThickness
            for(int k=0;k<pixelThickness;k++){
                zy.addAll(pixelsForZY);
                xy.addAll(pixelsForXY);
            }
        }

        List<Pixel> fillBlackPixels = new ArrayList<>();

        int blackRowsQuantity = (256 - zy.size()/256)/2;

        if(blackRowsQuantity>0)
            for(int i=0;i<blackRowsQuantity*256;i++)
                fillBlackPixels.add(new Pixel(0));

        zy.addAll(0,fillBlackPixels);
        zy.addAll(fillBlackPixels);

        xy.addAll(0,fillBlackPixels);
        xy.addAll(fillBlackPixels);

        result.add(createImageFromPixelsList(xy,256,xy.size()/256));
        result.add(createImageFromPixelsList(zy,256,zy.size()/256));

        return result;
    }

    public static String generateImageName(int number){
        if(number<10)
            return PATH.replaceAll("XX","0"+String.valueOf(number));
        else
            return PATH.replaceAll("XX",String.valueOf(number));
    }

    public static int getMax(List<Pixel> imageData){
        return Collections.max(imageData,new Comparator<Pixel>() {
            @Override
            public int compare(Pixel o1, Pixel o2) {
                return Integer.compare(o1.getGrayScale(),o2.getGrayScale());
            }
        }).getGrayScale();
    }

    public static int getMin(List<Pixel> imageData){
        return Collections.min(imageData, new Comparator<Pixel>() {
            @Override
            public int compare(Pixel o1, Pixel o2) {
                return Integer.compare(o1.getGrayScale(), o2.getGrayScale());
            }
        }).getGrayScale();
    }

    public static BufferedImage createImageFromPixelsList(List<Pixel> imageData, int width, int height) {
        BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                result.setRGB(j,i, imageData.get(i*width+j).getARGB());

        return result;
    }

    public static List<Pixel> createPixelsListFromImage(DICOM image){
        List<Pixel> imageData = new ArrayList<>(image.getWidth()*image.getHeight());

        for(int i=0;i<image.getHeight();i++)
            for(int j=0;j<image.getWidth();j++)
                imageData.add(new Pixel(image.getPixel(i,j)[0]));

        double minGrayScale = getMin(imageData);
        double maxGrayScale = getMax(imageData);
        maxGrayScale-=minGrayScale;

        for(Pixel pixel:imageData){
            pixel.setColor( (int) Math.round( (pixel.getGrayScale() - minGrayScale)/maxGrayScale *255));
        }

        return imageData;
    }

}

