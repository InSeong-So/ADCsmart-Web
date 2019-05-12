package kr.openbase.adcsmart.service.impl.fault.dto;

import java.util.ArrayList;
import java.util.Date;

public class OBDtoPktdumpInfo
{
	public static final  int	STATUS_SUCCESS	=100;// 진단 결과 이상 무.
	public static final  int	STATUS_FAILURE	=101;// 진단 실패한 경우.
	public static final  int	STATUS_STOP	    =102;// 진단 중지한 경우
	public static final  int	STATUS_CANCEL   =103;// 진단 취소한 경우.
	public static final  int	STATUS_DELETE   =104;// 삭제된 로그

	private static final  String PKT_OPTION_SRC_IP = "src_ip";
	private static final  String PKT_OPTION_DST_IP = "dst_ip";
	private static final  String PKT_OPTION_SRC_PORT = "src_port";
	private static final  String PKT_OPTION_DST_PORT = "dst_port";
	private static final  String PKT_OPTION_PROTOCOL = "proto";
	private static final  String PKT_OPTION_HOST = "host";
	private static final  String PKT_OPTION_PORT = "port";

	private Long 		logIndex;
	private String 		adcName;
	private Integer		adcIndex;
	private Date		occurTime;
	private	Date		endTime;
	private Integer     status;
	private Integer		elapsedTime;
	private String      interfaceName;
	private	Long		fileSize;// byte 단위.
	private String  	fileName;
	private String  	strFilter;
	private Integer     optionMaxPkt   = 10000;
	private Integer     optionMaxTime  = 600000;// msec. 600sec
	private Long        optionMaxSize  = 10000000L;// bytes. 10M
	private ArrayList<OBDtoPktdumpOption> filterList;

	@Override
	public String toString()
	{
		return String.format("OBDtoPktdumpInfo [logIndex=%s, adcName=%s, adcIndex=%s, occurTime=%s, endTime=%s, status=%s, elapsedTime=%s, interfaceName=%s, fileSize=%s, fileName=%s, strFilter=%s, optionMaxPkt=%s, optionMaxTime=%s, optionMaxSize=%s, filterList=%s]", logIndex, adcName, adcIndex, occurTime, endTime, status, elapsedTime, interfaceName, fileSize, fileName, strFilter, optionMaxPkt, optionMaxTime, optionMaxSize, filterList);
	}
	
	public String toFilterString(ArrayList<OBDtoPktdumpOption> dumpOption)
	{
		String retVal = "";
		if(dumpOption==null)
			return retVal;
		for(OBDtoPktdumpOption option:dumpOption)
		{
			switch(option.getType())
			{
			case OBDtoPktdumpOption.OPTION_TYPE_SRC_IP:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_SRC_IP, option.getContent());
				break;
			case OBDtoPktdumpOption.OPTION_TYPE_SRC_PORT:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_SRC_PORT, option.getContent());
				break;
			case OBDtoPktdumpOption.OPTION_TYPE_DST_IP:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_DST_IP, option.getContent());
				break;
			case OBDtoPktdumpOption.OPTION_TYPE_DST_PORT:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_DST_PORT, option.getContent());
				break;
			case OBDtoPktdumpOption.OPTION_TYPE_PROTOCOL:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_PROTOCOL, option.getContent());
				break;
			case OBDtoPktdumpOption.OPTION_TYPE_HOST:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_HOST, option.getContent());
				break;
			case OBDtoPktdumpOption.OPTION_TYPE_PORT:
				if(!retVal.isEmpty())
					retVal += ", ";
				retVal += String.format("%s=%s", PKT_OPTION_PORT, option.getContent());
				break;
			}
		}
		this.strFilter = retVal;
		return retVal;
	}
	
	public ArrayList<OBDtoPktdumpOption> toFilterArray(String optionList)
	{
		ArrayList<OBDtoPktdumpOption> retVal = new ArrayList<OBDtoPktdumpOption>();
		this.filterList = retVal;
		if(optionList==null)
			return retVal;
		
		String options[] = optionList.split(", ");
		for(int i=0;i<options.length;i++)
		{
			String option = options[i];
			String keyValue[] = option.split("=");
			if(keyValue.length!=2)
				continue;
			if(keyValue[0].compareTo(PKT_OPTION_SRC_IP)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_SRC_IP);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
			else if(keyValue[0].compareTo(PKT_OPTION_DST_IP)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_DST_IP);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
			else if(keyValue[0].compareTo(PKT_OPTION_SRC_PORT)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_SRC_PORT);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
			else if(keyValue[0].compareTo(PKT_OPTION_DST_PORT)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_DST_PORT);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
			if(keyValue[0].compareTo(PKT_OPTION_PROTOCOL)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_PROTOCOL);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
			if(keyValue[0].compareTo(PKT_OPTION_HOST)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_HOST);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
			if(keyValue[0].compareTo(PKT_OPTION_PORT)==0)
			{
				OBDtoPktdumpOption obj = new OBDtoPktdumpOption();
				obj.setType(OBDtoPktdumpOption.OPTION_TYPE_PORT);
				obj.setContent(keyValue[1]);
				retVal.add(obj);
			}
		}
		return retVal;
	}
	
	public String getInterfaceName()
	{
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}
	public String getStrFilter()
	{
		return strFilter;
	}
	public void setStrFilter(String strFilter)
	{
		this.strFilter = strFilter;
	}
	public ArrayList<OBDtoPktdumpOption> getFilterList()
	{
		return filterList;
	}
	public void setFilterList(ArrayList<OBDtoPktdumpOption> filterList)
	{
		this.filterList = filterList;
	}	
	public Integer getOptionMaxPkt()
	{
		return optionMaxPkt;
	}
	public void setOptionMaxPkt(Integer optionMaxPkt)
	{
		this.optionMaxPkt = optionMaxPkt;
	}
	public Integer getOptionMaxTime()
	{
		return optionMaxTime;
	}
	public void setOptionMaxTime(Integer optionMaxTime)
	{
		this.optionMaxTime = optionMaxTime;
	}
	public Long getOptionMaxSize()
	{
		return optionMaxSize;
	}
	public void setOptionMaxSize(Long optionMaxSize)
	{
		this.optionMaxSize = optionMaxSize;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public Long getFileSize()
	{
		return fileSize;
	}
	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}
	public Long getLogIndex()
	{
		return logIndex;
	}
	public void setLogIndex(Long logIndex)
	{
		this.logIndex = logIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	public Integer getElapsedTime()
	{
		return elapsedTime;
	}
	public void setElapsedTime(Integer elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}
}
