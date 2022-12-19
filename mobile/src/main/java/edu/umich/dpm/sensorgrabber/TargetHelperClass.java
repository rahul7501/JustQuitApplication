package edu.umich.dpm.sensorgrabber;

public class TargetHelperClass {
    String avgCigarettes, noOfCigarettesInPack, pricePerPack, limitPerDay, noOfDays, setDate;

    public TargetHelperClass(){}

    public TargetHelperClass(String avgCigarettes, String noOfCigarettesInPack, String pricePerPack, String limitPerDay, String noOfDays, String setDate) {
        this.avgCigarettes = avgCigarettes;
        this.noOfCigarettesInPack = noOfCigarettesInPack;
        this.pricePerPack = pricePerPack;
        this.limitPerDay = limitPerDay;
        this.noOfDays = noOfDays;
        this.setDate = setDate;

    }

    public String getAvgCigarettes() {
        return avgCigarettes;
    }

    public void setAvgCigarettes(String avgCigarettes) {
        this.avgCigarettes = avgCigarettes;
    }

    public String getNoOfCigarettesInPack() {
        return noOfCigarettesInPack;
    }

    public void setNoOfCigarettesInPack(String noOfCigarettesInPack) {
        this.noOfCigarettesInPack = noOfCigarettesInPack;
    }

    public String getPricePerPack() {
        return pricePerPack;
    }

    public void setPricePerPack(String pricePerPack) {
        this.pricePerPack = pricePerPack;
    }

    public String getLimitPerDay() {
        return limitPerDay;
    }

    public void setLimitPerDay(String limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }
}
