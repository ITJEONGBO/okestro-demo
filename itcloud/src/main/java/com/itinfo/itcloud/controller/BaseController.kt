package com.itinfo.itcloud.controller

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.IdNotFoundException
import com.itinfo.itcloud.error.InvalidRequestException
import com.itinfo.itcloud.error.ItemNotFoundException
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.response.toRes
import com.itinfo.util.ovirt.error.FailureType
import com.itinfo.util.ovirt.error.ItCloudException
import io.swagger.annotations.Api

import org.ovirt.engine.sdk4.Error
import org.postgresql.util.PSQLException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags=["Root"])
class RootController: BaseController() {
	@GetMapping("/")
	fun root(): String = "forward:index.html" // root로 강제 매핑
}

@ControllerAdvice
class BaseController(

) {
	@ExceptionHandler(InvalidRequestException::class, PSQLException::class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	fun handleInvalidRequest(e: Throwable): ResponseEntity<Res<Any?>> {
		log.error("handleInvalidRequest ... e: {}", e::class.simpleName)
		return HttpStatus.BAD_REQUEST.toResponseEntity(e.localizedMessage)
		/*
		Res.builder {
			head { Head.fail(HttpStatus.BAD_REQUEST.value(), e.localizedMessage)  }
		}
		*/
	}

	@ExceptionHandler(IdNotFoundException::class, ItemNotFoundException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleNotFound(e: Throwable): ResponseEntity<Res<Any?>>  {
		log.error("handleNotFound ... e: {}", e::class.simpleName)
		return HttpStatus.NOT_FOUND.toResponseEntity(e.localizedMessage)
		/*
		return Res.builder {
			head { Head.fail(HttpStatus.NOT_FOUND.value(), e.localizedMessage)  }
		}
		*/
	}

	@ExceptionHandler(Error::class, ItCloudException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleOvirtError(e: Throwable): ResponseEntity<Res<Any?>> {
		log.error("handleOvirtError ... e: {}", e::class.simpleName)
		return when {
			e.message?.contains("${FailureType.NOT_FOUND.code}".toRegex()) == true -> HttpStatus.NOT_FOUND.toResponseEntity(e.localizedMessage)
			e.message?.contains("${FailureType.BAD_REQUEST.code}".toRegex()) == true -> HttpStatus.BAD_REQUEST.toResponseEntity(e.localizedMessage)
			else -> HttpStatus.INTERNAL_SERVER_ERROR.toResponseEntity(e.localizedMessage)
		}
	}

	companion object {
		private val log by LoggerDelegate()
	}
}

fun HttpStatus.toResponseEntity(msg: String = ""): ResponseEntity<Res<Any?>> =
	ResponseEntity(this@toResponseEntity.toRes(msg), this@toResponseEntity)