package com.portal.job.feign;

import com.portal.job.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-portal-user-service")
public interface UserServiceClient {

    // Matches UserController: GET /api/users/{userId}
    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(@PathVariable Long userId);
}