package com.itinfo.controller;

import com.itinfo.security.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public abstract class BaseController {
	private static final long DEFAULT_TIME_SLEEP_IN_MILLI = 500L;
	private static final long DEFAULT_TIME_LONG_SLEEP_IN_MILLI = 3000L;

	public MemberService getMemberService() {
		return (MemberService) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	public void doSleep(long timeInMilli) {
		try { Thread.sleep(timeInMilli); } catch (InterruptedException e) { log.error(e.getLocalizedMessage()); }
	}
	public void doSleep() { doSleep(DEFAULT_TIME_SLEEP_IN_MILLI); }
	public void doLongSleep() { doSleep(DEFAULT_TIME_LONG_SLEEP_IN_MILLI); }
}
