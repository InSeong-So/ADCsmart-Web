<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area"> 
	<div> 
		<img src="imgs/title${img_lang!""}/h3_adminLog.gif" class="title_h3" />
	</div>
	<div class="control_Board">            
	    <div class="title_h4_21">
	        <li>${LANGCODEMAP["MSG_SYSSETTING_LOG"]!}
	            <select class="mar_lft5 width134 inputTextposition_nofloat" id="adcLogSel">
	            	<option value="" selected="selected">${LANGCODEMAP["MSG_ADMIN_LOG_SEL"]!}</option>
	                <option value="alteon">alteon.log</option>
	                <option value="f5">f5.log</option>
	                <option value="pas">pas.log</option>
	                <option value="pask">pask.log</option>
	            </select>
	        </li>
	    </div>
	<div class="control_positionR">
		<input type="button" class="refreshLnk Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_REFRESH"]!}">
	</div>        
	</div>    
	<!----- Contents List Start ----->
	<div class="Board align_center" cellpadding="0" cellspacing="0">
		<#include "logTableInContent.ftl">
	</div>
	<!----- Contents List End ----->
</div>

