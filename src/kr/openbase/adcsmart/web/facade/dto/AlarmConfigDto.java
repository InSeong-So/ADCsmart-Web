package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmAction;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmConfig;
import kr.openbase.adcsmart.service.utility.OBDefine;

/**
 * 경보 관리 항목 - 각 ADC마다 설정하는 경보 action을 항목
 * 
 * @author lucky77th
 * 
 */

public class AlarmConfigDto {
	OBDtoADCObject object;

	Integer adcType; // F5, Alteon, PAS/PASK
	Integer configLevel; // object가 ADC일 때만 유효한 값이다.Global, Group, ADC(mine)

	private AlarmActionDto adcDisconnectAction; // ADC 연결 실패 svc : adcDisconnect
	private AlarmActionDto adcBootAction; // ADC (재) 부팅 svc : adcBooting
	private AlarmActionDto adcStandbyAction; // Active->Standby svc : adcActiveToStandby
	private AlarmActionDto adcActiveAction; // Standby->Active svc : adcStandbyToActive
	private AlarmActionDto virtualServerDownAction; // VS down svc : virtualServerDown
	private AlarmActionDto poolMemberDownAction; // pool member down svc : poolMemberDown
	private AlarmActionDto vrrpCollisionAction; // vrrp/vrid 충돌 장애
	private AlarmActionDto gatewayDownAction; // Gateway Down
	private AlarmActionDto linkDownAction; // interface down svc : interfaceDown

	private AlarmActionDto adcCpuAction; // F5 CPU 사용률 초과 svc : adcCpuLimit
	private Integer adcCpuValue; // F5 CPU 기준
	private AlarmActionDto adcMPAction; // Alteon MP CPU 사용률 초과 svc : adcMPLimit
	private Integer adcMPValue; // Alteon MP 기준
	private AlarmActionDto adcSPAction; // Alteon SP CPU 사용률 초과 svc : adcSPLimit
	private Integer adcSPValue; // Alteon MP 기준

	private AlarmActionDto adcMemAction; // Memory 사용율 초과 svc : adcMemLimit
	private Integer adcMemValue; // Memory 기준
	private AlarmActionDto connectionLimitHighAction; // connection 기준값 초과
	private Long adcConnHighValue; // connection High기준
	private AlarmActionDto connectionLimitLowAction; // connection 기준값 이하
	private Long adcConnLowValue; // connection Low기준
	private AlarmActionDto adcSslTransAction; // F5 ssl transaction svc : adcSslTransaction
	private Long adcSslTransValue; // F5 ssl 기준
	private AlarmActionDto filterSessionLimitHighAction; // alteon filter session 기준값 초과
	private Long filterSessionHighValue; // alteon filter session High 기준

	private AlarmActionDto adcUptimeAction; // uptime svc : adcUptime
	private Integer adcUptimeValue; // uptime 기준
	private AlarmActionDto adcPurchaseAction; // 보유기간 svc : adcPurchase
	private Integer adcPurchaseValue; // 보유기간 기준
	private AlarmActionDto adcSslcertAction; // F5 SSL인증서 svc : adcSslcert
	private Integer adcSslcertValue; // SSL인증서 기준

	private AlarmActionDto interfaceErrorAction; // 인터페이스 오류 svc : interfaceError
	private Integer interfaceErrorValue; // 인터페이스 기준
	private AlarmActionDto interfaceUsageLimitAction; // 인터페이스 사용률 svc : interfaceUsageLimit
	private Integer interfaceUsageValue; // 인터페이스 사용률 기준
	private AlarmActionDto interfaceDuplexChangeAction;// 인터페이스 Duplex svc : interfaceDuplexChange
	private AlarmActionDto interfaceSpeedChangeAction; // 인터페이스 Speed svc : interfaceSpeedChange
	private AlarmActionDto adcConfBackupFailureAction; // ADC 설정 백업 svc : adcConfBackupFailure

	// Alteon ADC 시스템정보
	private AlarmActionDto temperatureTooHighAction; // Alteon CPU 온도 svc : temperatureTooHigh
	private AlarmActionDto fanNotOperationalAction; // Alteon 팬 상태 svc : fanNotOperational
	private AlarmActionDto onlyOnePowerSupplyAction; // Alteon PowerSupply svc : onlyOnePowerSupply

	// F5 ADC 시스템 정보
	private AlarmActionDto adcConfSyncFailureAction; // ADC 설정 동기화 svc : adcConfSyncFailure
	private AlarmActionDto cpuTempTooHighAction; // F5 CPU 온도 svc : cpuTempTooHigh
	private AlarmActionDto cpuFanTooSlowAction; // F5 CPU 팬 동작 svc : cpuFanTooSlow
	private AlarmActionDto cpuFanBadAction; // F5 CPU 팬 상태 svc : cpuFanBad
	private AlarmActionDto chassisTempTooHighAction; // F5 샤시 온도 svc : chassisTempTooHigh
	private AlarmActionDto chassisFanBadAction; // F5 샤시 팬 상태 svc : chassisFanBad
	private AlarmActionDto chassisPowerSupplyBadAction;// F5 샤시 전원 상태 svc : chassisPowerSupplyBad
	private AlarmActionDto voltageTooHighAction; // F5 전원 svc : voltageTooHigh
	private AlarmActionDto chassisFanTooSlowAction; // F5 샤시 팬 동작 svc : chassisFanTooSlow
	private AlarmActionDto blockDDoSAction; // F5 DDOS svc : blockDDoS

	// Not Use
	private AlarmActionDto adcConnectAction;
	private AlarmActionDto virtualServerUpAction;
	private AlarmActionDto poolMemberUpAction;
	private AlarmActionDto linkUpAction;

	private AlarmActionDto responseTimeAction; // 응답시간 초과 svc : responseTime
	private Integer responseTimeValue; // 응답시간 기준
	private AlarmActionDto redundancyCheckAction; // 이중화 장애 svc : redundancyCheck

	public AlarmConfigDto() {
	}

	public AlarmConfigDto(boolean init) {
		this.adcDisconnectAction = new AlarmActionDto();
		this.adcBootAction = new AlarmActionDto();
		this.adcStandbyAction = new AlarmActionDto();
		this.adcActiveAction = new AlarmActionDto();
		this.virtualServerDownAction = new AlarmActionDto();
		this.poolMemberDownAction = new AlarmActionDto();
		this.vrrpCollisionAction = new AlarmActionDto();
		this.gatewayDownAction = new AlarmActionDto();
		this.linkDownAction = new AlarmActionDto();

		this.adcCpuAction = new AlarmActionDto();
		this.adcMPAction = new AlarmActionDto();
		this.adcSPAction = new AlarmActionDto();
		this.adcMemAction = new AlarmActionDto();
		this.connectionLimitHighAction = new AlarmActionDto();
		this.connectionLimitLowAction = new AlarmActionDto();
		this.adcSslTransAction = new AlarmActionDto();
		this.adcUptimeAction = new AlarmActionDto();
		this.adcPurchaseAction = new AlarmActionDto();
		this.adcSslcertAction = new AlarmActionDto();
		this.filterSessionLimitHighAction = new AlarmActionDto();

		this.interfaceErrorAction = new AlarmActionDto();
		this.interfaceUsageLimitAction = new AlarmActionDto();
		this.interfaceDuplexChangeAction = new AlarmActionDto();
		this.interfaceSpeedChangeAction = new AlarmActionDto();
		this.adcConfBackupFailureAction = new AlarmActionDto();
		this.adcConfSyncFailureAction = new AlarmActionDto();

		this.temperatureTooHighAction = new AlarmActionDto();
		this.fanNotOperationalAction = new AlarmActionDto();
		this.onlyOnePowerSupplyAction = new AlarmActionDto();

		this.cpuTempTooHighAction = new AlarmActionDto();
		this.cpuFanTooSlowAction = new AlarmActionDto();
		this.cpuFanBadAction = new AlarmActionDto();
		this.chassisTempTooHighAction = new AlarmActionDto();
		this.chassisFanBadAction = new AlarmActionDto();
		this.chassisPowerSupplyBadAction = new AlarmActionDto();
		this.voltageTooHighAction = new AlarmActionDto();
		this.chassisFanTooSlowAction = new AlarmActionDto();
		this.blockDDoSAction = new AlarmActionDto();

		this.adcConnectAction = new AlarmActionDto();
		this.virtualServerUpAction = new AlarmActionDto();
		this.poolMemberUpAction = new AlarmActionDto();
		this.linkUpAction = new AlarmActionDto();

		this.responseTimeAction = new AlarmActionDto();
		this.redundancyCheckAction = new AlarmActionDto();
	}

