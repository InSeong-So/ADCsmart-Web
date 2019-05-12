package kr.openbase.adcsmart.web.report.jasper;


/**
 * 이 클래스는 JasperReports 생성에 필요한 프라퍼티를  설정할 수 있게한다.
 * @author oklee
 * @version 1.0
 */
public class JRProperties 
{
	private String 	srcPath = "";
	private String 	subReportPath = "";
	private String 	imgPath = "";
	private String 	destPathFile = "";
	private String 	outType = "";
	private String 	reportIndex = "";
	private Integer accntIndex;
	private String 	subImgPath = "";
	private String 	rptType = "";// report type.
	
	public String getRptType()
	{
		return rptType;
	}
	public void setRptType(String rptType)
	{
		this.rptType = rptType;
	}
	public Integer getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex)
	{
		this.accntIndex = accntIndex;
	}
	public String getSrcPath() 
	{
		return srcPath;
	}
	public void setSrcPath(String srcPath) 
	{
		this.srcPath = srcPath;
	}
	public String getSubReportPath() 
	{
		return subReportPath;
	}
	public void setSubReportPath(String subReportPath) 
	{
		this.subReportPath = subReportPath;
	}
	public String getImgPath() 
	{
		return imgPath;
	}
	public void setImgPath(String imgPath) 
	{
		this.imgPath = imgPath;
	}
	public String getDestPathFile() 
	{
		return destPathFile;
	}
	public void setDestPathFile(String destPathFile) 
	{
		this.destPathFile = destPathFile;
	}
	public String getOutType() 
	{
		return outType;
	}
	public void setOutType(String outType) 
	{
		this.outType = outType;
	}
	public String getReportIndex() 
	{
		return reportIndex;
	}
	public void setReportIndex(String reportIndex) 
	{
		this.reportIndex = reportIndex;
	}
	public String getSubImgPath()
	{
		return subImgPath;
	}
	public void setSubImgPath(String subImgPath)
	{
		this.subImgPath = subImgPath;
	}	
	@Override
	public String toString() 
	{
		return "JRProperties [srcPath=" + srcPath + ", subReportPath=" + subReportPath + ", imgPath=" + imgPath
				+ ", destPathFile=" + destPathFile + ", outType=" + outType + ", reportIndex=" + reportIndex + "]";
	}
	
}
