/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

/**
 * @author yh.Yang
 *
 */
public class SystemDatabaseInfoDto
{
/** 차트에 보내는 형태를 NumberUtil에서 파싱하여 보내는 것으로 변경하였기 때문에 String 형으로 변경합니다. 2013.03.20 yh.Yang
*	private Long totalDiskSize;
	private Long generalUsed;
	private Long indexUsed;
	private Long logUsed;*/
    
// 테이터형을 total used (size,Usage)와 total (size,Usage) 를 분리 추가한다. 2015.02.12 yh.Yang
	
	private String totalUsage;
	private String totalUsedUsage;
	private String generalUsage;
	private String indexUsage;
	private String logUsage;	
	private String etcUsage;
	
	private String totalSize;
	private String totalUsedSize;
	private String generalSize;
	private String indexSize;
	private String logSize;
	private String etcSize;

    public String getTotalUsage()
    {
        return totalUsage;
    }

    public void setTotalUsage(String totalUsage)
    {
        this.totalUsage = totalUsage;
    }

    public String getTotalUsedUsage()
    {
        return totalUsedUsage;
    }

    public void setTotalUsedUsage(String totalUsedUsage)
    {
        this.totalUsedUsage = totalUsedUsage;
    }

    public String getGeneralUsage()
    {
        return generalUsage;
    }

    public void setGeneralUsage(String generalUsage)
    {
        this.generalUsage = generalUsage;
    }

    public String getIndexUsage()
    {
        return indexUsage;
    }

    public void setIndexUsage(String indexUsage)
    {
        this.indexUsage = indexUsage;
    }

    public String getLogUsage()
    {
        return logUsage;
    }

    public void setLogUsage(String logUsage)
    {
        this.logUsage = logUsage;
    }

    public String getEtcUsage()
    {
        return etcUsage;
    }

    public void setEtcUsage(String etcUsage)
    {
        this.etcUsage = etcUsage;
    }

    public String getTotalSize()
    {
        return totalSize;
    }

    public void setTotalSize(String totalSize)
    {
        this.totalSize = totalSize;
    }

    public String getTotalUsedSize()
    {
        return totalUsedSize;
    }

    public void setTotalUsedSize(String totalUsedSize)
    {
        this.totalUsedSize = totalUsedSize;
    }

    public String getGeneralSize()
    {
        return generalSize;
    }

    public void setGeneralSize(String generalSize)
    {
        this.generalSize = generalSize;
    }

    public String getIndexSize()
    {
        return indexSize;
    }

    public void setIndexSize(String indexSize)
    {
        this.indexSize = indexSize;
    }

    public String getLogSize()
    {
        return logSize;
    }

    public void setLogSize(String logSize)
    {
        this.logSize = logSize;
    }

    public String getEtcSize()
    {
        return etcSize;
    }

    public void setEtcSize(String etcSize)
    {
        this.etcSize = etcSize;
    }

    @Override
    public String toString()
    {
        return "SystemDatabaseInfoDto [totalUsage=" + totalUsage + ", totalUsedUsage=" + totalUsedUsage + ", generalUsage=" + generalUsage + ", indexUsage=" + indexUsage + ", logUsage=" + logUsage + ", etcUsage=" + etcUsage + ", totalSize=" + totalSize + ", totalUsedSize=" + totalUsedSize + ", generalSize=" + generalSize + ", indexSize=" + indexSize + ", logSize=" + logSize + ", etcSize=" + etcSize + "]";
    }
}