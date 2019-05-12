package kr.openbase.adcsmart.service.dto;

public class OBDtoRespTimeInfo
{
    private Integer respIndex;
    private String occurTime;
    private Integer respTime;
    
    @Override
    public String toString()
    {
        return "OBDtoRespTimeInfo [respIndex=" + respIndex + ", occurTime="
                + occurTime + ", respTime=" + respTime + "]";
    }
    
    public Integer getRespIndex()
    {
        return respIndex;
    }
    public void setRespIndex(Integer respIndex)
    {
        this.respIndex = respIndex;
    }
    public String getOccurTime()
    {
        return occurTime;
    }
    public void setOccurTime(String occurTime)
    {
        this.occurTime = occurTime;
    }
    public Integer getRespTime()
    {
        return respTime;
    }
    public void setRespTime(Integer respTime)
    {
        this.respTime = respTime;
    }
}
