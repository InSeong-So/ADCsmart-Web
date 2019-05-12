var AlteonVs =  Class.create({
	initialize : function()
	{
		this.adc = {};
		this.newPoolName;
		this.jSonString;
		this.vServerNameWnd = new VServerNameWnd();
		this.initFlag=false;
		this.vsNameCheck;
		this.vsIndex = undefined;
	},
	setAdc : function(adcIndex, adcType, adcName)
	{
		this.adc.index = adcIndex;
		this.adc.type = adcType;
		this.adc.name = adcName;
		this.vServerNameWnd.setAdc(this.adc);
	},
	loadVsAddContent : function()
	{
		with (this)
		{
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadAlteonVsAddContent.action",
				data :
				{
					"alteonVsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params)
				{
					virtualServer.setActiveContent('VsAddContent');
					applyVsAddContentEvents();
					registerVsAddContentEvents(false);
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
				url : "virtualServer/loadAlteonVsModifyContent.action",
				data :
				{
					"alteonVsAdd.index" : index,
					"alteonVsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params)
				{
					virtualServer.setActiveContent('VsModifyContent');
					vsIndex = index;
					applyVsAddContentEvents();
					registerVsAddContentEvents(true);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	applyVsAddContentEvents : function()
	{
//		initTable([ ".virtualSvcTbd tr" ], [ 3 ], [ -1 ]);
	},
	//TODO
	registerVsAddContentEvents : function(isModify)
	{
		with (this)
		{
			$('#alteonVsAddFrm').submit(function()
			{
				FlowitUtil.log(Object.toJSON(_getVirtualSvcs()));
				if (!_validateVsAdd())
					return false;
				
				if(!isModify && $('#idVsName').val().trim() != '')
				{
					for(var i=0; i<vsNameCheck.length; i++) //이름 중복 체크. junhyun.ok_GS
					{
						if(vsNameCheck[i] == $('#idVsName').val())
						{
							$.obAlertNotice(VAR_ALT_NAMEDUPLICATE);
							return false;
						}
					}
				}
				
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? 'virtualServer/modifyAlteonVs.action' : 'virtualServer/addAlteonVs.action',
					data :
					{
						"alteonVsAdd.virtualSvcsInString" : Object.toJSON(_getVirtualSvcs())
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
								"alteonVsAdd.adcIndex" 		: data.alteonVsAdd.adcIndex,
								"alteonVsAdd.alteonId" 		: data.alteonVsAdd.alteonId,
								"alteonVsAdd.index" 		: data.alteonVsAdd.index,
								"alteonVsAdd.interfaceNo" 	: data.alteonVsAdd.interfaceNo,
								"alteonVsAdd.ip" 			: data.alteonVsAdd.ip,
								"alteonVsAdd.name" 			: data.alteonVsAdd.name,
								"alteonVsAdd.vrrpState" 	: data.alteonVsAdd.vrrpState,
								"alteonVsAdd.routerId" 		: data.alteonVsAdd.routerId,
								"alteonVsAdd.vrId" 			: data.alteonVsAdd.vrId,
								"alteonVsAdd.virtualSvcs" 	: data.alteonVsAdd.virtualSvcs,
								"alteonVsAdd.virtualSvcsInString" : Object.toJSON(_getVirtualSvcs()),
								"adc.index" : adcSetting.getAdc().index,
								"adc.name" : adcSetting.getAdc().name,
								"adc.type" : adcSetting.getAdc().type,
							},
							successFn : function(data)
							{
								data.pairIndex = 0;
								if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 		// Alteon 동기화 부분 임시 false 처리 service에서 pairIndex 값 0 으로 return
								{
									var chk = confirm(data.message);
//									var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
									if (chk)
									{
										ajaxManager.runJsonExt({
											url : isModify ? 'virtualServer/modifyAlteonVsPeer.action' : 'virtualServer/addAlteonVsPeer.action',
											data : 
											{ 
												"pairIndex" : data.pairIndex,
												"alteonVsAdd.adcIndex" 		: data.alteonVsAdd.adcIndex,
												"alteonVsAdd.alteonId" 		: data.alteonVsAdd.alteonId,
												"alteonVsAdd.index" 		: data.alteonVsAdd.index,
												"alteonVsAdd.interfaceNo" 	: data.alteonVsAdd.interfaceNo,
												"alteonVsAdd.ip" 			: data.alteonVsAdd.ip,
												"alteonVsAdd.name" 			: data.alteonVsAdd.name,
												"alteonVsAdd.vrrpState" 	: data.alteonVsAdd.vrrpState,
												"alteonVsAdd.routerId" 		: data.alteonVsAdd.routerId,
												"alteonVsAdd.vrId" 			: data.alteonVsAdd.vrId,
												"alteonVsAdd.virtualSvcs" 	: data.alteonVsAdd.virtualSvcs,
												"alteonVsAdd.virtualSvcsInString" : Object.toJSON(_getVirtualSvcs())
											},
											successFn : function(data) 
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
											errorFn : function(jqXhr)
											{
												$.obAlertAjaxError(VAR_COMMON_PVSAMFAIL, jqXhr);
//												exceptionEvent();
												virtualServer.loadListContent();
											}
										});
										return false;
									}
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
					},
					error : function(jqXhr)
					{
						// #3984-6 #9: 14.07.31 sw.jung 장비 설정 변경중 동기화 오류시 오류 메세지 개선
//						if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
						if (jqXhr.responseText.indexOf('Sync failed') > -1)
						{
//							$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
//							$.obAlertNotice(VAR_VS_VSTIMEERROR);
//							var chk = confirm(VAR_VS_VSTIMEERROR + "\n" + VAR_NODE_SYNC_PROGRESSING);
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
//											loadNodeListContent(searchedKey, this.orderType, this.orderDir);
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
						else if (jqXhr.responseText.indexOf('invalid interface num') > -1)
						{
							$.obAlertAjaxError(VAR_VS_INTERFACEAREA, jqXhr);
						}
						else
						{
							$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
						}	
//						else if (jqXhr.responseText.indexOf('invalid interface num') > -1)
//							$.obAlertAjaxError(VAR_VS_INTERFACEAREA, jqXhr);
//						else
//							$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
//						exceptionEvent();
					}
				}); 
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
		
			$('input[name="alteonVsAdd.name"]').blur(function(e)
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if (!$(this).val())
					return;
				
				_showVirtualSvrNameAvailable($(this));
			});
			
			$('input[name="alteonVsAdd.ip"]').blur(function(e)
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
//				if(FlowitUtil.checkIp($(this).val()))
//				{
//					_checkVSIPAddress($(this).val());
////					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>+VAR_COMMON_AVAIL);
				_setAlteonId($(this).val());
//				} 
//				else
//				{
//					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' +VAR_COMMON_NAVAIL);
//				}
			});
		
			$('.popUpVServerNameWndLnk').click(function(e)
			{
				vServerNameWnd.popUp();
			});
			
			$('.addVirtualSvcLnk').click(function(e)
			{
				FlowitUtil.log('addVirtualSvcLnk! -- ');
				e.preventDefault();
				var version = $('input[name="version"]').val().split(".");
				var popupSize = '';
				if(version[0] == '29')
				{
					popupSize = '1000px';
				}
				else
				{
					popupSize = '800px';
				}
				_popUpVirtualSvc(popupSize);
				
			});
			
			$('.virtualSvcTbd').on('click', '.virtualSvcPortLnk', function(e)
			{
				e.preventDefault();
				var $tr = $(this).parent().parent();
				var version = $('input[name="version"]').val().split(".");
				var popupSize = '';
				if(version[0] == '29')
				{
					popupSize = '1000px';
				}
				else
				{
					popupSize = '800px';
				}
				_popUpVirtualSvc(popupSize, $tr.parent().children().index($tr), $.parseJSON($tr.find('.virtualSvcsJson').text()));
			});
			
			$('.allVirtualSvcsChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.virtualSvcsChk').attr('checked', isChecked);
			});
			
			$('.delVirtualSvcs').click(function()
			{		//Virtual Service 삭제 (Alteon)
				with (this)
				{
					var chkDel = $(this).parent().parent().parent().parent().find('.virtualSvcsChk').filter(':checked').map(function()
					{
						return $(this).val(); 					//alert("chkDel : " + chkDel);
					}).get();
					
					if (chkDel.length == 0)
					{
						$.obAlertNotice(VAR_ALT_VSDSEL);
						return;
					}
					
					var $checkedTrs = $('.virtualSvcTbd > tr').filter(function(index)
					{
						return $(this).find('.virtualSvcsChk').is(':checked');
					});
					
					var chk = confirm(VAR_ALT_VSDEL);
					if (chk)
					{
						$checkedTrs.remove();
						$('.allVirtualSvcsChk').attr('checked', false);
						_setVirtualSvcCount();
					}
					else
					{
						return false;
					}
	//				$checkedTrs.remove();
	//				$('.allVirtualSvcsChk').attr('checked', false);
	//				_setVirtualSvcCount();
				}
			});
			
			$('.interfacesCbx').change(function(e)
			{
				$('input[name="alteonVsAdd.interfaceNo"]').val($(this).val());
			});
			
			$('.virtualServerAddCancelLnk').click(function(e)
			{
				e.preventDefault();
				virtualServer.loadListContent();
			});
			
			$('.virtualServerAddOkLnk').click(function(e)
			{
				e.preventDefault();
				$('#alteonVsAddFrm').submit();
			});
			
			_registerVsNameAutoCompleteEvents();
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1 || $('.cloneDiv input[name="rPort"]').val() == 0) 
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				$('input[name="alteonVsAdd.ip"]').attr("disabled", "disabled");
				$('input[name="alteonVsAdd.name"]').attr("disabled", "disabled");
				$('.allVirtualSvcsChk').attr("disabled", "disabled");
				$('.virtualSvcsChk').attr("disabled", "disabled");
				$('.delVirtualSvcs ').addClass("none");
			}
			else
			{
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
				$('input[name="alteonVsAdd.ip"]').removeAttr("disabled");
				$('input[name="alteonVsAdd.name"]').removeAttr("disabled");
				$('.allVirtualSvcsChk').removeAttr("disabled");
				$('.virtualSvcsChk').removeAttr("disabled");
				$('.delVirtualSvcs ').removeClass("none");
			}
//			var selectedVersion = adcSetting.getAdcVersion();			
//			if(selectedVersion.startsWith("29.5") == true)
//			{
//				$('.imgOff').removeClass("none");
//				$('.imgOn').addClass("none");
//			}
//			else
//			{
//				$('.imgOff').addClass("none");
//				$('.imgOn').removeClass("none");
//			}
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
					vsNameCheck = data.vsNameList;
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
	
	_checkVSIPAddress : function(ipAddress)
	{
		with(this)
		{
			// 유효한 alteon id 추출한다.
			ajaxManager.runJsonExt({
				url : "virtualServer/checkVSIPAddress.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"ipAddress" : ipAddress
				},
				successFn : function(data)
				{
					if(data.isExistVSIPAddress)
					{
						var $ipAddressBox = $('input[name="alteonVsAdd.ip"]');
						$ipAddressBox.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>'+VAR_COMMON_NAVAIL);
					}
					else
					{
						var $ipAddressBox = $('input[name="alteonVsAdd.ip"]');
						$ipAddressBox.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>'+VAR_COMMON_AVAIL);
					}
					return;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VEIFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	//TODO
	_validateVsAdd : function()
	{
		with(this)
		{
			// ip 필드 검사. 
			
			var virtualSvrIp = $('input[name="alteonVsAdd.ip"]').val();
			if (virtualSvrIp == "")
			{
				$.obAlertNotice(VAR_COMMON_IPINPUT);
				return false;
			}
			else if (getValidateIP(virtualSvrIp) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}			
			
			// from GS. #4012-1 #11, #3926-4 #18: 14.07.22 sw.jung obvalidation 활용 개선
//			var virtualname = $('input[name="alteonVsAdd.name"]').val();
//			if (virtualname != "")
//			{
//				if (getValidateStringint(virtualname, 1, 64) == false)
//				{
//					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
//					return false;
//				}
//			}
			if (!$('input[name="alteonVsAdd.name"]').validate(
				{
					name: $('input[name="alteonVsAdd.name"]').parent().parent().find('li').text(),
					type: "en_name",
					lengthRange: [1,64]
				}))
			{
				return false;
			}

			// index 필드 검사.
			var alteonId = $('input[name="alteonVsAdd.alteonId"]').val();
//			if (getValidateNumberRange(alteonId, 1, 1024)==false)
//			{
//				$.obAlertNotice(VAR_ALT_IIINCOR);
//				return false;
//			}
			var version = $('input[name="version"]').val().split(".");
			if(version[0] == '29')
			{
				if(alteonId.match(/^[0-9]+/))
		        {
		           if (getValidateNumberRange(alteonId, 1, 1024)==false)
		           {
		              $.obAlertNotice(VAR_ALT_IIINCOR);
		              return false;
		           }
		        }
		        else
		        {
		           if(getValidateStringint(alteonId, 1, 64) == false)
		           {
		              $.obAlertNotice(VAR_COMMON_SPECIALCHAR);
		              return false;
		           }
		        }
			}
			else
			{
				if (getValidateNumberRange(alteonId, 1, 1024)==false)
		        {
		            $.obAlertNotice(VAR_ALT_IIINCOR);
		            return false;
		        }
			}		
			
			return true;
		}
	},
	_getVirtualSvcs : function() 
	{
		return $('.virtualSvcsJson').map(function()
		{
			return $.parseJSON($(this).text());
		}).get();
	},
	_getSvcGroupName : function() 
	{
		return $('.svcGroupName').map(function() 
		{
			return $(this).text();
		}).get();
	},
	_getVirtualSvcs_Port : function() 
	{
		return $('.virtualSvcPortLnk').map(function() 
		{
			return $.parseJSON($(this).text());
		}).get();		
	},
	_getSvcGroupIndex : function() 
	{
		return $('.svcGroupIndex').map(function() 
		{
			if($(this).text().match(/^[0-9]+/))
				return $.parseJSON($(this).text());
			else
				return $(this).text();
		}).get();		
	},	
//	_isValidVirtualSvrName : function(virtualSvrName) //alert(" 2 - virtualSvrName : " + virtualSvrName);
//	{ 
//		return /^[a-zA-Z0-9](\w|-|\.|\*|\/|\:|\?|\=|\,|\&)*$/.test(virtualSvrName);
//	},
	_showVirtualSvrNameAvailable : function($virtualSvrName)
	{//알테온은 이름 체크하지 않음. 
		with(this)
		{
			FlowitUtil.log("_showVirtualSvrNameAvailable: %s", $virtualSvrName.val());
//		if (!_isValidVirtualSvrName($virtualSvrName.val()))
//			$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);
//		else 
//			$virtualSvrName.next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>+VAR_COMMON_AVAIL);
		}
	},
	_setAlteonId : function(ip)
	{
		with(this)
		{
			FlowitUtil.log('ip: ' + ip);
			var $id = $('input[name="alteonVsAdd.alteonId"]');
			if ($id.val() === 0 || $id.val())
				return;
			
			var lastIpPart = ip.split('.')[3];
			FlowitUtil.log('lastIpPart: ' + lastIpPart);
			$id.val(lastIpPart);
//			var defaultVSIndex = lastIpPart;
			// 유효한 alteon id 추출한다.
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveValidIndexAlteon.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"alteonVSIndex" : lastIpPart
				},
				successFn : function(data)
				{
					$id.val(data.alteonVSIndex);
//					defaultVSIndex = data.alteonVSIndex;
//					defaultPoolIndex = data.alteonPoolIndex;
					return;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	_setRouterId : function(ip)
	{
		FlowitUtil.log('ip: ' + ip);
		var $id = $('input[name="alteonVsAdd.routerId"]');
		if ($id.val() === 0 || $id.val())
			return;
		
		var lastIpPart = ip.split('.')[3];
		FlowitUtil.log('lastIpPart: ' + lastIpPart);
		$id.val(lastIpPart);
	},
	_setVrId : function(ip)
	{
		FlowitUtil.log('ip: ' + ip);
		var $id = $('input[name="alteonVsAdd.vrId"]');
		if ($id.val() === 0 || $id.val())
			return;
		
		var lastIpPart = ip.split('.')[3];
		FlowitUtil.log('lastIpPart: ' + lastIpPart);
		$id.val(lastIpPart);
	},
	_popUpVirtualSvc : function(popupSize, rowNoToModify, dataToModify)
	{
//		var $tmpVsIndex = $('input[name="alteonVsAdd.alteonId"]');
//		defaultVSIndex = $tmpVsIndex.val();
		
		$('.regOn').removeClass('none');
		$('.regOff').addClass('none');
		$('.memberAddLnk').removeClass('none');
		
		FlowitUtil.log("rowNoToModify: " + rowNoToModify + ", dataToModify: " + Object.toJSON(dataToModify));
		showPopup({
			'id' : '#modify_virtualService',
			'width' : popupSize,
			'height' : '470px'  
		});

		// 리스트형 테이블 초기화, 리스트형 테이블 높이 지정
//		$('.cloneDiv .ipList').fixheadertable({
//			height : 200
//		});
		//tableHeadSize('.cloneDiv #selectedMember', 2);
		
//		$('.Member').fixedHeaderTable({
//            fixedColumns : 2					
//        });
		
		
		$('.cloneDiv #selectedMember29').fixheadertable({			
			//caption     : 'My employees', 
		    colratio    : [100, 60, 150, 60, 50, 50, 50, 120, 100, 50], 
		    height      : 200, 
		    width       : 530, 
		    zebra       : true,
		    resizeCol   : true,
		    minColWidth : 50 
		});
		
		$('.cloneDiv #memberList29').fixheadertable({
//		    colratio    : [60, 130, 80],
		    colratio    : [60, 210], 
		    height      : 200, 
		    width       : 270, 
		    zebra       : true,
		    resizeCol   : true,
		    minColWidth : 50 			
      });
		
//		$('.cloneDiv #selectedMember').fixedHeaderTable({
//            fixedColumns : 0
//        });
		
//		$('.cloneDiv #memberList').fixedHeaderTable({
//            fixedColumns : 0					
//        });
		
		$('.popUpPoolNameWndLnk').click(function(e)
		{
			var $poolIdTxt = $('.cloneDiv input[name="poolId"]');
			var vServerNameWnd = new VServerNameWnd();
			vServerNameWnd.setAdc(this.adc);
			vServerNameWnd.setPoolIndex($poolIdTxt.val());
			
			vServerNameWnd.popUp4Pool();
		});
		// 리스트형 테이블 행의 첫번째 열 배경색 지정
		//$('.ipList > tbody > tr').each(function() {
			//$(this).children(":eq(1)").css({
			//	'background-color' : '#e7e8ed',
			//	'border-right-weight' : '0'
			//});
		//});
		
		// initialize value: -1 for adding a row, rowNo for modifying the row.
		
		
/*		// vs포트, real포트 사용가능 사용불가능 표시부분. (input 2가지 모두다 표시
 * $('.cloneDiv input[name="svcPort"]').blur(function(e) {
			if(FlowitUtil.checkNum($(this).val()))
				$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);
			else
				$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>+VAR_COMMON_AVAIL);
		});
		
	$('.cloneDiv input[name="rPort"]').blur(function(e) {		
			if(FlowitUtil.checkNum($(this).val()))		
				$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);
			else
				$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>+VAR_COMMON_AVAIL);
		});*/
	
/*	
		// vs포트, real포트 사용가능 사용불가능 표시부분. (input 2가지를 or 로 묶어서 하나로 표시
	var vPort = $('.cloneDiv input[name="svcPort"]');
	var vrPort = $('.cloneDiv input[name="rPort"]');

			$('.cloneDiv input[name="svcPort"]').blur(function(e) {
			
						if(!FlowitUtil.checkNum($(vPort).val()) && !FlowitUtil.checkNum($(vrPort).val()))
						
						$(vrPort).next().html('<input type=hidden class="mar_rgt5"/>');
						
						else
						$(vrPort).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);				
						});					
			
						
						
			$('.cloneDiv input[name="rPort"]').blur(function(e) {
			      if(!FlowitUtil.checkNum($(vPort).val()) && !FlowitUtil.checkNum($(vrPort).val()))
			      
			      $(vrPort).next().html('<input type=hidden class="mar_rgt5"/>');
			      
			      else
			      $(vrPort).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);
			      });		
*/	

		var vPort = $('.cloneDiv input[name="svcPort"]');
		var vrPort = $('.cloneDiv input[name="rPort"]');
//		var poolId = $('.cloneDiv input[name="poolId"]');
		
		vPort.blur(function(e)
		{
			if(checkNum($(vPort).val()))
			{
 				if ($(vPort).val() < 65535) 
 				{	
					$('input[name="rPort"]').next().html('<input type=hidden class="mar_rgt5"/>');
				}
				else 
				{	
					$(vrPort).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>'+VAR_COMMON_NAVAIL);
					$.obAlertNotice(VAR_COMMON_VSPRDISAGREE);	
					$('.cloneDiv input[name="svcPort"]').val('');
					//$('.cloneDiv input[name="svcPort"]').focus();
					//$(vrPort).next().html('<input type=hidden class="mar_rgt5"/>');
				}
			}
			else
			{
				$(vrPort).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>'+VAR_COMMON_NAVAIL);
				$.obAlertNotice(VAR_COMMON_VSPRDISAGREE);
				$('.cloneDiv input[name="svcPort"]').val('');
				//$('.cloneDiv input[name="svcPort"]').focus();
				//$(vrPort).next().html('<input type=hidden class="mar_rgt5"/>');
			}			
		});
		
		vrPort.blur(function(e)
		{
//			if(checkNumOne($(vrPort).val()))
//			{			
//				if ($(vrPort).val() < 65536)
//				{	
//					$('input[name="rPort"]').next().html('<input type=hidden class="mar_rgt5"/>');
//				}
//				else
//				{
//					$(vrPort).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);	
//					alert('입력하신 Real서버 포트는 범위에 맞지 않습니다. 가능한 포트 범위(1~65535)');	
//					$('.cloneDiv input[name="rPort"]').val('');
//					//$('.cloneDiv input[name="rPort"]').focus();
//					//$(vrPort).next().html('<input type=hidden class="mar_rgt5"/>');
//				}
//			}
//			else
//			{	
//				$(vrPort).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>+VAR_COMMON_NAVAIL);
//				alert('서버 포트는 범위에 맞지 않습니다. 가능한 포트 범위(1~65535)');
//				$('.cloneDiv input[name="rPort"]').val('');
//				//$('.cloneDiv input[name="rPort"]').focus();
//				//$(vrPort).next().html('<input type=hidden class="mar_rgt5"/>');
//			}
			
			var portNum = $('.cloneDiv input[name="rPort"]').val();
			if (getValidateNumberRange(portNum, 0, 65535)==false)
			{
				$.obAlertNotice(VAR_ALT_RPRDISAG);	
				$('.cloneDiv input[name="rPort"]').val('');
			}
			else
			{
				$('input[name="rPort"]').next().html('<input type=hidden class="mar_rgt5"/>');
			}
		});
		
		$('.cloneDiv .rowNoToModify').val(rowNoToModify == null ? -1 : rowNoToModify);
		
		var $tmpVsIndex = $('input[name="alteonVsAdd.alteonId"]');
		var defaultVSIndex = $tmpVsIndex.val();
		
		var orgSrvPort = null;
		if(dataToModify!=null)
			orgSrvPort=dataToModify.svcPort;
		
		// pool_index 확인  
		var orgPoolIndex = null;
		if(dataToModify!=null)
			orgPoolIndex = dataToModify.pool.index;
		
		with (this)
		{
			taskQ.add(function()
			{
				_fillAdcPools(taskQ, defaultVSIndex , dataToModify);
			});
/*			if (dataToModify)
			{
				taskQ.add(function()
				{
					_fillDataToModify(dataToModify, taskQ);
				});
			}*/
			taskQ.add(function()
			{
//				_fillAdcNodes(undefined, taskQ);
				_fillAdcNodes(orgPoolIndex, taskQ);		// pool_index : Node load 하는 부분에 값을 넘겨줌.		
			});
			
			taskQ.start();
			
	//		_applyMemberTableCss();		// used in _fillAdcPools & _fillAdcNodes.
			_registerSvcRegEvents(orgSrvPort);
		}
	},
	_fillAdcPools : function(taskQ, alteonVSIndex, dataToModify)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveAdcPools.action",
				data :
				{
					"adc.index" 	: adcSetting.getAdc().index,
					"adc.type" 		: adcSetting.getAdc().type,
					"alteonVSIndex" : alteonVSIndex
				},
				successFn : function(data)
				{
					FlowitUtil.log('_fillAdcPools: ' + Object.toJSON(data.adcPools));
					_addAdcPoolsToCbx(data.adcPools, data.alteonPoolIndex, data.alteonPoolIndexList);
//					_fillLoadBalancingTypes('Round Robin');
					_fillLoadBalancingTypes('Least Connections');
					if(!(dataToModify))
					{
						_fillHealthCheckTypes(data.adcHealths_alteon, "tcp");
					}
					if (dataToModify)
					{
						taskQ.add(function()
						{
							_fillDataToModify(dataToModify, data.adcHealths_alteon, taskQ);
						});
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ALT_VLFAIL, jqXhr);
//					exceptionEvent();
				}	
			},
			taskQ);
		}
	},
	_fillDataToModify : function(data, data_, taskQ)
	{
		with (this)
		{
			
			$('.cloneDiv input[name="svcPort"]').val(data.svcPort);
			$('.cloneDiv input[name="rPort"]').val(data.realPort);
//			$('.cloneDiv input[name="poolId"]').val(data.pool.alteonId);
			
			if (data.pool)
			{
				var $poolOptionToSelect = $('.cloneDiv .poolsCbx option[value="' + data.pool.index + '"]');
				if ($poolOptionToSelect.length == 0) 
				{
					$('.cloneDiv .poolsCbx option').filter(':first').attr('selected', true);
//					_enableFocusOnPoolNameTxt();
				}
				else
				{
					$poolOptionToSelect.attr('selected', true);
//					_disableFocusOnPoolNameTxt();
				}
						
				if($('.cloneDiv .poolsCbx').prop('selectedIndex') == 0)//SLB 설정 기등록된 virtual service 선택 시 Group 신규일때만 수정가능하고 기존 등록된 Group은 읽기만. junhyun.ok_GS
				{
					$('.cloneDiv input[name="poolName"]').attr("readOnly", false);				
				}
				else
				{
					$('.cloneDiv input[name="poolName"]').attr("readOnly", true);	
				}

				$('.cloneDiv input[name="poolName"]').val(data.pool.name);
				$('.cloneDiv input[name="poolId"]').val(data.pool.alteonId).attr("readOnly",true);
				var version = $('input[name="version"]').val().split(".");
				if(version[0] == '29')
				{
					_addToMemberTable_v29(data.pool.members, data.realPort);
				}
				else
				{
					for (var i=0; i < data.pool.members.length; i++)
					{
						var memberPort;
						if (data.realPort != 0)
//						if (data.pool.members[i].port_ === undefined)
						{
							memberPort = data.realPort;
						}
						else
						{			
							memberPort = data.pool.members[i].port_; 							
						}
						_addToMemberTable(data.pool.members[i].ip, memberPort, data.pool.members[i].isEnabled, data.pool.members[i].alteonNodeId);
					}
				}
				

				_fillLoadBalancingTypes(data.pool.loadBalancingType);
				/*var selectHealthCheck = data.pool.adcALTEONHealthCheck.id;*/
				if(data.pool.adcALTEONHealthCheck.id == undefined)
				{
					var healthcheck = data.pool.adcALTEONHealthCheck.split("extra:");
					
					_fillHealthCheckTypes(data_ , healthcheck[0] , healthcheck[1]);
				}
				else
				{
					_fillHealthCheckTypes(data_ , data.pool.adcALTEONHealthCheck.id);
				}
			
/*				ajaxManager.runJsonExt({
					url : "virtualServer/retrieveAdcPools.action",
					data :
					{
						"adc.index" 	: adcSetting.getAdc().index,
						"adc.type" 		: adcSetting.getAdc().type
					},
					successFn : function(data)
					{
						_fillHealthCheckTypes(data.adcHealths_alteon, selectHealthCheck);
					},
					errorFn : function(a,b,c){
						exceptionEvent();
					}	
				},
				taskQ);*/
				
			}
			
			if (taskQ)
			{
				taskQ.notifyTaskDone();
			}				
			// ADC 단절 시 Virtual Service 선택 시 확인 가능하게 수정. junhyun.ok_GS
			// op_mode 모니터링 시 확인 만 가능하도록 추가 적용. lucky77th
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1 || $('.cloneDiv input[name="rPort"]').val() == 0) 			
			{
				$('.cloneDiv input').attr('readonly','readonly');
				$('.cloneDiv select').attr('disabled','disabled');
				$('.regOn').addClass('none');
				$('.regOff').removeClass('none');
				$('.memberAddLnk').addClass('none');
				$('.cloneDiv .form_type1').find('td').off('dblclick');
				$('.cloneDiv .form_type1').find('tr').off('dblclick');
			}
			else
			{
				$('.regOn').removeClass('none');
				$('.regOff').addClass('none');
				$('.memberAddLnk').removeClass('none');
			}
		}
	},
/*	_enableFocusOnPoolNameTxt : function()
	{
		$('.cloneDiv input[name="poolName"]').off('focus');
	},
	_disableFocusOnPoolNameTxt : function()
	{
		$('.cloneDiv input[name="poolName"]').on('focus', function() {
			$(this).blur();
		});
	},*/
	_addAdcPoolsToCbx : function(adcPools, poolIndex, alteonPoolIndexList)
	{
		with (this)
		{
			var html = '';
			for (var i=0; i < adcPools.length; i++)
				html += '<option value="' + adcPools[i].index + '">' + adcPools[i].alteonId + '(' + adcPools[i].name + ')</option>';
			FlowitUtil.log(html);
			
			var $poolsCbx = $('.cloneDiv .poolsCbx');
			$poolsCbx.append(html);
			newPoolName =_createNewPoolName(adcPools);
			var $poolName = $('.cloneDiv input[name="poolName"]');
			$poolName.val(newPoolName);
//			var $poolIdTxt = $('.cloneDiv input[name="poolId"]');
//			$poolIdTxt.val(poolIndex);
//			_registerPoolsCbxEvents($poolsCbx, poolIndex);
			_setPoolIndex( $poolsCbx, poolIndex, alteonPoolIndexList);
		}
	},
	_setPoolIndex : function($poolsCbx, poolIndex, poolIndexList)
	{
		with(this)
		{
			var newPoolIndex = _generateSvcGroupIndex(poolIndex, poolIndexList);
			var $poolIdTxt = $('.cloneDiv input[name="poolId"]');
			$poolIdTxt.val(newPoolIndex);
			_registerPoolsCbxEvents($poolsCbx, newPoolIndex);
		}
	},
	_checkSvcGroupName : function(groupName)
	{
		with (this)
		{
			var groupNameList= jQuery.makeArray(_getSvcGroupName());
			
			for (var i =0; i < groupNameList.length; i++)
			{
				if (groupNameList[i] ==groupName)
				{
					return false;
				}
			}
			return true;
		}
	},
//	_checkSvcGroupIndex : function(groupIndex)
//	{
//		with (this)
//		{
//			var groupIndexList= jQuery.makeArray(_getSvcGroupIndex());
//			
//			alert(groupIndexList);
//			for (var i =0; i < groupIndexList.length; i++)
//			{
//				if (groupIndexList[i] ==groupIndex)
//				{
//					return false;
//				}
//			}
//			return true;
//		}
//	},
	_generateSvcGroupIndex : function(groupIndex, indexList)
	{
		with (this)
		{
			var retVal = groupIndex;
			var vsGroupIndexList= jQuery.makeArray(_getSvcGroupIndex());// 현재 vs에 할당된 group 목록.
		
			// 입력 받은 groupIndex가 현재 수정중인 vs에 포함되어 있는지 조사한다.
			var isIncluded = false;
			for (var i =0; i < vsGroupIndexList.length; i++)
			{
				if (vsGroupIndexList[i] ==groupIndex)
				{
					isIncluded = true;
					break;
				}
			}
			if(isIncluded==false)
			{// indexList에 포함되어 있는지 검사한다.
				for (var ii =0; ii < indexList.length; ii++)
				{
					if (indexList[ii]==retVal)
					{
						isIncluded = true;
						break;
					}
				}
				if(isIncluded==true)
				{
					return _generateSvcGroupIndex(groupIndex+1, indexList);
				}
				return retVal;
			}
			return _generateSvcGroupIndex(groupIndex+1, indexList);
		}
	},
	_createNewPoolName : function(adcPools)
	{
		with(this)
		{
			var vsName = $('input[name="alteonVsAdd.name"]').val();
			var maxNo = 0;
			// 이전 설정 목록과 비교한다.
			for (var i=0; i < adcPools.length; i++)
			{
				FlowitUtil.log(adcPools[i].name);
				var rex = new RegExp(vsName + '_Pool_\\d+$');
				var isSamePattern = rex.test(adcPools[i].name);
				FlowitUtil.log(isSamePattern);
				if (!isSamePattern)
				{
					continue;
				}
				
				var lastNo = parseInt(adcPools[i].name.substring(adcPools[i].name.lastIndexOf('_') + 1));
				if (lastNo > maxNo)
					maxNo = lastNo;
			}
			
			// 신규 등록된 목록과 비교한다.
			var newName = vsName + '_Pool_' + (maxNo+1);
			FlowitUtil.log(newName);
			var svcGroupNameList = jQuery.makeArray(_getSvcGroupName());
			for (var i=maxNo; i < maxNo+1024; i++)// 한장비에 최대 1024개까지만 pool이 등록될 수 있음으로..
			{
				newName = vsName + '_Pool_' + (i+1);
				if(_isRegSvcGroupName(newName, svcGroupNameList)==false)
					break;
			}
			return newName;
		}
	},
	_isRegSvcGroupName : function(name, groupList)
	{
		with(this)
		{
			for (var i=0; i < groupList.length; i++)
			{
				if(groupList[i] == name)
				{
					return true;
				}
			}
			return false;
		}
	},
	_registerPoolsCbxEvents : function($poolsCbx, poolIndex)
	{
		with (this)
		{
			$poolsCbx.change(function()
			{
				FlowitUtil.log('selIndex: ' + $(this).prop('selectedIndex'));
				var $poolNameTxt = $('.cloneDiv input[name="poolName"]');
				var $poolIdTxt = $('.cloneDiv input[name="poolId"]');
				if ($(this).prop('selectedIndex') == 0) //SLB 설정 virtual service 추가시 Group 신규일때만 수정가능하고 기존 등록된 Group은 읽기만. junhyun.ok_GS
				{
					$poolNameTxt.val(newPoolName);
					$poolIdTxt.val(poolIndex);
					$poolNameTxt.attr("readOnly",false);
					$poolIdTxt.attr("readOnly",false);
//					_enableFocusOnPoolNameTxt();
				}
				else
				{
					$poolNameTxt.val(_extractPoolNameFromPoolNameWithAlteonId($(this).children('option').filter(':selected').text()));
					$poolIdTxt.val(_extractPoolIdFromPoolIdWithAlteonId($(this).children('option').filter(':selected').text()));
					$poolNameTxt.attr("readOnly",true);
					$poolIdTxt.attr("readOnly",true);
//					_disableFocusOnPoolNameTxt();		
				}				
				$('.cloneDiv .memberTbd').empty();
				_fillVirtualSvc($(this).val());
				_fillAdcNodes($(this).val());
			});
		}
	},
	_extractPoolNameFromPoolNameWithAlteonId : function(poolNameWithAlteonId)
	{
		if (!poolNameWithAlteonId)
			return "";
		
		var poolNames = poolNameWithAlteonId.match(/[^\(\)]+(?=\))/);
		FlowitUtil.log(poolNames);
		if (!poolNames)
			return "";
		return poolNames[0];
	},
	_extractPoolIdFromPoolIdWithAlteonId : function(poolNameWithAlteonId)
	{
		if (!poolNameWithAlteonId)
			return "";
		
//		var poolId = poolNameWithAlteonId.match(/^[0-9]+/);
		var poolId = poolNameWithAlteonId.match(/^.*(?=\()/);
		FlowitUtil.log(poolId);
		if (!poolId)
			return "";
		
		return poolId;
	},

	_fillVirtualSvc : function(poolIndex)
	{
		with (this)
		{
			FlowitUtil.log('poolIndex: ' + poolIndex);
			if (!poolIndex)
			{
				var $tmpVsIndex = $('input[name="alteonVsAdd.alteonId"]');
				var defaultVSIndex = $tmpVsIndex.val();
				ajaxManager.runJsonExt({
					url : "virtualServer/retrieveAdcPools.action",
					data :
					{
						"adc.index" 	: adcSetting.getAdc().index,
						"adc.type" 		: adcSetting.getAdc().type,
						"alteonVSIndex" : defaultVSIndex
					},
					successFn : function(data)
					{
						_fillHealthCheckTypes(data.adcHealths_alteon);
						_addVirtualSvcMembersToTable(null);
						_fillLoadBalancingTypes(null);
						return;
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_ALT_VLFAIL, jqXhr);
//						exceptionEvent();
					}
				});
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
					var version = $('input[name="version"]').val().split(".");
					if(version[0] == '29')
					{
						_addVirtualSvcMembersToTable_v29(data.virtualSvc ? data.virtualSvc.members : null);
					}
					else
					{
						_addVirtualSvcMembersToTable(data.virtualSvc ? data.virtualSvc.members : null);
					}
					_fillLoadBalancingTypes(data.virtualSvc ? data.virtualSvc.loadBalancingType : null);
					_fillHealthCheckTypes(data.adcHealths_alteon, data.virtualSvc ? data.virtualSvc.adcALTEONHealthCheck.extra : null);			
				},
				errorFn : function(a,b,c)
				{
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
					html += '<td>' + virtualSvcMembers[i].ip + '</td>';
//					html += '<td>' + virtualSvcMembers[i].port_ + '</td>';
					html += '<td>' + $('.cloneDiv input[name="rPort"]').val() + '</td>';					
					html += '<td class="align_center"><select><option value="true"' + (virtualSvcMembers[i].isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="false"' + (virtualSvcMembers[i].isEnabled ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
					html += '<td class ="none">' + virtualSvcMembers[i].alteonNodeId   + '</td>';					
					html += '</tr>';
				}
			}
			
			FlowitUtil.log(html);
			$('.cloneDiv .memberTbd').empty().append(html);
			_applyMemberTableCss();
			_registerMemberTableEvents();
			FlowitUtil.log($('.cloneDiv .memberTbd').html());
			_setPoolMemberCount();
		}
	},
	_addVirtualSvcMembersToTable_v29 : function(virtualSvcMembers)
	{
		with (this)
		{
			var html = '';
			if (virtualSvcMembers)
			{
				for (var i=0; i < virtualSvcMembers.length; i++)
				{
					html += '<tr class="regMemberTr">';
					html += '<td><select><option value="true"' + (virtualSvcMembers[i].isEnabled ? 'selected="selected"' : '') +'>Enabled</option><option value="false"' + (virtualSvcMembers[i].isEnabled ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
					html += '<td>' + virtualSvcMembers[i].alteonNodeId + '</td>';
					html += '<td>' + virtualSvcMembers[i].ip + '</td>';
//					html += '<td>' + virtualSvcMembers[i].port_ + '</td>';
					html += '<td>' + $('.cloneDiv input[name="rPort"]').val() + '</td>';
					html += '<td>' + virtualSvcMembers[i].time   +  '</td>';	
					html += '<td>' + virtualSvcMembers[i].inter  +  '</td>';
					html += '<td>' + virtualSvcMembers[i].retry  +  '</td>';
					html += '<td>' + virtualSvcMembers[i].maxcon +  '</td>';
					html += '<td>' + virtualSvcMembers[i].backup +  '</td>';
					html += '<td>' + virtualSvcMembers[i].weight +  '</td>';								
//					html += '<td class = "none">' + virtualSvcMembers[i].alteonNodeId   +  '</td>';				
					html += '</tr>';
				}
			}
			
			FlowitUtil.log(html);
			$('.cloneDiv .memberTbd').empty().append(html);
			_applyMemberTableCss();
			_registerMemberTableEvents_v29();
			FlowitUtil.log($('.cloneDiv .memberTbd').html());
			_setPoolMemberCount();
		}
	},
	_setPoolMemberCount : function()
	{
		$('.cloneDiv .poolMemberCount').text($('.cloneDiv .memberTbd > tr').length);
	},
	_fillAdcNodes : function(poolIndex, taskQ)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveAdcNodes.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"poolIndex" : poolIndex == null ? $('.cloneDiv .poolsCbx').val() : poolIndex
				},
				successFn : function(data)
				{
					FlowitUtil.log('_fillAdcNodes: ' + Object.toJSON(data.adcNodes));
					var version = $('input[name="version"]').val().split(".");
					if(version[0] == '29')
					{
						_addAdcNodesToTbl_v29(data.adcNodes);
					}
					else
					{
						_addAdcNodesToTbl(data.adcNodes);
	}
					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_REFAIL, jqXhr);
//					exceptionEvent();
				}	
			}, taskQ);
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
				html += '<td class="none">' + adcNodes[i].alteonId + '</td>';
				html += '</tr>';
			}			
			$('.cloneDiv .adcNodeTbd').empty().append(html);
			_applyMemberTableCss();
			_registerNodeListTableEvents();
			_setAdcNodeCount();
		}
	},
	_addAdcNodesToTbl_v29 : function(adcNodes)
	{
		with (this)
		{
			var html = '';
			for (var i=0; i < adcNodes.length; i++)
			{
				html += '<tr class="adcNodeTr">';
				html += '<td class="state none"><select><option value="enable" ' + (adcNodes[i].state === 'enable' ? 'selected="selected"' : '') + '>Enabled</option><option value="disable" ' + (adcNodes[i].state === 'disable' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
				html += '<td class="adcNodeTd">' + adcNodes[i].alteonId +  '</td>';
				html += '<td class="adcNodeIp">' + adcNodes[i].ip +  '</td>';
				if(adcNodes[i].port_ == null)
				{
					var port_null = '';
					html += '<td>' + port_null  +  '</td>';
				}
				else
				{
					html += '<td>' + adcNodes[i].port_  +  '</td>';
				}		
				html += '<td class="none">' + adcNodes[i].time   +  '</td>';
				html += '<td class="none">' + adcNodes[i].inter  +  '</td>';
				html += '<td class="none">' + adcNodes[i].retry  +  '</td>';
				html += '<td class="none">' + adcNodes[i].maxcon +  '</td>';
				html += '<td class="none">' + adcNodes[i].backup +  '</td>';
				html += '<td class="none">' + adcNodes[i].weight +  '</td>';								
//				html += '<td class="none">' + adcNodes[i].alteonId +  '</td>';
				html += '</tr>';
			}			
			$('.cloneDiv .adcNodeTbd').empty().append(html);
			_applyMemberTableCss();
			_registerNodeListTableEvents_v29();
			_setAdcNodeCount();
		}
	},
	_setAdcNodeCount : function()
	{
		$('.cloneDiv .adcNodeCount').text($('.cloneDiv .adcNodeTbd > tr').length);
	},
	_moveMemberInputToMemberList : function()
	{
		with (this)
		{
		var $ip = $('.cloneDiv .memberIpTxt');
		var $enabled = $('.cloneDiv .memberEnabledChk');
//		var nonAlteonId = -1;
		if (!_validateMemberInput($ip.val()))
			return false;
		
//		_addToMemberTable($ip.val(), $enabled.is(':checked'), nonAlteonId);
//		_addToMemberTable($ip.val(), $enabled.is(':checked'), "", "");
		_addToMemberTable($ip.val(), $('.cloneDiv input[name="rPort"]').val(), $enabled.is(':checked'), "");
		_delFromNodeList();
		$ip.val('');
//		$enabled.attr('checked', true);
		}
	},
	
	_moveMemberInputToMemberList_v29 : function()
	{
		with (this)
		{
		var $ip = $('.cloneDiv .memberIpTxt');
		var $enabled = $('.cloneDiv .memberEnabledChk');
		//if (!_validateMemberInput_v29($ip.val()))
		if (!_isMember29Add($ip.val()))
			return false;
		_addToMemberTable_sub($enabled.is(':checked'), "", $ip.val(), "", "", "", "", "", "", "" ,"");
		_delFromNodeList_v29();
		$ip.val('');
//		$enabled.attr('checked', true);
		}
	},
	_validateMemberInput : function(ip, index)
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
			else if (_isMember(ip))
			{				
				$.obAlertNotice(ip + VAR_COMMON_ARMEMBER);
				return false;
			}	
			return true;
		}
	},
	
