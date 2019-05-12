<div> 
	<img src="imgs/title/h3_unusedslb.gif" class="title_h3"/> 
</div>
<div class="Board">
	<span class="inputTextposition">
 		&nbsp;ADC 선택
 		<select name="adcIndex" id="adcType">
			<option value="0">전체</option>
	 		<#list adcList![] as theAdc>
				<option value=${theAdc.index}>${theAdc.name}</option>
	 		</#list>
 		</select>
 		&nbsp;검색 종류
 		<select name="searchType" id="searchType">
			<option value="0">전체</option>
			<option value="1">Group</option>
			<option value="2">Real Server</option>
		</select>
	</span>
	<span class="btn_new">
    	<input class="Btn_green exportCssLnk" type="button" value="&nbsp;내보내기&nbsp;">
	</span>
	<span class="btn_new">
    	<input class="Btn_green searchLnk" type="button" value="&nbsp;검색&nbsp;">
	</span>
</div>
<br class="clearfix" />
<!----- Contents List Start ----->
<table class="Board" cellpadding="0" cellspacing="0">
	<tr class="StartLine">
		<td></td>
	</tr>
	<tr class="ContentsLine3">
		<td class="align_center"> 
			<textarea  class="session_textarea" rows="25">${contents}</textarea>
		</td>
	</tr>
	<tr class="EndLine">
		<td></td>
	</tr>
</table>

