<#setting number_format="0.####">
<div class="snb">
	<p class="Slider_close"><a href="javascript:;" class="snb_close"><img src="imgs/meun/btn_mov_lft2.png" title="${LANGCODEMAP["MSG_LAYOUT_LISTCLOSE"]!}"></a></p>
	<ul class="snb_tree">
		<li>
  		<ul class="depth0">	
  		<li>	
			<p class="allBlock adcListContentLnk css_textCursor">
				<span></a>${LANGCODEMAP["MSG_LAYOUT_LISTCONTAINER_TOTAL_ADC"]!}&nbsp;(${adcCount})</span>
			</p>
			</li>
			</ul>
			<ul class="depth1 cssAdcListContent">
				<#list adcGroupInfoMap?keys as key>
				<#assign theAdcGroup = adcGroupInfoMap[key]>
					<li>
						<p class="groupBlock">
							<a class="GroupButton f_left"><img src="imgs/layout/ico_plus.png" alt="${LANGCODEMAP["MSG_LAYOUT_SECONDEMENUVIEW"]!}"/></a>
							<span class="group_click_area f_left" style="width: 180px;" >
								<span class="groupNameArea">${theAdcGroup.name}&nbsp;(${theAdcGroup.adcsize})</span>
								<span class="GroupIndex" style="display: none;">${theAdcGroup.index}</span>
								<span class="GroupName" style="display: none;">${theAdcGroup.name}</span>
							</span>
						</p>
						<ul class="depth2">
							<#list theAdcGroup.adcs![] as theAdc>
								<li>
								<#if theAdc.status == 'available'>
									<p class="adcBlock" title="${theAdc.name!theAdc.ip}(${theAdc.type})">
								<#else>
									<p class="adcBlock" title="${theAdc.name!theAdc.ip}(${theAdc.type})&#13 ${LANGCODEMAP["MSG_LAYOUT_DISCONECT_TIME"]!} : ${theAdc.lastDisconnTime?string("yyyy-MM-dd HH:mm:ss")}">
								</#if>
										<span class="none adcIndex">${theAdc.index}</span>
										<span class="none isFlb">${theAdc.isFlb}</span>
										<span class="none opMode">${theAdc.opMode}</span>
										<#if theAdc.type == 'F5'>
											<span class="f_left textOver">
												<#if theAdc.status == 'available'>
													<img src="imgs/icon/icon_1d_conn.png" alt="available" />
												<#else>
													<img src="imgs/icon/icon_1d_disconn.png" alt="unavailable" />
												</#if>
												<span class="adcName">${theAdc.name!theAdc.ip}</span>
												<span class="none adcType">F5</span>
												<span class="none adcStatus">${theAdc.status}</span>
												<span class="none adcVersion">${theAdc.version!}</span>
											</span>
											<img class="conn" src="imgs/icon/adc/icon_f5_s.png" alt="f5" />
                                          	<span>	    
                                          		<#if theAdc.activeStandbyState == 0>
                                          			<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_alone_2.png" alt="alone" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_alone_1.png" alt="alone" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           		</#if>
	                                          	<#elseif theAdc.activeStandbyState == 1>
	                                          		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_active_2.png" alt="active" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_active_1.png" alt="active" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           		</#if>
	                                           	<#elseif theAdc.activeStandbyState == 2>
	                                           		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                           			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_standby_2.png" alt="standby" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_1.png" alt="standby" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           		</#if>
	                                           	<#else>	
	                                           		<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           	</#if>	                                       		
                                           	</span>	
										<#elseif theAdc.type == 'Alteon'>
											<span class="f_left textOver">
												<#if theAdc.status == 'available'>
													<img src="imgs/icon/icon_1d_conn.png" alt="available" />
												<#else>
													<img src="imgs/icon/icon_1d_disconn.png" alt="unavailable" />
												</#if>
												<span class="adcName">${theAdc.name!theAdc.ip}</span>
												<span class="none adcType">Alteon</span>
												<span class="none adcModel">${theAdc.model!''}</span>
												<span class="none adcStatus">${theAdc.status}</span>
												<span class="none adcVersion">${theAdc.version!}</span>
											</span>											
											<img class="conn" src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />	
											<span>	    
                                          		<#if theAdc.activeStandbyState == 0>
                                          			<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_alone_2.png" alt="alone" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_alone_1.png" alt="alone" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           		</#if>
	                                          	<#elseif theAdc.activeStandbyState == 1>
	                                          		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_active_2.png" alt="active" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_active_1.png" alt="active" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           		</#if>
	                                           	<#elseif theAdc.activeStandbyState == 2>
	                                           		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                           			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_standby_2.png" alt="standby" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_1.png" alt="standby" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           		</#if>
	                                           	<#else>	
	                                           		<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           	</#if>	                                       		
                                           	</span>	                                          										
										<#elseif theAdc.type == 'PAS'>
											<span class="f_left textOver">
												<#if theAdc.status == 'available'>
													<img src="imgs/icon/icon_1d_conn.png" alt="available" />
												<#else>
													<img src="imgs/icon/icon_1d_disconn.png" alt="unavailable" />
												</#if>
												<span class="adcName">${theAdc.name!theAdc.ip}</span>
												<span class="none adcType">PAS</span>
												<span class="none adcStatus">${theAdc.status}</span>
												<span class="none adcVersion">${theAdc.version!}</span>
											</span>
											<img class="conn" src="imgs/icon/adc/icon_piolink_s.png" alt="PAS" />
											<span>	    
                                          		<#if theAdc.activeStandbyState == 0>
                                          			<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_alone_2.png" alt="alone" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_alone_1.png" alt="alone" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           		</#if>
	                                          	<#elseif theAdc.activeStandbyState == 1>
	                                          		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_active_2.png" alt="active" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_active_1.png" alt="active" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           		</#if>
	                                           	<#elseif theAdc.activeStandbyState == 2>
	                                           		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                           			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_2.png" alt="standby" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_1.png" alt="standby" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           		</#if>
	                                           	<#else>	
	                                           		<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           	</#if>	                                       		
                                           	</span>	
										<#elseif theAdc.type == 'PASK'>
											<span class="f_left textOver">
												<#if theAdc.status == 'available'>
													<img src="imgs/icon/icon_1d_conn.png" alt="available" />
												<#else>
													<img src="imgs/icon/icon_1d_disconn.png" alt="unavailable" />
												</#if>
												<span class="adcName">${theAdc.name!theAdc.ip}</span>
												<span class="none adcType">PASK</span>
												<span class="none adcStatus">${theAdc.status}</span>
												<span class="none adcVersion">${theAdc.version!}</span>
											</span>
											<img class="conn" src="imgs/icon/adc/icon_piolink_s.png" alt="PASK" />
											<span>	    
                                          		<#if theAdc.activeStandbyState == 0>
                                          			<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_alone_2.png" alt="alone" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_alone_1.png" alt="alone" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           		</#if>
	                                          	<#elseif theAdc.activeStandbyState == 1>
	                                          		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_active_2.png" alt="active" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_active_1.png" alt="active" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           		</#if>
	                                           	<#elseif theAdc.activeStandbyState == 2>
	                                           		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                           			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_2.png" alt="standby" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_1.png" alt="standby" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           		</#if>
	                                           	<#else>	
	                                           		<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           	</#if>	                                       		
                                           	</span>		
										<#elseif theAdc.type == 'PiolinkUnknown'>
											<span class="f_left">
												<#if theAdc.status == 'available'>
													<img src="imgs/icon/icon_1d_conn.png" alt="available" />
												<#else>
													<img src="imgs/icon/icon_1d_disconn.png" alt="unavailable" />
												</#if>
												<span class="adcName">${theAdc.name!theAdc.ip}</span>
												<span class="none adcType">PAS</span>
												<span class="none adcStatus">${theAdc.status}</span>
												<span class="none adcVersion">${theAdc.version!}</span>
											</span>
											<img class="conn" src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
											<span>	    
                                          		<#if theAdc.activeStandbyState == 0>
                                          			<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_alone_2.png" alt="alone" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_alone_1.png" alt="alone" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           		</#if>
	                                          	<#elseif theAdc.activeStandbyState == 1>
	                                          		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                          			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn-1" src="imgs/icon/icon_active_2.png" alt="active" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_active_1.png" alt="active" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_active_3.png" alt="active" />
	                                           		</#if>
	                                           	<#elseif theAdc.activeStandbyState == 2>
	                                           		<#if (theAdc.peerip)?? && (theAdc.peerip) != "">	
	                                           			<#if (theAdc.activeStandbyDir == 1) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_2.png" alt="standby" />
	                                           			<#elseif (theAdc.activeStandbyDir == 2) >
	                                           				<img class="conn" src="imgs/icon/icon_standby_1.png" alt="standby" />
	                                           			<#else>
	                                           				<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           			</#if>
	                                           		<#else>
	                                           			<img class="conn" src="imgs/icon/icon_standby_3.png" alt="standby" />
	                                           		</#if>
	                                           	<#else>	
	                                           		<img class="conn" src="imgs/icon/icon_alone_3.png" alt="alone" />
	                                           	</#if>	                                       		
                                           	</span>																		
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

