package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.utility.OBException;


public class AdcExceptionDto
{
	private String generalMsg="not defined message";
	private String solutionMsg="not defined message";
	private String detailMsg="not defined message";
//	private Integer code;
	private String code;
	private Date	occurTime;
	private String  moduleName;
	
//	private String generalMsg = "";
	private ArrayList<Integer> codeList = new ArrayList<Integer>();
	private ArrayList<String> generalMsgList = new ArrayList<String>();
	private ArrayList<String> solutionMsgList = new ArrayList<String>();
	private ArrayList<String> detailMsgList = new ArrayList<String>();
	private ArrayList<String> moduleNameList = new ArrayList<String>();
	
	
	public String getGeneralMsg()
	{
		return generalMsg;
	}
	public void setGeneralMsg(String generalMsg)
	{
		this.generalMsg = generalMsg;
	}
	public void setOBException(OBException ex) 
	{
			if(null == ex)
				return;

//			this.generalMsgList.add(ex.getErrorMessage());
//			this.solutionMsgList.add(ex.getErrorMessage());
//			this.detailMsgList.add(ex.getErrorMessage());
//			this.moduleNameList.add(ex.getErrorMessage());
//			System.out.println(ex.getMessage());			//ADC 장비 정보 조회에 실패했습니다.
//			System.out.println(ex.getOccurTime());			//null
//			System.out.println(ex.getGeneralMsgList());		//[ADC 장비 정보 조회에 실패했습니다.]
//			System.out.println(ex.getSolutionMsgList());	//[다시 시도해 보십시오]
//			System.out.println(ex.getDetailedMsgList());	//[]
//			System.out.println(ex.getModuleNameList());		//[]
	}
	
	public String getSolutionMsg()
	{
		return solutionMsg;
	}
	public void setSolutionMsg(String solutionMsg)
	{
		this.solutionMsg = solutionMsg;
	}
	public String getDetailMsg()
	{
		return detailMsg;
	}
	public void setDetailMsg(String detailMsg)
	{
		this.detailMsg = detailMsg;
	}
/*	public Integer getCode()
	{
		return code;
	}
	public void setCode(Integer code)
	{
		this.code = code;
	}*/
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	
	public String getModuleName()
	{
		return moduleName;
	}
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public ArrayList<Integer> getCodeList()
	{
		return codeList;
	}
	public void setCodeList(ArrayList<Integer> codeList)
	{
		this.codeList = codeList;
	}

	
	public String getMessage() 
	{
		String result="";
		for(String msg:this.generalMsgList)
		{
			if(!result.isEmpty())
				result += msg + "(" + result + ")";
			else
				result += msg;
		}
					
		return result;
	}
	
	public ArrayList<String> getGeneralMsgList()
	{
		return generalMsgList;
	}

	public void setGeneralMsgList(ArrayList<String> generalMsgList)
	{
		this.generalMsgList = generalMsgList;
	}

	public ArrayList<String> getSolutionMsgList()
	{
		return solutionMsgList;
	}

	public void setSolutionMsgList(ArrayList<String> solutionMsgList)
	{
		this.solutionMsgList = solutionMsgList;
	}

	public ArrayList<String> getDetailMsgList()
	{
		return detailMsgList;
	}

	public void setDetailMsgList(ArrayList<String> detailMsgList)
	{
		this.detailMsgList = detailMsgList;
	}
	public ArrayList<String> getModuleNameList()
	{
		return moduleNameList;
	}
	public void setModuleNameList(ArrayList<String> moduleNameList)
	{
		this.moduleNameList = moduleNameList;
	}	
	
}
