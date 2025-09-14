package com.example.CouponService;

import com.example.CouponService.factories.ClockFactory;
import com.example.CouponService.repositories.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    @Autowired
    protected CouponRepository couponRepository;

    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    protected int randomServerPort;

    @BeforeEach
    public void truncateDb() {
        couponRepository.deleteAll();
    }

    protected String getLocalhost() {
        return "http://localhost:" + randomServerPort;
    }
}