//	//_validateMemberInput_v29 : function(ip, index)
//	_validateMemberInput_v29 : function(valiateVal)
//	{
//		with (this)
//		{
//			if (valiateVal[1] == '')
//			{
//				alert(VAR_COMMON_IPINPUT);
//				return false;
//			}
//			else if (!getValidateIP(valiateVal[1]))
//			{
//				alert(VAR_COMMON_IPFORMAT);
//				return false;
//			}
//			else if (_isNode29(valiateVal))
//			{
//				alert(ip + VAR_ALT_AIREAL);
//				return false;
//			}
//			else if (!_isMember29(valiateVal))
//			{
//				alert(valiateVal[1] + VAR_COMMON_ARMEMBER);
//				return false;
//			}
//			return true;
//		}
//	},
	
	_validateMemberInput_v29 : function(valiateVal)
	{
		with (this)
		{			
			if ($(valiateVal).find('.adcNodeIp').text() == '')
			{
				$.obAlertNotice(VAR_COMMON_IPINPUT);
				return false;
			}
			else if (!getValidateIP($(valiateVal).find('.adcNodeIp').text()))
			{
				$.obAlertNotice(VAR_COMMON_IPFORMAT);
				return false;
			}
//			else if (_isNode29(valiateVal))
//			{
//				alert(ip + VAR_ALT_AIREAL);
//				return false;
//			}
//			else if (!_isMember29Add($(valiateVal).val()))
//			{
//				alert($(valiateVal).find('.adcNodeIp').text() + VAR_COMMON_ARMEMBER);
//				return false;
//			}
			else if (!_isMember29($(valiateVal).text()))
			{
				$.obAlertNotice($(valiateVal).find('.adcNodeIp').text() + VAR_COMMON_ARMEMBER);
				return false;
			}
			return true;
		}
	},	
	_isMember : function(ip)
	{
		var regMemberIpTd = $('.cloneDiv .regMemberTr').children(':first-child');
		for (var i=0; i < regMemberIpTd.length; i++)
		{
			if (regMemberIpTd.eq(i).text() == ip)
				return true;
		}		
		return false;
	},
	
