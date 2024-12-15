package com.shop.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Member member;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<Member> voter;

}
