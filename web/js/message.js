// AJAX Alert
var VAR_AJAX_ALERT_0 = "";									// 서버와 일시적으로 연결할 수 없습니다. \n잠시 후에 새로고침(F5) 하시기 바랍니다

// Common
var VAR_COMMON_REGISUCCESS = ""; 							// 성공적으로 등록하였습니다.
var VAR_COMMON_DATEERROR = "";								// 종료날짜가 시작날짜 보다 작습니다. 기간을 다시 설정해 주십시오. 
var VAR_COMMON_SYSTEMSETFAIL = "";							// 시스템 설정 변경에 실패했습니다.	
var VAR_COMMON_IPINPUT = "";								// IP를 입력하세요.
var VAR_COMMON_PORTINPUT = "";								// Port를 입력하세요.
var VAR_COMMON_PORTINPUTRANGE = "";							// 입력하신 포트는 범위에 맞지 않습니다. 가능한 포트 범위(0~65535)
var VAR_COMMON_IPFORMAT = "";								// 입력하신 IP의 형식이 올바르지 않습니다. 
var VAR_COMMON_PEERIPFORMAT = "";							// 입력하신 PeerIP의 형식이 올바르지 않습니다.
var VAR_COMMON_ADCLISTLOAD = "";							// ADC 목록 로딩에 실패했습니다.  
var VAR_COMMON_EXPDATAEXIST	= "";							// 내보내기 데이터 존재 유무 검사에 실패했습니다.
var VAR_COMMON_STATRTDATE = ""; 							// 시작날짜를 입력하세요.
var VAR_COMMON_ENDDATE = ""; 								// 종료날짜를 입력하세요.
var VAR_COMMON_PASSWDINPUT = ""; 							// 비밀 번호를 입력하세요.
var VAR_COMMON_PASSWDCONFIRM = ""; 							// 비밀번호 확인을 입력하세요.
var VAR_COMMON_HISTORYDATAEXTRACT = ""; 					// 패킷 수집 이력 데이터 추출에 실패하였습니다.
var VAR_COMMON_STATUSEXTRACT = ""; 							// 패킷 수집 상태 추출에 실패하였습니다.
var VAR_COMMON_PASNOTSUPPORT = "";							// PAS에서는 지원되지 않는 기능입니다.
var VAR_COMMON_PASKNOTSUPPORT = "";							// PASK에서는 지원되지 않는 기능입니다.
var VAR_COMMON_F5NOTSUPPORT = "";							// F5에서는 지원되지 않는 기능입니다.
var VAR_COMMON_ALTEON23NOTSUPPORT = "";						// Alteon 2-3 series 에서는 지원되지 않는 기능입니다.
var VAR_COMMON_CPUMEMDATAEXTRACT = "";						// CPU/메모리 데이터 추출에 실패하였습니다.
var VAR_COMMON_AFEW = "";									// 개
var VAR_COMMON_BPS = "";									// bps
var VAR_COMMON_PPS = "";									// pps
var VAR_COMMON_PORT = "";									// 포트
var VAR_COMMON_IP = "";										// IP
var VAR_COMMON_MENUOPEN = "";								// 메뉴 열기                                        
var VAR_COMMON_MENUCLOSE = "";								// 메뉴 닫기 
var VAR_COMMON_ONLINE = "";									// 정상
var VAR_COMMON_OFFLINE = "";								// 단절
var VAR_COMMON_DISABLED = "";								// 꺼짐
var VAR_COMMON_MACFORMAT = "";								// 입력하신 MAC의 형식이 올바르지 않습니다.
var VAR_COMMON_LENGTHFORMAT = ""; 							// 입력하신 값의 길이가 올바르지 않습니다.
var VAR_COMMON_SPECIALCHAR = "";							// 특수문자가 포함되었습니다. 입력하신 값은 범위에 맞지 않습니다.						
var VAR_COMMON_NUMLENGTH = "";								// 숫자만 허용 가능합니다. 입력하신 값은 길이에 맞지 않습니다.
var VAR_COMMON_NUMRANGE = "";								// 숫자만 허용 가능합니다. 입력하신 값은 범위에 맞지 않습니다.
var VAR_COMMON_EMAILFORMAT = "";							// 입력하신 EAMIL 주소의 형식이 올바르지 않습니다.
var VAR_COMMON_PHONEFORMAT = "";							// 입력하신 전화번호의 형식이 올바르지 않습니다.
var VAR_COMMON_IPPORTFORMAT = "";							// 입력하신 IP:PORT의 형식이 올바르지 않습니다.
var VAR_COMMON_ALLADC = "";									// 전체 ADC
var VAR_COMMON_PAGE = "";									// 페이지

// 단위
var VAR_COMMON_KBPS = "";                                   // Kbps
var VAR_COMMON_MBPS = "";                                   // Mbps
var VAR_COMMON_GBPS = "";                                   // Gbps
var VAR_COMMON_TBPS = "";                                   // Tbps
var VAR_COMMON_KPPS	= "";									// Kpps
var VAR_COMMON_MPPS = "";									// Mpps
var VAR_COMMON_GPPS = "";									// Gpps
var VAR_COMMON_TPPS = "";									// Tpps
var VAR_COMMON_TPS = "";                                    // tps
var VAR_COMMON_KTPS = "";                                   // Ktps
var VAR_COMMON_MTPS = "";                                   // Mtps
var VAR_COMMON_GTPS = "";                                   // Gtps
var VAR_COMMON_TTPS = "";                                   // Ttps
var VAR_COMMON_RPS = "";                                    // rps
var VAR_COMMON_KRPS = "";                                   // Krps
var VAR_COMMON_MRPS = "";                                   // Mrps
var VAR_COMMON_GRPS = "";                                   // Grps
var VAR_COMMON_TRPS = "";                                   // Trps

//Protocol
var VAR_COMMON_TCP = "";									// TCP
var VAR_COMMON_UDP = "";									// UDP
var VAR_COMMON_ICMP = "";									// ICMP

//Time
var VAR_COMMON_HOUR = "";									// hours
var VAR_COMMON_MIN = "";									// minutes
var VAR_COMMON_SEC = "";									// seconds

// Common VS
var VAR_COMMON_VSLFAIL = "";				 				// Virtual Server 데이터 로딩에 실패했습니다.
var VAR_COMMON_PVSAMFAIL = "";								// Peer 장비의 Virtual Server 추가/수정에 실패했습니다.
var VAR_COMMON_VSAMFAIL = "";								// Virtual Server 추가/수정에 실패했습니다.
var VAR_COMMON_AVAIL = "";									// 사용 가능
var VAR_COMMON_NAVAIL = "";									// 사용 불가능
var VAR_COMMON_VSPRDISAGREE = "";							// 입력하신 가상서버 포트는 범위에 맞지 않습니다. 가능한 포트 범위(10~65534)
var VAR_COMMON_EMSELECT = "";								// Enable을 원하는 Member를 선택하십시오.
var VAR_COMMON_DMSELECT = "";								// Disable을 원하는 Member를 선택하십시오.
var VAR_COMMON_MDSELECT = "";								// 삭제를 원하는 Member를 선택하십시오.
var VAR_COMMON_MDEL = "";									// Member를 삭제 하시겠습니까?
var VAR_COMMON_PMSELECT = "";								// 포트변경을 원하는 Member를 선택하십시오.
var VAR_COMMON_VNLFAIL = "";								// Virtual Server 이름 로딩에 실패했습니다.
var VAR_COMMON_VEIFAIL = "";								// Virtual Server 존재 유무 검사에 실패했습니다.
var VAR_COMMON_IIFORMAT = "";								// IP 주소 형식이 올바르지 않습니다.
var VAR_COMMON_VEFAIL = "";									// Virtual Server 데이터 추출에 실패했습니다.
var VAR_COMMON_REFAIL = "";									// Real Server 데이터 추출에 실패했습니다.
var VAR_COMMON_ARMEMBER = "";								// 은(는) 이미 등록된 멤버입니다.
var VAR_COMMON_PINPUT = "";									// 포트정보를 입력하세요.
var VAR_COMMON_PEIFAIL = "";								// Peer 장비 존재 유무 검사에 실패했습니다.
var VAR_COMMON_PIFORMAT = "";								// 포트형식이 올바르지 않습니다.
var VAR_COMMON_PORT = "";									// 의 포트
var VAR_COMMON_VIFORMAT = "";								// 입력하신 가상서버 IP는 올바른 형식이 아닙니다.
var VAR_COMMON_VNOTALLOWED = "";							// 입력하신 가상서버 이름은 허용되지 않습니다.
var VAR_COMMON_IPPORT_DUPLICATION = "";						// 중복된 IP Port 입니다. 다시 입력해주세요.

