package tech.adagun.covidtracker.models;

public class RegionStats
{
    private String region;
    private int latestTotalCases;
    private int latestTotalDeaths;
    private int casesDiffPreviousDay;
    private int deathsDiffPreviousDay;

    public int getDeathsDiffPreviousDay()
    {
        return deathsDiffPreviousDay;
    }

    public void setDeathsDiffPreviousDay(int deathsDiffPreviousDay)
    {
        this.deathsDiffPreviousDay = deathsDiffPreviousDay;
    }



    public int getCasesDiffPreviousDay()
    {
        return casesDiffPreviousDay;
    }

    public void setCasesDiffPreviousDay(int casesDiffPreviousDay)
    {
        this.casesDiffPreviousDay = casesDiffPreviousDay;
    }




    public int getLatestTotalDeaths()
    {
        return latestTotalDeaths;
    }

    public void setLatestTotalDeaths(int latestTotalDeaths)
    {
        this.latestTotalDeaths = latestTotalDeaths;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public int getLatestTotalCases()
    {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases)
    {
        this.latestTotalCases = latestTotalCases;
    }

    @Override
    public String toString()
    {
        return "RegionStats{" +
                "region='" + region + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                ", latestTotalDeaths=" + latestTotalDeaths +
                '}';
    }
}
