package com.cruru.applicant.service;

import com.cruru.applicant.controller.dto.ApplicantMoveRequest;
import com.cruru.applicant.controller.dto.ApplicantResponse;
import com.cruru.applicant.controller.dto.ApplicantUpdateRequest;
import com.cruru.applicant.domain.Applicant;
import com.cruru.applicant.domain.repository.ApplicantRepository;
import com.cruru.applicant.exception.ApplicantNotFoundException;
import com.cruru.applicant.exception.badrequest.ApplicantNoChangeException;
import com.cruru.applicant.exception.badrequest.ApplicantRejectException;
import com.cruru.process.domain.Process;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    public List<Applicant> findAllByProcess(Process process) {
        return applicantRepository.findAllByProcess(process);
    }

    @Transactional
    public void updateApplicantInformation(long applicantid, ApplicantUpdateRequest request) {
        Applicant applicant = findById(applicantid);
        if (notChangedInformation(request, applicant)) {
            throw new ApplicantNoChangeException();
        }
        applicant.updateInfo(request.name(), request.email(), request.phone());
    }

    public Applicant findById(long applicantId) {
        return applicantRepository.findById(applicantId)
                .orElseThrow(ApplicantNotFoundException::new);
    }

    private boolean notChangedInformation(ApplicantUpdateRequest request, Applicant applicant) {
        return applicant.getName().equals(request.name()) && applicant.getEmail().equals(request.email())
                && applicant.getPhone().equals(request.phone());
    }

    @Transactional
    public void moveApplicantProcess(Process process, ApplicantMoveRequest moveRequest) {
        List<Applicant> applicants = applicantRepository.findAllById(moveRequest.applicantIds());
        applicants.forEach(applicant -> applicant.updateProcess(process));
    }

    @Transactional
    public void reject(long applicantId) {
        Applicant applicant = findById(applicantId);
        validateRejectable(applicant);
        applicant.reject();
    }

    private void validateRejectable(Applicant applicant) {
        if (applicant.isRejected()) {
            throw new ApplicantRejectException();
        }
    }

    public ApplicantResponse toApplicantResponse(Applicant applicant) {
        return new ApplicantResponse(
                applicant.getId(),
                applicant.getName(),
                applicant.getEmail(),
                applicant.getPhone(),
                applicant.getCreatedDate()
        );
    }
}
