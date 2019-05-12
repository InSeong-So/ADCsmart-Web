<!----- Contents List Start ----->
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area" >
	<div>
		<img src="imgs/title${img_lang!""}/h3_auditLog.gif" class="title_h3"/>		 
	</div>
	<!-- 1 --> 
	<div class="control_Board">
		<span class="calendar">		
		 <input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		</span>
		<span class="inputTextposition" >
			<input name="searchTxt" type="text" class="searchTxt inputText_search" id="searchTxt "value="${searchKey!}">
		</span>
		<span class="btn">
			<a href="javascript:;;" class="searchLnk">
				<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCLOG_SEARCH"]!}"/>
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
			&nbsp;<span class="msg_log none">※ ${LANGCODEMAP["MSG_LOG_DATA"]!} </span>
		</span>		
        <div class="control_positionR">	
		<input type="button" class="exportCssLnk Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_OUT_FILE"]!}"/>
		<input type="button" class="refreshLnk Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}"/>          
		
	    </div>
	</div>	
	<table class="Board" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
  		<#include "logTableInAuditLogContent.ftl">
  	</table>
  	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>	
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
<!----- Contents List End ----->
                        
 	<!----- Contents Page Start ----->
 	<div class="Board_97">
 		<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>	
</div>

<!-- 감사로그팝업  시작-->
<div id="auditlog-popup" class="pop_type_wrap">
    <h2>${LANGCODEMAP["MSG_ADCLOG_DETAIL"]!}</h2>
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
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}</th>
                    <td class="Lth0 occurtime-area" colspan="3"></td>               
                </tr>
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_USERNAME"]!}</th>
                    <td class="Lth0 generator-area"></td>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_CONNECTIP"]!}</th>
                    <td class="Lth0 ip-area"></td>                    
                </tr>
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>                
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}</th>
                    <td class="Lth0 type-area"></td>
                    <th class="Lth2">${LANGCODEMAP["MSG_ADCLOG_IMPORTANCE"]!}</th>
                    <td class="Lth0 level-area"></td>                                        
                </tr>            
                <tr class="DivideLine">
                    <td colspan="4"></td>
                </tr>                
                <tr>
                    <th class="Lth2">${LANGCODEMAP["MSG_FAULT_CONTENT"]!}</th>
                    <td class="Lth0 content-area" colspan="3">-</td>                
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
<!-- 감사로그팝업  끝-->