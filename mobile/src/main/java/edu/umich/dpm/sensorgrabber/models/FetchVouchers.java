package edu.umich.dpm.sensorgrabber.models;

public class FetchVouchers {

    long points, aed;
    String voucherImg, brandName;

    public FetchVouchers(long points, long aed, String voucherImg, String brandName) {
        this.points = points;
        this.aed = aed;
        this.voucherImg = voucherImg;
        this.brandName = brandName;
    }

    FetchVouchers() {
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getAed() {
        return aed;
    }

    public void setAed(long aed) {
        this.aed = aed;
    }

    public String getVoucherImg() {
        return voucherImg;
    }

    public void setVoucherImg(String voucherImg) {
        this.voucherImg = voucherImg;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
