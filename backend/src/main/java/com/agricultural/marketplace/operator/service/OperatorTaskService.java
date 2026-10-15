package com.agricultural.marketplace.operator.service;

import java.util.List;

import com.agricultural.marketplace.operator.dto.CreateOperatorTaskRequest;
import com.agricultural.marketplace.operator.dto.OperatorTaskResponse;
import com.agricultural.marketplace.operator.entity.OperatorTask;
import com.agricultural.marketplace.operator.repository.OperatorTaskRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperatorTaskService {

    private final OperatorTaskRepository operatorTaskRepository;

    public OperatorTaskService(OperatorTaskRepository operatorTaskRepository) {
        this.operatorTaskRepository = operatorTaskRepository;
    }

    public OperatorTaskResponse createTask(CreateOperatorTaskRequest request) {
        OperatorTask task = new OperatorTask(
                request.taskType(),
                null,
                request.referenceType(),
                request.referenceId(),
                request.dueAt(),
                "PENDING"
        );
        OperatorTask saved = operatorTaskRepository.save(task);
        return OperatorTaskResponse.fromEntity(saved);
    }

    public OperatorTaskResponse assignTask(Long taskId, Long assigneeId) {
        OperatorTask task = operatorTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator task not found with id: " + taskId));

        task.assignTo(assigneeId);
        return OperatorTaskResponse.fromEntity(operatorTaskRepository.save(task));
    }

    public OperatorTaskResponse completeTask(Long taskId) {
        OperatorTask task = operatorTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator task not found with id: " + taskId));

        task.complete();
        return OperatorTaskResponse.fromEntity(operatorTaskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public OperatorTaskResponse getTaskById(Long id) {
        OperatorTask task = operatorTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operator task not found with id: " + id));
        return OperatorTaskResponse.fromEntity(task);
    }

    @Transactional(readOnly = true)
    public Page<OperatorTaskResponse> listTasks(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return operatorTaskRepository.findByStatus(status.toUpperCase(), pageable)
                    .map(OperatorTaskResponse::fromEntity);
        }
        return operatorTaskRepository.findAll(pageable)
                .map(OperatorTaskResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<OperatorTaskResponse> listTasksByAssignee(Long assigneeId) {
        return operatorTaskRepository.findByAssigneeId(assigneeId).stream()
                .map(OperatorTaskResponse::fromEntity)
                .toList();
    }
}

