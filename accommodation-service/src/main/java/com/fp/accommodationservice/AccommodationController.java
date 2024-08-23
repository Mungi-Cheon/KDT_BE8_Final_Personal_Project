package com.fp.accommodationservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AccommodationController {

    @GetMapping("/api/accommodations")
    public ResponseEntity<String> test() {

        return ResponseEntity.ok("accommodation");
    }
}
