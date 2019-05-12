<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_backupList.gif" class="title_h3"/>		
	</div>
	<!-- 1 --> 
	<div class="control_Board">		
		<span class="calendar">				
		 <input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		 </span>
		<span class="inputTextposition">
			<input class="searchTxt inputText_search" type="text" value="${searchKey!}"/>
		</span>
		<span class="btn">			
			<a class="searchLnk" href="javascript:;">
				<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_SYSSETTINGBAK_SEARCH"]!}"/>
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>		
		<div class="control_positionR">	
		<input type="button" class="backupAddLnk Btn_white" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_ADD"]!}"/>  			
    	</div>
	</div>
	<br class="clearfix" />
	<table class="Board backupTable" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<#include "backupTableInListContent.ftl"/>				
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<table class="Board_97" border="0">
		<colgroup>							                            
			<col width="40px"/>
			<col width="auto"/>	
		</colgroup>
		<tr height="5px">
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="center">
					<#if accountRole! == 'system'>
						<input type="button" class="backupsDelLnk Btn_white_small" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_DEL"]!}"/>     						
					</#if>
 			</td>
			<td>&nbsp; </td>
		</tr>
	</table>
  	<div class="Board_97">  	
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>	
</div>

