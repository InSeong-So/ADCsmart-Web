var Node = Class.create({
	initialize : function() 
	{
		var fn = this;
		this.adc;
		this.searchedKey = undefined;
		this.orderDir  = 2; // 2 :  내림차순
		this.orderType = 33;// 33 : NodeIp
		this.pageNavi = new PageNavigator();
		this.orderGroupDir  = 0; // 2 :  내림차순
		this.orderGroupType = 0;// 37 : Group
		this.searchFlag = false;
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			fn.loadNodeTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
		});
		this.selectedRsIndexList = undefined;
	},
	onAdcChange : function()
	{
		this.orderDir = 2; //내림차순
		this.orderType = 33; //NodeIp
		node.loadListContent();
	},	
	loadListContent : function(searchKey, orderDir, orderType, orderGroupDir, orderGroupType)
	{
		if (adcSetting.getAdc().type == "F5")
		{
			if(header.getVsSettingTap() == 0)
			{
				virtualServer.loadListContent();
				return;
			}
			else if(header.getVsSettingTap() == 1)
			{
				profile.loadProfileListContent();
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
			
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieverNodeListTotal.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"searchKey" : searchKey
				},
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						rowTotal = 100;
					}
					else
					{
						rowTotal = data.rowTotal;
					}
					
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadNodeListContent(searchKey);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_RS_LOADFAIL, jqXhr);
				}
			});
		}
	},
	loadNodeListContent : function(searchKey, fromRow, toRow) 
	{
		with (this) 
		{		
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadNodeListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"searchKey" : searchKey == null ? undefined : searchKey,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType,
					"orderGroupDir" : this.orderGroupDir,
					"orderGroupType" : this.orderGroupType
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('SlbSetting');
					noticePageInfo();
					searchedKey = searchKey;
					registerNodeListContentEvents();
					$('[data-index]').each(function()
					{
//						rsGroupCount($(this).data('index'));
						rsGroupCount($(this));
//						if ($('tr[data-parent=' + $(this).data('index') + ']').length == 0 )
//						{
//							$('.Group').addClass('none');
//						}
						
						if (!$('.searchNotMsg').hasClass('none'))
//						if ($('.searchNotMsg:not(.none)'))
							$(this).addClass('none');
						
						$(this).find('.rsGroupState').text(rsGroupState($(this)));
					});
					
					if(selectedRsIndexList != undefined)
					{
						for(var i=0; i < selectedRsIndexList.length; i++)
						{
							$('#rsGrpSelected tbody tr').find('.nodeChk').each(function(index)
							{
								if($(this).val() == selectedRsIndexList[i])
								{
									$(this).attr('checked', 'checked');
								}
							});
						}
//						selectedRsIndexList = orgSelectedRsIndexList;
					}
					
					_avilableStateChk();
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_RS_LOADFAIL, jqXhr);
				}
			});
		}
	},
	loadNodeTableInListContent : function(searchKey, fromRow, toRow, orderType, orderDir)
	{
		with (this)
		{			
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadNodeTableInListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey == null ? undefined : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType	
				},
				target : "table.virtualSvrTable",
				successFn : function(params)
				{
					noticePageInfo();
					searckedKey = searchKey;
					registerNodeListContentEvents();
					$('[data-index]').each(function()
					{
//								rsGroupCount($(this).data('index'));
						rsGroupCount($(this));
						$(this).find('.rsGroupState').text(rsGroupState($(this)));
					});
					
					if (!$('.searchNotMsg').hasClass('none'))
						$(this).addClass('none');
					
					_avilableStateChk();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_RS_LOADFAIL, jqXhr);
				}
			});
		}
	},	
	rsGroupCount : function(obj)
	{		
		if ($('tr[data-parent=' + obj.data('index') + ']').length == 0 )
		{
			$('.btnPlusMinus[data-parent=' + obj.data('index') + ']').find('.plusminus').addClass('none');			
		}
		return obj.find('.rsGroupCount').text($('tr[data-parent=' + obj.data('index') + ']').length);			
//		return $('[data-index=' + index + '] .rsGroupCount').text($('tr[data-parent=' + index + ']').length);
	},
	rsGroupState : function(obj)
	{	
		var groupIndex = obj.data('index');
		  var state = $('.realServer[data-parent=' + groupIndex + ']:first .state span').text();
		  var isChildStateEqual = $('.realServer[data-parent=' + groupIndex + '] .state span').filter(function(){
		    return $(this).text() != state;
		  }).length < 1;
		  return isChildStateEqual ? state : '-';
		
		
//		var rsfisrtState = $('tr.realServer[data-parent= ' + obj.data('index') + ' ]:first').find('.state span').text();
//		var state = [];
//		for (var i= 0; i< $('tr.realServer[data-parent= ' + obj.data('index') + ' ]').size(); i++) 
//		{	
//			state[i] = $('tr.realServer[data-parent= ' + obj.data('index') + ' ]').find('.state span').eq(i).text();
//		}
		// realState 가 모두 같은경우
//		if ()
//		{
//			obj.find('.rsGroupState').text();
//		}
//		else
//		{
//			obj.find('.rsGroupState').text('-');
//		}
		
//		return obj.find('.rsGroupState').text($('tr.realServer[data-parent= ' + obj.data('index') + ' ]').find('.state span').text());	
	},	
	_avilableStateChk : function()
	{
		for (var i= 0; i< $('.nodeList').size(); i++) 
		{					
			if ($('.nodeList td').find('.notallowsize').eq(i).text() != 0)
			{
				$('.nodeList td').find('#node').eq(i).attr("disabled", "disabled");
				$('.nodeList td').find('#nodeState').eq(i).attr("disabled", "disabled");						
			}
			else if (($('.nodeList td').find('.allowsize').eq(i).text() == 0) &&
					($('.nodeList td').find('.notallowsize').eq(i).text() == 0))
			{
				$('.nodeList td').find('.imgChange').eq(i).attr("src", "imgs/icon/arrows_dis.png");
				
				$('.nodeList td').find('.imgChange').parent().eq(i).attr("isdetail", 2);
			}
			else
			{
				$('.nodeList td').find('#node').eq(i).removeAttr("disabled");
				$('.nodeList td').find('#nodeState').eq(i).removeAttr("disabled");
			}
			
		}
	},
	registerNodeListContentEvents : function() 
	{
		with (this) 
		{		
			$(document).click(function(e) {
//				$('.groupname_info').hide();
				$('.groupname_info').addClass('none');
				$('.rsGrpNmCancel:not(.none)').click();
			});
			
			$('.groupname_info').click(function(e)
			{
				e.stopPropagation();
			});
			
			$('input[name="searchKey"]').attr("placeholder", "Real Server IP, 이름");
						
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				orderGroupDir =  $(this).find('.orderGroupDir').text();
				orderGroupType =  $(this).find('.orderGroupType').text();
				var searchKey = $('input[name="textfield3"]').val();			
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType, orderGroupDir, orderGroupType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				orderGroupDir =  $(this).find('.orderGroupDir').text();
				orderGroupType =  $(this).find('.orderGroupType').text();
				var searchKey = $('input[name="textfield3"]').val();		
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType, orderGroupDir, orderGroupType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="textfield3"]').val();
				orderGroupDir =  $(this).find('.orderGroupDir').text();
				orderGroupType =  $(this).find('.orderGroupType').text();
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType, orderGroupDir, orderGroupType);
			});
			
			$('.vServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(0);
				virtualServer.loadListContent();
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
				loadListContent();
			});
			
			$('.noticeServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(3);
				noticeGrp.loadListContent();
			});
						
			$('.VSadmin_view').click(function(e)
			{
				var isdetail = $(this).attr("isdetail");
				if (isdetail == 0)
				{
					$(this).parent().next('div').show();
					$(this).attr("isdetail", "1");
					
					$(this).find('#imgChange').attr("src", "imgs/icon/arrows_up.png");
				}
				else if (isdetail == 1)
				{
					$(this).parent().next('div').hide();
					$(this).attr("isdetail", "0");
					
					$(this).find('#imgChange').attr("src", "imgs/icon/arrows_down.png");
				}
				else
				{
					
				}
			});
			
			$('.allNodeChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				
				for (var i= 0; i< $('.nodeList').size(); i++) 
				{
					if ($('tr.nodeList td').find('.notallowsize').eq(i).text() == 0)
					{
						$('tr.nodeList td').find('#node').eq(i).attr("checked", isChecked);	
						$('tr.Group td').find('.rsGrpChk').attr("checked", isChecked);
					}
					else
					{
						$('tr.nodeList td').find('#node').eq(i).removeAttr("checked");
						$('tr.Group td').find('.rsGrpChk').removeAttr("checked");
					}
				}
			});
			
			// RealServerGroup 추가
			$('.rsGroupAddLnk').click(function(e)
			{
				e.stopPropagation();
				$('.rsGrpNmCancel:not(.none)').click();
				$('.rsGrpName').removeClass('none');
//				$('.rsGrpName .rsGrpNm').removeClass('none');
//				$('.rsGrpName .rsGrpNmSave').removeClass('none');
//				$('.rsGrpName .rsGrpNmCancel').removeClass('none');
//				$('.rsGrpNmSave').removeClass('none');
//				$('.rsGrpNmCancel').removeClass('none');
				$('.rsGrpName .rsGrpNm').focus();
//				groupAddClickChangeCss();
				$('#rsGrpSelected tbody tr.rsGrpName').addClass("vsMonitorRowSelection");	
			});
			
			$('.rsGrpNm').keydown(function(e) 
			{
				if (e.which != 13)
					return;
								
				if($(this).parents('.rsGrpName').length > 0)
				{
					// 추가
					if (!valiateGrpName($('input[name="rsGrpNm"]')))
						return false;
					
					if ($('input[name="rsGrpNm"]').val() == "")
					{
						$.obAlertNotice(VAR_NODE_NAME_INPUT);
						return;
					}					
					groupSet(false);
				}					
				else
				{
					if (!valiateGrpName($('.Group').find('.rsGrpNm').not('.none')))
						return false;
					
					if ($('.Group').find('.rsGrpNm').not('.none').val() == "")	
					{
						$.obAlertNotice("1" + VAR_NODE_NAME_INPUT);
						return;
					}
					
					var grpIndex = $(this).parent().children('span:first').text();
					var grpName = $(this).val();
					groupSet(true, grpIndex, grpName);
				}						
			});
			
			$('.rsGrpNmSave').click(function()
			{
				if($(this).parents('.rsGrpName').length > 0)
				{
					// 추가
					if ($('input[name="rsGrpNm"]').val() == "")
					{
						$.obAlertNotice("2" + VAR_NODE_NAME_INPUT);
						return;
					}
					
					if (!valiateGrpName($('input[name="rsGrpNm"]')))
						return false;
					
					groupSet(false);
				}					
				else
				{
					// 수정
//					if ($('input[name="rsGrpModfiyNm"]').val() == "")
					if ($('.Group').find('.rsGrpNm').not('.none').val() == "")	
					{
						$.obAlertNotice("3" + VAR_NODE_NAME_INPUT);
						return;
					}
					
					if (!valiateGrpName($('.Group').find('.rsGrpNm').not('.none')))
						return false;
					
					
//					var grpIndex = $(this).parent().find('span:first').text();
//					var grpName = $(this).parent().find('input.rsGrpNm').val();
//					var grpIndex = $(this).parent().parent().data('index');
//					var grpName = $(this).parent().parent().find('.rsGrpNm').val();
					
					var grpIndex = $(this).parent().parent().parent().data('index');
					var grpName = $(this).parent().parent().parent().find('.rsGrpNm').val();
						
					groupSet(true, grpIndex, grpName);
				}	
			});
			
			$('.rsGrpNmCancel').click(function(e)
			{
				e.stopPropagation();
				if($(this).parents('.rsGrpName').length > 0)
				{
					// 추가
					$('.rsGrpName').addClass('none');
				}					
				else
				{
					// 수정
					$('.rsGrpName').addClass('none');
					$('.groupModify').addClass('none');
					$('.rsGrpNmOrg').removeClass('none');
				}	
				$(this).parent().parent().find('.conn').addClass('none').data('flag', false);
			});	
			
			$('#rsGrpSelected').click(function(e)
			{
				e.stopPropagation();
			});
			
			$('.Group').click(function(e)
			{				
				// Group TR Click
//				$('.rsGroupModifyLnk').removeClass('none').data('index', $(this).data('index')); // 클릭 시 수정버튼 활성화 하며 수정버튼에 인덱스 부여
//				console.log($('.rsGroupModifyLnk').data('index'));
//				$('.rsGroupDelLnk').removeClass('none').data('index', $(this).data('index'));
//				$(this).find('.conn').removeClass('none').data('flag', true);
				groupClickChangeCss($('.Group[data-index=' + $(this).data('index') + ']').data('index'));
			});
			
			// RealServerGroup 수정
