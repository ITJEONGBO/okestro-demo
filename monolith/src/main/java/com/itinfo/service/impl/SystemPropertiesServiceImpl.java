package com.itinfo.service.impl;

import com.itinfo.service.SystemPropertiesService;
import com.itinfo.dao.SystemPropertiesDao;
import com.itinfo.model.SystemPropertiesVo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemPropertiesServiceImpl extends BaseService implements SystemPropertiesService {
	@Autowired private SystemPropertiesDao systemPropertiesDao;

	@Override
	public SystemPropertiesVo retrieveSystemProperties() {
		log.info("... retrieveSystemProperties");
		return systemPropertiesDao.retrieveSystemProperties();
	}

	@Override
	public Integer saveSystemProperties(SystemPropertiesVo systemProperties) {
		log.info("... saveSystemProperties");
		return systemPropertiesDao.updateSystemProperties(systemProperties);
	}

	@Override
	public Object[] retrieveProgramVersion() {
		log.info("... retrieveProgramVersion");
		Object[] result = new Object[2];
		ClassPathResource resource = new ClassPathResource("version.txt");
		try {
			Path path = Paths.get(resource.getURI());
			List<String> content = Files.readAllLines(path);
			result[0] = "콘트라베이스 v" + (content.get(0)).substring(0, (content.get(0)).length() - 2) + "";
			result[1] = content.get(1);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
		log.info("... retrieveProgramVersion ... res: {}", result);
		return result;
	}
}