	public static OBDtoAlarmConfig toOBDtoAlarmConfig(AlarmConfigDto alarmConfigs) {
		OBDtoAlarmConfig alarmSvc = new OBDtoAlarmConfig();
		alarmSvc.setConfigLevel(alarmConfigs.getConfigLevel());
		alarmSvc.setAdcType(alarmConfigs.getAdcType());
		alarmSvc.setObject(alarmConfigs.getObject());

		if (alarmConfigs.getObject().getCategory() == 2 && alarmConfigs.configLevel != 2) {
		} else {
			OBDtoAlarmAction a = new OBDtoAlarmAction();
			a.setEnable(alarmConfigs.getAdcDisconnectAction().getEnable());
			a.setIntervalUnit(alarmConfigs.getAdcDisconnectAction().getIntervalUnit());
			a.setIntervalValue(alarmConfigs.getAdcDisconnectAction().getIntervalValue());
			a.setSyslog(alarmConfigs.getAdcDisconnectAction().getSyslog());
			a.setEmail(alarmConfigs.getAdcDisconnectAction().getEmail());
			a.setSms(alarmConfigs.getAdcDisconnectAction().getSms());
			a.setSnmptrap(alarmConfigs.getAdcDisconnectAction().getSnmptrap());
			alarmSvc.setAdcDisconnect(a);

			OBDtoAlarmAction b = new OBDtoAlarmAction();
			b.setEnable(alarmConfigs.getAdcBootAction().getEnable());
			b.setIntervalUnit(alarmConfigs.getAdcBootAction().getIntervalUnit());
			b.setIntervalValue(alarmConfigs.getAdcBootAction().getIntervalValue());
			b.setSyslog(alarmConfigs.getAdcBootAction().getSyslog());
			b.setEmail(alarmConfigs.getAdcBootAction().getEmail());
			b.setSms(alarmConfigs.getAdcBootAction().getSms());
			b.setSnmptrap(alarmConfigs.getAdcBootAction().getSnmptrap());
			alarmSvc.setAdcBooting(b);

			OBDtoAlarmAction c = new OBDtoAlarmAction();
			c.setEnable(alarmConfigs.getAdcStandbyAction().getEnable());
			c.setIntervalUnit(alarmConfigs.getAdcStandbyAction().getIntervalUnit());
			c.setIntervalValue(alarmConfigs.getAdcStandbyAction().getIntervalValue());
			c.setSyslog(alarmConfigs.getAdcStandbyAction().getSyslog());
			c.setEmail(alarmConfigs.getAdcStandbyAction().getEmail());
			c.setSms(alarmConfigs.getAdcStandbyAction().getSms());
			c.setSnmptrap(alarmConfigs.getAdcStandbyAction().getSnmptrap());
			alarmSvc.setAdcActiveToStandby(c);

			OBDtoAlarmAction d = new OBDtoAlarmAction();
			d.setEnable(alarmConfigs.getAdcActiveAction().getEnable());
			d.setIntervalUnit(alarmConfigs.getAdcActiveAction().getIntervalUnit());
			d.setIntervalValue(alarmConfigs.getAdcActiveAction().getIntervalValue());
			d.setSyslog(alarmConfigs.getAdcActiveAction().getSyslog());
			d.setEmail(alarmConfigs.getAdcActiveAction().getEmail());
			d.setSms(alarmConfigs.getAdcActiveAction().getSms());
			d.setSnmptrap(alarmConfigs.getAdcActiveAction().getSnmptrap());
			alarmSvc.setAdcStandbyToActive(d);

			OBDtoAlarmAction e = new OBDtoAlarmAction();
			e.setEnable(alarmConfigs.getVirtualServerDownAction().getEnable());
			e.setIntervalUnit(alarmConfigs.getVirtualServerDownAction().getIntervalUnit());
			e.setIntervalValue(alarmConfigs.getVirtualServerDownAction().getIntervalValue());
			e.setSyslog(alarmConfigs.getVirtualServerDownAction().getSyslog());
			e.setEmail(alarmConfigs.getVirtualServerDownAction().getEmail());
			e.setSms(alarmConfigs.getVirtualServerDownAction().getSms());
			e.setSnmptrap(alarmConfigs.getVirtualServerDownAction().getSnmptrap());
			alarmSvc.setVirtualServerDown(e);

			OBDtoAlarmAction f = new OBDtoAlarmAction();
			f.setEnable(alarmConfigs.getPoolMemberDownAction().getEnable());
			f.setIntervalUnit(alarmConfigs.getPoolMemberDownAction().getIntervalUnit());
			f.setIntervalValue(alarmConfigs.getPoolMemberDownAction().getIntervalValue());
			f.setSyslog(alarmConfigs.getPoolMemberDownAction().getSyslog());
			f.setEmail(alarmConfigs.getPoolMemberDownAction().getEmail());
			f.setSms(alarmConfigs.getPoolMemberDownAction().getSms());
			f.setSnmptrap(alarmConfigs.getPoolMemberDownAction().getSnmptrap());
			alarmSvc.setPoolMemberDown(f);

			OBDtoAlarmAction g = new OBDtoAlarmAction();
			g.setEnable(alarmConfigs.getLinkDownAction().getEnable());
			g.setIntervalUnit(alarmConfigs.getLinkDownAction().getIntervalUnit());
			g.setIntervalValue(alarmConfigs.getLinkDownAction().getIntervalValue());
			g.setSyslog(alarmConfigs.getLinkDownAction().getSyslog());
			g.setEmail(alarmConfigs.getLinkDownAction().getEmail());
			g.setSms(alarmConfigs.getLinkDownAction().getSms());
			g.setSnmptrap(alarmConfigs.getLinkDownAction().getSnmptrap());
			alarmSvc.setInterfaceDown(g);

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_ALTEON || alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				OBDtoAlarmAction rta = new OBDtoAlarmAction();
				rta.setEnable(alarmConfigs.getResponseTimeAction().getEnable());
				rta.setIntervalUnit(alarmConfigs.getResponseTimeAction().getIntervalUnit());
				rta.setIntervalValue(alarmConfigs.getResponseTimeAction().getIntervalValue());
				rta.setSyslog(alarmConfigs.getResponseTimeAction().getSyslog());
				rta.setEmail(alarmConfigs.getResponseTimeAction().getEmail());
				rta.setSms(alarmConfigs.getResponseTimeAction().getSms());
				rta.setSnmptrap(alarmConfigs.getResponseTimeAction().getSnmptrap());
				alarmSvc.setResponseTime(rta);
				alarmSvc.getResponseTime().setThreshold(alarmConfigs.getResponseTimeValue().longValue());

				OBDtoAlarmAction rdc = new OBDtoAlarmAction();
				rdc.setEnable(alarmConfigs.getRedundancyCheckAction().getEnable());
				rdc.setIntervalUnit(alarmConfigs.getRedundancyCheckAction().getIntervalUnit());
				rdc.setIntervalValue(alarmConfigs.getRedundancyCheckAction().getIntervalValue());
				rdc.setSyslog(alarmConfigs.getRedundancyCheckAction().getSyslog());
				rdc.setEmail(alarmConfigs.getRedundancyCheckAction().getEmail());
				rdc.setSms(alarmConfigs.getRedundancyCheckAction().getSms());
				rdc.setSnmptrap(alarmConfigs.getRedundancyCheckAction().getSnmptrap());
				alarmSvc.setRedundancyCheck(rdc);
			}

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				OBDtoAlarmAction h = new OBDtoAlarmAction();
				h.setEnable(alarmConfigs.getAdcCpuAction().getEnable());
				h.setSyslog(alarmConfigs.getAdcCpuAction().getSyslog());
				h.setIntervalUnit(alarmConfigs.getAdcCpuAction().getIntervalUnit());
				h.setIntervalValue(alarmConfigs.getAdcCpuAction().getIntervalValue());
				h.setEmail(alarmConfigs.getAdcCpuAction().getEmail());
				h.setSms(alarmConfigs.getAdcCpuAction().getSms());
				h.setSnmptrap(alarmConfigs.getAdcCpuAction().getSnmptrap());
				alarmSvc.setAdcCpuLimit(h);

				OBDtoAlarmAction i = new OBDtoAlarmAction();
				i.setEnable(0);
				i.setSyslog(0);
				i.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
				i.setIntervalValue(0);
				i.setEmail(0);
				i.setSms(0);
				i.setSnmptrap(0);
				alarmSvc.setAdcMPLimit(i);
				alarmSvc.setAdcSPLimit(i);
				alarmSvc.setVrrpCollision(i);
			} else {
				OBDtoAlarmAction h = new OBDtoAlarmAction();
				h.setEnable(0);
				h.setSyslog(0);
				h.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
				h.setIntervalValue(0);
				h.setEmail(0);
				h.setSms(0);
				h.setSnmptrap(0);
				alarmSvc.setAdcCpuLimit(h);

				OBDtoAlarmAction i = new OBDtoAlarmAction();
				i.setEnable(alarmConfigs.getAdcMPAction().getEnable());
				i.setIntervalUnit(alarmConfigs.getAdcMPAction().getIntervalUnit());
				i.setIntervalValue(alarmConfigs.getAdcMPAction().getIntervalValue());
				i.setSyslog(alarmConfigs.getAdcMPAction().getSyslog());
				i.setEmail(alarmConfigs.getAdcMPAction().getEmail());
				i.setSms(alarmConfigs.getAdcMPAction().getSms());
				i.setSnmptrap(alarmConfigs.getAdcMPAction().getSnmptrap());
				alarmSvc.setAdcMPLimit(i);

				OBDtoAlarmAction j = new OBDtoAlarmAction();
				j.setEnable(alarmConfigs.getAdcSPAction().getEnable());
				j.setIntervalUnit(alarmConfigs.getAdcSPAction().getIntervalUnit());
				j.setIntervalValue(alarmConfigs.getAdcSPAction().getIntervalValue());
				j.setSyslog(alarmConfigs.getAdcSPAction().getSyslog());
				j.setEmail(alarmConfigs.getAdcSPAction().getEmail());
				j.setSms(alarmConfigs.getAdcSPAction().getSms());
				j.setSnmptrap(alarmConfigs.getAdcSPAction().getSnmptrap());
				alarmSvc.setAdcSPLimit(j);

				if (alarmConfigs.adcType == OBDefine.ADC_TYPE_ALTEON) {
					OBDtoAlarmAction vrrp = new OBDtoAlarmAction();
					vrrp.setEnable(alarmConfigs.getVrrpCollisionAction().getEnable());
					vrrp.setIntervalUnit(alarmConfigs.getVrrpCollisionAction().getIntervalUnit());
					vrrp.setIntervalValue(alarmConfigs.getVrrpCollisionAction().getIntervalValue());
					vrrp.setSyslog(alarmConfigs.getVrrpCollisionAction().getSyslog());
					vrrp.setEmail(alarmConfigs.getVrrpCollisionAction().getEmail());
					vrrp.setSms(alarmConfigs.getVrrpCollisionAction().getSms());
					vrrp.setSnmptrap(alarmConfigs.getVrrpCollisionAction().getSnmptrap());
					alarmSvc.setVrrpCollision(vrrp);

					OBDtoAlarmAction gateWay = new OBDtoAlarmAction();
					gateWay.setEnable(alarmConfigs.getGatewayDownAction().getEnable());
					gateWay.setIntervalUnit(alarmConfigs.getGatewayDownAction().getIntervalUnit());
					gateWay.setIntervalValue(alarmConfigs.getGatewayDownAction().getIntervalValue());
					gateWay.setSyslog(alarmConfigs.getGatewayDownAction().getSyslog());
					gateWay.setEmail(alarmConfigs.getGatewayDownAction().getEmail());
					gateWay.setSms(alarmConfigs.getGatewayDownAction().getSms());
					gateWay.setSnmptrap(alarmConfigs.getGatewayDownAction().getSnmptrap());
					alarmSvc.setGatewayFailDown(gateWay);

					OBDtoAlarmAction filterSessionHigh = new OBDtoAlarmAction();
					filterSessionHigh.setEnable(alarmConfigs.getFilterSessionLimitHighAction().getEnable());
					filterSessionHigh.setIntervalUnit(alarmConfigs.getFilterSessionLimitHighAction().getIntervalUnit());
					filterSessionHigh
							.setIntervalValue(alarmConfigs.getFilterSessionLimitHighAction().getIntervalValue());
					filterSessionHigh.setSyslog(alarmConfigs.getFilterSessionLimitHighAction().getSyslog());
					filterSessionHigh.setEmail(alarmConfigs.getFilterSessionLimitHighAction().getEmail());
					filterSessionHigh.setSms(alarmConfigs.getFilterSessionLimitHighAction().getSms());
					filterSessionHigh.setSnmptrap(alarmConfigs.getFilterSessionLimitHighAction().getSnmptrap());
					alarmSvc.setFilterSessionLimitHigh(filterSessionHigh);
				}
			}

