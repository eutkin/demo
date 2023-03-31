package com.example.demo.list.repository;

import com.example.demo.list.entity.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ListRepository extends JpaRepository<ListEntity, UUID> {
    @Transactional(readOnly = true)
    boolean existsByName(String name);
}