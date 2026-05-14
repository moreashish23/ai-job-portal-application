package com.portal.job.controller;

import com.portal.job.domain.UserRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String homeController(){
        return "User - Service Ready" + UserRole.ROLE_JOB_SEEKER;
    }

}
