package com.cruru.applicant.service;

import static com.cruru.util.fixture.ApplicantFixture.createApplicantDobby;
import static com.cruru.util.fixture.ApplicantFixture.createRejectedApplicantLurgi;
import static com.cruru.util.fixture.DashboardFixture.createBackendDashboard;
import static com.cruru.util.fixture.ProcessFixture.createFinalProcess;
import static com.cruru.util.fixture.ProcessFixture.createFirstProcess;
import static com.cruru.util.fixture.QuestionFixture.createShortAnswerQuestion;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.cruru.answer.domain.Answer;
import com.cruru.answer.domain.repository.AnswerRepository;
import com.cruru.applicant.controller.dto.ApplicantBasicResponse;
import com.cruru.applicant.controller.dto.ApplicantDetailResponse;
import com.cruru.applicant.controller.dto.ApplicantMoveRequest;
import com.cruru.applicant.controller.dto.ApplicantResponse;
import com.cruru.applicant.controller.dto.ApplicantUpdateRequest;
import com.cruru.applicant.controller.dto.QnaResponse;
import com.cruru.applicant.domain.Applicant;
import com.cruru.applicant.domain.repository.ApplicantRepository;
import com.cruru.applicant.exception.ApplicantNotFoundException;
import com.cruru.applicant.exception.badrequest.ApplicantRejectException;
import com.cruru.dashboard.domain.Dashboard;
import com.cruru.dashboard.domain.repository.DashboardRepository;
import com.cruru.process.controller.dto.ProcessSimpleResponse;
import com.cruru.process.domain.Process;
import com.cruru.process.domain.repository.ProcessRepository;
import com.cruru.question.domain.Question;
import com.cruru.question.domain.repository.QuestionRepository;
import com.cruru.util.ServiceTest;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("지원자 서비스 테스트")
class ApplicantServiceTest extends ServiceTest {

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
    private EntityManager entityManager;

    @DisplayName("여러 건의 지원서를 요청된 프로세스로 일괄 변경한다.")
    @Test
    void updateApplicantProcess() {
        // given
        Dashboard dashboard = dashboardRepository.save(createBackendDashboard());
        Process beforeProcess = processRepository.save(createFirstProcess(dashboard));
        Process afterProcess = processRepository.save(createFinalProcess(dashboard));

        List<Applicant> applicants = applicantRepository.saveAll(List.of(
                createApplicantDobby(beforeProcess),
                createApplicantDobby(beforeProcess)
        ));
        List<Long> applicantIds = applicants.stream()
                .map(Applicant::getId)
                .toList();
        ApplicantMoveRequest moveRequest = new ApplicantMoveRequest(applicantIds);

        // when
        applicantService.updateApplicantProcess(afterProcess.getId(), moveRequest);

        // then
        List<Applicant> actualApplicants = entityManager.createQuery(
                        "SELECT a FROM Applicant a JOIN FETCH a.process", Applicant.class)
                .getResultList();
        assertThat(actualApplicants).allMatch(applicant -> applicant.getProcess().equals(afterProcess));
    }

    @DisplayName("id로 지원자를 찾는다.")
    @Test
    void findById() {
        // given
        Process process = processRepository.save(createFirstProcess());
        Applicant applicant = applicantRepository.save(createApplicantDobby(process));

        // when
        ApplicantBasicResponse basicResponse = applicantService.findById(applicant.getId());
        ProcessSimpleResponse processResponse = basicResponse.processResponse();
        ApplicantResponse applicantResponse = basicResponse.applicantResponse();

        // then
        assertAll(
                () -> assertThat(process.getId()).isEqualTo(processResponse.id()),
                () -> assertThat(process.getName()).isEqualTo(processResponse.name()),
                () -> assertThat(applicant.getId()).isEqualTo(applicantResponse.id()),
                () -> assertThat(applicant.getName()).isEqualTo(applicantResponse.name()),
                () -> assertThat(applicant.getEmail()).isEqualTo(applicantResponse.email()),
                () -> assertThat(applicant.getPhone()).isEqualTo(applicantResponse.phone())
        );
    }

