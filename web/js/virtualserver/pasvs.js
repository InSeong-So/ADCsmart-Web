var PASVs = Class.create({	// PAS VS 시작부분
	initialize : function() 
	{
		this.adc = {};
		this.newPoolName = '';
		this.vServerNameWnd = new VServerNameWnd();
		this.persistenceDetailWnd = new PersistenceDetailWnd();
		this.portChangeWnd = new PortChangeWnd();
		this.memberAddBatchWndPas = new MemberAddBatchWndPas();
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
				url : "virtualServer/loadPASVsAddVsAddContent.action",
				data : 
				{
					"pasVsAdd.adcIndex" : adcSetting.getAdc().index,
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
//					exceptionEvent();
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
				url : "virtualServer/loadPASVsModifyContent.action",
				data : 
				{
					"pasVsAdd.index" : index,
					"pasVsAdd.adcIndex" : adcSetting.getAdc().index,
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
//					exceptionEvent();
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
				url : "virtualServer/loadPASVsModifyContent.action",
				data : 
				{
					"pasVsAdd.adcIndex" : adcSetting.getAdc().index,
					"pasVsAdd.ip" : ip,
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
//					exceptionEvent();
				}	
			});
		}
	},
	registerVsAddContentEvents : function(isModify) 
	{
		with (this) 
		{
			$('#pasVsAddFrm').submit(function() 
			{
				if (!_validateVsAdd())
					return false;
				
				_updatePoolIndexInPoolCbxByPoolNameInPoolText();
				FlowitUtil.log(Object.toJSON(_getRegMembers()));
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? 'virtualServer/modifyPASVs.action' : 'virtualServer/addPASVs.action',
					data : 
					{ 
	//					"pasVsAdd.members" : [{"ip":"192.168.0.1","port":"1","isEnabled":"true"},{"ip":"192.168.0.4","port":"3","isEnabled":"false"}],
			    		"pasVsAdd.membersInString" : Object.toJSON(_getRegMembers())
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
						$.obAlertAjaxError(VAR_COMMON_PVSAMFAIL, jqXhr);
//						exceptionEvent();
					}
				}); 
			    
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
				
			$('input[name="pasVsAdd.name"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if (!$(this).val())
					return;
				
				_showVirtualSvrNameAvailable($(this));
			});
			
			$('input[name="pasVsAdd.ip"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if(FlowitUtil.checkIp($(this).val()))
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>' + VAR_COMMON_AVAIL);
				else
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
			});		
			
			$('input[name="pasVsAdd.port"]').blur(function(e) 
			{						
				if(checkNum($(this).val())) 
				{	
					if ($(this).val() < 65535) 
					{	
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
					else 
					{	
						$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
						$.obAlertNotice(VAR_COMMON_VSPRDISAGREE);
						$('input[name="pasVsAdd.port"]').val('');
						$('input[name="pasVsAdd.port"]').focus();
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
				}
				else 
				{ 
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
//					alert('입력하신 가상서버 포트는 올바른 방식이 아닙니다.');
					$.obAlertNotice(VAR_COMMON_VSPRDISAGREE);
					$('input[name="pasVsAdd.port"]').val('');
					$('input[name="pasVsAdd.port"]').focus();
					$(this).next().html('<input type=hidden class="mar_rgt5"/>');
				}
			});
			
			$('.popUpVServerNameWndLnk').click(function(e) 
			{
				vServerNameWnd.popUp();
			});
			
			$('.protocolCbx').change(function(e) 
			{
				$('input[name="pasVsAdd.port"]').val($(this).val());
				$('input[name="pasVsAdd.protocol"]').val(6);
				
			});
			
			$('input[name="pasVsAdd.port"]').change(function(e) 
			{
				$('input[name="pasVsAdd.protocol"]').val(6);
			});
			
			$('.memberAddLnk').click(function(e) 
			{
				e.preventDefault();
				moveMemberInputToMemberList();
			});
			
			$('.onMemberAddBatch').click(function(e) 
			{
				e.preventDefault();
				memberAddBatchWndPas.popUp();
			});
			
			$('#selectedMember').fixheadertable({			
				//caption     : 'My employees', 
			    colratio    : [50, 150, 50, 150], 
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
				with (this) 
				{
					if (header.getAccountRole() != "readOnly")
					{
						var ip = $(this).children().eq(1).text();
						$(this).remove();
						$('.adcNodeTd').eq(0).parent().parent().append('<tr><td class="adcNodeTd ui-widget-content">' + ip + '</td></tr>');
						_applyMemberTableCss();
						_registerNodeListTableEvents();
						_setPoolMemberCount();
						_setAdcNodeCount();
					}
					else
					{
						return;
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
				$('#pasVsAddFrm').submit();
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
			{		//Virtual Server Member 삭제(PAS)
				with (this) 
				{
					e.preventDefault();
										
					var chkDel = $(this).parent().parent().parent().parent().find('.memberChk').filter(':checked').map(function() {
						return $(this).val();
					}).get();
					
					//alert("chkDel.length : " + chkDel.length);
					
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
				portChangeWnd.popUp(_getCheckedMemberTRs());
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
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
//					exceptionEvent();
				}
			});			
		}
		
	},
	_validateVsAdd : function() {
		var virtualSvrIp = $('input[name="pasVsAdd.ip"]').val();
		// from GS. #4012-1 #11, #3926-4 #5: 14.07.29 sw.jung obvalidation 활용 개선
//		if (getValidateIP(virtualSvrIp) == false) 
//		{
//			alert(VAR_COMMON_VIFORMAT);
//			return false;
//		}
		if (!$('input[name="pasVsAdd.ip"]').validate(
			{
				name: $('input[name="pasVsAdd.ip"]').parent().parent().find('li').text(),
				required: true,
				type: "ip"
			}))
		{
			return false;
		}
		// from GS. #4012-1 #11, #3926-4 #5: 14.07.29 sw.jung obvalidation 활용 개선
//		var virtualSvrName = $('input[name="pasVsAdd.name"]').val();
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
//		if (!virtualServer.isValidVirtualSvrName(virtualSvrName)) 
//		{ //alert(" 1 - virtualSvrName : " + virtualSvrName);
//			alert(VAR_COMMON_VNOTALLOWED);
//			return false;
//		}
		if (!$('input[name="pasVsAdd.name"]').validate(
			{
				name: $('input[name="pasVsAdd.name"]').parent().parent().find('li').text(),
				required: true,
				type: "en_name",
				lengthRange: [1,64]
			}))
		{
			return false;
		}
		
//		var poolName = $('input[name="pasVsAdd.poolName"]').val();
//		if (!poolName) 
//		{
//			alert('Pool 이름이 입력되지 않았습니다.');
//			return false;
//		}
		
		var port = $('input[name="pasVsAdd.port"]').val();
		if (!port) 
		{
			$.obAlertNotice(VAR_PAS_PORNINP);
			return false;
		}
		if (getValidateNumberRange(port, 0, 65535) == false)
		{
			$.obAlertNotice(VAR_COMMON_PIFORMAT);
			return false;
		} 
		
		return true;
	},
	_updatePoolIndexInPoolCbxByPoolNameInPoolText : function() 
	{
		with (this) 
		{
			var poolName = $('input[name="pasVsAdd.poolName"]').val();			
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
						"virtualSvrPAS.name" : $virtualSvrName.val()
					},
					successFn : function(data) 
					{
						FlowitUtil.log('existsVirtualSvr: ' + data.existsVirtualSvr);
						if(data.existsVirtualSvr)
						{
							$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
						}
						else 
						{
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
			var adcPools = $('.poolCbx option').filter(':gt(0)').map(function(val, i) {
				return $(this).text();
			});
//			FlowitUtil.log(Object.toJSON(adcPools));
			for (var i=0; i < adcPools.length; i++) 
			{
				FlowitUtil.log(adcPools[i]);
				var rex = new RegExp(vsName);
				var isSamePattern = rex.test(adcPools[i]);
				FlowitUtil.log(isSamePattern);
				if (!isSamePattern)
					continue;				
			}
				
			newPoolName = vsName;
			FlowitUtil.log(newPoolName);
			$('input[name="pasVsAdd.poolName"]').val(newPoolName);
		}
	},
	_getRegMembers : function() 
	{
		return $('.regMemberTr').map(function() {
			$children = $(this).children();
			return {
				"ip" : $children.eq(1).text(),
				"port" : $children.eq(2).text(),
				"isEnabled" : $children.eq(3).children().eq(0).val(),
				"id" : $children.eq(4).text()
			};
		}).get();
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
					html += '<td class="align_center"><input class="memberChk" type="checkbox"/></td>';
					html += '<td class="align_left_P10">' + virtualSvcMembers[i].ip + '</td>';
					html += '<td class="align_center">' + virtualSvcMembers[i].port + '</td>';
					html += '<td class="align_center"><select><option value="true" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? 'selected="selected"' : '') +'>Enabled</option><option value="false" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
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
	_addAdcNodesToTbl : function(adcNodes) 
	{
		with (this) 
		{
			var html = '';
			for (var i=0; i < adcNodes.length; i++) 
			{
				html += '<tr>';
				html += '<td class="adcNodeTd">' + adcNodes[i].ip + '</td>';
				html += '</tr>';
			}
			
			$('.adcNodeTbd').empty().append(html);
			_applyMemberTableCss();
			_registerNodeListTableEvents();
			_setAdcNodeCount();
		}
	},
	_setAdcNodeCount : function() 
	{
		$('.adcNodeCount').text($('.adcNodeTbd > tr').length);
	},
	moveMemberInputToMemberList : function() 
	{
		with (this) 
		{
			var $ip = $('.memberIpTxt');
			var $port = $('.memberPortTxt');
			var $enabled = $('.memberEnabledChk');
			var $id = -1;
			if (!_validateMemberInput($ip.val(), $port.val()))
				return false;
			
			addToMemberTable($ip.val(), $port.val(), $enabled.is(':checked'), -1);
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
				$.obAlertNotice(VAR_COMMON_IPFORMAT);
				return false;
			} 
			else if (port == '') 
			{
				$.obAlertNotice(VAR_COMMON_PINPUT);
				return false;
			} 
			else if (!getValidateNumberRange(port, 0, 65535))
			{
				$.obAlertNotice(VAR_COMMON_PIFORMAT);
				return false;
			}
			else if (_isMember(ip)) 
			{
				$.obAlertNotice('IP ' + ip + VAR_COMMON_ARMEMBER);
				return false;
			}	
			return true;
		}
	},
	_isMember : function(ip) 
	{
		var regMemberIpTd = $('.regMemberTr').children(':nth-child(2)');	// regMemberIpTd 에 ip값 초기화
		for (var i=0; i < regMemberIpTd.length; i++) 
		{
			if(regMemberIpTd.eq(i).text() == ip)
			{
				return true;
			}
		}
		return false;
	},
	addToMemberTable : function(ip, port, isEnabled, id) 
	{
		with (this) 
		{
			var html = '<tr class="regMemberTr">';
			html += '<td class="align_center"><input class="memberChk" type="checkbox"/></td>';
			html += '<td class="align_left_P10">' + ip + '</td>';
			html += '<td class="align_center">' + port + '</td>';
			html += '<td class="align_center"><select><option value="true"' + (isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="false"' + (isEnabled ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
			html += '<td class="c_7" style="display: none;">' + id + '</td>';
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
			$.each(ipAndPorts, function(i, val) {
				FlowitUtil.log('addToMemberTable: i - ' + i + ', ' + Object.toJSON(val));
				addToMemberTable(val.ip, val.port, $enabled.is(':checked'), -1);
			});
			
//			for (var i=0; i < ipAndPorts.length; i++) {
//				FlowitUtil.log('addIpAndPortsToMemberTable: i - ' + i + ', ' + Object.toJSON(ipAndPorts[i]));
//				addToMemberTable(ipAndPorts[i].ip, ipAndPorts[i].port, $enabled.is(':checked'));
//			}
		}
	},
	// Node List 에서 Member 리스트로 넘어갈시 on/off 로 중복 제어한다.
	_delFromNodeList : function() 
	{
		with (this) 
		{
			$('.adcNodeTd.on').parent().remove();
			_setAdcNodeCount();
		}
	},
	_applyMemberTableCss : function () 
	{
		// 리스트형 테이블 행 롤오버 효과
//		$('.ipList tbody tr:not(.empty)').hover(function() {
		$('.fixed-table tbody tr:not(.empty)').hover(function() {
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
		$('.adcNodeTd').click(function(e) 
		{
			e.preventDefault();
			if (header.getAccountRole() != "readOnly")
			{
				var node = $(this).text();
				$('.memberIpTxt').val(node);
				$('.adcNodeTd').removeClass('on');
				$(this).addClass('on');
			}
			else
			{
				return;
			}
		});
	},
	_getCheckedMemberTRs : function() 
	{
		return $('#selectedMember .memberChk').filter(':checked').parent().parent();
	},
});