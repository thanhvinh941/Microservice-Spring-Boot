package com.controllers;

import com.entities.User;
import com.entities.value_objects.ResponseTemplateVO;
import com.services.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Retry(name = "basic")
    @RateLimiter(name = "basicExample")
    @PostMapping("/")
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @Retry(name = "basic")
    @RateLimiter(name = "basicExample")
    @GetMapping("/{userId}")
    @Cacheable(value = "users",key = "#userId")
    public ResponseTemplateVO getUser(@PathVariable("userId") Long userId) {
        return userService.getUserWithDepartment(userId);
    }

    @Retry(name = "basic")
    @RateLimiter(name = "basicExample")
    @GetMapping(value = "/secure")
    public String getSecure() {
        return "Secure endpoint available";
    }
}
