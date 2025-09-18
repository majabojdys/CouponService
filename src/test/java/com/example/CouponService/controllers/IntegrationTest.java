package com.example.CouponService.controllers;

import com.example.CouponService.repositories.CouponRepository;
import com.example.CouponService.repositories.CouponUsageRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
@ActiveProfiles("local")
public abstract class IntegrationTest {

    protected static WireMockServer wireMockServer;

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
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
        wireMockServer.resetAll();
    }

    @BeforeAll
    static void setupWireMock() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        System.setProperty("external.api.ip-checker.base-url", wireMockServer.baseUrl());
    }

    @AfterAll
    static void teardownWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    protected String getLocalhost() {
        return "http://localhost:" + randomServerPort;
    }
}
