package kr.openbase.adcsmart.web.report.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortErrDiscard;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class PortStatusDto 
{
	private String portName;
	private Date fromPeriod;
	private Date toPeriod;
	private List<ChartXYDto> errorChart;
	private List<ChartXYDto> discardChart;
	
	private PortStatusDtoTextHdr textHdr;
	
	public static PortStatusDto toPortStatusDto(OBDtoRptPortInfo statusFromSvc) 
	{
		if (statusFromSvc == null)
			return null;
		
		PortStatusDto status = new PortStatusDto();
		status.setPortName(statusFromSvc.getPortName());
		status.setFromPeriod(statusFromSvc.getBeginTime());
		status.setToPeriod(statusFromSvc.getEndTime());
		PortStatusDtoTextHdr textHdr = new PortStatusDtoTextHdr();
		textHdr.setTitle(OBMessages.getMessage(OBMessages.MSG_RPT_PORT_STATUS_TITLE));//"상태 정보");
		status.setTextHdr(textHdr);
		List<ChartXYDto> errorChart = new ArrayList<ChartXYDto>();
		List<ChartXYDto> discardChart = new ArrayList<ChartXYDto>();
		for (OBDtoRptPortErrDiscard e : statusFromSvc.getErrDiscardsList()) 
		{
			ChartXYDto errorXY = new ChartXYDto();
			errorXY.setX(e.getOccurTime());
			errorXY.setY(e.getErrors());
			errorChart.add(errorXY);
			ChartXYDto discardXY = new ChartXYDto();
			discardXY.setX(e.getOccurTime());
			discardXY.setY(e.getDiscards());
			discardChart.add(discardXY);
		}
		
		status.setErrorChart(errorChart);
		status.setDiscardChart(discardChart);
		return status;
	}
	
	public static List<PortStatusDto> toPortStatusDto(List<OBDtoRptPortInfo> statusesFromSvc) 
	{
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("111111111111"));
		if (statusesFromSvc == null)
			return null;
		
		List<PortStatusDto> statuses = new ArrayList<PortStatusDto>();
		for (OBDtoRptPortInfo status : statusesFromSvc)
			statuses.add(toPortStatusDto(status));

		for(PortStatusDto info:statuses)
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("info:%s", info));
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("22222222222222"));
		return statuses;
	}
	
	public PortStatusDtoTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(PortStatusDtoTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	public String getPortName() 
	{
		return portName;
	}
	public void setPortName(String portName) 
	{
		this.portName = portName;
	}
	public Date getFromPeriod() 
	{
		return fromPeriod;
	}
	public void setFromPeriod(Date fromPeriod) 
	{
		this.fromPeriod = fromPeriod;
	}
	public Date getToPeriod() 
	{
		return toPeriod;
	}
	public void setToPeriod(Date toPeriod) 
	{
		this.toPeriod = toPeriod;
	}
	public List<ChartXYDto> getErrorChart() 
	{
		return errorChart;
	}
	public void setErrorChart(List<ChartXYDto> errorChart) 
	{
		this.errorChart = errorChart;
	}
	public List<ChartXYDto> getDiscardChart() 
	{
		return discardChart;
	}
	public void setDiscardChart(List<ChartXYDto> discardChart) 
	{
		this.discardChart = discardChart;
	}

	@Override
	public String toString() 
	{
		return "PortStatusDto [portName=" + portName + ", fromPeriod=" + fromPeriod + ", toPeriod=" + toPeriod
				+ ", errorChart=" + errorChart + ", discardChart=" + discardChart + "]";
	}
}