<#if dashSvcData??>
<#if dashSvcData.vsCount == 0>
	<div class="adc-group" name="${dashSvcData.name!'adcName'}" index="${dashSvcData.index!''}">
<#else>
	<div class="adc-group" data-toggle="modal" data-target="#dashboardModal" name="${dashSvcData.name!'adcName'}" index="${dashSvcData.index!''}">
</#if>

	<div class="adcname textOver adcMonitoringLnk" title="${dashSvcData.name!'adcName'} (${dashSvcData.vsCount!'00'}/${dashSvcData.vsTotalCount!'00'})">
		<i class="fa fa-server" aria-hidden="true"></i>
		${dashSvcData.name!'adcName'}
		<b>(${dashSvcData.vsCount!'00'}/${dashSvcData.vsTotalCount!'00'}) (${dashSvcData.vsFilteredCount})</b>
	</div>
	
	
	<#if dashSvcData.vsList??>
	<#list dashSvcData.vsList?sort_by("vsStatus") as dashSvc>
	<#if dashSvc.vsStatus == 2>
		<#if (dashSvcData.vsDownCount gt 20) && (dashSvc_index == 0) >
		<div class="set bundle-no">
            <div class="label vs_red">V</div>
            <b>${dashSvcData.vsDownCount!'00'}</b>
        </div> 
		<#elseif (dashSvcData.vsDownCount lt 21) >
		<div class="set">
            <div class="label vs_red">V</div> 
            <div class="set_name hide_name">
                <a title="" href="#">
					<span title="${dashSvc.vsIP!''}:${dashSvc.vsPort!''}">
						<p>${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</p>
					</span>
				</a>
            ${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</div>                             
        </div>
	 	<#else>
		</#if>
		
		<#if (dashSvcData.vsDownCount gt 20) > 
        <div class="set bundle-detail hide_name textOver">
            <div class="label vs_red ">V</div>
            <div class="set_name hide_name">${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</div>
        </div>    
        </#if>  
		
	<#elseif dashSvc.vsStatus == 4>
		<#if (dashSvcData.vsPartDownCount gt 20) && (dashSvc_index == 0) >
		<div class="set bundle-no">
             <div class="label vs_orange">V</div>
            <b>${dashSvcData.vsPartDownCount!'00'}</b>
        </div>         
        <#elseif (dashSvcData.vsPartDownCount lt 21) >
        <div class="set">
            <div class="label vs_orange">V</div>  
            <div class="set_name hide_name">${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</div>
        </div> 
        <#else>
		</#if>
		
		<#if (dashSvcData.vsPartDownCount gt 20)>                           
        <div class="set  bundle-detail hide_name">
            <div class="label vs_orange ">V</div>
            <div class="set_name hide_name">${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</div>
        </div>  
        </#if>  
        
	<#else>
		<#if (dashSvcData.vsPartDownCount gt 20) >
        <div class="set bundle-no">
             <div class="label vs_orange">V</div>
            <b>${dashSvcData.vsPartDownCount!'00'}</b>
        </div>
        <div class="set  bundle-detail hide_name">
             <div class="label vs_orange">V</div>                            
            <div class="set_name hide_name">${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</div>
        </div>                                       
        <#else>
        <div class="set">
            <div class="label vs_orange">V</div>      
            <div class="set_name hide_name">${dashSvc.vsIP!''}:${dashSvc.vsPort!''}</div>                                                          
        </div> 
        </#if> 
		
	</#if>
	</#list>
	</#if>
</div>
</#if>