var AdcAlert = Class.create({
	initialize : function() 
	{		
		this.configLevel = undefined;
		this.adc = {};
	},
	setAdc : function(adcObj)
	{
		var adcVendor = undefined;
		if (adcObj.type == "F5")
		{
			adcVendor = 1;
		}
		else if (adcObj.type == "Alteon")
		{
			adcVendor = 2;
		}
		else if (adcObj.type == "PAS")
		{
			adcVendor = 3;
		}
		else if (adcObj.type == "PASK")
		{
			adcVendor = 4;
		}
		else
		{				
		}
		this.adc.index = adcObj.index;
		this.adc.name = adcObj.name;
		this.adc.type = adcObj.type;
		this.adc.vendor = adcVendor;
	},
	onAdcChange : function() 
	{
		this.setAdc(adcSetting.getAdc());
		this.loadAdcAlertListContent();
	},	
	loadAdcAlertListContent : function() 
	{
		with (this) 
		{	
			if (!adcSetting.isAdcSet()) 
			{
				$('#wrap .contents').empty();
				return;
			}
			setAdc(adcSetting.getAdc());
			ajaxManager.runHtmlExt({
				url : "adc/alert/loadAdcAlertListContent.action",
				data : 
				{
					"adcObject.index" : adc.index,
					"adcObject.name" : adc.name,
					"adcObject.vendor" : adc.vendor,
					"adc.type" : adc.type,
					"adcObject.category" : 2
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('AdcAlert');					
					registerAdcAlertListContentEvents();
					chkBoxControl();
					
					configLevel = $('.configLevelHidden').val();
					if (configLevel != 2)
					{
						$('.contents_area .alertTableArea').find('*').each(function (){
							$(this).attr("disabled", true);
						});						
					}					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ALERT_LOAD, jqXhr);
				}
			});
		}
	},
	// Radio Btn 전체 선택으로 변경
	loadAdcAlertListToGlobalContent : function() 
	{
		with (this) 
		{	
			if (!adcSetting.isAdcSet()) 
			{
				$('#wrap .contents').empty();
				return;
			}
			
			ajaxManager.runHtmlExt({
				url : "adc/alert/loadAdcAlertListToGlobalContent.action",
				data : 
				{
					"adcObject.index" : adc.index,
					"adcObject.name" : adc.name,
					"adcObject.vendor" : adc.vendor,
					"adc.type" : adc.type,
					"adcObject.category" : 2
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('AdcAlert');					
					registerAdcAlertListContentEvents();
					
					configLevel = $('.configLevelHidden').val();
					if (configLevel != 2)
					{
						$('.contents_area .alertTableArea').find('*').each(function (){
							$(this).attr("disabled", true);
						});
					}					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ALERT_LOAD, jqXhr);
				}
			});
		}
	},
	// Radio Btn 그룹 선택으로 변경
	loadAdcAlertListToGroupContent : function() 
	{
		with (this) 
		{	
			if (!adcSetting.isAdcSet()) 
			{
				$('#wrap .contents').empty();
				return;
			}
			
			ajaxManager.runHtmlExt({
				url : "adc/alert/loadAdcAlertListToGroupContent.action",
				data : 
				{
					"adcObject.index" : adc.index,
					"adcObject.name" : adc.name,
					"adcObject.vendor" : adc.vendor,
					"adc.type" : adc.type,
					"adcObject.category" : 2
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('AdcAlert');					
					registerAdcAlertListContentEvents();					
					
					configLevel = $('.configLevelHidden').val();
					if (configLevel != 2)
					{
						$('.contents_area .alertTableArea').find('*').each(function (){
							$(this).attr("disabled", true);
						});
					}					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ALERT_LOAD, jqXhr);
				}
			});
		}
	},
	loadInitAdcAlertConfiguration : function() 
	{
		with (this) 
		{			
			ajaxManager.runJsonExt({
				url : "adc/alert/loadInitAdcAlertConfiguration.action",
				data : 
				{
					"adcObject.index" : adc.index,
					"adcObject.name" : adc.name,
					"adcObject.vendor" : adc.vendor,
					"adc.type" : adc.type,
					"adcObject.category" : 2
				},				
				successFn : function(data) 
				{
					if(data.isSuccessful) 
					{						
						loadAdcAlertListContent();
					} 
					else 
					{	
						$.obAlertNotice(data.message);
					}					
				},
				errorFn : function(a,b,c)
				{
				}
			});
		}
	},
	chkBoxControl : function()
	{
		with(this)
		{
			$('.enableChk').each(function(e)
			{
				var isChecked = $(this).is(':checked');
				if (isChecked == true)
				{
					$(this).parent().parent().find('.syslogChk').attr("disabled", false);					
					$(this).parent().parent().find('.snmptrapChk').attr("disabled", false);
					$(this).parent().parent().find('.smsChk').attr("disabled", false);
				}
				else
				{
					$(this).parent().parent().find('.syslogChk').attr("disabled", true);
					$(this).parent().parent().find('.syslogChk').attr("checked", false);
					
					$(this).parent().parent().find('.snmptrapChk').attr("disabled", true);
					$(this).parent().parent().find('.snmptrapChk').attr("checked", false);

					$(this).parent().parent().find('.smsChk').attr("disabled", true);
					$(this).parent().parent().find('.smsChk').attr("checked", false);
				}
			});		
		}
	},
	
	registerAdcAlertListContentEvents : function() 
	{
		with (this) 
		{
			$('.userconfig-btn').click(function(e) 
			{
				e.preventDefault();
				sysSetting.loadLeftPane();
				sysSetting.loadContent();				
			});
			
			$('.syssetting-btn').click(function(e) 
			{
				e.preventDefault();
				sysSetting.loadLeftPane();
				config.loadConfigContent();
			});		
			
			$('.configCngBtn').click(function(e) 
			{
				configLevel = $(this).val();
				if (configLevel == 0)	// 전체 선택
				{
					loadAdcAlertListToGlobalContent();
				}
				else if (configLevel == 1) // 그룹 선택
				{
					loadAdcAlertListToGroupContent();
				}
				else if (configLevel == 2) // 개별 선택
				{
					$('.contents_area .alertTableArea').find('*').each(function (){
						$(this).attr("disabled", false);
					});					
					$('.configLevelHidden').val(configLevel);
					chkBoxControl();
				}
				else
				{					
				}
			});
			// 설정초기화 기능 차단
			/*$('.adcAlertinitLnk').click(function(e) 
			{
				e.preventDefault();
				loadInitAdcAlertConfiguration();			
			});*/
			
			$('.adcAlertOkLnk').click(function(e) 
			{
				e.preventDefault();
				$('#adcAlertFrm').submit();				
			});
			
			$('#adcAlertFrm').submit(function() 
			{
				var params = 
				{
					"adcObject.index" : adc.index,
					"adcObject.name" : adc.name,
					"adcObject.vendor" : adc.vendor,
					"adc.type" : adc.type,
					"adcObject.category" : 2
				};
				
				if (!validateData())
				{
					return false;
				}
				$(this).ajaxSubmit({
					dataType : 'json',
					url : 'adc/alert/modifyAdcAlertContent.action',
					data : params,					
					success : function(data)
					{
						if(data.isSuccessful) 
						{
							$.obAlertNotice(VAR_COMMON_REGISUCCESS);
							loadAdcAlertListContent();
						} 
						else 
						{	
							$.obAlertNotice(data.message);
						}
					},
					error : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_ALERT_SETFAIL, jqXhr);
					}
				});			
				
				return false;
			});	
			
			$('.enableChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				if (isChecked == true)
				{
					$(this).parent().parent().find('.syslogChk').attr("disabled", false);					
					$(this).parent().parent().find('.snmptrapChk').attr("disabled", false);
					$(this).parent().parent().find('.smsChk').attr("disabled", false);
				}
				else
				{
					$(this).parent().parent().find('.syslogChk').attr("disabled", true);
					$(this).parent().parent().find('.syslogChk').attr("checked", false);
					
					$(this).parent().parent().find('.snmptrapChk').attr("disabled", true);
					$(this).parent().parent().find('.snmptrapChk').attr("checked", false);

					$(this).parent().parent().find('.smsChk').attr("disabled", true);
					$(this).parent().parent().find('.smsChk').attr("checked", false);
				}
			});
			
			// 장애경보 allChk
			$('.allEnableChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.enableChk').attr('checked', isChecked);
				if (isChecked == true)
				{
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", false);
					$('.allSyslogChk').attr("disabled", false);
					
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", false);
					$('.allSnmpTrapChk').attr("disabled", false);

					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", false);
					$('.allSMSChk').attr("disabled", false);
				}
				else
				{
					$('.allSyslogChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("checked", false);
					
					$('.allSnmpTrapChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("checked", false);
					
					$('.allSMSChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("checked", false);

					$('input[name="alarmConfigs.adcDisconnectAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcBootAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcStandbyAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcActiveAction.syslog"]').val("0");
					$('input[name="alarmConfigs.virtualServerDownAction.syslog"]').val("0");					
					$('input[name="alarmConfigs.poolMemberDownAction.syslog"]').val("0");	
					$('input[name="alarmConfigs.vrrpCollisionAction.syslog"]').val("0");
					$('input[name="alarmConfigs.gatewayDownAction.syslog"]').val("0");
					$('input[name="alarmConfigs.linkDownAction.syslog"]').val("0");					
					$('input[name="alarmConfigs.redundancyCheckAction.syslog"]').val("0");
				}
				
				if (isChecked == true)
				{			
					$('input[name="alarmConfigs.adcDisconnectAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcBootAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcStandbyAction.enable"]').val("1");					
					$('input[name="alarmConfigs.adcActiveAction.enable"]').val("1");
					$('input[name="alarmConfigs.virtualServerDownAction.enable"]').val("1");
					$('input[name="alarmConfigs.vrrpCollisionAction.enable"]').val("1");
					$('input[name="alarmConfigs.gatewayDownAction.enable"]').val("1");
					$('input[name="alarmConfigs.poolMemberDownAction.enable"]').val("1");
					$('input[name="alarmConfigs.linkDownAction.enable"]').val("1");								
					
					$('input[name="alarmConfigs.redundancyCheckAction.enable"]').val("1");
				}
				else 
				{
					$('input[name="alarmConfigs.adcDisconnectAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcBootAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcStandbyAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcActiveAction.enable"]').val("0");
					$('input[name="alarmConfigs.virtualServerDownAction.enable"]').val("0");					
					$('input[name="alarmConfigs.poolMemberDownAction.enable"]').val("0");		
					$('input[name="alarmConfigs.vrrpCollisionAction.enable"]').val("0");
					$('input[name="alarmConfigs.gatewayDownAction.enable"]').val("0");
					$('input[name="alarmConfigs.linkDownAction.enable"]').val("0");
					
					$('input[name="alarmConfigs.redundancyCheckAction.enable"]').val("0");
				}
			});

			$('.allSyslogChk').click(function(e) 
			{			
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.syslogChk').attr('checked', isChecked);
				
				if (isChecked == true)
				{										
					$('input[name="alarmConfigs.adcDisconnectAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcBootAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcStandbyAction.syslog"]').val("1");					
					$('input[name="alarmConfigs.adcActiveAction.syslog"]').val("1");
					$('input[name="alarmConfigs.virtualServerDownAction.syslog"]').val("1");
					$('input[name="alarmConfigs.poolMemberDownAction.syslog"]').val("1");
					$('input[name="alarmConfigs.vrrpCollisionAction.syslog"]').val("1");
					$('input[name="alarmConfigs.gatewayDownAction.syslog"]').val("1");
					$('input[name="alarmConfigs.linkDownAction.syslog"]').val("1");	
					
					$('input[name="alarmConfigs.redundancyCheckAction.syslog"]').val("1");
				}
				else 
				{					
					$('input[name="alarmConfigs.adcDisconnectAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcBootAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcStandbyAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcActiveAction.syslog"]').val("0");
					$('input[name="alarmConfigs.virtualServerDownAction.syslog"]').val("0");					
					$('input[name="alarmConfigs.poolMemberDownAction.syslog"]').val("0");	
					$('input[name="alarmConfigs.vrrpCollisionAction.syslog"]').val("0");
					$('input[name="alarmConfigs.gatewayDownAction.syslog"]').val("0");
					$('input[name="alarmConfigs.linkDownAction.syslog"]').val("0");
					
					$('input[name="alarmConfigs.redundancyCheckAction.syslog"]').val("0");
				}
			});	
			
			$('.allSnmpTrapChk').click(function(e) 
			{			
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.snmptrapChk').attr('checked', isChecked);
				
				if (isChecked == true)
				{										
					$('input[name="alarmConfigs.adcDisconnectAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcBootAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcStandbyAction.snmptrap"]').val("1");					
					$('input[name="alarmConfigs.adcActiveAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.virtualServerDownAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.poolMemberDownAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.vrrpCollisionAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.gatewayDownAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.linkDownAction.snmptrap"]').val("1");	
					
					$('input[name="alarmConfigs.redundancyCheckAction.snmptrap"]').val("1");
				}
				else 
				{					
					$('input[name="alarmConfigs.adcDisconnectAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcBootAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcStandbyAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcActiveAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.virtualServerDownAction.snmptrap"]').val("0");					
					$('input[name="alarmConfigs.poolMemberDownAction.snmptrap"]').val("0");	
					$('input[name="alarmConfigs.vrrpCollisionAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.gatewayDownAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.linkDownAction.snmptrap"]').val("0");
					
					$('input[name="alarmConfigs.redundancyCheckAction.snmptrap"]').val("0");
				}
			});	
			
			$('.allSMSChk').click(function(e) 
			{			
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.smsChk').attr('checked', isChecked);
				
				if (isChecked == true)
				{										
					$('input[name="alarmConfigs.adcDisconnectAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcBootAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcStandbyAction.sms"]').val("1");					
					$('input[name="alarmConfigs.adcActiveAction.sms"]').val("1");
					$('input[name="alarmConfigs.virtualServerDownAction.sms"]').val("1");
					$('input[name="alarmConfigs.poolMemberDownAction.sms"]').val("1");
					$('input[name="alarmConfigs.vrrpCollisionAction.sms"]').val("1");
					$('input[name="alarmConfigs.gatewayDownAction.sms"]').val("1");
					$('input[name="alarmConfigs.linkDownAction.sms"]').val("1");	
					
					$('input[name="alarmConfigs.redundancyCheckAction.snmptrap"]').val("1");
				}
				else 
				{					
					$('input[name="alarmConfigs.adcDisconnectAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcBootAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcStandbyAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcActiveAction.sms"]').val("0");
					$('input[name="alarmConfigs.virtualServerDownAction.sms"]').val("0");					
					$('input[name="alarmConfigs.poolMemberDownAction.sms"]').val("0");	
					$('input[name="alarmConfigs.vrrpCollisionAction.sms"]').val("0");
					$('input[name="alarmConfigs.gatewayDownAction.sms"]').val("0");
					$('input[name="alarmConfigs.linkDownAction.sms"]').val("0");
					
					$('input[name="alarmConfigs.redundancyCheckAction.sms"]').val("0");
				}
			});	

			// ADC 성능정보 allChk
			$('.allEnableChk1').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.enableChk').attr('checked', isChecked);
				if (isChecked == true)
				{
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", false);
					$('.allSyslogChk1').attr("disabled", false);
					
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", false);
					$('.allSnmpTrapChk1').attr("disabled", false);

					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", false);
					$('.allSMSChk1').attr("disabled", false);
				}
				else
				{
					$('.allSyslogChk1').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("checked", false);
					
					$('.allSnmpTrapChk1').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("checked", false);
					
					$('.allSMSChk1').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("checked", false);

					$('input[name="alarmConfigs.adcCpuAction.enable"]').val("0");							
					$('input[name="alarmConfigs.adcMPAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcSPAction.enable"]').val("0");							
					$('input[name="alarmConfigs.adcMemAction.enable"]').val("0");
					$('input[name="alarmConfigs.connectionLimitHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.connectionLimitLowAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcSslTransAction.enable"]').val("0");
					$('input[name="alarmConfigs.responseTimeAction.enable"]').val("0");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.enable"]').val("0");
				}
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcCpuAction.enable"]').val("1");							
					$('input[name="alarmConfigs.adcMPAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcSPAction.enable"]').val("1");							
					$('input[name="alarmConfigs.adcMemAction.enable"]').val("1");
					$('input[name="alarmConfigs.connectionLimitHighAction.enable"]').val("1");
					$('input[name="alarmConfigs.connectionLimitLowAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcSslTransAction.enable"]').val("1");
					$('input[name="alarmConfigs.responseTimeAction.enable"]').val("1");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.enable"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.adcCpuAction.enable"]').val("0");							
					$('input[name="alarmConfigs.adcMPAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcSPAction.enable"]').val("0");							
					$('input[name="alarmConfigs.adcMemAction.enable"]').val("0");
					$('input[name="alarmConfigs.connectionLimitHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.connectionLimitLowAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcSslTransAction.enable"]').val("0");
					$('input[name="alarmConfigs.responseTimeAction.enable"]').val("0");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.enable"]').val("0");
				}
			});			
			
			$('.allSyslogChk1').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.syslogChk').attr('checked', isChecked);
	
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcCpuAction.syslog"]').val("1");							
					$('input[name="alarmConfigs.adcMPAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcSPAction.syslog"]').val("1");							
					$('input[name="alarmConfigs.adcMemAction.syslog"]').val("1");
					$('input[name="alarmConfigs.connectionLimitHighAction.syslog"]').val("1");
					$('input[name="alarmConfigs.connectionLimitLowAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcSslTransAction.syslog"]').val("1");
					$('input[name="alarmConfigs.responseTimeAction.syslog"]').val("1");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.syslog"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.adcCpuAction.syslog"]').val("0");							
					$('input[name="alarmConfigs.adcMPAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcSPAction.syslog"]').val("0");							
					$('input[name="alarmConfigs.adcMemAction.syslog"]').val("0");
					$('input[name="alarmConfigs.connectionLimitHighAction.syslog"]').val("0");
					$('input[name="alarmConfigs.connectionLimitLowAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcSslTransAction.syslog"]').val("0");
					$('input[name="alarmConfigs.responseTimeAction.syslog"]').val("0");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.syslog"]').val("0");
				}
			});		
			
			$('.allSnmpTrapChk1').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.snmptrapChk').attr('checked', isChecked);
	
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcCpuAction.snmptrap"]').val("1");							
					$('input[name="alarmConfigs.adcMPAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcSPAction.snmptrap"]').val("1");							
					$('input[name="alarmConfigs.adcMemAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.connectionLimitHighAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.connectionLimitLowAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcSslTransAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.responseTimeAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.snmptrap"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.adcCpuAction.snmptrap"]').val("0");							
					$('input[name="alarmConfigs.adcMPAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcSPAction.snmptrap"]').val("0");							
					$('input[name="alarmConfigs.adcMemAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.connectionLimitHighAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.connectionLimitLowAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcSslTransAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.responseTimeAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.snmptrap"]').val("0");
				}
			});
			
			$('.allSMSChk1').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.smsChk').attr('checked', isChecked);
	
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcCpuAction.sms"]').val("1");							
					$('input[name="alarmConfigs.adcMPAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcSPAction.sms"]').val("1");							
					$('input[name="alarmConfigs.adcMemAction.sms"]').val("1");
					$('input[name="alarmConfigs.connectionLimitHighAction.sms"]').val("1");
					$('input[name="alarmConfigs.connectionLimitLowAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcSslTransAction.sms"]').val("1");
					$('input[name="alarmConfigs.responseTimeAction.sms"]').val("1");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.sms"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.adcCpuAction.sms"]').val("0");							
					$('input[name="alarmConfigs.adcMPAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcSPAction.sms"]').val("0");							
					$('input[name="alarmConfigs.adcMemAction.sms"]').val("0");
					$('input[name="alarmConfigs.connectionLimitHighAction.sms"]').val("0");
					$('input[name="alarmConfigs.connectionLimitLowAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcSslTransAction.sms"]').val("0");
					$('input[name="alarmConfigs.responseTimeAction.sms"]').val("0");
					$('input[name="alarmConfigs.filterSessionLimitHighAction.sms"]').val("0");
				}
			});

			// 기간정보 allChk
			$('.allEnableChk2').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.enableChk').attr('checked', isChecked);
				if (isChecked == true)
				{
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", false);
					$('.allSyslogChk2').attr("disabled", false);
					
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", false);
					$('.allSnmpTrapChk2').attr("disabled", false);

					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", false);
					$('.allSMSChk2').attr("disabled", false);
				}
				else
				{
					$('.allSyslogChk2').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("checked", false);
					
					$('.allSnmpTrapChk2').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("checked", false);
					
					$('.allSMSChk2').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("checked", false);

					$('input[name="alarmConfigs.adcUptimeAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcPurchaseAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcSslcertAction.enable"]').val("0");
				}
					
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcUptimeAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcPurchaseAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcSslcertAction.enable"]').val("1");				
				}
				else 
				{							
					$('input[name="alarmConfigs.adcUptimeAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcPurchaseAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcSslcertAction.enable"]').val("0");				
				}
			});
			
			$('.allSyslogChk2').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.syslogChk').attr('checked', isChecked);
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcUptimeAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcPurchaseAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcSslcertAction.syslog"]').val("1");				
				}
				else 
				{							
					$('input[name="alarmConfigs.adcUptimeAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcPurchaseAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcSslcertAction.syslog"]').val("0");				
				}
			});
			
			$('.allSnmpTrapChk2').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.snmptrapChk').attr('checked', isChecked);
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcUptimeAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcPurchaseAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcSslcertAction.snmptrap"]').val("1");				
				}
				else 
				{							
					$('input[name="alarmConfigs.adcUptimeAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcPurchaseAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcSslcertAction.snmptrap"]').val("0");				
				}
			});
			
			$('.allSMSChk2').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.smsChk').attr('checked', isChecked);
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.adcUptimeAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcPurchaseAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcSslcertAction.sms"]').val("1");				
				}
				else 
				{							
					$('input[name="alarmConfigs.adcUptimeAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcPurchaseAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcSslcertAction.sms"]').val("0");				
				}
			});

			// ADC 시스템 정보 allChk
			$('.allEnableChk3').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.enableChk').attr('checked', isChecked);
				if (isChecked == true)
				{
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", false);
					$('.allSyslogChk3').attr("disabled", false);
					
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", false);
					$('.allSnmpTrapChk3').attr("disabled", false);
					
					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", false);
					$('.allSMSChk3').attr("disabled", false);
				}
				else
				{
					$('.allSyslogChk3').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.syslogChk').attr("checked", false);
					
					$('.allSnmpTrapChk3').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.snmptrapChk').attr("checked", false);
					
					$('.allSMSChk3').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("disabled", true);
					$(this).parent().parent().parent().parent().find('.smsChk').attr("checked", false);

					$('input[name="alarmConfigs.interfaceErrorAction.enable"]').val("0");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.enable"]').val("0");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.enable"]').val("0");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.enable"]').val("0");
					$('input[name="alarmConfigs.temperatureTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.fanNotOperationalAction.enable"]').val("0");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.enable"]').val("0");
					$('input[name="alarmConfigs.cpuTempTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.enable"]').val("0");
					$('input[name="alarmConfigs.cpuFanBadAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisTempTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisFanBadAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.enable"]').val("0");
					$('input[name="alarmConfigs.voltageTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.enable"]').val("0");					
					$('input[name="alarmConfigs.blockDDoSAction.enable"]').val("0");
				}
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.interfaceErrorAction.enable"]').val("1");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.enable"]').val("1");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.enable"]').val("1");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.enable"]').val("1");
					$('input[name="alarmConfigs.temperatureTooHighAction.enable"]').val("1");
					$('input[name="alarmConfigs.fanNotOperationalAction.enable"]').val("1");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.enable"]').val("1");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.enable"]').val("1");
					$('input[name="alarmConfigs.cpuTempTooHighAction.enable"]').val("1");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.enable"]').val("1");
					$('input[name="alarmConfigs.cpuFanBadAction.enable"]').val("1");
					$('input[name="alarmConfigs.chassisTempTooHighAction.enable"]').val("1");
					$('input[name="alarmConfigs.chassisFanBadAction.enable"]').val("1");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.enable"]').val("1");
					$('input[name="alarmConfigs.voltageTooHighAction.enable"]').val("1");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.enable"]').val("1");
					$('input[name="alarmConfigs.blockDDoSAction.enable"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.interfaceErrorAction.enable"]').val("0");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.enable"]').val("0");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.enable"]').val("0");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.enable"]').val("0");
					$('input[name="alarmConfigs.temperatureTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.fanNotOperationalAction.enable"]').val("0");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.enable"]').val("0");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.enable"]').val("0");
					$('input[name="alarmConfigs.cpuTempTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.enable"]').val("0");
					$('input[name="alarmConfigs.cpuFanBadAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisTempTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisFanBadAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.enable"]').val("0");
					$('input[name="alarmConfigs.voltageTooHighAction.enable"]').val("0");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.enable"]').val("0");					
					$('input[name="alarmConfigs.blockDDoSAction.enable"]').val("0");			
				}
			});
			
			$('.allSyslogChk3').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.syslogChk').attr('checked', isChecked);
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.interfaceErrorAction.syslog"]').val("1");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.syslog"]').val("1");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.syslog"]').val("1");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.syslog"]').val("1");
					$('input[name="alarmConfigs.temperatureTooHighAction.syslog"]').val("1");
					$('input[name="alarmConfigs.fanNotOperationalAction.syslog"]').val("1");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.syslog"]').val("1");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.syslog"]').val("1");
					$('input[name="alarmConfigs.cpuTempTooHighAction.syslog"]').val("1");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.syslog"]').val("1");
					$('input[name="alarmConfigs.cpuFanBadAction.syslog"]').val("1");
					$('input[name="alarmConfigs.chassisTempTooHighAction.syslog"]').val("1");
					$('input[name="alarmConfigs.chassisFanBadAction.syslog"]').val("1");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.syslog"]').val("1");
					$('input[name="alarmConfigs.voltageTooHighAction.syslog"]').val("1");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.syslog"]').val("1");
					$('input[name="alarmConfigs.blockDDoSAction.syslog"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.interfaceErrorAction.syslog"]').val("0");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.syslog"]').val("0");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.syslog"]').val("0");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.syslog"]').val("0");
					$('input[name="alarmConfigs.temperatureTooHighAction.syslog"]').val("0");
					$('input[name="alarmConfigs.fanNotOperationalAction.syslog"]').val("0");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.syslog"]').val("0");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.syslog"]').val("0");
					$('input[name="alarmConfigs.cpuTempTooHighAction.syslog"]').val("0");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.syslog"]').val("0");
					$('input[name="alarmConfigs.cpuFanBadAction.syslog"]').val("0");
					$('input[name="alarmConfigs.chassisTempTooHighAction.syslog"]').val("0");
					$('input[name="alarmConfigs.chassisFanBadAction.syslog"]').val("0");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.syslog"]').val("0");
					$('input[name="alarmConfigs.voltageTooHighAction.syslog"]').val("0");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.syslog"]').val("0");
					$('input[name="alarmConfigs.blockDDoSAction.syslog"]').val("0");			
				}
			});	
			
			$('.allSnmpTrapChk3').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.snmptrapChk').attr('checked', isChecked);
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.interfaceErrorAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.temperatureTooHighAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.fanNotOperationalAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.cpuTempTooHighAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.cpuFanBadAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.chassisTempTooHighAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.chassisFanBadAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.voltageTooHighAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.snmptrap"]').val("1");
					$('input[name="alarmConfigs.blockDDoSAction.snmptrap"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.interfaceErrorAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.temperatureTooHighAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.fanNotOperationalAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.cpuTempTooHighAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.cpuFanBadAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.chassisTempTooHighAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.chassisFanBadAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.voltageTooHighAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.blockDDoSAction.snmptrap"]').val("0");			
				}
			});	

			$('.allSMSChk3').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.smsChk').attr('checked', isChecked);
			
				if (isChecked == true)
				{
					$('input[name="alarmConfigs.interfaceErrorAction.sms"]').val("1");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.sms"]').val("1");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.sms"]').val("1");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.sms"]').val("1");
					$('input[name="alarmConfigs.temperatureTooHighAction.sms"]').val("1");
					$('input[name="alarmConfigs.fanNotOperationalAction.sms"]').val("1");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.sms"]').val("1");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.sms"]').val("1");
					$('input[name="alarmConfigs.cpuTempTooHighAction.sms"]').val("1");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.sms"]').val("1");
					$('input[name="alarmConfigs.cpuFanBadAction.sms"]').val("1");
					$('input[name="alarmConfigs.chassisTempTooHighAction.sms"]').val("1");
					$('input[name="alarmConfigs.chassisFanBadAction.sms"]').val("1");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.sms"]').val("1");
					$('input[name="alarmConfigs.voltageTooHighAction.sms"]').val("1");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.sms"]').val("1");
					$('input[name="alarmConfigs.blockDDoSAction.sms"]').val("1");
				}
				else 
				{							
					$('input[name="alarmConfigs.interfaceErrorAction.sms"]').val("0");
					$('input[name="alarmConfigs.interfaceUsageLimitAction.sms"]').val("0");
					$('input[name="alarmConfigs.interfaceDuplexChangeAction.sms"]').val("0");
					$('input[name="alarmConfigs.interfaceSpeedChangeAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcConfBackupFailureAction.sms"]').val("0");
					$('input[name="alarmConfigs.temperatureTooHighAction.sms"]').val("0");
					$('input[name="alarmConfigs.fanNotOperationalAction.sms"]').val("0");
					$('input[name="alarmConfigs.onlyOnePowerSupplyAction.sms"]').val("0");
					$('input[name="alarmConfigs.adcConfSyncFailureAction.sms"]').val("0");
					$('input[name="alarmConfigs.cpuTempTooHighAction.sms"]').val("0");
					$('input[name="alarmConfigs.cpuFanTooSlowAction.sms"]').val("0");
					$('input[name="alarmConfigs.cpuFanBadAction.snmptrap"]').val("0");
					$('input[name="alarmConfigs.chassisTempTooHighAction.sms"]').val("0");
					$('input[name="alarmConfigs.chassisFanBadAction.sms"]').val("0");
					$('input[name="alarmConfigs.chassisPowerSupplyBadAction.sms"]').val("0");
					$('input[name="alarmConfigs.voltageTooHighAction.sms"]').val("0");
					$('input[name="alarmConfigs.chassisFanTooSlowAction.sms"]').val("0");
					$('input[name="alarmConfigs.blockDDoSAction.sms"]').val("0");			
				}
			});	
		}
	},
	// from GS. #4012-1 #9, #3926-4 #6,13,14,15: 14.07.22 sw.jung obvalidation 활용 개선
	validateData : function()
	{
		return $.validate([
			{
				target: $('input[name="alarmConfigs.adcCpuValue"]'),
				name: $('input[name="alarmConfigs.adcCpuValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,100]
			},
			{
				target: $('input[name="alarmConfigs.adcSslTransValue"]'),
				name: $('input[name="alarmConfigs.adcSslTransValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,210000]
			},
			{
				target: $('input[name="alarmConfigs.adcSslcertValue"]'),
				name: $('input[name="alarmConfigs.adcSslcertValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,999]
			},
			{
				target: $('input[name="alarmConfigs.responseTimeValue"]'),
				name: $('input[name="alarmConfigs.responseTimeValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [1,10000]
			},
			{
				target: $('input[name="alarmConfigs.adcMPValue"]'),
				name: $('input[name="alarmConfigs.adcMPValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,100]
			},
			{
				target: $('input[name="alarmConfigs.adcSPValue"]'),
				name: $('input[name="alarmConfigs.adcSPValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,100]
			},
			{
				target: $('input[name="alarmConfigs.adcMemValue"]'),
				name: $('input[name="alarmConfigs.adcMemValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,100]
			},
			{
				target: $('input[name="alarmConfigs.adcConnHighValue"]'),
				name: "Concurrent Sessions",
				required: true,
				type: "number",
				range: [0,100000000]
			},
			{
				target: $('input[name="alarmConfigs.adcConnLowValue"]'),
				name: "Concurrent Sessions",
				required: true,
				type: "number",
				range: [0,100000000]
			},
			{
				target: $('input[name="alarmConfigs.adcUptimeValue"]'),
				name: $('input[name="alarmConfigs.adcUptimeValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,999]
			},
			{
				target: $('input[name="alarmConfigs.adcPurchaseValue"]'),
				name: $('input[name="alarmConfigs.adcPurchaseValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,999]
			},
			{
				target: $('input[name="alarmConfigs.interfaceErrorValue"]'),
				name: $('input[name="alarmConfigs.interfaceErrorValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,999]
			},
			{
				target: $('input[name="alarmConfigs.interfaceUsageValue"]'),
				name: $('input[name="alarmConfigs.interfaceUsageValue"]').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,100]
			},
			{
				target: $('#respTmValue'),
				name: $('#respTmValue').parent().parent().find('span').text(),
				required: true,
				type: "number",
				range: [0,10000]
			},
			{
				target: $('.timeType').find('option:selected[value="H"]').parent().parent().find('.timeValue'),
				name: $($('.timeValue').parent().parent().parent().parent().find('th')[2]).text(),
				required: true,
				type: "number",
				range: [0,24]
			},
			{
				target: $('.timeType').find('option:selected[value="M"]').parent().parent().find('.timeValue'),
				name: $($('.timeValue').parent().parent().parent().parent().find('th')[2]).text(),
				required: true,
				type: "number",
				range: [0,60]
			}
		]);
	},
//	validateData : function(e)
//	{
//		var adcCpuValue = $('input[name="alarmConfigs.adcCpuValue"]').val();
//		if(adcCpuValue != null)
//		{
//			if(getValidateNumberRange(adcCpuValue, 0, 100) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcSslTransValue = $('input[name="alarmConfigs.adcSslTransValue"]').val();
//		if(adcSslTransValue != null)
//		{
//			if(getValidateNumberRange(adcSslTransValue, 0, 210000) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcSslcertValue = $('input[name="alarmConfigs.adcSslcertValue"]').val();
//		if(adcSslTransValue != null)
//		{
//			if(getValidateNumberRange(adcSslcertValue, 0, 999) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}					
//		}
//		var responseTimeValue = $('input[name="alarmConfigs.responseTimeValue"]').val();
//		if (responseTimeValue != null)
//		{
//			if(getValidateNumberRange(responseTimeValue, 1, 10000) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcMPValue = $('input[name="alarmConfigs.adcMPValue"]').val();
//		if(adcMPValue != null)
//		{
//			if(getValidateNumberRange(adcMPValue, 0, 100) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcSPValue = $('input[name="alarmConfigs.adcSPValue"]').val();
//		if(adcSPValue != null)
//		{
//			if(getValidateNumberRange(adcSPValue, 0, 100) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcMemValue = $('input[name="alarmConfigs.adcMemValue"]').val();
//		if (adcMemValue != null)
//		{
//			if(getValidateNumberRange(adcMemValue, 0, 100) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcConnValue = $('input[name="alarmConfigs.adcConnValue"]').val();
//		if (adcConnValue != null)
//		{
//			if(getValidateNumberRange(adcConnValue, 0, 100000000) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcUptimeValue = $('input[name="alarmConfigs.adcUptimeValue"]').val();
//		if (adcUptimeValue != null)
//		{
//			if(getValidateNumberRange(adcUptimeValue, 0, 999) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var adcPurchaseValue = $('input[name="alarmConfigs.adcPurchaseValue"]').val();
//		if (adcPurchaseValue != null)
//		{
//			if(getValidateNumberRange(adcPurchaseValue, 0, 999) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var interfaceErrorValue = $('input[name="alarmConfigs.interfaceErrorValue"]').val();
//		if (interfaceErrorValue != null)
//		{
//			if(getValidateNumberRange(interfaceErrorValue, 0, 999) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		var interfaceUsageValue = $('input[name="alarmConfigs.interfaceUsageValue"]').val();
//		if (interfaceUsageValue != null)
//		{
//			if(getValidateNumberRange(interfaceUsageValue, 0, 100) == false)
//			{
//				alert(VAR_COMMON_NUMRANGE);
//				return false;
//			}
//		}
//		
//		if ($('#respTmValue').val() > 10000 || $('#respTmValue').val() < 0 )
//		{
//
//				alert(VAR_ALERT_RESPABNORMAL);						
//				return false;
//		}
//		return true;
//	}
});