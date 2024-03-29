package com.main.kparhar;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		VideoCapture capture = new VideoCapture(0);
		
		JFrame jframe = new JFrame("Facial Detection? More like Racial Detection...");
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JLabel vidpanel = new JLabel();
	    jframe.setContentPane(vidpanel);
	    jframe.setVisible(true);

	    CascadeClassifier eyeClassifier = new CascadeClassifier("C:/OpenCV/opencv/build/etc/haarcascades/haarcascade_eye.xml");
	    CascadeClassifier faceClassifier = new CascadeClassifier("C:/OpenCV/opencv/build/etc/haarcascades/haarcascade_frontalface_default.xml");
	    
	    Mat frame = new Mat();
	    while (true) {
	        if (capture.read(frame)) {
	            
	            MatOfRect faceDetections = new MatOfRect();
	            faceClassifier.detectMultiScale(frame, faceDetections);
	            
	            for (Rect rect : faceDetections.toArray()) {
	                Imgproc.rectangle(
	                   frame,                                               // where to draw the box
	                   new Point(rect.x, rect.y),                            // bottom left
	                   new Point(rect.x + rect.width, rect.y + rect.height), // top right
	                   new Scalar(0, 255, 0),
	                   3                                                     // RGB colour
	                );
	            }
	            
	            
	            Imgproc.line(frame, new Point(0, 1), new Point(),new Scalar(0, 255, 0), 3);
	            
	            MatOfRect eyeDetections = new MatOfRect();
	            eyeClassifier.detectMultiScale(frame, eyeDetections);
	            
	            for (Rect rect : eyeDetections.toArray()) {
	                Imgproc.rectangle(
	                   frame,                                               // where to draw the box
	                   new Point(rect.x, rect.y),                            // bottom left
	                   new Point(rect.x + rect.width, rect.y + rect.height), // top right
	                   new Scalar(0, 0, 255),
	                   3                                                     // RGB colour
	                );
	             }
	            
	            Core.flip(frame, frame, 1);
	        	jframe.setSize(imageFromMatrix(frame).getWidth(null), imageFromMatrix(frame).getHeight(null));
	            ImageIcon image = new ImageIcon(imageFromMatrix(frame));
	            vidpanel.setIcon(image);
	            vidpanel.repaint();

	        }
	    }
		
	}
	
	public static BufferedImage imageFromMatrix(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( m.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels()*m.cols()*m.rows();
		byte [] b = new byte[bufferSize];
		m.get(0,0,b);
		BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);  
		return image;
	}

}
