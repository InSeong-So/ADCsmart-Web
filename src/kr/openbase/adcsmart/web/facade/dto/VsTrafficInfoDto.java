/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;
import java.util.List;

/**
 * @author paul
 *
 */
public class VsTrafficInfoDto 
{
	
	private Date occurredTime;
	private String name;
	private String vsIndex;
	private String ipAddress;
	private Integer port;
	private Integer status;
	private String inBps;
	private String outBps;
	private String totalBps;
	private String inPps;
	private String outPps;
	private String totalPps;
	private String activeConnections;
	private String maxConnections;
	private String totalConnections;
	private List<VsMemberTrafficInfoDto> vsMemberTrafficInfoList;
	private String dispName;
	
	/**
	 * @return the occurredTime
	 */
	public Date getOccurredTime() 
	{
		return occurredTime;
	}
	
	/**
	 * @param occurredTime the occurredTime to set
	 */
	public void setOccurredTime(Date occurredTime) 
	{
		this.occurredTime = occurredTime;
	}
	
	/**
	 * @return the name
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	/**
	 * @return the vsIndex
	 */
	public String getVsIndex() 
	{
		return vsIndex;
	}

	/**
	 * @param vsIndex the vsIndex to set
	 */
	public void setVsIndex(String vsIndex) 
	{
		this.vsIndex = vsIndex;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() 
	{
		return ipAddress;
	}
	
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}
	
	/**
	 * @return the port
	 */
	public Integer getPort() 
	{
		return port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) 
	{
		this.port = port;
	}
	
	/**
	 * @return the status
	 */
	public Integer getStatus() 
	{
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) 
	{
		this.status = status;
	}
	
	/**
	 * @return the inBps
	 */
	public String getInBps() 
	{
		return inBps;
	}

	/**
	 * @param inBps the inBps to set
	 */
	public void setInBps(String inBps) 
	{
		this.inBps = inBps;
	}

	/**
	 * @return the outBps
	 */
	public String getOutBps() 
	{
		return outBps;
	}

	/**
	 * @param outBps the outBps to set
	 */
	public void setOutBps(String outBps) 
	{
		this.outBps = outBps;
	}

	/**
	 * @return the totalBps
	 */
	public String getTotalBps() 
	{
		return totalBps;
	}

	/**
	 * @param totalBps the totalBps to set
	 */
	public void setTotalBps(String totalBps) 
	{
		this.totalBps = totalBps;
	}

	/**
	 * @return the inPps
	 */
	public String getInPps() 
	{
		return inPps;
	}

	/**
	 * @param inPps the inPps to set
	 */
	public void setInPps(String inPps) 
	{
		this.inPps = inPps;
	}

	/**
	 * @return the outPps
	 */
	public String getOutPps() 
	{
		return outPps;
	}

	/**
	 * @param outPps the outPps to set
	 */
	public void setOutPps(String outPps) 
	{
		this.outPps = outPps;
	}

	/**
	 * @return the totalPps
	 */
	public String getTotalPps() 
	{
		return totalPps;
	}

	/**
	 * @param totalPps the totalPps to set
	 */
	public void setTotalPps(String totalPps) 
	{
		this.totalPps = totalPps;
	}

	/**
	 * @return the activeConnections
	 */
	public String getActiveConnections() 
	{
		return activeConnections;
	}

	/**
	 * @param activeConnections the activeConnections to set
	 */
	public void setActiveConnections(String activeConnections) 
	{
		this.activeConnections = activeConnections;
	}

	/**
	 * @return the maxConnections
	 */
	public String getMaxConnections() 
	{
		return maxConnections;
	}

	/**
	 * @param maxConnections the maxConnections to set
	 */
	public void setMaxConnections(String maxConnections) 
	{
		this.maxConnections = maxConnections;
	}

	/**
	 * @return the totalConnections
	 */
	public String getTotalConnections() 
	{
		return totalConnections;
	}

	/**
	 * @param totalConnections the totalConnections to set
	 */
	public void setTotalConnections(String totalConnections)
	{
		this.totalConnections = totalConnections;
	}

	/**
	 * @return the vsMemberTrafficInfoList
	 */
	public List<VsMemberTrafficInfoDto> getVsMemberTrafficInfoList() 
	{
		return vsMemberTrafficInfoList;
	}

	public String getDispName()
	{
		return dispName;
	}

	public void setDispName(String dispName)
	{
		this.dispName = dispName;
	}

	/**
	 * @param vsMemberTrafficInfoList the vsMemberTrafficInfoList to set
	 */
	public void setVsMemberTrafficInfoList(
			List<VsMemberTrafficInfoDto> vsMemberTrafficInfoList) 
	{
		this.vsMemberTrafficInfoList = vsMemberTrafficInfoList;
	}

	@Override
	public String toString()
	{
		return "VsTrafficInfoDto [occurredTime=" + occurredTime + ", name="
				+ name + ", vsIndex=" + vsIndex + ", ipAddress=" + ipAddress
				+ ", port=" + port + ", status=" + status + ", inBps=" + inBps
				+ ", outBps=" + outBps + ", totalBps=" + totalBps + ", inPps="
				+ inPps + ", outPps=" + outPps + ", totalPps=" + totalPps
				+ ", activeConnections=" + activeConnections
				+ ", maxConnections=" + maxConnections + ", totalConnections="
				+ totalConnections + ", vsMemberTrafficInfoList="
				+ vsMemberTrafficInfoList + ", dispName=" + dispName + "]";
	}

}
