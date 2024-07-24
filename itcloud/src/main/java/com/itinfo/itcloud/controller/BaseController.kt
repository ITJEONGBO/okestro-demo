package com.itinfo.itcloud.controller

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.IdNotFoundException
import com.itinfo.itcloud.error.InvalidRequestException
import com.itinfo.itcloud.error.ItemNotFoundException
import com.itinfo.itcloud.model.response.Res
import com.itinfo.util.ovirt.error.FailureType
import org.ovirt.engine.sdk4.Error
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class BaseController(

) {
	@ExceptionHandler(InvalidRequestException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleInvalidRequest(e: InvalidRequestException): Res<Any> {
		log.error("handleInvalidRequest ... e: {}", e::class.simpleName)
		return Res.fail(400, e.message)
	}

	@ExceptionHandler(IdNotFoundException::class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	fun handleIdNotFound(e: IdNotFoundException): Res<Any> {
		log.error("handleIdNotFound ... e: {}", e::class.simpleName)
		return Res.fail(404, e.message)
	}

	@ExceptionHandler(ItemNotFoundException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleItemNotFound(e: ItemNotFoundException): Res<Any> {
		log.error("handleItemNotFound ... e: {}", e::class.simpleName)
		return Res.fail(404, e.message)
	}

	@ExceptionHandler(Error::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleOvirtError(e: Error): Res<Any> {
		log.error("handleOvirtError ... e: {}", e::class.simpleName)
		return when {
			e.message?.contains(FailureType.NOT_FOUND.message.toRegex()) == true -> Res.fail(404, e.message)
			else -> Res.fail(400, e.message)
		}
	}

	companion object {
		private val log by LoggerDelegate()
	}
}