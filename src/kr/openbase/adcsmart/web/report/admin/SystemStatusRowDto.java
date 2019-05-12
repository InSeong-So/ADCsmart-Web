package kr.openbase.adcsmart.web.report.admin;

public class SystemStatusRowDto 
{
	private String category = "";
	private String item = "";
	private String result = "";
	private String detail = "";
	private String remarks = "";
	
	private SystemStatusRowDtoTextHdr textHdr;
	
	public SystemStatusRowDto(String category, String item, String result, String detail) 
	{
		this.category = category;
		this.item = item;
		this.result = result;
		this.detail = detail;
	}
	
	public SystemStatusRowDto(String item, String result, String detail)
	{
		this.item = item;
		this.result = result;
		this.detail = detail;
	}
	
	public String getCategory() 
	{
		return category;
	}
	public void setCategory(String category) 
	{
		this.category = category;
	}
	public String getItem() 
	{
		return item;
	}
	public void setItem(String item)
	{
		this.item = item;
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
	public String getRemarks() 
	{
		return remarks;
	}
	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}
	public SystemStatusRowDtoTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(SystemStatusRowDtoTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	@Override
	public String toString()
	{
		return "SystemStatusRowDto [category=" + category + ", item=" + item + ", result=" + result + ", detail=" + detail + ", remarks=" + remarks + ", textHdr=" + textHdr + "]";
	}
	
}
