package com.itinfo.service.impl

import com.itinfo.SystemServiceHelper

open class BaseService {
	val sysSrvHelper: SystemServiceHelper
		get() = SystemServiceHelper.getInstance()
}