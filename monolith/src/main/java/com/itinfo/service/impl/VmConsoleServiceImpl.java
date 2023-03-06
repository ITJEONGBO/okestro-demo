package com.itinfo.service.impl;

import com.itinfo.SystemServiceHelper;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.service.VmConsoleService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.model.SystemPropertiesVo;
import com.itinfo.model.VmConsoleVo;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.GraphicsConsole;
import org.ovirt.engine.sdk4.types.GraphicsType;
import org.ovirt.engine.sdk4.types.Ticket;
import org.ovirt.engine.sdk4.types.Vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("vmConsoleServiceImpl")
@Slf4j
@NoArgsConstructor
public class VmConsoleServiceImpl implements VmConsoleService {
	@Autowired private AdminConnectionService adminConnectionService;


	@Autowired private SystemPropertiesService systemPropertiesService;

	@Override
	public VmConsoleVo getDisplayTicket(VmConsoleVo vmConsoleVo) {
		Connection connection = adminConnectionService.getConnection();
		Vm vm
				= SystemServiceHelper.getInstance().findAllVms(connection, "name=" + vmConsoleVo.getVmName()).get(0);
		List<GraphicsConsole> consoles
				= SystemServiceHelper.getInstance().findAllVmGraphicsConsolesFromVm(connection, vm.id());

		GraphicsConsole console = null;
		for (GraphicsConsole c : consoles) {
			if (vmConsoleVo.getType().equalsIgnoreCase("VNC")) {
				if (c.protocol() == GraphicsType.VNC) {
					console = c;
					break;
				}
				continue;
			}
			if (c.protocol() == GraphicsType.SPICE) {
				console = c;
				break;
			}
		}
		Ticket ticket
				= SystemServiceHelper.getInstance().findTicketFromVm(connection, vm.id(), console.id());
		vmConsoleVo.setAddress(console.address());
		vmConsoleVo.setPort(String.valueOf(console.port().intValue()));
		vmConsoleVo.setPasswd(ticket.value());
		vmConsoleVo.setTlsPort(console.tlsPort() == null ? null : String.valueOf(console.tlsPort().intValue()));
		try {
			SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
			vmConsoleVo.setHostAddress(systemProperties.getVncIp());
			vmConsoleVo.setHostPort(systemProperties.getVncPort());
		} catch (Exception exception) {}
		return vmConsoleVo;
	}
}