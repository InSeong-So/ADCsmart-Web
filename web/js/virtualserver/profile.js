// @author: Hakmin Lee
var Profile = Class.create({
	initialize : function() 
	{
		this.adc;
		this.orderDir  = 1; // 오름차순 = 1
		this.orderType = 33;// profileName = 33
		this.searchFlag =false;
	},
	getActiveContent : function() 
	{
		return adcSetting.activeContent;
	},
	setActiveContent : function(content) 
	{
		adcSetting.activeContent = content;
	},
	isActiveContent : function(content) 
	{
		return adcSetting.activeContent == content;
	},
	setAdc : function(adc) 
	{
		this.adc = adc;
	},
	loadProfileListContent : function(searchKey, orderType, orderDir) 
	{	
		if (adcSetting.getAdc().type == "F5")
		{
			if(header.getVsSettingTap() == 0)
			{
				virtualServer.loadListContent();
				return;
			}
			else if(header.getVsSettingTap() == 2)
			{
				node.loadListContent();
				return;
			}
			else if (header.getVsSettingTap() == 3)
			{
				noticeGrp.loadListContent();
				return;
			}
		}
		with (this) 
		{
			if(!validateDaterefresh())
			{
				return false;
			}
			setActiveContent('ProfileListContent');
			ajaxManager.runHtml({
				url : "profile/loadProfileListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"searchKey" : searchKey == null ? undefined : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					setActiveContent('ProfileListContent');
					header.setActiveMenu('SlbSetting');
	//				AdcSetting.applyAdcListContentCss();
					registerProfileListContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PROFILE_LFAIL, jqXhr);
				}
			});
		}
	},
	registerProfileListContentEvents : function() 
	{
		with (this) 
		{			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="textfield3"]').val();
				searchFlag=true;
				loadProfileListContent(searchKey , orderDir , orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="textfield3"]').val();		
				searchFlag=true;
				loadProfileListContent(searchKey , orderDir , orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="textfield3"]').val();	
				searchFlag=true;
				loadProfileListContent(searchKey , orderDir , orderType);
			});
			
			$('.vServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(0);
				virtualServer.loadListContent();
			});
			
			$('.rServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(2);
				node.loadListContent();
			});
			
			$('.noticeServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(3);
				noticeGrp.loadListContent();
			});
			
			$('.addProfileLnk').click(function(e) 
			{
				e.preventDefault();				
				loadProfileAddContent();
			});
			
			$('.modifyProfileLnk').click(function(e) 
			{
				e.preventDefault();
				loadProfileModifyContent($(this).parent().parent().find('.profileChk').val());
			});
			
			$('.delProfiles').click(function(e) 
			{
				e.preventDefault();
				with (this) 
				{					
					var profileNames = $('.profileChk').filter(':checked').map(function() {
						return $(this).val();
					}).get();
					
//					var profileNames = $(this).parent().parent().find('.profileChk').filter(':checked').map(function() {
//						return $(this).val();
//					}).get();
					log.debug(profileNames);
					if (profileNames.length == 0) 
					{
						$.obAlertNotice(VAR_PROFILE_DSELE);
						return;
					}
					
					var chk = confirm(VAR_PROFILE_DEL);
					if(chk) 
					{
						delProfiles(profileNames);
					}
					else 
					{
						return false;
					}
//					delProfiles(profileNames);
				}
			});
			
			$('.allProfileChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.profileChk').attr('checked', isChecked);
			});
			
			// search event