			OBDtoAlarmAction k = new OBDtoAlarmAction();
			k.setEnable(alarmConfigs.getAdcMemAction().getEnable());
			k.setIntervalUnit(alarmConfigs.getAdcMemAction().getIntervalUnit());
			k.setIntervalValue(alarmConfigs.getAdcMemAction().getIntervalValue());
			k.setSyslog(alarmConfigs.getAdcMemAction().getSyslog());
			k.setEmail(alarmConfigs.getAdcMemAction().getEmail());
			k.setSms(alarmConfigs.getAdcMemAction().getSms());
			k.setSnmptrap(alarmConfigs.getAdcMemAction().getSnmptrap());
			alarmSvc.setAdcMemLimit(k);

			OBDtoAlarmAction connHigh = new OBDtoAlarmAction();
			connHigh.setEnable(alarmConfigs.getConnectionLimitHighAction().getEnable());
			connHigh.setIntervalUnit(alarmConfigs.getConnectionLimitHighAction().getIntervalUnit());
			connHigh.setIntervalValue(alarmConfigs.getConnectionLimitHighAction().getIntervalValue());
			connHigh.setSyslog(alarmConfigs.getConnectionLimitHighAction().getSyslog());
			connHigh.setEmail(alarmConfigs.getConnectionLimitHighAction().getEmail());
			connHigh.setSms(alarmConfigs.getConnectionLimitHighAction().getSms());
			connHigh.setSnmptrap(alarmConfigs.getConnectionLimitHighAction().getSnmptrap());
			alarmSvc.setConnectionLimitHigh(connHigh);

