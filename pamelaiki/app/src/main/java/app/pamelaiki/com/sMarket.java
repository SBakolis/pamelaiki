package app.pamelaiki.com;

import android.location.Location;

public class sMarket {

    private String sMarketName;
    private double sMarketDistance;
    private double latt;
    private double longt;

    public sMarket(String sMarketName, double sMarketDistance, double latt, double longt)
    {
        this.sMarketName = sMarketName;
        this.sMarketDistance = sMarketDistance;
        this.latt = latt;
        this.longt = longt;

    }

    public String getsMarketName()
    {
        return sMarketName;
    }

    public void setsMarketName(String sMarketName)
    {
        this.sMarketName = sMarketName;
    }

    public double getsMarketDistance()
    {
        return sMarketDistance;
    }

    public void setsMarketDistance(Float sMarketDistance)
    {
        this.sMarketDistance = sMarketDistance;
    }

    public double getlatt()
    {
        return latt;
    }

    public void setlatt(double latt)
    {
        this.latt = latt;
    }

    public double getlongt()
    {
        return longt;
    }

    public void setlongt(double longt)
    {
        this.longt = longt;
    }
}
