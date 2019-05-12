/**
 * Author: Kim Jong O Email: tgjoo@opensns.co.kr Update: 2012.04.17
 */
function exceptionEvent()
{
	ajaxManager.runJson(
	{
		url : "OBGeneral/getOBExceptionInfo.action",
		successFn : function(data) 
		{
			if (data.adcexception) 
			{
				popUpException(data.adcexception);
			}
			else
			{
				return false;
			}
		}
	});
}
// ajax 모듈 오류 메세지
function ajaxExceptionMessage(jqXhr, status, message)
{
//	if (status == 0)
//	{
//		alert(VAR_AJAX_ALERT_0);
//	}
//	else if (status == 12029)
//	{
//		alert(VAR_AJAX_ALERT_0);
//	}
//	else
//	{
//		alert(status+" : "+b+": "+c);
//	}
	var msg = 'Could not connect to server, please try refresh (F5)';
	$.obAlertAjaxError(msg, jqXhr);
}
// Object 형 안에 있는 Value 값을 모두 더한값과, 평균값(반올림) 구하기 (Key 값 무시)
function sumObjectValue(obj)
{
	var sum = 0;
	var CountNum = 0;
	var avgNum = 0;
	for( var el in obj )
	{
	    if( obj.hasOwnProperty( el ) )
	    {
	    	if(obj[el])
	    	{
	    		CountNum ++;
	    		sum += parseFloat( obj[el] );
	    	}	    	
	    }
	}
	avgNum = (sum / CountNum);
	var retVal =
	{
			avgNum : Math.round(avgNum),
			sumNum : sum
	};
	return retVal;
}
// 숫자에 천 단위로 (,)콤마를 찍는 함수
function addCommaforNumber(Num)
{
	 if(isNaN(Num)){return 0;}
	  var reg = /(^[+-]?\d+)(\d{3})/;   
	  Num += '';
	  while (reg.test(Num))
		  Num = Num.replace(reg, '$1' + ',' + '$2');
	  return Num;
}
//JS Date 형을 인자로 주면, YYYY-MM-DD 형으로 변환하는 함수
function parseDateTimeStringShort(dateString)
{	
	var sYear = dateString.getFullYear();
	var sMonth = dateString.getMonth() +1;
	if (sMonth < 10)
	{
		sMonth = '0' +sMonth;
	}
	var sDay = dateString.getDate();
	if (sDay < 10)
	{
		sDay = '0' +sDay;
	}
	
	var DateVal = (sYear + "-" + sMonth + "-" + sDay);
	
	return DateVal;	
}
//  Date String을 인자로 주면, YYYY-MM-DD hh:nn:ss 형으로 변환하는 함수
function parseDateTimeString(dateString)
{
	var DateObject = new Date();
	DateObject = parseDateTime(dateString);
	
	var sYear = DateObject.getFullYear();
	var sMonth = DateObject.getMonth() +1;
	if (sMonth < 10)
	{
		sMonth = '0' +sMonth;
	}
	var sDay = DateObject.getDate();
	if (sDay < 10)
	{
		sDay = '0' +sDay;
	}
	var sHour = DateObject.getHours();
	if (sHour < 10)
	{
		sHour = '0' +sHour;
	}
	var sMin = DateObject.getMinutes();
	if (sMin < 10)
	{
		sMin = '0' +sMin;
	}
	var sSec = DateObject.getSeconds();
	if (sSec < 10)
	{
		sSec = '0' +sSec;
	}	
	var DateMessage = (sYear + "-" + sMonth + "-" + sDay + "  "
			+ sHour + ":" + sMin + ":" + sSec);
	
	return DateMessage;	
}

