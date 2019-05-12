var F5Vs = Class.create({	// F5 VS 시작부분
	initialize : function() 
	{
		this.adc = {};
		this.newPoolName = '';
		this.vServerNameWnd = new VServerNameWnd();
		this.persistenceDetailWnd = new PersistenceDetailWnd();
		this.portChangeWnd = new PortChangeWnd();
		this.memberAddBatchWnd = new MemberAddBatchWnd();
		this.$unassignedAccountOptions = $();
		this.vsIndex = undefined;
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
				url : "virtualServer/loadF5VsAddContent.action",
				data : 
				{
					"f5VsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					$unassignedAccountOptions = $();
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
//			FlowitUtil.log('loadVsModifyContent ' + adcSetting.getAdc().index + ', ' + index);
//			console.log(adcVal.index);
//			console.log(adcVal.type);
//			console.log(adcVal.name);
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadF5VsModifyContent.action",
				data :
				{
					"f5VsAdd.index" : index,
					"f5VsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					$unassignedAccountOptions = $();
					vsIndex = index;
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
				url : "virtualServer/loadF5VsModifyContent.action",
				data : 
				{
					"f5VsAdd.adcIndex" : adcSetting.getAdc().index,
					"f5VsAdd.ip" : ip,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"adc.status" : adcSetting().getAdcStatus()
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
	_vsAddModify : function(isModify)
	{
		with (this)
		{
			_updatePoolIndexInPoolCbxByPoolNameInPoolText();
			FlowitUtil.log(Object.toJSON(_getRegMembers()));
			
			$(this).ajaxSubmit({
				dataType : 'json',
				url : isModify ? 'virtualServer/modifyF5Vs.action' : 'virtualServer/addF5Vs.action',
				data : 
				{ 
					"f5VsAdd.membersInString" : Object.toJSON(_getRegMembers())
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
//					if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
					/*
					if (jqXhr.responseText.indexOf('Sync failed') > -1)
					{
//						$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
//						$.obAlertNotice(VAR_VS_VSTIMEERROR);
//						var chk = confirm(VAR_VS_VSTIMEERROR + "\n" + VAR_NODE_SYNC_PROGRESSING);
						var chk = confirm(VAR_NODE_SYNC_PROGRESSING);
						if(chk) 
						{
							ajaxManager.runJsonExt({
								url : "virtualServer/loadRefreshListContent.action",
								data :
								{
									"adc.index" : adcSetting.getAdc().index,
									"adc.name" : adcSetting.getAdc().name,
									"adc.type" : adcSetting.getAdc().type,										
									"nodeIndexList" : checkedNodes,
									"state" : state,
									"refreshes" : true,
									"nodeCheckInString" : Object.toJSON(_getCheckedNodes()),
									"orderDir" : this.orderDir,
									"orderType" : this.orderType
								},
								successFn : function(data)
								{	
									if (!data.isSuccessful)
									{
										if(data.extraKey == 1)
										{
											return;
										}
										else
										{
											$.obAlertNotice(data.message);						
											return;							
										}	
									}	
									else
									{
										alert(VAR_NODE_SYNC_COMPLETE_RESET);										
//										loadNodeListContent(searchedKey, this.orderType, this.orderDir);
										loadVsModifyContent(vsIndex);
									}
								},
								errorFn : function(jqXhr)
								{
									pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
									$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
								}	
							});
						}
					}
					else
					{
						$.obAlertAjaxError(VAR_NODE_GROUP_SETFAIL, jqXhr);
					}	
					*/
					
					if (jqXhr.responseText.indexOf("Sync finished") > -1)
					{
						alert(VAR_NODE_SYNC_STATUS_RESET + " - " + VAR_NODE_SYNC_COMPLETE_RESET);
						
						loadNodeListContent(searchedKey, this.orderType, this.orderDir);
						loadVsModifyContent(vsIndex);				
					}
					else if (jqXhr.responseText.indexOf("Sync failed") > -1 || jqXhr.responseText.indexOf("Illegal null") > -1)
					{
						$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
						loadNodeListContent(searchedKey, this.orderType, this.orderDir);
						loadVsModifyContent(vsIndex);						
					}
				}
			}); 
		    
	        // always return false to prevent standard browser submit and page navigation
	        return false; 
		}
	},	
	selectRegisteredVlansForSubmit : function() 
	{
		$('#vlanTunnelSelectedSel > option').attr('selected', true);
	},
	registerVsAddContentEvents : function(isModify) 
	{
		with (this) 
		{
			
			$('#f5VsAddFrm').submit(function() 
			{
				if (!_validateVsAdd(isModify))
					return false;		
				
				if (header.accountRole == 'rsAdmin' && (_getEventRegMembers().length < 1))
				{
					alert("변경된 Member가 없습니다.");
					return false;		
				}
				
				_updatePoolIndexInPoolCbxByPoolNameInPoolText();
				FlowitUtil.log(Object.toJSON(_getRegMembers()));				
				
				selectRegisteredVlansForSubmit();
//				_vsAddModify(isModify);
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? 'virtualServer/modifyF5Vs.action' : 'virtualServer/addF5Vs.action',
					data : 
					{ 
			    		"f5VsAdd.membersInString" : Object.toJSON(_getRegMembers()),
			    		"f5VsAdd.eventMembersInString" : Object.toJSON(_getEventRegMembers())
					},
					success : function(data) 
					{
						if (!data.isSuccessful)
						{
							$.obAlertNotice(data.message);//중복 메세지 처리.
//							virtualServer.loadListContent();
							return;
						}
						
						ajaxManager.runJsonExt({
							url : "virtualServer/checkPairIndex.action",
							data :
							{
								"adc.index" : adcSetting.getAdc().index,
								"adc.name" : adcSetting.getAdc().name,
								"adc.type" : adcSetting.getAdc().type,
								"f5VsAdd.membersInString" : Object.toJSON(_getRegMembers()),
								"f5VsAdd.adcIndex" : data.pairIndex,		
								"f5VsAdd.index" : data.f5VsAdd.index,
								"f5VsAdd.ip" : data.f5VsAdd.ip,
								"f5VsAdd.name" : data.f5VsAdd.name,
								"f5VsAdd.port" : data.f5VsAdd.port,
								"f5VsAdd.poolName" : data.f5VsAdd.poolName,
								"f5VsAdd.loadBalancingType" : data.f5VsAdd.loadBalancingType,
								"f5VsAdd.healthCheckType" : data.f5VsAdd.healthCheckType,
								"f5VsAdd.profileIndex" : data.f5VsAdd.profileIndex
							},
							successFn : function(data)
							{								
								if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 
								{
									var chk = confirm(data.message);
//									var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
									if (chk)
									{
										$(this).ajaxSubmit({
											dataType : 'json',
											url : isModify ? 'virtualServer/modifyF5VsPeer.action' : 'virtualServer/addF5VsPeer.action',
											data : 
											{ 
												"adc.index" : adcSetting.getAdc().index,
												"adc.name" : adcSetting.getAdc().name,
												"adc.type" : adcSetting.getAdc().type,
												"f5VsAdd.membersInString" : Object.toJSON(_getRegMembers()),
												"f5VsAdd.adcIndex" : data.pairIndex,		
												"f5VsAdd.index" : data.f5VsAdd.index,
												"f5VsAdd.ip" : data.f5VsAdd.ip,
												"f5VsAdd.name" : data.f5VsAdd.name,
												"f5VsAdd.port" : data.f5VsAdd.port,
												"f5VsAdd.poolName" : data.f5VsAdd.poolName,
												"f5VsAdd.loadBalancingType" : data.f5VsAdd.loadBalancingType,
												"f5VsAdd.healthCheckType" : data.f5VsAdd.healthCheckType,
												"f5VsAdd.profileIndex" : data.f5VsAdd.profileIndex
											},
											success : function(data) 
											{
												if (!data.isSuccessful)
												{
													$.obAlertNotice(data.message);
													virtualServer.loadListContent();
													return;
												}
												else
												{
													virtualServer.loadListContent();
													return false;
												}
											},
											error : function(jqXhr)
											{
												$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
//												exceptionEvent();
												virtualServer.loadListContent();
											}
										});
										return false;
									}
									virtualServer.loadListContent();
								}
								virtualServer.loadListContent();
								return false;
							},
							errorFn : function(jqXhr)
							{
								$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
//								exceptionEvent();
							}	
						});
//						virtualServer.loadListContent();
					},
					error : function(jqXhr)
					{
						// #3984-6 #9: 14.07.31 sw.jung 장비 설정 변경중 동기화 오류시 오류 메세지 개선
//						if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
						/*
						if (jqXhr.responseText.indexOf('Sync failed') > -1)
						{
//							$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
							var chk = confirm(VAR_NODE_SYNC_PROGRESSING);
							if(chk) 
							{
								ajaxManager.runJsonExt({
									url : "virtualServer/loadRefreshListContent.action",
									data :
									{
										"adc.index" : adcSetting.getAdc().index,
										"adc.name" : adcSetting.getAdc().name,
										"adc.type" : adcSetting.getAdc().type,	
										"refreshes" : true,
										"orderDir" : this.orderDir,
										"orderType" : this.orderType
									},
									successFn : function(data)
									{	
										if (!data.isSuccessful)
										{
											if(data.extraKey == 1)
											{
												return;
											}
											else
											{
												$.obAlertNotice(data.message);						
												return;							
											}	
										}	
										else
										{
											alert(VAR_NODE_SYNC_COMPLETE_RESET);	
											
											loadVsModifyContent(vsIndex);
										}
									},
									errorFn : function(jqXhr)
									{
										pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
										$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
									}	
								});
							}
						}
						else
						{
							$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
						}
//						exceptionEvent();
					 */
						
						if (jqXhr.responseText.indexOf("Sync finished") > -1)
						{
							alert(VAR_NODE_SYNC_STATUS_RESET + " - " + VAR_NODE_SYNC_COMPLETE_RESET);
							
							loadVsModifyContent(vsIndex);		
						}
						else if (jqXhr.responseText.indexOf("Sync failed") > -1 || jqXhr.responseText.indexOf("Illegal null") > -1)
						{
							$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
							loadVsModifyContent(vsIndex);			
						}
						else
						{
							$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
							loadVsModifyContent(vsIndex);			
						}
					}
				}); 
			    
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
			
			$('#selectedMember').fixheadertable({			
				//caption     : 'My employees', 
			    colratio    : [50, 150, 40, 40, 120], 
			    height      : 200, 
			    width       : 420, 
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
						
			
			$('.profileLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(1);
				profile.loadProfileListContent();
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
			
			$('input[name="f5VsAdd.name"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if (!$(this).val())
					return;
				
				_showVirtualSvrNameAvailable($(this));
			});
			
			$('input[name="f5VsAdd.ip"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if(FlowitUtil.checkIp($(this).val()))
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>' + VAR_COMMON_AVAIL);
				else
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
			});
	
			$('input[name="f5VsAdd.port"]').blur(function(e) 
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
						$('input[name="f5VsAdd.port"]').val('');
						$('input[name="f5VsAdd.port"]').focus();
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
				}
				else 
				{ 
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
//					$.obAlertNotice('입력하신 가상서버 포트는 올바른 방식이 아닙니다.');
					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
					$('input[name="f5VsAdd.port"]').val('');
					$('input[name="f5VsAdd.port"]').focus();
					$(this).next().html('<input type=hidden class="mar_rgt5"/>');
				}
			});
			
			$('.popUpVServerNameWndLnk').click(function(e) 
			{
				vServerNameWnd.popUp();
			});
			
			$('.memStatus').change(function(e)
			{
//				$('input[name="memberChange"]').val(1);
				$(this).parent().find('.memberChange').val(1);
			});
			
			
			$('.protocolCbx').change(function(e) 
			{
				$('input[name="f5VsAdd.port"]').val($(this).val());
			});
			
			if($('.poolCbx option:selected').val() != "") //SLB 설정 virtual service 추가시 Group 신규일때만 수정가능하고 기존 등록된 Group은 읽기만. junhyun.ok_GS
			{
				$('input[name="f5VsAdd.poolName"]').attr("readOnly", true);
			}
			else
			{
				$('input[name="f5VsAdd.poolName"]').attr("readOnly", false);
			}
			
			$('.poolCbx').change(function(e) 
			{
				FlowitUtil.log('selIndex: ' + $(this).prop('selectedIndex'));
				var $poolName = $(this).prop('selectedIndex') == 0 ? newPoolName : $(this).children('option').filter(':selected').text();
				$('input[name="f5VsAdd.poolName"]').val($poolName);
				if($('.poolCbx option:selected').val() != "") //SLB 설정 virtual service 추가시 Group 신규일때만 수정가능하고 기존 등록된 Group은 읽기만. junhyun.ok_GS
				{
					$('input[name="f5VsAdd.poolName"]').attr("readOnly", true);
				}
				else
				{
					$('input[name="f5VsAdd.poolName"]').attr("readOnly", false);
				}
				_fillVirtualSvc($(this).val());
				_fillAdcNodes($(this).val());
			});
			
			$('.memberAddLnk').click(function(e) 
			{
				e.preventDefault();
				moveMemberInputToMemberList();
			});
			
			$('.onMemberAddBatch').click(function(e)
			{
				e.preventDefault();
				memberAddBatchWnd.popUp();
			});
						
			$('.memberTbd').on('dblclick', '.regMemberTr', function(e)
			{				
				with (this) 
				{
					if (header.getAccountRole() === "readOnly" || header.getAccountRole() === "rsAdmin")
					{
						return;
					}
					else
					{						
						var ip = $(this).children().eq(1).text();
						$(this).remove();
						if (_isNodeValidate(ip))
						{
							return false;
						}						
						$('.adcNodeTd').eq(0).parent().parent().append('<tr><td class="adcNodeTd ui-widget-content">' + ip + '</td></tr>');
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
				$('#f5VsAddFrm').submit();
			});
			
			$(".inputSelect").change(function(){
				var $profileIndex = $('select[name="f5VsAdd.profileIndex"]').val();
				if ($profileIndex == '')
					 $('.persistenceDetailLnk').attr("style", "visibility: hidden");
				else
					$('.persistenceDetailLnk').attr("style", "visibility: visible");
			});
			
			$('.persistenceDetailLnk').click(function(e) 
			{
				e.preventDefault();
				var $profileIndex = $('select[name="f5VsAdd.profileIndex"]').val();
				if ($profileIndex == '')
					return;
				
				persistenceDetailWnd.setIndex($profileIndex);
				persistenceDetailWnd.popUp();
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
				$memberTRs.find('td option[value="1"]').attr('selected', true);
				$memberTRs.find('.memberChange').val(1);
			
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
				
				$memberTRs.find('td option[value="0"]').attr('selected', true);	
				$memberTRs.find('.memberChange').val(1);
			});
			
			// Forced Offline 추가
			$('.forcedOffMembersLnk').click(function(e)
			{
				e.preventDefault();
				var $memberTRs = _getCheckedMemberTRs();
				
				if($memberTRs.length == 0)
				{
					$.obAlertNotice(VAR_COMMON_FORCEDOFFLINEMSELECT);
				}
				
				$memberTRs.find('td option[value="2"]').attr('selected', true);
				$memberTRs.find('.memberChange').val(1);
			});
			
			$('.delMembersLnk').click(function(e) 
			{		//Virtual Server Member 삭제(F5)
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
			
			// vlan and tunnels			
			$('.toValnTunnelSelectionLnk').click(function(e) 
			{
				e.preventDefault();
				moveValnTunnelToSelection();
			});
			
			$('.toValnTunnelDeselectionLnk').click(function(e) 
			{
				e.preventDefault();
				moveValnTunnelToDeselection();
			});
			
			$('.selectedVlanTunnelCount').text($('#vlanTunnelSelectedSel').children().length);
			
			$('.valnTunnel').change(function(){
				if($('.valnTunnel option:selected').val() != "0") 
				{
					$('.allVlan').removeClass('none');
				}
				else
				{
					$('.allVlan').addClass('none');
				}
			});
			
			
			if($('.valnTunnel option:selected').val() != "0") 
			{
				$('.allVlan').removeClass('none');
			}
			else
			{
				$('.allVlan').addClass('none');
			}		
						
//			$('input[name="vlanTunnelSearch"]').click(function()
//			{
//				
//			});
			
			// search event
			$('.vlanTunnelSearchLnk').click(function (e) 
			{
				e.preventDefault();
//						$('.Board input.searchTxt').val();
//						var searchKey = $('.Board input.inputText_search').val();
				
				var searchKey = $('input[name="vlanTunnelSearch"]').val();
				FlowitUtil.log('click:' + searchKey);
				_searchOnUnassignedVlanTunnel(searchKey);
			});
			
			_registerVsNameAutoCompleteEvents();
			
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1) 
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				$('input').attr('readonly','readonly');
				$('select').attr('disabled','disabled');
				$('.memberTbd').off('dblclick');
				$('.adcNodeTd').off('click');
				$('.f5vsmodfiyOn').addClass("none");
				$('.contents_area input').off('blur');
			}
			else
			{
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
				$('.f5vsmodfiyOn').removeClass("none");
			}
		}
	},
	moveValnTunnelToSelection : function()
	{
		with (this)
		{
			var $vlanTunnelSelected = $('#vlanTunnelSelectedSel');
			var $vlanTunnelDeselected = $('#vlanTunnelDeselectedSel');
			
			var $option = $vlanTunnelDeselected.children(':selected');
			
			if ($option.size() > 0)
				$vlanTunnelSelected.append($option);
			
			showSelectedVlanTunnelCount();
		}
	},
	moveValnTunnelToDeselection : function()
	{
		with (this)
		{
			var $vlanTunnelSelected = $('#vlanTunnelSelectedSel');
			var $vlanTunnelDeselected = $('#vlanTunnelDeselectedSel');
			
			var $option = $vlanTunnelSelected.children(':selected');
			
			if ($option.size() > 0)
				$vlanTunnelDeselected.append($option);
			
			showSelectedVlanTunnelCount();
		}
	},
	showSelectedVlanTunnelCount : function()
	{
		$('.selectedVlanTunnelCount').text($('#vlanTunnelSelectedSel').children().length);
	},
	_searchOnUnassignedVlanTunnel : function(searchKey) 
	{
		with (this) 
		{
			var $vlanTunnelDeselectedSel = $('#vlanTunnelDeselectedSel');
			
			if (searchKey != "")
			{
				if (!getValidateStringint(searchKey, 1, 64))
				{
					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
					return false;
				}
			}
			
			if (!searchKey) 
			{
				$vlanTunnelDeselectedSel.append($unassignedAccountOptions);
				$unassignedAccountOptions = $();
				return ;
			}
			
			_fillCbxWithSearchedAndSaveUnsearched(searchKey, $vlanTunnelDeselectedSel);
		}
	},
	_fillCbxWithSearchedAndSaveUnsearched : function(searchKey, $vlanTunnelDeselectedSel) 
	{
		with (this) 
		{
			$unassignedAccountOptions = $unassignedAccountOptions.add($vlanTunnelDeselectedSel.children().detach());
			var keyInLowerCase = searchKey.toLowerCase();
			FlowitUtil.log('keyInLowerCase: ' + keyInLowerCase);
			var $unsearchedOptions = $();
			$unassignedAccountOptions.each(function() 
			{
				var index = $(this).text().toLowerCase().indexOf(keyInLowerCase);
				FlowitUtil.log('index: ' + index);
				if (index == -1)
				{
					$unsearchedOptions = $unsearchedOptions.add($(this));
				}
				else
				{
					$vlanTunnelDeselectedSel.append($(this));
				}
			});
			
			$unassignedAccountOptions = $unsearchedOptions;
		}
	},
	_isNodeValidate : function(ip)
	{
		var regNodeIpTd = $('.adcNodeTbd > tr').children(':first-child');		
		for (var i=0; i < regNodeIpTd.length; i++)
		{
			if (regNodeIpTd.eq(i).text() == ip)
				return true;
		}		
		return false;
		
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
	//TODO
	_validateVsAdd : function(isModify) 
	{
		with (this) 
		{		
			// from GS. #4012-1 #11, #3926-4 #5: 14.07.22 sw.jung obvalidation 활용 개선
//			var virtualSvrIp = $('input[name="f5VsAdd.ip"]').val();
//			if (getValidateIP(virtualSvrIp) == false) 
//			{
//				alert(VAR_COMMON_VIFORMAT);
//				return false;
//			}
			if (!$('input[name="f5VsAdd.ip"]').validate(
				{
					name: $('input[name="f5VsAdd.ip"]').parent().parent().find('li').text(),
					required: true,
					type: "ip"
				}))
			{
				return false;
			}
			// from GS. #4012-1 #11, #3926-4 #5: 14.07.22 sw.jung obvalidation 활용 개선
//			var virtualname = $('input[name="f5VsAdd.name"]').val();
//			if (virtualname == "")
//			{
//				alert(VAR_COMMON_NAME);
//				return false;
//			}
//			else if (getValidateStringint(virtualname, 1, 64) == false)
//			{
//				alert(VAR_COMMON_SPECIALCHAR);
//				return false;
//			}
			if (!$('input[name="f5VsAdd.name"]').validate(
				{
					name: $('input[name="f5VsAdd.name"]').parent().parent().find('li').text(),
					required: true,
					type: "f5_name",
					lengthRange: [1,64]
				}))
			{
				return false;
			}
			
			if (!$('input[name="f5VsAdd.port"]').val()) 
			{
				$.obAlertNotice(VAR_F5_SER_NINPUT);
				return false;
			}
			var vport = $('input[name="f5VsAdd.port"]').val();
			if (getValidateNumberRange(vport, 0, 65535) == false)
			{
				regexp = /[^0-9]/gi;
				if (regexp.test(vport))
				{
					$.obAlertNotice(VAR_COMMON_NUMRANGE);
					return false;
				}
				else
				{
					$.obAlertNotice(VAR_VS_VSPORTRANGE);
					return false;
				}
			}
			var poolName = $('input[name="f5VsAdd.poolName"]').val();
			if (!poolName) 
			{
				$.obAlertNotice(VAR_F5_GRO_NINPUT);
				return false;
			}
			if (getValidateStringint(poolName, 1, 64) == false)
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				return false;
			}
			
			if(isModify)
			{
				return true;
			}
			else
			{
				var virtualSvrName = $('input[name="f5VsAdd.name"]').val();
				if (!_isValidVirtualSvrName(virtualSvrName)) 
				{ //alert(" 1 - virtualSvrName : " + virtualSvrName);
					$.obAlertNotice(VAR_COMMON_VNOTALLOWED);
					return false;	
				}				
			}			
		}
		return true;
	},
	_updatePoolIndexInPoolCbxByPoolNameInPoolText : function() 
	{
		with (this) 
		{
			var poolName = $('input[name="f5VsAdd.poolName"]').val();
			FlowitUtil.log(poolName);
			var pools = $('.poolCbx option').map(function() 
			{
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
	_isValidVirtualSvrName : function(virtualSvrName) //alert(" 2 - virtualSvrName : " + virtualSvrName);
	{ 
		return /^[_a-zA-Z](\w|-|\.|_)*$/.test(virtualSvrName);
	},
	_showVirtualSvrNameAvailable : function($virtualSvrName) 
	{
		FlowitUtil.log("_showVirtualSvrNameAvailable: %s", $virtualSvrName.val());
		with (this) 
		{
			if (!_isValidVirtualSvrName($virtualSvrName.val())) 
			{
				$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
				return;
			}

			taskQ.add(function() 
				{
				ajaxManager.runJsonExt({
					url : "virtualServer/existsVirtualSvrName.action",
					data : 
					{
						"adc.index" : adcSetting.getAdc().index,
						"adc.type" : adcSetting.getAdc().type,
						"virtualSvrF5.name" : $virtualSvrName.val()
					},
					successFn : function(data) 
					{
						FlowitUtil.log('existsVirtualSvr: ' + data.existsVirtualSvr);
						if(data.existsVirtualSvr)
							
							$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
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
			$('input[name="f5VsAdd.poolName"]').val(newPoolName);
		}
	},
	_getRegMembers : function()
	{
		return $('.regMemberTr').map(function() 
		{
			$children = $(this).children();
			return {
				"ip" : $children.eq(1).text(),
				"port" : $children.eq(2).text(),
//				"isEnabled" : $children.eq(3).children().eq(0).val()
				"ratio" : $children.eq(3).text(),
				"memStatus" : $children.eq(4).children().eq(0).val()
			};
		}).get();
	},
	_getEventRegMembers : function()
	{
		return $('.regMemberTr').map(function() 
		{
			if($(this).find('.memberChange').val() == 1)
			{
				$children = $(this).children();
				return {
					"ip" : $children.eq(1).text(),
					"port" : $children.eq(2).text(),
	//				"isEnabled" : $children.eq(3).children().eq(0).val()
					"ratio" : $children.eq(3).text(),
					"memStatus" : $children.eq(4).children().eq(0).val()
				};
			}
		}).get();
	},
	_fillVirtualSvc : function(poolIndex) 
	{
		with (this) 
		{
			FlowitUtil.log('poolIndex: ' + poolIndex);
			if (!poolIndex)
			{
				_addVirtualSvcMembersToTable(null);
				_fillLoadBalancingTypes(null);
				_fillHealthCheckTypes(null);
				return;
			}
			
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveVirtualSvc.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"poolIndex" : poolIndex
				},
				successFn : function(data) 
				{
					FlowitUtil.log('_fillVirtualSvc: ' + Object.toJSON(data.virtualSvc));
					_addVirtualSvcMembersToTable(data.virtualSvc ? data.virtualSvc.members : null);
					_fillLoadBalancingTypes(data.virtualSvc ? data.virtualSvc.loadBalancingType : null);
					_fillHealthCheckTypes(data.virtualSvc ? data.virtualSvc.healthCheckType : null);
				},
				completeFn : function(textStatus) 
				{
					FlowitUtil.log('_fillVirtualSvc: ' + textStatus);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VEFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
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
					html += '<td class="c_3">' + virtualSvcMembers[i].ratio + '</td>';
//					html += '<td class="c_3"><select><option value="true" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? 'selected="selected"' : '') +'>Enabled</option><option value="false" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
//					html += '<td class="c_3"><select><option value="true" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? 'selected="selected"' : '') +'>Enabled</option><option value="false" ' + (String(virtualSvcMembers[i].isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option><option value="forced" ' + (String(virtualSvcMembers[i].isEnabled) === 'forced' ? '' : 'selected="selected"') + '>Forced Offline</option></select></td>';
					
//					html += '<td class="c_3"><select><option value="1" ' + (String(virtualSvcMembers[i].memStatus) == 1 ? 'selected="selected"' : '') +'>Enabled</option><option value="0" ' + (String(virtualSvcMembers[i].memStatus) == 0 ? '' : 'selected="selected"') + '>Disabled</option><option value="2" ' + (String(virtualSvcMembers[i].memStatus) == 2 ? '' : 'selected="selected"') + '>Forced Offline</option></select></td>';
//					html += '</tr>';
					
					html += '<td class="c_3"><select>';
					if(String(virtualSvcMembers[i].memStatus) == 1)
					{
						html +=	'<option value="0">Disabled</option><option value="1" selected="selected">Enabled</option><option value="2">Forced Offline</option>';
					}
					else if(String(virtualSvcMembers[i].memStatus) == 0)
					{
						html +=	'<option value="0" selected="selected">Disabled</option><option value="1">Enabled</option><option value="2">Forced Offline</option>';
					}
					else
					{
						html +=	'<option value="0">Disabled</option><option value="1">Enabled</option><option value="2" selected="selected">Forced Offline</option>';
					}
					html += '</select></td></tr>';					
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
					_addAdcNodesToTbl(data.adcNodes);
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
				html += '<tr>';
				html += '<td class="adcNodeTd">' + adcNodes[i].ip + '</td>';
				html += '<td>' + adcNodes[i].state + '</td>';
				html += '<td class="none">' + adcNodes[i].ratio + '</td>';
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
	_fillLoadBalancingTypes : function(type) 
	{
		var options = '';
		if (type === 'NOT_ALLOWED') 
		{
			options += '<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>';
		} 
		else 
		{
			options += '<option value="Round Robin"' + (type === 'Round Robin' ? 'selected="selected"':'') + '>Round Robin</option>';
			options += '<option value="Least Connections"' + (type === 'Least Connections' ? 'selected="selected"':'') + '>Least Connections</option>';
		}
		
		$('select[name="f5VsAdd.loadBalancingType"]').empty().append(options);
	},
	_fillHealthCheckTypes : function(type) 
	{
		var options = '';
		if (type === 'NOT_ALLOWED') 
		{
			options += '<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>';
		} 
		else 
		{
			options += '<option value="NONE"' + (type === 'NONE' ? 'selected="selected"':'') + '>' + VAR_F5_NAPP + '</option>';
			options += '<option value="TCP"' + (type === 'TCP' ? 'selected="selected"':'') + '>TCP</option>';
			options += '<option value="HTTP"' + (type === 'HTTP' ? 'selected="selected"':'') + '>HTTP</option>';
			options += '<option value="HTTPS"' + (type === 'HTTPS' ? 'selected="selected"':'') + '>HTTPS</option>';
			options += '<option value="UDP"' + (type === 'UDP' ? 'selected="selected"':'') + '>UDP</option>';
			options += '<option value="GATEWAY_ICMP"' + (type === 'GATEWAY_ICMP' ? 'selected="selected"':'') + '>GATEWAY_ICMP</option>';
		}
		
		$('select[name="f5VsAdd.healthCheckType"]').empty().append(options);
	},
	moveMemberInputToMemberList : function() 
	{
		with (this) 
		{
		var $ip = $('.memberIpTxt');
		var $port = $('.memberPortTxt');
		var $ratio = $('.memberRatioTxt');
		var $enabled = $('.memberEnabledChk');
		if (!_validateMemberInput($ip.val(), $port.val()))
			return false;
		
		addToMemberTable($ip.val(), $port.val(), $ratio.val(), $enabled.is(':checked'));
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
			
			if (!getValidateIP(ip))
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
				$.obAlertNotice('IP : ' + ip + ', '+ VAR_COMMON_PORT + ' : ' +  port + ' ' + VAR_COMMON_ARMEMBER);
				return false;
			}
			
			return true;
		}
	},
	_isMember : function(ip, port)
	{
		var regMemberIpTd = $('.regMemberTr').children(':nth-child(2)');	// regMemberIpTd 에 ip값 초기화
		var regMemberPortTd = $('.regMemberTr').children(':nth-child(3)');	// regMemberPortTd dp port값 초기화
		for (var i=0; i < regMemberIpTd.length; i++) {
			if (regMemberIpTd.eq(i).text() == ip && regMemberPortTd.eq(i).text() == port)	// port 와 ip 값 비교
				return true;
		}
		
		return false;
	},
	
	// TODO - Real Server 목록에서 Member 목록으로 이동
	addToMemberTable : function(ip, port, ratio, isEnabled)
	{
		with (this) 
		{
			var html = '<tr class="regMemberTr">';
			html += '<td class="align_center"><input class="memberChk" type="checkbox"/></td>';
			html += '<td class="align_left_P5">' + ip + '</td>';
			html += '<td class="align_center">' + port + '</td>';
			html += '<td class="align_center">' + ratio + '</td>';
//			html += '<td class="align_center"><select><option value="true"' + (isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="false"' + (isEnabled ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
//			html += '<td class="align_center"><select><option value="true"' + (isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="false"' + (isEnabled ? '' : 'selected="selected"') + '>Disabled</option><option value="forced"' + (isEnabled ? '' : 'selected="selected"') + '>Forced Offline</option></select></td>';
			html += '<td class="align_center"><select><option value="1"' + (isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="0"' + (isEnabled ? '' : 'selected="selected"') + '>Disabled</option><option value="2"' + (isEnabled ? '' : 'selected="selected"') + '>Forced Offline</option></select></td>';
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
				if (_validateMemberInput(val.ip, val.port))
				{
					addToMemberTable(val.ip, val.port, "", $enabled.is(':checked'));
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
			$('.adcNodeTd.off').parent().remove();
			_setAdcNodeCount();
		}
	},
	_applyMemberTableCss : function () 
	{
		// 리스트형 테이블 행 롤오버 효과
//		$('.ipList tbody tr:not(.empty)').hover(function()
		$('.fixed-table tbody tr:not(.empty)').hover(
			function()
			{
				$(this).css({
					'background-color' : '#ccc',
					'cursor' : 'pointer'
				});
			}, 
			function() 
			{
				$(this).removeAttr('style');
			}
		);
		$('.fixed-table_1 tbody tr:not(.empty)').hover(
			function()
			{
				$(this).css({
					'background-color' : '#ccc',
					'cursor' : 'pointer'
				});
			}, 
			function() 
			{
				$(this).removeAttr('style');
			}
		);
		
		$('#selectedMember').parents('.t_fixed_header').find('.headtable').css('margin-right', '17px');
		$('#selectedMember').parents('.t_fixed_header').find('.body').css('overflow-y', 'auto');
		$('#selectedMember').parents('.t_fixed_header').find('.body').css('overflow-x', 'hidden');
		$('#memberList').parents('.t_fixed_header').find('.headtable').css('margin-right', '17px');
		$('#memberList').parents('.t_fixed_header').find('.body').css('overflow-y', 'auto');
	}, 
	_registerNodeListTableEvents : function()
	{
		$('.adcNodeTd').click(function(e) 
		{			
			e.preventDefault();
			if (header.getAccountRole() === "readOnly" || header.getAccountRole() === "rsAdmin")
			{
				return;
			}
			else
			{				
				var node = $(this).text();
//				$children = $(this).children();
//				var node_ip = $children.eq(0).text();
//				var node_ratio = $children.eq(2).text();
				var node_ratio;
				var node_ip;
				var array_data = node.split("|");
				node_ip = array_data[0];
				node_ratio = array_data[1];
				$('.memberIpTxt').val(node_ip);
				$('.memberRatioTxt').val(node_ratio);
				$('.adcNodeTd').removeClass('on');
				$(this).addClass('on');
			}
		});
	},
	_getCheckedMemberTRs : function() 
	{
		return $('#selectedMember .memberChk').filter(':checked').parent().parent();
	}
});