package com.example.herokusample.controller;

import com.example.herokusample.domain.cafe.Cafe;
import com.example.herokusample.domain.cafe.CafeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cafes")
public class CafeController {
    @Autowired
    private CafeService cafeService;

    @GetMapping("")
    public Page<Cafe> getCafes(Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String gu) {
        return cafeService.getCafes(pageable, name, city, gu);
    }

    @PostMapping("")
    public void createCafe(@RequestBody CafeDTO cafeDTO) {
        cafeService.createCafe(cafeDTO);
    }
}
