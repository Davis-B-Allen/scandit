package com.example.userservice.swagger;

import com.example.userservice.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class ExtraApiModels {

    @ApiModel(value = "UserLogin")
    public class UserLogin {

        @ApiModelProperty
        private String email;

        @ApiModelProperty
        private String password;
    }

}
