var ServiceGrpConfig = Class.create({
	initialize : function() 
	{
		var fn = this;
		this.searchedKey = undefined;
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 14;// id
		this.searchFlag = false;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderDir, orderType)
		{
			fn.loadServiceGrpTableInListContent(fn.searchedKey, fromRow, toRow, orderDir, orderType);
		});
	},
	
	loadListContent : function(searchKey, orderDir, orderType)
	{
		with(this)
		{
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "sysSetting/retrieveServiceGroupTotal.action",
				data :
				{
					"searchKey" : searchKey
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						rowTotal = 100;
					}
					else
					{
						rowTotal = data.rowTotal;
					}
					pageNavi.updateRowTotal(rowTotal, orderType);
					noticePageInfo();
					loadServiceGrpListContent(searchKey);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_GROUPINFOLOAD, jqXhr);
				}			
			});
		}		
	},	
	loadServiceGrpListContent : function(searchKey, orderDir, orderType) 
	{
		with (this)
		{			
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadServiceGroupContent.action',
				data : 
				{
					"searchKey" : searchKey,
					"orderType"	: orderType,
					"orderDir" : orderDir
				},
				target : "#wrap .contents", 
				successFn : function(params)
				{					
					header.setActiveMenu('SysSettingLogicalGroup');
					noticePageInfo();
					registerContents();
				},
				completeFn : function() 
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_ADMINLOGLOAD, jqXhr);
				}
			});		
		}
	},
	loadServiceGrpTableInListContent : function(searchKey, fromRow, toRow, orderDir, orderType)
	{
		with(this)
		{
			ajaxManager.runHtmlExt({
				url : "sysSetting/loadServiceGrpTableInListContent.action",
				data : 
				{
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "table.Board",
				successFn : function(params)
				{
					noticePageInfo();
					searchedKey = searchKey;
					registerContents();
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_GROUPINFOLOAD, jqXhr);
				}
			});	
		}
	},
	loadServiceGrpAddContent : function()
	{
		with(this)
		{
			ajaxManager.runHtmlExt({
				url : "sysSetting/loadServiceGrpAddContent.action",
				target : "#wrap .contents",
				successFn : function(params)
				{
					$unassignedAdcOptions = $();
//					displayAdcAssignmentByRole($('select[name="account.roleId"]').val());
					registerServiceGrpAddContentEvents(false);
//					restoreUserAddContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_LOADUSERADDCONTENT, jqXhr);
				}
			});
		}		
	},
	
	loadServiceGrpModifyContent : function(grpIndex) 
	{
		with (this) 
		{
//			setLeftTreeToUserMgmt();
			ajaxManager.runHtmlExt({
				url : "sysSetting/loadServiceGrpModifyContent.action",
				target: "#wrap .contents",
				data: 
				{
					"vsGrpInfo.index" : grpIndex
				},
				successFn : function(params) 
				{
					header.setActiveMenu('SysSetting');
					$unassignedAdcOptions = $();
//					displayAdcAssignmentByRole($('select[name="account.roleId"]').val());
					registerServiceGrpAddContentEvents(true, grpIndex);
//					restoreUserAddContent();
				},
				errorFn : function(jqXhr)
                {
					$.obAlertAjaxError(VAR_COMMON_SYSTEMSETFAIL, jqXhr);
//                    exceptionEvent();
                }
			});
		}
	},
	serviceGrpAddOrModifySubmit : function(isModify, grpIndex)  //ajaxSubmit를 함수로 따로 만듦. junhyun.ok_GS
	{
		with (this) 
		{
			var vsIndex = checkRegisteredVsForSubmit();
			
			if ($("#selectedList").find(".availableVs").size() > 11)
			{
				$.obAlertNotice(VAR_SERVICEGROUP_COUNTCHK);
				return false;
			}
			var vsGroupName = $('input[name="vsGrpInfo.name"]').val();
//			var rsIndex = checkRegisteredRsForSubmit();
//			alert(checkRegisteredVsForSubmit());
			var params = 
			{				
				"vsIndexList" : vsIndex,
				"vsGroupName" : vsGroupName,
				"vsGrpInfo.index" : grpIndex
//				"rsIndexList" : rsIndex
			};
			
			if (!_validateSvcGroupAdd())
				return false;
		
			$('#serviceGrpAddFrm').ajaxSubmit
			({
				dataType : 'json',
				url : isModify ? 'sysSetting/modifyServiceGrp.action' : 'sysSetting/addServiceGrp.action',
				data : params,		
				success : function(data) 
				{
					FlowitUtil.log(Object.toJSON(data));
					if (data.isSuccessful) 
					{ 
						$.obAlertNotice(VAR_COMMON_REGISUCCESS);							
						if (data.accountRole == 'system') 
							loadListContent();
					} 
					else
					{
						$.obAlertNotice(data.message);
					}	
				},
				error : function(jqXhr) 
				{
					$.obAlertAjaxError(VAR_SYSSETTING_USERADDMODIFY, jqXhr);
				}
			}); 
		}
	},
	registerServiceGrpAddContentEvents : function(isModify, grpIndex)
	{
		with(this)
		{
			$('#serviceGrpAddFrm').submit(function() 
			{
				selectRegisteredAdcsForSubmit();
				
				if(!isModify) //modify이 false 즉, 사용자를 추가할 때
				{// 계정 추가시 validateUserAdd
//					if (!validateUserAdd())
//						return false;
					
					var vsGroupName = $('input[name="vsGrpInfo.name"]').val();
					
					if (!_validateSvcGroupAdd())
						return false;
					
					ajaxManager.runJsonExt   // 사용자가 입력한 ID 중복 체크
					({
						url : "sysSetting/serviceGrpNameCheck.action",
						data :
						{
							"vsGroupName" : vsGroupName,
							"vsGrpInfo.index" : grpIndex
						},
						successFn : function(data)
						{
							if(!data.isSuccessful)
							{ //리턴값이 false면 ID 중복
								$.obAlertNotice(VAR_SYSSETTING_ACCOUNTIDCHECK);
							}
							else
							{	//리턴값이 true면 ID 중복 아님. 
								serviceGrpAddOrModifySubmit(false, grpIndex);
							}
						}
					});
				}
				else	//modify가 true 즉, 사용자를 수정할 때
				{// 계정 수정시 validateUserModify
//					if (!validateUserModify())
//						return false;
					
					serviceGrpAddOrModifySubmit(true, grpIndex);
				}
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
												
			$('.serviceGrpAddCancelLnk').click(function(e) 
			{
				e.preventDefault();
				loadListContent();
			});
			
			$('.serviceGrpAddOkLnk').click(function(e) 
			{					
				e.preventDefault();
				$('#serviceGrpAddFrm').submit();
			});
			
			$("#toVsDeselectionLnk").click(function() 
			{
				// rs 가 포함되어 잇는 경우 deselect X				
				if (isValidAdcCheck())
				{
					$.obAlertNotice(VAR_SYSSETTING_NOVSADC);
					moveAdcVsToDeselection();
					return false;
				}
				
				moveAdcVsToDeselection();
			});
						
			$("#toVsSelectionLnk").click(function() 
			{				
				//var checked_ids = [];
		
				// total serviec count
				var org_adcVs_count = $('#selectedList').children().find('.availableVs').length;
				var chk_adcVs_count = $("#unSelectedList").find(".jstree-checked").find('.availableVs').length;
				var total_count = org_adcVs_count + chk_adcVs_count;
				if (total_count > 9)
				{
					alert(VAR_SERVICEGROUP_AVAILABLE_COUNT);
					return false;
				}
				// class server (right list) 중에서 체크된것만 찾아온다.
				$("#unSelectedList .availableAdc").find(".jstree-checked").each(function(i)
				{
					// right list 에 있는 adc 정보 (id, name)
					var org_availableAdc_id = $(this).parent().parent().attr("id");	
					var org_availableAdc_nm = $(this).parent().parent().attr("nm");
//							alert(org_availableAdc_id);
					// right list 에 있는 virtual server 벙보 (id, name)
				    var org_availableVs_id = $(this).attr("id");
				    
				    var org_availableVs_nm = "";
				    if ($(this).attr("nm") == "")
				    {
				    	org_availableVs_nm = $(this).attr("ip");
				    }
				    else
				    {
				    	org_availableVs_nm = $(this).attr("nm");
				    }
				    
				    //checked_ids.push(src_server_id + "%%" + src_server_nm);
				    
				    var split = org_availableVs_id.split("_");  // vs 정보는 (adcIndex_vsIndex)
				    var adcIndex = split[0]; // adcIndex
				    
				    var isExist = false;
				    var availableAdc_id = "";
				    var availableAdc_nm = "";
				    // right list 에 있는 adc 가 left list 에 있는지 체크.
				    $("#selectedList").find(".availableAdc").each(function(i) 
				    {
				    	availableAdc_id = $(this).attr("id");		// left에 존재하는 id alert("availableAdc_id : " + availableAdc_id);
				    	availableAdc_nm = $(this).attr("nm");
				    	
//						    	alert("adcIndex : " + adcIndex);
				    	if (availableAdc_id == adcIndex) 		//
				    	{
				    		isExist = true;
				    		return false;
				    	}
				    });
				    
				    // left list 에 adc 가 없으면 adc 추가.
				    if (false == isExist) {
				    	availableAdc_id = org_availableAdc_id;
				    	availableAdc_nm = org_availableAdc_nm;
				    	//alert(availableAdc_id);
				    	//alert(availableAdc_nm);
				    	// root 노드를 하나 만든다.
//				    	if ($('#selectedList').children().find('.availableVs').length > 9)
//						{
//							alert("선택가능한 서비스 개수는 10개입니다.");
//							return false;
//						}
//				    	else
				    		$("#selectedList").jstree("create",-1,"first",{attr:{id:availableAdc_id, nm:availableAdc_nm, 'class':"availableAdc"}, data:availableAdc_nm}, false, true); 
//						    	$("#selectedList").jstree("create",null,"first",{attr:{id:org_availableAdc_id, nm:org_availableAdc_nm, class:"availableAdc"}, data:org_availableAdc_nm}, false, true);
				    }
				    
				    
				    isExist = false;
				    var availableVs_id = "";
				    //var availableVs_nm = "";
				    
				    // left list 에 존재하는 adc 에 vitual server 가 있는지 체크.
				    $("#selectedList #" + availableAdc_id).find(".availableVs").each(function(i) 
				    {
				    	availableVs_id = $(this).attr("id");
				    	//server_nm = $(this).attr("nm");
				    	
				    	if (availableVs_id == org_availableVs_id) 
				    	{
				    		isExist = true;
				    		return false;
				    	}
				    });
				    
				    // left list 에 virtual server 가 없으면 virtual server 추가.
				    if (false == isExist) 
				    {  	// left list에 추가한다.
				    	
//				    	if ($('#selectedList').children().find('.availableVs').length > 9)
//						{
//							alert("선택가능한 서비스 개수는 10개입니다.");
//							return false;
//						}
//				    	else
//				    	{
				    		$("#selectedList").jstree("create","#" + availableAdc_id,"first",{attr:{id:org_availableVs_id, nm:org_availableVs_nm, 'class':"availableVs"}, data:org_availableVs_nm}, false, true);				    		
//				    		$('#unSelectedList').find('.availableVs').attr("id").remove();
//				    		$("#unSelectedList").find('.jstree-checked').remove();
//				    		$("#unSelectedList").find(".jstree-checked").each(function(i)
//		    				{
////				    			$('#unSelectedList').find('.availableVs').attr("id").remove();
//				    			$(this).find('.availableVs').attr('id').remove();
////		    					if ($('#selectedList').children().find('.availableVs').length > 9)
////		    						return false;							
////		    					else
////		    						$(this).remove();
//		    				});
//				    	}					    	
				    }		    
				    
				}); 
				
				$("#unSelectedList").find(".jstree-checked").each(function(i)
				{	
					$(this).remove();
				});
				
//				$("#unSelectedList").find(".jstree-checked").find('.availableVs').each(function(i)
//				{			
//					if ($('#selectedList').children().find('.availableVs').length > 9)
//					{						
//						return false;
//					}
//					else
//					{
//						$(this).remove();
//					}
//					
//					
//					if($("#unSelectedList").find(".jstree-checked").find('.availableVs').length == 0)
//					{
//						$(this).parent().remove();
//					}
//				});
				
				
//				$("#selectedList").find(".availableVs").each(function(i) 
//				{
//					availableVs_id = $(this).attr('id');							
//					org_availableVs_id = $('#unSelectedList').find('.availableVs').attr('id');
//					
//					if ($('#selectedList').children().find('.availableVs').length > 9)
//					{
////						alert("aaa");
//					}
//					else
//					{
//						$("#unSelectedList").find(".jstree-checked").each(function(i)
//			    		{
//			    			if (availableVs_id == org_availableVs_id) 
//					    	{
//						    	$(this).remove();
//					    	}
//			    		});
//					}
//		    	
//				});
				
				// right list 에서 선택된 것들은 삭제.
//				$("#unSelectedList").find(".jstree-checked").each(function(i)
//				{
//					if ($('#selectedList').children().find('.availableVs').length > 9)
//					{
//						$("#selectedList").find(".availableAdc").each(function(i) 
//						{
//							availableVs_id = $(this).find('.availableVs').attr('id');							
//							org_availableVs_id = $('#unSelectedList').find('.availableVs').attr('id');
//							
//					    	if (availableVs_id == org_availableVs_id) 
//					    	{
//					    		$("#unSelectedList").find(".availableVs").attr('id').remove();
////					    		$(this).remove();
//					    	}
//						});
//						
//						return false;
//					}
//					else
//					{
//						$("#selectedList").find(".availableAdc").each(function(i) 
//						{
//							availableVs_id = $(this).find('.availableVs').attr('id');							
//							org_availableVs_id = $('#unSelectedList').find('.availableVs').attr('id');
//							
//					    	if (availableVs_id == org_availableVs_id) 
//					    	{
//					    		$("#unSelectedList").find(".availableVs").attr('id').remove();
////							    		$(this).remove();
//					    	}
//						});
//					}
////						$(this).remove();
//				});
				
				moveAdcVsToSelection();				 
   	      	});
			
			$(".unSelectedList").jstree({
				"themes" : 
				{
					"theme" : [ "classic" ]
				},
				"ui" : 
				{
//						"initially_select" : [ "rhtml_2" ]
				},
				"core" : 
				{ 
					"initially_open" : [ "adcGroup" ] 
				},
				"plugins" : [ "themes", "html_data", "ui", "crrm", "checkbox", "sort" ]
			});
			
			$(".selectedList").jstree({ 
                "themes" : 
                { 
                	"theme" : "classic", 
                	"dots" : true, 
                    "icons" : true 
                }, 
                "plugins" : [ "themes", "html_data", "ui", "crrm", "checkbox", "sort" ]
            });
		}
	},
	
	registerContents : function() 
	{
		with (this) 
		{
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('p.cont_sch input.searchTxt').val();				
				FlowitUtil.log('click:' + searchKey);		
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('p.cont_sch input.searchTxt').val();				
				FlowitUtil.log('click:' + searchKey);		
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('p.cont_sch input.searchTxt').val();				
				FlowitUtil.log('click:' + searchKey);		
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType);
			});
			$('.refreshLnk').click(function(e) 
			{
				e.preventDefault();
				loadListContent();
			});	
						
			$('.addServiceGrpLnk').click(function(e)
			{
				e.preventDefault();
				loadServiceGrpAddContent();
			});
			
			$('.accountIdLnk').click(function(e) 
			{
				e.preventDefault();
				var grpIndex = $(this).parent().parent().find('.svcGrpChk').val();
//				var grpName = $(this).text().trim();
				loadServiceGrpModifyContent(grpIndex);
			});
			
			$('.delGroupsLnk').click(function(e) 
			{
				e.preventDefault();
				delGroups();
			});
			
			$('.allSvcGrpChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				//$(this).parent().parent().parent().parent().find('.userChk').attr('checked', isChecked);
				$(this).parents('table.Board').find('.svcGrpChk').attr('checked', isChecked);
			});
			
			$('p.cont_sch a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('p.cont_sch input.searchTxt').val();
				if(($('.control_Board input.searchTxt').val() != "") && ($('.control_Board input.searchTxt').val() != null))
				{
					if(!validateSearchKey())
					{						
						return false;
					}	
				}				
				FlowitUtil.log('click:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
			});
			
			$('p.cont_sch input.searchTxt').keydown(function(e)
			{
				if (e.which != 13)
				{
					return;
				}
				if(($('.control_Board input.searchTxt').val() != "") && ($('.control_Board input.searchTxt').val() != null))
				{
					if(!validateSearchKey())
					{						
						return false;
					}	
				}
				var searchKey = $(this).val();

				FlowitUtil.log('click:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
			});
			
			if(this.searchFlag == true)
			{
				$('.dataNotExistMsg').addClass("none");
				if($('.userList').size() > 0)
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
				if($('.userList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
			
		}
	},
	validateSearchKey : function()
	{
		var search = $('.control_Board input.searchTxt');
		
		if(!isValidStringLength(search, 1, 20))
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
		
		return true;
	},
	isValidAdcCheck : function()
	{		
		return $('#selectedList .availableAdc').filter(function(){
	
//			if (!$(this).is(':checked'))
//			if (!$(this).parent().find('.availableAdc').hasClass('jstree-checked'))				
//			if (!$('#selectedList ul').filter(':first').find('.availableAdc').hasClass('jstree-checked'))
			if (!$(this).hasClass('jstree-checked') && !$(this).find('li').hasClass('jstree-checked'))
			{
				return false;
			}
			else
			{
				if($('#selectedRsList li[id="' + $(this).attr('id') +'"]').attr('id') == $(this).attr('id'))
				{	
					return $('#selectedRsList .availableAdc[nm="' + $(this).attr('nm') + '"]').size() > 0;
				}
				else
				{
//					$("#selectedList .availableAdc").find(".jstree-checked").each(function(i)
					$(this).find(".jstree-checked").each(function(i)
					{
						// left list 에 있는 adc 정보 (id, name)
						var org_availableAdc_id = $(this).parent().parent().attr("id");	
						var org_availableAdc_nm = $(this).parent().parent().attr("nm");					
	//							alert(org_availableAdc_id);
						
						// left list 에 있는 virtual Sever 정보 (id, name)
						var org_availableVs_id = $(this).attr("id");
						var org_availableVs_nm = $(this).attr("nm");
	//							alert(org_availableVs_id);			// 24_51
						
						var split = org_availableVs_id.split("_");					
						var adcIndex = split[0];
						
						var isExist = false;
						var availableAdc_id = "";
						var availableAdc_nm = "";
						
						// left list 에 있는 adc 가 right list 에 있는지 체크.
						$("#unSelectedList").find(".availableAdc").each(function(i)
						{
							availableAdc_id = $(this).attr("id");
							availableAdc_nm = $(this).attr("nm");
							
	//								alert(availableAdc_id);
							if (availableAdc_id == adcIndex)
							{
	//									alert(availableAdc_id);
	//									alert(adcIndex);
								isExist = true;
								return false;
							}
						});					
						
						// right list 에 adc 가 없으면 adc 추가.
						if (false == isExist)
						{
							availableAdc_id = org_availableAdc_id;
							availableAdc_nm = org_availableAdc_nm;
							$("#unSelectedList").jstree("create", -1, "first", {attr:{id:availableAdc_id, nm:availableAdc_nm, 'class':"availableAdc"}, data:availableAdc_nm}, false, true);
						}
						
						isExist = false;
						var availableVs_id = "";
						
						$("#unSelectedList #" + availableAdc_id).find(".availableVs").each(function(i)
						{
							availableVs_id = $(this).attr("id");	
	//								alert("1 : " + availableVs_id); 
							
							if(availableVs_id == org_availableVs_id)
							{
								isExist = true;
								return false;
							}
						});
						
						if (false == isExist)
						{				
							$("#unSelectedList").jstree("create", "#" + availableAdc_id, "first", {attr:{id:org_availableVs_id, nm:org_availableVs_nm, 'class':"availableVs"}, data:org_availableVs_nm}, false, true);
						}										
					});
					
					$("#selectedList").find(".jstree-checked").each(function(i)
					{
						if($('#selectedRsList').find('.availableAdc').attr('id') != $(this).attr('id').split('_')[0])
							$(this).remove();
					});
					
//					$("#selectedList").find(".jstree-checked").each(function(i)
//					$(this).find(".jstree-checked").each(function(i)
//					{						
//						if($('#selectedRsList li[id="' + $(this).attr('id') +'"]').attr('id') != $(this).attr('id'))
//						$(this).remove();
//					});
//					
//					$("#selectedList").find(".jstree-checked").each(function(i)
//					{
//						if($('#selectedRsList li[id="' + $(this).attr('id') +'"]').attr('id') != $(this).parent().parent().attr('id'))
//							$(this).remove();
//					});
				}
			}
		}).size() >0;		
		
//		var selectedAdcSize = $('#selectedList .availableAdc').filter(function(){
//			  return $('#selectedRsList .availableAdc[nm="' + $(this).attr('nm') + '"]').size() > 0;
//		}).size();		
//		
//		if (selectedAdcSize > 0)
//			return true;
//		else
//			return false;
	},
	isValidAdcChk : function()
	{
		var selectedAdcSize = $('#unSelectedRsList .availableAdc').filter(function(){
			  return $('#selectedList .availableAdc[nm="' + $(this).attr('nm') + '"]').size() > 0;
		}).size();
		
		if (selectedAdcSize > 0)
			return true;
		else
			return false;
	},	
	selectRegisteredAdcsForSubmit : function() 
	{
		$('.adcsSelectedSel > option').attr('selected', true);			
	},
	moveAdcVsToSelection : function() 
	{
		with (this) 
		{
			var $adcVsSelected = $('.selectedList');
			var $adcVsDeselected = $('.unSelectedList');
					
			var $option = $adcVsDeselected.children(':checked');
			FlowitUtil.log($option.size());
			if ($option.size() > 0)
				$adcVsSelected.append($option);
			
			showSelectedAdcVsCount();
		}
	},
	showSelectedAdcVsCount : function() 
	{
//		$('.selectedAdcsCount').text($('.adcsSelectedSel').children().length + '개 선택됨');
		var selectedSize;		
		selectedSize = $('#selectedList').children().find('.availableVs').length; //$('.selectedList .availableVs').size();
		$('.selectedAdcVsCount').text(selectedSize+"/10");	
	},
	moveAdcVsToDeselection : function() 
	{
		with (this) 
		{
			var $adcVsSelected = $('.selectedList');
			var $adcVsDeselected = $('.unSelectedList');
			
			var $option = $adcVsSelected.children(':checked');
			FlowitUtil.log($option.size());
			if ($option.size() > 0)
				$adcVsDeselected.append($option);
			
			showSelectedAdcVsCount();
		}
	},
	_searchOnUnassignedAdcs : function(searchKey) 
	{
		with (this) 
		{
			var $adcsDeselectedSel = $('.adcsDeselectedSel');
			if (!searchKey) 
			{
				$adcsDeselectedSel.append($unassignedAdcOptions);
				$unassignedAdcOptions = $();
				return ;
			}
			
			_fillCbxWithSearchedAndSaveUnsearched(searchKey, $adcsDeselectedSel);
		}
	},
	_fillCbxWithSearchedAndSaveUnsearched : function(searchKey, $adcsDeselectedSel) 
	{
		with (this) 
		{
			$unassignedAdcOptions = $unassignedAdcOptions.add($adcsDeselectedSel.children().detach());
			var keyInLowerCase = searchKey.toLowerCase();
			FlowitUtil.log('keyInLowerCase: ' + keyInLowerCase);
			var $unsearchedOptions = $();
			$unassignedAdcOptions.each(function() 
			{
				var index = $(this).text().toLowerCase().indexOf(keyInLowerCase);
				FlowitUtil.log('index: ' + index);
				if (index == -1)
					$unsearchedOptions = $unsearchedOptions.add($(this));
				else 
					$adcsDeselectedSel.append($(this));
			});
			
			$unassignedAdcOptions = $unsearchedOptions;
		}
	},
	_searchOnUnassignedVs : function(searchKey) 
	{
		with (this) 
		{
			var $vsDeselectedSel = $('#unSelectedList');
			if (!searchKey) 
			{
				$vsDeselectedSel.append($unassignedVsOptions);
				$unassignedVsOptions = $();
				return ;
			}
			
			_fillVsBoxWithSearchedAndSaveUnsearched(searchKey, $vsDeselectedSel);
		}
	},
	_fillVsBoxWithSearchedAndSaveUnsearched : function(searchKey, $vsDeselectedSel) 
	{
		with (this) 
		{
			$unassignedVsOptions = $unassignedVsOptions.add($vsDeselectedSel.children().detach());
			var keyInLowerCase = searchKey.toLowerCase();
			FlowitUtil.log('keyInLowerCase: ' + keyInLowerCase);
			var $unsearchedOptions = $();
			$unassignedVsOptions.each(function() 
			{
				var index = $(this).text().toLowerCase().indexOf(keyInLowerCase);
				FlowitUtil.log('index: ' + index);
				if (index == -1)
				{
					$unsearchedOptions = $unsearchedOptions.add($(this));
				}		
				else
				{
					$vsDeselectedSel.append($(this));
					$.obAlertNotice($vsDeselectedSel.append($(this)));
					FlowitUtil.log('222222222222222222222: ' + $vsDeselectedSel.append($(this)));
				}
					
			});
			$unassignedVsOptions = $unsearchedOptions;
		}
	},
	checkRegisteredVsForSubmit : function()
	{	
		var vsIndex = "";
		var selectedVsIndex = "";
		$("#selectedList").find(".availableVs").each(function(i)
		{
			selectedVsIndex = $(this).attr("id");
//			alert(selectedVsIndex);			
			if (vsIndex != "")
				vsIndex += "|";
			vsIndex += selectedVsIndex;			
//			alert(vsIndex);		
		});
		
		return vsIndex;		
	},
	
	delGroups : function()
	{
		with(this)
		{
			var groupIndexes = _getCheckedGroupIndexes();
			if(groupIndexes.length == 0)
			{
				$.obAlertNotice(VAR_SYSSETTING_GROUPDELSEL);
				return;
			}
			
			var chk = confirm(VAR_SYSSETTING_GROUPDEL + "\n" + VAR_SYSSETTING_DASHGROUPDEL);
			if(chk == false)
			{
				return;
			}
			
			ajaxManager.runJsonExt({
				url : "sysSetting/delServiceGrp.action",
				data :
				{
					"groupIndexList" : groupIndexes
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					loadListContent();					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_GROUPDELFAIL, jqXhr);
				}
			});
		}
	},
	
	_getCheckedGroupIndexes : function()
	{
		return $('.svcGrpChk').filter(':checked').map(function()
		{
			return $(this).val();
		}).get();
	},
	noticePageInfo : function()
	{
		with(this)
		{
			var currentPage = pageNavi.getCurrentPage();
			var lastPage = pageNavi.getLastPage();
			var countTotal = pageNavi.getRowTotal();
			var targetCntHtml = $('.noticePageCountInfo').filter(':last');
			var targetPageHtml = $('.noticePageInfo').filter(':last');
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	},
	_validateSvcGroupAdd : function()
	{
		with(this)
		{
			// 구간 이름 필드 검사. 
			
			var vsGroupName = $('input[name="vsGrpInfo.name"]').val();
			if (vsGroupName == "")
			{
				$.obAlertNotice(VAR_SYSSETTING_GROUPNAME);
				return false;
			}
			else if (getValidateStringint(vsGroupName, 1, 40) == false)
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				return false;
			}	

			
			if ($('#selectedList').length > 0 && $('#selectedList').find('ul').text() == '')
				
			{
				$.obAlertNotice(VAR_CONFIG_VSSELECT);
				return false;
			}
			return true;
		}	
	}	
});