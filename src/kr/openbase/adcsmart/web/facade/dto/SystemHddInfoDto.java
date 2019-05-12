/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

/**
 * @author yh.Yang
 *
 */
public class SystemHddInfoDto
{
/** 차트에 보내는 형태를 NumberUtil에서 파싱하여 보내는 것으로 변경하였기 때문에 String 형으로 변경합니다. 2013.03.20 y.h.Yang
*/
 // 테이터형을 total used (size,Usage)와 total (size,Usage) 를 분리 추가한다. 2015.02.12 yh.Yang
	private String totalUsage;
	private String usedUsage;
	private String freeUsage;
	private String totalUsage30Before;
	private String usedUsage30Before;
	
	private String totalSize;
	private String usedSize;
	private String freeSize;
	private String usedSize30Before;
	
    public String getTotalUsage()
    {
        return totalUsage;
    }
    public void setTotalUsage(String totalUsage)
    {
        this.totalUsage = totalUsage;
    }
    public String getUsedUsage()
    {
        return usedUsage;
    }
    public void setUsedUsage(String usedUsage)
    {
        this.usedUsage = usedUsage;
    }
    public String getFreeUsage()
    {
        return freeUsage;
    }
    public void setFreeUsage(String freeUsage)
    {
        this.freeUsage = freeUsage;
    }
    public String getTotalSize()
    {
        return totalSize;
    }
    public void setTotalSize(String totalSize)
    {
        this.totalSize = totalSize;
    }
    public String getUsedSize()
    {
        return usedSize;
    }
    public void setUsedSize(String usedSize)
    {
        this.usedSize = usedSize;
    }
    public String getFreeSize()
    {
        return freeSize;
    }
    public void setFreeSize(String freeSize)
    {
        this.freeSize = freeSize;
    }    
    public String getUsedUsage30Before()
    {
        return usedUsage30Before;
    }
    public void setUsedUsage30Before(String usedUsage30Before)
    {
        this.usedUsage30Before = usedUsage30Before;
    }    
    public String getTotalUsage30Before()
    {
        return totalUsage30Before;
    }
    public void setTotalUsage30Before(String totalUsage30Before)
    {
        this.totalUsage30Before = totalUsage30Before;
    }    
    public String getUsedSize30Before()
    {
        return usedSize30Before;
    }
    public void setUsedSize30Before(String usedSize30Before)
    {
        this.usedSize30Before = usedSize30Before;
    }
    @Override
    public String toString()
    {
        return "SystemHddInfoDto [totalUsage=" + totalUsage + ", usedUsage=" + usedUsage + ", freeUsage=" + freeUsage + ", totalUsage30Before=" + totalUsage30Before + ", usedUsage30Before=" + usedUsage30Before + ", totalSize=" + totalSize + ", usedSize=" + usedSize + ", freeSize=" + freeSize + ", usedSize30Before=" + usedSize30Before + "]";
    }

}