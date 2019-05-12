<#if dashSvcData??>
<div class="adc-group-none" index="${dashSvcData.index!''}">
	<div class="adcname textOver adcMonitoringLnk" title="${dashSvcData.name!'adcName'} (${dashSvcData.vsCount!'00'}/${dashSvcData.vsTotalCount!'00'})" style="cursor:pointer;">
		<i class="fa fa-server" aria-hidden="true"></i>
		${dashSvcData.name!'adcName'}
		<b>(${dashSvcData.vsCount!'00'}/${dashSvcData.vsTotalCount!'00'})</b>
	</div>
	<div class="set"></div> 
</div>
</#if>