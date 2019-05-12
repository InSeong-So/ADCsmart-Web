package kr.openbase.adcsmart.web.controller.fault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.fault.FaultDiagnosisFacade;

@Controller
@Scope(value = "prototype")
public class FaultDiagnosisAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(FaultDiagnosisAction.class);
	
	@Autowired
	private FaultDiagnosisFacade faultDiagnosisFacade;	
	
	private OBDtoCpuMemStatus adcSystemUsageData;
	private OBDtoFaultCheckStatus diagnosisStateInfo;
	private OBDtoADCObject adcObject;
	private long logkey;
	
	public String loadDiagnosisStateInfo() throws OBException
	{
		try
		{		
			diagnosisStateInfo = faultDiagnosisFacade.getdiagnosisStateInfo(logkey);
			log.debug("{}", diagnosisStateInfo);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}
	
	public String loadDiagnosisChartData() throws OBException
	{
		try
		{
			adcSystemUsageData = faultDiagnosisFacade.getadcSystemUsageData(adcObject);
			log.debug("{}", adcSystemUsageData);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String cancelFaultCheck() throws OBException
	{
		try
		{
			faultDiagnosisFacade.cancelFaultCheck(logkey, session.getSessionDto());			
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		return SUCCESS;
	}

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
	}	

	public OBDtoCpuMemStatus getAdcSystemUsageData()
	{
		return adcSystemUsageData;
	}

	public void setAdcSystemUsageData(OBDtoCpuMemStatus adcSystemUsageData)
	{
		this.adcSystemUsageData = adcSystemUsageData;
	}

	public OBDtoFaultCheckStatus getDiagnosisStateInfo()
	{
		return diagnosisStateInfo;
	}
	
	public void setDiagnosisStateInfo(OBDtoFaultCheckStatus diagnosisStateInfo)
	{
		this.diagnosisStateInfo = diagnosisStateInfo;
	}
	
	public long getLogkey()
	{
		return logkey;
	}

	public void setLogkey(long logkey)
	{
		this.logkey = logkey;
	}

	@Override
	public String toString()
	{
		return "FaultDiagnosisAction [faultDiagnosisFacade=" + faultDiagnosisFacade + ", adcSystemUsageData=" + adcSystemUsageData + ", diagnosisStateInfo=" + diagnosisStateInfo + ", adcObject=" + adcObject + ", logkey=" + logkey + "]";
	}	
}