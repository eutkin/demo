package com.example.demo.list.models;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String login;
}