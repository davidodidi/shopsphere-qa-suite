package com.shopsphere.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phone;
    private Address address;
    private String role;
    private boolean active;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String province;
        private String postalCode;
        private String country;
    }
}
