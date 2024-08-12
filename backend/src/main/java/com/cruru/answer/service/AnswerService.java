package com.cruru.answer.service;

import com.cruru.answer.domain.Answer;
import com.cruru.answer.domain.repository.AnswerRepository;
import com.cruru.answer.dto.AnswerResponse;
import com.cruru.applicant.domain.Applicant;
import com.cruru.applyform.controller.dto.AnswerCreateRequest;
import com.cruru.question.domain.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public void saveAnswerReplies(AnswerCreateRequest answerCreateRequest, Question question, Applicant applicant) {
        for (String reply : answerCreateRequest.replies()) {
            Answer answer = new Answer(reply, question, applicant);
            answerRepository.save(answer);
        }
    }

    public List<Answer> findAllByApplicant(Applicant applicant) {
        return answerRepository.findAllByApplicant(applicant);
    }

    public List<AnswerResponse> toAnswerResponses(List<Answer> answers) {
        return answers.stream()
                .map(this::toAnswerResponse)
                .toList();
    }

    private AnswerResponse toAnswerResponse(Answer answer) {
        Question question = answer.getQuestion();
        return new AnswerResponse(question.getSequence(), question.getContent(), answer.getContent());
    }
}
