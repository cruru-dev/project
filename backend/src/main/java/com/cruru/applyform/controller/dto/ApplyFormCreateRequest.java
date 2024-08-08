package com.cruru.applyform.controller.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record ApplyFormCreateRequest(
        @NotBlank(message = "제목은 필수 값입니다.")
        String title,

        String postingContent,

        @NotBlank(message = "시작 날짜는 필수 값입니다.")
        LocalDateTime startDate,

        @NotBlank(message = "종료 날짜는 필수 값입니다.")
        LocalDateTime endDate
) {

}
