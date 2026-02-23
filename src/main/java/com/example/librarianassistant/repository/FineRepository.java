package com.example.librarianassistant.repository;

import com.example.librarianassistant.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByUserId(Long userId);

    List<Fine> findByUserIdAndStatus(Long userId, Fine.FineStatus status);
}
