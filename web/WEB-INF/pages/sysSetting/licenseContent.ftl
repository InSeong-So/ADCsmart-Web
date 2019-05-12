<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
<div>
	<img src="imgs/title${img_lang!""}/h3_license.gif" class="title_h3"/>		 
</div>
<!--1-->
<div class="title_h4">
	<li>${LANGCODEMAP["MSG_SYSSETTING_LICENSE_STATUS"]!}</li>
</div>
<form name="form" class="setting" id="uploadform" enctype="multipart/form-data" method="post">	
<table class="Board" cellpadding="0" cellspacing="0">	
	<colgroup>
		<col width="260px"/>
		<col>
	</colgroup>
	<tr>
		<td colspan="2" class="StartLine"></td>
	</tr>						
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_STATUS_INFO"]!}</li></th>
		<td class="Lth0">${licInfo.state!}</td>
	</tr>
	<tr class="EndLine2">
		<td colspan="2"></td>
    </tr>
</table>       
<!--2-->
<div class="title_h4_1">
<li>${LANGCODEMAP["MSG_SYSSETTING_SYSTEM_STATUS"]!}</li>
</div>
<table class="Board" cellpadding="0" cellspacing="0">
    <colgroup>
		<col width="260px"/>
		<col>
	</colgroup>
	<tr>
		<td colspan="2" class="StartLine"></td>
	</tr>
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_VERSION"]!}</li></th>
		<td class="Lth0">${licInfo.version!}</td>
	</tr>
	<tr class="DivideLine"><td colspan="2"></td></tr>                                
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_MODEL_NAME"]!}</li></th>
		<td class="Lth0">${licInfo.model!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="2"></td>
	</tr>                                
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_SOFTWARE_KEY"]!}</li></th>
		<td class="Lth0">${licInfo.serial!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="2"></td>
	</tr>                                
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_MAX_ADC_MANAGE_COUNT"]!}</li></th>
		<td class="Lth0">${licInfo.maxAdcNum!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="2"></td>                                 
	</tr>
	<!--
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_MAX_VS_MANAGE_COUNT"]!}</li></th>
		<td class="Lth0">${licInfo.maxVSnum!}</td>
	</tr>
	<tr class="DivideLine"><td colspan="2"></td></tr>                                
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_MAX_USER_COUNT"]!}</li></th>
		<td class="Lth0">${licInfo.maxUserNum!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="2"></td>
	</tr>
	-->                                
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_USE_PERIOD"]!}</li></th>
		<td class="Lth0">${licInfo.period!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="2"></td>
	</tr>                                
	<tr>
		<th class="Lth1" ><li>${LANGCODEMAP["MSG_SYSSETTING_USED_MAC"]!}</i></th>
		<td class="Lth0" >${licInfo.macAddress!}</td>
	</tr>
	<tr class="DivideLine"><td colspan="2"></td></tr>                                
	<tr>
		<th class="Lth1"><li>${LANGCODEMAP["MSG_SYSSETTING_ISSUE_DATE"]!}</li></th>
		<td class="Lth0">${licInfo.issueDate!}</td>
	</tr>
    <tr class="EndLine2">
    	<td colspan="2"></td>
    </tr>
	<tr height="8px"> 
		<td colspan="2"></td>
	</tr>	
	<tr class="StartLine"> 
		<td colspan="2"></td>
	</tr>				                                           
 	<tr>
		<th class="Lth2"><li>${LANGCODEMAP["MSG_SYSSETTING_LICENSE_UPDATE"]!}</li></th>
		<td class="Lth0">
			<input type="file" name="upload" id="upload" />
			&nbsp;
			<input type="button" class="licenseUploadOkLnk Btn_white" value="${LANGCODEMAP["MSG_SYSSETTING_UPDATE"]!}"/>
		</td>
	</tr>     
	<tr class="EndLine2">
		<td colspan="2"></td>
	</tr>
</table>
</form>	
</div>								
<!----- Contents Page End ----->
