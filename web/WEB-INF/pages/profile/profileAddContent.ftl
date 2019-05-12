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
					<a class="vServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_vs_1.gif" />												
					</a>
				</td>
				<td>
					<img src="imgs/meun${img_lang!""}/3depth_profile_0.gif" />					
				</td>
				<td>
					<a class="rServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_rs_1.gif" />												
					</a>
				</td>
				<#if accountRole != 'readOnly'>				
				<td>
					<a class="noticeServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_notice_1.gif" />												
					</a>
				</td>
				</#if>
				<td>&nbsp;</td>					
			</tr>
		</table>	
	<div>  
-->
<div> 
	<#if !(profileAdd.index)??>
		<img src="imgs/title${img_lang!""}/h3_profileAdd.gif" class="title_h3" />		 
	<#else>
		<img src="imgs/title${img_lang!""}/h3_profileModify.gif" class="title_h3" />
	</#if>
</div>

<form id="profileAddFrm" method="post">
	<!----- Contents List Start ----->
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px"/>
			<col >
		</colgroup>
		<tr class="StartLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_PROFILE_NAME"]!}</li>
			</th>
			<td class="Lth0">
				<#if ((profileAdd.index)!'') == ''>
					<input class="inputText width160" id="textfield4" name="profileAdd.name" type="text" value="${profileAdd.name!''}"/>
				<#else>
					<input class="inputText width160" id="textfield4" disabled="disabled" type="text" value="${profileAdd.name!''}"/>
					<input class="inputText width160" id="textfield4" name="profileAdd.name" type="hidden" value="${profileAdd.name!''}"/>
				</#if>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th  class="Lth2">
				<li>Persistence Type</li>
			</th>
			<td class="Lth0">
				<select name="profileAdd.persistenceType" id="select" class="inputSelect width164">
					<option value="SourceAddressAffinity" selected="selected">Source Address Affinity</option>					
				</select>
			</td>
		</tr>
		<#if ((profileAdd.index)!'') == '' || ((profileAdd.parentProfileName)!'') != ''>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Parent Profile</li>
			</th>
			<td class="Lth0">
				<select name="profileAdd.parentProfileName" id="select2" class="inputSelect width164">
					<#if ((profileAdd.index)!'') != '' && ((profileAdd.parentProfileName)!'') != ''>
						<option value="${profileAdd.parentProfileName!''}" selected="selected">${profileAdd.parentProfileName!''}</option>
					</#if>
					<#if ((profileAdd.index)!'') == ''>
					<option value="source_addr" selected="selected">source_addr</option>
					</#if>
				</select>
			</td>
		</tr>
		</#if>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Match Across Services</li>
			</th>
			<td class="Lth0">
				<#if accountRole == 'readOnly'>
					<input id="ch1" name="profileAdd.isMatchAcrossServices" type="checkbox" value="true" ${(profileAdd.isMatchAcrossServices!false)?string('checked="checked"', '')} disabled="disabled"/> 
				<#else>	
					<input id="ch1" name="profileAdd.isMatchAcrossServices" type="checkbox" value="true" ${(profileAdd.isMatchAcrossServices!false)?string('checked="checked"', '')}/>
				</#if>
				<label for="ch1">${LANGCODEMAP["MSG_PROFILE_CHECK"]!}</label>
			</td>
	    </tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Timeout</li>
			</th>
			<td class="Lth0">
				<#if accountRole == 'readOnly'>
					<input type="text" name="profileAdd.timeOutInSec" id="textfield" class="inputText width160" value="${profileAdd.timeOutInSec!180}" disabled="disabled"/> ${LANGCODEMAP["MSG_PROFILE_SECOND"]!}
				<#else>	
					<input type="text" name="profileAdd.timeOutInSec" id="textfield" class="inputText width160" value="${profileAdd.timeOutInSec!180}"/> ${LANGCODEMAP["MSG_PROFILE_SECOND"]!}
				</#if>
			</td>
		</tr>
		<tr class="EndLine2">
			<td colspan="2"></td>
		</tr>
	    <td colspan="4">                            
	    	<div class="position_cT10">
				<#if accountRole != 'readOnly' && accountRole != 'vsAdmin' && accountRole != 'rsAdmin'>
					<span class="imgOn"><input class="Btn_red okProfileAddLnk" type="button" value="${LANGCODEMAP["MSG_PROFILE_COMPLETE"]!}"></span>
				</#if>	
	    		<span class="imgOn"><input class="Btn_white cancelProfileAddLnk" type="button" value="${LANGCODEMAP["MSG_PROFILE_CANCEL"]!}"></span>
	    		
	    		<span class="imgOff none">
						<input class="Btn_white cancelProfileAddLnk" type="button" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}">
					</span>	
	        </div> 
	    </td>
	</table>
</form>
</div>