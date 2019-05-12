<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<#if adcObject.category == 0>
			<img src="imgs/title${img_lang!""}/h3_adcAlertLog_all.gif" class="title_h3" />
		<#elseif adcObject.category == 1>
			<img src="imgs/title${img_lang!""}/h3_adcAlertLog_group.gif" class="title_h3" />
		<#elseif adcObject.category == 2>
			<img src="imgs/title${img_lang!""}/h3_adcAlertLog_adc.gif" class="title_h3" />
		<#else>
		</#if>
		<span class="title_h3txt title-name-area"></span>					
	</div>
	<!-- 1 -->
	<div class="control_Board">
		<span class="calendar">
			<input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		</span>
		<span class="inputTextposition">
			<input name="searchKey" type="text" class="inputText searchTxt inputText_search" id="textfield3" value="${searchKey!}" />
		</span>
		<span class="btn">
			<a class="searchLnk" href="#">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCLOG_SEARCH"]!}" />
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
			&nbsp;<span class="msg_log none">※ ${LANGCODEMAP["MSG_LOG_DATA"]!} </span>
		</span>				
        <div class="control_positionR">	
			<input type="button" id="refreshLnk"  class="imgOn Btn_white" value="${LANGCODEMAP["MSG_MONITOR_ALERT_REFRESH"]!}"/>      	
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}"/> 
		</div>
	</div>
	<br class="clearfix" />	
	<table class="Board alertLogTable" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<#if adcObject.category == 2>
			<#include "alertMonitorTableInListContent.ftl"/>
		<#else>
			<#include "alertMonitorTableInListGroupContent.ftl"/>
		</#if>
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents List End ----->
	<div class="Board disabledChk">
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>						
</div>

<!-- 상세로그팝업  시작-->
<div id="alertlog-popup" class="pop_type_wrap">
    <h2>${LANGCODEMAP["MSG_MONITOR_ALERT_DETAIL"]!}</h2>
    <div class="pop2_contents">        
        <div class="description condition">
            <table class="Board_100" cellpadding="0" cellspacing="0">
                <colgroup>
                    <col width="15%" />
                    <col width="25%" />
                    <col width="15%" />
                    <col width="18%" />                                                           
                </colgroup>
                <tr class="StartLine">
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_MONITOR_ALERT_ADC_NAME"]!}</th>
                    <td class="Lth0 adcname-area">-</td>
                    <th class="Lth2">${LANGCODEMAP["MSG_MONITOR_ALERT_OCCU_TIME"]!}</th>
                    <td class="Lth0 occurtime-area" colspan="2">-</td>                    
                </tr>
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_MONITOR_ALERT_TYPE"]!}</th>
                    <td class="Lth0 type-area">-</td>
                    <th class="Lth2">${LANGCODEMAP["MSG_MONITOR_ALERT_TIME"]!}</th>
                    <td class="Lth0 alerttime-area">-</td>
                                                         
                </tr>            
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>
                <tr>
                	<th class="Lth2">${LANGCODEMAP["MSG_MONITOR_ALERT_ACTION"]!}</th>
                    <td class="Lth0 action-area" colspan="3">-</td>
                </tr>
               	<tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>              
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_MONITOR_ALERT_CONTENT"]!}</th>
                    <td class="Lth0 event-area" colspan="3">-</td>                
                </tr>        
                <tr class="EndLine2">
                    <td colspan="4"></td>
                </tr>
            </table>
        </div>        
        <div class="position_cT10">
            <span>            
                <input class="Btn_white popupclosebtn" type="button" value="${LANGCODEMAP["MSG_MONITOR_ALERT_CLOSE"]!}">
            </span>
    </div>
    <p class="close">
        <a href="#" title="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"> 
            <img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}" />
        </a>
    </p>
</div>
<!-- 상세로그팝업  끝-->
