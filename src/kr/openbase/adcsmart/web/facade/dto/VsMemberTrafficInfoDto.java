/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

/**
 * @author paul
 *
 */
public class VsMemberTrafficInfoDto 
{	
	private String index;// vsvc에 할당된 멤버의 index 정보..
	private Date occurredTime;
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
	private String addPort;
    public String getIndex()
    {
        return index;
    }
    public void setIndex(String index)
    {
        this.index = index;
    }
    public Date getOccurredTime()
    {
        return occurredTime;
    }
    public void setOccurredTime(Date occurredTime)
    {
        this.occurredTime = occurredTime;
    }
    public String getIpAddress()
    {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
    public Integer getPort()
    {
        return port;
    }
    public void setPort(Integer port)
    {
        this.port = port;
    }
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    public String getInBps()
    {
        return inBps;
    }
    public void setInBps(String inBps)
    {
        this.inBps = inBps;
    }
    public String getOutBps()
    {
        return outBps;
    }
    public void setOutBps(String outBps)
    {
        this.outBps = outBps;
    }
    public String getTotalBps()
    {
        return totalBps;
    }
    public void setTotalBps(String totalBps)
    {
        this.totalBps = totalBps;
    }
    public String getInPps()
    {
        return inPps;
    }
    public void setInPps(String inPps)
    {
        this.inPps = inPps;
    }
    public String getOutPps()
    {
        return outPps;
    }
    public void setOutPps(String outPps)
    {
        this.outPps = outPps;
    }
    public String getTotalPps()
    {
        return totalPps;
    }
    public void setTotalPps(String totalPps)
    {
        this.totalPps = totalPps;
    }
    public String getActiveConnections()
    {
        return activeConnections;
    }
    public void setActiveConnections(String activeConnections)
    {
        this.activeConnections = activeConnections;
    }
    public String getMaxConnections()
    {
        return maxConnections;
    }
    public void setMaxConnections(String maxConnections)
    {
        this.maxConnections = maxConnections;
    }
    public String getTotalConnections()
    {
        return totalConnections;
    }
    public void setTotalConnections(String totalConnections)
    {
        this.totalConnections = totalConnections;
    }
    public String getAddPort()
    {
        return addPort;
    }
    public void setAddPort(String addPort)
    {
        this.addPort = addPort;
    }
    @Override
    public String toString()
    {
        return "VsMemberTrafficInfoDto [index=" + index + ", occurredTime="
                + occurredTime + ", ipAddress=" + ipAddress + ", port=" + port
                + ", status=" + status + ", inBps=" + inBps + ", outBps="
                + outBps + ", totalBps=" + totalBps + ", inPps=" + inPps
                + ", outPps=" + outPps + ", totalPps=" + totalPps
                + ", activeConnections=" + activeConnections
                + ", maxConnections=" + maxConnections + ", totalConnections="
                + totalConnections + ", addPort=" + addPort + "]";
    }			
}
