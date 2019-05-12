var MonitorAppliance = Class.create({
	initialize : function()
	{
		this.adc;
		this.categoryIndex;		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;		
		
		this.selectedChartTapMode = undefined;
		this.intervalMonitor = undefined;
		
		this.monitorContentEventsInit = false;
	},
	onAdcChange : function()
	{
		with(this)
		{
			this.monitorContentEventsInit = false;
			loadApplianceMonitorContent();
		}
	},		
	loadContent : function (activeAdcInfo)
	{
		// 최초 접근시 인터페이스 차트 접근 금지
		if (this.selectedChartTapMode == "interfaceChartTap")
			this.selectedChartTapMode = "interfaceListTap";
		with(this)
		{
			adc = activeAdcInfo;
			categoryIndex = 2;

			if (!adc || adc.categoryIndex!=2)
			{// adc가 선택되어 있지 않으면 작업을 수행하지 않는다.
				return;
			}
			
			var params = 
			{
				"adcObject.index" 		: adc.index,					
				"adcObject.category"	: categoryIndex
			};			
			ajaxManager.runHtmlExt({
				url : "monitor/loadApplianceMonitorContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{
//					header.setActiveMenu('MonitorAppliance');	
					loadApplianceMapContentNew();
					loadApplianceChartContent();
//					if (adc.adcStatus === "available") 
//					{
//						loadApplianceChartContent();
//					}					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
				}
			});
		}
	},
	loadApplianceMonitorContent : function ()
	{
		this.monitorContentEventsInit = false;
		this.adc = adcSetting.getAdc();
		this.categoryIndex = 2;
		// 최초 접근시 인터페이스 차트 접근 금지
		if (this.selectedChartTapMode == "interfaceChartTap")
			this.selectedChartTapMode = "interfaceListTap";
						
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params = 
			{
				"adcObject.index" 		: adc.index,					
				"adcObject.category"	: categoryIndex
			};			
			ajaxManager.runHtmlExt({
				url : "monitor/loadApplianceMonitorContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{
					header.setActiveMenu('MonitorAppliance');	
					loadApplianceMapContent();
					loadApplianceChartContent();
//					if (adcSetting.getAdcStatus() == "available") 
//					{
//						loadApplianceChartContent();
//					}					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
				}
			});
		}
	},
	loadApplianceMapContent : function ()
	{
		with(this)
		{
			var AdcModelNum = $('.AdcModelNum').val();			

			if (adcSetting.getAdcStatus() != "available")
			{
				if (adc.type == "Alteon")
				{	
					if ((AdcModelNum == 2208) || (AdcModelNum == 2216) || (AdcModelNum == 2424) || (AdcModelNum == 3408) || (AdcModelNum == 4408))
					{					
						$adcModelSpec="alteon";
					}
					else if ((AdcModelNum == 4024) || (AdcModelNum == 4416) || (AdcModelNum == 5224) || (AdcModelNum == 5412) || (AdcModelNum == 6420) || (AdcModelNum == 6421))
					{
						$adcModelSpec="alteon";
					}
					else
					{
						$adcModelSpec="alteon_basic";
					}
				}
				else if (adc.type == "F5")
				{	
					if ((AdcModelNum == 1600) 
							|| (AdcModelNum == 2000) || (AdcModelNum == 2200) || (AdcModelNum == 2600) || (AdcModelNum == 2800) 
							|| (AdcModelNum == 3600) || (AdcModelNum == 3900) 
							|| (AdcModelNum == 4000) || (AdcModelNum == 4200) || (AdcModelNum == 4600) || (AdcModelNum == 4800) 
							|| (AdcModelNum == 5000) || (AdcModelNum == 5200) || (AdcModelNum == 5600) || (AdcModelNum == 5800)
							|| (AdcModelNum == 6800) || (AdcModelNum == 6400) || (AdcModelNum == 6900) 
							|| (AdcModelNum == 7000) || (AdcModelNum == 7200) || (AdcModelNum == 7600) || (AdcModelNum == 7800)
							|| (AdcModelNum == 8400) || (AdcModelNum == 8900) 
							|| (AdcModelNum == 10000) || (AdcModelNum == 10200) || (AdcModelNum == 10600) || (AdcModelNum == 10800))
					{					
						$adcModelSpec="F5_BIG_IP";
					}
					else if (AdcModelNum == 2400)
					{
						$adcModelSpec="F5_VIPRION";
					}
					else
					{
						$adcModelSpec="F5_basic";
					}		
				}
				else
				{
					$adcModelSpec = "";
					AdcModelNum = 0;
				}
								
				if (AdcModelNum == -1)
				{
					model = "";
				}
				else if ((AdcModelNum == 1600) 
						|| (AdcModelNum == 2000) || (AdcModelNum == 2200) 
						|| (AdcModelNum == 3600) || (AdcModelNum == 3900) 
						|| (AdcModelNum == 4000) || (AdcModelNum == 4200)
						|| (AdcModelNum == 5000) || (AdcModelNum == 5200) 
						|| (AdcModelNum == 6400) || (AdcModelNum == 6800) || (AdcModelNum == 6900) 
						|| (AdcModelNum == 7000) || (AdcModelNum == 7200) 
						|| (AdcModelNum == 8400) || (AdcModelNum == 8900) 
						|| (AdcModelNum == 10000) || (AdcModelNum == 10200) 						
						|| (AdcModelNum == 2400) || (AdcModelNum == 2208) || (AdcModelNum == 2216) || (AdcModelNum == 2424) || (AdcModelNum == 3408) 
						|| (AdcModelNum == 4408) || (AdcModelNum == 4024) || (AdcModelNum == 4416) || (AdcModelNum == 5224) || (AdcModelNum == 5412)
						|| (AdcModelNum == 6420) || (AdcModelNum == 6024) || (AdcModelNum == 5208))
				{
					model = "_" + AdcModelNum;
				}
				else if((AdcModelNum == 2600) || (AdcModelNum == 2800)
						|| (AdcModelNum == 4600) || (AdcModelNum == 4800) 
						|| (AdcModelNum == 5600) || (AdcModelNum == 5800) 
						|| (AdcModelNum == 7600) || (AdcModelNum == 7800) 
						|| (AdcModelNum == 10600) || (AdcModelNum == 10800)){

					model = "_i"+AdcModelNum;
				}
				else if (AdcModelNum == 6421)
				{
					model = "_6420p";
				}
				else
				{
					model = "";
				}
				
				if (AdcModelNum == -1)
				{					
					if(langCode=="ko_KR")
					{
						img_dest = "url(imgs/monitoring/device/" + $adcModelSpec + model + "_null.gif)";
					}
					else
					{
						img_dest = "url(imgs/monitoring_eng/device/" + $adcModelSpec + model + "_null.gif)";
					}
					
					$('.contents_area .nulldata').css('background-image', img_dest);
					$('.nulldata').removeClass("none");		
					$('.offdata').addClass("none");
					$('.disabledChk').addClass("none");
				}
				else if (AdcModelNum == 0) // PAS, PASK
				{					
					$('.nulldata').addClass("none");		
					$('.offdata').addClass("none");
					$('.disabledChk').removeClass("none");
				}
				else
				{
					img_dest = "url(imgs/monitoring/device/" + $adcModelSpec + model + "_off.gif)";
					$('.contents_area .offdata').css('background-image', img_dest);
					$('.nulldata').addClass("none");		
					$('.offdata').removeClass("none");
				}
				
//				$('.disabledChk').addClass("none");
//				$('.nulldata').removeClass("none");		
//				$('.offdata').removeClass("none");
					
			}
			else
			{
				$('.disabledChk').removeClass("none");
				$('.nulldata').addClass("none");
				$('.offdata').addClass("none");
					
				var applianceMapURL= "";
				if (adc.type == "Alteon")
				{
					if (AdcModelNum == 2208)
					{
						applianceMapURL = "monitor/Alteon2208Content.action";
					}
					else if (AdcModelNum == 2216)
					{
						applianceMapURL = "monitor/Alteon2216Content.action";
					}
					else if (AdcModelNum == 2424)
					{
						applianceMapURL = "monitor/Alteon2424Content.action";
					}
					else if (AdcModelNum == 3408)
					{
						applianceMapURL = "monitor/Alteon3408Content.action";
					}
					else if (AdcModelNum == 4408)
					{
						applianceMapURL = "monitor/Alteon4408Content.action";
					}
					else if (AdcModelNum == 4024)
					{
						applianceMapURL = "monitor/Alteon4024Content.action";
					}
					else if (AdcModelNum == 4416)
					{
						applianceMapURL = "monitor/Alteon4416Content.action";
					}
					else if (AdcModelNum == 5208)
					{
						applianceMapURL = "monitor/Alteon5208Content.action";
					}
					else if (AdcModelNum == 5224)
					{
						applianceMapURL = "monitor/Alteon5224Content.action";
					}
					else if (AdcModelNum == 5412)
					{
						applianceMapURL = "monitor/Alteon5412Content.action";
					}
					else if (AdcModelNum == 6420 || (AdcModelNum == 6421))
					{
						applianceMapURL = "monitor/Alteon6420Content.action";
					}
					else if (AdcModelNum == 6024)
					{
						applianceMapURL = "monitor/Alteon6024Content.action";
					}
					else
					{
						applianceMapURL = "monitor/AlteonBasicContent.action";
					}
				}
				else if (adc.type == "F5")
				{
					if (AdcModelNum == 1600)
					{
						applianceMapURL = "monitor/F5_1600Content.action";
					}
					else if ((AdcModelNum == 2000) || (AdcModelNum == 2200))
					{
						applianceMapURL = "monitor/F5_2000Content.action";
					}
					else if (AdcModelNum == 2400)
					{
						applianceMapURL = "monitor/F5_2400Content.action";
					}
					else if (AdcModelNum == 2600)
					{
						applianceMapURL = "monitor/F5_2600Content.action";
					}
					else if (AdcModelNum == 2800)
					{
						applianceMapURL = "monitor/F5_2800Content.action";
					}
					else if (AdcModelNum == 3600)
					{
						applianceMapURL = "monitor/F5_3600Content.action";
					}
					else if (AdcModelNum == 3900)
					{
						applianceMapURL = "monitor/F5_3900Content.action";
					}
					else if ((AdcModelNum == 4000) || (AdcModelNum == 4200))
					{
						applianceMapURL = "monitor/F5_4000Content.action";
					}
					else if (AdcModelNum == 4600)
					{
						applianceMapURL = "monitor/F5_4600Content.action";
					}
					else if (AdcModelNum == 4800)
					{
						applianceMapURL = "monitor/F5_4800Content.action";
					}
					else if (AdcModelNum == 5600)
					{
						applianceMapURL = "monitor/F5_5600Content.action";
					}
					else if (AdcModelNum == 5800)
					{
						applianceMapURL = "monitor/F5_5800Content.action";
					}
					else if (AdcModelNum == 6400)
					{
						applianceMapURL = "monitor/F5_6400Content.action";
					}
					else if (AdcModelNum == 6800)
					{
						applianceMapURL = "monitor/F5_6800Content.action";
					}
					else if (AdcModelNum == 6900)
					{
						applianceMapURL = "monitor/F5_6900Content.action";
					}
					else if (AdcModelNum == 7600)
					{
						applianceMapURL = "monitor/F5_7600Content.action";
					}
					else if (AdcModelNum == 7800)
					{
						applianceMapURL = "monitor/F5_7800Content.action";
					}
					else if ((AdcModelNum == 5000) || (AdcModelNum == 5200) || (AdcModelNum == 7000) || (AdcModelNum == 7200))
					{
						applianceMapURL = "monitor/F5_7000Content.action";
					}
					else if (AdcModelNum == 8400)
					{
						applianceMapURL = "monitor/F5_8400Content.action";
					}
					else if (AdcModelNum == 8900)
					{
						applianceMapURL = "monitor/F5_8900Content.action";
					}
					else if ((AdcModelNum == 10000) || (AdcModelNum == 10200))
					{
						applianceMapURL = "monitor/F5_10000Content.action";
					}
					else if (AdcModelNum == 10600)
					{
						applianceMapURL = "monitor/F5_10600Content.action";
					}
					else if (AdcModelNum == 10800)
					{
						applianceMapURL = "monitor/F5_10800Content.action";
					}
					else
					{
						applianceMapURL = "monitor/F5_BasicContent.action";
					}
				}
				var params = 
				{
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex
				};			
				ajaxManager.runHtmlExt({
					url : applianceMapURL,
					data : params,
					target : "div.AdcModelArea",				
					successFn : function(data)
					{
						// 장비모니터링 OS Version Insert 부분
						var versionArea = $('.AdcModelArea .version').filter(':last');
						var versionData = $('.AdcswVersion').val();
						versionArea.empty();
						if (versionData == "" || versionData == null)
						{
							versionArea.append("&nbsp;");
						}
						else
						{
							versionArea.append(versionData);
						}							
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
					}
				});			
			}
		}
	},
	loadApplianceMapContentNew : function ()
	{
		with(this)
		{
			var AdcModelNum = $('.AdcModelNum').val();			

			if (adc.adcStatus !== "available")
			{
				if (adc.type === "Alteon")
				{	
					if ((AdcModelNum == 2208) || (AdcModelNum == 2216) || (AdcModelNum == 2424) || (AdcModelNum == 3408) || (AdcModelNum == 4408))
					{					
						$adcModelSpec="alteon";
					}
					else if ((AdcModelNum == 4024) || (AdcModelNum == 4416) || (AdcModelNum == 5208) || (AdcModelNum == 5224) || (AdcModelNum == 5412) || (AdcModelNum == 6420) || (AdcModelNum == 6421) || (AdcModelNum == 6024))
					{
						$adcModelSpec="alteon";
					}
					else
					{
						$adcModelSpec="alteon_basic";
					}
				}
				else if (adc.type === "F5")
				{	
					if ((AdcModelNum == 1600) 
							|| (AdcModelNum == 2000) || (AdcModelNum == 2200) || (AdcModelNum == 2600) || (AdcModelNum == 2800)
							|| (AdcModelNum == 3600) || (AdcModelNum == 3900) 
							|| (AdcModelNum == 4000) || (AdcModelNum == 4200) || (AdcModelNum == 4600) || (AdcModelNum == 4800) 
							|| (AdcModelNum == 5000) || (AdcModelNum == 5200) || (AdcModelNum == 5600) || (AdcModelNum == 5800) 
							|| (AdcModelNum == 6800) || (AdcModelNum == 6400) || (AdcModelNum == 6900) 
							|| (AdcModelNum == 7000) || (AdcModelNum == 7200) || (AdcModelNum == 7600) || (AdcModelNum == 7800) 
							|| (AdcModelNum == 8400) || (AdcModelNum == 8900) 
							|| (AdcModelNum == 10000) || (AdcModelNum == 10200) || (AdcModelNum == 10600) || (AdcModelNum == 10800) )
					{					
						$adcModelSpec="F5_BIG_IP";
					}
					else if (AdcModelNum == 2400)
					{
						$adcModelSpec="F5_VIPRION";
					}
					else
					{
						$adcModelSpec="F5_basic";
					}		
				}
				
				if (AdcModelNum == -1)
				{
					model = "";
				}
				else if ((AdcModelNum == 1600) 
						|| (AdcModelNum == 2000) || (AdcModelNum == 2200) 
						|| (AdcModelNum == 3600) || (AdcModelNum == 3900) 
						|| (AdcModelNum == 4000) || (AdcModelNum == 4200)  
						|| (AdcModelNum == 5000) || (AdcModelNum == 5200) 
						|| (AdcModelNum == 6400) || (AdcModelNum == 6800) || (AdcModelNum == 6900) 
						|| (AdcModelNum == 7000) || (AdcModelNum == 7200) 
						|| (AdcModelNum == 8400) || (AdcModelNum == 8900) 
						|| (AdcModelNum == 10000) || (AdcModelNum == 10200)
						|| (AdcModelNum == 2400) || (AdcModelNum == 2208) || (AdcModelNum == 2216) || (AdcModelNum == 2424) || (AdcModelNum == 3408) 
						|| (AdcModelNum == 4408) || (AdcModelNum == 4024) || (AdcModelNum == 4416) || (AdcModelNum == 5208) || (AdcModelNum == 5224) || (AdcModelNum == 5412)
						|| (AdcModelNum == 6420) || (AdcModelNum == 6024))
				{				
					model = "_" + AdcModelNum;
				}
				else if((AdcModelNum == 2600) || (AdcModelNum == 2800)
						|| (AdcModelNum == 4600) || (AdcModelNum == 4800) 
						|| (AdcModelNum == 5600) || (AdcModelNum == 5800) 
						|| (AdcModelNum == 7600) || (AdcModelNum == 7800) 
						|| (AdcModelNum == 10600) || (AdcModelNum == 10800)){

					model = "_i"+AdcModelNum;
				}
				else if (AdcModelNum == 6421)
				{				
					model = "_6420p";
				}
				else
				{
					model = "";
				}
				
				if (AdcModelNum == -1)
				{
					if(langCode=="ko_KR")
					{
						img_dest = "url(imgs/monitoring/device/" + $adcModelSpec + model + "_null.gif)";
					}
					else
					{
						img_dest = "url(imgs/monitoring_eng/device/" + $adcModelSpec + model + "_null.gif)";
					}				
					
					$('.contents_area .nulldata').css('background-image', img_dest);
					$('.nulldata').removeClass("none");		
					$('.offdata').addClass("none");
					$('.disabledChk').addClass("none");
				}
				else
				{
					img_dest = "url(imgs/monitoring/device/" + $adcModelSpec + model + "_off.gif)";
					$('.contents_area .offdata').css('background-image', img_dest);
					$('.nulldata').addClass("none");		
					$('.offdata').removeClass("none");
				}
			}
			else
			{
				$('.disabledChk').removeClass("none");
				$('.nulldata').addClass("none");
				$('.offdata').addClass("none");
					
				var applianceMapURL= "";
				if (adc.type == "Alteon")
				{
					if (AdcModelNum == 2208)
					{
						applianceMapURL = "monitor/Alteon2208Content.action";
					}
					else if (AdcModelNum == 2216)
					{
						applianceMapURL = "monitor/Alteon2216Content.action";
					}
					else if (AdcModelNum == 2424)
					{
						applianceMapURL = "monitor/Alteon2424Content.action";
					}
					else if (AdcModelNum == 3408)
					{
						applianceMapURL = "monitor/Alteon3408Content.action";
					}
					else if (AdcModelNum == 4408)
					{
						applianceMapURL = "monitor/Alteon4408Content.action";
					}
					else if (AdcModelNum == 4024)
					{
						applianceMapURL = "monitor/Alteon4024Content.action";
					}
					else if (AdcModelNum == 4416)
					{
						applianceMapURL = "monitor/Alteon4416Content.action";
					}
					else if (AdcModelNum == 5208)
					{
						applianceMapURL = "monitor/Alteon5208Content.action";
					}
					else if (AdcModelNum == 5224)
					{
						applianceMapURL = "monitor/Alteon5224Content.action";
					}
					else if (AdcModelNum == 5412)
					{
						applianceMapURL = "monitor/Alteon5412Content.action";
					}
					else if (AdcModelNum == 6420 || (AdcModelNum == 6421))
					{
						applianceMapURL = "monitor/Alteon6420Content.action";
					}
					else if (AdcModelNum == 6024)
					{
						applianceMapURL = "monitor/Alteon6024Content.action";
					}
					else
					{
						applianceMapURL = "monitor/AlteonBasicContent.action";
					}
				}
				else if (adc.type == "F5")
				{
					if (AdcModelNum == 1600)
					{
						applianceMapURL = "monitor/F5_1600Content.action";
					}
					else if ((AdcModelNum == 2000) || (AdcModelNum == 2200))
					{
						applianceMapURL = "monitor/F5_2000Content.action";
					}
					else if (AdcModelNum == 2400)
					{
						applianceMapURL = "monitor/F5_2400Content.action";
					}
					else if (AdcModelNum == 2600)
					{
						applianceMapURL = "monitor/F5_2600Content.action";
					}
					else if (AdcModelNum == 2800)
					{
						applianceMapURL = "monitor/F5_2800Content.action";
					}
					else if (AdcModelNum == 3600)
					{
						applianceMapURL = "monitor/F5_3600Content.action";
					}
					else if (AdcModelNum == 3900)
					{
						applianceMapURL = "monitor/F5_3900Content.action";
					}
					else if ((AdcModelNum == 4000) || (AdcModelNum == 4200))
					{
						applianceMapURL = "monitor/F5_4000Content.action";
					}
					else if (AdcModelNum == 4600)
					{
						applianceMapURL = "monitor/F5_4600Content.action";
					}
					else if (AdcModelNum == 4800)
					{
						applianceMapURL = "monitor/F5_4800Content.action";
					}
					else if (AdcModelNum == 5600)
					{
						applianceMapURL = "monitor/F5_5600Content.action";
					}
					else if (AdcModelNum == 5800)
					{
						applianceMapURL = "monitor/F5_5800Content.action";
					}
					else if (AdcModelNum == 6800)
					{
						applianceMapURL = "monitor/F5_6800Content.action";
					}
					else if (AdcModelNum == 6900)
					{
						applianceMapURL = "monitor/F5_6900Content.action";
					}
					else if ((AdcModelNum == 5000) || (AdcModelNum == 5200) || (AdcModelNum == 7000) || (AdcModelNum == 7200))
					{
						applianceMapURL = "monitor/F5_7000Content.action";
					}
					else if (AdcModelNum == 7600)
					{
						applianceMapURL = "monitor/F5_7600Content.action";
					}
					else if (AdcModelNum == 7800)
					{
						applianceMapURL = "monitor/F5_7800Content.action";
					}
					else if (AdcModelNum == 8400)
					{
						applianceMapURL = "monitor/F5_8400Content.action";
					}
					else if (AdcModelNum == 8900)
					{
						applianceMapURL = "monitor/F5_8900Content.action";
					}
					else if ((AdcModelNum == 10000) || (AdcModelNum == 10200))
					{
						applianceMapURL = "monitor/F5_10000Content.action";
					}
					else if (AdcModelNum == 10600)
					{
						applianceMapURL = "monitor/F5_10600Content.action";
					}
					else if (AdcModelNum == 10800)
					{
						applianceMapURL = "monitor/F5_10800Content.action";
					}
					else
					{
						applianceMapURL = "monitor/F5_BasicContent.action";
					}
				}
				var params = 
				{
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex
				};			
				ajaxManager.runHtmlExt({
					url : applianceMapURL,
					data : params,
					target : "div.AdcModelArea",				
					successFn : function(data)
					{
						// 장비모니터링 OS Version Insert 부분
						var versionArea = $('.AdcModelArea .version').filter(':last');
						versionArea.empty();
						versionArea.append($('.AdcswVersion').val());	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
					}
				});			
			}
		}
	},
	//Chart Area Ajax runHtml , allChartTap , detailChartTap
