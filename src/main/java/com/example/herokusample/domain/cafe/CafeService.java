package com.example.herokusample.domain.cafe;


import com.example.herokusample.controller.CafeDTO;
import com.example.herokusample.domain.cafe.repository.CafeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CafeService {
    @Autowired
    private CafeRepository cafeRepository;

    public Page<Cafe> getCafes(Pageable pageable, String name, String city, String gu) {
        return cafeRepository.findBySearchOption(pageable, name, city, gu);
    }

    public void createCafe(CafeDTO cafeDTO) {
        Cafe cafe = Cafe.builder().name(cafeDTO.getName()).city(cafeDTO.getCity()).gu(cafeDTO.getGu()).build();
        cafeRepository.save(cafe);
    }
    
  
}
