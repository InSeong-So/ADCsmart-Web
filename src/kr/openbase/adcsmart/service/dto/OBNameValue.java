package kr.openbase.adcsmart.service.dto;

public class OBNameValue
{
    private String name;
    private String value;
    
    @Override
    public String toString()
    {
        return "OBNameValue [name=" + name + ", value=" + value + "]";
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
}
