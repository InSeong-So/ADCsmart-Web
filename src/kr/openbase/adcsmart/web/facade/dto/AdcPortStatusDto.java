package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;
import java.util.List;

public class AdcPortStatusDto
{	
	private String intfName;
	private Integer linkStatus;
	private Long pktsIn;
	private Long pktsOut;
	private Long pktsTot;
	private Long bytesIn;
	private Long bytesOut;
	private Long bytesTot;
	private Long errorsIn;
	private Long errorsOut;
	private Long errorsTot;
	private Long dropsIn;
	private Long dropsOut;
	private Long dropsTot;
	
	//byte를 단위변환
	private String ppktsIn;
	private String ppktsOut;
	private String ppktsTot;
	private String bbytesIn;
	private String bbytesOut;
	private String bbytesTot;
	private String eerrorsIn;
	private String eerrorsOut;
	private String eerrorsTot;
	private String ddropsIn;
	private String ddropsOut;
	private String ddropsTot;
	private String dispName="";
	
	private Date occurTime;
	private List<String> intfNameList;
	
	@Override
	public String toString()
	{
		return "AdcPortStatusDto [intfName=" + intfName + ", linkStatus="
				+ linkStatus + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut
				+ ", pktsTot=" + pktsTot + ", bytesIn=" + bytesIn
				+ ", bytesOut=" + bytesOut + ", bytesTot=" + bytesTot
				+ ", errorsIn=" + errorsIn + ", errorsOut=" + errorsOut
				+ ", errorsTot=" + errorsTot + ", dropsIn=" + dropsIn
				+ ", dropsOut=" + dropsOut + ", dropsTot=" + dropsTot
				+ ", ppktsIn=" + ppktsIn + ", ppktsOut=" + ppktsOut
				+ ", ppktsTot=" + ppktsTot + ", bbytesIn=" + bbytesIn
				+ ", bbytesOut=" + bbytesOut + ", bbytesTot=" + bbytesTot
				+ ", eerrorsIn=" + eerrorsIn + ", eerrorsOut=" + eerrorsOut
				+ ", eerrorsTot=" + eerrorsTot + ", ddropsIn=" + ddropsIn
				+ ", ddropsOut=" + ddropsOut + ", ddropsTot=" + ddropsTot
				+ ", dispName=" + dispName + ", occurTime=" + occurTime
				+ ", intfNameList=" + intfNameList + "]";
	}
	
	public List<String> getIntfNameList()
	{
		return intfNameList;
	}

	public void setIntfNameList(List<String> intfNameList)
	{
		this.intfNameList = intfNameList;
	}

	public Date getOccurTime()
	{
		return occurTime;
	}

	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}

	public String getIntfName() 
	{
		return intfName;
	}
	
	public String getDispName()
	{
		return dispName;
	}

	public void setDispName(String dispName)
	{
		this.dispName = dispName;
	}

	public void setIntfName(String intfName) 
	{
		this.intfName = intfName;
	}
	
	public Integer getLinkStatus() 
	{
		return linkStatus;
	}
	
	public void setLinkStatus(Integer linkStatus) 
	{
		this.linkStatus = linkStatus;
	}
	
	public Long getPktsIn() 
	{
		return pktsIn;
	}
	
	public void setPktsIn(Long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	
	public Long getPktsOut() 
	{
		return pktsOut;
	}
	
	public void setPktsOut(Long pktsOut) 
	{
		this.pktsOut = pktsOut;
	}
	
	public Long getPktsTot() 
	{
		return pktsTot;
	}
	
	public void setPktsTot(Long pktsTot) 
	{
		this.pktsTot = pktsTot;
	}
	
	public Long getBytesIn() 
	{
		return bytesIn;
	}
	
	public void setBytesIn(Long bytesIn) 
	{
		this.bytesIn = bytesIn;
	}
	
	public Long getBytesOut() 
	{
		return bytesOut;
	}
	
	public void setBytesOut(Long bytesOut) {
		this.bytesOut = bytesOut;
	}

	public Long getBytesTot()
	{
		return bytesTot;
	}

	public void setBytesTot(Long bytesTot)
	{
		this.bytesTot = bytesTot;
	}

	public Long getErrorsIn()
	{
		return errorsIn;
	}

	public void setErrorsIn(Long errorsIn)
	{
		this.errorsIn = errorsIn;
	}

	public Long getErrorsOut()
	{
		return errorsOut;
	}

	public void setErrorsOut(Long errorsOut)
	{
		this.errorsOut = errorsOut;
	}

	public Long getErrorsTot()
	{
		return errorsTot;
	}

	public void setErrorsTot(Long errorsTot)
	{
		this.errorsTot = errorsTot;
	}

	public Long getDropsIn()
	{
		return dropsIn;
	}

	public void setDropsIn(Long dropsIn)
	{
		this.dropsIn = dropsIn;
	}

	public Long getDropsOut()
	{
		return dropsOut;
	}

	public void setDropsOut(Long dropsOut)
	{
		this.dropsOut = dropsOut;
	}

	public Long getDropsTot()
	{
		return dropsTot;
	}

	public void setDropsTot(Long dropsTot)
	{
		this.dropsTot = dropsTot;
	}

	public String getPpktsIn()
	{
		return ppktsIn;
	}

	public void setPpktsIn(String ppktsIn)
	{
		this.ppktsIn = ppktsIn;
	}

	public String getPpktsOut()
	{
		return ppktsOut;
	}

	public void setPpktsOut(String ppktsOut)
	{
		this.ppktsOut = ppktsOut;
	}

	public String getPpktsTot()
	{
		return ppktsTot;
	}

	public void setPpktsTot(String ppktsTot)
	{
		this.ppktsTot = ppktsTot;
	}

	public String getBbytesIn()
	{
		return bbytesIn;
	}

	public void setBbytesIn(String bbytesIn)
	{
		this.bbytesIn = bbytesIn;
	}

	public String getBbytesOut()
	{
		return bbytesOut;
	}

	public void setBbytesOut(String bbytesOut)
	{
		this.bbytesOut = bbytesOut;
	}

	public String getBbytesTot()
	{
		return bbytesTot;
	}

	public void setBbytesTot(String bbytesTot)
	{
		this.bbytesTot = bbytesTot;
	}

	public String getEerrorsIn()
	{
		return eerrorsIn;
	}

	public void setEerrorsIn(String eerrorsIn)
	{
		this.eerrorsIn = eerrorsIn;
	}

	public String getEerrorsOut()
	{
		return eerrorsOut;
	}

	public void setEerrorsOut(String eerrorsOut)
	{
		this.eerrorsOut = eerrorsOut;
	}

	public String getEerrorsTot()
	{
		return eerrorsTot;
	}

	public void setEerrorsTot(String eerrorsTot)
	{
		this.eerrorsTot = eerrorsTot;
	}

	public String getDdropsIn()
	{
		return ddropsIn;
	}

	public void setDdropsIn(String ddropsIn)
	{
		this.ddropsIn = ddropsIn;
	}

	public String getDdropsOut()
	{
		return ddropsOut;
	}

	public void setDdropsOut(String ddropsOut)
	{
		this.ddropsOut = ddropsOut;
	}

	public String getDdropsTot()
	{
		return ddropsTot;
	}

	public void setDdropsTot(String ddropsTot)
	{
		this.ddropsTot = ddropsTot;
	}	
}
