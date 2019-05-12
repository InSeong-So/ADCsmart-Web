/*
 * a ADC 장비의 Pool에 설정된 Member 정보
 */
package kr.openbase.adcsmart.service.dto;
import java.io.Serializable;

import kr.openbase.adcsmart.service.impl.f5.DtoPoolMember;

public class OBDtoAdcPoolMemberF5 implements Serializable 
{
	private static final long serialVersionUID = 10L;
	private String index;
	private String ipAddress;// ip 주소가 입력된다.
	private Integer port;
	private Integer state;
	private Integer status;
	private Integer ratio;

	public OBDtoAdcPoolMemberF5 ()
	{
		this.ipAddress = null;
		this.port = null;
		this.state = null;
		this.status = null;
		this.index = null;
		this.ratio = null;
	}
    public OBDtoAdcPoolMemberF5 (DtoPoolMember obj)
    {
        if(obj==null)
        {
            return;
        }
        this.ipAddress = obj.getIpAddress();
        this.port = obj.getPort();
        this.state = obj.getState();
        this.status = obj.getStatus();
        this.index = null;
        this.ratio = obj.getRatio();
    }
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public OBDtoAdcPoolMemberF5 (OBDtoAdcPoolMemberF5 obj)
	{
		if(obj!=null)
		{
			this.index = obj.index;
			this.ipAddress = obj.ipAddress;
			this.port = obj.port;
			this.state = obj.state;
			this.status = obj.status;
			this.ratio = obj.ratio;
		}
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getPort()
	{
		return this.port;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public Integer getState()
	{
		return this.state;
	}	
	public Integer getRatio()
    {
        return ratio;
    }
    public void setRatio(Integer ratio)
    {
        this.ratio = ratio;
    }
    @Override
	public String toString() {
		return "OBDtoAdcPoolMemberF5 [index=" + index + ", ipAddress="
				+ ipAddress + ", port=" + port + ", state=" + state
				+ ", status=" + status + ", ratio=" + ratio + "]";
	}

    public boolean configEquals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        OBDtoAdcPoolMemberF5 other = (OBDtoAdcPoolMemberF5) obj;
        if(ipAddress == null)
        {
            if(other.ipAddress != null)
                return false;
        }
        else if(!ipAddress.equals(other.ipAddress))
            return false;
        if(port == null)
        {
            if(other.port != null)
                return false;
        }
        else if(!port.equals(other.port))
            return false;
        if(ratio == null)
        {
            if(other.ratio != null)
                return false;
        }
        else if(!ratio.equals(other.ratio))
            return false;
        if(state == null)
        {
            if(other.state != null)
                return false;
        }
        else if(!state.equals(other.state))
            return false;

        return true;
    }
}
