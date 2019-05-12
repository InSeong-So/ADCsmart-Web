package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfo;

public class ResultDetailDto
{
	private String title;
	private String result;
	private String detail;
	
	public static ResultDetailDto toResultDetailDto(OBDtoRptSysInfo sysInfoFromSvc)
	{
		if (sysInfoFromSvc == null)
		{
			return null;
		}	
		
		ResultDetailDto resultDetailDto = new ResultDetailDto();
		resultDetailDto.setTitle(sysInfoFromSvc.getName());
		resultDetailDto.setResult(sysInfoFromSvc.getResult());
		resultDetailDto.setDetail(sysInfoFromSvc.getContents());
		return resultDetailDto;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getResult()
	{
		return result;
	}
	public void setResult(String result) 
	{
		this.result = result;
	}
	public String getDetail()
	{
		return detail;
	}
	public void setDetail(String detail)
	{
		this.detail = detail;
	}
	@Override
	public String toString() 
	{
		return "ResultDetailDto [result=" + result + ", detail=" + detail + "]";
	}
	
}