			OBDtoAlarmAction connLow = new OBDtoAlarmAction();
			connLow.setEnable(alarmConfigs.getConnectionLimitLowAction().getEnable());
			connLow.setIntervalUnit(alarmConfigs.getConnectionLimitLowAction().getIntervalUnit());
			connLow.setIntervalValue(alarmConfigs.getConnectionLimitLowAction().getIntervalValue());
			connLow.setSyslog(alarmConfigs.getConnectionLimitLowAction().getSyslog());
			connLow.setEmail(alarmConfigs.getConnectionLimitLowAction().getEmail());
			connLow.setSms(alarmConfigs.getConnectionLimitLowAction().getSms());
			connLow.setSnmptrap(alarmConfigs.getConnectionLimitLowAction().getSnmptrap());
			alarmSvc.setConnectionLimitLow(connLow);

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				OBDtoAlarmAction m = new OBDtoAlarmAction();
				m.setEnable(alarmConfigs.getAdcSslTransAction().getEnable());
				m.setIntervalUnit(alarmConfigs.getAdcSslTransAction().getIntervalUnit());
				m.setIntervalValue(alarmConfigs.getAdcSslTransAction().getIntervalValue());
				m.setSyslog(alarmConfigs.getAdcSslTransAction().getSyslog());
				m.setEmail(alarmConfigs.getAdcSslTransAction().getEmail());
				m.setSms(alarmConfigs.getAdcSslTransAction().getSms());
				m.setSnmptrap(alarmConfigs.getAdcSslTransAction().getSnmptrap());
				alarmSvc.setAdcSslTransaction(m);
			} else {
				OBDtoAlarmAction m = new OBDtoAlarmAction();
				m.setEnable(0);
				m.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
				m.setIntervalValue(0);
				m.setSyslog(0);
				m.setEmail(0);
				m.setSms(0);
				m.setSnmptrap(0);
				alarmSvc.setAdcSslTransaction(m);
			}

			OBDtoAlarmAction n = new OBDtoAlarmAction();
			n.setEnable(alarmConfigs.getAdcUptimeAction().getEnable());
			n.setIntervalUnit(alarmConfigs.getAdcUptimeAction().getIntervalUnit());
			n.setIntervalValue(alarmConfigs.getAdcUptimeAction().getIntervalValue());
			n.setSyslog(alarmConfigs.getAdcUptimeAction().getSyslog());
			n.setEmail(alarmConfigs.getAdcUptimeAction().getEmail());
			n.setSms(alarmConfigs.getAdcUptimeAction().getSms());
			n.setSnmptrap(alarmConfigs.getAdcUptimeAction().getSnmptrap());
			alarmSvc.setAdcUptime(n);

			OBDtoAlarmAction o = new OBDtoAlarmAction();
			o.setEnable(alarmConfigs.getAdcPurchaseAction().getEnable());
			o.setIntervalUnit(alarmConfigs.getAdcPurchaseAction().getIntervalUnit());
			o.setIntervalValue(alarmConfigs.getAdcPurchaseAction().getIntervalValue());
			o.setSyslog(alarmConfigs.getAdcPurchaseAction().getSyslog());
			o.setEmail(alarmConfigs.getAdcPurchaseAction().getEmail());
			o.setSms(alarmConfigs.getAdcPurchaseAction().getSms());
			o.setSnmptrap(alarmConfigs.getAdcPurchaseAction().getSnmptrap());
			alarmSvc.setAdcPurchase(o);

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				OBDtoAlarmAction p = new OBDtoAlarmAction();
				p.setEnable(alarmConfigs.getAdcSslcertAction().getEnable());
				p.setIntervalUnit(alarmConfigs.getAdcSslcertAction().getIntervalUnit());
				p.setIntervalValue(alarmConfigs.getAdcSslcertAction().getIntervalValue());
				p.setSyslog(alarmConfigs.getAdcSslcertAction().getSyslog());
				p.setEmail(alarmConfigs.getAdcSslcertAction().getEmail());
				p.setSms(alarmConfigs.getAdcSslcertAction().getSms());
				p.setSnmptrap(alarmConfigs.getAdcSslcertAction().getSnmptrap());
				alarmSvc.setAdcSslcert(p);
			} else {
				OBDtoAlarmAction p = new OBDtoAlarmAction();
				p.setEnable(0);
				p.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
				p.setIntervalValue(0);
				p.setSyslog(0);
				p.setEmail(0);
				p.setSms(0);
				p.setSnmptrap(0);
				alarmSvc.setAdcSslcert(p);
			}

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_ALTEON || alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				OBDtoAlarmAction q = new OBDtoAlarmAction();
				q.setEnable(alarmConfigs.getInterfaceErrorAction().getEnable());
				q.setIntervalUnit(alarmConfigs.getInterfaceErrorAction().getIntervalUnit());
				q.setIntervalValue(alarmConfigs.getInterfaceErrorAction().getIntervalValue());
				q.setSyslog(alarmConfigs.getInterfaceErrorAction().getSyslog());
				q.setEmail(alarmConfigs.getInterfaceErrorAction().getEmail());
				q.setSms(alarmConfigs.getInterfaceErrorAction().getSms());
				q.setSnmptrap(alarmConfigs.getInterfaceErrorAction().getSnmptrap());
				alarmSvc.setInterfaceError(q);

				OBDtoAlarmAction r = new OBDtoAlarmAction();
				r.setEnable(alarmConfigs.getInterfaceUsageLimitAction().getEnable());
				r.setIntervalUnit(alarmConfigs.getInterfaceUsageLimitAction().getIntervalUnit());
				r.setIntervalValue(alarmConfigs.getInterfaceUsageLimitAction().getIntervalValue());
				r.setSyslog(alarmConfigs.getInterfaceUsageLimitAction().getSyslog());
				r.setEmail(alarmConfigs.getInterfaceUsageLimitAction().getEmail());
				r.setSms(alarmConfigs.getInterfaceUsageLimitAction().getSms());
				r.setSnmptrap(alarmConfigs.getInterfaceUsageLimitAction().getSnmptrap());
				alarmSvc.setInterfaceUsageLimit(r);

				OBDtoAlarmAction s = new OBDtoAlarmAction();
				s.setEnable(alarmConfigs.getInterfaceDuplexChangeAction().getEnable());
				s.setIntervalUnit(alarmConfigs.getInterfaceDuplexChangeAction().getIntervalUnit());
				s.setIntervalValue(alarmConfigs.getInterfaceDuplexChangeAction().getIntervalValue());
				s.setSyslog(alarmConfigs.getInterfaceDuplexChangeAction().getSyslog());
				s.setEmail(alarmConfigs.getInterfaceDuplexChangeAction().getEmail());
				s.setSms(alarmConfigs.getInterfaceDuplexChangeAction().getSms());
				s.setSnmptrap(alarmConfigs.getInterfaceDuplexChangeAction().getSnmptrap());
				alarmSvc.setInterfaceDuplexChange(s);

				OBDtoAlarmAction t = new OBDtoAlarmAction();
				t.setEnable(alarmConfigs.getInterfaceSpeedChangeAction().getEnable());
				t.setIntervalUnit(alarmConfigs.getInterfaceSpeedChangeAction().getIntervalUnit());
				t.setIntervalValue(alarmConfigs.getInterfaceSpeedChangeAction().getIntervalValue());
				t.setSyslog(alarmConfigs.getInterfaceSpeedChangeAction().getSyslog());
				t.setEmail(alarmConfigs.getInterfaceSpeedChangeAction().getEmail());
				t.setSms(alarmConfigs.getInterfaceSpeedChangeAction().getSms());
				t.setSnmptrap(alarmConfigs.getInterfaceSpeedChangeAction().getSnmptrap());
				alarmSvc.setInterfaceSpeedChange(t);

				OBDtoAlarmAction u = new OBDtoAlarmAction();
				u.setEnable(alarmConfigs.getAdcConfBackupFailureAction().getEnable());
				u.setIntervalUnit(alarmConfigs.getAdcConfBackupFailureAction().getIntervalUnit());
				u.setIntervalValue(alarmConfigs.getAdcConfBackupFailureAction().getIntervalValue());
				u.setSyslog(alarmConfigs.getAdcConfBackupFailureAction().getSyslog());
				u.setEmail(alarmConfigs.getAdcConfBackupFailureAction().getEmail());
				u.setSms(alarmConfigs.getAdcConfBackupFailureAction().getSms());
				u.setSnmptrap(alarmConfigs.getAdcConfBackupFailureAction().getSnmptrap());
				alarmSvc.setAdcConfBackupFailure(u);

				if (alarmConfigs.adcType == OBDefine.ADC_TYPE_ALTEON) {
					OBDtoAlarmAction w = new OBDtoAlarmAction();
					w.setEnable(alarmConfigs.getTemperatureTooHighAction().getEnable());
					w.setIntervalUnit(alarmConfigs.getTemperatureTooHighAction().getIntervalUnit());
					w.setIntervalValue(alarmConfigs.getTemperatureTooHighAction().getIntervalValue());
					w.setSyslog(alarmConfigs.getTemperatureTooHighAction().getSyslog());
					w.setEmail(alarmConfigs.getTemperatureTooHighAction().getEmail());
					w.setSms(alarmConfigs.getTemperatureTooHighAction().getSms());
					w.setSnmptrap(alarmConfigs.getTemperatureTooHighAction().getSnmptrap());
					alarmSvc.setTemperatureTooHigh(w);

					OBDtoAlarmAction x = new OBDtoAlarmAction();
					x.setEnable(alarmConfigs.getFanNotOperationalAction().getEnable());
					x.setIntervalUnit(alarmConfigs.getFanNotOperationalAction().getIntervalUnit());
					x.setIntervalValue(alarmConfigs.getFanNotOperationalAction().getIntervalValue());
					x.setSyslog(alarmConfigs.getFanNotOperationalAction().getSyslog());
					x.setEmail(alarmConfigs.getFanNotOperationalAction().getEmail());
					x.setSms(alarmConfigs.getFanNotOperationalAction().getSms());
					x.setSnmptrap(alarmConfigs.getFanNotOperationalAction().getSnmptrap());
					alarmSvc.setFanNotOperational(x);

					OBDtoAlarmAction y = new OBDtoAlarmAction();
					y.setEnable(alarmConfigs.getOnlyOnePowerSupplyAction().getEnable());
					y.setIntervalUnit(alarmConfigs.getOnlyOnePowerSupplyAction().getIntervalUnit());
					y.setIntervalValue(alarmConfigs.getOnlyOnePowerSupplyAction().getIntervalValue());
					y.setSyslog(alarmConfigs.getOnlyOnePowerSupplyAction().getSyslog());
					y.setEmail(alarmConfigs.getOnlyOnePowerSupplyAction().getEmail());
					y.setSms(alarmConfigs.getOnlyOnePowerSupplyAction().getSms());
					y.setSnmptrap(alarmConfigs.getOnlyOnePowerSupplyAction().getSnmptrap());
					alarmSvc.setOnlyOnePowerSupply(y);
				} else {
					OBDtoAlarmAction w = new OBDtoAlarmAction();
					w.setEnable(0);
					w.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
					w.setIntervalValue(0);
					w.setSyslog(0);
					w.setEmail(0);
					w.setSms(0);
					w.setSnmptrap(0);
					alarmSvc.setTemperatureTooHigh(w);
					alarmSvc.setFanNotOperational(w);
					alarmSvc.setOnlyOnePowerSupply(w);
				}

				if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
					OBDtoAlarmAction v = new OBDtoAlarmAction();
					v.setEnable(alarmConfigs.getAdcConfSyncFailureAction().getEnable());
					v.setIntervalUnit(alarmConfigs.getAdcConfSyncFailureAction().getIntervalUnit());
					v.setIntervalValue(alarmConfigs.getAdcConfSyncFailureAction().getIntervalValue());
					v.setSyslog(alarmConfigs.getAdcConfSyncFailureAction().getSyslog());
					v.setEmail(alarmConfigs.getAdcConfSyncFailureAction().getEmail());
					v.setSms(alarmConfigs.getAdcConfSyncFailureAction().getSms());
					v.setSnmptrap(alarmConfigs.getAdcConfSyncFailureAction().getSnmptrap());
					alarmSvc.setAdcConfSyncFailure(v);

					OBDtoAlarmAction z = new OBDtoAlarmAction();
					z.setEnable(alarmConfigs.getCpuTempTooHighAction().getEnable());
					z.setIntervalUnit(alarmConfigs.getCpuTempTooHighAction().getIntervalUnit());
					z.setIntervalValue(alarmConfigs.getCpuTempTooHighAction().getIntervalValue());
					z.setSyslog(alarmConfigs.getCpuTempTooHighAction().getSyslog());
					z.setEmail(alarmConfigs.getCpuTempTooHighAction().getEmail());
					z.setSms(alarmConfigs.getCpuTempTooHighAction().getSms());
					z.setSnmptrap(alarmConfigs.getCpuTempTooHighAction().getSnmptrap());
					alarmSvc.setCpuTempTooHigh(z);

					OBDtoAlarmAction aa = new OBDtoAlarmAction();
					aa.setEnable(alarmConfigs.getCpuFanTooSlowAction().getEnable());
					aa.setIntervalUnit(alarmConfigs.getCpuFanTooSlowAction().getIntervalUnit());
					aa.setIntervalValue(alarmConfigs.getCpuFanTooSlowAction().getIntervalValue());
					aa.setSyslog(alarmConfigs.getCpuFanTooSlowAction().getSyslog());
					aa.setEmail(alarmConfigs.getCpuFanTooSlowAction().getEmail());
					aa.setSms(alarmConfigs.getCpuFanTooSlowAction().getSms());
					aa.setSnmptrap(alarmConfigs.getCpuFanTooSlowAction().getSnmptrap());
					alarmSvc.setCpuFanTooSlow(aa);

					OBDtoAlarmAction bb = new OBDtoAlarmAction();
					bb.setEnable(alarmConfigs.getCpuFanBadAction().getEnable());
					bb.setIntervalUnit(alarmConfigs.getCpuFanBadAction().getIntervalUnit());
					bb.setIntervalValue(alarmConfigs.getCpuFanBadAction().getIntervalValue());
					bb.setSyslog(alarmConfigs.getCpuFanBadAction().getSyslog());
					bb.setEmail(alarmConfigs.getCpuFanBadAction().getEmail());
					bb.setSms(alarmConfigs.getCpuFanBadAction().getSms());
					bb.setSnmptrap(alarmConfigs.getCpuFanBadAction().getSnmptrap());
					alarmSvc.setCpuFanBad(bb);

					OBDtoAlarmAction cc = new OBDtoAlarmAction();
					cc.setEnable(alarmConfigs.getChassisTempTooHighAction().getEnable());
					cc.setIntervalUnit(alarmConfigs.getChassisTempTooHighAction().getIntervalUnit());
					cc.setIntervalValue(alarmConfigs.getChassisTempTooHighAction().getIntervalValue());
					cc.setSyslog(alarmConfigs.getChassisTempTooHighAction().getSyslog());
					cc.setEmail(alarmConfigs.getChassisTempTooHighAction().getEmail());
					cc.setSms(alarmConfigs.getChassisTempTooHighAction().getSms());
					cc.setSnmptrap(alarmConfigs.getChassisTempTooHighAction().getSnmptrap());
					alarmSvc.setChassisTempTooHigh(cc);

					OBDtoAlarmAction dd = new OBDtoAlarmAction();
					dd.setEnable(alarmConfigs.getChassisFanBadAction().getEnable());
					dd.setIntervalUnit(alarmConfigs.getChassisFanBadAction().getIntervalUnit());
					dd.setIntervalValue(alarmConfigs.getChassisFanBadAction().getIntervalValue());
					dd.setSyslog(alarmConfigs.getChassisFanBadAction().getSyslog());
					dd.setEmail(alarmConfigs.getChassisFanBadAction().getEmail());
					dd.setSms(alarmConfigs.getChassisFanBadAction().getSms());
					dd.setSnmptrap(alarmConfigs.getChassisFanBadAction().getSnmptrap());
					alarmSvc.setChassisFanBad(dd);

					OBDtoAlarmAction ee = new OBDtoAlarmAction();
					ee.setEnable(alarmConfigs.getChassisPowerSupplyBadAction().getEnable());
					ee.setIntervalUnit(alarmConfigs.getChassisPowerSupplyBadAction().getIntervalUnit());
					ee.setIntervalValue(alarmConfigs.getChassisPowerSupplyBadAction().getIntervalValue());
					ee.setSyslog(alarmConfigs.getChassisPowerSupplyBadAction().getSyslog());
					ee.setEmail(alarmConfigs.getChassisPowerSupplyBadAction().getEmail());
					ee.setSms(alarmConfigs.getChassisPowerSupplyBadAction().getSms());
					ee.setSnmptrap(alarmConfigs.getChassisPowerSupplyBadAction().getSnmptrap());
					alarmSvc.setChassisPowerSupplyBad(ee);

					OBDtoAlarmAction ff = new OBDtoAlarmAction();
					ff.setEnable(alarmConfigs.getVoltageTooHighAction().getEnable());
					ff.setIntervalUnit(alarmConfigs.getVoltageTooHighAction().getIntervalUnit());
					ff.setIntervalValue(alarmConfigs.getVoltageTooHighAction().getIntervalValue());
					ff.setSyslog(alarmConfigs.getVoltageTooHighAction().getSyslog());
					ff.setEmail(alarmConfigs.getVoltageTooHighAction().getEmail());
					ff.setSms(alarmConfigs.getVoltageTooHighAction().getSms());
					ff.setSnmptrap(alarmConfigs.getVoltageTooHighAction().getSnmptrap());
					alarmSvc.setVoltageTooHigh(ff);

					OBDtoAlarmAction gg = new OBDtoAlarmAction();
					gg.setEnable(alarmConfigs.getChassisFanTooSlowAction().getEnable());
					gg.setIntervalUnit(alarmConfigs.getChassisFanTooSlowAction().getIntervalUnit());
					gg.setIntervalValue(alarmConfigs.getChassisFanTooSlowAction().getIntervalValue());
					gg.setSyslog(alarmConfigs.getChassisFanTooSlowAction().getSyslog());
					gg.setEmail(alarmConfigs.getChassisFanTooSlowAction().getEmail());
					gg.setSms(alarmConfigs.getChassisFanTooSlowAction().getSms());
					gg.setSnmptrap(alarmConfigs.getChassisFanTooSlowAction().getSnmptrap());
					alarmSvc.setChassisFanTooSlow(gg);

					OBDtoAlarmAction jj = new OBDtoAlarmAction();
					jj.setEnable(alarmConfigs.getBlockDDoSAction().getEnable());
					jj.setIntervalUnit(alarmConfigs.getBlockDDoSAction().getIntervalUnit());
					jj.setIntervalValue(alarmConfigs.getBlockDDoSAction().getIntervalValue());
					jj.setSyslog(alarmConfigs.getBlockDDoSAction().getSyslog());
					jj.setEmail(alarmConfigs.getBlockDDoSAction().getEmail());
					jj.setSms(alarmConfigs.getBlockDDoSAction().getSms());
					jj.setSnmptrap(alarmConfigs.getBlockDDoSAction().getSnmptrap());
					alarmSvc.setBlockDDoS(jj);
				} else {
					OBDtoAlarmAction v = new OBDtoAlarmAction();
					v.setEnable(0);
					v.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
					v.setIntervalValue(0);
					v.setSyslog(0);
					v.setEmail(0);
					v.setSms(0);
					v.setSnmptrap(0);
					alarmSvc.setAdcConfSyncFailure(v);
					alarmSvc.setCpuTempTooHigh(v);
					alarmSvc.setCpuFanTooSlow(v);
					alarmSvc.setCpuFanBad(v);
					alarmSvc.setChassisTempTooHigh(v);
					alarmSvc.setChassisFanBad(v);
					alarmSvc.setChassisPowerSupplyBad(v);
					alarmSvc.setVoltageTooHigh(v);
					alarmSvc.setChassisFanTooSlow(v);
					alarmSvc.setBlockDDoS(v);
				}
			} else {
				OBDtoAlarmAction q = new OBDtoAlarmAction();
				q.setEnable(0);
				q.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
				q.setIntervalValue(0);
				q.setSyslog(0);
				q.setEmail(0);
				q.setSms(0);
				q.setSnmptrap(0);
				alarmSvc.setInterfaceError(q);
				alarmSvc.setInterfaceUsageLimit(q);
				alarmSvc.setInterfaceDuplexChange(q);
				alarmSvc.setInterfaceSpeedChange(q);
				alarmSvc.setAdcConfBackupFailure(q);
				alarmSvc.setAdcConfSyncFailure(q);
				alarmSvc.setTemperatureTooHigh(q);
				alarmSvc.setFanNotOperational(q);
				alarmSvc.setOnlyOnePowerSupply(q);
				alarmSvc.setCpuTempTooHigh(q);
				alarmSvc.setCpuFanTooSlow(q);
				alarmSvc.setCpuFanBad(q);
				alarmSvc.setChassisTempTooHigh(q);
				alarmSvc.setChassisFanBad(q);
				alarmSvc.setChassisPowerSupplyBad(q);
				alarmSvc.setVoltageTooHigh(q);
				alarmSvc.setChassisFanTooSlow(q);
				alarmSvc.setBlockDDoS(q);
			}

			// 임계치 Info
			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				alarmSvc.getAdcCpuLimit().setThreshold(alarmConfigs.getAdcCpuValue().longValue());
			} else {
				alarmSvc.getAdcMPLimit().setThreshold(alarmConfigs.getAdcMPValue().longValue());
				alarmSvc.getAdcSPLimit().setThreshold(alarmConfigs.getAdcSPValue().longValue());
				if (alarmConfigs.getFilterSessionHighValue() != null)
					if (alarmSvc.getFilterSessionLimitHigh() != null)
						alarmSvc.getFilterSessionLimitHigh()
								.setThreshold(alarmConfigs.getFilterSessionHighValue().longValue());
					else {
						if (alarmSvc.getFilterSessionLimitHigh() != null)
							alarmSvc.getFilterSessionLimitHigh().setThreshold(0L);
					}
			}

			alarmSvc.getAdcMemLimit().setThreshold(alarmConfigs.getAdcMemValue().longValue());
			alarmSvc.getConnectionLimitHigh().setThreshold(alarmConfigs.getAdcConnHighValue());
			alarmSvc.getConnectionLimitLow().setThreshold(alarmConfigs.getAdcConnLowValue());
			alarmSvc.getAdcUptime().setThreshold(alarmConfigs.getAdcUptimeValue().longValue());
			alarmSvc.getAdcPurchase().setThreshold(alarmConfigs.getAdcPurchaseValue().longValue());

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5) {
				alarmSvc.getAdcSslTransaction().setThreshold(alarmConfigs.getAdcSslTransValue());
				alarmSvc.getAdcSslcert().setThreshold(alarmConfigs.getAdcSslcertValue().longValue());
			} else {
				alarmSvc.getAdcSslTransaction().setThreshold(0L);
				alarmSvc.getAdcSslcert().setThreshold(0L);
			}

			if (alarmConfigs.adcType == OBDefine.ADC_TYPE_F5 || alarmConfigs.adcType == OBDefine.ADC_TYPE_ALTEON) {
				alarmSvc.getInterfaceError().setThreshold(alarmConfigs.getInterfaceErrorValue().longValue());
				alarmSvc.getInterfaceUsageLimit().setThreshold(alarmConfigs.getInterfaceUsageValue().longValue());
			} else {
				alarmSvc.getInterfaceError().setThreshold(0L);
				alarmSvc.getInterfaceUsageLimit().setThreshold(0L);
			}
		}
		return alarmSvc;
	}

	public AlarmActionDto getAdcDisconnectAction() {
		return adcDisconnectAction;
	}

	public void setAdcDisconnectAction(AlarmActionDto adcDisconnectAction) {
		this.adcDisconnectAction = adcDisconnectAction;
	}

	public AlarmActionDto getAdcBootAction() {
		return adcBootAction;
	}

	public void setAdcBootAction(AlarmActionDto adcBootAction) {
		this.adcBootAction = adcBootAction;
	}

	public AlarmActionDto getAdcStandbyAction() {
		return adcStandbyAction;
	}

	public void setAdcStandbyAction(AlarmActionDto adcStandbyAction) {
		this.adcStandbyAction = adcStandbyAction;
	}

	public AlarmActionDto getAdcActiveAction() {
		return adcActiveAction;
	}

	public void setAdcActiveAction(AlarmActionDto adcActiveAction) {
		this.adcActiveAction = adcActiveAction;
	}

	public AlarmActionDto getVirtualServerDownAction() {
		return virtualServerDownAction;
	}

	public void setVirtualServerDownAction(AlarmActionDto virtualServerDownAction) {
		this.virtualServerDownAction = virtualServerDownAction;
	}

	public AlarmActionDto getPoolMemberDownAction() {
		return poolMemberDownAction;
	}

	public void setPoolMemberDownAction(AlarmActionDto poolMemberDownAction) {
		this.poolMemberDownAction = poolMemberDownAction;
	}

	public AlarmActionDto getLinkDownAction() {
		return linkDownAction;
	}

	public void setLinkDownAction(AlarmActionDto linkDownAction) {
		this.linkDownAction = linkDownAction;
	}

	public AlarmActionDto getAdcCpuAction() {
		return adcCpuAction;
	}

	public void setAdcCpuAction(AlarmActionDto adcCpuAction) {
		this.adcCpuAction = adcCpuAction;
	}

	public Integer getAdcCpuValue() {
		return adcCpuValue;
	}

	public void setAdcCpuValue(Integer adcCpuValue) {
		this.adcCpuValue = adcCpuValue;
	}

	public AlarmActionDto getAdcMPAction() {
		return adcMPAction;
	}

	public void setAdcMPAction(AlarmActionDto adcMPAction) {
		this.adcMPAction = adcMPAction;
	}

	public Integer getAdcMPValue() {
		return adcMPValue;
	}

	public void setAdcMPValue(Integer adcMPValue) {
		this.adcMPValue = adcMPValue;
	}

	public AlarmActionDto getAdcSPAction() {
		return adcSPAction;
	}

	public void setAdcSPAction(AlarmActionDto adcSPAction) {
		this.adcSPAction = adcSPAction;
	}

	public Integer getAdcSPValue() {
		return adcSPValue;
	}

	public void setAdcSPValue(Integer adcSPValue) {
		this.adcSPValue = adcSPValue;
	}

	public AlarmActionDto getAdcMemAction() {
		return adcMemAction;
	}

	public void setAdcMemAction(AlarmActionDto adcMemAction) {
		this.adcMemAction = adcMemAction;
	}

	public Integer getAdcMemValue() {
		return adcMemValue;
	}

	public void setAdcMemValue(Integer adcMemValue) {
		this.adcMemValue = adcMemValue;
	}

	public OBDtoADCObject getObject() {
		return object;
	}

	public void setObject(OBDtoADCObject object) {
		this.object = object;
	}

	public Integer getConfigLevel() {
		return configLevel;
	}

	public void setConfigLevel(Integer configLevel) {
		this.configLevel = configLevel;
	}

	public AlarmActionDto getConnectionLimitHighAction() {
		return connectionLimitHighAction;
	}

	public void setConnectionLimitHighAction(AlarmActionDto connectionLimitHighAction) {
		this.connectionLimitHighAction = connectionLimitHighAction;
	}

	public Long getAdcConnHighValue() {
		return adcConnHighValue;
	}

	public void setAdcConnHighValue(Long adcConnHighValue) {
		this.adcConnHighValue = adcConnHighValue;
	}

	public AlarmActionDto getConnectionLimitLowAction() {
		return connectionLimitLowAction;
	}

	public void setConnectionLimitLowAction(AlarmActionDto connectionLimitLowAction) {
		this.connectionLimitLowAction = connectionLimitLowAction;
	}

	public Long getAdcConnLowValue() {
		return adcConnLowValue;
	}

	public void setAdcConnLowValue(Long adcConnLowValue) {
		this.adcConnLowValue = adcConnLowValue;
	}

	public void setAdcType(Integer adcType) {
		this.adcType = adcType;
	}

	public AlarmActionDto getAdcSslTransAction() {
		return adcSslTransAction;
	}

	public void setAdcSslTransAction(AlarmActionDto adcSslTransAction) {
		this.adcSslTransAction = adcSslTransAction;
	}

	public Long getAdcSslTransValue() {
		return adcSslTransValue;
	}

	public void setAdcSslTransValue(Long adcSslTransValue) {
		this.adcSslTransValue = adcSslTransValue;
	}

	public AlarmActionDto getAdcUptimeAction() {
		return adcUptimeAction;
	}

	public void setAdcUptimeAction(AlarmActionDto adcUptimeAction) {
		this.adcUptimeAction = adcUptimeAction;
	}

	public Integer getAdcUptimeValue() {
		return adcUptimeValue;
	}

	public void setAdcUptimeValue(Integer adcUptimeValue) {
		this.adcUptimeValue = adcUptimeValue;
	}

	public AlarmActionDto getAdcPurchaseAction() {
		return adcPurchaseAction;
	}

	public void setAdcPurchaseAction(AlarmActionDto adcPurchaseAction) {
		this.adcPurchaseAction = adcPurchaseAction;
	}

	public Integer getAdcPurchaseValue() {
		return adcPurchaseValue;
	}

	public void setAdcPurchaseValue(Integer adcPurchaseValue) {
		this.adcPurchaseValue = adcPurchaseValue;
	}

	public AlarmActionDto getAdcSslcertAction() {
		return adcSslcertAction;
	}

	public void setAdcSslcertAction(AlarmActionDto adcSslcertAction) {
		this.adcSslcertAction = adcSslcertAction;
	}

	public Integer getAdcSslcertValue() {
		return adcSslcertValue;
	}

	public void setAdcSslcertValue(Integer adcSslcertValue) {
		this.adcSslcertValue = adcSslcertValue;
	}

	public AlarmActionDto getInterfaceErrorAction() {
		return interfaceErrorAction;
	}

	public void setInterfaceErrorAction(AlarmActionDto interfaceErrorAction) {
		this.interfaceErrorAction = interfaceErrorAction;
	}

	public Integer getInterfaceErrorValue() {
		return interfaceErrorValue;
	}

	public void setInterfaceErrorValue(Integer interfaceErrorValue) {
		this.interfaceErrorValue = interfaceErrorValue;
	}

	public AlarmActionDto getInterfaceUsageLimitAction() {
		return interfaceUsageLimitAction;
	}

	public void setInterfaceUsageLimitAction(AlarmActionDto interfaceUsageLimitAction) {
		this.interfaceUsageLimitAction = interfaceUsageLimitAction;
	}

	public Integer getInterfaceUsageValue() {
		return interfaceUsageValue;
	}

	public void setInterfaceUsageValue(Integer interfaceUsageValue) {
		this.interfaceUsageValue = interfaceUsageValue;
	}

	public AlarmActionDto getInterfaceDuplexChangeAction() {
		return interfaceDuplexChangeAction;
	}

	public void setInterfaceDuplexChangeAction(AlarmActionDto interfaceDuplexChangeAction) {
		this.interfaceDuplexChangeAction = interfaceDuplexChangeAction;
	}

	public AlarmActionDto getInterfaceSpeedChangeAction() {
		return interfaceSpeedChangeAction;
	}

	public void setInterfaceSpeedChangeAction(AlarmActionDto interfaceSpeedChangeAction) {
		this.interfaceSpeedChangeAction = interfaceSpeedChangeAction;
	}

	public AlarmActionDto getAdcConfBackupFailureAction() {
		return adcConfBackupFailureAction;
	}

	public void setAdcConfBackupFailureAction(AlarmActionDto adcConfBackupFailureAction) {
		this.adcConfBackupFailureAction = adcConfBackupFailureAction;
	}

	public AlarmActionDto getAdcConfSyncFailureAction() {
		return adcConfSyncFailureAction;
	}

	public void setAdcConfSyncFailureAction(AlarmActionDto adcConfSyncFailureAction) {
		this.adcConfSyncFailureAction = adcConfSyncFailureAction;
	}

	public AlarmActionDto getTemperatureTooHighAction() {
		return temperatureTooHighAction;
	}

	public void setTemperatureTooHighAction(AlarmActionDto temperatureTooHighAction) {
		this.temperatureTooHighAction = temperatureTooHighAction;
	}

	public AlarmActionDto getFanNotOperationalAction() {
		return fanNotOperationalAction;
	}

	public void setFanNotOperationalAction(AlarmActionDto fanNotOperationalAction) {
		this.fanNotOperationalAction = fanNotOperationalAction;
	}

	public AlarmActionDto getOnlyOnePowerSupplyAction() {
		return onlyOnePowerSupplyAction;
	}

	public void setOnlyOnePowerSupplyAction(AlarmActionDto onlyOnePowerSupplyAction) {
		this.onlyOnePowerSupplyAction = onlyOnePowerSupplyAction;
	}

	public AlarmActionDto getCpuTempTooHighAction() {
		return cpuTempTooHighAction;
	}

	public void setCpuTempTooHighAction(AlarmActionDto cpuTempTooHighAction) {
		this.cpuTempTooHighAction = cpuTempTooHighAction;
	}

	public AlarmActionDto getCpuFanTooSlowAction() {
		return cpuFanTooSlowAction;
	}

	public void setCpuFanTooSlowAction(AlarmActionDto cpuFanTooSlowAction) {
		this.cpuFanTooSlowAction = cpuFanTooSlowAction;
	}

	public AlarmActionDto getCpuFanBadAction() {
		return cpuFanBadAction;
	}

	public void setCpuFanBadAction(AlarmActionDto cpuFanBadAction) {
		this.cpuFanBadAction = cpuFanBadAction;
	}

	public AlarmActionDto getChassisTempTooHighAction() {
		return chassisTempTooHighAction;
	}

	public void setChassisTempTooHighAction(AlarmActionDto chassisTempTooHighAction) {
		this.chassisTempTooHighAction = chassisTempTooHighAction;
	}

	public AlarmActionDto getChassisFanBadAction() {
		return chassisFanBadAction;
	}

	public void setChassisFanBadAction(AlarmActionDto chassisFanBadAction) {
		this.chassisFanBadAction = chassisFanBadAction;
	}

	public AlarmActionDto getChassisPowerSupplyBadAction() {
		return chassisPowerSupplyBadAction;
	}

	public void setChassisPowerSupplyBadAction(AlarmActionDto chassisPowerSupplyBadAction) {
		this.chassisPowerSupplyBadAction = chassisPowerSupplyBadAction;
	}

	public AlarmActionDto getVoltageTooHighAction() {
		return voltageTooHighAction;
	}

	public void setVoltageTooHighAction(AlarmActionDto voltageTooHighAction) {
		this.voltageTooHighAction = voltageTooHighAction;
	}

	public AlarmActionDto getChassisFanTooSlowAction() {
		return chassisFanTooSlowAction;
	}

	public void setChassisFanTooSlowAction(AlarmActionDto chassisFanTooSlowAction) {
		this.chassisFanTooSlowAction = chassisFanTooSlowAction;
	}

	public AlarmActionDto getBlockDDoSAction() {
		return blockDDoSAction;
	}

	public void setBlockDDoSAction(AlarmActionDto blockDDoSAction) {
		this.blockDDoSAction = blockDDoSAction;
	}

	public AlarmActionDto getAdcConnectAction() {
		return adcConnectAction;
	}

	public void setAdcConnectAction(AlarmActionDto adcConnectAction) {
		this.adcConnectAction = adcConnectAction;
	}

	public AlarmActionDto getVirtualServerUpAction() {
		return virtualServerUpAction;
	}

	public void setVirtualServerUpAction(AlarmActionDto virtualServerUpAction) {
		this.virtualServerUpAction = virtualServerUpAction;
	}

	public AlarmActionDto getPoolMemberUpAction() {
		return poolMemberUpAction;
	}

	public void setPoolMemberUpAction(AlarmActionDto poolMemberUpAction) {
		this.poolMemberUpAction = poolMemberUpAction;
	}

	public AlarmActionDto getLinkUpAction() {
		return linkUpAction;
	}

	public void setLinkUpAction(AlarmActionDto linkUpAction) {
		this.linkUpAction = linkUpAction;
	}

	public AlarmActionDto getResponseTimeAction() {
		return responseTimeAction;
	}

	public void setResponseTimeAction(AlarmActionDto responseTimeAction) {
		this.responseTimeAction = responseTimeAction;
	}

	public Integer getResponseTimeValue() {
		return responseTimeValue;
	}

	public void setResponseTimeValue(Integer responseTimeValue) {
		this.responseTimeValue = responseTimeValue;
	}

	public AlarmActionDto getRedundancyCheckAction() {
		return redundancyCheckAction;
	}

	public void setRedundancyCheckAction(AlarmActionDto redundancyCheckAction) {
		this.redundancyCheckAction = redundancyCheckAction;
	}

	public AlarmActionDto getVrrpCollisionAction() {
		return vrrpCollisionAction;
	}

	public void setVrrpCollisionAction(AlarmActionDto vrrpCollisionAction) {
		this.vrrpCollisionAction = vrrpCollisionAction;
	}

	public AlarmActionDto getGatewayDownAction() {
		return gatewayDownAction;
	}

	public void setGatewayDownAction(AlarmActionDto gatewayDownAction) {
		this.gatewayDownAction = gatewayDownAction;
	}

	public Integer getAdcType() {
		return adcType;
	}

	public AlarmActionDto getFilterSessionLimitHighAction() {
		return filterSessionLimitHighAction;
	}

	public void setFilterSessionLimitHighAction(AlarmActionDto filterSessionLimitHighAction) {
		this.filterSessionLimitHighAction = filterSessionLimitHighAction;
	}

	public Long getFilterSessionHighValue() {
		return filterSessionHighValue;
	}

	public void setFilterSessionHighValue(Long filterSessionHighValue) {
		this.filterSessionHighValue = filterSessionHighValue;
	}

	@Override
	public String toString() {
		return "AlarmConfigDto [object=" + object + ", adcType=" + adcType + ", configLevel=" + configLevel
				+ ", adcDisconnectAction=" + adcDisconnectAction + ", adcBootAction=" + adcBootAction
				+ ", adcStandbyAction=" + adcStandbyAction + ", adcActiveAction=" + adcActiveAction
				+ ", virtualServerDownAction=" + virtualServerDownAction + ", poolMemberDownAction="
				+ poolMemberDownAction + ", vrrpCollisionAction=" + vrrpCollisionAction + ", gatewayDownAction="
				+ gatewayDownAction + ", linkDownAction=" + linkDownAction + ", adcCpuAction=" + adcCpuAction
				+ ", adcCpuValue=" + adcCpuValue + ", adcMPAction=" + adcMPAction + ", adcMPValue=" + adcMPValue
				+ ", adcSPAction=" + adcSPAction + ", adcSPValue=" + adcSPValue + ", adcMemAction=" + adcMemAction
				+ ", adcMemValue=" + adcMemValue + ", connectionLimitHighAction=" + connectionLimitHighAction
				+ ", adcConnHighValue=" + adcConnHighValue + ", connectionLimitLowAction=" + connectionLimitLowAction
				+ ", adcConnLowValue=" + adcConnLowValue + ", adcSslTransAction=" + adcSslTransAction
				+ ", adcSslTransValue=" + adcSslTransValue + ", filterSessionLimitHighAction="
				+ filterSessionLimitHighAction + ", filterSessionHighValue=" + filterSessionHighValue
				+ ", adcUptimeAction=" + adcUptimeAction + ", adcUptimeValue=" + adcUptimeValue + ", adcPurchaseAction="
				+ adcPurchaseAction + ", adcPurchaseValue=" + adcPurchaseValue + ", adcSslcertAction="
				+ adcSslcertAction + ", adcSslcertValue=" + adcSslcertValue + ", interfaceErrorAction="
				+ interfaceErrorAction + ", interfaceErrorValue=" + interfaceErrorValue + ", interfaceUsageLimitAction="
				+ interfaceUsageLimitAction + ", interfaceUsageValue=" + interfaceUsageValue
				+ ", interfaceDuplexChangeAction=" + interfaceDuplexChangeAction + ", interfaceSpeedChangeAction="
				+ interfaceSpeedChangeAction + ", adcConfBackupFailureAction=" + adcConfBackupFailureAction
				+ ", temperatureTooHighAction=" + temperatureTooHighAction + ", fanNotOperationalAction="
				+ fanNotOperationalAction + ", onlyOnePowerSupplyAction=" + onlyOnePowerSupplyAction
				+ ", adcConfSyncFailureAction=" + adcConfSyncFailureAction + ", cpuTempTooHighAction="
				+ cpuTempTooHighAction + ", cpuFanTooSlowAction=" + cpuFanTooSlowAction + ", cpuFanBadAction="
				+ cpuFanBadAction + ", chassisTempTooHighAction=" + chassisTempTooHighAction + ", chassisFanBadAction="
				+ chassisFanBadAction + ", chassisPowerSupplyBadAction=" + chassisPowerSupplyBadAction
				+ ", voltageTooHighAction=" + voltageTooHighAction + ", chassisFanTooSlowAction="
				+ chassisFanTooSlowAction + ", blockDDoSAction=" + blockDDoSAction + ", adcConnectAction="
				+ adcConnectAction + ", virtualServerUpAction=" + virtualServerUpAction + ", poolMemberUpAction="
				+ poolMemberUpAction + ", linkUpAction=" + linkUpAction + ", responseTimeAction=" + responseTimeAction
				+ ", responseTimeValue=" + responseTimeValue + ", redundancyCheckAction=" + redundancyCheckAction + "]";
	}
}