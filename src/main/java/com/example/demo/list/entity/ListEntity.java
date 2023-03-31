package com.example.demo.list.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@FieldNameConstants
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ListEntity {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_list",
            joinColumns = @JoinColumn(name = "list_id")
    )
    private List<UserEntity> users;
}