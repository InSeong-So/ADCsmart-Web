<div class="monitor_con4-1">	
	<div class="monitor_con4_B">
		<table class="table200" cellpadding="0" cellspacing="0" >
	        <colgroup>
				<col width="30%"/>
	            <col width="35%"/>
	            <col width="35%"/>                                      
			</colgroup>
		    <tr>
	    		<th class="table200_th-L">구분</th>
	    		<th class="table200_th-L">전체(bps)</th>
	    		<th class="table200_th-L">전일(bps)</th>
	  		</tr>	  		
	  		<tr height="30px";>
				<td>최근</td>
				<td>${dashAdcTrafficStatus.currTotal.value}</td>
				<td>${dashAdcTrafficStatus.prevTotal.value}</td>
			</tr>
	  		<tr height="30px";>
				<td>최대</td>
				<td>${dashAdcTrafficStatus.currMax.value!""}</td>
				<td>${dashAdcTrafficStatus.prevMax.value!""}</td>
			</tr>
	  		<tr height="30px";>
				<td>최소</td>
				<td>${dashAdcTrafficStatus.curMin.value!""}</td>
				<td>${dashAdcTrafficStatus.prevMin.value!""}</td>
			</tr>
	  		<tr height="30x";>
				<td>평균</td>
				<td>${dashAdcTrafficStatus.currAvg.value!""}</td>
				<td>${dashAdcTrafficStatus.prevAvg.value!""}</td>
			</tr>
		</table>
	</div>
	<div class="monitor_con4_C">
		<table class="table200" cellpadding="0" cellspacing="0" style="table-layout: fixed;" >
	    	<colgroup>
				<col width="60%"/>
	            <col width="40%"/>                       
			</colgroup>
		    <tr>
		    	<th class="table200_th-L">Top5 ADC</th>
		    	<th class="table200_th-L">트래픽(bps)</th>
		    </tr>
	    	<#list dashTop5AdcTrafficList![] as test>		
				<tr height="24px">
					<td class="text-align-left10 textOver" title="ADC이름:${test.nameIp!""}"> 
					<#if test_index ==0>
						<img src="../../../imgs/dashboard/num2_1.gif">&nbsp; ${test.nameIp!""}</td>
						<td>${test.throughput.value!""}</td>
					<#elseif test_index ==1>
						<img src="../../../imgs/dashboard/num2_2.gif">&nbsp; ${test.nameIp!""}</td>
						<td>${test.throughput.value!""}</td>
					<#elseif test_index ==2>
						<img src="../../../imgs/dashboard/num2_3.gif">&nbsp; ${test.nameIp!""}</td>
						<td>${test.throughput.value!""}</td>
					<#elseif test_index ==3>
						<img src="../../../imgs/dashboard/num2_4.gif">&nbsp; ${test.nameIp!""}</td>
						<td>${test.throughput.value!""}</td>
					<#elseif test_index ==4>
						<img src="../../../imgs/dashboard/num2_5.gif">&nbsp; ${test.nameIp!""}</td>
						<td>${test.throughput.value!""}</td>
					<#else>
					 	<!-- 에러	 -->
					</#if>
				</tr>
			</#list>    	
	  	</table>
	</div>
</div>