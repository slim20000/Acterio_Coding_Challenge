package com.slim.acterio.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserUpdateRequest {

    private String firstName;
    private String lastName;
    private String email;

}
