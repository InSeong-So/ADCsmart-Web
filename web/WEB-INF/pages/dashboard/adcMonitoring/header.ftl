<div class="listcontainer_monitor">
<li class="title_h5-3">ADC 목록</li>	
	<table class="table_monitor" cellpadding="0" cellspacing="0" >
		<colgroup>
				<col width="54%"/>
				<col width="20%"/>
				<col width="26%"/>
	  	</colgroup>
	  	<tr>
	    	<td class="table_monitor_th">ADC이름</td>
	    	<td class="table_monitor_th">Connection</td>
	    	<td class="table_monitor_th">Throughput</td>
	  	</tr>
	</table>			

			
	<div class="table_scr listcontainer_monitor_div">
		<table class="table_monitor" summary="" style="TABLE-layout:fixed">
			<caption></caption>
			<colgroup>
				<col width="54%"/>
				<col width="20%"/>
				<col width="26%"/>
			</colgroup>
			<tbody>
				<tr id="firstGroup" class="top"> <!--전체-->
					<td class="table_monitor_td1">
						<span class="f_left">
							<!--
							<a val="minus" group="all" class="btnPlusMinus css_textCursor">
								 <img class="plusminus " src="/imgs/layout/icon_folder_off.gif" alt=""/>
							</a>
							-->
							&nbsp;&nbsp;<a class="css_textCursor" id="allGroup">전체</a>
						</span>
						<span class="none objIndex">${adcSummarysConversion.objectInfo.index!}</span>
						<span class="none objName">${adcSummarysConversion.objectInfo.name!}</span>
						<span class="none objVendor">${adcSummarysConversion.objectInfo.vendor!}</span>
						<span class="none objCategory">${adcSummarysConversion.objectInfo.category!}</span>
					</td>
					<td class="text-align-left10 table_monitor_td1">
					<div style="width:99%">
                        <div style="width:40%;float:left;text-align:right ">
							<a class="connectionGraphLnk css_textCursor">
								<img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" />
							</a>
						</div>
						<div style="width:60%;float:left;text-align:left">
							${adcSummarysConversion.connectionInfo.value}
						</div>
				    </div>
					</td>
					<td class="text-align-left10 table_monitor_td1">
						<div style="width:99%">
	                        <div style="width:40%;float:left;text-align:right">
								<a class="throughputGraphLnk css_textCursor">
									<img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" />
								</a>
							</div>
							<div style="width:60%;float:left;text-align:left">
								${adcSummarysConversion.throughputInfo.value}
							</div>
					    </div>
					</td>
				</tr>
				<#list adcSummarysConversion.adcList![] as adcGroup>
<!--1depth-->				
				<tr id="_${adcGroup.objectInfo.index!}" val="plus" group="_${adcGroup.objectInfo.index!}" class="sub sub1 unselected table_monitor_td btnPlusMinus css_textCursor" style="display:none;" >
					<td id="secondGroup2 table_monitor_td" class="secondGroup2" title="${adcGroup.objectInfo.name!""}"> 	
						<span class="f_left table_monitor_td">								
						<!--
							<a val="plus" group="_${adcGroup.objectInfo.index!}" class="btnPlusMinus css_textCursor">
								<img class="plusminus" src="/imgs/layout/icon_folder_off.gif"" alt=""/>
							</a>
						-->
							<img class="plusminus" src="/imgs/layout/icon_folder_off.gif"" alt=""/>
							<a class="adcGroups css_textCursor">${adcGroup.objectInfo.name}</a>										
						</span>
						<!--
						<a class="adcInfoMgmtLnk css_textCursor"><img src="/imgs/monitoring/ico_note.png" alt="" class="f_right mar_rgt10" /></a>
						-->
						<span class="none objIndex">${adcGroup.objectInfo.index!}</span>
						<span class="none objName">${adcGroup.objectInfo.name!}</span>
						<span class="none objVendor">${adcGroup.objectInfo.vendor!}</span>
						<span class="none objCategory">${adcGroup.objectInfo.category!}</span>
					</td>
					<td class="text-align-left10 table_monitor_td">
						<div style="width:99%">
	                        <div style="width:40%;float:left;text-align:right">
								<a class="connectionGraphLnk css_textCursor">
									<img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" />
								</a>
							</div>
							<div style="width:60%;float:left;text-align:left">
								${adcGroup.connectionInfo.value}
							</div>
					    </div>
					</td>
					<td class="text-align-left10 table_monitor_td">
						<div style="width:99%">
	                        <div style="width:40%;float:left;text-align:right">
								<a class="throughputGraphLnk css_textCursor">
									<img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" />
								</a>
							</div>
							<div style="width:60%;float:left;text-align:left">
								${adcGroup.throughputInfo.value}
							</div>
				    </div>
					</td>							
				</tr>
<!--2depth-->																									
				<#list adcGroup.adcList![] as adcInfo>
				<tr class="sub sub2 _${adcGroup.objectInfo.index!} unselected  table335_td" style="display:none;">							
					<td id="thirdGroup2" class="thirdGroup2 text-align-left10 table_monitor_td2" title="ADC 이름:${adcInfo.objectInfo.name!""}&#13;ADC 장비:${adcInfo.objectInfo.vendor!""}" > 			
						<p style="text-overflow:ellipsis;overflow:hidden;white-space:nowrap;>			
							<span class="f_left">
								<a class="adcInfos css_textCursor">${adcInfo.objectInfo.name}</a>																
							</span>									
							<span class="none objIndex">${adcInfo.objectInfo.index!}</span>
							<span class="none objName">${adcInfo.objectInfo.name!}</span>
							<span class="none objVendor">${adcInfo.objectInfo.vendor!}</span>
							<span class="none objCategory">${adcInfo.objectInfo.category!}</span>						
							<!--
							<a class="adcInfoMgmtLnk css_textCursor"><img src="/imgs/monitoring/ico_note.png" alt="" class="f_right mar_rgt10" /></a>
							-->
						</p>
					</td>
					<td class="text-align-left10 table_monitor_td2">
						<div style="width:99%">
	                        <div style="width:40%;float:left;text-align:right">
								<a class="connectionGraphLnk css_textCursor">
									<img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" />
								</a>
							</div>
							<div style="width:60%;float:left;text-align:left">
								${adcInfo.connectionInfo.value}
							</div>
					    </div>
					</td>
					<td class="text-align-left10 table_monitor_td2">
						<div style="width:99%">
	                        <div style="width:40%;float:left;text-align:right">
								<a class="throughputGraphLnk css_textCursor">
									<img src="/imgs/monitoring/ico_graph.png" alt="" class="mar_lft5" />
								</a>
							</div>
							<div style="width:60%;float:left;text-align:left">
								${adcInfo.throughputInfo.value}
							</div>
					    </div>
					</td>	
				</tr>
				</#list>
				</#list>
			</tbody>
		</table>
	</div>
</div>
