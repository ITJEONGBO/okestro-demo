package com.itinfo.util.ovirt

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.EventService
import org.ovirt.engine.sdk4.services.EventsService
import org.ovirt.engine.sdk4.types.Event

private fun Connection.srvEvents(): EventsService =
	systemService.eventsService()

fun Connection.findAllEvents(searchQuery: String = "", follow: String = ""): Result<List<Event>> = runCatching {
	if (searchQuery.isNotEmpty() && follow.isNotEmpty())
		this.srvEvents().list().search(searchQuery).follow(follow).caseSensitive(false).send().events()
	else if (searchQuery.isNotEmpty())
		this.srvEvents().list().search(searchQuery).caseSensitive(false).send().events()
	else if (follow.isNotEmpty())
		this.srvEvents().list().follow(follow).caseSensitive(false).send().events()
	else
		this.srvEvents().list().send().events()
}.onSuccess {
	Term.EVENT.logSuccess("목록조회")
}.onFailure {
	Term.EVENT.logFail("목록조회")
	throw it
}

private fun Connection.srvEvent(eventId: String): EventService =
	this.srvEvents().eventService(eventId)

fun Connection.findEvent(eventId: String): Result<Event?> = runCatching {
	this.srvEvent(eventId).get().send().event()
}.onSuccess {
	Term.EVENT.logSuccess("상세조회")
}.onFailure {
	Term.EVENT.logFail("상세조회")
	throw it
}