package kr.openbase.adcsmart.web.report.faultdiagnosis;

import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class OBDtoReportDiagnosisElement
{
	private String category;
	private String title;
	private String status;
	private Integer statusCode;
	private String summary;
	private String details;
	
	private OBDtoReportTextHdr textHdr;

	@Override
	public String toString()
	{
		return "OBDtoReportDiagnosisElement [category=" + category + ", title=" + title + ", status=" + status + ", statusCode=" + statusCode + ", summary=" + summary + ", details=" + details + ", textHdr=" + textHdr + "]";
	}
	
	public Integer getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(Integer statusCode)
	{
		this.statusCode = statusCode;
	}

	public OBDtoReportTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(OBDtoReportTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}
	
	public String getTitle()
	{
		return title;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getDetails()
	{
		return details;
	}
	public void setDetails(String details)
	{
		this.details = details;
	}
}
