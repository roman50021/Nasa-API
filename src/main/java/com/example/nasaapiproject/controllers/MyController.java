package com.example.nasaapiproject.controllers;


import com.example.nasaapiproject.test.NasaApiResponse;
import com.example.nasaapiproject.service.NasaApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyController {
    private final NasaApiService nasaApiService;

    @GetMapping("/mars-photos")
    public ResponseEntity<?> getCuriosityPhotos(@RequestParam("sol") int sol) {
        try {
            NasaApiResponse response = nasaApiService.getCuriosityPhotos(sol);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data from NASA API");
        }
    }
}