package kr.openbase.adcsmart.web.facade.dto.adcmon;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTrafficStatus;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class OBDtoAdcmonTrafficStatus
{
	private OBDtoAdcmonDataObj currTotal;// 최근 전체 트래픽
	private OBDtoAdcmonDataObj prevTotal;// 전일 최근 트래픽.
	private OBDtoAdcmonDataObj currMax;// 현재 최대.
	private OBDtoAdcmonDataObj prevMax;// 전일 최대.
	private OBDtoAdcmonDataObj curMin;// 현재 최소.
	private OBDtoAdcmonDataObj prevMin;// 전일 최소.
	private OBDtoAdcmonDataObj currAvg;// 현재 평균.
	private OBDtoAdcmonDataObj prevAvg;//전일 평균.
	@Override
	public String toString()
	{
		return "OBDtoAdcmonTrafficStatus [currTotal=" + currTotal
				+ ", prevTotal=" + prevTotal + ", currMax=" + currMax
				+ ", prevMax=" + prevMax + ", curMin=" + curMin + ", prevMin="
				+ prevMin + ", currAvg=" + currAvg + ", prevAvg=" + prevAvg
				+ "]";
	}// 전일 평균.
	public OBDtoAdcmonDataObj getCurrTotal()
	{
		return currTotal;
	}
	public void setCurrTotal(OBDtoAdcmonDataObj currTotal)
	{
		this.currTotal = currTotal;
	}
	public OBDtoAdcmonDataObj getPrevTotal()
	{
		return prevTotal;
	}
	public void setPrevTotal(OBDtoAdcmonDataObj prevTotal)
	{
		this.prevTotal = prevTotal;
	}
	public OBDtoAdcmonDataObj getCurrMax()
	{
		return currMax;
	}
	public void setCurrMax(OBDtoAdcmonDataObj currMax)
	{
		this.currMax = currMax;
	}
	public OBDtoAdcmonDataObj getPrevMax()
	{
		return prevMax;
	}
	public void setPrevMax(OBDtoAdcmonDataObj prevMax)
	{
		this.prevMax = prevMax;
	}
	public OBDtoAdcmonDataObj getCurMin()
	{
		return curMin;
	}
	public void setCurMin(OBDtoAdcmonDataObj curMin)
	{
		this.curMin = curMin;
	}
	public OBDtoAdcmonDataObj getPrevMin()
	{
		return prevMin;
	}
	public void setPrevMin(OBDtoAdcmonDataObj prevMin)
	{
		this.prevMin = prevMin;
	}
	public OBDtoAdcmonDataObj getCurrAvg()
	{
		return currAvg;
	}
	public void setCurrAvg(OBDtoAdcmonDataObj currAvg)
	{
		this.currAvg = currAvg;
	}
	public OBDtoAdcmonDataObj getPrevAvg()
	{
		return prevAvg;
	}
	public void setPrevAvg(OBDtoAdcmonDataObj prevAvg)
	{
		this.prevAvg = prevAvg;
	}

	public OBDtoAdcmonTrafficStatus toAdcmonTrafficStatus(OBDtoDashboardTrafficStatus item)
	{
		OBDtoAdcmonTrafficStatus retVal = new OBDtoAdcmonTrafficStatus();
		// 최근 전체 트래픽
		OBDtoDataObj currTotal = item.getCurrTotal();
		OBDtoAdcmonDataObj obj = new OBDtoAdcmonDataObj();
		obj.setValue(NumberUtil.toStringWithUnit(currTotal.getValue(), " "));
		retVal.setCurrTotal(obj);
		// 전일 최근 트래픽
		OBDtoDataObj prevTotal = item.getPrevTotal();
		OBDtoAdcmonDataObj obj1 = new OBDtoAdcmonDataObj();
		obj1.setValue(NumberUtil.toStringWithUnit(prevTotal.getValue(), " "));
		retVal.setPrevTotal(obj1);
		// 현재 최대.
		OBDtoDataObj currMax = item.getCurrMax();
		OBDtoAdcmonDataObj obj2 = new OBDtoAdcmonDataObj();
		obj2.setValue(NumberUtil.toStringWithUnit(currMax.getValue(), " "));
		retVal.setCurrMax(obj2);
		// 전일 최대.
		OBDtoDataObj prevMax = item.getPrevMax();
		OBDtoAdcmonDataObj obj3 = new OBDtoAdcmonDataObj();
		obj3.setValue(NumberUtil.toStringWithUnit(prevMax.getValue(), " "));
		retVal.setPrevMax(obj3);
		// 현재 최소.
		OBDtoDataObj curMin = item.getCurMin();
		OBDtoAdcmonDataObj obj4 = new OBDtoAdcmonDataObj();
		obj4.setValue(NumberUtil.toStringWithUnit(curMin.getValue(), " "));
		retVal.setCurMin(obj4);
		// 전일 최소.
		OBDtoDataObj prevMin = item.getPrevMin();
		OBDtoAdcmonDataObj obj5 = new OBDtoAdcmonDataObj();
		obj5.setValue(NumberUtil.toStringWithUnit(prevMin.getValue(), " "));
		retVal.setPrevMin(obj5);
		// 현재 평균.
		OBDtoDataObj currAvg = item.getCurrAvg();
		OBDtoAdcmonDataObj obj6 = new OBDtoAdcmonDataObj();
		obj6.setValue(NumberUtil.toStringWithUnit(currAvg.getValue(), " "));
		retVal.setCurrAvg(obj6);
		//전일 평균.
		OBDtoDataObj prevAvg = item.getPrevAvg();
		OBDtoAdcmonDataObj obj7 = new OBDtoAdcmonDataObj();
		obj7.setValue(NumberUtil.toStringWithUnit(prevAvg.getValue(), " "));
		retVal.setPrevAvg(obj7);
		
		return retVal;
	}
}


