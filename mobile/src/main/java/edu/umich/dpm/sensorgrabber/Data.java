package edu.umich.dpm.sensorgrabber;

public class Data {
    private String time;
    private float AX;
    private float AY;
    private float AZ;
    private float GX;
    private float GY;
    private float GZ;

    public Data(){}
    public Data(String time,float AX, float AY, float AZ) {
        this.time=time;
        this.AX = AX;
        this.AY = AY;
        this.AZ = AZ;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getAX() {
        return AX;
    }

    public void setAX(float AX) {
        this.AX = AX;
    }

    public float getAY() {
        return AY;
    }

    public void setAY(float AY) {
        this.AY = AY;
    }

    public float getAZ() {
        return AZ;
    }

    public void setAZ(float AZ) {
        this.AZ = AZ;
    }

    public float getGX() {
        return GX;
    }

    public void setGX(float GX) {
        this.GX = GX;
    }

    public float getGY() {
        return GY;
    }

    public void setGY(float GY) {
        this.GY = GY;
    }

    public float getGZ() {
        return GZ;
    }

    public void setGZ(float GZ) {
        this.GZ = GZ;
    }

    public Data retunObj()
    {
        return this;
    }
    @Override
    public String toString() {
        return "Data{" +
                "AX=" + AX +
                ", AY=" + AY +
                ", AZ=" + AZ +
                ", GX=" + GX +
                ", GY=" + GY +
                ", GZ=" + GZ +
                '}';
    }
}
