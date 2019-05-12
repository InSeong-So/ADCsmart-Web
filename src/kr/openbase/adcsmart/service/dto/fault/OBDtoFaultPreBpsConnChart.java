package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

public class OBDtoFaultPreBpsConnChart
{
    ArrayList<OBDtoFaultBpsConnData> bpsConnData;
    
    private Long maxBpsIn=-1L;
    private Long maxBpsOut=-1L;
    private Long maxBpsTot=-1L;
    private Long maxConnCurr=-1L;
    private Integer maxRespTime=-1;
    private String maxBpsInUnit;
    private String maxBpsOutUnit;
    private String maxBpsTotUnit;
    private String maxConnCurrUnit;
    private String maxRespTimeUnit;
    
    private Long avgBpsIn=-1L;
    private Long avgBpsOut=-1L;
    private Long avgBpsTot=-1L;
    private Long avgConnCurr=-1L;
    private Integer avgRespTime=-1;
    private String avgBpsInUnit;
    private String avgBpsOutUnit;
    private String avgBpsTotUnit;
    private String avgConnCurrUnit;
    private String avgRespTimeUnit;
    
    private Long currBpsIn=-1L;
    private Long currBpsOut=-1L;
    private Long currBpsTot=-1L;
    private Long currConnCurr=-1L;
    private Integer currRespTime=-1;
    private String currBpsInUnit;
    private String currBpsOutUnit;
    private String currBpsTotUnit;
    private String currConnCurrUnit;
    private String currRespTimeUnit;
    
    private Long preMaxBpsIn=-1L;
    private Long preMaxBpsOut=-1L;
    private Long preMaxBpsTot=-1L;
    private Long preMaxConnCurr=-1L;
    private Integer preMaxRespTime=-1;
    private String preMaxBpsInUnit;
    private String preMaxBpsOutUnit;
    private String preMaxBpsTotUnit;
    private String preMaxConnCurrUnit;
    private String preMaxRespTimeUnit;
    
    private Long preAvgBpsIn=-1L;
    private Long preAvgBpsOut=-1L;
    private Long preAvgBpsTot=-1L;
    private Long preAvgConnCurr=-1L;
    private Integer preAvgRespTime=-1;
    private String preAvgBpsInUnit;
    private String preAvgBpsOutUnit;
    private String preAvgBpsTotUnit;
    private String preAvgConnCurrUnit;
    private String preAvgRespTimeUnit;
    
    private Long preCurrBpsIn=-1L;
    private Long preCurrBpsOut=-1L;
    private Long preCurrBpsTot=-1L;
    private Long preCurrConnCurr=-1L;
    private Integer preCurrRespTime=-1;
    private String preCurrBpsInUnit;
    private String preCurrBpsOutUnit;
    private String preCurrBpsTotUnit;
    private String preCurrConnCurrUnit;
    private String preCurrRespTimeUnit;
    
    private Long subtractionMaxBpsIn=-1L;
    private Long subtractionMaxBpsOut=-1L;
    private Long subtractionMaxBpsTot=-1L;
    private Long subtractionMaxConnCurr=-1L;
    private Integer subtractionMaxRespTime=-1;
    private String subtractionMaxBpsInUnit;
    private String subtractionMaxBpsOutUnit;
    private String subtractionMaxBpsTotUnit;
    private String subtractionMaxConnCurrUnit;
    private String subtractionMaxRespTimeUnit;
    private double subtractionMaxBpsInPercent=-1F;
    private double subtractionMaxBpsOutPercent=-1F;
    private double subtractionMaxBpsTotPercent=-1F;
    private double subtractionMaxConnCurrPercent=-1F;
    private double subtractionMaxRespTimePercent=-1F;
    
    private Long subtractionAvgBpsIn=-1L;
    private Long subtractionAvgBpsOut=-1L;
    private Long subtractionAvgBpsTot=-1L;
    private Long subtractionAvgConnCurr=-1L;
    private Integer subtractionAvgRespTime=-1;
    private String subtractionAvgBpsInUnit;
    private String subtractionAvgBpsOutUnit;
    private String subtractionAvgBpsTotUnit;
    private String subtractionAvgConnCurrUnit;
    private String subtractionAvgRespTimeUnit;
    private double subtractionAvgBpsInPercent=-1F;
    private double subtractionAvgBpsOutPercent=-1F;
    private double subtractionAvgBpsTotPercent=-1F;
    private double subtractionAvgConnCurrPercent=-1F;
    private double subtractionAvgRespTimePercent=-1F;
    
    private Long subtractionCurrBpsIn=-1L;
    private Long subtractionCurrBpsOut=-1L;
    private Long subtractionCurrBpsTot=-1L;
    private Long subtractionCurrConnCurr=-1L;
    private Integer subtractionCurrRespTime=-1;
    private String subtractionCurrBpsInUnit;
    private String subtractionCurrBpsOutUnit;
    private String subtractionCurrBpsTotUnit;
    private String subtractionCurrConnCurrUnit;
    private String subtractionCurrRespTimeUnit;
    private double subtractionCurrBpsInPercent=-1F;
    private double subtractionCurrBpsOutPercent=-1F;
    private double subtractionCurrBpsTotPercent=-1F;
    private double subtractionCurrConnCurrPercent=-1F;
    private double subtractionCurrRespTimePercent=-1F;
    