    @DisplayName("id에 해당하는 지원자가 존재하지 않으면 Not Found 예외가 발생한다.")
    @Test
    void findById_notFound() {
        // given
        long invalidId = -1L;

        // when&then
        assertThatThrownBy(() -> applicantService.findById(invalidId))
                .isInstanceOf(ApplicantNotFoundException.class);
    }

    @DisplayName("id로 지원자의 상세 정보를 찾는다.")
    @Test
    void findDetailById() {
        // given
        Dashboard dashboard = createBackendDashboard();
        dashboardRepository.save(dashboard);
        Process process = createFirstProcess(dashboard);
        processRepository.save(process);
        Applicant applicant = createApplicantDobby(process);
        applicantRepository.save(applicant);

        Question question = questionRepository.save(createShortAnswerQuestion(null));
        Answer answer = new Answer("토끼", question, applicant);
        answerRepository.save(answer);

        // when
        ApplicantDetailResponse applicantDetailResponse = applicantService.findDetailById(applicant.getId());

        //then
        List<QnaResponse> qnaResponses = applicantDetailResponse.qnaResponses();
        assertAll(
                () -> assertThat(qnaResponses.get(0).question()).isEqualTo(question.getContent()),
                () -> assertThat(qnaResponses.get(0).answer()).isEqualTo(answer.getContent())
        );
    }

    @DisplayName("id로 지원자를 불합격시킨다.")
    @Test
    void reject() {
        // given
        Applicant applicant = applicantRepository.save(createApplicantDobby());

        // when
        applicantService.reject(applicant.getId());

        // then
        assertThat(applicantRepository.findById(applicant.getId()).get().getIsRejected()).isTrue();
    }

    @DisplayName("이미 불합격한 지원자를 불합격시키려 하면 예외가 발생한다.")
    @Test
    void reject_alreadyRejected() {
        // given
        Applicant applicant = applicantRepository.save(createRejectedApplicantLurgi());

        // when&then
        assertThatThrownBy(() -> applicantService.reject(applicant.getId()))
                .isInstanceOf(ApplicantRejectException.class);
    }

    @DisplayName("지원자의 이름, 이메일, 전화번호 변경에 성공한다.")
    @Test
    void update() {
        // given
        String toChangeName = "도비";
        String toChangeEmail = "dev.dobby@gmail.com";
        String toChangePhone = "010111111111";
        Applicant applicant = applicantRepository.save(createApplicantDobby());
        ApplicantUpdateRequest request = new ApplicantUpdateRequest(toChangeName, toChangeEmail, toChangePhone);

        // when
        applicantService.update(request, applicant.getId());

        // then
        Optional<Applicant> changedApplicant = applicantRepository.findById(applicant.getId());
        assertAll(
                () -> assertThat(changedApplicant).isPresent(),
                () -> assertThat(changedApplicant.get().getName()).isEqualTo(toChangeName),
                () -> assertThat(changedApplicant.get().getEmail()).isEqualTo(toChangeEmail),
                () -> assertThat(changedApplicant.get().getPhone()).isEqualTo(toChangePhone),
                () -> assertThat(changedApplicant.get().getProcess()).isEqualTo(createApplicantDobby().getProcess())
        );
    }

    @DisplayName("지원자가 존재하지 않으면 예외가 발생한다.")
    @Test
    void update_applicantNotFound() {
        // given
        String toChangeName = "도비";
        String toChangeEmail = "dev.dobby@gmail.com";
        String toChangePhone = "010111111111";
        ApplicantUpdateRequest request = new ApplicantUpdateRequest(toChangeName, toChangeEmail, toChangePhone);
        long invalidId = -1L;

        // when&then
        assertThatThrownBy(() -> applicantService.update(request, invalidId))
                .isInstanceOf(ApplicantNotFoundException.class);
    }
}
