package com.itinfo.itcloud.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itinfo.common.GsonLocalDateTimeAdapter
import java.time.LocalDateTime

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeAdapter())
    .setPrettyPrinting()
    .disableHtmlEscaping()
    .create()