    @Override
    public String toString()
    {
        return "OBDtoFaultPreBpsConnChart [bpsConnData=" + bpsConnData
                + ", maxBpsIn=" + maxBpsIn + ", maxBpsOut=" + maxBpsOut
                + ", maxBpsTot=" + maxBpsTot + ", maxConnCurr=" + maxConnCurr
                + ", maxRespTime=" + maxRespTime + ", maxBpsInUnit="
                + maxBpsInUnit + ", maxBpsOutUnit=" + maxBpsOutUnit
                + ", maxBpsTotUnit=" + maxBpsTotUnit + ", maxConnCurrUnit="
                + maxConnCurrUnit + ", maxRespTimeUnit=" + maxRespTimeUnit
                + ", avgBpsIn=" + avgBpsIn + ", avgBpsOut=" + avgBpsOut
                + ", avgBpsTot=" + avgBpsTot + ", avgConnCurr=" + avgConnCurr
                + ", avgRespTime=" + avgRespTime + ", avgBpsInUnit="
                + avgBpsInUnit + ", avgBpsOutUnit=" + avgBpsOutUnit
                + ", avgBpsTotUnit=" + avgBpsTotUnit + ", avgConnCurrUnit="
                + avgConnCurrUnit + ", avgRespTimeUnit=" + avgRespTimeUnit
                + ", currBpsIn=" + currBpsIn + ", currBpsOut=" + currBpsOut
                + ", currBpsTot=" + currBpsTot + ", currConnCurr="
                + currConnCurr + ", currRespTime=" + currRespTime
                + ", currBpsInUnit=" + currBpsInUnit + ", currBpsOutUnit="
                + currBpsOutUnit + ", currBpsTotUnit=" + currBpsTotUnit
                + ", currConnCurrUnit=" + currConnCurrUnit
                + ", currRespTimeUnit=" + currRespTimeUnit + ", preMaxBpsIn="
                + preMaxBpsIn + ", preMaxBpsOut=" + preMaxBpsOut
                + ", preMaxBpsTot=" + preMaxBpsTot + ", preMaxConnCurr="
                + preMaxConnCurr + ", preMaxRespTime=" + preMaxRespTime
                + ", preMaxBpsInUnit=" + preMaxBpsInUnit
                + ", preMaxBpsOutUnit=" + preMaxBpsOutUnit
                + ", preMaxBpsTotUnit=" + preMaxBpsTotUnit
                + ", preMaxConnCurrUnit=" + preMaxConnCurrUnit
                + ", preMaxRespTimeUnit=" + preMaxRespTimeUnit
                + ", preAvgBpsIn=" + preAvgBpsIn + ", preAvgBpsOut="
                + preAvgBpsOut + ", preAvgBpsTot=" + preAvgBpsTot
                + ", preAvgConnCurr=" + preAvgConnCurr + ", preAvgRespTime="
                + preAvgRespTime + ", preAvgBpsInUnit=" + preAvgBpsInUnit
                + ", preAvgBpsOutUnit=" + preAvgBpsOutUnit
                + ", preAvgBpsTotUnit=" + preAvgBpsTotUnit
                + ", preAvgConnCurrUnit=" + preAvgConnCurrUnit
                + ", preAvgRespTimeUnit=" + preAvgRespTimeUnit
                + ", preCurrBpsIn=" + preCurrBpsIn + ", preCurrBpsOut="
                + preCurrBpsOut + ", preCurrBpsTot=" + preCurrBpsTot
                + ", preCurrConnCurr=" + preCurrConnCurr + ", preCurrRespTime="
                + preCurrRespTime + ", preCurrBpsInUnit=" + preCurrBpsInUnit
                + ", preCurrBpsOutUnit=" + preCurrBpsOutUnit
                + ", preCurrBpsTotUnit=" + preCurrBpsTotUnit
                + ", preCurrConnCurrUnit=" + preCurrConnCurrUnit
                + ", preCurrRespTimeUnit=" + preCurrRespTimeUnit
                + ", subtractionMaxBpsIn=" + subtractionMaxBpsIn
                + ", subtractionMaxBpsOut=" + subtractionMaxBpsOut
                + ", subtractionMaxBpsTot=" + subtractionMaxBpsTot
                + ", subtractionMaxConnCurr=" + subtractionMaxConnCurr
                + ", subtractionMaxRespTime=" + subtractionMaxRespTime
                + ", subtractionMaxBpsInUnit=" + subtractionMaxBpsInUnit
                + ", subtractionMaxBpsOutUnit=" + subtractionMaxBpsOutUnit
                + ", subtractionMaxBpsTotUnit=" + subtractionMaxBpsTotUnit
                + ", subtractionMaxConnCurrUnit=" + subtractionMaxConnCurrUnit
                + ", subtractionMaxRespTimeUnit=" + subtractionMaxRespTimeUnit
                + ", subtractionMaxBpsInPercent=" + subtractionMaxBpsInPercent
                + ", subtractionMaxBpsOutPercent="
                + subtractionMaxBpsOutPercent
                + ", subtractionMaxBpsTotPercent="
                + subtractionMaxBpsTotPercent
                + ", subtractionMaxConnCurrPercent="
                + subtractionMaxConnCurrPercent
                + ", subtractionMaxRespTimePercent="
                + subtractionMaxRespTimePercent + ", subtractionAvgBpsIn="
                + subtractionAvgBpsIn + ", subtractionAvgBpsOut="
                + subtractionAvgBpsOut + ", subtractionAvgBpsTot="
                + subtractionAvgBpsTot + ", subtractionAvgConnCurr="
                + subtractionAvgConnCurr + ", subtractionAvgRespTime="
                + subtractionAvgRespTime + ", subtractionAvgBpsInUnit="
                + subtractionAvgBpsInUnit + ", subtractionAvgBpsOutUnit="
                + subtractionAvgBpsOutUnit + ", subtractionAvgBpsTotUnit="
                + subtractionAvgBpsTotUnit + ", subtractionAvgConnCurrUnit="
                + subtractionAvgConnCurrUnit + ", subtractionAvgRespTimeUnit="
                + subtractionAvgRespTimeUnit + ", subtractionAvgBpsInPercent="
                + subtractionAvgBpsInPercent + ", subtractionAvgBpsOutPercent="
                + subtractionAvgBpsOutPercent
                + ", subtractionAvgBpsTotPercent="
                + subtractionAvgBpsTotPercent
                + ", subtractionAvgConnCurrPercent="
                + subtractionAvgConnCurrPercent
                + ", subtractionAvgRespTimePercent="
                + subtractionAvgRespTimePercent + ", subtractionCurrBpsIn="
                + subtractionCurrBpsIn + ", subtractionCurrBpsOut="
                + subtractionCurrBpsOut + ", subtractionCurrBpsTot="
                + subtractionCurrBpsTot + ", subtractionCurrConnCurr="
                + subtractionCurrConnCurr + ", subtractionCurrRespTime="
                + subtractionCurrRespTime + ", subtractionCurrBpsInUnit="
                + subtractionCurrBpsInUnit + ", subtractionCurrBpsOutUnit="
                + subtractionCurrBpsOutUnit + ", subtractionCurrBpsTotUnit="
                + subtractionCurrBpsTotUnit + ", subtractionCurrConnCurrUnit="
                + subtractionCurrConnCurrUnit
                + ", subtractionCurrRespTimeUnit="
                + subtractionCurrRespTimeUnit
                + ", subtractionCurrBpsInPercent="
                + subtractionCurrBpsInPercent
                + ", subtractionCurrBpsOutPercent="
                + subtractionCurrBpsOutPercent
                + ", subtractionCurrBpsTotPercent="
                + subtractionCurrBpsTotPercent
                + ", subtractionCurrConnCurrPercent="
                + subtractionCurrConnCurrPercent
                + ", subtractionCurrRespTimePercent="
                + subtractionCurrRespTimePercent + "]";
    }
    
