var SysSetting = Class
		.create({
			initialize : function() {
				this.isInitialized = false;
				this.$unassignedAdcOptions = $();
				this.$unassignedVsOptions = $();
				this.auditLog = new ClassAuditLog();
				this.pwChangeWnd = new PwChangeWnd();
				// this.systemInfo = new SystemInfo();
				this.sysBackup = new SysBackup();
				this.vsIndexList = undefined;
				this.rsIndexList = undefined;
				this.orderDir = 2; // 2는 내림차순
				this.orderType = 33;// id

				this.searchLastHours = 12;
				this.searchStartTime = undefined;
				this.searchEndTime = undefined;
				this.selectedSearchTimeMode = undefined;
				this.searchFlag = false;

			},
			loadLeftPane : function() {
				with (this) {
					isInitialized = true;
					header.setActiveMenu('SysSettingUser');
					applyLeftCss();
					registerLeftEvents();

					$('.snb').addClass('none');
					$('.snb_systools').addClass('none');
					$('.snb_system').removeClass('none');

					$('.listcontainer').css('background-color', '#2d3d5e');
					$('.pick').addClass('none');
					$('.pick_system').removeClass('none');
					$('.pick_systools').addClass('none');
					$('.adc_search').addClass('none');
					$('.adc_search_1').removeClass('none');
				}
			},
			applyLeftCss : function() {
				// SNB 높이 설정
				// $(window).resize(function()
				// {
				// $('.snb_system').height($(window).height() -
				// $('.snb_system').position().top);
				// });

				$('.snb_system .snb_tree p').click(function() {
					$('.snb_system .snb_tree p').each(function() {
						$(this).removeClass('on');
					});

					$(this).addClass('on');
				});

				// SNB Tree 클릭 이벤트
				$('.snb_system .snb_tree .depth1 > li > p a').click(
						function(e) {
							e.stopImmediatePropagation();
							var $sub_menu = $(this).parent().nextAll('ul');

							if ($sub_menu.css('display') == 'none') {
								over($(this));
								$sub_menu.slideDown(200);
							} else {
								out($(this));
								$sub_menu.slideUp(200);
							}
						});

				// SNB 메뉴 숨기기/보이기
				$('.snb_system .snb_close')
						.toggle(
								function(e) {
									$('.snb_system')
											.animate(
													{
														'left' : '-205px'
													},
													100,
													function() {
														$(
																'.snb_system .snb_close')
																.attr('class',
																		'snb_open')
																.children('img')
																.attr(
																		{
																			'src' : 'imgs/layout/btn_open.png',
																			'alt' : VAR_COMMON_MENUOPEN
																		});
														$('.content_wrap').css(
																'margin-left',
																'10px');
													});
								},
								function() {
									$('.snb_system')
											.animate(
													{
														'left' : '0px'
													},
													100,
													function() {
														$(
																'.snb_system .snb_open')
																.attr('class',
																		'snb_close')
																.children('img')
																.attr(
																		{
																			'src' : 'imgs/layout/btn_close.png',
																			'alt' : VAR_COMMON_MENUCLOSE
																		});
														$('.content_wrap').css(
																'margin-left',
																'215px');
													});
								});

				$(window).trigger('resize');

				// 백그라운드 이미지 제거
				// $('.depth2 > li > p').css({'background-image':'url()'});
			},
			registerLeftEvents : function() {
				with (this) {
					$('.userMgmtPgh111').click(function(e) {
						e.preventDefault();
						loadUserListContent();
					});

					$('.roleMgmtPgh').click(function(e) {
						e.preventDefault();
						loadRoleMgmtContent();
					});

					$('.sysMgmtPgh').click(function(e) {
						e.preventDefault();
						loadSysMgmtContent();
					});

					$('.vsFilterPgh').click(function(e) {
						e.preventDefault();
						vsFilterConfig.loadListContent();
					});

					$('.auditLogPgh').click(function(e) {
						e.preventDefault();
						auditLog.loadContent();
					});

					$('.sysBackupPgh').click(function(e) {
						e.preventDefault();
						sysBackup.loadListContent();
					});

					$('.systemInfoPgh').click(function(e) {
						e.preventDefault();
						systemInfo.loadSystemInfoContent();
					});

					$('.licensePgh').click(function(e) // 라이선스
					{
						e.preventDefault();
						license.loadLicenseContent();
					});

					$('.configPgh').click(function(e) // 환경설정
					{
						e.preventDefault();
						config.loadConfigContent();
					});

					$('.logContentPgh').click(function(e) // 장비 로그
					{
						e.preventDefault();
						logContentInfo.loadContent();
					});

					$('.responseTimePgh').click(function(e) // 구간 응답시간 Check
					{
						e.preventDefault();
						respTime.loadContent();
					});

					$('.productPgh').click(function(e) // 제품관리
					{
						e.preventDefault();
						product.loadProductContent();
					});
				}
			},
			loadContent : function(searchKey, orderType, orderDir) {
				with (this) {
					var accountRole = 'system';

					ajaxManager.runJsonExt({
						url : "base.action",
						successFn : function(data) {
							FlowitUtil.log('data: ' + Object.toJSON(data));
							accountRole = data.accountRole;
							if (accountRole != 'system') {
								loadUserModifyContent();
								return;
							}

							setLeftTreeToUserMgmt();
							ajaxManager.runHtmlExt({
								url : "sysSetting/loadUserListContent.action",
								data : {
									"searchKey" : searchKey,
									"orderType" : orderType,
									"orderDir" : orderDir
								},
								target : "#wrap .contents",
								successFn : function(params) {
									// header.setActiveMenu('SysSettingUser');
									// applyUserListContentCss();
									registerUserListContentEvents();
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(
											VAR_SYSSETTING_LOADUSERLISTCONTENT,
											jqXhr);
									// exceptionEvent();
								}
							});
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(
									VAR_SYSSETTING_LOADUSERLISTCONTENT, jqXhr);
							// exceptionEvent();
						}
					});
				}
			},
			loadUserListContent : function(searchKey, orderType, orderDir) {
				with (this) {
					var accountRole = 'system';

					ajaxManager.runJsonExt({
						url : "base.action",
						successFn : function(data) {
							FlowitUtil.log('data: ' + Object.toJSON(data));
							accountRole = data.accountRole;
							if (accountRole != 'system') {
								loadUserModifyContent();
								return;
							}

							setLeftTreeToUserMgmt();
							ajaxManager.runHtmlExt({
								url : "sysSetting/loadUserListContent.action",
								data : {
									"searchKey" : searchKey,
									"orderType" : orderType,
									"orderDir" : orderDir
								},
								target : "#wrap .contents",
								successFn : function(params) {
									header.setActiveMenu('SysSettingUser');
									// applyUserListContentCss();
									registerUserListContentEvents();
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(
											VAR_SYSSETTING_LOADUSERLISTCONTENT,
											jqXhr);
									// exceptionEvent();
								}
							});
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(
									VAR_SYSSETTING_LOADUSERLISTCONTENT, jqXhr);
							// exceptionEvent();
						}
					});
				}
			},
			/*
			 * applyUserListContentCss : function() { initTable([ "#table1 tbody
			 * tr"], [1,2,5,6], [-1]); },
			 */
			registerUserListContentEvents : function() {
				with (this) {
					$('.orderDir_Desc').click(function(e) {
						e.preventDefault();
						orderDir = $(this).find('.orderDir').text();
						orderType = $(this).find('.orderType').text();
						var searchKey = $('p.cont_sch input.searchTxt').val();
						FlowitUtil.log('click:' + searchKey);
						searchFlag = true;
						loadUserListContent(searchKey, orderDir, orderType);
					});

					$('.orderDir_Asc').click(function(e) {
						e.preventDefault();
						orderDir = $(this).find('.orderDir').text();
						orderType = $(this).find('.orderType').text();
						var searchKey = $('p.cont_sch input.searchTxt').val();
						FlowitUtil.log('click:' + searchKey);
						searchFlag = true;
						loadUserListContent(searchKey, orderDir, orderType);
					});

					$('.orderDir_None').click(function(e) {
						e.preventDefault();
						orderDir = $(this).find('.orderDir').text();
						orderType = $(this).find('.orderType').text();
						var searchKey = $('p.cont_sch input.searchTxt').val();
						FlowitUtil.log('click:' + searchKey);
						searchFlag = true;
						loadUserListContent(searchKey, orderDir, orderType);
					});

					$('.allUsersChk').click(
							function(e) {
								var isChecked = $(this).is(':checked');
								// $(this).parent().parent().parent().parent().find('.userChk').attr('checked',
								// isChecked);
								$(this).parents('table.Board').find('.userChk')
										.attr('checked', isChecked);
							});

					$('.accountIdLnk').click(
							function(e) {
								var accountIndex = $(this).parent().parent()
										.find('.userChk').val();
								loadUserModifyContent(accountIndex);
							});

					$('.addAccountLnk').click(function(e) {
						// $('roleId').attr('disabled', 'true');
						loadUserAddContent();
					});

					// $('.cloneAccountLnk').click(function(e)
					// {
					// cloneUserContent();
					// });

					$('.delAccountsLnk').click(function(e) {

						// var chk = confirm('계정을 삭제 하시겠습니까?');
						// if(chk) {
						// delAccounts();
						// }
						// else {
						// return false;
						// }
						delAccounts();
					});

					// search event
					// TODO
					$('p.cont_sch a.searchLnk')
							.click(
									function(e) {
										e.preventDefault();
										var searchKey = $(
												'p.cont_sch input.searchTxt')
												.val();
										if (($('.control_Board input.searchTxt')
												.val() != "")
												&& ($(
														'.control_Board input.searchTxt')
														.val() != null)) {
											if (!validateSearchKey()) {
												return false;
											}
										}
										FlowitUtil.log('click:' + searchKey);
										searchFlag = true;
										loadUserListContent(searchKey);
									});

					$('p.cont_sch input.searchTxt')
							.keydown(
									function(e) {
										if (e.which != 13) {
											return;
										}
										if (($('.control_Board input.searchTxt')
												.val() != "")
												&& ($(
														'.control_Board input.searchTxt')
														.val() != null)) {
											if (!validateSearchKey()) {
												return false;
											}
										}
										var searchKey = $(this).val();

										FlowitUtil.log('click:' + searchKey);
										searchFlag = true;
										loadUserListContent(searchKey);
									});

					if (this.searchFlag == true) {
						$('.dataNotExistMsg').addClass("none");
						if ($('.userList').size() > 0) {
							$('.searchNotMsg').addClass("none");
						} else {
							$('.searchNotMsg').removeClass("none");
						}
						searchFlag = false;
					} else {
						$('.searchNotMsg').addClass("none");
						if ($('.userList').size() > 0) {
							$('.dataNotExistMsg').addClass("none");
						} else {
							$('.dataNotExistMsg').removeClass("none");
						}
					}
				}
			},
			loadUserAddContent : function() {
				with (this) {
					ajaxManager.runHtmlExt({
						url : "sysSetting/loadUserAddContent.action",
						target : "#wrap .contents",
						successFn : function(params) {
							$unassignedAdcOptions = $();
							displayAdcAssignmentByRole($(
									'select[name="account.roleId"]').val());
							registerUserAddContentEvents(false);
							restoreUserAddContent();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(
									VAR_SYSSETTING_LOADUSERADDCONTENT, jqXhr);
						}
					});
				}
			},
			/*
			 * cloneUserContent : function() { var accountIndices =
			 * _getCheckedAccountIndices(); if (accountIndices.length == 0) {
			 * $.obAlertNotice(VAR_SYSSETTING_ACCOUNTDELSEL); return; }
			 * 
			 * var chk = confirm(VAR_SYSSETTING_ACCOUNTDEL);
			 * 
			 * if (chk == false) { return; }
			 * 
			 * ajaxManager.runJsonExt({ url : "sysSetting/cloneUser.action",
			 * data : { "accountIndices" : accountIndices }, successFn :
			 * function(data) { FlowitUtil.log(Object.toJSON(data)); if
			 * (!data.isSuccessful) { $.obAlertNotice(data.message); return; }
			 * 
			 * loadUserListContent(); },
			 * 
			 * errorFn : function(jqXhr) {
			 * $.obAlertAjaxError(VAR_SYSSETTING_ACCOUNTDELFAIL, jqXhr); } }); },
			 */
			checkRegisteredVsForSubmit : function() {
				var vsIndex = "";
				var selectedVsIndex = "";
				$("#selectedList").find(".availableVs").each(function(i) {
					selectedVsIndex = $(this).attr("id");
					// alert(selectedVsIndex);
					if (vsIndex != "")
						vsIndex += "|";
					vsIndex += selectedVsIndex;
					// alert(vsIndex);
				});

				return vsIndex;
			},
			checkRegisteredRsForSubmit : function() {
				var rsIndex = "";
				var selectedRsIndex = "";
				$("#selectedRsList").find('.availableRs').each(function(i) {
					selectedRsIndex = $(this).attr("id");
					if (rsIndex != "")
						rsIndex += "|";

					rsIndex += selectedRsIndex;
				});

				return rsIndex;
			},
			UserAddOrModifySubmit : function(isModify) // ajaxSubmit를 함수로 따로
			// 만듦. junhyun.ok_GS
			{
				with (this) {
					var vsIndex = checkRegisteredVsForSubmit();
					var rsIndex = checkRegisteredRsForSubmit();
					// alert(checkRegisteredVsForSubmit());

					var params = "";

					
					// var params =
					// {
					// "vsIndexList" : vsIndex,
					// "rsIndexList" : rsIndex
					// };

					if (isModify) {
						params = {
							"vsIndexList" : vsIndex,
							"rsIndexList" : rsIndex
						};
					} else {
						params = {
							"vsIndexList" : vsIndex,
							"rsIndexList" : rsIndex,
							// "account.roleId" :
							// $('select[name="account.roleId"]
							// option:selected').val()
							"roleIdVal" : $(
									'select[name="account.roleId"] option:selected')
									.val()
						};
					}

					$('#userAddFrm')
							.ajaxSubmit(
									{
										dataType : 'json',
										url : isModify ? 'sysSetting/modifyUser.action'
												: 'sysSetting/addUser.action',
										data : params,
										success : function(data) {
											FlowitUtil.log(Object.toJSON(data));
											if (data.isSuccessful) {
												$('.loginIdLnk')
														.find('span')
														.filter(':last')
														.text(
																$(
																		'input[name="account.phone"]')
																		.val());
												$
														.obAlertNotice(VAR_COMMON_REGISUCCESS);
												if (data.accountRole == 'system')
													loadUserListContent();
											} else {
												$.obAlertNotice(data.message);
											}
										},
										error : function(jqXhr) {
											$
													.obAlertAjaxError(
															VAR_SYSSETTING_USERADDMODIFY,
															jqXhr);
										}
									});
				}
			},
			registerUserAddContentEvents : function(isModify) {
				with (this) {
					$('#userAddFrm')
							.submit(
									function() // 아이디 중복 체크 때문에 변경
									// junhyun.ok_GS
									{
										selectRegisteredAdcsForSubmit();

										if (!isModify) // modify이 false 즉, 사용자를
										// 추가할 때
										{// 계정 추가시 validateUserAdd
											if (!validateUserAdd())
												return false;

											var userId = $(
													'input[name="account.id"]')
													.val();
											ajaxManager
													.runJsonExt // 사용자가 입력한 ID
													// 중복 체크
													({
														url : "sysSetting/UserIdCheck.action",
														data : {
															"account.id" : userId
														},
														successFn : function(
																data) {
															if (!data.isSuccessful) { // 리턴값이
																// false면
																// ID
																// 중복
																$
																		.obAlertNotice(VAR_SYSSETTING_ACCOUNTIDCHECK);
															} else { // 리턴값이
																// true면
																// ID 중복
																// 아님.
																UserAddOrModifySubmit(false);
															}
														}
													});
										} else // modify가 true 즉, 사용자를 수정할 때
										{// 계정 수정시 validateUserModify
											if (!validateUserModify())
												return false;

											UserAddOrModifySubmit(true);
										}
										// always return false to prevent
										// standard browser submit and page
										// navigation
										return false;
									});

					/*
					 * //alert("isModify : " + isModify); if (isModify) {
					 * //alert("account.roleId : " +
					 * $('select[name="account.roleId"]').val());
					 * 
					 * $('#roleId').attr('disabled', 'true');
					 * 
					 * if ($('select[name="account.roleId"]').val() == '1') {
					 * 
					 * $('#adcChk').attr('disabled', 'true'); } else {
					 * $('#roleId').removeAttr("disabled");
					 * $('#adcChk').removeAttr("disabled"); } } else //
					 * $('#roleId').attr('disabled', ''); //alert("roleId add : " +
					 * roleId); $('#roleId').removeAttr("disabled");
					 */
					$('.alert-type-btn').change(function(e) {
						var checkedVal = $(this).val();

						if (checkedVal == 1) {
							$('#usesAlertBeep').removeAttr("disabled");
						} else {
							$('#usesAlertBeep').attr("disabled", "disalbed");
							$('#usesAlertBeep').removeAttr("checked");
						}
					});

					$('.resetPassword').click(
							function(e) {
								e.preventDefault();
								if (!confirm(VAR_SYSSETTING_PASSWDRESET))
									return;

								resetAccountPassword($(
										'input[name="account.index"]').val());
							});
					$('.popUpChangePasswdWndLnk').click(function(e) {
						e.preventDefault();
						pwChangeWnd.popUp();
					});

					$('.selectableEmailDomain').change(
							function(e) {
								FlowitUtil.log($(this).val());
								$('input[name="account.emailDomain"]').val(
										$(this).val());
							});

					// onRoleChanged
					$('select[name="account.roleId"]').change(function(e) {
						displayAdcAssignmentByRole($(this).val());
					});

					// $('#roleId').attr('disabled', 'true');
					/*
					 * $('#roleId').change(function() { roleId = $('#roleId
					 * option:selected').val(); alert("roleId ADD : " + roleId);
					 * });
					 */

					$('.toAdcSelectionLnk').click(function(e) {
						e.preventDefault();
						moveAdcsToSelection();
					});

					$('.toAdcDeselectionLnk').click(function(e) {
						e.preventDefault();
						moveAdcsToDeselection();
					});

					$('.userAddCancelLnk').click(function(e) {
						e.preventDefault();
						loadUserListContent();
					});

					$('.userAddOkLnk')
							.click(
									function(e) {
										FlowitUtil
												.log(' '
														+ $(
																'input[name="account.id"]:first')
																.val()
														+ $(
																'select[name="account.adcVsList.adcIndex"]:first')
																.val()
														+ $(
																'select[name="account.roleId"]')
																.val());
										e.preventDefault();
										$('#userAddFrm').submit();
									});

					// search event
					$('.btn1 .adcSearchLnk').click(
							function(e) {
								e.preventDefault();
								var searchKey = $(
										'.inputTextposition1 .adcSearchTxt')
										.val();
								FlowitUtil.log('click:' + searchKey);
								_searchOnUnassignedAdcs(searchKey);
							});

					$('.inputTextposition1 .adcSearchTxt').keydown(function(e) {
						if (e.which != 13)
							return;

						var searchKey = $(this).val();
						FlowitUtil.log('click:' + searchKey);
						_searchOnUnassignedAdcs(searchKey);
					});
					$('.btn1 .adcSearchLnk1').click(
							function(e) {
								e.preventDefault();
								// var searchKey = $('.inputTextposition1
								// .adcSearchTxt').val();

								var searchKey = $(
										'.inputTextposition1 .adcSearchTxt1')
										.val();

								FlowitUtil.log('click:' + searchKey);
								_searchOnUnassignedVs(searchKey);
							});

					$('.inputTextposition1 .adcSearchTxt1').keydown(
							function(e) {
								if (e.which != 13)
									return;

								var searchKey = $(this).val();
								FlowitUtil.log('click:' + searchKey);
								_searchOnUnassignedVs(searchKey);
							});

					$('.accntClone')
							.change(
									function(e) {
										e.preventDefault();
										var selectedAccntIndex = $(
												'select[name="accountClone"] option:selected')
												.val();

										loadUserRoleModifyContent(selectedAccntIndex);
									});

					// right-list 에서 vs 선택시 left-selected list 에 ADC가 없는 경우
					// 우선 ADC를 create 후 선택된 vs move
					// move 된 vs 는 right-list 에서 remove

					// 선택된 Node 가져오는 방법
					// $("#unSelectedList").jstree("get_selected");

					$("#toVsDeselectionLnk").click(function() {
						// rs 가 포함되어 잇는 경우 deselect X
						if (isValidAdcCheck()) {
							$.obAlertNotice(VAR_SYSSETTING_NOVSADC);
							moveAdcsToDeselection();
							return false;
						}

						moveAdcsToDeselection();
					});

					$("#toVsSelectionLnk")
							.click(
									function() {
										// var checked_ids = [];

										// class server (right list) 중에서 체크된것만
										// 찾아온다.
										$("#unSelectedList .availableAdc")
												.find(".jstree-checked")
												.each(
														function(i) {
															// right list 에 있는
															// adc 정보 (id, name)
															var org_availableAdc_id = $(
																	this)
																	.parent()
																	.parent()
																	.attr("id");
															var org_availableAdc_nm = $(
																	this)
																	.parent()
																	.parent()
																	.attr("nm");
															// alert(org_availableAdc_id);
															// right list 에 있는
															// virtual server 벙보
															// (id, name)
															var org_availableVs_id = $(
																	this).attr(
																	"id");
															var org_availableVs_nm = $(
																	this).attr(
																	"nm");
															// checked_ids.push(src_server_id
															// + "%%" +
															// src_server_nm);

															var split = org_availableVs_id
																	.split("_"); // vs
															// 정보는
															// (adcIndex_vsIndex)
															var adcIndex = split[0]; // adcIndex

															var isExist = false;
															var availableAdc_id = "";
															var availableAdc_nm = "";
															// right list 에 있는
															// adc 가 left list 에
															// 있는지 체크.
															$("#selectedList")
																	.find(
																			".availableAdc")
																	.each(
																			function(
																					i) {
																				availableAdc_id = $(
																						this)
																						.attr(
																								"id"); // left에
																				// 존재하는
																				// id
																				// alert("availableAdc_id
																				// : "
																				// +
																				// availableAdc_id);
																				availableAdc_nm = $(
																						this)
																						.attr(
																								"nm");

																				// alert("adcIndex
																				// : "
																				// +
																				// adcIndex);
																				if (availableAdc_id == adcIndex) //
																				{
																					isExist = true;
																					return false;
																				}
																			});

															// left list 에 adc 가
															// 없으면 adc 추가.
															if (false == isExist) {
																availableAdc_id = org_availableAdc_id;
																availableAdc_nm = org_availableAdc_nm;
																// alert(availableAdc_id);
																// alert(availableAdc_nm);
																// root 노드를 하나
																// 만든다.
																$(
																		"#selectedList")
																		.jstree(
																				"create",
																				-1,
																				"first",
																				{
																					attr : {
																						id : availableAdc_id,
																						nm : availableAdc_nm,
																						'class' : "availableAdc"
																					},
																					data : availableAdc_nm
																				},
																				false,
																				true);
																// $("#selectedList").jstree("create",null,"first",{attr:{id:org_availableAdc_id,
																// nm:org_availableAdc_nm,
																// class:"availableAdc"},
																// data:org_availableAdc_nm},
																// false, true);
															}

															isExist = false;
															var availableVs_id = "";
															// var
															// availableVs_nm =
															// "";

															// left list 에 존재하는
															// adc 에 vitual
															// server 가 있는지 체크.
															$(
																	"#selectedList #"
																			+ availableAdc_id)
																	.find(
																			".availableVs")
																	.each(
																			function(
																					i) {
																				availableVs_id = $(
																						this)
																						.attr(
																								"id");
																				// server_nm
																				// =
																				// $(this).attr("nm");

																				if (availableVs_id == org_availableVs_id) {
																					isExist = true;
																					return false;
																				}
																			});

															// left list 에
															// virtual server 가
															// 없으면 virtual
															// server 추가.
															if (false == isExist) { // left
																// list에
																// 추가한다.
																$(
																		"#selectedList")
																		.jstree(
																				"create",
																				"#"
																						+ availableAdc_id,
																				"first",
																				{
																					attr : {
																						id : org_availableVs_id,
																						nm : org_availableVs_nm,
																						'class' : "availableVs"
																					},
																					data : org_availableVs_nm
																				},
																				false,
																				true);
															}
														});

										// right list 에서 선택된 것들은 삭제.
										$("#unSelectedList").find(
												".jstree-checked").each(
												function(i) {
													$(this).remove();
												});

										moveAdcsToSelection();

									});

					$("#toRsDeselectionLnk")
							.click(
									function() {
										$("#selectedRsList .availableAdc")
												.find(".jstree-checked")
												.each(
														function(i) {
															// left list 에 있는
															// adc 정보 (id, name)
															var org_availableAdc_id = $(
																	this)
																	.parent()
																	.parent()
																	.attr("id");
															var org_availableAdc_nm = $(
																	this)
																	.parent()
																	.parent()
																	.attr("nm");
															// alert(org_availableAdc_id);

															// left list 에 있는
															// virtual Sever 정보
															// (id, name)
															var org_availableVs_id = $(
																	this).attr(
																	"id");
															var org_availableVs_nm = $(
																	this).attr(
																	"nm");
															// alert(org_availableVs_id);
															// // 24_51

															var split = org_availableVs_id
																	.split("_");
															var adcIndex = split[0];

															var isExist = false;
															var availableAdc_id = "";
															var availableAdc_nm = "";

															// left list 에 있는
															// adc 가 right list
															// 에 있는지 체크.
															$(
																	"#unSelectedRsList")
																	.find(
																			".availableAdc")
																	.each(
																			function(
																					i) {
																				availableAdc_id = $(
																						this)
																						.attr(
																								"id");
																				availableAdc_nm = $(
																						this)
																						.attr(
																								"nm");

																				// alert(availableAdc_id);
																				if (availableAdc_id == adcIndex) {
																					// alert(availableAdc_id);
																					// alert(adcIndex);
																					isExist = true;
																					return false;
																				}
																			});

															// right list 에 adc
															// 가 없으면 adc 추가.
															if (false == isExist) {
																availableAdc_id = org_availableAdc_id;
																availableAdc_nm = org_availableAdc_nm;
																$(
																		"#unSelectedRsList")
																		.jstree(
																				"create",
																				-1,
																				"first",
																				{
																					attr : {
																						id : availableAdc_id,
																						nm : availableAdc_nm,
																						'class' : "availableAdc"
																					},
																					data : availableAdc_nm
																				},
																				false,
																				true);
															}

															isExist = false;
															var availableVs_id = "";

															$(
																	"#unSelectedRsList #"
																			+ availableAdc_id)
																	.find(
																			".availableRs")
																	.each(
																			function(
																					i) {
																				availableVs_id = $(
																						this)
																						.attr(
																								"id");
																				// alert("1
																				// : "
																				// +
																				// availableVs_id);

																				if (availableVs_id == org_availableVs_id) {
																					isExist = true;
																					return false;
																				}
																			});

															if (false == isExist) {
																$(
																		"#unSelectedRsList")
																		.jstree(
																				"create",
																				"#"
																						+ availableAdc_id,
																				"first",
																				{
																					attr : {
																						id : org_availableVs_id,
																						nm : org_availableVs_nm,
																						'class' : "availableRs"
																					},
																					data : org_availableVs_nm
																				},
																				false,
																				true);
															}
														});

										$("#selectedRsList").find(
												".jstree-checked").each(
												function(i) {
													$(this).remove();
												});

										moveAdcsToDeselection();
									});

					$("#toRsSelectionLnk")
							.click(
									function() {
										if (!isValidAdcChk()) {
											$
													.obAlertNotice(VAR_SYSSETTING_NORSADC);
											return false;
										}

										// var checked_ids = [];

										// class server (right list) 중에서 체크된것만
										// 찾아온다.
										$("#unSelectedRsList .availableAdc")
												.find(".jstree-checked")
												.each(
														function(i) {
															// right list 에 있는
															// adc 정보 (id, name)
															var org_availableAdc_id = $(
																	this)
																	.parent()
																	.parent()
																	.attr("id");
															var org_availableAdc_nm = $(
																	this)
																	.parent()
																	.parent()
																	.attr("nm");
															// alert(org_availableAdc_id);
															// right list 에 있는
															// virtual server 벙보
															// (id, name)
															var org_availableVs_id = $(
																	this).attr(
																	"id");
															var org_availableVs_nm = $(
																	this).attr(
																	"nm");
															// checked_ids.push(src_server_id
															// + "%%" +
															// src_server_nm);

															var split = org_availableVs_id
																	.split("_"); // vs
															// 정보는
															// (adcIndex_vsIndex)
															var adcIndex = split[0]; // adcIndex

															var isExist = false;
															var availableAdc_id = "";
															var availableAdc_nm = "";
															// right list 에 있는
															// adc 가 left list 에
															// 있는지 체크.
															$("#selectedRsList")
																	.find(
																			".availableAdc")
																	.each(
																			function(
																					i) {
																				availableAdc_id = $(
																						this)
																						.attr(
																								"id"); // left에
																				// 존재하는
																				// id
																				// alert("availableAdc_id
																				// : "
																				// +
																				// availableAdc_id);
																				availableAdc_nm = $(
																						this)
																						.attr(
																								"nm");

																				// alert("adcIndex
																				// : "
																				// +
																				// adcIndex);
																				if (availableAdc_id == adcIndex) //
																				{
																					isExist = true;
																					return false;
																				}
																			});

															// left list 에 adc 가
															// 없으면 adc 추가.
															if (false == isExist) {
																availableAdc_id = org_availableAdc_id;
																availableAdc_nm = org_availableAdc_nm;
																// alert(availableAdc_id);
																// alert(availableAdc_nm);
																// root 노드를 하나
																// 만든다.
																$(
																		"#selectedRsList")
																		.jstree(
																				"create",
																				-1,
																				"first",
																				{
																					attr : {
																						id : availableAdc_id,
																						nm : availableAdc_nm,
																						'class' : "availableAdc"
																					},
																					data : availableAdc_nm
																				},
																				false,
																				true);
																// $("#selectedList").jstree("create",null,"first",{attr:{id:org_availableAdc_id,
																// nm:org_availableAdc_nm,
																// class:"availableAdc"},
																// data:org_availableAdc_nm},
																// false, true);
															}

															isExist = false;
															var availableVs_id = "";
															// var
															// availableVs_nm =
															// "";

															// left list 에 존재하는
															// adc 에 vitual
															// server 가 있는지 체크.
															$(
																	"#selectedRsList #"
																			+ availableAdc_id)
																	.find(
																			".availableRs")
																	.each(
																			function(
																					i) {
																				availableVs_id = $(
																						this)
																						.attr(
																								"id");
																				// server_nm
																				// =
																				// $(this).attr("nm");

																				if (availableVs_id == org_availableVs_id) {
																					isExist = true;
																					return false;
																				}
																			});

															// left list 에
															// virtual server 가
															// 없으면 virtual
															// server 추가.
															if (false == isExist) { // left
																// list에
																// 추가한다.
																$(
																		"#selectedRsList")
																		.jstree(
																				"create",
																				"#"
																						+ availableAdc_id,
																				"first",
																				{
																					attr : {
																						id : org_availableVs_id,
																						nm : org_availableVs_nm,
																						'class' : "availableRs"
																					},
																					data : org_availableVs_nm
																				},
																				false,
																				true);
															}
														});

										// right list 에서 선택된 것들은 삭제.
										$("#unSelectedRsList").find(
												".jstree-checked").each(
												function(i) {
													$(this).remove();
												});

										moveAdcsToSelection();

									});

					$(".unSelectedList").jstree(
							{
								"themes" : {
									"theme" : [ "classic" ]
								},
								"ui" : {
								// "initially_select" : [ "rhtml_2" ]
								},
								"core" : {
									"initially_open" : [ "adcGroup" ]
								},
								"plugins" : [ "themes", "html_data", "ui",
										"crrm", "checkbox", "sort" ]
							});

					$(".selectedList").jstree(
							{
								"themes" : {
									"theme" : "classic",
									"dots" : true,
									"icons" : true
								},
								"plugins" : [ "themes", "html_data", "ui",
										"crrm", "checkbox", "sort" ]
							});
				}
			},
			isValidAdcCheck : function() {
				return $('#selectedList .availableAdc')
						.filter(
								function() {

									// if (!$(this).is(':checked'))
									// if
									// (!$(this).parent().find('.availableAdc').hasClass('jstree-checked'))
									// if (!$('#selectedList
									// ul').filter(':first').find('.availableAdc').hasClass('jstree-checked'))
									if (!$(this).hasClass('jstree-checked')
											&& !$(this).find('li').hasClass(
													'jstree-checked')) {
										return false;
									} else {
										if ($(
												'#selectedRsList li[id="'
														+ $(this).attr('id')
														+ '"]').attr('id') == $(
												this).attr('id')) {
											return $(
													'#selectedRsList .availableAdc[nm="'
															+ $(this)
																	.attr('nm')
															+ '"]').size() > 0;
										} else {
											// $("#selectedList
											// .availableAdc").find(".jstree-checked").each(function(i)
											$(this)
													.find(".jstree-checked")
													.each(
															function(i) {
																// left list 에
																// 있는 adc 정보
																// (id, name)
																var org_availableAdc_id = $(
																		this)
																		.parent()
																		.parent()
																		.attr(
																				"id");
																var org_availableAdc_nm = $(
																		this)
																		.parent()
																		.parent()
																		.attr(
																				"nm");
																// alert(org_availableAdc_id);

																// left list 에
																// 있는 virtual
																// Sever 정보 (id,
																// name)
																var org_availableVs_id = $(
																		this)
																		.attr(
																				"id");
																var org_availableVs_nm = $(
																		this)
																		.attr(
																				"nm");
																// alert(org_availableVs_id);
																// // 24_51

																var split = org_availableVs_id
																		.split("_");
																var adcIndex = split[0];

																var isExist = false;
																var availableAdc_id = "";
																var availableAdc_nm = "";

																// left list 에
																// 있는 adc 가
																// right list 에
																// 있는지 체크.
																$(
																		"#unSelectedList")
																		.find(
																				".availableAdc")
																		.each(
																				function(
																						i) {
																					availableAdc_id = $(
																							this)
																							.attr(
																									"id");
																					availableAdc_nm = $(
																							this)
																							.attr(
																									"nm");

																					// alert(availableAdc_id);
																					if (availableAdc_id == adcIndex) {
																						// alert(availableAdc_id);
																						// alert(adcIndex);
																						isExist = true;
																						return false;
																					}
																				});

																// right list 에
																// adc 가 없으면 adc
																// 추가.
																if (false == isExist) {
																	availableAdc_id = org_availableAdc_id;
																	availableAdc_nm = org_availableAdc_nm;
																	$(
																			"#unSelectedList")
																			.jstree(
																					"create",
																					-1,
																					"first",
																					{
																						attr : {
																							id : availableAdc_id,
																							nm : availableAdc_nm,
																							'class' : "availableAdc"
																						},
																						data : availableAdc_nm
																					},
																					false,
																					true);
																}

																isExist = false;
																var availableVs_id = "";

																$(
																		"#unSelectedList #"
																				+ availableAdc_id)
																		.find(
																				".availableVs")
																		.each(
																				function(
																						i) {
																					availableVs_id = $(
																							this)
																							.attr(
																									"id");
																					// alert("1
																					// : "
																					// +
																					// availableVs_id);

																					if (availableVs_id == org_availableVs_id) {
																						isExist = true;
																						return false;
																					}
																				});

																if (false == isExist) {
																	$(
																			"#unSelectedList")
																			.jstree(
																					"create",
																					"#"
																							+ availableAdc_id,
																					"first",
																					{
																						attr : {
																							id : org_availableVs_id,
																							nm : org_availableVs_nm,
																							'class' : "availableVs"
																						},
																						data : org_availableVs_nm
																					},
																					false,
																					true);
																}
															});

											$("#selectedList")
													.find(".jstree-checked")
													.each(
															function(i) {
																if ($(
																		'#selectedRsList')
																		.find(
																				'.availableAdc')
																		.attr(
																				'id') != $(
																		this)
																		.attr(
																				'id')
																		.split(
																				'_')[0])
																	$(this)
																			.remove();
															});

											// $("#selectedList").find(".jstree-checked").each(function(i)
											// $(this).find(".jstree-checked").each(function(i)
											// {
											// if($('#selectedRsList li[id="' +
											// $(this).attr('id')
											// +'"]').attr('id') !=
											// $(this).attr('id'))
											// $(this).remove();
											// });
											//					
											// $("#selectedList").find(".jstree-checked").each(function(i)
											// {
											// if($('#selectedRsList li[id="' +
											// $(this).attr('id')
											// +'"]').attr('id') !=
											// $(this).parent().parent().attr('id'))
											// $(this).remove();
											// });
										}
									}
								}).size() > 0;

				// var selectedAdcSize = $('#selectedList
				// .availableAdc').filter(function(){
				// return $('#selectedRsList .availableAdc[nm="' +
				// $(this).attr('nm') + '"]').size() > 0;
				// }).size();
				//		
				// if (selectedAdcSize > 0)
				// return true;
				// else
				// return false;
			},
			isValidAdcChk : function() {
				var selectedAdcSize = $('#unSelectedRsList .availableAdc')
						.filter(
								function() {
									return $(
											'#selectedList .availableAdc[nm="'
													+ $(this).attr('nm') + '"]')
											.size() > 0;
								}).size();

				if (selectedAdcSize > 0)
					return true;
				else
					return false;
			},
			validateSearchKey : function() {
				var search = $('.control_Board input.searchTxt').val();

				if (!isValidStringLength(search, 1, 64)) {
					var data = VAR_COMMON_LENGTHFORMAT + "(" + 1 + "~" + 64
							+ ")";
					$.obAlertNotice(data);
					$('.control_Board input.searchTxt').val('');
					return false;
				}

				if (!isExistSpecialCharacter(search)) {
					$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
					$('.control_Board input.searchTxt').val('');
					return false;
				}

				// if(getValidateStringint(search, 1, 64) == false)
				// {
				// alert(VAR_FAULTSETTING_SPECIALCHAR);
				// $('.control_Board input.searchTxt').val('');
				// // $('.control_Board input.searchTxt').focus();
				// return false;
				// }
				/*
				 * if(getValidateStringint(search, 1, 64) == "charErr") {
				 * alert(VAR_FAULTSETTING_SPECIALCHAR + " : special"); return
				 * false; } if(getValidateStringint(search, 1, 64) ==
				 * "RangeErr") { alert(VAR_FAULTSETTING_SPECIALCHAR + " :
				 * legnth"); return false; }
				 */
				// alert(VAR_FAULTSETTING_SPECIALCHAR);
				// return false;
				return true;
			},
			validateUserAdd : function() {
				var data = {
						"userId" : $('input[name="account.id"]').val(),
						"userName" : $('input[name="account.name"]').val(),
						"userPhoneNumb" : $('input[name="account.phone"]').val(),
						"userPassword" : $('input[name="account.password"]').val()
				};
				
				var idName = $('input[name="account.id"]').val();
				if (idName == '') {
					$.obAlertNotice(VAR_SYSSETTING_IDINPUT);
					return false;
				}
				if (idName != '') // 중복된 아이디 체크 추가 junhyun.ok_GS
				{

				}
				if (getValidateID(idName) == false) {
					$.obAlertNotice(VAR_SYSSETTING_IDRULEWRONG);
					return false;
				}

				var password = $('input[name="account.password"]').val();
				if (password == '') {
					$.obAlertNotice(VAR_SYSSETTING_PASSWDINPUT);
					return false;
				} else if (getValidatePasswd(data) == false) {
					$.obAlertNotice(VAR_SYSSETTING_PASSWDRULEWRONG);
					return false;
				}
				
				var confirmPassword = $('input[name="account.confirmPassword"]')
						.val();
				if (confirmPassword == '') {
					$.obAlertNotice(VAR_SYSSETTING_PASSWDCONFIRMINPUT);
					return false;
				} else if (confirmPassword != password) {
					$.obAlertNotice(VAR_SYSSETTING_PASSWDMATCH);
					return false;
				}

				if (password != confirmPassword) {
					$.obAlertNotice(VAR_SYSSETTING_PASSWDMATCH);
					return false;
				}

				var ipFilter = $('input[name="account.ipFilter"]').val();
				// if (ipFilter == '')
				// {
				// $.obAlertNotice(VAR_COMMON_IPINPUT);
				// }
				// else
				if (ipFilter != '') {
					if (getValidateIPMode(ipFilter) == false) {
						$.obAlertNotice(VAR_COMMON_IPFORMAT);
						return false;
					}
				}
				// var name = $('input[name="account.name"]').val();
				// if(name !='')
				// {
				// if(getValidateStringint(name, 0, 64) == false)
				// {
				// alert(VAR_COMMON_SPECIALCHAR);
				// return false;
				// }
				// }

				var userName = $('input[name="account.name"]').val();
				if (userName == '') {
					$.obAlertNotice(VAR_SYSSETTING_USERNAMEINPUT);
					return false;
				}

				if (!$('input[name="account.name"]').validate(
						{
							name : $('input[name="account.name"]').parent()
									.parent().find('li').text(),
							type : "name"
						})) {
					return false;
				}

				var email = $('input[name="account.emailBeforeDomain"]').val()
						+ "@" + $('input[name="account.emailDomain"]').val();
				if ($('input[name="account.emailBeforeDomain"]').val() != ''
						|| $('input[name="account.emailDomain"]').val() != '') {
					if (getValidateEmail(email) == false) {
						$.obAlertNotice(VAR_COMMON_EMAILFORMAT);
						return false;
					}
				}

				// var phone = $('input[name="account.phone"]').val();
				// if(phone !='')
				// {
				// if(getValidatePhone(phone) == false)
				// {
				// alert(VAR_COMMON_PHONEFORMAT);
				// return false;
				// }
				// }
				if (!$('input[name="account.phone"]').validate(
						{
							phonenum : $('input[name="account.phone"]')
									.parent().parent().find('li').text(),
							type : "phonenum"
						})) {
					return false;
				}

				// from GS. #4012-1 #10, #3926-4 #17: 14.07.29 sw.jung:
				// obvalidation 활용 개선
				// var description = $(".contents_area .valdescription").val();
				// if(description !='')
				// {
				// if(getValidateStringint(description, 0, 512) == false)
				// {
				// alert(VAR_COMMON_SPECIALCHAR);
				// return false;
				// }
				// }
				if (!$(".contents_area .valdescription").validate(
						{
							name : $(".contents_area .valdescription").parent()
									.parent().find('li').text(),
							type : "name",
							lengthRange : [ 0, 512 ]
						})) {
					return false;
				}

				if ($('input[id="LimitedMode"]').is(':checked')) {
					if (!$.trim($('input[name="account.startTime"]').val())) {
						$.obAlertNotice(VAR_COMMON_STATRTDATE);
						return false;
					}

					if (!$.trim($('input[name="account.endTime"]').val())) {
						$.obAlertNotice(VAR_COMMON_ENDDATE);
						return false;
					}

					if ($('input[name="account.startTime"]').val() > $(
							'input[name="account.endTime"]').val()) {
						$.obAlertNotice(VAR_COMMON_DATEERROR);
						return false;
					}
					return true;
				}

				// from GS. #4012-1 #10, #3926-4 #20: 14.07.29 sw.jung: VSadmin
				// VS할당여부 검사
				// if ($('select[name="account.roleId"]').val() == 4 &&
				// $('#selectedList').length > 0 &&
				// $('#selectedList').find('ul').text() == '')
				if (($('select[name="account.roleId"]').val() == 4 || $(
						'select[name="account.roleId"]').val() == 5)
						&& $('#selectedList').length > 0
						&& $('#selectedList').find('ul').text() == '')

				{
					$.obAlertNotice(VAR_CONFIG_VSSELECT);
					return false;
				}

				return true;
			},
			validateUserModify : function() {
				if ($('input[id="LimitedMode"]').is(':checked')) {
					if (!$.trim($('input[name="account.startTime"]').val())) {
						$.obAlertNotice(VAR_COMMON_STATRTDATE);
						return false;
					}

					if (!$.trim($('input[name="account.endTime"]').val())) {
						$.obAlertNotice(VAR_COMMON_ENDDATE);
						return false;
					}

					if ($('input[name="account.startTime"]').val() > $(
							'input[name="account.endTime"]').val()) {
						$.obAlertNotice(VAR_COMMON_DATEERROR);
						return false;
					}
					return true;
				}

				// var name = $('input[name="account.name"]').val();
				// if(name !='')
				// {
				// if(getValidateStringint(name, 0, 64) == false)
				// {
				// alert(VAR_COMMON_SPECIALCHAR);
				// return false;
				// }
				// }

				if (!$('input[name="account.name"]').validate(
						{
							name : $('input[name="account.name"]').parent()
									.parent().find('li').text(),
							type : "name"
						})) {
					return false;
				}

				var email = $('input[name="account.emailBeforeDomain"]').val()
						+ "@" + $('input[name="account.emailDomain"]').val();
				if ($('input[name="account.emailBeforeDomain"]').val() != ''
						|| $('input[name="account.emailDomain"]').val() != '') {
					if (getValidateEmail(email) == false) {
						$.obAlertNotice(VAR_COMMON_EMAILFORMAT);
						return false;
					}
				}

				// var $email =
				// $('input[name="account.emailBeforeDomain"]').val() + "@" +
				// $('input[name="account.emailDomain"]').val();
				// if (!$email.validate(
				// {
				// email: $email.parent().parent().find('li').text(),
				// type: "eamil"
				// }))
				// {
				// return false;
				// }

				// var phone = $('input[name="account.phone"]').val();
				// if(phone !='')
				// {
				// if(getValidatePhone(phone) == false)
				// {
				// alert(VAR_COMMON_PHONEFORMAT);
				// return false;
				// }
				// }
				if (!$('input[name="account.phone"]').validate(
						{
							phonenum : $('input[name="account.phone"]')
									.parent().parent().find('li').text(),
							type : "phonenum"
						})) {
					return false;
				}
				// from GS. #4012-1 #10, #3926-4 #17: 14.07.29 sw.jung:
				// obvalidation 활용 개선
				// var description = $(".contents_area
				// .valmoddescription").val();
				// if(description !='')
				// {
				// if(getValidateStringint(description, 0, 512) == false)
				// {
				// alert(VAR_COMMON_SPECIALCHAR);
				// return false;
				// }
				// }
				if (!$(".contents_area .valmoddescription").validate(
						{
							name : $(".contents_area .valmoddescription")
									.parent().parent().find('li').text(),
							type : "name",
							lengthRange : [ 0, 512 ]
						})) {
					return false;
				}

				// from GS. #4012-1 #10, #3926-4 #20: 14.07.29 sw.jung: VSadmin
				// VS할당여부 검사
				// if ($('select[name="account.roleId"]').val() == 4 &&
				// $('#selectedList').length > 0 &&
				// $('#selectedList').find('ul').text() == '')
				if (($('select[name="account.roleId"]').val() == 4 || $(
						'select[name="account.roleId"]').val() == 5)
						&& $('#selectedList').length > 0
						&& $('#selectedList').find('ul').text() == '') {
					$.obAlertNotice(VAR_CONFIG_VSSELECT);
					return false;
				}

				return true;
			},
			resetAccountPassword : function(accountIndex) {
				with (this) {
					FlowitUtil.log('accountIndex:', accountIndex);
					if (accountIndex === undefined)
						return;

					ajaxManager.runJsonExt({
						url : "sysSetting/resetAccountPassword.action",
						data : {
							"account.index" : accountIndex
						},
						successFn : function(data) {
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								return;
							}
							$.obAlertNotice(VAR_SYSSETTING_PASSWDRESET);
							loadUserListContent();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSSETTING_PASSWDRESETFAIL,
									jqXhr);
						}
					});
				}
			},
			displayAdcAssignmentByRole : function(roleId) {
				// roleId
				// SystemAdmin :1 ,ConfigAdmin : 2, ReadOnly : 3 , VSAdmin : 4 ,
				// RSAdmin : 5
				FlowitUtil.log('roleId: ' + roleId);
				var $adcAssignment = $('.adcAssignment');
				var $adcVsAssignment = $('.adcVsAssignment');
				var $adcRsAssignment = $('.adcRsAssignment');
				// if (roleId == '1')
				// {
				// $adcAssignment.hide();
				// }
				// else
				// {
				// $adcAssignment.show();
				// }

				if (roleId == '1') {
					$adcAssignment.hide();
					$adcVsAssignment.hide();
					$adcRsAssignment.hide();
				} else if (roleId == '4') {
					$adcAssignment.hide();
					$adcVsAssignment.show();
					$adcRsAssignment.hide();
				} else if (roleId == '5') {
					$adcAssignment.hide();
					$adcVsAssignment.show();
					$adcRsAssignment.show();
				} else {
					$adcAssignment.show();
					$adcVsAssignment.hide();
					$adcRsAssignment.hide();
				}
			},
			selectRegisteredAdcsForSubmit : function() {
				$('.adcsSelectedSel > option').attr('selected', true);
			},

			// checkRegisteredVsForSubmit : function()
			// {
			// $("#selectedList").find(".jstree-checked").each(function(i)
			// {
			// $('#selectedList').attr('checked',true);
			// });
			// },

			/*
			 * _getChkConfirm : function() { var chk = confirm('계정을 삭제
			 * 하시겠습니까?'); if (chk) { return true; } else { return false; } },
			 */

			delAccounts : function() {
				with (this) {
					var accountIndices = _getCheckedAccountIndices();
					if (accountIndices.length == 0) {
						$.obAlertNotice(VAR_SYSSETTING_ACCOUNTDELSEL);
						return;
					}

					// var chkConfirm = _getChkConfirm();

					var chk = confirm(VAR_SYSSETTING_ACCOUNTDEL);

					if (chk == false) {
						return;
					}

					ajaxManager.runJsonExt({
						url : "sysSetting/delAccounts.action",
						data : {
							"accountIndices" : accountIndices
						},
						successFn : function(data) {
							FlowitUtil.log(Object.toJSON(data));
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								return;
							}

							loadUserListContent();
						},

						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSSETTING_ACCOUNTDELFAIL,
									jqXhr);
						}
					});
				}
			},
			_getCheckedAccountIndices : function() {
				return $('.userChk').filter(':checked').map(function() {
					return $(this).val();
				}).get();
			},
			loadRoleMgmtContent : function() {
				FlowitUtil.log('-- loadAdcAddContent');
				ajaxManager.runHtmlExt({
					url : "sysSetting/loadRoleMgmtContent.action",
					target : "#wrap .contents",
					successFn : function(params) {
					},
					errorFn : function(a, b, c) {
						exceptionEvent();
					}
				});
			},
			loadSysMgmtContent : function() {
				ajaxManager.runHtmlExt({
					url : "sysSetting/loadSysMgmtContent.action",
					target : "#wrap .contents",
					successFn : function(params) {

					},
					errorFn : function(jqXhr) {
						$
								.obAlertAjaxError(
										VAR_SYSSETTING_SYSTEMINFOLOAD, jqXhr);
						// exceptionEvent();
					}
				});
			},
			moveAdcsToSelection : function() {
				with (this) {
					var $adcsSelected = $('.adcsSelectedSel');
					var $adcsDeselected = $('.adcsDeselectedSel');

					var $option = $adcsDeselected.children(':selected');
					FlowitUtil.log($option.size());
					if ($option.size() > 0)
						$adcsSelected.append($option);

					showSelectedAdcsCount();
				}
			},
			showSelectedAdcsCount : function() {
				// $('.selectedAdcsCount').text($('.adcsSelectedSel').children().length
				// + '개 선택됨');
				var selectedSize;
				if ($('select[name="account.roleId"]').val() == '4') {
					selectedSize = $('#selectedList').children().find(
							'.availableAdc').length;
					$('.selectedAdcsCount').text(selectedSize);
				} else if ($('select[name="account.roleId"]').val() == '5') {
					selectedSize = $('#selectedRsList ul')
							.find('.availableAdc').length;
					$('.selectedAdcRsCount').text(selectedSize);
					$('.selectedAdcsCount')
							.text(
									$('#selectedList').children().find(
											'.availableAdc').length);
				} else {
					selectedSize = $('.adcsSelectedSel').children().length;
					$('.selectedAdcsCount').text(selectedSize);
				}
			},
			moveAdcsToDeselection : function() {
				with (this) {
					var $adcsSelected = $('.adcsSelectedSel');
					var $adcsDeselected = $('.adcsDeselectedSel');

					var $option = $adcsSelected.children(':selected');
					FlowitUtil.log($option.size());
					if ($option.size() > 0)
						$adcsDeselected.append($option);

					showSelectedAdcsCount();
				}
			},
			_searchOnUnassignedAdcs : function(searchKey) {
				with (this) {
					var $adcsDeselectedSel = $('.adcsDeselectedSel');
					if (!searchKey) {
						$adcsDeselectedSel.append($unassignedAdcOptions);
						$unassignedAdcOptions = $();
						return;
					}

					_fillCbxWithSearchedAndSaveUnsearched(searchKey,
							$adcsDeselectedSel);
				}
			},
			_fillCbxWithSearchedAndSaveUnsearched : function(searchKey,
					$adcsDeselectedSel) {
				with (this) {
					$unassignedAdcOptions = $unassignedAdcOptions
							.add($adcsDeselectedSel.children().detach());
					var keyInLowerCase = searchKey.toLowerCase();
					FlowitUtil.log('keyInLowerCase: ' + keyInLowerCase);
					var $unsearchedOptions = $();
					$unassignedAdcOptions.each(function() {
						var index = $(this).text().toLowerCase().indexOf(
								keyInLowerCase);
						FlowitUtil.log('index: ' + index);
						if (index == -1)
							$unsearchedOptions = $unsearchedOptions
									.add($(this));
						else
							$adcsDeselectedSel.append($(this));
					});

					$unassignedAdcOptions = $unsearchedOptions;
				}
			},
			_searchOnUnassignedVs : function(searchKey) {
				with (this) {
					var $vsDeselectedSel = $('#unSelectedList');
					if (!searchKey) {
						$vsDeselectedSel.append($unassignedVsOptions);
						$unassignedVsOptions = $();
						return;
					}

					_fillVsBoxWithSearchedAndSaveUnsearched(searchKey,
							$vsDeselectedSel);
				}
			},
			_fillVsBoxWithSearchedAndSaveUnsearched : function(searchKey,
					$vsDeselectedSel) {
				with (this) {
					$unassignedVsOptions = $unassignedVsOptions
							.add($vsDeselectedSel.children().detach());
					var keyInLowerCase = searchKey.toLowerCase();
					FlowitUtil.log('keyInLowerCase: ' + keyInLowerCase);
					var $unsearchedOptions = $();
					$unassignedVsOptions.each(function() {
						var index = $(this).text().toLowerCase().indexOf(
								keyInLowerCase);
						FlowitUtil.log('index: ' + index);
						if (index == -1) {
							$unsearchedOptions = $unsearchedOptions
									.add($(this));
						} else {
							$vsDeselectedSel.append($(this));
							$.obAlertNotice($vsDeselectedSel.append($(this)));
							FlowitUtil.log('222222222222222222222: '
									+ $vsDeselectedSel.append($(this)));
						}

					});
					$unassignedVsOptions = $unsearchedOptions;
				}
			},
			loadUserModifyContent : function(accountIndex) {
				with (this) {
					setLeftTreeToUserMgmt();
					ajaxManager
							.runHtmlExt({
								url : "sysSetting/loadUserModifyContent.action",
								target : "#wrap .contents",
								data : {
									"account.index" : accountIndex
								},
								successFn : function(params) {
									header.setActiveMenu('SysSetting');
									$unassignedAdcOptions = $();
									displayAdcAssignmentByRole($(
											'select[name="account.roleId"]')
											.val());
									registerUserAddContentEvents(true);
									restoreUserAddContent();
									// loadUserRoleModifyContent(accountIndex);
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(
											VAR_COMMON_SYSTEMSETFAIL, jqXhr);
									// exceptionEvent();
								}
							});
				}
			},
			loadUserRoleModifyContent : function(accountIndex) {
				with (this) {
					setLeftTreeToUserMgmt();
					ajaxManager
							.runHtmlExt({
								url : "sysSetting/loadUserRoleModifyContent.action",
								target : "#wrap .userRoleArea",
								data : {
									"account.index" : accountIndex
								},
								successFn : function(parmas) {
									header.setActiveMenu('SysSetting');
									$unassignedAdcOptions = $();
									// 선택한 권한에 따른 권한 값을 알아야함.
									loadAccntCloneRole(accountIndex);
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(
											VAR_COMMON_SYSTEMSETFAIL, jqXhr);
									// exceptionEvent();
								}
							});
				}
			},
			loadAccntCloneRole : function(accountIndex) {
				with (this) {
					ajaxManager
							.runJsonExt({
								url : "sysSetting/loadAccntCloneRole.action",
								data : {
									"account.index" : accountIndex
								},
								successFn : function(data) {
									if (!data.isSuccessful) {
										$.obAlertNotice(data.message);
										return;
									}

									$('select[name="account.roleId"]').val(
											data.account.roleId);

									displayAdcAssignmentByRole(data.account.roleId);
									$('select[name="account.roleId"]').attr(
											"disabled", "disabled")

									registerUserAddContentEvents(false);
									restoreUserAddContent();
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(
											VAR_COMMON_SYSTEMSETFAIL, jqXhr);
								}
							});
				}
			},
			restoreUserAddContent : function() {
				with (this) {
					if (!searchStartTime) {
						searchStartTime = new Date();
					}
					if (!searchEndTime) {
						searchEndTime = new Date();
					}
					$('input[name="account.startTime"]').datepicker({
						// maxDate: "0",
						dateFormat : "yy-mm-dd",
						showOn : "button",
						buttonImage : "imgs/meun/btn_calendar.png",
						buttonImageOnly : true,
						defaultDate : searchStartTime,
						onSelect : function(dateText, inst) {
							$('#LimitedMode').attr('checked', 'checked');
						}
					});

					$('input[name="account.endTime"]').datepicker({
						// maxDate: "0",
						dateFormat : "yy-mm-dd",
						showOn : "button",
						buttonImage : "imgs/meun/btn_calendar.png",
						buttonImageOnly : true,
						defaultDate : searchEndTime,
						onSelect : function(dateText, inst) {
							$('#LimitedMode').attr('checked', 'checked');
						}
					});
					$('input[name="account.accountMode"]').change(
							function() {
								var unLimitedMode = $(
										'input[id="unLimitedMode"]').is(
										':checked');
								if (unLimitedMode == true) {
									$('input[name="account.startTime"]')
											.val("");
									$('input[name="account.endTime"]').val("");
								}
								unLimitedMode = null;
							});
				}
			},
			setLeftTreeToUserMgmt : function() {
				$('.snb_system .snb_tree p').each(function() {
					$(this).removeClass('on');
				});

				$('.snb_system .snb_tree p.userMgmtPgh').addClass('on');
			},
			loadSystemInfoContent : function() {
				with (this) {
					FlowitUtil.log("-- loadSystemInfoContent");
					systemInfo.loadSystemInfoContent();
				}
			}
		});

