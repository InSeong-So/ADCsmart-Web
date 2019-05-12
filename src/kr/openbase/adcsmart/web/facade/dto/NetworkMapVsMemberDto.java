/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

/**
 * @author paul
 *
 */
public class NetworkMapVsMemberDto 
{
	private String index;
	private String ipAddress;
	private Integer port;
	private String addPort;
	private Integer status;
	private Integer bakupType;
	private String bakupIndex;
    public String getIndex()
    {
        return index;
    }
    public void setIndex(String index)
    {
        this.index = index;
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
    public String getAddPort()
    {
        return addPort;
    }
    public void setAddPort(String addPort)
    {
        this.addPort = addPort;
    }
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    public Integer getBakupType()
    {
        return bakupType;
    }
    public void setBakupType(Integer bakupType)
    {
        this.bakupType = bakupType;
    }
    public String getBakupIndex()
    {
        return bakupIndex;
    }
    public void setBakupIndex(String bakupIndex)
    {
        this.bakupIndex = bakupIndex;
    }
    @Override
    public String toString()
    {
        return "NetworkMapVsMemberDto [index=" + index + ", ipAddress="
                + ipAddress + ", port=" + port + ", addPort=" + addPort
                + ", status=" + status + ", bakupType=" + bakupType
                + ", bakupIndex=" + bakupIndex + "]";
    }
}
