<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_monitor_L2Search.gif" class="title_h3"/>				
	</div>
	<form id = "searchForm" class="searchKeyword">
		<table class="Board" cellpadding="0" cellspacing="0">
			<caption>&nbsp;</caption>
			<colgroup>
				<col width="200px" />
				<col width="130px" />			
				<col width="auto"/>
			</colgroup>	
			<tr class="StartLine">
				<td colspan="4" ></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_L2INFO_TARGET"]!}</li>
				</th>
				<td class="Lth0" colspan="3">
					<#if adc.type == 'Alteon'>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					<#elseif adc.type == 'F5'>
						<img src="/imgs/icon/adc/icon_f5_s.png"/>
					<#elseif adc.type == 'PAS'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#elseif adc.type == 'PASK'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#else>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					</#if>
					&nbsp;<span class="txt_bold">${adc.name!""}</span> 
				</td>			
			</tr>
			<tr class="DivideLine1">
				<td colspan="4"></td>
			</tr>
			<tr>
				<th class="Lth2A"  rowspan="4">
					<li>${LANGCODEMAP["MSG_L2INFO_KEYWORD"]!}</li>
				</th>
				<td class="Lth0A">
					<input name="IPchk" type="checkbox" class="IPchk" /> IP
				</td>
				<td class="Lth0-1" colspan="2">
					<input type="text" name="searchKey_IP" id="searchTxt" class="inputText width130" disabled="disabled"/>
				</td>
			</tr>
			<tr>
				<td class="Lth0A">
					<input name="MACchk" type="checkbox" class="MACchk" /> MAC
				</td>
				<td class="Lth0-1" colspan="2">
					<input type="text" name="searchKey_MAC" id="searchTxt" class="inputText width130" disabled="disabled"/>
				</td>
			</tr>
			<tr>
				<td class="Lth0A">					
					<input name="VLANchk" type="checkbox" class="VLANchk" /> VLAN
				</td>
				<td class="Lth0-1" colspan="2">
					<input type="text" name="searchKey_VLAN" id="searchTxt" class="inputText width130" disabled="disabled"/>
				</td>
			</tr>	
			<tr>
				<td class="Lth0A">					
					<input name="IFchk" type="checkbox" class="IFchk" /> PORT
				</td>
				<td class="Lth0-1" >
					<input type="text" name="searchKey_Interface" id="searchTxt" class="inputText width130" disabled="disabled"/>
				</td>
				<td>		
                    <div class="position_R10B4">
                    <input type="button" class="imgOn searchLnk Btn_white" value="${LANGCODEMAP["MSG_L2INFO_SEARCH"]!}"/>   	
					<input type="button" class="imgOn exportCssLnk Btn_white" value="${LANGCODEMAP["MSG_L2INFO_OUT_FILE"]!}"/> 
					  
                    <input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_L2INFO_SEARCH"]!}"/>   	
					<input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_L2INFO_OUT_FILE"]!}"/> 
			      </div> 
				</td>
			</tr>					
			<tr class="EndLine2">
				<td colspan="4"></td>
			</tr>
		</table>
	</form>
	<div class="Board">
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>	
	</div>	
	<table class="Board l2Table" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "l2InfoTableInListContent.ftl"/>		
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	
  	<div class="Board_97 disabledChk">  	
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
    </div>
</div>