// 시스템 백업 페이지
var SysBackup = Class
		.create({
			initialize : function() {
				var fn = this;
				this.searchedOption = {
					"searchKey" : undefined,
					"startTimeL" : undefined,
					"endTimeL" : undefined
				};
				this.searchStartTime = undefined;
				this.searchEndTime = undefined;
				this.orderDir = 2; // 2는 내림차순
				this.orderType = 11;// 11은 occurTime
				this.searchFlag = false;
				this.pageNavi = new PageNavigator();
				this.pageNavi
						.onChange(function(fromRow, toRow, orderType, orderDir) {
							FlowitUtil
									.log(
											'fromRow: %s, toRow: %s, orderType: %s, orderDir: %s',
											fromRow, toRow, orderType, orderDir);
							fn.loadBackupTableInListContent(fn.searchedOption,
									fromRow, toRow, orderType, orderDir);
						});
			},
			loadListContent : function(searchOption, orderType, orderDir) {
				with (this) {
					if (!validateDaterefresh()) {
						return false;
					}

					FlowitUtil.log(
							"searchOption: %s, orderType: %s, orderDir: %s",
							searchOption, orderType, orderDir);
					var rowTotal = 0;
					ajaxManager
							.runJsonExt({
								url : "sysSetting/backup/retrieveSysBackupsTotal.action",
								data : {
									"searchKey" : searchOption ? searchOption.searchKey
											: undefined,
									"startTimeL" : searchOption ? searchOption.startTimeL
											: undefined,
									"endTimeL" : searchOption ? searchOption.endTimeL
											: undefined
								},
								successFn : function(data) {
									if (!data.isSuccessful) {
										$.obAlertNotice(data.message);
									} else {
										rowTotal = data.rowTotal;
									}

									FlowitUtil.log("row total is %s: ",
											rowTotal);
									pageNavi
											.updateRowTotal(rowTotal, orderType);
									loadBackupListContent(searchOption);
									header.setActiveMenu('SysSettingBackup');
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(VAR_SYSBACK_EXTRACTION,
											jqXhr);
								}
							});
				}
			},
			loadBackupListContent : function(searchOption, fromRow, toRow) {
				with (this) {
					FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s",
							searchOption, fromRow, toRow);
					ajaxManager
							.runHtmlExt({
								url : "sysSetting/backup/loadListContent.action",
								data : {
									"fromRow" : fromRow === undefined ? pageNavi
											.getFirstRowOfCurrentPage()
											: fromRow,
									"toRow" : toRow === undefined ? pageNavi
											.getLastRowOfCurrentPage() : toRow,
									"searchKey" : searchOption ? searchOption.searchKey
											: undefined,
									"startTimeL" : searchOption ? searchOption.startTimeL
											: undefined,
									"endTimeL" : searchOption ? searchOption.endTimeL
											: undefined,
									"orderDir" : this.orderDir,
									"orderType" : this.orderType
								},
								target : "#wrap .contents",
								successFn : function(params) {
									noticePageInfo();
									searchedOption = searchOption;
									_applyListContentCss();
									_registerListContentEvents();
								},
								completeFn : function() {
									pageNavi.refresh();
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(VAR_SYSBACK_LOAD, jqXhr);
								}
							});
				}
			},
			loadBackupTableInListContent : function(searchOption, fromRow,
					toRow, orderType, orderDir) {
				with (this) {
					FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s",
							searchOption, fromRow, toRow);
					ajaxManager
							.runHtmlExt({
								url : "sysSetting/backup/loadSysBackupTableInListContent.action",
								data : {
									"fromRow" : fromRow === undefined ? pageNavi
											.getFirstRowOfCurrentPage()
											: fromRow,
									"toRow" : toRow === undefined ? pageNavi
											.getLastRowOfCurrentPage() : toRow,
									"searchKey" : searchOption ? searchOption.searchKey
											: undefined,
									"startTimeL" : searchOption ? searchOption.startTimeL
											: undefined,
									"endTimeL" : searchOption ? searchOption.endTimeL
											: undefined,
									"orderDir" : this.orderDir,
									"orderType" : this.orderType
								},
								target : "table.backupTable",
								successFn : function(params) {
									noticePageInfo();
									searchedOption = searchOption;
									_applyListContentCss();
									_registerListContentEvents();
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(VAR_SYSBACK_LOAD, jqXhr);
								}
							});
				}
			},
			_applyListContentCss : function() {
				// 테이블 컬럼 정렬
				initTable([ "#table1 tbody tr" ], [ 3, 4, 5, 6 ], [ 7 ]);
			},
			_registerListContentEvents : function() {
				with (this) {
					if (this.searchFlag == true) {
						$('.dataNotExistMsg').addClass("none");
						if ($('.backupList').size() > 0) {
							$('.searchNotMsg').addClass("none");
						} else {
							$('.searchNotMsg').removeClass("none");
						}
						searchFlag = false;
					} else {
						$('.searchNotMsg').addClass("none");
						if ($('.backupList').size() > 0) {
							$('.dataNotExistMsg').addClass("none");
						} else {
							$('.dataNotExistMsg').removeClass("none");
						}

					}

					$('.orderDir_Desc').click(
							function(e) {
								e.preventDefault();
								orderDir = $(this).find('.orderDir').text();
								orderType = $(this).find('.orderType').text();
								var option = {
									"searchKey" : $(
											'.control_Board input.searchTxt')
											.val(),
									"startTimeL" : searchStartTime,
									"endTimeL" : searchEndTime
								};
								searchFlag = true;
								loadListContent(option, orderDir, orderType);
							});

					$('.orderDir_Asc').click(
							function(e) {
								e.preventDefault();
								orderDir = $(this).find('.orderDir').text();
								orderType = $(this).find('.orderType').text();
								var option = {
									"searchKey" : $(
											'.control_Board input.searchTxt')
											.val(),
									"startTimeL" : searchStartTime,
									"endTimeL" : searchEndTime
								};
								searchFlag = true;
								loadListContent(option, orderDir, orderType);
							});

					$('.orderDir_None').click(
							function(e) {
								e.preventDefault();
								orderDir = $(this).find('.orderDir').text();
								orderType = $(this).find('.orderType').text();
								var option = {
									"searchKey" : $(
											'.control_Board input.searchTxt')
											.val(),
									"startTimeL" : searchStartTime,
									"endTimeL" : searchEndTime
								};
								searchFlag = true;
								loadListContent(option, orderDir, orderType);
							});

					$('.allBackupChk').click(
							function(e) {
								var isChecked = $(this).is(':checked');
								$(this).parents('table.backupTable').find(
										'.backupChk')
										.attr('checked', isChecked);
							});

					$('.backupAddLnk').click(function(e) {
						e.preventDefault();
						loadAddContent();
					});
					$('.downloadLnk').click(
							function(e) {
								e.preventDefault();
								var backupIndex = $(this).parent().parent()
										.find('.backupIndex').text();
								_checkFileExistAndDownload(backupIndex);
							});

					$('.delSchedule').click(
							function(e) {
								e.preventDefault();
								var index = $(this).parent().find(
										'.scheduleIndex').text();
								if (confirm(VAR_SYSBACK_DEL))
									delBackupSchedule(index);
							});

					$('.backupsDelLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제
					$('.backupsDelLnk').click(
							function(e) {
								e.preventDefault();
								var backupIndices = $('.backupChk').filter(
										':checked').siblings('.backupIndex')
										.map(function() {
											return $(this).text();
										}).get();
								FlowitUtil
										.log("backupIndices: ", backupIndices);
								if (backupIndices.length == 0) {
									$.obAlertNotice(VAR_SYSBACK_DELSEL);
									return;
								}

								var chk = confirm(VAR_SYSBACK_DEL);
								if (chk) {
									delBackups(backupIndices);
								} else {
									return false;
								}
								// delBackups(backupIndices);
							});

					// search event
					// TODO
					$('.control_Board a.searchLnk').click(
							function(e) {
								e.preventDefault();
								var option = {
									"searchKey" : $(
											'.control_Board input.searchTxt')
											.val(),
									"startTimeL" : searchStartTime,
									"endTimeL" : searchEndTime
								};
								FlowitUtil.log('searchOption: %s', option);
								searchFlag = true;
								loadListContent(option);
							});
					$('.control_Board input.searchTxt').keydown(
							function(e) {
								if (e.which != 13) {
									return;
								}
								var option = {
									"searchKey" : $(
											'.control_Board input.searchTxt')
											.val(),
									"startTimeL" : searchStartTime,
									"endTimeL" : searchEndTime
								};
								FlowitUtil.log('searchOption: %s', option);
								searchFlag = true;
								loadListContent(option);
							});

					// DatePicker 초기값 Setting
					if (null != searchStartTime) // 검색을 한번이라도 했을 경우
					{
						$('#reservationtime').val(
								new Date(searchStartTime)
										.format('yyyy-mm-dd HH:MM')
										+ " ~ "
										+ new Date(searchEndTime)
												.format('yyyy-mm-dd HH:MM'));
					} else // 검색을 한번도 하지않았을 경우
					{
						$('#reservationtime').val(
								moment().subtract(12, 'hour').format(
										'YYYY-MM-DD HH:mm')
										+ " ~ "
										+ moment().format('YYYY-MM-DD HH:mm'));
					}
					// DateRangePicker
					if (langCode == "ko_KR") {
						$('#reservationtime')
								.daterangepicker(
										{
											ranges : {
												'오늘' : [
														moment()
																.startOf('days'),
														moment() ],
												'최근 1일' : [
														moment().subtract(
																'days', 1),
														moment() ],
												'최근 7일' : [
														moment().subtract(
																'days', 7),
														moment() ],
												'최근 30일' : [
														moment().subtract(
																'days', 30),
														moment() ],
												'이번 달' : [
														moment().startOf(
																'month'),
														moment().endOf('month') ],
												'지난 달' : [
														moment()
																.subtract(
																		'month',
																		1)
																.startOf(
																		'month'),
														moment().subtract(
																'month', 1)
																.endOf('month') ]
											},
											startDate : moment().subtract(12,
													'hour').format(
													'YYYY-MM-DD HH:mm'),
											endDate : moment().format(
													'YYYY-MM-DD HH:mm'),
											opens : 'right', // 달력위치
											timePicker : true,
											timePickerIncrement : 30,
											timePicker12Hour : false,
											format : 'YYYY-MM-DD HH:mm'
										},
										function(start, end, label) {
											// console.log(start.toISOString(),
											// end.toISOString(), label);
											searchStartTime = Number(start
													.format('x'));
											searchEndTime = Number(end
													.format('x'));
										});
					} else {
						// 리뉴얼 DatePicker
						$('#reservationtime')
								.daterangepicker(
										{
											ranges : {
												'Today' : [
														moment()
																.startOf('days'),
														moment() ],
												'Yesterday' : [
														moment().subtract(
																'days', 1),
														moment() ],
												'Last 7 Days' : [
														moment().subtract(
																'days', 7),
														moment() ],
												'Last 30 Days' : [
														moment().subtract(
																'days', 30),
														moment() ],
												'This Month' : [
														moment().startOf(
																'month'),
														moment().endOf('month') ],
												'Last Month' : [
														moment()
																.subtract(
																		'month',
																		1)
																.startOf(
																		'month'),
														moment().subtract(
																'month', 1)
																.endOf('month') ]
											},
											startDate : moment().subtract(12,
													'hour').format(
													'YYYY-MM-DD HH:mm'),
											endDate : moment().format(
													'YYYY-MM-DD HH:mm'),
											opens : 'right', // 달력위치
											timePicker : true,
											timePickerIncrement : 30,
											timePicker12Hour : false,
											format : 'YYYY-MM-DD HH:mm'
										},
										function(start, end, label) {
											// console.log(start.toISOString(),
											// end.toISOString(), label);
											searchStartTime = Number(start
													.format('x'));
											searchEndTime = Number(end
													.format('x'));
										});
					}
				}
			},
			validateDaterefresh : function() {
				if (($('.control_Board input.searchTxt').val() != "")
						&& ($('.control_Board input.searchTxt').val() != null)) {
					var search = $('.control_Board input.searchTxt').val();

					if (!isValidStringLength(search, 1, 64)) {
						var data = VAR_COMMON_LENGTHFORMAT + "(" + 1 + "~" + 64
								+ ")";
						$.obAlertNotice(data);
						$('.control_Board input.searchTxt').val('');
						return false;
					}

					if (!isExistSpecialCharacter(search)) {
						$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
						$('.control_Board input.searchTxt').val('');
						return false;
					}
					// if(getValidateStringint(search, 1, 64) == false)
					// {
					// alert(VAR_FAULTSETTING_SPECIALCHAR);
					// $('.control_Board input.searchTxt').val('');
					// return false;
					// }
				}

				return true;
			},
			_downloadBackup : function(index) {
				with (this) {
					ajaxManager
							.runHtmlExt({
								url : "sysSetting/backup/downloadBackupfile.action",
								data : {
									"backupIndex" : index
								},
								target : "#downloadLnk",
								successFn : function(params) {
									var url = "sysSetting/backup/downloadBackupfile.action?backupIndex="
											+ index;
									$('#downloadFrame').attr('src', url);
								},
								errorFn : function(jqXhr) {
									$.obAlertAjaxError(VAR_SYSBACK_DOWNLOAD,
											jqXhr);
								}
							});
				}
			},
			_checkFileExistAndDownload : function(index) {
				with (this) {
					ajaxManager.runJsonExt({
						url : "sysSetting/backup/isDownloadFileExist.action",
						data : {
							"backupIndex" : index
						},
						successFn : function(data) {
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								return;
							}
							_downloadBackup(index);
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSBACK_DOWNLOAD, jqXhr);
						}
					});
				}
			},
			delBackups : function(backupIndices) {
				with (this) {
					ajaxManager.runJsonExt({
						url : "sysSetting/backup/delSysBackups.action",
						data : {
							"sysBackupIndices" : backupIndices,
						},
						successFn : function(data) {
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								return;
							}

							loadListContent();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSBACK_DELFAIL, jqXhr);
						}
					});
				}
			},
			delBackupSchedule : function(index) {
				with (this) {
					ajaxManager.runJsonExt({
						url : "sysSetting/backup/delBackupSchedule.action",
						data : {
							"sysBackupIndices" : [ index ],
						},
						successFn : function(data) {
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								return;
							}

							loadListContent();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSBACK_DELFAIL, jqXhr);
						}
					});
				}
			},
			loadAddContent : function() {
				with (this) {
					ajaxManager.runHtmlExt({
						url : "sysSetting/backup/loadAddContent.action",
						target : "#wrap .contents",
						successFn : function(params) {
							initAddContentValues(false);
							registerAddContentEvents(false);
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSBACK_LOAD, jqXhr);
						}
					});
				}
			},
			initAddContentValues : function(isModify) {
				with (this) {
				}
			},
			registerAddContentEvents : function(isModify) {
				with (this) {
					$('#scheduleType').change(function() {
						$('.onSchedule').addClass('none');
						switch (Number($(this).val())) {
						case 0:
							$('.onNow').removeClass('none');
							break;
						case 1: // 매일
							$('.onDaily').removeClass('none');
							break;
						case 2: // 매주
							$('.onWeekly').removeClass('none');
							break;
						case 3: // 매월
							$('.onMonthly').removeClass('none');
							break;
						case 4: // 한번만
							$('.onOnce').removeClass('none');
							break;
						default:
						}
					});

					$('#scheduleDate').datepicker({
						minDate : "0",
						dateFormat : "yy-mm-dd",
						showOn : "button",
						buttonImage : "imgs/meun/btn_calendar.png",
						buttonImageOnly : true
					});
					var currentDate = new Date();
					$('#scheduleDate').val(
							currentDate.getFullYear() + "-"
									+ (currentDate.getMonth() + 1) + "-"
									+ currentDate.getDate());
					$('#scheduleDate').addClass('onSchedule onOnce none');
					$('.ui-datepicker-trigger').addClass(
							'onSchedule onOnce none');

					$('#sysBackupAddFrm')
							.submit(
									function() {
										if ($('#scheduleType').val() == 0
												&& !validateAddContent())
											return false;

										$(this)
												.ajaxSubmit(
														{
															type : 'POST',
															dataType : 'json',
															url : isModify ? 'sysSetting/backup/modifySysBackup.action'
																	: 'sysSetting/backup/addSysBackup.action',
															success : function(
																	data) {
																if (data.isSuccessful) {
																	$
																			.obAlertNotice(VAR_COMMON_REGISUCCESS);
																	loadListContent();
																} else {
																	$
																			.obAlertNotice(data.message);
																}
															},
															error : function(
																	jqXhr) {
																$
																		.obAlertAjaxError(
																				VAR_SYSBACK_ADDMODIFYFAIL,
																				jqXhr);
																loadListContent();
															}
														});

										// always return false to prevent
										// standard browser submit and page
										// navigation
										return false;
									});

					$('.cancelLnk').click(function(e) {
						e.preventDefault();
						loadListContent();
					});

					$('.okLnk').click(function(e) {
						e.preventDefault();
						$('#sysBackupAddFrm').submit();
					});

					$('input[name="sysBackupAdd.target"]')
							.change(
									function() {
										var $searchTimeModeChecked = $('input[name="sysBackupAdd.target"]:checked');
										if ($searchTimeModeChecked.attr('id') == 'all'
												|| $searchTimeModeChecked
														.attr('id') == 'adcsmart') {
											$('#logDel').removeAttr("disabled");
										} else if ($searchTimeModeChecked
												.attr('id') == 'adcsmartSettings'
												|| $searchTimeModeChecked
														.attr('id') == 'Settings') {
											$('#logDel').attr("disabled",
													"disabled");
										}
									});
				}
			},
			validateAddContent : function() {
				if (!$.trim($('input[name="sysBackupAdd.fileName"]').val())) {
					$.obAlertNotice(VAR_SYSBACK_FILENAMEINPUT);
					return false;
				}
				var filename = $('input[name="sysBackupAdd.fileName"]').val();
				if (getValidateStringint(filename, 1, 64) == false) {
					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
					return false;
				}

				var description = $(".valdescription").val();
				if (description != '') {
					if (getValidateStringint(description, 1, 512) == false) {
						$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
						return false;
					}
				}

				return true;
			},
			noticePageInfo : function() {
				with (this) {
					var currentPage = pageNavi.getCurrentPage();
					var lastPage = pageNavi.getLastPage();
					var countTotal = pageNavi.getRowTotal();
					var targetCntHtml = $('.noticePageCountInfo').filter(
							':last');
					var targetPageHtml = $('.noticePageInfo').filter(':last');
					targetCntHtml.html(addThousandSeparatorCommas(countTotal));
					targetPageHtml.html("("
							+ addThousandSeparatorCommas(currentPage) + "/"
							+ addThousandSeparatorCommas(lastPage)
							+ VAR_COMMON_PAGE + ")");
				}
			}
		});