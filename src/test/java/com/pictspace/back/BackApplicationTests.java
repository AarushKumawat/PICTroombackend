package com.pictspace.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class) // Activate TestConfig
public class BackApplicationTests {

	@Test
	void contextLoads() {
	}

}
