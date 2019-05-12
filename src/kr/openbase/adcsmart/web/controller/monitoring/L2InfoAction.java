package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchOption;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.monitoring.L2InfoFacade;
import kr.openbase.adcsmart.web.json.l2SearchOptionJsonAdapter;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class L2InfoAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(L2InfoAction.class);
	
	@Autowired	
	private L2InfoFacade l2InfoFacade;
	
	private Integer rowTotal;									// L2 리스트 Total Count
	private List<OBDtoL2SearchInfo> l2InfoList;					// L2 리스트 (Table Data)
	private AdcDto adc;											// ADC Info Dto	
	private String l2SearchKeyListString;						// L2 검색 조건 String Set
	private ArrayList<OBDtoL2SearchOption> l2SearchKeyList;		// L2 검색 조건 Array List	
	private OBDtoADCObject adcObject;							// 현재 선택한 adc object.
	private OBDtoSearch searchObj;								// 검색 Dto
	private OBDtoOrdering orderObj;								// 정렬 Dto
	
	//초기 페이지 Load	
	public String loadL2SearchContent() throws OBException 
	{		
		try
		{
			l2InfoFacade.cleanLocalL2List(adcObject);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
	// L2 정보 ADC에서 DB로 SET (Return 값 무시)
	public String setL2InfoListToDB() throws OBException
	{
		try
		{
			l2InfoList = l2InfoFacade.getL2InfoList(adcObject, l2SearchKeyList, searchObj);					
			log.debug("l2InfoList:{}", l2InfoList);							
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	//L2 총 리스트 갯수 Get
	public String loadL2InfoListTotal() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			if (adcObject != null && adcObject.getIndex() != null)
			{
				rowTotal = l2InfoFacade.loadL2InfoListTotal(adcObject);
			}
			log.debug("row total: {}", rowTotal);
		} 
		catch (OBException e) 
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}	
	// L2 정보 Get ( 페이징 정렬 조건에 맞게 제공)
	public String loadL2InfoTableInListContent() throws OBException
	{
		try
		{
			l2InfoList = l2InfoFacade.getL2InfoListBySort(adcObject, searchObj, orderObj);					
			log.debug("l2InfoList:{}", l2InfoList);							
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	// Export 전 Check
	public String checkExportL2InfoExist() throws OBException
	{
		try
		{
			l2InfoList = l2InfoFacade.getL2InfoListBySort(adcObject, searchObj, orderObj);					
			log.debug("l2InfoList:{}", l2InfoList);
			
			if (l2InfoList != null && l2InfoList.size()>0)
			{
				isSuccessful = true;
				l2InfoList = null;
			}
			else
			{
				isSuccessful = false;
				l2InfoList = null;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
			}
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	// Export L2 Info
	public String downloadL2Info() throws OBException 
	{
		try 
		{
			l2InfoList = l2InfoFacade.getL2InfoListBySort(adcObject, searchObj, orderObj);					
			log.debug("l2InfoList:{}", l2InfoList);
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithL2Info(l2InfoList);
			File csv = csvMaker.write();
			l2InfoList = null;
			if (csv != null)
			{
				setStrutsStream(csv);
			}			
		} 
        catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		
		return SUCCESS;
	}
	
	private void convertl2SearchKeyToJSON()
	{
		if (StringUtils.isEmpty(l2SearchKeyListString))
			return ;		
		
		l2SearchKeyList = new ArrayList<OBDtoL2SearchOption>();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoL2SearchOption.class, new l2SearchOptionJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(l2SearchKeyListString).getAsJsonArray();
		for (JsonElement e : jarray)
			l2SearchKeyList.add(gson.fromJson(e, OBDtoL2SearchOption.class));		
	}

	public List<OBDtoL2SearchInfo> getL2SearchInfoList()
	{
		return l2InfoList;
	}

	public void setL2SearchInfoList(List<OBDtoL2SearchInfo> l2SearchInfoList)
	{
		this.l2InfoList = l2SearchInfoList;
	}

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
	}
	
	public String getL2SearchKeyListString()
	{
		return l2SearchKeyListString;
	}

	public void setL2SearchKeyListString(String l2SearchKeyListString)
	{
		this.l2SearchKeyListString = l2SearchKeyListString;
		convertl2SearchKeyToJSON();
	}

	public OBDtoSearch getSearchObj()
	{
		return searchObj;
	}

	public void setSearchObj(OBDtoSearch searchObj)
	{
		this.searchObj = searchObj;
	}

	public OBDtoOrdering getOrderObj()
	{
		return orderObj;
	}

	public void setOrderObj(OBDtoOrdering orderObj)
	{
		this.orderObj = orderObj;
	}

	public Integer getRowTotal()
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
	}
	
	public List<OBDtoL2SearchInfo> getL2InfoList()
	{
		return l2InfoList;
	}

	public void setL2InfoList(List<OBDtoL2SearchInfo> l2InfoList)
	{
		this.l2InfoList = l2InfoList;
	}

	public ArrayList<OBDtoL2SearchOption> getL2SearchKeyList()
	{
		return l2SearchKeyList;
	}

	public void setL2SearchKeyList(ArrayList<OBDtoL2SearchOption> l2SearchKeyList)
	{
		this.l2SearchKeyList = l2SearchKeyList;
	}
	
	public AdcDto getAdc()
	{
		return adc;
	}
	
	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}
	
	@Override
	public String toString()
	{
		return "L2InfoAction [l2InfoFacade=" + l2InfoFacade + ", rowTotal=" + rowTotal + ", l2InfoList=" + l2InfoList + ", adc=" + adc + ", l2SearchKeyListString=" + l2SearchKeyListString + ", l2SearchKeyList=" + l2SearchKeyList + ", adcObject=" + adcObject + ", searchObj=" + searchObj + ", orderObj=" + orderObj + "]";
	}	
}