package kr.openbase.adcsmart.web.facade.dto.adcmon;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSConnHistory;

public class OBDtoDashboardTop5Data
{
	private Date 	occurTime=null;
	
	private String 	firstName="";
	private String 	secondName="";
	private String 	thirdName="";
	private String 	fourthName="";
	private String 	fifthName="";
	
	private Long	firstValue=0L; //현재 데이터.
	private Long	secondValue=0L; //현재 데이터.
	private Long	thirdValue=0L; //현재 데이터.
	private Long	fourthValue=0L; //현재 데이터.
	private Long	fifthValue=0L; //현재 데이터.
	
	public ArrayList<OBDtoDashboardTop5Data>toTop5Data(ArrayList<OBDtoDashboardVSConnHistory> list)
	{
		TreeMap<Long, OBDtoDashboardTop5Data> top5Map = new TreeMap<Long, OBDtoDashboardTop5Data>();
		// 첫번째 데이터 추출.
		OBDtoDashboardVSConnHistory item;
		if(list.size()<=0)
		{
			ArrayList<OBDtoDashboardTop5Data> retVal = new ArrayList<OBDtoDashboardTop5Data>();
			return retVal;
		}
		item = list.get(0);
		if(null != item)
		{
			ArrayList<OBDtoDataObj> connList = item.getConnection();
			for(OBDtoDataObj conn:connList)
			{
				long time = conn.getOccurTime().getTime()/60000;// 60초 단위로 시간을 truncate 한다..
				OBDtoDashboardTop5Data obj = top5Map.get(time);
				if(obj==null)
				{
					obj = new OBDtoDashboardTop5Data();
					obj.setOccurTime(conn.getOccurTime());
					obj.setFirstName(item.getVsIPAddress());
					obj.setFirstValue(conn.getValue());
					top5Map.put(time, obj);
				}
				else
				{
					obj.setFirstName(item.getVsIPAddress());
					obj.setFirstValue(conn.getValue());
				}
			}
		}
		if(list.size()==1)
		{
			ArrayList<OBDtoDashboardTop5Data> retVal = new ArrayList<OBDtoDashboardTop5Data>(top5Map.values());
			return retVal;
		}
		// second data 
		item = list.get(1);
		if(null != item)
		{
			ArrayList<OBDtoDataObj> connList = item.getConnection();
			for(OBDtoDataObj conn:connList)
			{
				long time = conn.getOccurTime().getTime()/60000;// 60초 단위.
				OBDtoDashboardTop5Data obj = top5Map.get(time);
				if(obj==null)
				{
					obj = new OBDtoDashboardTop5Data();
					obj.setOccurTime(conn.getOccurTime());
					obj.setSecondName(item.getVsIPAddress());
					obj.setSecondValue(conn.getValue());
					top5Map.put(time, obj);
				}
				else
				{
					obj.setSecondName(item.getVsIPAddress());
					obj.setSecondValue(conn.getValue());
				}
			}
		}		
		if(list.size()==2)
		{
			ArrayList<OBDtoDashboardTop5Data> retVal = new ArrayList<OBDtoDashboardTop5Data>(top5Map.values());
			return retVal;
		}
		// third data 
		item = list.get(2);
		if(null != item)
		{
			ArrayList<OBDtoDataObj> connList = item.getConnection();
			for(OBDtoDataObj conn:connList)
			{
				long time = conn.getOccurTime().getTime()/60000;// 60초 단위.
				OBDtoDashboardTop5Data obj = top5Map.get(time);
				if(obj==null)
				{
					obj = new OBDtoDashboardTop5Data();
					obj.setOccurTime(conn.getOccurTime());
					obj.setThirdName(item.getVsIPAddress());
					obj.setThirdValue(conn.getValue());
					top5Map.put(time, obj);
				}
				else
				{
					obj.setThirdName(item.getVsIPAddress());
					obj.setThirdValue(conn.getValue());
				}
			}
		}		
		if(list.size()==3)
		{
			ArrayList<OBDtoDashboardTop5Data> retVal = new ArrayList<OBDtoDashboardTop5Data>(top5Map.values());
			return retVal;
		}
		// fourth data 
		item = list.get(3);
		if(null != item)
		{
			ArrayList<OBDtoDataObj> connList = item.getConnection();
			for(OBDtoDataObj conn:connList)
			{
				long time = conn.getOccurTime().getTime()/60000;// 60초 단위.
				OBDtoDashboardTop5Data obj = top5Map.get(time);
				if(obj==null)
				{
					obj = new OBDtoDashboardTop5Data();
					obj.setOccurTime(conn.getOccurTime());
					obj.setFourthName(item.getVsIPAddress());
					obj.setFourthValue(conn.getValue());
					top5Map.put(time, obj);
				}
				else
				{
					obj.setFourthName(item.getVsIPAddress());
					obj.setFourthValue(conn.getValue());
				}
			}
		}		
		if(list.size()==4)
		{
			ArrayList<OBDtoDashboardTop5Data> retVal = new ArrayList<OBDtoDashboardTop5Data>(top5Map.values());
			return retVal;
		}
		// fifth data 
		item = list.get(4);
		if(null != item)
		{
			ArrayList<OBDtoDataObj> connList = item.getConnection();
			for(OBDtoDataObj conn:connList)
			{
				long time = conn.getOccurTime().getTime()/60000;// 60초 단위.
				OBDtoDashboardTop5Data obj = top5Map.get(time);
				if(obj==null)
				{
					obj = new OBDtoDashboardTop5Data();
					obj.setOccurTime(conn.getOccurTime());
					obj.setFifthName(item.getVsIPAddress());
					obj.setFifthValue(conn.getValue());
					top5Map.put(time, obj);
				}
				else
				{
					obj.setFifthName(item.getVsIPAddress());
					obj.setFifthValue(conn.getValue());
				}
			}
		}		
		
		ArrayList<OBDtoDashboardTop5Data> retVal = new ArrayList<OBDtoDashboardTop5Data>(top5Map.values());
		return retVal;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoDashboardTop5Data [occurTime=" + occurTime
				+ ", firstName=" + firstName + ", secondName=" + secondName
				+ ", thirdName=" + thirdName + ", fourthName=" + fourthName
				+ ", fifthName=" + fifthName + ", firstValue=" + firstValue
				+ ", secondValue=" + secondValue + ", thirdValue=" + thirdValue
				+ ", fourthValue=" + fourthValue + ", fifthValue=" + fifthValue
				+ "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getSecondName()
	{
		return secondName;
	}
	public void setSecondName(String secondName)
	{
		this.secondName = secondName;
	}
	public String getThirdName()
	{
		return thirdName;
	}
	public void setThirdName(String thirdName)
	{
		this.thirdName = thirdName;
	}
	public String getFourthName()
	{
		return fourthName;
	}
	public void setFourthName(String fourthName)
	{
		this.fourthName = fourthName;
	}
	public String getFifthName()
	{
		return fifthName;
	}
	public void setFifthName(String fifthName)
	{
		this.fifthName = fifthName;
	}
	public Long getFirstValue()
	{
		return firstValue;
	}
	public void setFirstValue(Long firstValue)
	{
		this.firstValue = firstValue;
	}
	public Long getSecondValue()
	{
		return secondValue;
	}
	public void setSecondValue(Long secondValue)
	{
		this.secondValue = secondValue;
	}
	public Long getThirdValue()
	{
		return thirdValue;
	}
	public void setThirdValue(Long thirdValue)
	{
		this.thirdValue = thirdValue;
	}
	public Long getFourthValue()
	{
		return fourthValue;
	}
	public void setFourthValue(Long fourthValue)
	{
		this.fourthValue = fourthValue;
	}
	public Long getFifthValue()
	{
		return fifthValue;
	}
	public void setFifthValue(Long fifthValue)
	{
		this.fifthValue = fifthValue;
	}
}
