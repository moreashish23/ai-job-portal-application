package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> homeController(){
        ApiResponse response = new ApiResponse();
        response.setMessage("Service for managing candidate resumes, including resume builder,\n" +
                "\t\tmultiple versions, and resume parsing");

        response.setStatus(true);

        return ResponseEntity.ok(response);
    }

}