//			$('p.cont_sch a.searchLnk').click(function (e) {
			$('.btn a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				log.debug('click:' + searchKey);
				searchFlag=true;
				loadProfileListContent(searchKey);
			});
			
			$('.inputTextposition1 input.searchTxt').keydown(function(e) 
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				log.debug('click:' + searchKey);
				searchFlag=true;
				loadProfileListContent(searchKey);
			});
			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.profileList').size() > 0)
				{
					$('.searchNotMsg').addClass("none");
				}
				else
				{
					$('.searchNotMsg').removeClass("none");
				}
				searchFlag=false;
			}
			else
			{
				$('.searchNotMsg').addClass("none");
				$('.nulldataMsg').addClass("none");
				if($('.profileList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1)
			{		
				$('.searchNotMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				$('.allProfileChk').attr("disabled", "disabled");
				$('.profileChk').attr("disabled", "disabled");
				
				if ($('.profileList').size() > 0 )
				{
					$('.disabledChk').removeClass("none");
					$('.nulldataMsg').addClass("none");	   					
				}
				else
				{
					$('.disabledChk').addClass("none");
					$('.nulldataMsg').removeClass("none");
					$('input[name="searchKey"]').attr("disabled", "disabled");
					$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
				}							
			}			
		}
	},
	loadProfileAddContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtml({
				url : "profile/loadProfileAddContent.action",
				data : 
				{
					"profileAdd.adcIndex" : adcSetting.getAdc().index
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					registerProfileAddContentEvents(false);
				}
			});
		}
	},
	registerProfileAddContentEvents : function(isModify) 
	{
		with (this) 
		{
			$('#profileAddFrm').submit(function() 
			{ 
				if (!_validateProfileAdd())
					return false;
				
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? 'profile/modifyProfile.action' : 'profile/addProfile.action',
					data : 
					{
						"profileAdd.adcIndex" : adcSetting.getAdc().index
					},
					success : function(data) 
					{
						if (data.isSuccessful) 
						{
							$.obAlertNotice(VAR_PROFILE_REGI_SUCC);
							loadProfileListContent();
						} 
						else
						{
							$.obAlertNotice(data.message);
						}
					},
					error : function(jqXhr)
					{
						// #3984-6 #8: 14.07.30 sw.jung SLB 프로필 추가시 중복이름 검사
						if (jqXhr.responseText.indexOf('16908390') > -1)
							$.obAlertAjaxError(VAR_ALT_NAMEDUPLICATE, jqXhr);
						// #3984-6 #9: 14.07.30 sw.jung 이미 삭제된 프로필일 경우(타 장비/F5에서)
						else if (jqXhr.responseText.indexOf('16908342') > -1)
							$.obAlertAjaxError(VAR_PROFILE_NOTEXIST, jqXhr);
						else
							$.obAlertAjaxError(VAR_PROFILE_ADDFAIL, jqXhr);
					}
				}); 
		 
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
			
			$('.vServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(0);
				virtualServer.loadListContent();
			});
			
			$('.rServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(2);
				node.loadListContent();
			});
			
			$('.noticeServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(3);
				noticeGrp.loadListContent();
			});
			
			$('.cancelProfileAddLnk').click(function(e) 
			{
				e.preventDefault();
				loadProfileListContent();
			});
			
			$('.okProfileAddLnk').click(function(e) 
			{
				e.preventDefault();
				$('#profileAddFrm').submit();
			});
			
			if (adcSetting.getAdcStatus() != "available") 
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");				
			}
			else
			{
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");	
			}
		}
	},
	validateDaterefresh : function()
	{
		if(($('.control_Board input.searchTxt').val() != "") && ($('.control_Board input.searchTxt').val() != null))
		{
			var search = $('.control_Board input.searchTxt').val();
			if (!isValidStringLength(search, 1, 64))
			{
				var data = VAR_COMMON_LENGTHFORMAT+"("+1+"~"+64+")";
				$.obAlertNotice(data);
				$('.control_Board input.searchTxt').val('');
				return false;
			}

			if (!isExistSpecialCharacter(search))
			{
				$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
				$('.control_Board input.searchTxt').val('');
				return false;
			}			
//			var search = $('.control_Board input.searchTxt').val();
//			if(getValidateStringint(search, 1, 64) == false)
//			{
//				alert(VAR_FAULTSETTING_SPECIALCHAR);
//				return false;
//			}			
		}

		if ($('input[name="fromPeriod"]').val() > $('input[name="toPeriod"]').val())
		{
			$.obAlertNotice(VAR_COMMON_DATEERROR);
			return false;
		}		
		return true;		
	},
	// from GS. #4012-1 #11, #3984-1 #3: 14.07.29 sw.jung 프로필 추가/수정 유효성 검사 구문 개선
	_validateProfileAdd : function()
	{
		return $.validate([
			 	{
			 		target: $('input[name="profileAdd.name"]'),
			 		name: "",
			 		required: true,
			 		type: "f5_name",
			 		lengthRange: [1,64]
			 	},
			 	{
			 		target: $('input[name="profileAdd.timeOutInSec"]'),
			 		name: "",
			 		required: true,
			 		type: "number",
			 		range: [1,4294967295]
			 	}
			 ]);
	},
//	_validateProfileAdd : function() 
//	{
//		var profileName = $('input[name="profileAdd.name"]').val();
//		if (profileName == "")
//		{
//			alert(VAR_PROFILE_NAME);
//			return false;
//		}
//		
//		else if (getValidateStringint(profileName, 1, 64) == false)
//		{
//			alert(VAR_PROFILE_NNOTALL);
//			return false;
//		}
//		var profileTimeout = $('input[name="profileAdd.timeOutInSec"]').val();
//		if (getValidateNumberRange(profileTimeout, 1, 4294967295) == false)
//		{
//			alert(VAR_PROFILE_TIMPIALLOW);
//			return false;
//		}		
//		return true;
//	},
	loadProfileModifyContent : function(profileIndex) 
	{
		with (this) 
		{
			ajaxManager.runHtml({
				url : "profile/loadProfileModifyContent.action",
				data: 
				{
					"profileAdd.adcIndex" : adcSetting.getAdc().index,
					"profileAdd.index" : profileIndex
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					registerProfileAddContentEvents(true);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PROFILE_LFAIL, jqXhr);
				}
			});
		}
	},
	delProfiles : function(profileIndices) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "profile/delProfiles.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"profileIndices" : profileIndices
				},
				successFn : function(data) 
				{
					if (!data.isSuccessful) 
					{
						$.obAlertNotice(data.message);
						return;
					}
					log.debug('deleted successfully.');
					loadProfileListContent();
				},
				errorFn : function(jqXhr)
				{
					// #3984-1 #2 사용중 프로필 검출을 위해 Exception 메세지 확인
					if (jqXhr.responseText.indexOf('17236611') >= 0)
						$.obAlertAjaxError(VAR_PROFILE_USED, jqXhr);
					else
						$.obAlertAjaxError(VAR_PROFILE_DELFAIL, jqXhr);
				}
			});
		}
	}
});
