<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div> 
		<img src="imgs/title${img_lang!""}/h3_diagnosisHistoryList.gif" class="title_h3"/>
	</div>
	<div class="control_Board">
		<span class="calendar">
		<input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		</span>		
		<span class="inputTextposition">
			<input class="searchTxt inputText_search" name="searchKey" type="text" value="${searchKey!}"/>
 		</span>
		<span class="btn">
			<a href="#" class="searchLnk">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_DIAG_HIS_SERACH"]!}"/>
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>					
        <div class="control_positionR">
        	<#if accountRole != 'readOnly'>	
			<input type="button" class="imgOn addFaultSet Btn_white" value="${LANGCODEMAP["MSG_DIAG_HIS_ADD"]!}"/>           	
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_DIAG_HIS_ADD"]!}"/>  	             		
	        </#if>
			<input type="button" class="imgOn refreshLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_HIS_REFRSH"]!}"/>           	
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_DIAG_HIS_REFRSH"]!}"/>			
		</div>
	</div>
	<table class="Board historyTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "historyTableInListContent.ftl"/>	
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<table class="Board_97 disabledChk" border="0" >
		<colgroup>							                            
			<col width="5%"/>
			<col width="95%"/>
		</colgroup>
		<tr height="5px">
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="center">
			<#if accountRole! != 'readOnly'>
				<input type="button" class="imgOn historyDelLnk Btn_white_small" value="${LANGCODEMAP["MSG_DIAG_HIS_DEL"]!}"/>			
       			<input type="button" class="imgOff none Btn_white_small" disabled="disabled"  value="${LANGCODEMAP["MSG_DIAG_HIS_DEL"]!}"/>  	
       		</#if>
 			</td>
			<td></td>
		</tr>
	</table>
	<div class="Board disabledChk">
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>			
</div>