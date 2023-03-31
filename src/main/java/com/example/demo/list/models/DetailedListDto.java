package com.example.demo.list.models;

import lombok.Data;

import java.util.Set;

@Data
public class DetailedListDto {
    private String name;
    private Set<UserDto> users;
}
