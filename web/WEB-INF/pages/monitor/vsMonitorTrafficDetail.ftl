<div>
	<#if ((adc.type) == "Alteon") && (flbSelect == 2)>
	<h2>					
		<span class="textOver" title="${vsTrafficInfo.name!""}> <#setting number_format="0.####">Group">${vsTrafficInfo.name!""} <#setting number_format="0.####">Group</span> 
			<span></span>
	</h2>
	<#else>
	<h2>					
		<span class="textOver" title="${vsTrafficInfo.name!""}> <#setting number_format="0.####">${LANGCODEMAP["MSG_NETWORK_PORT"]!}(${vsTrafficInfo.port!0})">${vsTrafficInfo.name!""} <#setting number_format="0.####">${LANGCODEMAP["MSG_NETWORK_PORT"]!} ${vsTrafficInfo.port!0}</span> 
		<#if (vsTrafficInfo.vsMemberTrafficInfoList??)>
			<span>(${vsTrafficInfo.vsMemberTrafficInfoList?size})</span>	
		<#else>
			<span>(0)</span>
		</#if>
	</h2>	
	</#if>
	<div class="pop_contents">
	     <#if ((adc.type) == "Alteon") && (flbSelect == 2)>
	     	<div id="flbSelect">
				<ul class="tabs_3">
			   		<li>
			   			<a class="css_textCursor memberlnk" id="flbStatusMember" style="background-color:#666666; color: #fff;">
			   				<span class="">Member</span>
			   			</a>
			   		</li>
			   		<li>
			   			<a class="css_textCursor filterlnk" id="flbStatusFilter">
			   				<span class="">Filter</span>
			   			</a>
			   		</li>
				</ul>
			</div>
		</#if>	
	<!----- Contents List Start ----->
	<div class="VsMonitorTrafficTable" id="flb_table">
	</div>
		<div class="center mar_top10">	
           	<input type="button" class="popupclosebtn Btn_white" value="${LANGCODEMAP["MSG_NETWORK_POP_CLOSE"]!}" />				
		</div>		 
	</div>
</div> 
<p class="close">
    <a href="#" title="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"> 
        <img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}" />
    </a>
</p>