    public ArrayList<OBDtoFaultBpsConnData> getBpsConnData()
    {
        return bpsConnData;
    }

    public void setBpsConnDate(ArrayList<OBDtoFaultBpsConnData> bpsConnData)
    {
        this.bpsConnData = bpsConnData;
    }

    public Long getMaxBpsIn()
    {
        return maxBpsIn;
    }
    public void setMaxBpsIn(Long maxBpsIn)
    {
        this.maxBpsIn = maxBpsIn;
    }
    public Long getMaxBpsOut()
    {
        return maxBpsOut;
    }
    public void setMaxBpsOut(Long maxBpsOut)
    {
        this.maxBpsOut = maxBpsOut;
    }
    public Long getMaxBpsTot()
    {
        return maxBpsTot;
    }
    public void setMaxBpsTot(Long maxBpsTot)
    {
        this.maxBpsTot = maxBpsTot;
    }
    public Long getMaxConnCurr()
    {
        return maxConnCurr;
    }
    public void setMaxConnCurr(Long maxConnCurr)
    {
        this.maxConnCurr = maxConnCurr;
    }

    public Long getAvgBpsIn()
    {
        return avgBpsIn;
    }

    public void setAvgBpsIn(Long avgBpsIn)
    {
        this.avgBpsIn = avgBpsIn;
    }

    public Long getAvgBpsOut()
    {
        return avgBpsOut;
    }

    public void setAvgBpsOut(Long avgBpsOut)
    {
        this.avgBpsOut = avgBpsOut;
    }

    public Long getAvgBpsTot()
    {
        return avgBpsTot;
    }

    public void setAvgBpsTot(Long avgBpsTot)
    {
        this.avgBpsTot = avgBpsTot;
    }

    public Long getAvgConnCurr()
    {
        return avgConnCurr;
    }

    public void setAvgConnCurr(Long avgConnCurr)
    {
        this.avgConnCurr = avgConnCurr;
    }

    public Long getCurrBpsIn()
    {
        return currBpsIn;
    }

    public void setCurrBpsIn(Long currBpsIn)
    {
        this.currBpsIn = currBpsIn;
    }

    public Long getCurrBpsOut()
    {
        return currBpsOut;
    }

