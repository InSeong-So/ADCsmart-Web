<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_slbScheduleList.gif" class="title_h3" />			 
	</div>
	<div class="control_Board">
		<span class="inputTextposition1">
			<input name="searchKey" type="text" class="searchTxt inputText_search"  value="${searchKey!}" />
		</span>
		<span class="btn"> 
			<a class="searchLnk" href="#">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_VSF5_LISTSEARCH"]!}" />
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>
        <div class="control_positionR">	
        	<!--
        	<span class="inputTextposition1">
				<input name="userPhone" type="text" class="phoneTxt inputText_search"  value="${userPhone!}" />
			</span>
			<span class="btnSms"> 
				<input type="button" class="sendSMS Btn_white" value="테스트 SMS전송"/>
			</span>
			-->
	    <#if (accountRole == 'system') || (accountRole == 'config')>
			<input type="button" class="imgOn addSlbScheduleLnk Btn_white" value="${LANGCODEMAP["MSG_VSF5_LISTADD"]!}"/>  	        
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_VSF5_LISTADD"]!}"/>  				
	    </#if>
			<input type="button" class="imgOn refreshLnk Btn_white status_imgon" value="${LANGCODEMAP["MSG_VSF5_LISTREFRESH"]!}"/>   
			<input type="button" class="imgOff none refreshLnk Btn_white status_imgoff" disabled="disabled" value="${LANGCODEMAP["MSG_VSF5_LISTREFRESH"]!}"/> 	  	
		</div>
	</div>
	<br class="clearfix" />

	<!----- Contents List Start ----->
	<table class="newBoard" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "slbScheduleTableInListContent.ftl"/>			
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents Page Start ----->
	<table class="Board_97 disabledChk" border="0">
	<colgroup>							                            
		<col width="5%"/>
		<col width="95%"/>
	</colgroup>	
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<tr>
		  	<td class="align_center">
	       		<input type="button" class="imgOn delSlbSchedule Btn_white_small" value="삭제"/>
		  	</td>
		  	<td></td>
		</tr>
		<td></td>
	    <td></td>
	</table>
	<!----- Contents Page End ----->
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>
</div>