<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_reportList.gif" class="title_h3"/>				 
	</div>
	<div class="control_Board">
		<span class="calendar" >			
		 <input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		</span>
		<span class="inputTextposition" >
			<input name="reportSearchTxt" class="searchTxt inputText_search" type="text" value="${searchKey!}"/>
		</span>
		<span class="btn">
			<a class="searchLnk" href="#">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_REPORT_LISTSEARCH"]!}"/>
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>		
		<#if accountRole! != 'readOnly' && accountRole! != 'vsAdmin' && accountRole! != 'rsAdmin'>
        <div class="control_positionR">	
		<input type="button" class="imgOn reportAddLnk Btn_white" value="${LANGCODEMAP["MSG_REPORT_ADD"]!}"/>      
		<input type="button" id="refresh" class="imgOn Btn_white" value="${LANGCODEMAP["MSG_REPORT_REFLESH"]!}"/>
		 		            
    	<input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_REPORT_ADD"]!}"/>      
		<input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_REPORT_REFLESH"]!}"/> 		
    	</div>
        </#if>
	</div>
	<table class="Board reportList_tblreportTable reportTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "reportTableInListContent.ftl"/>
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!--	
	<table class="Board_97" border="0" >
		<tr height="5px">
			<td></td>
		</tr>
		<tr>
			<td>
				<span class="button white small">
					<button type="button" class="reportsDelLnk">${LANGCODEMAP["MSG_REPORT_DEL"]!}</button>
				</span>
			</td>
		</tr>
	</table>
	-->	
	<table class="Board_97 disabledChk" border="0" >
		<colgroup>							                            
			<col width="40px"/>
			<col width="auto"/>
		</colgroup>
		<tr height="5px">
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="center">
			<#if accountRole! != 'readOnly' && accountRole! !='vsAdmin' && accountRole! != 'rsAdmin'>
                <input type="button" class="imgOn reportsDelLnk Btn_white_small" value="${LANGCODEMAP["MSG_REPORT_DEL"]!}"/>          			
				<input type="button" class="imgOff none Btn_white_small" disabled="disabled"  value="${LANGCODEMAP["MSG_REPORT_DEL"]!}"/>  				
       		</#if>
 			</td>
			<td>&nbsp;</td>
		</tr>
	</table>	
  	<div class="Board_97 disabledChk">  	
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
    </div>
</div>
