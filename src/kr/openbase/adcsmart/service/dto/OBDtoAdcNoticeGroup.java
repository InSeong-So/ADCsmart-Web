package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcNoticeGroup
{	
	private Integer adcIndex;	
	private String poolIndex;
	private String poolName;
	private String alteonID;
	private Integer accntIndex;
    
	public Integer getAdcIndex()
    {
        return adcIndex;
    }
    public String getPoolName()
    {
        return poolName;
    }
    public void setPoolName(String poolName)
    {
        this.poolName = poolName;
    }
    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
    }
    public String getPoolIndex()
    {
        return poolIndex;
    }
    public void setPoolIndex(String poolIndex)
    {
        this.poolIndex = poolIndex;
    }
    public String getAlteonID()
    {
        return alteonID;
    }
    public void setAlteonID(String alteonID)
    {
        this.alteonID = alteonID;
    }
    public Integer getAccntIndex() 
    {
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex) 
	{
		this.accntIndex = accntIndex;
	}
	@Override
	public String toString() 
	{
		return "OBDtoAdcNoticeGroup [adcIndex=" + adcIndex + ", poolIndex=" + poolIndex + ", poolName=" + poolName
				+ ", alteonID=" + alteonID + ", accntIndex=" + accntIndex + "]";
	}
}
