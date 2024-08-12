package com.cruru.applicant.controller.dto;

import com.cruru.answer.dto.AnswerResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ApplicantAnswerResponses(
        @JsonProperty("details")
        List<AnswerResponse> answerResponses
) {

}
