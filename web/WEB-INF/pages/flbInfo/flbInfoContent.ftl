<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_filterInfo.gif" class="title_h3" />				
	</div>
<div class="control_Board">			
	<div class="title_h4_21">
		<span>
		<li>	
		${LANGCODEMAP["MSG_FLBFILTERINFO_INTERFACE"]!}
			<select class="mar_lft5 width134 inputTextposition_nofloat" id="physicalport">
				<option value="0">${LANGCODEMAP["MSG_FLBFILTERINFO_ALLPORT"]!}</option>
					<#if physicalPortList?has_content>
						<#list physicalPortList as physicalPort>					
							<option value="${physicalPort.index}">${physicalPort.index!""}<#if physicalPort.description?has_content>: ${physicalPort.description!""}</#if>
							</option>
						</#list>	
					</#if>	
			</select>	
			<span class="total03"> 
				${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
			</span>						
		</li>		
		</span>	
	</div>

	<div class="control_positionR">
			<span class="imgOn">
				<input type="button" id="exportCssLnk" class="Btn_white export_width" value="${LANGCODEMAP["MSG_ADCLOG_OUT_FILE"]!}">
			</span>
			<span class="refeshImgOn">
				<input type="button" id="refreshLnk" class="Btn_white export_width" value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}">
			</span>
			
			<span class="imgOff none">
				<input type="button" id="exportCssLnk" class="Btn_white export_width" disabled="disabled" value="${LANGCODEMAP["MSG_ADCLOG_OUT_FILE"]!}">
			</span>
			<span class="refeshImgOff none">
				<input type="button" id="refreshLnk" class="Btn_white export_width" disabled="disabled" value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}">
			</span>	
		</div>		
</div>		
	<!----- Contents List Start ----->
	<div class="flb_Info">
		<table class="Board flbInfoList" id="flb_table" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
			<#include "flbInfoList.ftl"/>
		</table>			
	</div>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<!----- Contents List End ----->
	<div class="Board_97 disabledChk">		
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>	
</div>	
<br class="clearfix" />						