//	loadApplianceChartContent : function(flag, spnum)
	loadApplianceChartContent : function(spnum, intervalMonitor)
	{
		with(this)
		{			
			if (!selectedChartTapMode)
			{
				selectedChartTapMode = "allChartTap";
			}
			
			if (adc.type == "Alteon" && selectedChartTapMode == "sslConnChartTap")
			{
				selectedChartTapMode = "allChartTap";
			}
			else if (adc.type == "Alteon" && selectedChartTapMode == "httpChartTap")
			{
				selectedChartTapMode = "allChartTap";
			}
			else
			{
			}
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadIntervalInfo.action",
				data		: {},
				successFn	: function(data)
				{	
					intervalMonitor = data.monitoringPeriod;
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
//			console.log("intervalMonitor0 : ", intervalMonitor);
			if (selectedChartTapMode == "allChartTap")
			{				
				ajaxManager.runHtmlExt({
					url : "monitor/loadApplianceAllChartContent.action",				
					target : "div.ApplianceChartArea",				
					successFn : function(data)
					{			
//						intervalMonitor = data.intervalMonitor;
//						console.log("intervalMonitor1 : ", intervalMonitor);
						registApplianceMonitoContentEvents();
						restoreApplianceMonitoContent();
						loadSessionHistoryInfo(intervalMonitor);
						loadBpsHistoryInfo(intervalMonitor);
						loadCpuHistroyInfo(intervalMonitor);
						loadMemHistoryInfo(intervalMonitor);
						loadPktErrHistoryInfo(intervalMonitor);
						loadPktDropHistoryInfo(intervalMonitor);
						if(adc.type == "F5")
						{
							loadSSLTransactionHistoryInfo(intervalMonitor);
							loadHTTPRequestHistoryInfo(intervalMonitor);
						}
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
					}
				});
			}
			else if (selectedChartTapMode == "interfaceListTap")
			{
				loadInterfaceList();
			}
			else if (selectedChartTapMode == "interfaceChartTap")
			{
				loadInterfaceChart();
			}
			else
			{
//				console.log("intervalMonitor2 : ", intervalMonitor);
				ajaxManager.runHtmlExt({
					url : "monitor/loadApplianceDetailChartContent.action",				
					target : "div.ApplianceChartArea",				
					successFn : function(data)
					{
						registApplianceMonitoContentEvents();
						restoreApplianceMonitoContent();
						switch(selectedChartTapMode)
						{
							case "csChartTap" :
								loadDetailCSInfo(intervalMonitor);
								break;
							case "throughputChartTap" :
								loadDetailThroughputInfo(intervalMonitor);
								break;
							case "sslConnChartTap" :
								loadDetailSSLInfo(intervalMonitor);
								break;
							case "httpChartTap" :
								loadDetailHTTPInfo(intervalMonitor);
								break;
							case "cpuChartTap" :
								if(adc.type == "Alteon")
								{
									if(spnum > 0)
									{
//										console.log("intervalMonitor3 : ", intervalMonitor);
										loadDetailSPSessionInfo(spnum, intervalMonitor);
									}
									else
									{
										loadDetailCPUInfo(intervalMonitor);
									}
								}
								else
								{
									loadDetailCPUInfo(intervalMonitor);
								}
								$('#detailChart').css({'width' : '100%' , 'height': '330px'});								
								break;
							case "memoryChartTap" :
								loadDetailMemoryInfo(intervalMonitor);
								break;
							case "errpChartTap" :
								loadDetailPktErrInfo(intervalMonitor);
								break;
							case "dropChartTap" :
								loadDetailPktDropInfo(intervalMonitor);
								break;
						}
						
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
					}
				});				
			}			
		}		
	},
	registApplianceMonitoContentEvents : function()
	{
		with(this)
		{
			if(monitorContentEventsInit){
				return;
			}else{
				monitorContentEventsInit = true;
			}
			
			$('#chartAreaTap_All').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});					
				
				selectedChartTapMode = "allChartTap";				
				loadApplianceChartContent();
			});
			$('#chartAreaTap_CS').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});				
				
				selectedChartTapMode = "csChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_Throughput').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});				
				
				selectedChartTapMode = "throughputChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_SSL').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});
				
				selectedChartTapMode = "sslConnChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_HTTP').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});				
				
				selectedChartTapMode = "httpChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_CPU').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});				
				
				selectedChartTapMode = "cpuChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_Memory').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});				
				
				selectedChartTapMode = "memoryChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_ErrP').click(function() 
			{	
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});					
				
				selectedChartTapMode = "errpChartTap";
				loadApplianceChartContent();
			});
			$('#chartAreaTap_DropP').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});					
				
				selectedChartTapMode = "dropChartTap";
				loadApplianceChartContent();
			});
			// 인터페이스 모니터링 ADC모니터링 탭으로 이전
			$('#chartAreaTap_interface').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});					
				
				selectedChartTapMode = "interfaceListTap";
				loadApplianceChartContent();
			});
			
			$('.apCursorTap').hover(function()
			{
				if ($(this).css('background-color') != 'rgb(102, 102, 102)')
				{
					$(this).css('background-color','#E8E8E8');
				}
			},function()
			{
				if ($(this).css('background-color') != 'rgb(102, 102, 102)')
				{	
					$(this).css('background-color','#fff');	
				}
							
			});			
			
//			$('#refresh').click(function(event)
//			{
//				with(this)
//				{										
//					event.preventDefault();					
//					var spNumVal = $('.spNum').text();
////					loadApplianceChartContent(true, spNumVal);
//					loadApplianceChartContent(spNumVal);
//					loadApplianceMapContent();
//				}				
//			});		
//			
//			$('.spCPUSessionMain').click(function(e)
//			{
//				e.preventDefault();
//				loadDetailCPUInfo(intervalMonitor);
//			});
//			
//			$('.exportCssLnk').click(function()
//			{
//				event.preventDefault();					
//				_checkExportDataExist(selectedChartTapMode);
//			});
			
			// 인터페이스 모니터링 전용 이벤트
