package com.appsdeveloperblog.app.ws.ui.model.request;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailsRequestModel {
    /*
    * "firstName": "Hao",
    "lastName": "Qin",
    "email": "test1149@test.com",
    "password": "123",
    * */

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<AddressRequestModel> addresses;

}
