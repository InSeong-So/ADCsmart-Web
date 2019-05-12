package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import kr.openbase.adcsmart.service.dto.OBDtoMultiDataObj;

public class Statistic5GraphDataDto
{
	private Date 	occurTime=null;
	
	private String 	firstName="";
	private String 	secondName="";
	private String 	thirdName="";
	private String 	fourthName="";
	private String 	fifthName="";
	
	private String 	sixName ="";
	private String 	sevenName ="";
	private String 	eightName  ="";
	private String 	nineName="";
	private String 	tenName="";
	
	private String 	elevenName ="";
	private String 	twelveName="";
	private String 	thirteenName="";
	private String 	fourteenName="";
	private String 	fifteenName ="";
	
	private String 	sixteenName="";
	private String 	seventeenName ="";
	private String 	eighteenName="";
	private String 	nineteenName="";
	private String 	twentyName="";
	
	private String 	tOneName="";
	private String 	tTwoName="";
	private String 	tThreeName="";
	private String 	tFourName="";
	private String 	tFiveName ="";
	
	private String 	tSixName="";
	private String 	tSevenName="";
	private String 	tEightName="";
	private String 	tNineName="";
	private String 	thirtyName="";
	
	private String 	thrOneName="";
	private String 	thrTwoName="";	
	
	private Long	firstValue=0L; //현재 데이터.
	private Long	secondValue=0L; //현재 데이터.
	private Long	thirdValue=0L; //현재 데이터.
	private Long	fourthValue=0L; //현재 데이터.
	private Long	fifthValue=0L; //현재 데이터.
	
	private Long sixValue=0L;
	private Long sevenValue=0L;
	private Long eightValue=0L;
	private Long nineValue=0L;
	private Long tenValue=0L;
	
	private Long elevenValue=0L;
	private Long twelveValue=0L;
	private Long thirteenValue=0L;
	private Long fourteenValue=0L;
	private Long fifteenValue=0L;
	
	private Long sixteenValue=0L;
	private Long seventeenValue=0L;
	private Long eighteenValue=0L;
	private Long nineteenValue=0L;
	private Long twentyValue=0L;
	
	private Long tOneValue=0L;
	private Long tTwoValue=0L;
	private Long tThreeValue=0L;
	private Long tFourValue=0L;
	private Long tFiveValue=0L;
	
	private Long tSixValue=0L;
	private Long tSevenValue=0L;
	private Long tEightValue=0L;
	private Long tNineValue=0L;
	private Long thirtyValue=0L;
	
	private Long thrOneValue=0L;
	private Long thrTwoValue=0L;
	
	@Override
	public String toString()
	{
		return "Statistic5GraphDataDto [occurTime=" + occurTime
				+ ", firstName=" + firstName + ", secondName=" + secondName
				+ ", thirdName=" + thirdName + ", fourthName=" + fourthName
				+ ", fifthName=" + fifthName + ", sixName=" + sixName
				+ ", sevenName=" + sevenName + ", eightName=" + eightName
				+ ", nineName=" + nineName + ", tenName=" + tenName
				+ ", elevenName=" + elevenName + ", twelveName=" + twelveName
				+ ", thirteenName=" + thirteenName + ", fourteenName="
				+ fourteenName + ", fifteenName=" + fifteenName
				+ ", sixteenName=" + sixteenName + ", seventeenName="
				+ seventeenName + ", eighteenName=" + eighteenName
				+ ", nineteenName=" + nineteenName + ", twentyName="
				+ twentyName + ", tOneName=" + tOneName + ", tTwoName="
				+ tTwoName + ", tThreeName=" + tThreeName + ", tFourName="
				+ tFourName + ", tFiveName=" + tFiveName + ", tSixName="
				+ tSixName + ", tSevenName=" + tSevenName + ", tEightName="
				+ tEightName + ", tNineName=" + tNineName + ", thirtyName="
				+ thirtyName + ", thrOneName=" + thrOneName + ", thrTwoName="
				+ thrTwoName + ", firstValue=" + firstValue + ", secondValue="
				+ secondValue + ", thirdValue=" + thirdValue + ", fourthValue="
				+ fourthValue + ", fifthValue=" + fifthValue + ", sixValue="
				+ sixValue + ", sevenValue=" + sevenValue + ", eightValue="
				+ eightValue + ", nineValue=" + nineValue + ", tenValue="
				+ tenValue + ", elevenValue=" + elevenValue + ", twelveValue="
				+ twelveValue + ", thirteenValue=" + thirteenValue
				+ ", fourteenValue=" + fourteenValue + ", fifteenValue="
				+ fifteenValue + ", sixteenValue=" + sixteenValue
				+ ", seventeenValue=" + seventeenValue + ", eighteenValue="
				+ eighteenValue + ", nineteenValue=" + nineteenValue
				+ ", twentyValue=" + twentyValue + ", tOneValue=" + tOneValue
				+ ", tTwoValue=" + tTwoValue + ", tThreeValue=" + tThreeValue
				+ ", tFourValue=" + tFourValue + ", tFiveValue=" + tFiveValue
				+ ", tSixValue=" + tSixValue + ", tSevenValue=" + tSevenValue
				+ ", tEightValue=" + tEightValue + ", tNineValue=" + tNineValue
				+ ", thirtyValue=" + thirtyValue + ", thrOneValue="
				+ thrOneValue + ", thrTwoValue=" + thrTwoValue + "]";
	}
	
