package com.skycast.controller;

import com.skycast.model.ApiData;
import com.skycast.service.ApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/skycast")
public class ApiController {

    ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping
    public ApiData getCurrentData(HttpServletRequest request) throws IOException {
        return apiService.getApiAllData(request);
    }

}
