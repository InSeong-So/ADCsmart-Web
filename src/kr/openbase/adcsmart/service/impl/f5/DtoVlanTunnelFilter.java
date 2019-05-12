package kr.openbase.adcsmart.service.impl.f5;

import java.io.Serializable;
import java.util.ArrayList;


public class DtoVlanTunnelFilter implements Serializable
{
	private static final long serialVersionUID = 10L;
	Integer status;
	ArrayList<String> vlanName;
	
	@Override
	public String toString()
	{
		return "DtoVlanTunnelFilter [status=" + status + ", vlanName=" + vlanName + "]";
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public ArrayList<String> getVlanName()
	{
		return vlanName;
	}
	public void setVlanName(ArrayList<String> vlanName)
	{
		this.vlanName = vlanName;
	}

    public boolean configEquals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        DtoVlanTunnelFilter other = (DtoVlanTunnelFilter) obj;
        if(status == null)
        {
            if(other.status != null)
                return false;
        }
        else if(!status.equals(other.status))
            return false;
        
        //다음 두줄: F5읽기에서는 vlan이 없으면 null 리턴, DB읽기에서는 vlan이 없으면 empty list 리턴, 같은 건데 비교는 틀리다고 하므로  맞추려고 아래와 같이 작업함
        if(vlanName.isEmpty()) vlanName = null;
        if(other.vlanName.isEmpty()) other.vlanName = null;
        
        if((vlanName == null && other.vlanName != null) || (vlanName != null && other.vlanName == null)) 
        {
            return false;
        }
        if(vlanName != null && other.vlanName != null)
        {
            if(vlanName.containsAll(other.vlanName) && other.vlanName.containsAll(vlanName)) //ArrayList 일치
            {
                return true;                
            }
            else
            {
                return false;
            }
        }
        return true;
    }
}