	public ArrayList<Statistic5GraphDataDto> toStatistic5GraphData(ArrayList<OBDtoMultiDataObj> list)
	{
		LinkedHashMap<Date, Statistic5GraphDataDto> top5Map = new LinkedHashMap<Date,Statistic5GraphDataDto>();
		OBDtoMultiDataObj item;
		
		if(list.size() <=0)
		{
			ArrayList<Statistic5GraphDataDto> retVal = new ArrayList<Statistic5GraphDataDto>();
			return retVal;
		}
		
		
		for(int i = 0; i<list.size(); i++)
		{
			item = list.get(i);
			
			if(null != item)
			{
				//ArrayList<OBDtoDataObj> connList = item.getData();
				/*for(OBDtoDataObj conn:connList)
				{
					Statistic5GraphDataDto obj = top5Map.get(conn.getOccurTime());
					if(i==0)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setFirstName(item.getName());
							obj.setFirstValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setFirstName(item.getName());
							obj.setFirstValue(conn.getValue());
						}
					}
					else if(i==1)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setSecondName(item.getName());
							obj.setSecondValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setSecondName(item.getName());
							obj.setSecondValue(conn.getValue());
						}
					}
					else if(i==2)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setThirdName(item.getName());
							obj.setThirdValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setThirdName(item.getName());
							obj.setThirdValue(conn.getValue());
						}
					}
					else if(i==3)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setFourthName(item.getName());
							obj.setFourthValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setFourthName(item.getName());
							obj.setFourthValue(conn.getValue());
						}
					}
					else if(i==4)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setFifthName(item.getName());
							obj.setFifthValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setFifthName(item.getName());
							obj.setFifthValue(conn.getValue());
						}
					}
					else if(i==5)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setSixName(item.getName());
							obj.setSixValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setSixName(item.getName());
							obj.setSixValue(conn.getValue());
						}
					}
					else if(i==6)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setSevenName(item.getName());
							obj.setSevenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setSevenName(item.getName());
							obj.setSevenValue(conn.getValue());
						}
					}
					else if(i==7)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setEightName(item.getName());
							obj.setEightValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setEightName(item.getName());
							obj.setEightValue(conn.getValue());
						}
					}
					else if(i==8)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setNineName(item.getName());
							obj.setNineValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setNineName(item.getName());
							obj.setNineValue(conn.getValue());
						}
					}
					else if(i==9)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setTenName(item.getName());
							obj.setTenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setTenName(item.getName());
							obj.setTenValue(conn.getValue());
						}
					}
					else if(i==10)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setElevenName(item.getName());
							obj.setElevenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setElevenName(item.getName());
							obj.setElevenValue(conn.getValue());
						}
					}
					else if(i==11)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setTwelveName(item.getName());
							obj.setTwelveValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setTwelveName(item.getName());
							obj.setTwelveValue(conn.getValue());
						}
					}
					else if(i==12)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setThirteenName(item.getName());
							obj.setThirteenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setThirteenName(item.getName());
							obj.setThirteenValue(conn.getValue());
						}
					}
					else if(i==13)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setFourteenName(item.getName());
							obj.setFourteenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setFourteenName(item.getName());
							obj.setFourteenValue(conn.getValue());
						}
					}
					else if(i==14)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setFifteenName(item.getName());
							obj.setFifteenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setFifteenName(item.getName());
							obj.setFifteenValue(conn.getValue());
						}
					}
					else if(i==15)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setSixteenName(item.getName());
							obj.setSixteenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setSixteenName(item.getName());
							obj.setSixteenValue(conn.getValue());
						}
					}//////////16  - Seventeen
					else if(i==16)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setSeventeenName(item.getName());
							obj.setSeventeenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setSeventeenName(item.getName());
							obj.setSeventeenValue(conn.getValue());
						}
					}
					else if(i==17)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setEighteenName(item.getName());
							obj.setEighteenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setEighteenName(item.getName());
							obj.setEighteenValue(conn.getValue());
						}
					}
					else if(i==18)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setNineteenName(item.getName());
							obj.setNineteenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setNineteenName(item.getName());
							obj.setNineteenValue(conn.getValue());
						}
					}
					else if(i==19)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setTwentyName(item.getName());
							obj.setTwentyValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setTwentyName(item.getName());
							obj.setTwentyValue(conn.getValue());
						}
					}
					else if(i==20)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settOneName(item.getName());
							obj.settOneValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settOneName(item.getName());
							obj.settOneValue(conn.getValue());
						}
					}
					else if(i==21)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settTwoName(item.getName());
							obj.settTwoValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settTwoName(item.getName());
							obj.settTwoValue(conn.getValue());
						}
					}
					else if(i==22)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settThreeName(item.getName());
							obj.settThreeValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settThreeName(item.getName());
							obj.settThreeValue(conn.getValue());
						}
					}
					else if(i==23)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settFourName(item.getName());
							obj.settFourValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settFourName(item.getName());
							obj.settFourValue(conn.getValue());
						}
					}
					else if(i==24)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settFiveName(item.getName());
							obj.settFiveValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settFiveName(item.getName());
							obj.settFiveValue(conn.getValue());
						}
					}
					else if(i==25)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settSixName(item.getName());
							obj.settSixValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settSixName(item.getName());
							obj.settSixValue(conn.getValue());
						}
					}
					else if(i==26)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settSevenName(item.getName());
							obj.settSevenValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settSevenName(item.getName());
							obj.settSevenValue(conn.getValue());
						}
					}
					else if(i==27)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settEightName(item.getName());
							obj.settEightValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settEightName(item.getName());
							obj.settEightValue(conn.getValue());
						}
					}
					else if(i==28)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.settNineName(item.getName());
							obj.settNineValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.settNineName(item.getName());
							obj.settNineValue(conn.getValue());
						}
					}
					else if(i==29)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setThirtyName(item.getName());
							obj.setThirtyValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setThirtyName(item.getName());
							obj.setThirtyValue(conn.getValue());
						}
					}
					else if(i==30)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setThrOneName(item.getName());
							obj.setThrOneValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setThrOneName(item.getName());
							obj.setThrOneValue(conn.getValue());
						}
					}
					else if(i==31)
					{
						if(obj==null)
						{
							obj = new Statistic5GraphDataDto();
							obj.setOccurTime(conn.getOccurTime());
							obj.setThrTwoName(item.getName());
							obj.setThrTwoValue(conn.getValue());
							top5Map.put(obj.getOccurTime(), obj);
						}
						else
						{
							obj.setThrTwoName(item.getName());
							obj.setThrTwoValue(conn.getValue());
						}
					}
					
				}*/
			}
		}
		ArrayList<Statistic5GraphDataDto> retVal = new ArrayList<Statistic5GraphDataDto>(top5Map.values());
		return retVal;
	}
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getSecondName()
	{
		return secondName;
	}
	public void setSecondName(String secondName)
	{
		this.secondName = secondName;
	}
	public String getThirdName()
	{
		return thirdName;
	}
	public void setThirdName(String thirdName)
	{
		this.thirdName = thirdName;
	}
	public String getFourthName()
	{
		return fourthName;
	}
	public void setFourthName(String fourthName)
	{
		this.fourthName = fourthName;
	}
	public String getFifthName()
	{
		return fifthName;
	}
	public void setFifthName(String fifthName)
	{
		this.fifthName = fifthName;
	}
	public Long getFirstValue()
	{
		return firstValue;
	}
	public void setFirstValue(Long firstValue)
	{
		this.firstValue = firstValue;
	}
	public Long getSecondValue()
	{
		return secondValue;
	}
	public void setSecondValue(Long secondValue)
	{
		this.secondValue = secondValue;
	}
	public Long getThirdValue()
	{
		return thirdValue;
	}
	public void setThirdValue(Long thirdValue)
	{
		this.thirdValue = thirdValue;
	}
	public Long getFourthValue()
	{
		return fourthValue;
	}
	public void setFourthValue(Long fourthValue)
	{
		this.fourthValue = fourthValue;
	}
	public Long getFifthValue()
	{
		return fifthValue;
	}
	public void setFifthValue(Long fifthValue)
	{
		this.fifthValue = fifthValue;
	}

