<#setting number_format="0.####">
<div class="portlet-header ui-widget-header ui-corner-all">${(widgetInfo.name)!''} -
	<span class="sub_title"> ${(widgetTarget.name)!''}</span>
		<a class="" href="#"><span class="ui-icon ui-icon-closethick "></span></a>
		<a class="" href="#"><span class="ui-icon ui-icon-gear "></span></a>
		<div style="display: none;">
			<input name="widgetInfo.name" class="widgetName" type="text" value="${widgetInfo.name!''}"/>
			<input name="widgetInfo.type" class="widgetType" type="text" value="${widgetInfo.type!''}"/>
			<input name="widgetInfo.width" class="widgetWidth" type="text" value="${widgetInfo.width!''}"/>
			<input name="widgetInfo.height" class="widgetHeight" type="text" value="${widgetInfo.height!''}"/>
			<input name="widgetInfo.xPosition" class="widgetxPosition" type="text" value="${widgetInfo.xPosition!''}"/>
			<input name="widgetInfo.yPosition" class="widgetyPosition" type="text" value="${widgetInfo.yPosition!''}"/>
			<input name="widgetInfo.moreInfoIndex" class="moreInfoIndex" type="text" value="${widgetInfo.moreInfoIndex!''}"/>
			
			<input name="widgetTarget.category" class="targetCategory" type="text" value="${widgetTarget.category!''}"/>
			<input name="widgetTarget.index" class="targetIndex" type="text" value="${widgetTarget.index!''}"/>
			<input name="widgetTarget.strIndex" class="targetStrIndex" type="text" value="${widgetTarget.strIndex!''}"/>
			<input name="widgetTarget.name" class="targetName" type="text" value="${widgetTarget.name!''}"/>
			<input name="widgetTarget.desciption" class="targetDesciption" type="text" value="${widgetTarget.desciption!''}"/>
			
			<input name="widgetItem.widthMinSize" class="widthMinSize" type="text" value="${widgetItem.widthMinSize!''}"/>
			<input name="widgetItem.widthMaxSize" class="widthMaxSize" type="text" value="${widgetItem.widthMaxSize!''}"/>
			<input name="widgetItem.heightMinSize" class="heightMinSize" type="text" value="${widgetItem.heightMinSize!''}"/>
			<input name="widgetItem.heightMaxSize" class="heightMaxSize" type="text" value="${widgetItem.heightMaxSize!''}"/>
		</div>
</div>
<div class="portlet-content">	
	<table class="widget_table" >
		<colgroup>
			<col width="70%"/>
	        <col width="30%"/>                       
		</colgroup>
		<thead>			
		    <tr>
		    	<th>${LANGCODEMAP["MSG_WIDGET_ADC_NAME"]!}</th>
		    	<th>${LANGCODEMAP["MSG_WIDGET_TRAFFIC"]!}</th>
		    </tr>
		</thead>
		<tbody>
	    	<#list top10AdcTrafficList![] as test>
				<tr>
						<td class="text-align-left10 textOver" title="${LANGCODEMAP["MSG_WIDGET_ADC_NAME"]!}:${test.nameIp!""}"> 
						<#if test_index ==0>
							<img src="../../../imgs/dashboard/num_1.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==1>
							<img src="../../../imgs/dashboard/num_2.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==2>
							<img src="../../../imgs/dashboard/num_3.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==3>
							<img src="../../../imgs/dashboard/num_4.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==4>
							<img src="../../../imgs/dashboard/num_5.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==5>
							<img src="../../../imgs/dashboard/num_6.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==6>
							<img src="../../../imgs/dashboard/num_7.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==7>
							<img src="../../../imgs/dashboard/num_8.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==8>
							<img src="../../../imgs/dashboard/num_9.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#elseif test_index ==9>
							<img src="../../../imgs/dashboard/num_10.png">&nbsp; ${test.nameIp!""}</td>
							<td class="text-align-center">${test.throughput.value!""}</td>
						<#else>
						 	<td class="text-align-center">${test.throughput.value!""}</td>
						</#if>
					</tr>
			</#list>
		</tbody>						  	
	</table>
	<div style="border: 1px solid #ccc"></div>		  	
</div>