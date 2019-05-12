<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_vsList.gif" class="title_h3" />				 
	</div>
	<div class="control_Board">
		<span class="inputTextposition1">
			<input name="searchKey" type="text" class="searchTxt inputText_search" value="${searchKey!}" />
		</span>
		<span class="btn"> 
			<a class="searchLnk" href="#">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_VSPAS_LISTSERACH"]!}" />
			</a>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>
        <div class="control_positionR">		
	    <#if (accountRole == 'system') || (accountRole == 'config')>
	    	<input type="button" class="imgOn addVirtualServerLnk Btn_white" value="${LANGCODEMAP["MSG_VSPAS_LISTADD"]!}"/>   
	        <input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_VSPAS_LISTADD"]!}"/>  	
	    </#if>
	        <input type="button" class="imgOn refreshLnk Btn_white status_imgon" value="${LANGCODEMAP["MSG_VSPAS_LISTREFRESH"]!}"/>  
		  	<input type="button" class="imgOff none Btn_white status_imgoff" disabled="disabled" value="${LANGCODEMAP["MSG_VSPAS_LISTREFRESH"]!}"/> 
		</div>
	</div>
	<br class="clearfix" />
	
	<!----- Contents List Start ----->
	<table class="Board virtualSvrTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "virtualSvrTableInpasListContent.ftl"/>
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
    <div class="notadc-msg none">${LANGCODEMAP["MSG_NOTSUPPORT_ADC"]!}</div>
    <div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents Page Start ----->
	<table class="Board_97 disabledChk" border="0">
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<tr>
		  	<td>
		  		<#if accountRole == 'vsAdmin'>
		         		<input type="button" class="imgOn enableVssLnk Btn_white_small" value="Enable"/> 	  		    
	  		    		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Enable"/>
		         		<input type="button" class="imgOn disableVssLnk Btn_white_small" value="Disable"/> 
		         		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Disable"/>
		  		<<#elseif accountRole != 'readOnly' && accountRole != 'rsAdmin'>
		         		<input type="button" class="imgOn enableVssLnk Btn_white_small" value="Enable"/> 	  		    
	  		    		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Enable"/>
		         		<input type="button" class="imgOn disableVssLnk Btn_white_small" value="Disable"/> 
		         		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Disable"/>
		         		<input type="button" class="imgOn delVssLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSPAS_LISTDEL"]!}"/>
		         		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="${LANGCODEMAP["MSG_VSPAS_LISTDEL"]!}"/>
		       	</#if>
	       	</td>
		</tr>
		<td></td>
	    <td ></td>
	</table>
	<!----- Contents Page End ----->
	
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>
</div>