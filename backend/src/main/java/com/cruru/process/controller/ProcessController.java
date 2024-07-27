package com.cruru.process.controller;

import com.cruru.process.controller.dto.ProcessCreateRequest;
import com.cruru.process.controller.dto.ProcessResponse;
import com.cruru.process.controller.dto.ProcessUpdateRequest;
import com.cruru.process.controller.dto.ProcessesResponse;
import com.cruru.process.service.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/processes")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @GetMapping
    public ResponseEntity<ProcessesResponse> read(@RequestParam(name = "dashboard_id") Long dashboardId) {
        ProcessesResponse processes = processService.findByDashboardId(dashboardId);
        return ResponseEntity.ok().body(processes);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestParam(name = "dashboard_id") Long dashboardId,
            @RequestBody @Valid ProcessCreateRequest request
    ) {
        processService.create(request, dashboardId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{process_id}")
    public ResponseEntity<ProcessResponse> update(
            @PathVariable(name = "process_id") Long processId,
            @RequestBody @Valid ProcessUpdateRequest request
    ) {
        ProcessResponse response = processService.update(request, processId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{process_id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "process_id") Long processId) {
        processService.delete(processId);
        return ResponseEntity.noContent().build();
    }
}
