package com.slim.acterio.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegistrationRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;


}
