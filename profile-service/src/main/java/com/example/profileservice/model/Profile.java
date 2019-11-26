package com.example.profileservice.model;

import com.example.profileservice.controller.UserProfileController;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Profile entity to represent profiles in our data model.
 * Each profile can have an additional email, a mobile number, and an address.
 * We can search for profiles by username in the {@link UserProfileController}.
 *
 */
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

    /**
     * The default Profile constructor.
     * */
    public Profile() {}

    /**
     * Constructor which takes id, additionalemail, mobile, address and username as arguments.
     * @param id this profile's ID
     * @param additionalemail the additional email for this profile
     * @param mobile the mobile number for this profile
     * @param address the address for this profile
     * @param username the name of the user associated with this profile
     * */
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
