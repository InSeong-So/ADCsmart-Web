<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_serviceGroupList.gif" class="title_h3"/>		
	</div>
	<div class="control_Board">
		<p class="cont_sch">
			<span class="inputTextposition1" >
				<input name="searchTxt" type="text" class="searchTxt inputText_search" id="searchTxt" value="${searchKey!}">
			</span>
			<span class="btn">
				<a href="javascript:;" class="searchLnk">
					<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_USERLIST_SEARCH"]!}"/>
				</a>
			</span>
			<span class="total01"> 
				${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
				&nbsp;<span class="msg_log none">※ ${LANGCODEMAP["MSG_LOG_DATA"]!} </span>
			</span>
			<#if accountRole! == 'system'>	
			<div class="control_positionR">
				<input type="button" class="addServiceGrpLnk Btn_white" value="${LANGCODEMAP["MSG_USERLIST_ADD"]!}"/>  			
		    </div>
			</#if>
		</p>
	</div>
	<br class="clearfix" />
	<!----- Contents List Start ----->
	<table class="Board" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<#include "serviceGrpTableInListContent.ftl">
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>	
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<table class="Board_97" >
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
				  		<input type="button" class="delGroupsLnk Btn_white_small" value="${LANGCODEMAP["MSG_USERLIST_DELETE"]!}"/>  
					</#if>
 			</td>
			<td>&nbsp; </td>
		</tr>
	</table>
	<!----- Contents Page End ----->
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>
</div>