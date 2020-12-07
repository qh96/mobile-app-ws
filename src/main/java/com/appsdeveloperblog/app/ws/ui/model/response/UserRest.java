package com.appsdeveloperblog.app.ws.ui.model.response;

import lombok.Data;

import java.util.List;

@Data
public class UserRest {

    private String userId; // can use database id; malicious user
    private String firstName;
    private String lastName;
    private String email;

    private List<AddressesRest> addresses;
}
