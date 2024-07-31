package com.cruru.dashboard.controller.dto;

import com.cruru.question.controller.dto.QuestionCreateRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record DashboardCreateRequest(
        @NotBlank(message = "공고 제목은 필수 값입니다.")
        String title,

        @JsonProperty("posting_content")
        String postingContent,

        List<QuestionCreateRequest> questions,

        @NotBlank(message = "시작 날짜는 필수 값입니다.")
        LocalDateTime startDate,

        @NotBlank(message = "종료 날짜는 필수 값입니다.")
        LocalDateTime dueDate
) {

}
