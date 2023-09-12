package dev.hub.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
class SecurityApplicationTests {

	@Test
	void testSecurityApp() {
		log.debug("==== testSecurityApp ====");
	}

}
