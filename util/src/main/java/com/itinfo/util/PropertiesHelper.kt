package com.itinfo.util

import java.io.IOException
import java.io.InputStream
import java.security.InvalidParameterException
import java.util.Properties
import kotlin.jvm.Throws


/**
 * [PropertiesHelper]
 * 프로퍼티 설정 유틸
 * 
 * @author chlee
 * @since 2023.08.07
 */
class PropertiesHelper {
    companion object {
        @Volatile private var INSTANCE: PropertiesHelper? = null
        @JvmStatic fun getInstance(): PropertiesHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: build().also { INSTANCE = it }
        }
        fun build(): PropertiesHelper = PropertiesHelper()
    }

    @Throws(InvalidParameterException::class)
    fun loadProperties(resFileFullPath: String): Properties? {
        if (resFileFullPath.isEmpty()) throw InvalidParameterException("파라미터값 불량 ... resFileFullPath: $resFileFullPath")
        return try {
            val io: InputStream? = Thread.currentThread().contextClassLoader.getResourceAsStream(resFileFullPath)
            val config = Properties()
            config.load(io)
            config
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}