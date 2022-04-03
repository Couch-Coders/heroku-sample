package com.example.herokusample.domain.cafe.repository;


import com.example.herokusample.domain.cafe.Cafe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CafeRepositoryCustom {
    Page<Cafe> findBySearchOption(Pageable pageable, String name, String city, String gu);
}
