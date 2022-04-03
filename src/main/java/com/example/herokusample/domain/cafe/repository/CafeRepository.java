package com.example.herokusample.domain.cafe.repository;


import com.example.herokusample.domain.cafe.Cafe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {
}