//			if (selectedChartTapMode == "interfaceChartTap")
//			{
//				$.each(allInterfaceNames, function(index, value)
//				{
//					$('#interfaceNamePicker').append('<option value="' + index + '">' + value + '</option>');
//				});
//				
//				$('#interfaceNamePicker option[value="' + interfaceNameList + '"]').attr('selected', 'selected');
//				$('#interfaceTypePicker option[value="' + selectedTraffic + '"]').attr('selected', 'selected');
//				
//				$('#interfaceNamePicker').change(function()
//				{
//					loadInterfaceChart($(this).val(), $('#interfaceTypePicker').val());
//				});
//				
//				$('#interfaceTypePicker').change(function()
//				{
//					loadInterfaceChart($('#interfaceNamePicker').val(), $(this).val());
//				});
//				
//				$('#exportCssLnk').click(function()
//				{
//					event.preventDefault();					
//					_checkExportStatisticsDataExist();
//				});
//				
//				$('#interfaceListBtn').click(function()
//				{
//					selectedChartTapMode = "interfaceListTap";
//					loadApplianceChartContent();
//				});
//			}
//			else if (selectedChartTapMode == "interfaceListTap")
//			{
//				$('.orderDir_Desc').click(function(e)
//				{
//					e.preventDefault();
//					orderDir =  $(this).find('.orderDir').text();
//					orderType =  $(this).find('.orderType').text();						
//					loadInterfaceList(orderType, orderDir);
//				});			
//				
//				$('.orderDir_Asc').click(function(e)
//				{
//					e.preventDefault();
//					orderDir =  $(this).find('.orderDir').text();
//					orderType =  $(this).find('.orderType').text();	
//					loadInterfaceList(orderType, orderDir);
//				});			
//				
//				$('.orderDir_None').click(function(e)
//				{
//					e.preventDefault();
//					orderDir =  $(this).find('.orderDir').text();
//					orderType =  $(this).find('.orderType').text();	
//					loadInterfaceList(orderType, orderDir);
//				});
//				
//				$('.bbyteTot').click(function()
//				{
//					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Bytes');
//				});
//				
//				$('.ppktsTot').click(function()
//				{
//					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Packets');
//				});
//				
//				$('.eerrorsTot').click(function()
//				{
//					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Errors');
//				});
//
//				$('.ddropsTot').click(function()
//				{
//					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Drops');
//				});
//			}
		}		
	},
	TapClear : function()
	{
		with(this)
		{
			$('#chartAreaTap_All').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_CS').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_Throughput').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_interface').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_SSL').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_HTTP').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_CPU').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_Memory').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_ErrP').css({'background-color': '#fff', 'color': 'black'});
			$('#chartAreaTap_DropP').css({'background-color': '#fff', 'color': 'black'});		
		}
	},
	restoreApplianceMonitoContent : function()
	{
		with(this)
		{
			
			$('#refresh').click(function(event)
			{
				with(this)
				{										
					event.preventDefault();					
					var spNumVal = $('.spNum').text();
//							loadApplianceChartContent(true, spNumVal);
					loadApplianceChartContent(spNumVal);
					loadApplianceMapContent();
				}				
			});		
			
			$('.spCPUSessionMain').click(function(e)
			{
				e.preventDefault();
				loadDetailCPUInfo(intervalMonitor);
			});
			
			$('.exportCssLnk').click(function()
			{
				event.preventDefault();					
				_checkExportDataExist(selectedChartTapMode);
			});
			
			// 인터페이스 모니터링 전용 이벤트
			if (selectedChartTapMode == "interfaceChartTap")
			{
				$.each(allInterfaceNames, function(index, value)
				{
					$('#interfaceNamePicker').append('<option value="' + index + '">' + value + '</option>');
				});
				
				$('#interfaceNamePicker option[value="' + interfaceNameList + '"]').attr('selected', 'selected');
				$('#interfaceTypePicker option[value="' + selectedTraffic + '"]').attr('selected', 'selected');
				
				$('#interfaceNamePicker').change(function()
				{
					loadInterfaceChart($(this).val(), $('#interfaceTypePicker').val());
				});
				
				$('#interfaceTypePicker').change(function()
				{
					loadInterfaceChart($('#interfaceNamePicker').val(), $(this).val());
				});
				
				$('#exportCssLnk').click(function()
				{
					event.preventDefault();					
					_checkExportStatisticsDataExist();
				});
				
				$('#interfaceListBtn').click(function()
				{
					selectedChartTapMode = "interfaceListTap";
					loadApplianceChartContent();
				});
			}
			else if (selectedChartTapMode == "interfaceListTap")
			{
				$('.orderDir_Desc').click(function(e)
				{
					e.preventDefault();
					orderDir =  $(this).find('.orderDir').text();
					orderType =  $(this).find('.orderType').text();						
					loadInterfaceList(orderType, orderDir);
				});			
				
				$('.orderDir_Asc').click(function(e)
				{
					e.preventDefault();
					orderDir =  $(this).find('.orderDir').text();
					orderType =  $(this).find('.orderType').text();	
					loadInterfaceList(orderType, orderDir);
				});			
				
				$('.orderDir_None').click(function(e)
				{
					e.preventDefault();
					orderDir =  $(this).find('.orderDir').text();
					orderType =  $(this).find('.orderType').text();	
					loadInterfaceList(orderType, orderDir);
				});
				
				$('.bbyteTot').click(function()
				{
					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Bytes');
				});
				
				$('.ppktsTot').click(function()
				{
					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Packets');
				});
				
				$('.eerrorsTot').click(function()
				{
					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Errors');
				});

				$('.ddropsTot').click(function()
				{
					loadInterfaceChart([$(this).parents('tr').data('intfname') + ''], 'Drops');
				});
			}
			
			if (!selectedChartTapMode)
			{
				selectedChartTapMode = "allChartTap";
			}
			else if(selectedChartTapMode == "csChartTap")
			{					
				TapClear();
				$('#chartAreaTap_CS').css({'background-color' : '#666666','color': '#fff'});				
			}
			else if(selectedChartTapMode == "throughputChartTap")
			{
				TapClear();
				$('#chartAreaTap_Throughput').css({'background-color' : '#666666','color': '#fff'});				
			}
			else if(selectedChartTapMode == "interfaceChartTap")
			{
				TapClear();
				$('#chartAreaTap_interface').css({'background-color' : '#666666','color': '#fff'});
			}
			else if(selectedChartTapMode == "interfaceListTap")
			{
				TapClear();
				$('#chartAreaTap_interface').css({'background-color' : '#666666','color': '#fff'});
			}
			else if(selectedChartTapMode == "sslConnChartTap")
			{
				if(adc.type == "F5")
				{
					TapClear();
					$('#chartAreaTap_SSL').css({'background-color' : '#666666','color': '#fff'});		
				}
				else
				{
					TapClear();
					selectedChartTapMode = "allChartTap";
					$('#chartAreaTap_All').css({'background-color' : '#666666','color': '#fff'});
				}						
			}
			else if(selectedChartTapMode == "httpChartTap")
			{
				if(adc.type == "F5")
				{
					TapClear();
					$('#chartAreaTap_HTTP').css({'background-color' : '#666666','color': '#fff'});
				}
				else
				{
					TapClear();
					selectedChartTapMode = "allChartTap";
					$('#chartAreaTap_All').css({'background-color' : '#666666','color': '#fff'});
				}							
			}
			else if(selectedChartTapMode == "cpuChartTap")
			{
				TapClear();
				$('#chartAreaTap_CPU').css({'background-color' : '#666666','color': '#fff'});
			}
			else if(selectedChartTapMode == "memoryChartTap")
			{
				TapClear();
				$('#chartAreaTap_Memory').css({'background-color' : '#666666','color': '#fff'});				
			}
			else if(selectedChartTapMode == "errpChartTap")
			{
				TapClear();
				$('#chartAreaTap_ErrP').css({'background-color' : '#666666','color': '#fff'});				
			}
			else if(selectedChartTapMode == "dropChartTap")
			{
				TapClear();				
				$('#chartAreaTap_DropP').css({'background-color' : '#666666','color': '#fff'});
			}
			else
			{
				TapClear();
				selectedChartTapMode = "allChartTap";
				$('#chartAreaTap_All').css({'background-color' : '#666666','color': '#fff'});				
			}
			
			if(adc.type == "F5")
			{
				$('.f5Area_Content').css('display', '');
				$('.f5Area_Line').css('display', '');			
			}
			else if(adc.type == "Alteon")
			{
				$('.f5Area_Content').css('display', 'none');
				$('.f5Area_Line').css('display', 'none');	
			}
			else
			{
				$('.f5Area_Content').css('display', 'none');
				$('.f5Area_Line').css('display', 'none');				
			};			
			
			// DatePicker 초기값 Setting
			if (null != searchStartTime) // 검색을 한번이라도 했을 경우
			{
				$('#reservationtime').val(new Date(searchStartTime).format('yyyy-mm-dd HH:MM')
						+" ~ "+ new Date(searchEndTime).format('yyyy-mm-dd HH:MM'));	
			}
			else // 검색을 한번도 하지않았을 경우
			{
				$('#reservationtime').val(moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm')
						+" ~ "+ moment().format('YYYY-MM-DD HH:mm'));
			}		

			// 리뉴얼 DatePicker + Timepicker
			if(langCode=="ko_KR")
			{
				$('#reservationtime').daterangepicker({					
					ranges: {					 
					 '최근 1시간' : [moment().subtract('hour', 1) ,moment()],
					 '최근 3시간' : [moment().subtract('hour', 3) ,moment()],
					 '최근 6시간' : [moment().subtract('hour', 6) ,moment()],
					 '최근 12시간' : [moment().subtract('hour', 12) ,moment()],
					 '최근 24시간' : [moment().subtract('hour', 24) ,moment()],
					 '오늘': [moment().startOf('days'), moment()],
					 '최근 30일': [moment().subtract('days', 30), moment()]					 
			      	},
				    startDate: moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm'),
					endDate: moment().format('YYYY-MM-DD HH:mm'),
				    opens: 'left', // 달력위치
	                timePicker: true,
	                timePickerIncrement: 30,
	                timePicker12Hour : false,
	                format: 'YYYY-MM-DD HH:mm'
	              }, function(start, end, label) {
//	            	  console.log(start.toISOString(), end.toISOString(), label);
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
			}
			else
			{
				// 리뉴얼 DatePicker
				$('#reservationtime').daterangepicker({					
					ranges: {					 
					 'Last hour' : [moment().subtract('hour', 1) ,moment()],
					 'Last 3 hours' : [moment().subtract('hour', 3) ,moment()],
					 'Last 6 hours' : [moment().subtract('hour', 6) ,moment()],
					 'Last 12 hours' : [moment().subtract('hour', 12) ,moment()],
					 'Last 1 Day' : [moment().subtract('hour', 24) ,moment()],
					 'Today': [moment().startOf('days'), moment()],
					 'Last 30 Days': [moment().subtract('days', 30), moment()]					 
			      	},
				    startDate: moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm'),
					endDate: moment().format('YYYY-MM-DD HH:mm'),
				    opens: 'left', // 달력위치
	                timePicker: true,
	                timePickerIncrement: 30,
	                timePicker12Hour : false,
	                format: 'YYYY-MM-DD HH:mm'
	              }, function(start, end, label) {
//	            	  console.log(start.toISOString(), end.toISOString(), label);
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
			}
		}		
	},	
	loadSessionHistoryInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};				

				ajaxManager.runJsonExt({
					url			: "monitor/loadSessionHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "SessionHistoryChart";
						GenerateConcurrentSessionHistoryChart(data, chartnameInput, interval);
												
						var $SessionHistoryNumber = $('#SessionCurr').filter(':last');
						var SessionHistoryCurr = '';
						if (data.sessionHistory.currValue != -1)
						{
							SessionHistoryCurr = data.sessionHistory.current;
						}
						else
						{
							SessionHistoryCurr = "-";
						}							
						$SessionHistoryNumber.empty();						
						$SessionHistoryNumber.html(SessionHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');						
						
						var $SessionHistoryAverage = $('#SessionPrev').filter(':last');
						var SessionHistoryPrev = '';
						if (data.sessionHistory.prevValue != -1)
						{
							SessionHistoryPrev = data.sessionHistory.yesterdayAvg;
						}
						else
						{
							SessionHistoryPrev = "-";
						}
						$SessionHistoryAverage.empty();
						$SessionHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + SessionHistoryPrev + '&nbsp;' + VAR_COMMON_AFEW);
					},
					completeFn	: function(data)
					{
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_SESSIONUSAGELOAD, jqXhr);
					}
				});
		}
	},
	loadBpsHistoryInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				
				ajaxManager.runJsonExt({
					url			: "monitor/loadBpsHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "BpsHistoryChart";
						GenerateBpsHistoryChart(data, chartnameInput, interval);				
						
						var $BpsHistoryNumber = $('#BpsCurr').filter(':last');
						var BpsHistoryCurr = '';
						$BpsHistoryNumber.empty();	
						if (data.bpsHistory.currValue != -1)
						{
							BpsHistoryCurr = data.bpsHistory.currValue;
						}
						else
						{
							BpsHistoryCurr = "-";
						}
						
						if (BpsHistoryCurr < 1000 || BpsHistoryCurr == "-")
						{
							$BpsHistoryNumber.html(BpsHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_BPS + '</span>');
						}						
						else if (BpsHistoryCurr < 1000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KBPS + '</span>');						
						}
						else if (BpsHistoryCurr < 1000000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MBPS + '</span>');	
						}
						else if (BpsHistoryCurr < 1000000000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000000000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GBPS + '</span>');	
						}
						else if (BpsHistoryCurr < 1000000000000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000000000000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TBPS + '</span>');	
						}												
						
						var $BpsHistoryAverage = $('#BpsPrev').filter(':last');
						var BpsHistoryPrev = '';
						$BpsHistoryAverage.empty();
						if (data.bpsHistory.prevValue != -1)
						{
							BpsHistoryPrev = data.bpsHistory.prevValue;
						}
						else
						{
							BpsHistoryPrev = "-";
						}
						
						if (BpsHistoryPrev < 1000 || BpsHistoryPrev == "-")
						{
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev + '&nbsp;' + VAR_COMMON_BPS);
						}						
						else if (BpsHistoryPrev < 1000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_KBPS);					
						}
						else if (BpsHistoryPrev < 1000000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_MBPS);	
						}
						else if (BpsHistoryPrev < 1000000000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000000000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_GBPS);
						}
						else if (BpsHistoryPrev < 1000000000000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000000000000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' +  VAR_COMMON_TBPS);
						}											
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_TRAFFICUSAGELOAD, jqXhr);
					}
				});	
		}
	},
	// All CPU
