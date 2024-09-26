package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String items;

    private String fileName;
    private String filePath;
    private int rating;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}