    public void setCurrBpsOut(Long currBpsOut)
    {
        this.currBpsOut = currBpsOut;
    }

    public Long getCurrBpsTot()
    {
        return currBpsTot;
    }

    public void setCurrBpsTot(Long currBpsTot)
    {
        this.currBpsTot = currBpsTot;
    }

    public Long getCurrConnCurr()
    {
        return currConnCurr;
    }

    public void setCurrConnCurr(Long currConnCurr)
    {
        this.currConnCurr = currConnCurr;
    }

    public Long getPreMaxBpsIn()
    {
        return preMaxBpsIn;
    }

    public void setPreMaxBpsIn(Long preMaxBpsIn)
    {
        this.preMaxBpsIn = preMaxBpsIn;
    }

    public Long getPreMaxBpsOut()
    {
        return preMaxBpsOut;
    }

    public void setPreMaxBpsOut(Long preMaxBpsOut)
    {
        this.preMaxBpsOut = preMaxBpsOut;
    }

    public Long getPreMaxBpsTot()
    {
        return preMaxBpsTot;
    }

    public void setPreMaxBpsTot(Long preMaxBpsTot)
    {
        this.preMaxBpsTot = preMaxBpsTot;
    }

    public Long getPreMaxConnCurr()
    {
        return preMaxConnCurr;
    }

    public void setPreMaxConnCurr(Long preMaxConnCurr)
    {
        this.preMaxConnCurr = preMaxConnCurr;
    }

    public Long getPreAvgBpsIn()
    {
        return preAvgBpsIn;
    }

    public void setPreAvgBpsIn(Long preAvgBpsIn)
    {
        this.preAvgBpsIn = preAvgBpsIn;
    }

    public Long getPreAvgBpsOut()
    {
        return preAvgBpsOut;
    }

    public void setPreAvgBpsOut(Long preAvgBpsOut)
    {
        this.preAvgBpsOut = preAvgBpsOut;
    }

    public Long getPreAvgBpsTot()
    {
        return preAvgBpsTot;
    }

    public void setPreAvgBpsTot(Long preAvgBpsTot)
    {
        this.preAvgBpsTot = preAvgBpsTot;
    }

    public Long getPreAvgConnCurr()
    {
        return preAvgConnCurr;
    }

    public void setPreAvgConnCurr(Long preAvgConnCurr)
    {
        this.preAvgConnCurr = preAvgConnCurr;
    }

    public Long getPreCurrBpsIn()
    {
        return preCurrBpsIn;
    }

    public void setPreCurrBpsIn(Long preCurrBpsIn)
    {
        this.preCurrBpsIn = preCurrBpsIn;
    }

    public Long getPreCurrBpsOut()
    {
        return preCurrBpsOut;
    }

    public void setPreCurrBpsOut(Long preCurrBpsOut)
    {
        this.preCurrBpsOut = preCurrBpsOut;
    }

    public Long getPreCurrBpsTot()
    {
        return preCurrBpsTot;
    }

    public void setPreCurrBpsTot(Long preCurrBpsTot)
    {
        this.preCurrBpsTot = preCurrBpsTot;
    }

    public Long getPreCurrConnCurr()
    {
        return preCurrConnCurr;
    }

    public void setPreCurrConnCurr(Long preCurrConnCurr)
    {
        this.preCurrConnCurr = preCurrConnCurr;
    }

    public Long getSubtractionMaxBpsIn()
    {
        return subtractionMaxBpsIn;
    }

    public void setSubtractionMaxBpsIn(Long subtractionMaxBpsIn)
    {
        this.subtractionMaxBpsIn = subtractionMaxBpsIn;
    }

    public Long getSubtractionMaxBpsOut()
    {
        return subtractionMaxBpsOut;
    }

    public void setSubtractionMaxBpsOut(Long subtractionMaxBpsOut)
    {
        this.subtractionMaxBpsOut = subtractionMaxBpsOut;
    }

    public Long getSubtractionMaxBpsTot()
    {
        return subtractionMaxBpsTot;
    }

    public void setSubtractionMaxBpsTot(Long subtractionMaxBpsTot)
    {
        this.subtractionMaxBpsTot = subtractionMaxBpsTot;
    }

    public Long getSubtractionMaxConnCurr()
    {
        return subtractionMaxConnCurr;
    }

    public void setSubtractionMaxConnCurr(Long subtractionMaxConnCurr)
    {
        this.subtractionMaxConnCurr = subtractionMaxConnCurr;
    }

    public double getSubtractionMaxBpsInPercent()
    {
        return subtractionMaxBpsInPercent;
    }

    public void setSubtractionMaxBpsInPercent(double subtractionMaxBpsInPercent)
    {
        this.subtractionMaxBpsInPercent = subtractionMaxBpsInPercent;
    }

    public double getSubtractionMaxBpsOutPercent()
    {
        return subtractionMaxBpsOutPercent;
    }

    public void setSubtractionMaxBpsOutPercent(double subtractionMaxBpsOutPercent)
    {
        this.subtractionMaxBpsOutPercent = subtractionMaxBpsOutPercent;
    }

    public double getSubtractionMaxBpsTotPercent()
    {
        return subtractionMaxBpsTotPercent;
    }