//	loadAdcCpuHistroyInfo : function(interval)
//	{
//		with(this)
//		{
//			var params = {
//					"adcObject.index" 		: adc.index,					
//					"adcObject.category"	: categoryIndex,
//					"adcObject.vendor"		: ,
//					"startTimeL"		: searchStartTime,
//					"endTimeL"			: searchEndTime
//				};				
//				
//				ajaxManager.runJsonExt({
//					url			: "monitor/loadAdcCpuHistroyInfo.action",
//					data		: params,
//					successFn	: function(data)
//					{
//						GenerateAdcCpuHistroyChart(data, interval);
//						
//						var $CpuHistoryAverage = $('#CpuPrev').filter(':last');
//						var CpuHistoryPrev = '';
//						if (data.cpuHistroy.prevValue != -1)
//						{
//							CpuHistoryPrev = data.cpuHistroy.prevValue;
//						}
//						else
//						{
//							CpuHistoryPrev = "-";
//						}
//						$CpuHistoryAverage.empty();
//						$CpuHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + CpuHistoryPrev + '&nbsp;%');				
//					},
//					completeFn	: function(data)
//					{	
//					},
//					errorFn : function(jqXhr)
//					{
//						$.obAlertAjaxError(VAR_APPLIANCE_CPUUSAGELOAD, jqXhr);
//					}
//				});			
//		}
//	},
	// F5 CPU tab Click
	loadCpuHistroyInfo : function(interval)
	{
		with(this)
		{			
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"adcObject.vendor"		: adc.type == "F5" ? 1 : 2,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};				
				
				ajaxManager.runJsonExt({
					url			: "monitor/loadAdcCpuHistroyInfo.action",
					data		: params,
					successFn	: function(data)
					{
						GenerateCpuHistroyChart(data, interval);
						
						var $CpuHistoryAverage = $('#CpuPrev').filter(':last');
						var CpuHistoryPrev = '';
						if (data.cpuAllHistroy.prevValue != -1)
						{
							CpuHistoryPrev = data.cpuAllHistroy.prevValue;
						}
						else
						{
							CpuHistoryPrev = "-";
						}
						$CpuHistoryAverage.empty();
						$CpuHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + CpuHistoryPrev + '&nbsp;%');				
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_CPUUSAGELOAD, jqXhr);
					}
				});			
		}
	},
	loadMemHistoryInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};				
				ajaxManager.runJsonExt({
					url			: "monitor/loadMemHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "MemHistoryChart";
						GenerateMemHistoryChart(data, chartnameInput, interval);
						var $MemHistoryNumber = $('#MemCurr').filter(':last');
						var MemHistoryCurr = '';
						if (data.memHistory.currValue != -1)
						{
							MemHistoryCurr = data.memHistory.currValue;
						}
						else
						{
							MemHistoryCurr = "-";
						}
						$MemHistoryNumber.empty();								
						$MemHistoryNumber.html(MemHistoryCurr +'&nbsp;<span class="unit">%</span>');						
						
						var $MemHistoryAverage = $('#MemPrev').filter(':last');
						var MemHistoryPrev = '';
						if (data.memHistory.prevValue != -1)
						{
							MemHistoryPrev = data.memHistory.prevValue;
						}
						else
						{
							MemHistoryPrev = "-";
						}
						$MemHistoryAverage.empty();
						$MemHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + MemHistoryPrev + '&nbsp;%');						
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_MEMUSAGELOAD, jqXhr);
					}
				});			
		}
	},
	loadPktErrHistoryInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};				
				ajaxManager.runJsonExt({
					url			: "monitor/loadPktErrHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "PktErrHistoryChart";
						GeneratePktErrHistoryChart(data, chartnameInput, interval);
						var $PktErrHistoryNumber = $('#PktECurr').filter(':last');
						var PktErrHistoryCurr = '';
						if (data.pktErrHistory.currValue != -1)
						{
							PktErrHistoryCurr = data.pktErrHistory.currValue;
						}
						else
						{
							PktErrHistoryCurr = "-";
						}
						$PktErrHistoryNumber.empty();								
						$PktErrHistoryNumber.html(PktErrHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');						
						
						var $PktErrHistoryAverage = $('#PktEPrev').filter(':last');
						var PktErrHistoryPrev = '';
						if (data.pktErrHistory.prevValue != -1)
						{
							PktErrHistoryPrev = data.pktErrHistory.prevValue;
						}
						else
						{
							PktErrHistoryPrev = "-";
						}
						$PktErrHistoryAverage.empty();
						$PktErrHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + PktErrHistoryPrev + '&nbsp;' + VAR_COMMON_AFEW);						
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_PKTERRGENLOAD, jqXhr);
					}
				});			
		}
	},
	loadPktDropHistoryInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};
				
				ajaxManager.runJsonExt({
					url			: "monitor/loadPktDropHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "PktDropHistoryChart";
						GeneratePktDropHistoryChart(data, chartnameInput, interval);
						var $PktDropHistoryNumber = $('#PktDCurr').filter(':last');
						var PktDropHistoryCurr = '';
						if (data.pktDropHistory.currValue != -1)
						{
							PktDropHistoryCurr = data.pktDropHistory.currValue;
						}
						else
						{
							PktDropHistoryCurr = "-";
						}
						$PktDropHistoryNumber.empty();								
						$PktDropHistoryNumber.html(PktDropHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW+ '</span>');						
						
						var $PktDropHistoryAverage = $('#PktDPrev').filter(':last');
						var PktDropHistoryPrev = '';
						if (data.pktDropHistory.prevValue != -1)
						{
							PktDropHistoryPrev = data.pktDropHistory.prevValue;
						}
						else
						{
							PktDropHistoryPrev = "-";
						}
						$PktDropHistoryAverage.empty();
						$PktDropHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + PktDropHistoryPrev + '&nbsp;' +VAR_COMMON_AFEW);						
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_LOSSLOAD, jqXhr);
					}
				});
		}
	},
	// F5 일때만 Load
	loadSSLTransactionHistoryInfo : function(interval)
	{
		with (this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};
				
			ajaxManager.runJsonExt({
				url			: "monitor/loadSSLTransactionHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{
					var chartnameInput = "SSLTransactionChart";
					GenerateSSLTransactionHistoryChart(data, chartnameInput, interval);
					var $SSLTransactionNumber = $('#SSLCurr').filter(':last');
					var SSLTransactionHistoryCurr = '';
					$SSLTransactionNumber.empty();	
					if (data.sslTransactionHistory.currValue != -1)
					{
						SSLTransactionHistoryCurr = data.sslTransactionHistory.currValue;
					}
					else
					{
						SSLTransactionHistoryCurr = "-";
					}
					
					if (SSLTransactionHistoryCurr < 1000 || SSLTransactionHistoryCurr == "-")
					{
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_TPS + '</span>');
					}						
					else if (SSLTransactionHistoryCurr < 1000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KTPS + '</span>');						
					}
					else if (SSLTransactionHistoryCurr < 1000000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MTPS + '</span>');	
					}
					else if (SSLTransactionHistoryCurr < 1000000000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000000000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GTPS + '</span>');	
					}
					else if (SSLTransactionHistoryCurr < 1000000000000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000000000000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_TTPS + '</span>');	
					}												
					
					var $SSLTransactionAverage = $('#SSLPrev').filter(':last');
					var SSLTransactionHistoryPrev = '';
					$SSLTransactionAverage.empty();
					if (data.sslTransactionHistory.prevValue != -1)
					{
						SSLTransactionHistoryPrev = data.sslTransactionHistory.prevValue;
					}
					else
					{
						SSLTransactionHistoryPrev = "-";
					}
					
					if (SSLTransactionHistoryPrev < 1000 || SSLTransactionHistoryPrev == "-")
					{
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev + '&nbsp;' + VAR_COMMON_TPS);
					}						
					else if (SSLTransactionHistoryPrev < 1000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_KTPS);					
					}
					else if (SSLTransactionHistoryPrev < 1000000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_MTPS);	
					}
					else if (SSLTransactionHistoryPrev < 1000000000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000000000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_GTPS);
					}
					else if (SSLTransactionHistoryPrev < 1000000000000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000000000000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_TTPS);
					}
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
	},
	// F5 일때만 Load
	loadHTTPRequestHistoryInfo : function(interval)
	{
		with (this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};
				
			ajaxManager.runJsonExt({
				url			: "monitor/loadHTTPRequestHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{
					var chartnameInput = "HTTPRequestChart";
					GenerateHTTPRequestHistoryChart(data, chartnameInput, interval);
					var $HTTPRequestNumber = $('#HTTPCurr').filter(':last');
					var HTTPRequestHistoryCurr = '';
					$HTTPRequestNumber.empty();	
					if (data.httpRequestHistory.currValue != -1)
					{
						HTTPRequestHistoryCurr = data.httpRequestHistory.currValue;
					}
					else
					{
						HTTPRequestHistoryCurr = "-";
					}
					
					if (HTTPRequestHistoryCurr < 1000 || HTTPRequestHistoryCurr == "-")
					{
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_RPS + '</span>');
					}						
					else if (HTTPRequestHistoryCurr < 1000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KRPS + '</span>');						
					}
					else if (HTTPRequestHistoryCurr < 1000000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MRPS + '</span>');	
					}
					else if (HTTPRequestHistoryCurr < 1000000000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000000000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GRPS + '</span>');	
					}
					else if (HTTPRequestHistoryCurr < 1000000000000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000000000000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_TRPS + '</span>');	
					}												
					
					var $HTTPRequestAverage = $('#HTTPPrev').filter(':last');
					var HTTPRequestHistoryPrev = '';
					$HTTPRequestAverage.empty();
					if (data.httpRequestHistory.prevValue != -1)
					{
						HTTPRequestHistoryPrev = data.httpRequestHistory.prevValue;
					}
					else
					{
						HTTPRequestHistoryPrev = "-";
					}
					
					if (HTTPRequestHistoryPrev < 1000 || HTTPRequestHistoryPrev == "-")
					{
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev + '&nbsp;' + VAR_COMMON_RPS);
					}						
					else if (HTTPRequestHistoryPrev < 1000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_KRPS);					
					}
					else if (HTTPRequestHistoryPrev < 1000000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_MRPS);	
					}
					else if (HTTPRequestHistoryPrev < 1000000000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000000000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_GRPS);
					}
					else if (HTTPRequestHistoryPrev < 1000000000000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000000000000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_TRPS);
					}					
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_HTTPREQLOAD, jqXhr);
				}
			});			
		}
	},
	// Concurrent Session 상세 Chart
	loadDetailCSInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};
				
				ajaxManager.runJsonExt({
					url			: "monitor/loadSessionHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "detailChart";
						var roleFLB = data.adcInfo.roleFlbYn;
						// FLB 가 포함된 ConcurrentSession Chart 는 Alteon 이면서 FLB 설정이 추가된 ADC 만 보여집니다.
						if (adc.type == "Alteon" && roleFLB == 1)	
						{
							GenerateConcurrentSessionHistoryDetailChart(data, chartnameInput, interval);
						}
						else
						{
							GenerateConcurrentSessionHistoryChart(data, chartnameInput, interval);
						}						
						var $SessionHistoryTitle = $(".title").filter(':last');
						$SessionHistoryTitle.empty();
						$SessionHistoryTitle.html("Concurrent Session");
												
						var $SessionHistoryNumber = $('#DetailCurr').filter(':last');
						var SessionHistoryCurr = '';
						if (data.sessionHistory.currValue != -1)
						{
							SessionHistoryCurr = data.sessionHistory.current;
						}
						else
						{
							SessionHistoryCurr = "-";
						}							
						$SessionHistoryNumber.empty();						
						$SessionHistoryNumber.html(SessionHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');						
						
						var $SessionHistoryAverage = $('#DetailPrev').filter(':last');
						var SessionHistoryPrev = '';
						if (data.sessionHistory.prevValue != -1)
						{
							SessionHistoryPrev = data.sessionHistory.yesterdayAvg;
						}
						else
						{
							SessionHistoryPrev = "-";
						}
						$SessionHistoryAverage.empty();
						$SessionHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + SessionHistoryPrev + '&nbsp;' + VAR_COMMON_AFEW);
					},
					completeFn	: function(data)
					{
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_SESSIONUSAGELOAD, jqXhr);
					}
				});
		}
	},
	// Throughtput 상세 Chart
	loadDetailThroughputInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
				};
				ajaxManager.runJsonExt({
					url			: "monitor/loadBpsHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "detailChart";
						GenerateBpsHistoryChart(data, chartnameInput, interval);				
						var $ThroughputTitle = $(".title").filter(':last');
						$ThroughputTitle.empty();
						$ThroughputTitle.html("Throughput");
						
						var $BpsHistoryNumber = $('#DetailCurr').filter(':last');
						var BpsHistoryCurr = '';
						$BpsHistoryNumber.empty();	
						if (data.bpsHistory.currValue != -1)
						{
							BpsHistoryCurr = data.bpsHistory.currValue;
						}
						else
						{
							BpsHistoryCurr = "-";
						}
						
						if (BpsHistoryCurr < 1000 || BpsHistoryCurr == "-")
						{
							$BpsHistoryNumber.html(BpsHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_BPS + '</span>');
						}						
						else if (BpsHistoryCurr < 1000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KBPS + '</span>');						
						}
						else if (BpsHistoryCurr < 1000000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MBPS + '</span>');	
						}
						else if (BpsHistoryCurr < 1000000000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000000000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GBPS + '</span>');	
						}
						else if (BpsHistoryCurr < 1000000000000000)
						{
							BpsHistoryCurr = BpsHistoryCurr/1000000000000;
							$BpsHistoryNumber.html(BpsHistoryCurr.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TBPS + '</span>');	
						}												
						
						var $BpsHistoryAverage = $('#DetailPrev').filter(':last');
						var BpsHistoryPrev = '';
						$BpsHistoryAverage.empty();
						if (data.bpsHistory.prevValue != -1)
						{
							BpsHistoryPrev = data.bpsHistory.prevValue;
						}
						else
						{
							BpsHistoryPrev = "-";
						}
						
						if (BpsHistoryPrev < 1000 || BpsHistoryPrev == "-")
						{
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev + '&nbsp;' + VAR_COMMON_BPS);
						}						
						else if (BpsHistoryPrev < 1000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_KBPS);					
						}
						else if (BpsHistoryPrev < 1000000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_MBPS);	
						}
						else if (BpsHistoryPrev < 1000000000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000000000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_GBPS);
						}
						else if (BpsHistoryPrev < 1000000000000000)
						{
							BpsHistoryPrev = BpsHistoryPrev/1000000000000;
							$BpsHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + BpsHistoryPrev.toFixed(1) + '&nbsp;' +  VAR_COMMON_TBPS);
						}											
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_TRAFFICUSAGELOAD, jqXhr);
					}
				});	
		}
	},
	// 인터페이스 모니터링 차트
	loadInterfaceChart: function(interfaceNameList, selectedTraffic, intervalMonitor)
	{
		this['interfaceNameList'] = interfaceNameList ? interfaceNameList : this['interfaceNameList'];
		this['selectedTraffic'] = selectedTraffic ? selectedTraffic : this['selectedTraffic'];
		with (this)
		{
			selectedChartTapMode = "interfaceChartTap";
			var params =
			{
				"interfaceNameList" : interfaceNameList,
				"selectedTraffic" : selectedTraffic,
				"adc.index" : this.adc.index,
				"startTimeL"		: searchStartTime,
				"endTimeL"			: searchEndTime
			};
			
			ajaxManager.runHtmlExt({
				url: "monitor/loadApplianceInterfaceChartContent.action",
				target: "div.ApplianceChartArea",
				successFn: function(data)
				{
					registApplianceMonitoContentEvents();
					restoreApplianceMonitoContent();
					
					ajaxManager.runJsonExt({
						url	:"monitor/loadInterFaceGraphData.action",
						data :params,
						successFn :function(data)
						{
							generateInterfaceChartData(data, selectedTraffic, intervalMonitor);							
						},
						errorFn : function(jqXhr)
						{
							$.obAlertAjaxError(VAR_STATISTICS_USAGLOADFAIL, jqXhr);
						}			
					});
				}
			});
		}
	},
	generateInterfaceChartData : function(data, selectedTraffic, intervalMonitor)
	{
		var inMaxData = []; var outMaxData = []; var totalMaxData = [];
		var inAvgData = 0; var outAvgData = 0; var totalAvgData = 0;		
		var chartData = [];
		var chartDataList = data.statistic5data[0].data;
		if (chartDataList.length > 0 && chartDataList != null)
		{
			for ( var i = 0; i < chartDataList.length; i++)
			{
				if (i == 0)
				{
					if (data.startTime < chartDataList[0].occurTime)
					{
						var startTime = parseDateTime(data.startTime);
						chartData.push({occurredTime:startTime});
					}
				}
				var column = chartDataList[i];
				if (column)
				{
					var occurTime = column.occurTime;
					var date = parseDateTime(occurTime);
					var firstValue = undefined;
					var secondValue = undefined;
					var thirdValue = undefined;
					var valueName = undefined;
					
					if (column.inValue > -1 && column.inValue != null)
					{
						firstValue = column.inValue;
						inMaxData.push(column.inValue);
						inAvgData += column.inValue;
					}		
					if (column.outValue > -1 && column.outValue != null)
					{
						secondValue = column.outValue;
						outMaxData.push(column.outValue);
						outAvgData += column.outValue;
					}
					if (column.totalValue > -1 && column.totalValue != null)
					{
						thirdValue = column.totalValue;
						totalMaxData.push(column.totalValue);
						totalAvgData += column.totalValue;
					}					
					
					var dataObject =
					{
						occurredTime : date,
						firstValue : firstValue,
						secondValue : secondValue,
						thirdValue : thirdValue						
					};
					// add object to dataProvider array
					chartData.push(dataObject);
				}
				if (i == (chartDataList.length - 1))
				{
					if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
					{
						var endTime = parseDateTime(data.endTime);
						chartData.push({occurredTime:endTime});								 
					}
				}
			}
		}
		else
		{
			var startTime = parseDateTime(data.startTime);
			var endTime = parseDateTime(data.endTime);
			var dataObject =
			{
			occurredTime : startTime
			};
			chartData.push(dataObject);
			var dataObject =
			{
					occurredTime : endTime
			};
			chartData.push(dataObject);				 
		}
				
		var maxDataObj = {
				inMax : Math.max.apply( Math, inMaxData),
				outMax :  Math.max.apply( Math, outMaxData),
			    totalMax : Math.max.apply( Math, totalMaxData)
		};
		var avgDataOnj = {
				inAvg : (inAvgData / inMaxData.length),
				outAvg : (outAvgData / outMaxData.length),
				totalAvg : (totalAvgData / totalMaxData.length)
		};		
		var chartunit = "";
		if (selectedTraffic == "Bytes")
		{
			chartunit = VAR_COMMON_BPS;
		}
		else if (selectedTraffic == "Packets")
		{
			chartunit = VAR_COMMON_PPS;
		}
		else
		{
			chartunit = VAR_STATISTICS_AFEW;
		} 
		var chartOption =
		{
			min : 0,
			max : null,
			linecolor1 : "#6cb8c8",
			linecolor2 : "#fbc51a",
			linecolor3 : "gray",
			chartname : "detailChart",
			axistitle : chartunit,
			maxPos : null,
			cursorColor : "#0f47c7",
			interval : intervalMonitor
		};		
		obchart.OBNewInterFaceChart(chartData, chartOption);
		this.displayMaxAvgData(maxDataObj, avgDataOnj);
	},
	displayMaxAvgData : function(maxDataObj, avgDataOnj)
	{
		with(this)
		{
			if (maxDataObj == null || maxDataObj === undefined ||
					avgDataOnj == null || avgDataOnj === undefined)
			{
				return;
			}
			var $dom_chart_max_in = $('.contents_area #chartdata_max_in').filter(':last');
			var $dom_chart_max_out = $('.contents_area #chartdata_max_out').filter(':last');
			var $dom_chart_max_total = $('.contents_area #chartdata_max_total').filter(':last');
			
			var $dom_chart_avg_in = $('.contents_area #chartdata_avg_in').filter(':last');
			var $dom_chart_avg_out = $('.contents_area #chartdata_avg_out').filter(':last');
			var $dom_chart_avg_total = $('.contents_area #chartdata_avg_total').filter(':last');
			
			if (selectedTraffic == "Bytes")
			{
				writeDom_Bps(maxDataObj.inMax, $dom_chart_max_in);
				writeDom_Bps(avgDataOnj.inAvg, $dom_chart_avg_in);
	
				writeDom_Bps(maxDataObj.outMax, $dom_chart_max_out);
				writeDom_Bps(avgDataOnj.outAvg, $dom_chart_avg_out);

				writeDom_Bps(maxDataObj.totalMax, $dom_chart_max_total);
				writeDom_Bps(avgDataOnj.totalAvg, $dom_chart_avg_total);

			}
			else if(selectedTraffic == "Packets")
			{
				writeDom_Pps(maxDataObj.inMax, $dom_chart_max_in);
				writeDom_Pps(avgDataOnj.inAvg, $dom_chart_avg_in);
	
				writeDom_Pps(maxDataObj.outMax, $dom_chart_max_out);
				writeDom_Pps(avgDataOnj.outAvg, $dom_chart_avg_out);

				writeDom_Pps(maxDataObj.totalMax, $dom_chart_max_total);
				writeDom_Pps(avgDataOnj.totalAvg, $dom_chart_avg_total);
			}
			else
			{
				$dom_chart_max_in.html(maxDataObj.inMax +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');
				$dom_chart_avg_in.html(avgDataOnj.inAvg +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');
				
				$dom_chart_max_out.html(maxDataObj.outMax +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');
				$dom_chart_avg_out.html(avgDataOnj.outAvg +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');
				
				$dom_chart_max_total.html(maxDataObj.totalMax +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');			
				$dom_chart_avg_total.html(avgDataOnj.totalAvg +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');
				
			}					
		}
	},
	writeDom_Bps : function(value, $dom)
	{
		with(this)
		{
			if (value < 1000 || value == "-")
			{
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_BPS + '</span>');
			}						
			else if (value < 1000000)
			{
				value = value/1000;
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KBPS + '</span>');						
			}
			else if (value < 1000000000)
			{
				value = value/1000000;
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MBPS + '</span>');	
			}
			else if (value < 1000000000000)
			{
				value = value/1000000000;
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GBPS + '</span>');	
			}
			else if (value < 1000000000000000)
			{
				value = value/1000000000000;
				$dom.html(value.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TBPS + '</span>');	
			}
			else
			{}
		}
	},
	writeDom_Pps : function(value, $dom)
	{
		with(this)
		{
			if (value < 1000 || value == "-")
			{
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_PPS + '</span>');
			}						
			else if (value < 1000000)
			{
				value = value/1000;
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KPPS + '</span>');						
			}
			else if (value < 1000000000)
			{
				value = value/1000000;
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MPPS + '</span>');	
			}
			else if (value < 1000000000000)
			{
				value = value/1000000000;
				$dom.html(value.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GPPS + '</span>');	
			}
			else if (value < 1000000000000000)
			{
				value = value/1000000000000;
				$dom.html(value.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TPPS + '</span>');	
			}
			else
			{}
		}
	},
	// Concurrent Session 
	_checkExportDataExist : function(selectedChartTapMode) //내보내기 시 내보낼 데이터 유무 체크 하여 없을 경우 알림 창
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: this.adc.index,					
					"adcObject.category"	: this.categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime,
					"selectedChartTapMode" : selectedChartTapMode,
					"cpuNum"             : $('.spNum').text()
				};	

			ajaxManager.runJsonExt
			({
				url : "monitor/checkExportDataExist.action",
				data : params,
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					exportADCCsv(selectedChartTapMode);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_EXPDATAEXIST, jqXhr);
				}
			});
		}
	},
	
	exportADCCsv : function(selectedChartTapMode)
	{
		with(this)
		{				
			var params = "selectedChartTapMode=" + this.selectedChartTapMode +
							"&adcObject.index=" + this.adc.index + "&adcObject.category=" + this.categoryIndex + "&startTimeL=" +searchStartTime +
									"&endTimeL=" + searchEndTime + "&cpuNum=" + $('.spNum').text();
			var url = "monitor/download.action?" + params;			
			$('#downloadFrame').attr('src',url);
		}
	},
	
	_checkExportStatisticsDataExist : function() //내보내기 시 내보낼 데이터 유무 체크 하여 없을 경우 알림 창
	{
		with(this)
		{
			var params = {
					"interfaceNameList" : this.interfaceNameList,	
					"selectedTraffic" : this.selectedTraffic,
					"adc.index" : this.adc.index,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};

			ajaxManager.runJsonExt
			({
				url : "monitor/checkExportStatisticsDataExist.action",
				data : params,
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					exportCsv();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_EXPDATAEXIST, jqXhr);
				}
			});
		}
	},
	exportCsv : function()
	{
		var interfaceName = "";
		with(this)
		{
			for (var i=0; i < this.interfaceNameList.length; i++)
			{
				interfaceName += "&interfaceNameList[" + i + "]=" + encodeURIComponent(this.interfaceNameList[i]);
			}			
			var params = "selectedTraffic=" + this.selectedTraffic +
							"&adc.index=" + this.adc.index + "&startTimeL=" +searchStartTime +
									"&endTimeL=" + searchEndTime;
			var url = "monitor/downloadStatistics.action?" + params + interfaceName;			
			$('#downloadFrame').attr('src',url);
		}
	},
	// 인터페이스 모니터링 리스트
	loadInterfaceList: function(orderType, orderDir)
	{
		this.adc = adcSetting.getAdc();
		var global = this;
		with (this)
		{			
			if (!adc)
				return;
			
			var params =
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"orderType" : orderType ? orderType : 14,
				"orderDir"   : orderDir ? orderDir : 1
			};
			
			ajaxManager.runHtmlExt({
				url : "monitor/loadApplianceInterfaceListContent.action",				
				data : params,
				target: "div.ApplianceChartArea",
				successFn	: function(data)
				{
					global['allInterfaceNames'] = {};
					$('[data-intfname]').each(function()
					{
						global['allInterfaceNames'][$(this).data('intfname')] = $(this).find('#dispName').text();
					});
					registApplianceMonitoContentEvents();
					restoreApplianceMonitoContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_STATISTICS_LOADFAIL, jqXhr);
				}	
			});
		}
	},
	// SSL 상세 Chart
	loadDetailSSLInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				
			ajaxManager.runJsonExt({
				url			: "monitor/loadSSLTransactionHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{
					var chartnameInput = "detailChart";
					GenerateSSLTransactionHistoryChart(data, chartnameInput, interval);
					var $SSLTitle = $(".title").filter(':last');
					$SSLTitle.empty();
					$SSLTitle.html("SSL Transactions");					
					
					var $SSLTransactionNumber = $('#DetailCurr').filter(':last');
					var SSLTransactionHistoryCurr = '';
					$SSLTransactionNumber.empty();	
					if (data.sslTransactionHistory.currValue != -1)
					{
						SSLTransactionHistoryCurr = data.sslTransactionHistory.currValue;
					}
					else
					{
						SSLTransactionHistoryCurr = "-";
					}
					
					if (SSLTransactionHistoryCurr < 1000 || SSLTransactionHistoryCurr == "-")
					{
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_TPS + '</span>');
					}						
					else if (SSLTransactionHistoryCurr < 1000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KTPS + '</span>');						
					}
					else if (SSLTransactionHistoryCurr < 1000000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MTPS + '</span>');	
					}
					else if (SSLTransactionHistoryCurr < 1000000000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000000000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GTPS + '</span>');	
					}
					else if (SSLTransactionHistoryCurr < 1000000000000000)
					{
						SSLTransactionHistoryCurr = SSLTransactionHistoryCurr/1000000000000;
						$SSLTransactionNumber.html(SSLTransactionHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_TTPS + '</span>');	
					}												
					
					var $SSLTransactionAverage = $('#DetailPrev').filter(':last');
					var SSLTransactionHistoryPrev = '';
					$SSLTransactionAverage.empty();
					if (data.sslTransactionHistory.prevValue != -1)
					{
						SSLTransactionHistoryPrev = data.sslTransactionHistory.prevValue;
					}
					else
					{
						SSLTransactionHistoryPrev = "-";
					}
					
					if (SSLTransactionHistoryPrev < 1000 || SSLTransactionHistoryPrev == "-")
					{
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev + '&nbsp;' + VAR_COMMON_TPS);
					}						
					else if (SSLTransactionHistoryPrev < 1000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_KTPS);					
					}
					else if (SSLTransactionHistoryPrev < 1000000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_MTPS);	
					}
					else if (SSLTransactionHistoryPrev < 1000000000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000000000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_GTPS);
					}
					else if (SSLTransactionHistoryPrev < 1000000000000000)
					{
						SSLTransactionHistoryPrev = SSLTransactionHistoryPrev/1000000000000;
						$SSLTransactionAverage.html(VAR_APPLIANCE_PERAVERAGE + SSLTransactionHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_TTPS);
					}
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
	},
	// HTTP Request 상세 Chart
	loadDetailHTTPInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				
			ajaxManager.runJsonExt({
				url			: "monitor/loadHTTPRequestHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{
					var chartnameInput = "detailChart";
					GenerateHTTPRequestHistoryChart(data, chartnameInput, interval);
					var $HTTPTitle = $(".title").filter(':last');
					$HTTPTitle.empty();
					$HTTPTitle.html("HTTP Requests");					
					
					var $HTTPRequestNumber = $('#DetailCurr').filter(':last');
					var HTTPRequestHistoryCurr = '';
					$HTTPRequestNumber.empty();	
					if (data.httpRequestHistory.currValue != -1)
					{
						HTTPRequestHistoryCurr = data.httpRequestHistory.currValue;
					}
					else
					{
						HTTPRequestHistoryCurr = "-";
					}
					
					if (HTTPRequestHistoryCurr < 1000 || HTTPRequestHistoryCurr == "-")
					{
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_RPS + '</span>');
					}						
					else if (HTTPRequestHistoryCurr < 1000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KRPS + '</span>');						
					}
					else if (HTTPRequestHistoryCurr < 1000000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MRPS + '</span>');	
					}
					else if (HTTPRequestHistoryCurr < 1000000000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000000000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GRPS + '</span>');	
					}
					else if (HTTPRequestHistoryCurr < 1000000000000000)
					{
						HTTPRequestHistoryCurr = HTTPRequestHistoryCurr/1000000000000;
						$HTTPRequestNumber.html(HTTPRequestHistoryCurr.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_TRPS + '</span>');	
					}												
					
					var $HTTPRequestAverage = $('#DetailPrev').filter(':last');
					var HTTPRequestHistoryPrev = '';
					$HTTPRequestAverage.empty();
					if (data.httpRequestHistory.prevValue != -1)
					{
						HTTPRequestHistoryPrev = data.httpRequestHistory.prevValue;
					}
					else
					{
						HTTPRequestHistoryPrev = "-";
					}
					
					if (HTTPRequestHistoryPrev < 1000 || HTTPRequestHistoryPrev == "-")
					{
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev + '&nbsp;' + VAR_COMMON_RPS);
					}						
					else if (HTTPRequestHistoryPrev < 1000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_KRPS);					
					}
					else if (HTTPRequestHistoryPrev < 1000000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_MRPS);	
					}
					else if (HTTPRequestHistoryPrev < 1000000000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000000000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_GRPS);
					}
					else if (HTTPRequestHistoryPrev < 1000000000000000)
					{
						HTTPRequestHistoryPrev = HTTPRequestHistoryPrev/1000000000000;
						$HTTPRequestAverage.html(VAR_APPLIANCE_PERAVERAGE + HTTPRequestHistoryPrev.toFixed(1) + '&nbsp;' + VAR_COMMON_TRPS);
					}					
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_HTTPREQLOAD, jqXhr);
				}
			});
		}
	},
	
	// SP별 세션 Detail : 선택한 SP의 넘버를 넘겨서 화면에 SP값의 추이를 표현함.	
	// SP별 세션 상테 Chart
	loadDetailSPSessionInfo : function(spNum, interval) 
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"adcObject.vendor"		: spNum,
					"cpuNum"				: spNum,
					"startTimeL"			: searchStartTime,
					"endTimeL"				: searchEndTime
			};			
			ajaxManager.runJsonExt({
				url			: "monitor/loadCpuSPHistroyInfo.action",
				data		: params,
				successFn	: function(data)
				{
					$('.exportCssLnk').removeClass('none')
					GenerateCpuSPHistroyDetailChart(data, interval);
					
//					$('.CpuOnlyNone').removeClass("none");					
					$('.spCpuSub').removeAttr("disabled");
//					$('.info').removeClass("none");
//					$('.infoMain').addClass("none");
					$('.average').removeClass("none");
					$('.averageM').addClass("none");
					$('.sessionTitle').removeClass("none");
					$('.sessionCurr').removeClass("none");
					
					$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar.png");	
					
					$('.spCPUSessionMain').removeClass("none");
					var $CPUTitle = $(".title").filter(':last');
					$CPUTitle.empty();			
					var spNumVal = spNum -1;
					$CPUTitle.html('<span class="spNum none">'+ spNum +'</span>' + "SP" + spNumVal + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CPU&nbsp;" + VAR_APPLIANCE_SPAVG + "&nbsp;");
					
					var $CpuHistoryNumber = $('#DetailCurr').filter(':last');
					var CpuHistoryCurr = '';
					if (data.cpuSPHistory.avgUsageValue != -1)
					{
						CpuHistoryCurr = data.cpuSPHistory.avgUsageValue;
					}
					else
					{
						CpuHistoryCurr = "-";
					}
					$CpuHistoryNumber.empty();								
					$CpuHistoryNumber.html(CpuHistoryCurr +'&nbsp;<span class="unit">%</span>');						
					
					var $CpuHistoryAverage = $('#DetailPrev').filter(':last');
					var CpuHistoryPrev = '';
					if (data.cpuSPHistory.prevValue != -1)
					{
						CpuHistoryPrev = data.cpuSPHistory.prevValue;
					}
					else
					{
						CpuHistoryPrev = "-";
					}
					
					var CpuHistoryMax = '';
					if (data.cpuSPHistory.maxUsageValue != -1)
					{
						CpuHistoryMax = data.cpuSPHistory.maxUsageValue;
					}
					else
					{
						CpuHistoryMax = "-";
					}					
					
					$CpuHistoryAverage.empty();
					$CpuHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + CpuHistoryPrev + '&nbsp;%' + "&nbsp;&nbsp;" + VAR_APPLIANCE_MAX + CpuHistoryMax + '&nbsp;%');		
					
					var $CPUSessionTitle = $(".sessionTitle").filter(':last');
					$CPUSessionTitle.empty();	
					$CPUSessionTitle.html("Session" + "&nbsp;" + VAR_APPLIANCE_SPAVG + "&nbsp;");
										
					var $CpuHistorySessionNumber = $('#DetailSessionCurr').filter(':last');
					
					var CpuHistorySessionCurr = '';
					if (data.cpuSPHistory.avgConnsValue != -1)
					{
						CpuHistorySessionCurr = data.cpuSPHistory.avgConnsValue;
					}
					else
					{
						CpuHistorySessionCurr = "-";
					}
					$CpuHistorySessionNumber.empty();								
					$CpuHistorySessionNumber.html(CpuHistorySessionCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');
					
					var $CpuHistorySessionAverage = $('#DetailSessionPrev').filter(':last');
					
					// Session Avg
					var CpuHistorySessionPrev = '';
					if (data.cpuSPHistory.currValue != -1)
					{
						CpuHistorySessionPrev = data.cpuSPHistory.currValue;
					}
					else
					{
						CpuHistorySessionPrev = "-";
					}
					
					// Session Max
					var CpuHistorySessionMax = '';
					if (data.cpuSPHistory.maxConnsValue != -1)
					{
						CpuHistorySessionMax = data.cpuSPHistory.maxConnsValue;
					}
					else
					{
						CpuHistorySessionMax = "-";
					}
					
					// Session Maximun
					var CpuSpSessionMax = '';
					if (data.cpuSPHistory.spSessionMaxUnit != -1)
					{
						CpuSpSessionMax = data.cpuSPHistory.spSessionMaxUnit;
					}
					else
					{
						CpuSpSessionMax = "-";
					}
					
					$CpuHistorySessionAverage.empty();
					$CpuHistorySessionAverage.html(VAR_APPLIANCE_PERAVERAGE + CpuHistorySessionPrev + '&nbsp;' + VAR_COMMON_AFEW + "&nbsp;&nbsp;" + 
							VAR_APPLIANCE_MAX + CpuHistorySessionMax + '&nbsp;' + VAR_COMMON_AFEW + "&nbsp;&nbsp;" +
							VAR_APPLIANCE_SPMAX + CpuSpSessionMax + '&nbsp;' + VAR_COMMON_AFEW);		
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_CPUUSAGELOAD, jqXhr);
				}
			});
		}
	},
	
	// SP별 CPU Main - Alteon 인 경우와 아닌경우로 구분 GenerateChart 를 다르게 표현함.
	// CPU 상세 Chart -> SP 별 CPU, Session 전체 Chart
	loadDetailCPUInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				
				var loadUrl="";
				if(adc.type == "Alteon")
				{
					loadUrl = "monitor/loadCpuSpConnectionInfo.action";
				}
				else
				{
					loadUrl = "monitor/loadCpuHistroyInfo.action";
				}
				
				ajaxManager.runJsonExt({
					url			: loadUrl,
					data		: params,
					successFn	: function(data)
					{
						if(adc.type == "Alteon")
						{
							$('.exportCssLnk').addClass('none')
							GenerateSPCpuSessionChart(data, interval);
/*	
//							$('.CpuOnlyNone').addClass("none");
							$('.spCPUSessionMain').addClass("none");
							$('.average').addClass("none");
							$('.averageM').removeClass("none");
							$('.sessionTitle').addClass("none");	// DetailSessionCurr DetailSessionPrev							
							$('.sessionCurr').addClass("none");
//							$('.infoMain').removeClass("none");
							
							$('.spCpuSub').attr("disabled", "disabled");
							$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");	
							
							var $CPUSPTitle = $(".title").filter(':last');
							$CPUSPTitle.empty();					
//							$CPUSPTitle.html("Session");
							$CPUSPTitle.html("CPU");
														
							var $CpuSPMaximum = $('#DetailMaximun').filter(':last');
							var CpuHistoryCurr = '';
							if (data.cpuSpConns != null)
							{
								if (data.cpuSpConns.cpuAvg != -1)
								{
									CpuHistoryCurr = data.cpuSpConns.cpuAvg;
								}
								else
								{
									CpuHistoryCurr = "-";
								}
							}
							$CpuSPMaximum.empty();
							$CpuSPMaximum.html(VAR_APPLIANCE_SPMAX + '&nbsp;' + CpuHistoryCurr + VAR_COMMON_AFEW);	
*/							
							var $CPUTitle = $(".title").filter(':last');
							$CPUTitle.empty();
							$CPUTitle.html("CPU");		
							
							var $CpuHistoryNumber = $('#DetailCurr').filter(':last');
							var CpuHistoryCurr = '';
							if (data.cpuSpConns.cpuAvg != -1)
							{
								CpuHistoryCurr = data.cpuSpConns.cpuAvg;
							}
							else
							{
								CpuHistoryCurr = "-";
							}					
							
							$CpuHistoryNumber.empty();								
							$CpuHistoryNumber.html(CpuHistoryCurr +'&nbsp;<span class="unit">%</span>');						
							
							var $CpuHistoryAverage = $('#DetailPrev').filter(':last');
							var CpuHistoryPrev = '';
							if (data.cpuSpConns.cpuPreAvg != -1)
							{
								CpuHistoryPrev = data.cpuSpConns.cpuPreAvg;
							}
							else
							{
								CpuHistoryPrev = "-";
							}
							$CpuHistoryAverage.empty();
							$CpuHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + CpuHistoryPrev + '&nbsp;%');		
						}
						else
						{
							$('.exportCssLnk').removeClass('none')
							GenerateCpuHistroyDetailChart(data, interval);
							
							var $CPUTitle = $(".title").filter(':last');
							$CPUTitle.empty();
							$CPUTitle.html("CPU");	
							
							var $CpuHistoryNumber = $('#DetailCurr').filter(':last');
							var CpuHistoryCurr = '';
							if (data.cpuHistroy.currValue != -1)
							{
								CpuHistoryCurr = data.cpuHistroy.currValue;
							}
							else
							{
								CpuHistoryCurr = "-";
							}
							$CpuHistoryNumber.empty();								
							$CpuHistoryNumber.html(CpuHistoryCurr +'&nbsp;<span class="unit">%</span>');						
							
							var $CpuHistoryAverage = $('#DetailPrev').filter(':last');
							var CpuHistoryPrev = '';
							if (data.cpuHistroy.prevValue != -1)
							{
								CpuHistoryPrev = data.cpuHistroy.prevValue;
							}
							else
							{
								CpuHistoryPrev = "-";
							}
							$CpuHistoryAverage.empty();
							$CpuHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + CpuHistoryPrev + '&nbsp;%');	
						}									
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_CPUUSAGELOAD, jqXhr);
					}
				});
		}
	},
	// Memory 상세 Chart
	loadDetailMemoryInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				
				ajaxManager.runJsonExt({
					url			: "monitor/loadMemHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "detailChart";
						GenerateMemHistoryChart(data, chartnameInput, interval);
						var $MemoryTitle = $(".title").filter(':last');
						$MemoryTitle.empty();
						$MemoryTitle.html("Memory");	
						
						
						var $MemHistoryNumber = $('#DetailCurr').filter(':last');
						var MemHistoryCurr = '';
						if (data.memHistory.currValue != -1)
						{
							MemHistoryCurr = data.memHistory.currValue;
						}
						else
						{
							MemHistoryCurr = "-";
						}
						$MemHistoryNumber.empty();								
						$MemHistoryNumber.html(MemHistoryCurr +'&nbsp;<span class="unit">%</span>');						
						
						var $MemHistoryAverage = $('#DetailPrev').filter(':last');
						var MemHistoryPrev = '';
						if (data.memHistory.prevValue != -1)
						{
							MemHistoryPrev = data.memHistory.prevValue;
						}
						else
						{
							MemHistoryPrev = "-";
						}
						$MemHistoryAverage.empty();
						$MemHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + MemHistoryPrev + '&nbsp;%');						
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_MEMUSAGELOAD, jqXhr);
					}
				});	
		}
	},
	// Error Packets 상세 Chart
	loadDetailPktErrInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				ajaxManager.runJsonExt({
					url			: "monitor/loadPktErrHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "detailChart";
						GeneratePktErrHistoryChart(data, chartnameInput, interval);
						var $PkrErrTitle = $(".title").filter(':last');
						$PkrErrTitle.empty();
						$PkrErrTitle.html("Error Packets");	
						
						var $PktErrHistoryNumber = $('#DetailCurr').filter(':last');
						var PktErrHistoryCurr = '';
						if (data.pktErrHistory.currValue != -1)
						{
							PktErrHistoryCurr = data.pktErrHistory.currValue;
						}
						else
						{
							PktErrHistoryCurr = "-";
						}
						$PktErrHistoryNumber.empty();								
						$PktErrHistoryNumber.html(PktErrHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');						
						
						var $PktErrHistoryAverage = $('#DetailPrev').filter(':last');
						var PktErrHistoryPrev = '';
						if (data.pktErrHistory.prevValue != -1)
						{
							PktErrHistoryPrev = data.pktErrHistory.prevValue;
						}
						else
						{
							PktErrHistoryPrev = "-";
						}
						$PktErrHistoryAverage.empty();
						$PktErrHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + PktErrHistoryPrev + '&nbsp;' + VAR_COMMON_AFEW);						
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_PKTERRGENLOAD, jqXhr);
					}
				});
		}
	},
	// Dropped Packets 상세 Chart
	loadDetailPktDropInfo : function(interval)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				
				ajaxManager.runJsonExt({
					url			: "monitor/loadPktDropHistoryInfo.action",
					data		: params,
					successFn	: function(data)
					{
						var chartnameInput = "detailChart";
						GeneratePktDropHistoryChart(data, chartnameInput, interval);
						var $PkrDropTitle = $(".title").filter(':last');
						$PkrDropTitle.empty();
						$PkrDropTitle.html("Dropped Packets");
						
						var $PktDropHistoryNumber = $('#DetailCurr').filter(':last');
						var PktDropHistoryCurr = '';
						if (data.pktDropHistory.currValue != -1)
						{
							PktDropHistoryCurr = data.pktDropHistory.currValue;
						}
						else
						{
							PktDropHistoryCurr = "-";
						}
						$PktDropHistoryNumber.empty();								
						$PktDropHistoryNumber.html(PktDropHistoryCurr +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW+ '</span>');						
						
						var $PktDropHistoryAverage = $('#DetailPrev').filter(':last');
						var PktDropHistoryPrev = '';
						if (data.pktDropHistory.prevValue != -1)
						{
							PktDropHistoryPrev = data.pktDropHistory.prevValue;
						}
						else
						{
							PktDropHistoryPrev = "-";
						}
						$PktDropHistoryAverage.empty();
						$PktDropHistoryAverage.html(VAR_APPLIANCE_PERAVERAGE + PktDropHistoryPrev + '&nbsp;' +VAR_COMMON_AFEW);						
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_APPLIANCE_LOSSLOAD, jqXhr);
					}
				});
		}
	},
	
	GenerateHTTPRequestHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.httpRequestHistory != null)
			{
				chartDataList = data.httpRequestHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.value < 0 )
						{					
						}											
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}						
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			 
			 var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : VAR_COMMON_RPS,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "HTTP Requests",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);
		}
	},
	GenerateSSLTransactionHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.sslTransactionHistory != null)
			{
				chartDataList = data.sslTransactionHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.value < 0 )
						{					
						}											
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}						
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			
			 var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : VAR_COMMON_TPS,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "SSL Transactions",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);	
		}
	},
	// Concurrent Session Summary Chart
	GenerateConcurrentSessionHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			var maxData = [];
			if (data.sessionHistory != null)
			{
				chartDataList = data.sessionHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.flbSession < 0 && column.slbSession < 0)
						{
						}
						else if (column.slbSession < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);							
							var firstValue = column.flbSession;
							var dataObject =
							{
								occurredTime : date,								
								firstValue : firstValue
							};
							var maxdataObject = 
							{
								value : secondValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
						else if (column.flbSession < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.slbSession;							
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue								
							};
							var maxdataObject = 
							{
								value : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.slbSession + column.flbSession;

							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue	
							};
							var maxdataObject = 
							{
								value : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}					
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			 
			 var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : VAR_COMMON_AFEW,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Concurrent Session",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);			
		}		
	},
	// Concurrent Session Detail Chart
	GenerateConcurrentSessionHistoryDetailChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			var maxData = [];
			if (data.sessionHistory != null)
			{
				chartDataList = data.sessionHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.flbSession < 0 && column.slbSession < 0)
						{					
						}
						else if (column.slbSession < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);							
							var secondValue = column.flbSession;
							var dataObject =
							{
								occurredTime : date,								
								secondValue : secondValue
							};
							var maxdataObject = 
							{
								value : secondValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
						else if (column.flbSession < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.slbSession;							
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue								
							};
							var maxdataObject = 
							{
								value : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.slbSession;
							var secondValue = column.flbSession;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue,
								secondValue : secondValue,
								thirdValue : (firstValue + secondValue)
							};
							var maxdataObject = 
							{
								value : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}						
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			 
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "gray",
				 linecolor2 : "#6cb8c8",
				 linecolor3 : "#fbc51a",
				 chartname : "detailChart",
				 axistitle1 : VAR_COMMON_AFEW,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 title : "Concurrent Session",
				 interval : interval
			};
			 obchart.OBConnFlbSlbMultiChartViewer(chartData, chartOption);			
		}		
	},
	// ADC트래픽 Chart
	GenerateBpsHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.bpsHistory != null)
			{
				chartDataList = data.bpsHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.value < 0 )
						{
						}											
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }
			 var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : VAR_COMMON_BPS,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Throughput",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);				
		}		
	},
	//CPU Chart
	GenerateCpuHistroyChart : function(data, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.cpuAllHistroy != null)
			{
				chartDataList = data.cpuAllHistroy.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{	
						if (column.mpCpu < 0 && column.spCpuAvg < 0)
						{							
						}	
						else if (column.mpCpu > 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.mpCpu;
							var secondValue = column.spCpuAvg;
							var dataObject =
							{
								occurredTime : date,								
								firstValue : firstValue,
								secondValue : secondValue,
								
								firstName : "MP Usage",
								secondName : "SP Avg",	
							};
							chartData.push(dataObject);
						}
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.spCpuAvg;
							var dataObject =
							{
								occurredTime : date,								
								firstValue : firstValue
							};
							chartData.push(dataObject);
						}
						
						// CPU 현재값 작성은 평균값으로 작성해야 하기 때문에 이곳에 위치한다.
						var $CpuHistoryNumber = $('#CpuCurr').filter(':last');
						var CpuHistoryCurr = '';
						if (data.cpuAllHistroy.currValue != -1)
						{
//							CpuHistoryCurr = avgObj.avgNum;
							CpuHistoryCurr = data.cpuAllHistroy.currValue;	
//							CpuHistoryCurr = cpuAvg;
						}
						else
						{
							CpuHistoryCurr = "-";
						}
						$CpuHistoryNumber.empty();								
						$CpuHistoryNumber.html(CpuHistoryCurr +'&nbsp;<span class="unit">%</span>');
						// add object to dataProvider array
						chartData.push(dataObject);
						}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				}				 
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }		
			 var chartOption =
				{
					 min : 0,
					 max : 100,
					 linecolor : "#6cb8c8",
					 chartname : "CpuHistroyChart",
					 axistitle : "%",
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "CPU",
					 interval : interval
				};
			 
			if (adc.type == "Alteon")
			{
				obchart.OBMultiLineChartViewer(chartData, chartOption);
			}
			else
			{
				chartOption.cursorColor = "#0f47c7";
				obchart.OBAreaChartViewer(chartData, chartOption);
			}
		}		
	},
	
	//CPU DetailChart
	GenerateCpuHistroyDetailChart : function(data, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.cpuHistroy != null)
			{
				chartDataList = data.cpuHistroy.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					 
					var column = chartDataList[i];
					var occurTime = column.occurTime;
					var date = parseDateTime(occurTime);
					var mpValue = undefined;
			        var spValue1 = undefined;
					var spValue2 = undefined;
					var spValue3 = undefined;
					var spValue4 = undefined;
					var spValue5 = undefined;
					var spValue6 = undefined;
					var spValue7 = undefined;
					var spValue8 = undefined;
					var spValue9 = undefined;
					var spValue10 = undefined;
					var spValue11 = undefined;
					var spValue12 = undefined;
					var spValue13 = undefined;
					var spValue14 = undefined;
					var spValue15 = undefined;
					var spValue16 = undefined;
					var spValue17 = undefined;
					var spValue18 = undefined;
					var spValue19 = undefined;
					var spValue20 = undefined;
					var spValue21 = undefined;
					var spValue22 = undefined;
					var spValue23 = undefined;
					var spValue24 = undefined;
					var spValue25 = undefined;
					var spValue26 = undefined;
					var spValue27 = undefined;
					var spValue28 = undefined;
					var spValue29 = undefined;
					var spValue30 = undefined;
					var spValue31 = undefined;
					var spValue32 = undefined;
					
					var spVal = undefined;
					var spValue = new Array();
					var spValueData = new Array();
					
					for (var j = 0; j < column.cpus.length; j++)
					{
						
						if (column.mpCpu > -1 && column.mpCpu != null)
						{
							mpValue = column.mpCpu;
						}
						
//						console.log(column.cpus.length);
//						console.log(column.cpus[j]);
						
						if(column.cpus[j] >= 0)
						{
						
							spVal = column.cpus[j];							
						}
						spValue.push(spVal);
						
//						console.log(spValue);
//						console.log(spValue[0]);
//						console.log(spValue[1]);
					
						var dataObject =
						{
								occurredTime : date,
								MPValue : mpValue,	
								
								Value1 : spValue[0],
								Value2 : spValue[1],
								Value3 : spValue[2],
								Value4 : spValue[3],
								Value5 : spValue[4],
								Value6 : spValue[5],
								Value7 : spValue[6],
								Value8 : spValue[7],
								Value9 : spValue[8],
								Value10 : spValue[9],
								Value11 : spValue[10],
								Value12 : spValue[11],
								Value13 : spValue[12],
								Value14 : spValue[13],
								Value15 : spValue[14],
								Value16 : spValue[15],
								Value17 : spValue[16],
								Value18 : spValue[17],
								Value19 : spValue[18],
								Value20 : spValue[19],
								Value21 : spValue[20],
								Value22 : spValue[21],
								Value23 : spValue[22],
								Value24 : spValue[23],
								Value25 : spValue[24],
								Value26 : spValue[25],
								Value27 : spValue[26],
								Value28 : spValue[27],
								Value29 : spValue[28],
								Value30 : spValue[29],
								Value31 : spValue[30],
								Value32 : spValue[31]
						};
						// add object to dataProvider array
						chartData.push(dataObject);
						}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				}				 
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }
			 var min = 0;
			 var max = 100;
			 var chartname = "detailChart";			
			 
			 obchart.OBCPUChartViewerNonAlteon(chartData, min, max, chartname, interval);
		}		
	},
	
	// SP별 CPU 사용률, Connection 추이 Detail Chart
	GenerateCpuSPHistroyDetailChart : function(data, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			var maxData = [];
			if (data.cpuSPHistory != null)
			{
				chartDataList = data.cpuSPHistory.history;				
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				for ( var i = 0; i < chartDataList.length; i++)
				{
					if (i == 0)
					{
						if (data.startTime < chartDataList[0].occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{	
						if (column.cpuValue < 0 && column.cpuConns < 0)
						{					
						}
						else if (column.cpuValue < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);							
							var spValue = column.cpuValue;
							var dataObject =
							{
								"occurredTime": date,								
								"column-1": spValue
							};
							var maxdataObject = 
							{
								value : spValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
//						else if (column.cpuConns < 0)
//						{
//							var occurTime = column.occurTime;
//							var date = parseDateTime(occurTime);
//							var spConns = column.cpuConns;							
//							var dataObject =
//							{
//								"occurredTime": date,
//								"column-2": spConns								
//							};
//							var maxdataObject = 
//							{
//								value : spConns
//							};
//							// add object to dataProvider array
//							chartData.push(dataObject);
//							maxData.push(maxdataObject);
//						}	
//						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var spValue = column.cpuValue;
							var spConns = column.cpuConns;
							
							var dataObject =						
						 	{						
								"occurredTime": date,
								"column-1": spValue,
								"column-2": spConns
						 	};
							
							var maxdataObject = 
							{
								value : data.cpuSPHistory.avgUsageValue,			// Usage Avg	
								value2 : data.cpuSPHistory.maxUsageValue,			// Usage Max
								value3 : data.cpuSPHistory.avgConnsValue,			// Session Avg
								value4 : data.cpuSPHistory.maxConnsValue			// Session Max
//								value5 : data.cpuSPHistory.spSessionMax				// Session 임계치값								
//								value6 : data.cpuSPHistory.spSessionMaxUnit 		// 임계치값 unit
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				}				 
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }
//			var maxData = [];
//			var maxdataObject = 
//			{
////				value : chartDataList.cpuMaxValue,
////				value2 : chartDataList.cpuAvgValue
//				value : data.cpuSPHistroy.cpuPrevValue,
//			};
//			
//			maxData.push(maxdataObject);
			 
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor : "#6cb8c8",
//				 chartname : chartnameInput,
				 chartname : "detailChart",
				 axistitle1 : "%",
				 axistitle2 : VAR_COMMON_AFEW,
//				 maxPos : null,
//				 maxPos : Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
//				 avgPos : Math.max.apply(null, $.map(maxData, function(o){return o.value2;})),
				 avgPos : Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
				 maxPos : Math.max.apply(null, $.map(maxData, function(o){return o.value2;})),
				 avgSPSession : Math.max.apply(null, $.map(maxData, function(o){return o.value3;})),
				 maxSPSession : Math.max.apply(null, $.map(maxData, function(o){return o.value4;})),
				 spSessionMax : data.cpuSPHistory.spSessionMax, //Math.max.apply(null, $.map(maxData, function(o){return o.value5;})),
//				 spSessionMaxUnit : Math.max.apply(null, $.map(maxData, function(o){return o.value6;})),
				 spSessionMaxUnit : data.cpuSPHistory.spSessionMaxUnit,
				 cursorColor : "#0f47c7",
			     cpuCheck : data.cpuNum,
			     interval : interval
			};	
			
			obchart.OBAreaChartSPDetailViewer(chartData, chartOption);	
		}		
	},
	
	//CPU SP별 사용률, Connectino DetailChart - Alteon
	GenerateSPCpuSessionChart : function(data, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.cpuSpConns != null)
			{
				chartDataList = data.cpuSpConns;
			}
			var maxData = [];
			
			var spValue1 = undefined; var spConns1 = undefined;
			var spValue2 = undefined; var spConns2 = undefined;
			var spValue3 = undefined; var spConns3 = undefined;
			var spValue4 = undefined; var spConns4 = undefined;
			var spValue5 = undefined; var spConns5 = undefined;
			var spValue6 = undefined; var spConns6 = undefined;
			var spValue7 = undefined; var spConns7 = undefined;
			var spValue8 = undefined; var spConns8 = undefined;
			var spValue9 = undefined; var spConns9 = undefined;
			var spValue10 = undefined; var spConns10 = undefined;
			var spValue11 = undefined; var spConns11 = undefined;
			var spValue12 = undefined; var spConns12 = undefined;
			var spValue13 = undefined; var spConns13 = undefined;
			var spValue14 = undefined; var spConns14 = undefined;
			var spValue15 = undefined; var spConns15 = undefined;
			var spValue16 = undefined; var spConns16 = undefined;
			var spValue17 = undefined; var spConns17 = undefined;
			var spValue18 = undefined; var spConns18 = undefined;
			var spValue19 = undefined; var spConns19 = undefined;
			var spValue20 = undefined; var spConns20 = undefined;
			var spValue21 = undefined; var spConns21 = undefined;
			var spValue22 = undefined; var spConns22 = undefined;
			var spValue23 = undefined; var spConns23 = undefined;
			var spValue24 = undefined; var spConns24 = undefined;
			var spValue25 = undefined; var spConns25 = undefined;
			var spValue26 = undefined; var spConns26 = undefined;
			var spValue27 = undefined; var spConns27 = undefined;
			var spValue28 = undefined; var spConns28 = undefined;
			var spValue29 = undefined; var spConns29 = undefined;
			var spValue30 = undefined; var spConns30 = undefined;
			var spValue31 = undefined; var spConns31 = undefined;
			var spValue32 = undefined; var spConns32 = undefined;
			
			if((chartDataList.cpu1Value > -1 && chartDataList.cpu1Value != null) || (chartDataList.cpu1Conns > -1 && chartDataList.cpu1Conns != null))
			{
				spValue1 = chartDataList.cpu1Value;	
				spConns1 = chartDataList.cpu1Conns;
				var dataObject = {
						"category": "MP",
						"column-1": spValue1,
//						"column-1": 55,
//						"column-2": spConns1
				    };
				chartData.push(dataObject);
				
//				var maxdataObject = 
//				{
//					"value" : spConns1
//				};				
//				maxData.push(maxdataObject);
			}						
			if((chartDataList.cpu2Value > -1 && chartDataList.cpu2Value != null) || (chartDataList.cpu2Conns > -1 && chartDataList.cpu2Conns != null))
			{
				spValue2 = chartDataList.cpu2Value;
				spConns2 = chartDataList.cpu2Conns;
				var dataObject1 = {
				    	"category": "SP1",
				    	"column-1": spValue2,
						"column-2": spConns2
//				    	"column-1": 45,
//						"column-2": 240000
				    };
				chartData.push(dataObject1);
				
				var maxdataObject1 = 
				{
					"value" : spConns2
				};				
				maxData.push(maxdataObject1);
			}
			if((chartDataList.cpu3Value > -1 && chartDataList.cpu3Value != null) || (chartDataList.cpu3Conns > -1 && chartDataList.cpu3Conns != null))
			{
				spValue3 = chartDataList.cpu3Value;
				spConns3 = chartDataList.cpu3Conns;
				var dataObject2 = {
				    	"category": "SP2",
				    	"column-1": spValue3,
						"column-2": spConns3
//				    	"column-1": 65,
//						"column-2": 340000
				    };
				chartData.push(dataObject2);
				
				var maxdataObject2 = 
				{
					"value" : spConns3
				};				
				maxData.push(maxdataObject2);
			}			
			if((chartDataList.cpu4Value > -1 && chartDataList.cpu4Value != null) || (chartDataList.cpu4Conns > -1 && chartDataList.cpu4Conns != null))
			{
				spValue4 = chartDataList.cpu4Value;
				spConns4 = chartDataList.cpu4Conns;
				var dataObject3 = {
				    	"category": "SP3",
				    	"column-1": spValue4,
						"column-2": spConns4
//				    	"column-1": 79,
//						"column-2": 300000
				    };
				chartData.push(dataObject3);
				
				var maxdataObject3 = 
				{
					"value" : spConns4
				};				
				maxData.push(maxdataObject3);
			}
			if((chartDataList.cpu5Value > -1 && chartDataList.cpu5Value != null) || (chartDataList.cpu5Conns > -1 && chartDataList.cpu5Conns != null))
			{
				spValue5 = chartDataList.cpu5Value;	
				spConns5 = chartDataList.cpu5Conns;
				var dataObject4 = {
				    	"category": "SP4",
				    	"column-1": spValue5,
						"column-2": spConns5
				    };
				chartData.push(dataObject4);
				
				var maxdataObject4 = 
				{
					"value" : spConns5
				};				
				maxData.push(maxdataObject4);
			}	
			if((chartDataList.cpu6Value > -1 && chartDataList.cpu6Value != null) || (chartDataList.cpu6Conns > -1 && chartDataList.cpu6Conns != null))
			{
				spValue6 = chartDataList.cpu6Value;	
				spConns6 = chartDataList.cpu6Conns;
				var dataObject5= {
				    	"category": "SP5",
				    	"column-1": spValue6,
						"column-2": spConns6
				    };
				chartData.push(dataObject5);
				
				var maxdataObject5 = 
				{
					"value" : spConns6
				};				
				maxData.push(maxdataObject5);
			}
			if((chartDataList.cpu7Value > -1 && chartDataList.cpu7Value != null) || (chartDataList.cpu7Conns > -1 && chartDataList.cpu7Conns != null))
			{
				spValue7 = chartDataList.cpu7Value;	
				spConns7 = chartDataList.cpu7Conns;
				var dataObject6= {
				    	"category": "SP6",
				    	"column-1": spValue7,
						"column-2": spConns7
				    };
				chartData.push(dataObject6);
				
				var maxdataObject6 = 
				{
					"value" : spConns7
				};				
				maxData.push(maxdataObject6);
			}
			if((chartDataList.cpu8Value > -1 && chartDataList.cpu8Value != null) || (chartDataList.cpu8Conns > -1 && chartDataList.cpu8Conns != null))
			{
				spValue8 = chartDataList.cpu8Value;	
				spConns8 = chartDataList.cpu8Conns;
				var dataObject7= {
				    	"category": "SP7",
				    	"column-1": spValue8,
						"column-2": spConns8
				    };
				chartData.push(dataObject7);
				
				var maxdataObject7 = 
				{
					"value" : spConns8
				};				
				maxData.push(maxdataObject7);
			}
			if((chartDataList.cpu9Value > -1 && chartDataList.cpu9Value != null) || (chartDataList.cpu9Conns > -1 && chartDataList.cpu9Conns != null))
			{
				spValue9 = chartDataList.cpu9Value;	
				spConns9 = chartDataList.cpu9Conns;
				var dataObject8= {
				    	"category": "SP8",
				    	"column-1": spValue9,
						"column-2": spConns9
				    };
				chartData.push(dataObject8);
				
				var maxdataObject8 = 
				{
					"value" : spConns9
				};				
				maxData.push(maxdataObject8);
			}
			if((chartDataList.cpu10Value > -1 && chartDataList.cpu10Value != null) || (chartDataList.cpu10Conns > -1 && chartDataList.cpu10Conns != null))
			{
				spValue10 = chartDataList.cpu10Value;	
				spConns10 = chartDataList.cpu10Conns;
				var dataObject9= {
				    	"category": "SP9",
				    	"column-1": spValue10,
						"column-2": spConns10
				    };
				chartData.push(dataObject9);
				
				var maxdataObject9 = 
				{
					"value" : spConns10
				};				
				maxData.push(maxdataObject9);
			}
			if((chartDataList.cpu11Value > -1 && chartDataList.cpu11Value != null) || (chartDataList.cpu11Conns > -1 && chartDataList.cpu11Conns != null))
			{
				spValue11 = chartDataList.cpu11Value;	
				spConns11 = chartDataList.cpu11Conns;
				var dataObject10= {
				    	"category": "SP10",
				    	"column-1": spValue11,
						"column-2": spConns11
				    };
				chartData.push(dataObject10);
				
				var maxdataObject10 = 
				{
					"value" : spConns11
				};				
				maxData.push(maxdataObject10);
			}
			if((chartDataList.cpu12Value > -1 && chartDataList.cpu12Value != null) || (chartDataList.cpu12Conns > -1 && chartDataList.cpu12Conns != null))
			{
				spValue12 = chartDataList.cpu12Value;	
				spConns12 = chartDataList.cpu12Conns;
				var dataObject11= {
				    	"category": "SP11",
				    	"column-1": spValue12,
						"column-2": spConns12
				    };
				chartData.push(dataObject11);
				
				var maxdataObject11 = 
				{
					"value" : spConns12
				};				
				maxData.push(maxdataObject11);
			}
			if((chartDataList.cpu13Value > -1 && chartDataList.cpu13Value != null) || (chartDataList.cpu13Conns > -1 && chartDataList.cpu13Conns != null))
			{
				spValue13 = chartDataList.cpu13Value;	
				spConns13 = chartDataList.cpu13Conns;
				var dataObject12= {
				    	"category": "SP12",
				    	"column-1": spValue13,
						"column-2": spConns13
				    };
				chartData.push(dataObject12);
				
				var maxdataObject12 = 
				{
					"value" : spConns13
				};				
				maxData.push(maxdataObject12);
			}
			if((chartDataList.cpu14Value > -1 && chartDataList.cpu14Value != null) || (chartDataList.cpu14Conns > -1 && chartDataList.cpu14Conns != null))
			{
				spValue14 = chartDataList.cpu14Value;	
				spConns14 = chartDataList.cpu14Conns;
				var dataObject13= {
				    	"category": "SP13",
				    	"column-1": spValue14,
						"column-2": spConns14
				    };
				chartData.push(dataObject13);
				
				var maxdataObject13 = 
				{
					"value" : spConns14
				};				
				maxData.push(maxdataObject13);
			}			
			if((chartDataList.cpu15Value > -1 && chartDataList.cpu15Value != null) || (chartDataList.cpu15Conns > -1 && chartDataList.cpu15Conns != null))
			{
				spValue15 = chartDataList.cpu15Value;	
				spConns15 = chartDataList.cpu15Conns;
				var dataObject14= {
				    	"category": "SP14",
				    	"column-1": spValue15,
						"column-2": spConns15
				    };
				chartData.push(dataObject14);
				
				var maxdataObject14 = 
				{
					"value" : spConns15
				};				
				maxData.push(maxdataObject14);
			}
			if((chartDataList.cpu16Value > -1 && chartDataList.cpu16Value != null) || (chartDataList.cpu16Conns > -1 && chartDataList.cpu16Conns != null))
			{
				spValue16 = chartDataList.cpu16Value;	
				spConns16 = chartDataList.cpu16Conns;
				var dataObject15= {
				    	"category": "SP15",
				    	"column-1": spValue16,
						"column-2": spConns16
				    };
				chartData.push(dataObject15);
				
				var maxdataObject15 = 
				{
					"value" : spConns16
				};				
				maxData.push(maxdataObject15);
			}
			if((chartDataList.cpu17Value > -1 && chartDataList.cpu17Value != null) || (chartDataList.cpu17Conns > -1 && chartDataList.cpu17Conns != null))
			{
				spValue17 = chartDataList.cpu17Value;	
				spConns17 = chartDataList.cpu17Conns;
				var dataObject16= {
				    	"category": "SP16",
				    	"column-1": spValue17,
						"column-2": spConns17
				    };
				chartData.push(dataObject16);
				
				var maxdataObject16 = 
				{
					"value" : spConns17
				};				
				maxData.push(maxdataObject16);
			}
			if((chartDataList.cpu18Value > -1 && chartDataList.cpu18Value != null) || (chartDataList.cpu18Conns > -1 && chartDataList.cpu18Conns != null))
			{
				spValue18 = chartDataList.cpu18Value;	
				spConns18 = chartDataList.cpu18Conns;
				var dataObject17= {
				    	"category": "SP17",
				    	"column-1": spValue18,
						"column-2": spConns18
				    };
				chartData.push(dataObject17);
				
				var maxdataObject17 = 
				{
					"value" : spConns18
				};				
				maxData.push(maxdataObject17);
			}
			if((chartDataList.cpu19Value > -1 && chartDataList.cpu19Value != null) || (chartDataList.cpu19Conns > -1 && chartDataList.cpu19Conns != null))
			{
				spValue19 = chartDataList.cpu19Value;	
				spConns19 = chartDataList.cpu19Conns;
				var dataObject18= {
				    	"category": "SP18",
				    	"column-1": spValue19,
						"column-2": spConns19
				    };
				chartData.push(dataObject18);
				
				var maxdataObject18 = 
				{
					"value" : spConns19
				};				
				maxData.push(maxdataObject18);
			}
			if((chartDataList.cpu20Value > -1 && chartDataList.cpu20Value != null) || (chartDataList.cpu20Conns > -1 && chartDataList.cpu20Conns != null))
			{
				spValue20 = chartDataList.cpu20Value;	
				spConns20 = chartDataList.cpu20Conns;
				var dataObject19= {
				    	"category": "SP19",
				    	"column-1": spValue20,
						"column-2": spConns20
				    };
				chartData.push(dataObject19);
				
				var maxdataObject19 = 
				{
					"value" : spConns20
				};				
				maxData.push(maxdataObject19);
			}						
			if((chartDataList.cpu21Value > -1 && chartDataList.cpu21Value != null) || (chartDataList.cpu21Conns > -1 && chartDataList.cpu21Conns != null))
			{
				spValue21 = chartDataList.cpu21Value;	
				spConns21 = chartDataList.cpu21Conns;
				var dataObject20 = {
						"category": "SP20",
						"column-1": spValue21,
						"column-2": spConns21
				    };
				chartData.push(dataObject20);
				
				var maxdataObject20 = 
				{
					"value" : spConns21
				};				
				maxData.push(maxdataObject20);
			}					
			if((chartDataList.cpu22Value > -1 && chartDataList.cpu22Value != null) || (chartDataList.cpu22Conns > -1 && chartDataList.cpu22Conns != null))
			{
				spValue22 = chartDataList.cpu22Value;
				spConns22 = chartDataList.cpu22Conns;
				var dataObject21 = {
				    	"category": "SP21",
				    	"column-1": spValue22,
						"column-2": spConns22
				    };
				chartData.push(dataObject21);
				
				var maxdataObject21 = 
				{
					"value" : spConns22
				};				
				maxData.push(maxdataObject21);
			}
			if((chartDataList.cpu23Value > -1 && chartDataList.cpu23Value != null) || (chartDataList.cpu23Conns > -1 && chartDataList.cpu23Conns != null))
			{
				spValue23 = chartDataList.cpu23Value;
				spConns23 = chartDataList.cpu23Conns;
				var dataObject22 = {
				    	"category": "SP22",
				    	"column-1": spValue23,
						"column-2": spConns23
				    };
				chartData.push(dataObject22);
				
				var maxdataObject22 = 
				{
					"value" : spConns23
				};				
				maxData.push(maxdataObject22);
			}			
			if((chartDataList.cpu24Value > -1 && chartDataList.cpu24Value != null) || (chartDataList.cpu24Conns > -1 && chartDataList.cpu24Conns != null))
			{
				spValue24 = chartDataList.cpu24Value;
				spConns24 = chartDataList.cpu24Conns;
				var dataObject23 = {
				    	"category": "SP23",
				    	"column-1": spValue24,
						"column-2": spConns24
				    };
				chartData.push(dataObject23);
				
				var maxdataObject23 = 
				{
					"value" : spConns24
				};				
				maxData.push(maxdataObject23);
			}
			if((chartDataList.cpu25Value > -1 && chartDataList.cpu25Value != null) || (chartDataList.cpu25Conns > -1 && chartDataList.cpu25Conns != null))
			{
				spValue25 = chartDataList.cpu25Value;	
				spConns25 = chartDataList.cpu25Conns;
				var dataObject24 = {
				    	"category": "SP24",
				    	"column-1": spValue25,
						"column-2": spConns25
				    };
				chartData.push(dataObject24);
				
				var maxdataObject24 = 
				{
					"value" : spConns25
				};				
				maxData.push(maxdataObject24);
			}	
			if((chartDataList.cpu26Value > -1 && chartDataList.cpu26Value != null) || (chartDataList.cpu26Conns > -1 && chartDataList.cpu26Conns != null))
			{
				spValue26 = chartDataList.cpu26Value;	
				spConns26 = chartDataList.cpu26Conns;
				var dataObject25= {
				    	"category": "SP25",
				    	"column-1": spValue26,
						"column-2": spConns26
				    };
				chartData.push(dataObject25);
				
				var maxdataObject25 = 
				{
					"value" : spConns26
				};				
				maxData.push(maxdataObject25);
			}
			if((chartDataList.cpu27Value > -1 && chartDataList.cpu27Value != null) || (chartDataList.cpu27Conns > -1 && chartDataList.cpu27Conns != null))
			{
				spValue27 = chartDataList.cpu27Value;	
				spConns27 = chartDataList.cpu27Conns;
				var dataObject26= {
				    	"category": "SP26",
				    	"column-1": spValue27,
						"column-2": spConns27
				    };
				chartData.push(dataObject26);
				
				var maxdataObject26 = 
				{
					"value" : spConns27
				};				
				maxData.push(maxdataObject26);
			}
			if((chartDataList.cpu28Value > -1 && chartDataList.cpu28Value != null) || (chartDataList.cpu28Conns > -1 && chartDataList.cpu28Conns != null))
			{
				spValue28 = chartDataList.cpu28Value;	
				spConns28 = chartDataList.cpu28Conns;
				var dataObject27= {
				    	"category": "SP27",
				    	"column-1": spValue28,
						"column-2": spConns28
				    };
				chartData.push(dataObject27);
				
				var maxdataObject27 = 
				{
					"value" : spConns28
				};				
				maxData.push(maxdataObject27);
			}
			if((chartDataList.cpu29Value > -1 && chartDataList.cpu29Value != null) || (chartDataList.cpu29Conns > -1 && chartDataList.cpu29Conns != null))
			{
				spValue29 = chartDataList.cpu29Value;	
				spConns29 = chartDataList.cpu29Conns;
				var dataObject28= {
				    	"category": "SP28",
				    	"column-1": spValue29,
						"column-2": spConns29
				    };
				chartData.push(dataObject28);
				
				var maxdataObject28 = 
				{
					"value" : spConns29
				};				
				maxData.push(maxdataObject28);
			}
			if((chartDataList.cpu30Value > -1 && chartDataList.cpu30Value != null) || (chartDataList.cpu30Conns > -1 && chartDataList.cpu30Conns != null))
			{
				spValue30 = chartDataList.cpu30Value;	
				spConns30 = chartDataList.cpu30Conns;
				var dataObject29= {
				    	"category": "SP29",
				    	"column-1": spValue30,
						"column-2": spConns30
				    };
				chartData.push(dataObject29);
				
				var maxdataObject29 = 
				{
					"value" : spConns30
				};				
				maxData.push(maxdataObject29);
			}
			if((chartDataList.cpu31Value > -1 && chartDataList.cpu31Value != null) || (chartDataList.cpu31Conns > -1 && chartDataList.cpu31Conns != null))
			{
				spValue31 = chartDataList.cpu31Value;	
				spConns31 = chartDataList.cpu31Conns;
				var dataObject30= {
				    	"category": "SP30",
				    	"column-1": spValue31,
						"column-2": spConns31
				    };
				chartData.push(dataObject30);
				
				var maxdataObject30 = 
				{
					"value" : spConns31
				};				
				maxData.push(maxdataObject30);
			}
			if((chartDataList.cpu32Value > -1 && chartDataList.cpu32Value != null) || (chartDataList.cpu32Conns > -1 && chartDataList.cpu32Conns != null))
			{
				spValue32 = chartDataList.cpu32Value;	
				spConns32 = chartDataList.cpu32Conns;
				var dataObject31= {
				    	"category": "SP31",
				    	"column-1": spValue32,
						"column-2": spConns32
				    };
				chartData.push(dataObject31);
				
				var maxdataObject31 = 
				{
					"value" : spConns32
				};				
				maxData.push(maxdataObject31);
			}
			
			if((chartDataList.spSessionMax == -1) || (chartDataList.spSessionMax == undefined))
			{
				var dataObject= {
				    	"category": "CPU",
				    	"column-1": undefined,
						"column-2": undefined
				    };
				chartData.push(dataObject);
			}
//			var maxData = [];
//			var maxdataObject = 
//			{			
////				value : chartDataList.spSessionMax  // 임계치값
////				value2 : chartDataList.cpuAvgValue
////				value2 : chartDataList.spSessionMaxUnit // 임계치값 unit
//			};
//			
//			maxData.push(maxdataObject);
			 
//			alert(chartDataList.spSessionMin);
//			var spSessionMin = chartDataList.spSessionMin -  (chartDataList.spSessionMin * 0.00001)
//			alert(spSessionMin);
			
			var spUsageMin = 0;
			if(chartDataList.spUsageMin > 0)
			{
				spUsageMin = chartDataList.spUsageMin -  (chartDataList.spUsageMin * 0.00001);
			}
			else
			{
				spUsageMin = chartDataList.spUsageMin;
			}					
			
			var spSessionMin = 0;
			if(chartDataList.spSessionMin > 0)
			{
				spSessionMin = chartDataList.spSessionMin -  (chartDataList.spSessionMin * 0.00001);
			}
			else
			{
				spSessionMin = chartDataList.spSessionMin;
			}
							
			var chartOption =
			{
//				 min : 0,
				 min : spUsageMin,
//				 min : chartDataList.spUsageMin,
				 max : 100,
				 linecolor : "#6cb8c8",
				 chartname : "detailChart",
				 axistitle1 : "%",
				 axistitle2 : VAR_COMMON_AFEW,
			
				 minSession : spSessionMin,
//				 minSession : chartDataList.spSessionMin,
				 maxPos : Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
				 spSessionMax : chartDataList.spSessionMax, //Math.max.apply(null, $.map(maxData, function(o){return o.value;})),		
//				 spSessionMaxUnit : $.map(maxData, function(o){return o.value2;}),
				 spSessionMaxUnit : chartDataList.spSessionMaxUnit,								 
//				 avgPos : Math.max.apply(null, $.map(maxData, function(o){return o.value2;})),
				 cursorColor : "#0f47c7",
				 interval : interval
			};
			
			obchart.OBAreaChartSPViewer(chartData, chartOption);				
		}		
	},
	//Memory Chart
	GenerateMemHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.memHistory != null)
			{
				chartDataList = data.memHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.value < 0 )
						{					
						}											
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}						
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			 
			 var chartOption =
				{
					 min : 0,
					 max : 100,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : "%",
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Memory",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);			
		}		
	},
	//Packet Error 수 Chart
	GeneratePktErrHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.pktErrHistory != null)
			{
				chartDataList = data.pktErrHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.value < 0 )
						{					
						}											
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}						
					}				
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			
			 var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : VAR_COMMON_AFEW,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Error Packets",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);
			
		}	
	},
	//Packet Drop 수 Chart
	GeneratePktDropHistoryChart : function(data, chartnameInput, interval)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.pktDropHistory != null)
			{
				chartDataList = data.pktDropHistory.history;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						if (column.value < 0 )
						{					
						}											
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			 }
			 else
			 {
				 var startTime = parseDateTime(data.startTime);
				 var endTime = parseDateTime(data.endTime);
				 var dataObject =
				 {
						 occurredTime : startTime
				 };
				 chartData.push(dataObject);
				 var dataObject =
				 {
						 occurredTime : endTime
				 };
				 chartData.push(dataObject);
			 }			
			 var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartnameInput,
					 axistitle : VAR_COMMON_AFEW,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Dropped Packets",
					 interval : interval
				};
			 obchart.OBAreaChartViewer(chartData, chartOption);			
		}
	},
});