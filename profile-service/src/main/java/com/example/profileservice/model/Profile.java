package com.example.profileservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String additionalemail;

    @Column
    private String mobile;

    @Column
    private String address;

    @JsonIgnore
    @Column(nullable = false)
    private String username;

    public Profile() {}

    public Profile(Long id, String additionalemail, String mobile, String address, String username) {
        this.id = id;
        this.additionalemail = additionalemail;
        this.mobile = mobile;
        this.address = address;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdditionalEmail() {
        return additionalemail;
    }

    public void setAdditionalEmail(String additionalemail) {
        this.additionalemail = additionalemail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
