<div class="listcontainer">
	<div class="snb">
	<!--	
		<p class="title">ADC 목록</p>
	-->	
		<ul class="snb_tree">
			<li>
				<p class="allBlock">
					<a class="css_textCursor">전체 (${adcCounts})</a>
				</p>
				<ul class="depth1">
					<#list adcGroups![] as theAdcGroup>
					<li>
						<p class="groupBlock">
							<span>
								<!--
								<#if (theAdcGroup.unavailableAdcCount!0) == 0>
									<img src="/imgs/layout/icon_sys_conn.png" alt="available" />
								<#else>
									<img src="/imgs/layout/icon_sys_disconn.png" alt="unavailable" />
								</#if>
								-->
								<span class="none groupIndex">${theAdcGroup.index!}</span>
								<span class="groupName">${theAdcGroup.name!}</span>
								(${(theAdcGroup.adcs![])?size})
							</span>
							<!-- 
							<a href="#">
								<img src="/imgs/layout/bu_arr_off.png" alt="하위 메뉴 보기" />
							</a> 
							-->
						</p>
						<ul class="depth2">
							<#list (theAdcGroup.adcs)![] as theAdc>
							<li>
								<p class="adcBlock">
									<span class="f_left textOver">
										<#if theAdc.status == 'available'>
											<img src="/imgs/icon/icon_1d_conn.png" alt="available" />
										<#else>
											<img src="/imgs/icon/icon_1d_disconn.png" alt="unavailable" />
										</#if>
										<span class="none adcIndex">${theAdc.index!}</span>
										<span class="adcName">${theAdc.name!}</span>
										<span class="none adcType">${theAdc.type!}</span>
									</span>
									<#if theAdc.type! == 'F5'>
										<img class="conn" src="/imgs/icon/adc/icon_f5_s.png" alt="연결" />
									<#elseif theAdc.type! == 'Alteon'>
										<img class="conn" src="/imgs/icon/adc/icon_alteon_s.png" alt="연결" />
									<#elseif theAdc.type! == 'PAS'>
										<img class="conn" src="/imgs/icon/adc/icon_piolink_s.png" alt="연결" />
									<#elseif theAdc.type! == 'PASK'>
										<img class="conn" src="/imgs/icon/adc/icon_piolink_s.png" alt="연결" />									
									</#if>
								</p>
							</li>
							</#list>
						</ul>
					</li>
					</#list>
				</ul>
			</li>
		</ul>
	</div>	
</div>
<!--
<div class="adc_search">
	<span class="inputTextposition" >
   		<input name="searchTxt" type="text" class="searchTxt" value="${searchKey!}"/>
	</span>
    <span class="btn">
    	<a class="searchLnk" href="#"><img src="/imgs/meun/btn_search_1.png" alt="검색"/></a>
	</span>  
</div>
-->