	public String getSixName()
	{
		return sixName;
	}

	public void setSixName(String sixName)
	{
		this.sixName = sixName;
	}

	public String getSevenName()
	{
		return sevenName;
	}

	public void setSevenName(String sevenName)
	{
		this.sevenName = sevenName;
	}

	public String getEightName()
	{
		return eightName;
	}

	public void setEightName(String eightName)
	{
		this.eightName = eightName;
	}

	public String getNineName()
	{
		return nineName;
	}

	public void setNineName(String nineName)
	{
		this.nineName = nineName;
	}

	public String getTenName()
	{
		return tenName;
	}

	public void setTenName(String tenName)
	{
		this.tenName = tenName;
	}

	public String getElevenName()
	{
		return elevenName;
	}

	public void setElevenName(String elevenName)
	{
		this.elevenName = elevenName;
	}

	public String getTwelveName()
	{
		return twelveName;
	}

	public void setTwelveName(String twelveName)
	{
		this.twelveName = twelveName;
	}

	public String getThirteenName()
	{
		return thirteenName;
	}

	public void setThirteenName(String thirteenName)
	{
		this.thirteenName = thirteenName;
	}

	public String getFourteenName()
	{
		return fourteenName;
	}

	public void setFourteenName(String fourteenName)
	{
		this.fourteenName = fourteenName;
	}

