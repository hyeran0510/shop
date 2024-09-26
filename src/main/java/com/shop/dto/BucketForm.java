package com.shop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BucketForm {

    private Long id;

    @NotEmpty(message = "제목은 필수사항입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수사항입니다.")
    private String items;

    @NotNull(message = "별점은 필수사항입니다.")
    private Integer rating;
}