//			$('.rsGroupAdd').off('click');
//			$('.rsGroupAdd').keydown(function(e)
//			{
//				groupSet(false);
//			});
			$('.Group .rsGroupModifyLnk').off('click');
			$('.Group .rsGroupModifyLnk').click(function(e)
			{
				e.stopPropagation();
				$('.rsGrpNmCancel:not(.none)').click();
//						alert($(this).data('index'));
//				$('.Group[data-index=' + $(this).data('index') + '] .rsGrpNmOrg').addClass('none'); // 부여된 인덱스를 통해 해당 그룹에 접근
//				$('.Group[data-index=' + $(this).data('index') + '] .groupModify').removeClass('none').data('index', undefined);
//				$('.Group[data-index=' + $(this).parent().parent().data('index') + '] .rsGrpNmOrg').addClass('none'); // 부여된 인덱스를 통해 해당 그룹에 접근
//				$('.Group[data-index=' +  $(this).parent().parent().data('index') + '] .groupModify').removeClass('none').data('index', undefined);
				
				$('.Group[data-index=' + $(this).parent().parent().parent().data('index') + '] .rsGrpNmOrg').addClass('none'); // 부여된 인덱스를 통해 해당 그룹에 접근
				$('.Group[data-index=' +  $(this).parent().parent().parent().data('index') + '] .groupModify').removeClass('none').data('index', undefined);
				$(this).parent().parent().find('.rsGrpNm').focus();
				
//				$(this).find('.imgbtn').addClass('none').data('index', $(this).data('index'));
				$(this).parent().find('.conn').addClass('none').data('flag', true);
				groupClickChangeCss($('.Group[data-index=' + $(this).parents('.Group').data('index') + ']').data('index'));
//				$('.Group[data-index=' + $(this).data('index') + '] .rsGrpNmSave').removeClass('none').data('index', undefined);
//				$('.Group[data-index=' + $(this).data('index') + '] .rsGrpNmCancel').removeClass('none').data('index', undefined);
				
//				$('#rsGrpNmModifySave').removeClass('none');
//				$('#rsGrpNmModifyCancel').removeClass('none');
			});
			