	public String getFifteenName()
	{
		return fifteenName;
	}

	public void setFifteenName(String fifteenName)
	{
		this.fifteenName = fifteenName;
	}

	public String getSixteenName()
	{
		return sixteenName;
	}

	public void setSixteenName(String sixteenName)
	{
		this.sixteenName = sixteenName;
	}

	public String getSeventeenName()
	{
		return seventeenName;
	}

	public void setSeventeenName(String seventeenName)
	{
		this.seventeenName = seventeenName;
	}

	public String getEighteenName()
	{
		return eighteenName;
	}

	public void setEighteenName(String eighteenName)
	{
		this.eighteenName = eighteenName;
	}

	public String getNineteenName()
	{
		return nineteenName;
	}

	public void setNineteenName(String nineteenName)
	{
		this.nineteenName = nineteenName;
	}

	public String getTwentyName()
	{
		return twentyName;
	}

	public void setTwentyName(String twentyName)
	{
		this.twentyName = twentyName;
	}

	public String gettOneName()
	{
		return tOneName;
	}

	public void settOneName(String tOneName)
	{
		this.tOneName = tOneName;
	}

	public String gettTwoName()
	{
		return tTwoName;
	}

	public void settTwoName(String tTwoName)
	{
		this.tTwoName = tTwoName;
	}

	public String gettThreeName()
	{
		return tThreeName;
	}

	public void settThreeName(String tThreeName)
	{
		this.tThreeName = tThreeName;
	}

	public String gettFourName()
	{
		return tFourName;
	}

	public void settFourName(String tFourName)
	{
		this.tFourName = tFourName;
	}

	public String gettFiveName()
	{
		return tFiveName;
	}

	public void settFiveName(String tFiveName)
	{
		this.tFiveName = tFiveName;
	}

	public String gettSixName()
	{
		return tSixName;
	}

	public void settSixName(String tSixName)
	{
		this.tSixName = tSixName;
	}

	public String gettSevenName()
	{
		return tSevenName;
	}

	public void settSevenName(String tSevenName)
	{
		this.tSevenName = tSevenName;
	}

	public String gettEightName()
	{
		return tEightName;
	}

	public void settEightName(String tEightName)
	{
		this.tEightName = tEightName;
	}

	public String gettNineName()
	{
		return tNineName;
	}

	public void settNineName(String tNineName)
	{
		this.tNineName = tNineName;
	}

	public String getThirtyName()
	{
		return thirtyName;
	}

	public void setThirtyName(String thirtyName)
	{
		this.thirtyName = thirtyName;
	}

	public String getThrOneName()
	{
		return thrOneName;
	}

	public void setThrOneName(String thrOneName)
	{
		this.thrOneName = thrOneName;
	}

	public String getThrTwoName()
	{
		return thrTwoName;
	}

	public void setThrTwoName(String thrTwoName)
	{
		this.thrTwoName = thrTwoName;
	}

	public Long getSixValue()
	{
		return sixValue;
	}

	public void setSixValue(Long sixValue)
	{
		this.sixValue = sixValue;
	}

	public Long getSevenValue()
	{
		return sevenValue;
	}

