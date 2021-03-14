package com.cpaglobal.employeeservice.model;

import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class Company{
    private String name;
    private String catchPhrase;
    private String bs;

    @Builder
    public Company(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }
}
