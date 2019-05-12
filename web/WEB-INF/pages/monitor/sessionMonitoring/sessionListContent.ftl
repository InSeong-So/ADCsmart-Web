<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_monitor_sessionSearch.gif" class="title_h3"/>				
	</div>
	<form id = "searchForm" class="searchKeyword">
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px" />
			<col width="auto"/>
		</colgroup>	
		<tr class="StartLine">
			<td colspan="2" ></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_SESSION_DIAGNOSTIC_TARGET"]!}</li>
			</th>
			<td class="Lth0">
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
			<td colspan="2"></td>
		</tr>
		<#if adc.type == 'Alteon'>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_SESSION_LB"]!}</li>
				</th>
				<td class="Lth0">
					<input type="radio" name="searchOption" class="searchOption" id="selectedSLB" checked="checked" value="SLB">
					<label class="" for="selectedSLB">${LANGCODEMAP["MSG_SESSION_SLB"]!}</label>&nbsp;&nbsp;&nbsp;
					<input type="radio" name="searchOption" class="searchOption" id="selectedFLB" value="FLB">
					<label class="" for="selectedFLB">${LANGCODEMAP["MSG_SESSION_FLB"]!}</label>&nbsp;&nbsp;&nbsp;
				</td>			
			</tr>
			<tr class="DivideLine1">
				<td colspan="2"></td>
			</tr>
		<#else>
		</#if>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_SESSION_SEARCH_KEY"]!}</li>
			</th>
			<td class="divSelect0 selectKeyword">
			<div id="addSlt" class="addSlt">			
				<select name="select0" class="defaultSelect width130" >
					<option value="0">${LANGCODEMAP["MSG_SESSION_CIP"]!}</option>
					<#if selectedOption == 'SLB'>
						<option value="1">${LANGCODEMAP["MSG_SESSION_VIP"]!}</option>
					<#else>
						<option value="1">${LANGCODEMAP["MSG_SESSION_DIP"]!}</option>
					</#if>					
					<option value="2">${LANGCODEMAP["MSG_SESSION_REALIP"]!}</option>
					<option value="3">${LANGCODEMAP["MSG_SESSION_CPORT"]!}</option>
					<#if selectedOption == 'SLB'>
						<option value="4">${LANGCODEMAP["MSG_SESSION_VPORT"]!}</option>
						<option value="5">${LANGCODEMAP["MSG_SESSION_REALPORT"]!}</option>
					<#else>
						<option value="4">${LANGCODEMAP["MSG_SESSION_DPORT"]!}</option>
					</#if>
					
					<#if adc.type == 'Alteon'>
						<option value="7">${LANGCODEMAP["MSG_SESSION_AGE_MINUTE"]!}</option>
					<#else>
						<option value="6">${LANGCODEMAP["MSG_SESSION_PROTOCOL"]!}</option>
					</#if>
				</select>&nbsp;
				<span class="defaultArea">
					<input type="text" name="srcIp" class="searchLnkTxt inputText width130">
				</span>
				<a title="${LANGCODEMAP["MSG_SESSION_DEL"]!}"  id="delButton" href="#"> 
					<#if (langCode) == "ko_KR">
						<img class="delKeyword" src="/imgs/btn/btn_delkeyword.gif"/>
					<#elseif (langCode) == "en_US">
						<img class="delKeyword" src="/imgs/btn_eng/btn_delkeyword.gif"/>
					<#else>
						<img class="delKeyword" src="/imgs/btn/btn_delkeyword.gif"/>
					</#if>					
				</a>
			</div>
			<div class="addSearchKeyword" id="addSearchKeyword"> </div>	
			<span  id="addButton">	
				<span title="${LANGCODEMAP["MSG_SESSION_ADD"]!}"  id="addSelect" class="addSelect" href="#"> 
					<#if (langCode) == "ko_KR">
						<img class="addKeyword" src="/imgs/btn/btn_addkeyword.gif"/>
					<#elseif (langCode) == "en_US">
						<img class="addKeyword" src="/imgs/btn_eng/btn_addkeyword.gif"/>
					<#else>
						<img class="addKeyword" src="/imgs/btn/btn_addkeyword.gif"/>
					</#if>					
				</span>
				<!-- <button type="button" id="addSelect" class="addSelect">${LANGCODEMAP["MSG_SESSION_ADD"]!}</button>	 -->
            </span>
            <div class="position_R10B4">           
				<input type="button" class="imgOn searchLnk Btn_white" value="${LANGCODEMAP["MSG_SESSION_SEARCH"]!}"/>              
			    <input type="button" class="imgOn exportCssLnk Btn_white" value="${LANGCODEMAP["MSG_SESSION_EXPORT"]!}"/> 
			     
			   	<input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_SESSION_SEARCH"]!}"/>              
			    <input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_SESSION_EXPORT"]!}"/>  	
			</div> 
			</td>               
		</tr>

		<tr>

		</tr>		
				                                                                                                                 
		<tr class="EndLine2">
			<td colspan="2"></td>
		</tr>
	</table>
	</form>	
	<div class="control_Board">	
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>
	</div>
	<table class="Board sessionTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "sessionTableInListContent.ftl"/>
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	
  	<div class="Board_97 disabledChk">  	
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
    </div>
</div>