    public void setSubtractionMaxBpsTotPercent(double subtractionMaxBpsTotPercent)
    {
        this.subtractionMaxBpsTotPercent = subtractionMaxBpsTotPercent;
    }

    public double getSubtractionMaxConnCurrPercent()
    {
        return subtractionMaxConnCurrPercent;
    }

    public void setSubtractionMaxConnCurrPercent(
            double subtractionMaxConnCurrPercent)
    {
        this.subtractionMaxConnCurrPercent = subtractionMaxConnCurrPercent;
    }

    public Long getSubtractionAvgBpsIn()
    {
        return subtractionAvgBpsIn;
    }

    public void setSubtractionAvgBpsIn(Long subtractionAvgBpsIn)
    {
        this.subtractionAvgBpsIn = subtractionAvgBpsIn;
    }

    public Long getSubtractionAvgBpsOut()
    {
        return subtractionAvgBpsOut;
    }

    public void setSubtractionAvgBpsOut(Long subtractionAvgBpsOut)
    {
        this.subtractionAvgBpsOut = subtractionAvgBpsOut;
    }

    public Long getSubtractionAvgBpsTot()
    {
        return subtractionAvgBpsTot;
    }

    public void setSubtractionAvgBpsTot(Long subtractionAvgBpsTot)
    {
        this.subtractionAvgBpsTot = subtractionAvgBpsTot;
    }

    public Long getSubtractionAvgConnCurr()
    {
        return subtractionAvgConnCurr;
    }

    public void setSubtractionAvgConnCurr(Long subtractionAvgConnCurr)
    {
        this.subtractionAvgConnCurr = subtractionAvgConnCurr;
    }

    public double getSubtractionAvgBpsInPercent()
    {
        return subtractionAvgBpsInPercent;
    }

    public void setSubtractionAvgBpsInPercent(double subtractionAvgBpsInPercent)
    {
        this.subtractionAvgBpsInPercent = subtractionAvgBpsInPercent;
    }

    public double getSubtractionAvgBpsOutPercent()
    {
        return subtractionAvgBpsOutPercent;
    }

    public void setSubtractionAvgBpsOutPercent(double subtractionAvgBpsOutPercent)
    {
        this.subtractionAvgBpsOutPercent = subtractionAvgBpsOutPercent;
    }

    public double getSubtractionAvgBpsTotPercent()
    {
        return subtractionAvgBpsTotPercent;
    }

    public void setSubtractionAvgBpsTotPercent(double subtractionAvgBpsTotPercent)
    {
        this.subtractionAvgBpsTotPercent = subtractionAvgBpsTotPercent;
    }

    public double getSubtractionAvgConnCurrPercent()
    {
        return subtractionAvgConnCurrPercent;
    }

    public void setSubtractionAvgConnCurrPercent(
            double subtractionAvgConnCurrPercent)
    {
        this.subtractionAvgConnCurrPercent = subtractionAvgConnCurrPercent;
    }

    public Long getSubtractionCurrBpsIn()
    {
        return subtractionCurrBpsIn;
    }

    public void setSubtractionCurrBpsIn(Long subtractionCurrBpsIn)
    {
        this.subtractionCurrBpsIn = subtractionCurrBpsIn;
    }

    public Long getSubtractionCurrBpsOut()
    {
        return subtractionCurrBpsOut;
    }

    public void setSubtractionCurrBpsOut(Long subtractionCurrBpsOut)
    {
        this.subtractionCurrBpsOut = subtractionCurrBpsOut;
    }

    public Long getSubtractionCurrBpsTot()
    {
        return subtractionCurrBpsTot;
    }

    public void setSubtractionCurrBpsTot(Long subtractionCurrBpsTot)
    {
        this.subtractionCurrBpsTot = subtractionCurrBpsTot;
    }

    public Long getSubtractionCurrConnCurr()
    {
        return subtractionCurrConnCurr;
    }

    public void setSubtractionCurrConnCurr(Long subtractionCurrConnCurr)
    {
        this.subtractionCurrConnCurr = subtractionCurrConnCurr;
    }

    public double getSubtractionCurrBpsInPercent()
    {
        return subtractionCurrBpsInPercent;
    }

    public void setSubtractionCurrBpsInPercent(double subtractionCurrBpsInPercent)
    {
        this.subtractionCurrBpsInPercent = subtractionCurrBpsInPercent;
    }

    public double getSubtractionCurrBpsOutPercent()
    {
        return subtractionCurrBpsOutPercent;
    }

    public void setSubtractionCurrBpsOutPercent(double subtractionCurrBpsOutPercent)
    {
        this.subtractionCurrBpsOutPercent = subtractionCurrBpsOutPercent;
    }

    public double getSubtractionCurrBpsTotPercent()
    {
        return subtractionCurrBpsTotPercent;
    }

    public void setSubtractionCurrBpsTotPercent(double subtractionCurrBpsTotPercent)
    {
        this.subtractionCurrBpsTotPercent = subtractionCurrBpsTotPercent;
    }

    public double getSubtractionCurrConnCurrPercent()
    {
        return subtractionCurrConnCurrPercent;
    }

    public void setSubtractionCurrConnCurrPercent(
            double subtractionCurrConnCurrPercent)
    {
        this.subtractionCurrConnCurrPercent = subtractionCurrConnCurrPercent;
    }

