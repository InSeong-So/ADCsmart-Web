var FaultSetting = Class.create({
	initialize : function()
	{
		this.OBajaxManager = new OBAjax();
		this.adc = {};
		this.schedulePeriod = 0;		
		this.templateIndex = undefined;
		this.templateNm = undefined;
		this.faultMaxDays = undefined;
		this.flbFaultMaxDays = undefined;
		this.adcLogCount = 0;
		this.clientIp = undefined;
		this.vsIndex = undefined;
		this.vsIp = undefined;
		this.vsPort = undefined;
		this.vsName = undefined;
		this.everyMin = 0;						
		this.everyHr = 0;						
		this.everyOnceText = undefined;			
		this.everyDayOfMonth = 0;				
		this.everyDayOfWeek = 0;				
		this.everyDayMonth = 0; 				
		this.scheduleType = 0;					
		this.scheduleIndex = 0;
		this.svcFlag = 0;
		this.adcModelNum = undefined;
		this.beforeData = undefined;
		this.afterData = undefined;
	},
	onAdcChange : function() 
	{
		with (this)
		{
			loadFaultSettingContent();
		}
	},

	getIndex : function(index)
	{		
		if (index == 1)
		{
			selectedIndex = adcSetting.getGroupIndex();
		}
		else if (index == 2)
		{
			selectedIndex = adcSetting.getAdc().index;
		}
		else
		{
			selectedIndex = 0;
		}
		return selectedIndex;
	},
	
	loadFaultSettingContent : function()
	{
		with (this)
		{
//			this.adcModelNum = adcSetting.getAdcModel();
			if (adcSetting.getSelectIndex() == 2)
			{
				this.adcModelNum = adcSetting.getAdcModel().substr(0, 1);
			}
			else
			{
				this.adcModelNum = null;
			}

			ajaxManager.runHtmlExt({
				url : "faultSetting/loadFaultSettingContent.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"adc.model"				: this.adcModelNum,
					"faultMaxDays" 			: $('select[name="faultMaxDays"]').val(),
					"flbFaultMaxDays" 		: $('select[name="flbFaultMaxDays"]').val(),
					"adcLogCount" 			: $('input[name="adcLogCount"]').val(),
					"clientIp" 				: $('input[name="clientIp"]').val()
//					"templateIndex" : templateIndex1
				},
				successFn : function(params)
				{
					registerFaultSettingContentEvents();
//					header.setActiveMenu('FaultSetting');
					header.setActiveMenu('FaultHistory');
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTSETTING_LOAD, jqXhr);
				}
			});
		}
	},
	
	// Template Header 페이지 Refresh, Template Save 이후에 사용
	refreshFaultSettingContent :function(templateName)
	{
		with (this)
		{
//			this.adcModelNum = adcSetting.getAdcModel();
			OBajaxManager.runHtml({
				url : "faultSetting/loadFaultSettingContent.action",
				target : "#wrap .contents",	
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"adc.model"				: this.adcModelNum,
					"faultMaxDays" 			: $('select[name="faultMaxDays"]').val(),
					"flbFaultMaxDays" 		: $('select[name="flbFaultMaxDays"]').val(),
					"adcLogCount" 			: $('input[name="adcLogCount"]').val(),
					"clientIp" 				: $('input[name="clientIp"]').val()
				},
				successFn	: function(params) 
				{
					$('#selectTemplate option').each(function() 
					{
						if ($(this).text() == templateName)
						{
							var $TemlateNmInput = $('#templateNm');
							
							$('.templateSave').addClass('none');
							$('.templateModify').removeClass('none');							
							$('.customTemplate').addClass('none');
							$('.policyTemplate').removeClass('none');
							$('.policyTemplateNm').removeClass('none');
							
							$TemlateNmInput.val(templateName);
							
							// TODO
							var templateIndex = $(this).val();
							loadFaultTableInListContent(templateIndex);
						}													
					});
					registerFaultSettingContentEvents();
					header.setActiveMenu('FaultSetting');
				},	
				errorFn : function(jqXhr)
				{
//					exceptionEvent();
					$.obAlertAjaxError(VAR_FAULTSETTING_LOAD, jqXhr);
				}
			});
		}
	},
	loadFaultTableInListContent : function(index)	
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "faultSetting/loadFaultTableInListContent.action",
//				target : "table.settingTable",
				target : ".settingTable",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"adc.model"				: this.adcModelNum,
					"templateIndex"			: index					
				},
				successFn : function(params)
				{				
					registerFaultSettingContentEvents();
					selectboxChange(index);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTSETTING_LOAD, jqXhr);
				}
			});
		}
	},
	
	loadFaultSchduleContent : function(index, flag, hr, min, week, month, dayMonth)
	{
		with (this) 
		{
			//this.adcModelNum = adcSetting.getAdcModel().substr(0, 1);	
			
			if (adcSetting.getSelectIndex() == 2)
			{
				this.adcModelNum = adcSetting.getAdcModel().substr(0, 1);
			}
			else
			{
				this.adcModelNum = null;
			}
			
			ajaxManager.runHtmlExt({
				url : "faultSetting/loadFaultSettingContent.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: adcSetting.getGroupIndex(),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"adc.model"				: this.adcModelNum,
					"templateIndex" 		: index,
					"scheduleType" 			: flag
				},
				successFn : function(params) 
				{
					registerFaultSettingContentEvents();
					scheduleSet(flag, hr, min, week, month, dayMonth);
					header.setActiveMenu('FaultSetting');
				},
				errorFn : function(jqXhr)
                {
					$.obAlertAjaxError(VAR_FAULTSETTING_LOAD, jqXhr);
                }
			});
		}
	},
	
	_getHwCheck : function()
	{
		return $('#hw input[type=checkbox]').map(function() 
		{
			var hw_index = $(this).val();
			var hw_ischecked = $(this).is(':checked') ? "1" : "0";
			var hw_value = "";
			if (hw_index == 9) 
			{
				hw_value = $(this).parent().find("input[type=text]").val();
			}
			return {
				"index" : hw_index,
				"state" : hw_ischecked,
				"value" : hw_value
			};
		}).get();
	},
	
	_getL23Check : function()
	{
		return $('#l23 input[type=checkbox]').map(function() 
		{
			var l23_index = $(this).val();
			var l23_ischecked = $(this).is(':checked') ? "1" : "0";
			var l23_value = $(this).parent().find("input[type=text]").val();
			
			return {
				"index" : l23_index,
				"state" : l23_ischecked,
				"value" : l23_value
			};
		}).get();
	},
	
	_getL47Check : function()
	{
		return $('#l47 input[type=checkbox]').map(function() 
		{
			var l47_index = $(this).val();
			var l47_ischecked = $(this).is(':checked') ? "1" : "0";
			var l47_value = "";
			if (l47_index == 2) 
			{
				l47_value = $(this).parent().find("input[type=text]").val();
			}
			return {
				"index" : l47_index,
				"state" : l47_ischecked,
				"value" : l47_value
			};
		}).get();
	},
	
	_getSvcCheck : function()
	{
		return $('#svc input[type=checkbox]').map(function() 
		{
			var svc_index = $(this).val();
			var svc_ischecked = $(this).is(':checked') ? "1" : "0";
			var svc_value =  $(this).parent().find("input[type=text]").val();
			
			return {
				"index" : svc_index,
				"state" : svc_ischecked,
				"value" : svc_value
			};
		}).get();
	},
		
	faultDiagnosisSet : function(flag, changeChk)
	{		
		with (this)
		{
			if (!_validateTemplateAdd(flag))
			{
				return false;
			}
			
			if (($('select[name="svc"] :selected').val() == "") || ($('select[name="svc"] :selected').val() === undefined))
			{							
			}
			else
			{
				svcValue = $('select[name="svc"] :selected').val();
				var array_data = svcValue.split("|");
				vsIp = array_data[0];
				vsPort = array_data[1];
				vsIndex = array_data[2];
				vsName = array_data[3];
			}
//			if ((adcSetting.getAdc().type != "Alteon") && (adcSetting.getAdcModel().substr(0, 1) != 2 || adcSetting.getAdcModel().substr(0, 1) != 3)) 
//			{
//				svcValue = $('select[name="svc"] :selected').val();
//				var array_data = svcValue.split("|");
//				vsIp = array_data[0];
//				vsPort = array_data[1];
//				vsIndex = array_data[2];
//				vsName = array_data[3];
//			}
			
			if (flag == 0)
			{
				chkUrl = "faultSetting/startFaultCheck.action";
			}
			else if (flag == 1)
			{
				chkUrl = "faultSetting/registerFaultCheckSchedule.action";
			}
			else if (flag == 2 || flag == 3)
			{
				chkUrl = "faultSetting/saveFaultCheckTemplate.action";
			}
						
			scheduleType = $('#schedulePeriod :selected').val();
			
			if (scheduleType == 0)
			{
				everyHr = 0;
				everyMin = 0;
				everyDay = 0;
				everyDayWeek = 0;
				everyOnce= 0;
			}
			else if (scheduleType == 1)	//매일
			{
				everyHr = $('#everyHour :selected').val();
				everyMin =  $('#everyMin :selected').val();
				everyDayOfWeek = 0;
				everyDayOfMonth = 0;				
				everyDayMonth= 0;				
			}
			else if (scheduleType == 2)	//매주
			{
				everyHr = $('#everyHour :selected').val();
				everyMin =  $('#everyMin :selected').val();
				everyDayOfWeek = $('#everyDayWeek :selected').val();
				everyDayOfMonth = 0;					
				everyDayMonth= 0;
			}
			else if (scheduleType == 3)	//매월
			{			
				everyHr = $('#everyHour :selected').val();
				everyMin =  $('#everyMin :selected').val();				
				everyDayOfWeek = 0;
				everyDayOfMonth = $('#everyDay :selected').val();
				everyDayMonth= 0;
			}
			else if (scheduleType == 4)	//한번만
			{
				everyOnceText = $('#everyOnce').val();
				
				var array_data = everyOnceText.split("-");
				everyMonth = array_data[1];	//월
				everyDay = array_data[2];	//일
				
				everyHr = $('#everyHour :selected').val();
				everyMin =  $('#everyMin :selected').val();
				everyDayOfWeek = 0;
				everyDayOfMonth = everyDay;	
				everyDayMonth= everyMonth;				
			}
			
			if ($('#svcChkFlg').is(':checked') == true)
			{
				svcFlag = 1;
			}
			else
			{
				svcFlag = 0;
			}
			
			var templateIdx = 0;
			if (changeChk)
			{
				templateIdx = $("#selectTemplate").val();
			}
			else
			{
				templateIdx = 0;
			}
			var adcName = adcSetting.getAdc().name;
			ajaxManager.runJsonExt({
				url : chkUrl,
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
//					"templateIndex" : $("#selectTemplate").val(),
					"templateIndex" : templateIdx,
					"templateNm" : $('#templateNm').val(),
					"adcObject.desciption" : $('.diagnosisTarget').text().trim(),
					"hwCheckInString" : Object.toJSON(_getHwCheck()),
					"l23CheckInString" : Object.toJSON(_getL23Check()),
					"l47CheckInString" : Object.toJSON(_getL47Check()),
					"svcCheckInString" : Object.toJSON(_getSvcCheck()),
					"faultMaxDays" : $('select[name="faultMaxDays"]').val(),
					"flbFaultMaxDays" : $('select[name="flbFaultMaxDays"]').val(),
					"adcLogCount" : $('input[name="adcLogCount"]').val(),
					"clientIp" : $('input[name="clientIp"]').val(),
					"svcValue" : $('select[name="svc"]').val(),
					"vsIp" : vsIp,
					"vsPort" : vsPort,
					"vsIndex" : vsIndex,
					"vsName" : vsName,	
					"scheduleType" : scheduleType,
					"everyHr" : everyHr,
					"everyMin" : everyMin,					
					"everyDayOfWeek" : everyDayOfWeek,					
					"everyDayOfMonth" : everyDayOfMonth,
					"everyDayMonth" : everyDayMonth,
					"svcFlag" : svcFlag
				},				
				successFn : function(data) 
				{ 
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}	
					if (flag == 0)
					{
//						alert("진단 설정 정보를 성공적으로 전달하였습니다. 진단을 시작합니다.");
						faultDiagnosis.loadFaultDiagnosisPopup(data.faultCheckIndex, adcName);
					}
					else if (flag == 1)	// 진단예약
					{
						$.obAlertNotice(VAR_FAULTSETTING_SCHEDULESUCC);
						loadFaultSettingContent();
					}
					else if (flag == 2)	// 템플릿 저장
					{						
//						if (data.templateIndex == 0)
//						{
						// TODO: 템플릿저장
							refreshFaultSettingContent($('#templateNm').val());
							$.obAlertNotice(VAR_FAULTSETTING_TEMPLATESAVESUCC);
//						}
//						else 
//						{							
//							var $TemlateNmInput = $('#templateNm');
//							var templateName = $TemlateNmInput.val(); 
//							var $TemlateNoticArea = $('.policyTemplateNm').filter(':last');
//							
//							$("#selectTemplate").children('option').filter(':selected').text(templateName);
//							
//							
//							$('.templateSave').addClass('none');
//							$('.templateModify').removeClass('none');
//							
//							$('.customTemplate').addClass('none');
//							$('.policyTemplate').removeClass('none');
//							$('.policyTemplateNm').removeClass('none');
//							
//							$TemlateNoticArea.empty();						
//							$TemlateNoticArea.html(templateName);
//							$TemlateNmInput.val(templateName);							
//						}
//						loadFaultSettingContent();
					}
					else if (flag == 3)	// 템플릿 수정
					{
						var $TemlateNmInput = $('#templateNm');
						var templateName = $TemlateNmInput.val(); 
						var $TemlateNoticArea = $('.policyTemplateNm').filter(':last');
						
						$("#selectTemplate").children('option').filter(':selected').text(templateName);
						
						
						$('.templateSave').addClass('none');
						$('.templateModify').removeClass('none');
						
						$('.customTemplate').addClass('none');
						$('.policyTemplate').removeClass('none');
						$('.policyTemplateNm').removeClass('none');
						
						$TemlateNoticArea.empty();						
						$TemlateNoticArea.html(templateName);
						$TemlateNmInput.val(templateName);
						$.obAlertNotice(VAR_FAULTSETTING_TEMPLAGEMODIFYSUCC);
					}
					
				},
				errorFn : function(jqXhr)
				{
					if (flag == 1)
					{
//						$.obAlertAjaxError("진단 예약에 실패했습니다.", jqXhr);	
						$.obAlertAjaxError(VAR_FAULTSETTING_REGISCHEDULE, jqXhr);	
					}
					else
					{
						$.obAlertAjaxError(VAR_FAULTSETTING_FAIL, jqXhr);
					}
				}	
			});
		}
	},
	
	faultDianosisDel : function()
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "faultSetting/delFaultCheckTemplate.action",
				data : 
				{					
					"templateIndex" : templateIndex,
					"templateNm" : $('.policyTemplateNm').text() // 14.07.12 17:00 sw.jung: 템플릿 삭제 감사로그 이름 null뜨는 버그 해결
				},
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					loadFaultSettingContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTSETTING_TEMPLATEDELFAIL, jqXhr);
				}
			});
		}
	},

	// 저장 후 저장한 Index 로 select box 를 선택한다.
	selectboxChange : function(selectIndex)
	{
		with (this)
		{
			templateIndex = selectIndex; // 인덱스 갱신
		}
		
		$('#selectTemplate option').each(function() 
		{
			if ($(this).val() == selectIndex)
			{
				$(this).attr("selected", "selected");
				return false;
			}
		});	
		
		if (($('select[name="svc"] :selected').val() == "") || ($('select[name="svc"] :selected').val() === undefined))
		{							
		}
		else
		{
			svcValue = $('select[name="svc"] :selected').val();
			var array_data = svcValue.split("|");
			vsIp = array_data[0];
			vsPort = array_data[1];
			vsIndex = array_data[2];
			vsName = array_data[3];
			var $VirtualSvcArea = $('#virtualSvcDesc').filter(':last');
			$VirtualSvcArea.empty();
			var html = '';
			html += '<span> IP : '+ vsIp +' / Port : '+ vsPort +' / Name : '+ vsName +'  </span>';
					
			$VirtualSvcArea.html(html);	
		}
//		if ((adcSetting.getAdc().type != "Alteon") && (adcSetting.getAdcModel().substr(0, 1) != 2 || adcSetting.getAdcModel().substr(0, 1) != 3)) 
//		{
//			svcValue = $('select[name="svc"] :selected').val();
//			var array_data = svcValue.split("|");
//			vsIp = array_data[0];
//			vsPort = array_data[1];
//			vsIndex = array_data[2];				
//		}	
	},
	
	_validateTemplateAdd : function(flag)
	{
		with (this)
		{	
			// 장애 정보를 선택하지 않은 경우
			var chkFlag = false;				
			for (var i=0; i<$('.chkFlag #faultChk').length; i++)
			{	
				var isChecked = $('.chkFlag #faultChk')[i].checked;
				if (isChecked == true)
				{
					chkFlag = true;
				}
			}
			
			svcChkFlag = $('#svcChkFlg').is(':checked');
			if (svcChkFlag == true)
			{
				// ClinetIP Address - IP만 허용
				if (!getValidateIP($('input[name="clientIp"]').val()))
				{
					$.obAlertNotice(VAR_COMMON_IPFORMAT);
					return false;
				}
				
				if (($('#clientIp').val() == "") || ($('#clientIp').val() == null))
				{
					$.obAlertNotice(VAR_FAULTSETTING_SVCCLIENIPINPUT);
					return false;
				}
			}
			
			if (chkFlag == true || svcChkFlag == true)
			{				
			}
			else
			{
				if (flag == 0)
				{
					$.obAlertNotice(VAR_FAULTSETTING_ITEMNOTSEL);
				}
				else if (flag == 1)
				{
					$.obAlertNotice(VAR_FAULTSETTING_SCHEDULEITEMNOTSEL);
				}
				else if (flag == 2)
				{
					$.obAlertNotice(VAR_FAULTSETTING_TEMPLATEITEMNOTSEL);
				}
				return false;
			}
						
			var thresholdHWAdcLogCount = $('input[name="adcLogCount"]').val(); 	
			
			if (!getValidateNumberRange(thresholdHWAdcLogCount, 1, 1000))
			{
				$.obAlertNotice(VAR_FAULTSETTING_ADCLOGCOUNT);
				return false;
			}
			
//			if (thresholdHWAdcLogCount <1 || thresholdHWAdcLogCount>100)
//			{
//				alert(VAR_FAULTSETTING_ADCLOGCOUNT);
//				return false;
//			}
			
			// 서비스 진단 활성화 && IP가 있을 때만 검사
			if ($('input[name="clientIp"]:not(:disabled)').val())
			{
				if (!FlowitUtil.checkIp($('input[name="clientIp"]').val())) 
				{
					$.obAlertNotice(VAR_FAULTSETTING_CLIENTIPINCCORECT);
					return false;
				}
			}
			
			templateNm = $('input[name="templateNm"]').val();
			templateIndex = $("#selectTemplate").val();
			templateList = $('#selectTemplate option');
			var validateTemplateNameKey = false;
			
			//flag : 0 진단 , flag : 1 예약, flag : 2 템플릿 저장				
			for(var i=0; i < templateList.length; i++)
			{
				if (templateList[i].innerHTML == templateNm)
				{
					validateTemplateNameKey = true;
				}				
			}
			
			if (flag == 2)
			{
				// from GS. #4012-1 #5, #3926-4 #7,11: 14.07.29 sw.jung obvalidation 활용 개선
//				if (!getValidateStringint(templateNm, 1, 64))
//				{
//					alert(VAR_FAULTSETTING_TMEPLATENAME);
//					return false;
//				}
				if (!$('input[name="templateNm"]').validate(
					{
						name: $('input[name="templateNm"]').parent().find('label.templateModify').text().replace(":","").trim(),
						required: true,
						type: "name",
						lengthRange: [1,64]
					}))
				{
					return false;
				}
				
				if(templateNm == null || templateNm == "")
				{
					$.obAlertNotice(VAR_FAULTSETTING_TEMPLATENAMEINPUT);
					return false;
				}
				if(validateTemplateNameKey == true)
				{
					$.obAlertNotice(VAR_FAULTSETTING_ALREADYSAVENAME);
					return false;
				}
			}

//			if ((flag ==1 || flag == 2) || (templateIndex == 0 && templateNm.length > 0)) // 진단아니고 신규가 아니고
			if (flag == 3 || flag == 2 || (templateIndex == 0 && templateNm.length > 0)) // 진단아니고 신규가 아니고				
			{
				// from GS. #4012-1 #5, #3926-4 #7,11: 14.07.29 sw.jung obvalidation 활용 개선
//				if (!getValidateStringint(templateNm, 1, 64))
//				{
//					alert(VAR_FAULTSETTING_TMEPLATENAME);
//					return false;
//				}
				if (!$('input[name="templateNm"]').validate(
					{
						name: $('input[name="templateNm"]').parent().find('label.templateModify').text().replace(":","").trim(),
						required: true,
						type: "name",
						lengthRange: [1,64]
					}))
				{
					return false;
				}
				
				//if (false == inputCheckSpecial(templateNm)) return false;
				
//				if (templateNm.length == 0 || templateNm.length > 64) 
//				{
//					alert(VAR_FAULTSETTING_TMEPLATENAME);
//					return false;
//				}
			}
			
			return true;
		}
	},
	
	/*특수문자사용못하게*/
	inputCheckSpecial : function (str)
	{
        var strobj = str;        
//        re = /[~!@\#$%^&*\()\=+_']/gi;
        re = /[~!@\#$%^&*\()\=+']/gi;
        if(re.test(strobj))
        {
	        $.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
	        return false;
        }
        return true;
    },
    scheduleSet : function(flag, hr, min, week, month, dayMonth)
    {  
		schedulePeriod = flag;
		$('#schedulePeriod option[value=' + flag  + ']').attr("selected", ""); 
		//매일
		if (schedulePeriod == 1)
		{
			$('.everyHour').removeClass('none');
			$('.everyMin').removeClass('none');	
			$('.everyDayWeek').addClass('none');
			$('.everyDay').addClass('none');
			$('.everyOnce').addClass('none');					
			$('.scheduleDay').removeClass('none');
			$('.startFaultCheck').addClass('none');
			$('.registerFaultCheckSchedule').removeClass('none');
			$('#everyHour option[value=' + hr  + ']').attr("selected", "");
			$('#everyMin option[value=' + min  + ']').attr("selected", "");
			$('.datepickerImage').addClass('none');
		}
		//매주
		else if (schedulePeriod == 2)
		{
			$('.everyHour').removeClass('none');
			$('.everyMin').removeClass('none');		
			$('.everyDayWeek').removeClass('none');
			$('.everyDay').addClass('none');
			$('.everyOnce').addClass('none');
			$('.scheduleDay').removeClass('none');
			$('.startFaultCheck').addClass('none');
			$('.registerFaultCheckSchedule').removeClass('none');
			$('#everyDayWeek option[value=' + week  + ']').attr("selected", "");
			$('#everyHour option[value=' + hr  + ']').attr("selected", "");
			$('#everyMin option[value=' + min  + ']').attr("selected", "");
			$('.datepickerImage').addClass('none');
		}
		//매월
		else if (schedulePeriod == 3)
		{
			$('.everyHour').removeClass('none');
			$('.everyMin').removeClass('none');				
			$('.everyDayWeek').addClass('none');
			$('.everyDay').removeClass('none');
			$('.everyOnce').addClass('none');					
			$('.scheduleDay').removeClass('none');
			$('.startFaultCheck').addClass('none');
			$('.registerFaultCheckSchedule').removeClass('none');
			$('#everyDay option[value=' + dayMonth  + ']').attr("selected", "");
			$('#everyHour option[value=' + hr  + ']').attr("selected", "");
			$('#everyMin option[value=' + min  + ']').attr("selected", "");
			$('.datepickerImage').addClass('none');
		}
		else if (schedulePeriod == 4)
		{
			$('.everyHour').removeClass('none');
			$('.everyMin').removeClass('none');						
			$('.everyDayWeek').addClass('none');
			$('.everyDay').addClass('none');
			$('.everyOnce').removeClass('none');
			$('.scheduleDay').removeClass('none');    					
			$('.startFaultCheck').addClass('none');
			$('.registerFaultCheckSchedule').removeClass('none');
			if (month < 10)
			{
				month = '0'+month;
			}
			if (dayMonth < 10)
			{
				dayMonth = '0'+dayMonth;
			}
			$('#everyOnce').val(getCurrYear() + '-' + month + '-' + dayMonth);	
			$('#everyHour option[value=' + hr  + ']').attr("selected", "");
			$('#everyMin option[value=' + min  + ']').attr("selected", "");
			$('.datepickerImage').removeClass('none');
		}
		else
		{
			$('.everyHour').addClass('none');
			$('.everyMin').addClass('none');						
			$('.everyDayWeek').addClass('none');
			$('.everyDay').addClass('none');
			$('.everyOnce').addClass('none');
			$('.scheduleDay').addClass('none');
			$('.startFaultCheck').removeClass('none');
			$('.registerFaultCheckSchedule').addClass('none');
			$('.datepickerImage').addClass('none');
		}				
    },
    changeDiagnosisSet : function()
	{
		with(this)
		{
//			this.beforeJsonData = Object.toJSON(_getL23Check());
//			alert(this.beforeJsonData);
//			alert(Object.toJSON(_getL23Check()));
//			afterHwData = Object.toJSON(_getHwCheck());
//			afterL23Data = Object.toJSON(_getL23Check());
//			afterL47Data = Object.toJSON(_getL47Check());
//			afterSvcData = Object.toJSON(_getSvcCheck());
//			afterData = afterHwData + " , " + afterL23Data + " , " + afterL47Data + " , " + afterSvcData;
			
			afterData = Object.toJSON(_getHwCheck()) + " , " + Object.toJSON(_getL23Check()) + " , " + Object.toJSON(_getL47Check()) + " , " + Object.toJSON(_getSvcCheck());
//			alert("set : " + afterData);
			
			if (beforeData != afterData)
			{				
//				$.msgBox({
//					    title: "Are You Sure",
//					    content: "Would you like a cup of coffee?",
//					    type: "confirm",
//					    buttons: [{ value: "Yes" }, { value: "No" }, { value: "Cancel"}],
//					    success: function (result) 
//					    {
//					        if (result == "Yes") 
//					        {
//					            alert("One cup of coffee coming right up!");
//					        }
//					    }
//					});

//				MessageBoxResult result = MessageBox.Show("Do you want to close this window?", "Confirmation", MessageBoxButton.YesNoCancel);
//				if (result == MessageBoxResult.Yes)
//				{
//				    // Yes code here
//				}
//				else if (result == MessageBoxResult.No)
//				{
//				    // No code here
//				}
//				else
//				{
//				    // Cancel code here
//				} 
					
//				bootbox.dialog({
//					  message: "I am a custom dialog",
//					  title: "Custom title",
//					  buttons: {
//					    success: {
//					      label: "Success!",
//					      className: "btn-success",
//					      callback: function() {
//					        Example.show("great success");
//					      }
//					    },
//					    danger: {
//					      label: "Danger!",
//					      className: "btn-danger",
//					      callback: function() {
//					        Example.show("uh oh, look out!");
//					      }
//					    },
//					    main: {
//					      label: "Click ME!",
//					      className: "btn-primary",
//					      callback: function() {
//					        Example.show("Primary button");
//					      }
//					    }
//					  }
//					});
				
				var chk = confirm(VAR_FAULTSETTING_TEMPLATEMODIADD);
				if (chk)
				{
					faultDiagnosisSet(3, true);
                	faultDiagnosisSet(0, true);
				}
				else
				{
					faultDiagnosisSet(0, false);
				}
				/*
				var dialog = $('<p>진단 템플릿이 변경되었습니다. 저장 후 진단하시겠습니까?</p>').dialog({
	                buttons: 
	                {
	                    "Yes": function() 
	                    {
	                    	// 메세지를 받으면 템플릿 저장 프로세스를 진행 한 후 진단을 실시
	                    	//alert('템플릿 수정 & 진단 시작');
	                    	faultDiagnosisSet(3, true);
	                    	faultDiagnosisSet(0, true);
	                    	dialog.dialog('close');
	                    },
	                    "No":  function() 
	                    {
	                    	//  진단을 시작하되 템플릿 인덱스 정보를 null 하여 서비스단에 전달한다. 정책 템플릿이 선택되지 않은것으로 간주
	                    	//alert('진단 시작 & 정책템플릿 선택 X ');
	                    	faultDiagnosisSet(0, false);
	                    	dialog.dialog('close');
	                    },
	                    "Cancel":  function() 
	                    {
	                    	// 진단을 진단하지 않는다.
	                        //alert('진단 하지 않음');
	                        dialog.dialog('close');
	                    }
	                }
	            });
	            */
			}
			else
			{
				faultDiagnosisSet(0);
			}
		}
	},
	
	registerFaultSettingContentEvents : function()
	{
		with(this)
		{
			// GS 14.07.12 sw.jung: F5 라우팅/세션테이블 진단 기능 제거
			if (adcSetting.getAdc().type == "F5" || faultHistory.getAdcType() == "F5")
			{
				$('.hwChk[value=3]').parent().remove();
				$('.l23Chk[value=5]').parent().remove();
				$('.l47Chk[value=3]').parent().remove();
			}			
			
			$('.templeteModify').remove(); // GS 14.07.12 sw.jung 템플릿 수정기능 임시 제거(버튼만 삭제)
			
			$('#selectedIp').change(function(e)
			{
				if ($(this).val() == 0)
				{
					$('#clientIp').val("");
				}
				else
				{
					$('#clientIp').val($(this).val());
				}
			});
			$('#virtualSvc').change(function(e) 
			{
				if ($('select[name="svc"] :selected').val() != "")
				{
					svcValue = $('select[name="svc"] :selected').val();
					var array_data = svcValue.split("|");
					vsIp = array_data[0];
					vsPort = array_data[1];
					vsIndex = array_data[2];
					vsName = array_data[3];
				}
//				if ((adcSetting.getAdc().type != "Alteon") && (adcSetting.getAdcModel().substr(0, 1) != 2 || adcSetting.getAdcModel().substr(0, 1) != 3)) 
//				{
//					svcValue = $('select[name="svc"] :selected').val();
//					var array_data = svcValue.split("|");
//					vsIp = array_data[0];
//					vsPort = array_data[1];
//					vsIndex = array_data[2];				
//				}
				
				
				var $VirtualSvcArea = $('#virtualSvcDesc').filter(':last');
				$VirtualSvcArea.empty();
				var html = '';
				html += '<span> IP : '+ vsIp +' / Port : '+ vsPort +' / Name : '+ vsName +'  </span>';
						
				$VirtualSvcArea.html(html);							
			});
			$('#selectTemplate').change(function(e)
			{				
				templateIndex = $(this).val();
				templateName = $(this).children('option').filter(':selected').text();
				
				$('input[name="templateNames"]').val(templateName);
				loadFaultTableInListContent(templateIndex);
				
				var $TemlateNoticArea = $('.policyTemplateNm').filter(':last');				
				var $TemlateNmInput = $('#templateNm');
				
				if (templateIndex != 0)
				{
					$('.templateSave').addClass('none');
					$('.templateModify').removeClass('none');
					$('.policyTemplateNm').removeClass('none');				
					$TemlateNoticArea.empty();						
					$TemlateNoticArea.html(templateName);					
					$TemlateNmInput.val(templateName);
				}
				else
				{
					$('.templateSave').removeClass('none');
					$('.templateModify').addClass('none');
					$('.policyTemplateNm').addClass('none');					
					$TemlateNoticArea.empty();						
					$TemlateNoticArea.html('');					
					$TemlateNmInput.val('');
				}				
			});
						
//			$('#everyOnce').datepicker({
//				dateFormat : "yy-mm-dd",
//			});
//			
//			$('#everyOnce').datepicker({
//				//maxDate: "0",
//				minDate: "0",
//				dateFormat : "yy-mm-dd",
//				//showOn: "button",
//				//buttonImage: "imgs/meun/btn_calendar.png",
//				//buttonImageOnly: true
//			});
//			
//			$('#datepickerImage').click(function()
//			{
//				$('#everyOnce').datepicker('show');
//			});
			
			// 14.07.12 날짜 빈 값 유효성 처리 목적에서 datepicker를 일반적 형태로 전환
			$('#everyOnce').datepicker({
				minDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true
			});
			var currentDate = new Date();
			$('#everyOnce').val(currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate());
			$('.ui-datepicker-trigger').addClass('everyOnce none');
			
			// -----여기까지
			    
			$('#everyHour').change(function() 
			{
				everyHr = $('#everyHour option:selected').val();
			});
			
			$('#everyMin').change(function() 
			{
				everyMin = $('#everyMin option:selected').val();
			});
			
			$('#everyDayMon').change(function() 
			{
				everyDayMon = $('#everyDayMon option:selected').val();
			});
			
			$('#everyMon').change(function() 
			{
				everyMon = $('#everyMon option:selected').val();
			});
			
			$('#everyDayWeek').change(function() 
			{
				everyDayWeek = $('#everyDayWeek option:selected').val();
			});
						
			//예약 변경시
			$('#schedulePeriod').change(function(e)
			{
				schedulePeriod = $('#schedulePeriod option:selected').val(); 				
				//매일
				if (schedulePeriod == 1)
				{
					$('.everyHour').removeClass('none');
					$('.everyMin').removeClass('none');	
					$('.everyDayWeek').addClass('none');
					$('.everyDay').addClass('none');
					$('.everyOnce').addClass('none');					
					$('.scheduleDay').removeClass('none');					
					$('.startFaultCheck').addClass('none');
					$('.registerFaultCheckSchedule').removeClass('none');	
					$('.datepickerImage').addClass('none');
				}
				//매주
				else if (schedulePeriod == 2)
				{
					$('.everyHour').removeClass('none');
					$('.everyMin').removeClass('none');		
					$('.everyDayWeek').removeClass('none');
					$('.everyDay').addClass('none');
					$('.everyOnce').addClass('none');
					$('.scheduleDay').removeClass('none');					
					$('.startFaultCheck').addClass('none');
					$('.registerFaultCheckSchedule').removeClass('none');
					$('.datepickerImage').addClass('none');
				}
				//매월
				else if (schedulePeriod == 3)
				{
					$('.everyHour').removeClass('none');
					$('.everyMin').removeClass('none');				
					$('.everyDayWeek').addClass('none');
					$('.everyDay').removeClass('none');
					$('.everyOnce').addClass('none');					
					$('.scheduleDay').removeClass('none');					
					$('.startFaultCheck').addClass('none');
					$('.registerFaultCheckSchedule').removeClass('none');
					$('.datepickerImage').addClass('none');
				}
				//한번만
				else if (schedulePeriod == 4)
				{
					$('.everyHour').removeClass('none');
					$('.everyMin').removeClass('none');						
					$('.everyDayWeek').addClass('none');
					$('.everyDay').addClass('none');
					$('.everyOnce').removeClass('none');
					$('.scheduleDay').removeClass('none');					
					$('.startFaultCheck').addClass('none');
					$('.registerFaultCheckSchedule').removeClass('none');
					$('.datepickerImage').removeClass('none');
				}
				else
				{
					$('.everyHour').addClass('none');
					$('.everyMin').addClass('none');						
					$('.everyDayWeek').addClass('none');
					$('.everyDay').addClass('none');
					$('.everyOnce').addClass('none');
					$('.scheduleDay').addClass('none');					
					$('.startFaultCheck').removeClass('none');
					$('.registerFaultCheckSchedule').addClass('none');
					$('.datepickerImage').addClass('none');
				}
			});
			
//			beforeHwData = Object.toJSON(_getHwCheck());
//			beforeL23Data = Object.toJSON(_getL23Check());
//			beforeL47Data = Object.toJSON(_getL47Check());
//			beforeSvcData = Object.toJSON(_getSvcCheck());			
//			beforeData = beforeHwData + " , " + beforeL23Data + " , " + beforeL47Data + " , " + beforeSvcData;
			
			beforeData = Object.toJSON(_getHwCheck()) + " , " + Object.toJSON(_getL23Check()) + " , " + Object.toJSON(_getL47Check()) + " , " + Object.toJSON(_getSvcCheck());
//			alert("load : " + beforeData);
			
			//진단 버튼 click
			$('.startFaultCheck').off('click');
			$('.startFaultCheck').click(function(e)
			{

//				templateIndex = $("#selectTemplate :selected").val();
//				templateNm = $("#templateNm :input").val();
//				alert("templateIndex : " + templateIndex);
				//startFaultCheck();
				
				//this.afterJsonData = Object.toJSON(_getL23Check());
				//if (this.beforeJsonData != this.afterJsonData)
				//{
//					alert(this.beforeJsonData);
//					alert(this.afterJsonData);
					if ($('#selectTemplate').val() == 0)
					{
						faultDiagnosisSet(0);
					}
					else
					{
						changeDiagnosisSet();
					}
//					
				//}
				//else
				//{
				//	faultDiagnosisSet(0);
				//}
/*				
 				var chkFlag = false;				
				for (var i=0; i<$('.chkFlag #faultChk').length; i++)
				{
					var isChecked = $('.chkFlag #faultChk')[i].checked;
					if (isChecked == true)
					{
						chkFlag = true;
					}
				}				
				svcChkFlag = $('#svcChkFlg').is(':checked');								
				if (svcChkFlag == true)
				{
					if (($('#clientIp').val() == "") || ($('#clientIp').val() == null))
					{
						alert("서비스 진단 사용자 IP를 입력하십시오.");
						return false;
					}
				}				
				if (chkFlag == true || svcChkFlag == true)
				{
					faultDiagnosisSet(0);
				}
				else
				{
					alert("진단을 위한 선택된 내용이 없습니다.");
					return false;
				}
*/		
			});
			
			//예약설정 변경시 이미지 변경
			$('.registerFaultCheckSchedule').off('click'); // 중복 이벤트 방지를 위한 등록 해제
			$('.registerFaultCheckSchedule').click(function(e)
			{	
				faultDiagnosisSet(1);
			});
			
			//사용자 지정 템플릿 저장
			$('.templeteSave').off('click'); // 중복 이벤트 방지를 위한 등록 해제
			$('.templeteSave').click(function(e)
			{
				faultDiagnosisSet(2, true);
			});
			
			//사용자 지정 템플릿 수정
			$('.templeteModify').off('click'); // 중복 이벤트 방지를 위한 등록 해제
			$('.templeteModify').click(function(e)
			{
				faultDiagnosisSet(3);
			});
						
			//사용자 지정 템플릿 삭제
			$('.templeteDel').off('click'); // 중복 이벤트 방지를 위한 등록 해제
			$('.templeteDel').click(function(e)
			{
				var chk = confirm(VAR_FAULTSETTING_TEMPLATEDEL);
				if (chk)
				{
					faultDianosisDel();
				}
				else
				{
					return false;
				}
			});
			
			// 서비스 진단 default disabled
			$('#svcChkFlg').click(function(e)
			{
				var isChecked = $(this).is(":checked");
				if (isChecked == true)
				{
					$('#clientIp').removeAttr("disabled");
					$('#selectedIp').removeAttr("disabled");
					$('#selectedSvc').removeAttr("disabled");
				}
				else
				{
					$('#clientIp').attr("disabled", "disabled");
					$('#selectedIp').attr("disabled", "disabled");
					$('#selectedSvc').attr("disabled", "disabled");
				}
			});
						
			//Virtual Server 유휴설정 변경
			$('select[name="faultMaxDays"]').change(function(e)
			{
				faultMaxDays = $(this).val();
			});
			
			//Virtual Server 유휴설정 변경
			$('select[name="flbFaultMaxDays"]').change(function(e)
			{
				flbFaultMaxDays = $(this).val();
			});
			
			//ADC 장비 진단 전체 선택시
			$('.adcChkFlg').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parents().find('.allHwChk').attr('checked', isChecked);
				$(this).parents().find('.hwChk').attr('checked', isChecked);
				$(this).parents().find('.allL23Chk').attr('checked', isChecked);
				$(this).parents().find('.l23Chk').attr('checked', isChecked);
				$(this).parents().find('.allL47Chk').attr('checked', isChecked);
				$(this).parents().find('.l47Chk').attr('checked', isChecked);			
				
				if (isChecked == true)
				{
				}
				else
				{					
				}
			});
			
			//Checkbox 전체 선택시
			$('.allHwChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parents().find('.hwChk').attr('checked', isChecked);
				
				if (isChecked == true)
				{					
				}
				else
				{					
				}
			});
			
			$('.allL23Chk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parents().find('.l23Chk').attr('checked', isChecked);
				
				if (isChecked == true)
				{					
				}
				else
				{					
				}
			});
			
			$('.allL47Chk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parents().find('.l47Chk').attr('checked', isChecked);
				
				if (isChecked == true)
				{					
				}
				else
				{					
				}
			});			

			if ((adcSetting.getSelectIndex() == 2) && (adcSetting.getAdc().type == "Alteon") && (this.adcModelNum == 2 || this.adcModelNum ==3))
			{
				$('.serviceDiagnosis').addClass("none");
			}
			else
			{
				$('.serviceDiagnosis').removeClass("none");
			}
		}		
	}
});