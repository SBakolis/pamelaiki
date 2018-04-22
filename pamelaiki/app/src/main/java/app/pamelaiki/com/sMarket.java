package app.pamelaiki.com;

public class sMarket {

    private String sMarketName;
    private String sMarketDistance;
    private float latt;
    private float longt;

    public sMarket(String sMarketName, String sMarketDistance, float latt, float longt)
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

    public String getsMarketDistance()
    {
        return sMarketDistance;
    }

    public void setsMarketDistance(String sMarketDistance)
    {
        this.sMarketDistance = sMarketDistance;
    }

    public float getlatt()
    {
        return latt;
    }

    public void setlatt(float latt)
    {
        this.latt = latt;
    }

    public float getlongt()
    {
        return longt;
    }

    public void setlongt(float longt)
    {
        this.longt = longt;
    }
}
