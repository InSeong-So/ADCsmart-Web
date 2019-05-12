package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoMonTotalRealOne
{
	private String    index;
	private String    name;
	private String    ip;
	private Integer   status;
	private Integer   state;
	private String    backup;
	private Integer   used;  //real이 쓰이고 있는지 0:안씀, 1:씀
	private Integer   group; //real이 포함된 그룹 수
	private Integer   ratio;
	//private String    id; //??
	private String    alteonId;

	private Integer   adcIndex;
	private String    adcName;
	private String    adcIp;
	private Integer   adcType; //화면에 표시하지 않지만 필요
	@Override
    public String toString()
    {
        return "OBDtoMonTotalRealOne [index=" + index + ", name=" + name
                + ", ip=" + ip + ", status=" + status + ", state=" + state
                + ", backup=" + backup + ", used=" + used + ", group=" + group
                + ", ratio=" + ratio + ", alteonId=" + alteonId + ", adcIndex="
                + adcIndex + ", adcName=" + adcName + ", adcIp=" + adcIp
                + ", adcType=" + adcType + "]";
    }
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public String getBackup()
	{
		return backup;
	}
	public void setBackup(String backup)
	{
		this.backup = backup;
	}
	public Integer getUsed()
	{
		return used;
	}
	public void setUsed(Integer used)
	{
		this.used = used;
	}
	public Integer getGroup()
	{
		return group;
	}
	public void setGroup(Integer group)
	{
		this.group = group;
	}
	public Integer getRatio()
	{
		return ratio;
	}
	public void setRatio(Integer ratio)
	{
		this.ratio = ratio;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp)
	{
		this.adcIp = adcIp;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
    public String getAlteonId()
    {
        return alteonId;
    }
    public void setAlteonId(String alteonId)
    {
        this.alteonId = alteonId;
    }
}
