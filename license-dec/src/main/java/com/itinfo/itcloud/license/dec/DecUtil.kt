package com.itinfo.itcloud.license.dec

import com.itinfo.common.LoggerDelegate
import org.slf4j.Logger


/**
 * [DecUtil]
 * 복호화 유틸
 */
class DecUtil {

	companion object {
		@Volatile private var INSTANCE: DecUtil? = null
		@JvmStatic fun getInstance(): DecUtil = INSTANCE ?: synchronized(this) {
			INSTANCE ?: build().also { INSTANCE = it }
		}
		private fun build(): DecUtil {
			return DecUtil()
		}
		private val log: Logger by LoggerDelegate()
	}
}
