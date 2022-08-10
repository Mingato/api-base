package com.netagentciadigital.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class AppUser {

    @Id
    private String id;

    private String email;

    private String name;

    private String password;

    private String phone;

}
