<#setting number_format="0.####">
<div class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" id="${(widgetInfo.index)!''}">
	<div class="portlet-header ui-widget-header ui-corner-all">${(widgetInfo.name)!''} -
	<span class="sub_title"> ${(widgetTarget.name)!''}</span>
	<#if (widgetInfo.moreInfoIndex) == 0>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_ALL"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 1>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_ENABLE"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 2>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_DISABLE"]!}]</span>	
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
	<div class="portlet-content" style="overflow-x: hidden; overflow-y: auto; height: 206px;">	
		<table class="widget_table" cellspacing="0" style="table-layout: fixed"  >
	   		<colgroup>
				<col width="11%"/>
				<col width="10%"/>
				<col width="4%"/>
				<col width="9%"/>
				<col width="5%"/>
				<col width="5%"/> 
				<col width="5%"/>
				<col width="5%"/>
				<col width="10%"/>
				<col width="9%"/>
				<col width="8%"/>
				<col width="8%"/>
				<col width="5%"/>					
				<col width="6%"/>
			</colgroup>
<thead>			
		   <tr>
	   			<th rowspan="2">${LANGCODEMAP["MSG_WIDGET_ADC_NAME"]!}</th>
	           	<th rowspan="2">IP</th>
	           	<th rowspan="2">${LANGCODEMAP["MSG_WIDGET_STATUS"]!}</th>
	           	<th rowspan="2">${LANGCODEMAP["MSG_WIDGET_UPDATE_TIME"]!}</th>
	           	<th colspan="4" class="noline">${LANGCODEMAP["MSG_WIDGET_VS_COUNT"]!}</th>
	           	<th rowspan="2">${LANGCODEMAP["MSG_WIDGET_PRODUCT_NAME"]!}</th>
	           	<th rowspan="2">${LANGCODEMAP["MSG_WIDGET_VERSION"]!}</th>
	           	<th rowspan="2">Sessions</th>
	           	<th rowspan="2">Throughput</th>
	           	<th rowspan="2">CPU</th>
	           	<th rowspan="2">Memory</th>
	  		</tr>
	  		<tr>
	        	<th>${LANGCODEMAP["MSG_WIDGET_ALL"]!}</th>
	          	<th>${LANGCODEMAP["MSG_WIDGET_ENABLE"]!}</th>
	          	<th>${LANGCODEMAP["MSG_WIDGET_DISABLE"]!}</th>
	          	<th>${LANGCODEMAP["MSG_WIDGET_OFF"]!}</th>
	        </tr>
</thead>
<tbody>	 	        
	       	<#if adcSummaryList??>
			<#list adcSummaryList as adcSummary>
				<#if 0 == adcSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_1d_disconn.png">
					<#assign statusText = "Disabled">
				</#if>
				<#if 1 == adcSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_1d_conn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_WIDGET_ENABLE']!}">   
				</#if>
				<#if 2 == adcSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_1d_disconn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_WIDGET_DISABLE']!}">
				</#if>
				<#if 3 == adcSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_1d_disconn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_WIDGET_ABNORMAL']!}">
				</#if>	
				
        		<tr class="adcSummaryListRow">
              		<td class="text-align-left10 textOver" title="${adcSummary.name}">${adcSummary.name!""}
						<span class="none adcIndex">${adcSummary.index!}</span>						
					</td>
             		<td class="text-align-center textOver">${(adcSummary.ip)!""}</td>
             		<td class="text-align-center"><img src="${imageFileName!""}" alt="${statusText}"/></td>
              		<td class="text-align-center textOver">	              		
              			<#if (adcSummary.updateTime)??>
              				${adcSummary.updateTime!""}
              			<#else>
              				-
              			</#if>
              		</td>
              		<td class="text-align-center">${adcSummary.vsTotalCount!0}</td>
	              	<td class="text-align-center">${adcSummary.vsAvailableCount!0}</td>
	              	<td class="text-align-center">${adcSummary.vsUnavailableCount!0}</td>
	              	<td class="text-align-center">${adcSummary.vsDisableCount!0}</td>
	              	<td class="text-align-left10 textOver" title="${adcSummary.model!}">
	              		<#if (adcSummary.model)??>
							<#if (adcSummary.type)??>
								<#if 1 == adcSummary.type>
									<img src="/imgs/popup/icon_f5_s.png" alt="f5" class="adc_name" />
								</#if>
								<#if 2 == adcSummary.type>
									<img src="/imgs/popup/icon_alteon_s.png" alt="alteon" class="adc_name" />
								</#if>
								<#if 3 == adcSummary.type>
									<img src="/imgs/popup/icon_pas_s.png" alt="pas" class="adc_name" />
								</#if>
								<#if 4 == adcSummary.type>
									<img src="/imgs/popup/icon_pas_s.png" alt="pask" class="adc_name" />
								</#if>
							</#if>
							${(adcSummary.model)!""}
						<#else>
							-
						</#if>
					</td>
	              	<td class="text-align-center">
	              		<#if (adcSummary.os)??>
	              			${(adcSummary.os)!""}
	              		<#else>
	              			-
	              		</#if>
	              	</td>
              		<td class="text-align-center">${adcSummary.connection!0}</td>
              		<td class="text-align-center">
	              		<#if adcSummary.throughput == "-1">
	              			0
	              		<#else>              		
	              			${adcSummary.throughput!0}
	              		</#if>
              		</td>
              		<td>
              			<#if ((adcSummary.cpuUsage) > 0)>
              				${adcSummary.cpuUsage!0}%
              			<#else>
              				-
              			</#if>
              		</td>
					<td>
						<#if ((adcSummary.memoryUsage) > 0) >
              				${adcSummary.memoryUsage!0}%
              			<#else>
              				-
              			</#if>							
					</td>
            	</tr>		           
          	</#list>
			</#if>	
</tbody>	 				
	    </table>
		<div style="border: 1px solid #ccc"></div>		    		
	</div>
</div>