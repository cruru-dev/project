package com.cruru.applicant.service;

import static com.cruru.util.fixture.ApplicantFixture.createPendingApplicantDobby;
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
import com.cruru.util.fixture.ApplicantFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
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
                createPendingApplicantDobby(beforeProcess),
                createPendingApplicantDobby(beforeProcess)
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
        Applicant applicant = applicantRepository.save(createPendingApplicantDobby(process));

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
        Applicant applicant = createPendingApplicantDobby(process);
        applicantRepository.save(applicant);

        Question question = questionRepository.save(createShortAnswerQuestion(null));
        questionRepository.save(question);
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
        Applicant applicant = applicantRepository.save(ApplicantFixture.createPendingApplicantDobby());

        // when
        applicantService.reject(applicant.getId());

        // then
        assertThat(applicantRepository.findById(applicant.getId()).get().isRejected()).isTrue();
    }

    @DisplayName("이미 불합격한 지원자를 불합격시키려 하면 예외가 발생한다.")
    @Test
    void reject_alreadyRejected() {
        // given
        Applicant targetApplicant = createPendingApplicantDobby();
        targetApplicant.reject();
        Applicant rejectedApplicant = applicantRepository.save(targetApplicant);

        // when&then
        Long applicantId = rejectedApplicant.getId();
        assertThatThrownBy(() -> applicantService.reject(applicantId))
                .isInstanceOf(ApplicantRejectException.class);
    }

    @DisplayName("프로세스 내의 모든 지원자를 조회한다.")
    @Test
    void findAllByProcess() {
        // given
        Process process = processRepository.save(createFirstProcess());
        Applicant applicant1 = applicantRepository.save(createPendingApplicantDobby(process));
        Applicant applicant2 = applicantRepository.save(createPendingApplicantDobby(process));
        Applicant applicant3 = applicantRepository.save(createPendingApplicantDobby(process));
        List<Applicant> applicants = List.of(applicant1, applicant2, applicant3);

        // when
        List<Applicant> applicantsInProcess = applicantService.findAllByProcess(process);

        // then
        assertThat(applicantsInProcess).containsExactlyElementsOf(applicants);
    }
}