    public Integer getSubtractionMaxRespTime()
    {
        return subtractionMaxRespTime;
    }

    public void setSubtractionMaxRespTime(Integer subtractionMaxRespTime)
    {
        this.subtractionMaxRespTime = subtractionMaxRespTime;
    }

    public double getSubtractionMaxRespTimePercent()
    {
        return subtractionMaxRespTimePercent;
    }

    public void setSubtractionMaxRespTimePercent(
            double subtractionMaxRespTimePercent)
    {
        this.subtractionMaxRespTimePercent = subtractionMaxRespTimePercent;
    }

    public Integer getSubtractionAvgRespTime()
    {
        return subtractionAvgRespTime;
    }

    public void setSubtractionAvgRespTime(Integer subtractionAvgRespTime)
    {
        this.subtractionAvgRespTime = subtractionAvgRespTime;
    }

    public double getSubtractionAvgRespTimePercent()
    {
        return subtractionAvgRespTimePercent;
    }

    public void setSubtractionAvgRespTimePercent(
            double subtractionAvgRespTimePercent)
    {
        this.subtractionAvgRespTimePercent = subtractionAvgRespTimePercent;
    }

    public Integer getSubtractionCurrRespTime()
    {
        return subtractionCurrRespTime;
    }

    public void setSubtractionCurrRespTime(Integer subtractionCurrRespTime)
    {
        this.subtractionCurrRespTime = subtractionCurrRespTime;
    }

    public double getSubtractionCurrRespTimePercent()
    {
        return subtractionCurrRespTimePercent;
    }

    public void setSubtractionCurrRespTimePercent(
            double subtractionCurrRespTimePercent)
    {
        this.subtractionCurrRespTimePercent = subtractionCurrRespTimePercent;
    }

    public Integer getMaxRespTime()
    {
        return maxRespTime;
    }

    public void setMaxRespTime(Integer maxRespTime)
    {
        this.maxRespTime = maxRespTime;
    }

    public Integer getAvgRespTime()
    {
        return avgRespTime;
    }

    public void setAvgRespTime(Integer avgRespTime)
    {
        this.avgRespTime = avgRespTime;
    }

    public Integer getCurrRespTime()
    {
        return currRespTime;
    }

    public void setCurrRespTime(Integer currRespTime)
    {
        this.currRespTime = currRespTime;
    }

    public Integer getPreMaxRespTime()
    {
        return preMaxRespTime;
    }

    public void setPreMaxRespTime(Integer preMaxRespTime)
    {
        this.preMaxRespTime = preMaxRespTime;
    }

    public Integer getPreAvgRespTime()
    {
        return preAvgRespTime;
    }

    public void setPreAvgRespTime(Integer preAvgRespTime)
    {
        this.preAvgRespTime = preAvgRespTime;
    }

    public Integer getPreCurrRespTime()
    {
        return preCurrRespTime;
    }

    public void setPreCurrRespTime(Integer preCurrRespTime)
    {
        this.preCurrRespTime = preCurrRespTime;
    }

    public String getMaxBpsInUnit()
    {
        return maxBpsInUnit;
    }

    public void setMaxBpsInUnit(String maxBpsInUnit)
    {
        this.maxBpsInUnit = maxBpsInUnit;
    }

    public String getMaxBpsOutUnit()
    {
        return maxBpsOutUnit;
    }

    public void setMaxBpsOutUnit(String maxBpsOutUnit)
    {
        this.maxBpsOutUnit = maxBpsOutUnit;
    }

    public String getMaxBpsTotUnit()
    {
        return maxBpsTotUnit;
    }

    public void setMaxBpsTotUnit(String maxBpsTotUnit)
    {
        this.maxBpsTotUnit = maxBpsTotUnit;
    }

    public String getMaxConnCurrUnit()
    {
        return maxConnCurrUnit;
    }

    public void setMaxConnCurrUnit(String maxConnCurrUnit)
    {
        this.maxConnCurrUnit = maxConnCurrUnit;
    }

    public String getMaxRespTimeUnit()
    {
        return maxRespTimeUnit;
    }

    public void setMaxRespTimeUnit(String maxRespTimeUnit)
    {
        this.maxRespTimeUnit = maxRespTimeUnit;
    }

    public String getAvgBpsInUnit()
    {
        return avgBpsInUnit;
    }

    public void setAvgBpsInUnit(String avgBpsInUnit)
    {
        this.avgBpsInUnit = avgBpsInUnit;
    }

    public String getAvgBpsOutUnit()
    {
        return avgBpsOutUnit;
    }

    public void setAvgBpsOutUnit(String avgBpsOutUnit)
    {
        this.avgBpsOutUnit = avgBpsOutUnit;
    }

    public String getAvgBpsTotUnit()
    {
        return avgBpsTotUnit;
    }

    public void setAvgBpsTotUnit(String avgBpsTotUnit)
    {
        this.avgBpsTotUnit = avgBpsTotUnit;
    }

    public String getAvgConnCurrUnit()
    {
        return avgConnCurrUnit;
    }

