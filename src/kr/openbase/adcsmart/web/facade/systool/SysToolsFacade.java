package kr.openbase.adcsmart.web.facade.systool;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBEnvManagement;
import kr.openbase.adcsmart.service.OBSystemTools;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRespGroup;
import kr.openbase.adcsmart.service.dto.OBDtoRespInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRespMultiChartData;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemToolsImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;

/*
 * 코딩 가이드:
 * 1. public 함수의 Exception은 무조건 OBException, Exception 두종류로  throw 한다.
 * 2. 내부에서 exception의 try-catch는 가급적이면 하지 않는다. 필요할 경우에만 추가한다.
 * 3. AdcFacade.java 파일을 참조한다.
 *
 */

@Component
public class SysToolsFacade
{
    private transient Logger log = LoggerFactory.getLogger(SysToolsFacade.class);
    
	private OBSystemTools sysTools;
    private OBAdcManagement adcMgmt;
    private OBEnvManagement envMgmt;
	
	public SysToolsFacade() 
	{
		sysTools = new OBSystemToolsImpl();
        adcMgmt = new OBAdcManagementImpl();
        envMgmt = new OBEnvManagementImpl();
	}
	
	
	/*
     * sysView Dump Download
     */
    
    // 화면 load
    
    
    // dump file create
	public String runDumpFile() throws OBException, Exception
    {
        try 
        {
            return envMgmt.runDumpConfig();
        } 
        catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }
    
    // dump file download
	public String runDumpDownload() throws OBException, Exception
    {
        try 
        {
            return envMgmt.runDumpConfig();
        } 
        catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }
	
	public String portUsageGetContent() throws OBException, Exception 
	{
		return sysTools.portUsageGetContent();
	}
	
	public String [] portUsageMakeCsvHeader() throws OBException, Exception 
	{// csv 컬럼 헤더 정보를 추가한다.
		return sysTools.portUsageMakeCsvHeader();// String[]{"생성시간", "사용자", "접속 IP", "로그 종류", "중요도", "상세 내용"};
	}
	public String [] unUsedSlbMakeCsvHeader(Integer accntIndex, Integer adcIndex) throws OBException, Exception 
	{// csv 컬럼 헤더 정보를 추가한다.
		return sysTools.unUsedSlbInfoCsvHeader(accntIndex, adcIndex);// String[]{"생성시간", "사용자", "접속 IP", "로그 종류", "중요도", "상세 내용"};
	}
	
	public ArrayList<String[]> portUsageMakeCsvBody() throws OBException, Exception 
	{// csv 데이터 정보를 추가한다.
		return sysTools.portUsageMakeCsvBody();
	}
	public ArrayList<String[]> unUsedSlbMakeCsvBody(Integer accntIndex, Integer adcIndex) throws OBException, Exception 
	{// csv 데이터 정보를 추가한다.
		return sysTools.unUsedSlbInfoCsvBody(accntIndex, adcIndex);
	}
	public String slbSessionGetContent(String adcIp , String adcType , String accountId, String password, int connService, int connPort, int ipType, String ip) throws OBException, Exception
	{		
		return sysTools.slbSessionGetContent(adcIp, adcType, accountId, password, connService, connPort, ipType, ip);  
	}
	public String unUsedSlbGetContent(Integer accntIndex, Integer adcIndex, Integer searchType) throws OBException, Exception
	{		
		return sysTools.unUsedSlbInfoContent(accntIndex, adcIndex, searchType); 
	}
	
	public ArrayList<AdcDto> getAdcsByAccountIndex(Integer accountIndex) throws OBException, Exception 
	{
		ArrayList<AdcDto> adcs = new ArrayList<AdcDto>();
		for (OBDtoAdcInfo adcFromSvc : new OBAdcManagementImpl().getAdcInfoList(accountIndex)) 
		{
			AdcDto adc = AdcDto.toAdcDto(adcFromSvc);
			adcs.add(adc);
		}

		return adcs;
	}
	/*public String slbSessionMakeCsvBody(String adcIp , String adcType , String accountId, String password, String ipType, String ip) throws OBException, Exception 
	{// csv 데이터 정보를 추가한다.
		String infoSess;
		infoSess = sysTools.slbSessionMakeCsvBody(adcIp, adcType, accountId, password, ipType, ip); 
		return infoSess;
	}
*/
	
	// respInfo Check
	public ArrayList<OBDtoRespGroup> getRespInfoList(OBDtoSearch searchOption, Integer orderType, Integer orderDir) throws OBException, Exception
	{
	    return adcMgmt.getResponseTimeList(searchOption, orderType, orderDir);
	}
	
	public OBDtoRespGroup getRespInfo(Integer respIndex) throws OBException, Exception
	{
	    return adcMgmt.getRespSectionCheck(respIndex);
	}
	
	public ArrayList<OBDtoRespMultiChartData> getResponseTimeHistory(Integer respIndex, OBDtoSearch searchOption) throws Exception
    {
        return adcMgmt.getResponseTimeHistory(respIndex, searchOption);
//        return adcMgmt.getResponseTimeHistory(respIndex); 
    }
	
    public boolean isExistRespIntervalCheck(String respName) throws OBException, Exception
    {
        log.debug("respName: {}", respName);
        return adcMgmt.isExistRespSectionCheck(respName); 
    }
    
    public boolean addRespIntervalCheck(OBDtoRespGroup respInfo) throws OBException, Exception
    {
        log.debug("respInfo: {}", respInfo);
        return adcMgmt.addRespSectionCheck(respInfo); 
    }
    
    public boolean setRespIntervalCheck(OBDtoRespGroup respInfo) throws OBException, Exception
    {
        log.debug("respInfo: {}", respInfo);
        
        ArrayList<OBDtoRespInfo> respInfoData = respInfo.getRespInfo();
        int size = respInfoData.size();
        for(int i = 0; i < size; i++)
        {
            respInfoData.get(i).setRespOrder(i);
        }
        return adcMgmt.setRespSectionCheck(respInfo); 
    }
    
    public void delRespIntervalCheck(List<String> respIndex) throws OBException, Exception
    {
        log.debug("respIndex: {}", respIndex);
        adcMgmt.delRespSectionCheck(new ArrayList<String>(respIndex)); 
    }
}
