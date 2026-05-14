package com.portal.job.controller;

import com.portal.job.domain.UserRole;
import com.portal.job.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse home(){
        return new ApiResponse("Job service Working" + UserRole.ROLE_EMPLOYER, true);
    }

}
