package com.itinfo.service.impl;

import com.itinfo.model.ModelsKt;
import com.itinfo.service.SystemPermissionsService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.PermissionVo;
import com.itinfo.model.RoleVo;
import com.itinfo.model.UserVo;

import java.util.List;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.Permission;
import org.ovirt.engine.sdk4.types.Role;
import org.ovirt.engine.sdk4.types.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemPermissionsServiceImpl extends BaseService implements SystemPermissionsService {

	@Autowired private ConnectionService connectionService;

	@Override
	public List<PermissionVo> retrieveSystemPermissions() {
		log.info("... retrieveSystemPermissions");
		Connection connection = connectionService.getConnection();
		List<Permission> permissions
				= getSysSrvHelper().findAllPermissions(connection);
		return ModelsKt.toPermissionVos(permissions, connection);
	}

	@Override
	public List<RoleVo> retrieveRoles() {
		log.info("... retrieveRoles");
		Connection connection = connectionService.getConnection();
		List<Role> roles
				= getSysSrvHelper().findAllRoles(connection);
		return ModelsKt.toRoleVos(roles);
	}

	@Override
	public void addSystemPermissions(List<UserVo> users) {
		log.info("... addSystemPermissions[{}]", users.size());
		Connection connection = connectionService.getConnection();
		users.forEach(user -> {
			if (!"".equals(user.getRoleId())) {
				User user2Add = ModelsKt.user2Add(user);
				Permission p2Add = ModelsKt.permission2Add(user);

				getSysSrvHelper().addUser(connection, user2Add);
				getSysSrvHelper().addPermission(connection, p2Add);
			}
		});
	}

	@Override
	public void removeSystemPermissions(List<PermissionVo> permissions) {
		log.info("... removeSystemPermissions[{}]", permissions.size());
		Connection connection = connectionService.getConnection();
		permissions.forEach(permission ->
			getSysSrvHelper().removePermission(connection, permission.getId())
		);
	}
}
