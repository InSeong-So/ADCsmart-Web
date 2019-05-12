<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<#if adcObject.category == 0>
			<img src="imgs/title${img_lang!""}/h3_adclog_group_all.gif" class="title_h3" />
		<#elseif adcObject.category == 1>
			<img src="imgs/title${img_lang!""}/h3_adclog_group.gif" class="title_h3" />
		<#elseif adcObject.category == 2>
			<img src="imgs/title${img_lang!""}/h3_adclog_adc.gif" class="title_h3" />
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
		<input name="searchTxt" type="text" class="searchTxt inputText_search" id="searchTxt" value="${searchKey!}" />
		</span>
		<span class="btn">
			<a class="searchLnk imgOn" href="#">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCLOG_SEARCH"]!}" />
			</a>			
			<img class="imgOff none" src="imgs/meun/btn_search_1_off.png" alt="${LANGCODEMAP["MSG_ADCLOG_SEARCH"]!}" />		
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} 
			<span class="txt_bold noticePageCountInfo">00</span>
			${LANGCODEMAP["MSG_HISTORY_COUNT"]!} 
			<span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
			&nbsp;<span class="msg_log none">※ ${LANGCODEMAP["MSG_LOG_DATA"]!} </span>
		</span>			
		<div class="control_positionR">	
			<input type="button" class="imgOn exportCssLnk Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_OUT_FILE"]!}"/>
			<input type="button" class="imgOn refreshLnk Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}"/>      
	
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_ADCLOG_OUT_FILE"]!}"/>
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}"/>    
		</div>
	</div>
	<br class="clearfix" />
	<table class="Board adcLogTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#if adcObject.category == 2>
			<#include "logTableInAdcLogContent.ftl">
		<#else>
			<#include "logTableInGroupLogContent.ftl">
		</#if>
		
		
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>	
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<div class="Board disabledChk">
		<p class="pageNavigator">
		</p>
		<div id="select1" class="pageRowCountCbx">
		</div>	
	</div>
</div>

<!-- 상세로그팝업  시작-->
<div id="adclog-popup" class="pop_type_wrap">
    <h2>${LANGCODEMAP["MSG_ADCLOG_DETAIL"]!}</h2>
    <div class="pop2_contents">        
        <div class="description condition">
            <table class="Board_100" cellpadding="0" cellspacing="0">
                <colgroup>
                    <col width="20%" />
                    <col width="30%" />
                    <col width="20%" />
                    <col width="30%" />                    
                </colgroup>
                <tr class="StartLine">
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_ADCNAME"]!}</th>
                    <td class="Lth0 adcname-area">-</td>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}</th>
                    <td class="Lth0 occurtime-area">-</td>                    
                </tr>
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>
                <tr>                    
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}</th>
                    <td class="Lth0 level-area" colspan="3">-</td>                    
                </tr>
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>                
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_CONTENT"]!}</th>
                    <td class="Lth0 content-area" colspan="3" >-</td>                
                </tr>       
                <tr class="EndLine2">
                    <td colspan="4"></td>
                </tr>
            </table>
        </div>        
        <div class="position_cT10">
           <input type="button" class="popupclosebtn Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_CLOSE"]!}"/> 
   		</div>
    	<p class="close">
        	<a href="#" title="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"> 
         	   <img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}" />
        	</a>
    	</p>
	</div>
</div>
<!-- 상세로그팝업  끝-->