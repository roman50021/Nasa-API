package com.example.nasaapiproject.repositories;

import com.example.nasaapiproject.model.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiResponseRepository extends JpaRepository<ApiResponse, Long> {
    ApiResponse findBySol(int sol);
}
