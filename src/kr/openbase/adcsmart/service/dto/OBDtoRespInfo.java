package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBDtoRespInfo
{
    private Integer index;
    private Integer groupIndex;
    private String ipaddress;
    private Integer type;
    private Integer port;
    private String path;
    private String comment;
    private Integer maxRespTime;
    private Integer avgRespTime;
    private Integer minRespTime;
    private Integer currRespTime;
    private Object threadPoolObj;
    private Integer respOrder;
    private ArrayList<OBDtoRespTimeInfo> respTimeInfo;    
    private String maxRespTimeUnit;
    private String avgRespTimeUnit;
    private String minRespTimeUnit;
    private String currRespTimeUnit;
    
    @Override
    public String toString()
    {
        return "OBDtoRespInfo [index=" + index + ", groupIndex=" + groupIndex
                + ", ipaddress=" + ipaddress + ", type=" + type + ", port="
                + port + ", path=" + path + ", comment=" + comment
                + ", maxRespTime=" + maxRespTime + ", avgRespTime="
                + avgRespTime + ", minRespTime=" + minRespTime
                + ", currRespTime=" + currRespTime + ", threadPoolObj="
                + threadPoolObj + ", respOrder=" + respOrder
                + ", respTimeInfo=" + respTimeInfo + ", maxRespTimeUnit="
                + maxRespTimeUnit + ", avgRespTimeUnit=" + avgRespTimeUnit
                + ", minRespTimeUnit=" + minRespTimeUnit
                + ", currRespTimeUnit=" + currRespTimeUnit + "]";
    }
    public Integer getRespOrder()
    {
        return respOrder;
    }
    public void setRespOrder(Integer respOrder)
    {
        this.respOrder = respOrder;
    }
    public String getIpaddress()
    {
        return ipaddress;
    }
    public void setIpaddress(String ipaddress)
    {
        this.ipaddress = ipaddress;
    }
    public Integer getType()
    {
        return type;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }
    public Integer getPort()
    {
        return port;
    }
    public void setPort(Integer port)
    {
        this.port = port;
    }
    public String getPath()
    {
        return path;
    }
    public void setPath(String path)
    {
        this.path = path;
    }
    public String getComment()
    {
        return comment;
    }
    public void setComment(String comment)
    {
        this.comment = comment;
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
    public ArrayList<OBDtoRespTimeInfo> getRespTimeInfo()
    {
        return respTimeInfo;
    }
    public Integer getMinRespTime()
    {
        return minRespTime;
    }

    public Object getThreadPoolObj()
    {
        return threadPoolObj;
    }

    public void setThreadPoolObj(Object threadPoolObj)
    {
        this.threadPoolObj = threadPoolObj;
    }

    public void setMinRespTime(Integer minRespTime)
    {
        this.minRespTime = minRespTime;
    }

    public Integer getIndex()
    {
        return index;
    }

    public Integer getGroupIndex()
    {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex)
    {
        this.groupIndex = groupIndex;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }

    public void setRespTimeInfo(ArrayList<OBDtoRespTimeInfo> respTimeInfo)
    {
        this.respTimeInfo = respTimeInfo;
    }
    public String getMaxRespTimeUnit()
    {
        maxRespTimeUnit = OBUtility.toStringWithIntUnit(maxRespTime, "");
        return maxRespTimeUnit;
    }
    public void setMaxRespTimeUnit(String maxRespTimeUnit)
    {
        this.maxRespTimeUnit = maxRespTimeUnit;
    }
    public String getAvgRespTimeUnit()
    {
        avgRespTimeUnit = OBUtility.toStringWithIntUnit(avgRespTime, "");
        return avgRespTimeUnit;
    }
    public void setAvgRespTimeUnit(String avgRespTimeUnit)
    {
        this.avgRespTimeUnit = avgRespTimeUnit;
    }
    public String getMinRespTimeUnit()
    {
        minRespTimeUnit = OBUtility.toStringWithIntUnit(minRespTime, "");
        return minRespTimeUnit;
    }
    public void setMinRespTimeUnit(String minRespTimeUnit)
    {
        this.minRespTimeUnit = minRespTimeUnit;
    }
    public String getCurrRespTimeUnit()
    {
        currRespTimeUnit = OBUtility.toStringWithIntUnit(currRespTime, "");
        return currRespTimeUnit;
    }
    public void setCurrRespTimeUnit(String currRespTimeUnit)
    {
        this.currRespTimeUnit = currRespTimeUnit;
    }
}
