package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.dto.response.user.UserChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/users/charts", produces = "application/json")
public class ChartController {

    @Autowired
    private UserService userService;

    @GetMapping
    @CachePut(value = "userChartCacheOne")
    @CacheEvict(value = "userChartCacheOne", allEntries = true)
    public ResponseEntity<UserChart> getUserChart() {
        UserChart userChart = userService.getUserChart();

        if (userChart != null) {
            return ResponseEntity.ok(userChart);
        }

        return ResponseEntity.notFound().build();
    }
}
