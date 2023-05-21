package com.skycast.repo;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

// Autowires necessary dependencies
@SpringBootTest
// Connects to the test database
@AutoConfigureMockMvc
// Uses application-test.forecast file instead of the regular one
@TestPropertySource("classpath:application-test.properties")
// Resets the database after every test, so that they don't clash
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ForecstRepoTest {



}
