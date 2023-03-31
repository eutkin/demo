package com.example.demo.list.models;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ShortListDto {
    private String name;
    private Set<UUID> users;
}