// slbSchedule
var VAR_SCHEDULE_REQ_SELECT = "";							// 선택을 원하는 요청자를 선택하십시오.
var VAR_SCHEDULE_REQ_DSELECT = "";							// 삭제를 원하는 요청자를 선택하십시오.
var VAR_SCHEDULE_REQ_DEL = "";								// 삭제를 원하시는 요청자를 선택하십시오.
var VAR_VS_SCHEDULEUSER_CNTFAIL = "";						// slb스케줄링 사용자 개수 추출에 실패했습니다.
var VAR_VS_SCHEDULEUSER_LOADFAIL = "";						// slb스케줄링 사용자 페이지 로딩에 실패했습니다.
var VAR_VS_SCHEDULEUSER_ADDFAIL = "";						// slb스케줄링 사용자 추가에 실패했습니다.
var VAR_VS_SCHEDULEUSER_DELFAIL = "";						// slb스케줄링 사용자 삭제에 실패했습니다.
var VAR_VS_SCHEDULEUSER_MODIFYFAIL = "";					// slb스케줄링 사용자 변경에 실패했습니다.
var VAR_VS_SCHEDULEUSER_LOADFAIL = "";						// slb스케줄링 요청자 페이지 로딩에 실패했습니다.
var VAR_VS_SCHEDULEUSER_DELFAIL = "";						// slb스케줄링 요청자 삭제에 실패했습니다.
var VAR_VS_SCHEDULERESP_LOADFAIL = "";						// slb스케줄링 수신자 페이지 로딩에 실패했습니다.
var VAR_VS_SCHEDULECNTFAIL = "";							// slb스케줄링 개수 추출에 실패했습니다.
var VAR_VS_SCHEDULELOADFAIL = "";							// slb스케줄링 페이지 로딩에 실패했습니다.
var VAR_VS_SCHEDULEDELSEL = "";								// 삭제를 원하시는 스케줄을 선택하십시오.
var VAR_VS_SCHEDULEDEL = "";								// 스케줄을 삭제 하시겠습니까?
var VAR_VS_SCHEDULEDLFAIL = "";								// 스케줄 삭제에 실패했습니다.
var VAR_VS_SCHEDULE_NAMEINPUT = "";							// 이름을 입력하세요.
var VAR_VS_SCHEDULE_TEAMINPUT = "";							// 부서/팀을 입력하세요.
var VAR_VS_SCHEDULE_PHONEINPUT = "";						// 전화번호를 입력하세요.
var VAR_VS_SCHEDULE_RECEIVERSEL = "";						// 수신자를 선택하세요.
var VAR_VS_SCHEDULE_ADDFAIL = ""; 							// slb스케줄링 등록에 실패했습니다.
var VAR_VS_SCHEDULE_MODIFYFAIL = ""; 						// slb스케줄링 수정에 실패했습니다.

// daterangepicker
var VAR_DATE_TODAY = "";									// 오늘
var VAR_DATE_YESTERDAY = "";								// 최근 1일
var VAR_DATE_LAST7DAYS = "";								// 최근 7일
var VAR_DATE_LAST30DAYS = "";								// 최근 30일
var VAR_DATE_THISMONTH = "";								// 이번 달
var VAR_DATE_LASTMONTH = "";								// 지난 달

// sysSetting
var VAR_SYSSETTING_LOADUSERLISTCONTENT = "";				// 사용자 목록 로딩에 실패했습니다. 
var VAR_SYSSETTING_LOADUSERADDCONTENT = "";					// 사용자 관리 페이지 로딩에 실패했습니다.
var VAR_SYSSETTING_USERADDMODIFY = "";						// 사용자 추가/편집에 실패했습니다.
var VAR_SYSSETTING_PASSWDRESET = "";						// 비밀번호를 초기화하시겠습니까?
var VAR_SYSSETTING_IDINPUT = "";							// 아이디를 입력하세요.
var VAR_SYSSETTING_IDRULEWRONG = "";						// 아이디 규칙이 맞지 않습니다. \n 아이디는 알파벳으로 시작하고 알파벳과 숫자 조합의 5~16자 사이로 구성됩니다.
var VAR_SYSSETTING_USERNAMEINPUT = "";						// 사용자 이름을 입력하세요.
var VAR_SYSSETTING_PASSWDINPUT = "";						// 비밀 번호를 입력하세요.
var VAR_SYSSETTING_PASSWDCONFIRMINPUT = "";					// 비밀번호 확인을 입력하세요.
var VAR_SYSSETTING_PASSWDMATCH = "";						// 비밀번호와 비밀번호 확인 입력 값이 일치하지 않습니다.
var VAR_SYSSETTING_PASSWDRULEWRONG = "";					// 비밀번호 규칙이 맞지 않습니다. \n 비밀번호는 알파벳과 숫자 조합의 5~16자 사이로 구성됩니다.
var VAR_SYSSETTING_PASSWD_RULEWRONG = "";					// 비밀번호 규칙이 맞지 않습니다. \n 비밀번호는 알파벳과 숫자 조합의 8~16자 사이로 구성됩니다.
var VAR_SYSSETTING_STARTDATEINPUT = "";						// 시작날짜를 입력하세요.
var VAR_SYSSETTING_ENDDATEINPUT = "";						// 종료날짜를 입력하세요.
var VAR_SYSSETTING_PASSWDRESET = "";						// 비밀번호를 초기화하였습니다.
var VAR_SYSSETTING_PASSWDRESETFAIL = "";					// 사용자 계정의 패스워드 초기화에 실패했습니다.
var VAR_SYSSETTING_ACCOUNTDELSEL = "";						// 삭제를 원하시는 계정을 선택하십시오.
var VAR_SYSSETTING_ACCOUNTDEL = "";							// 계정을 삭제 하시겠습니까?
var VAR_SYSSETTING_ACCOUNTDELFAIL = ""; 					// 사용자 삭제에 실패했습니다.
var VAR_SYSSETTING_SYSTEMINFOLOAD = ""; 					// 시스템 설정 정보 로딩에 실패했습니다.
var VAR_SYSSETTING_ADMINLOGLOAD	= "";						// 로그 파일 로딩에 실패했습니다.
var VAR_SYSSETTING_ACCOUNTIDCHECK = "";						// 중복된 아이디입니다. 다시 입력해주세요.  
var VAR_SYSSETTING_SYSTEM_TIME_SYNC_FAIL = "";				// 시스템 시간 동기화에 실패했습니다.
var VAR_SYSSETTING_NOVSADC = "";							// RS선택에서 해당 ADC가 존재하지 않습니다.
var VAR_SYSSETTING_NORSADC = "";							// VS선택에서 해당 ADC가 존재하지 않습니다. 
var VAR_SYSSETTING_GROUPDELSEL = "";						// 삭제를 원하시는 그룹을 선택하십시오.
var VAR_SYSSETTING_GROUPDEL = "";							// 서비스 그룹을 삭제 하시겠습니까?
var VAR_SYSSETTING_DASHGROUPDEL = "";						// 대시보드에 등록된 서비스 그룹이 함께 삭제 됩니다.
var VAR_SYSSETTING_GROUPDELFAIL = "";						// 서비스 그룹 삭제에 실패했습니다.
var VAR_SYSSETTING_GROUPINFOLOAD = "";						// 서비스 그룹 정보 로딩에 실패했습니다.
var VAR_SYSSETTING_GROUPNAME = "";							// 서비스 그룹이름을 입력하세요.
var VAR_SYSSETTING_LOGFILESEL = "";							// 로그파일을 선택하세요.

