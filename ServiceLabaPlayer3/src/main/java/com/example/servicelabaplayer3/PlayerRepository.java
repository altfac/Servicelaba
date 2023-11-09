package com.example.servicelabaplayer3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Audio, Long> {
    List<Audio> findAll();
    Optional<Audio> findByName(String fileName);
}