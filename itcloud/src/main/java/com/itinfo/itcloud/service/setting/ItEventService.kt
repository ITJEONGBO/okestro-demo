package com.itinfo.itcloud.service.setting

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.computing.toEventVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.findAllEvents
import org.ovirt.engine.sdk4.types.Event
import org.springframework.stereotype.Service

interface ItEventService {
    /**
     * [ItEventService.findAll]
     * 이벤트 목록
     *
     * @return List<[EventVo]> 이벤트 목록
     */
    @Throws(Error::class)
    fun findAll(): List<EventVo>
}
@Service
class EventServiceImpl (

): BaseService(), ItEventService {

    @Throws(Error::class)
    override fun findAll(): List<EventVo> {
        log.info("findAll ...")
        val res: List<Event> =
            conn.findAllEvents(max = "1000")
                .getOrDefault(listOf())
        return res.toEventVos()
    }


    companion object {
        private val log by LoggerDelegate()
    }
}