/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servershootingstar;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Goran
 */
public class BallDetector {
    static JFrame guiFrame;
    static JLabel guiLabel;
    static Image image;
    static ImageIcon guiImageIcon;
    static double cos = 0.0;
    
    public static BufferedImage Mat2BufferedImage(Mat m) {
        // source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
        // The output can be assigned either to a BufferedImage or to an Image

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public static void initSwing(Mat mat) {

        guiFrame = new JFrame("window");
        guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // image size is 640x480
        guiFrame.setSize(mat.width() + 20, mat.height() + 40);
        guiFrame.setLayout(new FlowLayout());


        guiLabel = new JLabel();
        guiFrame.add(guiLabel);
        guiFrame.setVisible(true);
        updateSwing(mat);
    }
    
    public static void updateSwing(Mat mat) {
        image = Mat2BufferedImage(mat);
        guiImageIcon = new ImageIcon(image);
        guiLabel.setIcon(guiImageIcon);
        guiLabel.repaint();
    }
    
    public static String getAngleFromRobot(int input) {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("before");
            int point;
            try {
                Mat frame = new Mat();
                System.out.println("AAAAAA");
                Mat originalFrame = new Mat();
                System.out.println("BBBBBB");
                VideoCapture videoCapture = new VideoCapture(0);
                System.out.println("CCCCCCCC");
                videoCapture.read(originalFrame);
//                System.out.println("original" + originalFrame.dump());
//                initSwing(originalFrame);
                int workaround = 20;
                while (workaround > 0) {
                    System.out.println("workaround " + workaround);
                    videoCapture.read(originalFrame);
//                    System.out.println(originalFrame.dump() + originalFrame.dump().length());
                    workaround--;
                }
//                Imgcodecs.imwrite("C:\\Users\\Goran\\Desktop\\Goran.jpg", originalFrame);
                Mat cropped = originalFrame.submat(originalFrame.rows() / 4, originalFrame.rows() / 4 * 3, 0, originalFrame.cols());
                   initSwing(cropped);
                Imgproc.cvtColor(cropped, frame, Imgproc.COLOR_BGR2HSV);

                // insert lower and upper bounds for colors
                Scalar greenLowerB = new Scalar(20, 55, 55);
                Scalar greenUpperB = new Scalar(40, 255, 255);

                Scalar redLowerB = new Scalar(160, 100, 35);
                Scalar red1LowerB = new Scalar(0, 100, 35);

                Scalar redUpperB = new Scalar(180, 255, 255);
                Scalar red1UpperB = new Scalar(20, 255, 255);

                Scalar blueLowerB = new Scalar(100, 100, 35);
                Scalar blueUpperB = new Scalar(120, 255, 155);

                Mat mask = new Mat();

                if (input == 1) {
                    Mat otherMask = new Mat();
                    Core.inRange(frame, redLowerB, redUpperB, mask);
                    Core.inRange(frame, red1LowerB, red1UpperB, otherMask);
                    Core.bitwise_or(mask, otherMask, mask);
                } else if (input == 2) {
                    Core.inRange(frame, greenLowerB, greenUpperB, mask);
                } else {
                    Core.inRange(frame, blueLowerB, blueUpperB, mask);
                }
                Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));

                int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
                for (int i = 0; i < mask.rows(); ++i) {
                    for (int j = 0; j < mask.cols(); ++j) {
                        double value = mask.get(i, j)[0];
                        //System.out.println(value);
                        if (value > 1) {
                            minX = Math.min(minX, i);
                            maxX = Math.max(maxX, i);
                            minY = Math.min(minY, j);
                            maxY = Math.max(maxY, j);
                        }
                    }
                }

                Imgproc.circle(mask, new Point((maxY + minY) / 2, (minX + maxX) / 2), 3, new Scalar(0, 0, 0));
                initSwing(mask);

                point = (minY + maxY) / 2;

                point = point - 320;

                cos = point / 320.0;
                System.out.println("OK");
            }
            catch(Exception ex) {
                point = (new Random()).nextInt(640);
                cos = -1;
                System.out.println("error imase, davam random brojka: " + point);
                ex.printStackTrace();
                
            }

//            System.out.println();
//            System.out.println("tockata u granica od [-320, 320]");
//            System.out.println(point);
//            System.out.println("cosinus vrednost");
//            System.out.println(cos);
//            System.out.println();
            System.out.println("cos = " + cos);
            if(cos == -1) {
                return "-1";
            }
            int res = (int)(2 * Math.toDegrees(Math.acos(cos)) / 3);
            System.out.println("Res: " + res);
            return String.valueOf(res);
    }
}
