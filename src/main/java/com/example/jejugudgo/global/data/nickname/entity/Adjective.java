package com.example.jejugudgo.global.data.nickname.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adjective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String adjective;
}
