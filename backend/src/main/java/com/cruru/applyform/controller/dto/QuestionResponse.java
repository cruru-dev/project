package com.cruru.applyform.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionResponse(
        long id,

        String type,

        @JsonProperty("label")
        String content,

        String description,

        int orderIndex,

        @JsonProperty("choices")
        List<ChoiceResponse> choiceResponses
) {

}
