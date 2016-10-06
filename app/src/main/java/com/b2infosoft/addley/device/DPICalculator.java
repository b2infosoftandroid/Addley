package com.b2infosoft.addley.device;

/**
 * Created by rajesh on 5/31/2016.
 */

/*
    LDPI: 36 x 36
    MDPI: 48 x 48
    HDPI: 72 x 72
    XHDPI: 96 x 96
    XXHDPI: 144 x 144
    XXXHDPI: 192 x 192
    TVDPI: 64 x 64

 */

    /*

    Formula for Conversion between Units

        px = dp * (dpi / 160)

     */

public class DPICalculator {

    private final float LDPI = 120;
    private final float MDPI = 160;
    private final float HDPI = 240;
    private final float XHDPI = 320;
    private final float XXHDPI = 480;
    private final float XXXHDPI = 640;

    private float forDeviceDensity;
    private float width;
    private float height;

    public DPICalculator(){  }

    public DPICalculator(float forDeviceDensity, float width, float height){
        this.forDeviceDensity = forDeviceDensity;
        this.width = width;
        this.height = height;
    }

    public static void main(String... args) {
        DPICalculator dpiCalculator = new DPICalculator(240,330,120);
        dpiCalculator.calculateDPI();
    }



    private float getPx(float dp, float value) {
        float px = dp * (value / forDeviceDensity );
        return px;
    }

    private void calculateDPI() {

        float ldpiW = getPx(LDPI,width);
        float ldpiH =  getPx(LDPI,height);
        float mdpiW = getPx(MDPI,width);
        float mdpiH =  getPx(MDPI,height);
        float hdpiW = getPx(HDPI,width);
        float hdpiH =  getPx(HDPI,height);
        float xdpiW = getPx(XHDPI,width);
        float xdpiH =  getPx(XHDPI,height);
        float xxdpiW = getPx(XXHDPI,width);
        float xxdpiH =  getPx(XXHDPI,height);
        float xxxdpiW = getPx(XXXHDPI,width);
        float xxxdpiH =  getPx(XXXHDPI,height);

        System.out.println("LDPI: " + ldpiW + " X " + ldpiH);
        System.out.println("MDPI: " + mdpiW + " X " + mdpiH);
        System.out.println("HDPI: " + hdpiW + " X " + hdpiH);
        System.out.println("XHDPI: " + xdpiW + " X " + xdpiH);
        System.out.println("XXHDPI: " + xxdpiW + " X " + xxdpiH);
        System.out.println("XXXHDPI: " + xxxdpiW + " X " + xxxdpiH);
    }
}
