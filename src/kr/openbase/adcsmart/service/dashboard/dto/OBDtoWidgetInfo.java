package kr.openbase.adcsmart.service.dashboard.dto;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;

public class OBDtoWidgetInfo
{
	private String 			index;
	private String  		name;
	private Integer    		type;
	private OBDtoADCObject 	targetObj;
	private Integer			width;
	private Integer			widthMin;
	private Integer			widthMax;
	private Integer        	height;
	private Integer        	heightMin;
	private Integer        	heightMax;
	private Integer         xPosition;// start with 1
	private Integer         yPosition;// start with 1
	private Integer			moreInfoIndex;
	private String          extendSelectIndex;
		
	@Override
    public String toString()
    {
        return "OBDtoWidgetInfo [index=" + index + ", name=" + name + ", type="
                + type + ", targetObj=" + targetObj + ", width=" + width
                + ", widthMin=" + widthMin + ", widthMax=" + widthMax
                + ", height=" + height + ", heightMin=" + heightMin
                + ", heightMax=" + heightMax + ", xPosition=" + xPosition
                + ", yPosition=" + yPosition + ", moreInfoIndex="
                + moreInfoIndex + ", extendSelectIndex=" + extendSelectIndex
                + "]";
    }
	
	public String getExtendSelectIndex()
    {
        return extendSelectIndex;
    }

    public void setExtendSelectIndex(String extendSelectIndex)
    {
        this.extendSelectIndex = extendSelectIndex;
    }

    public Integer getMoreInfoIndex()
	{
		return moreInfoIndex;
	}

	public void setMoreInfoIndex(Integer moreInfoIndex)
	{
		this.moreInfoIndex = moreInfoIndex;
	}

	public Integer getWidthMin()
	{
		return widthMin;
	}
	public void setWidthMin(Integer widthMin)
	{
		this.widthMin = widthMin;
	}
	public Integer getWidthMax()
	{
		return widthMax;
	}
	public void setWidthMax(Integer widthMax)
	{
		this.widthMax = widthMax;
	}
	public Integer getHeightMin()
	{
		return heightMin;
	}
	public void setHeightMin(Integer heightMin)
	{
		this.heightMin = heightMin;
	}
	public Integer getHeightMax()
	{
		return heightMax;
	}
	public void setHeightMax(Integer heightMax)
	{
		this.heightMax = heightMax;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getxPosition()
	{
		return xPosition;
	}
	public void setxPosition(Integer xPosition)
	{
		this.xPosition = xPosition;
	}
	public Integer getyPosition()
	{
		return yPosition;
	}
	public void setyPosition(Integer yPosition)
	{
		this.yPosition = yPosition;
	}
	public Integer getHeight()
	{
		return height;
	}
	public void setHeight(Integer height)
	{
		this.height = height;
	}
	public Integer getWidth()
	{
		return width;
	}
	public void setWidth(Integer width)
	{
		this.width = width;
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
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
	public OBDtoADCObject getTargetObj()
	{
		return targetObj;
	}
	public void setTargetObj(OBDtoADCObject targetObj)
	{
		this.targetObj = targetObj;
	}
}
