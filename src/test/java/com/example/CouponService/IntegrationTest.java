package com.example.CouponService;

import com.example.CouponService.repositories.CouponRepository;
import com.example.CouponService.repositories.CouponUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public abstract class IntegrationTest {

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @MockitoSpyBean
    protected CouponUsageRepository couponUsageRepository;

    @MockitoSpyBean
    protected CouponRepository couponRepository;

    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    protected int randomServerPort;

    @BeforeEach
    public void truncateDb() {
        couponUsageRepository.deleteAll();
        couponRepository.deleteAll();
    }

    protected String getLocalhost() {
        return "http://localhost:" + randomServerPort;
    }
}
