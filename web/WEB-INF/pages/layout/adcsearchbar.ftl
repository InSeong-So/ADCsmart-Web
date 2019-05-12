<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="adc_search">
	<span class="inputTextposition" >
    	<input name="searchTxt" type="text" size="28" class="searchTxt inputText" value="${searchKey!}"/>
    </span>
    <span class="btn">
    	<a class="searchLnk" href="#">  
			<img src="imgs/meun${img_lang!""}/btn_search_1.png" alt="${LANGCODEMAP["MSG_LAYOUT_SEARCH"]!}"/>			
    	</a>
    </span>
</div>

<div class="adc_search_1 none">
	<span class="inputTextposition">    	
    </span>
    <span class="btn"></span>
</div>
