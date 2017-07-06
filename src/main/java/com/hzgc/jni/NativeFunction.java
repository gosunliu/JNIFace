package com.hzgc.jni;

public class NativeFunction {
    protected static native float[] feature_extract(int[] data, int width, int height);

    protected static native void init();

    protected static native void destory();

    static {
        System.loadLibrary("Face");
    }

    public static void main(String args[]) throws Exception {

    }
}
