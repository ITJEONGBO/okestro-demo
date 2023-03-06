package com.itinfo.service.impl;

import com.itinfo.SystemServiceHelper;
import com.itinfo.model.ModelsKt;
import com.itinfo.service.SystemPermissionsService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.PermissionVo;
import com.itinfo.model.RoleVo;
import com.itinfo.model.UserVo;

import java.util.List;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.RolesService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.Permission;
import org.ovirt.engine.sdk4.types.Role;
import org.ovirt.engine.sdk4.types.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemPermissionsServiceImpl implements SystemPermissionsService {

	@Autowired private ConnectionService connectionService;

	@Override
	public List<PermissionVo> retrieveSystemPermissions() {
		Connection connection = connectionService.getConnection();
		List<Permission> permissions
				= SystemServiceHelper.getInstance().findAllPermissions(connection);
		return ModelsKt.toPermissionVos(permissions, connection);
	}

	@Override
	public List<RoleVo> retrieveRoles() {
		Connection connection = connectionService.getConnection();
		SystemService systemService = connection.systemService();
		RolesService rolesService = systemService.rolesService();
		List<Role> roles
				= rolesService.list().send().roles();
		return ModelsKt.toRoleVos(roles);
	}

	@Override
	public void addSystemPermissions(List<UserVo> users) {
		Connection connection = connectionService.getConnection();
		users.forEach(user -> {
			if (!"".equals(user.getRoleId())) {
				User user2Add = ModelsKt.user2Add(user);
				Permission p2Add = ModelsKt.permission2Add(user);

				SystemServiceHelper.getInstance().addUser(connection, user2Add);
				SystemServiceHelper.getInstance().addPermission(connection, p2Add);
			}
		});
	}

	@Override
	public void removeSystemPermissions(List<PermissionVo> permissions) {
		Connection connection = connectionService.getConnection();
		permissions.forEach(permission ->
				SystemServiceHelper.getInstance().removePermission(connection, permission.getId())
		);
	}
}
