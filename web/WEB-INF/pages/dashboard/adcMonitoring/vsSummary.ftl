<div class="monitor_con1">
	<li class="title_h5-2">
		<span>
			<#if (curCategory == 0) >
				전체
			<#elseif (curCategory == 1) >
			${curName}
			<#elseif (curCategory == 2) > 
				<#if (curVendor == 1)> 					
					<img alt="" src="/imgs/icon/adc/icon_f5_s.png"> ${curName}
				<#elseif (curVendor == 2)> 					
					<img alt="" src="/imgs/icon/adc/icon_alteon_s.png"> ${curName} 
				<#elseif (curVendor == 3)> 					
					<img alt="" src="/imgs/icon/adc/icon_piolink_s.png"> ${curName} 
				<#elseif (curVendor == 4)> 					
					<img alt="" src="/imgs/icon/adc/icon_piolink_s.png"> ${curName} 
				<#else> 
					&nbsp; 
				</#if>
			<#else> 
				&nbsp;
			</#if>
		</span>
	</li>	
		<br class="clearfix" />	
			<span class="monitor_sum1">
				<#if (adcTotalVSSummary.avail.diff > 0) >
					<img src="/imgs/dashboard/up.png">${(adcTotalVSSummary.avail.diff)!0}
				<#elseif (adcTotalVSSummary.avail.diff = 0) >
					
				<#else>
					<img src="/imgs/dashboard/down.png">${(adcTotalVSSummary.avail.diff)!0}
				</#if>	
			</span>
			<span class="monitor_sum2">
				<#if (adcTotalVSSummary.unavail.diff > 0) > 
					<img src="/imgs/dashboard/up.png">${(adcTotalVSSummary.unavail.diff)!0}
				<#elseif (adcTotalVSSummary.unavail.diff = 0)>
				
				<#else>
					<img src="/imgs/dashboard/down.png">${(adcTotalVSSummary.unavail.diff)!0}
				</#if>
			</span>	
		<br class="clearfix" />		
			<span class="monitor_sum3">
				<#if (adcTotalVSSummary.disable.diff > 0) >
					<img src="/imgs/dashboard/up.png">${(adcTotalVSSummary.disable.diff)!0}
				<#elseif (adcTotalVSSummary.disable.diff = 0) >	
							
				<#else>
					<img src="/imgs/dashboard/down.png">${(adcTotalVSSummary.disable.diff)!0}
				</#if>
			</span>
			<span class="monitor_sum4">
				<#if (adcTotalVSSummary.unavailOverNDays.diff > 0) >
					<img src="/imgs/dashboard/up.png">${(adcTotalVSSummary.unavailOverNDays.diff)!0}
				<#elseif (adcTotalVSSummary.unavailOverNDays.diff = 0) >
				
				<#else>
					<img src="/imgs/dashboard/down.png">${(adcTotalVSSummary.unavailOverNDays.diff)!0}
				</#if>
			</span>

		<br class="clearfix" />				
			<span class="monitor_sum1_area" id="monitor_sum1">${(adcTotalVSSummary.avail.value)!0}</span>	
			<span class="monitor_sum2_area" id="monitor_sum2">${(adcTotalVSSummary.unavail.value)!0}</span>
		<br class="clearfix" />
			<span class="monitor_sum3_area" id="monitor_sum3">${(adcTotalVSSummary.disable.value)!0}</span>	
			<span class="monitor_sum4_area" id="monitor_sum4">${(adcTotalVSSummary.unavailOverNDays.value)!0}</span>

</div>
<div class="monitor_con2">
	<li class="title_h5-3">Top10 Virtual Server</li>
   	<table class="table_VS10" cellspacing="0" >
   		<colgroup>
			<col width="34%"/>
			<col width="10%"/>
			<col width="10%"/>
			<col width="23%"/>
			<col width="23%"/>                         
    	</colgroup>
	   	<tr>
           	<td class="table_VS10_th">Virtual Server IP</td>
           	<td class="table_VS10_th">포트</td>
           	<td class="table_VS10_th">상태</td>
           	<td class="table_VS10_th">Connection</td>
           	<td class="table_VS10_th">Throughput</td>
      	</tr>
       	<#list adcTop10VSTrafficListConversion![] as vsTrafficInfo>
        	<tr title="ADC이름:${vsTrafficInfo.adcName!}&nbsp;IP주소:${vsTrafficInfo.adcIP!}">
        		<td class="text-align-center textOver vIp table_VS10_td">${vsTrafficInfo.nameIp}</td>
        		<td class="vPort table_VS10_td"><#setting number_format="0.####">${vsTrafficInfo.port}</td>
	            <td class="table_VS10_td"> 
	            	<#if vsTrafficInfo.status == 0>
	            		<img src="/imgs/icon/icon_vs_disabled.png" >
					<#elseif vsTrafficInfo.status == 1>
						<img src="/imgs/icon/icon_vs_conn.png">
					<#elseif vsTrafficInfo.status == 2>
						<img src="/imgs/icon/icon_vs_disconn.png">
					<#else>
						<img src="/imgs/icon/icon_vs_disconn.png">
					</#if>
				</td>
				<td class="text-align-center table_VS10_td">
					<div style="width:99%">
	                   	<div style="width:50%;float:left;text-align:right;">
	                       	<a class="trafficConnection" href="#"><img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" /></a>
	                   	</div>
	                   	<div style="width:50%;float:left;text-align:left">    
	                      	&nbsp;${vsTrafficInfo.connection.value}
	                   	</div>
                   	</div>
                 </td>
                 <td class="text-align-center table_VS10_td">
                   	<div style="width:99%">
                   		<div style="width:50%;float:left;text-align:right;">
                       		<a class="trafficThroughput" href="#"><img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" /></a>
                   		</div>
                   		<div style="width:50%;float:left;text-align:left">    
                       		&nbsp;${vsTrafficInfo.throughput.value}
                   		</div>
                   	</div>
                </td>
				<td style="display:none" class="vsIndex">
					${vsTrafficInfo.index}
				</td>
				<td style="display:none" class="vendor">
					${vsTrafficInfo.vendor}
				</td>		              
	            <!--
	            <td><img src="/imgs/icon/icon_2d_1.png" width="12" height="12"></td>
	            -->
        	</tr>  
	   	</#list>
    </table>
</div>


