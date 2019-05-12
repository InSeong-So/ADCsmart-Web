/**
 * 
 */
package kr.openbase.adcsmart.web.facade.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBSystemView;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;
import kr.openbase.adcsmart.service.dto.OBDtoUsageMem;
import kr.openbase.adcsmart.service.impl.OBSystemViewImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.SystemCpuDataDto;
import kr.openbase.adcsmart.web.facade.dto.SystemCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemDatabaseInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemHddInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemMemoryDataDto;
import kr.openbase.adcsmart.web.facade.dto.SystemMemoryInfoDto;
import kr.openbase.adcsmart.web.util.NumberUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author paul
 *
 */
@Component
public class SystemInfoFacade 
{
	
	private static transient Logger log = LoggerFactory.getLogger(SystemInfoFacade.class);
	
	private OBSystemView systemViewSvc;

	public SystemInfoFacade() 
	{
		systemViewSvc = new OBSystemViewImpl();
	}
	
	public SystemCpuInfoDto getSystemCpuInfo(Date startTime, Date endTime) throws OBException, Exception 
	{
		SystemCpuInfoDto systemCpuInfo = null;
		
		log.debug("startTime:{}, endTime:{}", new Object[] {
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		
		OBDtoCpu cpu = systemViewSvc.getUsageCpuList(startTime, endTime);
		log.debug("{}", cpu);
		if (null != cpu)
		{
			systemCpuInfo = getSystemCpuInfo(cpu);
		}
		log.debug("{}", systemCpuInfo);
		
		return systemCpuInfo;
	}
	
	public SystemMemoryInfoDto getSystemMemoryInfo(Date startTime, Date endTime) throws OBException, Exception 
	{
		SystemMemoryInfoDto systemMemoryInfoDto = null;
		
		log.debug("startTime:{}, endTime:{}", new Object[] {
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
				, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		
		OBDtoMemory memory = systemViewSvc.getUsageMemList(startTime, endTime);
		log.debug("{}", memory);
		if (null != memory)
		{
			systemMemoryInfoDto = getSystemMemoryInfo(memory);
		}
		log.debug("{}", systemMemoryInfoDto);
		
		return systemMemoryInfoDto;
	}
	
	public SystemHddInfoDto getSystemHddInfo() throws OBException, Exception 
	{
		SystemHddInfoDto systemHddInfo = null;
		
		OBDtoHddInfo hdd = systemViewSvc.getUsageHdd();
		log.debug("{}", hdd);
		if (null != hdd) 
		{
			systemHddInfo = getSystemHddInfo(hdd);
		}
		log.debug("{}", systemHddInfo);
		
		return systemHddInfo;
	}
	
	public SystemDatabaseInfoDto getSystemDatabaseInfo() throws OBException, Exception 
	{
		SystemDatabaseInfoDto systemDatabaseInfo = null;
		
		OBDtoDatabase database = systemViewSvc.getUsageDatabase();
		log.debug("{}", database);
		if (null != database) 
		{
			systemDatabaseInfo = getSystemDatabaseInfo(database);
		}
		log.debug("{}", systemDatabaseInfo);
		
		return systemDatabaseInfo;
	}
	private SystemCpuDataDto getSystemCpuData(OBDtoUsageCpu cpuUsage) throws OBException, Exception
	{
		SystemCpuDataDto systemCpuData = new SystemCpuDataDto();
		systemCpuData.setOccurredTime(cpuUsage.getOccurTime());
		systemCpuData.setUsage(cpuUsage.getUsage());
		return systemCpuData;
	}
	
	/*   시스템 정보 cpu 그래프 값 출력위해 변환. Integer - > String  */	
	private SystemCpuInfoDto getSystemCpuInfo(OBDtoCpu cpu) throws OBException, Exception
	{	
		SystemCpuInfoDto systemCpuInfo = new SystemCpuInfoDto();
		
		if(cpu.getMaxValue() != null)
        {
		    systemCpuInfo.setIntMaxUsage((int)(long)cpu.getMaxValue()); 
        }
        else
        {
            systemCpuInfo.setIntMaxUsage(0);
        }
        if(cpu.getAvgValue() != null)
        {
            systemCpuInfo.setIntAvgUsage((int)(long)cpu.getAvgValue());
        }
        else
        {
            systemCpuInfo.setIntAvgUsage(0);
        }
        if(cpu.getMinValue() != null)
        {
            systemCpuInfo.setIntMinUsage((int)(long)cpu.getMinValue());    
        }
        else
        {
            systemCpuInfo.setIntMinUsage(0);
        }
        systemCpuInfo.setMaxUsage(NumberUtil.toStringWithPercentageChart(cpu.getMaxValue(), ""));
		systemCpuInfo.setAvgUsage(NumberUtil.toStringWithPercentageChart(cpu.getAvgValue(), ""));
		systemCpuInfo.setMinUsage(NumberUtil.toStringWithPercentageChart(cpu.getMinValue(), ""));
		systemCpuInfo.setSystemCpuDataList(new ArrayList<SystemCpuDataDto>());
		
		if (!CollectionUtils.isEmpty(cpu.getCpuList())) 
		{
			for (OBDtoUsageCpu cpuUsage : cpu.getCpuList()) 
			{
				systemCpuInfo.getSystemCpuDataList().add(getSystemCpuData(cpuUsage));
			}
			String curValue = NumberUtil.toStringInteger(
					systemCpuInfo.getSystemCpuDataList().get(
							systemCpuInfo.getSystemCpuDataList().size() - 1).getUsage());
			Integer intCurValue = 0;
            if(systemCpuInfo.getSystemCpuDataList() != null)
            {			
			  intCurValue = systemCpuInfo.getSystemCpuDataList().get(systemCpuInfo.getSystemCpuDataList().size() - 1).getUsage();
            }
			systemCpuInfo.setCurUsage(curValue);
			systemCpuInfo.setIntCurUsage(intCurValue);
		}
			
		return systemCpuInfo;		
	}
	
	private SystemMemoryInfoDto getSystemMemoryInfo(OBDtoMemory memory) throws OBException, Exception 
	{
		SystemMemoryInfoDto systemMemoryInfo = new SystemMemoryInfoDto();
		
		if(memory.getMaxUsage() != null)
		{
		    systemMemoryInfo.setMaxUsage((int)(long)memory.getMaxUsage()); 
		}
		else
		{
		    systemMemoryInfo.setMaxUsage(0);
		}
		if(memory.getMinUsage() != null)
		{
		    systemMemoryInfo.setMinUsage((int)(long)memory.getMinUsage());
		}
		else
		{
		    systemMemoryInfo.setMinUsage(0);
		}
		if(memory.getAvgUsage() != null)
		{
		    systemMemoryInfo.setAvgUsage((int)(long)memory.getAvgUsage());    
		}
		else
		{
		    systemMemoryInfo.setAvgUsage(0);
		}			
		systemMemoryInfo.setMaxValue(NumberUtil.toStringWithDataUnit(memory.getMaxValue(), ""));
		systemMemoryInfo.setMinValue(NumberUtil.toStringWithDataUnit(memory.getMinValue(), ""));
		systemMemoryInfo.setAvgValue(NumberUtil.toStringWithDataUnit(memory.getAvgValue(), ""));
		systemMemoryInfo.setSystemMemoryDataList(new ArrayList<SystemMemoryDataDto>());
		
		if (!CollectionUtils.isEmpty(memory.getMemList())) 
		{
			for (OBDtoUsageMem memoryUsage : memory.getMemList()) 
			{
				systemMemoryInfo.getSystemMemoryDataList().add(getSystemMemoryData(memoryUsage));
			}
			String curValue = NumberUtil.toStringWithDataUnit(
					systemMemoryInfo.getSystemMemoryDataList().get(
							systemMemoryInfo.getSystemMemoryDataList().size() - 1).getUsed(), "");
			systemMemoryInfo.setCurValue(curValue);
			Integer curUsage = 0;
			if(systemMemoryInfo.getSystemMemoryDataList() != null)
			{
			    curUsage = systemMemoryInfo.getSystemMemoryDataList().get(systemMemoryInfo.getSystemMemoryDataList().size() - 1).getUsage();
	        }			
			systemMemoryInfo.setCurUsage(curUsage);
		}
		return systemMemoryInfo;
	}
	
	private SystemMemoryDataDto getSystemMemoryData(OBDtoUsageMem memoryUsage) throws OBException, Exception {
		SystemMemoryDataDto systemMemoryData = new SystemMemoryDataDto();
		systemMemoryData.setOccurredTime(memoryUsage.getOccurTime());
		systemMemoryData.setTotal(memoryUsage.getTotal());
		systemMemoryData.setUsed(memoryUsage.getUsed());
		systemMemoryData.setUsage(memoryUsage.getUsage());
		return systemMemoryData;
	}
	
	private SystemHddInfoDto getSystemHddInfo(OBDtoHddInfo hdd) throws OBException, Exception 
	{
		SystemHddInfoDto systemHddInfo = new SystemHddInfoDto();
		/* Byte로 차트에 보낸다 
		systemHddInfo.setTotal(hdd.getHddTotal());
		systemHddInfo.setUsed(hdd.getHddUsed());
		systemHddInfo.setFree(hdd.getHddFree());*/			

		/* Disk Usage 소수 2째자리까지*/
		systemHddInfo.setTotalUsage(NumberUtil.toStringPercentageValue(hdd.getHddTotal(), hdd.getHddTotal()));
		systemHddInfo.setUsedUsage(NumberUtil.toStringPercentageValue(hdd.getHddUsed(), hdd.getHddTotal()));
		systemHddInfo.setFreeUsage(NumberUtil.toStringPercentageValue(hdd.getHddFree(), hdd.getHddTotal()));
				
		/* Disk size 단위환산 데이터 K, M, G, T */
		systemHddInfo.setTotalSize(NumberUtil.toStringWithDataUnit(hdd.getHddTotal(), ""));
		systemHddInfo.setUsedSize(NumberUtil.toStringWithDataUnit(hdd.getHddUsed(), ""));
		systemHddInfo.setFreeSize(NumberUtil.toStringWithDataUnit(hdd.getHddFree(), ""));
		
		if (hdd.getHddUsed30daysBefore() != null && hdd.getHddUsed30daysBefore() != -1)
        {
            systemHddInfo.setUsedUsage30Before(NumberUtil.toStringPercentageValue(hdd.getHddUsed30daysBefore(), hdd.getHddTotal30daysBefore()));            
            systemHddInfo.setUsedSize30Before(NumberUtil.toStringWithDataUnit(hdd.getHddUsed() - hdd.getHddUsed30daysBefore(), ""));
        }
		
		return systemHddInfo;
	}
	
	private SystemDatabaseInfoDto getSystemDatabaseInfo(OBDtoDatabase database) throws OBException, Exception 
	{
		
		SystemDatabaseInfoDto systemDatabaseInfo = new SystemDatabaseInfoDto();
		Long etcSize = database.getTotalUsedSized()-(database.getGeneralUsed()+database.getIndexUsed()+database.getLogUsed());

		/* Disk size 데이터 BYTE
		systemDatabaseInfo.setTotalDiskSize(database.getTotalDiskSize());
		systemDatabaseInfo.setGeneralUsed(database.getGeneralUsed());
		systemDatabaseInfo.setIndexUsed(database.getIndexUsed());
		systemDatabaseInfo.setLogUsed(database.getLogUsed());*/
		
		/* Disk size 단위환산 데이터 GBYTE 로만 
		systemDatabaseInfo.setTotalDiskSize(NumberUtil.toStringWithUnitchart(database.getTotalUsedSized()));
		systemDatabaseInfo.setGeneralUsed(NumberUtil.toStringWithUnitchart(database.getGeneralUsed()));
		systemDatabaseInfo.setIndexUsed(NumberUtil.toStringWithUnitchart(database.getIndexUsed()));
		systemDatabaseInfo.setLogUsed(NumberUtil.toStringWithUnitchart(database.getLogUsed()));
		Long etcSize = database.getTotalUsedSized()-(database.getLogUsed()+database.getGeneralUsed()+database.getIndexUsed());
		systemDatabaseInfo.setEtcUsed(NumberUtil.toStringWithUnitchart(etcSize));*/			
		
		/* Disk size 단위환산 데이터 K, M, G, T */
		systemDatabaseInfo.setTotalSize(NumberUtil.toStringWithDataUnit(database .getTotalDiskSize(), ""));
		systemDatabaseInfo.setTotalUsedSize(NumberUtil.toStringWithDataUnit(database.getTotalUsedSized(), ""));
		systemDatabaseInfo.setGeneralSize(NumberUtil.toStringWithDataUnit(database.getGeneralUsed(), ""));
		systemDatabaseInfo.setIndexSize(NumberUtil.toStringWithDataUnit(database.getIndexUsed(), ""));
		systemDatabaseInfo.setLogSize(NumberUtil.toStringWithDataUnit(database.getLogUsed(), ""));
		systemDatabaseInfo.setEtcSize(NumberUtil.toStringWithDataUnit(etcSize, ""));
		
		/* Disk Usage 소수 2째자리까지*/
        systemDatabaseInfo.setTotalUsage(NumberUtil.toStringPercentageValue(database.getTotalDiskSize(), database.getTotalDiskSize()));
        systemDatabaseInfo.setTotalUsedUsage(NumberUtil.toStringPercentageValue(database.getTotalUsedSized(), database.getTotalUsedSized()));
        systemDatabaseInfo.setGeneralUsage(NumberUtil.toStringPercentageValue(database.getGeneralUsed(), database.getTotalUsedSized()));
        systemDatabaseInfo.setIndexUsage(NumberUtil.toStringPercentageValue(database.getIndexUsed(), database.getTotalUsedSized()));
        systemDatabaseInfo.setLogUsage(NumberUtil.toStringPercentageValue(database.getLogUsed(), database.getTotalUsedSized()));
        systemDatabaseInfo.setEtcUsage(NumberUtil.toStringPercentageValue(etcSize, database.getTotalUsedSized()));
       		
		return systemDatabaseInfo;
	}
	
}
