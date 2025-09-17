package com.example.CouponService.commons;

public enum Country {
    POLAND("Poland"),
    GERMANY("Germany"),
    UNITED_KINGDOM("United Kingdom"),
    UNITED_STATES("United States"),
    SPAIN("Spain");

    private final String country;

    Country(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

}