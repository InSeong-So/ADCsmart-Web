<div id="columns">			
			<!----- 1 ----->
				<div class="pin">
					<#if (networkMap.networkMapVsList)??>
						<#list networkMap.networkMapVsList as networkMapVs>
							<#if networkMapVs_index % 3 == 0>
								<#if 1 == networkMapVs.status>
									<#if networkMapVs.lbClass == 2 >
										<#assign imageFileName = "icon_flb_conn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									<#else>
										<#assign imageFileName = "icon_vs_conn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									</#if>
								<#elseif 2 == networkMapVs.status>
									<#if networkMapVs.lbClass == 2 >
										<#assign imageFileName = "icon_flb_disconn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									<#else>
										<#assign imageFileName = "icon_vs_disconn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									</#if>
								<#elseif networkMapVs.lbClass == 2>
									<#assign imageFileName = "icon_flb_disabled.png">
									<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_OFF']!}">
								<#else>
									<#assign imageFileName = "icon_vs_disabled.png">
									<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_OFF']!}">
								</#if>
							
								<table class="Board_networkmap networkMapVsList" id="table${networkMapVs_index}" cellpadding="0" cellspacing="0" summary="${networkMapVs.name} ${LANGCODEMAP["MSG_NETWORK_VS_STATUS"]!}" style="table-layout: fixed;">
									<colgroup>
										<col width="30px"/>
										<col width="auto" />	
										<col width="60px" />						
									</colgroup>
									<thead>
										<#assign title = networkMapVs.ipAddress!"">
										<#if networkMapVs.name?? && "" != networkMapVs.name>
											<#assign title = networkMapVs.name>
										</#if>
										<tr class="ContentsHeadLine ">
											<th class="align_center">
												<img src="imgs/icon/${imageFileName}" alt="${statusText}"/>
											</th>
											<th class="align_left_P5 textOver">
												<a class="css_textCursor">
													<#if networkMapVs.lbClass == 2 >
														<span class="maxVsNameNetworkMap" title="${title}&#13;Metric:${networkMapVs.loadBalancingType!""}&#13;HealthCheck:${networkMapVs.healthCheckType!""}">
															<span class="vsTitile"><#setting number_format="0.####">Group ${title}&nbsp;&nbsp;</span>
															<span class="vsMemberCount">(${networkMapVs.networkMapVsMemberList?size})</span>
														</span>
														<#else>
														<span class="maxVsNameNetworkMap" title="${title}&#13;Metric:${networkMapVs.loadBalancingType!""}&#13;HealthCheck:${networkMapVs.healthCheckType!""}">
															<span class="vsTitile"><#setting number_format="0.####">${title}&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} ${networkMapVs.port?replace("999", ", ")}</span>
															<span class="vsMemberCount">(${networkMapVs.networkMapVsMemberList?size})</span>
														</span>
													</#if>
												</a>
                                                <span class="none vsPort">${title}</span>
												<span class="none vsIndex">${networkMapVs.index}</span>																	
                                                <span class="none vsvcIndex">${networkMapVs.vsvcIndex!}</span>                                                                 
												<span class="none port">${networkMapVs.port}</span>
												<span class="none lbClass">${networkMapVs.lbClass}</span>
												<span><input type="text" id="vsDescription" style="background-color:transparent;border:0 solid black;text-align:left; size="35" readonly value="${networkMapVs.description!}"></span>
											</th>
											<#if networkMapVs.groupBakupType??>
												<#if 0 != networkMapVs.groupBakupType && ((adc.type) == "Alteon")>
													<td class="align_right paddingR10">
                                                        <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
														<a class="css_textCursor backupgroup">
															<span class=""><img src="imgs/icon/icon_backup_on.gif" alt=""/></span>
															<span class="none vsname">${networkMapVs.name}</span>
															<span class="none port">${networkMapVs.port}</span>
															<span class="none groupBakupType">${networkMapVs.groupBakupType}</span>
															<span class="none groupBakupId">${networkMapVs.groupBakupId}</span>
															<#if networkMapVs.networkMapVsBackupList?? && ((networkMapVs.networkMapVsBackupList?size) > 0)>
																<#list networkMapVs.networkMapVsBackupList as networkMapBackup>
																	<#if networkMapVs.groupBakupType == 2>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																	<#elseif networkMapBackup.nodeIndex == networkMapVs.groupBakupId>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																		<#break>
																	</#if>
																</#list>
															</#if>
														</a>
													</td>
												<#else>
													<td class="align_right paddingR10">
    													<span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
													</td>
												</#if>
											<#else>
												<td class="align_right paddingR10">
												    <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
												</td>
											</#if>
								 		</tr>								
									</thead>
									<tbody>
										<#if networkMapVs.networkMapVsMemberList?? && ((networkMapVs.networkMapVsMemberList?size) > 0)>
										<#list networkMapVs.networkMapVsMemberList as networkMapVsMember>
				  							<#if 0 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_0.png">
												<#assign statusText = "Disabled">
							 				<#elseif 1 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_1.png">
												<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
											<#elseif 2 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_2.png">
												<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_ISOLATION']!}">
											</#if>
										<tr class="ContentsLineA">
											<td class="align_right">
												<img src="imgs/icon/${imageFileName}"  />
											</td>
											<td class="align_left_P10">
												${networkMapVsMember.ipAddress}
												<#if (lbType != 2)>												
													<#if (networkMapVsMember.addPort)?? && (networkMapVsMember.port == 0)>
													&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} 
														${networkMapVsMember.addPort}
													<#elseif (networkMapVsMember.port == -1)>
														
													<#else>
													&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} 
														${networkMapVsMember.port}
													</#if>
												</#if>
											</td>
											<#if networkMapVsMember.bakupType??>
												<#if 1 == networkMapVsMember.bakupType && ((adc.type) == "Alteon")>
													<td>
														<a class="css_textCursor backupreal">
															<span class="none vsname">${networkMapVs.name}</span>
															<span class="none port">${networkMapVs.port}</span>
															<span class="none nodebackup">${networkMapVsMember.bakupIndex}</span>
															<#if networkMapVs.networkMapVsBackupList?? && ((networkMapVs.networkMapVsBackupList?size) > 0)>
																<#list networkMapVs.networkMapVsBackupList as networkMapBackup>
				  													<#if (networkMapBackup.nodeIndex == networkMapVsMember.bakupIndex &&
				  														networkMapBackup.bakType == 1)>
																		<span class=""><img src="imgs/icon/icon_backup_on.gif" alt=""/></span>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																	</#if>
																</#list>
															</#if>
														</a>
													</td>
												<#else>
													<td></td>
												</#if>
											<#else>
												<td></td>
											</#if>
										</tr>							
										<#if !networkMapVsMember_has_next>
											<#if 0 == networkMapVsMember_index>
												<!--<tr><td></td></tr>-->
												<!--<tr><td></td></tr>-->
											<#elseif 1 == networkMapVsMember_index>
												<!--<tr><td></td></tr>-->
											</#if>
										</#if>								
										</#list>
										<#else>
										<!--<tr><td></td></tr>-->
										<!--<tr><td></td></tr>-->
										<!--<tr><td></td></tr>-->	
										</#if>
									</tbody>					
								</table>					
							</#if>
						</#list>
					</#if>	
				</div>	
				<!----- Contents List End ----->
				<!----- 2 ----->					
				<div class="pin">
					<#if (networkMap.networkMapVsList)??>
						<#list networkMap.networkMapVsList as networkMapVs>
							<#if networkMapVs_index % 3 == 1>
								<#if 1 == networkMapVs.status>
									<#if networkMapVs.lbClass == 2 >
										<#assign imageFileName = "icon_flb_conn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									<#else>
										<#assign imageFileName = "icon_vs_conn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									</#if>
								<#elseif 2 == networkMapVs.status>
									<#if networkMapVs.lbClass == 2 >
										<#assign imageFileName = "icon_flb_disconn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									<#else>
										<#assign imageFileName = "icon_vs_disconn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									</#if>
								<#elseif networkMapVs.lbClass == 2>
									<#assign imageFileName = "icon_flb_disabled.png">
									<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_OFF']!}">
								<#else>
									<#assign imageFileName = "icon_vs_disabled.png">
									<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_OFF']!}">
								</#if>
								<table class="Board_networkmap networkMapVsList" id="table${networkMapVs_index}" cellpadding="0" cellspacing="0" summary="${networkMapVs.name} ${LANGCODEMAP["MSG_NETWORK_VS_STATUS"]!}" style="table-layout: fixed;">
									<colgroup>
										<col width="30px"/>
										<col width="auto" />	
										<col width="60px" />					
									</colgroup>
									<thead>
										<#assign title = networkMapVs.ipAddress!"">
										<#if networkMapVs.name?? && "" != networkMapVs.name>
											<#assign title = networkMapVs.name>
										</#if>
										<tr class="ContentsHeadLine ">
											<th class="align_center">
												<img src="imgs/icon/${imageFileName}" alt="${statusText}"/>
											</th>
											<th class="align_left_P5 textOver">
												<a class="css_textCursor">
													<#if networkMapVs.lbClass == 2 >
														<span class="maxVsNameNetworkMap" title="${title}&#13;Metric:${networkMapVs.loadBalancingType!""}&#13;HealthCheck:${networkMapVs.healthCheckType!""}">
															<span class="vsTitile"><#setting number_format="0.####">Group ${title}&nbsp;&nbsp;</span>
															<span class="vsMemberCount">(${networkMapVs.networkMapVsMemberList?size})</span>
														</span>
														<#else>
														<span class="maxVsNameNetworkMap" title="${title}&#13;Metric:${networkMapVs.loadBalancingType!""}&#13;HealthCheck:${networkMapVs.healthCheckType!""}">
															<span class="vsTitile"><#setting number_format="0.####">${title}&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} ${networkMapVs.port?replace("999", ", ")}</span>
															<span class="vsMemberCount">(${networkMapVs.networkMapVsMemberList?size})</span>
														</span>
													</#if>
												</a>
												<span class="none vsIndex">${networkMapVs.index}</span>																	
                                                <span class="none vsvcIndex">${networkMapVs.vsvcIndex!}</span>                                                                 
												<span class="none port">${networkMapVs.port}</span>
												<span class="none lbClass">${networkMapVs.lbClass}</span>
                                                <span><input type="text" id="vsDescription" style="background-color:transparent;border:0 solid black;text-align:left; size="35" readonly value="${networkMapVs.description!}"></span>
											</th>
											<#if networkMapVs.groupBakupType??>
												<#if 0 != networkMapVs.groupBakupType && ((adc.type) == "Alteon")>
													<td class="align_right paddingR10">
                                                        <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
														<a class="css_textCursor backupgroup">
															<span class=""><img src="imgs/icon/icon_backup_on.gif" alt=""/></span>
															<span class="none vsname">${networkMapVs.name}</span>
															<span class="none port">${networkMapVs.port}</span>
															<span class="none groupBakupType">${networkMapVs.groupBakupType}</span>
															<span class="none groupBakupId">${networkMapVs.groupBakupId}</span>
															<#if networkMapVs.networkMapVsBackupList?? && ((networkMapVs.networkMapVsBackupList?size) > 0)>
																<#list networkMapVs.networkMapVsBackupList as networkMapBackup>
																	<#if networkMapVs.groupBakupType == 2>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																	<#elseif networkMapBackup.nodeIndex == networkMapVs.groupBakupId>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																		<#break>
																	</#if>
																</#list>
															</#if>
														</a>
													</td>
												<#else>
													<td class="align_right paddingR10">
                                                        <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
													</td>
												</#if>
											<#else>
												<td class="align_right paddingR10">
                                                    <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
												</td>
											</#if>
								 		</tr>								
									</thead>
									<tbody>
										<#if networkMapVs.networkMapVsMemberList?? && ((networkMapVs.networkMapVsMemberList?size) > 0)>
										<#list networkMapVs.networkMapVsMemberList as networkMapVsMember>
				  							<#if 0 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_0.png">
												<#assign statusText = "Disabled">
							 				<#elseif 1 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_1.png">
												<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
											<#elseif 2 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_2.png">
												<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_ISOLATION']!}">
											</#if>
										<tr class="ContentsLineA">
											<td class="align_right ">
												<img src="imgs/icon/${imageFileName}"  />
											</td>
											<td class="align_left_P10">
												${networkMapVsMember.ipAddress}
												<#if (lbType != 2)>												
													<#if (networkMapVsMember.addPort)?? && (networkMapVsMember.port == 0)>
													&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} 
														${networkMapVsMember.addPort}
													<#elseif (networkMapVsMember.port == -1)>
														
													<#else>
													&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} 
														${networkMapVsMember.port}
													</#if>
												</#if>
											</td>
											<#if networkMapVsMember.bakupType??>
												<#if 1 == networkMapVsMember.bakupType && ((adc.type) == "Alteon")>
													<td>
														<a class="css_textCursor backupreal">
															<span class="none vsname">${networkMapVs.name}</span>
															<span class="none port">${networkMapVs.port}</span>
															<span class="none nodebackup">${networkMapVsMember.bakupIndex}</span>
															<#if networkMapVs.networkMapVsBackupList?? && ((networkMapVs.networkMapVsBackupList?size) > 0)>
																<#list networkMapVs.networkMapVsBackupList as networkMapBackup>
				  													<#if (networkMapBackup.nodeIndex == networkMapVsMember.bakupIndex &&
				  														networkMapBackup.bakType == 1)>
																		<span class=""><img src="imgs/icon/icon_backup_on.gif" alt=""/></span>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																	</#if>
																</#list>
															</#if>
														</a>
													</td>
												<#else>
													<td></td>
												</#if>
											<#else>
												<td></td>
											</#if>									
										</tr>								
										<#if !networkMapVsMember_has_next>
											<#if 0 == networkMapVsMember_index>
												<!--<tr><td></td></tr>-->
												<!--<tr><td></td></tr>-->
											<#elseif 1 == networkMapVsMember_index>
												<!--<tr><td></td></tr>-->
											</#if>
										</#if>								
										</#list>
										<#else>
										<!--<tr><td></td></tr>-->
										<!--<tr><td></td></tr>-->
										<!--<tr><td></td></tr>-->	
										</#if>
									</tbody>						
								</table>					
							</#if>
						</#list>
					</#if>	
				</div>	
				<!----- Contents List End ----->
				<!----- 3 ----->			
				<div class="pin">
					<#if (networkMap.networkMapVsList)??>
						<#list networkMap.networkMapVsList as networkMapVs>
							<#if networkMapVs_index % 3 == 2>
								<#if 1 == networkMapVs.status>
									<#if networkMapVs.lbClass == 2 >
										<#assign imageFileName = "icon_flb_conn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									<#else>
										<#assign imageFileName = "icon_vs_conn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									</#if>
								<#elseif 2 == networkMapVs.status>
									<#if networkMapVs.lbClass == 2 >
										<#assign imageFileName = "icon_flb_disconn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									<#else>
										<#assign imageFileName = "icon_vs_disconn.png">
										<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
									</#if>
								<#elseif networkMapVs.lbClass == 2>
									<#assign imageFileName = "icon_flb_disabled.png">
									<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_OFF']!}">
								<#else>
									<#assign imageFileName = "icon_vs_disabled.png">
									<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_OFF']!}">
								</#if>
								<table class="Board_networkmap networkMapVsList" id="table${networkMapVs_index}" cellpadding="0" cellspacing="0" summary="${networkMapVs.name} ${LANGCODEMAP["MSG_NETWORK_VS_STATUS"]!}" style="table-layout: fixed;">
									<colgroup>
										<col width="30px"/>
										<col width="auto" />	
										<col width="60px" />				
									</colgroup>
									<thead>
										<#assign title = networkMapVs.ipAddress!"">
										<#if networkMapVs.name?? && "" != networkMapVs.name>
											<#assign title = networkMapVs.name>
										</#if>
										<tr class="ContentsHeadLine ">
											<th class="align_center">
												<img src="imgs/icon/${imageFileName}" alt="${statusText}"/>
											</th>
											<th class="align_left_P5 textOver">
												<a class="css_textCursor">
													<#if networkMapVs.lbClass == 2 >
														<span class="maxVsNameNetworkMap" title="${title}&#13;Metric:${networkMapVs.loadBalancingType!""}&#13;HealthCheck:${networkMapVs.healthCheckType!""}">
															<span class="vsTitile"><#setting number_format="0.####">Group ${title}&nbsp;&nbsp;</span>
															<span class="vsMemberCount">(${networkMapVs.networkMapVsMemberList?size})</span>
														</span>
														<#else>
														<span class="maxVsNameNetworkMap" title="${title}&#13;Metric:${networkMapVs.loadBalancingType!""}&#13;HealthCheck:${networkMapVs.healthCheckType!""}">
															<span class="vsTitile"><#setting number_format="0.####">${title}&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} ${networkMapVs.port?replace("999", ", ")}</span>
															<span class="vsMemberCount">(${networkMapVs.networkMapVsMemberList?size})</span>
														</span>
													</#if>
												</a>
												<span class="none vsIndex">${networkMapVs.index}</span>
                                                <span class="none vsvcIndex">${networkMapVs.vsvcIndex!}</span>                                                                 
												<span class="none port">${networkMapVs.port}</span>
												<span class="none lbClass">${networkMapVs.lbClass}</span>
                                                <span><input type="text" id="vsDescription" style="background-color:transparent;border:0 solid black;text-align:left; size="35" readonly value="${networkMapVs.description!}"></span>
											</th>
											<#if networkMapVs.groupBakupType??>
												<#if 0 != networkMapVs.groupBakupType && ((adc.type) == "Alteon")>
													<td class="align_right paddingR10">
                                                        <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
														<a class="css_textCursor backupgroup">
															<span class=""><img src="imgs/icon/icon_backup_on.gif" alt=""/></span>
															<span class="none vsname">${networkMapVs.name}</span>
															<span class="none port">${networkMapVs.port}</span>
															<span class="none groupBakupType">${networkMapVs.groupBakupType}</span>
															<span class="none groupBakupId">${networkMapVs.groupBakupId}</span>
															<#if networkMapVs.networkMapVsBackupList?? && ((networkMapVs.networkMapVsBackupList?size) > 0)>
																<#list networkMapVs.networkMapVsBackupList as networkMapBackup>
																	<#if networkMapVs.groupBakupType == 2>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																	<#elseif networkMapBackup.nodeIndex == networkMapVs.groupBakupId>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																		<#break>
																	</#if>
																</#list>
															</#if>
														</a>
													</td>
												<#else>
													<td class="align_right paddingR10">
                                                        <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
													</td>
												</#if>
											<#else>
												<td class="align_right paddingR10">
                                                    <span class="paddingR4"><img class="vsDescEdit css_textCursor" src="imgs/btn/btn_config.png" alt=""/></span>
												</td>
											</#if>
								 		</tr>								
									</thead>
									<tbody>
										<#if networkMapVs.networkMapVsMemberList?? && ((networkMapVs.networkMapVsMemberList?size) > 0)>
										<#list networkMapVs.networkMapVsMemberList as networkMapVsMember>
				  							<#if 0 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_0.png">
												<#assign statusText = "Disabled">
							 				<#elseif 1 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_1.png">
												<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_NORMAL']!}">
											<#elseif 2 == networkMapVsMember.status>
												<#assign imageFileName = "icon_2d_2.png">
												<#assign statusText = "${LANGCODEMAP['MSG_NETWORK_ISOLATION']!}">
											</#if>
										<tr class="ContentsLineA">
											<td class="align_right ">
												<img src="imgs/icon/${imageFileName}"  />
											</td>
											<td class="align_left_P10">
												${networkMapVsMember.ipAddress}
												<#if (lbType != 2)>												
													<#if (networkMapVsMember.addPort)?? && (networkMapVsMember.port == 0)>
													&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} 
														${networkMapVsMember.addPort}
													<#elseif (networkMapVsMember.port == -1)>
														
													<#else>
													&nbsp;&nbsp;${LANGCODEMAP["MSG_NETWORK_PORT"]!} 
														${networkMapVsMember.port}
													</#if>
												</#if>
											</td>
											<#if networkMapVsMember.bakupType??>
												<#if 1 == networkMapVsMember.bakupType && ((adc.type) == "Alteon")>
													<td>
														<a class="css_textCursor backupreal">
															<span class="none vsname">${networkMapVs.name}</span>
															<span class="none port">${networkMapVs.port}</span>
															<span class="none nodebackup">${networkMapVsMember.bakupIndex}</span>
															<#if networkMapVs.networkMapVsBackupList?? && ((networkMapVs.networkMapVsBackupList?size) > 0)>
																<#list networkMapVs.networkMapVsBackupList as networkMapBackup>				  													
				  													<#if (networkMapBackup.nodeIndex == networkMapVsMember.bakupIndex &&
				  														networkMapBackup.bakType == 1)>
																		<span class=""><img src="imgs/icon/icon_backup_on.gif" alt=""/></span>
																		<span class="none ipaddress">${networkMapBackup.ipAddress}</span>
																		<span class="none status">${networkMapBackup.status}</span>
																	</#if>
																</#list>
															</#if>
														</a>
													</td>
												<#else>
													<td></td>
												</#if>
											<#else>
												<td></td>
											</#if>
										</tr>								
										<#if !networkMapVsMember_has_next>
											<#if 0 == networkMapVsMember_index>
												<!--<tr><td></td></tr>-->
												<!--<tr><td></td></tr>-->
											<#elseif 1 == networkMapVsMember_index>
												<!--<tr><td></td></tr>-->
											</#if>
										</#if>								
										</#list>
										<#else>
										<!--<tr><td></td></tr>-->
										<!--<tr><td></td></tr>-->
										<!--<tr><td></td></tr>-->	
										</#if>
									</tbody>
								</table>					
							</#if>
						</#list>
					</#if>	
				</div>
			</div>
		</div>
	</div> 