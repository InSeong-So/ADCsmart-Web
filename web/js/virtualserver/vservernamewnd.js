var VServerNameWnd =  Class.create({
	initialize : function() 
	{
		this.adc = undefined;
		this.poolIndex = undefined;
		this.orderType = undefined;
		this.orderDir = undefined;
//		this.slbSchedule = new SlbSchedule();
	},
	setAdc : function(adc)
	{
		this.adc = adc;
	},
	setPoolIndex : function(poolIndex) 
	{
		this.poolIndex = poolIndex;
	},
	popUp : function(requesteIdx) 
	{
		with (this) 
		{
			if (requesteIdx == 1)
			{
				showPopup({
					"id" : "#vs_ipList2",
					"width" : "494px"
				});
			}
			else
			{
				showPopup({
					"id" : "#vs_ipList",
					"width" : "494px"
				});
			}
			_loadVServerNames();
			_registerEvents();
		}
	},
	popUp4Pool : function() 
	{
		with (this) 
		{
			showPopup({
				"id" : "#vs_ipList",
				"width" : "494px"
			});
			_loadUsedVServer4PoolAlteon();
			_registerEvents4Pool();
		}
	},
	_loadUsedVServer4PoolAlteon : function(searchKey, poolIndex) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "virtualServer/searchVSListUsedByPoolAlteon.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"alteonPoolIndex" : poolIndex,
					"searchKey" : searchKey == undefined ? "" : searchKey
				},
				successFn : function(data) 
				{
					FlowitUtil.log('virtualServers: ' + Object.toJSON(data.virtualServers));
					$('.cloneDiv .vServerNameTbd').html(_getVServerNameHtmlRows(data.virtualServers));
					_applyTableCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VPVSER_SEARFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},	
	_loadVServerNames : function(searchKey) 
	{
		with (this) 
		{
			if (!validateSearchKey())
			{
				return false;
			}
			ajaxManager.runJsonExt({
				url : "virtualServer/loadListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"searchKey" : searchKey == undefined ? "" : searchKey,
					"orderType" : orderType == undefined ? "0" : orderType,
					"orderDir"  : orderDir ==  undefined ? "0" : orderDir
				},
				successFn : function(data) 
				{
					FlowitUtil.log('virtualServers: ' + Object.toJSON(data.virtualServers));
					$('.cloneDiv .vServerNameTbd').html(_getVServerNameHtmlRows(data.virtualServers));
					_applyTableCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VPVSER_LOADFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	
	validateSearchKey : function()
	{
		if ($('.cloneDiv .searchTxt').val() != "" && $('.cloneDiv .searchTxt').val() != undefined)
		{
			if (!getValidateStringint($('.cloneDiv .searchTxt').val(), 1, 1024))
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				return false;
			}
		}
		
		return true;
	},
	_registerEvents : function(requesteIdx) 
	{
		with (this) 
		{
			$('.cloneDiv .closeLnk, .cloneDiv .closeWndLnk').click(function(e) 
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			
			// search event
			$('p.sch a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('.cloneDiv .searchTxt').val();
				FlowitUtil.log('click:' + searchKey);				
				_loadVServerNames(searchKey);
			});
			
			$('.requestorAllChk').click(function(e)
			{
//				e.preventDefault();
				var isChecked = $(this).is(':checked');
//				$('#selelctedReq .requestorChk').attr('checked', isChecked);
//				$('.cloneDiv .requestorChk').attr('checked', isChecked);
				$('.cloneDiv #selectedReq .requestorChk').attr('checked', isChecked);
			});
			
			$('.delSlbUsersLnk').off('click');
			$('.delSlbUsersLnk').click(function(e)
			{
				e.preventDefault();
				
				with (this)
				{
					var chkDel = $(this).parent().parent().find('.requestorChk').filter(':checked').map(function() {
						return $(this).val();
					}).get();
					
					if(chkDel.length == 0)
					{
						$.obAlertNotice(VAR_SCHEDULE_REQ_DSELECT);
						return;
					}
					
					var chk = confirm(VAR_SCHEDULE_REQ_DEL);
					if(chk)
					{
						delSlbUsers(chkDel);
					}
					else
					{
						return false;
					}
				}
			});
			
//			if(requesteIdx == 0)
//			{
//				
//			}
//			else
//			{
//				
//			}
		}
	},
	_registerEvents4Pool : function() 
	{
		with (this) 
		{
			$('.cloneDiv .closeLnk, .cloneDiv .closeWndLnk').click(function(e) 
			{
				e.preventDefault();
				$('.popup_type1').remove();
				
				$('.cloneDiv').remove('#vs_ipList');
//				$('.cloneDiv').nextSibling().remove();
			});
			
			// search event
			$('p.sch a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $(this).siblings('input.searchTxt').val();
				FlowitUtil.log('click:' + searchKey);
				_loadUsedVServer4PoolAlteon(searchKey);
			});
		}
	},
	_getVServerNameHtmlRows : function(vServerNames) 
	{
		if(vServerNames==null)
			return '';
		
		var html = '';
		for (var i=0; i < vServerNames.length; i++) 
		{
			html += '<tr>';
			html += '<td class="align_center">' + (i+1) + '</td>';
			html += '<td class="align_left_P10">' + vServerNames[i].name + '</td>';
			html += '<td class="align_center">' + vServerNames[i].virtualIp + '</td>';
			html += '</tr>';
		}
		FlowitUtil.log('html: ' + html);
		
		return html;
	},
	_applyTableCss : function() 
	{
		initTable([ ".cloneDiv .table_type1 tbody tr" ], [ 1, 2], [ -1 ]);
	},
	
	delSlbUsers : function(chkDel)
	{
		with (this)
		{
//			var checkedSlbUsers = _getCheckedSlbUsers();
//			
//			if(checkedSlbUsers.length == 0)
//			{
//				$.obAlertNotice(VAR_COMMON_REQ_DSELECT);
//				return;
//			}
//			
//			var chk = confirm(VAR_SYSTOOLS_RESPDEL)
//			if(chk == false)
//			{
//				return;
//			}
			
			ajaxManager.runJsonExt({
				url : "slbSchedule/delSlbUser.action",
				data :
				{
					"slbUserIndexes" : chkDel
				},
				successFn : function(data)
				{
					if (!data.isSuccessful) 
					{
						$.obAlertNotice(data.message);
						return;
					}
//					slbSchedule.loadListContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_ADCINFODDELFAIL, jqXhr);
				}
			});
		}
	},
	
	_getCheckedSlbUsers : function()
	{
//		var chkSlbUsers = $('.cloneDiv .delSlbUsersLnk').parent().parent().find('.requestorChk').filter(':checked').map(function() {
		var chkSlbUsers = $('.cloneDiv .requestorChk').filter(':checked').map(function() {
			return $(this).val();
		}).get();
		
		return chkSlbUsers;
	}
});