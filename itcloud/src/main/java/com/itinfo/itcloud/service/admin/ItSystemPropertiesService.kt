package com.itinfo.itcloud.service.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.util.BasicConfiguration
import com.itinfo.util.model.SystemPropertiesVo
import org.springframework.stereotype.Service

interface ItSystemPropertiesService {
	/**
	 * [ItSystemPropertiesService.findOne]
	 * 시스템 설정 값 조회
	 *
	 * @return [SystemPropertiesVo] 시스템 설정 값
	 */
	fun findOne(): SystemPropertiesVo
	/**
	 * [ItSystemPropertiesService.update]
	 * 시스템 설정 값 조회
	 *
	 * @param sysprop [SystemPropertiesVo] 시스템 설정
	 *
	 * @return [Int] 저장성공여부
	 */
	fun update(sysprop: SystemPropertiesVo): SystemPropertiesVo?
}

@Service
class SystemPropertyServiceImpl: ItSystemPropertiesService {
	private val basicConf: BasicConfiguration
		get() = BasicConfiguration.getInstance()

	override fun findOne(): SystemPropertiesVo {
		log.info("searchSystemProperties ... ")
		return basicConf.systemProperties
	}

	override fun update(sysprop: SystemPropertiesVo): SystemPropertiesVo? {
		log.info("update ... ")
		// TODO:
		return null
	}

	companion object {
		private val log by LoggerDelegate()
	}
}