// Date String을 인자로 주면, YYYYMMDDhh:nn:ss 형으로 변환하는 함수
function parseDateTimeStringFunc(dateString)
{
//	var DateObject = new Date();
//	DateObject = parseDateTime(dateString);
	
//	now = new Date();
	year = "" + dateString.getFullYear();
	month = "" + (dateString.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
	day = "" + dateString.getDate(); if (day.length == 1) { day = "0" + day; }
	hour = "" + dateString.getHours(); if (hour.length == 1) { hour = "0" + hour; }
	minute = "" + dateString.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
	second = "" + dateString.getSeconds(); if (second.length == 1) { second = "0" + second; }
	return year + month + day + hour + minute + second;
}

function parseDateTime(dateString)	
{// split the string get each field
	var date = new Date();
	var tokens = dateString.split("T");
	if (2 == tokens.length) 
	{
		var dateTokens = tokens[0].split("-");
		if (3 == dateTokens.length) 
		{
			date.setFullYear(Number(dateTokens[0]), Number(dateTokens[1]) - 1, Number(dateTokens[2]));
//				date.setMonth(Number(dateTokens[1]) - 1, Number(dateTokens[2]));
		}
		var timeTokens = tokens[1].split(":");
		if (3 == timeTokens.length) 
		{
			date.setHours(Number(timeTokens[0]), Number(timeTokens[1]), Number(timeTokens[2]), 0);
		}
	}

	return date;
}

//OBException 오류메세지
function popUpException(exception) {
	mywd=470;
	myht=290;
	myleft=(document.body.clientWidth - mywd)/2;
	mytop=(document.body.clientHeight - myht)/2;
	
	myOpt='scrollbar=no;toolbars=no;menubar=no;location=no;location:no;dialogTop:'+mytop+'px;dialogLeft:'+myleft+'px;dialogWidth:'+mywd+'px;dialogHeight:'+myht+'px;center:no;help:no;resizable:no;status:no';
	//alert(exception);
	var myObj = new Object();
//	myObj.gMsg = exception.generalMsg;
//	myObj.code = exception.code;
//	myObj.sMsg = exception.solutionMsg;
//	myObj.dMsg = exception.detailMsg;
//	myObj.oTime = exception.occurTime;
//	myObj.mName = exception.moduleName;
	
	/*
	DB 쿼리에 실패했습니다. -> 시스템 부가기능 변경에 실패했습니다. 
	DB 상태를 점검하십시오.,다시 시도해 보십시오.

	DB 쿼리에 실패했습니다. 
	-> DB 상태를 점검하십시오.
	시스템 부가기능 변경에 실패했습니다. 
	-> 다시 시도해 보십시오.

*/
//	var ArrayList<S> myObj = new ArrayLsit<Object>();	
//	var myObj = new Array();
	var gMsg = "";
	var errNum = 1;
//	for(var i=0; i<exception.generalMsgList.length; i++) 
	for(var i=exception.generalMsgList.length-1; i>=0; i--, errNum++) 
	{
//		if (0 != i) gMsg += "\n";
		gMsg += "" + errNum + "." + VAR_COMM_CONTENT+ ": " + exception.generalMsgList[i] + "\n";
		if (exception.solutionMsgList[i].length != 0)
			gMsg += "    " + VAR_COMM_ACTION + ": " + exception.solutionMsgList[i] + "\n";
	}
	
	myObj.gMsg = gMsg;
	myObj.sMsg = exception.solutionMsgList;
	myObj.dMsg = exception.detailMsgList;
	myObj.oTime = exception.occurTime;
	myObj.mName = exception.moduleNameList;
	
//	[DB 쿼리에 실패했습니다., 시스템 부가기능 변경에 실패했습니다. ]
//	[DB 상태를 점검하십시오., 다시 시도해 보십시오.]
	
//	retVal = window.showModalDialog("errorPage.jsp",myObj,myOpt);
	retVal = window.showModalDialog("errorPage2.jsp",myObj,myOpt);
//	retVal = window.showModalDialog("errorPage3.jsp",myObj,myOpt);

}
// 메뉴 롤오버 효과
function rollover(ele) {
	// rollover effect
	$(ele).hover(function() {
		var dn_src = $(this).children('img').attr('src');
		if (dn_src.match('.gif') != null) {
			$(this).children('img').attr('src', dn_src.replace('_off.gif', '_on.gif'));
		} else {
			$(this).children('img').attr('src', dn_src.replace('_off.jpg', '_on.jpg'));
		}
	}, function() {
		var dn_src = $(this).children('img').attr('src');
		if (dn_src.match('.gif') != null) {
			$(this).children('img').attr('src', dn_src.replace('_on.gif', '_off.gif'));
		} else {
			$(this).children('img').attr('src', dn_src.replace('_on.jpg', '_off.jpg'));
		}
	});
}

function over(ele) {
	var dn_src = $(ele).children('img').attr('src');
	if (dn_src.match('.gif') != null) {
		$(ele).children('img').attr('src', dn_src.replace('_0.gif', '_1.gif'));
	}else if(dn_src.match('.png') != null){
		$(ele).children('img').attr('src', dn_src.replace('_0.png', '_1.png'));
	}else {
		$(ele).children('img').attr('src', dn_src.replace('_0.jpg', '_1.jpg'));
	}
}

function out(ele) {
	var dn_src = $(ele).children('img').attr('src');
	if(dn_src.match('.png') != null){
			$(ele).children('img').attr('src', dn_src.replace('_1.png', '_0.png'));	
	}else if ($(this).parent('li').attr('class') != 'on') {
		if (dn_src.match('.gif') != null) {
			$(ele).children('img').attr('src', dn_src.replace('_1.gif', '_0.gif'));
		} else {
			$(ele).children('img').attr('src', dn_src.replace('_1.jpg', '_0.jpg'));
		}
	}
}
/*
function over(ele) {
	var dn_src = $(ele).children('img').attr('src');
	if (dn_src.match('.gif') != null) {
		$(ele).children('img').attr('src', dn_src.replace('_off.gif', '_on.gif'));
	}else if(dn_src.match('.png') != null){
		$(ele).children('img').attr('src', dn_src.replace('_off.png', '_on.png'));
	}else {
		$(ele).children('img').attr('src', dn_src.replace('_off.jpg', '_on.jpg'));
	}
}

function out(ele) {
	var dn_src = $(ele).children('img').attr('src');
	if(dn_src.match('.png') != null){
			$(ele).children('img').attr('src', dn_src.replace('_on.png', '_off.png'));	
	}else if ($(this).parent('li').attr('class') != 'on') {
		if (dn_src.match('.gif') != null) {
			$(ele).children('img').attr('src', dn_src.replace('_on.gif', '_off.gif'));
		} else {
			$(ele).children('img').attr('src', dn_src.replace('_on.jpg', '_off.jpg'));
		}
	}
}
*/
//function initMenu() {
//	$('.wrap .header .topSnb > li').each(function() {
//		out($(this).children('a'));
//
//		if ($(this).children('ul').length > 0) {
//			$(this).children('ul').stop(true, true).fadeOut();
//		}
//	});
//}

function initMenu() {
	$('.top .menu1 > span').each(function() {
		out($(this).children('a'));

		if ($(this).children('ul').length > 0) {
			$(this).children('ul').stop(true, true).fadeOut();
		}
	});
}

// 테이블 초기화 함수
// param1 : 테이블명 정의 함수 ex) ["테이블1의 tr",테이블2의 tr"] *꼭 tr까지만 정의 해줘야 합니다.
// param2 : 테이블 왼쪽 정렬 컬럼 번호, 컬럼 번호는 0부터 시작 ex) 3,7번째 컬럼 왼쪽 정렬시 ["2","6"]
// param3 : 테이블 오른쪽 정렬 컬럼 번호, 컬럼 번호는 0부터 시작 ex) 1,2번째 컬럼 오른쪽 정렬시 ["0","2"] *정렬
// 시킬것이 없으면 -1을 적음

function initTableRE(ele) {
	for ( var j = 0; j < ele.length; j++) {
		$(ele[j]).each(function(i) {
			if (i % 2 != 1) {
				$(this).css({
					'background-color' : '#fff'
				});
			}			
		});
	}
}

function initTable(ele, lft, rgt) {
	for ( var j = 0; j < ele.length; j++) {
		$(ele[j]).each(function(i) {
			if (i % 2 != 1) {
				$(this).css({
					'background-color' : '#ffff'  //'#f3f4f7'
				});
			}

			$(this).find('td').each(function(i, e) {
				var t = false;
				for ( var l = 0; l < lft.length; l++) {
					if (i == lft[l]) {
						$(this).css({
						    'text-align' : 'left',
						    'padding-left' : '3px'
						});
						t = true;
					}
				}
				for ( var r = 0; r < rgt.length; r++) {
					if (i == rgt[r]) {
						$(this).css({
						    'text-align' : 'right',
						    'padding-right' : '5px'
						});
						t = true;
					}
				}
				if (!t) {
					$(this).css({
						'text-align' : 'center'
					});
				}
			});
		});
	}
}

// 트리 메뉴 초기화
function initTree(ele) {
	ele.parent().parent('ul').children('li').each(function() {
		// var $siblingEle = $(this).find('ul');
		// $siblingEle.slideUp(200);
		$(this).children('p').removeClass('on');
		// $('.depth3 > li a').removeClass('bold');
	});
}

// input 넓이 100% 설정
function input100(ele) {
	$(window).resize(function() {
		var pWid = $(ele).parent('p').width();
		var eleWid = pWid - $(ele).next('a').width() - parseInt($(ele).next('a').css('margin-left')) - 7;
		$(ele).width(eleWid);
	});
	$(window).trigger('resize');
}

// 리스트형 테이블 스타일 정의
function tableHeadSize(ele, opt) {
	var defaultSize = 8;
	if (opt['hsize'] != null)
		defaultSize = opt['hsize'];
	var tableSize = $(ele).find('tbody > tr').size();
	var $tableWrap = $(ele).parents('.t_fixed_header .body > div');
	var $tableBody = $(ele).parents('.t_fixed_header .body');
	var $tableHead = $(ele).parents('.t_fixed_header').children('.headtable');

	if ($tableWrap.height() > $tableBody.height()) {
		$tableHead.css('margin-right', '17px');
		$tableBody.css('overflow-y', 'scroll');
		$(ele).find('tbody > tr').each(function() {
			if ($(this).attr('class') == 'empty') {
				$(this).remove();
			}
		});
	} else {
		$tableHead.css('margin-right', '0');
		$tableBody.css('overflow-y', 'hidden');
		$tableWrap.width('100%');

		for (; tableSize < defaultSize; tableSize++) {
			var $t =$("<tr class='empty'></tr>");
			for ( var i = 0; i < opt['col']; i++) {
				 $t.append('<td></td>');
			}
			$(ele).find('tbody').append($t);
		}
	}
}

// 레이어 팝업창 열기
function showPopup(opt) 
{
	var $pop = null;
	// 팝업창 열기
	$pop = $(opt['id']).clone(); //alert(opt['id']);
	$pop.addClass('cloneDiv');
	$pop.css('width', opt['width']);
	if (opt['height'] != null) 
	{
		$pop.find('.pop_contents').css('height', opt['height']);
	}
	$('body').append("<div class='popup_type1'></div>");
	$('body').append($pop);
	$pop.show();// .draggable();
	
	//alert("window width : " + $(window).width());			//1263
	//alert("window height : " + $(window).height());			//839
	//alert("$pop width : " + $pop.width());					//460
	//alert("$pop height : " + $pop.height());				//112
	
	// 팝업창 정중앙 위치
	$pop.css({
	    'left' : ($(window).width() - $pop.width()) / 2,
	    'top' : ($(window).height() - $pop.height()) / 2
	});
	$('.popup_type1').height($('#wrap').height());
	// 팝업창 닫기
	$pop.find('.close').click(function(e) 
	{
		$('.popup_type1').remove();
		$pop.remove();
	});
}
// 레이어 팝업창 닫기
function closeLPopup() 
{
	$('.popup_type1').remove();
	$('.cloneDiv').remove();
}

// 버튼 이미지 변환
function btn_on(ele) 
{
	var dn_src = $(ele).attr('src');
	if (dn_src.match('.gif') != null) 
	{
		$(ele).attr('src', dn_src.replace('_off.gif', '_on.gif'));
	} 
	else 
	{
		$(ele).attr('src', dn_src.replace('_off.png', '_on.png'));
	}
}

function btn_off(ele) 
{
	var dn_src = $(ele).attr('src');
	if (dn_src.match('.gif') != null) 
	{
		$(ele).attr('src', dn_src.replace('_on.gif', '_off.gif'));
	} 
	else 
	{
		$(ele).attr('src', dn_src.replace('_on.png', '_off.png'));
	}
}

//function snbAutoHeight() {
//	$(window).resize(function() {
//		$('.snb').height($(window).height() - $('.snb').position().top);
//	});
//}

// window 팝업 표시
function winPopup(opt) 
{
	var option = "height=" + opt['height'] + ",width=" + opt['width'] + ",toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizeable=no,left=0,top=0";
	window.open(opt['src'], "", option);
}

// window 팝업 닫기
function closePopup() 
{
	top.close();
}

// 탭 메뉴 열고 닫기
function closeAccordionType1(ele) 
{
	$(ele).find('h2').click(function(e) 
	{
		if ($(this).next().css('display') == 'block') 
		{
			$(this).next().hide();// slideUp(100);
			var src = $(this).nextAll('.open').find('img').attr('src');
			$(this).nextAll('.open').find('img').attr('src', src.replace('up2.png', 'down2.png'));
		} 
		else 
		{
			$(this).next('div').show();// slideDown(300);
			var src = $(this).nextAll('.open').find('img').attr('src');
			$(this).nextAll('.open').find('img').attr('src', src.replace('down2.png', 'up2.png'));
		}
	});
	$(ele).find('.open').click(function(e) 
	{
		$(this).prevAll('h2').trigger('click');
	});
}

// 콤보박스
function selectBoxType1(ele) 
{
	// 사용자 정의 콤보박스 넓이 설정
	$(ele).width($(ele).children('ul').width());

	// 사용자 정의 콤보박스 버튼 클릭 이벤트
	$(ele).children('.selected').children('a').click(function() 
	{
		var $p = $(this).parents(ele);
		if ($p.find('ul').is(':hidden')) 
		{
			over($(this));
			$p.find('ul').fadeIn(function() 
			{
				$(this).css('z-index', 1);
			});
		} 
		else 
		{
			out($(this));
			$p.find('ul').fadeOut(function() 
			{
				$(this).css('z-index', -1);
			});
		}
	}).blur(function() {
		var $p = $(this).parents(ele);
		if ($p.find('ul').is(':visible')) 
		{
			$(this).trigger('click');
		}
	});

	// 사용자 정의 콤보박스 텍스트 클릭 이벤트
	$(ele).children('.selected').children('span').click(function() 
	{
		$(this).next('a').trigger('click').focus();
	});

	// 사용자 정의 콤보박스 리스트 아이템 클릭 이벤트
	$(ele).children('ul').children('li').click(function(e) 
	{
		var $p = $(this).parents(ele);
		$p.find('.selected a').trigger('click');
		$p.find('.selected span').text($(this).text());
	});
}

// 대시보드창 사이즈 변경시 호출
function dash_resize() 
{
	var contentH = $(window).height() - $('.dash_content').position().top;
	$('.dash_content').innerHeight(contentH);
	var cont = $('.dash_content').height() - $('.dash_content .accordion_type1 > li:eq(0)').outerHeight() - $('.dash_content .accordion_type1 > li:eq(2)').outerHeight();
	cont = cont - (parseInt($('.dash_content .accordion_type1 > li:eq(1) > div').css('padding-top')) * 2) - $('.dash_content .accordion_type1 > li:eq(1) > h2').outerHeight();
	cont = cont - (parseInt($('.dash_content .accordion_type1 > li:eq(1)').css('margin-top')) * 2);
	$('.dash_content .accordion_type1 > li:eq(1) > div').height(cont);

	rowNum = Math.floor(cont / 24) - 1;
	rowHeight = rowNum * 24;

	htmlStr = '<table id="table1" summary="' + VAR_COMM_ADCVSIPSTAT + '">' + table1 + '</table>';
	$('.vsMonitor').children().first().remove();
	$('.vsMonitor').prepend(htmlStr);

	// 테이블 헤더 고정
	$('#table1').fixheadertable({
	    height : rowHeight,
	    colratio : [ -1, 28, 8, 28, 27, 9 ]
	});
	tableHeadSize('#table1', {
	    'col' : '5',
	    'hsize' : rowNum
	});

	// 빈 colgroup 삭제
	$('#table1').find('colgroup').each(function() 
	{
		if ($(this).children().length == 0)
			$(this).remove();
	});

	// 테이블 컬럼 정렬
	initTable([ "#table1 tbody tr" ], [ 2 ], [ -1 ]);
}

function historyPopup() 
{	
	showPopup({
	    'id' : '.pop_type_wrap',
	    'width' : '850px',
	    'height' : '500px'
	});

	// 리스트형 테이블 초기화
	$('.cloneDiv .ipList').fixheadertable({
		height : 125
	});

	tableHeadSize('.cloneDiv #lastedMember', {
	    'col' : '2',
	    'hsize' : '5'
	});
	tableHeadSize('.cloneDiv #orgMember', {
	    'col' : '2',
	    'hsize' : '5'
	});

	// 리스트형 테이블 행의 첫번째 열 배경색 지정
	$('.cloneDiv #orgMember > tbody > tr').each(function(i) 
	{
		$(this).children(":eq(0)").addClass('c1');
		$(this).children(":eq(1)").addClass('c2');
	});
	$('.cloneDiv #lastedMember > tbody > tr').each(function() 
	{
		$(this).children(":eq(0)").addClass('c1');
		$(this).children(":eq(1)").addClass('c2');
	});

	// 리스트 마지막 행 밑줄 적용
	$('.cloneDiv #orgMember > tbody > tr:last td').each(function() {
		$(this).css('border-bottom', '1px solid #B5B5B5');
	});
	$('.cloneDiv #lastedMember > tbody > tr:last td').each(function() {
		$(this).css('border-bottom', '1px solid #B5B5B5');
	});

	// 리스트형 테이블 행 롤오버 효과
	$('.ipList tbody tr:not(.empty)').hover(function() {
		$(this).css({
		    'background-color' : '#ccc',
		    'cursor' : 'pointer'
		});
		$(this).children(":eq(1)").css('background-color', '#ccc');
	}, function() {
		$(this).removeAttr('style');
		$(this).children(":eq(1)").css('background-color', '#e7e8ed');
	});

	// 테이블 컬럼 정렬
	initTable([ ".cloneDiv #lastedMember tr,.cloneDiv #orgMember tr" ], [ 0, 1 ], [ -1 ]);
	
	if ( $.browser.msie ) 
	{
		 if( $.browser.version == 7 )
		 {
			 w= $('.cloneDiv .pop_contents .none_bt').width();
			 $('.cloneDiv .form_type1').width(w); 
			
		 }
	}
}

//리스트형 테이블 스타일 정의
function tableHeadSizeWith$table($table, opt) 
{
	var defaultSize = 8;
	if (opt['hsize'] != null)
		defaultSize = opt['hsize'];
	var tableSize = $table.find('tbody > tr').size();
	var $tableWrap = $table.parents('.t_fixed_header').find('.body > div');
	var $tableBody = $table.parents('.t_fixed_header').find('.body');
	var $tableHead = $table.parents('.t_fixed_header').find('.headtable');

	if ($tableWrap.height() > $tableBody.height()) 
	{
		$tableHead.css('margin-right', '17px');
		$tableBody.css('overflow-y', 'scroll');
		$table.find('tbody > tr').each(function(){
			if ($(this).attr('class') == 'empty') 
			{
				$(this).remove();
			}
		});
	} 
	else 
	{
		$tableHead.css('margin-right', '0px');
		$tableBody.css('overflow-y', 'hidden');
		$tableWrap.width('100%');

		for (; tableSize < defaultSize; tableSize++) 
		{
			var $t =$("<tr class='empty'></tr>");
			for ( var i = 0; i < opt['col']; i++) 
			{
				 $t.append('<td></td>');
			}
			$table.find('tbody').append($t);
		}
	}
}

//리스트형 테이블 스타일 정의
function tableHeadSizeWithoutEmptyRows($table) 
{
	var $tableWrap = $table.parents('.t_fixed_header').find('.body > div');
	var $tableBody = $table.parents('.t_fixed_header').find('.body');
	var $tableHead = $table.parents('.t_fixed_header').find('.headtable');

	$tableBody.css('overflow-x', 'hidden');
	if ($tableWrap.height() > $tableBody.height()) 
	{
		$tableHead.css('margin-right', '17px');
		$tableBody.css('overflow-y', 'scroll');
		$table.find('tbody > tr').each(function() {
			if ($(this).attr('class') == 'empty') 
			{
				$(this).remove();
			}
		});
	} 
	else 
	{
		$tableHead.css('margin-right', '0');
		$tableBody.css('overflow-y', 'hidden');
		$tableWrap.width('100%');
	}
}

// 시스템 운영 화면 resize
function resizeSystemAdminView() 
{
	var contentH = $(window).height() - $('.dash_content').position().top;
	$('.dash_content').innerHeight(contentH);
	var cont = $('.dash_content').height() - $('.dash_content .accordion_type1 > li:eq(0)').outerHeight() - $('.dash_content .accordion_type1 > li:eq(2)').outerHeight();
	cont = cont - (parseInt($('.dash_content .accordion_type1 > li:eq(1) > div').css('padding-top')) * 2) - $('.dash_content .accordion_type1 > li:eq(1) > h2').outerHeight();
	cont = cont - (parseInt($('.dash_content .accordion_type1 > li:eq(1)').css('margin-top')) * 2);
	$('.dash_content .accordion_type1 > li:eq(1) > div').height(cont);

	var rowNum = Math.floor(cont / 24) - 1;
	var rowHeight = rowNum * 24;
	$('.vsConfigLog .body').css("overflow-y", "auto");
	$('.vsConfigLog .body').height(rowHeight);
	
	tableHeadSize('#table1', {
	    'col' : '5',
	    'hsize' : rowNum
	});
}

// 현지화 image folder return 2014.02.18
function getImgLang()
{
	var img_lang;
	if (langCode == "ko_KR")
	{
		img_lang = "";
	}
	else
	{
		img_lang = "_eng";
	}
	
	return img_lang;
}

//virtual server port validation 2012.12.04
function checkNum(port) 
{
	var regexp = /^\d{2,5}$/;	
	return regexp.test(port);
};

//function checkNumOne(port) 
//{
////	var regexp = /^\d{1,5}$/;	
//	var regexp = /^\1[0-9]{0,4}$/;
//	return regexp.test(port);
//};

function checkNumOne(port) 
{
	var regexp;
	if (port < 10)
	{
		regexp = /^[1-9]$/;
	}
	else
	{
		regexp = /^\d{2,5}$/;
	}
	return regexp.test(port);
};

// 숫자 Range 허용- VAR_COMMON_NUMRANGE  
function getValidateNumberRange(value, number1, number2)
{
	var checkNumberRange = /[^0-9]/;
	var checked = checkNumberRange.test(value);
	if (!checked && (value >= number1 && value <= number2)) 
		return true;
	return false;
}

// 숫자 Length 허용 - VAR_COMMON_NUMLENGTH 
function getValidateNumberLength(value, number1, number2)
{
	var checkNumberLength = /[^0-9]/;
	var checked = checkNumberLength.test(value);
	if (checked && value > number1)
		return false;
	return true;
}

//L2검색 IP - VAR_COMMON_IPFORMAT
function getValidateSearchIP(value)
{
	 var checkIp = /^([0-9]+(\.)?)+$/;
	 return checkIp.test(value);
}

//L2검색 MAC - VAR_COMMON_MACFORMAT
function getValidateMac(value)
{
	 var checkMac = /^[a-zA-Z0-9:]+$/;
	 return checkMac.test(value);
}

//L2검색 Port - 물리적인 Interface 허용 
function getValidateInterfacePort(value)
{
	var checkInterfacePort = /^([0-9]+(\.)?(\/)?)+$/;
	return checkInterfacePort.test(value);
}

// 특수문자 제외 - VAR_COMMON_SPECIALCHAR
function getValidateStringint(value, number1, number2)
{
	var checkStringint = /[~!\$^&*\=+|:;?"<,>'()]/;
//	var checkStringint = /^[a-zA-Z0-9가-힣]/gi; // TODO: 14.07.12 sw.jung: 특수문자 판별식 변경?
	var checked = checkStringint.test(value);
	if (!checked && (value.length >= number1 && value.length <= number2)) 
		return true;
	return false;
}

function isExistSpecialCharacter(value)
{
	var checkStringint = /[~!\$^&*\=+|:;?"<,>'()]/;
//	var checkStringint = /^[a-zA-Z0-9가-힣]/gi; // TODO: 14.07.12 sw.jung: 특수문자 판별식 변경?
	var checked = checkStringint.test(value);
	if (!checked) 
		return true;
	return false;
}

function isValidStringLength(value, number1, number2)
{
	if (value.length >= number1 && value.length <= number2) 
		return true;
	return false;
}

//F5 ID 알파벳으로 시작 .-_만 허용 - 
function getValidateF5ID(value, number1, number2)
{
	var checkStringint = /^[A-Za-z][a-zA-Z0-9._-]*?$/;
	var checked = checkStringint.test(value);
	if (checked)
		return true;
	return false;
}
//F5 Snmp .*/:_?=@,&-특수문자 허용 -  ! 가 빠짐 확인
function getValidateF5Snmp(value, number1, number2)
{
	var checkStringint = /^[a-zA-Z0-9.*:\/_?=@,&\-!]+$/;
	var checked = checkStringint.test(value);
	if (checked) 
		return true;
	return false;
}
//Common Length Check - 
function getValidateLength(value, number1, number2)
{
	if (value.length >= number1 && value.length <= number2)
		return true;
	return false;
}
/*function getValidateStringint(value, number1, number2)
{
	var checkStringint = /[~!\#$^&*\=+|:;?"<,>'()\-]/;
	var checked = checkStringint.test(value);
	
	if (checked)
	{
		return "charErr";
	}
	
	if (!(value.length >= number1 && value.length <= number2))
	{
		return "rangeErr";
	}	
	
//	if (!checked && (value.length >= number1 && value.length <= number2)) 
//		return true;
	return false;
}*/

// ID - VAR_SYSSETTING_IDRULEWRONG
function getValidateID(value)
{
//	var checkID = /^[a-zA-Z][a-zA-Z0-9]{4,15}$/g;//알파벳으로 시작하고 알파벳과 숫자 조합의 5~16자 사이로 구성..
	var checkID = /^[A-Za-z][a-zA-Z0-9._\-@]{4,15}$/g;
	return checkID.test(value);
}

function passwordEncode(plainPasswd){
	var encryptedPassword = CryptoJS.SHA512(plainPasswd);
	return encryptedPassword.toString();
}

//function passwordDecode(encryptedPassword){
//	var keyHash = CryptoJS.SHA256("obkey");
//	var iv = CryptoJS.enc.Hex.parse('101112131415161718191a1b1c1d1e1f');
//	var decryptedPassword = CryptoJS.AES.decrypt(encryptedPassword, keyHash, { iv : iv });
//	return decryptedPassword.toString(CryptoJS.enc.Utf8);
//}

function checkPasswdHistory(passwdHash, oldPasswdList) {
	if (oldPasswdList.indexOf(passwdHash) != -1){
		return false;
	}else{
		return true;
	}
}

// password - VAR_SYSSETTING_PASSWDRULEWRONG
function getValidatePasswd(data, oldPasswdList, checkLength) {
	var passwdHash = passwordEncode(data.userPassword);
	
	if (oldPasswdList) {
		// 이전 패스워드에 현재 패스워드가 포함되어 있는지 검사함.
		if(checkPasswdHistory(passwdHash, oldPasswdList) == false) {
			$.obAlertNotice(VAR_COMMON_PASSWDHISTORY);
			return false;
		}
	}

	var bagicTextRegExp = new RegExp("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[?!@#$%^&*-]).{8,16}$");
    var continuousText = [
    	"0123456789",
    	"abcdefghijklmnopqrstuvwxyz",
    	"ABCDEFGHIJKLMNOPQRSTUVWXYZ",
    	"qwertyuiopasdfghjklzxcvbnm",
    	"QWERTYUIOPASDFGHJKLZXCVBNM",
    	data.userPhoneNumb + ""
    ];
    
    if(continuousText[5] == ""){
    	continuousText.splice(5, 1);
    }
    
    var continuousTextRegExp = "";
	var sameTextRegExp = new RegExp("(.)\\1\\1");
	var userInformationTextRegExp = new RegExp("" + data.userId + "|" + data.userName + "");
	
	if(!checkLength){
    	checkLength = 3; // 연속되는 글자수를 지정하지 않으면 3으로 지정
	}
    
	if(bagicTextRegExp.test(data.userPassword)){
	    for(var i = 0; i < continuousText.length; i++){
	        var compareOne = continuousText[i];
	        var compareTwo = continuousText[i] + continuousText[i];
	        for(var j = 0; j < compareOne.length; j++){
	            var startIndex = compareOne.substr(j, 1); // 0
	            var endIndex = compareTwo.substr(j, checkLength); // 012
	            continuousTextRegExp += "["+startIndex+"]{"+checkLength+",}|"; // [0]{3,}|012|[1]{3,}|123|...
	            continuousTextRegExp += endIndex+"|";
	        }
	    }
	    continuousTextRegExp = new RegExp(continuousTextRegExp.replace(/.$/, "")); // 마지막의 글자를 하나 지우고 정규식으로 선언
	    
	    if(continuousTextRegExp.test(data.userPassword) || sameTextRegExp.test(data.userPassword) || userInformationTextRegExp.test(data.userPassword)){
	    	return false;
	    } else {
	    	return true;
	    }
    } else {
    	return false;
    }
}

// IP - VAR_COMMON_IPFORMAT
function getValidateIP(value)
{
	var checkIP = /^((25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|\d)$/;
	return checkIP.test(value);
}

// IP:PORT - VAR_COMMON_IPPORTFORMAT
function getValidateIPPort(value)
{
	var checkIPPort = /^((25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|\d):0*(?:6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{1,3}|[0-9])$/;
	return checkIPPort.test(value);	
}

function getValidateIPMode(value)
{
	var checkIPPort = /(\.)|([0-9])|(\/)|(\*)+$/;
	return checkIPPort.test(value);	
}

// Email - VAR_COMMON_EMAILFORMAT
function getValidateEmail(value)
{
	var checkEmail = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
	return checkEmail.test(value);
}

// phone - VAR_COMMON_PHONEFORMAT
function getValidatePhone(value)
{
	var checkPhone = /^([0-9]{2,3})([0-9]{3,4})([0-9]{4})$/;
	return checkPhone.test(value);	
}

//adcAlert 2012.01.25
function checkNumAlert()
{
//	var regexp = /[^0-9]$/;         
//	var regexp = /^[0-9]/;
	var regexp = /[0-9]/;
	
	return regexp.test();
}

// 현재년도
function getCurrYear()
{
	var thisDate = new Date();
	var curYear = thisDate.getFullYear();
	
	return curYear;		
}

function getCompareDate(defaultDate, b)
{
	var thisDate = new Date();
	var currentDate = new Date(defaultDate);
	var preDay = thisDate.getDate();
	
	currentDate.setDate(preDay - b);
	
	var monthDate = "";
	var dayDate = "";
	
	if(currentDate.getMonth() < 10)
	{
		monthDate = '0'+ (currentDate.getMonth()+1);
	}
	else
	{
		monthDate = (currentDate.getMonth()+1);
	}
	
	if(currentDate.getDate() < 10)
	{
		dayDate = "0"+ currentDate.getDate();
	}	
	else
	{
		dayDate = currentDate.getDate();
	}
	
	var compareDate = currentDate.getFullYear() + "-" + (monthDate) + "-" + dayDate;
	return compareDate;
}

//현재 시분초
function getTime()
{
	var today = new Date();
	var hourTime = 0;
	var minuteTime = 0;
	var secondTime = 0;
	
	if (today.getHours() < 10)
	{
		hourTime = '0'+today.getHours();
	}
	else
	{
		hourTime = today.getHours();
	}
	
	if (today.getMinutes() < 10)
	{
		minuteTime = '0'+today.getMinutes();
	}	
	else
	{
		minuteTime = today.getMinutes();
	}
	
	if (today.getSeconds() < 10)
	{
		secondTime = '0'+today.getSeconds();
	}
	else
	{
		secondTime = today.getSeconds();
	}
	
	var timeValue = hourTime + '' + minuteTime + '' + secondTime;
	
	return timeValue;
}

//숫자 천단위 컴마 넣기 정규식 함수
function addComma(value)
{
	var reg = /(^[+-]?\d+)(\d{3})/;
	value += '';
	while(reg.test(value))
	{
		value = value.replace(reg, '$1' + ',' + '$2');
	}
	return value;
}

//콤마찍기
function comma(str) 
{
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function addThousandSeparatorCommas(num) 
{
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// prototype.startsWith
// http://mths.be/startswith v0.2.0 
if (!String.prototype.startsWith) {
	  (function() {
	    'use strict'; // needed to support `apply`/`call` with `undefined`/`null`
	    var defineProperty = (function() {
	      // IE 8 only supports `Object.defineProperty` on DOM elements
	      try {
	        var object = {};
	        var $defineProperty = Object.defineProperty;
	        var result = $defineProperty(object, object, object) && $defineProperty;
	      } catch(error) {}
	      return result;
	    }());
	    var toString = {}.toString;
	    var startsWith = function(search) {
	      if (this == null) {
	        throw TypeError();
	      }
	      var string = String(this);
	      if (search && toString.call(search) == '[object RegExp]') {
	        throw TypeError();
	      }
	      var stringLength = string.length;
	      var searchString = String(search);
	      var searchLength = searchString.length;
	      var position = arguments.length > 1 ? arguments[1] : undefined;
	      // `ToInteger`
	      var pos = position ? Number(position) : 0;
	      if (pos != pos) { // better `isNaN`
	        pos = 0;
	      }
	      var start = Math.min(Math.max(pos, 0), stringLength);
	      // Avoid the `indexOf` call if no match is possible
	      if (searchLength + start > stringLength) {
	        return false;
	      }
	      var index = -1;
	      while (++index < searchLength) {
	        if (string.charCodeAt(start + index) != searchString.charCodeAt(index)) {
	          return false;
	        }
	      }
	      return true;
	    };
	    if (defineProperty) {
	      defineProperty(String.prototype, 'startsWith', {
	        'value': startsWith,
	        'configurable': true,
	        'writable': true
	      });
	    } else {
	      String.prototype.startsWith = startsWith;
	    }
	  }());
	}


//var SecurityNoticeWnd =  Class.create({
//	initialize : function() {
//		this.isPropertySet = false;
//	},
//	popUpIfPropertySet : function(onCloseFn) {
//		with (this) {
//			ajaxManager.runJson({
//				url : "showsSecurityNotice.action",
//				data : {
//				},
//				successFn : function(data) {		//alert(data.isSecurityNoticeShown);
//					FlowitUtil.log("isSecurityNoticeShown is : ", data.isSecurityNoticeShown);
//					if (data.isSecurityNoticeShown)
//						popUp(onCloseFn);
//					else {
//						if (onCloseFn)
//							onCloseFn();
//					}
//				}
//			});
//		}
//	},
//	popUp : function(onCloseFn) {
//		with (this) {
//			var $pop = null;
//			// 팝업창 열기
//			$pop = $("#noticePop").clone();
//			$pop.addClass('cloneDiv');
//			$pop.css('width', "380px");
//			$('body').append("<div class='popup_type1'></div>");
//			$('body').append($pop);
//			$pop.show();// .draggable();
//			// 팝업창 정중앙 위치
//			$pop.css({
//			    'left' : ($(window).width() - $pop.width()) / 2,
//			    'top' : ($(window).height() - $pop.height()) / 2
//			});
//			$('.popup_type1').height($('body').height());
//			_registerEvents(onCloseFn);
//		}
//	},
//	_registerEvents : function(onCloseFn) {
//		$('.cloneDiv .closeWndLnk').click(function(e) {
//			e.preventDefault();
//			$('.popup_type1').remove();
//			$('.cloneDiv').remove();
//			if (onCloseFn)
//				onCloseFn();
//		});
//	}
//});
//================================================================================================================================================
