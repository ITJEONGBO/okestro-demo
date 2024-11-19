package com.itinfo.itcloud.model.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "reboot-host")
data class RutilProperties(
    var id: String = "",
    var password: String = ""
)