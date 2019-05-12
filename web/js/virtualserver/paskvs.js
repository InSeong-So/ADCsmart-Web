var PASKVs = Class.create({	// PASK VS 시작부분
	initialize : function() 
	{
		this.adc = {};
		this.newPoolName = '';
		this.vServerNameWnd = new VServerNameWnd();
		this.persistenceDetailWnd = new PersistenceDetailWnd();
		this.portChangeWndPASK = new PortChangeWndPASK();
		this.MemberAddBatchWndPASK = new MemberAddBatchWndPASK();
		this.initFlag=false;
	},
	setAdc : function(adcIndex, adcType, adcName) 
	{
		this.adc.index = adcIndex;
		this.adc.type = adcType;
		this.adc.name = adcName;
		this.vServerNameWnd.setAdc(this.adc);
		this.persistenceDetailWnd.setAdc(this.adc);
	},
	loadVsAddContent : function()
	{
		with (this) 
		{
			FlowitUtil.log('loadVsAddContent ' + adcSetting.getAdc().index);
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadPASKVsAddContent.action",
				data : 
				{
					"paskVsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					registerVsAddContentEvents(false);
					_applyMemberTableCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
				}	
			});
		}
	},
	loadVsModifyContent : function(index) 
	{
		with (this) 
		{
			FlowitUtil.log('loadVsModifyContent ' + adcSetting.getAdc().index + ', ' + index);
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadPASKVsModifyContent.action",
				data : 
				{
					"paskVsAdd.index" : index,
					"paskVsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					registerVsAddContentEvents(true);
					_applyMemberTableCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
				}	
			});
		}
	},
	loadVsModifyByIpContent : function(ip) 
	{
		with (this) 
		{
			FlowitUtil.log('loadVsModifyContent: ' + adc.index + ', ' + ip);
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadPASKVsModifyContent.action",
				data : 
				{
					"paskVsAdd.adcIndex" : adcSetting.getAdc().index,
					"paskVsAdd.ip" : ip,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					registerVsAddContentEvents(true);
					_applyMemberTableCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
				}	
			});
		}
	},
	registerVsAddContentEvents : function(isModify) 
	{
		with (this) 
		{
			$('#paskVsAddFrm').submit(function() 
			{
				if (!_validateVsAdd())
					return false;
				
				_updatePoolIndexInPoolCbxByPoolNameInPoolText();
				FlowitUtil.log(Object.toJSON(_getRegMembers()));
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? 'virtualServer/modifyPASKVs.action' : 'virtualServer/addPASKVs.action',
					data : 
					{ 
	//					"paskVsAdd.members" : [{"ip":"192.168.0.1","port":"1","isEnabled":"true"},{"ip":"192.168.0.4","port":"3","isEnabled":"false"}],
			    		"paskVsAdd.membersInString" : Object.toJSON(_getRegMembers())
					},
					success : function(data) 
					{
						if (!data.isSuccessful) 
						{
							$.obAlertNotice(data.message);
							return;
						}
						
						virtualServer.loadListContent();
					},
					error : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
//						exceptionEvent();
					}
				}); 
			    
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
			// 하단 registerVsAddContentEvents 함수는 이중화 기능이 구현되면 사용하기 위해 주석처리합니다. 2013.07.29
			
			$('input[name="paskVsAdd.name"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if (!$(this).val())
					return;
				
				_showVirtualSvrNameAvailable($(this));
			});
			
			$('input[name="paskVsAdd.ip"]').blur(function(e) 
			{			
				FlowitUtil.log('blur! -- ' + $(this).val());
				if(FlowitUtil.checkIp($(this).val()))
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>' + VAR_COMMON_AVAIL);
				else
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
			});					
			
			$('input[name="paskVsAdd.port"]').blur(function(e) 
			{						
				if (getValidateNumberRange(port, 0, 65535) == false)
				{	
					if ($(this).val() < 65535) 
					{	
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
					else 
					{	
						$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
						$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
						$('input[name="paskVsAdd.port"]').val('');
						$('input[name="paskVsAdd.port"]').focus();
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
				}
				else 
				{ 
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
					$('input[name="paskVsAdd.port"]').val('');
					$('input[name="paskVsAdd.port"]').focus();
					$(this).next().html('<input type=hidden class="mar_rgt5"/>');
				}
			});
			
			$('.popUpVServerNameWndLnk').click(function(e) 
			{
				vServerNameWnd.popUp();
			});
			
			$('.protocolCbx').change(function(e) 
			{
				$('input[name="paskVsAdd.port"]').val($(this).val());
				$('input[name="paskVsAdd.protocol"]').val(6);
			});
			
			$('input[name="paskVsAdd.port"]').change(function(e) 
			{
				$('input[name="paskVsAdd.protocol"]').val(6);
			});			

			// PAS-K Health Check SelectBox Changer
			$('.healthCbx').change(function(e) 
			{
				FlowitUtil.log('seldbIndex: ' + $(this).prop('selectedDbIndex'));
				_fillPASKHealths($(this).val());
			});			
			
			$('.memberAddLnk').click(function(e) 
			{
				e.preventDefault();
				moveMemberInputToMemberList();
			});
			
			$('.onMemberAddBatch').click(function(e) 
			{
				e.preventDefault();
				MemberAddBatchWndPASK.popUp();
			});
			
			$('#selectedMember').fixheadertable({			
				//caption     : 'My employees', 
			    colratio    : [50, 150, 50, 150, 0], 
			    height      : 200, 
			    width       : 400, 
			    zebra       : false,
			    resizeCol   : false,
			    minColWidth : 50 
			});	
			
			$('#memberList').fixheadertable({
			    colratio    : [150, 80], 
			    height      : 200, 
			    width       : 250, 
			    zebra       : true,
			    resizeCol   : true,
			    minColWidth : 50 			
			});	
			
			$('.memberTbd').on('dblclick', '.regMemberTr', function(e) 
			{
				if (header.getAccountRole() == "readOnly")
				{
					return;
				}
				// configurable 이 0 일시에는 이벤트 작동 false
				var configurable = $('input[name="paskVsAdd.configurable"]').val();
				if (configurable == "")
				{
					configurable = 1;
				}
				if (configurable == 0)
				{
					return false;
				}				
				with (this)
				{
					var ip = $(this).children().eq(2).text();
					var port = $(this).children().eq(3).text();				
					var state = $(this).children().eq(6).text();
					var id = $(this).children().eq(5).text();
					var html = '';					
					$(this).remove();
					if (id == -1)	// Member를 수동으로 추가할 시에는 id -1값으로 node List에 보내지 않는다.
					{
						_applyMemberTableCss();
						//_registerNodeListTableEvents();
						_setPoolMemberCount();
						_setAdcNodeCount();					
					}
					else
					{
						html += '<tr class="adcNodeTr">';
						html += '<td class="adcNodeIp align_left_P10">' + ip + '</td>';
						html += '<td class="adcNodePort align_center">' + port + '</td>';
						html += '<td class="adcNodeState" style="display: none;">'  + state + '</td>';
						html += '<td class="adcNodeId" style="display: none;">' + id + '</td>';
						html += '</tr>';					
		
						$('.adcNodeTbd').append(html);						
						_applyMemberTableCss();
						_registerNodeListTableEvents();
						_setPoolMemberCount();
						_setAdcNodeCount();
					}
				}
			});
			
			_registerNodeListTableEvents();
			
			$('.virtualServerAddCancelLnk').click(function(e) 
			{
				with (this) 
				{
					e.preventDefault();
					virtualServer.loadListContent();
				}
			});
			
			$('.virtualServerAddOkLnk').click(function(e) 
			{
				e.preventDefault();
				$('#paskVsAddFrm').submit();
			});			
			
			$('.allMembersChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$('#selectedMember .memberChk').attr('checked', isChecked);
			});
			
			$('.enableMembersLnk').click(function(e) 
			{
				e.preventDefault();
				var $memberTRs = _getCheckedMemberTRs();
				FlowitUtil.log($memberTRs.length);
				
				if ($memberTRs.length == 0) 
				{
					$.obAlertNotice(VAR_COMMON_EMSELECT);
					return;
				}
				
				$memberTRs.each(function() 
				{
					FlowitUtil.log($(this).html());
				});
				$memberTRs.find('td option[value="true"]').attr('selected', true);
			});
			
			$('.disableMembersLnk').click(function(e) 
			{
				e.preventDefault();
				var $memberTRs = _getCheckedMemberTRs();
				
				if ($memberTRs.length == 0) 
				{
					$.obAlertNotice(VAR_COMMON_DMSELECT);
					return;
				}
				
				$memberTRs.find('td option[value="false"]').attr('selected', true);
			});
		
			$('.delMembersLnk').click(function(e) 
			{	//Virtual Server Member 삭제(PASK)
				with (this) 
				{
					e.preventDefault();
										
					var chkDel = $(this).parent().parent().parent().parent().find('.memberChk').filter(':checked').map(function() {
						return $(this).val();
					}).get();				
					
					if (chkDel.length == 0) 
					{
						$.obAlertNotice(VAR_COMMON_MDSELECT);
						return;
					}
						
					var $memberTRs = _getCheckedMemberTRs();
					
					var chk = confirm(VAR_COMMON_MDEL);
					if (chk) 
					{
						$memberTRs.remove();
						_setPoolMemberCount();
					}
					else 
					{
						return false;
					}
//					$memberTRs.remove();
//					_setPoolMemberCount();
				}
			});
			
			$('.changeMemberPortsLnk').click(function(e) 
			{				
				e.preventDefault();
				
				if (_getCheckedMemberTRs().length == 0) 
				{
					$.obAlertNotice(VAR_COMMON_PMSELECT);
					return;
				}
				
				portChangeWndPASK.popUp(_getCheckedMemberTRs());
			});			
			
			_registerVsNameAutoCompleteEvents();
			
			if (adcSetting.getAdcStatus() != "available")
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				$('.allServersChk').attr("disabled", "disabled");
				$('.serverChk').attr("disabled", "disabled");
				
				if ($('.virtualServerList').size() > 0 )
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
	
	_registerVsNameAutoCompleteEvents : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "virtualServer/loadVsNameList.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				successFn : function(data)
				{					
					$('#idVsName').autocomplete({
						source : data.vsNameList
					});
					
				},
				completeFn : function(textStatus)
				{

				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VNLFAIL, jqXhr);
//					exceptionEvent();
				}
			});			
		}
		
	},
	_validateVsAdd : function() 
	{
		var virtualSvrIp = $('input[name="paskVsAdd.ip"]').val();
		if (getValidateIP(virtualSvrIp) == false) 
		{
			$.obAlertNotice(VAR_COMMON_IPFORMAT);
			return false;
		}
		
		// from GS. #4012-1 #11, #3926-4 18: 14.07.22 sw.jung obvalidation 활용 개선
//		var virtualSvrName = $('input[name="paskVsAdd.name"]').val();
//		if (virtualSvrName == "")
//		{
//			alert(VAR_COMMON_NAME);
//			return false;
//		}
//		else if (getValidateStringint(virtualSvrName, 1, 64) == false)
//		{
//			alert(VAR_COMMON_VNOTALLOWED);
//			return false;
//		}
		if (!$('input[name="paskVsAdd.name"]').validate(
			{
				name: $('input[name="paskVsAdd.name"]').parent().parent().find('li').text(),
				required: true,
				type: "en_name",
				lengthRange: [1,64]
			}))
		{
			return false;
		}
		
		var port = $('input[name="paskVsAdd.port"]').val();
		if (!port) 
		{
			$.obAlertNotice(VAR_COMMON_PINPUT);
			return false;
		}
		
		if (getValidateNumberRange(port, 0, 65535) == false)
		{
			$.obAlertNotice(VAR_PASK_PORTIFORMAT);
			return false;
		} 
		return true;
	},
	_updatePoolIndexInPoolCbxByPoolNameInPoolText : function() 
	{
		with (this) 
		{
			var poolName = $('input[name="paskVsAdd.poolName"]').val();
			FlowitUtil.log(poolName);
			var pools = $('.poolCbx option').map(function() {
				return {
					"index" : $(this).val(),
					"name" :$(this).text()
				};
			}).get();
			for (var i=0; i < pools.length; i++) 
			{
				FlowitUtil.log(Object.toJSON(pools[i]));
				FlowitUtil.log(i + ": " + (pools[i].name == poolName));
				if (pools[i].name == poolName) 
				{
					var $poolOptionToSelect = $('.poolCbx option[value="' + pools[i].index + '"]');
					$poolOptionToSelect.attr('selected', true);
					return;
				}
			}
			
			$('.poolCbx option').filter(':first').attr('selected', true);
		}
	},
	_showVirtualSvrNameAvailable : function($virtualSvrName) 
	{
		FlowitUtil.log("_showVirtualSvrNameAvailable: %s", $virtualSvrName.val());
		if (!virtualServer.isValidVirtualSvrName($virtualSvrName.val())) 
		{
			$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
			return;
		}
		
		with (this) 
		{
			taskQ.add(function() 
			{
				ajaxManager.runJsonExt({
					url : "virtualServer/existsVirtualSvrName.action",
					data : 
					{
						"adc.index" : adcSetting.getAdc().index,
						"adc.type" : adcSetting.getAdc().type,
						"virtualSvrPASK.name" : $virtualSvrName.val()
					},
					successFn : function(data) 
					{
						FlowitUtil.log('existsVirtualSvr: ' + data.existsVirtualSvr);
						if(data.existsVirtualSvr)
							$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
						else {
							$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>' + VAR_COMMON_AVAIL);
							setNewPoolName($virtualSvrName.val());
						}
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_COMMON_VEIFAIL, jqXhr);
//						exceptionEvent();
					}	
				}, taskQ);
			});
			taskQ.start(true);
		}
	},
	setNewPoolName : function(vsName) 
	{
		with (this) 
		{
			var adcPools = $('.poolCbx option').filter(':gt(0)').map(function(val, i) 
			{
				return $(this).text();
			});
//			FlowitUtil.log(Object.toJSON(adcPools));
			var maxNo = 0;
			for (var i=0; i < adcPools.length; i++) 
			{
				FlowitUtil.log(adcPools[i]);
				var rex = new RegExp(vsName + '_Pool_\\d+$');
				var isSamePattern = rex.test(adcPools[i]);
				FlowitUtil.log(isSamePattern);
				if (!isSamePattern)
					continue;
				
				var lastNo = parseInt(adcPools[i].substring(adcPools[i].lastIndexOf('_') + 1));
				if (lastNo > maxNo)
					maxNo = lastNo;
			}
				
			newPoolName = vsName + '_Pool_' + (maxNo+1);
			FlowitUtil.log(newPoolName);
			$('input[name="paskVsAdd.poolName"]').val(newPoolName);
		}
	},
	// membersInString FOR convertMembersToJSON 매핑 부분
	_getRegMembers : function() 
	{
		return $('.regMemberTr').map(function() {
			$children = $(this).children();
			return {
				"index" : $children.eq(1).text(),
				"ip" : $children.eq(2).text(),
				"port" : $children.eq(3).text(),
				"isEnabled" : $children.eq(4).children().eq(0).val(),
				"id" : $children.eq(5).text()
			};
		}).get();
	},
