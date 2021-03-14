package com.cpaglobal.employeeservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class Address{
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    @JsonProperty("geo")
    private Geolocation geoLocation;

    @Builder
    public Address(String street, String suite, String city, String zipcode,
                   Geolocation geoLocation) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geoLocation =geoLocation;
    }
}
