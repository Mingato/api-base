package com.netagentciadigital.api.service;

import com.netagentciadigital.api.commons.exceptions.DataNotFoundException;
import com.netagentciadigital.api.model.AppUser;
import com.netagentciadigital.api.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }


    public AppUser findById(String id) {
        Optional<AppUser> product = appUserRepository.findById(id);
        if(product.isEmpty()){
            throw new DataNotFoundException("User with id '" + id + "' not found!");
        }

        return product.get();
    }
}
