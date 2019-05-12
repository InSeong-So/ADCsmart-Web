<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<form id="adcAlertFrm" class="setting" method="post">	
	<div id="SessInfo">
	<#if alarmConfig.object.category == 0>
		<img src="imgs/title${img_lang!""}/h3_loadAdcAlert_all.gif" class="title_h3" />
	<#elseif alarmConfig.object.category == 1>
		<img src="imgs/title${img_lang!""}/h3_loadAdcAlert_group.gif" class="title_h3" />
	<#else>
	</#if>
		<ul class="tabs">	  		
	   		<li>
	   			<a class="css_textCursor" id="targetTap_Alteon" style="background-color: #666; color:white;">
	   				<span class="none status">1</span>Alteon
	   			</a>
	   		</li>
	   		<li>
	  			<a class="adcSummary css_textCursor" id="targetTap_F5">
	  				<span class="none status">0</span>F5
	  			</a>
	  		</li>
	  		<li>
	  			<a class="adcSummary css_textCursor" id="targetTap_PAS">
	  				<span class="none status">0</span>PAS
	  			</a>
	  		</li>
	  		<li>
	  			<a class="adcSummary css_textCursor" id="targetTap_PASK">
	  				<span class="none status">0</span>PASK
	  			</a>
	  		</li>
		</ul>
	</div>
	<div class="alertTableArea">
		<#include "groupAlertListContentAlteon.ftl"/>	
	</div>	
	<div class="Alertset">
		<div class="set">
		<#if alertSetting.alertType == 0>
			- ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE"]!} <span class="txt_blue2">${LANGCODEMAP["MSG_ALERT_SETTING_NONE"]!}</span> ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE_LAST"]!}
		<#elseif alertSetting.alertType == 1>
			<#if alertSetting.isBeepOn == true>
				- ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE"]!} <span class="txt_blue2"> ${LANGCODEMAP["MSG_ALERT_SETTING_POPUP_BEEP"]!}</span> ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE_LAST"]!}
			<#else>
				- ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE"]!} <span class="txt_blue2"> ${LANGCODEMAP["MSG_ALERT_SETTING_POPUP_TYPE"]!}</span> ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE_LAST"]!}		
			</#if>
		<#elseif alertSetting.alertType == 2>
			- ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE"]!} <span class="txt_blue2"> ${LANGCODEMAP["MSG_ALERT_SETTING_TICKER"]!}</span> ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE_LAST"]!}
		<#else>
		</#if>
		<span class="txt_gray2">${LANGCODEMAP["MSG_ALERT_LINK_NOTICE"]!} <a href="#" class="userconfig-btn"> ${LANGCODEMAP["MSG_ALERT_LINK_USERCONFIG"]!} </a> ${LANGCODEMAP["MSG_ALERT_LINK_NOTICE_LAST"]!}</span> 
		</div>
		<div class="sysip">
		<#if envAdditional.syslogServerAddress?has_content>
		- ${LANGCODEMAP["MSG_ALERT_SYSIP_NOTICE"]!}  <span class="txt_blue2"> ${envAdditional.syslogServerAddress}</span> ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE_LAST"]!} <span class="txt_gray2">${LANGCODEMAP["MSG_ALERT_LINK_NOTICE"]!} <a href="#" class="syssetting-btn"> ${LANGCODEMAP["MSG_ALERT_LINK_SYSCONFIG"]!} </a> ${LANGCODEMAP["MSG_ALERT_LINK_NOTICE_LAST"]!}</span> 
		<#else>
		- ${LANGCODEMAP["MSG_ALERT_SYSIP_NOTICE_NONE"]!}
		</#if>
		</div>
		<div class="sysip">
		- ${LANGCODEMAP["MSG_ALERT_TIME_NOTICE"]!} <span class="txt_blue2">${LANGCODEMAP["MSG_ALERT_TIME"]!}</span> ${LANGCODEMAP["MSG_ALERT_SETTING_NOTICE_LAST"]!}
		</div>
	</div>
	<table class="Board" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="100px" />
			<col width="auto" />
			<col width="100px" />			
		</colgroup>	
		<tr>
			<td>
			</td>
			<td class="align_center" >
				<input type="button" class="adcAlertOkLnk Btn_red" value="${LANGCODEMAP["MSG_ALERT_APPLY"]!}"/> 		
			</td>
			<td></td>	
		</tr>
	</table>
</form>
</div>
<script>
function changeCheckbox(obj) 
{
	//alert(obj.checked);
	//alert(obj.id);

	if (obj.checked == true) 
	{
		$("#" + $(obj).attr("id_h")).val("1");
	}
	else 
	{
		$("#" + $(obj).attr("id_h")).val("0");
	}	
	//alert($("#" + $(obj).attr("id_h")).val());		
}
</script>
	