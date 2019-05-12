var VsFilterConfig = Class.create({
			initialize : function() {
				var fn = this;
				var vsFilterInfoList = undefined;
				this.searchedKey = undefined;
				this.orderDir = 2; // 2는 내림차순
				this.orderType = 34;// 
				this.searchFlag = false;
				this.pageNavi = new PageNavigator();
				this.pageNavi.onChange(function(fromRow, toRow, orderDir,
						orderType) {
					fn.loadVsFilterTableInListContent(fn.searchedKey, fromRow,
							toRow, orderDir, orderType);
				});
			},

			loadListContent : function(searchKey, orderDir, orderType) {
				with (this) {
					var rowTotal = 0;
					ajaxManager.runJsonExt({
						url : "sysSetting/retrieveVsFilterTotal.action",
						data : {
							"searchKey" : searchKey,
							"orderType"	: orderType,
							"orderDir" : orderDir
						},
						successFn : function(data) {
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								rowTotal = 100;
							} else {
								rowTotal = data.rowTotal;
								vsFilterInfoList = data.vsFilterInfoList;
							}
							pageNavi.updateRowTotal(rowTotal, orderType);
							noticePageInfo();
							loadVsFilterListContent(searchKey, orderDir, orderType);
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSSETTING_GROUPINFOLOAD,
									jqXhr);
						}
					});
				}
			},
			loadVsFilterListContent : function(searchKey, orderDir, orderType,
					fromRow, toRow) {
				with (this) {
					ajaxManager.runHtmlExt({
						url : 'sysSetting/loadVsFilterListContent.action',
						data : {
							"fromRow" : fromRow === undefined ? pageNavi
									.getFirstRowOfCurrentPage() : fromRow,
							"toRow" : toRow === undefined ? pageNavi
									.getLastRowOfCurrentPage() : toRow,
							"searchKey" : searchKey,
							"orderDir" : orderDir,
							"orderType" : orderType
						},
						target : "#wrap .contents",
						successFn : function(params) {
							header.setActiveMenu('SysSettingVsFilter');
							noticePageInfo();
							registerContents();
							searchedKey=searchKey;
						},
						completeFn : function() {
							pageNavi.refresh();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSSETTING_ADMINLOGLOAD,
									jqXhr);
						}
					});
				}
			},
			loadVsFilterTableInListContent : function(searchKey, fromRow,
					toRow, orderDir, orderType) {
				with (this) {
					ajaxManager.runHtmlExt({
						url : "sysSetting/loadVsFilterListContent.action",
						data : {
							"fromRow" : fromRow === undefined ? pageNavi
									.getFirstRowOfCurrentPage() : fromRow,
							"toRow" : toRow === undefined ? pageNavi
									.getLastRowOfCurrentPage() : toRow,
							"searchKey" : searchKey,
							"orderDir" : orderDir,
							"orderType" : orderType
						},
						// target : "table.Board",
						target : "#wrap .contents",
						successFn : function(params) {
							noticePageInfo();
							searchedKey = searchKey;
							// vsFilterInfoList =
							// params.vsFilterInfoList;
							registerContents();
						},
						completeFn : function() {
							pageNavi.refresh();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError(VAR_SYSSETTING_GROUPINFOLOAD,
									jqXhr);
						}
					});
				}
			},
			registerContents : function() {
				with (this) {
					$('.hideVSFilterLnk').click(function(e) {
						e.preventDefault();
						showHideVSFilterLnk("hide")
					});

					$('.showVSFilterLnk').click(function(e) {
						e.preventDefault();
						showHideVSFilterLnk("show")
					});

					$('.orderDir_Desc').click(function(e) {
						e.preventDefault();
						orderDir = $(this).find('.orderDir').text();
						orderType = $(this).find('.orderType').text();
						var searchKey = $('p.cont_sch input.searchTxt').val();
						FlowitUtil.log('click:' + searchKey);
						searchFlag = true;
						loadListContent(searchKey, orderDir, orderType);
					});

					$('.orderDir_Asc').click(function(e) {
						e.preventDefault();
						orderDir = $(this).find('.orderDir').text();
						orderType = $(this).find('.orderType').text();
						var searchKey = $('p.cont_sch input.searchTxt').val();
						FlowitUtil.log('click:' + searchKey);
						searchFlag = true;
						loadListContent(searchKey, orderDir, orderType);
					});

					$('.orderDir_None').click(function(e) {
						e.preventDefault();
						orderDir = $(this).find('.orderDir').text();
						orderType = $(this).find('.orderType').text();
						var searchKey = $('p.cont_sch input.searchTxt').val();
						FlowitUtil.log('click:' + searchKey);
						searchFlag = true;
						loadListContent(searchKey, orderDir, orderType);
					});
					$('.refreshLnk').click(function(e) {
						e.preventDefault();
						loadListContent();
					});

					$('.allFilterChk').click(
							function(e) {
								var isChecked = $(this).is(':checked');
								// $(this).parent().parent().parent().parent().find('.userChk').attr('checked',
								// isChecked);
								$(this).parents('table.Board').find(
										'.filterChk')
										.attr('checked', isChecked);
							});

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
										loadListContent(searchKey);
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
										loadListContent(searchKey);
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
			validateSearchKey : function() {
				var search = $('.control_Board input.searchTxt');

				if (!isValidStringLength(search, 1, 20)) {
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

				return true;
			},
			showHideVSFilterLnk : function(flag) {
				with (this) {
					var vsFilterInfoIndexList = _getCheckedData();
					if (vsFilterInfoIndexList.length == 0) {
						$.obAlertNotice("변경할 항목을 선택해 주세요.");
						return;
					}

					// var chk = confirm(VAR_SYSSETTING_GROUPDEL + "\n" +
					// VAR_SYSSETTING_DASHGROUPDEL);
					// if(chk == false)
					// {
					// return;
					// }
					// data222 = { "list": vsShowHideFilterList}
					var actionUrl = "sysSetting/showVSFilterList.action";
					if (flag == 'hide')
						actionUrl = "sysSetting/hideVSFilterList.action";
					ajaxManager.runJsonExt({
						url : actionUrl,
						data : {
							"vsFilterInfoIndexList" : vsFilterInfoIndexList
						},
						successFn : function(data) {
							if (!data.isSuccessful) {
								$.obAlertNotice(data.message);
								return;
							}
							loadListContent();
						},
						errorFn : function(jqXhr) {
							$.obAlertAjaxError("표시 설정에 실패했습니다.", jqXhr);
						}
					});
				}
			},
			_getCheckedData : function() {
				return $('.filterChk').filter(':checked').map(function() {
					return $(this).val();
					// return vsFilterInfoList.find(x=> x.index ===
					// $(this).val());
				}).get();
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
			},

		});