<div class="adc_search">
	<span class="inputTextposition" >
    	<input id="adcSearchTxt" name="searchTxt" type="text" class="inputText_listcontaier" value="${searchKey!}"/>
    </span>
    <span class="btn">
    	<a id="adcSearchBtn" href="#">  
			<img src="imgs/meun${img_lang!""}/btn_search_1.png" alt="${LANGCODEMAP["MSG_LAYOUT_SEARCH"]!}"/>			
    	</a>
    </span>
</div>

<div class="adc_search_1 none">
	<span class="inputTextposition">    	
    </span>
    <span class="btn"></span>
</div>	

<div class="snb_system none">
	<ul class="snb_tree">
		<li>
			<ul class="depth1">
				<li>
					<p class="userMgmtPgh">${LANGCODEMAP["MSG_LAYOUT_USER_MAN"]!}</p>
				</li>
				<#if accountRole == 'system'>
					<li>
						<p class="sysBackupPgh">${LANGCODEMAP["MSG_LAYOUT_SYS_BAKUP"]!}</p>
					</li>
					<li>
						<p class="systemInfoPgh">${LANGCODEMAP["MSG_LAYOUT_SYS_INFO"]!}</p>
					</li>
					<li>
						<p class="licensePgh">${LANGCODEMAP["MSG_LAYOUT_LICENCE"]!}</p>
					</li>
					<li>
						<p class="auditLogPgh">${LANGCODEMAP["MSG_LAYOUT_AUDIT_LOG"]!}</p>
					</li>
					<li>
						<p class="logContentPgh">${LANGCODEMAP["MSG_LAYOUT_ADMIN_LOG"]!}</p>
					</li>
					<li>
						<p class="configPgh">${LANGCODEMAP["MSG_LAYOUT_SYS_SETTING"]!}</p>
					</li>					
				</#if>
			</ul>
		</li>
	</ul>