    public void setAvgConnCurrUnit(String avgConnCurrUnit)
    {
        this.avgConnCurrUnit = avgConnCurrUnit;
    }

    public String getAvgRespTimeUnit()
    {
        return avgRespTimeUnit;
    }

    public void setAvgRespTimeUnit(String avgRespTimeUnit)
    {
        this.avgRespTimeUnit = avgRespTimeUnit;
    }

    public String getCurrBpsInUnit()
    {
        return currBpsInUnit;
    }

    public void setCurrBpsInUnit(String currBpsInUnit)
    {
        this.currBpsInUnit = currBpsInUnit;
    }

    public String getCurrBpsOutUnit()
    {
        return currBpsOutUnit;
    }

    public void setCurrBpsOutUnit(String currBpsOutUnit)
    {
        this.currBpsOutUnit = currBpsOutUnit;
    }

    public String getCurrBpsTotUnit()
    {
        return currBpsTotUnit;
    }

    public void setCurrBpsTotUnit(String currBpsTotUnit)
    {
        this.currBpsTotUnit = currBpsTotUnit;
    }

    public String getCurrConnCurrUnit()
    {
        return currConnCurrUnit;
    }

    public void setCurrConnCurrUnit(String currConnCurrUnit)
    {
        this.currConnCurrUnit = currConnCurrUnit;
    }

    public String getCurrRespTimeUnit()
    {
        return currRespTimeUnit;
    }

    public void setCurrRespTimeUnit(String currRespTimeUnit)
    {
        this.currRespTimeUnit = currRespTimeUnit;
    }

    public String getPreMaxBpsInUnit()
    {
        return preMaxBpsInUnit;
    }

    public void setPreMaxBpsInUnit(String preMaxBpsInUnit)
    {
        this.preMaxBpsInUnit = preMaxBpsInUnit;
    }

    public String getPreMaxBpsOutUnit()
    {
        return preMaxBpsOutUnit;
    }

    public void setPreMaxBpsOutUnit(String preMaxBpsOutUnit)
    {
        this.preMaxBpsOutUnit = preMaxBpsOutUnit;
    }

    public String getPreMaxBpsTotUnit()
    {
        return preMaxBpsTotUnit;
    }

    public void setPreMaxBpsTotUnit(String preMaxBpsTotUnit)
    {
        this.preMaxBpsTotUnit = preMaxBpsTotUnit;
    }

    public String getPreMaxConnCurrUnit()
    {
        return preMaxConnCurrUnit;
    }

    public void setPreMaxConnCurrUnit(String preMaxConnCurrUnit)
    {
        this.preMaxConnCurrUnit = preMaxConnCurrUnit;
    }

    public String getPreMaxRespTimeUnit()
    {
        return preMaxRespTimeUnit;
    }

    public void setPreMaxRespTimeUnit(String preMaxRespTimeUnit)
    {
        this.preMaxRespTimeUnit = preMaxRespTimeUnit;
    }

    public String getPreAvgBpsInUnit()
    {
        return preAvgBpsInUnit;
    }

    public void setPreAvgBpsInUnit(String preAvgBpsInUnit)
    {
        this.preAvgBpsInUnit = preAvgBpsInUnit;
    }

    public String getPreAvgBpsOutUnit()
    {
        return preAvgBpsOutUnit;
    }

    public void setPreAvgBpsOutUnit(String preAvgBpsOutUnit)
    {
        this.preAvgBpsOutUnit = preAvgBpsOutUnit;
    }

    public String getPreAvgBpsTotUnit()
    {
        return preAvgBpsTotUnit;
    }

    public void setPreAvgBpsTotUnit(String preAvgBpsTotUnit)
    {
        this.preAvgBpsTotUnit = preAvgBpsTotUnit;
    }

    public String getPreAvgConnCurrUnit()
    {
        return preAvgConnCurrUnit;
    }

    public void setPreAvgConnCurrUnit(String preAvgConnCurrUnit)
    {
        this.preAvgConnCurrUnit = preAvgConnCurrUnit;
    }

    public String getPreAvgRespTimeUnit()
    {
        return preAvgRespTimeUnit;
    }

    public void setPreAvgRespTimeUnit(String preAvgRespTimeUnit)
    {
        this.preAvgRespTimeUnit = preAvgRespTimeUnit;
    }

    public String getPreCurrBpsInUnit()
    {
        return preCurrBpsInUnit;
    }

    public void setPreCurrBpsInUnit(String preCurrBpsInUnit)
    {
        this.preCurrBpsInUnit = preCurrBpsInUnit;
    }

    public String getPreCurrBpsOutUnit()
    {
        return preCurrBpsOutUnit;
    }

    public void setPreCurrBpsOutUnit(String preCurrBpsOutUnit)
    {
        this.preCurrBpsOutUnit = preCurrBpsOutUnit;
    }

    public String getPreCurrBpsTotUnit()
    {
        return preCurrBpsTotUnit;
    }

    public void setPreCurrBpsTotUnit(String preCurrBpsTotUnit)
    {
        this.preCurrBpsTotUnit = preCurrBpsTotUnit;
    }

    public String getPreCurrConnCurrUnit()
    {
        return preCurrConnCurrUnit;
    }