//	_isMember : function(validateVal)
//	{
//		var regMemberIpTd = $('.cloneDiv .regMemberTr').children(':nth-child(2)');
//		//var regMemberIpTd = $('.cloneDiv .regMemberTr').each(function() {
//		//	return $(this).text();
//		//}).get();
//		
//		for (var i=0; i < regMemberIpTd.length; i++)
//		{
//			if (regMemberIpTd.eq(i).text() == validateVal[i])
//			//if (regMemberIpTd[i + 1] == validateVal[i])
//				return true;
//		}		
//		return false;
//	},
	
	// alteon29 버전 이하도 같은 형식으로 변경시  _isMember 로 통합
	_isMember29: function(validateVal)
	{
		var result = true;
		
		$('.regMemberTr').each(
		function() {
			result = $(this).text().indexOf(validateVal) < 0;
			return result;
		});
		
		return result;
	},	
	
	_isMember29Add : function(ip)
	{
		with(this)
		{
			if(ip == '')
			{
				$.obAlertNotice(VAR_COMMON_IPINPUT);
				return false;
			}
			else if (!getValidateIP(ip))
			{
				$.obAlertNotice(VAR_COMMON_IPFORMAT);
				return false;
			}
			
			var regMemberIpTd = $('.cloneDiv .regMemberTr').children(':nth-child(3)');
			for (var i=0; i < regMemberIpTd.length; i++)
			{
				if (regMemberIpTd.eq(i).text() == ip)
				{
					var chk = confirm("IP가 존재하는데 추가하시겠습니까?");
					if (chk)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}		
			return true;
		}
	},
	
	_isNode : function(ip)
	{
		var regNodeIpTd = $('.cloneDiv .adcNodeTbd > tr').children(':first-child');		
		for (var i=0; i < regNodeIpTd.length; i++)
		{
			if (regNodeIpTd.eq(i).text() == ip)
				return true;
		}		
		return false;
		
	},
	
	_isNode29 : function(validateVal, validIp)
	{
		var regNodeIdxTd = $('.cloneDiv .adcNodeTbd > tr').children(':nth-child(2)');	//index
		var regNodeIpTd = $('.cloneDiv .adcNodeTbd > tr').children(':nth-child(3)');	//ip
		
		for (var i=0; i < regNodeIpTd.length; i++)
		{
			if ((regNodeIdxTd.eq(i).text() == validateVal) && (regNodeIpTd.eq(i).text() == validIp))
				return true;
		}		
		return false;
		
	},
	
	// alteon29 버전 이하도 같은 형식으로 변경시  _idNode 로 통합
	//_isNode29: function(validateVal, validateIpVal)
	_isNode29_: function(validateVal)
	{
		var result = true;
		
		$('.adcNodeTr').each(
		function() {
			result = $(this).text().indexOf(validateVal) < 0;
			return result;
		});
		
		return result;
		
//		var result;
//		var resultIp;
//		
//		$('.adcNodeTr').each(
//		function() {
//			result = $(this).text().indexOf(validateVal) < 0;
//			resultIp = $(this).text().indexOf(validateIpVal) < 0;
//			
//			if((result == true) && (resultIp == true))
//			return true;
//		});
//		
//		return false;
	},
	
	_addToMemberTable : function(ip, port, enabled, alteonNodeId)
	{		
		FlowitUtil.log('ip: ' + ip + ', enabled: ' + enabled);
		with (this)
		{			
			var html = '<tr class="regMemberTr">';
			if (header.getAccountRole() == "readOnly")
			{
				html += '<td>' + ip + '</td>';
				html += '<td>' + port + '</td>';
				html += '<td class="align_center"><select disabled="disabled"><option value="true" ' + (String(enabled) === 'true' ? 'selected="selected"' : '') + '>Enabled</option><option value="false" ' + (String(enabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
				html += '<td class="none">' + alteonNodeId + '</td>';
				html += '</tr>';
			}
			else
			{
				html += '<td>' + ip + '</td>';
//				if(port===undefined)
//					html += '<td></td>';
//				else
					html += '<td>' + port + '</td>';
				html += '<td class="align_center"><select><option value="true" ' + (String(enabled) === 'true' ? 'selected="selected"' : '') + '>Enabled</option><option value="false" ' + (String(enabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
				html += '<td class="none">' + alteonNodeId + '</td>';
				html += '</tr>';
			}
			$('.cloneDiv .memberTbd').append(html);
			_applyMemberTableCss();
			_registerMemberTableEvents();			
			_setPoolMemberCount();
		}
	},
	_addToMemberTable_sub : function(isEnabled, index, ip, port_, time, inter, retry, maxcon, backup, weight, alteonNodeId)
	{
		FlowitUtil.log('ip: ' + ip);
		with (this)
		{
			var html = '<tr class="regMemberTr">';
			html += '<td><select><option value="true" ' + (String(isEnabled) === 'true' ? 'selected="selected"' : '') + '>Enabled</option><option value="false" ' + (String(isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
			html += '<td>' + index + '</td>';
			html += '<td>' + ip + '</td>';
			html += '<td>' + port_ + '</td>';
			html += '<td>' + time + '</td>';	
			html += '<td>' + inter + '</td>';
			html += '<td>' + retry + '</td>';
			html += '<td>' + maxcon + '</td>';
			html += '<td>' + backup + '</td>';
			html += '<td>' + weight + '</td>';
			
//			html += '<td class="none">' + alteonNodeId + '</td>';
			html += '</tr>';				
			$('.cloneDiv .memberTbd').append(html);
			_applyMemberTableCss();
			_registerMemberTableEvents_v29();		
			_setPoolMemberCount();
		}
	},
	_addToMemberTable_v29 : function(member, realPort)
	{
		FlowitUtil.log('member: ' + member);
		with (this)
		{		
			var html = '';
			for (var i=0; i < member.length; i++)
			{
				html += '<tr class="regMemberTr">';
				if (header.getAccountRole() == "readOnly")
				{
					html += '<td><select disabled="disabled"><option value="true" ' + (String(member[i].isEnabled) === 'true' ? 'selected="selected"' : '') + '>Enabled</option><option value="false" ' + (String(member[i].isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
				}
				else
				{
					html += '<td><select><option value="true" ' + (String(member[i].isEnabled) === 'true' ? 'selected="selected"' : '') + '>Enabled</option><option value="false" ' + (String(member[i].isEnabled) === 'true' ? '' : 'selected="selected"') + '>Disabled</option></select></td>';
				}
				html += '<td>' + member[i].alteonNodeId + '</td>';
				html += '<td>' + member[i].ip + '</td>';
								
				if (realPort != 0)
				{
					html += '<td>' + realPort + '</td>';
				}
				else
				{				
					if(member[i].port_ === undefined)
					{
						var port_null = '';
						html += '<td>' + port_null + '</td>';
					}
					else
					{
						html += '<td>' + member[i].port_ + '</td>';
					}
				}
				
//				if(member[i].port_ === undefined)
//				{
//					var port_null = '';
//					html += '<td>' + port_null + '</td>';
//				}
//				else
//				{
//					html += '<td>' + member[i].port_ + '</td>';
//				}
				
				if(member[i].time === undefined)
				{
					var time_null = '';
					html += '<td>' + time_null + '</td>';
				}
				else
				{
					html += '<td>' + member[i].time + '</td>';
				}
				html += '<td>' + member[i].inter + '</td>';
				html += '<td>' + member[i].retry + '</td>';
				html += '<td>' + member[i].maxcon + '</td>';
				html += '<td>' + member[i].backup + '</td>';
				html += '<td>' + member[i].weight + '</td>';
												
//				html += '<td class = "none">' + member[i].alteonNodeId   +  '</td>';
				html += '</tr>';
				FlowitUtil.log(html);		
			}	
			$('.cloneDiv .memberTbd').append(html);
			_applyMemberTableCss();
			_registerMemberTableEvents_v29();
			FlowitUtil.log($('.cloneDiv .memberTbd').html());
			_setPoolMemberCount();
		}
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
			options += '<option value="Hash"' + (type === 'Hash' ? 'selected="selected"':'') + '>Hash</option>';
		}
		
		$('.cloneDiv select[name="loadBalancingType"]').empty().append(options);
	},
	_fillHealthCheckTypes : function(adcHealths_alteon, selectHealthCheck, extra)
	{
		var options = '';
		if(adcHealths_alteon != null)
		{
			if (selectHealthCheck === 'Not Allowed')
			{
				options += '<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>';
			}
			else
			{
				for (var i=0; i < adcHealths_alteon.length; i++) 
				{
					var healCheck = adcHealths_alteon[i].id;					
					options += '<option value="'+adcHealths_alteon[i].id +"extra:"+adcHealths_alteon[i].extra+'"' + (healCheck === ''+selectHealthCheck+'' ? 'selected="selected"':'') + '>'+adcHealths_alteon[i].extra+'</option>';
				}
			}		
		}
			$('.cloneDiv input[name="extra"]').val(extra);
			$('.cloneDiv select[name="healthCheckType"]').empty().append(options);
			
	},
	_delFromNodeList : function()
	{
		with (this)
		{
			$('.cloneDiv .adcNodeTd.on').parent().remove();
			_setAdcNodeCount();
			_applyMemberTableCss();
		}
	},
	_delFromNodeList_v29 : function()
	{
		with (this)
		{
			$('.cloneDiv .adcNodeTr.on').remove();
			_setAdcNodeCount();
			_applyMemberTableCss();
		}
	},
	_applyMemberTableCss : function ()
	{
		// 리스트형 테이블 행 롤오버 효과
		$('.cloneDiv .ipList tbody tr:not(.empty)').hover(function()
		{
			$(this).css({
				'background-color' : '#ccc',
				'cursor' : 'pointer'
			});
		},
		function()
		{
			$(this).removeAttr('style');
		});
		
		tableHeadSizeWithoutEmptyRows($('.cloneDiv #selectedMember'));
		tableHeadSizeWithoutEmptyRows($('.cloneDiv #memberList'));		
	}, 
	_checkSvcReg : function(srvPort)
	{
		with (this)
		{
			var Port= jQuery.makeArray(_getVirtualSvcs_Port());
			
			for (var i =0; i < Port.length; i++)
			{
				if(srvPort!=null)
				{
					if (Port[i] == srvPort)
					{
						return false;
					}
				}
			}
			return true;
		}
	},
	_checkSvcReg : function(srvPort, orgSrvPort)
	{
		with (this)
		{
			var Port= jQuery.makeArray(_getVirtualSvcs_Port());
			
			for (var i =0; i < Port.length; i++)
			{
				if (Port[i] == srvPort)
//				if (Port[i] ==$('input[name="svcPort"]').val())
				{
					if(Port[i]==orgSrvPort)
						continue;
					return false;
				}
			}
			return true;
		}
	},	
	_registerSvcRegEvents : function(orgSrvPort)
	{
		$('.cloneDiv input[name="svcPort"]').val('');
		$('.cloneDiv input[name="rPort"]').val('');
		with (this)
		{
			$('.cloneDiv .protocolCbx').change(function(e)
			{
				$('input[name="svcPort"]').val($(this).val());
				$('input[name="rPort"]').val($(this).val());
				
				if(orgSrvPort!=null)
				{
					if(orgSrvPort!=$(this).val())
					{
						if(_checkSvcReg($(this).val())==false)
						{
							$.obAlertNotice(VAR_ALT_SERDUP);
							return false;
						}
					}
				}
				else
				{
					if(_checkSvcReg($(this).val())==false)
					{
						$.obAlertNotice(VAR_ALT_SERDUP);
						return false;
					}
				}
//	
//				var Port= jQuery.makeArray(_getVirtualSvcs_Port());
//				
//				for (var i =0; i < Port.length; i++)
//				{
//					if (Port[i] ==$('input[name="svcPort"]').val())
//					{
//						alert("중복된 서비스 포트입니다. 새로운 포트를 지정하세요 ");
//						return false;
//					}
//				}
			});
//			$('.cloneDiv .poolsCbx').change(function(e)
//			{
//				alert("aaaaaaapoolsCbx qqqassss");
////				$('input[name="poolName"]').val($(this).val());
////				$('input[name="poolId"]').val($(this).val());
//	
//				var poolIndexList= jQuery.makeArray(_getVirtualSvcs_PoolIndex());
//				var curIndex = $('input[name="poolId"]').val();
//				alert("poolIndexList:"+poolIndexList);
//				alert("curIndex:"+curIndex);
//				for (var i =0; i < poolIndexList.length; i++)
//				{
//					if (poolIndexList[i] == curIndex)
//					{
//						alert("중복 Group Index 입니다. 새로운 Group Index를 지정하세요 ");
//						return false;
//					}
//				}
//			});
			
//		$('.cloneDiv .rPortEnabledChk').click(function(e) {
//			var $rPortEnabledChk = $('.rPortEnabledChk');
//			if ($(this).is(':checked'))
//				$rPortEnabledChk.attr('readonly', 'readonly');
//			else
//				$rPortEnabledChk.removeAttr('readonly');
//		});
		
/*		$('.cloneDiv .protocolCbx').blur(function(e) {
			_setRPortToSvcPortIfEmpty();
		});*/
		
			$('.cloneDiv input[name="svcPort"]').blur(function(e)
			{
				_setRPortToSvcPortIfEmpty();
			});
				
			$('.cloneDiv .memberAddLnk').click(function(e)
			{
				e.preventDefault();
				var version = $('input[name="version"]').val().split(".");
				
				if($('.cloneDiv input[name="rPort"]').val() == 0)
				{
					return;
				}
				
				if(version[0] == '29')
				{
					_moveMemberInputToMemberList_v29();
				}		
				else
				{
					_moveMemberInputToMemberList();
				}
				
			});
			//TODO
			$('.cloneDiv .regMemberOkLnk').click(function(e)
			{
				with (this)
				{
					e.preventDefault();
					var virtualSvcWithRowNoToModify;
					if (!_validateVirtualSvc(orgSrvPort))
						return false;
					var version = $('input[name="version"]').val().split(".");
					if(version[0] == '29')
					{
						virtualSvcWithRowNoToModify = _getRegVirtualSvcWithRowNoToModify_v29();
					}
					else
					{
						virtualSvcWithRowNoToModify = _getRegVirtualSvcWithRowNoToModify();
					}
					var svcPort = $('.cloneDiv input[name="svcPort"]').val();
					if(getValidateNumberRange(svcPort, 10, 65535) == false)
					{
						$.obAlertNotice(VAR_COMMON_NUMRANGE);
						return false;
					}
					var rPort = $('.cloneDiv input[name="rPort"]').val();
					if(rPort != "")
					{
						if(getValidateNumberRange(rPort, 0, 65535) == false)
						{
							$.obAlertNotice(VAR_COMMON_NUMRANGE);
							return false;
						}	
					}
					var poolName =$('.cloneDiv input[name="poolName"]').val();
					if(poolName != "")
					{
						if(getValidateStringint(poolName, 1, 64) == false)
						{
							$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
							return false;
						}
					}
					var poolId = $('.cloneDiv input[name="poolId"]').val();
					if(version[0] == '29')
					{
						if(poolId != "")
						{						
							if(getValidateStringint(poolId, 1, 64) == false)
							{
								$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
								return false;
							}
						}
					}	
					else
					{
						if(poolId != "")
						{	
							if(getValidateNumberRange(poolId, 1, 1024) == false)
							{
								$.obAlertNotice(VAR_COMMON_NUMRANGE);
								return false;
							}
						}
					}
					FlowitUtil.log(Object.toJSON(virtualSvcWithRowNoToModify));
					_applyVirtualSvc(virtualSvcWithRowNoToModify.rowNoToModify, virtualSvcWithRowNoToModify.virtualSvc);
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
				}
			});
			
			$('.cloneDiv .regMemberCancelLnk').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();			
			});
		}
	},
	_setRPortToSvcPortIfEmpty : function()
	{		
		var $svcPort = $('.cloneDiv input[name="svcPort"]');
		var $rPort = $('.cloneDiv input[name="rPort"]');
		if ($svcPort.val() != '' && $rPort.val() == '')
		{
			$rPort.val($svcPort.val());
		}		
	},
	_getRegVirtualSvcWithRowNoToModify : function()
	{
		with (this)
		{
			_setRPortToSvcPortIfEmpty();
			var poolName = $('.cloneDiv input[name="poolName"]').val();
//			var poolIndexAndAlteonId = _findPoolIndexAndAlteonIdByPoolNameFromPoolsCbx(poolName);
			var poolId = $('.cloneDiv input[name="poolId"]').val();
			var poolIndexAndAlteonId = _findPoolIndexAndAlteonIdByPoolNameFromPoolsCbx(poolId);
			
			var virtualSvc =
			{
				"svcPort" : $('.cloneDiv input[name="svcPort"]').val(),
				"realPort" : $('.cloneDiv input[name="rPort"]').val(),
				"pool" :
				{
					"index" : poolIndexAndAlteonId ? poolIndexAndAlteonId.index : undefined,
					"name" : poolName,					
					"loadBalancingType" : $('.cloneDiv select[name="loadBalancingType"]').val(),
					"adcALTEONHealthCheck" : $('.cloneDiv select[name="healthCheckType"]').val(),
					"extra" : $('.cloneDiv input[name="extra"]').val(),
					"alteonId" : $('.cloneDiv input[name="poolId"]').val()						
				}
			};
			
			virtualSvc.pool.members = $('.cloneDiv .regMemberTr').map(function()
			{
				var $children = $(this).children();
				return{
					"ip" : $children.eq(0).text(),
					"port" : $children.eq(1).text(),
					"isEnabled" : $children.eq(2).children().eq(0).val(),
					"alteonNodeId" : $children.eq(3).text()
				};
			}).get();
			
			return{
				"rowNoToModify" : $('.cloneDiv .rowNoToModify').val(),
				"virtualSvc" : virtualSvc
			};
		}
	},
	_getRegVirtualSvcWithRowNoToModify_v29 : function()
	{
		with (this)
		{
			_setRPortToSvcPortIfEmpty();
			var poolName = $('.cloneDiv input[name="poolName"]').val();
//			var poolIndexAndAlteonId = _findPoolIndexAndAlteonIdByPoolNameFromPoolsCbx(poolName);
			
			var poolId = $('.cloneDiv input[name="poolId"]').val();
			var poolIndexAndAlteonId = _findPoolIndexAndAlteonIdByPoolNameFromPoolsCbx(poolId);
			var virtualSvc =
			{
				"svcPort" : $('.cloneDiv input[name="svcPort"]').val(),
				"realPort" : $('.cloneDiv input[name="rPort"]').val(),
				"pool" :
				{
					"index" : poolIndexAndAlteonId ? poolIndexAndAlteonId.index : undefined,
					"name" : poolName,
					"loadBalancingType" : $('.cloneDiv select[name="loadBalancingType"]').val(),
					"adcALTEONHealthCheck" : $('.cloneDiv select[name="healthCheckType"]').val(),
					"extra" : $('.cloneDiv input[name="extra"]').val(),
					"alteonId" : $('.cloneDiv input[name="poolId"]').val()						
				}
			};
			
			virtualSvc.pool.members = $('.cloneDiv .regMemberTr').map(function()
			{
				var $children = $(this).children();
				return{
					"isEnabled": $children.eq(0).children().eq(0).val(),
					"alteonNodeId" : $children.eq(1).text(),
					"ip"       : $children.eq(2).text(),
					"port_"  	: $children.eq(3).text(),
					"time"   : $children.eq(4).text(),
					"inter"     : $children.eq(5).text(),
					"retry"    : $children.eq(6).text(),
					"maxcon"   : $children.eq(7).text(),
					"backup"    : $children.eq(8).text(),
					"weight"   : $children.eq(9).text()
				};
			}).get();
			
			return{
				"rowNoToModify" : $('.cloneDiv .rowNoToModify').val(),
				"virtualSvc" : virtualSvc
			};
		}
	},
	_findPoolIndexAndAlteonIdByPoolNameFromPoolsCbx : function(poolName)
	{
		with (this)
		{
			// find pool index and alteon id by pool name From PoolsCbx that is equal to the name input on the text box. 
			var pools = $('.cloneDiv .poolsCbx option').map(function() {
				return {
					"index" : $(this).val(),
					"alteonIdWithPoolName" : $(this).text()
				};
			}).get();
			FlowitUtil.log(pools);
			for (var i=0; i < pools.length; i++) {
				if (_extractPoolNameFromPoolNameWithAlteonId(pools[i].alteonIdWithPoolName) == poolName) {
					var alteonId = pools[i].alteonIdWithPoolName.substring(0, pools[i].alteonIdWithPoolName.indexOf('('));
					FlowitUtil.log(alteonId);
					return {
						"index" : pools[i].index,
						"alteonId" : alteonId
					};
				}
			}
			
			var $pool = $('.cloneDiv .poolsCbx option').filter(':selected');
			var alteonId = $pool.text().substring(0, $pool.text().indexOf('('));
			FlowitUtil.log(alteonId);
			return {
				"index" : $pool.val(),
				"alteonId" : alteonId
			};
		}
	},
	_registerMemberTableEvents : function()
	{
		with (this)
		{
			var $regMemberTr = $('.cloneDiv .regMemberTr');
			$regMemberTr.off('dblclick');
			$regMemberTr.dblclick(function(e) {				
				if($('.cloneDiv input[name="rPort"]').val() == 0)
				{
					return;
				}
				if (header.getAccountRole() == "readOnly")
				{
					return false;
				}
				var ip = $(this).children().eq(0).text();
				var alteonNodeId = $(this).children().eq(2).text();
				$(this).remove();
				_setPoolMemberCount();
				if (_isNode(ip))
				{
					return false;
				}				
				if(alteonNodeId == "")
				{
					$.obAlertNotice(VAR_ALT_REANNDEL);
					return false;
				}
				$('.cloneDiv .adcNodeTbd').append('<tr><td class="adcNodeTd ui-widget-content">' + ip + '</td><td class="none"> '+alteonNodeId+' </td></tr>');
				_applyMemberTableCss();
				_registerNodeListTableEvents();				
				_setAdcNodeCount();
			});
		}
	},
	_registerMemberTableEvents_v29 : function()
		{
		with (this)
		{
			var $regMemberTr = $('.cloneDiv .regMemberTr');
			$regMemberTr.off('dblclick');
			$regMemberTr.dblclick(function(e){
				if (header.getAccountRole() == "readOnly")
				{
					return false;
				}
				var alteonNodeId = $(this).children().eq(1).text();
				var ip = $(this).children().eq(2).text();
				var port_ = $(this).children().eq(3).text();
				var time = $(this).children().eq(4).text();
				var inter = $(this).children().eq(5).text();
				var retry = $(this).children().eq(6).text();
				var maxcon = $(this).children().eq(7).text();
				var backup = $(this).children().eq(8).text();
				var weight = $(this).children().eq(9).text();
				
				//port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10
				
				//index=17_17_211_17_46_0, 
				//alteonNodeID=46, 
				//ipAddress=192.168.199.46, 
				//port=0, 
				//state=1, 
				//status=1, 
				//backupType=0, 
				//backupId=17_0, 
				//extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], 
				
				//OBDtoAdcPoolAlteon [index=17_212, name=, alteonId=212, lbMethod=2, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_212_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_212_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_212_17_48_0, alteonNodeID=48, ipAddress=192.168.199.48, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_212_17_49_0, alteonNodeID=49, ipAddress=192.168.199.49, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_234, name=, alteonId=234, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_234_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_234_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_235, name=, alteonId=235, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_235_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_235_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_235_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_57, name=, alteonId=57, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_57_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_57_17_46_0, alteonNodeID=46, ipAddress=192.168.199.46, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_57_17_47_0, alteonNodeID=47, ipAddress=192.168.199.47, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_57_17_278_0, alteonNodeID=278, ipAddress=2.2.2.2, port=0, state=1, status=0, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_140, name=, alteonId=140, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_140_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_140_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_210, name=, alteonId=210, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_210_17_48_0, alteonNodeID=48, ipAddress=192.168.199.48, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_210_17_49_0, alteonNodeID=49, ipAddress=192.168.199.49, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_335, name=, alteonId=335, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_335_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_335_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_233, name=, alteonId=233, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_233_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_233_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_2, name=, alteonId=2, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_2_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_2_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_2_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_38, name=, alteonId=38, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_38_17_141_0, alteonNodeID=141, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=1, backupId=17_142, extra=port:,inter:0,retry:0,backup:142,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_40, name=, alteonId=40, lbMethod=1, healthCheck=-1, bakType=2, bakID=17_140, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_40_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_40_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_52, name=, alteonId=52, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_52_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_52_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_52_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_53, name=, alteonId=53, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_53_17_333_0, alteonNodeID=333, ipAddress=172.172.2.45, port=0, state=1, status=2, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_53_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_53_17_45_0, alteonNodeID=45, ipAddress=192.168.199.45, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_54, name=, alteonId=54, lbMethod=1, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_54_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_54_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_241, name=LABTEST_Pool_1, alteonId=241, lbMethod=0, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_241_17_141_0, alteonNodeID=141, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=1, backupId=17_142, extra=port:,inter:0,retry:0,backup:142,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_241_17_42_0, alteonNodeID=42, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_241_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_4, name=_Pool_26, alteonId=4, lbMethod=0, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_4_17_141_0, alteonNodeID=141, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=1, backupId=17_142, extra=port:,inter:0,retry:0,backup:142,weight:1,maxcon:200000/physical,timeout:10]]], OBDtoAdcPoolAlteon [index=17_238, name=smarttest2_Pool_1, alteonId=238, lbMethod=0, healthCheck=-1, bakType=0, bakID=0, healthCheckV2=OBDtoAdcHealthCheckAlteon [dbIndex=17_tcp, id=tcp, name=, type=18, destinationIp=none, extra=tcp], memberList=[OBDtoAdcPoolMemberAlteon [index=17_17_238_17_41_0, alteonNodeID=41, ipAddress=192.168.199.41, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_238_17_142_0, alteonNodeID=142, ipAddress=192.168.199.42, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_238_17_43_0, alteonNodeID=43, ipAddress=192.168.199.43, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10], OBDtoAdcPoolMemberAlteon [index=17_17_238_17_44_0, alteonNodeID=44, ipAddress=192.168.199.44, port=0, state=1, status=1, backupType=0, backupId=17_0, extra=port:,inter:0,retry:0,backup:0,weight:1,maxcon:200000/physical,timeout:10]]]]
				
				
//				var idx = $(this).children().eq(8).text(); 
				
//				var alteonNodeId = $(this).children().eq(9).text();
				$(this).remove();
				_setPoolMemberCount();
				
				if(alteonNodeId == "")
				{
					$.obAlertNotice(VAR_ALT_REANNDEL);
					return false;
				}
//				var arrayVal = [alteonNodeId,ip,port_,time,inter,retry,maxcon,backup,weight];					 
				
//				if (_isNode29(arrayVal))
				if (_isNode29(alteonNodeId, ip))
//				if (_isNode29($(this)))
				{
					return false;
				}
//				$('.cloneDiv .adcNodeTr').eq(0).parent().append('<tr class="adcNodeTr"><td class="ui-widget-content">' + ip + '</td><td>' +port_+ '</td><td class="none">' +inter+ '</td><td class="none">' +retry+ '</td><td class="none">' +backup+ '</td><td class="none">' +weight+ '</td><td class="none">' +maxcon+ '</td><td class="none">' +time+ '</td><td class="none"></td><td class="none">' +alteonNodeId+ '</td></tr>');
//				$('.cloneDiv .adcNodeTr').eq(0).parent().append('<tr class="adcNodeTr"><td class="ui-widget-content none">Enabled</td><td>' + alteonNodeId + '</td><td>' +ip+ '<td>' +port_+ '</td><td class="none">' +time+ '</td><td class="none">' +inter+ '</td><td class="none">' +retry+ '</td><td class="none">' +maxcon+ '</td><td class="none">' +backup+ '</td><td class="none">' +weight+ '</td><td class="none"></td></tr>');
				$('.cloneDiv .adcNodeTr').eq(0).parent().append('<tr class="adcNodeTr"><td class="ui-widget-content adcNodeTd none">Enabled</td><td>' + alteonNodeId + '</td><td class="adcNodeIp">' +ip+ '<td>' +port_+ '</td><td class="none">' +time+ '</td><td class="none">' +inter+ '</td><td class="none">' +retry+ '</td><td class="none">' +maxcon+ '</td><td class="none">' +backup+ '</td><td class="none">' +weight+ '</td><td class="none"></td></tr>');
				_applyMemberTableCss();
				_registerNodeListTableEvents_v29();				
				_setAdcNodeCount();
			});
		}
	},
	_registerNodeListTableEvents : function()
	{
		
		with(this)
		{
			$('.cloneDiv .adcNodeTd').on('dblclick',function(e)
			{
				with(this)
				{
					if($('.cloneDiv input[name="rPort"]').val() == 0)
					{
						return;
					}
					if (header.getAccountRole() == "readOnly")
					{
						return;
					}
					if(this.initFlag==true)
					{
						return;
					}

					e.preventDefault();
					
					var node = $(this).text();
					var alteonNodeId = $(this).parents().children().eq(1).text();
					var $enabled = $('.cloneDiv .memberEnabledChk');
					if (!_validateMemberInput(node))
						return false;
					this.initFlag=true;
					//$('.cloneDiv .memberIpTxt').val(node);					
					_addToMemberTable(node, $('.cloneDiv input[name="rPort"]').val(), $enabled.is(':checked'), alteonNodeId);					
					$('.cloneDiv .adcNodeTd').removeClass('on');
					$(this).addClass('on');
					_delFromNodeList();
				}
			});
		}
	},
	_registerNodeListTableEvents_v29 : function()
	{
		
		with(this)
		{
			$('.cloneDiv .adcNodeTr').on('dblclick',function(e)
			{
				with(this)
				{
					if (header.getAccountRole() == "readOnly")
					{
						return;
					}
					if(this.initFlag==true)
					{
						return;
					}
					this.initFlag=true;
					var node = '';
					var port_ = '';
					var time = '';	
					var inter = '';
					var retry = '';
					var maxcon = '';
					var backup = '';
					var weight = '';
					
					var alteonNodeId = '';
					
					e.preventDefault();
					FlowitUtil.log('-------- click');
					if($(this).children().eq(1).text() != "null")
					{
						alteonNodeId = $(this).children().eq(1).text();
					}
					if($(this).children().eq(2).text() != "null")
					{
						node = $(this).children().eq(2).text();
					}
					if($(this).children().eq(3).text() != "null")
					{
						port_ = $(this).children().eq(3).text();
					}
					if($(this).children().eq(4).text() != "null")
					{
						time = $(this).children().eq(4).text();
					}
					if($(this).children().eq(5).text() != "null")
					{
						inter = $(this).children().eq(5).text();
					}
					if($(this).children().eq(6).text() != "null")
					{
						retry = $(this).children().eq(6).text();
					}
					if($(this).children().eq(7).text() != "null")
					{
						maxcon = $(this).children().eq(7).text();
					}
					if($(this).children().eq(8).text() != "null")
					{
						backup = $(this).children().eq(8).text();
					}
					if($(this).children().eq(9).text() != "null")
					{
						weight = $(this).children().eq(9).text();
					}
					
//					if($(this).children().eq(9).text() != "null")
//					{
//						alteonNodeId = $(this).children().eq(9).text();
//					}
					
					var arrayVal = [alteonNodeId,node,port_,time,inter,retry,maxcon,backup,weight];					 
										
					var $enabled = $('.cloneDiv .memberEnabledChk');
//					if (!_validateMemberInput_v29(arrayVal))
					if (!_validateMemberInput_v29($(this)))
						return false;
					
//					if (!_validateMemberInput_v29(node, alteonNodeId))
//						return false;
					//$('.cloneDiv .memberIpTxt').val(node);					
					_addToMemberTable_sub($enabled.is(':checked'), alteonNodeId, node, $('.cloneDiv input[name="rPort"]').val(), time, inter, retry, maxcon, backup, weight);					
					$('.cloneDiv .adcNodeTr').removeClass('on');
					$(this).addClass('on');
					_delFromNodeList_v29();
				}
			});
		}
	},
	_validateVirtualSvc : function(orgSrvPort)
	{
		with(this)
		{
			if ($('.cloneDiv input[name="svcPort"]').val() == '') 
			{
				$.obAlertNotice(VAR_ALT_SER_INP);
				return false;
			}
			else if ($('.cloneDiv .regMemberTr').length == 0)
			{
				$.obAlertNotice(VAR_ALT_MEMINP);
				return false;
			}	
			
			var vPort = $('.cloneDiv input[name="svcPort"]').val();
//			var vrPort = $('.cloneDiv input[name="rPort"]').val();
	
			if(_checkSvcReg(vPort, orgSrvPort)==false)
			{
				$.obAlertNotice(VAR_ALT_SERDUP);
				vPort = null;
				return false;
			}
			vPort = null;
			return true;
		}
	},
	_applyVirtualSvc : function(rowNoToModify, virtualSvc)
	{
		with (this)
		{
			html = '';
			html += '<tr class="ContentsLine3">';
			html += '<td class="align_center"><input class="virtualSvcsChk" type="checkbox" value="' + virtualSvc.svcPort + '"/><span class="none virtualSvcsJson">' + Object.toJSON(virtualSvc) + '</span></td>';
			html += '<td class="align_center"><a class="virtualSvcPortLnk">' + virtualSvc.svcPort + '</a></td>';
			html += '<td class="align_center">';
			html += '<a class="svcGroupIndex">' + virtualSvc.pool.alteonId + '</a>';
			html += '</td>';
			html += '<td class="align_left">';
			html += '<a class="svcGroupName">' + virtualSvc.pool.name + '</a>';
			html += '</td>';
			html += '<td class="align_center">' + virtualSvc.pool.members.length + '</td>';
			html += '</tr>';
			if (rowNoToModify == undefined || rowNoToModify == -1)
				$('.virtualSvcTbd').append(html);
			else
				$('.virtualSvcTbd > tr').eq(rowNoToModify).replaceWith(html);		
			
			_applyVirtualSvcCss();
			_setVirtualSvcCount();
		}
	},
	_setVirtualSvcCount : function()
	{
		$('.virtualSvcCountSpn').text($('.virtualSvcTbd .ContentsLine3').length);
	},
	_applyVirtualSvcCss : function()
	{
		$('.virtualSvcTbd tr').removeAttr('style');
		// 테이블 컬럼 정렬
		initTable([ ".table_type1 tbody tr" ], [ -1 ], [ -1 ]);
	}
});