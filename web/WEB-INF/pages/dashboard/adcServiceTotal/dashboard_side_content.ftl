<ul class="list-group">
	<li class="list-group-item">
		<i class="fa fa-exclamation-triangle" aria-hidden="true"></i> 최근 장애 내역
	</li>
	
	<#if dashFaultStatus??>
	<#list dashFaultStatus.logList![] as logInfo>
	<li class="list-group-item">
		<b>${logInfo.content!""}</b>
		<div></div>
		<div class="badge">${logInfo.occurred!""}</div>
	</li>
	</#list> 
	</#if>

</ul>