//			$('#rsGrpNmModifySave').not('none').click(function()
//			{
//				if ($('input[name="rsGrpModfiyNm"]').val() == "")
//				{
//					alert("RealServerGroup 이름을 입력하세요.");
//					return;
//				}
//				
//				var grpIndex = $(this).parent().children('span:first').text();
//				var grpName = $(this).val();
//				groupSet(true, grpIndex, grpName);
//			});
//			
//			$('.rsGrpNmModifyCancel').not('none').click(function()
//			{
//				$('.rsGrpName').addClass('none');
//				$('.rsGrpModifyNm').addClass('none');
//				$('.rsGrpNmModifySave').addClass('none');
//				$('.rsGrpNmModifyCancel').addClass('none');
//				$('.rsGrpNmOrg').removeClass('none');
//			});			
			
//			$('.groupModify').keydown(function(e) 
//			{
//				if (e.which != 13)
//					return;
//				
////				if ($('input[name="rsGrpNm"]').val() == "")
////				{
////					alert("RealServerGroup 이름을 입력하세요.");				
////					return;
////				}
//				
////				var grpIndex = $(this).children('span').text();
//				var grpIndex = $(this).parent().children('span:first').text();
//				var grpName = $(this).val();
//				groupSet(true, grpIndex, grpName);
//			});

			// RealServerGroup 삭제
			$('.Group .rsGroupDelLnk').off('click');
			$('.Group .rsGroupDelLnk').click(function(e)
			{
//				var grpIndex = $(this).parent().children('span:first').text();
//				var grpIndex = $('.Group').data('index');
//				var grpIndex = $('.Group[data-index=' + $(this).data('index') + ']').data('index');
//				var grpIndex = $('.Group[data-index=' + $(this).parent().parent().data('index') + ']').data('index');
				
				var grpIndex = $('.Group[data-index=' + $(this).parent().parent().parent().data('index') + ']').data('index');
				delRsGroup(grpIndex);
			});
			
			// RealServerGroup 이동			
			
			$('.rsGrpChk').click(function(e)
			{
				e.stopPropagation();
				// 그룹 선택 (포함된 rs 모두 선택)
				var isChecked = $(this).is(':checked');
				for (var i= 0; i< $('.realServer[data-parent=' + $(this).parent().parent().data('index') + ']').size(); i++) 
				{
					$('.realServer[data-parent=' + $(this).parent().parent().data('index') + ']').find('#node').eq(i).attr("checked", isChecked);
				}
			});				
			
			$('.rsGroupMoveLnk').off('click');
			$('.rsGroupMoveLnk').click(function(e)
			{
				// Group 이동 Popup
				e.preventDefault();
				e.stopPropagation();
//				$('.groupname_info').show();
				
				if($('.virtualSvrTable .Group').size() == 0)
				{
					$.obAlertNotice(VAR_NODE_GROUP_MOVENOT);
					return;
				}
				else
				{
					var checkedNodes = _getCheckedServers();
					
					if (checkedNodes.length == 0)
					{
						$.obAlertNotice(VAR_NODE_NOT_SELECTED);
						return;
					}
					$('.groupname_info').removeClass('none');
				}
			});
			
			$('#rsGrpSave').off('click');
			$('#rsGrpSave').click(function()
			{				
				// 기존의 group 에 속해있던 real은 기존 groupIndex 가 필요함, 신규로 그룹에 등록하는 경우는 null
//				var rsGrpOrgIndex = "";
				var rsGrpIndex = $('.layer_conts option:selected').val();
				rsGroupSave(rsGrpIndex);
			});
			
			$('#rsGrpCancel').off('click');
			$('#rsGrpCancel').click(function()
			{
//				$('#group_move').addClass('none');
				$('.groupname_info').addClass('none');
			});
			
			$('.btnPlusMinus').click(function(e)
			{
				e.stopPropagation();
				// + - 선택
				var value = $(this).attr("val");
				
				if (value == "plus")
				{
//					$('.plusminus').attr('src', "/imgs/layout/ico_minus.png");
//					$('.plusminus').parent().parent().data('index').attr('src', "/imgs/layout/ico_minus.png");
//					$('.plusminus[data-parent=' + $(this).parent().data('index') + ']').attr('src', "/imgs/layout/ico_minus.png");
					$('.btnPlusMinus[data-parent=' + $(this).parent().parent().data('index') + ']').children().attr('src', "/imgs/layout/minus.gif");
//					$('.realServer').removeClass('none').data('index', $(this).data('index'));
					$('.realServer[data-parent=' + $(this).parent().parent().data('index') + ']').removeClass('none');
					$(this).attr("val", "minus");
				}
				else
				{
//					$('.plusminus').attr('src', "/imgs/layout/ico_plus.png");
//					$('.plusminus').parent().parent().data('index').attr('src', "/imgs/layout/ico_plus.png");
//					$('.plusminus[data-parent=' + $(this).parent().data('index') + ']').attr('src', "/imgs/layout/ico_plus.png");
					$('.btnPlusMinus[data-parent=' + $(this).parent().parent().data('index') + ']').children().attr('src', "/imgs/layout/plus.gif");
//					$('.realServer').addClass('none').data('index', $(this).data('index'));
					$('.realServer[data-parent=' + $(this).parent().parent().data('index') + ']').addClass('none');
					$(this).attr("val", "plus");
				}	
				
//				$('.rsGroupModifyLnk').addClass('none');
//				$('.rsGroupDelLnk').addClass('none');
			});
			
			$('.Group').mouseover(function(e)
			{
				if (!$(this).find('.conn').data('flag'))
					$(this).find('.conn').removeClass('none');
			});
			
			$('.Group').mouseleave(function(e)
			{
				if (!$(this).find('.conn').data('flag'))
					$(this).find('.conn').addClass('none');
			});
			
			
