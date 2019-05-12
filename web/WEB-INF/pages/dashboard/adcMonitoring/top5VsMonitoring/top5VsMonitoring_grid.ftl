<div class="monitor_con6-1">	
	<div class="monitor_con6_B">  
		<table class="table200" cellpadding="0" cellspacing="0">
	        <colgroup>
				<col width="40%"/>
	            <col width="14%"/> 
	            <col width="23%"/>
	            <col width="23%"/>                        
			</colgroup>
		    <tr>
		    	<th class="table200_th-L">Top5 Virtual Server</th>
		    	<th class="table200_th-L">포트</td>
		    	<th class="table200_th-L">Connection</th>
		    	<th class="table200_th-L">Throughput</th>
		    </tr>    	
	    	<#list dashTop5VSList![] as top5VSList>			
				<tr class="textOver" title="ADC이름:${top5VSList.adcName!}&nbsp;IP주소:${top5VSList.adcIP!}" height="24px";>
				<#if top5VSList_index ==0>
					<td class="text-align-left10 textOver"><img src="../../../imgs/dashboard/num2_1.gif">&nbsp; ${top5VSList.nameIp!""}</td>
				 	<td><#setting number_format="0.####">${top5VSList.port!""}</td>
					<td>${top5VSList.connection.value!""}</td>
					<td>${top5VSList.throughput.value!""}</td>
				<#elseif top5VSList_index ==1>
					<td class="text-align-left10 textOver"><img src="../../../imgs/dashboard/num2_2.gif">&nbsp; ${top5VSList.nameIp!""}</td>
				 	<td>${top5VSList.port!""}</td>
					<td>${top5VSList.connection.value!""}</td>
					<td>${top5VSList.throughput.value!""}</td>
				<#elseif top5VSList_index ==2>
					<td class="text-align-left10 textOver"><img src="../../../imgs/dashboard/num2_3.gif">&nbsp; ${top5VSList.nameIp!""}</td>
				 	<td>${top5VSList.port!""}</td>
					<td>${top5VSList.connection.value!""}</td>
					<td>${top5VSList.throughput.value!""}</td>
				<#elseif top5VSList_index ==3>
					<td class="text-align-left10 textOver"><img src="../../../imgs/dashboard/num2_4.gif">&nbsp; ${top5VSList.nameIp!""}</td>
				 	<td>${top5VSList.port!""}</td>
					<td>${top5VSList.connection.value!""}</td>
					<td>${top5VSList.throughput.value!""}</td>
				<#elseif top5VSList_index ==4>
					<td class="text-align-left10 textOver"><img src="../../../imgs/dashboard/num2_5.gif">&nbsp; ${top5VSList.nameIp!""}</td>
				 	<td>${top5VSList.port!""}</td>
					<td>${top5VSList.connection.value!""}</td>
					<td>${top5VSList.throughput.value!""}</td>
				<#else>
				 	<!-- 에러	 -->
				</#if>
				</tr>			 		
			</#list>   
		</table>
	</div>
</div>