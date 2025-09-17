package com.example.CouponService.clients;

import com.example.CouponService.dtos.DtoIpCheckerResponse;
import com.example.CouponService.exceptions.IpCheckerException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IpCheckerClient {

    private final RestClient ipCheckerRestClient;

    public IpCheckerClient(RestClient ipCheckerRestClient) {
        this.ipCheckerRestClient = ipCheckerRestClient;
    }

    public String getCountryByIp(String ip) {
        DtoIpCheckerResponse response = ipCheckerRestClient.get()
                .uri("/json/" + ip)
                .retrieve()
                .body(DtoIpCheckerResponse.class);
        if (response == null || response.country() == null) {
            throw new IpCheckerException(ip);
        }
        return response.country();
    }

}
