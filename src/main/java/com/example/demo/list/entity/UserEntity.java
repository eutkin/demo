package com.example.demo.list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@FieldNameConstants
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserEntity {

    @Id
    private UUID id = UUID.randomUUID();

    private String login;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_list",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private List<ListEntity> lists;
}