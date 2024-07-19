package com.cruru.applicant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.cruru.answer.domain.Answer;
import com.cruru.answer.domain.repository.AnswerRepository;
import com.cruru.applicant.controller.dto.ApplicantDetailResponse;
import com.cruru.applicant.controller.dto.ApplicantMoveRequest;
import com.cruru.applicant.controller.dto.ApplicantResponse;
import com.cruru.applicant.controller.dto.QnaResponse;
import com.cruru.applicant.domain.Applicant;
import com.cruru.applicant.domain.repository.ApplicantRepository;
import com.cruru.applicant.exception.ApplicantNotFoundException;
import com.cruru.choice.domain.repository.ChoiceRepository;
import com.cruru.dashboard.domain.Dashboard;
import com.cruru.dashboard.domain.repository.DashboardRepository;
import com.cruru.process.domain.Process;
import com.cruru.process.domain.repository.ProcessRepository;
import com.cruru.question.domain.Question;
import com.cruru.question.domain.repository.QuestionRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ApplicantServiceTest {

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @AfterEach
    void tearDown() {
        choiceRepository.deleteAllInBatch();
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();

        applicantRepository.deleteAllInBatch();
        processRepository.deleteAllInBatch();
        dashboardRepository.deleteAllInBatch();
    }

    @DisplayName("여러 건의 지원서를 요청된 프로세스로 일괄 변경한다.")
    @Test
    void updateApplicantProcess() {
        // given
        Dashboard dashboard = dashboardRepository.save(new Dashboard(1L, "모집 공고1", null));
        Process beforeProcess = new Process(0, "이전 프로세스", "프로세스 설명1", dashboard);
        Process afterProcess = new Process(1, "이후 프로세스", "프로세스 설명2", dashboard);
        List<Process> processes = List.of(beforeProcess, afterProcess);
        processRepository.saveAll(processes);
        List<Applicant> applicants = List.of(new Applicant(1L, null, null, null, beforeProcess, false),
                new Applicant(2L, null, null, null, beforeProcess, false),
                new Applicant(3L, null, null, null, beforeProcess, false),
                new Applicant(4L, null, null, null, beforeProcess, false),
                new Applicant(5L, null, null, null, beforeProcess, false));
        applicantRepository.saveAll(applicants);

        // when
        List<Long> applicantIds = List.of(1L, 2L, 3L, 4L, 5L);
        ApplicantMoveRequest moveRequest = new ApplicantMoveRequest(applicantIds);
        applicantService.updateApplicantProcess(afterProcess.getId(), moveRequest);

        // then
        List<Applicant> actualApplicants = applicantRepository.findAllById(applicantIds);
        boolean processAllMoved = actualApplicants.stream()
                .map(Applicant::getProcess)
                .allMatch(process -> process.equals(afterProcess));
        assertThat(processAllMoved).isTrue();
    }

    @DisplayName("id로 지원자를 찾는다.")
    @Test
    void findById() {
        // given
        Applicant applicant = new Applicant(1L, "명오", "myun@mail.com", "01012341234", null, false);
        applicant = applicantRepository.save(applicant);

        // when
        ApplicantResponse found = applicantService.findById(applicant.getId());

        // then
        assertThat(applicant.getId()).isEqualTo(found.id());
    }

    @DisplayName("id에 해당하는 지원자가 존재하지 않으면 Not Found 예외가 발생한다.")
    @Test
    void findById_notFound() {
        // given&when&then
        assertThatThrownBy(() -> applicantService.findById(-1L))
                .isInstanceOf(ApplicantNotFoundException.class);
    }

    @DisplayName("id로 지원자의 상세 정보를 찾는다.")
    @Test
    void findDetailById() {
        // given
        Dashboard dashboard = new Dashboard("프론트 부원 모집", null);
        dashboardRepository.save(dashboard);
        Process process = new Process(0, "서류", "서류 단계", dashboard);
        processRepository.save(process);
        Applicant applicant = new Applicant(1L, "명오", "myun@mail.com", "01012341234", process, false);
        applicantRepository.save(applicant);

        Question question = new Question("좋아하는 동물은?", 0, dashboard);
        questionRepository.save(question);
        Answer answer = new Answer("토끼", question, applicant);
        answerRepository.save(answer);

        // when
        ApplicantDetailResponse applicantDetailResponse = applicantService.findDetailById(applicant.getId());

        //then
        List<QnaResponse> qnaResponses = applicantDetailResponse.qnaResponses();
        assertAll(
                () -> assertThat(applicantDetailResponse.applicantName()).isEqualTo(applicant.getName()),
                () -> assertThat(applicantDetailResponse.dashboardName()).isEqualTo(dashboard.getName()),
                () -> assertThat(qnaResponses.get(0).question()).isEqualTo(question.getContent()),
                () -> assertThat(qnaResponses.get(0).answer()).isEqualTo(answer.getContent())
        );
    }
}
