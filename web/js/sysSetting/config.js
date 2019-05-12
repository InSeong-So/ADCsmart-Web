var Config = Class.create({
	initialize : function() 
	{
		this.logViewPeriodType = 12;
		this.autoRefrash = 0;
		this.isTimeSync = 1;
		this.timeServerAddress = "time.kriss.re.kr";		

		this.syncTime = undefined;	
		
		this.backOption = 0;
		this.backupTime = 0;
	},
	
	loadContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadConfigContent.action',
				target : "#wrap .contents",
				successFn : function(params) 
				{
					registerConfigContentEvents();
					displaySyncSystemTimePanel();
				},
                errorFn : function(jqXhr)
                {
                	$.obAlertAjaxError(VAR_CONFIG_LOAD, jqXhr);
                }
			});
		}
	},
	loadConfigContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadConfigContent.action',
				target : "#wrap .contents",
				successFn : function(params) 
				{
					registerConfigContentEvents();
					displaySyncSystemTimePanel();
					header.setActiveMenu('SysSettingConfig');
				},
                errorFn : function(jqXhr)
                {
                	$.obAlertAjaxError(VAR_CONFIG_LOAD, jqXhr);
                }
			});
		}
	},
	displaySyncSystemTimePanel : function()
	{
		with(this)
		{
			var use_ntp_YN = $('#use_ntp_value').val();
			var $ntpInputArea = $('.ntp_input_area');
			var $manuallyInputArea = $('.manually_input_area');
			if (use_ntp_YN == 1)
			{
				$manuallyInputArea.addClass('none');
				$ntpInputArea.removeClass('none');				
				
			}			
			else
			{
				$manuallyInputArea.addClass('none');
				$ntpInputArea.addClass('none');
			}
		}
	},
	registerConfigContentEvents : function () 
	{
		with (this) 
		{
			$('#logViewPeriodType').change(function() 
			{
				logViewPeriodType = $('#logViewPeriodType option:selected').val(); 
//				alert('모니터링 조회시간 : ' + logViewPeriodType);
//				var data = $('input[id="logViewPeriodType"]').val();
//				logViewPeriodType = $("select[name='logVPT'] option[value='"+data+"']").attr("selected", "selected");
			});
			
//			$('#backOption').change(function() {
//				backOption = $('#backOption option:selected').val();
////				alert('option : ' + backOption);
//			});
			
			$('#backupTime').change(function() 
			{
				backupTime = $('#backupTime option:se41lected').val();
			});
			
			$('#autoRefrash').change(function() 
			{
				autoRefrash = $('#autoRefrash option:selected').val();
				
//				var data2 = $('input[id="autoRefrash"]').val();
//				autoRefrash = $("select[name='autoR'] option[value='"+data2+"']").attr("selected", "selected");
			});	
			
			$('#logFilterType').change(function() 
			{
				logFilterType = $('#logFilterType option:selected').val();
			});
			
			$('.addAdcLogFilterLnk').click(function(e) 
			{
				e.preventDefault();
				moveAdcLogFilterInputList();
			});
			
			$('.allLogFiltersChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$('#selectedLogFilter .logFilterChk').attr('checked', isChecked);
			});	
			
//			$('.filterTbd').on('dblclick', '.regFilterTr', function(e) {
//				FlowitUtil.log('-- dblclick');
//				with (this) {
//				var userPattern = $(this).children().eq(1).text();
//				$(this).remove();
////				$('.adcNodeTd').eq(0).parent().parent().append('<tr><td class="adcNodeTd ui-widget-content">' + ip + '</td></tr>');
////				_applyMemberTableCss();
////				_registerNodeListTableEvents();
////				_setAdcLogFilterCount();
////				_setAdcNodeCount();
//				}
//			});
			$('.apply_systemtime_sync').click(function(e) 
			{
				with (this) 
				{
					e.preventDefault();
					var manuallyTimeL = getManuallyTimeToLong();				
					var params = 
					{
						"syncSystemTimeInfo.timeSyncType" : $('#sync_system_time_select').val(),
						"syncSystemTimeInfo.primary_NTP" : $('#primary_ntp').val(),
						"syncSystemTimeInfo.secondary_NTP" : $('#secondary_ntp').val(),
						"syncSystemTimeInfo.intervalNTPSync" : $('#ntp_interval').val(),						
						"manuallyTimeL" : manuallyTimeL			
					};
					
					if (!validateNTPOption())
						return;
					
					ajaxManager.runJsonExt({
						url : "sysSetting/modifySyncSystemTimeConfig.action",
						data : params,
						successFn : function(data) 
						{
							if(!data.isSuccessful)
							{
								$.obAlertNotice(VAR_SYSSETTING_SYSTEM_TIME_SYNC_FAIL);
								return;
							}							
							$.obAlertNotice(VAR_COMMON_REGISUCCESS);
							loadConfigContent();
						},
						 errorFn : function(jqXhr)
			             {
			             	$.obAlertAjaxError(VAR_SYSSETTING_SYSTEM_TIME_SYNC_FAIL, jqXhr);
			             }
					});
				}
			});
				
			$('.delAdcLogFilterLnk').click(function(e) 
			{
				with (this) 
				{
					e.preventDefault();

//					var chkDel = $(this).parent().parent().parent().parent().parent().parent().parent().parent().find('#selectedLogFilter .logFilterChk').filter(':checked').map(function() {
					var chkDel = $('#selectedLogFilter .logFilterChk').filter(':checked').map(function() 
					{
						return $(this).val();  //alert("chkDel : " + chkDel);
					}).get();

					if (chkDel.length == 0) 
					{
						$.obAlertNotice(VAR_CONFIG_ADCLOGFILTERSEL);
						return;
					}
					
					var $filterTRs = _getCheckedFilterTRs();
					
					var chk = confirm(VAR_CONFIG_ADCLOGFILTERDEL);
					if(chk) 
					{
						$filterTRs.remove();
						_setAdcLogFilterCount();
					}
					else 
					{
						return false;
					}
				}
			});
			$('#sync_system_time_select').change(function(e) 
			{
				with (this) 
				{			
					e.preventDefault();
					var selected_sync_type = $(this).val();
					var $ntpInputArea = $('.ntp_input_area');
					var $manuallyInputArea = $('.manually_input_area');
					if (selected_sync_type == 1)
					{
						$manuallyInputArea.removeClass('none');
						$ntpInputArea.addClass('none');
						inputTimeDataManuallySelectBox();
						
					}
					else if (selected_sync_type == 2)
					{
						$manuallyInputArea.addClass('none');
						$ntpInputArea.removeClass('none');
					}
					else
					{
						$manuallyInputArea.addClass('none');
						$ntpInputArea.addClass('none');
					}
				}
			});

			if (!syncTime) 
			{
				syncTime = new Date();
			}			
			
			$('input[name="settingTime"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "/imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: syncTime,
				onSelect: function(dateText, inst)
				{					
					syncTime = $("input[name='settingTime']").datepicker("getDate");					
				}
			});			

			$('.smsActionChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.smsActionChk').attr('checked', isChecked);
				if (isChecked == true) {
					$('.smsActionType').attr("disabled", false);
					$('.smsHPNumbers').attr("disabled", false);
				} else {
					$('.smsActionType').attr("disabled", true);
					$('.smsHPNumbers').attr("disabled", true);
				}
			});		
			
			$('.systemConfigOkLnk').click(function() 
			{
				var syslogIP = $('input[name="eadditional.syslogServerAddress"]').val();
				if(syslogIP !='')
				{	
					if(getValidateIP(syslogIP) == false)
					{
						$.obAlertNotice(VAR_COMMON_IPFORMAT);
						return false;
					}
				}
				
				var adcMonitorSec = $('input[name="eadditional.intervalAdcConfSync"]').val();
				if(getValidateNumberRange(adcMonitorSec, 30, 600) == false) // 유효성 검사 메시지 수정 junhyun.ok_GSTEST
				{
					regexp = /[^0-9]/gi;      // 정규표현식을 이용하여 숫자만 입력하도록 유효성 체크 (메시지 추가).
	                if (regexp.test(adcMonitorSec))
	                {
	                    $.obAlertNotice(VAR_COMMON_NUMRANGE);
	                    return false;
	                }
	                else
	                {
	                	$.obAlertNotice(VAR_CONFIG_ADCMONITCYCLE); // 모니터링 주기 유효성 검사 메시지 수정. 
	                	return false;
	                }
	            }
				
				// 구간응답시간 Interval 값 받아오기
				var  respTimeInterval = $('input[name="eadditional.respTimeInterval"]').val();
				
				if($('input[name="eadditional.respTimeSection"]').is(':checked') == true)
				{
					if(getValidateNumberRange(respTimeInterval, 60, 600) == false) // 유효성 검사(60~600)
					{
						regexp = /[^0-9]/gi;      // 정규표현식을 이용하여 숫자만 입력하도록 유효성 체크 (메시지 추가).
		                if (regexp.test(respTimeInterval))
		                {
		                    $.obAlertNotice(VAR_COMMON_NUMRANGE);
		                    return false;
		                }
		                else
		                {
		                	$.obAlertNotice(VAR_CONFIG_respTimeInterval); // 모니터링 주기 유효성 검사 메시지 수정. 
		                	return false;
		                }
		            }
				}
				
				$('#envNetworkFrm').submit();
			});		
			
			//구간 응답시간 체크하지 않을 시 값 입력 못하게 적용.
			$('input[name="eadditional.respTimeSection"]').click(function(){
				if($(this).is(':checked')) 
				{
					$('input[name="eadditional.respTimeInterval"]').attr("disabled", false);
				} 
				else 
				{
					$('input[name="eadditional.respTimeInterval"]').attr("disabled", true);
				}
			});
			
			var selectSnmpVersion = $('input[name="eadditional.snmpCommunity.version"]:checked').val();
			if (selectSnmpVersion == 2)
			{
				$('.snmpv2Version').removeClass("none");
				$('.snmpv3Version').addClass("none");
			}
			else if (selectSnmpVersion == 3)
			{
				$('.snmpv2Version').addClass("none");
				$('.snmpv3Version').removeClass("none");
			}
			else
			{
				$('.snmpv2Version').removeClass("none");
				$('.snmpv3Version').addClass("none");
			}
			
			// snmp version radio 버튼 클릭시 발생하는 이벤트
			$('input[name="eadditional.snmpCommunity.version"]').change(function()
			{
				var selectSnmpVersion = $('input[name="eadditional.snmpCommunity.version"]:checked').val();

				if (selectSnmpVersion == "2")
				{
					$('.snmpv2Version').removeClass("none");
					$('.snmpv3Version').addClass("none");
				}
				else if (selectSnmpVersion == "3")
				{
					$('.snmpv2Version').addClass("none");
					$('.snmpv3Version').removeClass("none");
				}
				else
				{
					$('.snmpv2Version').removeClass("none");
					$('.snmpv3Version').addClass("none");
				}
			});
			
			// snmp auth protocal change
			$('.authProtocolCbx').change(function()
			{
				svcValue = $('select[name="eadditional.snmpCommunity.algorithm"] :selected').val();
			});
			
			$('#envNetworkFrm').submit(function() 
			{	
				
				var manuallyTimeL = getManuallyTimeToLong();				
				var params = 	
				{
					"eview.logViewPeriodType" : logViewPeriodType,
					"eview.autoRefrash" : autoRefrash,
					"eadditional.isTimeSync" : isTimeSync,
					"eadditional.timeServerAddress" : timeServerAddress,
					"sbackup.backOption" : backOption,
					"sbackup.backupTime" : backupTime,
					"manuallyTimeL" : manuallyTimeL,
					"eadditional.snmpTrap.snmpTrapServerAddress" : $('input[name="eadditional.snmpTrapServerAddress"]').val(),
					"eadditional.snmpTrap.snmpTrapPort" : $('input[name="eadditional.snmpTrapPort"]').val(),
					"eadditional.snmpTrap.snmpTrapCommunity" : $('input[name="eadditional.snmpTrapCommunity"]').val(),
					"eadditional.snmpTrap.snmpTrapVersion" : $('input[name="eadditional.snmpTrapVersion"]:checked').val(),
					"eadditional.respTimeSection" : $('input[name="eadditional.respTimeSection"]:checked').val(),
					"eadditional.snmpCommunity.accessType" : $('#snmpAccessType option:selected').val(),
//					"eadditional.snmpCommunity.algorithm" : $('#authProtocolCbx option:selected').val()
					
//					"lfilter.type" : logFilterType					
				};				
				if (!validateSystemConfigAdd())
					return false;
				$(this).ajaxSubmit({
					dataType : 'json',
					url : 'sysSetting/modifyConfigContent.action',
					data : params,
					success : function(data) 
					{
						log.debug(Object.toJSON(data));
						if(data.isSuccessful) 
						{
							$.obAlertNotice(VAR_COMMON_REGISUCCESS);
							loadConfigContent();
						} 
						else
						{
							$.obAlertNotice(VAR_COMMON_SYSTEMSETFAIL);
						}
					},
					error : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_COMMON_SYSTEMSETFAIL, jqXhr);
//						exceptionEvent();
					}
				});
				
				return false;
			});	
		}	
	},
	inputTimeDataManuallySelectBox : function()
	{
		with(this)
		{			
			var hour_value = $('#synctime_hour_value').val();
			var min_value = $('#synctime_min_value').val();
			var sec_value = $('#synctime_sec_value').val();
		
			$('#synctime_hour').val(hour_value);
			$('#synctime_min').val(min_value);
			$('#synctime_sec').val(sec_value);
		}
	},
	getManuallyTimeToLong : function()
	{
		with(this)
		{
			var manuallyTime = new Date();
			var retVal;
			var splitDateObj = $('input[name="settingTime"]').val().split("-");
			manuallyTime.setFullYear(splitDateObj[0]);
			manuallyTime.setMonth(splitDateObj[1] -1);
			manuallyTime.setDate(splitDateObj[2]);
			manuallyTime.setHours($('#synctime_hour').val());
			manuallyTime.setMinutes($('#synctime_min').val());
			manuallyTime.setSeconds($('#synctime_sec').val());
			retVal = manuallyTime.getTime();
			return retVal;		
		}
	},

	moveAdcLogFilterInputList : function() 
	{
		with (this) 
		{
			var $userPattern = $('.filterPatternTxt');
//			var $type = $('.filterTypeTxt');
			var $type = $('#logFilterType option:selected');
			if (!_validateFilterInput($userPattern.val(), $type.val()))
			{
				return false;
			}
		
			addToFilterTable($userPattern.val(), $type.val());
		 }
	},
	
	_validateFilterInput : function(userPattern, type) 
	{
		with (this) 
		{
			if (userPattern == '') 
			{
				$.obAlertNotice(VAR_CONFIG_PATTERNINPUT);
				return false;
			} 
			else if (type == '') 
			{
				$.obAlertNotice(VAR_CONFIG_AGREETYPESEL);
				return false;
			}	
			
			return true;
		}
	},
	
	addToFilterTable : function(userPattern, type) 
	{
		with (this) 
		{		
			var typeNm = "";
			if (type == 0) 
			{
				typeNm = VAR_CONFIG_SECTION;
			} 
			else if (type == 1) 
			{
				typeNm = VAR_CONFIG_WHOLE;
			}
			
			var html = '<tr class="regFilterTr">';
			html += '<td class="align_center"><input class="logFilterChk" type="checkbox"/></td>';
			html += '<td class="align_left_P20"> <input type="hidden" name="filterUserPattern" value="' + userPattern + '"/>' + userPattern + '</td>';
			html += '<td class="align_center"> <input type="hidden" name="filterType" value="' + type + '"/>' + typeNm + '</td>';			
			html += '</tr>';
			
			$('.filterTbd').append(html);
			_applyFilterTableCss();
//			_registerMemberTableEvents();
//			FlowitUtil.log($('.filterTbd').html());
			_setAdcLogFilterCount();
		}
	},	
	
	_setAdcLogFilterCount : function() 
	{
		$('.adcLogFilterCount').text($('.filterTbd > tr').length);
	},

	_applyFilterTableCss : function () 
	{
		// 리스트형 테이블 행 롤오버 효과
		$('.ipList tbody tr:not(.empty)').hover(function() {
			$(this).css({
				'background-color' : '#ccc',
				'cursor' : 'pointer'
			});												//마우스오버시 배경색 변경
		}, function() {
			$(this).removeAttr('style');
		});
		
		$('#selectedLogFilter').parents('.t_fixed_header').find('.headtable').css('margin-right', '17px');
		$('#selectedLogFilter').parents('.t_fixed_header').find('.body').css('overflow-y', 'scroll');

	}, 
	
	_applyFilterTableCss : function () 
	{
		// 리스트형 테이블 행 롤오버 효과
		$('.cloneDiv .ipList tbody tr:not(.empty)').hover(function() {
			$(this).css({
				'background-color' : '#ccc',
				'cursor' : 'pointer'
			});
		}, function() {
			$(this).removeAttr('style');
		});
		
		tableHeadSizeWithoutEmptyRows($('.cloneDiv #selectedLogFilter'));
	
	},
	_getCheckedFilterTRs : function() 
	{ //alert("del1");		
		return $('#selectedLogFilter .logFilterChk').filter(':checked').parent().parent(); 
	},
	validateNTPOption: function()
	{
		if (!$('#primary_ntp').validate({
			name: $('#primary_ntp').parents('.ntp_input_area').find('span:first').text(),
			required: true,
			checked: $('#sync_system_time_select').val() == 2,
			type: "ip"
		}))
			return false;
		
		if (!$('#secondary_ntp').validate({
			name: $('#secondary_ntp').parents('.ntp_input_area').find('span:first').text(),
			checked: $('#sync_system_time_select').val() == 2,
			type: "ip"
		}))
			return false;
		
		if (!$('#ntp_interval').validate({
			name: $('#ntp_interval').parents('.ntp_input_area').find('span:first').text(),
			required: true,
			checked: $('#sync_system_time_select').val() == 2,
			type: "number",
			range: [1,24]
		}))
			return false;
		
		return true;
	},
	validateSystemConfigAdd : function() 
	{		
		//기본설정
		if ($('input[name="enetwork.ipAddress"]').val() == '') 
		{
			$.obAlertNotice(VAR_CONFIG_IPINPUT);
			return false;
		} 
		else if ($('input[name="enetwork.netmask"]').val() == '') 
		{
			$.obAlertNotice(VAR_CONFIG_NETMASKINPUT);
			return false;
		} 
		else if ($('input[name="enetwork.gateway"]').val() == '') 
		{
			$.obAlertNotice(VAR_CONFIG_GATEWAYINPUT);
			return false;			
		} 
		else if ($('input[name="enetwork.hostName"]').val() == '') 
		{
			$.obAlertNotice(VAR_CONFIG_SYSTEMNAMEINPUT);
			return false;
		}
		
		// NTP 설정
		if (!this.validateNTPOption())
			return false;
		
		//부가기능 설정
		if ($('input[name="eadditional.intervalConfSync"]').val() == '') 
		{
			$.obAlertNotice(VAR_CONFIG_SYNCCYCLEINPUT);
			return false;
		}
		
		//화면표시 설정
		if ($('input[name="eview.logViewCount"]').val() == '') 
		{
			$.obAlertNotice(VAR_CONFIG_LISTCOUNTINPUT);
			return false;
		}
		
		// SNMP TRAP 설정
		if($('input[name="eadditional.snmpTrapServerAddress"]').val() != "")
		{
			if (getValidateIP($('input[name="eadditional.snmpTrapServerAddress"]').val()) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}	
		}
		
		if($('input[name="eadditional.snmpTrapPort"]').val() != "")
		{
			if (getValidateNumberRange($('input[name="eadditional.snmpTrapPort"]').val(), 0, 65535) == false)
			{	
				$.obAlertNotice(VAR_ADCSETTING_PORTFORMAT);
				return false;
			}
		}
		
		if($('input[name="eadditional.snmpTrapCommunity"]').val() != "")
		{
			if (!getValidateLength($('input[name="eadditional.snmpTrapCommunity"]').val(), 1, 32))
			{
				$.obAlertNotice(VAR_COMMON_LENGTHFORMAT);
				return false;
			}
		}
		
		return true;		
	}
});
