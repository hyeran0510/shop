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

    @Column(length = 200)  // 원본 파일 이름을 저장하는 필드
    private String fileName;

    @Column(length = 300)  // 파일 경로는 URL 형태이므로 길이를 더 주는 것이 좋음
    private String filePath;

    private int rating;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    // 생성자 또는 @PrePersist, @PreUpdate 어노테이션을 통해 날짜 필드 자동 설정
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now(); // 생성 시점과 수정 시점은 동일
    }

    @PreUpdate
    public void preUpdate() {
        this.modifyDate = LocalDateTime.now(); // 수정 시점만 업데이트
    }
}
