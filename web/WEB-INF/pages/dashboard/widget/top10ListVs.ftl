<#setting number_format="0.####">
<div class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" id="${(widgetInfo.index)!''}">
	<div class="portlet-header ui-widget-header ui-corner-all">${(widgetInfo.name)!''} -
	<span class="sub_title"> ${(widgetTarget.name)!''}</span>
	<#if (widgetInfo.moreInfoIndex) == 37>
		<span class="sub_title">[Sessions]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 38>
		<span class="sub_title">[bps In]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 39>
		<span class="sub_title">[bps Out]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 40>
		<span class="sub_title">[bps Total]</span>
	</#if>
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
	   	<table class="widget_table"  >
	   		<colgroup>
				<col width="23%"/>
				<col width="9%"/>
				<col width="9%"/>
				<col width="auto"/>
				<col width="10%"/>
				<col width="10%"/>
				<col width="10%"/>
				<col width="10%"/>				
	    	</colgroup>
			<thead>
			   	<tr>
		           	<th>Virtual Server IP</th>
			       	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYPORT"]!}</th>
			       	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYSTATUS"]!}</th>
			       	<th>${LANGCODEMAP["MSG_LAYOUT_ADC_NAME_DOUBLE"]!}</th>
			       	<th>Sessions</th>
			       	<th>bpsIn</th>
			       	<th>bpsOut</th>
			       	<th>bpsTotal</th>
		      	</tr> 
			</thead>
			<tbody>
	       		<#list top10VSTrafficList![] as vsTrafficInfo>
		        	<tr title="${LANGCODEMAP["MSG_WIDGET_ADC_NAME"]!}:${vsTrafficInfo.adcName!}&nbsp;${LANGCODEMAP["MSG_WIDGET_IP_ADDRESS"]!}:${vsTrafficInfo.adcIP!}">
		        		<td class="text-align-left10 textOver vIp">
			        		<#if vsTrafficInfo_index ==0>
			        			<img src="../../../imgs/dashboard/num_1.png">			        			
			        		<#elseif vsTrafficInfo_index ==1>
			        			<img src="../../../imgs/dashboard/num_2.png">
			        		<#elseif vsTrafficInfo_index ==2>
			        			<img src="../../../imgs/dashboard/num_3.png">
			        		<#elseif vsTrafficInfo_index ==3>
			        			<img src="../../../imgs/dashboard/num_4.png">
			        		<#elseif vsTrafficInfo_index ==4>
			        			<img src="../../../imgs/dashboard/num_5.png">
			        		<#elseif vsTrafficInfo_index ==5>
			        			<img src="../../../imgs/dashboard/num_6.png">
			        		<#elseif vsTrafficInfo_index ==6>
			        			<img src="../../../imgs/dashboard/num_7.png">
			        		<#elseif vsTrafficInfo_index ==7>
			        			<img src="../../../imgs/dashboard/num_8.png">
			        		<#elseif vsTrafficInfo_index ==8>
			        			<img src="../../../imgs/dashboard/num_9.png">
			        		<#elseif vsTrafficInfo_index ==9>
			        			<img src="../../../imgs/dashboard/num_10.png">
			        		<#else>
			        		</#if>
			        		&nbsp; ${vsTrafficInfo.nameIp}</td>
		        		</td>
		        		<td class="text-align-center vPort"><#setting number_format="0.####">${vsTrafficInfo.port}</td>
			            <td class="text-align-center"> 
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
						<td class="text-align-left10 textOver vIp">${vsTrafficInfo.adcName}</td>
						<td class="text-align-center">${vsTrafficInfo.connection.value}</td>
						<td class="text-align-center">${vsTrafficInfo.bpsIn.value}</td>
						<td class="text-align-center">${vsTrafficInfo.bpsOut.value}</td>
		                <td class="text-align-center">${vsTrafficInfo.bpsTotal.value}</td>
						<td style="display:none" class="vsIndex">${vsTrafficInfo.index}</td>
						<td style="display:none" class="vendor">${vsTrafficInfo.vendor}</td>
		        	</tr>
		   		</#list>
			</tbody>
		</table>
		<div style="border: 1px solid #ccc"></div>		    
	</div>
</div>