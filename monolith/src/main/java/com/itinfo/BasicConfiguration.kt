package com.itinfo

import com.itinfo.util.PropertiesHelper

import java.util.Properties

/**
 * [BasicConfiguration]
 * 기본설정
 *
 * @author chlee
 * @since 2023.08.07
 */
class BasicConfiguration {
	private var globalProp: Properties? = null
	private var databaseProp: Properties? = null
	companion object {
		private const val PROP_COMMON_FULL_PATH = "properties/common.properties"
		private const val PNAME_OKESTRO_IP_ADDRESS = "okestro.ip"
		private const val PNAME_OKESTRO_PORT_HTTP = "okestro.port-http"
		private const val PNAME_OKESTRO_PORT_HTTPS = "okestro.port-https"

		private const val PROP_DATABASE_FULL_PATH = "properties/database.properties"
		private const val PNAME_POSTGRES_DRIVER_CLASS_NAME = "postgres.driverClassName"
		private const val PNAME_POSTGRES_PORT = "postgres.port"
		private const val PNAME_POSTGRES_DATA_SOURCE_DB = "postgres.dataSource.db"
		private const val PNAME_POSTGRES_DATA_SOURCE_ID = "postgres.dataSource.jdbc.id"
		private const val PNAME_POSTGRES_DATA_SOURCE_PW = "postgres.dataSource.jdbc.pw"

		private const val PNAME_POSTGRES_DATA_SOURCE_ENGINE_DB = "postgres.dataSourceEngine.db"
		private const val PNAME_POSTGRES_DATA_SOURCE_ENGINE_ID = "postgres.dataSourceEngine.jdbc.id"
		private const val PNAME_POSTGRES_DATA_SOURCE_ENGINE_PW = "postgres.dataSourceEngine.jdbc.pw"


		private val propH: PropertiesHelper = PropertiesHelper.getInstance()
		@Volatile private var INSTANCE: BasicConfiguration? = null
		@JvmStatic fun getInstance(): BasicConfiguration = INSTANCE ?: synchronized(this) {
			INSTANCE ?: build().also { INSTANCE = it }
		}
		private fun build(): BasicConfiguration {
			val bc = BasicConfiguration()
			bc.globalProp = propH.loadProperties(PROP_COMMON_FULL_PATH)
			bc.databaseProp = propH.loadProperties(PROP_DATABASE_FULL_PATH)
			return bc
		}
	}

	val okestroIpAddress: String
		get() = globalProp?.get(PNAME_OKESTRO_IP_ADDRESS).toString()
	val okestroPortHttp: String
		get() = globalProp?.get(PNAME_OKESTRO_PORT_HTTP).toString()
	val okestroPortHttps: String
		get() = globalProp?.get(PNAME_OKESTRO_PORT_HTTPS).toString()

	val postgresDriverClassname: String
		get() = globalProp?.get(PNAME_POSTGRES_DRIVER_CLASS_NAME).toString()
	val postgresPort: Int
		get() = globalProp?.get(PNAME_POSTGRES_PORT).toString().toIntOrNull() ?: 5432
	val postgresDataSourceDb: String
		get() = globalProp?.get(PNAME_POSTGRES_DATA_SOURCE_DB).toString()
	val postgresDataSourceId: String
		get() = globalProp?.get(PNAME_POSTGRES_DATA_SOURCE_ID).toString()
	val postgresDataSourcePw: String
		get() = globalProp?.get(PNAME_POSTGRES_DATA_SOURCE_PW).toString()
	val postgresDataSourceEngineDb: String
		get() = globalProp?.get(PNAME_POSTGRES_DATA_SOURCE_ENGINE_DB).toString()
	val postgresDataSourceEngineId: String
		get() = globalProp?.get(PNAME_POSTGRES_DATA_SOURCE_ENGINE_ID).toString()
	val postgresDataSourceEnginePw: String
		get() = globalProp?.get(PNAME_POSTGRES_DATA_SOURCE_ENGINE_PW).toString()
}