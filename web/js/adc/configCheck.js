var ConfigCheck =  Class.create({
	initialize : function() 
	{
		this.iniOpMode = undefined;
	},
	
	// ADC 설정 정보 초기 페이지 Open 및
	// config.adcIpaddress, config.adcId, config.adcPassword html 에 Data set
	loadContent : function(adcIndex, adcType, opMode_input)
	{
		with (this) {
			var opMode = opMode_input;
			if (opMode == null || opMode === undefined)
			{
				opMode = adcSetting.getAdc().mode;				
			}
			this.iniOpMode = opMode;
			ajaxManager.runHtmlExt({
				url: "adcSetting/loadContent.action",
				target: "#wrap .contents",
				data:
				{
					"adcIndex" : adcIndex,
					"adcType"  : adcType,
					"opMode" : opMode
				},
				successFn : function(data) 
				{					
					registerContentEvents(adcIndex, adcType);
					var adcIpaddress = $('input[name="adcIpaddress"]').val();
					var adcId = $('input[name="adcId"]').val();
					var adcPassword = $('input[name="adcPassword"]').val();
					var checkType = 0;
					loadCheckContent(adcIndex, adcIpaddress, adcId, adcPassword, checkType, adcType, false, opMode);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
				}	
			});		
		}
	},
	// 연결상태  Content ( Html + Data )
	loadCheckContent : function(adcIndex, adcIpaddress, adcId, adcPassword, configCheckType, adcType, refreshes, opMode)
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url: "adcSetting/checkAdcConfig.action",				
				target: "#wrap .contents",
				data:
				{
					"adcIndex" : adcIndex,
					"adcIpaddress" : adcIpaddress,
					"adcId" : adcId,
					"adcPassword" : adcPassword,
					"configCheckType" : configCheckType,
					"adcType" : adcType,
					"refreshes" : refreshes,
					"opMode" : opMode
				},
				successFn : function(data) 
				{
					var separator = false;
					loadChkStatusAll(adcIndex, adcIpaddress, adcId, adcPassword, configCheckType, adcType, refreshes, false, opMode);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_CONFIGCHECK_ADCSETLOAD, jqXhr);
				}	
			});		
		}
	},
	
	// 연결 상태 연결 테스트 진행
	loadChkStatusAll : function(adcIndex, adcIpaddress, adcId, adcPassword, configCheckType, adcType, refreshes, separator, opMode)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url: "adcSetting/chkAdcStatusAll.action",
				target: "#wrap .connectTest",
				data:
				{
					"adcIndex" : adcIndex,
					"adcType" : adcType,
					"opMode" : opMode
				},
				successFn : function(params) 
				{
					if (opMode != 1 && !separator)
					{
						loadTableInContent(adcIndex, adcIpaddress, adcId, adcPassword, configCheckType, adcType, refreshes);
					}					
					registerContentEvents(adcIndex, adcType);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_CONFIGCHECK_ADCSETLOAD, jqXhr);
				}	
			});
		}
	},	
	
	// 설정 상태 Content ( Html + Data )
	loadTableInContent : function(adcIndex, adcIpaddress, adcId, adcPassword, configCheckType, adcType, refreshes)
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url: "adcSetting/loadContentTableIn.action",				
				target: "#wrap .configTableIn",
				data:
				{
					"adcIndex" : adcIndex,
					"adcIpaddress" : adcIpaddress,
					"adcId" : adcId,
					"adcPassword" : adcPassword,
					"configCheckType" : configCheckType,
					"adcType" : adcType,
					"refreshes" : refreshes
				},
				successFn : function(data) 
				{
					registerContentEvents(adcIndex, adcType);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_CONFIGCHECK_ADCSETLOAD, jqXhr);
				}	
			});		
		}
	},
	
	registerContentEvents : function(adcIndex, adcType) 
	{
		with (this) 
		{
			var adcIpaddress = $('input[name="adcIpaddress"]').val();
			var adcId = $('input[name="adcId"]').val();
			var adcPassword = $('input[name="adcPassword"]').val();
			
			// 상단 탭 - ADC 수정 화면
			$('.adcModifyLnk').click(function(e) 
			{ 
				e.preventDefault();
				header.setAdcSettingTap(0);
				adcSetting.loadAdcModifyContent(adcIndex);
			});			
			
			// 연결테스트 Start Btn 
			$('.connectAdcChkResult').click(function(e)
			{				
				loadChkStatusAll(adcIndex, null, null, null, null, adcType, null, true, iniOpMode);
			});			
			
			// 설정 상태 새로고침 Btn
			$('.refreshAdcSet').click(function(e) 
			{
				var checkType = 0;
				e.preventDefault();
				loadTableInContent(adcIndex, adcIpaddress, adcId, adcPassword, checkType, adcType, true);
			});
			
			//ADC 설정 정보 - 전체 점검 버튼 (PAS, PASK Btn)
			$('.allConfigCheck').click(function(e) 
			{
				var checkType = 0;
				e.preventDefault();
				loadCheckContent(adcIndex, adcIpaddress, adcId, adcPassword, checkType, adcType, false);
			});
		}
	}	
});