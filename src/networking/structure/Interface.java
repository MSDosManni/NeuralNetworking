package networking.structure;

import java.net.*;
import java.io.*;

public class Interface {
    static Socket clientSocket;
    static double[][] trainImages = new double[8000][];
    static double[][] trainLabels = new double[8000][];
    static double[][] testImages = new double[2000][];
    static double[][] testLabels = new double[2000][];

    public static void loadData() {
        try {
            byte[] images = java.nio.file.Files.readAllBytes(new java.io.File(".\\Train\\t10k-images-14x14.idx3-ubyte").toPath());
            byte[] labels = java.nio.file.Files.readAllBytes(new java.io.File(".\\Train\\t10k-labels.idx1-ubyte").toPath());
            loadImages(images);
            loadLabels(labels);
        } catch (Exception e) {
        }
    }

    public static double[][] getTrainImages() {
        return trainImages;
    }

    public static double[][] getTrainLabels() {
        return trainLabels;
    }

    public static double[][] getTestImages() {
        return testImages;
    }

    public static double[][] getTestLabels() {
        return testLabels;
    }

    static void loadImages(byte[] im) {
        int curImage = 0;
        int curTest = 0;
        for (int i = 0; i < 10000; i++) {
            if (i % 5 == 0) {
                testImages[curTest] = new double[196];
                for (int o = 0; o < 196; o++) {
                    testImages[curTest][o] = (im[i * 196 + o + 16] & 0xFF) / 255f;
                    //println((im[i*196+o+16]&0xFF)/255f);
                }
                curTest++;
            } else {
                trainImages[curImage] = new double[196];
                for (int o = 0; o < 196; o++) {
                    trainImages[curImage][o] = (im[i * 196 + o + 16] & 0xFF) / 255f;
                }
                curImage++;
            }
        }
    }

    static void loadLabels(byte[] la) {
        int curLabel = 0;
        int curTest = 0;
        for (int i = 0; i < 10000; i++) {
            if (i % 5 == 0) {
                testLabels[curTest] = new double[10];
                testLabels[curTest][la[i + 8]] = 1f;
                curTest++;
            } else {
                trainLabels[curLabel] = new double[10];
                trainLabels[curLabel][la[i + 8]] = 1f;
                curLabel++;
            }
        }
    }

    public static void connectSocket() {
        try {
            clientSocket = new Socket("localhost", 8001);
        } catch (Exception e) {
            System.out.println("Connection failed");
        }
    }

    public static double[] getPixels() {
        try {
            clientSocket.getOutputStream().write(new byte[]{0});
            clientSocket.getOutputStream().flush();
            byte[] read = readStream();
            double[] ret = new double[read.length];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = (double) read[i];
            }
            return ret;
        } catch (Exception e) {
            System.out.println("Transmission failed. ");
            return new double[]{0};
        }

    }

    static byte[] readStream() {
        try {
            byte[] byts = new byte[196];
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            dis.readFully(byts);
            return byts;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}