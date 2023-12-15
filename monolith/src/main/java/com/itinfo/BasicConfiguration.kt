package com.itinfo

import com.itinfo.model.SystemPropertiesVo
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
		
		private const val PNAME_OKESTRO_PORT_HTTP = "okestro.port-http"
		private const val PNAME_OKESTRO_PORT_HTTPS = "okestro.port-https"
		private const val PNAME_OKESTRO_VERSION = "okestro.version"
		private const val PNAME_OKESTRO_RELEASE_DATE = "okestro.release-date"

		private const val PROP_DATABASE_FULL_PATH = "properties/database.properties"
		private const val PNAME_POSTGRES_DRIVER_CLASS_NAME = "postgres.driverClassName"
		private const val PNAME_POSTGRES_JDBC_PROTOCOL = "postgres.jdbc.protocol"
		private const val PNAME_POSTGRES_JDBC_URL = "postgres.jdbc.url"
		private const val PNAME_POSTGRES_JDBC_PORT = "postgres.jdbc.port"
		
		private const val PNAME_POSTGRES_DATA_SOURCE_DB = "postgres.dataSource.db"
		private const val PNAME_POSTGRES_DATA_SOURCE_ID = "postgres.dataSource.jdbc.id"
		private const val PNAME_POSTGRES_DATA_SOURCE_PW = "postgres.dataSource.jdbc.pw"

		private const val PNAME_POSTGRES_DATA_SOURCE_ENGINE_DB = "postgres.dataSourceEngine.db"
		private const val PNAME_POSTGRES_DATA_SOURCE_ENGINE_ID = "postgres.dataSourceEngine.jdbc.id"
		private const val PNAME_POSTGRES_DATA_SOURCE_ENGINE_PW = "postgres.dataSourceEngine.jdbc.pw"

		private const val PNAME_SYSTEM_ADMIN_ID = "system.admin.id"
		private const val PNAME_SYSTEM_ADMIN_PW = "system.admin.pw"
		private const val PNAME_SYSTEM_OVIRT_IP = "system.ovirt.ip"
		private const val PNAME_SYSETM_OVIRT_VNC_IP = "system.ovirt.vnc.ip"
		private const val PNAME_SYSETM_OVIRT_VNC_PORT = "system.ovirt.vnc.port"
		private const val PNAME_SYSETM_OVIRT_THRESHOLD_CPU = "system.ovirt.threshold.cpu"
		private const val PNAME_SYSETM_OVIRT_THRESHOLD_MEMORY = "system.ovirt.threshold.memory"
		private const val PNAME_SYSETM_OVIRT_GRAFANA_URI = "system.ovirt.grafana.uri"
		private const val PNAME_SYSETM_DEEPLEARNING_URI = "system.deeplearning.uri"
		private const val PNAME_SYSETM_SYMPHONY_POWER_CONTROL = "system.symphony_power_control"
		private const val PNAME_SYSETM_LOGIN_LIMIT = "system.login.limit"


		

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

	val okestroPortHttp: String
		get() = globalProp?.get(PNAME_OKESTRO_PORT_HTTP).toString()
	val okestroPortHttps: String
		get() = globalProp?.get(PNAME_OKESTRO_PORT_HTTPS).toString()
	val okestroVersion: String
		get() = globalProp?.get(PNAME_OKESTRO_VERSION).toString()
	val okestroReleaseDate: String
		get() = globalProp?.get(PNAME_OKESTRO_RELEASE_DATE).toString()

	val postgresDriverClassname: String
		get() = databaseProp?.get(PNAME_POSTGRES_DRIVER_CLASS_NAME).toString()

	val postgresProtocol: String
		get() = databaseProp?.get(PNAME_POSTGRES_JDBC_PROTOCOL).toString()
	val postgresUrl: String
		get() = databaseProp?.get(PNAME_POSTGRES_JDBC_URL).toString() ?: "localhost"
	val postgresPort: Int
		get() = databaseProp?.get(PNAME_POSTGRES_JDBC_PORT).toString().toIntOrNull() ?: 5432
	val postgresDataSourceDb: String
		get() = databaseProp?.get(PNAME_POSTGRES_DATA_SOURCE_DB).toString()
	val postgresDataSourceId: String
		get() = databaseProp?.get(PNAME_POSTGRES_DATA_SOURCE_ID).toString()
	val postgresDataSourcePw: String
		get() = databaseProp?.get(PNAME_POSTGRES_DATA_SOURCE_PW).toString()
	val postgresDataSourceEngineDb: String
		get() = databaseProp?.get(PNAME_POSTGRES_DATA_SOURCE_ENGINE_DB).toString()
	val postgresDataSourceEngineId: String
		get() = databaseProp?.get(PNAME_POSTGRES_DATA_SOURCE_ENGINE_ID).toString()
	val postgresDataSourceEnginePw: String
		get() = databaseProp?.get(PNAME_POSTGRES_DATA_SOURCE_ENGINE_PW).toString()

	val systemAdminId: String
		get() = globalProp?.get(PNAME_SYSTEM_ADMIN_ID).toString()
	val systemAdminPw: String
		get() = globalProp?.get(PNAME_SYSTEM_ADMIN_PW).toString()
	val ovirtIp: String
		get() = globalProp?.get(PNAME_SYSTEM_OVIRT_IP).toString()
	val ovirtVncIp: String
		get() = globalProp?.get(PNAME_SYSETM_OVIRT_VNC_IP).toString()
	val ovirtVncPort: Int
		get() = globalProp?.get(PNAME_SYSETM_OVIRT_VNC_PORT).toString().toIntOrNull() ?: 9999
	val ovirtThresholdCpu: Int
		get() = globalProp?.get(PNAME_SYSETM_OVIRT_THRESHOLD_CPU).toString().toIntOrNull() ?: 80
	val ovirtThresholdMemory: Int
		get() = globalProp?.get(PNAME_SYSETM_OVIRT_THRESHOLD_MEMORY).toString().toIntOrNull() ?: 78
	val ovirtGrafanaUri: String
		get() = globalProp?.get(PNAME_SYSETM_OVIRT_GRAFANA_URI).toString()
	val deeplearningUri: String
		get() = globalProp?.get(PNAME_SYSETM_DEEPLEARNING_URI).toString()
	val symphonyPowerControl: Boolean
		get() = globalProp?.get(PNAME_SYSETM_SYMPHONY_POWER_CONTROL).toString().toBoolean() ?: false
	val loginLimit: Int
		get() = globalProp?.get(PNAME_SYSETM_LOGIN_LIMIT).toString().toIntOrNull() ?: 5
	val systemProperties: SystemPropertiesVo
		get() = SystemPropertiesVo.systemPropertiesVo {
			id { systemAdminId }
			password { systemAdminPw }
			ip { ovirtIp }
			vncIp { ovirtVncIp }
			vncPort { "$ovirtVncPort" }
			cpuThreshold { ovirtThresholdCpu }
			memoryThreshold { ovirtThresholdMemory }
			grafanaUri { ovirtGrafanaUri }
			deeplearningUri { deeplearningUri }
			symphonyPowerControll { symphonyPowerControl }
			loginLimit { loginLimit }
		}
}