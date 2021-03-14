package com.cpaglobal.employeeservice.model;

import lombok.*;


@Data
@NoArgsConstructor
@ToString
public class Employee {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    @Builder
    public Employee(int id, String name, String username, String email, Address address,
                    String phone, String website, Company company) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address =address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }
}