// sysTools
var VAR_SYSTOOLS_RESPADDFAIL = "";							// 구간 추가에 실패했습니다.
var VAR_SYSTOOLS_RESPLOADFAIL = "";							// 구간 응답시간 데이터 로딩에 실패했습니다.
var VAR_SYSTOOLS_RESPDELSEL = "";							// 삭제를 원하시는 구간을 선택하십시오.
var VAR_SYSTOOLS_RESPDEL = "";								// 구간을 삭제 하시겠습니까?
var VAR_SYSTOOLS_RESPDELFAIL = "";							// 구간 삭제에 실패했습니다.	
var VAR_SYSTOOLS_TOP = "";									// 최상단입니다.
var VAR_SYSTOOLS_BOTTOM = "";								// 최하단입니다.
var VAR_SYSTOOLS_RESPNAME = "";								// 구간 이름을 입력하세요.
var VAR_SYSTOOLS_RANGEOVER = "";							// 추가 허용 개수가 초과되었습니다.
var VAR_SYSTOOLS_MOVEONEROWSEL = "";						// 이동하려는 행을 하나만 선택해주세요.
var VAR_SYSTOOLS_MOVEROWSEL = "";							// 이동 하려는 행을 선택해주세요.
var VAR_SYSTOOLS_DELROWSEL = "";							// 삭제 하려는 행을 선택해주세요.
var VAR_SYSTOOLS_ATLEASTONE	= "";							// 최소 하나의 멤버는 있어야 합니다.

// sysBack
var VAR_SYSBACK_EXTRACTION = ""; 							// 백업 정보 추출에 실패했습니다.
var VAR_SYSBACK_LOAD = ""; 									// 백업 정보 로딩에 실패했습니다.
var VAR_SYSBACK_DELSEL = ""; 								// 삭제를 원하시는 백업을 선택하십시오.
var VAR_SYSBACK_DEL = ""; 									// 백업을 삭제 하시겠습니까?
var VAR_SYSBACK_FILENAMEINPUT = ""; 						// 백업 파일명을 입력하세요.
var VAR_SYSBACK_DOWNLOAD = ""; 								// 백업 정보 다운로드에 실패했습니다.
var VAR_SYSBACK_DELFAIL = ""; 								// 백업정보 삭제에 실패했습니다.
var VAR_SYSBACK_ADDMODIFYFAIL = ""; 						// 백업 추가/변경에 실패했습니다.

// sysInfo
var VAR_SYSINFO_LOAD = "";									// 시스템 정보 로딩에 실패했습니다.

//시스템관리 (라이선스) - license
var VAR_LICENSE_LOAD = "";									// 라이센스 정보 로딩에 실패했습니다.
var VAR_LICENSE_FILEUPLOAD = "";							// 라이센스 파일 업로드에 실패했습니다.
var VAR_LICENSE_FILENAMEDIFFER = "";						// 파일명이 다릅니다.
var VAR_LICENSE_NOTLICFILE = "";							// lic 파일이 아닙니다.
var VAR_LICENSE_NOFILE = "";								// 선택된 파일이 없습니다.

//시스템관리 (설정) - config
var VAR_CONFIG_LOAD = "";									//시스템 설정 정보 로딩에 실패했습니다.
var VAR_CONFIG_ADCLOGFILTERSEL = "";						//삭제를 원하시는 ADC 로그 필터를 선택하십시오.
var VAR_CONFIG_ADCLOGFILTERDEL = "";						//ADC 로그 필터를 삭제 하시겠습니까?
var VAR_CONFIG_PATTERNINPUT = "";							//패턴을 입력하세요.
var VAR_CONFIG_AGREETYPESEL = "";							//일치타입을 선택하세요.
var VAR_CONFIG_SECTION = "";								//부분
var VAR_CONFIG_WHOLE = "";									//전체
var VAR_CONFIG_NETMASKINPUT = "";							//Netmask를 입력하세요.
var VAR_CONFIG_GATEWAYINPUT = "";							//Gateway를 입력하세요.
var VAR_CONFIG_SYSTEMNAMEINPUT = "";						//시스템명을 입력하세요.
var VAR_CONFIG_SYNCCYCLEINPUT = "";							//ADC 설정 동기화 주기를 입력하세요.
var VAR_CONFIG_LISTCOUNTINPUT = "";							//목록 표시 개수를 입력하세요.
var VAR_CONFIG_ADCMONITCYCLE = "";							//주기는 30초에서 600초 사이의 시간을 입력하세요.
var VAR_CONFIG_VSSELECT = "";								//VS를 선택해주세요. 
var VAR_CONFIG_respTimeInterval = "";						// 구간 응답시간은 60초에서 600초 사이의 시간을 입력하세요.

// 경보 Alert
var VAR_ALERT_LOAD = "";									// 경보 설정 데이터 로딩에 실패했습니다.
var VAR_ALERT_SETFAIL = "";									// 경보 설정에 실패했습니다.
var VAR_ALERT_RESPABNORMAL = ""; 							// 응답시간이 비정상적입니다. 0~10000 사이값을 입력하십시오.

// slb이력 AdcHistory
var VAR_HISTORY_LOAD = "";									// 설정 이력 데이터 로딩에 실패했습니다.
var VAR_HISTORY_RECOVERINSPECTION = "";						// 설정 이력 복구 여부 검사에 실패했습니다.
var VAR_HISTORY_PEERSLBRECOVER = "";						// Peer 장비의 SLB 복구에 실패했습니다.							
var VAR_HISTORY_PEERINFOEXTRACT = "";						// Peer 장비 정보 추출에 실패했습니다.
var VAR_HISTORY_SLBRECOVER = "";							// SLB 복구에 실패했습니다.

// AdcSetting
var VAR_ADCSETTING_ADCDELSEL = "";							// 삭제를 원하시는 ADC를 선택하십시오.                        
var VAR_ADCSETTING_ADCDEL = "";								// ADC를 삭제 하시겠습니까?                              
var VAR_ADCSETTING_ADCINFOSAVEFAIL = "";					// ADC 정보 저장에 실패했습니다.                           
var VAR_ADCSETTING_ADCINFODDELFAIL = "";					// ADC 정보 삭제에 실패했습니다.                           
var VAR_ADCSETTING_ADDMODIFYFAIL = "";						// ADC 정보 추가/변경에 실패했습니다.                        
var VAR_ADCSETTING_ADCKINDNOTCLEAR = "";					// ADC 종류가 명확하지 않습니다.                           
var VAR_ADCSETTING_ADCNAMEINPUT = "";						// ADC 이름을 입력하세요.                               
var VAR_ADCSETTING_PORTFORMAT = "";							// 입력하신 통신포트는 형식에 맞지 않습니다.                      
var VAR_ADCSETTING_PORTRANGE = "";							// 입력하신 통신포트는 범위에 맞지 않습니다. \n 가능한 포트 범위(0~65535)
var VAR_ADCSETTING_ADCGROUPSEL = "";						// ADC 그룹을 선택하세요.                               
var VAR_ADCSETTING_CONNTESTNOT = "";						// 연결 테스트를 수행하지 않았습니다. \n 등록을 원하시면 확인을 눌러 주십시오.    
var VAR_ADCSETTING_CONNTESTSUCCESS = "";					// 연결 테스트 성공                                    
var VAR_ADCSETTING_NETWORKERROR = "";						// 연결 테스트 실패 (네트워크 오류)                                    
var VAR_ADCSETTING_LOGINFAIL = "";							// 연결 테스트 실패 (로그인 실패)                                     
var VAR_ADCSETTING_SNMPERROR = "";							// 연결 테스트 실패 (SNMP Community 오류)            
var VAR_ADCSETTING_NOTSUPPORT = "";							// 연결 테스트 실패 (지원하지 않는 버전)  
var VAR_ADCSETTING_CONNTESTFAIL = "";						// 연결 테스트를 실패하였습니다.                              
var VAR_ADCSETTING_LOGINIDINPUT = "";						// ADC 로그인 아이디를 입력하세요.                          
var VAR_ADCSETTING_PWINPUT = "";							// ADC 로그인 패스워드를 입력하세요.   
var VAR_ADCSETTING_CLILOGINIDINPUT = "";					// CLI 로그인 아이디를 입력하세요.
var VAR_ADCSETTING_CLIPWINPUT = "";							// CLI 로그인 비밀번호를 입력하세요.
var VAR_ADCSETTING_SNMPINPUT = "";		 					// SNMP를 입력하세요.
var VAR_ADCSETTING_SYSLOGIPINPUT = "";                      // SYSLOGIP를 입력하세요.
var VAR_ADCSETTING_ADCGROUPDEL = "";						// ADC 그룹을 삭제 하시겠습니까?
var VAR_ADCSETTING_ADCGROUPUSED = "";
var VAR_ADCSETTING_GROUPNAMEINPUT = "";						// 그룹명을 입력하세요.                                  
var VAR_ADCSETTING_ADCGROUPDETSEL = "";						// 삭제를 원하시는 ADC 그룹을 선택하십시오.                     
var VAR_ADCSETTING_ADCGROUPDELFAIL = "";					// ADC 그룹 삭제에 실패했습니다.                           
var VAR_ADCSETTING_ADCINFOEXTRACT = "";						// ADC 정보 추출에 실패했습니다.                           
var VAR_ADCSETTING_FLBGROUPINFOEXTRACT ="";					// FLB 그룹 정보 추출에 실패하였습니다.
var VAR_ADCSETTING_FLBINFOSAVEFAIL ="";						// FLB 그룹 정보 저장에 실패했습니다.
var VAR_ADCSETTING_INVALIDLICENSE = "";						// ADC 정보를 추가할 수 없습니다. 라이선스를 확인해주십시오.