//			var $rsGroupCount = $('.rsGroupCount').filter(':last');
//			$rsGroupCount.empty();
//			var html = ''; 		
//			var rsGroupCount = $('.realServer[data-parent=' + $('.Group').data('index') + ']').size();
//			for (int i=0; i < $('.Group').size(); i++)
//			{
//				if ($('.Group').data('index') ==  $('.realServer[data-parent=' + $('.Group').data('index') + ']'))
//				{
//					var $rsGroupCount = $('.rsGroupCount').filter(':last');
//					$rsGroupCount.empty();
//					var html = ''; 	
//					var rsGroupCount = $('.realServer[data-parent=' + $('.Group').data('index') + ']').size();
//					html += '<span class="rsGroupCount">' + rsGroupCount + '</span></span>';
//					$rsGroupCount.append(html);
//				}
//			}
			
			$('.enableNodeLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
			$('.enableNodeLnk').click(function(e)
			{
				setNodes(1);	
//				setNodes(1, function() {$('.realServer :checked').parents('.realServer').find('.state').text('Disable');})
			});
			$('.disableNodeLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.disableNodeLnk').click(function(e)
			{
				setNodes(0);
			});
			$('.forcedOffNodeLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.forcedOffNodeLnk').click(function(e)
			{
				setNodes(2);
			});
			
//			$('.enableNodeLnk').click(function(e)
//			{
//				e.preventDefault();
//				var $nodeTRs = _getCheckNodeTRs();
//				
//				if ($nodeTRs.length == 0)
//				{
//					alert(VAR_COMMON_EMSELECT);
//					return;
//				}
//				
//				$nodeTRs.find('td option[value="1"]').attr('selected', true);
//			});
//			
//			$('.disableNodeLnk').click(function(e)
//			{
//				e.preventDefault();
//				var $nodeTRs = _getCheckNodeTRs();
//				
//				if ($nodeTRs.length == 0)
//				{
//					alert(VAR_COMMON_DMSELECT);
//					return;
//				}
//				
//				$nodeTRs.find('td option[value="0"]').attr('selected', true);
//			});
//			
//			$('.forcedOffNodeLnk').click(function(e)
//			{
//				e.preventDefault();
//				var $nodeTRs = _getCheckNodeTRs();
//				
//				if ($nodeTRs.length == 0)
//				{
//					alert(VAR_COMMON_FORCEDOFFLINEMSELECT);
//					return;
//				}
//				
//				$nodeTRs.find('td option[value="2"]').attr('selected', true);
//			});
			
			$('.setNodeOkLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
			$('.setNodeOkLnk').click(function(e)
			{			
				var selectedNodeState = _getCheckedNodes();
				
//				if(stateChk(selectedNodeState, state) == false)
					setNodes();
			});
			
//			$('.enableNodeLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
//			$('.enableNodeLnk').click(function(e)
//			{			
//				var selectedNodeState = _getCheckedNodes();
//				
//				if(stateChk(selectedNodeState, 1) == false)
//					setNodes(1);				
//				var selectedNodeState = _getCheckedNodes();
//				
//				for ( var i = 0; i < selectedNodeState.length; i++) 
//				{
//					if($('#nodeVsCount tr.ContentsLine3.nodeList td #nodeState option').filter(':selected').eq(i).val() != 0)
//					{
//						alert("선택된 node에는 이미 enable 상태가 있습니다.");
//						return true;
//					}
//				}
				
//				for (var i= 0; i< $('.nodeList').size(); i++) 
//				{					
//					if ($('#nodeVsCount tr.ContentsLine3.nodeList td').find('.notallowsize').eq(i).text() != 0)
//					{
//						$('#nodeVsCount tr.ContentsLine3.nodeList td').find('#node').eq(i).attr("disabled", "disabled");
//						$('#nodeVsCount tr.ContentsLine3.nodeList td').find('#nodeState').eq(i).attr("disabled", "disabled");						
//					}
//					else
//					{				
//						$('#nodeVsCount tr.ContentsLine3.nodeList td').find('#node').eq(i).removeAttr("disabled");
//						$('#nodeVsCount tr.ContentsLine3.nodeList td').find('#nodeState').eq(i).removeAttr("disabled");
//					}
//				}
//			});
			
//			$('.disableNodeLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
//			$('.disableNodeLnk').click(function(e)
//			{
//				var selectedNodeState = _getCheckedNodes();
//				if(stateChk(selectedNodeState, 0) == false)
//					setNodes(0);
//			});
//			
//			$('.forcedOffNodeLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
//			$('.forcedOffNodeLnk').click(function(e)
//			{
//				var selectedNodeState = _getCheckedNodes();
//				if(stateChk(selectedNodeState, 2) == false)
//					setNodes(2);				
//			});
			
			// search event
//			$('p.cont_sch a.searchLnk').click(function (e) {
			$('.btn a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				log.debug('click:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
			});
			
			$('.inputTextposition1 input.searchTxt').keydown(function(e) 
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				log.debug('click:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
			});
			
//			if (adcSetting.getAdc().mode == 1)
//			{		
//				$('.imgOff').removeClass("none");
//				$('.imgOn').addClass("none");
//				
//				$('.allNodeChk').attr("disabled", "disabled");
//				$('.nodeChk').attr("disabled", "disabled");
//				
//				if ($('.nodeList').size() > 0 )
//				{
//					$('.disabledChk').removeClass("none");
//					$('.nulldataMsg').addClass("none");	   					
//				}
//				else
//				{
//					$('.disabledChk').addClass("none");
//					$('.nulldataMsg').removeClass("none");
//					$('input[name="searchKey"]').attr("disabled", "disabled");
//					$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
//				}	
//			}
//			else
//			{
//				$('.allNodeChk').removeAttr("disabled");
//				$('.nodeChk').removeAttr("disabled");
//			}
			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.nodeList').size() > 0)
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
				if($('.nodeList').size() > 0)
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
				
				$('.allNodeChk').attr("disabled", "disabled");
				$('.nodeChk').attr("disabled", "disabled");
				
				if ($('.nodeList').size() > 0 )
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
			
			$('#selectColumn_pop').click(function (e)
			{
				e.preventDefault();		
				node._loadSelColPop();			
			});
		}
	},	
	stateChk : function(selectedNodeState, state)
	{	
		for ( var i = 0; i < selectedNodeState.length; i++) 
		{
			var isChecked = $('#nodeVsCount tr.nodeList td').find('#node').eq(i).is(':checked');
			if (isChecked)
			{
				if(state == 1)
				{
//					if($('#nodeVsCount tr.ContentsLine3.nodeList td #nodeState option').filter(':selected').eq(i).val() == 1)
					if($('#nodeVsCount tr.nodeList td').find('#inputState').eq(i).val() == 1)	
					{
						$.obAlertNotice(VAR_NODE_SELECTED_ALREADY + " enabled " + VAR_NODE_STATUS_HAVE);
						return true;
					}

				}			
				else if(state == 0)
				{
//					if($('#nodeVsCount tr.ContentsLine3.nodeList td #nodeState option').filter(':selected').eq(i).val() == 0)
					if($('#nodeVsCount tr.nodeList td').find('#inputState').eq(i).val() == 0)
					{
						$.obAlertNotice(VAR_NODE_SELECTED_ALREADY + " disabled " + VAR_NODE_STATUS_HAVE);
						return true;
					}

				}
				else
				{
//					if($('#nodeVsCount tr.ContentsLine3.nodeList td #nodeState option').filter(':selected').eq(i).val() == 2)
					if($('#nodeVsCount tr.nodeList td').find('#inputState').eq(i).val() == 2)
					{
						$.obAlertNotice(VAR_NODE_SELECTED_ALREADY + " forced offline " + VAR_NODE_STATUS_HAVE);
						return true;
					}

				}
			}
		}
		return false;
	},
	_loadSelColPop : function()
	{
		with(this)
		{
			$('#selCol-popup table tbody').html('');
			$('#rsGrpSelected .ContentsHeadLine').find('th').each(function()			
			{
				$('#selCol-popup table tbody').append(
				['<tr>',
					 '<td class="align_center">',
						 '<input name="colChk" class="colChk" type="checkbox"' + ($(this).is('.default') ? 'checked="checked" disabled="disabled"' : ($(this).is('.none') ? '' : 'checked="checked"')) + '>',
					 '</td>',
					 '<td class="align_left_P11">',
					 	$(this).find('a').contents().filter(function(){return this.nodeType === 3;}).text().trim()
					 	+ $(this).contents().filter(function(){return this.nodeType === 3;}).text().trim(),
					 '</td>',
				 '</tr>'].join(''));
			});
			
			$('#selCol-popup .table_type11 tr:eq(1)').find('.colChk').parent().parent().css('display', 'none');						
			showPopup({
				'id' : '#selCol-popup',
				'width' : '400px'
			});
			
			_registerEvents();
			
		}
	},
	// TODO Add, Modify, Delete RSGroup
	groupSet : function(modify, index, name)
	{
		with (this)
		{
			actionUrl = ""; 
			grpName = "";
			if (modify)
			{
				actionUrl = "virtualServer/modifyRsGroup.action";
				grpName = name;
			}
			else
			{
				actionUrl = "virtualServer/addRsGroup.action";
				grpName = $('input[name="rsGrpNm"]').val();
			}
			
			ajaxManager.runJsonExt({
				url : actionUrl,
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"rsGroupIndex" : 	index,
					"rsGroupName" : grpName,
					"orderDir" : orderDir,
					"orderType" : orderType
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					else
					{
						$.obAlertNotice(data.message);
					}
//					loadListContent(searchedKey, false, orderDir, orderType);
					loadNodeListContent(searchedKey, this.orderType, this.orderDir); 
					$('.rsGroupModifyLnk').addClass('none');
					$('.rsGroupDelLnk').addClass('none');
				},
				errorFn : function(jqXhr)
				{
					if (modify)
					{
						$.obAlertAjaxError(VAR_NODE_GROUP_MODIFYFAIL, jqXhr);
					}
					else
					{
						$.obAlertAjaxError(VAR_NODE_GROUP_ADDFAIL, jqXhr);
					}
				}
			});
		}		
	},
	
	groupClickChangeCss : function(grpIndex)
	{
		$('#rsGrpSelected tbody tr.Group').removeClass("vsMonitorRowSelection");		
		$('#rsGrpSelected tbody tr.Group').each(function(index) {
//			if ($('#rsGrpSelected tbody tr.Group').data('index') === grpIndex)			
//			if ($('.btnPlusMinus[data-parent=' + $(this).parent().data('index') + ']') === grpIndex)
			if ($(this).data('index') === grpIndex)
			{
//				$(this).parent()..addClass("vsMonitorRowSelection");
//				$('#rsGrpSelected tbody tr.Group[data-index=' + grpIndex + ']').addClass('vsMonitorRowSelection');
				$(this).addClass('vsMonitorRowSelection');
				return false;

			}
		});
	},
	
	delRsGroup : function(index)
	{
		with (this)
		{
			var chk = confirm(VAR_NODE_GROUP_DEL);
			if (chk == false)
			{
				return;
			}
			
			ajaxManager.runJsonExt({
				url : "virtualServer/delRsGroup.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"rsGroupIndex" : 	index,
					"orderDir" : orderDir,
					"orderType" : orderType
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					else
					{
						$.obAlertNotice(data.message);
					}
//					loadListContent(searchedKey, false, orderDir, orderType);
					loadNodeListContent(searchedKey, this.orderType, this.orderDir); 
					$('.rsGroupModifyLnk').addClass('none');
					$('.rsGroupDelLnk').addClass('none');
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NODE_GROUP_DELFAIL, jqXhr);
				}
			});
		}		
	},
	rsGroupSave : function(index)
	{
		with (this)
		{
			var checkNodes = _getCheckedServers();
			
			if (checkNodes.length == 0)
			{
				$.obAlertNotice(VAR_NODE_NOT_SELECTED);
				return;
			}
			
			var chk = confirm(checkNodes.length + VAR_NODE_GROUP_FEWMOVE);
			if (chk)
			{
				ajaxManager.runJsonExt({
					url : "virtualServer/updateRsGroup.action",
					data : 
					{
						"adc.index" : adcSetting.getAdc().index,
						"nodeIndexList" : checkNodes,
						"nodeCheckInString" : Object.toJSON(_getCheckedNodes()),
						"rsGroupIndex" : index
					},
					successFn : function(data)
					{
						if(!data.isSuccessful)
						{
							$.obAlertNotice(data.message);
							return;
						}
						else
						{
							$.obAlertNotice(data.message);
						}
//						loadListContent(searchedKey, false, orderDir, orderType);
						loadNodeListContent(searchedKey, this.orderType, this.orderDir); 
						$('.rsGroupModifyLnk').addClass('none');
						$('.rsGroupDelLnk').addClass('none');
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_NODE_GROUP_MOVEFAIL, jqXhr);
					}
				});
			}
			else
			{
				return false;
			}
		}
	},	
