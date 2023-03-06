package com.itinfo.service;

import com.itinfo.model.PermissionVo;
import com.itinfo.model.RoleVo;
import com.itinfo.model.UserVo;

import java.util.List;

public interface SystemPermissionsService {
	List<PermissionVo> retrieveSystemPermissions();

	List<RoleVo> retrieveRoles();

	void addSystemPermissions(List<UserVo> users);

	void removeSystemPermissions(List<PermissionVo> permissions);
}

