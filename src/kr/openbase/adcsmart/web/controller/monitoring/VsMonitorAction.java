package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.FlbFilterInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.VsConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsTrafficInfoDto;
import kr.openbase.adcsmart.web.facade.monitoring.VsMonitorFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBDefineWeb;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class VsMonitorAction extends BaseAction
{	
	private transient Logger log = LoggerFactory.getLogger(VsMonitorAction.class);
	
	@Autowired
	private VsMonitorFacade vsMonitorFacade;
	
	private AdcDto adc;
	private String searchKey;
	private String vsIndex;
	private Integer port;
	private String searchTimeMode;
	private Integer hour;
	private Date startTime;
	private Date endTime;
	private Integer totalCount;
	private Integer fromRow;
	private Integer toRow;
	private List<VsTrafficInfoDto> vsTrafficInfoList;
	private VsTrafficInfoDto vsTrafficInfo;
	private VsConnectionInfoDto vsConnectionInfo;
	private VsConnectionDataDto vsConnectionData;	
	private Integer orderDir; 								// 오른차순 = 1, 내림차순 = 2
	private Integer orderType; 								// vsIp = 2
	private Integer lbType = 0;
	private Integer flbSelect = 1;
	private ArrayList<FlbFilterInfoDto> filteVsTrafficInfo;

	
	/**
	 * @return the adc
	 */
	public AdcDto getAdc() 
	{
		return adc;
	}

	public Integer getFlbSelect()
	{
		return flbSelect;
	}

	public void setFlbSelect(Integer flbSelect)
	{
		this.flbSelect = flbSelect;
	}

	/**
	 * @param adc the adc to set
	 */
	public void setAdc(AdcDto adc) 
	{
		this.adc = adc;
	}
	
	/**
	 * @return the searchKey
	 */
	public String getSearchKey() 
	{
		return searchKey;
	}

	/**
	 * @param searchKey the searchKey to set
	 */
	public void setSearchKey(String searchKey) 
	{
		this.searchKey = searchKey;
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
	 * @return the searchTimeMode
	 */
	public String getSearchTimeMode()
	{
		return searchTimeMode;
	}

	/**
	 * @param searchTimeMode the searchTimeMode to set
	 */
	public void setSearchTimeMode(String searchTimeMode) 
	{
		this.searchTimeMode = searchTimeMode;
	}

	/**
	 * @return the hour
	 */
	public Integer getHour()
	{
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(Integer hour) 
	{
		this.hour = hour;
	}
	
	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount()
	{
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) 
	{
		this.totalCount = totalCount;
	}

	/**
	 * @return the fromRow
	 */
	public Integer getFromRow()
	{
		return fromRow;
	}

	/**
	 * @param fromRow the fromRow to set
	 */
	public void setFromRow(Integer fromRow)
	{
		this.fromRow = fromRow;
	}

	/**
	 * @return the toRow
	 */
	public Integer getToRow()
	{
		return toRow;
	}

	/**
	 * @param toRow the toRow to set
	 */
	public void setToRow(Integer toRow)
	{
		this.toRow = toRow;
	}

	/**
	 * @return the vsTrafficInfoList
	 */
	public List<VsTrafficInfoDto> getVsTrafficInfoList() 
	{
		return vsTrafficInfoList;
	}

	/**
	 * @param vsTrafficInfoList the vsTrafficInfoList to set
	 */
	public void setVsTrafficInfoList(List<VsTrafficInfoDto> vsTrafficInfoList)
	{
		this.vsTrafficInfoList = vsTrafficInfoList;
	}
	
	/**
	 * @return the vsTrafficInfo
	 */
	public VsTrafficInfoDto getVsTrafficInfo() 
	{
		return vsTrafficInfo;
	}

	/**
	 * @param vsTrafficInfo the vsTrafficInfo to set
	 */
	public void setVsTrafficInfo(VsTrafficInfoDto vsTrafficInfo)
	{
		this.vsTrafficInfo = vsTrafficInfo;
	}
	
	/**
	 * @return the vsConnectionInfo
	 */
	public VsConnectionInfoDto getVsConnectionInfo()
	{
		return vsConnectionInfo;
	}

	/**
	 * @param vsConnectionInfo the vsConnectionInfo to set
	 */
	public void setVsConnectionInfo(VsConnectionInfoDto vsConnectionInfo)
	{
		this.vsConnectionInfo = vsConnectionInfo;
	}
	
	/**
	 * @return the vsConnectionData
	 */
	public VsConnectionDataDto getVsConnectionData()
	{
		return vsConnectionData;
	}

	/**
	 * @param vsConnectionData the vsConnectionData to set
	 */
	public void setVsConnectionData(VsConnectionDataDto vsConnectionData)
	{
		this.vsConnectionData = vsConnectionData;
	}
	
	public String getContentDisposition()
	{
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) 
	{
		this.contentDisposition = contentDisposition;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) 
	{
		this.inputStream = inputStream;
	}

	public long getContentLength() 
	{
		return contentLength;
	}

	public void setContentLength(long contentLength)
	{
		this.contentLength = contentLength;
	}
	
//	public String retrieveTotalCountVsTrafficInfoList() throws Exception
//	{
//		try
//		{
//			if (null != adc && null != adc.getIndex()) 
//			{
//				totalCount = vsMonitorFacade.getTotalCountTrafficInfoList(adc, searchKey);
//			}
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		
//		return SUCCESS;
//	}

	public String loadVsMonitorContent() throws Exception
	{
		return SUCCESS;
	}
	
//	public String loadVsTrafficInfoList() throws Exception 
//	{
//		try 
//		{
//			if (null != adc && null != adc.getIndex() && -1 != fromRow && -1 != toRow) 
//			{
//				vsTrafficInfoList = vsMonitorFacade.getVsTrafficInfoList(adc, searchKey, fromRow, toRow, orderType, orderDir);
//				log.debug("{}", vsTrafficInfoList);
//			}
//		} 
//		catch (Exception e) 
//		{
//			throw e;
//		}
//		
//		return SUCCESS;
//	}
	
	public String loadVsTrafficInfo() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()) &&!StringUtils.isEmpty(vsIndex))
			{
				vsTrafficInfo = vsMonitorFacade.getVsTrafficInfo(adc, vsIndex, port);
				log.debug("{}", vsTrafficInfo);
			}
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String loadMemberVsTrafficInfo() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()) &&!StringUtils.isEmpty(vsIndex))
			{
				vsTrafficInfo = vsMonitorFacade.getVsTrafficMemberInfo(adc, vsIndex);
				log.debug("{}", vsTrafficInfo);
			}
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	//TODO
	public String loadFilterVsTrafficInfo() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()) &&!StringUtils.isEmpty(vsIndex)) 
			{
				filteVsTrafficInfo = vsMonitorFacade.getVsTrafficFilterInfo(adc, vsIndex);
				log.debug("{}", filteVsTrafficInfo);
			}
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	public String checkExportVsMonitorDataExist() throws Exception
	{
		try
		{
			setSearchTimeInterval();		
			
			if(adc.getIndex() != null && vsIndex != null && port != null)
			{
				vsConnectionInfo = vsMonitorFacade.getVsConnectionInfo(adc, vsIndex, port, startTime, endTime);	
			}
										
			log.debug("{}", vsConnectionInfo);		
			if(vsConnectionInfo != null)
			{
				isSuccessful = true;			
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;	
	}
	
	public String downloadVsMonitor() throws Exception 
	{
		try 
		{			
			log.debug("adc:{}", new Object[]{adc});
			log.debug("startTime:{}, endTime:{}", startTime, endTime);
			log.debug("vsIndex:{}, port:{}", vsIndex, port);
			
			setSearchTimeInterval();		
			vsConnectionInfo = vsMonitorFacade.getVsConnectionInfo(adc, vsIndex, port, startTime, endTime);					
			log.debug("{}", vsConnectionInfo);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithVsMonitorList(vsConnectionInfo);
			File csv = csvMaker.write();
			if (csv != null)
			{
				setStrutsStream(csv);
			}
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String loadVsConnectionInfo() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()) &&!StringUtils.isEmpty(vsIndex))
			{
				setSearchTimeInterval();
				vsConnectionInfo = vsMonitorFacade.getVsConnectionInfo(adc, vsIndex, port, startTime, endTime);
				log.debug("{}", vsConnectionInfo);
			}
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
/*
 * 기존에 실시간은 팝업으로 표현하던 방식을 변경함으로 아래 내용은 우선 주석처리함.	
	public String loadRealTimeSessionInfo() throws Exception 
	{
		return SUCCESS;
	}
	
	public String loadRealTimeSessionInfoContent() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType())) 
			{
				vsTrafficInfoList = vsMonitorFacade.getVsTrafficInfoList(adc, null, null, null);
				log.debug("{}", vsTrafficInfoList);
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		return SUCCESS;
	}
	*/
	public String loadRealTimeVsConnectionData() throws Exception 
	{
		try 
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()) && !StringUtils.isEmpty(vsIndex)) 
			{
				vsConnectionData = vsMonitorFacade.getRealTimeVsConnectionData(adc, vsIndex, port);
			}
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	private void setSearchTimeInterval() 
	{
		if (!StringUtils.isEmpty(searchTimeMode) && searchTimeMode.equals(OBDefineWeb.SEARCH_TIME_PERIOD_MODE) && null != startTime && null != endTime) 
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.HOUR_OF_DAY, 24);
			calendar.add(Calendar.MILLISECOND, -1);
			endTime = calendar.getTime();
		} 
		else 
		{
			endTime = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			if (null != hour) 
			{
				calendar.add(Calendar.HOUR_OF_DAY, -hour);
			} 
			else 
			{
				calendar.add(Calendar.HOUR_OF_DAY, -1);
			}
			startTime = calendar.getTime();			
		}
		
		log.debug("startTime: " + startTime.toString() + ", endTime: " + endTime.toString());
	}

	public Integer getOrderDir()
	{
		return orderDir;
	}

	public void setOrderDir(Integer orderDir)
	{
		this.orderDir = orderDir;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	public Integer getLbType()
	{
		return lbType;
	}

	public void setLbType(Integer lbType)
	{
		this.lbType = lbType;
	}

	public ArrayList<FlbFilterInfoDto> getFilteVsTrafficInfo()
	{
		return filteVsTrafficInfo;
	}

	public void setFilteVsTrafficInfo(ArrayList<FlbFilterInfoDto> filteVsTrafficInfo)
	{
		this.filteVsTrafficInfo = filteVsTrafficInfo;
	}

	@Override
	public String toString()
	{
		return "VsMonitorAction [vsMonitorFacade=" + vsMonitorFacade + ", adc=" + adc + ", searchKey=" + searchKey + ", vsIndex=" + vsIndex + ", port=" + port + ", searchTimeMode=" + searchTimeMode + ", hour=" + hour + ", startTime=" + startTime + ", endTime=" + endTime + ", totalCount=" + totalCount + ", fromRow=" + fromRow + ", toRow=" + toRow + ", vsTrafficInfoList=" + vsTrafficInfoList + ", vsTrafficInfo=" + vsTrafficInfo + ", vsConnectionInfo=" + vsConnectionInfo + ", vsConnectionData=" + vsConnectionData + ", orderDir=" + orderDir + ", orderType=" + orderType + ", lbType=" + lbType + ", flbSelect=" + flbSelect + ", filteVsTrafficInfo=" + filteVsTrafficInfo + "]";
	}
}