package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * User is a dto class used for login and member registration.
 */
@Getter
@Setter
public class User {
    private String id;
    private String password;
    private String password2;
    private String name;

}
