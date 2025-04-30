package org.haruni.domain.haruni.repository;

import org.haruni.domain.haruni.entity.Haruni;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HaruniRepository extends JpaRepository<Haruni, Long> {
    Optional<Haruni> findByUserId(Long userId);
}