	public void setSevenValue(Long sevenValue)
	{
		this.sevenValue = sevenValue;
	}

	public Long getEightValue()
	{
		return eightValue;
	}

	public void setEightValue(Long eightValue)
	{
		this.eightValue = eightValue;
	}

	public Long getNineValue()
	{
		return nineValue;
	}

	public void setNineValue(Long nineValue)
	{
		this.nineValue = nineValue;
	}

	public Long getTenValue()
	{
		return tenValue;
	}

	public void setTenValue(Long tenValue)
	{
		this.tenValue = tenValue;
	}

	public Long getElevenValue()
	{
		return elevenValue;
	}

	public void setElevenValue(Long elevenValue)
	{
		this.elevenValue = elevenValue;
	}

	public Long getTwelveValue()
	{
		return twelveValue;
	}

	public void setTwelveValue(Long twelveValue)
	{
		this.twelveValue = twelveValue;
	}

	public Long getThirteenValue()
	{
		return thirteenValue;
	}

	public void setThirteenValue(Long thirteenValue)
	{
		this.thirteenValue = thirteenValue;
	}

	public Long getFourteenValue()
	{
		return fourteenValue;
	}

	public void setFourteenValue(Long fourteenValue)
	{
		this.fourteenValue = fourteenValue;
	}

	public Long getFifteenValue()
	{
		return fifteenValue;
	}

	public void setFifteenValue(Long fifteenValue)
	{
		this.fifteenValue = fifteenValue;
	}

	public Long getSixteenValue()
	{
		return sixteenValue;
	}

	public void setSixteenValue(Long sixteenValue)
	{
		this.sixteenValue = sixteenValue;
	}

	public Long getSeventeenValue()
	{
		return seventeenValue;
	}

	public void setSeventeenValue(Long seventeenValue)
	{
		this.seventeenValue = seventeenValue;
	}

	public Long getEighteenValue()
	{
		return eighteenValue;
	}

	public void setEighteenValue(Long eighteenValue)
	{
		this.eighteenValue = eighteenValue;
	}

	public Long getNineteenValue()
	{
		return nineteenValue;
	}

	public void setNineteenValue(Long nineteenValue)
	{
		this.nineteenValue = nineteenValue;
	}

	public Long getTwentyValue()
	{
		return twentyValue;
	}

	public void setTwentyValue(Long twentyValue)
	{
		this.twentyValue = twentyValue;
	}

	public Long gettOneValue()
	{
		return tOneValue;
	}

	public void settOneValue(Long tOneValue)
	{
		this.tOneValue = tOneValue;
	}

	public Long gettTwoValue()
	{
		return tTwoValue;
	}

	public void settTwoValue(Long tTwoValue)
	{
		this.tTwoValue = tTwoValue;
	}

	public Long gettThreeValue()
	{
		return tThreeValue;
	}

	public void settThreeValue(Long tThreeValue)
	{
		this.tThreeValue = tThreeValue;
	}

	public Long gettFourValue()
	{
		return tFourValue;
	}

	public void settFourValue(Long tFourValue)
	{
		this.tFourValue = tFourValue;
	}

	public Long gettFiveValue()
	{
		return tFiveValue;
	}

	public void settFiveValue(Long tFiveValue)
	{
		this.tFiveValue = tFiveValue;
	}

	public Long gettSixValue()
	{
		return tSixValue;
	}

	public void settSixValue(Long tSixValue)
	{
		this.tSixValue = tSixValue;
	}

	public Long gettSevenValue()
	{
		return tSevenValue;
	}

	public void settSevenValue(Long tSevenValue)
	{
		this.tSevenValue = tSevenValue;
	}

	public Long gettEightValue()
	{
		return tEightValue;
	}

	public void settEightValue(Long tEightValue)
	{
		this.tEightValue = tEightValue;
	}

	public Long gettNineValue()
	{
		return tNineValue;
	}

	public void settNineValue(Long tNineValue)
	{
		this.tNineValue = tNineValue;
	}

	public Long getThirtyValue()
	{
		return thirtyValue;
	}

	public void setThirtyValue(Long thirtyValue)
	{
		this.thirtyValue = thirtyValue;
	}

	public Long getThrOneValue()
	{
		return thrOneValue;
	}

	public void setThrOneValue(Long thrOneValue)
	{
		this.thrOneValue = thrOneValue;
	}

	public Long getThrTwoValue()
	{
		return thrTwoValue;
	}

	public void setThrTwoValue(Long thrTwoValue)
	{
		this.thrTwoValue = thrTwoValue;
	}
	
	
	
	
}