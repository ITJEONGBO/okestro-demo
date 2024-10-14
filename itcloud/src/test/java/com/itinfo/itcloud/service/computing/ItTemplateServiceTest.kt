package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.computing.TemplateVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.service.computing.ItClusterServiceTest.Companion
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItTemplateServiceTest]
 * [ItTemplateService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.10.06
 */
@SpringBootTest
class ItTemplateServiceTest {
	@Autowired private lateinit var service: ItTemplateService

	private lateinit var origin: String
	private lateinit var tempId: String
	private lateinit var vmId: String

	@BeforeEach
	fun setup() {
		origin = "00000000-0000-0000-0000-000000000000"
		tempId = "57d00a15-c510-466b-b3e2-02cf5450441b"
		vmId = "9181fa0b-d031-4dbd-a031-6de2e2913eb6"
	}

	/**
	 * [should_findAll]
	 * [ItTemplateService.findAll]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAll]
	 */
	@Test
	fun should_findAll(){
		log.debug("should_findAll")
		val result: List<TemplateVo> =
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
	}

	/**
	 * [should_findAll]
	 * [ItTemplateService.findOne]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findOne]
	 */
	@Test
	fun should_findOne(){
		log.debug("should_findOne")
		val result: TemplateVo? =
			service.findOne(tempId)

		assertThat(result, `is`(not(nullValue())))
		println(result)
	}

	/**
	 * [should_findAllDisksFromVm]
	 * [ItTemplateService.findAllDisksFromVm]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAllDisksFromVm]
	 */
	@Test
	fun should_findAllDisksFromVm(){
		log.debug("should_findAllDisksFromVm")
		val result: List<DiskImageVo> =
			service.findAllDisksFromVm(vmId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
	}

	/**
	 * [should_add_update_and_remove_Template]
	 * [ItTemplateService.add], [ItTemplateService.update], [ItTemplateService.remove]에 대한 단위테스트
	 * 외부공급자 생성x
	 *
	 * @see ItTemplateService.add
	 * @see ItTemplateService.update
	 * @see ItTemplateService.remove
	 **/
	@Test
	fun should_add_update_and_remove_Template() {
		log.debug("should_addTemplate ... ")
		val addTemplate: TemplateVo = TemplateVo.builder {

		}

		val addResult: TemplateVo? =
			service.add(vmId, addTemplate)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(not(nullValue())))
		assertThat(addResult?.name, `is`(addTemplate.name))
		assertThat(addResult?.description, `is`(addTemplate.description))
		assertThat(addResult?.comment, `is`(addTemplate.comment))


		log.debug("should_updateTemplate ... ")
		val updateTemplate: TemplateVo = TemplateVo.builder {
			id { addResult?.id }
			name { "testTemplate" }
		}

		val updateResult: TemplateVo? =
			service.update(updateTemplate)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(updateTemplate.id))
		assertThat(updateResult?.name, `is`(updateTemplate.name))
		assertThat(updateResult?.description, `is`(updateTemplate.description))
		assertThat(updateResult?.comment, `is`(updateTemplate.comment))

		log.debug("should_removeTemplate ... ")
		val removeResult =
			updateResult?.let { service.remove(it.id) }

		assertThat(removeResult, `is`(true))
	}


	/**
	 * [should_findAllVmsFromTemplate]
	 * [ItTemplateService.findAllVmsFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAllVmsFromTemplate]
	 */
	@Test
	fun should_findAllVmsFromTemplate(){
		log.debug("should_findAllVmsFromTemplate")
		val result: List<VmVo> =
			service.findAllVmsFromTemplate(tempId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(0))
	}

	/**
	 * [should_findAllNicsFromTemplate]
	 * [ItTemplateService.findAllNicsFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAllNicsFromTemplate]
	 */
	@Test
	fun should_findAllNicsFromTemplate(){
		log.debug("should_findAllNicsFromTemplate")
		val result: List<NicVo> =
			service.findAllNicsFromTemplate(tempId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
	}

	/**
	 * [should_addNicFromTemplate]
	 * [ItTemplateService.addNicFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.addNicFromTemplate]
	 */
	@Test
	fun should_addNicFromTemplate(){
		log.debug("should_addNicFromTemplate... ")
		val nicVo: NicVo = NicVo.builder {

		}
		val result: NicVo? =
			service.addNicFromTemplate(tempId, nicVo)

		assertThat(result, `is`(not(nullValue())))
	}

	/**
	 * [should_updateNicFromTemplate]
	 * [ItTemplateService.updateNicFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.updateNicFromTemplate]
	 */
	@Test
	fun should_updateNicFromTemplate(){
		log.debug("should_updateNicFromTemplate... ")
		val nicVo: NicVo = NicVo.builder {

		}
		val result: NicVo? =
			service.updateNicFromTemplate(tempId, nicVo)

		assertThat(result, `is`(not(nullValue())))
	}

	/**
	 * [should_removeNicFromTemplate]
	 * [ItTemplateService.removeNicFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.removeNicFromTemplate]
	 */
	@Test
	fun should_removeNicFromTemplate(){
		log.debug("should_removeNicFromTemplate... ")
		val result: Boolean =
			service.removeNicFromTemplate(tempId, nicId = "d")

		assertThat(result, `is`(not(nullValue())))
	}


	/**
	 * [should_findAllDisksFromTemplate]
	 * [ItTemplateService.findAllDisksFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAllDisksFromTemplate]
	 */
	@Test
	fun should_findAllDisksFromTemplate(){
		log.debug("should_findAllDisksFromTemplate")
		val result: List<DiskAttachmentVo> =
			service.findAllDisksFromTemplate(tempId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(6))
	}

	/**
	 * [should_findAllStorageDomainsFromTemplate]
	 * [ItTemplateService.findAllStorageDomainsFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAllStorageDomainsFromTemplate]
	 */
	@Test
	fun should_findAllStorageDomainsFromTemplate(){
		log.debug("should_findAllStorageDomainsFromTemplate")
		val result: List<StorageDomainVo> =
			service.findAllStorageDomainsFromTemplate(tempId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
	}

	/**
	 * [should_findAllEventsFromTemplate]
	 * [ItTemplateService.findAllEventsFromTemplate]에 대한 단위테스트
	 *
	 * @see [ItTemplateService.findAllEventsFromTemplate]
	 */
	@Test
	fun should_findAllEventsFromTemplate(){
		log.debug("should_findAllEventsFromTemplate")
		val result: List<EventVo> =
			service.findAllEventsFromTemplate(tempId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(4))
	}




	companion object {
		private val log by LoggerDelegate()
	}
}