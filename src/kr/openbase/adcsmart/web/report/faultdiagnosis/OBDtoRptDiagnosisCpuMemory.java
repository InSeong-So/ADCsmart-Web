package kr.openbase.adcsmart.web.report.faultdiagnosis;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.web.report.admin.ChartMultiXYDto;
import kr.openbase.adcsmart.web.report.admin.ChartXYDto;
import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class OBDtoRptDiagnosisCpuMemory
{
	private ArrayList<ChartXYDto> memHistory;
	private ArrayList<ChartMultiXYDto> cpuHistory;

	private OBDtoReportTextHdr textHdr;
	
	public OBDtoReportTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(OBDtoReportTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	public static OBDtoRptDiagnosisCpuMemory toArrayList(ArrayList<OBDtoCpuMemStatus> list, Integer adcType)
	{
		if (list == null || list.size()==0)
			return null;
		
		OBDtoRptDiagnosisCpuMemory retVal = new OBDtoRptDiagnosisCpuMemory();
		// #3984-6 #6: 14.07.30 sw.jung 진단보고서 차트 CPU범주 오류 수정
		OBDtoReportTextHdr textHdr = OBDtoReportTextHdr.toTextHdr("CPU1", "CPU2", "CPU3", "CPU4", "CPU5", "CPU6", "CP7", "CP8", "CP9", "CP10", "CP11", "CP12", "CP13", "CP14", "CP15", "CP16");
		switch(adcType.intValue())
		{
		case OBDefine.ADC_TYPE_ALTEON:
			retVal.setTextHdr(OBDtoReportTextHdr.toTextHdr(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_MPSP_USAGE)));//"MP/SP 사용량 추이"));// 순서 변경 하지 말것. 순서 변경시 jasper report에서도 변경해야 함.
			// #3984-6 #6: 14.07.30 sw.jung 진단보고서 차트 CPU범주 오류 수정
			textHdr = OBDtoReportTextHdr.toTextHdr("MP", "SP1", "SP2", "SP3", "SP4", "SP5", "SP6", "SP7", "SP8", "SP9", "SP10", "SP11", "SP12", "SP13", "SP14", "SP15", "SP16");
			break;
		case OBDefine.ADC_TYPE_F5:
			retVal.setTextHdr(OBDtoReportTextHdr.toTextHdr(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_CPU_USAGE)));//"CPU 사용량 추이"));// 순서 변경 하지 말것. 순서 변경시 jasper report에서도 변경해야 함.
		default:
			retVal.setTextHdr(OBDtoReportTextHdr.toTextHdr(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_CPU_USAGE)));//"CPU 사용량 추이"));// 순서 변경 하지 말것. 순서 변경시 jasper report에서도 변경해야 함.
		}
		
		ArrayList<ChartXYDto> memHistory = new ArrayList<ChartXYDto>();
		
		ArrayList<ChartMultiXYDto> cpuHistory = new ArrayList<ChartMultiXYDto>();
		
		
		for (OBDtoCpuMemStatus info:list) 
		{
			ChartXYDto memory = new ChartXYDto();
			memory.setTextHdr(OBDtoReportTextHdr.toTextHdr("Memory"));// 순서 변경 하지 말것. 순서 변경시 jasper report에서도 변경해야 함.
			
			memory.setX(info.getOccurTime());
			memory.setY((long)info.getMemUsage());
			memHistory.add(memory);

			ArrayList<Long> cpuValue = new ArrayList<Long>();
			
			// #3984-6 #6: 14.07.30 sw.jung 진단보고서 차트 CPU범주 오류 수정
			cpuValue.add((long)info.getCpu1Usage());
			cpuValue.add((long)info.getCpu2Usage());
			cpuValue.add((long)info.getCpu3Usage());
			cpuValue.add((long)info.getCpu4Usage());
			cpuValue.add((long)info.getCpu5Usage());
			cpuValue.add((long)info.getCpu6Usage());
			cpuValue.add((long)info.getCpu7Usage());
			cpuValue.add((long)info.getCpu8Usage());
			cpuValue.add((long)info.getCpu9Usage());
			cpuValue.add((long)info.getCpu10Usage());
			cpuValue.add((long)info.getCpu11Usage());
			cpuValue.add((long)info.getCpu12Usage());
			cpuValue.add((long)info.getCpu13Usage());
			cpuValue.add((long)info.getCpu14Usage());
			cpuValue.add((long)info.getCpu15Usage());
			cpuValue.add((long)info.getCpu16Usage());

			ChartMultiXYDto cpu = new ChartMultiXYDto();
			cpu.setTextHdr(textHdr);
			cpu.setX(info.getOccurTime());
			cpu.setY(cpuValue);
			
			cpuHistory.add(cpu);
		}
		
		retVal.setMemHistory(memHistory);
		retVal.setCpuHistory(cpuHistory);
		return retVal;
	}
	
	public ArrayList<ChartXYDto> getMemHistory()
	{
		return memHistory;
	}
	public void setMemHistory(ArrayList<ChartXYDto> memHistory)
	{
		this.memHistory = memHistory;
	}

	public ArrayList<ChartMultiXYDto> getCpuHistory()
	{
		return cpuHistory;
	}

	public void setCpuHistory(ArrayList<ChartMultiXYDto> cpuHistory)
	{
		this.cpuHistory = cpuHistory;
	}
	
}
