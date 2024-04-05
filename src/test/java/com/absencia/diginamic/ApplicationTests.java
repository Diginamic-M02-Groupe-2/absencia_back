package com.absencia.diginamic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.absencia.diginamic.service.NightTaskService;

@SpringBootTest
class ApplicationTests {
	@SpyBean
	private NightTaskService nightTask;

	@Test
	void testNightTask() {
		nightTask.run();
	}
}