// FLB Filter Info
var VAR_FLBFILTERINFO_FILTERINFOEXTRACT= "";				// Filter 정보 추출에 실패했습니다.

// ConfigCheck
var VAR_CONFIGCHECK_SNMPSTATUSACTIVITY ="";					// 의 SNMP 상태를 활성화 합니다.
var VAR_CONFIGCHECK_RCOMMUNITYSYNC ="";						// 의 SNMP Read Community String 을 동기화 합니다.
var VAR_CONFIGCHECK_SYSLOGACTIVITY =""; 					// 의 SYSLOG 상태를 활성화 합니다.
var VAR_CONFIGCHECK_SYSLOGHOSTREGI =""; 					// 의 SYSLOG Host에 등록합니다.
var VAR_CONFIGCHECK_SNMPALLOWLISTREGI =""; 					// 의 SNMP Allow List에 등록합니다.
var VAR_CONFIGCHECK_VSTATSTATUSACTIVITY =""; 				// 의 VSTAT 상태를 활성화 합니다.
var VAR_CONFIGCHECK_ADCSETLOAD =""; 						// ADC 설정 데이터 로딩에 실패했습니다.
var VAR_CONFIGCHECK_ADCSETFAIL =""; 						// ADC 설정에 실패했습니다.

// adc로그, audit로그 logAnalysis
var VAR_LOGANALYSIS_EXTRACT =""; 							// ADC 로그 추출에 실패했습니다.
var VAR_SYSTEMLOGANALYSIS_EXTRACT =""; 						// 감사 로그 추출에 실패했습니다.

//보고서 - report
var VAR_REPORT_COUNTEXTRACT = "";							// 보고서 개수 추출에 실패했습니다.
var VAR_REPORT_LOAD = "";									// 보고서 데이터 로딩에 실패했습니다.
var VAR_REPORT_DELSEL = "";									// 삭제를 원하시는 보고서를 선택하십시오.
var VAR_REPORT_DEL = "";									// 보고서를 삭제 하시겠습니까?
var VAR_REPORT_VALIDATE = "";								// 보고서 유효성 검사에 실패했습니다.
var VAR_REPORT_DELFAIL = "";								// 보고서 삭제에 실패했습니다.
var VAR_REPORT_ADDFAIL = "";								// 보고서 추가에 실패했습니다.					
var VAR_REPORT_ADDMODIDFY = "";								// 보고서 추가/변경에 실패했습니다.
var VAR_REPORT_SYSTEMOPER = "";								// 시스템운영보고서_
var VAR_REPORT_FAILANALYSIS = "";							// 장애분석보고서_
var VAR_REPORT_L4OPER = "";									// L4운영보고서_
var VAR_REPORT_L4OPERDAY = "";								// L4운영보고서(일간)_
var VAR_REPORT_L4OPERWEEK = "";								// L4운영보고서(주간)_
var VAR_REPORT_L4OPERMONTH = "";							// L4운영보고서(월간)_					
var VAR_REPORT_NAMEINPUT = "";								// 보고서 이름을 입력하세요.					
var VAR_REPORT_ATLEASTONE = ""; 							// 최소한 1개 이상의 ADC 이름을 추가해 주십시오.
var VAR_REPORT_ADCNOTEXCLUDE = ""; 							// 해당 ADC는 제외 할 수 없습니다.
var VAR_REPORT_RESULTNOTSELECT = "";						// 진단결과가 선택되지 않았습니다.

//시스템관리 (비밀번호Pop) - pwchangewnd
var VAR_PW_CHANGESUCCESS = ""; 								// 비밀번호 변경이 완료 되었습니다.
var VAR_PW_CHANGEFAIL = ""; 								// 비밀번호 변경에 실패했습니다.
var VAR_PW_INPUTWRONG = "";									// 입력한 비밀번호가 틀립니다.
var VAR_PW_RULWRONG = ""; 									// 비밀번호 규칙이 맞지 않습니다. \n 비밀번호는 알파벳과 숫자 조합의 5~16자 사이로 구성됩니다.

// 패킷분석 faultAnalysis & faultAnalysisPop
var VAR_PKT_HISTORYDEL = "";								// 패킷 수집 이력 삭제에 실패하였습니다.
var VAR_PKT_LISTDELSEL = ""; 								// 삭제를 원하시는 패킷 목록을 선택하십시오.
var VAR_PKT_DEL = "";	 									// 패킷을 삭제 하시겠습니까?
var VAR_PKT_DOWNLOADFILEEXSITINPECT = "";					// 다운로드 파일 존재 유무 검사에 실패하였습니다.