    public void setPreCurrConnCurrUnit(String preCurrConnCurrUnit)
    {
        this.preCurrConnCurrUnit = preCurrConnCurrUnit;
    }

    public String getPreCurrRespTimeUnit()
    {
        return preCurrRespTimeUnit;
    }

    public void setPreCurrRespTimeUnit(String preCurrRespTimeUnit)
    {
        this.preCurrRespTimeUnit = preCurrRespTimeUnit;
    }

    public String getSubtractionMaxBpsInUnit()
    {
        return subtractionMaxBpsInUnit;
    }

    public void setSubtractionMaxBpsInUnit(String subtractionMaxBpsInUnit)
    {
        this.subtractionMaxBpsInUnit = subtractionMaxBpsInUnit;
    }

    public String getSubtractionMaxBpsOutUnit()
    {
        return subtractionMaxBpsOutUnit;
    }

    public void setSubtractionMaxBpsOutUnit(String subtractionMaxBpsOutUnit)
    {
        this.subtractionMaxBpsOutUnit = subtractionMaxBpsOutUnit;
    }

    public String getSubtractionMaxBpsTotUnit()
    {
        return subtractionMaxBpsTotUnit;
    }

    public void setSubtractionMaxBpsTotUnit(String subtractionMaxBpsTotUnit)
    {
        this.subtractionMaxBpsTotUnit = subtractionMaxBpsTotUnit;
    }

    public String getSubtractionMaxConnCurrUnit()
    {
        return subtractionMaxConnCurrUnit;
    }

    public void setSubtractionMaxConnCurrUnit(String subtractionMaxConnCurrUnit)
    {
        this.subtractionMaxConnCurrUnit = subtractionMaxConnCurrUnit;
    }

    public String getSubtractionMaxRespTimeUnit()
    {
        return subtractionMaxRespTimeUnit;
    }

    public void setSubtractionMaxRespTimeUnit(String subtractionMaxRespTimeUnit)
    {
        this.subtractionMaxRespTimeUnit = subtractionMaxRespTimeUnit;
    }

    public String getSubtractionAvgBpsInUnit()
    {
        return subtractionAvgBpsInUnit;
    }

    public void setSubtractionAvgBpsInUnit(String subtractionAvgBpsInUnit)
    {
        this.subtractionAvgBpsInUnit = subtractionAvgBpsInUnit;
    }

    public String getSubtractionAvgBpsOutUnit()
    {
        return subtractionAvgBpsOutUnit;
    }

    public void setSubtractionAvgBpsOutUnit(String subtractionAvgBpsOutUnit)
    {
        this.subtractionAvgBpsOutUnit = subtractionAvgBpsOutUnit;
    }

    public String getSubtractionAvgBpsTotUnit()
    {
        return subtractionAvgBpsTotUnit;
    }

    public void setSubtractionAvgBpsTotUnit(String subtractionAvgBpsTotUnit)
    {
        this.subtractionAvgBpsTotUnit = subtractionAvgBpsTotUnit;
    }

    public String getSubtractionAvgConnCurrUnit()
    {
        return subtractionAvgConnCurrUnit;
    }

    public void setSubtractionAvgConnCurrUnit(String subtractionAvgConnCurrUnit)
    {
        this.subtractionAvgConnCurrUnit = subtractionAvgConnCurrUnit;
    }

    public String getSubtractionAvgRespTimeUnit()
    {
        return subtractionAvgRespTimeUnit;
    }

    public void setSubtractionAvgRespTimeUnit(String subtractionAvgRespTimeUnit)
    {
        this.subtractionAvgRespTimeUnit = subtractionAvgRespTimeUnit;
    }

    public String getSubtractionCurrBpsInUnit()
    {
        return subtractionCurrBpsInUnit;
    }

    public void setSubtractionCurrBpsInUnit(String subtractionCurrBpsInUnit)
    {
        this.subtractionCurrBpsInUnit = subtractionCurrBpsInUnit;
    }

    public String getSubtractionCurrBpsOutUnit()
    {
        return subtractionCurrBpsOutUnit;
    }

    public void setSubtractionCurrBpsOutUnit(String subtractionCurrBpsOutUnit)
    {
        this.subtractionCurrBpsOutUnit = subtractionCurrBpsOutUnit;
    }

    public String getSubtractionCurrBpsTotUnit()
    {
        return subtractionCurrBpsTotUnit;
    }

    public void setSubtractionCurrBpsTotUnit(String subtractionCurrBpsTotUnit)
    {
        this.subtractionCurrBpsTotUnit = subtractionCurrBpsTotUnit;
    }

    public String getSubtractionCurrConnCurrUnit()
    {
        return subtractionCurrConnCurrUnit;
    }

    public void setSubtractionCurrConnCurrUnit(String subtractionCurrConnCurrUnit)
    {
        this.subtractionCurrConnCurrUnit = subtractionCurrConnCurrUnit;
    }

    public String getSubtractionCurrRespTimeUnit()
    {
        return subtractionCurrRespTimeUnit;
    }

    public void setSubtractionCurrRespTimeUnit(String subtractionCurrRespTimeUnit)
    {
        this.subtractionCurrRespTimeUnit = subtractionCurrRespTimeUnit;
    }        
}
