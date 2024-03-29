package com.absencia.diginamic;

import com.absencia.diginamic.task.NightTask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class ApplicationTests {
	@SpyBean
	private NightTask nightTask;

	@Test
	void testNightTask() {
		nightTask.run();
	}
}