var VAR_PKTPOP_INPROGRESS = ""; 							// 패킷 수집 진행중이기 때문에 페이지를 닫을 수 없습니다.
var VAR_PKTPOP_SRCIPALREADYSEL = ""; 						// Source IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_DSTIPALREAYSEL = ""; 						// Destination IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_SRCPORTALREADYSEL = ""; 						// Source Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_PORTALREADYSEL = ""; 						// Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_DSTPORTALREADYSEL = ""; 						// Destination Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_PROTOCOLALREADYSEL = ""; 					// Protocol은 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_HOSTALREADYSEL = ""; 						// Host는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_PKTPOP_COUNTLIMITOPTION = "";						// 패킷 개수로 제한옵션은 필수 조건입니다. 체크를 해제할 수 없습니다.
var VAR_PKTPOP_STARTFAIL = ""; 								// 패킷 수집 시작에 실패하였습니다.
var VAR_PKTPOP_STOPCANCEL = ""; 							// 패킷 수집 정지/취소에 실패하였습니다.
var VAR_PKTPOP_STOPFAIL = ""; 								// 패킷 수집 정지에 실패하였습니다.
var VAR_PKTPOP_DEL = ""; 									// 삭제
var VAR_PKTPOP_EXISTSAMESEARCHCOND = ""; 					// 동일한 검색조건이 존재합니다.
var VAR_PKTPOP_COUNTINPUT = ""; 							// 패킷 개수를 입력하세요.
var VAR_PKTPOP_ONLYNUMINPUT = ""; 							// 숫자만 입력가능합니다.
var VAR_PKTPOP_MAXNOTMORE_ALTEON = "";						// 패킷 개수는 최대  10,000개를 넘을 수 없습니다.
var VAR_PKTPOP_MAXNOTMORE_F5 = "";							// 패킷 개수는 최대  500,000개를 넘을 수 없습니다.
var VAR_PKTPOP_GREATERTHANZERO = ""; 						// 패킷 개수는 0보다는 커야합니다.
var VAR_PKTPOP_TIMEINPUT = ""; 								// 시간을 입력하세요.
var VAR_PKTPOP_TIMENOTEXCEEDMAX = "";						// 시간은 최대  600초를 넘을 수 없습니다.
var VAR_PKTPOP_CAPACITYINPUT = ""; 							// 용량을 입력하세요.
var VAR_PKTPOP_CAOACITYNOTEXCEEDMAX = "";					// 용량은 최대  10000Kbytes를 넘을 수 없습니다.
var VAR_PKTPOP_CAPACITYGREATERTHANZERO = "";				// 용량은 0보다는 커야합니다.
var VAR_PKTPOP_FILENAMEMUSTBEINPUT = ""; 					// 파일명은 반드시 입력하여야 합니다.
var VAR_PKTPOP_SRCIPFORMAT = "";  							// 입력하신 Souce_IP의 형식이 올바르지 않습니다.
var VAR_PKTPOP_DSTIPFORMAT = "";  							// 입력하신 Destination_IP의 형식이 올바르지 않습니다.
var VAR_PKTPOP_HOSTIPFORMAT = ""; 							// 입력하신 Host_IP 형식이 올바르지 않습니다.
var VAR_PKTPOP_SRCPORTFORMAT = ""; 							// 입력하신 Source_Port의 형식이 올바르지 않습니다.
var VAR_PKTPOP_DSTPORTFORMAT = ""; 							// 입력하신 Destination_Port의 형식이 올바르지 않습니다.
var VAR_PKTPOP_PORTFORMAT = ""; 	    					// 입력하신 Port의 형식이 올바르지 않습니다.
var VAR_PKTPOP_ADD = ""; 									// 추가
var VAR_PKTPOP_SRCIP = "";									// Source IP
var VAR_PKTPOP_DSTIP = "";									// Destination IP
var VAR_PKTPOP_SRCPORT = "";								// Source Port
var VAR_PKTPOP_DSTPORT = "";								// Destination Port
var VAR_PKTPOP_PROTOCOL = "";								// Protocol
var VAR_PKTPOP_HOST	= "";									// Host
var VAR_PKTPOP_ONEMORE = "";								// 최소한 1개 이상의 옵션을 추가해 주십시오.
// 진단이력 faultHistory
var VAR_FAULTHISTORY_COUNTEXTRACT = ""; 					// 장애 진단 이력 개수 추출에 실패했습니다.
var VAR_FAULTHISTORY_LOAD = ""; 							// 장애 진단 이력 데이터 로딩에 실패했습니다.
var VAR_FAULTHISTORY_SCHEDULELOAD = ""; 					// 장애 진단 예약 목록 로딩에 실패했습니다.
var VAR_FAULTHISTORY_SCHEDULEDEL = ""; 						// 장애 진단 예약 삭제에 실패했습니다.
var VAR_FAULTHISTORY_DELSEL = "";							// 삭제를 원하시는 장애 진단 이력을 선택하십시오.
var VAR_FAULTHISTORY_DEL = "";								// 장애 진단 이력을 삭제 하시겠습니까?
var VAR_FAULTHISTORY_DELFAIL = "";							// 장애 진단 이력 삭제에 실패했습니다.

// 진단결과 faultResult
var VAR_FAULTRESULT_LOAD = ""; 								// 장애 진단 이력 로딩에 실패했습니다.
var VAR_FAULTRESULT_CPUMEMDATALOAD = ""; 					// CPU/Memory 데이터 로딩에 실패했습니다.
var VAR_FAULTRESULT_PKTLOSSLOAD = ""; 						// 패킷 손실 정보 로딩에 실패했습니다.
var VAR_FAULTRESULT_INPROGRESSPRINT = ""; 					// 진행 상태 출력에 실패했습니다.
var VAR_FAULTRESULT_PKTDUMPFILEEXIST = "";					// 패킷 덤프 파일 존재 유무 검사에 실패했습니다
var VAR_FAULTRESULT_EXISTINPECT	= ""; 						// 장애 결과 데이터 존재 유무 검사에 실패했습니다.

// 진단 팝업 faultDiagnosis
var VAR_FAULTDIAGNOSIS_STATUSLOAD = ""; 					// 장애 진단 상태 로딩에 실패했습니다.
var VAR_FAULTDIAGNOSIS_INFOLOADING = ""; 					// 해당 진단 정보를 불러오는 중입니다.
var VAR_FAULTDIAGNOSIS_CANCEL = ""; 						// 진단취소
var VAR_FAULTDIAGNOSIS_PAGENOTCLOSE = ""; 					// 진단 진행중이기 때문에 페이지를 닫을 수 없습니다.
var VAR_FAULTDIAGNOSIS_DONE = ""; 							// 진단을 마쳤습니다.
var VAR_FAULTDIAGNOSIS_TIME = ""; 							// 진단시간 :
var VAR_FAULTDIAGNOSIS_FAIL = ""; 							// 진단에 실패하였습니다.
var VAR_FAULTDIAGNOSIS_ALREADYCANCEL = ""; 					// 이미 취소된 진단입니다.
var VAR_FAULTDIAGNOSIS_CANCELFAIL = ""; 					// 장애 진단 취소에 실패했습니다.
var VAR_FAULTDIAGNOSIS_CANCELSUCCESS = ""; 					// 진단을 취소하였습니다.

// 진단 faultSetting
var VAR_FAULTSETTING_LOAD = ""; 							// 장애 진단 페이지 로딩에 실패했습니다.
var VAR_FAULTSETTING_SCHEDULESUCC = ""; 					// 진단 예약 설정에 성공하였습니다.
var VAR_FAULTSETTING_TEMPLATESAVESUCC = "";					// 정책 템플릿 저장에 성공하였습니다.
var VAR_FAULTSETTING_TEMPLAGEMODIFYSUCC = "";				// 정책 템플릿 수정에 성공하였습니다.
var VAR_FAULTSETTING_REGISCHEDULE = "";						// 이미 등록된 장애검사 예약입니다.
var VAR_FAULTSETTING_FAIL = ""; 							// 장애 진단에 실패했습니다.
var VAR_FAULTSETTING_TEMPLATEDELFAIL = ""; 					// 장애 진단 템플릿 삭제에 실패했습니다.
var VAR_FAULTSETTING_SVCCLIENIPINPUT = ""; 					// 서비스 진단 사용자 IP를 입력하십시오.
var VAR_FAULTSETTING_ITEMNOTSEL = ""; 						// 진단 항목을 선택하지 않았습니다.
var VAR_FAULTSETTING_SCHEDULEITEMNOTSEL = ""; 				// 예약 항목을 선택하지 않았습니다.
var VAR_FAULTSETTING_TEMPLATEITEMNOTSEL = ""; 				// 템플릿 저장 항목을 선택하지 않았습니다.
var VAR_FAULTSETTING_ADCLOGCOUNT = ""; 						// ADC 로그 개수가 비정상적입니다. 1~100 사이의 정수로 입력하십시오.				
var VAR_FAULTSETTING_CLIENTIPINCCORECT = ""; 				// 입력하신 사용자 IP의 형식이 올바르지 않습니다.
var VAR_FAULTSETTING_TEMPLATENAMEINPUT = ""; 				// 템플릿 이름을 입력(지정) 하십시오.
var VAR_FAULTSETTING_ALREADYSAVENAME = ""; 					// 같은 템플릿 이름이 이미 저장되어 있습니다.
var VAR_FAULTSETTING_TMEPLATENAME = ""; 					// 템플릿 이름이 비정상적입니다. 1~64자 사이의 문자열로 입력하십시오.					
var VAR_FAULTSETTING_SPECIALCHAR = ""; 						// 특수문자는 입력하실수 없습니다.
var VAR_FAULTSETTING_TEMPLATEDEL = ""; 						// 정책 템플릿를 삭제 하시겠습니까?
var VAR_FAULTSETTING_TEMPLATEMODIADD = "";					// 진단 템플릿이 변경되었습니다.\n 저장 후 진단하시겠습니까?

