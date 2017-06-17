package com.hzgc.jni;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class NativeFunction {
    public static native float[] feature_extract(int[] data, int width, int height);
    public static native void init();
    public static native void destory();
    static {
        System.loadLibrary("Face");
    }

    public static double featureCompare(float[] currentFeature, float[] historyFeature){
        double similarityDegree = 0;
        double currentFeatureMultiple = 0;
        double historyFeatureMultiple = 0;
        for(int i=0;i<currentFeature.length;i++){
            similarityDegree = similarityDegree + currentFeature[i] * historyFeature[i];
            currentFeatureMultiple = currentFeatureMultiple + Math.pow(currentFeature[i], 2);
            historyFeatureMultiple = historyFeatureMultiple + Math.pow(historyFeature[i], 2);
        }
        return similarityDegree/Math.sqrt(currentFeatureMultiple)/Math.sqrt(historyFeatureMultiple);
    }

    public static float[] featureExtract(byte[] imageData) {
        float[] feature = null;
        BufferedImage faceImage;
        try {
            if (null != imageData) {
                faceImage = ImageIO.read(new ByteArrayInputStream(imageData));
                int height = faceImage.getHeight();
                int width = faceImage.getWidth();
                int[] rgbArray = new int[height * width * 3];
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int pixel = faceImage.getRGB(w, h);
                        rgbArray[h * width * 3 + w * 3] = (pixel & 0xff0000) >> 16;
                        rgbArray[h * width * 3 + w * 3 + 1] = (pixel & 0xff00) >> 8;
                        rgbArray[h * width * 3 + w * 3 + 2] = (pixel & 0xff);
                    }
                }
                feature = feature_extract(rgbArray, width, height);
                return feature;
            } else {
                throw new NullPointerException("The data of picture is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feature;
    }

    public static float[] featureExtract(String imagePath) {
        float[] feature = null;
        File imageFile;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        byte[]  buffer = new byte[1024];
        try {
            imageFile = new File(imagePath);
            if (imageFile.exists()){
                baos = new ByteArrayOutputStream();
                fis = new FileInputStream(imageFile);
                int len = 0;
                while ((len = fis.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                bais = new ByteArrayInputStream(baos.toByteArray());
                BufferedImage image = ImageIO.read(bais);
                int height = image.getHeight();
                int width = image.getWidth();
                int[] rgbArray = new int[height * width * 3];
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int pixel = image.getRGB(w, h);
                        rgbArray[h * width * 3 + w * 3] = (pixel & 0xff0000) >> 16;
                        rgbArray[h * width * 3 + w * 3 + 1] = (pixel & 0xff00) >> 8;
                        rgbArray[h * width * 3 + w * 3 + 2] = (pixel & 0xff);
                    }
                }
                feature = feature_extract(rgbArray, width, height);
                return feature;
            } else {
                throw new FileNotFoundException(imageFile.getName() + " is not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bais){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return feature;
    }
    public static void main(String args[]) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            NativeFunction.init();
            float[] currentFeature = NativeFunction.featureExtract("C:\\Users\\Administrator.SKY-20170426OET\\Desktop\\GsFaceLib20170608\\x64\\Release\\Abel_Pacheco_0001.jpg");
            float[] historyFeature = NativeFunction.featureExtract("C:\\Users\\Administrator.SKY-20170426OET\\Desktop\\GsFaceLib20170608\\x64\\Release\\Abel_Pacheco_0004.jpg");
            System.out.println(NativeFunction.featureCompare(currentFeature, historyFeature));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            NativeFunction.destory();
        }
    }
}
