<input type="hidden" class="ticker-countset" value="${tickerData.alertCount}">
<#if tickerData.alertCount != 0>

	<#if tickerData.alertList[0].objectType == 1>  <!-- ADC -->
		<ul id="ticker" title="${LANGCODEMAP["MSG_LAYOUT_ADC_NAME_DOUBLE"]!} : ${tickerData.alertList[0].adcName!''}&nbsp;&#13;message : ${tickerData.alertList[0].event!''}">
	<#elseif tickerData.alertList[0].objectType == 2>  <!-- VS -->
		<ul id="ticker" title="${LANGCODEMAP["MSG_LAYOUT_ADC_NAME_DOUBLE"]!} : ${tickerData.alertList[0].adcName!''}&nbsp;&#13;${LANGCODEMAP["MSG_LAYOUT_VS_NAME_DOUBLE"]!} : ${tickerData.alertList[0].object!''}&nbsp;&#13;message : ${tickerData.alertList[0].event!''}">
	<#elseif tickerData.alertList[0].objectType == 3>   <!-- MEMBER -->
		<ul id="ticker" title="${LANGCODEMAP["MSG_LAYOUT_ADC_NAME_DOUBLE"]!} : ${tickerData.alertList[0].adcName!''}&nbsp;&#13;${LANGCODEMAP["MSG_LAYOUT_MEMBER_NAME_DOUBLE"]!} : ${tickerData.alertList[0].object!''}&nbsp;&#13;message : ${tickerData.alertList[0].event!''}">
	<#elseif tickerData.alertList[0].objectType == 4>  <!-- INTERFACE -->
		<ul id="ticker" title="${LANGCODEMAP["MSG_LAYOUT_ADC_NAME_DOUBLE"]!} : ${tickerData.alertList[0].adcName!''}&nbsp;&#13;${LANGCODEMAP["MSG_LAYOUT_INTERFACE_NAME_DOUBLE"]!} : ${tickerData.alertList[0].object!''}&nbsp;&#13;message : ${tickerData.alertList[0].event!''}">
	<#elseif tickerData.alertList[0].objectType == 9>  <!-- ETC -->
		<ul id="ticker" title="${LANGCODEMAP["MSG_LAYOUT_ADC_NAME_DOUBLE"]!} : ${tickerData.alertList[0].adcName!''}&nbsp;&#13;${LANGCODEMAP["MSG_LAYOUT_ETC"]!} : ${tickerData.alertList[0].object!''}&nbsp;&#13;message : ${tickerData.alertList[0].event!''}">
	<#else>
	</#if>
	<span class="wicon"><img src="/imgs/icon/warning.png" alt="warning"></span>
	<span class="wtotal refresh_ticker">${LANGCODEMAP["MSG_LAYOUT_TOTAL_ALERT"]!}: ${tickerData.alertCount!}건</span>
	<button type="button" id="ticker_del_btn">${LANGCODEMAP["MSG_LAYOUT_DEL"]!}</button>
	<li style="display: list-item;" class="textOver refresh_ticker">
		<a href="#" class="alertMsgPop">${tickerData.alertList[0].title!''}</a>
	</li>	
	</ul>
	
<#else>
</#if>