//모니터링(장애) - faultLog
var VAR_FAULT_LOGCOUNTEXTRACT = ""; 						// 장애 로그 개수 추출에 실패했습니다.
var VAR_FAULT_LOGDATALOAD = ""; 							// 장애 로그 데이터 로딩에 실패했습니다.
var VAR_FAULT_WARNING = "";									// 경고
var VAR_FAULT_FAULT = "";									// 장애

//모니터링(장비) - appliance
var VAR_APPLIANCE_LAOD = ""; 								// ADC 장비 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_PERAVERAGE = ""; 							// 전일평균
var VAR_APPLIANCE_SESSIONUSAGELOAD = ""; 					// 세션 사용량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_TRAFFICUSAGELOAD = ""; 					// 트래픽 사용량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_CPUUSAGELOAD = ""; 						// CPU 사용량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_MEMUSAGELOAD = ""; 						// 메모리 사용량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_PKTERRGENLOAD = ""; 						// 패킷 오류 발생량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_LOSSLOAD = ""; 							// 손실 패킷량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_HTTPREQLOAD = ""; 						// Http 요청량 추이 데이터 로딩에 실패했습니다.
var VAR_APPLIANCE_MAX = "";									// 최대값
var VAR_APPLIANCE_SPMAX = "";								// 임계치
var VAR_APPLIANCE_SPAVG = "";								// 평균
var VAR_REALSERVER_USED = "";								// 사용
var VAR_REALSERVER_NOTUSED = "";							// 사용안함

//모니터링(L2) - L2
var VAR_L2_SEARCH = ""; 									// L2 검색에 실패했습니다.
var VAR_L2_LOAD = ""; 										// L2 검색 데이터 로딩에 실패했습니다.

// 모니터링(성능) - servicePerfomance
var VAR_SVCPERFOM_LAOD = ""; 								// 서비스 성능 데이터 로딩에 실패했습니다.
var VAR_SVCPERFOM_REALRESPONSENOTSUPPORT = ""; 				// 실시간 조회에서는 응답시간 조회를 지원하지 않습니다.
var VAR_SVCPERFOM_EXPEXISTINSPECT = "";						// 서비스 성능 데이터 내보내기 데이터 존재 유무 검사에 실패했습니다.
var VAR_SVCPERFOM_MEMBERLOAD = "";							// Member 데이터 로딩에 실패했습니다.
var VAR_SVCPERFOM_PREDAY = "";								// 전일
var VAR_SVCPERFOM_PREWEEKS = "";							// 전주
var VAR_SVCPERFOM_PREMONTH = "";							// 전월
var VAR_SVCMONITOR_YES = "";								// 있음
var VAR_sVCMONITOR_NOT ="";									// 없음
var VAR_SVCMONITOR_RESPTIME = "";							// 응답시간

var VAR_SVCPERFOM_ALLGRP_AVAILABLE_COUNTCHK = "";			// 선택가능한 서비스 개수는 5개입니다.

// GROUP / REAL SERVER
var VAR_REALSERVER_LAODFAIL	= "";							// RealServer 모니터링 데이터 로딩에 실패했습니다.
var VAR_GROUP_LAODFAIL = "";								// GROUP 모니터링 데이터 로딩에 실패했습니다.
var VAR_GROUP_NAME = "";									// GROUP 이름

// 모니터링(VS요약) - networkMap
var VAR_NETWORK_VSSUMMARYLOAD = "";							// VS 요약 데이터 로딩에 실패했습니다.

// 모니터링(세션) - session
var VAR_SESSIONMOR_SEDLFAIL = "";							// 세션 데이터 로딩에 실패했습니다.                                
var VAR_SESSIONMOR_SEDCFAIL = "";							// 세션 데이터 개수 추출에 실패했습니다.                           
var VAR_SESSIONMOR_SEDSFAIL = ""; 							// 세션 데이터 검색에 실패했습니다.                                
var VAR_SESSIONMOR_SEDSOFAIL = "";							// 세션 데이터 정렬에 실패했습니다.                             
var VAR_SESSIONMOR_SRCASEL = "";							// Client IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_SESSIONMOR_VIPASEL = "";							// Virtual IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_SESSIONMOR_DSTASEL = "";							// Destination IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_SESSIONMOR_REALASEL = "";							// Real IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.                             
var VAR_SESSIONMOR_SRCPALSEL = "";							// Client Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.                                
var VAR_SESSIONMOR_DSTPASEL	= ""; 							// Destination Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_SESSIONMOR_VPASEL	= ""; 							// Virtual Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
var VAR_SESSIONMOR_REALPASEL = "";							// Real Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.                           
var VAR_SESSIONMOR_PROALRSEL = "";							// Protocol은 이미 선택 되었습니다. 다른 키워드를 선택하세요.                                   
var VAR_SESSIONMOR_SRCIDOMI = "";							// Client IP와 Virtual IP중 하나는 반드시 입력 하여야 합니다.  
var VAR_SESSIONMOR_SRCIIFORM = "";							// 입력하신 Client IP의 형식이 올바르지 않습니다.                   
var VAR_SESSIONMOR_SRCINPUT = "";							// Client IP를 입력 하세요.                                        
var VAR_SESSIONMOR_DSTIFORM = "";							// 입력하신 Virtual IP의 형식이 올바르지 않습니다.             
var VAR_SESSIONMOR_DSTINPUT = "";							// Virtual IP를 입력 하세요.                                   
var VAR_SESSIONMOR_REALIFORM = "";							// 입력하신 real IP의 형식이 올바르지 않습니다.             
var VAR_SESSIONMOR_REALINPUT = "";							// real IP를 입력 하세요.                                   
var VAR_SESSIONMOR_SRCPIFORM = "";							// 입력하신 ClientPort의 형식이 올바르지 않습니다.                
var VAR_SESSIONMOR_DSTPIFORM = "";							// 입력하신 Virtual Port의 형식이 올바르지 않습니다.           
var VAR_SESSIONMOR_REALPIFORM = "";							// 입력하신 real Port의 형식이 올바르지 않습니다.           
var VAR_SESSIONMOR_ADD = "";								// 추가                                                            
var VAR_SESSIONMOR_DEL = "";								// 삭제   
var VAR_SESSIONMOR_CIP = "";								// Client IP
var VAR_SESSIONMOR_VIP = "";								// Virtual IP
var VAR_SESSIONMOR_DIP = "";								// Destination IP
var VAR_SESSIONMOR_REALIP = "";								// Real IP
var VAR_SESSIONMOR_CPORT = "";								// Client Port
var VAR_SESSIONMOR_VPORT = "";								// Virtual Port
var VAR_SESSIONMOR_REALPORT = "";							// Real Port
var VAR_SESSIONMOR_DPORT = "";								// Destination Port
var VAR_SESSIONMOR_PROTO = "";								// Protocol

// 모니터링(인터페이스)) - statistics
var VAR_STATISTICS_LOADFAIL = "";							// 인터페이스 정보 로딩에 실패했습니다.                           
var VAR_STATISTICS_USAGLOADFAIL = "";						// 인터페이스별 사용량 그래프 로딩에 실패했습니다.                
var VAR_STATISTICS_AFEW = "";								// 개                                                             
var VAR_STATISTICS_TRAFFICBPS = "";							// 트래픽_bps                                                     
var VAR_STATISTICS_TRAFFICPPS = "";							// 트래픽_pps                                                     
var VAR_STATISTICS_ERRGEN = "";								// 오류 발생                                                      
var VAR_STATISTICS_LOSSGEN = "";							// 손실 발생                                                      
var VAR_STATISTICS_CHECKINSEL = "";							// 확인 할 인터페이스를 선택 해 주세요.               

