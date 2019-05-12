package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcSystemResources
{
	private Date occurTime;
	private Integer cpu1Usage=-1;
	
	private Integer cpu2Usage=-1;
	private Integer cpu3Usage=-1;
	private Integer cpu4Usage=-1;
	private Integer cpu5Usage=-1;
	private Integer cpu6Usage=-1;
	private Integer cpu7Usage=-1;
	private Integer cpu8Usage=-1;
	private Integer cpu9Usage=-1;
	private Integer cpu10Usage=-1;
	private Integer cpu11Usage=-1;
	private Integer cpu12Usage=-1;
	private Integer cpu13Usage=-1;
	private Integer cpu14Usage=-1;
	private Integer cpu15Usage=-1;
	private Integer cpu16Usage=-1;

	private Integer memUsage;

	@Override
	public String toString()
	{
		return String.format("OBDtoAdcSystemResources [occurTime=%s, cpu1Usage=%s, cpu2Usage=%s, cpu3Usage=%s, cpu4Usage=%s, cpu5Usage=%s, cpu6Usage=%s, cpu7Usage=%s, cpu8Usage=%s, cpu9Usage=%s, cpu10Usage=%s, cpu11Usage=%s, cpu12Usage=%s, cpu13Usage=%s, cpu14Usage=%s, cpu15Usage=%s, cpu16Usage=%s, memUsage=%s, getClass()=%s, hashCode()=%s, toString()=%s]", occurTime, cpu1Usage, cpu2Usage, cpu3Usage, cpu4Usage, cpu5Usage, cpu6Usage, cpu7Usage, cpu8Usage, cpu9Usage, cpu10Usage, cpu11Usage, cpu12Usage, cpu13Usage, cpu14Usage, cpu15Usage, cpu16Usage, memUsage, getClass(), hashCode(), super.toString());
	}

	public Date getOccurTime()
	{
		return occurTime;
	}

	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}

	public Integer getCpu1Usage()
	{
		return cpu1Usage;
	}

	public void setCpu1Usage(Integer cpu1Usage)
	{
		this.cpu1Usage = cpu1Usage;
	}

	public Integer getCpu2Usage()
	{
		return cpu2Usage;
	}

	public void setCpu2Usage(Integer cpu2Usage)
	{
		this.cpu2Usage = cpu2Usage;
	}

	public Integer getCpu3Usage()
	{
		return cpu3Usage;
	}

	public void setCpu3Usage(Integer cpu3Usage)
	{
		this.cpu3Usage = cpu3Usage;
	}

	public Integer getCpu4Usage()
	{
		return cpu4Usage;
	}

	public void setCpu4Usage(Integer cpu4Usage)
	{
		this.cpu4Usage = cpu4Usage;
	}

	public Integer getCpu5Usage()
	{
		return cpu5Usage;
	}

	public void setCpu5Usage(Integer cpu5Usage)
	{
		this.cpu5Usage = cpu5Usage;
	}

	public Integer getCpu6Usage()
	{
		return cpu6Usage;
	}

	public void setCpu6Usage(Integer cpu6Usage)
	{
		this.cpu6Usage = cpu6Usage;
	}

	public Integer getCpu7Usage()
	{
		return cpu7Usage;
	}

	public void setCpu7Usage(Integer cpu7Usage)
	{
		this.cpu7Usage = cpu7Usage;
	}

	public Integer getCpu8Usage()
	{
		return cpu8Usage;
	}

	public void setCpu8Usage(Integer cpu8Usage)
	{
		this.cpu8Usage = cpu8Usage;
	}

	public Integer getCpu9Usage()
	{
		return cpu9Usage;
	}

	public void setCpu9Usage(Integer cpu9Usage)
	{
		this.cpu9Usage = cpu9Usage;
	}

	public Integer getCpu10Usage()
	{
		return cpu10Usage;
	}

	public void setCpu10Usage(Integer cpu10Usage)
	{
		this.cpu10Usage = cpu10Usage;
	}

	public Integer getCpu11Usage()
	{
		return cpu11Usage;
	}

	public void setCpu11Usage(Integer cpu11Usage)
	{
		this.cpu11Usage = cpu11Usage;
	}

	public Integer getCpu12Usage()
	{
		return cpu12Usage;
	}

	public void setCpu12Usage(Integer cpu12Usage)
	{
		this.cpu12Usage = cpu12Usage;
	}

	public Integer getCpu13Usage()
	{
		return cpu13Usage;
	}

	public void setCpu13Usage(Integer cpu13Usage)
	{
		this.cpu13Usage = cpu13Usage;
	}

	public Integer getCpu14Usage()
	{
		return cpu14Usage;
	}

	public void setCpu14Usage(Integer cpu14Usage)
	{
		this.cpu14Usage = cpu14Usage;
	}

	public Integer getCpu15Usage()
	{
		return cpu15Usage;
	}

	public void setCpu15Usage(Integer cpu15Usage)
	{
		this.cpu15Usage = cpu15Usage;
	}

	public Integer getCpu16Usage()
	{
		return cpu16Usage;
	}

	public void setCpu16Usage(Integer cpu16Usage)
	{
		this.cpu16Usage = cpu16Usage;
	}

	public Integer getMemUsage()
	{
		return memUsage;
	}

	public void setMemUsage(Integer memUsage)
	{
		this.memUsage = memUsage;
	}
}
