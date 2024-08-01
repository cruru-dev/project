package com.cruru.choice.service;

import com.cruru.choice.controller.dto.ChoiceCreateRequest;
import com.cruru.choice.domain.Choice;
import com.cruru.choice.domain.repository.ChoiceRepository;
import com.cruru.choice.exception.ChoiceEmptyBadRequestException;
import com.cruru.choice.exception.ChoiceIllegalSaveException;
import com.cruru.question.domain.Question;
import com.cruru.question.domain.repository.QuestionRepository;
import com.cruru.question.exception.QuestionNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoiceService {

    private final ChoiceRepository choiceRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public List<Choice> createAll(List<ChoiceCreateRequest> requests, long questionId) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        if (!targetQuestion.hasChoice()) {
            throw new ChoiceIllegalSaveException();
        }
        if (requests.isEmpty()) {
            throw new ChoiceEmptyBadRequestException();
        }
        return choiceRepository.saveAll(toChoices(requests, targetQuestion));
    }

    private List<Choice> toChoices(List<ChoiceCreateRequest> requests, Question question) {
        return requests.stream()
                .map(request -> new Choice(request.choice(), request.orderIndex(), question))
                .toList();
    }
}