// virtualserver (alteon) - alteonvs
var VAR_ALT_VSDSEL = "";								 	// 삭제를 원하시는 Virtual Service를 선택하십시오.
var VAR_ALT_VSDEL = "";								 		// Virtual Service를 삭제 하시겠습니까?
var VAR_ALT_IIINCOR = "";									// Index(ID)값이 잘못 입력되었습니다. 1~1024 사이의 정수로 입력하십시오.          
var VAR_ALT_RPRDISAG = "";	 							 	// 입력하신 Real서버 포트는 범위에 맞지 않습니다. 가능한 포트 범위(0~65535)       
var VAR_ALT_VLFAIL = "";									// Virtual GROUP 데이터 로딩에 실패했습니다.                                      
var VAR_ALT_AIREAL = "";									// 은(는) 이미 REAL SERVER 목록에 있습니다. REAL SERVER 목록에서 선택 하세요.     
var VAR_ALT_SERDUP = "";									// 중복된 서비스 포트입니다. 새로운 포트를 지정하세요.                            
var VAR_ALT_REANNDEL = "";									// Alteon REAL SERVER Id가 없어서 삭제 됩니다.                                    
var VAR_ALT_SER_INP = "";									// 서비스 포트를 입력하세요.                                                      
var VAR_ALT_MEMINP = "";									// 멤버를 등록하세요.                                                             
var VAR_ALT_NAMEDUPLICATE = "";								// 이름이 중복되었습니다. 다시 입력해 주세요.

// virtualserver (f5) - f5vs
var VAR_F5_SER_NINPUT = "";									// 서비스포트가 입력되지 않았습니다.
var VAR_F5_GRO_NINPUT = "";									// GROUP 이름이 입력되지 않았습니다
var VAR_F5_NAPP = "";										//  지정 안함
var VAR_F5_ID = "";											// 입력하신 ID는 알파벳으로 시작하며, 문자는 .-_만 허용됩니다.
var VAR_F5_SNMP	= "";										// 잘못된 문자 입니다. 오직 특수문자는 해당 특수문자만 허용 합니다. 

// virtualserver (pask) - paskvs
var VAR_PASK_HEAEFAIL = "";									// Health Check 데이터 추출에 실패했습니다.
var VAR_PASK_PORTIFORMAT = "";								// 포트형식이 올바르지 않습니다. 0~65545 사이의 정수로 입력하십시오.

// virtualserver (pas) - pasvs
var VAR_PAS_PORNINP = "";									// 포트가 입력되지 않았습니다.

// virtual_pop
var VAR_VPMEMBER_IPNFORMAT = "";			 				// [IP주소]:[포트번호]의 형식이 아닙니다. 입력된 내용을 확인하시고 다시 입력하여 주시기 바랍니다.
var VAR_VPPERSI_PROEXTFAIL = "";							// 프로파일 데이터 추출에 실패했습니다.
var VAR_VPPORT_PORNNUM = "";								// 입력된 포트 값이 숫자가 아닙니다.
var VAR_VPVSER_SEARFAIL = "";								// Virtual Server 검색에 실패했습니다.  
var VAR_VPVSER_LOADFAIL = "";								// Virtual Server 데이터 로딩에 실패했습니다.

// virtualserver - virtualserver
var VAR_VS_VSECEFAIL = "";									// Virtual Server/Service 개수 추출에 실패했습니다.
var VAR_VS_VSELFAIL = "";									// Virtual Server 페이지 로딩에 실패했습니다.
var VAR_VS_ENAVSELE = "";									// Enable을 원하시는 Virtual Server를 선택하십시오.
var VAR_VS_ENAFAIL = "";									// Virtual Server Enable에 실패했습니다. 
var VAR_VS_PEEVEFAIL = "";									// Peer 장비의 Virtual Server Enable에 실패했습니다.
var VAR_VS_DISVSEL = "";									// Disable을 원하시는 Virtual Server를 선택하십시오.
var VAR_VS_VSEDFAIL = "";									// Virtual Server Disable에 실패했습니다.
var VAR_VS_VSEDSEL = "";									// 삭제를 원하시는 Virtual Server를 선택하십시오.
var VAR_VS_VSEDEL = "";										// Virtual Server를 삭제 하시겠습니까?
var VAR_VS_VIRDFAIL = "";									// Virtual Server 삭제에 실패했습니다.
var VAR_VS_PEEVDFAIL = "";									// Peer 장비의 Virtual Server 삭제에 실패했습니다.
var VAR_VS_PEEVDIFAIL = "";									// Peer 장비의 Virtual Server Disable에 실패했습니다.
var VAR_VS_VSTIMEERROR = "";								// 설정에 실패했습니다.\n설정을 시도하기 전에 설정 변경 내역이 있습니다.
var VAR_VS_HASNULLVALUE = "";								// 이전 설정에 복구 불가능한 설정이 존재합니다.\n복구를 진행할 수 없습니다.
var VAR_VS_INTERFACEAREA = "";								// VRRP Interface 추가 시 가능한 IP 범위를 초과하였습니다.

// virtualserver (profile) - profile
var VAR_PROFILE_LFAIL = "";									// 프로파일 데이터 로딩에 실패했습니다.
var VAR_PROFILE_DSELE = "";									// 삭제를 원하시는 프로파일을 선택하십시오.
var VAR_PROFILE_DEL = "";									// 프로파일을 삭제 하시겠습니까?
var VAR_PROFILE_REGI_SUCC = "";								// 성공적으로 등록하였습니다.
var VAR_PROFILE_ADDFAIL = "";								// 프로파일 추가에 실패했습니다. 
var VAR_PROFILE_NNOTALL = "";								// 입력하신 이름은 허용되지 않습니다.
var VAR_PROFILE_TIMPIALLOW = ""; 							// Timeout값은 양의 정수만 허용됩니다.
var VAR_PROFILE_DELFAIL = "";								// 프로파일 삭제에 실패했습니다. 
var VAR_PROFILE_NAME = "";									// 프로파일 이름을 입력하세요.
var VAR_PROFILE_USED = "";									// 사용중인 프로파일은 삭제할 수 없습니다.
var VAR_PROFILE_NOTEXIST = "";								// 존재하지 않는 프로파일입니다.
var VAR_VS_VSPORTRANGE = "";								// 포트의 범위가 맞지 않습니다. (가능한 포트 범위 0~65534)

// node (realserver)
var VAR_NODE_NAME_INPUT = "";								// RealServerGroup 이름을 입력하세요.
var VAR_NODE_GROUP_MOVENOT = "";  							// 이동할 Group이 없습니다.
var VAR_NODE_NOT_SELECTED = ""; 							// 선택된 real server가 없습니다.
var VAR_NODE_SELECTED_ALREADY = "";							// 선택된 real server 에는 이미
var VAR_NODE_STATUS_HAVE = "";								// 상태가 있습니다.
var VAR_NODE_GROUP_MODIFYFAIL = "";							// Real Server Group 수정에 실패했습니다.
var VAR_NODE_GROUP_ADDFAIL = "";							// Real Server Group 추가에 실패했습니다.
var VAR_NODE_GROUP_DEL = "";								// Real Server 그룹을 삭제하시겠습니까?
var VAR_NODE_GROUP_DELFAIL = "";							// Real Server Group 삭제에 실패했습니다.
var VAR_NODE_GROUP_FEWMOVE = "";							// 개의 Real Server를 선택한 그룹으로 이동하시겠습니까?
var VAR_NODE_GROUP_MOVEFAIL = "";							// Real Server Group으로 이동에 실패했습니다.
var VAR_NODE_GROUP_STATUSMODIFY = "";						// 상태를 변경하시겠습니까?
var VAR_NODE_GROUP_STATUSMODIFYS = "";						// 개의 Real Server
var VAR_NODE_GROUP_SETFAIL = "";							// real server 설정에  실패했습니다.
var VAR_NODE_SYNC_PROGRESSING = "";							// 동기화를 진행하시겠습니까?
var VAR_NODE_SYNC_COMPLETE_RESET = "";						// 동기화가 완료가 되었습니다. 설정을 다시 진행하시기 바랍니다.

