package com.itinfo.itcloud.license.enc

import com.itinfo.common.LoggerDelegate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable

/**
 * [EncUtil]
 * 복호화 유틸
 */
class EncUtil {

	companion object {
		@Volatile private var INSTANCE: EncUtil? = null
		@JvmStatic fun getInstance(): EncUtil = INSTANCE ?: synchronized(this) {
			INSTANCE ?: build().also { INSTANCE = it }
		}
		private fun build(): EncUtil {
			return EncUtil()
		}
		private val log: Logger by LoggerDelegate()
	}
}