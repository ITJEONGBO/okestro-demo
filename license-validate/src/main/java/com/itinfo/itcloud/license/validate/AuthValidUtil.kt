package com.itinfo.itcloud.license.validate

import com.itinfo.common.LoggerDelegate
import org.slf4j.Logger


/**
 * [AuthValidUtil]
 * AUTH 검증 유틸
 */
class AuthValidUtil {

	companion object {
		@Volatile private var INSTANCE: AuthValidUtil? = null
		@JvmStatic fun getInstance(): AuthValidUtil = INSTANCE ?: synchronized(this) {
			INSTANCE ?: build().also { INSTANCE = it }
		}
		private fun build(): AuthValidUtil {
			return AuthValidUtil()
		}
		private val log: Logger by LoggerDelegate()
	}
}
