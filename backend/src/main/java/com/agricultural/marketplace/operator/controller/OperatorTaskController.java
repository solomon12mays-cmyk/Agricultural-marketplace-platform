package com.agricultural.marketplace.operator.controller;

import java.util.List;

import com.agricultural.marketplace.operator.dto.AssignTaskRequest;
import com.agricultural.marketplace.operator.dto.CreateOperatorTaskRequest;
import com.agricultural.marketplace.operator.dto.OperatorTaskResponse;
import com.agricultural.marketplace.operator.service.OperatorTaskService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operator/tasks")
public class OperatorTaskController {

    private final OperatorTaskService operatorTaskService;

    public OperatorTaskController(OperatorTaskService operatorTaskService) {
        this.operatorTaskService = operatorTaskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OperatorTaskResponse>> createTask(
            @Valid @RequestBody CreateOperatorTaskRequest request) {
        OperatorTaskResponse response = operatorTaskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<OperatorTaskResponse>> assignTask(
            @PathVariable Long id,
            @Valid @RequestBody AssignTaskRequest request) {
        OperatorTaskResponse response = operatorTaskService.assignTask(id, request.assigneeId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OperatorTaskResponse>> completeTask(@PathVariable Long id) {
        OperatorTaskResponse response = operatorTaskService.completeTask(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OperatorTaskResponse>> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(operatorTaskService.getTaskById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OperatorTaskResponse>>> listTasks(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OperatorTaskResponse> page = operatorTaskService.listTasks(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<ApiResponse<List<OperatorTaskResponse>>> listByAssignee(@PathVariable Long assigneeId) {
        return ResponseEntity.ok(ApiResponse.success(operatorTaskService.listTasksByAssignee(assigneeId)));
    }
}
