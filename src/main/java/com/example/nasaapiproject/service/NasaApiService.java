package com.example.nasaapiproject.service;

import com.example.nasaapiproject.model.ApiResponse;
import com.example.nasaapiproject.repositories.ApiResponseRepository;
import com.example.nasaapiproject.test.Camera;
import com.example.nasaapiproject.test.NasaApiResponse;
import com.example.nasaapiproject.test.Photo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class NasaApiService {
    private static final String API_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    private static final String API_KEY = "5bqE9EJb21kA6v5IQzfW1yF0myhTT9HGt4KYaCHN";

    private final RestTemplate restTemplate;
    private final ApiResponseRepository repository;

    private final ObjectMapper objectMapper;


    public NasaApiService(RestTemplate restTemplate, ApiResponseRepository repository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public NasaApiResponse getCuriosityPhotos(int sol) {
        ApiResponse cachedResponse = repository.findBySol(sol);
        if (cachedResponse != null) {
            return mapApiResponseToNasaApiResponse(cachedResponse);
        } else {
            String url = API_URL + "?sol=" + sol + "&api_key=" + API_KEY;
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();
                try {
                    NasaApiResponse apiResponse = objectMapper.readValue(responseBody, NasaApiResponse.class);

                    saveApiResponse(sol, apiResponse);
                    return apiResponse;
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse response from NASA API", e);
                }
            } else {
                throw new RuntimeException("Failed to fetch data from NASA API");
            }
        }
    }

    @Transactional
    public void saveApiResponse(int sol, NasaApiResponse apiResponse) {
        ApiResponse responseToSave = mapNasaApiResponseToApiResponse(sol, apiResponse);
        repository.save(responseToSave);
    }

    private NasaApiResponse mapApiResponseToNasaApiResponse(ApiResponse cachedResponse) {
        NasaApiResponse nasaApiResponse = new NasaApiResponse();
        Photo photo = new Photo();
        photo.setId(cachedResponse.getPhotoId());
        photo.setSol(cachedResponse.getSol());
        Camera camera = new Camera();
        camera.setId(cachedResponse.getCameraId());
        camera.setName(cachedResponse.getCameraName());
        // Устанавливаем объект камеры фотографии
        photo.setCamera(camera);
        nasaApiResponse.setPhotos(Collections.singletonList(photo));
        return nasaApiResponse;
    }

    private ApiResponse mapNasaApiResponseToApiResponse(int sol, NasaApiResponse nasaApiResponse) {
        List<Photo> photos = nasaApiResponse.getPhotos();
        Photo photo = photos.get(0); // Получаем первый элемент списка, если он есть
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setPhotoId(photo.getId());
        apiResponse.setSol(photo.getSol());
        Camera camera = photo.getCamera();
        apiResponse.setCameraId(camera.getId());
        apiResponse.setCameraName(camera.getName());
        return apiResponse;
    }
}