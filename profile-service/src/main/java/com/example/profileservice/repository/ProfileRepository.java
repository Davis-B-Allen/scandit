package com.example.profileservice.repository;

import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

    public Profile findProfileByUsername(String username);
}