//	setNodes : function(state, callback)
	setNodes : function(state)
	{
		with (this)
		{
			var checkedNodes = _getCheckedServers();
			var selectedNodeState = _getCheckedNodesStatus();
			
			var selectedNodeInfo = new Array();
		
			for(var i=0; i < selectedNodeState.length; i++)
			{
				var selectedIpStatus = " RSIP : " + selectedNodeState[i].ipaddress + " / State : " + selectedNodeState[i].stateVal + "\n";
				
				selectedNodeInfo.push(selectedIpStatus);
//				selectedNodeInfo.push(selectedNodeState[i].ipaddress + ' : ' + selectedNodeState[i].state);
			}
						
			if (checkedNodes.length == 0)
			{
				$.obAlertNotice(VAR_NODE_NOT_SELECTED);
				return;
			}
//			if(stateChk(checkedNodes, state))
//			{
//				return;
//			}
			
			var nodeSetState = "Enable";
			if (state == 0)
				nodeSetState = "Disable";
			else if (state == 1)
				nodeSetState = "Enable";
			else if (state == 2)
				nodeSetState = "Forced Offline";
			else
				nodeSetState = "Enable";
			
			var chk = confirm(selectedNodeInfo + " (" + checkedNodes.length + ")" + VAR_NODE_GROUP_STATUSMODIFY
					+ nodeSetState + VAR_NODE_GROUP_STATUSMODIFYS);
			if(chk) 
			{				
				ajaxManager.runJsonExt({
					url : "virtualServer/setNode.action",
					data : 
					{
						"adc.index" : adcSetting.getAdc().index,
						"adc.name" : adcSetting.getAdc().name,
						"adc.type" : adcSetting.getAdc().type,
						"nodeIndexList" : checkedNodes,
						"state" : state,
						"nodeCheckInString" : Object.toJSON(_getCheckedNodes()),
						"orderDir" : this.orderDir,
						"orderType" : this.orderType
					},
					target : "#wrap .contents_area",
					successFn : function(param)
					{	
						selectedRsIndexList = undefined;
						loadNodeListContent(searchedKey, this.orderType, this.orderDir);
//						if (callback)
//							callback(param);
//						else
//							loadNodeListContent(searchedKey, this.orderType, this.orderDir);
//						
//						if(state == 1)
//							$('.realServer :checked').parents('.realServer').find('.state').text('Enable');
//						else if(state == 0)
//							$('.realServer :checked').parents('.realServer').find('.state').text('Disable');
//						else if(state == 2)
//							$('.realServer :checked').parents('.realServer').find('.state').text('Forced Offline');
						
					},
					errorFn : function(jqXhr)
					{
						/*if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
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
//												return;
//												alert(data.message);
												alert(VAR_NODE_SYNC_STATUS_RESET + " - " + VAR_NODE_SYNC_COMPLETE_RESET);
												loadNodeListContent(searchedKey, this.orderType, this.orderDir);												
											}
											else
											{
//												pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
												$.obAlertNotice(data.message);						
												return;							
											}	
											
											selectedRsIndexList = undefined;
										}	
										else
										{
											alert(VAR_NODE_SYNC_COMPLETE_RESET);
											
											var rsIndexList = new Array();
											$('#rsGrpSelected tbody tr').find('.nodeChk:checked').each(function(index)
											{
												 rsIndexList.push($(this).val());
											});
												
											selectedRsIndexList = rsIndexList;
											
											loadNodeListContent(searchedKey, this.orderType, this.orderDir);
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
						}	*/	
						
						if (jqXhr.responseText.indexOf('Sync finished') > -1)
						{
//							$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
//							$.obAlertNotice(VAR_VS_VSTIMEERROR);
//							var chk = confirm(VAR_VS_VSTIMEERROR + "\n" + VAR_NODE_SYNC_PROGRESSING);
							alert(VAR_NODE_SYNC_STATUS_RESET + " - " + VAR_NODE_SYNC_COMPLETE_RESET);
							
							var rsIndexList = new Array();
							$('#rsGrpSelected tbody tr').find('.nodeChk:checked').each(function(index)
							{
								 rsIndexList.push($(this).val());
							});
								
							selectedRsIndexList = rsIndexList;
							
							loadNodeListContent(searchedKey, this.orderType, this.orderDir);	
						}
						else if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
						{
//							$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
//							$.obAlertNotice(VAR_VS_VSTIMEERROR);
//							var chk = confirm(VAR_VS_VSTIMEERROR + "\n" + VAR_NODE_SYNC_PROGRESSING);
							$.obAlertAjaxError(VAR_NODE_GROUP_SETFAIL, jqXhr);
							loadNodeListContent(searchedKey, this.orderType, this.orderDir);	
						}
						else
						{
							$.obAlertAjaxError(VAR_NODE_GROUP_SETFAIL, jqXhr);
							loadNodeListContent(searchedKey, this.orderType, this.orderDir);
						}
					}
				});
			}
			else 
			{
				return false;
			}
			
		}
	},
	_getCheckedNodes : function()
	{
		return  $('.nodeChk').filter(':checked').map(function() {
			var node_index = $(this).val();
			var node_ipaddress = $(this).parent().parent().find('td.ip').text();
			var node_name = $(this).parent().parent().find('td.name').text();
			var node_ratio = $(this).parent().parent().find('td.ratio').text();
			var node_state = $(this).parent().parent().find('td.state input').val();
			var node_stateVal = $(this).parent().parent().find('td.state span').text();
			
			// 기존의 group 에 속해있던 real은 기존 groupIndex 가 필요함, 신규로 그룹에 등록하는 경우는 null
//			var dataParent = $(this).parent().parent().attr('data-parent');
			
			var node_alteonID = $(this).parent().parent().find('td.session span').text();
			
			var node_groupIndex = "";
			
			if ($(this).parent().parent().attr('data-parent') != undefined)
				node_groupIndex = $(this).parent().parent().attr('data-parent');
			else
				node_groupIndex = -1;
			
//			var node_state = $(this).parent().parent().find('select').children('option').filter(':selected').val();
			return {
				"index" : node_index,
				"ipaddress" : node_ipaddress,
				"name" : node_name,
				"ratio" : node_ratio,
				"state" : node_state,
				"alteonID" : node_alteonID,
				"groupIndex" : node_groupIndex,
				"stateVal" : node_stateVal
			};
		}).get();		
	},
	_getCheckedServers : function()
	{
		var servers = $('.nodeChk').filter(':checked').map(function() {
			return $(this).val();
		}).get();
		FlowitUtil.log(Object.toJSON(servers));
		return servers;
	},
	_getCheckedNodesStatus : function()
	{
		return  $('.nodeChk').filter(':checked').map(function() {
			var node_index = $(this).val();
			var node_ipaddress = $(this).parent().parent().find('td.ip').text();
			var node_name = $(this).parent().parent().find('td.name').text();
			var node_ratio = $(this).parent().parent().find('td.ratio').text();
			var node_state = $(this).parent().parent().find('td.state input').val();
			var node_stateVal = $(this).parent().parent().find('td.state span').text();
			
			// 기존의 group 에 속해있던 real은 기존 groupIndex 가 필요함, 신규로 그룹에 등록하는 경우는 null
	//		var dataParent = $(this).parent().parent().attr('data-parent');
			
			var node_alteonID = $(this).parent().parent().find('td.session span').text();
			
			var node_groupIndex = "";
			
			if ($(this).parent().parent().attr('data-parent') != undefined)
				node_groupIndex = $(this).parent().parent().attr('data-parent');
			else
				node_groupIndex = -1;
			
	//		var node_state = $(this).parent().parent().find('select').children('option').filter(':selected').val();
			return {
				"index" : node_index,
				"ipaddress" : node_ipaddress,
				"name" : node_name,
				"ratio" : node_ratio,
				"state" : node_state,
				"alteonID" : node_alteonID,
				"groupIndex" : node_groupIndex,
				"stateVal" : node_stateVal
			};
		}).get();	
	},
	
	_getCheckNodeTRs : function()
	{
		return $('.selectedNode .nodeChk').filter(':checked').parent().parent();
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
		
		return true;	
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
	
	valiateGrpName : function(name)
	{
		if (!isValidStringLength(name.val(), 1, 37))
		{
			var data = VAR_COMMON_LENGTHFORMAT+"("+1+"~"+37+")";
			$.obAlertNotice(data);
			name.val('');
			return false;
		}

		if (!isExistSpecialCharacter(name.val()))
		{
			$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
			name.val('');
			return false;
		}	
		
		return true;
	}
});
