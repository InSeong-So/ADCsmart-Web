<#setting number_format="0.####">
<div class="portlet-header ui-widget-header ui-corner-all">${(widgetInfo.name)!''} -
	<span class="sub_title"> ${(widgetTarget.name)!''}</span>
	<#if (widgetInfo.moreInfoIndex) == 10>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_SUMMARYALL"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 11>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_SUMMARY_ENABLE"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 12>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_DISABLE"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 13>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_DISABLE_SEVEN_DAY"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 14>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_SUMMARYOFF"]!}]</span>	
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
	<table class="widget_table" style="table-layout: fixed" >
	  	<colgroup>
			<col width="14%"/>
			<col width="5%"/>
			<col width="4%"/>
			<col width="13%"/>
			<col width="14%"/>				
			<col width="21%"/>				
			<col width="11%"/>
			<col width="9%"/>
			<col width="9%"/>      
		</colgroup>
		<thead>			
		    <tr>
	        	<th>VS IP</th>
	           	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYPORT"]!}</th>
	           	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYSTATUS"]!}</th>
	           	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYUPDATE_TIME"]!}</th>
	           	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYADC_NAME"]!}</th>
	           	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYPRODUCT_NAME"]!}</th>
	           	<th>${LANGCODEMAP["MSG_WIDGET_SUMMARYPRODUCT_VERSION"]!}</th>
	           	<th>Sessions</th>
	           	<th>Throughput</th>				
          	</tr>
		</thead>
		<tbody>	          	
	       	<#if vsSummaryList??>
			<#list vsSummaryList as vsSummary>
				<#if 0 == vsSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_vs_disabled.png">
					<#assign statusText = "Disabled">
				</#if>
				<#if 1 == vsSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_vs_conn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_WIDGET_ENABLE']!}">   
				</#if>
				<#if 2 == vsSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_vs_disconn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_WIDGET_DISABLE']!}">
				</#if>
				<#if 3 == vsSummary.status>
					<#assign imageFileName = "/imgs/icon/icon_yellowDot.png">
					<#assign statusText = "${LANGCODEMAP['MSG_WIDGET_ABNORMAL']!}">
				</#if>
				
        		<tr class="vsSummaryListRow" >              	
	              	<td class="text-align-left10 textOver">
						<span class="none vsType">${vsSummary.vsType}</span>
						<span class="none vsIndex">${vsSummary.index}</span>
						
						<#if 1 == vsSummary.vsType>-</#if>
						${vsSummary.ip!""}
					</td>
					<td class="text-align-center"><span class="none vsPort">${vsSummary.port}</span>
							<#if (vsSummary.port!) == 0>
								-
							<#else>
								${vsSummary.port!}
							</#if>
						</td>
					<td class="textOver text-align-center"><img src="${imageFileName}" alt="${statusText}"/></td>
					<td class="text-align-center textOver">
						<#if (vsSummary.updateTime)??>
							${vsSummary.updateTime!""}
						<#else>
							-
						</#if>
					</td>
					<td class="text-align-left10 textOver" title="${vsSummary.adcName!""}">
						<span class="none adcIndex">${vsSummary.adcIndex}</span>
						${vsSummary.adcName!""}
					</td>
					<td class="text-align-left10 textOver" title="${vsSummary.model!""}">
						<#if (vsSummary.model)??>							 
							<#if (vsSummary.adcType)??>
								<#if 1 == vsSummary.adcType>
									<span class="none adcType">F5</span>
									<img src="/imgs/popup/icon_f5_s.png" alt="f5" class="adc_name" />
								</#if>
								<#if 2 == vsSummary.adcType>
									<span class="none adcType">Alteon</span>
									<img src="/imgs/popup/icon_alteon_s.png" alt="alteon" class="adc_name" />
								</#if>
								<#if 3 == vsSummary.adcType>
									<span class="none adcType">PAS</span>
									<img src="/imgs/popup/icon_pas_s.png" alt="pas" class="adc_name" />
								</#if>
								<#if 4 == vsSummary.adcType>
									<span class="none adcType">PASK</span>
									<img src="/imgs/popup/icon_pas_s.png" alt="pask" class="adc_name" />
								</#if>
							</#if>
							${vsSummary.model!""}
						<#else>
							<#if 1 == vsSummary.adcType>
								<span class="none adcType">F5</span>									
							</#if>
							<#if 2 == vsSummary.adcType>
								<span class="none adcType">Alteon</span>
							</#if>
							<#if 3 == vsSummary.adcType>
								<span class="none adcType">PAS</span>
							</#if>
							<#if 4 == vsSummary.adcType>
								<span class="none adcType">PASK</span>
							</#if>
							- 
						</#if>
					</td>
					<td class="text-align-center">
						<#if (vsSummary.os)??>
							${vsSummary.os!""}
						<#else>
							-
						</#if>
					</td>
					<td class="text-align-center">${vsSummary.connection!""}</td>
					<td class="text-align-center">${vsSummary.throughput!""}</td>	
	            </tr>
			</#list>
			</#if>
		</tbody>					
	</table>	
	<div style="border: 1px solid #ccc"></div>		    	
	</div>