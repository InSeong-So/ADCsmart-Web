/*
 * TMP_SLB_VIRTUAL_SERVER 테이블 처
 */
package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class OBDtoAdcVServerAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String    index;
	private Integer   adcIndex;
	private Timestamp applyTime;
	private Integer   useYN;
	private Integer   status;
	private Integer   state;
	private String   alteonId;
	private String    name;
	private String    vIP;
	private boolean   vrrpYN; //vrrpState를 추가하면서 이 값은 전혀 안 쓴다. 이력에서 이 class 인스턴스를 저장하기 때문에 제거할 수 없어서 남겨둔다. 
	private Integer   routerIndex;
	private Integer   vrIndex;
	private Integer   ifNum;
	private ArrayList<OBDtoAdcVService> vserviceList;
	private Integer vrrpState; //vrrpState, -1=설정 없음, 0=disable, 1=enable 
	private String    subInfo; //기타 정보칸, virtual server에 할당된 network class가 있다.
	private Integer   nwclassId; //네트워크 class 설정 여부 0: 설정 없음, 1:설정함

	@Override
	public String toString()
	{
		return "OBDtoAdcVServerAlteon [index=" + index + ", adcIndex="
				+ adcIndex + ", applyTime=" + applyTime + ", useYN=" + useYN
				+ ", status=" + status + ", state=" + state + ", alteonId="
				+ alteonId + ", name=" + name + ", vIP=" + vIP + ", vrrpYN="
				+ vrrpYN + ", routerIndex=" + routerIndex + ", vrIndex="
				+ vrIndex + ", ifNum=" + ifNum + ", vserviceList="
				+ vserviceList + ", vrrpState=" + vrrpState + ", subInfo="
				+ subInfo + ", nwclassId=" + nwclassId + "]";
	}



	public OBDtoAdcVServerAlteon()
	{
	}
	//객체 clone 생성자
	public OBDtoAdcVServerAlteon(OBDtoAdcVServerAlteon obj)
	{
		if(obj!=null)
		{
			this.index = obj.getIndex();
			this.adcIndex = obj.getAdcIndex();
			this.applyTime = obj.getApplyTime();
			this.useYN = obj.getUseYN();
			this.status = obj.getStatus();
			this.state = obj.getState();
			this.alteonId = obj.getAlteonId();
			this.name = obj.getName();
			this.vIP = obj.getvIP();
			this.vrrpYN = false; //안 쓴다.
			this.vrrpState = obj.getVrrpState();
			this.routerIndex = obj.getRouterIndex();
			this.vrIndex = obj.getVrIndex();
			this.ifNum = obj.getIfNum();
			
			this.vserviceList = new ArrayList<OBDtoAdcVService>();
			for(OBDtoAdcVService obj2:obj.getVserviceList())
			{
				if(obj2!=null)
				{
					OBDtoAdcVService temp = new OBDtoAdcVService(obj2);
					this.vserviceList.add(temp);
				}
			}
			this.vrrpState = obj.getVrrpState();
			this.subInfo = obj.getSubInfo();
			this.nwclassId = obj.getNwclassId();
		}
	}

	public Integer getUseYN()
	{
		return useYN;
	}
	public void setUseYN(Integer useYN)
	{
		this.useYN = useYN;
	}	
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getApplyTime()
	{
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime)
	{
		this.applyTime = applyTime;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getAlteonId()
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getvIP()
	{
		return vIP;
	}
	public void setvIP(String vIP)
	{
		this.vIP = vIP;
	}
	public boolean isVrrpYN()
	{
		return vrrpYN;
	}
	public void setVrrpYN(boolean vrrpYN)
	{
		this.vrrpYN = vrrpYN;
	}
	public Integer getRouterIndex()
	{
		return routerIndex;
	}
	public void setRouterIndex(Integer routerIndex)
	{
		this.routerIndex = routerIndex;
	}
	public Integer getVrIndex()
	{
		return vrIndex;
	}
	public void setVrIndex(Integer vrIndex)
	{
		this.vrIndex = vrIndex;
	}
	public Integer getIfNum()
	{
		return ifNum;
	}
	public void setIfNum(Integer ifNum)
	{
		this.ifNum = ifNum;
	}
	public ArrayList<OBDtoAdcVService> getVserviceList()
	{
		return vserviceList;
	}
	public void setVserviceList(ArrayList<OBDtoAdcVService> vserviceList)
	{
		this.vserviceList = vserviceList;
	}
	public Integer getVrrpState()
	{
		return vrrpState;
	}
	public void setVrrpState(Integer vrrpState)
	{
		this.vrrpState = vrrpState;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public String getSubInfo()
	{
		return subInfo;
	}
	public void setSubInfo(String subInfo)
	{
		this.subInfo = subInfo;
	}
	public Integer getNwclassId()
	{
		return nwclassId;
	}
	public void setNwclassId(Integer nwclassId)
	{
		this.nwclassId = nwclassId;
	}
}
