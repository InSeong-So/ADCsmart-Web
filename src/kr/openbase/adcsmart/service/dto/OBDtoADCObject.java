package kr.openbase.adcsmart.service.dto;

public class OBDtoADCObject
{
	public static final  int	CATEGORY_ALL	 =0;
	public static final  int	CATEGORY_GROUP	 =1;
	public static final  int	CATEGORY_ADC	 =2;
	public static final  int	CATEGORY_VS		 =3; // virtual server
	public static final  int	CATEGORY_VSVC	 =4; // virtual service
	public static final  int	CATEGORY_MEMBER  =5; // service/server member
	public static final  int	CATEGORY_POOLGROUP  =6; // service/server member
	public static final  int    CATEGORY_SERVICEGROUP  =7; // SERVICE GROUP
	public static final  int    CATEGORY_SERVICEGROUPVSLIST  =8; // 서비스 그룹의 VS리스트
	public static final  int    CATEGORY_SERVICEGROUPMEMBERLIST  =9; // 서비스 그룹의 MEMBER리스트

	private Integer category=0; //0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
	private Integer index=0;
	private String  strIndex; //virtual server, service는 본 필드를 이용한다.
	private String 	name="";
	private Integer vendor=0;
	private Integer status=0;
	private String  desciption="";
	private Object  extraInfo=null;
	private String  alteonId=""; // AlteonId : Integer -> String으로 변경에 따른 추가
	private Integer port=0;
	
	public OBDtoADCObject()
	{
	}
	
	public OBDtoADCObject(Integer category, Integer index, Integer vendor)
	{
		super();
		this.category = category;
		this.index = index;
		this.vendor = vendor;
	}

	@Override
    public String toString()
    {
        return "OBDtoADCObject [category=" + category + ", index=" + index
                + ", strIndex=" + strIndex + ", name=" + name + ", vendor="
                + vendor + ", status=" + status + ", desciption=" + desciption
                + ", extraInfo=" + extraInfo + ", alteonId=" + alteonId
                + ", port=" + port + "]";
    }
	public Object getExtraInfo()
	{
		return extraInfo;
	}
	public void setExtraInfo(Object extraInfo)
	{
		this.extraInfo = extraInfo;
	}
	public String getStrIndex()
	{
		return strIndex;
	}
	public void setStrIndex(String strIndex)
	{
		this.strIndex = strIndex;
	}
	public Integer getCategory()
	{
		return category;
	}
	public void setCategory(Integer category)
	{
		this.category = category;
	}
	public Integer getVendor()
	{
		return vendor;
	}
	public void setVendor(Integer vendor)
	{
		this.vendor = vendor;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getDesciption()
	{
		return desciption;
	}
	public void setDesciption(String desciption)
	{
		this.desciption = desciption;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
    public String getAlteonId()
    {
        return alteonId;
    }
    public void setAlteonId(String alteonId)
    {
        this.alteonId = alteonId;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }	
}
