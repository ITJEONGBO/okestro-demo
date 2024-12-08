package com.itinfo.itcloud.model.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class RutilProperties(
    @Value("\${application.version}")
    var version: String = "",

    @Value("\${application.releaseDate}")
    var releaseDate: String = "",

    @Value("\${reboot-host.id}")
    var id: String = "",

    @Value("\${reboot-host.password}")
    var password: String = ""
)