// NOTICE
var VAR_NOTICE_VSLOAD_FAIL = "";							// 공지 Virtual Server 목록 로딩에 실패했습니다.
var VAR_NOTICE_SERVICE_LOAD_FAIL = "";						// 서비스 Virtual Sever 목록 로딩에 실패했습니다.
var VAR_NOTICE_WANT_CHANGE_VSSEL = "";						// 공지 Group 전환을 원하는 virtual server를 선택하세요.
var VAR_NOTICE_WANT_REVERT_VSSEL = "";						// 공지 Group 복구를 원하는 virtual server를 선택하세요.
var VAR_NOTICE_CHANGE_FAIL = "";							// 공지 전환에  실패했습니다.
var VAR_NOTICE_RESTORE_FAIL = "";							// 공지 복구에  실패했습니다.
var VAR_NOTICE_GROUPLOAD_FAIL = ""; 						// 공지 그룹 로딩에 실패했습니다.
var VAR_NOTICE_ALLGROUPLOAD_FAIL = "";						// 공지 그룹 전체로딩에 실패했습니다.
var VAR_NOTICE_VSSET_ARREADY = "";							// 공지 그룹으로 설정된 Virtual Server가 존재합니다.
var VAR_NOTICE_ARREADY_SETGROUP = "";						// 이미 설정된 공지 그룹입니다.
var VAR_NOTICE_GROUPSET_FAIL = "";							// 공지 그룹지정 설정에  실패했습니다.

//다이나믹 데시보드 dashboardHeader
var VAR_DASH_GROUP = "";									// 그룹
var VAR_DASH_GROUPA = "";									// 그룹 전체
var VAR_DASH_PREVIEW = "";									// 미리보기
var VAR_DASH_THEMAX	= "";									// 하나의 대시보드엔 최대
var VAR_DASH_THEWAAVA = "";									// 개의 위젯을 추가할 수 있습니다.
var VAR_DASH_ADDOPSEL = "";									// 추가 옵션탭의 기간을 선택하여 주십시오
var VAR_DASH_ADDOSSEL = "";									// 추가 옵션탭의 status를 선택하여 주십시오
var VAR_DASH_ADDOOSEL = "";									// 추가 옵션탭의 기준을 선택하여 주십시오
var VAR_DASH_GROSEL = "";									// 그룹을 선택하여 주십시오
var VAR_DASH_ADCSEL = "";									// ADC를 선택하여 주십시오
var VAR_DASH_VSESEL = "";									// Virtual Server를 선택하여 주십시오
var VAR_DASH_FLBGROUP = "";									// FLB Group을 선택하여 주십시오
var VAR_DASH_VSERSEL = "";									// Virtual Service를 선택하여 주십시오
var VAR_DASH_SAVAWNEXI = "";								// 저장 가능한 대시보드 위젯이 존재하지 않습니다.
var VAR_DASH_DEFBNSAVA = "";								// 기본으로 제공하는 대시보드는 새 이름으로 저장만 가능합니다.
var VAR_DASH_BORINPUT = "";									// 대시보드 이름을 입력하시기 바랍니다.
var VAR_DASH_BOADSEL = "";									// 삭제할 Dashboard를 선택해 주십시오
var VAR_DASH_ADCDNAVA = "";									// ADC 요약은 삭제할 수 없는 대시보드 입니다.
var VAR_DASH_ADCDELNAVA = "";								// ADC 모니터링은 삭제할 수 없는 대시보드 입니다.
var VAR_DASH_FAUDNAVA = "";									// 장비 모니터링은 삭제할 수 없는 대시보드 입니다.
var VAR_DASH_BOADEL = "";									// 대시보드를 삭제 하시겠습니까?
var VAR_DASH_BOADELSEL = "";								// 
var VAR_DASH_BOADSUC = "";									// 대시보드가 삭제 되었습니다.
var VAR_DASH_BOADSUCDEL = "";								//
var VAR_DASH_ALLGROUP = "";									// 전체 그룹
var VAR_DASH_ALRSBOA = "";									// 은 이미 저장된 대시보드 이름입니다.
var VAR_DASH_BOASSUC = "";									// 대시보드 저장이 완료되었습니다.
var VAR_DASH_BOARD = "";									//
var VAR_DASH_BOANSAVE = "";									// 새 이름으로 저장 하시겠습니까?
var VAR_DASH_BOANOTSAVE = "";								// 대시보드가 저장되지 않았습니다. 계속 진행하시겠습니까?

var VAR_DASH_ADCDISCONN	= "";								// ADC 끊김
var VAR_DASH_VSDISCONN = "";								// Virtual Server 끊김
var VAR_DASH_GMDISCONN = "";								// GROUP Member 끊김
var VAR_DASH_PMDISCONN = "";								// Pool Member 끊김
var VAR_DASH_ADCLINKDOWN = "";								// ADC Link Down
var VAR_DASH_USAGE	= "";									// 사용량

var VAR_DASH_NOTICE_TOTAL_NUMBER_OF_ALERTS						= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ALERTS						= "";
var VAR_DASH_NOTICE_TOTAL_MOST_RECENT_ALERTS					= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONFIGURATION_CHANGES		= "";
var VAR_DASH_NOTICE_TOTAL_MOST_RECENT_SETTINGS_CHANGES			= "";
var VAR_DASH_NOTICE_TOTAL_ADC_SUMMARY							= "";
var VAR_DASH_NOTICE_TOTAL_VS_SUMMARY							= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_SESSION			= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_DETAIL			= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_FLBGROUP			= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_THROUGHPUT					= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_CPU_USAGE				= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_MEMORY_USAGE			= "";
var VAR_DASH_NOTICE_TOTAL_TOTAL_ADC_TRAFFIC						= "";
var VAR_DASH_NOTICE_TOTAL_CHANGES_IN_VS_STATUS					= "";
var VAR_DASH_NOTICE_TOTAL_ADC_LIST								= "";
var VAR_DASH_NOTICE_TOTAL_VS_LIST								= "";
var VAR_DASH_NOTICE_TOTAL_TOP_10_VS_BY_THROUGHPUT				= "";
var VAR_DASH_NOTICE_TOTAL_TOP_10_ADC_BY_TRAFFIC					= "";
var VAR_DASH_NOTICE_TOTAL_ADC_MONITORING						= "";
var VAR_DASH_NOTICE_TOTAL_TRENDS_IN_SERVICE_RESPONSE_TIME		= "";
var VAR_DASH_NOTICE_TOTAL_ADC_STATUS_NOTIFICATION				= ""; // 선택한 ADC의 그룹 및 개별의 상태를 표현합니다.
var VAR_DASH_NOTICE_TOTAL_VS_STATUS_NOTIFICATION				= ""; // 선택한 서비스의 그룹 및 개별의 상태를 표현합니다.
var VAR_DASH_NOTICE_TOTAL_MEMBER_STATUS_NOTIFICATION			= ""; // 선택한 멤버의 그룹 및 개별의 상태를 표현합니다.

//common JS
var VAR_COMM_CONTENT = "";									// 내용
var VAR_COMM_ACTION = "";									// 조치
var VAR_COMM_ADCVSIPSTAT = "";								// adc virtual server ip 및 상태

//etc JS
var VAR_ETC_TASKRUN	= ""; 									// 다른 작업이 실행 중입니다. 작업 완료 후 다시 실행해 주시기 바랍니다.
var VAR_ETC_FIRST = "";										// 처음으로
var VAR_ETC_PRE = ""; 										// 이전페이지
var VAR_ETC_NEXT = ""; 										// 다음페이지
var VAR_ETC_LAST = ""; 										// 마지막으로
var VAR_ETC_LISTCOUNT = ""; 								// 개
var VAR_ETC_LISTOPEN = ""; 									// 목록 열기
var VAR_ETC_ALL = "";										// 전체