</div>

<div class="snb_systools none">
	<ul class="snb_tree">
		<li>
			<ul class="depth1">
				<li>
					<p class="toolsPortInfo">${LANGCODEMAP["MSG_LAYOUT_IDLENESS_PORT"]!}</p>
				</li>
				<!--
				<#if accountRole == 'system'>
					<li>
						<p class="toolsSlbSessionInfo">SLB 세션 검색</p>
					</li>
				</#if>
				-->
			</ul>
		</li>
	</ul>
</div>

<script type="text/javascript">
	$(function(){
		// SNB 높이 설정
		//$('.snb').height($(document).height()-$('.snb').position().top);
		
		// SNB Tree 클릭 이벤트
		/*
		$('.snb_tree p').click(function(e) {
			var $selectItem = $(this);
			var $sub_menu = $selectItem.nextAll('ul');
			// tree메뉴 초기화 함수 
			initTree($selectItem);

			if($sub_menu.css('display') == 'none'){
				$selectItem.addClass('on');
				$sub_menu.slideDown(200);
			}else{
				$sub_menu.slideUp(200);
			}
		});
		*/
		// SNB 메뉴 클릭시 컨텐츠 변경
/*
		$('.depth1 > li > p').click(function(e) {
			var txt = $(this).text().split('(')[0];
			$('.accordion_type1 > li').each(function(index, element) {
				if($(this).children('h2').text() == txt){
					$(this).children('h2').trigger('click');
				}
			});
		});
		
		// depth3 스타일 적용
		$('.depth3 > li a').click(function(e) {
			// tree메뉴 초기화 함수 
			$(this).parents('.depth3').find('li').each(function(index, element) {
				$(this).children('a').removeClass('on');
			});;
			$(this).addClass('on');
		});
		
		// SNB 메뉴 숨기기/보이기
		$('.Slider_close').toggle(function(e) {
			$('.snb').animate({'left':'-205px'},100,function(){
				$('.snb_close').attr('class','snb_open').children('img').attr({'src':'imgs/layout/btn_open.png','alt':'메뉴 열기'});
				$('.content_wrap').css('margin-left','10px');
			});	
		},function(){
			$('.snb').animate({'left':'0px'},100,function(){
				$('.snb_open').attr('class','snb_close').children('img').attr({'src':'imgs/layout/btn_close.png','alt':'메뉴 닫기'});
				$('.content_wrap').css('margin-left','215px');
			});
		});
*/		
	});
</script>