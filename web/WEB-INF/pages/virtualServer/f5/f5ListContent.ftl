<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
<!--
	<div class="title_h2">
		<table class="Board100" cellpadding="0" cellspacing="0">
			<colgroup>							                            
				<col width="111px"/>
				<col width="111px"/>
				<col/>				
  	  		</colgroup>
			<tr>
				<td>
					<img src="imgs/meun${img_lang!""}/3depth_vs_0.gif" />										
				</td>
				<td>
					<a class="profileLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_profile_1.gif" />											
					</a>
				</td>
				
				<td>
					<a class="rServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_rs_1.gif" />												
					</a>
				</td>			
				<td>
					<a class="noticeServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_notice_1.gif" />												
					</a>
				</td>
				
				<td>&nbsp;</td>				
			</tr>
		</table>
	</div>
--->
	<div>
		<img src="imgs/title${img_lang!""}/h3_vsList.gif" class="title_h3" />			 
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
	    <#if (accountRole == 'system') || (accountRole == 'config')>
			<input type="button" class="imgOn addVirtualServerLnk Btn_white" value="${LANGCODEMAP["MSG_VSF5_LISTADD"]!}"/>  	        
			<input type="button" class="imgOff none Btn_white" disabled="disabled"  value="${LANGCODEMAP["MSG_VSF5_LISTADD"]!}"/>  				
	    </#if>
			<input type="button" class="imgOn refreshLnk Btn_white status_imgon" value="${LANGCODEMAP["MSG_VSF5_LISTREFRESH"]!}"/>   
			<input type="button" class="imgOff none refreshLnk Btn_white status_imgoff" disabled="disabled" value="${LANGCODEMAP["MSG_VSF5_LISTREFRESH"]!}"/> 	  	
		</div>
	</div>
	<br class="clearfix" />

	<!----- Contents List Start ----->
	<table class="Board virtualSvrTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "virtualSvrTableInF5ListContent.ftl"/>
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
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
  		     		<input type="button" class="imgOn disableVssLnk Btn_white_small" value="Disable"/>
  		     		
  		     		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Enable"/>
  		     		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Disable"/> 
		  		<#elseif accountRole != 'readOnly' && accountRole != 'rsAdmin'>
  		     		<input type="button" class="imgOn enableVssLnk Btn_white_small" value="Enable"/>
  		     		<input type="button" class="imgOn disableVssLnk Btn_white_small" value="Disable"/>
  		     		<input type="button" class="imgOn delVssLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSF5_LISTDEL"]!}"/>
		       		
  		     		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Enable"/>
  		     		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="Disable"/>
  		     		<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="${LANGCODEMAP["MSG_VSF5_LISTDEL"]!}"/> 		     		
	       		</#if>
		  	</td>
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