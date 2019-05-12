<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_monitor_device_adc.gif" class="title_h3" />				
	</div>
	
	<div class="nulldata none"></div>
	<div class="offdata none"></div>
		<input type="hidden" class="AdcModelNum" value="${adcInfo.adcType}" />
		<input type="hidden" class="AdcswVersion" value="${adcInfo.swVersion}" />
		<!-- Appliance Map Area -->
		<div class="AdcModelArea"></div>
		<!-- Appliance Map Area -->
		<div class="Board_top25 disabledChk">
			<div class="title_h4_2">
				<li>${LANGCODEMAP["MSG_APPLIANCE_SYSTEM_RESO"]!}</li>
			</div>
		</div>
		<!-- Chart Content Navigator -->
		<div id="SessInfo" class="disabledChk">
			<ul class="tabs_2">	  		
		   		<li>
		   			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_All" style="background-color:#666666; color: #fff;">
		   				<span class="">${LANGCODEMAP["MSG_APPLIANCE_ALL"]!}</span>
		   			</a>
		   		</li>
		   		<li>
		   			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_CS">
		   				<span class="">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}</span>
		   			</a>
		   		</li>	   		
		   		<li>
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_Throughput">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}</span>
		  			</a>
		  		</li>
		  		<li>
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_interface">
		  				<span class="">${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}</span>
		  			</a>
		  		</li>
		   		<li class="f5Area_Content">
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_SSL">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_SSLTANSACT"]!}</span>
		  			</a>
		  		</li>
		   		<li class="f5Area_Content">
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_HTTP">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_HTTPREQUEST"]!}</span>
		  			</a>
		  		</li>	
		   		<li>
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_CPU">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_CPU"]!}</span>
		  			</a>
		  		</li>	
		   		<li>
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_Memory">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_MEMORY"]!}</span>
		  			</a>
		  		</li>	
		   		<li>
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_ErrP">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_ERROR_COUNT"]!}</span>
		  			</a>
		  		</li>	
		   		<li>
		  			<a class="adcSummary css_textCursor apCursorTap" id="chartAreaTap_DropP">
		  				<span class="">${LANGCODEMAP["MSG_APPLIANCE_LOSS_COUNT"]!}</span>
		  			</a>
		  		</li>
			</ul>
		</div>	
		<!-- Chart Content Navigator -->
		<!-- Chart Area -->
		<div class="ApplianceChartArea disabledChk"></div>
		<!-- Chart Area -->
</div>