//	_fillVirtualSvc : function(poolIndex) 
//	{
//		with (this) 
//		{
//			FlowitUtil.log('poolIndex: ' + poolIndex);
//			if (!poolIndex) 
//			{
//				_addVirtualSvcMembersToTable(null);
//				_fillLoadBalancingTypes(null);
//				_fillHealthCheckTypes(null);
//				return;
//			}
//			
//			ajaxManager.runJsonExt({
//				url : "virtualServer/retrieveVirtualSvc.action",
//				data : 
//				{
//					"adc.index" : adcSetting.getAdc().index,
//					"adc.type" : adcSetting.getAdc().type,
//					"poolIndex" : poolIndex
//				},
//				successFn : function(data) 
//				{
//					FlowitUtil.log('_fillVirtualSvc: ' + Object.toJSON(data.virtualSvc));
//					_addVirtualSvcMembersToTable(data.virtualSvc ? data.virtualSvc.members : null);
//					_fillLoadBalancingTypes(data.virtualSvc ? data.virtualSvc.loadBalancingType : null);
//					_fillHealthCheckTypes(data.virtualSvc ? data.virtualSvc.healthCheckType : null);
//				},
//				completeFn : function(textStatus) 
//				{
//					FlowitUtil.log('_fillVirtualSvc: ' + textStatus);
//				},
//				errorFn : function(a,b,c)
//				{
//					exceptionEvent();
//				}
//			});
//		}
//	},
	
	_fillPASKHealths : function(healthCheckDbIndex) 
	{
		with (this) 
		{
			FlowitUtil.log('healthCheckDbIndex: ' + healthCheckDbIndex);
			if (!healthCheckDbIndex) 
			{				
				_fillhealthCheckDbIndex(null);
				_fillhealthCheckId(null);
				_fillhealthCheckName(null);
				_fillhealthCheckType(null);
				_fillhealthCheckPort(null);
				_fillhealthCheckInterval(null);
				_fillhealthCheckTimeout(null);
				_fillhealthCheckState(null);				
				return;
			}
			
			ajaxManager.runJsonExt({
				url : "virtualServer/retrievePASKHealths.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"healthCheckDbIndex" : healthCheckDbIndex
				},
				successFn : function(data) 
				{
					FlowitUtil.log('_fillPASKHealths: ' + Object.toJSON(data.PASKHealths));
					_fillhealthCheckDbIndex(data.PASKHealths ? data.PASKHealths.dbIndex : null);
					_fillhealthCheckId(data.PASKHealths ? data.PASKHealths.id : null);
					_fillhealthCheckName(data.PASKHealths ? data.PASKHealths.name : null);
					_fillhealthCheckType(data.PASKHealths ? data.PASKHealths.type : null);
					_fillhealthCheckPort(data.PASKHealths ? data.PASKHealths.port : null);
					_fillhealthCheckInterval(data.PASKHealths ? data.PASKHealths.interval : null);
					_fillhealthCheckTimeout(data.PASKHealths ? data.PASKHealths.timeout : null);
					_fillhealthCheckState(data.PASKHealths ? data.PASKHealths.state : null);				
				},
				completeFn : function(textStatus) 
				{
					FlowitUtil.log('_fillPASKHealths: ' + textStatus);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PASK_HEAEFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	
	_fillhealthCheckDbIndex : function(dbIndex) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckDbIndex"]').val(dbIndex);
		}
	},
	_fillhealthCheckId : function(id) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckId"]').val(id);
		}
	},
	_fillhealthCheckName : function(name) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckName"]').val(name);
		}
	},
	_fillhealthCheckType : function(type) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckType"]').val(type);
		}
	},
	_fillhealthCheckPort : function(port) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckPort"]').val(port);
		}
	},
	_fillhealthCheckInterval : function(interval) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckInterval"]').val(interval);
		}
	},
	_fillhealthCheckTimeout : function(timeout) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckTimeout"]').val(timeout);
		}
	},
	_fillhealthCheckState : function(state) 
	{
		with (this) 
		{
			$('input[name="paskVsAdd.healthCheckState"]').val(state);
		}
	},
	
	_addVirtualSvcMembersToTable : function(virtualSvcMembers) 
	{
		with (this) 
		{
			var html = '';
			if (virtualSvcMembers) 
			{
				for (var i=0; i < virtualSvcMembers.length; i++) 
				{
					html += '<tr class="regMemberTr">';
					html += '<td class="c_1"><input class="memberChk" type="checkbox"/></td>';
					html += '<td class="c_2">' + virtualSvcMembers[i].ip + '</td>';
					html += '<td class="c_3">' + virtualSvcMembers[i].port + '</td>';
					html += '<td class="c_3"><select><option value="true" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? 'selected="selected"' : '') +'>Enabled</option><option value="false" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
					html += '</tr>';
				}
			}			
			FlowitUtil.log(html);
			$('.memberTbd').empty().append(html);
			_applyMemberTableCss();
	//		_registerMemberTableEvents();
			FlowitUtil.log($('.memberTbd').html());
			_setPoolMemberCount();
		}
	},
	_setPoolMemberCount : function() 
	{
		$('.poolMemberCount').text($('.memberTbd > tr').length);
	},
	_fillAdcNodes : function(poolIndex) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveAdcNodes.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"poolIndex" : poolIndex
				},
				successFn : function(data) 
				{
					FlowitUtil.log('_fillAdcNodes: ' + Object.toJSON(data.adcNodes));
					//_addAdcNodesToTbl(data.adcNodes);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_REFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_addAdcNodesToTbl : function(adcNodes) 
	{
		with (this) 
		{
			var html = '';
			for (var i=0; i < adcNodes.length; i++) 
			{
				html += '<select>';
				html += '<option class="adcNodeIp">' + adcNodes[i].ip + '</td>';
				html += '</select>';
			}
			
			$('.adcNodeTbd').empty().append(html);
			_applyMemberTableCss();
			//_registerNodeListTableEvents();
			_setAdcNodeCount();
		}
	},
	_setAdcNodeCount : function() 
	{
		$('.adcNodeCount').text($('.adcNodeTbd > tr').length);
	},
//	_fillLoadBalancingTypes : function(type) 
//	{
//		var options = '';
//		if (type === 'NOT_ALLOWED') 
//		{
//			options += '<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>';
//		} else {
//			options += '<option value="Round Robin"' + (type === 'Round Robin' ? 'selected="selected"':'') + '>Round Robin</option>';
//			options += '<option value="Least Connections"' + (type === 'Least Connections' ? 'selected="selected"':'') + '>Least Connections</option>';
//			options += '<option value="Hash"' + (type === 'Hash' ? 'selected="selected"':'') + '>Hash</option>';
//		}
//		
//		$('select[name="paskVsAdd.loadBalancingType"]').empty().append(options);
//	},
//	_fillHealthCheckTypes : function(type) 
//	{
//		var options = '';
//		if (type === 'NOT_ALLOWED') 
//		{
//			options += '<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>';
//		} 
//		else 
//		{
//			options += '<option value="NONE"' + (type === 'NONE' ? 'selected="selected"':'') + '>지정 안함</option>';
//			options += '<option value="TCP"' + (type === 'TCP' ? 'selected="selected"':'') + '>TCP</option>';
//			options += '<option value="HTTP"' + (type === 'HTTP' ? 'selected="selected"':'') + '>HTTP</option>';
//			options += '<option value="HTTPS"' + (type === 'HTTPS' ? 'selected="selected"':'') + '>HTTPS</option>';
//			options += '<option value="UDP"' + (type === 'UDP' ? 'selected="selected"':'') + '>UDP</option>';
//			options += '<option value="GATEWAY_ICMP"' + (type === 'GATEWAY_ICMP' ? 'selected="selected"':'') + '>GATEWAY_ICMP</option>';
//		}
//		
//		$('select[name="paskVsAdd.healthCheckType"]').empty().append(options);
//	},
	moveMemberInputToMemberList : function() 
	{		
		with (this)
		{
			var $index = null;
			var $ip = $('.memberIpTxt');
			var $port = $('.memberPortTxt');
			var $enabled = $('.memberEnabledChk');
			var $id = -1;		
			if (!_validateMemberInput($ip.val(), $port.val()))
				return false;
			
			addToMemberTable($index, $ip.val(), $port.val(), $enabled.is(':checked'), $id);
			_delFromNodeList();
			$ip.val('');
			$port.val('');
//		$enabled.attr('checked', true);
		}
	},
	_validateMemberInput : function(ip, port) 
	{
		with (this) 
		{
			if (ip == '') 
			{
				$.obAlertNotice(VAR_COMMON_IPINPUT);
				return false;
			} 			
			else if (!getValidateIP(ip))
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			} 
			
			if (port == '') 
			{
				$.obAlertNotice(VAR_COMMON_PINPUT);
				return false;
			} 
			else if (!getValidateNumberRange(port, 0, 65535))
			{
				$.obAlertNotice(VAR_COMMON_PIFORMAT);
				return false;
			} 
			else if (_isMember(ip, port)) 
			{
//				$.obAlertNotice('IP ' + ip + VAR_COMMON_PORT + port + VAR_COMMON_ARMEMBER);
				$.obAlertNotice('IP : ' + ip + ', '+ VAR_COMMON_PORT + ' : ' +  port + ' ' + VAR_COMMON_ARMEMBER);
				return false;
			}
			
			return true;
		}
	},
	_isMember : function(ip, port) 
	{
		var regMemberIpTd = $('.regMemberTr').children(':nth-child(3)');	// regMemberIpTd 에 ip값 초기화
		var regMemberPortTd = $('.regMemberTr').children(':nth-child(4)');	// regMemberPortTd dp port값 초기화
		for (var i=0; i < regMemberIpTd.length; i++) 
		{
			if (regMemberIpTd.eq(i).text() == ip && regMemberPortTd.eq(i).text() == port)	// port 와 ip 값 비교
				return true;
		}		
		return false;
	},
	addToMemberTable : function(index, ip, port, isEnabled, id, nodeState) 
	{
		with (this) 
		{
			var html = '<tr class="regMemberTr">';
			html += '<td class="align_center"><input class="memberChk" type="checkbox"/></td>';
			html += '<td class="align_left_P10" style="display: none;">' + index + '</td>';
			html += '<td class="align_left_P10">' + ip + '</td>';
			html += '<td class="align_center">' + port + '</td>';
			html += '<td class="align_center"><select><option value="true"' + (isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="false"' + (isEnabled ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
			html += '<td class="align_center" style="display: none;">' + id + '</td>';
			html += '<td class="align_center" style="display: none;">' + nodeState + '</td>';
			html += '</tr>';
			FlowitUtil.log(html);
			$('.memberTbd').append(html);
			_applyMemberTableCss();
	//		_registerMemberTableEvents();
			FlowitUtil.log($('.memberTbd').html());
			_setPoolMemberCount();
		}
	},
	addIpAndPortsToMemberTable : function(ipAndPorts) 
	{
		FlowitUtil.log('-- ' + Object.toJSON(ipAndPorts));
		with (this) 
		{
			var $enabled = $('.memberEnabledChk');
			var $id = -1;
			$.each(ipAndPorts, function(i, val) 
			{
				FlowitUtil.log('addToMemberTable: i - ' + i + ', ' + Object.toJSON(val));
				if (_validateMemberInput(val.ip, val.port))
				{
					addToMemberTable(null, val.ip, val.port, $enabled.is(':checked'), $id);
				}
			});
			
//			for (var i=0; i < ipAndPorts.length; i++) {
//				FlowitUtil.log('addIpAndPortsToMemberTable: i - ' + i + ', ' + Object.toJSON(ipAndPorts[i]));
//				addToMemberTable(ipAndPorts[i].ip, ipAndPorts[i].port, $enabled.is(':checked'));
//			}
		}
	},
	_delFromNodeList : function() 
	{
		with (this) 
		{
			$('.adcNodeTr.on').remove();
			_setAdcNodeCount();
		}
	},
	_applyMemberTableCss : function () 
	{
		// 리스트형 테이블 행 롤오버 효과
		$('.fixed-table tbody tr:not(.empty)').hover(function() {
			$(this).css({
				'background-color' : '#ccc',
				'cursor' : 'pointer'
			});
		}, function() {
			$(this).removeAttr('style');
		});
		$('.fixed-table_1 tbody tr:not(.empty)').hover(function() {
			$(this).css({
				'background-color' : '#ccc',
				'cursor' : 'pointer'
			});
		}, function() {
			$(this).removeAttr('style');
		});
		
		$('#selectedMember').parents('.t_fixed_header').find('.headtable').css('margin-right', '17px');
		$('#selectedMember').parents('.t_fixed_header').find('.body').css('overflow-y', 'scroll');
		$('#memberList').parents('.t_fixed_header').find('.headtable').css('margin-right', '17px');
		$('#memberList').parents('.t_fixed_header').find('.body').css('overflow-y', 'scroll');
	}, 
	_registerNodeListTableEvents : function()
	{		
		with (this) 
		{			
			// NodeList 더블클릭 이벤트
			$('.adcNodeTr').on('dblclick',function(e) 
			{
				e.preventDefault();
				with (this)
				{
					if (header.getAccountRole() == "readOnly")
					{
						return;
					}
				// 함수 중복 콜 방지 initFlag
					if(this.initFlag==true)
					{
						return;
					}
					this.initFlag=true;				
					// configurable 이 0 일시에는 이벤트 작동 false
					var configurable = $('input[name="paskVsAdd.configurable"]').val();
					if (configurable == "")
					{
						configurable = 1;
					}
					if (configurable == 0)
					{						
						return false;
					}
				
					//var adcNodeIndex = $(this).children(':nth-child(1)').text();
					var adcNodeIp = $(this).children(':nth-child(1)').text();
					var adcNodePort = $(this).children(':nth-child(2)').text();
					var adcNodeState = $(this).children(':nth-child(3)').text();			
					var adcNodeId = $(this).children(':nth-child(4)').text();					
					var isEnabled;
					if (adcNodeState == 'enable')
					{
						isEnabled = true;
					}
					if (adcNodeState == 'disable')
					{
						isEnabled = false;
					}
					// Member Table 의 Ip,Port 중복확인
					if (!_validateMemberInput(adcNodeIp, adcNodePort))
					{
						return false;
					}
				
					addToMemberTable(null, adcNodeIp, adcNodePort, isEnabled, adcNodeId, adcNodeState);
					$('.adcNodeTr').removeClass('on');
					$(this).addClass('on');
					_delFromNodeList();
					//adcNodeIndex = null;
					adcNodeIp = null;
					adcNodePort = null;
					adcNodeState = null;			
					adcNodeId = null;					
					isEnabled = null;
				}
			});
		}		
	},	
	
	_getCheckedMemberTRs : function() 
	{
		return $('#selectedMember .memberChk').filter(':checked').parent().parent();
	},
});